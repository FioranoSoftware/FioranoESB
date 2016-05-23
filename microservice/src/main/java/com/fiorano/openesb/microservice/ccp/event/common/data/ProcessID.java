/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
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

public class ProcessID extends Data {
    private String value;

    /**
     * This method initializes an object of this class
     * @param value ProcessID of the component JVM
     */
    public ProcessID(String value) {
        this.value = value;
    }


    /**
     * Default Constructor
     */
    @SuppressWarnings("UnusedDeclaration")
    public ProcessID() {
    }

    /**
     * Returns the process ID of the component JVM as a String
     * @return String - Process ID of component JVM
     */
    public String toString() {
        return this.value;
    }

    /**
     * Returns the Process ID of the component JVM if the component is not launched in memory.
     *
     * @return String - Process ID of component JVM
     * @see #setValue(String)
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the Process ID of the component JVM
     *
     * @param value Process ID of the component JVM
     * @see #getValue()
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the data type for the data represented by this object.
     *
     * @return DataType - Data type value for this data object i.e. i.e. {@link com.fiorano.openesb.microservice.ccp.event.common.data.Data.DataType#PID}
     * @see com.fiorano.openesb.microservice.ccp.event.common.data.Data.DataType
     */
    @Override
    public DataType getDataType() {
        return DataType.PID;
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this data object.
     *
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        value = bytesMessage.readUTF();
    }

    /**
     * Writes this data object to the bytesMessage.
     *
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeUTF(value);
    }

    /**
     * Reads the values from the data input stream and sets the properties of this data object.
     *
     * @param in Input data stream
     * @throws IOException If an exception occurs while reading values from the stream
     * @see #toStream(java.io.DataOutput)
     */
    public void fromStream(DataInput in) throws IOException {
        value = in.readUTF();
    }

    /**
     * Writes this data object to the data stream.
     *
     * @param out Output data stream
     * @throws IOException If an exception occurs while writing value to the stream
     * @see #fromStream(java.io.DataInput)
     */
    public void toStream(DataOutput out) throws IOException {
        out.writeUTF(value);
    }
}
