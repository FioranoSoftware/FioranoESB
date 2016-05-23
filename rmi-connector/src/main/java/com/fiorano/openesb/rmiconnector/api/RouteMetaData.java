/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class RouteMetaData implements Serializable {
    private static final long serialVersionUID = 6105389315502986947L;

    private String routeName;
    private String sourceServiceInstance;
    private String targetServiceInstance;
    private PortInstanceMetaData sourcePortInstance;
    private PortInstanceMetaData targetPortInstance;

    /**
     *  This constructor is used to initialize values for objects of this class
     *  @param routeName Name of the Route
     *  @param sourceServiceInstance Name of the Source Service Instance
     *  @param targetServiceInstance Name of the Target Service Instance
     *  @param sourcePortInstance Port Instance of the source
     *  @param targetPortInstance Port Instance of the target
     */
    public RouteMetaData(String routeName, String sourceServiceInstance, String targetServiceInstance, PortInstanceMetaData sourcePortInstance, PortInstanceMetaData targetPortInstance) {
        this.routeName = routeName;
        this.sourceServiceInstance = sourceServiceInstance;
        this.targetServiceInstance = targetServiceInstance;
        this.sourcePortInstance = sourcePortInstance;
        this.targetPortInstance = targetPortInstance;
    }

    /**
     *  This method returns the name of the Route
     *  @return String - Name of the Route
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     *  This method returns the name of the Source Service Instance
     *  @return String - Name of the Source Service Instance
     */
    public String getSourceServiceInstance() {
        return sourceServiceInstance;
    }

    /**
     *  This method returns the name of the Target Service Instance
     *  @return String - Name of the Target Service Instance
     */
    public String getTargetServiceInstance() {
        return targetServiceInstance;
    }

    /**
     *  This method returns the Port Instance of the Source Port
     *  @return PortInstanceMetaData - Object containing Port Instance of the source
     */
    public PortInstanceMetaData getSourcePortInstance() {
        return sourcePortInstance;
    }

    /**
     *  This method returns the Port Instance of the Target Port
     *  @return PortInstanceMetaData - Object containing Port Instance of the target
     */
    public PortInstanceMetaData getTargetPortInstance() {
        return targetPortInstance;
    }

    /**
     *  This method sets the name of the Route
     *  @param routeName Name of the Route
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    /**
     *  This method sets the name of the Source Service Instance
     *  @param sourceServiceInstance Name of the Source Service Instance
     */
    public void setSourceServiceInstance(String sourceServiceInstance) {
        this.sourceServiceInstance = sourceServiceInstance;
    }

    /**
     *  This method sets the name of the Target Service Instance
     *  @param targetServiceInstance Name of the Target Service Instance
     */
    public void setTargetServiceInstance(String targetServiceInstance) {
        this.targetServiceInstance = targetServiceInstance;
    }

    /**
     *  This method sets the Port Instance of the Source Port
     *  @param sourcePortInstance Object containing Port Instance of the source
     */
    public void setSourcePortInstance(PortInstanceMetaData sourcePortInstance) {
        this.sourcePortInstance = sourcePortInstance;
    }

    /**
     *  This method sets the Port Instance of the Target Port
     *  @param  targetPortInstance Object containing Port Instance of the target
     */
    public void setTargetPortInstance(PortInstanceMetaData targetPortInstance) {
        this.targetPortInstance = targetPortInstance;
    }
}
