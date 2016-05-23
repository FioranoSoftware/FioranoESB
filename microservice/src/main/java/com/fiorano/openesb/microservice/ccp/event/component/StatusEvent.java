/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.component;


import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

public class StatusEvent extends ControlEvent {
    private StatusType statusType;
    private Status status;
    private OperationScope operationScope;
    private String errorMessage = "";

    /**
     * Returns the event type for the event represented by this object.
     * @return {@link com.fiorano.openesb.microservice.ccp.event.CCPEventType} - An enumeration constant representing status event from component i.e. {@link CCPEventType#STATUS}
     */
    public CCPEventType getEventType() {
        return CCPEventType.STATUS;
    }

    /**
     * This enumeration defines the scope of the operation under which this event is beinf sent by component process.
     * For example, if the event is being sent while component process is in the middle of startup sequence, it's scope
     * should be {@link #COMPONENT_LAUNCH} etc. In general, the scope defines the state that the component is currently in.
     * @author FSTPL
     * @version 10
     */
    public enum OperationScope {
        /**
         * This constant wraps the time duration during which component is in startup face e.g. initialization of external connections required
         * to execute business logic. Note that dynamic creation of new connections while component is in running state does not fall under this scope.
         */
        COMPONENT_LAUNCH,

        /**
         * This constant wraps the time duration during which component process is in launching state.
         */
        COMPONENT_LAUNCHING,

        /**
         * This constant wraps the time duration between the moments where component starts it shutdown sequence and is finally shutdown.
         * Status messages which relate to component shutdown should be sent under this scope. 
         */
        COMPONENT_STOP,

        /**
         * This constant wraps the time duration during which component process is in running state.
         */
        COMPONENT_RUNNING
    }

    /**
     * This enumeration defines the category under which this event falls.
     * @author FSTPL
     * @version 10
     */
    public enum StatusType {
        /**
         * Use this enumeration constant when sending informative message to Peer Server.
         */
        INFORMATION,

        /**
         * Use this enumeration constant when sending warning messages to Peer Server. Warning messages
         * are distinguished from Error messages in the sense that warning messages are not harmful
         * but can be sometimes corrected by taking appropriate actions.
         */
        WARNING,

        /**
         * Use this enumeration constant when sending error messages. For example, if an exception happens while
         * collecting some statistics requested by Peer Server.
         */
        ERROR
    }

    /**
     * This enumeration defines various values for component status. In essence, it defines the current
     * state that component is in.
     * @author FSTPL
     * @version 10
     */
    public enum Status {
        /**
         * Used to indicate that component is now running and is fully functional.
         */
        COMPONENT_STARTED,

        /**
         * Used to indicate that component is now connected to peer server.
         */
        COMPONENT_CONNECTED,

        /**
         * Used to indicate that component has started the shutdown sequence. On receiving this event, peer server will wait for configured timeout before issuing a force shutdown command to component process.
         */
        COMPONENT_STOPPING,

        /**
         * Used to indicate that component will now launch on the Peer Server
         */
        COMPONENT_LAUNCHING,

        /**
         * Used to indicate that component will now close the connection with the peer server.
         */
        COMPONENT_DISCONNECTING,

        /**
         * Used to indicate that component has stopped all execution e.g. closing external connections (if any) etc. After sending this event, the component should close all connections to peer server.
         * Additionally, a separate process component should issue a System.exit() with proper status code.
         */
        COMPONENT_STOPPED,

        /**
         * Used to indicate that component state is undefined. This might happen due to some unexpected failure in the business logic of the component. 
         */
        COMPONENT_UNKNOWN,
		
		SCHEDULER_STOPPED
    }

    /**
     * Returns the {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.StatusType} under which this event falls.
     * @return StatusType - Status Type represented by {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.StatusType}
     * @see #setStatusType(com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.StatusType)
     */
    public StatusType getStatusType() {
        return statusType;
    }

    /**
     * Returns the {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.Status} of the component process.
     * @return Status - Component Status represented by {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.Status}
     * @see #setStatus(com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.Status)
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.OperationScope} of this operation.
     * @return OperationScope - Operation Scope represented by {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.OperationScope}
     * @see #setOperationScope(com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.OperationScope)
     */
    public OperationScope getOperationScope() {
        return operationScope;
    }

    /**
     * Returns the {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.StatusType} under which this event falls.
     * @param statusType Status Type
     * @see #getStatusType()
     */
    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    /**
     * Sets the {@link Status} of the component process.
     * @param status Status of component process.
     * @see #getStatus()
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Sets the {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.OperationScope} under which this operation falls.
     * @param operationScope Operation Scope
     * @see #getOperationScope()
     * @see com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.OperationScope
     */
    public void setOperationScope(OperationScope operationScope) {
        this.operationScope = operationScope;
    }

    /**
     * Returns the error message
     * @return String - Error message
     * @see #setErrorMessage(String)
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message field. Applicable only if this event falls under {@link com.fiorano.openesb.microservice.ccp.event.component.StatusEvent.StatusType#ERROR} category.
     * @param errorMessage Error message
     * @see #getErrorMessage()
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
        statusType = StatusType.valueOf(bytesMessage.readUTF());
        status = Status.valueOf(bytesMessage.readUTF());
        errorMessage = bytesMessage.readUTF();
        operationScope = OperationScope.valueOf(bytesMessage.readUTF());
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
        bytesMessage.writeUTF(statusType.toString());
        bytesMessage.writeUTF(status.toString());
        bytesMessage.writeUTF(errorMessage);
        bytesMessage.writeUTF(operationScope.toString());
    }

    /**
     * Returns a string representation of the object.
     * @return String - Representation of the object as a String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Status Event Properties");
        builder.append("-------------------------------------");
        builder.append(super.toString());
        builder.append(" Status Type : ").append(statusType.name());
        builder.append(" Status : ").append(status.name());
        builder.append(" Operation Scope : ").append(operationScope.name());
        return builder.toString();
    }
}
