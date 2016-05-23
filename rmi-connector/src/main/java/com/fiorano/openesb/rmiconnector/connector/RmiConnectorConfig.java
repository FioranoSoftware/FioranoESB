/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.connector;

import com.fiorano.openesb.utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RmiConnectorConfig {
    private static RmiConnectorConfig rmiConnectorConfig = new RmiConnectorConfig();
    private int     rmiRegistryPort=2099;
    private String  interceptorClassName="fiorano.jmx.connector.FioranoJMXInterceptor";
    /**Port on which Rmi Registry will export the Mx4J RMIConnector stubs.*/
    private int rmiServerPort=2099;
    /** specifies host name on which the registry would export objects. This value is set as the system property 'java.rmi.server.hostname'*/
    private String hostname="localhost";

    private String rmiServerSocketFactoryClassName="fiorano.rmi.serverfac.def.FioranoRMIServerSocketFactory";
    private String rmiClientSocketFactoryClassName="fiorano.rmi.clientfac.def.FioranoRMIClientSocketFactory";

    public int getRmiRegistryPort()
    {
        return rmiRegistryPort;
    }

    public int getRmiServerPort() {
        return rmiServerPort;
    }

    public String getHostname() {
        return hostname;
    }

    public String getInterceptorClassName()
    {
        return interceptorClassName;
    }

    public void setRmiRegistryPort(String port)
    {
        rmiRegistryPort = Integer.valueOf(port);
    }

    public void setRmiServerPort(String rmiPortExportObjects) {
        this.rmiServerPort = Integer.valueOf(rmiPortExportObjects);
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setInterceptorClassName(String interceptorClassName)
    {
        interceptorClassName = interceptorClassName;
    }

    public String getRmiServerSocketFactoryClassName()
    {
        return rmiServerSocketFactoryClassName;
    }

    public String getRmiClientSocketFactoryClassName()
    {
        return rmiClientSocketFactoryClassName;
    }

    public void setRmiClientSocketFactoryClassName(String rmiClientSocketFactoryClassName)
    {
        this.rmiClientSocketFactoryClassName=rmiClientSocketFactoryClassName;
    }

    public void setRmiServerSocketFactoryClassName(String rmiServerSocketFactoryClassName)
    {
        this.rmiServerSocketFactoryClassName=rmiServerSocketFactoryClassName;
    }

    private RmiConnectorConfig() {
            File configFile = new File(System.getProperty("user.dir") + File.separator
                    + "etc" + File.separator + "com.fiorano.openesb.rmiconnector.cfg");
            if (!configFile.exists()) {
                return;
            }
            try {
                ConfigReader.readConfigFromPropertiesFile(configFile, this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
    }

    public static RmiConnectorConfig getConfig(){
        return rmiConnectorConfig;
    }
}
