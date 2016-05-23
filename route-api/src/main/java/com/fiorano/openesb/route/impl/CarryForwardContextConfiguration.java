/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.InputPortInstance;
import com.fiorano.openesb.application.application.PortInstance;
import com.fiorano.openesb.route.RouteOperationConfiguration;

public class CarryForwardContextConfiguration extends RouteOperationConfiguration {
    private Application application;
    private String serviceInstanceName;
    private PortInstance portInstance;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    public PortInstance getPortInstance() {
        return portInstance;
    }

    public void setPortInstance(PortInstance portInstance) {
        this.portInstance = portInstance;
    }
}
