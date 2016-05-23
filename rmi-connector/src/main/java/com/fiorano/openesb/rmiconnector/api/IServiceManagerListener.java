/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServiceManagerListener extends Remote {

    /**
     * This notification is sent to the client when a Service Instance is deleted from the server
     *
     * @param serviceInstanceName The name of the Service Instance deleted
     * @param serviceVersion      The version of the Service Instance deleted
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceDeleted(String serviceInstanceName, float serviceVersion) throws RemoteException;

    /**
     * This notification is sent to the client when a Service Instance is deployed in the server
     *
     * @param serviceInstanceName The name of the Service Instance deployed
     * @param serviceVersion      The version of the Service Instance deployed
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void serviceDeployed(String serviceInstanceName, float serviceVersion) throws RemoteException;

}
