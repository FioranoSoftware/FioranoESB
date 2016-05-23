/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.java;

import com.fiorano.openesb.microservice.launch.AdditionalConfiguration;

public class JavaLaunchConfiguration implements AdditionalConfiguration {
    private boolean isDebugMode;
    private int debugPort;
    private String providerUrl;
    private String compRepoPath;
    private String schemaRepoPath;
    private String jettyUrl;
    private String jettySSLUrl;
    private boolean watchForControlEvents;
    private String MS_JAVA_HOME;
    private String userDefinedJavaHome;
    private String ICF;

    public JavaLaunchConfiguration(){
    }

    public JavaLaunchConfiguration(boolean isDebugMode, int debugPort, String providerUrl, String compRepoPath, String schemaRepoPath, String jettyURL, String jettySSLUrl, boolean watchForControlEvents, String MS_JAVA_HOME, String userDefinedJavaHome, String ICF){
        this.isDebugMode = isDebugMode;
        this.debugPort = debugPort;
        this.providerUrl = providerUrl;
        this.compRepoPath = compRepoPath;
        this.schemaRepoPath = schemaRepoPath;
        this.jettySSLUrl = jettySSLUrl;
        this.jettyUrl = jettyURL;
        this.watchForControlEvents = watchForControlEvents;
        this.MS_JAVA_HOME = MS_JAVA_HOME;
        this.userDefinedJavaHome = userDefinedJavaHome;
        this.ICF = ICF;
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setIsDebugMode(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    public int getDebugPort() {
        return debugPort;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
    }

    public String getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    public String getCompRepoPath() {
        return compRepoPath;
    }

    public void setCompRepoPath(String compRepoPath) {
        this.compRepoPath = compRepoPath;
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

    public boolean isWatchForControlEvents() {
        return watchForControlEvents;
    }

    public void setWatchForControlEvents(boolean watchForControlEvents) {
        this.watchForControlEvents = watchForControlEvents;
    }

    public String getMS_JAVA_HOME() {
        return MS_JAVA_HOME;
    }

    public void setMS_JAVA_HOME(String MS_JAVA_HOME) {
        this.MS_JAVA_HOME = MS_JAVA_HOME;
    }

    public String getUserDefinedJavaHome() {
        return userDefinedJavaHome;
    }

    public void setUserDefinedJavaHome(String userDefinedJavaHome) {
        this.userDefinedJavaHome = userDefinedJavaHome;
    }

    public String getSchemaRepoPath() {
        return schemaRepoPath;
    }

    public void setSchemaRepoPath(String schemaRepoPath) {
        this.schemaRepoPath = schemaRepoPath;
    }

    @Override
    public String getICF() {
        return ICF;
    }

    public void setICF(String ICF) {
        this.ICF = ICF;
    }
}
