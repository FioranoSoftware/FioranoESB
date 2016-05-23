/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route;

import com.fiorano.openesb.transport.Message;
import com.fiorano.openesb.transport.PortConfiguration;

public interface Route<M extends Message> {
    String getRouteName();
    void start() throws Exception;
    void stop() throws Exception;
    void delete();
    void changeTargetDestination(PortConfiguration portConfiguration) throws Exception;
    void changeSourceDestination(PortConfiguration portConfiguration) throws Exception;
    void handleMessage(M message) throws Exception;
    String getSourceDestinationName();
    String getTargetDestinationName();
    void modifyHandler(RouteOperationConfiguration configuration) throws Exception;
    void removeHandler(RouteOperationConfiguration configuration) throws Exception;
}
