/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.jmsroute;

import com.fiorano.openesb.route.RouteService;
import com.fiorano.openesb.jmsroute.impl.JMSRouteServiceImpl;
import com.fiorano.openesb.transport.TransportService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class Activator implements BundleActivator {

    private Logger logger;

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws BundleException {
        logger = LoggerFactory.getLogger(getClass());
        logger.trace("Starting JMS Route bundle.");
        ServiceReference<TransportService> serviceReference = context.getServiceReference(TransportService.class);
        if(serviceReference == null) {
            logger.error("Transport service is not available for Route. ");
            System.out.println("Startup Failed. Transport service is not available for Route. Please make sure " +
                    "transport provider is started and restart the OpenESB server");
            return;
        }
        TransportService service = context.getService(serviceReference);
        context.registerService(RouteService.class, new JMSRouteServiceImpl(service),new Hashtable<String, Object>());
        logger.debug("Started JMS Route bundle.");
    }

    public void stop(BundleContext context) {
        logger.trace("Stopping JMS Route bundle ");
    }
}