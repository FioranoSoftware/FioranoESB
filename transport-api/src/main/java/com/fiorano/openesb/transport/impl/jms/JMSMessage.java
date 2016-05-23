/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport.impl.jms;

import com.fiorano.openesb.transport.Message;
import com.fiorano.openesb.transport.TransportService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Enumeration;

public class JMSMessage implements Message<javax.jms.Message> {
    javax.jms.Message message;



    public JMSMessage(javax.jms.Message message) {
        this.message = message;

    }

    public javax.jms.Message getMessage() {
        return message;
    }

    @Override
    public String getBody() throws JMSException {
        if(message instanceof TextMessage) {
            return ((TextMessage) message).getText();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setInternalMessage(javax.jms.Message message) {
        this.message = message;
    }

    /*public javax.jms.Message reset(TransportService transportService , Message<javax.jms.Message> message) throws Exception {
        JMSMessage messageToClone = (JMSMessage) transportService.createMessage(new JMSMessageConfiguration(getJmsType(message)));
        javax.jms.Message writeableMessage = messageToClone.getMessage();
        javax.jms.Message readOnlyMessage = message.getMessage();
        clone(readOnlyMessage,writeableMessage);
        return writeableMessage;
    }*/

    private JMSMessageConfiguration.MessageType getJmsType(Message<javax.jms.Message> message) throws JMSException {
        /*String msgType = message.getMessage().getJMSType();
        if(JMSMessageConfiguration.MessageType.Text.toString().equals(msgType)){

        };*/
        return JMSMessageConfiguration.MessageType.valueOf(message.getMessage().getJMSType());
    }
    /*private void clone(javax.jms.Message readOnlyMessage, javax.jms.Message writeableMessage) {
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
            if(readOnlyMessage instanceof  TextMessage) {
                ((TextMessage)writeableMessage).setText(((TextMessage) readOnlyMessage).getText());
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }*/
}
