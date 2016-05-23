/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.common;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collection;

public class LogLevelRequestEvent extends DataRequestEvent {
    private ArrayList<String> loggerNames;

    /**
     * Initializes the new log level request event
     */
    public LogLevelRequestEvent() {
        this.loggerNames = new ArrayList<String>();
        dataIdentifiers.add(DataIdentifier.LOG_LEVELS);
    }

    /**
     * Initializes the new log level request event
     * @param loggerNames the names of the loggers for which log level is desired
     */
    public LogLevelRequestEvent(ArrayList<String> loggerNames) {
        this.loggerNames = loggerNames;
        dataIdentifiers.add(DataIdentifier.LOG_LEVELS);
    }

    /**
     * Returns a collection of data requests contained within this event. Each request is identified
     * by a {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent.DataIdentifier}. The response to all of these requests should be wrapped in
     * {@link com.fiorano.openesb.microservice.ccp.event.common.DataEvent} by the component process and sent as a single event to Peer Server.
     *
     * This request event will always have one element in the collection returned by this API.
     * The element returned will always be {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent.DataIdentifier#LOG_LEVELS}
     *
     * @return Collection - Collection of data identifiers
     * @see #setDataIdentifiers(java.util.Collection)
     */
    @Override
    public Collection<DataIdentifier> getDataIdentifiers() {
        return dataIdentifiers;
    }

    /**
     * This API does nothing as this request event only represents requests for log levels for specified logger names.
     * Data Identifiers for this event is internally set as {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent.DataIdentifier#LOG_LEVELS}
     *
     * @param dataIdentifiers Collection of data identifiers
     * @see #getDataIdentifiers()
     */
    @Override
    public void setDataIdentifiers(Collection<DataIdentifier> dataIdentifiers) {
        // Do nothing
    }

    /**
     * Returns a list of logger names for which log level is desired
     * @return ArrayList - A list of logger names for which log level is desired
     */
    public ArrayList<String> getLoggerNames() {
        return loggerNames;
    }

    /**
     * Sets logger names for which log level will be requested by this event
     * @param loggerNames A list of logger names for which log level is desired
     */
    public void setLoggerNames(ArrayList<String> loggerNames) {
        this.loggerNames = loggerNames;
    }

    /**
     * Adds a new logger name to the list of logger names for which request for log level is being made
     * @param loggerName Logger name to be added
     */
    public void addLoggerName(String loggerName) {
        if (!loggerNames.contains(loggerName))
            loggerNames.add(loggerName);
    }

    /**
     * Writes this event object to the bytesMessage.
     *
     * @param bytesMessage Bytes message
     * @throws javax.jms.JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    @Override
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.setIntProperty("Request Type", RequestEventFactory.LOG_LEVEL_REQUEST_EVENT);

        if (loggerNames != null) {
            bytesMessage.writeInt(loggerNames.size());
            for(String loggerName : loggerNames)
                bytesMessage.writeUTF(loggerName);
        } else
            bytesMessage.writeInt(-1);
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this event object.
     *
     * @param bytesMessage Bytes message
     * @throws javax.jms.JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);

        int size = bytesMessage.readInt();
        for (int i = 0; i < size; i++) {
            if(loggerNames == null)
                loggerNames = new ArrayList<String>();

            loggerNames.add(bytesMessage.readUTF());
        }
    }

    /**
     * Returns a string representation of the object.
     *
     * @return String - Representation of the object as a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(" Logger Names : ").append(loggerNames != null ? loggerNames.toString() : "");
        return builder.toString();
    }
}
