/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging.api;

import com.fiorano.openesb.utils.exception.FioranoException;
import org.apache.log4j.Logger;

import java.util.Hashtable;

public interface IFioranoLoggerFactory
{

    public Logger getLog4JLogger(String category)
        throws FioranoException;

    /**
     * All Module calls this method to get instance of IFioranoLogger
     *
     * @param category String
     * @return IFioranoLogger
     * @throws FioranoException
     */
    public IFioranoLogger getLogger(String category)
        throws FioranoException;

    /**
     * Sets trace level for object
     *
     * @param component
     * @param level
     */
    public void setTraceLevel(String component, int level);

    /**
     * Sets trace level for object
     *
     * @param level
     */
    public void setTraceLevel(int level);

    /**
     * Returns all components for object
     *
     * @return
     */
    public Hashtable getAllComponents();

    /**
     * Returns level for component for object
     *
     * @param component
     * @return
     */
    public int getLevelForComponent(String component);
}
