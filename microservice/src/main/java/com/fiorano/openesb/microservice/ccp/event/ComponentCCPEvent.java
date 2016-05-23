/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event;


public class ComponentCCPEvent {
    public ComponentCCPEvent(String componentId, ControlEvent controlEvent) {
        this.componentId = componentId;
        this.controlEvent = controlEvent;
    }

    private String componentId;
    private ControlEvent controlEvent;

    public String getComponentId() {
        return componentId;
    }

    public ControlEvent getControlEvent() {
        return controlEvent;
    }
}
