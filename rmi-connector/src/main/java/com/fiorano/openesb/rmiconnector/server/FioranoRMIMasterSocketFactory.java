/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.server;

import com.fiorano.openesb.rmiconnector.client.FioranoRMIClientSocketFactory;

import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.ArrayList;
import java.util.List;

public class FioranoRMIMasterSocketFactory {

    private static FioranoRMIMasterSocketFactory masterfac;

    /**
     * gets an instance of the RMI master socket factory.
     * @return FioranoRMIMasterSocketFactory
     */
    public synchronized static FioranoRMIMasterSocketFactory getInstance(){
       if(masterfac == null)     //singleton instance
            masterfac = new FioranoRMIMasterSocketFactory();
       return masterfac;
    }

    /**
     * Returns an array list of size 2. The first element is an instance of RMIClientSocketFactory & the
     * second , an instance of RMIServerSocketFactory. If the classes are not found or if any error
     * occurs, it returns the default fiorano rmi server & client socket factories.
     * @param serverFactoryClassName
     * @param clientFactoryClassName
     * @return List
     * @see com.fiorano.openesb.rmiconnector.client.FioranoRMIClientSocketFactory
     * @see com.fiorano.openesb.rmiconnector.server.FioranoRMIServerSocketFactory
     */
    public List getSocketFactories(String serverFactoryClassName,String clientFactoryClassName)
    {
        RMIServerSocketFactory ssf = null;
        RMIClientSocketFactory csf = null;
        try
        {
            Class client = Class.forName(clientFactoryClassName);
            Class server = Class.forName(serverFactoryClassName);
            csf = (RMIClientSocketFactory)client.newInstance();
            ssf = (RMIServerSocketFactory)server.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            csf = new FioranoRMIClientSocketFactory();
            ssf = new FioranoRMIServerSocketFactory();
        } catch (IllegalAccessException e) {
            csf = new FioranoRMIClientSocketFactory();
            ssf = new FioranoRMIServerSocketFactory();
        } catch (InstantiationException e) {
            csf = new FioranoRMIClientSocketFactory();
            ssf = new FioranoRMIServerSocketFactory();
        }

        List list = new ArrayList(2);
        list.add(0,csf);
        list.add(1,ssf);
        return list;
    }

}
