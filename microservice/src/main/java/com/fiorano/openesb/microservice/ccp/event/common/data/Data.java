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
import java.io.Serializable;

public abstract class Data implements Serializable {

    /**
     * This enumeration defines a unique constant for each implementation of {@link Data} class.
     * Each {@link Data} class corresponds to a {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent.DataIdentifier}.
     * @author FSTPL
     * @version 10
     */
    public enum DataType {
        /**
         * Constant representing data of type {@link MemoryUsage}
         */
        COMPONENT_MEMORY_USAGE,

        /**
         * Constant representing data of type {@link LogLevel}
         */
        LOG_LEVEL,

        /**
         * Constant representing data of type {@link ProcessID}
         */
        PID,

        /**
         * Constant representing data of type {@link ComponentStats}
         */
        COMPONENT_STATS,

        COMPONENT_CONFIGURATION,
        NAMED_CONFIGURATION,
        PORT_CONFIGURATION,
        MANAGEABLE_PROPERTIES,
    }

    /**
     * Returns the data type for the data represented by this object.
     * @return DataType - Data type value for this data object
     * @see com.fiorano.openesb.microservice.ccp.event.common.data.Data.DataType
     */
    public abstract DataType getDataType();

    /**
     * Reads the values from the bytesMessage and sets the properties of this data object.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    public abstract void fromMessage(BytesMessage bytesMessage) throws JMSException;

    /**
     * Writes this data object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public abstract void toMessage(BytesMessage bytesMessage) throws JMSException;

    /**
     * Reads the values from the data input stream and sets the properties of this data object.
     * @param in Data input stream
     * @throws IOException If an exception occurs while reading values from the stream
     * @see #toStream(java.io.DataOutput)
     */
    public abstract void fromStream(DataInput in) throws IOException;

    /**
     * Writes this data object to the data stream.
     * @param out Output data stream
     * @throws IOException If an exception occurs while writing value to the stream
     * @see #fromStream(java.io.DataInput)
     */
    public abstract void toStream(DataOutput out) throws IOException;

    /**
     * Factory method to obtain a new instance of {@link Data} as per the argument passed to the method.
     * @param dataType Type of {@link Data} to be returned.
     * @return DataType - Data type represented by the passed argument
     * @exception IllegalArgumentException if the data type is not found.
     */
    public static Data getDataObject(DataType dataType) {
        switch(dataType){
            case COMPONENT_MEMORY_USAGE:
                return new MemoryUsage();
            case LOG_LEVEL:
                return new LogLevel();
            case PID:
                return new ProcessID();
            case COMPONENT_STATS:
                return new ComponentStats();
            case COMPONENT_CONFIGURATION:
                return new MicroserviceConfiguration();
            case NAMED_CONFIGURATION:
                return new NamedConfiguration();
            case MANAGEABLE_PROPERTIES:
                return new ManageableProperties();
            case PORT_CONFIGURATION:
                return new PortConfiguration();
        }
        throw new IllegalArgumentException("INVALID_DATA_TYPE - " + dataType.name());
    }
}
