/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;
import java.util.*;

public class ApplicationStateData implements Serializable {

    private static final long serialVersionUID = 4566594311656096579L;
    private long launchTime;

    private long killTime;

    private Hashtable<String, ServiceStateData> serviceStates;

    private Hashtable<String, String> serviceExceptionTraces;

    private String eventProcessID;

    private String EventProcessLabel;

    private List<String> debugRoutes;

    /**
     * Default Constructor
     */
    public ApplicationStateData() {
        serviceStates = new Hashtable<String, ServiceStateData>();
        serviceExceptionTraces = new Hashtable<String, String>();
        debugRoutes = new ArrayList<String>();
        eventProcessID = "";
        EventProcessLabel = "";
        launchTime = -1;
        killTime = -1;
    }

    /**
     * This method adds the Debug <code>routeGUID</code> to the list of debug Routes
     *
     * @param routeGUID RouteID
     */
    public void addDebugRoute(String routeGUID) {
        debugRoutes.add(routeGUID);
    }

    /**
     * This method returns the Iterator of Debug Routes.
     *
     * @return Iterator - List of Debug Routes
     */
    public Iterator getDebugRoutes() {
        return debugRoutes.iterator();
    }

    /**
     * This method sets the launch Time for EventProcess in this <code>EventProcessStateData</code> object.
     *
     * @param launchTime Launch Time value
     */
    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    /**
     * This method returns the launch Time for EventProcess in this <code>EventProcessStateData</code> object.
     *
     * @return long - Launch Time value
     */
    public long getLaunchTime() {
        return launchTime;
    }

    /**
     * This method sets the Abort Time for EventProcess in this <code>EventProcessStateData</code> object.
     *
     * @param killTime Abort Time value
     */
    public void setKillTime(long killTime) {
        this.killTime = killTime;
    }

    /**
     * This method returns the Abort Time for EventProcess in this <code>EventProcessStateData</code> object.
     *
     * @return long - Abort Time value
     */
    public long getKillTime() {
        return killTime;
    }

    /**
     * This method sets the Event Process ID in this <code>EventProcessStateData</code> object.
     *
     * @param eventProcessID Event Process ID.
     */
    public void setEventProcessID(String eventProcessID) {
        this.eventProcessID = eventProcessID;
    }

    /**
     * This method returns the EventProcess ID from this <code>EventProcessStateData</code> object.
     *
     * @return String - Event Process ID
     */
    public String getEventProcessID() {
        return eventProcessID;
    }

    /**
     * This method adds the specified <code>ServiceStateData</code>
     * object as status for the specified service instance, in this
     * <code>EventProcessStateData</code> object.
     *
     * @param instName The name of service instance.
     * @param status   object of ServiceInstanceStateDetails
     */
    public void addServiceStatus(String instName, ServiceStateData status) {
        serviceStates.put(instName, status);
    }

    /**
     * This method returns the status for specified service instance from this <code>EventProcessStateData</code> object.
     *
     * @param instName service instance name
     * @return ServiceStateData - an object of ServiceStateData
     */
    public ServiceStateData getServiceStatus(String instName) {
        return serviceStates.get(instName);
    }

    /**
     * This method returns enumeration of names of all the service instances belonging to an EventProcess
     * with this <code>EventProcessStateData</code> object.
     *
     * @return Enumeration - list of service instance names.
     */
    public Enumeration getAllServiceNames() {
        return serviceStates.keys();
    }

    /**
     * This method returns the exception trace for specified service instance, from this <code>EventProcessStateData</code> object.
     *
     * @param instName service instance name
     * @return String - Service Status value
     */
    public String getServiceExceptionTrace(String instName) {
        return serviceExceptionTraces.get(instName);
    }

    /**
     * This method returns the exception trace for all the service instance, from this
     * <code>EventProcessStateData</code> object.
     *
     * @return Enumeration - exception trace for all the service instances.
     */
    public Enumeration getAllServiceWithExceptions() {
        return serviceExceptionTraces.keys();
    }

    /**
     * This method adds the specified exception trace for specified service instance, to this <code>EventProcessStateData</code> object.
     *
     * @param instName Service instance name
     * @param trace    trace to be added.
     */
    public void addServiceExceptionTrace(String instName, String trace) {
        serviceExceptionTraces.put(instName, trace);
    }

    /**
     * This method returns environment label of running event process
     * @return String - event process environment label
     */
    public String getEventProcessLabel() {
        return EventProcessLabel;
    }

    /**
     * This method sets the specified <code>eventProcessLabel</code> as the environment label of this event process
     * @param eventProcessLabel environment label to be set
     */
    public void setEventProcessLabel(String eventProcessLabel) {
        this.EventProcessLabel = eventProcessLabel;
    }


}

