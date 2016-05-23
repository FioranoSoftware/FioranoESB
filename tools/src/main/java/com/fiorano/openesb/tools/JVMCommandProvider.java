/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.service.*;
import com.fiorano.openesb.microservice.launch.java.JavaLaunchConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConstants;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class JVMCommandProvider extends CommandProvider<JavaLaunchConfiguration> {
    private LaunchConfiguration<JavaLaunchConfiguration> launchConfiguration;

    private static final Map favorites = Collections.singletonMap("FIORANO_HOME", new File(getFioranoHome()));

    private String m_componentRepositoryDir;
    private List<String> resourceParents = new ArrayList();
    private String customClassPath;
    private String jvmArguments;
    private Queue<String> genClassPath;
    private Queue<String> javaLibQueue;
    private Properties systemProps = new Properties();

    public List<String> generateCommand(LaunchConfiguration<JavaLaunchConfiguration> launchConfiguration) throws FioranoException {
        this.launchConfiguration = launchConfiguration;
        this.m_componentRepositoryDir = System.getProperty("COMP_REPOSITORY_PATH");
        initialize();
        List<String> command = new ArrayList();
        command.add(getLaunchCommand());
        String executionDir = getExecutionDir(launchConfiguration);
        String javaLibraryPathStr = getJavaLibraryPath(executionDir);
        command.add(prepareSystemProperty("java.library.path", javaLibraryPathStr));

        if (systemProps != null) {
            Enumeration propNames = systemProps.propertyNames();
            while (propNames.hasMoreElements()) {
                String propName = (String) propNames.nextElement();
                String propValue = systemProps.getProperty(propName);
                command.add(prepareSystemProperty(propName, propValue));
            }
        }
        if (launchConfiguration.getAdditionalConfiguration()!=null && launchConfiguration.getAdditionalConfiguration().isDebugMode())
            command.addAll(getDebugParams(launchConfiguration.getAdditionalConfiguration().getDebugPort()));

        if ((jvmArguments != null) && (jvmArguments.trim().length() > 0))
            ArrayUtil.toCollection(StringUtil.getTokens(jvmArguments, " ", true), command);

        prepareClasspath(command);
        command.add(getComponentPS(launchConfiguration.getMicroserviceId(), launchConfiguration.getMicroserviceVersion()).getExecution().getExecutable());
        List<String> commandLineParams = getCommandLineParams(launchConfiguration);
        command.addAll(commandLineParams);
       // logger.debug(command.toString());
        return command;
    }

    private void prepareClasspath(List<String> command) {
        command.add("-classpath");
        String classPath = customClassPath + File.pathSeparator +
                convertToPath(genClassPath);
        command.add(classPath);
    }

    private String getJavaLibraryPath(String executionDir) {
        String javaLibraryPathStr = null;

        if ((javaLibQueue != null) && (javaLibQueue.size() > 0))
            javaLibraryPathStr = convertToPath(javaLibQueue);

        if ((javaLibraryPathStr == null) || (javaLibraryPathStr.equalsIgnoreCase("")))
            javaLibraryPathStr = executionDir;

        String userLibraryPath = (String) systemProps.get("java.library.path");
        if (userLibraryPath != null) {
            javaLibraryPathStr = userLibraryPath + File.pathSeparator + javaLibraryPathStr;
            systemProps.remove("java.library.path");
        }
        return javaLibraryPathStr;
    }

    private static String getFioranoHome() {
        return System.getProperty("user.dir");
    }

    private void initialize() throws FioranoException {
        createSystemProps();
        List<String> list = toList(getCustomJVMParams());
        checkExtDir(list);
        checkEndorsedDirs(list);
        checkLibraryPath(list);
        checkCustomClasspath(list);
        checkJavaHome();

        // set JVM arguments
        String jvmParams = "";
        if ((list != null) && (list.size() > 0)) {
            jvmParams = toSpaceSeparatedString(list);
            jvmParams = Pattern.compile("\\$\\{user_home\\}", Pattern.CASE_INSENSITIVE).matcher(jvmParams).replaceAll(System.getProperty("user.home"));
            jvmParams = Pattern.compile("\\$\\{fiorano_home\\}", Pattern.CASE_INSENSITIVE).matcher(jvmParams).replaceAll(System.getProperty("user.dir"));
            jvmParams = Pattern.compile("\\$\\{appName\\}", Pattern.CASE_INSENSITIVE).matcher(jvmParams).replaceAll(launchConfiguration.getApplicationName());
            jvmParams = Pattern.compile("\\$\\{serviceInstanceName\\}", Pattern.CASE_INSENSITIVE).matcher(jvmParams).replaceAll(launchConfiguration.getServiceName());
            int logFileParamIndex = jvmParams.indexOf("-Xloggc:");
            if (logFileParamIndex != -1) {
                int logFileNameEndIndex = jvmParams.indexOf(" ", logFileParamIndex);
                String gcFileName = jvmParams.substring(logFileParamIndex + "-Xloggc:".length(), logFileNameEndIndex != -1 ? logFileNameEndIndex : jvmParams.length()).trim();
                File gcFile = new File(gcFileName);
                File parentFile = gcFile.getParentFile();
                if (!gcFile.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    parentFile.mkdirs();
                    try {
                        //noinspection ResultOfMethodCallIgnored
                        gcFile.createNewFile();
                    } catch (IOException e) {
                        //Ignore this exception. If file could not be created, GCC logs will be printed in component logs.
                    }
                }
            }
        }

        StringBuilder componentJVMParameters = new StringBuilder();
        componentJVMParameters.append(jvmParams);
        if (Boolean.getBoolean("isService")/* && Boolean.valueOf(TransportConfig.getInstance().getValue("WatchForControlEvents"))*/)
            componentJVMParameters.append(" -Xrs");

        jvmArguments = componentJVMParameters.toString();
        Queue<String> cPathQueue = new LinkedList();
        Queue<String> javaLibPathQueue = new LinkedList();

        updatePaths(launchConfiguration.getMicroserviceId(), launchConfiguration.getMicroserviceVersion(), cPathQueue, javaLibPathQueue);

        Enumeration<ServiceRef> dependencies = launchConfiguration.getRuntimeDependencies();
        if (dependencies != null) {
            while (dependencies.hasMoreElements()) {
                ServiceRef runtimeDependency = dependencies.nextElement();
                updatePaths(runtimeDependency.getGUID(), String.valueOf(runtimeDependency.getVersion()), cPathQueue, javaLibPathQueue);
            }
        }

        setGenClassPath(cPathQueue);
        setJavaLibQueue(javaLibPathQueue);
    }

    protected String getCustomJVMParams() {
        String customJVMParams = null;
        for (RuntimeArgument runtimeArg : launchConfiguration.getRuntimeArgs()) {
            if (runtimeArg.getName().equalsIgnoreCase("JVM_PARAMS")) {
                customJVMParams = runtimeArg.getValueAsString();
                break;
            }
        }
        return customJVMParams;
    }

    private String prepareSystemProperty(String name, String value) {
        return "-D" + name + "="  + value ;
    }

    private List<String> getDebugParams(int debugPort) {
        return Arrays.asList("-Xdebug",
                "-Xrunjdwp:transport=dt_socket,server=y,address=" + debugPort + ",suspend=n",
                "-Xnoagent");
    }

    private void updatePaths(String componentGUID,
                             String componentVersion, Queue<String> cPathQueue, Queue<String> javaLibPathQueue) throws FioranoException {
        ResourcePacket resPacket = new ResourcePacket();
        resPacket.classPathQueue = cPathQueue;
        resPacket.javaLibPathQueue = javaLibPathQueue;
        List<ComponentPacket> traversedComponents = new ArrayList();

        cPathQueue.add(getMSHome(componentGUID, componentVersion));
        javaLibPathQueue.add(getMSHome(componentGUID, componentVersion));

        resourceParents.clear();
        resourceParents.add(getMSHome(componentGUID, componentVersion));
        fillResourcePacket(componentGUID, componentVersion, resPacket, traversedComponents);
        addDefaults(cPathQueue);
    }

    private String getMSHome(String componentGUID, String componentVersion) {
        return m_componentRepositoryDir + File.separator + componentGUID + File.separator + componentVersion;
    }

    protected void addDefaults(Queue<String> cPathQueue) {
        String catalogs = getFioranoHome() + File.separator + "Schemas";
        cPathQueue.add(catalogs);
    }

    private String convertToPath(Queue<String> pathQueue) {
        String result = "";

        for (Iterator<String> iterator = pathQueue.iterator(); iterator.hasNext(); ) {
            String path = iterator.next();
            try {
                path = new File(path).getCanonicalPath();
            } catch (Exception ex) {
//              todo  this.getLogger().warning("Canonical path not found for " + path);
            }
            result = result.equalsIgnoreCase("") ? path : result + path;

            if (iterator.hasNext())
                result = result + File.pathSeparator;
        }
        return result;
    }

    private List<String> toList(String customJVMParams) {
        ArrayList<String> list = new ArrayList();
        if ((customJVMParams == null) || (customJVMParams.equalsIgnoreCase("")))
            return null;
        String[] tokens = customJVMParams.split("\\s");

        for (String token : tokens) {
            if (!StringUtil.isEmpty(token))
                list.add(token);
        }

        return list;
    }

    private String toSpaceSeparatedString(List<String> list) {
        String result = "";
        for (Object aList : list) {
            String next = String.valueOf(aList);
            if (result.equals(""))
                result = next;
            else
                result = result + " " + next;
        }
        return result;
    }

    private void checkCustomClasspath(List<String> list) {
        customClassPath = removeCustomClassPath(list);
        if (customClassPath == null)
            customClassPath = "";

    }

    protected void checkJavaHome() {
        //1st preference - user defined JAVA_HOME which is set in component properties.
        //2nd preference - JAVA_HOME which is set in server config file.
        for (RuntimeArgument runtimeArg : launchConfiguration.getRuntimeArgs()) {
            if (runtimeArg.getName().equalsIgnoreCase("JAVA_HOME")) {
                String userDefinedJavaHome = runtimeArg.getValueAsString();
                if (userDefinedJavaHome != null && userDefinedJavaHome.trim().length() != 0) {
                    systemProps.setProperty(LaunchConstants.USER_DEFINED_JAVA_HOME, userDefinedJavaHome);
                    return;
                }
                break;
            }
        }
    }

    private void checkExtDir(List<String> list) {
        String javaHome =  System.getProperty("java.home");
        String systemExtDir = javaHome + File.separator + "lib" + File.separator + "ext" + File.separator;
        if (!javaHome.endsWith("jre")) {
            systemExtDir = systemExtDir + File.pathSeparator + javaHome + File.separator + "jre" + File.separator + "lib" + File.separator + "ext" + File.separator;
        }
        systemExtDir = systemExtDir + File.pathSeparator + getFioranoHome() + File.separator + "esb" + File.separator + "lib" + File.separator + "ext";

        String javaExtDirs = popWithPrefix(list, "-Djava.ext.dirs=");

        if (javaExtDirs != null) {
            javaExtDirs = javaExtDirs + File.pathSeparator  + systemExtDir;
            systemProps.setProperty("java.ext.dirs", javaExtDirs);
        } else {
            systemProps.setProperty("java.ext.dirs", systemExtDir);
        }
    }

    private void fillResourcePacket(String componentGUID, String componentVersion, ResourcePacket resPacket,
                                    List<ComponentPacket> traversedComponents)
            throws FioranoException {
        ComponentPacket compPacket = new ComponentPacket(componentGUID, componentVersion);
        traversedComponents.add(compPacket);
        addResources(componentGUID, componentVersion, resPacket);
        addDependencies(componentGUID, componentVersion, resPacket, traversedComponents);
    }

    private String removeCustomClassPath(List<String> list) {
        if (list == null || (list.isEmpty()))
            return null;
        int index = -1;
        for (String next : list) {
            if (next.startsWith("-cp") || next.startsWith("-classpath")) {
                index = list.indexOf(next);
                break;
            }
        }

        if (index == -1)
            return null;

        // In case -cp or -classpath were the last elements user has not specified classpath or -cp, remove.

        if (list.size() >= (index + 1)) {
            String classPath = list.get(index + 1);

            list.remove(index);
            list.remove(classPath);

            return classPath;
        }
        list.remove(index);
        return null;
    }

    protected void createSystemProps() {
        systemProps.setProperty("javax.management.builder.initial", "mx4j.server.MX4JMBeanServerBuilder");
        systemProps.setProperty("java.endorsed.dirs", getEndorsedDirs());
        systemProps.setProperty("ENABLE_CLIENT_LOGGER", "true");
        systemProps.setProperty("DontSetReadOnly", "true");
        systemProps.setProperty("mx4j.log.priority", "error");
        systemProps.setProperty("COMP_REPOSITORY_DIR", System.getProperty("COMP_REPOSITORY_PATH"));
        systemProps.setProperty("FIORANO_HOME", getFioranoHome());
        systemProps.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        //todo need to assign log handlers
        List logModules = launchConfiguration.getLogModules();

        if (logModules != null) {
            String categories = "";
            Iterator modules = logModules.iterator();
            while (modules.hasNext()) {
                LogModule logModule = (LogModule) modules.next();
                String moduleName = logModule.isUniqueNameRequired() ? LoggerUtil.getServiceLoggerName(logModule.getName(), launchConfiguration.getApplicationName(), launchConfiguration.getApplicationVersion(),
                        launchConfiguration.getMicroserviceId(), launchConfiguration.getServiceName()) : logModule.getName();
                String logLevel = logModule.getTraceLevelAsString();
                systemProps.setProperty(moduleName, logLevel);
                categories = categories + moduleName;
                if (modules.hasNext()) categories = categories + ",";
            }
            if (categories.length() > 0)
                systemProps.setProperty("LOG_CATEGORIES", categories);

        }
    }

    private String getLaunchCommand() {
        boolean isDebug = false;
        if(launchConfiguration.getAdditionalConfiguration()!=null){
            isDebug = launchConfiguration.getAdditionalConfiguration().isDebugMode();
        }

        String java = Util.isWindows() ? "java.exe" : "java";
        String userJavaHome =  ServerConfig.getConfig().getJavaHome();
        String javaHome = (userJavaHome != null && userJavaHome.trim().length() != 0) ? userJavaHome : System.getProperty("java.home");
        if (isDebug) {
            int index = javaHome.lastIndexOf(File.separator);
            String str = javaHome.substring(index + 1, javaHome.length());
            if ("jre".equalsIgnoreCase(str)) {
                javaHome = javaHome.substring(0, index);
            }
        }
        String wrapper = Util.isWindows() ? "\"" : "";
        javaHome = Util.isWindows() && (javaHome.startsWith("/") || javaHome.startsWith("\\")) ? javaHome.substring(1) : javaHome;
        return wrapper + javaHome + File.separator + "bin" + File.separator + java +
                wrapper;
    }


    private void checkEndorsedDirs(List<String> list) {
        String systemEndorsedDir = systemProps.getProperty("java.endorsed.dirs");
        String userEndorsedDirs = popWithPrefix(list, "-Djava.endorsed.dirs=");

        if (userEndorsedDirs != null) {
            userEndorsedDirs = userEndorsedDirs + File.pathSeparator + systemEndorsedDir;
            systemProps.setProperty("java.endorsed.dirs", userEndorsedDirs);
        } else {
            systemProps.setProperty("java.endorsed.dirs", systemEndorsedDir);
        }
    }

    private void checkLibraryPath(List<String> list) {
        String systemLibraryPath = systemProps.getProperty("java.library.path");
        String userLibraryPath =  popWithPrefix(list, "-Djava.library.path=");

        if (userLibraryPath != null) {
            userLibraryPath = userLibraryPath + (systemLibraryPath != null ? File.pathSeparator + systemLibraryPath : "");
            systemProps.setProperty("java.library.path", userLibraryPath);
        } else if (systemLibraryPath != null) {
            systemProps.setProperty("java.library.path", systemLibraryPath);
        }
    }

    private String popWithPrefix(List<String> list, String prefix) {
        if (list == null || (list.isEmpty()))
            return null;
        int index = -1;
        for (String next : list) {
            if (next.startsWith(prefix)) {
                index = list.indexOf(next);
                break;
            }
        }
        if (index == -1)
            return null;

        String property = list.remove(index);

        return property.substring(prefix.length());
    }

    private String getEndorsedDirs() {

        return getFioranoHome() + File.separator + "esb" + File.separator + "lib" + File.separator + "endorsed" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "sax" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "dom" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "xerces" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "xalan"+
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "saxon" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "xml-commons-resolver" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "mx4j" +
                File.pathSeparator + getFioranoHome() + File.separator + "extlib" + File.separator + "ObjectHandler";
    }

    private void addDependencies(String componentGUID, String componentVersion, ResourcePacket resPacket, List<ComponentPacket> traversedComponents)
            throws FioranoException {
        Service componentPS = getComponentPS(componentGUID,
                componentVersion);

        for (ServiceRef serviceRef : componentPS.getDeployment().getServiceRefs()) {

            String dependencyGUID = serviceRef.getGUID();
            String dependencyVersion = String.valueOf(serviceRef.getVersion());
            ComponentPacket dependencyPacket = new ComponentPacket(dependencyGUID, dependencyVersion);
            if (traversedComponents.contains(dependencyPacket))
                continue;

            fillResourcePacket(dependencyGUID, dependencyVersion, resPacket, traversedComponents);
        }
    }

    private void addResources(String componentGUID,
                              String componentVersion, ResourcePacket resPacket)
            throws FioranoException {
        Service componentPS = getComponentPS(componentGUID,
                componentVersion);

        String componentBaseDir = m_componentRepositoryDir + File.separator + componentGUID + File.separator + componentVersion;

        for (Resource resource : componentPS.getDeployment().getResources()) {
            String resc = resource.getName();
            if (resource.isRequiredForExecution()) {
                String completeResc = FileUtil.resolve(new File(componentBaseDir), resc, favorites).getAbsolutePath();
                if (!(completeResc.endsWith(".jar") || completeResc.endsWith(".zip") || completeResc.endsWith(".ear")
                        || completeResc.endsWith(".war") || completeResc.endsWith(".dll") || completeResc.endsWith(".so") || completeResc.endsWith(".sl"))) {
                    addDirs(new File(completeResc).getParent(), resPacket);
                }

                if ((completeResc.endsWith(".jar") || completeResc.endsWith(".zip")
                        || completeResc.endsWith(".ear") || completeResc.endsWith(".war"))) {
                    resPacket.classPathQueue.add(completeResc);
                } else if (completeResc.endsWith(".dll") || completeResc.endsWith(".so") || completeResc.endsWith(".sl")) {
                    resPacket.javaLibPathQueue.add(new File(completeResc).getParent());
                }
            }
        }
    }

    private void addDirs(String resParent, ResourcePacket resPacket) {
        if (!resourceParents.contains(resParent)) {
            resPacket.classPathQueue.add(resParent);
            resPacket.javaLibPathQueue.add(resParent);
            resourceParents.add(resParent);
        }
    }

    public void setGenClassPath(Queue<String> genClassPath) {
        this.genClassPath = genClassPath;
    }

    public void setJavaLibQueue(Queue<String> javaLibQueue) {
        this.javaLibQueue = javaLibQueue;
    }

    private class ComponentPacket {
        String componentGUID;
        String componentVersion;

        public ComponentPacket(String componentGUID, String componentVersion) {
            this.componentGUID = componentGUID;
            this.componentVersion = componentVersion;
        }

        /**
         * @param obj object
         * @return boolean
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof ComponentPacket))
                return false;

            ComponentPacket compPacket = (ComponentPacket) obj;

            return (equals(componentGUID, compPacket.componentGUID))
                    && (equals(componentVersion, compPacket.componentVersion));
        }

        /**
         * @return String
         */
        public String toString() {
            return componentGUID + " " + componentVersion;
        }

        private boolean equals(String str1, String str2) {
            return (str1 == null) && (str2 == null) || !((str1 == null) || (str2 == null)) && str1.equalsIgnoreCase(str2);

        }

    }

    private class ResourcePacket {
        Queue<String> classPathQueue;
        Queue<String> javaLibPathQueue;
    }
}

