/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.applicationcontroller.MicroServiceLaunchConfiguration;
import com.fiorano.openesb.microservice.launch.AdditionalConfiguration;
import com.fiorano.openesb.microservice.launch.java.JavaLaunchConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConstants;
import com.fiorano.openesb.microservice.launch.impl.JVMCommandProvider;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.utils.StringUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Properties;

public class ServiceManualLauncher extends FioranoTask {

    public static String JAVA_HOME;
    public static String FIORANO_HOME;
    public static String COMP_REPOSITORY_PATH;
    public static String SCHEMA_REPO_PATH;
    public static boolean watchForControlEvents;
    public static String MS_JAVA_HOME;
    public static String userDefinedJavaHome;
    private boolean ntService = false;
    private boolean noninteractive = false;
    public String propfileNT;
    public static String componentFile;
    private String servInstanceName, eventProcessName;
    private float appVersion;
    private Properties prop = new Properties();
    private PropertyReader propertyReader;

    /**
     * Sets componentFile path for object
     *
     * @param componentFile path
     */
    public void setComponentFile(String componentFile) {
        ServiceManualLauncher.componentFile = componentFile;
    }

    /**
     * Sets NT service for object
     *
     * @param ntOption boolean specifying whether the scriptgen has to be launched as NTService
     */
    public void setNTService(boolean ntOption) {
        ntService = ntOption;
    }

    /**
     * Sets Manual Interaction for object
     *
     * @param manualInteration boolean specifying whether the scriptgen has to be launched as NTService
     */
    public void setNoninteractive(boolean manualInteration) {
        this.noninteractive = manualInteration;
    }

    /**
     * Sets fiorano home for object
     *
     * @param fioranoHome path of fiorano SOA installation
     */
    public void setFioranoHome(String fioranoHome) {
        FIORANO_HOME = fixPath(fioranoHome);
        System.setProperty("FIORANO_HOME", FIORANO_HOME);
    }

    /**
     * Sets components path for object
     *
     * @param componentPath components repository path
     */
    public void setComponentsPath(String componentPath) {
        COMP_REPOSITORY_PATH = fixPath(componentPath);
        System.setProperty("COMP_REPOSITORY_PATH", COMP_REPOSITORY_PATH);
    }

    /**
     * Sets schema path for object
     *
     * @param schemaRepoPath components repository path
     */
    public void setSchemaRepoPath(String schemaRepoPath) {
        SCHEMA_REPO_PATH = fixPath(schemaRepoPath);
        System.setProperty("SCHEMA_REPO_PATH", SCHEMA_REPO_PATH);
    }

    public void setWatchForControlEvents(String watchForControlEvents){
        this.watchForControlEvents = Boolean.valueOf(watchForControlEvents);
    }

    public void setMsJavaHome(String msJavaHome){
        this.MS_JAVA_HOME = msJavaHome;
    }

    public void setUserDefinedJavaHome(String userDefinedJavaHome){
        this.userDefinedJavaHome =  userDefinedJavaHome;
    }

    /**
     * Sets java home for object
     *
     * @param javaHome java installation directory
     */
    public void setJavaHome(String javaHome) {
        JAVA_HOME = javaHome;
    }

