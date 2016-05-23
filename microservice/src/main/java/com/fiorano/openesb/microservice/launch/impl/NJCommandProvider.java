/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.microservice.launch.LaunchConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NJCommandProvider extends CommandProvider {

    @Override
    protected List<String> generateCommand(LaunchConfiguration launchConfiguration) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(getExecutionDir(launchConfiguration)+ File.separator +
                getComponentPS(launchConfiguration.getAdditionalConfiguration().getCompRepoPath(),launchConfiguration.getMicroserviceId(), launchConfiguration.getMicroserviceVersion()).getExecution().getExecutable());
        command.addAll(getCommandLineParams(launchConfiguration));
        return command;
    }


}
