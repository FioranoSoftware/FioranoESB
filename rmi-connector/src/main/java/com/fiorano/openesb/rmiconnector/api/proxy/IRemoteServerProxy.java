/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api.proxy;

import com.fiorano.openesb.rmiconnector.api.*;
import com.fiorano.openesb.rmiconnector.api.ServiceException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IRemoteServerProxy extends Remote {

    public Object invoke (String methodName,Object []methodArgs, HashMap additionalInfo) throws RemoteException, ServiceException;

}
