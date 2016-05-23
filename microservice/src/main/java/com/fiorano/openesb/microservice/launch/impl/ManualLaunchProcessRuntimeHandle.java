/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.utils.exception.FioranoException;

public class ManualLaunchProcessRuntimeHandle extends SeparateProcessRuntimeHandle {

    protected ManualLaunchProcessRuntimeHandle(LaunchConfiguration launchConfiguration, CCPCommandHelper ccpCommandHelper) throws FioranoException {
        super(launchConfiguration, ccpCommandHelper);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
