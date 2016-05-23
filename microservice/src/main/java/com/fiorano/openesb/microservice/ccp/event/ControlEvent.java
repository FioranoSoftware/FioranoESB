/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.util.Date;

public abstract class ControlEvent {

    /**
     * Header property name representing event type for the message sent/received.
     * Refer {@link CCPEventType} for list of available event types.
     */
    public static final String EVENT_TYPE_HEADER = "FIORANO_ESB_CCP_EVENT_TYPE_HEADER";

    /**
     * Header property name representing the source of the event. This property must be set by the component for all
     * information/status messages which it sends to Peer Server. The value of this property should be set to
     * <code>appGUID__componentInstanceName</code> where <b>appGUID</b> is the application GUID to which this
     * component belongs and <b>componentInstanceName</b> is the actual instance name for this service instance.<br><br>
     *
     * Example Usage: SOURCE_OBJECT=SIMPLECHAT__chat1.
     */
    public static final String SOURCE_OBJECT = "SOURCE_OBJECT";

    /**
     * Header property name representing the target service instances which should process this command/request.
     * Command/Requests from Peer Server will contain this header property to target the components which should
     * process this command/request. If multiple components are intended to process a command/request, multiple values
     * will be separated by ';' e.g. TARGET_OBJECTS=SIMPLECHAT__chat1;SIMPLECHAT__chat2.<br><br>
     *
     * In order to filter out un-necessary commands/requests being received by a component,
     * component should create subscriber to CCP_PEER_TO_COMPONENT_TRANSPORT topic using appropriate JMS message selector.
     */
    public static final String TARGET_OBJECTS = "TARGET_OBJECTS";

    private Long eventId = System.nanoTime();
    private double protocolVersion = 1.0f;
    private long dateTime = System.currentTimeMillis();
    private String description= "";
    private int priority = 5;
    private long expiryTime = -1;
    private boolean isReplyNeeded;
    private long replyTimeout = -1;
    private long correlationID = 0;

