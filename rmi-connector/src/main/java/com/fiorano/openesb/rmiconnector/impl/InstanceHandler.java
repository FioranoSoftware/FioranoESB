/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.rmiconnector.api.*;
import com.fiorano.openesb.rmiconnector.api.proxy.RemoteClientInterceptor;
import com.fiorano.openesb.security.*;
import com.fiorano.openesb.utils.Constants;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;

public class InstanceHandler {
    //Rmi Manager Instance
    private RmiManager rmiManager;
    //Handle ID of the client
    private String handleID;

    private String context;
    //Keeps track if user is logged out, else it will force logout after cleanup of resources.
    private volatile boolean isLoggedOut = false;
    //ApplicationManager
    private volatile ApplicationManager applicationManager;

    private volatile MicroServiceManager microServiceManager;
    private volatile BreakPointManager breakPointManager;
    private volatile ConfigurationManager namedConfigManager;
    private volatile IUserSecurityManager userSecurityManager;
    //schema reference manager
    private volatile SchemaReferenceManager schemaReferenceManager;
    //Service Provider manager
    private volatile ServiceProviderManager spManager;


    public InstanceHandler(RmiManager rmiManager, String handleID) {
        this(rmiManager, handleID,"ESB");
    }

    public InstanceHandler(RmiManager rmiManager, String handleID, String context) {
        this.rmiManager = rmiManager;
        this.handleID = handleID;
        this.context = context;
    }

