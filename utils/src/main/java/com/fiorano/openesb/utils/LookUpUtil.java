/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.utils;

public class LookUpUtil {

    public final static String VERSION_DOT_REPLACEMENT_STRING = "_";

    public static String getApplicationLookupName(String appGuid, String appVersion) {
        return appGuid + LoggerUtil.JNDI_CONSTANT + appVersion.replace(LoggerUtil.LOG_NAME_SEPARATOR, VERSION_DOT_REPLACEMENT_STRING);

    }

    public static String getApplicationLookupName(String appGuid, float appVersion) {
        String appVer = String.valueOf(appVersion);
        return appGuid + LoggerUtil.JNDI_CONSTANT + appVer.replace(LoggerUtil.LOG_NAME_SEPARATOR, VERSION_DOT_REPLACEMENT_STRING);

    }

    public static String getServiceInstanceLookupName(String appGuid, String appVersion, String serviceGuid) {
        return getApplicationLookupName(appGuid, appVersion) + LoggerUtil.JNDI_CONSTANT + serviceGuid;
    }

    public static String getServiceInstanceLookupName(String appGuid, float appVersion, String serviceGuid) {
        return getApplicationLookupName(appGuid, appVersion) + LoggerUtil.JNDI_CONSTANT + serviceGuid;
    }

    public static String[] returnAppGUIDAndVersion(String app_version){
        if (app_version == null)
            return  null;
        String[] result = new String[2];
        int lastIndexOfDelim = app_version.lastIndexOf(LoggerUtil.JNDI_CONSTANT);
        result[0] = app_version.substring(0, lastIndexOfDelim);
        result[1] = app_version.substring(lastIndexOfDelim+2);
        return result;
    }
}
