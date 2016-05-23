/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;

public class FioranoRMIServerSocketFactory implements RMIServerSocketFactory {

    public final String socketFactoryID = "FioranoRMIServerDef";

    public ServerSocket createServerSocket(int port) throws IOException {

        return RMISocketFactory.getDefaultSocketFactory().createServerSocket(port);
    }

    public boolean equals(Object obj) {

        boolean ret=false;

        if (this == obj) ret= true;

        else if (obj == null) ret= false;

        else if (getClass() == obj.getClass()) ret = true;

        return ret;
    }

    public int hashCode() {    //keeping hashcode inline with .equals as per javadoc.
        return socketFactoryID.hashCode();
    }
}
