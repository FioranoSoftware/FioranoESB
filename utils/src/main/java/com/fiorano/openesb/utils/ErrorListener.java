/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public interface ErrorListener {
    public void warning(Exception exception) throws Exception;
    public void error(Exception exception) throws Exception;
    public void fatalError(Exception exception) throws Exception;
}