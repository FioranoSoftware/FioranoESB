/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.crypto;

public interface IEncryptionLogger {

    public void trace(String message);

    public void debug(String message);

    public void info(String message);

    public void warn(String message);

    public void error(String message);

    public void trace(String message, Throwable t);

    public void debug(String message, Throwable t);

    public void info(String message, Throwable t);

    public void warn(String message, Throwable t);

    public void error(String message, Throwable t);
}
