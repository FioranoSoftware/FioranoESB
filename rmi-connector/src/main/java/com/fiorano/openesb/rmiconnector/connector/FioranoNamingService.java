/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.connector;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import mx4j.tools.naming.NamingServiceMBean;

public class FioranoNamingService implements mx4j.tools.naming.NamingServiceMBean{

    private int port=1099;
    private Registry registry;
    private volatile boolean running;
    private RMIClientSocketFactory rmiCsf;
    private RMIServerSocketFactory rmiSsf;

    public FioranoNamingService(int port) {
        this.port = port;
    }

    public FioranoNamingService(int port,RMIClientSocketFactory rmiCsf, RMIServerSocketFactory rmiSsf) {
        this.port=port;
        this.rmiCsf = rmiCsf;
        this.rmiSsf = rmiSsf;
    }

     public void setPort(int port)
    {
       if (isRunning()) throw new IllegalStateException("NamingService is running, cannot change the port");
       this.port = port;
    }

    public int getPort()
    {
       return port;
    }

    public boolean isRunning()
    {
       return running;
    }

    public void start() throws RemoteException
    {
       if (!isRunning())
       {
          if(rmiCsf != null && rmiSsf != null)
            registry = LocateRegistry.createRegistry(port,rmiCsf,rmiSsf);
          else
            registry = LocateRegistry.createRegistry(port);
          running = true;
       }
    }

    public void stop() throws NoSuchObjectException
    {
       if (isRunning())
       {
          running = !UnicastRemoteObject.unexportObject(registry, true);
       }
    }

    public String[] list() throws RemoteException
    {
       if (!isRunning()) throw new IllegalStateException("Fiorano NamingService is not running");
       return registry.list();
    }

    public void unbind(String name) throws RemoteException, NotBoundException
    {
       if (!isRunning()) throw new IllegalStateException("Fiorano NamingService is not running");
       registry.unbind(name);
    }

}
