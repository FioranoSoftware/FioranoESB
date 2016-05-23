/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.application.configuration.data.NamedObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IConfigurationRepositoryListener extends Remote {

    /**
     * This notification is sent to the client whenever a configuration is persisted into configuration repository
     * @param namedObject An object containing essential parameters which define the configuration persisted
     */
    public void configurationPersisted(NamedObject namedObject) throws RemoteException;

    /**
     * This notification is sent to the client whenever a configuration is deleted from configuration repository
     * @param namedObject An object containing essential parameters which define the configuration deleted
     */
    public void configurationDeleted(NamedObject namedObject) throws RemoteException;
}
