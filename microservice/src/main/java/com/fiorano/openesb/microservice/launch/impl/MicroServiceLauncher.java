/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.microservice.ccp.CCPEventManager;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.launch.Launcher;
import com.fiorano.openesb.microservice.launch.MicroServiceRuntimeHandle;

import java.util.Map;

public class MicroServiceLauncher implements Launcher<MicroServiceRuntimeHandle>{
    private final CCPEventManager ccpEventManager;

    public MicroServiceLauncher(CCPEventManager ccpEventManager) throws Exception {
        this.ccpEventManager = ccpEventManager;
    }

    public MicroServiceRuntimeHandle launch(LaunchConfiguration launchConfiguration, String configuration) throws Exception {
        Launcher launcher = null;
        if(launchConfiguration.getLaunchMode() == LaunchConfiguration.LaunchMode.SEPARATE_PROCESS) {
             launcher = new SeparateProcessLauncher(ccpEventManager);
        } else if(launchConfiguration.getLaunchMode() == LaunchConfiguration.LaunchMode.IN_MEMORY) {
            launcher = new InMemoryLauncher();
        } else  if(launchConfiguration.getLaunchMode() == LaunchConfiguration.LaunchMode.MANUAL) {
            return new ManualLaunchProcessRuntimeHandle(launchConfiguration, new CCPCommandHelper(ccpEventManager,launchConfiguration));
        } else if (launchConfiguration.getLaunchMode() == LaunchConfiguration.LaunchMode.DOCKER) {
            return new ManualLaunchProcessRuntimeHandle(launchConfiguration, new CCPCommandHelper(ccpEventManager,launchConfiguration));
        }
        return launcher.launch(launchConfiguration, configuration);
    }
}