    /**
     * This method contains the execution logic.
     */
    public void executeTask() {
        propertyReader = new PropertyReader(this);
        if (StringUtil.isEmpty(COMP_REPOSITORY_PATH) ||
                COMP_REPOSITORY_PATH.equalsIgnoreCase("${COMPONENT_REP_PATH}")) {
            throw new IllegalArgumentException("Component Repository path not specified.");
        }

        loadProperties();
        servInstanceName = prop.getProperty(LaunchConstants.COMP_INSTANCE_NAME);
        String serviceVersion = prop.getProperty(LaunchConstants.COMPONENT_VERSION);
        appVersion = Float.parseFloat(prop.getProperty(LaunchConstants.EVENT_PROC_VERSION));
        eventProcessName = prop.getProperty(LaunchConstants.EVENT_PROC_NAME);
        boolean applicationLaunched;
        boolean componentLaunched;
        try {
            applicationLaunched = checkApplicationLaunched();
            componentLaunched = checkComponentLaunched();
        } catch (FioranoException e) {
            System.err.println("ERROR: Exiting");
            return;
        }
        if (!applicationLaunched) {
           /* ExceptionDisplayDialog.showException(null, new StringBuffer().append("<html>COMPONENT LAUNCH ERROR: Unable to launch \"").
                    append(servInstanceName).append("\" since the Event Process <P>\"").append(eventProcessName).
                    append("\" is not running. ").append("<html>").toString());*/
            System.err.println("ERROR: Exiting. Application not running");
            return;
        }
        if (componentLaunched) {
            /*ExceptionDisplayDialog.showException(null, new StringBuffer().append("<html>COMPONENT LAUNCH ERROR: Unable to launch \"").
                    append(servInstanceName).append("\" since <P>\"").append(servInstanceName).append("\" in \"" + eventProcessName + "\"").
                    append(" is already running. ").append("<html>").toString());*/
            System.err.println("ERROR: Exiting. Component already launched");
            return;
        }
        ServiceInstance serviceInstance = null;
        Properties serverConfig = null;
        Properties transportConfig = null;
        try {
            serviceInstance = rmiClient.getApplicationManager().getServiceInstance(eventProcessName, appVersion, servInstanceName);
            serverConfig = rmiClient.getApplicationManager().getServerConfig();
            transportConfig = rmiClient.getApplicationManager().getTransportConfig();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        AdditionalConfiguration ac = new JavaLaunchConfiguration(serviceInstance.isDebugMode(), serviceInstance.getDebugPort(), transportConfig.getProperty("providerURL"), FIORANO_HOME, COMP_REPOSITORY_PATH, SCHEMA_REPO_PATH, serverConfig.getProperty("JettySSLUrl"), serverConfig.getProperty("JettyUrl"), watchForControlEvents, MS_JAVA_HOME, userDefinedJavaHome, transportConfig.getProperty("java.naming.factory.initial"));
        LaunchConfiguration launchConfiguration = new MicroServiceLaunchConfiguration(eventProcessName, String.valueOf(appVersion), loginInfo.user, loginInfo.pwd, serviceInstance, ac);
        com.fiorano.openesb.microservice.launch.impl.JVMCommandProvider jvmCommandProvider = new JVMCommandProvider();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            ProcessBuilder command = processBuilder.command(jvmCommandProvider.generateCommand(launchConfiguration));
            File directory = new File(COMP_REPOSITORY_PATH + File.separator + serviceInstance.getGUID() + File.separator + serviceVersion);
            command.directory(directory);
            command.inheritIO();
            Process proc = command.start();
            Runtime.getRuntime().addShutdownHook(new ShutDownThread(proc, rmiClient, eventProcessName, appVersion, servInstanceName));
            createStreamRedirectors(proc);
            int proc_exit = proc.waitFor();
            System.out.println("Launching Component Status : " + proc_exit);
        } catch (FioranoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RmiClient getConnectionManager(){
        return rmiClient;
    }


    private void loadProperties() {
        if (prop.isEmpty()) {
            try {
                prop = propertyReader.read(componentFile);
            } catch (FioranoException e) {
                System.out.println("ERROR : Failed to load launch script. The properties file given is not specified or Invalid");
                System.exit(-1);
            }
        }
    }

    private boolean checkComponentLaunched() throws FioranoException {
        try {
            if(rmiClient == null) {
                System.out.println("Could not connect to enterprise server.\n Please make sure the event process is running.");
                return true;
            }
            return rmiClient.getApplicationManager().isServiceRunning(eventProcessName, appVersion, servInstanceName);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new FioranoException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new FioranoException(e);
        }
    }

    private void createStreamRedirectors(Process process) {
        new StreamRedirector(process.getInputStream(), System.out);
        new StreamRedirector(process.getErrorStream(), System.err);
    }

    private boolean checkApplicationLaunched() throws FioranoException {
        try {
            if(rmiClient == null) {
                System.out.println("Could not connect to enterprise server.\n Please make sure the event process is running.");
                return true;
            }
            return rmiClient.getApplicationManager().isRunning(eventProcessName,appVersion);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new FioranoException(e);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new FioranoException(e);
        }
    }

    /**
     * returns the computed Fiorano Home
     *
     * @param filePath
     * @return path of fiorano home
     */
    private String fixPath(String filePath) {
        File f = new File(filePath);
        try {
            filePath = f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}

class ShutDownThread extends Thread {

    private Process proc = null;
    private RmiClient rmiClient;
    private String epName;
    private float version;
    private String serviceInstanceName;

    ShutDownThread(Process proc, RmiClient rmiClient, String eventProcessName, float appVersion, String servInstanceName) {
        this.proc = proc;
        this.rmiClient = rmiClient;
        this.epName = eventProcessName;
        this.version = appVersion;
        this.serviceInstanceName = servInstanceName;
    }

    /**
     * Main processing method for the ShutDownThread object
     */
    public void run() {
        if (proc != null) {
            try {
                rmiClient.getApplicationManager().stopServiceInstance(epName, version, serviceInstanceName);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

}