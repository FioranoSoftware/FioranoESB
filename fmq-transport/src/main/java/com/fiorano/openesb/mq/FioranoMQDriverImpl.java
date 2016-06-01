/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.mq;

import fiorano.jms.common.FioranoException;
import fiorano.jms.md.UnifiedConnectionFactoryMetaData;
import fiorano.jms.runtime.admin.MQAdminConnection;
import fiorano.jms.runtime.admin.MQAdminConnectionFactory;
import fiorano.jms.runtime.admin.MQAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class FioranoMQDriverImpl implements MQDriver {

    private MQAdminConnection ac;
    private MQAdminService adminService;
    private InitialContext ic;
    private final Logger logger;
    private Properties properties;

    public FioranoMQDriverImpl(Properties properties) throws Exception {
        this.properties = properties;
        logger = LoggerFactory.getLogger(Activator.class.getName());
    }

    public void initialize(Properties properties) throws FioranoException, NamingException {
        properties.setProperty("java.naming.provider.url", properties.getProperty("providerURL"));
        properties.setProperty("provider.url", properties.getProperty("providerURL"));
        ic = new InitialContext(properties);
        logger.info("Created Initial Context :: " + ic);
        MQAdminConnectionFactory acf = (MQAdminConnectionFactory) ic.lookup(properties.getProperty("fiorano.acf.name"));
        ac = acf.createMQAdminConnection(properties.getProperty(Context.SECURITY_PRINCIPAL), properties.getProperty(Context.SECURITY_CREDENTIALS));
        logger.info("Created Admin Connection :: " + ac);
        adminService = ac.getMQAdminService();
        System.out.println("Connected to Fiorano MQ Server");
    }

    public void deleteDestination(String name, String type) throws Exception {
        if (type.equalsIgnoreCase("QUEUE")) {
            adminService.deleteQueue(name);
        } else if (type.equalsIgnoreCase("TOPIC")) {
            adminService.deleteTopic(name);
        }
    }

    public ConnectionFactory getConnectionFactory(String name) throws Exception {
        UnifiedConnectionFactoryMetaData unifiedConnectionFactoryMetaData = new UnifiedConnectionFactoryMetaData();
        unifiedConnectionFactoryMetaData.setName(name);
        unifiedConnectionFactoryMetaData.setDescription(name + " created by Fiorano Open ESB");
        unifiedConnectionFactoryMetaData.setConnectURL(properties.getProperty("providerURL"));
        try {
            adminService.createConnectionFactory(unifiedConnectionFactoryMetaData);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return (ConnectionFactory) ic.lookup(name);
    }

    public void cleanUp() throws Exception {
        ac.close();
    }

}
