/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp;

public abstract class ComponentWorkflowListener implements IEventListener {
    private String componentInstanceName;
    private String applicationName;
    private String applicationVersion;

    protected ComponentWorkflowListener(String componentInstanceName, String applicationName, String applicationVersion) {
        this.componentInstanceName = componentInstanceName;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
    }

    public String getId() {
        return applicationName + "__" + applicationVersion + "__" + componentInstanceName;
    }
}

