/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.events.EventIds;
import com.fiorano.openesb.events.EventsManager;
import com.fiorano.openesb.events.MicroServiceEvent;
import com.fiorano.openesb.microservice.ccp.ComponentWorkflowListener;
import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ComponentCCPEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent;
import com.fiorano.openesb.microservice.ccp.event.common.data.ComponentStats;
import com.fiorano.openesb.microservice.ccp.event.common.data.MemoryUsage;
import com.fiorano.openesb.microservice.ccp.event.component.StatusEvent;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.launch.MicroServiceRuntimeHandle;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SeparateProcessRuntimeHandle extends MicroServiceRuntimeHandle {

    private Process osProcess;
    private CCPCommandHelper ccpCommandHelper;
    private ComponentLifeCycleWorkflow lifeCycleWorkflow;
    private Logger coreLogger;
    private boolean shutdownOfCCPComponentInProgress;
    private int numberOfForceShutdownAttempts;
    private volatile boolean isKilling;
    private final Object killSyncObject = new Object();
    private long componentStopWaitTime = 10000;

    public SeparateProcessRuntimeHandle(LaunchConfiguration launchConfiguration, CCPCommandHelper ccpCommandHelper) throws FioranoException {
        super(launchConfiguration);
        this.ccpCommandHelper = ccpCommandHelper;
        lifeCycleWorkflow = new ComponentLifeCycleWorkflow(serviceInstName, launchConfiguration.getApplicationName(), launchConfiguration.getApplicationVersion());
        ccpCommandHelper.registerListener(lifeCycleWorkflow, CCPEventType.STATUS);
        coreLogger = LoggerFactory.getLogger(com.fiorano.openesb.microservice.bundle.Activator.class);
    }

    public void setProcess(Process process){
        this.osProcess = process;
    }

    public boolean isRunning() {
        return isAlive();
    }

    public boolean isAlive() {
        try {
            if(osProcess==null){
                return false;
            }
            osProcess.exitValue();
            return false;
        } catch(IllegalThreadStateException e) {
            return true;
        }
    }
    public void stop() throws Exception {
        killComponent(true, false, "General");
        gracefulKill = true;
        isRunning=false;
    }

    public void kill() {
        if(osProcess!=null){
            osProcess.destroy();
        }
    }

    @Override
    public void setLogLevel(Map<String, String> modules) throws Exception {
        ccpCommandHelper.setLogLevel(modules);
    }

    @Override
    public LaunchConfiguration.LaunchMode getLaunchMode() {
        return LaunchConfiguration.LaunchMode.SEPARATE_PROCESS;
    }

    /**
     * Kills this component
     * should only be called from EventProcessHandle.
     *
     * @param isComponentConnOpen whether to clean component connections or not
     * @param reason              Reason for which component is being killed
     * @throws FioranoException If an exception occurs
     */
    public void killComponent(boolean isComponentConnOpen, boolean userAction, String reason) throws Exception {
        synchronized (this) {
            if (shutdownOfCCPComponentInProgress)
                return;
            shutdownOfCCPComponentInProgress = true;
        }
        if (isComponentConnOpen) {
            try {
                ccpCommandHelper.stopComponent(componentStopWaitTime);
            } catch (FioranoException e) {
                cleanupComponentResources(userAction,true,reason);
            }
            waitForCCPComponentDeath(true, userAction, reason);
        } else {
            cleanupComponentResources(userAction, true, reason);
        }
    }

    public void waitForCCPComponentDeath(boolean isComponentConnectionOpen, boolean userAction, String reason) throws FioranoException {
        try {
            if (confirmProcessExit()) {
                if (coreLogger != null)
                    coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOPPED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

                if (lifeCycleWorkflow != null && getLaunchMode() != LaunchConfiguration.LaunchMode.IN_MEMORY) {
                    ccpCommandHelper.unregisterListener(lifeCycleWorkflow, CCPEventType.STATUS);
                    lifeCycleWorkflow = null;
                }
                generateServiceUnboundEvent(reason, !userAction && reason != null && !(reason.equalsIgnoreCase(CoreConstants.APPLICATION_CLOSED_CONNECTION)));
                if (coreLogger != null)
                    coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_RESOURCE_CLEANUP, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                cleanupComponentResources(userAction, false, reason);
            } else {
                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

                Thread stopThread = null;
                try {
                    stopThread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                waitFor();
                            } catch (InterruptedException e) {
                                //Ignore this exception. After configured stop wait interval, this thread is intentionally interrupted for the purpose of cleanup.
                            } finally {
                                //On certain platforms, windows for example, interrupted status of the thread is not cleared when interrupted exception is thrown.
                                //We should clear this ourselves. Courtesy : http://kylecartmell.com/?p=9
                                //Though, even if we don't clear interrupted status, it won't effect our logic as we are not calling
                                //any other method on process object from this thread. Without this, the problems will occur only in cases where some method
                                //e.g. waitFor() or exitValue() is called on a process from a thread which has already been interrupted.
                                Thread.interrupted();
                            }
                        }
                    }, "Process wait thread for CCP enabled component " + getServiceInstName() + " in Event Process " + launchConfiguration.getApplicationName() + "version" + getAppVersion());   //NORBUtil

                    stopThread.setDaemon(true);
                    stopThread.start();
                    stopThread.join(componentStopWaitTime);
                } catch (InterruptedException e) {
                    coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT_THREAD_INTERRUPTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

                    if (coreLogger.isDebugEnabled())
                        coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT_THREAD_INTERRUPTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()), e);
                }

                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT_OVER, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                if (!confirmProcessExit()) {
                    //Interrupt the thread now as configured wait interval is over.
                    //This will wakeup the thread if it's waiting on process object to terminate.
                    stopThread.interrupt();

                    coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.START_FORCE_SHUTDOWN, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                    if (lifeCycleWorkflow != null && getLaunchMode() != LaunchConfiguration.LaunchMode.MANUAL) {
                        ccpCommandHelper.unregisterListener(lifeCycleWorkflow, CCPEventType.STATUS);
                        lifeCycleWorkflow = null;
                    }
                    cleanupComponentResources(userAction, true, reason);
                } else {
                    coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOPPED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

                    if (lifeCycleWorkflow != null && getLaunchMode() != LaunchConfiguration.LaunchMode.MANUAL) {
                        ccpCommandHelper.unregisterListener(lifeCycleWorkflow, CCPEventType.STATUS);
                        lifeCycleWorkflow = null;
                    }
                    generateServiceUnboundEvent(reason, !userAction && reason != null && !(reason.equalsIgnoreCase(CoreConstants.APPLICATION_CLOSED_CONNECTION)));
                    coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_RESOURCE_CLEANUP, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                    cleanupComponentResources(userAction, false, reason);
                }
            }
        } finally {
            synchronized (this) {
                shutdownOfCCPComponentInProgress = false;
            }
        }
    }

    private boolean confirmProcessExit() {
        try {
            if(osProcess==null){
                return true;
            }
            osProcess.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }
    }

    protected void waitFor() throws InterruptedException {
        osProcess.waitFor();
    }

    private void cleanupComponentResources(boolean userAction, boolean shutdownComponentProcess, String reason) throws FioranoException {
        synchronized (killSyncObject) {
            if (!isKilling)
                isKilling = true;
            else {
                coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_KILL_INPROGRESS, launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion(), getServiceInstName()));
                return;
            }
        }
        try {
            destroyComponent(shutdownComponentProcess, userAction, reason);
        } catch (FioranoException ex) {
            try {
                generateServiceKillFailedEvent(ExceptionUtil.getMessage(ex));
            } catch (FioranoException e) {
                coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.FAILED_TO_GENERATE_STOP_EVENT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()), e);
            }
            throw new FioranoException(Bundle.FAILED_TO_KILL_COMPONENT.toUpperCase(), ex, RBUtil.getMessage(Bundle.class,
                    Bundle.FAILED_TO_KILL_COMPONENT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
        } finally {
            synchronized (killSyncObject) {
                isKilling = false;
            }
            bServiceDestroyed = true;
        }
    }


    private void destroyComponent(boolean shutdownComponentProcess, boolean userAction, String reason) throws FioranoException {
        coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.DESTROY_PROCESS, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

        if (shutdownComponentProcess) {
            if (numberOfForceShutdownAttempts < 1)
                numberOfForceShutdownAttempts = 1;

            int count = 0;
            for (; count < numberOfForceShutdownAttempts; count++) {
                if (count == 0 || confirmProcessExit()) {
                    coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.FORCE_SHUTDOWN_ATTEMPT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion(), count + 1));

                    // Certain In-Memory components like Sender and other scheduler components which shutdown
                    // themselves after processing should not be shutdown in a separate thread as it leads to a dead-lock like situation (See Stacktrace in the Bug).
                    // The flag cleanConnections can be used to indicate this scenario as component connection is already closed by
                    // the component and thus cleanConnection boolean is set as false. In other cases, where component is shutdown
                    // by user action, cleanConnections boolean is set as true.
                    shutdown(componentStopWaitTime);

                    if (runStop(userAction, reason)) break;
                } else {
                    coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOPPED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                    generateServiceUnboundEvent(reason, !userAction && reason != null && !(reason.equalsIgnoreCase(CoreConstants.APPLICATION_CLOSED_CONNECTION)));
                    break;
                }
            }

            if (count == numberOfForceShutdownAttempts) {
                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_SHUTDOWN_FAILED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
            }
        }

        bServiceDestroyed = true;
        servStateDetails.setRunningVersion(getVersion());
        servStateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_DESTROYED);
        servStateDetails.setKillTime(System.currentTimeMillis());
    }

    //// TODO: 28-02-2016
    private void shutdown(long componentStopWaitTime) {
        if(osProcess!=null){
            osProcess.destroy();
        }
    }

    private boolean runStop(boolean userAction, String reason) throws FioranoException {
        Thread stopThread = null;
        try {
            stopThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        waitFor();
                    } catch (InterruptedException e) {
                        //Ignore this exception. After each configured retry interval, this thread is intentionally interrupted for the purpose of cleanup.
                    } finally {
                        //On certain platforms, windows for example, interrupted status of the thread is not cleared when interrupted exception is thrown.
                        //We should clear this ourselves. Courtesy : http://kylecartmell.com/?p=9
                        //Though, even if we don't clear interrupted status, it won't effect our logic as we are not calling
                        //any other method on process object from this thread. Without this, the problems will occur only in cases where some method
                        //e.g. waitFor() or exitValue() is called on a process from a thread which has already been interrupted.
                        Thread.interrupted();
                    }
                }
            }, "Process wait thread for component " + getServiceInstName() + " in Event Process " + launchConfiguration.getApplicationName() + "version" + getAppVersion());   //NORBUtil

            stopThread.setDaemon(true);
            stopThread.start();
            long retryIntervalBetweenForceShutdownAttempts = 5;
            stopThread.join(retryIntervalBetweenForceShutdownAttempts);
        } catch (InterruptedException e) {
            coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT_THREAD_INTERRUPTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

            if (coreLogger.isDebugEnabled())
                coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_WAIT_THREAD_INTERRUPTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()), e);
        }

        if (!confirmProcessExit()) {
            //Interrupt the thread now as configured wait interval is over.
            //This will wakeup the thread if it's waiting on process object to terminate.
            stopThread.interrupt();
        } else {
            coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOPPED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
            generateServiceUnboundEvent(reason, !userAction && reason != null && !(reason.equalsIgnoreCase(CoreConstants.APPLICATION_CLOSED_CONNECTION)));
            return true;
        }
        return false;
    }

    public class ComponentLifeCycleWorkflow extends ComponentWorkflowListener {
        private ComponentLifeCycleWorkflow(String componentInstanceName, String applicationName, String applicationVersion) {
            super(componentInstanceName, applicationName, applicationVersion);
        }

        public void onEvent(ComponentCCPEvent event) {
            try {
                if (event.getComponentId().equalsIgnoreCase(LookUpUtil.getServiceInstanceLookupName(launchConfiguration.getApplicationName(), getAppVersion(), getServiceInstName()))) {
                    StatusEvent status = (StatusEvent) event.getControlEvent();
                    if (status.getOperationScope() == StatusEvent.OperationScope.COMPONENT_STOP) {
                        if (status.getStatusType() == StatusEvent.StatusType.ERROR) {
                            coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_STOPPING_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            generateServiceKillFailedEvent(status.getErrorMessage());
                        } else if (status.getStatusType() == StatusEvent.StatusType.WARNING)
                            coreLogger.warn(RBUtil.getMessage(Bundle.class, Bundle.WARN_STOPPING_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                        else if (status.getStatusType() == StatusEvent.StatusType.INFORMATION) {
                            coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.STOP_COMPONENT_UPDATE, status.getStatus().toString(), getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            if (status.getStatus() == StatusEvent.Status.COMPONENT_STOPPED) {
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOPPED_EVENT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));

                                /**
                                 * Unbound event should not be raised at this point. At this point, component has not yet terminated it's JMS connection to Peer Server. We can handle this case by waiting for the process
                                 * object to die using com.fiorano.peer.launch.process.runtime.IProcess.waitFor() API. Thus, unbound event is raised when component process has actually terminated.
                                 */
                                //Un-register workflow listener now for this component.
                                if(!shutdownOfCCPComponentInProgress){
                                    waitForCCPComponentDeath(true, false, "component stopped");
                                }
                                if (lifeCycleWorkflow != null && getLaunchMode() != LaunchConfiguration.LaunchMode.MANUAL) {
                                    ccpCommandHelper.unregisterListener(lifeCycleWorkflow, CCPEventType.STATUS);
                                    lifeCycleWorkflow = null;
                                }
                            } else if (status.getStatus() == StatusEvent.Status.COMPONENT_STOPPING) {
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STOP_TIME_WAIT, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            } else if (status.getStatus() == StatusEvent.Status.COMPONENT_DISCONNECTING) {
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_DISCONNECTING, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            }
                        }
                    } else if (status.getOperationScope() == StatusEvent.OperationScope.COMPONENT_LAUNCH) {
                        if (status.getStatusType() == StatusEvent.StatusType.ERROR) {
                            coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_LAUNCH_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            generateServiceFailedToLaunchEvent(status.getErrorMessage());
                        } else if (status.getStatusType() == StatusEvent.StatusType.WARNING)
                            coreLogger.warn(RBUtil.getMessage(Bundle.class, Bundle.WARNING_LAUNCH_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                        else if (status.getStatusType() == StatusEvent.StatusType.INFORMATION) {
                            coreLogger.debug(RBUtil.getMessage(Bundle.class, Bundle.LAUNCH_PROCESS_UPDATE, status.getStatus().toString(), getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                            if (status.getStatus() == StatusEvent.Status.COMPONENT_STARTED) {
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_STARTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                                generateServiceBoundEvent();
                            } else if (status.getStatus() == StatusEvent.Status.COMPONENT_CONNECTED)
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_CONNECTED, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                        }
                    } else if (status.getOperationScope() == StatusEvent.OperationScope.COMPONENT_RUNNING) {
                        if (status.getStatusType() == StatusEvent.StatusType.INFORMATION) {
                            if (status.getStatus() == StatusEvent.Status.COMPONENT_LAUNCHING) {
                                coreLogger.info(RBUtil.getMessage(Bundle.class, Bundle.COMPONENT_LAUNCHING, getServiceInstName(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                                generateServiceBoundingEvent();
                            } else if (status.getStatusType() == StatusEvent.StatusType.ERROR) {
                                coreLogger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_LAUNCH_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                                generateServiceFailedToLaunchEvent(status.getErrorMessage());
                            } else if (status.getStatusType() == StatusEvent.StatusType.WARNING)
                                coreLogger.warn(RBUtil.getMessage(Bundle.class, Bundle.WARNING_LAUNCH_COMPONENT, getServiceInstName(), status.getStatus().toString(), status.getErrorMessage(), launchConfiguration.getApplicationName() + CoreConstants.APP_VERSION_DELIM + getAppVersion()));
                        }
                    }
                }
            } catch (FioranoException e) {
                coreLogger.error(e.getMessage());
            }
        }
    }

    public MemoryUsage getMemoryUsage() {
        try {
            return ccpCommandHelper.getMemoryUsage();
        } catch (FioranoException e) {
            coreLogger.error(e.getMessage());
        }
        return null;
    }

    public ComponentStats getComponentStats() throws FioranoException {
        return ccpCommandHelper.getComponentStats();
    }

}
