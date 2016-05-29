/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.jmsroute.impl;

import com.fiorano.openesb.jmsroute.Activator;
import com.fiorano.openesb.route.*;
import com.fiorano.openesb.route.impl.AbstractRouteImpl;
import com.fiorano.openesb.transport.*;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.transport.impl.jms.JMSPort;
import com.fiorano.openesb.transport.impl.jms.JMSProducerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSRouteImpl extends AbstractRouteImpl<JMSMessage> implements Route<JMSMessage> {

    private Producer<JMSMessage> producer;
    private JMSPort sourcePort;
    private TransportService<JMSPort, JMSMessage> transportService;
    private RouteConfiguration routeConfiguration;
    private Consumer<JMSMessage> messageConsumer;
    private String sourceDestintaion;
    private String targetDestination;
    private boolean isStarted;
    private Logger logger;

    public JMSRouteImpl(String routeName, final TransportService<JMSPort, JMSMessage> transportService, final RouteConfiguration routeConfiguration) throws Exception {
        super(routeName, routeConfiguration.getRouteOperationConfigurations());
        this.transportService = transportService;
        this.routeConfiguration = routeConfiguration;
        logger = LoggerFactory.getLogger(Activator.class);


        routeOperationHandlers.put(RouteOperationType.SEND, new RouteOperationHandler<JMSMessage>() {
            public void handleOperation(JMSMessage message) throws FilterMessageException {
                try {
                    producer.send(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        sourcePort = transportService.enablePort(routeConfiguration.getSourceConfiguration());
    }

    public void start() throws Exception {
        if (isStarted) {
            return;
        }
        this.sourceDestintaion = routeConfiguration.getSourceConfiguration().getName();
        this.targetDestination = routeConfiguration.getDestinationConfiguration().getName();
        producer = transportService.createProducer(transportService.enablePort(routeConfiguration.getDestinationConfiguration()), new JMSProducerConfiguration(), getSessionId());
        messageConsumer = transportService.createConsumer(sourcePort, routeConfiguration.getConsumerConfiguration(), getSessionId());
        messageConsumer.attachMessageListener(new MessageListener<JMSMessage>() {
            public void messageReceived(JMSMessage message) {
                    handleMessage(message);
            }
        });
        isStarted = true;
    }

    private String getSessionId() {
        return sourceDestintaion + "__" + routeName + "__" + targetDestination;
    }

    public void stop() throws Exception {
        messageConsumer.close();
        producer.close();
        isStarted = false;
    }

    public void delete() {
        transportService.closeSession(getSessionId());
    }

    public void changeTargetDestination(PortConfiguration portConfiguration) throws Exception {
        producer.close();
        producer = null;
        routeConfiguration.setDestinationConfiguration(portConfiguration);
        this.targetDestination = portConfiguration.getName();
        producer = transportService.createProducer(transportService.enablePort(portConfiguration), new JMSProducerConfiguration(), getSessionId());
    }

    public void changeSourceDestination(PortConfiguration portConfiguration) throws Exception {
        messageConsumer.close();
        routeConfiguration.setSourceConfiguration(portConfiguration);
        sourcePort = transportService.enablePort(routeConfiguration.getSourceConfiguration());
        this.sourceDestintaion = portConfiguration.getName();
        messageConsumer = transportService.createConsumer(sourcePort, routeConfiguration.getConsumerConfiguration(), getSessionId());
        messageConsumer.attachMessageListener(new MessageListener<JMSMessage>() {
            public void messageReceived(JMSMessage message) {
                    handleMessage(message);
            }
        });

    }

    public String getSourceDestinationName() {
        return sourceDestintaion;
    }

    public String getTargetDestinationName() {
        return targetDestination;
    }

}
