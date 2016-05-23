/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.jmsroute.impl;

import com.fiorano.openesb.route.Route;
import com.fiorano.openesb.route.RouteService;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.transport.impl.jms.JMSPort;
import com.fiorano.openesb.transport.impl.jms.JMSPortConfiguration;


public class JMSRouteServiceImpl implements RouteService<JMSRouteConfiguration> {
    private TransportService<JMSPort,JMSMessage> transportService;


    public JMSRouteServiceImpl(TransportService<JMSPort,JMSMessage>  transportService) {
        this.transportService = transportService;
    }

    public Route createRoute(String routeName, JMSRouteConfiguration routeConfiguration) throws Exception {
        return new JMSRouteImpl(routeName, transportService,routeConfiguration);
    }


}
