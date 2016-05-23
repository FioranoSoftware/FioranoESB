/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.exception;

public class FioranoException extends Exception {

    public FioranoException(Throwable th){
        super(th);
    }

    public FioranoException(String message, Throwable cause) {
        super(message, cause);
    }

    public FioranoException(String error) {
        super(error);
    }

    public FioranoException() {

    }

    public FioranoException(String message, String... messages) {
        super(message);
    }

    public FioranoException(String message, Throwable cause, String... messages) {
        super(message);
    }

    public FioranoException(Class bundleClass, String bundle, String... params) {
        super(bundle);
    }

    public FioranoException(Class bundleClass, String bundle, Throwable e, String... params) {
        super(bundle);
    }

}
