/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl.cl;

import com.fiorano.openesb.application.service.Service;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.utils.exception.FioranoException;


public interface IClassLoaderManager {
    ClassLoader getClassLoader(Service sps, LaunchConfiguration launchConfiguration, ClassLoader parent) throws FioranoException;

    void unloadClassLoader(Service sps, LaunchConfiguration launchConfiguration) throws FioranoException;
}