    /**
     * Returns supported Component Control Protocol (CCP) Version.
     * @return double - Supported Component Control Protocol (CCP) Version.
     * @see #getProtocolVersion()
     */
    public double getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Sets supported Component Control Protocol (CCP) Version.
     * @param protocolVersion Supported Component Control Protocol (CCP) Version.
     * @see #setProtocolVersion(double)
     */
    public void setProtocolVersion(double protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    /**
     * Returns the event type for the event represented by this object.
     * @return {@link CCPEventType} - An enumeration constant representing the event
     * @see CCPEventType
     */
    public abstract CCPEventType getEventType();

    /**
     * Returns the time at which this event was generated. This value is the difference, measured in milliseconds,
     * between the current time and midnight, January 1, 1970 UTC.
     * @return long - Event Generation Time
     * @see #getDateTime()
     */
    public long getDateTime() {
        return dateTime;
    }

    /**
     * Sets the event generation time value for this event to specified value.
     * @param dateTime Event Generation Time
     * @see #setDateTime(long)
     */
    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns an abstract string describing this event.
     * @return String - Event Description
     * @see #getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the event description to specified value.
     * @param description Event Description
     * @see #setDescription(String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the priority of this event. A high priority command from Peer Server should be executed before
     * other pending commands with lower priority.
     * @return int - Priority of this event
     * @see #getPriority()
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this event. A high priority command from Peer Server should be executed before
     * other pending commands with lower priority. Default priority is set to '5'.
     * @param priority Priority of this event
     * @see #setPriority(int)
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the expiry time of this event. A command from Peer Server to component process should be treated as expired
     * if this time has already elapsed. A value of '-1' means that this command will never expire.
     * @return long - Expiry Time of the event
     * @see #setExpiryTime(long)
     */
    public long getExpiryTime() {
        return expiryTime;
    }

    /**
     * Sets the expiry time for this event. A command from Peer Server to component process should be treated as expired
     * if this time has already elapsed. A value of '-1' means that this command will never expire. Default value is '-1'.
     * @param expiryTime Expiry Time of the event
     * @see #getExpiryTime()
     */
    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    /**
     * Specifies whether this event needs a reply from the recipient of this event. If yes, the event should be replied with
     * appropriate event(s).
     * @return Boolean - returns true if reply is needed, false otherwise
     * @see #setReplyNeeded(boolean)
     */
    public boolean isReplyNeeded() {
        return isReplyNeeded;
    }

    /**
     * Set this value to true to this event needs a reply from recipient of this event. Default value is 'false'.
     * @param replyNeeded Boolean true if reply is needed, false otherwise
     * @see #isReplyNeeded()
     */
    public void setReplyNeeded(boolean replyNeeded) {
        isReplyNeeded = replyNeeded;
    }

    /**
     * Specifies the amount of time that the sender of this event will wait for a reply to this event if this event needs a
     * reply from the recipient. This value can be very useful where event is sent in a request-reply model (looses meaning with
     * asynchronous messaging). A value of '-1' means that sender will wait forever. Default value is '-1'.
     * @return long - Reply timeout value for the event
     * @see #setReplyTimeout(long)
     */
    public long getReplyTimeout() {
        return replyTimeout;
    }

    /**
     * Sets the amount of time that the sender of this event will wait for a reply to this event if this event needs a
     * reply from the recipient.
     * @param replyTimeout Reply timeout value for the event
     * @see #getReplyTimeout()
     */
    public void setReplyTimeout(long replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

    /**
     * Returns the correlation ID for this event object.
     * @return long - Correlation ID for the event
     * @see #setCorrelationID(long)
     */
    public long getCorrelationID() {
        return correlationID;
    }

    /**
     * Sets the correlation ID for this event. This ID is used by Peer Server to identify the response to a command/request.
     * Whenever Peer Server sends a command/request for which it needs a reply, it will set a unique event ID for that event.
     * The event ID can be retrieved using API {@link #getEventId()}. The response events sent by components to Peer Server
     * for this command should have this ID set as correlation ID for the event object.
     * @param correlationID Correlation ID for the event
     * @see #getCorrelationID()
     */
    public void setCorrelationID(long correlationID) {
        this.correlationID = correlationID;
    }

    /**
     * Reads the values from the bytesMessage and sets the properties of this event object.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while reading values from the message
     * @see #toMessage(javax.jms.BytesMessage)
     */
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        eventId = bytesMessage.readLong();
        protocolVersion = bytesMessage.readDouble();
        dateTime = bytesMessage.readLong();
        description = bytesMessage.readUTF();
        priority = bytesMessage.readInt();
        expiryTime = bytesMessage.readLong();
        isReplyNeeded = bytesMessage.readBoolean();
        replyTimeout = bytesMessage.readLong();
        correlationID = bytesMessage.readLong();
    }

    /**
     * Writes this event object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writing value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeLong(eventId);
        bytesMessage.writeDouble(protocolVersion);
        bytesMessage.writeLong(dateTime);
        bytesMessage.writeUTF(description);
        bytesMessage.writeInt(priority);
        bytesMessage.writeLong(expiryTime);
        bytesMessage.writeBoolean(isReplyNeeded);
        bytesMessage.writeLong(replyTimeout);
        bytesMessage.writeLong(correlationID);
    }

    /**
     * Returns a string representation of the object.
     * @return String - representation of the object as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Event ID : ").append(eventId);
        builder.append(" Protocol Version : ").append(protocolVersion);
        builder.append(" Event Time : ").append(new Date(dateTime));
        builder.append(" Event Description : ").append(description);
        builder.append(" Event Priority : ").append(priority);
        builder.append(" Event Expiry Time : ").append(expiryTime != -1 ? new Date(expiryTime) : expiryTime);
        builder.append(" Reply Needed : ").append(isReplyNeeded);
        builder.append(" Reply Timeout : ").append(replyTimeout);
        builder.append(" Correlation ID : ").append(correlationID);
        return builder.toString();
    }

    /**
     * Returns the event ID set by Peer Server for an event for which Peer Server needs a reply from the component process. The
     * event which is sent as a reply to this event should have this same value set as correlation ID of the event.
     * @return Long - Event ID
     */
    public Long getEventId() {
        return eventId;
    }
}