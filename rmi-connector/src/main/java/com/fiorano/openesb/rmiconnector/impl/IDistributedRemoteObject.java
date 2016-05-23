/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.rmiconnector.api.ServiceException;

import java.rmi.RemoteException;
import java.util.HashMap;

public interface IDistributedRemoteObject {

    /**
     * The method is invoked to get a service.
     * @param methodName method to invoke.
     * @param args arguments provided to the method
     * @param additionalInfo additional info like Client Locale, Client IP Addresses etc. Serves as a place holder.
     * @return Object service to be provided.
     * @throws RemoteException
     * @throws ServiceException
     */
    Object invoke(String methodName, Object[] args, HashMap additionalInfo) throws RemoteException, ServiceException;

    /**
     * Gets called when the object is no longer being referenced.
     * @see java.rmi.server.Unreferenced
     */
    void unreferenced();
}
