/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport.impl.jms;

import com.fiorano.openesb.transport.Producer;

import javax.jms.JMSException;
import javax.jms.MessageProducer;

public class JMSProducer implements Producer<JMSMessage> {
    private MessageProducer producer;

    public JMSProducer(MessageProducer producer) {
        this.producer = producer;
    }

    public void send(JMSMessage message) throws JMSException {
        producer.send(message.message);
    }

    @Override
    public void close() throws JMSException {
        producer.close();
    }


}
