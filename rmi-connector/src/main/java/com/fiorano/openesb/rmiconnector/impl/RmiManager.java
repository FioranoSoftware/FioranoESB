/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.service.Schema;
import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.events.EventsManager;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.namedconfig.NamedConfigRepository;
import com.fiorano.openesb.rmiconnector.Activator;
import com.fiorano.openesb.rmiconnector.connector.RmiConnector;
import com.fiorano.openesb.schemarepo.SchemaRepository;
import com.fiorano.openesb.security.ConnectionHandle;
import com.fiorano.openesb.security.SecurityManager;
import com.fiorano.openesb.rmiconnector.api.*;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import org.osgi.framework.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.util.*;

public class RmiManager implements IRmiManager{

    Map<String, InstanceHandler> handlerMap = new HashMap<String, InstanceHandler>();
    private ApplicationController applicationController;
    private ApplicationRepository applicationRepository;
    private EventsManager eventsManager;
    private SecurityManager securityManager;
    private MicroServiceRepoManager microServiceRepoManager;
    private NamedConfigRepository namedConfigRepository;
    private SchemaRepository schemaRepository;
    private RMIServerSocketFactory ssf;
    private RMIClientSocketFactory csf;
    private int rmiServerPort;
    private DapiEventManager dapiEventManager;
    private ServerManager serverManager;
    private ServerLogManager logManager;
    private List ipAliases;
    private RmiConnector rmiConnector;
    private Logger logger = LoggerFactory.getLogger(Activator.class);

    public RmiManager(BundleContext context, RmiConnector rmiConnector) throws RemoteException {
        logger.info("Initializing Rmi Manager");
        org.osgi.framework.ServiceReference[] references = new org.osgi.framework.ServiceReference[0];
        try {
            references = context.getServiceReferences(ApplicationController.class.getName(),null);
            applicationController = (ApplicationController) context.getService(references[0]);
            references = context.getServiceReferences(ApplicationRepository.class.getName(),null);
            applicationRepository = (ApplicationRepository) context.getService(references[0]);
            references = context.getServiceReferences(EventsManager.class.getName(), null);
            eventsManager = (EventsManager) context.getService(references[0]);
            references = context.getServiceReferences(SecurityManager.class.getName(), null);
            securityManager = (SecurityManager) context.getService(references[0]);
            references = context.getServiceReferences(MicroServiceRepoManager.class.getName(), null);
            microServiceRepoManager = (MicroServiceRepoManager) context.getService(references[0]);
            references = context.getServiceReferences(NamedConfigRepository.class.getName(), null);
            namedConfigRepository = (NamedConfigRepository) context.getService(references[0]);
            schemaRepository = context.getService(context.getServiceReference(SchemaRepository.class));
            serverManager = ServerManager.GETINSTANCE(this);
            logManager = new ServerLogManager();
            dapiEventManager = new DapiEventManager(eventsManager, namedConfigRepository);
            dapiEventManager.startEventListener();

        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
        this.rmiConnector = rmiConnector;
        logger.info("Initialized Rmi Manager");
    }
    public RMIServerSocketFactory getSsf() {
        return rmiConnector.getSsf();
    }

    public RMIClientSocketFactory getCsf() {
        return rmiConnector.getCsf();
    }

    public int getRmiServerPort(){
        return rmiConnector.getRmiConnectorConfig().getRmiServerPort();
    }

    public int getRmiRegistryPort(){
        return rmiConnector.getRmiConnectorConfig().getRmiRegistryPort();
    }

    public DapiEventManager getDapiEventsManager() {
        return dapiEventManager;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public ApplicationRepository getApplicationRepository() {
        return applicationRepository;
    }

    public NamedConfigRepository getNamedConfigRepository(){
        return namedConfigRepository;
    }

    public SchemaRepository getSchemaRepository(){
        return schemaRepository;
    }

    public EventsManager getEventsManager() {
        return eventsManager;
    }

    public void setEventsManager(EventsManager eventsManager) {
        this.eventsManager = eventsManager;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public MicroServiceRepoManager getMicroServiceRepoManager() {
        return microServiceRepoManager;
    }

    public void setMicroServiceRepoManager(MicroServiceRepoManager microServiceRepoManager) {
        this.microServiceRepoManager = microServiceRepoManager;
    }

    public void startDapiEventListener() {
        dapiEventManager = new DapiEventManager( eventsManager, namedConfigRepository);
        dapiEventManager.startEventListener();
    }

    public void stopDapiEventListener() {
        if (dapiEventManager != null)
            dapiEventManager.stopEventListener();
    }


    public String login(String userName, String password) throws RemoteException, ServiceException {
        return login(userName, password, "unknown");
    }


    public String login(String userName, String password, String agent) throws RemoteException, ServiceException {
        String handleId = null;
        try {
            logger.info("User " +userName+" trying to login to the server");
            handleId = securityManager.login(userName, password);
            logger.info("User " + userName + " logged in successfully");
            String clientIP = "UNKNOWN";
            try {
                clientIP = RemoteServer.getClientHost();
            } catch (ServerNotActiveException e) {
                //ignore.we will never get it.we will allways be in a remote call.
            }
            securityManager.addConnectionHandle(handleId, new ConnectionHandle(handleId, userName, password, agent, clientIP));
        } catch (LoginException e) {
            throw new ServiceException(e.getMessage());
        }

        if (!handlerMap.containsKey(handleId)) {
            InstanceHandler instanceHandler = new InstanceHandler(this, handleId, agent);
            handlerMap.put(handleId, instanceHandler);
        }
        return handleId;
    }

    public IApplicationManager getApplicationManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getApplicationManager();
    }

    public IServiceManager getServiceManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getMicroServiceManager();
    }

    public IFPSManager getFPSManager(String handleID) throws RemoteException, ServiceException {
        return null;
    }


    public IServiceProviderManager getServiceProviderManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getServiceProviderManager();
    }


