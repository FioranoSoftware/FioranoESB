/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.component;

import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataEvent;
import com.fiorano.openesb.microservice.ccp.event.common.RequestEventFactory;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collection;

public class LookupConfigEvent extends ControlEvent {
    protected Collection<String > dataIdentifiers;
    private long repetitionCount;
    private long interval;

    /**
     * Initializes new data request event
     */
    public LookupConfigEvent() {
        dataIdentifiers = new ArrayList<String>();
    }

    /**
     * Returns the event type for the event represented by this object.
     * @return {@link com.fiorano.openesb.microservice.ccp.event.CCPEventType} - An enumeration constant representing the data request event from Peer Server i.e. {@link CCPEventType#DATA_REQUEST}
     */
    public CCPEventType getEventType() {
        return CCPEventType.LOOKUP_CONFIG;
    }

    /**
     * Returns a collection of data requests contained within this event. Each request is identified
     * by a data identifier. The response to all of these requests should be wrapped in
     * {@link DataEvent} by the component process and sent as a single event to Peer Server.
     * @return Collection - Set of data identifiers
     * @see #setDataIdentifiers(java.util.Collection)
     */
    public Collection<String> getDataIdentifiers() {
        return dataIdentifiers;
    }

    /**
     * Sets the data requests that a component should process.
     * @param dataIdentifiers Collection of data identifiers
     * @see #getDataIdentifiers()
     */
    public void setDataIdentifiers(Collection<String> dataIdentifiers) {
        this.dataIdentifiers = dataIdentifiers;
    }

    /**
     * Returns the number of times a response to this event should be sent by component process. Each response should be
     * separated with a time-gap of {@link #getInterval()} milliseconds.
     * @return long - Number of responses to be sent by component process to Peer Server for this request
     * @see #setRepetitionCount(long)
     */
    public long getRepetitionCount() {
        return repetitionCount;
    }

    /**
     * Sets the number of responses to specified value.
     * @param repetitionCount Number of responses to be sent by component process to Peer Server for this request
     * @see #getRepetitionCount()
     */
    public void setRepetitionCount(long repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    /**
     * Returns the time value in milliseconds between two consecutive responses to this command. This value is applicable only in cases
     * where the value returned by {@link #getRepetitionCount()} is greater than 1.
     * @return long - Time value in milliseconds between two consecutive responses to this command
     * @see #setInterval(long)
     */
    public long getInterval() {
        return interval;
    }

    /**
     * Sets the time-gap in milliseconds between 2 consecutive responses to this command/request.
     * @param interval Time-gap in milliseconds between two consecutive responses to this command
     * @see #getInterval()
     */
    public void setInterval(long interval) {
        this.interval = interval;
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
        repetitionCount = bytesMessage.readLong();
        interval = bytesMessage.readLong();

        int size = bytesMessage.readInt();
        for (int i = 0; i < size; i++) {
            if(dataIdentifiers == null)
                dataIdentifiers = new ArrayList<>();

            dataIdentifiers.add(String.valueOf(bytesMessage.readUTF()));
        }
    }

    /**
     * Writes this event object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    @Override
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.setIntProperty("Request Type", RequestEventFactory.DATA_REQUEST_EVENT);

        super.toMessage(bytesMessage);
        bytesMessage.writeLong(repetitionCount);
        bytesMessage.writeLong(interval);

        if (dataIdentifiers != null) {
            bytesMessage.writeInt(dataIdentifiers.size());
            for(String  id:dataIdentifiers)
                bytesMessage.writeUTF(id);
        } else
            bytesMessage.writeInt(-1);
    }

    /**
     * Returns a string representation of the object.
     * @return String - representation of the object as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Data Request Event Properties");
        builder.append("-------------------------------------");
        builder.append(super.toString());
        builder.append(" Data Identifiers : ").append(dataIdentifiers != null ? dataIdentifiers.toString() : "");
        builder.append(" repetition Count : ").append(repetitionCount);
        builder.append(" Interval between consecutive events : ").append(interval);
        return builder.toString();
    }
}
