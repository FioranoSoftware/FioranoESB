/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging.api;

import java.util.Properties;
import java.util.logging.Handler;

public interface ILogManager
{
    /**
     * Create a Handler object with type (Console, file .. )
     * and configuration specified in 'properties' parameter. It
     * recognizes properties defined in standard jdk logging.
     *
     * The created handler is associated with parameter uniqueId.
     *
     * @param type
     * @param uniqueId
     * @param properties
     * @return
     * @exception Exception
     */
    public Handler createHandler(int type, String uniqueId,
                                 Properties properties)
        throws Exception;

    /**
     * Create a Handler object with configuration specified
     * in 'properties' parameter. It recognizes properties
     * defined in standard jdk logging.
     *
     * The created handler is associated with parameter uniqueId.
     *
     * @param uniqueId
     * @param properties
     * @return
     * @exception Exception
     */
    public Handler createHandler(String uniqueId, Properties properties)
        throws Exception;

    /**
     * Destroy the parameter handler.
     *
     * @param uniqueId
     * @param handler
     */
    public void destroyHandler(String uniqueId, Handler handler);

    /**
     * Destroy all handlers associated with the given id.
     *
     * @param uniqueId
     */
    public void destroyHandlers(String uniqueId);

    /**
     * Clear error logs stored for a given Id
     *
     * @param uniqueId
     * @return boolean specifying whether all logs were deleted or not
     */
    public boolean clearErrLogs(String uniqueId);

    /**
     * Clear Out logs stored for a given Id
     *
     * @param uniqueId
     * @return boolean specifying whether all logs were deleted or not
     */
    public boolean clearOutLogs(String uniqueId);

    /**
     * <p>Get the errLogs for a given uniqueId</p>
     *
     * @param uniqueId
     * @return
     */
    public ILogIterator getErrLogs(String uniqueId);

    /**
     * <p>Get the outLogs for a given uniqueId</p>
     *
     * @param uniqueId
     * @return
     */
    public ILogIterator getOutLogs(String uniqueId);

}
