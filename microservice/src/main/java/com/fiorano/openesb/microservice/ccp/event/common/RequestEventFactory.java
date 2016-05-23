/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.common;

public class RequestEventFactory {

    /**
     * This integer constant is used to represent request of type {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent}.
     */
    public static final int DATA_REQUEST_EVENT = 1;

    /**
     * This integer constant is used to represent request of type {@link com.fiorano.openesb.microservice.ccp.event.common.LogLevelRequestEvent}.
     */
    public static final int LOG_LEVEL_REQUEST_EVENT = 2;

    /**
     * Factory method to obtain a new instance of {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent} as per the argument passed to the method.
     * @param requestType Type of {@link com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent} to be returned. The request type should be one of the
     * constant integers specified in this class. Otherwise, an exception saying that request type is not valid may be thrown.
     * @return DataRequestEvent - Request type represented by the passed argument
     * @exception IllegalArgumentException if the request type is not found.
     */
    public static DataRequestEvent getRequestEvent(int requestType) {
        switch (requestType) {
            case DATA_REQUEST_EVENT:
                return new DataRequestEvent();
            case LOG_LEVEL_REQUEST_EVENT:
                return new LogLevelRequestEvent();
            default:
                throw new IllegalArgumentException("EVENT_NOT_FOUND");
        }
    }
}
