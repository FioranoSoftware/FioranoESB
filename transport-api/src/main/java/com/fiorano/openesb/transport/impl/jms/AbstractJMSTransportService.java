/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport.impl.jms;

import com.fiorano.openesb.transport.*;
import com.fiorano.openesb.transport.bundle.Activator;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.jms.Message;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractJMSTransportService implements TransportService<JMSPort, JMSMessage> {

    protected Properties properties;
    private Map<String, Session> sessions = new ConcurrentHashMap<>();
    private Connection connection;

    protected AbstractJMSTransportService(Properties properties) throws Exception {
        this.properties = properties;
    }

    protected void initialize() throws Exception {
        ConnectionFactory cf = ((AbstractJMSConnectionProvider) getConnectionProvider()).getConnectionFactory("ConnectionFactory");
        String connectionRetryCount = TransportConfig.getInstance().getValue("CONNECTION_RETRY_COUNT", "10");
        int count = Integer.valueOf(connectionRetryCount), i = 0;
        while ((connection = getConnection(cf))== null && i++ < count) {
            try {
                String connectionRetryInterval = TransportConfig.getInstance().getValue("CONNECTION_RETRY_INTERVAL", "3000");
                System.out.println("Waiting for connection with JMS provider. Attempt - " + i);
                Thread.sleep(Long.valueOf(connectionRetryInterval));
            } catch (InterruptedException e1) {
                //
            }
        }
        if(connection == null) {
            throw new JMSException("Could not connect to JMS server");
        }
    }

    private Connection getConnection(ConnectionFactory cf) throws JMSException {
        try {
            TransportConfig providerConfig = TransportConfig.getInstance();
            Connection connection = cf.createConnection(providerConfig.getValue("userName"), providerConfig.getValue("password"));
            connection.start();
            return connection;
        } catch (JMSException e) {
            if (e.getMessage().toUpperCase().contains("CONNECT ")) {
                return null;
            } else {
                throw e;
            }
        }
    }

    public Consumer<JMSMessage> createConsumer(JMSPort port, ConsumerConfiguration consumerConfiguration,String sessionId) throws Exception {
        String selector = ((JMSConsumerConfiguration) consumerConfiguration).getSelector();
        Session session = getSession(sessionId);
        MessageConsumer messageConsumer = selector != null ? session.createConsumer(port.getDestination(), selector) :
                session.createConsumer(port.getDestination());
        return new JMSConsumer(messageConsumer);
    }

    private Session getSession(String sessionId) throws JMSException {
        Session session;
        if(sessions.containsKey(sessionId)) {
            session = sessions.get(sessionId);
        } else {
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            sessions.put(sessionId,session);
        }
        return session;
    }

    public Producer<JMSMessage> createProducer(JMSPort port, ProducerConfiguration producerConfiguration, String sessionId) throws JMSException {
        return new JMSProducer(getSession(sessionId).createProducer(port.getDestination()));
    }

    public JMSMessage createMessage(MessageConfiguration messageConfiguration) throws Exception {
        JMSMessageConfiguration config = (JMSMessageConfiguration) messageConfiguration;
        Message message;
        Session session = getSession("DEFAULT");
        switch (config.getType()) {
            case Bytes:
                message = session.createBytesMessage();
                break;
            case Text:
                message = session.createTextMessage();
                break;
            case Stream:
                message = session.createStreamMessage();
                break;
            case Object:
                message = session.createObjectMessage();
                break;
            default:
                message = session.createMessage();
        }
        return new JMSMessage(message);
    }


    public JMSPort enablePort(PortConfiguration configuration) throws Exception {
        JMSPortConfiguration portConfiguration = (JMSPortConfiguration) configuration;
        Session session = getSession("DEFAULT");
        switch (portConfiguration.getPortType()) {
            case QUEUE:
                return new JMSPort(session.createQueue(portConfiguration.getName()));
            case TOPIC:
                return new JMSPort(session.createTopic(portConfiguration.getName()));
        }
        return null;
    }

    @Override
    public void closeSession(String sessionId) {
        if(sessions.containsKey(sessionId)) {
            Session remove = sessions.remove(sessionId);
            try {
                remove.close();
            } catch (JMSException e) {
                LoggerFactory.getLogger(Activator.class).debug(e.getMessage(),e);
            }
        }
    }
}
