/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.RouteConfiguration;
import com.fiorano.openesb.route.RouteOperationConfiguration;
import com.fiorano.openesb.transport.PortConfiguration;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRouteConfiguration implements RouteConfiguration {
    private List<RouteOperationConfiguration> routeOperationConfigurations = new ArrayList<RouteOperationConfiguration>();

    public List<RouteOperationConfiguration> getRouteOperationConfigurations() {
        return routeOperationConfigurations;
    }
}
