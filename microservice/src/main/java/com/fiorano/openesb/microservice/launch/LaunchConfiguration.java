/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch;

import com.fiorano.openesb.application.application.LogManager;
import com.fiorano.openesb.application.service.RuntimeArgument;
import com.fiorano.openesb.application.service.ServiceRef;

import java.util.Enumeration;
import java.util.List;

public interface LaunchConfiguration<A extends AdditionalConfiguration> {
    String getUserName();
    String getPassword();

    List<RuntimeArgument> getRuntimeArgs();

    List getLogModules();

    Enumeration<ServiceRef> getRuntimeDependencies();

    enum LaunchMode {SEPARATE_PROCESS, IN_MEMORY,DOCKER, NONE, MANUAL}
    LaunchMode getLaunchMode();
    long getStopRetryInterval();
    int getNumberOfStopAttempts();
    String getMicroserviceId();
    String getMicroserviceVersion();
    String getServiceName();
    String getApplicationName();
    String getApplicationVersion();
    A getAdditionalConfiguration();
    LogManager getLogManager();
}
