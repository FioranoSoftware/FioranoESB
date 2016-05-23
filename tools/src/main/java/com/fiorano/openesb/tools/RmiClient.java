/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.rmiconnector.api.IApplicationManager;
import com.fiorano.openesb.rmiconnector.api.IRmiManager;
import com.fiorano.openesb.rmiconnector.api.IServiceManager;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.rmiconnector.client.FioranoRMIClientSocketFactory;
import com.fiorano.openesb.rmiconnector.impl.MicroServiceManager;
import com.fiorano.openesb.rmiconnector.impl.RmiManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {
    IRmiManager rmiManager;
    IApplicationManager applicationManager;
    IServiceManager microServiceManager;
    public RmiClient(RmiLoginInfo loginInfo) throws RemoteException, NotBoundException, ServiceException {
        FioranoRMIClientSocketFactory csf = new FioranoRMIClientSocketFactory();
        Registry registry = LocateRegistry.getRegistry(loginInfo.hostname, loginInfo.port, csf);
        rmiManager = (IRmiManager) registry.lookup("rmi");
        String handleId = rmiManager.login(loginInfo.user, loginInfo.pwd);
        applicationManager = rmiManager.getApplicationManager(handleId);
        microServiceManager = rmiManager.getServiceManager(handleId);
    }

    public IRmiManager getRmiManager() {
        return rmiManager;
    }

    public IApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public IServiceManager getMicroServiceManager() {
        return microServiceManager;
    }
}
