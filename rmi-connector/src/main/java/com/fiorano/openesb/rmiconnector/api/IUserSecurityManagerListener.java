/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserSecurityManagerListener extends Remote {

    /**
     * This method sends a notification to the client when a user password is changed
     *
     * @param userName The username for which password has been changed.
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void changePassword(String userName) throws RemoteException;


}
