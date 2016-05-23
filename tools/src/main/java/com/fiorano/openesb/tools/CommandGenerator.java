/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import com.fiorano.openesb.application.service.*;
import com.fiorano.openesb.microservice.launch.LaunchConstants;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandGenerator {

    private String EventProcessName;
    private String appVersion;
    private String servInstName;
    private String serviceGUID;
    private String serviceVersion;
    private String componentRepositoryPath;

    private String pathSeperator = System.getProperty("path.separator");

    private String classpathDependency;
    private String classpathResource;
    private String executionDir;
    private String executable;
    private Map favorites = null;
    ArrayList<String> cmd = new ArrayList<String>(50);
    private Properties properties = new Properties();
    private boolean isCCPEnabled = false;
    RmiLoginInfo loginInfo;

    private List<String> resourceParents = new ArrayList<String>();

    public CommandGenerator(Properties properties, RmiLoginInfo loginInfo) {
        this.loginInfo = loginInfo;
        this.properties = properties;
        componentRepositoryPath = properties.getProperty(LaunchConstants.COMPONENT_REPO_PATH);
        EventProcessName = properties.getProperty(LaunchConstants.EVENT_PROC_NAME);
        appVersion = properties.getProperty(LaunchConstants.EVENT_PROC_VERSION);
        servInstName = properties.getProperty(LaunchConstants.COMP_INSTANCE_NAME);
        serviceGUID = properties.getProperty(LaunchConstants.COMPONENT_GUID);
        serviceVersion = properties.getProperty(LaunchConstants.COMPONENT_VERSION);
        favorites = Collections.singletonMap(ScriptGenConstants.FIORANO_HOME, new File(ServiceManualLauncher.FIORANO_HOME));
    }

    public String[] generateExecCommand() throws FioranoException, FileNotFoundException {
        initScriptDetails();
        cmd.add(getJavaExec());
        cmd.add(getSysProp("user.dir",executionDir));
        cmd.add(getSysProp("java.library.path", executionDir));
        cmd.add(getSysProp("FPS_HOME",getFpsHome()));
        cmd.add(getSysProp("javax.management.builder.initial", "mx4j.server.MX4JMBeanServerBuilder"));
        cmd.add(getSysProp("java.endorsed.dirs",getEndorsedDirs()));
        cmd.add(getSysProp("LOGGER_NAME", "ALL"));
        cmd.add(getSysProp("JMX_NOTIFICATION_QUEUE",getconnFactory()+"__JMX_SERVICE_QUEUE"));
        cmd.add(getSysProp("JMX_CONNECT_QUEUE", getconnFactory() + "__JMX_CONNECT_QUEUE"));
        cmd.add(getSysProp("JMX_REQUEST_QUEUE",getconnFactory() + "__JMX_SERVICE_QUEUE"));
        cmd.add(getSysProp("COMP_REPOSITORY_DIR", componentRepositoryPath));
        cmd.add(getSysProp("JMX_CONNECTOR_SERVER_ID", getconnFactory()));
        cmd.add(getSysProp("ENABLE_JMX", "true"));
        cmd.add(getSysProp("ENABLE_CLIENT_LOGGER", "true"));
        cmd.add(getSysProp("JMX_DOMAIN_NAME",getconnFactory()));

        //Add Log Manager Details
        String handlerClass = properties.getProperty(ScriptGenConstants.HANDLER_CLASS);
        if (handlerClass != null) {
            cmd.add(getSysProp(ScriptGenConstants.HANDLER_CLASS, handlerClass));
            if (ScriptGenConstants.FILE_HANDLER_CLASS.equals(handlerClass)) {

                String fileHandlerDir = properties.getProperty(ScriptGenConstants.FILE_HANDLER_DIR);
                if (fileHandlerDir != null) {
                    cmd.add(getSysProp(ScriptGenConstants.FILE_HANDLER_DIR, fileHandlerDir));
                }

                Object fileHandlerLimit = properties.get(ScriptGenConstants.FILE_HANDLER_LIMIT);
                if (fileHandlerLimit != null) {
                    cmd.add(getSysProp(ScriptGenConstants.FILE_HANDLER_LIMIT, fileHandlerLimit.toString()));
                }

                Object fileHandlerCount = properties.get(ScriptGenConstants.FILE_HANDLER_COUNT);
                if (fileHandlerCount != null) {
                    cmd.add(getSysProp(ScriptGenConstants.FILE_HANDLER_COUNT, fileHandlerCount.toString()));
                }
            }
            if (ScriptGenConstants.FILE_HANDLER_CLASS.equals(handlerClass) || ScriptGenConstants.CONSOLE_HANDLER_CLASS.equals(handlerClass)) {
                Object includeTimestamp = properties.get(ScriptGenConstants.INCLUDE_TIMESTAMP);
                if (includeTimestamp != null) {
                    cmd.add(getSysProp(ScriptGenConstants.INCLUDE_TIMESTAMP, includeTimestamp.toString()));
                }

                Object dateFormat = properties.get(ScriptGenConstants.DATE_FORMAT);
                if (dateFormat != null) {
                    cmd.add(getSysProp(ScriptGenConstants.DATE_FORMAT, dateFormat.toString()));
                }
            }
        }

        String jvmArgs = properties.getProperty(ScriptGenConstants.RUNTIME_ARG_PREFIX + ScriptGenConstants.JVM_PARAMS);
        if ((jvmArgs != null) && (jvmArgs.trim().length() > 0)) {
            ArrayUtil.toCollection(StringUtil.getTokens(jvmArgs, " ", true), cmd);
        }

        addLogModulesToCmd();

        cmd.add(getSysProp(ScriptGenConstants.FIORANO_HOME,ServiceManualLauncher.FIORANO_HOME));
        cmd.add(ScriptGenConstants.CLASSPATH);
        cmd.add(classpathDependency + pathSeperator + addFmqClient()+getLicense() + addXMLCatalog() +addObjectHandler()+ classpathResource);
        cmd.add(executable);

        addRuntimeArgsToCmd();

        cmd.add(LaunchConstants.CCP_ENABLED);
        cmd.add(String.valueOf(isCCPEnabled));
        cmd.add(LaunchConstants.URL);
        cmd.add(properties.getProperty(LaunchConstants.URL));
        String username = properties.getProperty(LaunchConstants.USERNAME);
        if (!StringUtil.isEmpty(username)) {
            cmd.add(LaunchConstants.USERNAME);
            cmd.add(username);
        }else{
            cmd.add(LaunchConstants.USERNAME);
            cmd.add(loginInfo.user);
        }
        String passwd = properties.getProperty(LaunchConstants.PASSWORD);
        if (!StringUtil.isEmpty(passwd)) {
            cmd.add(LaunchConstants.PASSWORD);
            cmd.add(passwd);
        }else {
            cmd.add(LaunchConstants.PASSWORD);
            cmd.add(loginInfo.pwd);
        }
        cmd.add(LaunchConstants.CONN_FACTORY);
        cmd.add("ConnectionFactory");
        cmd.add(LaunchConstants.COMP_INSTANCE_NAME);
        cmd.add(servInstName);
        cmd.add(LaunchConstants.EVENT_PROC_NAME);
        cmd.add(EventProcessName);
        cmd.add(LaunchConstants.EVENT_PROC_VERSION);
        cmd.add(appVersion);
        cmd.add(LaunchConstants.COMPONENT_GUID);
        cmd.add(serviceGUID);
        cmd.add(LaunchConstants.COMPONENT_VERSION);
        cmd.add(serviceVersion);
        cmd.add(LaunchConstants.COMPONENT_REPO_PATH);
        cmd.add(componentRepositoryPath);
        return cmd.toArray(new String[cmd.size()]);
    }

    private String getRuntimeArgName(String runtimeArg) {
        return runtimeArg.replace(ScriptGenConstants.RUNTIME_ARG_PREFIX, "");
    }

    private void addRuntimeArgsToCmd() {

        Enumeration enumeration = properties.propertyNames();

        while (enumeration.hasMoreElements()) {

            String runtimeArg = (String) enumeration.nextElement();
            if (runtimeArg.startsWith(ScriptGenConstants.RUNTIME_ARG_PREFIX)) {
                String runtimeArgName = getRuntimeArgName(runtimeArg);
                if (!ScriptGenConstants.JVM_PARAMS.equals(runtimeArgName)) {
                    cmd.add(runtimeArgName);
                    cmd.add(properties.getProperty(runtimeArg));
                }
            }
        }
    }

    private String getLoggerName(String logModule) {
        logModule = logModule.replace(ScriptGenConstants.LOG_MODULE_PREFIX, "");
        if (properties.getProperty(ScriptGenConstants.LOG_MODULE_IS_UNIQUE_PREFIX + logModule) == null || Boolean.valueOf(properties.getProperty(ScriptGenConstants.LOG_MODULE_IS_UNIQUE_PREFIX + logModule))) {
            return LoggerUtil.getServiceLoggerName(logModule, EventProcessName, appVersion, serviceGUID, servInstName);
        } else {
            return logModule;
        }
    }

    private void addLogModulesToCmd() {

        Enumeration enumeration = properties.propertyNames();

        String categories = "";

        while (enumeration.hasMoreElements()) {
            String logModule = (String) enumeration.nextElement();
            if (logModule.startsWith(ScriptGenConstants.LOG_MODULE_PREFIX)) {
                String loggerName = getLoggerName(logModule);
                cmd.add(getSysProp(loggerName, properties.get(logModule).toString()));
                categories = categories + loggerName + ",";
            }
        }

        if (categories.length() > 0) {
            categories = categories.substring(0, categories.length() - 1);  //removing ',' at the end
            cmd.add(getSysProp(ScriptGenConstants.LOG_CATEGORIES, categories));
        }
    }

    private String addFmqClient()  {
        return ServiceManualLauncher.FIORANO_HOME + File.separator + "fmq" + File.separator +"lib" + File.separator + "fmq-rtl.jar"+pathSeperator;
    }

    private String addXMLCatalog() {
        return ServiceManualLauncher.FIORANO_HOME + File.separator + "xml-catalog" + pathSeperator;
    }

    private String addObjectHandler() {
        return ServiceManualLauncher.FIORANO_HOME + File.separator + "extlib" + File.separator + "ObjectHandler"+ File.separator + "ObjectHandler.jar" + pathSeperator;
    }

    private String getconnFactory() {
        return EventProcessName + LaunchConstants.JNDI_CONSTANT + appVersion.replace(".","_") +LaunchConstants.JNDI_CONSTANT + servInstName;
    }

    /**
     * This method actually generates the script by using ServiceDescriptor xml
     * file of the corresponding service.
     */
    private void initScriptDetails() throws FioranoException, FileNotFoundException {
        String serviceDescPath = generateXMLFileName(serviceGUID, serviceVersion);
        Service service = new Service();
        service.setFieldValues(new FileInputStream(serviceDescPath));

        Hashtable serviceDependencies = fetchComponentInfo(serviceGUID, serviceVersion);
        Enumeration serviceDependencyEnum = serviceDependencies.keys();

        // adding the main component dir as the first entry to its classpath
        classpathDependency = componentRepositoryPath
                + serviceGUID + File.separator + serviceVersion;
        resourceParents.clear();
        resourceParents.add(classpathDependency);

        System.out.println("Final List of Dependencies : ");
        while (serviceDependencyEnum.hasMoreElements()) {
            String dependencyGuid = (String) serviceDependencyEnum.nextElement();
            String dependencyVersion = (String) serviceDependencies.get(dependencyGuid);

            System.out.println(dependencyGuid + "  " + dependencyVersion);

            String filePath = generateXMLFileName(dependencyGuid, dependencyVersion);
            String[] filesDependency = getResourcePaths(filePath);

            if (classpathDependency == null) {
                classpathDependency = returnCumulativePath(filesDependency);
            } else {
                classpathDependency += pathSeperator + returnCumulativePath(filesDependency);
            }
        }

        updateResources(service);

        Execution execution = service.getExecution();

        isCCPEnabled = execution.isCCPSupported();  //@todo : To be replaced with the value in instance properties once it is supported

        String executionDir = execution.getWorkingDirectory();

        if (".".equals(executionDir)) {
            this.executionDir = componentRepositoryPath + serviceGUID + File.separator + serviceVersion;
        } else {
            this.executionDir = executionDir;
        }
        System.out.println("Executing Dir :: " + this.executionDir);
        executable = execution.getExecutable();
        System.out.println("Executable :: " + executable);
    }

    private void updateResources(Service service) {

        for (Object o : service.getDeployment().getResources()) {
            Resource resource = (Resource) o;
            String resourcePath = getCompleteResourcePath(resource, service.getGUID(), service.getVersion());
            if (System.getProperty("os.name").toLowerCase().indexOf("win") == -1) {
                if (classpathResource == null) {
                    classpathResource = resourcePath;
                } else {
                    classpathResource += pathSeperator + resourcePath;
                }
            } else if (classpathResource == null) {
                classpathResource = resourcePath;
            } else {
                classpathResource += pathSeperator + resourcePath;
            }
        }
    }

    /**
     * Fetches the dependencies
     *
     * @param componentGUID    guid
     * @param componentVersion version number
     * @return hashtable containing the dependencies
     */
    public Hashtable fetchComponentInfo(String componentGUID, String componentVersion) throws FioranoException, FileNotFoundException {
        Hashtable<String, String> dependencies = new Hashtable<String, String>();
        ArrayList<String> traversedComponents = new ArrayList<String>();
        fetchRecursive(componentGUID, componentVersion, dependencies, traversedComponents);
        return dependencies;
    }

    /**
     * Fetches the List of dependencies
     *
     * @param componentGUID       component GUID
     * @param componentVersion    version number of the component
     * @param dependencies    Hashtable which stores the dependencies
     * @param traversedComponents stores the keys
     */
    private void fetchRecursive(String componentGUID, String componentVersion, Hashtable<String, String> dependencies,
                                ArrayList<String> traversedComponents) throws FioranoException, FileNotFoundException {
        String key = componentGUID + "__" + componentVersion;
        String serviceDescPath = generateXMLFileName(componentGUID, componentVersion);
        if (traversedComponents.contains(key)) {
            return;
        }

        Service service = new Service();
        service.setFieldValues(new FileInputStream(serviceDescPath));

        traversedComponents.add(key);

        Iterator dependenciesIterator = getServiceDependencies(service);
        while (dependenciesIterator.hasNext()) {
            ServiceRef sd = (ServiceRef) dependenciesIterator.next();
            String serviceGUID = sd.getGUID();
            String serviceVersion = String.valueOf(sd.getVersion());
            dependencies.put(serviceGUID, serviceVersion);
            fetchRecursive(serviceGUID, serviceVersion, dependencies, traversedComponents);
        }
    }

    /**
     * Returns service dependencies for class
     *
     * @return service dependencies
     */
    public Iterator getServiceDependencies(Service service) {
        Deployment deploymentInfo = service.getDeployment();
        // Return a empty vector
        if (deploymentInfo == null) {
            return CollectionUtil.EMPTY_ITERATOR;
        }
        return deploymentInfo.getServiceRefs().iterator();
    }

    /**
     * This functions returns the path of the ServiceDescriptor.xml file.
     *
     * @param service name of the service.
     * @param version service version.
     * @return path of XML file.
     */
    private String generateXMLFileName(String service, String version) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(componentRepositoryPath);
        buffer.append(File.separator);
        buffer.append(service);
        buffer.append(File.separator);
        buffer.append(version);
        buffer.append(File.separator);
        buffer.append("ServiceDescriptor.xml");
        return buffer.toString();
    }


    private String getCompleteResourcePath(Resource resource, String guid, float serviceVersion) {
        String name = resource.getName();
        String filePath = componentRepositoryPath + File.separator + guid + File.separator + serviceVersion;
        return FileUtil.resolve(new File(filePath), name, favorites).getAbsolutePath();
    }

    private String[] getResourcePaths(String filepath) {
        List<String> list = new ArrayList<String>();
        Service service = new Service();
        try {
            service.setFieldValues(new FileInputStream(filepath));
        } catch (FioranoException e) {
            e.printStackTrace();
            return new String[0];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new String[0];
        }

        Iterator resourcesIterator = service.getDeployment().getResources().iterator();
        while (resourcesIterator.hasNext()) {
            Resource resource = (Resource) resourcesIterator.next();
            if(!resource.isRequiredForExecution())
                continue;

            String completeResourcePath =getCompleteResourcePath(resource, service.getGUID(), service.getVersion());

            if(!(completeResourcePath.endsWith(".jar") || completeResourcePath.endsWith(".zip") || completeResourcePath.endsWith(".ear") || completeResourcePath.endsWith(".war")
                    ||completeResourcePath.endsWith(".dll")||completeResourcePath.endsWith(".so")||completeResourcePath.endsWith(".sl")))
            {
                String parent = new File(completeResourcePath).getParent();
                if(!resourceParents.contains(parent))
                {
                    list.add(parent);
                    resourceParents.add(parent);
                }
            }

            else list.add(completeResourcePath);
        }

        return list.toArray(new String[0]);
    }

    /**
     * This function generates the classpath in one line, by taking the array
     * of classpath. In between every classpath variable, it places appropriate
     * delimiter.
     *
     * @param str classpath files
     * @return classpath.
     */
    private String returnCumulativePath(String[] str) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
            if (i != (str.length - 1)) {
                buffer.append(pathSeperator);
            }
        }
        return buffer.toString();
    }

    private static String getLicense() {
        return ServiceManualLauncher.FIORANO_HOME + "licenses" + System.getProperty("path.separator");
    }

    private String getFpsHome() {
        return ServiceManualLauncher.FIORANO_HOME + "esb" + File.separator + "fps";
    }

    private String getEndorsedDirs() {
        String extlib = ServiceManualLauncher.FIORANO_HOME + "extlib";
        StringBuffer buffer = new StringBuffer();

        buffer.append(extlib).append(File.separator).append("sax").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("dom").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("xerces").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("saxon").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("xalan").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("xml-commons-resolver").append(pathSeperator);
        buffer.append(extlib).append(File.separator).append("mx4j").append(pathSeperator);

        return buffer.toString();
    }

    private String getJavaExec() {
        String javaExec;
        if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) {
            javaExec = "\"" + ServiceManualLauncher.JAVA_HOME + File.separator + "bin" + File.separator + "java.exe" + "\"";
        } else {
            javaExec = ServiceManualLauncher.JAVA_HOME + File.separator + "bin" + File.separator + "java";
        }
        return javaExec;
    }

    private String getSysProp(String name, String value) {
        return "-D" + name + "=" + value;
    }

    protected  ArrayList getCMDArray() {
        return (ArrayList)cmd.clone();
    }

}

