/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class TESPerformanceStats extends DmiObject
{
    // in kb unit
    private long    m_lFreeMemory;
    // in kb unit
    private long    m_lTotalMemory;
    private String m_strMemoryUsage;
    private int     m_iTotalThreadCount;
    private int     m_iTotalProcessCount;

    private Double cpuUtilization;

    /**
     *  Constructor for the TESPerformanceStats object
     */
    public TESPerformanceStats()
    {
    }


    /**
     *  Gets the free memory value from this <code>TESPerformanceStats</code>
     *  object.
     *
     * @return The free memory value
     * @see #setFreeMemory(long)
     * @since Tifosi2.0
     */
    public long getFreeMemory()
    {
        return m_lFreeMemory;
    }


    /**
     *  Gets the value of total Memory from this <code>TESPerformanceStats</code>
     *  object.
     *
     * @return The value of total Memory
     * @see #setTotalMemory(long)
     * @since Tifosi2.0
     */
    public long getTotalMemory()
    {
        return m_lTotalMemory;
    }


    /**
     *  Gets the value of memory usage from this object of <code>TESPerformanceStats</code>
     *  .
     *
     * @return The value of memory usage
     * @see #setMemoryUsage(java.lang.String)
     * @since Tifosi2.0
     */
    public String getMemoryUsage()
    {
        return m_strMemoryUsage;
    }


    /**
     *  Gets the total thread count for the FPS, for which this object
     *  represents performance statistics.
     *
     * @return The total thread count value
     * @see #setTotalThreadCount(int)
     * @since Tifosi2.0
     */
    public int getTotalThreadCount()
    {
        return m_iTotalThreadCount;
    }


    /**
     *  Gets the total process count for the TES, for which this object
     *  represents performance statistics.
     *
     * @return The total process count
     * @see #setTotalProcessCount(int)
     * @since Tifosi2.0
     */
    public int getTotalProcessCount()
    {
        return m_iTotalProcessCount;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.TES_PERFORMANCE_STATS;
    }


    /**
     *  Sets the freeMemory attribute of the TESPerformanceStats object
     *
     * @param freeMemory The new freeMemory value
     */
    public void setFreeMemory(long freeMemory)
    {
        m_lFreeMemory = freeMemory;
    }


    /**
     *  Sets the total Memory for this <code>TESPerformanceStats</code> object
     *  to the specified <code>totalMemory</code> argument.
     *
     * @param totalMemory The long to be set as total Memory
     * @see #getTotalMemory()
     * @since Tifosi2.0
     */
    public void setTotalMemory(long totalMemory)
    {
        m_lTotalMemory = totalMemory;
    }


    /**
     *  Sets the memory usage for this <code>TESPerformanceStats</code> object
     *  to the specified <code>memoryUsage</code> argument.
     *
     * @param memoryUsage The long to be set as memory usage
     * @see #getMemoryUsage()
     * @since Tifosi2.0
     */
    public void setMemoryUsage(String memoryUsage)
    {
        m_strMemoryUsage = memoryUsage;
    }


    /**
     *  Sets the total thread count for the FPS, for which this object
     *  represents performance statistics, to the specified <code>totalThreadCount</code>
     *  argument.
     *
     * @param totalThreadCount The integer to be set as total thread count
     * @see #getTotalThreadCount()
     * @since Tifosi2.0
     */
    public void setTotalThreadCount(int totalThreadCount)
    {
        m_iTotalThreadCount = totalThreadCount;
    }


    /**
     *  Sets the total process count for the TES, for which this object
     *  represents performance statistics, to the specified <code>totalProcessCount</code>
     *  argument.
     *
     * @param totalProcessCount The integer to be set as total process count
     * @see #getTotalProcessCount()
     * @since Tifosi2.0
     */
    public void setTotalProcessCount(int totalProcessCount)
    {
        m_iTotalProcessCount = totalProcessCount;
    }

    /**
     * returns the Cpu Utilization by FES server process
     * @return cpuUtilization
     */
    public Double getCpuUtilization() {
        return cpuUtilization;
    }

    /**
     * Sets the CPU Utilization by the FES server process.
     * @param cpuUtilization float representing cpu utilization by the server
     */

    public void setCpuUtilization(Double cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }

    /**
     *  This method has not been implemented in this version. (Resets the values
     *  of the data members of this object.)
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method tests whether this <code>TESPerformanceStats</code> object
     *  has the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method reads this object <code>TESPerformanceStats</code> from the
     *  specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception java.io.IOException IOException that might be thrown while reading from stream
     * @since Tifosi2.0
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        m_lFreeMemory = is.readLong();
        m_lTotalMemory = is.readLong();
        m_strMemoryUsage = readUTF(is);
        m_iTotalThreadCount = is.readInt();
        m_iTotalProcessCount = is.readInt();
        cpuUtilization = is.readDouble();
    }


    /**
     *  This method is called to write this <code>TESPerformanceStats</code>
     *  object to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception java.io.IOException IOException that might be thrown while writing to stream
     * @since Tifosi2.0
     */
    public void toStream(java.io.DataOutput out, int versionNo)
        throws java.io.IOException
    {
        out.writeLong(m_lFreeMemory);
        out.writeLong(m_lTotalMemory);
        writeUTF(out, m_strMemoryUsage);
        out.writeInt(m_iTotalThreadCount);
        out.writeInt(m_iTotalProcessCount);
        out.writeDouble(cpuUtilization);
    }


    /**
     *  This method returns the String representation of this <code>TESPerformanceStats</code>
     *  object.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("TES  performance stats.");
        strBuf.append("[");
        strBuf.append("Free memory = ");
        strBuf.append(String.valueOf(m_lFreeMemory));
        strBuf.append(", ");
        strBuf.append("Total memory = ");
        strBuf.append(String.valueOf(m_lTotalMemory));
        strBuf.append(", ");
        strBuf.append("Memory Usage = ");
        strBuf.append(m_strMemoryUsage);
        strBuf.append(", ");
        strBuf.append("Total thread count = ");
        strBuf.append(String.valueOf(m_iTotalThreadCount));
        strBuf.append(", ");
        strBuf.append("Total process count = ");
        strBuf.append(String.valueOf(m_iTotalProcessCount));
        strBuf.append(", ");
        strBuf.append("CPU Utilization = ");
        strBuf.append(String.valueOf(cpuUtilization));
        strBuf.append("]");
        return strBuf.toString();
    }
}
