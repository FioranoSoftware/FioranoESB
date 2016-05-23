/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class SystemUtil {

    public static Double getProcessCPUUtilization() {
        try {
            OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            return operatingSystemMXBean.getProcessCpuLoad();
            //return -1d;
        } catch (Exception e){
            return -1d;
        }
    }

    public static String formatCPUUtilization(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if(d < 0) {
            return "NA";
        } else {
            return decimalFormat.format(d*100).replace(",",".");
        }
    }
}
