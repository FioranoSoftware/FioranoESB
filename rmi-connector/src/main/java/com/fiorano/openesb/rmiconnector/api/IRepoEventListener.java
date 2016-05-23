/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRepoEventListener extends Remote {
    /**
     * This notification is sent to the client when a Service Instance is deleted from the server
     *
     * @param serviceGUID    The name of the Service Instance deleted
     * @param serviceVersion The version of the Service Instance deleted
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceDeleted(String serviceGUID, float serviceVersion) throws RemoteException;

    /**
     * This notification is sent to the client when a Service Instance is deployed in the server
     *
     * @param serviceGUID    The name of the Service Instance deployed
     * @param serviceVersion The version of the Service Instance deployed
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceDeployed(String serviceGUID, float serviceVersion) throws RemoteException;

    /**
     * This notification is sent to the client when an Event Process is deleted from the server
     *
     * @param appGUID    The Application GUID of the Event Process
     * @param appVersion The version of the Event Process deleted
     * @param handleId   Id of the deleting Client
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call     *
     */
    public void applicationDeleted(String appGUID, float appVersion, String handleId) throws RemoteException;

    /**
     * This notification is sent to the client when an Event Process is deployed(saved) in the server (i.e. any changes to the event process done and hence saved).
     *
     * @param appGUID    The Application GUID of the Event Process
     * @param appVersion The version of the Event Process deployed
     * @param handleId   Id of the deploying Client
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call     *
     */
    public void applicationDeployed(String appGUID, float appVersion, String handleId) throws RemoteException;
     /**
     * This notification is sent to the client when a resource of a service is deployed(saved) to the server.
     * @param serviceGUID The name of the Service Instance
     * @param resourceName The name of resource deployed
     * @param serviceVersion The version of the service whose resource is created
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void resourceDeployed(String resourceName,String serviceGUID, float serviceVersion) throws RemoteException;
    /**
     * This notification is sent to the client when a resource of a service is removed from the server.
     * @param serviceGUID The name of the Service Instance
     * @param resourceName The name of resource deleted
     * @param serviceVersion The version of the service whose resource is created
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void resourceDeleted(String resourceName,String serviceGUID, float serviceVersion) throws RemoteException;

    /**
     * This notification is sent to the client when the serviceDescriptor.xml of a Service is modified in the server
     *
     * @param serviceGUID    The name of the Service Instance whose descriptor is modified
     * @param serviceVersion The version of the Service whose descriptor is modified
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void descriptorModified(String serviceGUID, float serviceVersion) throws RemoteException;
}
