/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.microservice.launch.impl.cl;

public class LogHelper {
    public static Object getOutMessage(String clm, int i, String log) {
        return log;
    }

    public static Throwable getOutMessage(String clm, int i, String uniqueComponentIdentifier, String s) {
        return new Exception(uniqueComponentIdentifier);
    }

    public static String getErrMessage(String clm, int i, String s) {
        return s;
    }
}
