/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.RouteOperationHandler;
import com.fiorano.openesb.route.bundle.Activator;
import com.fiorano.openesb.transport.Message;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.transport.impl.jms.JMSMessageConfiguration;
import com.fiorano.openesb.transport.impl.jms.JMSPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jms.*;
import java.util.Enumeration;

public class MessageCreationHandler implements RouteOperationHandler<Message<javax.jms.Message>> {

    private TransportService<JMSPort,JMSMessage> transportService;
    private Logger logger ;

    public MessageCreationHandler(MessageCreationConfiguration  messageCreationConfiguration) {
        this.logger = LoggerFactory.getLogger(Activator.class);
        this.transportService = messageCreationConfiguration.getTransportService();
    }

    @Override
    public void handleOperation(Message<javax.jms.Message> message) throws Exception {
        JMSMessage messageToClone = transportService.createMessage(new JMSMessageConfiguration(getJmsType(message)));
        javax.jms.Message writeableMessage = messageToClone.getMessage();
        javax.jms.Message readOnlyMessage = message.getMessage();
        clone(readOnlyMessage,writeableMessage);
        message.setInternalMessage(writeableMessage);
    }

    private void clone(javax.jms.Message readOnlyMessage, javax.jms.Message writeableMessage) {
        try {
            Enumeration<String> propertyNames = readOnlyMessage.getPropertyNames();
            while (propertyNames.hasMoreElements()) {
                String s = propertyNames.nextElement();
                Object o = readOnlyMessage.getObjectProperty(s);
                if (o instanceof java.lang.Boolean) {
                    writeableMessage.setBooleanProperty(s, readOnlyMessage.getBooleanProperty(s));
                } else if (o instanceof java.lang.Byte) {
                    writeableMessage.setByteProperty(s, readOnlyMessage.getByteProperty(s));
                } else if (o instanceof java.lang.Double) {
                    writeableMessage.setDoubleProperty(s, readOnlyMessage.getDoubleProperty(s));
                } else if (o instanceof java.lang.String) {
                    writeableMessage.setStringProperty(s, readOnlyMessage.getStringProperty(s));
                } else if (o instanceof java.lang.Float) {
                    writeableMessage.setFloatProperty(s, readOnlyMessage.getFloatProperty(s));
                } else if (o instanceof java.lang.Integer) {
                    writeableMessage.setIntProperty(s, readOnlyMessage.getIntProperty(s));
                } else if (o instanceof java.lang.Long) {
                    writeableMessage.setLongProperty(s, readOnlyMessage.getLongProperty(s));
                } else if (o instanceof java.lang.Short) {
                    writeableMessage.setShortProperty(s, readOnlyMessage.getShortProperty(s));
                } else {
                    writeableMessage.setObjectProperty(s, readOnlyMessage.getObjectProperty(s));
                }
            }
            if (readOnlyMessage instanceof TextMessage) {
                ((TextMessage) writeableMessage).setText(((TextMessage) readOnlyMessage).getText());
            }
            writeableMessage.setJMSCorrelationID(readOnlyMessage.getJMSCorrelationID());
            writeableMessage.setJMSDestination(readOnlyMessage.getJMSDestination());
            writeableMessage.setJMSRedelivered(readOnlyMessage.getJMSRedelivered());
            writeableMessage.setJMSDeliveryMode(readOnlyMessage.getJMSDeliveryMode());
            writeableMessage.setJMSExpiration(readOnlyMessage.getJMSExpiration());
            writeableMessage.setJMSMessageID(readOnlyMessage.getJMSMessageID());
            writeableMessage.setJMSPriority(readOnlyMessage.getJMSPriority());
            writeableMessage.setJMSReplyTo(readOnlyMessage.getJMSReplyTo());
            writeableMessage.setJMSTimestamp(readOnlyMessage.getJMSTimestamp());
            writeableMessage.setJMSType(readOnlyMessage.getJMSType());


        } catch (JMSException e) {
            logger.error(" Unable to clone message " + e.getMessage());
        }
    }

    private JMSMessageConfiguration.MessageType getJmsType(Message<javax.jms.Message> message) throws JMSException {
        javax.jms.Message jmsMessage = message.getMessage();
        if(jmsMessage instanceof TextMessage){
            return JMSMessageConfiguration.MessageType.Text;
        } else if(jmsMessage instanceof BytesMessage) {
            return JMSMessageConfiguration.MessageType.Bytes;
        } else if(jmsMessage instanceof ObjectMessage) {
            return JMSMessageConfiguration.MessageType.Object;
        } else if(jmsMessage instanceof StreamMessage) {
            return JMSMessageConfiguration.MessageType.Stream;
        }
        return JMSMessageConfiguration.MessageType.Text;
    }
}