    public synchronized IApplicationManager getApplicationManager() throws RemoteException {
        if (applicationManager == null) {
            //original resource == server side stub
            applicationManager = new ApplicationManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(applicationManager,rmiManager.getRmiServerPort(),rmiManager.getCsf(),rmiManager.getSsf());

            //client proxy instance
            IApplicationManager returnObject = (IApplicationManager) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{IApplicationManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            applicationManager.setClientProxyInstance(returnObject);
        }
        return applicationManager.getClientProxyInstance();
    }

    public synchronized IServiceManager getMicroServiceManager() throws RemoteException {
        if (microServiceManager == null) {
            //original resource == server side stub
            microServiceManager = new MicroServiceManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(microServiceManager,rmiManager.getRmiServerPort(),rmiManager.getCsf(),rmiManager.getSsf());

            //client proxy instance
            IServiceManager returnObject = (IServiceManager) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{IServiceManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            microServiceManager.setClientProxyInstance(returnObject);
        }
        return microServiceManager.getClientProxyInstance();
    }

    public synchronized IDebugger getBreakpointManager() throws RemoteException {
        if (breakPointManager == null) {
            //original resource == server side stub
            breakPointManager = new BreakPointManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(breakPointManager,rmiManager.getRmiServerPort(),rmiManager.getCsf(),rmiManager.getSsf());

            //client proxy instance
            IDebugger returnObject = (IDebugger) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{IDebugger.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            breakPointManager.setClientProxyInstance(returnObject);
        }
        return breakPointManager.getClientProxyInstance();
    }

    public synchronized IConfigurationManager getConfigurationManager() throws RemoteException {
        if (namedConfigManager == null) {
            //original resource == server side stub
            namedConfigManager = new ConfigurationManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(namedConfigManager,rmiManager.getRmiServerPort(),rmiManager.getCsf(),rmiManager.getSsf());

            //client proxy instance
            IConfigurationManager returnObject = (IConfigurationManager) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{IConfigurationManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            namedConfigManager.setClientProxyInstance(returnObject);
        }
        return namedConfigManager.getClientProxyInstance();
    }

    public synchronized IServiceProviderManager getServiceProviderManager() throws RemoteException {

        if (spManager == null)
        {
            //original resource == server side stub
            spManager = new ServiceProviderManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(spManager,rmiManager.getRmiServerPort(),rmiManager.getCsf(),rmiManager.getSsf());

            //client proxy instance
            IServiceProviderManager returnObject = (IServiceProviderManager)Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[] {IServiceProviderManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            spManager.setClientProxyInstance(returnObject);
        }
        return spManager.getClientProxyInstance();
    }

    public synchronized IUserSecurityManager getuserSecurityManager() throws RemoteException {
        if (userSecurityManager == null) {
            userSecurityManager = new UserSecurityManager(rmiManager, this, context);

            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy((IDistributedRemoteObject) userSecurityManager, rmiManager.getRmiServerPort(), rmiManager.getCsf(), rmiManager.getSsf());

            //client proxy instance
            IUserSecurityManager returnObject = (IUserSecurityManager) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{IUserSecurityManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            userSecurityManager.setClientProxyInstance(returnObject);
            return returnObject;
        }
        return userSecurityManager.getClientProxyInstance();
    }

    public synchronized ISchemaReferenceManager getSchemaReferenceManager() throws RemoteException{
        if(schemaReferenceManager == null){
            //original resourece == server side stub
            schemaReferenceManager = new SchemaReferenceManager(rmiManager, this);
            //server side proxy instance to original resource.
            RemoteServerProxy serverSideProxy = new RemoteServerProxy(schemaReferenceManager, rmiManager.getRmiServerPort(), rmiManager.getCsf(), rmiManager.getSsf());

            //client proxy instance
            ISchemaReferenceManager returnObject = (ISchemaReferenceManager) Proxy.newProxyInstance
                    (
                            this.getClass().getClassLoader(),
                            new Class[]{ISchemaReferenceManager.class, Serializable.class},
                            //client stub & interceptor instance
                            new RemoteClientInterceptor(serverSideProxy)
                    );
            schemaReferenceManager.setClientProxyInstance(returnObject);
        }

        return schemaReferenceManager.getClientProxyInstance();

    }

    public void removeHandler() {
        isLoggedOut = true;
        if (applicationManager != null) {
            onUnReferenced(Constants.APPLICATION_MANAGER);
        }
        if (microServiceManager != null) {
            onUnReferenced(Constants.MICRO_SERVICE_MANAGER);
        }
        if (spManager != null) {
            onUnReferenced(Constants.SERVICE_PROVIDER_MANAGER);
        }
        if (breakPointManager != null){
            onUnReferenced(Constants.BREAKPOINT_MANAGER);
        }
        if(namedConfigManager != null){
            onUnReferenced(Constants.CONFIGURATION_MANAGER);
        }
        if (userSecurityManager != null) {
            onUnReferenced(Constants.USER_SECURITY_MANAGER);
        }
        if(schemaReferenceManager != null){
            onUnReferenced(Constants.SCHEMA_REFERENCE_MANAGER);
        }
    }

    /**
     * onUnreferenced will be called from the unreferenced method of Remote Object
     * @param e String
     */
    public synchronized void onUnReferenced(String e) {
        if (e.equals(Constants.APPLICATION_MANAGER)) {
            applicationManager = null;
            rmiManager.unRegisterApplicationRepoEventListeners(handleID);
            rmiManager.unRegisterAllApplicationListeners(handleID);
            canlogoutForceFully();
        } else if (e.equals(Constants.SERVICE_MANAGER)) {
            microServiceManager = null;
            rmiManager.unRegisterServiceRepoEventListener(handleID);
            canlogoutForceFully();
        } else if (e.equals(Constants.SERVICE_PROVIDER_MANAGER)) {
            spManager = null;
            canlogoutForceFully();
        } else if (e.equals(Constants.BREAKPOINT_MANAGER)) {
            breakPointManager = null;
            canlogoutForceFully();
        } else if (e.equals(Constants.CONFIGURATION_MANAGER)) {
            namedConfigManager = null;
            rmiManager.unRegisterConfigurationRepositoryListener(handleID);
            canlogoutForceFully();
        } else if (e.equals(Constants.USER_SECURITY_MANAGER)) {
            userSecurityManager = null;
            canlogoutForceFully();
        } else if(e.equals(Constants.SCHEMA_REFERENCE_MANAGER)){
            schemaReferenceManager = null;
            canlogoutForceFully();
        }
    }

    private void canlogoutForceFully() {
        if (applicationManager == null && microServiceManager == null && spManager == null && breakPointManager ==null && namedConfigManager == null && userSecurityManager == null && !isLoggedOut) {
            try {
                ConnectionHandle handle=rmiManager.getSecurityManager().getConnectionHandle(handleID);
                if(null == handle)
                    return;
              /*  if(!rmiManager.isStarted())//when we Unexport rmiManager.the unferenced method will be called. so we shud force fully logout user with reason that server no longer active.
                    rmiLogger.info(Bundle.class, Bundle.FORCE_LOGOUT1,handle.getUserName());
                else
                    rmiLogger.info(Bundle.class, Bundle.FORCE_LOGOUT,handle.getUserName());*/
                rmiManager.logout(handleID);
            } catch (RemoteException willNeverHappen) {
            } catch (ServiceException ignore) {
            }
        }
    }

    String getHandleID() {
        return handleID;
    }
}

