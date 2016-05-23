/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.namedconfig;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class Activator implements BundleActivator {

    private Logger logger;

    public void start(BundleContext context) {
        logger = LoggerFactory.getLogger(getClass());
        logger.trace("Starting Named Configuration bundle.");
        NamedConfigRepository namedConfigRepository = new NamedConfigRepository();
        context.registerService(NamedConfigRepository.class,namedConfigRepository,new Hashtable<String, Object>());
        logger.debug("Started Named Configuration bundle.");
    }

    public void stop(BundleContext context) {
        logger.trace("Stopped Named Configuration bundle.");
    }

}