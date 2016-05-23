/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.microservice.ccp.event.component;

import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

public class HandShakeAckEvent extends ControlEvent {
    private boolean ccpSupported = false;
    private double minVersionSupported = 1.0;
    private double maxVersionSupported = 1.0;
    private String comment = "";

    /**
     * Returns the event type for the event represented by this object.
     * @return {@link com.fiorano.openesb.microservice.ccp.event.CCPEventType} - An enumeration constant representing handshake acknowledgement event from component i.e. {@link CCPEventType#HANDSHAKE_ACK}
     */
    public CCPEventType getEventType() {
        return CCPEventType.HANDSHAKE_ACK;
    }

    /**
     * Specifies whether component supports CCP or not.
     * @return boolean - true if CCP is supported by the component, false otherwise
     * @see #setCcpSupported(boolean)
     */
    public boolean isCcpSupported() {
        return ccpSupported;
    }

    /**
     * Sets the CCP supported value. If component process supports CCP, communication between
     * component process and Peer Server is done via CCP channel.
     * @param ccpSupported true if CCP is supported by the component, false otherwise
     * @see #isCcpSupported()
     */
    public void setCcpSupported(boolean ccpSupported) {
        this.ccpSupported = ccpSupported;
    }

    /**
     * Returns the minimum version of CCP supported by the component
     * @return double - Minimum version of CCP supported by the component
     * @see #setMinVersionSupported(double)
     */
    public double getMinVersionSupported() {
        return minVersionSupported;
    }

    /**
     * Sets the minimum version of CCP supported by the component. Default value is '1.0'.
     * @param minVersionSupported Minimum version of CCP supported by the component
     * @see #getMinVersionSupported()
     */
    public void setMinVersionSupported(double minVersionSupported) {
        this.minVersionSupported = minVersionSupported;
    }

    /**
     * Returns the maximum version of CCP supported by the component
     * @return double - Maximum version of CCP supported by the component
     * @see #setMaxVersionSupported(double)
     */
    public double getMaxVersionSupported() {
        return maxVersionSupported;
    }

    /**
     * Sets the maximum version of CCP supported by the component. Default value is '1.0'.
     * @param maxVersionSupported Maximum version of CCP supported by the component
     * @see #getMaxVersionSupported()
     */
    public void setMaxVersionSupported(double maxVersionSupported) {
        this.maxVersionSupported = maxVersionSupported;
    }

    /**
     * Returns the comment value passed with this event.
     * @return String - Comment value passed with this event
     * @see #setComment(String)
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment to the specified string
     * @param comment Comment
     * @see #getComment() 
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this event object.
     * @param bytesMessage bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);
        minVersionSupported = bytesMessage.readDouble();
        maxVersionSupported = bytesMessage.readDouble();
        ccpSupported = bytesMessage.readBoolean();
        comment = bytesMessage.readUTF();
    }

    /**
     * Writes this event object to the bytesMessage.
     * @param bytesMessage bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    @Override
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.writeDouble(minVersionSupported);
        bytesMessage.writeDouble(maxVersionSupported);
        bytesMessage.writeBoolean(ccpSupported);
        bytesMessage.writeUTF(comment);
    }

    /**
     * Returns a string representation of the object.
     * @return String - representation of the object as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Hand-shake Acknowledgement Event Properties");
        builder.append("-------------------------------------");
        builder.append(super.toString());
        builder.append(" CCP Supported : ").append(ccpSupported);
        builder.append(" Min CCP Version Supported : ").append(minVersionSupported);
        builder.append(" Max CCP Version Supported : ").append(maxVersionSupported);
        builder.append(" Comment : ").append(comment);
        return builder.toString();
    }
}
