/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.connector;

import com.fiorano.openesb.rmiconnector.server.FioranoRMIMasterSocketFactory;
import com.fiorano.openesb.utils.ConfigReader;
import com.fiorano.openesb.utils.exception.FioranoException;

import javax.management.MBeanServerFactory;
import javax.management.remote.*;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.naming.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.*;

public class RmiConnector {
    private RMIServerSocketFactory ssf;
    private RMIClientSocketFactory csf;
    FioranoNamingService namingService;
    private JMXConnectorServer connectorServer;

    public RmiConnector(){

    }

    public RmiConnectorConfig getRmiConnectorConfig(){
        return RmiConnectorConfig.getConfig();
    }

    public void createService()
            throws FioranoException
    {
        try
        {
            System.setProperty("java.rmi.server.hostname", RmiConnectorConfig.getConfig().getHostname());
            //Start Naming Service first.
            FioranoRMIMasterSocketFactory masterfac = FioranoRMIMasterSocketFactory.getInstance();
            List factories = masterfac.getSocketFactories("FioranoRMIServerSocketFactory",
                    "FioranoRMIClientSocketFactory");

            csf =(RMIClientSocketFactory)factories.get(0);
            ssf =(RMIServerSocketFactory)factories.get(1);
            setRmiRegistryIpAddress();// ---->do before starting fiorano naming service !
            //namingService = new FioranoNamingService(rmiConnectorConfig.getRmiRegistryPort(),csf,ssf);
            namingService = new FioranoNamingService(RmiConnectorConfig.getConfig().getRmiRegistryPort(),csf,ssf);
            namingService.start();
            System.out.println("RMI registry listening on " + RmiConnectorConfig.getConfig().getRmiRegistryPort());
        }
        catch (Exception ex)
        {

        }
    }

    /** Sets the 'java.rmi.server.hostname' system property. <br>WARNING: Call it before creating rmi registry else it is of no use. </br>*/
    private void setRmiRegistryIpAddress(){

        StringBuilder bf= new StringBuilder();
        try
        {
            NetworkInterface iface;
            for(Enumeration ifaces = NetworkInterface.getNetworkInterfaces();ifaces.hasMoreElements();)
            {
                iface = (NetworkInterface)ifaces.nextElement();
                InetAddress ia;
                Enumeration ips =    iface.getInetAddresses();
                while(ips.hasMoreElements())
                {
                    ia = (InetAddress)ips.nextElement();
                    // get all the ip aliases from all the network cards except for
                    // loopback addresses. loop back addresses are not included because, then rmi registry would
                    // export objects on 127.0.0.1 or localhost => hence server would be accessible to clients present only on localhost i.e the same machine as server!!!!
                    if(ia instanceof Inet4Address && !ia.isLoopbackAddress())
                    {
                        String ip=ia.getHostAddress();
                        bf.append(ip);
                        bf.append(',');
                    }
                }
            }
            String hosts=bf.toString();
            if(hosts.endsWith(","))//hosts="a,b,c,"
                hosts=hosts.substring(0,hosts.lastIndexOf(","));// hosts="a,b,c"
            //Note: if network card is disabled=>hosts is empty, rmi runtimes uses localhost/127.0.0.1.
            System.setProperty("java.rmi.server.hostname",hosts);
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
    }

    public RMIServerSocketFactory getSsf() {
        return ssf;
    }

    public RMIClientSocketFactory getCsf() {
        return csf;
    }
}
