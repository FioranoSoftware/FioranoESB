/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.microservice.bundle;

import com.fiorano.openesb.microservice.ccp.CCPEventManager;
import com.fiorano.openesb.microservice.launch.impl.MicroServiceLauncher;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.transport.TransportService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class Activator implements BundleActivator {

    private Logger logger;

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        logger = LoggerFactory.getLogger(getClass());
        logger.trace("Starting Microservice bundle.");
        ServiceReference<TransportService> serviceReference = context.getServiceReference(TransportService.class);
        if(serviceReference == null) {
            logger.error("Transport service is not available for Microservice. Exiting");
            System.out.println("Startup Failed. Transport service is not available for Microservice. Please make sure " +
                    "transport provider is started and restart the OpenESB server");
            return;
        }

        TransportService service = context.getService(serviceReference);
        CCPEventManager ccpEventManager = new CCPEventManager(service);
        MicroServiceLauncher microServiceLauncher = new MicroServiceLauncher(ccpEventManager);
        MicroServiceRepoManager microServiceRepoManager = MicroServiceRepoManager.getInstance();
        context.registerService(CCPEventManager.class,ccpEventManager,new Hashtable<String, Object>());
        context.registerService(MicroServiceLauncher.class, microServiceLauncher, new Hashtable<String, Object>());
        context.registerService(MicroServiceRepoManager.class, microServiceRepoManager, new Hashtable<String, Object>());
        logger.debug("Started Microservice bundle.");
    }

    public void stop(BundleContext context) {
        logger.trace("Stopped Microservice bundle.");
    }

}