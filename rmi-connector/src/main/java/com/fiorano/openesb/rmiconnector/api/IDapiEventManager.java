/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.util.Hashtable;

public interface IDapiEventManager {


    /**
     * Registers FES Event Manager to listen for FES events
     */
    public void startEventListener();

    /**
     * Sets client ip addresses for handle ids
     *
     * @param clientIPAddress handle id Vs. client ip address
     */
    public void setClientIPAddresses(Hashtable<String, String> clientIPAddress);

    /**
     * Registers specified Event Listener for application events of specified appGUID
     *
     * @param eventListener application Event listener
     * @param appGUID       application GUID
     * @param handleId      handle id of client
     */
    public void registerApplicationEventListener(IApplicationManagerListener eventListener, String appGUID, float appVersion, String handleId);

    /**
     * Unregisters specified event Listener for specified appGUID
     *
     * @param eventListener application events listener
     * @param appGUID       application GUID
     * @param handleId      handle id of client
     */
    public void unRegisterApplicationEventListener(IApplicationManagerListener eventListener, String appGUID, float appVersion, String handleId);

    /**
     * Registers specified Event Listener for security events of specified username
     *
     * @param securityListener security Event listener
     * @param userName       user name
     * @param handleId      handle id of client
     */
    public void registerSecurityEventListener(IUserSecurityManagerListener securityListener, String userName, String handleId);

    /**
     * Unregisters specified event Listener for security events of specified username
     *
     * @param securityListener security events listener
     * @param userName       user name
     * @param handleId      handle id of client
     */
    public void unRegisterSecurityEventListener(IUserSecurityManagerListener securityListener, String userName, String handleId);

    /**
     * Registers specified Event Listener for service Repository Events
     *
     * @param eventListener service Event listener
     * @param handleId      handle id of client
     */
    public void registerServiceRepoEventListener(IRepoEventListener eventListener, String handleId);

    /**
     * Unregisters specified event listener for specified service GUID
     *
     * @param handleId handle id of client
     */
    public void unRegisterServiceRepoEventListener(String handleId);

    /**
     * Registers the specified server state event Listener with the Fiorano Enterprise Server(fes).
     *
     * @param eventListener server state listener
     * @param handleId      handle Id of client
     */
    public void registerServerStateListener(IServerStateListener eventListener, String handleId);

    /**
     * Unregisters the specified server state event Listener from the Fiorano Enterprise Server(fes).
     *
     * @param handleId handle id of client
     */
    public void unRegisterServerStateListener(String handleId);

    /**
     * Unregisters FES Event Manager to stop listening FES events
     */
    void stopEventListener();

     /**
     * Registers specified Event Listener for Application Repository Events
     *
     * @param eventListener service Event listener
     * @param handleId      handle id of client
     */
    public void registerApplicationRepoEventListener(IRepoEventListener eventListener, String handleId);

    /**
     * UnRegisters specified Event Listener for Application Repository Events
     * @param handleId  handle id of client
     */
    public void unRegisterApplicationRepoEventListener(String handleId);

    /**
     * Registers specified Event Listener for Configuration Repository Events
     *
     * @param configurationRepositoryListener service Event listener
     * @param handleId handle id of client
     */
    public void registerConfigurationRepositoryEventListener(IConfigurationRepositoryListener configurationRepositoryListener, String handleId);

    /**
     * UnRegisters specified Event Listener for Configuration Repository Events
     *
     * @param handleId handle id of client
     */
    public void unRegisterConfigurationRepositoryEventListener(String handleId);

    /**
     * Unregisters all event process listeners for specified handleID
     *
     * @param handleID handle id of client
     */
    public void unregisterAllApplicationListeners(String handleID);

    /**
     * Unregisters all Old listeners for specified handleID
     *
     * @param handleID handle id of client
     */
    public void unRegisterOldListeners(String handleID);
}
