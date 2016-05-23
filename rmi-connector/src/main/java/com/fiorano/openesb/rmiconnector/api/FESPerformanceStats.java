/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class FESPerformanceStats implements Serializable {

    private static final long serialVersionUID = -5962694989534118145L;
    /** in kb unit **/
    private long    freeMemoryInKB;
    /** in kb unit **/
    private long    totalMemoryInKB;
    private String  memoryUsageInKB;
    private int     totalThreadCount;
    private int     totalProcessCount;

    private Double  cpuUtilization;


    /* -------------------------------------------------------Setters ----------------------------------*/
    /**
     * This method sets the Free Memory in KB
     *
     * @param freeMemoryInKB Free memory in KB
     */
    public void setFreeMemoryInKB(long freeMemoryInKB) {
        this.freeMemoryInKB = freeMemoryInKB;
    }

    /**
     * This method sets the Total Memory in KB
     *
     * @param totalMemoryInKB Total memory in KB
     */
    public void setTotalMemoryInKB(long totalMemoryInKB) {
        this.totalMemoryInKB = totalMemoryInKB;
    }

    /**
     * This method sets the total Process Count
     *
     * @param totalProcessCount Total Process Count
     */
    public void setTotalProcessCount(int totalProcessCount) {
        this.totalProcessCount = totalProcessCount;
    }

    /**
     * This method sets the total Thread Count
     *
     * @param totalThreadCount Total Thread Count
     */
    public void setTotalThreadCount(int totalThreadCount) {
        this.totalThreadCount = totalThreadCount;
    }

    /**
     * This method sets the Memory Usage in KB
     *
     * @param memoryUsageInKB Memory Usage in KB
     */
    public void setMemoryUsageInKB(String memoryUsageInKB) {
        this.memoryUsageInKB = memoryUsageInKB;
    }

    /**
     * This method sets the cpu utilization
     *
     * @param cpuUtilization CPU Utilization
     */
    public void setCpuUtilization(Double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    /* -------------------------------------------------------Getters ----------------------------------*/

    /**
     * This method returns the Free Memory in KB
     *
     * @return long - Free Memory in KB
     */
    public long getFreeMemoryInKB() {
        return freeMemoryInKB;
    }

    /**
     * This method returns the Total Memory in KB
     *
     * @return long - Total Memory in KB
     */
    public long getTotalMemoryInKB() {
        return totalMemoryInKB;
    }

    /**
     * This method returns the Memory Usage in KB
     *
     * @return long - Memory Usage in KB
     */
    public String getMemoryUsageInKB() {
        return memoryUsageInKB;
    }

    /**
     * This method returns the total Thread Count
     *
     * @return int - total Thread Count
     */
    public int getTotalThreadCount() {
        return totalThreadCount;
    }

    /**
     * This method returns the total Process Count
     *
     * @return int - total Process Count
     */
    public int getTotalProcessCount() {
        return totalProcessCount;
    }

    /**
     * This method returns the CPU Utilization
     *
     * @return Double - CPU Utilization
     */
    public Double getCpuUtilization() {
        return cpuUtilization;
    }
}
