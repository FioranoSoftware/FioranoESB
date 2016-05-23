/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.amq;

import com.fiorano.openesb.transport.TransportService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

public class Activator implements BundleActivator {

    private AMQTransportService service;
    private Logger logger;

    public Activator() {
        logger = LoggerFactory.getLogger(getClass());
    }

    public void start(BundleContext context) throws Exception {
        System.out.println("Starting Active MQ Transport");
        try {
            Properties properties = new Properties();
            try (FileInputStream inStream = new FileInputStream(System.getProperty("user.dir") + File.separator
                    + "etc" + File.separator + "com.fiorano.openesb.transport.provider.cfg")) {
                properties.load(inStream);
            }
            if (properties.containsKey("provider.name") && !"activemq".equalsIgnoreCase(properties.getProperty("provider.name"))) {
                return;
            }
            service = new AMQTransportService(properties);
        } catch (JMSException e) {
            System.out.println("Could not connect to MQ Server.");
            context.getBundle(0).stop();
        }
        context.registerService(TransportService.class, service, new Hashtable<String,Object>());
        System.out.println("Started Active MQ Transport");
    }

    public void stop(BundleContext context) {
        try {
            service.stop();
        } catch (Exception e) {
            logger.debug("Error stopping Active MQ Transport " + e.getMessage());
        }
        System.out.println("Stopped Active MQ Transport");
    }

}