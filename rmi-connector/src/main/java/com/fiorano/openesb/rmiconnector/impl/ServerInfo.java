/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.SystemInfo;
import com.fiorano.openesb.application.TESPerformanceStats;
import com.fiorano.openesb.utils.SystemUtil;

public class ServerInfo
{

    /**
     * Gets the System information of the FES Server.
     *
     * @return SystemInfo
     */
    public static SystemInfo getTESSystemInfo()
    {
        SystemInfo info = new SystemInfo();

        String OpSysName = System.getProperty("os.name");

        info.setOSName(OpSysName);

        String OpSysVersion = System.getProperty("os.version");

        info.setOSVersion(OpSysVersion);

        String jreSpecVersion = System.getProperty("java.specification.version");

        info.setJRESpecVersion(jreSpecVersion);

        String jreImplVersion = System.getProperty("java.version");

        info.setJREImplVersion(jreImplVersion);

        String jreImplVendor = System.getProperty("java.vendor");

        info.setJREImplVendor(jreImplVendor);

        String jvmSpecVersion = System.getProperty("java.vm.specification.version");

        info.setJVMSpecVersion(jvmSpecVersion);

        String jvmImplVersion = System.getProperty("java.vm.version");

        info.setJVMImplVersion(jvmImplVersion);

        String jvmImplVendor = System.getProperty("java.vm.vendor");

        info.setJVMImplVendor(jvmImplVendor);

        String jvmImplName = System.getProperty("java.vm.name");

        info.setJVMImplName(jvmImplName);
        return info;
    }

    /**
     * Gets the preformance statistics of the FES Server.
     *
     * @return TESPerformanceStats
     */
    public static TESPerformanceStats getTESPerformanceStats()
    {
        // in Kb
        long freeMemory = (Runtime.getRuntime().freeMemory()) / 1024;
        // in Kb
        long totalMemory = (Runtime.getRuntime().totalMemory()) / 1024;

        long maxMemory = (Runtime.getRuntime().maxMemory()) / 1024;
        String strMemUsage = (totalMemory - freeMemory) + "K" + "/" + maxMemory + "K";
        //same as thread.activeCount()
        int threadCount = Thread.currentThread().getThreadGroup().activeCount();
        int processCount = 1;

        TESPerformanceStats performanceStats = new TESPerformanceStats();

        performanceStats.setFreeMemory(freeMemory);
        performanceStats.setTotalMemory(totalMemory);
        performanceStats.setMemoryUsage(strMemUsage);
        performanceStats.setTotalProcessCount(processCount);
        performanceStats.setTotalThreadCount(threadCount);
        performanceStats.setCpuUtilization(SystemUtil.getProcessCPUUtilization());
        return performanceStats;
    }
}