    public IDebugger getBreakpointManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getBreakpointManager();
    }


    public IConfigurationManager getConfigurationManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getConfigurationManager();
    }


    public IUserSecurityManager getUserSecurityManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getuserSecurityManager();
    }


    public IKeyStoreManager getKeyStoreManager(String handleID) throws RemoteException, ServiceException {
        return null;
    }


    public ISchemaReferenceManager getSchemaReferenceManager(String handleID) throws RemoteException, ServiceException {
        return handlerMap.get(handleID).getSchemaReferenceManager();
    }


    public IDocTrackManager getDocTrackManager(String handleID) throws RemoteException, ServiceException {
        return null;
    }


    public void logout(String handleId) throws RemoteException, ServiceException {
        if(handleId==null){
            throw new ServiceException("handle ID should not be null");
        }
        InstanceHandler instanceHandler = handlerMap.get(handleId);
        if(instanceHandler != null)
            instanceHandler.removeHandler();
        handlerMap.remove(handleId);
        ConnectionHandle connectionHandle = securityManager.removeConnectionHandle(handleId);
        logger.info("User "+connectionHandle.getUserName() + " logged out successfullly");
    }


    public void unRegisterOldListeners(String handleId) throws RemoteException, ServiceException {
        dapiEventManager.unRegisterOldListeners(handleId);
    }

    public ConnectionHandle getConnectionHandle(String handleId) {
        return securityManager.getConnectionHandle(handleId);
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public ServerLogManager getLogManager() {
        return logManager;
    }


    public void unRegisterApplicationRepoEventListeners(String handleId) {
        //Unregisters the Application repository Listener for the specific client(Based on handleID)
        dapiEventManager.unRegisterApplicationRepoEventListener(handleId);
    }

    public void unRegisterAllApplicationListeners(String handleId) {
        //Unregisters the Application Event Listenrers for the specific client(Based on handleID)
        dapiEventManager.unregisterAllApplicationListeners(handleId);
    }

    public void unRegisterServiceRepoEventListener(String handleId) {
        //UnRegisters the Service Repository Listener for the specific client(Based on handleID)
        dapiEventManager.unRegisterMicroServiceRepoEventListener(handleId);
    }

    public void unRegisterServerStateListener(String handleId) {
        //unregisters the Peer Server status Listener for the specific client(Based on handleID)
        dapiEventManager.unRegisterServerStateListener(handleId);
    }

    public void unRegisterConfigurationRepositoryListener(String handleId) {
        //unregisters the Peer Server status Listener for the specific client(Based on handleID)
        dapiEventManager.unRegisterConfigurationRepositoryEventListener(handleId);
    }

    public synchronized List getIPAliases()
    {
        if(ipAliases!=null){
            return ipAliases;
        }
        ipAliases = new ArrayList();

        try
        {
            NetworkInterface iface;
            // changed to include all ip's from all network cards.
            for(Enumeration ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();){

                iface = (NetworkInterface)ifaces.nextElement();

                InetAddress ia;
                for(Enumeration ips =    iface.getInetAddresses();ips.hasMoreElements();)
                {
                    ia = (InetAddress)ips.nextElement();
                    // The check here get all the ip aliases from all the network cards except for
                    // loopback addresses. we obviously wont want the loop back addresses as the FPS address
                    // Inet4 address check is to prevent mac addresses from returning.
                    // - prasanna
                    if(ia instanceof Inet4Address && !ia.isLoopbackAddress())
                    {
                        String address = ia.getHostAddress();
                        String name = ia.getHostName();
                        ipAliases.add(name);
                        ipAliases.add(address);
                    }
                }
            }
            // in case we launch the component without connected to the network, the only
            // available address will be loopback addresses. Hence we need to get the hostname
            // and add it here

            if(ipAliases.isEmpty()){
                ipAliases.add(InetAddress.getLocalHost().getHostName().toUpperCase());
                ipAliases.add(InetAddress.getLocalHost().getHostAddress());
            }
            String hostname =  rmiConnector.getRmiConnectorConfig().getHostname();
            if(hostname!=null && !hostname.trim().isEmpty())
                ipAliases.add((hostname));
        }
        catch (Throwable e)
        {
            // do not do anything. This won't make lot of difference.
        }
        return ipAliases;
    }
}
