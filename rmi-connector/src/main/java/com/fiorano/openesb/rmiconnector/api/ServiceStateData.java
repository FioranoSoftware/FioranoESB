/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class ServiceStateData implements Serializable {
    private static final long serialVersionUID = -2418417840124799866L;
    private long launchTime;
    private long killTime;
    private String statusString;
    private String serviceGUID;
    private String serviceInstName;
    private String serviceNodeName;
    private String runningVersion;
    private String strUniqueRunningInstID;
    private boolean gracefulKill;


    /**
     * Default Constructor
     */
    public ServiceStateData() {
    }

    /**
     * This method gets the launch time for the service instance, about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @return long - The launch Time value for the service instance
     */
    public long getLaunchTime() {
        return launchTime;
    }

    /**
     * This method gets the abort time for the service instance, about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @return long - Abort Time for the service instance
     */
    public long getKillTime() {
        return killTime;
    }

    /**
     * This method gets service status for the service instance, about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @return String - The isRunning value
     */
    public String getStatusString() {
        return statusString;
    }


    /**
     * This method gets the serviceGUID for the service instance, about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @return String - The serviceGUID value
     */
    public String getServiceGUID() {
        return serviceGUID;
    }

    /**
     * This method gets the Instance name for the service instance, about which
     * this object of <code>ServiceStateData</code> contains state
     * details.
     *
     * @return String - The Instance name of service instance.
     */
    public String getServiceInstanceName() {
        return serviceInstName;
    }

    /**
     * This method sets the specified string as node name on which the service
     * instance is running, about which this object of <code>ServiceStateData</code> contains state details
     *
     * @return String - The node name over which the service instance is running
     */
    public String getServiceNodeName() {
        return serviceNodeName;
    }


    /**
     * This method gets the runningVersion of theServiceInstance.
     *
     * @return String - The runningVersion.
     */
    public String getRunningVersion() {
        return runningVersion;
    }

    /**
     * Returns unique running inst ID for object
     *
     * @return String - The unique running id of the running Service Instance
     */
    public String getUniqueRunningInstID() {
        return strUniqueRunningInstID;
    }

    /**
     * The Graceful way of killing a service instance is killing it from RTL.
     * While restoring the state of the service instance, the service instances that are not killed from RTL will be relaunched
     * This method checks whether a service instance is gracefully killed or not.
     * @return boolean - true if service instance is killed gracefully, false otherwise
     */
    public boolean isGracefulKill() {
        return gracefulKill;
    }

    /**
     * This method sets if the service instance is killed gracefully
     *
     * @param gracefulKill true if gracefully killed, false otherwise
     */
    public void setGracefulKill(boolean gracefulKill) {
        this.gracefulKill = gracefulKill;
    }

    /**
     * This method sets the launch time for the service instance, about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @param launchTime The time to be set as launch time
     */
    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    /**
     * This method sets the launch time for the service instance about which
     * this object of <code>ServiceStateData</code > contains state
     * details.
     *
     * @param killTime The time to be set as abort time
     */
    public void setKillTime(long killTime) {
        this.killTime = killTime;
    }

    /**
     * This method sets the specified string as service status. It is set for
     * the service instance, details of which is contained in the object of <code>ServiceStateData</code>.
     *
     * @param statusString The new statusString value
     */
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    /**
     * This method sets the specified string as serviceGUID for the service
     * instance,details of which is contained in the object of <code>ServiceStateData</code>.
     *
     * @param serviceGUID The string to be set as serviceGUID
     */
    public void setServiceGUID(String serviceGUID) {
        this.serviceGUID = serviceGUID;
    }

    /**
     * This method sets the specified string as serviceInstancename for the
     * service instance, details of which is contained in the object of <code>ServiceStateData</code>.
     *
     * @param serviceInstName String that is to be set as instance name
     */
    public void setServiceInstanceName(String serviceInstName) {
        this.serviceInstName = serviceInstName;
    }

    /**
     * This method sets the specified string as node name on which the service
     * instance is running, for the service instance, details of which is contained in the object of <code>ServiceStateData</code>
     *
     * @param serviceNodeName String to be set as node name over which service instance is running
     */
    public void setServiceNodeName(String serviceNodeName) {
        this.serviceNodeName = serviceNodeName;
    }

    /**
     * This method sets the runningVersion of the service instance.
     *
     * @param runningVersion The runningVersion.
     */
    public void setRunningVersion(String runningVersion) {
        this.runningVersion = runningVersion;
    }

    /**
     * This methods sets the unique running inst ID of the service instance
     *
     * @param strUniqueRunningInstID Unique running ID of the running service instance
     */
    public void setUniqueRunningInstID(String strUniqueRunningInstID) {
        this.strUniqueRunningInstID = strUniqueRunningInstID;
    }
}

