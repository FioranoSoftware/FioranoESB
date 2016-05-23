/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.amq;

import com.fiorano.openesb.transport.ConnectionProvider;
import com.fiorano.openesb.transport.PortConfiguration;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.AbstractJMSTransportService;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.transport.impl.jms.JMSPort;
import com.fiorano.openesb.transport.impl.jms.JMSPortConfiguration;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AMQTransportService extends AbstractJMSTransportService implements TransportService<JMSPort, JMSMessage> {

    private BrokerViewMBean adminMBean;
    private JMXConnector connector;

    public AMQTransportService(Properties properties) throws Exception {
        super(properties);
        initialize();
    }

    @Override
    protected void initialize() throws Exception {
        super.initialize();
        initializeAdminConnector();
    }

    private void initializeAdminConnector() throws IOException, MalformedObjectNameException {
        String username = properties.getProperty("userName");
        String password = properties.getProperty("password");
        Map<String, String[]> env = new HashMap<>();
        String[] credentials = new String[]{username, password};
        env.put(JMXConnector.CREDENTIALS, credentials);
        String serviceURL = properties.getProperty("jmxURL");
        connector = JMXConnectorFactory.newJMXConnector(new JMXServiceURL(serviceURL), env);
        connector.connect();
        MBeanServerConnection connection = connector.getMBeanServerConnection();
        ObjectName activeMQ = new ObjectName("org.apache.activemq:type=Broker,brokerName=amq-broker");
        adminMBean = MBeanServerInvocationHandler.newProxyInstance(connection, activeMQ, BrokerViewMBean.class, true);
    }

    public ConnectionProvider getConnectionProvider() {
        return new AMQConnectionProvider(properties);
    }

    public void disablePort(PortConfiguration portConfiguration) throws Exception {
        JMSPortConfiguration jmsPortConfiguration = (JMSPortConfiguration) portConfiguration;
        if (jmsPortConfiguration.getPortType() == JMSPortConfiguration.PortType.QUEUE) {
            adminMBean.removeQueue(jmsPortConfiguration.getName());
        } else {
            adminMBean.removeTopic(jmsPortConfiguration.getName());
        }
    }

    public void stop() {
        try {
            if (adminMBean != null) {
                try {
                    adminMBean.stop();
                } catch (InstanceNotFoundException e) {
                    Logger myLogger = LoggerFactory.getLogger(Activator.class);
                    myLogger.trace("Error stopping Admin MBean " + e.getMessage());
                }
            }
            if (connector != null) {
                connector.close();
            }
        } catch (Exception e) {
            //Ignore - could be because AMQ stopped.
        }
    }
}
