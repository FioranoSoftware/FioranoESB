/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class ServiceInstanceMetaData implements Serializable {
    private static final long serialVersionUID = 2824986873475326887L;

    private String serviceInstanceName;
    private String serviceGUID;
    private float version;
    private String shortDescription;
    private String longDescription;
    private String nodes[];
    private int launchType;


    /**
     *  This constructor is used to initialize values for objects of this class
     *  @param serviceInstanceName Name of the Service Instance
     *  @param serviceGUID GUID of the Service Instance
     *  @param version Version of the Service Instance
     *  @param shortDescription Short Description about the Service Instance
     *  @param longDescription Long Description about the Service Instance
     *  @param nodes Array of names of Peer Servers set for launching the Event Process
     *  @param launchType Launch Type of the Service Instance
     */
    public ServiceInstanceMetaData(String serviceInstanceName, String serviceGUID, float version, String shortDescription, String longDescription, String[] nodes, int launchType) {
        this.serviceInstanceName = serviceInstanceName;
        this.serviceGUID = serviceGUID;
        this.version = version;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.nodes = nodes;
        this.launchType = launchType;
    }

    /**
     * This method returns the name of the Service Instance
     * @return String - Name of the Service Instance
     */
    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    /**
     * This method returns the GUID of the Service Instance
     * @return String - GUID of the Service Instance
     */
    public String getServiceGUID() {
        return serviceGUID;
    }

    /**
     * This method returns the version of the Service Instance
     * @return Float - Version of the Service Instance
     */
    public float getVersion() {
        return version;
    }

    /**
     * This method returns the short description about the Service Instance
     * @return String - Short Description about the Service Instance
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * This method returns the long description about the Service Instance
     * @return String - Long Description about the Service Instance
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * This method returns the list of nodes configured for launching the Service Instance
     * @return String Array - List of nodes
     */
    public String[] getNodes() {
        return nodes;
    }

    /**
     * This method returns the launch type of the Service Instance
     * @return int - Launch Type of the Service Instance
     */
    public int getLaunchType() {
        return launchType;
    }

    /**
     * This method sets the name of the Service Instance
     * @param serviceInstanceName Name of the Service Instance
     */
    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    /**
     * This method sets the GUID of the Service Instance
     * @param serviceGUID GUID of the Service Instance
     */
    public void setServiceGUID(String serviceGUID) {
        this.serviceGUID = serviceGUID;
    }

    /**
     * This method sets the version of the Service Instance
     * @param version Version of the Service Instance
     */
    public void setVersion(float version) {
        this.version = version;
    }

    /**
     * This method sets the short description about the Service Instance
     * @param shortDescription Short Description about the Service Instance
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * This method sets the long description about the Service Instance
     * @param longDescription Long Description about the Service Instance
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * This method sets the list of nodes configured for launching the Service Instance
     * @param nodes Array of nodes
     */
    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    /**
     * This method sets the launch type of the Service Instance
     * @param launchType Launch Type of the Service Instance
     */
    public void setLaunchType(int launchType) {
        this.launchType = launchType;
    }
}
