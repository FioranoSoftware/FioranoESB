/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.schemarepo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

    private Logger logger;

    public void start(BundleContext context) {
        logger = LoggerFactory.getLogger(getClass());
        logger.trace("Starting schema repository bundle.");
        context.registerService(SchemaRepository.class, SchemaRepository.getSingletonInstance(), null);
        logger.debug("Started schema repository bundle.");
    }

    public void stop(BundleContext context) {
        logger.trace("Stopped schema repository bundle.");
    }

}