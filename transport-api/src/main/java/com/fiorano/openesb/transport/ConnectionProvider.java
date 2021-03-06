/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport;

public interface ConnectionProvider<C,CC extends ConnectionConfiguration> {
    void prepareConnectionMD(CC cc) throws Exception;
    void releaseConnectionMD(CC cc);
    C createConnection(CC cc) throws Exception;

}
