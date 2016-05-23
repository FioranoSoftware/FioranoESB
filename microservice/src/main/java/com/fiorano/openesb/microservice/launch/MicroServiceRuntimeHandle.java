/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch;

import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.events.EventsManager;
import com.fiorano.openesb.events.MicroServiceEvent;
import com.fiorano.openesb.microservice.bundle.Activator;
import com.fiorano.openesb.microservice.ccp.event.common.data.ComponentStats;
import com.fiorano.openesb.microservice.launch.impl.Bundle;
import com.fiorano.openesb.microservice.launch.impl.CoreConstants;
import com.fiorano.openesb.microservice.launch.impl.EventStateConstants;
import com.fiorano.openesb.utils.RBUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class MicroServiceRuntimeHandle {

    protected boolean isRunning;
    protected String serviceInstName;
    protected String strStatus;
    protected String exceptionTrace;
    protected long killTime;
    protected long launchTime;
    protected String serviceGUID;
    protected float runningVersion;
    protected volatile boolean bServiceDestroyed;
    protected ServiceInstanceStateDetails servStateDetails = new ServiceInstanceStateDetails();

    protected boolean gracefulKill;
    protected LaunchConfiguration launchConfiguration;
    protected EventsManager eventManager;
    protected Logger logger = LoggerFactory.getLogger(Activator.class);

    protected MicroServiceRuntimeHandle(LaunchConfiguration launchConfiguration){
        this.launchConfiguration = launchConfiguration;
        this.serviceInstName = launchConfiguration.getServiceName();
        this.serviceGUID = launchConfiguration.getMicroserviceId();
        setRunningVersion(Float.parseFloat(launchConfiguration.getMicroserviceVersion()));
        strStatus = EventStateConstants.SERVICE_HANDLE_CREATED;
        eventManager = FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getService(FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getServiceReference(EventsManager.class));
    }

    protected String getAppVersion() {
        return launchConfiguration.getApplicationVersion();
    }

    public String getServiceInstName() {
        return launchConfiguration.getServiceName();
    }

    public void setServiceInstName(String serviceInstName) {
        this.serviceInstName = serviceInstName;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getExceptionTrace() {
        return exceptionTrace;
    }

    public void setExceptionTrace(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

    public long getKillTime() {
        return killTime;
    }

    public void setKillTime(long killTime) {
        this.killTime = killTime;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public String getServiceGUID() {
        return serviceGUID;
    }

    public void setServiceGUID(String serviceGUID) {
        this.serviceGUID = serviceGUID;
    }

    public float getRunningVersion() {
        return runningVersion;
    }

    public void setRunningVersion(float runningVersion) {
        this.runningVersion = runningVersion;
    }

    public boolean isGracefulKill() {
        return gracefulKill;
    }

    public void setGracefulKill(boolean gracefulKill) {
        this.gracefulKill = gracefulKill;
    }

    public LaunchConfiguration getLaunchConfiguration() {
        return launchConfiguration;
    }

    public void setLaunchConfiguration(LaunchConfiguration launchConfiguration) {
        this.launchConfiguration = launchConfiguration;
    }

    public abstract boolean isRunning();

    public abstract void stop() throws Exception;

    protected abstract   void kill() throws Exception;

    protected abstract void setLogLevel(Map<String, String> modules) throws Exception;

    public abstract ComponentStats getComponentStats() throws FioranoException;

    public abstract LaunchConfiguration.LaunchMode getLaunchMode();

    public ServiceInstanceStateDetails getServiceStateDetails(){
        ServiceInstanceStateDetails details = new ServiceInstanceStateDetails();

        details.setKillTime(killTime);
        details.setLaunchTime(launchTime);
        details.setServiceGUID(serviceGUID);
        details.setServiceInstanceName(serviceInstName);
        details.setStatusString(strStatus);
        details.setGracefulKill(gracefulKill);
        details.setRunningVersion(String.valueOf(runningVersion));
        details.setLaunchType(convertLaunchMode(launchConfiguration.getLaunchMode()));
        return details;
    }

    private int convertLaunchMode(LaunchConfiguration.LaunchMode launchMode) {
        if (launchMode.name().equalsIgnoreCase("SEPARATE_PROCESS"))
            return 1;
        else if (launchMode.name().equalsIgnoreCase("IN_MEMORY"))
            return 2;
        else if (launchMode.name().equalsIgnoreCase("MANUAL"))
            return 4;
        else
            return 3;

    }

    public void generateServiceBoundEvent() throws FioranoException {
        bServiceDestroyed = false;
        isRunning=true;
        String message = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_BOUND, getServiceInstName(), getNodeName());
        servStateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_BOUND);
        generateMicroServiceEvent(MicroServiceEvent.MicroServiceEventType.SERVICE_LAUNCHED, Event.EventCategory.INFORMATION, EventStateConstants.SERVICE_HANDLE_BOUND, getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), message, AlertModules.SERVICE_LAUNCH_KILL);
        strStatus=EventStateConstants.SERVICE_HANDLE_BOUND;
    }

    private String getNodeName() {
        return "Node";
    }

    protected void generateServiceKillFailedEvent(String reason) throws FioranoException {
        String status = EventStateConstants.SERVICE_FAILED_TO_KILL;
        MicroServiceEvent.MicroServiceEventType eventType = MicroServiceEvent.MicroServiceEventType.SERVICE_STOP_FAILED;
        Event.EventCategory eventCategory = Event.EventCategory.ERROR;
        String description = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_KILL_FAILURE, getNodeName());
        servStateDetails.setRunningVersion(getVersion());
        servStateDetails.setStatusString(status);
        strStatus=status;
        generateServiceEvent(MicroServiceEvent.MicroServiceEventType.SERVICE_STOP_FAILED, eventCategory, status, getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), description, AlertModules.SERVICE_LAUNCH_KILL);

    }

    protected void generateServiceFailedToLaunchEvent(String reason) throws FioranoException {
        String status = EventStateConstants.SERVICE_FAILED_TO_LAUNCH;
        MicroServiceEvent.MicroServiceEventType eventType = MicroServiceEvent.MicroServiceEventType.SERVICE_LAUNCH_FAILED;
        Event.EventCategory eventCategory = Event.EventCategory.ERROR;
        String description = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_LAUNCH_FAILURE, getNodeName());
        servStateDetails.setRunningVersion(getVersion());
        servStateDetails.setStatusString(status);
        strStatus = status;
        generateServiceEvent(eventType, eventCategory, status, getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), description, AlertModules.SERVICE_LAUNCH_KILL);

    }

    protected void generateServiceBoundingEvent() throws FioranoException {
        bServiceDestroyed = false;
        String message = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_BOUNDING, getServiceInstName(), getNodeName());
        servStateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_BOUNDING);
        strStatus=EventStateConstants.SERVICE_HANDLE_BOUNDING;
        generateMicroServiceEvent(MicroServiceEvent.MicroServiceEventType.SERVICE_LAUNCHING, Event.EventCategory.INFORMATION, EventStateConstants.SERVICE_HANDLE_BOUNDING, getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), message, AlertModules.SERVICE_LAUNCH_KILL);

    }

    public MicroServiceEvent getMicroServiceEvent(MicroServiceEvent.MicroServiceEventType eventType, Event.EventCategory category, String status, String serviceGUID, String serviceVersion, String serviceInstName,
                                                  String appGuid, String appVersion, String description, String moduleName, String serverID) throws FioranoException {
        MicroServiceEvent event = new MicroServiceEvent();
        event.setMicroServiceEventType(eventType);
        event.setEventCategory(category);
        event.setEventScope("OpenESB");
        event.setSource("Peer Server");
        event.setEventModule(moduleName);
        event.setEventGenerationDate(System.currentTimeMillis());
        event.setEventStatus(status);
        event.setServiceGUID(serviceGUID);
        event.setServiceVersion(Float.parseFloat(serviceVersion));
        event.setApplicationGUID(launchConfiguration.getApplicationName());
        event.setApplicationVersion(appVersion);
        event.setServiceInstance(serviceInstName);
        event.setEventDescription(description);
        event.setSourceTPSName("FPS");
        event.setBuildVersionNo(getBuildNo());
        event.setSink(serverID);
        return event;
    }

    //todo implement in common class
    protected static int getBuildNo() {
        return 100;
    }


    /**
     * Generates Service Unbound event
     *
     * @param reason Reason for which component is being killed
     * @throws FioranoException exception
     */
    protected void generateServiceUnboundEvent(String reason, boolean isWarning) throws FioranoException {
        String message;
        if (reason != null && reason.contains("STOPPING_COMPONENT_DUE_TO_PRESENCE_OF_MULTIPLE_INSTANCES_IN_NETWORK")) {
            message = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_UNBOUND2, getServiceInstName(), getNodeName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion(), reason);
            servStateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_UNBOUND_SERVER_MANAGEMENT_ACTION);
            strStatus=EventStateConstants.SERVICE_HANDLE_UNBOUND_SERVER_MANAGEMENT_ACTION;
            generateServiceEvent(MicroServiceEvent.MicroServiceEventType.SERVICE_STOPPED, isWarning ? Event.EventCategory.WARNING : Event.EventCategory.INFORMATION, EventStateConstants.SERVICE_HANDLE_UNBOUND_SERVER_MANAGEMENT_ACTION,
                    getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), message, AlertModules.SERVICE_LAUNCH_KILL);
        } else {
            if (isWarning || reason != null)
                message = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_UNBOUND2, getServiceInstName(), getNodeName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion(), reason);
            else
                message = RBUtil.getMessage(Bundle.class, Bundle.SERVICE_UNBOUND1, getServiceInstName(), getNodeName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion());

            servStateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_UNBOUND);
            strStatus=EventStateConstants.SERVICE_HANDLE_UNBOUND;
            generateServiceEvent(MicroServiceEvent.MicroServiceEventType.SERVICE_STOPPED, isWarning ? Event.EventCategory.WARNING : Event.EventCategory.INFORMATION, EventStateConstants.SERVICE_HANDLE_UNBOUND,
                    getServiceGUID(), getVersion(), getServiceInstName(), launchConfiguration.getApplicationName(), getAppVersion(), message, AlertModules.SERVICE_LAUNCH_KILL);
        }
    }

    private void generateServiceEvent(MicroServiceEvent.MicroServiceEventType eventType, Event.EventCategory category, String status, String serviceGUID, String serviceVersion, String serviceInstName,
                                      String appGuid, String appVersion, String description, String moduleName) throws FioranoException {
        MicroServiceEvent event = getMicroServiceEvent(eventType, category, status, serviceGUID, serviceVersion, serviceInstName, launchConfiguration.getApplicationName(), appVersion, description, moduleName, getNodeName());
        logger.debug("Generating micro Service event "+event.toString());
        eventManager.raiseEvent(event);
    }
    private void generateMicroServiceEvent(MicroServiceEvent.MicroServiceEventType eventType, Event.EventCategory category, String status, String serviceGUID, String serviceVersion, String serviceInstName,
                                           String appGuid, String appVersion, String description, String moduleName) throws FioranoException {
        MicroServiceEvent event = getMicroServiceEvent(eventType, category, status, serviceGUID, serviceVersion, serviceInstName, launchConfiguration.getApplicationName(), appVersion, description, moduleName, getNodeName());
        logger.debug("Generating micro Service event "+event.toString());
        eventManager.raiseEvent(event);
    }

    public String getVersion() {
        return launchConfiguration.getMicroserviceVersion();
    }
    private class AlertModules {
        public static final String SERVICE_LAUNCH_KILL = "SERVICE_LAUNCH_KILL";
    }

}

