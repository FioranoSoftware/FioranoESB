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

public class ComponentStats extends Data {

    public DataType getDataType() {
        return DataType.COMPONENT_STATS;
    }

    private String value;

    /**
     * This method initializes an object of this class
     * @param value Component Statistics of the component 
     */
    public ComponentStats(String value) {
        this.value = value;
    }


    /**
     * Default Constructor
     */
    @SuppressWarnings("UnusedDeclaration")
    public ComponentStats() {
    }

    /**
     * Returns the stats of the component  as a String
     * @return String - stats of component 
     */
    public String toString() {
        return this.value;
    }

    /**
     * Returns the stats of the component  if the component is not launched in memory.
     *
     * @return String - stats of component 
     * @see #setValue(String)
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the stats of the component 
     *
     * @param value stats of the component 
     * @see #getValue()
     */
    public void setValue(String value) {
        this.value = value;
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
