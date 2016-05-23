/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.management;

import java.util.Map;

public class Response {
    private Map<String,ApplicationHeader> applications;

    private String message ;

    private boolean status ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<String, ApplicationHeader> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, ApplicationHeader> applications) {
        this.applications = applications;
    }
}
