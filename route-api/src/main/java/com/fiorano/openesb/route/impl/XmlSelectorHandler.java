/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.FilterMessageException;
import com.fiorano.openesb.route.RouteOperationHandler;
import com.fiorano.openesb.route.Selector;
import com.fiorano.openesb.route.bundle.Activator;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

public class XmlSelectorHandler implements RouteOperationHandler<JMSMessage> {
    private Selector selector;
    private XmlSelectorConfiguration selectorConfiguration;
    private Logger logger;

    public XmlSelectorHandler(XmlSelectorConfiguration selectorConfiguration) {
        this.selectorConfiguration = selectorConfiguration;
        this.selector = new XMLContentSelector(selectorConfiguration);
        this.logger = LoggerFactory.getLogger(Activator.class);
    }

    @Override
    public void handleOperation(JMSMessage message) throws FilterMessageException, FioranoException {
        try {
            String content= selectorConfiguration.getTarget().equalsIgnoreCase("Body") ?
                    message.getBody() : JmsMessageUtil.getApplicationContext(message.getMessage());
            if (!selector.isMessageSelected(content)) {
                throw new FilterMessageException();
            }
        } catch (JMSException e) {
            logger.error("Exception occurred in message selector : " + e.getMessage() + e.getStackTrace());
        }
    }
}
