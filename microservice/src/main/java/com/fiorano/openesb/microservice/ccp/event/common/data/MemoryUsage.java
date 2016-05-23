/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.common.data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MemoryUsage extends Data {
    private long heapMemoryUsed;
    private long heapMemoryAllocated;
    private long nonHeapMemoryUsed;
    private long nonHeapMemoryAllocated;

    /**
     * Initializes an object of MemoryUsage
     * @param heapMemoryUsed
     * @param heapMemoryAllocated
     * @param nonHeapMemoryUsed
     * @param nonHeapMemoryAllocated
     */
    public MemoryUsage(long heapMemoryUsed, long heapMemoryAllocated, long nonHeapMemoryUsed, long nonHeapMemoryAllocated) {
        this.heapMemoryUsed = heapMemoryUsed;
        this.heapMemoryAllocated = heapMemoryAllocated;
        this.nonHeapMemoryUsed = nonHeapMemoryUsed;
        this.nonHeapMemoryAllocated = nonHeapMemoryAllocated;
    }

    /**
     * Default Constructor
     */
    public MemoryUsage() {
    }

    /**
     * This method is not implemented
     * @param nonHeapMemoryUsage
     */
    public void setNonHeapMemoryUsage(String nonHeapMemoryUsage) {
        String[] TMPMemoryUsage = nonHeapMemoryUsage.split("/");
        nonHeapMemoryUsed = Long.parseLong(TMPMemoryUsage[0].substring(0, TMPMemoryUsage[0].length() - 1));
        nonHeapMemoryAllocated = Long.parseLong(TMPMemoryUsage[1].substring(1, TMPMemoryUsage[1].length() - 1));
    }

    /**
     * This method is not implemented
     * @param heapMemoryUsage
     */
    public void setHeapMemoryUsage(String heapMemoryUsage) {
        String[] TMPMemoryUsage = heapMemoryUsage.split("/");
        heapMemoryUsed = Long.parseLong(TMPMemoryUsage[0].substring(0, TMPMemoryUsage[0].length() - 1));
        heapMemoryAllocated = Long.parseLong(TMPMemoryUsage[1].substring(1, TMPMemoryUsage[1].length() - 1));
    }

    /**
     * This method returns a String representation of the Object
     * @return String - Representation of the Object as a String
     */
    public String toString() {
        StringBuffer strbuff = new StringBuffer(this.heapMemoryUsed + "");
        strbuff.append("K/");
        strbuff.append(heapMemoryAllocated);
        strbuff.append("K;");
        strbuff.append(nonHeapMemoryUsed);
        strbuff.append("K/");
        strbuff.append(nonHeapMemoryAllocated);
        strbuff.append("K");
        return strbuff.toString();
    }

    /**
     * Returns the heap memory (in bytes) being currently used by the VM.
     * @return long - Heap memory used (in bytes)
     * @see #setHeapMemoryUsed(long)
     */
    public long getHeapMemoryUsed() {
        return heapMemoryUsed;
    }

    /**
     * This method tells the Peer Server the amount of heap memory currently being used by JVM (in bytes).
     * @param heapMemoryUsed Heap Memory Used (in bytes)
     * @see #getHeapMemoryUsed()
     */
    public void setHeapMemoryUsed(long heapMemoryUsed) {
        this.heapMemoryUsed = heapMemoryUsed;
    }

    /**
     * Gets the allocated Heap Memory value (in bytes)
     * @return long - Heap Memory Allocated (in bytes)
     * @see #setHeapMemoryAllocated(long)
     */
    public long getHeapMemoryAllocated() {
        return heapMemoryAllocated;
    }

    /**
     * This method tells the Peer Server the amount of heap memory currently allocated to the JVM (in bytes).
     * @param heapMemoryAllocated Heap Memory Allocated (in bytes)
     * @see #getHeapMemoryAllocated()
     */
    public void setHeapMemoryAllocated(long heapMemoryAllocated) {
        this.heapMemoryAllocated = heapMemoryAllocated;
    }

    /**
     * Returns the non-heap memory (in bytes) currently being used by the VM.
     * @return long - Heap memory used (in bytes)
     * @see #setNonHeapMemoryUsed(long)
     */
    public long getNonHeapMemoryUsed() {
        return nonHeapMemoryUsed;
    }

    /**
     *This method tells the Peer Server the amount of non-heap memory currently being used by the JVM (in bytes).
     * @param nonHeapMemoryUsed Heap Memory Used (in bytes)
     * @see #getNonHeapMemoryUsed()
     */
    public void setNonHeapMemoryUsed(long nonHeapMemoryUsed) {
        this.nonHeapMemoryUsed = nonHeapMemoryUsed;
    }

    /**
     * Gets the allocated non-heap Memory value (in bytes)
     * @return long - Heap Memory Allocated (in bytes)
     * @see #setNonHeapMemoryAllocated(long)
     */
    public long getNonHeapMemoryAllocated() {
        return nonHeapMemoryAllocated;
    }

    /**
     * This method tells the Peer Server the amount of non-heap memory currently allocated to the JVM (in bytes).
     * @param nonHeapMemoryAllocated Heap Memory Allocated (in bytes)
     * @see #getNonHeapMemoryAllocated()
     */
    public void setNonHeapMemoryAllocated(long nonHeapMemoryAllocated) {
        this.nonHeapMemoryAllocated = nonHeapMemoryAllocated;
    }

    /**
     * Returns the data type for the data represented by this object.
     * @return DataType - Data type value for this data object i.e. i.e. {@link com.fiorano.openesb.microservice.ccp.event.common.data.Data.DataType#COMPONENT_MEMORY_USAGE}
     * @see com.fiorano.openesb.microservice.ccp.event.common.data.Data.DataType
     */
    @Override
    public DataType getDataType() {
        return DataType.COMPONENT_MEMORY_USAGE;
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this data object.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        heapMemoryUsed = bytesMessage.readLong();
        heapMemoryAllocated = bytesMessage.readLong();
        nonHeapMemoryUsed = bytesMessage.readLong();
        nonHeapMemoryAllocated = bytesMessage.readLong();
    }

    /**
     * Writes this data object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeLong(heapMemoryUsed);
        bytesMessage.writeLong(heapMemoryAllocated);
        bytesMessage.writeLong(nonHeapMemoryUsed);
        bytesMessage.writeLong(nonHeapMemoryAllocated);
    }

    /**
     * Reads the values from the data input stream and sets the properties of this data object.
     * @param in Input data stream
     * @throws IOException If an exception occurs while reading values from the stream
     * @see #toStream(java.io.DataOutput)
     */
    public void fromStream(DataInput in) throws IOException {
        heapMemoryUsed = in.readLong();
        heapMemoryAllocated = in.readLong();
        nonHeapMemoryUsed = in.readLong();
        nonHeapMemoryAllocated = in.readLong();
    }

    /**
     * Writes this data object to the data stream.
     * @param out Output data stream
     * @throws IOException If an exception occurs while writing value to the stream
     * @see #fromStream(java.io.DataInput)
     */
    public void toStream(DataOutput out) throws IOException {
        out.writeLong(heapMemoryUsed);
        out.writeLong(heapMemoryAllocated);
        out.writeLong(nonHeapMemoryUsed);
        out.writeLong(nonHeapMemoryAllocated);
    }
}
