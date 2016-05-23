/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport.impl.jms;

import com.fiorano.openesb.utils.ConfigReader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TransportConfig {
    private static TransportConfig CONFIGURATION_LOOKUP_HELPER = new TransportConfig();
    private Properties properties;
    private String userName = "karaf";
    private String password = "karaf";
    private String brokerURL ="tcp://localhost:61616?wireFormat.maxInactivityDuration=0";
    private String jmxURL = "service:jmx:rmi:///jndi/rmi://localhost:1099/karaf-root";
    private String providerURL = "tcp://localhost:61616";

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getUserName() {
        return userName;
    }

    public void setuserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setbrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public String getJmxURL() {
        return jmxURL;
    }

    public void setjmxURL(String jmxURL) {
        this.jmxURL = jmxURL;
    }

    public String getProviderURL() {
        return providerURL;
    }

    public void setproviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    private TransportConfig() {
        File configFile = new File(System.getProperty("user.dir") + File.separator
                + "etc" + File.separator + "com.fiorano.openesb.transport.provider.cfg");
        if (!configFile.exists()) {
            return;
        }
        try {
            ConfigReader.readConfigFromPropertiesFile(configFile, this);
            properties = new Properties();
            ConfigReader.readPropertiesFromFile(configFile, properties);
        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static TransportConfig getInstance() {
        return CONFIGURATION_LOOKUP_HELPER;
    }

    public String getValue(String key) {
        return properties.getProperty(key);
    }

    public String getValue(String key, String defaultValue) {
        String property = properties.getProperty(key);
        return property != null ? property : defaultValue;
    }

    public void saveConfig(){
        try {
            Properties properties = new Properties();
            properties.setProperty("userName", getUserName());
            properties.setProperty("password", getPassword());
            properties.setProperty("brokerURL", getBrokerURL());
            properties.setProperty("jmxURL", getJmxURL());
            properties.setProperty("providerURL", getProviderURL());

            File file = new File(System.getProperty("user.dir") + File.separator
                    + "etc" + File.separator + "com.fiorano.openesb.transport.provider.cfg");
            FileOutputStream fileOut = new FileOutputStream(file);
            properties.store(fileOut, "Transport Config");
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Properties getProperties(){
        return properties;
    }

    public Map<String, String> getConnectionProperties(){
        Properties properties = TransportConfig.getInstance().getProperties();
        Map<String, String> map = new HashMap<>();
        for (String name: properties.stringPropertyNames())
            map.put(name, properties.getProperty(name));
        return map;
    }
}
