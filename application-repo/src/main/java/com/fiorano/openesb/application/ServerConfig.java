/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.ConfigReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ServerConfig {
    private String repositoryPath="./esb/server/repository";
    private String runtimeDataPath="./data";
    private long CCPTimeOut=5000;
    private long applicationStateRestoreWaitTime=5000;
    private String jettyUrl="http://localhost:8181";
    private String jettySSLUrl="https://localhost:8443";
    private String javaHome = System.getProperty("java.home");
    private Properties properties;

    public String getRepositoryPath() {
        File file = new File(repositoryPath);
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getRuntimeDataPath() {
        File file = new File(runtimeDataPath);
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return runtimeDataPath;
    }

    public void setRuntimeDataPath(String runtimeDataPath) {
        this.runtimeDataPath = runtimeDataPath;
    }

    public long getCCPTimeOut() {
        return CCPTimeOut;
    }

    public void setCCPTimeOut(String CCPTimeOut) {
        this.CCPTimeOut = Long.parseLong(CCPTimeOut);
    }

    public long getApplicationStateRestoreWaitTime() {
        return applicationStateRestoreWaitTime;
    }

    public void setApplicationStateRestoreWaitTime(String applicationStateRestoreWaitTime) {
        this.applicationStateRestoreWaitTime = Long.parseLong(applicationStateRestoreWaitTime);
    }

    private static ServerConfig serverConfig = new ServerConfig();

    private ServerConfig() {
        File configFile = new File(System.getProperty("user.dir") + File.separator
                + "etc" + File.separator + "com.fiorano.openesb.server.cfg");
        if (!configFile.exists()) {
            return;
        }
        try {
            ConfigReader.readConfigFromPropertiesFile(configFile, this);
            properties = new Properties();
            ConfigReader.readPropertiesFromFile(configFile, properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void saveConfig(){
        try {
            Properties properties = new Properties();
            properties.setProperty("repositoryPath", getRepositoryPath());
            properties.setProperty("runtimeDataPath", getRuntimeDataPath());
            properties.setProperty("CCPTimeOut", String.valueOf(getCCPTimeOut()));
            properties.setProperty("applicationStateRestoreWaitTime", String.valueOf(getApplicationStateRestoreWaitTime()));
            properties.setProperty("jettyUrl", getJettyUrl());
            properties.setProperty("jettySSLUrl", getJettySSLUrl());
            properties.setProperty("javaHome",getJavaHome());

            File file = new File(System.getProperty("user.dir") + File.separator
                    + "etc" + File.separator + "com.fiorano.openesb.server.cfg");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Server Config");
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ServerConfig getConfig(){
        return serverConfig;
    }

    public String getJettyUrl() {
        return jettyUrl;
    }

    public void setJettyUrl(String jettyUrl) {
        this.jettyUrl = jettyUrl;
    }

    public String getJettySSLUrl() {
        return jettySSLUrl;
    }

    public void setJettySSLUrl(String jettySSLUrl) {
        this.jettySSLUrl = jettySSLUrl;
    }


    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
