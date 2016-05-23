/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.application.service.LogModule;
import com.fiorano.openesb.microservice.ccp.CCPEventManager;
import com.fiorano.openesb.microservice.ccp.CCPResponseCallback;
import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ComponentCCPEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent;
import com.fiorano.openesb.microservice.ccp.event.common.data.ComponentStats;
import com.fiorano.openesb.microservice.ccp.event.common.data.Data;
import com.fiorano.openesb.microservice.ccp.event.common.data.MemoryUsage;
import com.fiorano.openesb.microservice.ccp.event.common.data.ProcessID;
import com.fiorano.openesb.microservice.ccp.event.peer.CommandEvent;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.utils.I18NUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CCPCommandHelper {

    private Map<String, String> logLevels;
    private CCPEventManager.CCPEventGenerator ccpEventGenerator;
    private CCPEventManager ccpEventManager;
    private String componentId;
    private ProcessID processID;

    private long ccpRequestTimeout;
    private LaunchConfiguration launchConfiguration;

    public CCPCommandHelper(CCPEventManager ccpEventManager, LaunchConfiguration launchConfiguration) {
        this.launchConfiguration = launchConfiguration;
        this.ccpEventGenerator = ccpEventManager.getCcpEventGenerator();
        this.ccpEventManager = ccpEventManager;
        this.componentId = launchConfiguration.getApplicationName() + "__"
                + launchConfiguration.getApplicationVersion().replace(".","_") + "__" + launchConfiguration.getServiceName();
    }

    public String getComponentId() {
        return componentId;
    }

    public void setLogLevel(Map<String, String> modules) throws Exception {
        Map<String, String> modified = new HashMap<String, String>();
        for (Map.Entry<String, String> module : modules.entrySet()) {
            if (!logLevels.get(module.getKey()).equalsIgnoreCase(module.getValue()))
                modified.put(module.getKey(), module.getValue());
        }

        logLevels.putAll(modules);
        Logger.getLogger((getComponentId()).toUpperCase()).setLevel(getMaxLogLevel());

        CommandEvent setLogLevelCommand = new CommandEvent();
        setLogLevelCommand.setCommand(CommandEvent.Command.SET_LOGLEVEL);
        setLogLevelCommand.setArguments(modified);
        setLogLevelCommand.setReplyNeeded(false);
        ccpEventGenerator.sendEvent(setLogLevelCommand, getComponentId());
    }

    /**
     * Returns max Log level from all logModules
     *
     * @return Level
     */
    public Level getMaxLogLevel() {
        Level maxLevel = null;
        if (logLevels != null) {
            for (String module : logLevels.values()) {
                Level level = LogModule.parseLevel(module);
                if (level != null) {
                    // if it is max, no more work
                    if (level == Level.ALL) return Level.ALL;
                    if (maxLevel == null) maxLevel = level;
                    else
                        // Greater int value => Less Logging.
                        if (maxLevel.intValue() > level.intValue())
                            maxLevel = level;
                }
            }
        }
        if (maxLevel == null)
            maxLevel = Level.ALL;
        return maxLevel;
    }

    public ComponentStats getComponentStats() throws FioranoException {
        return (ComponentStats) _getData(DataRequestEvent.DataIdentifier.COMPONENT_STATS);
    }

    public void flushMessages() throws Exception {
        CommandEvent flushCommand = new CommandEvent();
        flushCommand.setCommand(CommandEvent.Command.FLUSH_MESSAGES);
        flushCommand.setReplyNeeded(false);
        ccpEventGenerator.sendEvent(flushCommand, getComponentId());
    }

    public MemoryUsage getMemoryUsage() throws FioranoException {
        return (MemoryUsage) getData(DataRequestEvent.DataIdentifier.MEMORY_USAGE);
    }

    public ProcessID getProcessID() throws FioranoException {
        if (processID == null) {
            processID = (ProcessID) getData(DataRequestEvent.DataIdentifier.PID);
        }
        return processID;
    }

    private Data getData(DataRequestEvent.DataIdentifier dataIdentifier) throws FioranoException {
        return _getData(dataIdentifier);
    }

    private Data _getData(DataRequestEvent.DataIdentifier dataIdentifier) throws FioranoException {
            String applicationName = launchConfiguration.getApplicationName();
            String applicationVersion = launchConfiguration.getApplicationVersion();
            String serviceName = launchConfiguration.getServiceName();
            DataRequestEvent dataRequestEvent = new DataRequestEvent();
            Collection<DataRequestEvent.DataIdentifier> dataIDs = new ArrayList<>();
            dataIDs.add(dataIdentifier);
            dataRequestEvent.setDataIdentifiers(dataIDs);
            dataRequestEvent.setRepetitionCount(1);
            dataRequestEvent.setReplyNeeded(true);
            dataRequestEvent.setReplyTimeout(ccpRequestTimeout);

            final Object waitObject = new Object();
            ResponseCallback callback = new ResponseCallback(dataRequestEvent.getEventId(), waitObject);
            ccpEventGenerator.sendEvent(dataRequestEvent, callback, getComponentId());

            synchronized (waitObject) {
                try {
                    if (!callback.isReplyReceived())
                        waitObject.wait(ccpRequestTimeout);
                } catch (InterruptedException e) {
                    //Ignore the exception
                }
            }

            //Help GC - Remove the callback handler after timeout
            ccpEventManager.removeCallback(dataRequestEvent.getEventId());
            if (callback.isReplyReceived()) {
                ComponentCCPEvent ccpDataEvent = callback.getDataEvent();

                if (ccpDataEvent != null) {
                    DataEvent dataEvent = (DataEvent) ccpDataEvent.getControlEvent();
                    Map<DataRequestEvent.DataIdentifier, Data> data = dataEvent.getData();
                    return data != null ? data.get(dataIdentifier) : null;
                } else if (callback.isCorrelationIDMismatch()) {
                    throw new FioranoException(Bundle.CCP_DATA_ID_MISMATCH.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.CCP_DATA_ID_MISMATCH, applicationName +
                            CoreConstants.APP_VERSION_DELIM + applicationVersion, serviceName, dataIdentifier.toString()));
                } else {
                    throw new FioranoException(Bundle.CCP_DATA_NULL.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.CCP_DATA_NULL, applicationName
                            + CoreConstants.APP_VERSION_DELIM + applicationVersion, serviceName, dataIdentifier.toString()));
                }
            } else {
                    throw new FioranoException(Bundle.CCP_DATA_REQUEST_TIMEOUT.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.CCP_DATA_REQUEST_TIMEOUT, applicationName
                            + CoreConstants.APP_VERSION_DELIM + applicationVersion, serviceName, dataIdentifier.toString()));
            }
    }

    public void stopComponent(long componentStopWaitTime) throws Exception {
        CommandEvent stopCommand = new CommandEvent();
        stopCommand.setCommand(CommandEvent.Command.INITIATE_SHUTDOWN);
        stopCommand.setReplyNeeded(true);
        stopCommand.setReplyTimeout(componentStopWaitTime);
        ccpEventGenerator.sendEvent(stopCommand, getComponentId());
    }

    public void unregisterListener(SeparateProcessRuntimeHandle.ComponentLifeCycleWorkflow lifeCycleWorkflow, CCPEventType status) {
        ccpEventManager.unregisterListener(lifeCycleWorkflow);
    }

    public synchronized void registerListener(SeparateProcessRuntimeHandle.ComponentLifeCycleWorkflow listener, CCPEventType... eventTypes){
        ccpEventManager.registerListener(listener, eventTypes);
    }


    private class ResponseCallback implements CCPResponseCallback {
        private final Object waitObject;
        private boolean replyReceived = false;
        private Long eventID;
        private ComponentCCPEvent dataEvent;
        private boolean correlationIDMismatch;

        private ResponseCallback(Long eventID, final Object waitObject) {
            this.waitObject = waitObject;
            this.eventID = eventID;
        }

        public boolean isReplyReceived() {
            return replyReceived;
        }

        public boolean isCorrelationIDMismatch() {
            return correlationIDMismatch;
        }

        public ComponentCCPEvent getDataEvent() {
            return dataEvent;
        }

        public void onResponse(Long requestID, ComponentCCPEvent event) {
            if (requestID.equals(eventID))
                this.dataEvent = event;
            else
                correlationIDMismatch = true;

            replyReceived = true;
            synchronized (waitObject) {
                waitObject.notify();
            }
        }
    }


}
