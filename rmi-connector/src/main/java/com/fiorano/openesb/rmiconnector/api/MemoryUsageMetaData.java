/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;
import java.math.BigDecimal;

public class MemoryUsageMetaData implements Serializable {


    private static final long serialVersionUID = -2978352936750388123L;
    private double heapMemoryUsed;
    private double heapMemoryAllocated;
    private double nonHeapMemoryUsed;
    private double nonHeapMemoryAllocated;

    /**
     *  Default Constructor
     */
    public MemoryUsageMetaData() {
        //To change body of created methods use File | Settings | File Templates.
    }

    /**
     *  This constructor is used to set values for objects of this class
     *  @param heapMemoryUsed Heap memory currently being used by the JVM
     *  @param heapMemoryAllocated Heap memory allocated to the JVM
     *  @param nonHeapMemoryUsed Non-Heap memory currently being used by JVM
     *  @param nonHeapMemoryAllocated Non-Heap memory allocated to the JVM
     */
    public MemoryUsageMetaData(Long heapMemoryUsed, Long heapMemoryAllocated, Long nonHeapMemoryUsed, Long nonHeapMemoryAllocated){
        this.heapMemoryUsed         = heapMemoryUsed/1024d;
        this.heapMemoryAllocated    = heapMemoryAllocated/1024d;
        this.nonHeapMemoryUsed      = nonHeapMemoryUsed/1024d;
        this.nonHeapMemoryAllocated = nonHeapMemoryAllocated/1024d;
    }
    public MemoryUsageMetaData(String memoryUsage){
        String []memUsage=memoryUsage.split(";");
        String []heapmemUsage=memUsage[0].split("/");
        String []nonheapmemUsage=memUsage[1].split("/");
        heapMemoryUsed          = Double.parseDouble(heapmemUsage[0].substring(0, heapmemUsage[0].length() - 1));
        heapMemoryAllocated     = Double.parseDouble(heapmemUsage[1].substring(0, heapmemUsage[1].length() - 1));
        nonHeapMemoryUsed       = Double.parseDouble(nonheapmemUsage[0].substring(0, nonheapmemUsage[0].length() - 1));
        nonHeapMemoryAllocated  = Double.parseDouble(nonheapmemUsage[1].substring(0, nonheapmemUsage[1].length() - 1));
    }

    /**
     * This method returns the heap memory (in bytes) being used by the JVM at the time of call.
     * @return double - Heap Memory Used (in bytes)
     * @see #setHeapMemoryUsed(long)
     */
    public double getHeapMemoryUsed() {
        return heapMemoryUsed;
    }

    /**
     * This method is used to inform the Peer Server of the amount of heap memory currently being used by the component JVM (in bytes).
     * @param heapMemoryUsed Heap Memory Used (in bytes)
     * @see #getHeapMemoryUsed()
     */
    public void setHeapMemoryUsed(long heapMemoryUsed) {
        this.heapMemoryUsed = heapMemoryUsed;
    }

    /**
     * This method returns the allocated Heap Memory value (in bytes)
     * @return double - Heap Memory Allocated (in bytes)
     * @see #setHeapMemoryAllocated(double)
     */
    public double getHeapMemoryAllocated() {
        return heapMemoryAllocated;
    }

    /**
     * This method is used to inform Peer Server of the amount of heap memory currently allocated to component JVM (in bytes).
     * @param heapMemoryAllocated Heap Memory Allocated (in bytes)
     * @see #getHeapMemoryAllocated()
     */
    public void setHeapMemoryAllocated(double heapMemoryAllocated) {
        this.heapMemoryAllocated = heapMemoryAllocated;
    }

    /**
     * This method returns the non-heap memory (in bytes) being currently used by the VM.
     * @return double - Heap memory used (in bytes)
     * @see #setNonHeapMemoryUsed(double)
     */
    public double getNonHeapMemoryUsed() {
        return nonHeapMemoryUsed;
    }

    /**
     * This method is used to inform Peer Server of the amount of non - heap memory currently being used by component JVM (in bytes)
     * @param nonHeapMemoryUsed Heap Memory Used (in bytes)
     * @see #getNonHeapMemoryUsed()
     */
    public void setNonHeapMemoryUsed(double nonHeapMemoryUsed) {
        this.nonHeapMemoryUsed = nonHeapMemoryUsed;
    }

    /**
     * This method returns the allocated non-heap Memory value (in bytes)
     * @return double - Heap Memory Allocated (in bytes)
     * @see #setNonHeapMemoryAllocated(double)
     */
    public double getNonHeapMemoryAllocated() {
        return nonHeapMemoryAllocated;
    }

    /**
     * This method is used to inform Peer Server of the amount of non - heap memory currently allocated to component JVM (in bytes).
     * @param nonHeapMemoryAllocated Heap Memory Allocated (in bytes)
     * @see #getNonHeapMemoryAllocated()
     */
    public void setNonHeapMemoryAllocated(double nonHeapMemoryAllocated) {
        this.nonHeapMemoryAllocated = nonHeapMemoryAllocated;
    }

    /**
     *  This method converts memory usage and allocation values to String (internal calls specific)
     *  @return String - Memory usage details as String
     */
    public String toString(){
        String memoryUsageDetails;

        memoryUsageDetails  = "Heap Memory Used          : "+round(heapMemoryUsed);
        memoryUsageDetails += "\nHeap Memory Allocated     : "+round(heapMemoryAllocated);
        memoryUsageDetails += "\nNon HeapMemory Used       : "+round(nonHeapMemoryUsed );
        memoryUsageDetails += "\nNon Heap Memory Allocated : "+round(nonHeapMemoryAllocated );
        return memoryUsageDetails;
    }

    /**
     *  This method converts decimal values of memory usage to round figures (internal calls specific)
     *  @return double - Memory usage details as round figure double
     */
    public static double round(double memory)
    {
        int precision =2,roundingMode= BigDecimal.ROUND_HALF_UP;
        memory=memory/1024d;//to MB from KB

        BigDecimal bd = new BigDecimal(memory);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }


}
