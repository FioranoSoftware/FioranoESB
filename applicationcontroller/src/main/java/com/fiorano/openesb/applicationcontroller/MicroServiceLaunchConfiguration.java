/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.application.LogManager;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.application.service.RuntimeArgument;
import com.fiorano.openesb.application.service.ServiceRef;
import com.fiorano.openesb.microservice.launch.AdditionalConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class MicroServiceLaunchConfiguration implements LaunchConfiguration {

    private String userName;
    private String password;
    private List<RuntimeArgument> runtimeArgs;
    private long stopRetryInterval;
    private int numberOfStopAttempts;
    private String microserviceId;
    private String microserviceVersion;
    private String serviceName;
    private String applicationName;
    private String applicationVersion;
    private AdditionalConfiguration additionalConfiguration;
    private LaunchMode launchMode;
    private List logModules;
    private LogManager logManager;
    private Vector<ServiceRef> runtimeDependencies;

    public MicroServiceLaunchConfiguration(String appGuid, String appVersion, String userName, String password, final ServiceInstance si, AdditionalConfiguration ac){
        this.userName = userName;
        this.password = password;
        this.runtimeArgs = si.getRuntimeArguments();
        this.microserviceId = si.getGUID();
        this.serviceName = si.getName();
        this.microserviceVersion = String.valueOf(si.getVersion());
        this.applicationName = appGuid;
        this.applicationVersion = appVersion;
        int i = si.getLaunchType();
        if(i==1){
            this.launchMode = LaunchMode.SEPARATE_PROCESS;
        }else if (i==2){
            this.launchMode = LaunchMode.IN_MEMORY;
        }else if (i==3){
            this.launchMode = LaunchMode.DOCKER;
        }else if (i==4){
            this.launchMode = LaunchMode.MANUAL;
        }
        this.logModules = si.getLogModules();
        this.logManager = si.getLogManager();
        for (ServiceRef runtimeDependency : (si.getServiceRefs())) {
            addRuntimeDependency(runtimeDependency);
        }

        additionalConfiguration = ac;

    }
    public void addRuntimeDependency(ServiceRef servDependencyInfo)
    {
        if (runtimeDependencies == null)
        {
            runtimeDependencies = new Vector<>();
        }
        if (!runtimeDependencies.contains(servDependencyInfo))
            runtimeDependencies.add(servDependencyInfo);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List getRuntimeArgs() {
        return runtimeArgs;
    }

    public List getLogModules() {
        return logModules;
    }

    public Enumeration<ServiceRef> getRuntimeDependencies() {
        if(runtimeDependencies!=null){
            return runtimeDependencies.elements();
        }
        return null;
    }

    public LaunchMode getLaunchMode() {
        return launchMode;
    }

    public long getStopRetryInterval() {
        return 0;
    }

    public int getNumberOfStopAttempts() {
        return 0;
    }

    public String getMicroserviceId() {
        return microserviceId;
    }

    public String getMicroserviceVersion() {
        return microserviceVersion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public AdditionalConfiguration getAdditionalConfiguration() {
        return additionalConfiguration;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}
