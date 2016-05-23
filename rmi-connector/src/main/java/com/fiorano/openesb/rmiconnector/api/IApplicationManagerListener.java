/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.rmiconnector.impl.ApplicationManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IApplicationManagerListener extends Remote {

    /**
     * This method sends a notification to the client when an Event Process is deleted from the server
     *
     * @param appVersion The version of the Event Process deleted
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @deprecated Use {@link IRepositoryEventListener} to register for repository Updation events : applicationDeleted, applicationDeployed.
     */
    public void applicationDeleted(float appVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when an Event Process is deployed(saved) in the server (i.e. any changes to the event process done and hence saved).
     *
     * @param appVersion The version of the Event Process deployed
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @deprecated Use {@link IRepositoryEventListener} to register for repository Update events : applicationDeleted, applicationDeployed.
     */
    public void applicationDeployed(float appVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when a Service Instance is launched in the server
     *
     * @param serviceInstanceName The name of the Service Instance started
     * @param serviceVersion      The version of the Service Instance started
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceInstanceStarted(String serviceInstanceName, float serviceVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when a Service Instance is in Starting state.
     *
     * @param serviceInstanceName The name of the Service Instance started
     * @param serviceVersion      The version of the Service Instance started
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceInstanceStarting(String serviceInstanceName, float serviceVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when a Service Instance is stopped in the server
     *
     * @param serviceInstanceName The name of the Service Instance stopped
     * @param serviceVersion      The version of the Service Instance stopped
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceInstanceStopped(String serviceInstanceName, float serviceVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when a breakpoint is added to a route
     *
     * @param routeGUID route GUID
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void routeBreakPointAdded(String routeGUID) throws RemoteException;

    /**
     * This method sends a notification to the client when a breakpoint is removed from a route
     *
     * @param routeGUID route GUID
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void routeBreakPointRemoved(String routeGUID) throws RemoteException;

    /**
     * This method sends a notification to the client when an Event Process has started up successfully in the server
     *
     * @param appVersion The version of the Event Process launched
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void applicationStarted(float appVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when an Event Process is starting up in the server.
     *
     * @param appVersion The version of the Event Process launched
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void applicationStarting(float appVersion) throws RemoteException;

    /**
     * This method sends a notification to the client when an Event Process is stopped in the server
     *
     * @param appVersion The version of the Event Process stopped
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void applicationStopped(float appVersion) throws RemoteException;
}
