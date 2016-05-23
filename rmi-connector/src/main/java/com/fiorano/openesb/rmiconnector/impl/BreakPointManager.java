/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.applicationcontroller.ApplicationHandle;
import com.fiorano.openesb.application.BreakpointMetaData;
import com.fiorano.openesb.rmiconnector.Activator;
import com.fiorano.openesb.rmiconnector.api.IDebugger;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

public class BreakPointManager extends AbstractRmiManager implements IDebugger {
    private Logger logger = LoggerFactory.getLogger(Activator.class);
    private ApplicationController applicationController;

    private InstanceHandler handler;

    protected BreakPointManager(RmiManager rmiManager, InstanceHandler handler) {
        super(rmiManager);
        this.applicationController = rmiManager.getApplicationController();
        this.handler = handler;
        setHandleID(handler.getHandleID());
    }

    @Override
    public BreakpointMetaData addBreakpoint(String appGUID, float appVersion, String routeName) throws RemoteException, ServiceException {
        ApplicationHandle appHandle = applicationController.getApplicationHandle(appGUID, appVersion, handleId);
        if(appHandle==null){
            throw new ServiceException("application not running");
        }
        try {
            return appHandle.addBreakPoint(routeName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public BreakpointMetaData getBreakpointMetaData(String appGUID, float appVersion, String routeName) throws RemoteException, ServiceException {
        ApplicationHandle appHandle = applicationController.getApplicationHandle(appGUID, appVersion, handleId);
        if(appHandle==null){
            throw new ServiceException("application not running");
        }
        try {
            return appHandle.getBreakpointMetaData(routeName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String[] getRoutesWithDebugger(String appGUID, float appVersion) throws RemoteException, ServiceException {
        ApplicationHandle appHandle = applicationController.getApplicationHandle(appGUID, appVersion, handleId);
        if(appHandle==null){
            throw new ServiceException("application not running");
        }
        try {
            return appHandle.getRoutesWithDebugger();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void removeBreakpoint(String appGUID, float appVersion, String routeName) throws RemoteException, ServiceException {
        ApplicationHandle appHandle = applicationController.getApplicationHandle(appGUID, appVersion, handleId);
        if(appHandle==null){
            throw new ServiceException("application not running");
        }
        try {
            appHandle.removeBreakPoint(routeName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void removeAllBreakpoints(String appGUID, float appVersion) throws RemoteException, ServiceException {
        ApplicationHandle appHandle = applicationController.getApplicationHandle(appGUID, appVersion, handleId);
        if(appHandle==null){
            throw new ServiceException("application not running");
        }
        try {
            appHandle.removeAllBreakpoints();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void messageModifiedonDebugger(String appGUID, float version, String routeGUID, String messageID) throws RemoteException, ServiceException {
        logger.info("message with ID " + messageID+" is modified in the breakpoint present on route " +routeGUID + " of application " +appGUID+":"+version);
    }

    @Override
    public void messageDeletedonDebugger(String appGUID, float version, String routeGUID, String messageID) throws RemoteException, ServiceException {
        logger.info("message with ID " + messageID+" is deleted in the breakpoint present on route " +routeGUID + " of application " +appGUID+":"+version);
    }

    @Override
    public void pauseRoute(String appGUID, float version, String routeGUID, String handleId, boolean maintainSequence, long pauseTime) throws RemoteException, ServiceException {

    }
    private IDebugger clientProxyInstance;

    void setClientProxyInstance(IDebugger clientProxyInstance) {
        this.clientProxyInstance = clientProxyInstance;
    }

    IDebugger getClientProxyInstance() {
        return clientProxyInstance;
    }
}
