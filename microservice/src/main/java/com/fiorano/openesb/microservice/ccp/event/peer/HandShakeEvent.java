/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.peer;


import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

public class HandShakeEvent extends ControlEvent {
    private double minVersionSupported = 1.0;
    private double maxVersionSupported = 1.0;

    /**
     * Returns the event type for the event represented by this object.
     * @return {@link com.fiorano.openesb.microservice.ccp.event.CCPEventType} - An enumeration constant representing handshake initiation event i.e. {@link CCPEventType#HANDSHAKE_INITIATE}
     */
    public CCPEventType getEventType() {
        return CCPEventType.HANDSHAKE_INITIATE;
    }

    /**
     * Returns the minimum version of CCP supported by Peer Server.
     * @return double - Minimum version of CCP supported by Peer Server.
     * @see #setMinVersionSupported(double)
     */
    public double getMinVersionSupported() {
        return minVersionSupported;
    }

    /**
     * Sets minimum version of CCP supported for this event. This method is used by Peer Server while sending handshake event to component.
     * @param minVersionSupported Minimum version of CCP supported
     * @see #getMinVersionSupported()
     */
    public void setMinVersionSupported(double minVersionSupported) {
        this.minVersionSupported = minVersionSupported;
    }

    /**
     * Returns the maximum version of CCP supported by Peer Server.
     * @return double - Maximum version of CCP supported by Peer Server.
     * @see #setMaxVersionSupported(double)
     */
    public double getMaxVersionSupported() {
        return maxVersionSupported;
    }

    /**
     * Sets the maximum version of CCP supported for this event. This method is used by Peer Server while sending handshake event to component.
     * @param maxVersionSupported Maximum version of CCP supported
     * @see #getMaxVersionSupported() 
     */
    public void setMaxVersionSupported(double maxVersionSupported) {
        this.maxVersionSupported = maxVersionSupported;
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this event object.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);
        minVersionSupported = bytesMessage.readDouble();
        maxVersionSupported = bytesMessage.readDouble();
    }

    /**
     * Writes this event object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    @Override
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.writeDouble(minVersionSupported);
        bytesMessage.writeDouble(maxVersionSupported);
    }

    /**
     * Returns a string representation of the object.
     * @return String - Representation of the object as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HandShake Event Properties");
        builder.append("-------------------------------------");
        builder.append(super.toString());
        builder.append(" Min Supported CCP Version : ").append(minVersionSupported);
        builder.append(" Max Supported CCP Version : ").append(maxVersionSupported);
        return builder.toString();
    }
}
