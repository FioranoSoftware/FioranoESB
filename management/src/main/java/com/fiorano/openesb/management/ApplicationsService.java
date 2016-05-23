/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.management;


public interface ApplicationsService {


    Response getApplications();

    Response performApplicationAction(String applicationName , String applicationVersion, Action action);

    Response performMicroServiceAction(String appGuid, String version, String microServiceName, Action action);

}
