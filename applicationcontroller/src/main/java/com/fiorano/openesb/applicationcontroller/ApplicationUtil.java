/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.microservice.ccp.event.ComponentCCPEvent;
import com.fiorano.openesb.utils.Constants;

public class ApplicationUtil {

    public static String getAppName(ComponentCCPEvent event) {
        String componentId = event.getComponentId();
        return componentId.substring(0, componentId.indexOf(Constants.NAME_DELIMITER));
    }
    public static String getAppVersion(ComponentCCPEvent event) {
        String componentId = event.getComponentId();
        return componentId.substring(componentId.indexOf(Constants.NAME_DELIMITER) + 2,componentId.lastIndexOf(Constants.NAME_DELIMITER)).replace("_", ".");
    }
    public static String getInstanceName(ComponentCCPEvent event) {
        String componentId = event.getComponentId();
        String version = componentId.substring(componentId.indexOf(Constants.NAME_DELIMITER) + 2);
        String instance = version.substring(version.indexOf(Constants.NAME_DELIMITER) + 2);
        if(instance.contains(Constants.NAME_DELIMITER)){
            return instance.substring(0, instance.indexOf(Constants.NAME_DELIMITER));
        }
        return instance;
    }

    public static String getKey(String appGuid, String version) {
        return appGuid+ ":"+ version;
    }

    public static String[] returnAppGUIDAndVersion(String app_version){
        if (app_version == null)
            return  null;
        String[] result = new String[2];
        int lastIndexOfDelim = app_version.lastIndexOf(Constants.NAME_DELIMITER);
        result[0] = app_version.substring(0, lastIndexOfDelim);
        result[1] = app_version.substring(lastIndexOfDelim+2);
        return result;
    }
}
