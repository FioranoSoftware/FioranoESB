/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.jmsroute.impl;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.route.RouteConfiguration;
import com.fiorano.openesb.route.impl.AbstractRouteConfiguration;
import com.fiorano.openesb.transport.PortConfiguration;
import com.fiorano.openesb.transport.impl.jms.JMSConsumerConfiguration;
import com.fiorano.openesb.transport.impl.jms.JMSPortConfiguration;


public class JMSRouteConfiguration extends AbstractRouteConfiguration implements RouteConfiguration {

    private JMSPortConfiguration sourceConfiguration;

    private JMSPortConfiguration destinationConfiguration;
    private String jmsSelector;

    public JMSRouteConfiguration(JMSPortConfiguration sourceConfiguration, JMSPortConfiguration destinationConfiguration, String jmsSelector) {
        this.sourceConfiguration = sourceConfiguration;
        this.destinationConfiguration = destinationConfiguration;
        this.jmsSelector = jmsSelector;
    }

    public JMSPortConfiguration getSourceConfiguration() {
        return sourceConfiguration;
    }

    public JMSPortConfiguration getDestinationConfiguration() {
        return destinationConfiguration;
    }

    public JMSConsumerConfiguration getConsumerConfiguration() {
        return new JMSConsumerConfiguration(jmsSelector);
    }

    public void setSourceConfiguration(PortConfiguration sourceConfiguration) {
        this.sourceConfiguration = (JMSPortConfiguration) sourceConfiguration;
    }

    public void setDestinationConfiguration(PortConfiguration destinationConfiguration) {
        this.destinationConfiguration = (JMSPortConfiguration) destinationConfiguration;
    }

    public String getJmsSelector() {
        return jmsSelector;
    }

    public void setJmsSelector(String jmsSelector) {
        this.jmsSelector = jmsSelector;
    }
}
