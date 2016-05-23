/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;


import com.fiorano.openesb.utils.logging.api.ILogConstants;

import java.util.Properties;
import java.util.logging.*;

public class FioranoBaseHandler
     extends Handler
{
    // internal handler Object
    protected Handler m_handler;

    protected Properties m_props;

    // type of handler
    protected int   m_type;

    /**
     * @param handler
     * @param props
     */
    public FioranoBaseHandler(Handler handler, Properties props)
    {
        m_handler = handler;
        m_props = props;

        if (handler instanceof FileHandler)
            m_type = ILogConstants.FILE_HANDLER_TYPE;
        else if (handler instanceof ConsoleHandler)
            m_type = ILogConstants.CONSOLE_HANDLER_TYPE;
        else
            m_type = ILogConstants.UNKNOWN_HANDLER_TYPE;
    }

    /**
     * Returns type for object
     *
     * @return
     */
    public int getType()
    {
        return m_type;
    }

    /**
     * Returns properties for object
     *
     * @return
     */
    public Properties getProperties()
    {
        return m_props;
    }

    /**
     * Return the <tt>Formatter</tt> for this <tt>Handler</tt>.
     *
     * @return the <tt>Formatter</tt> (may be null).
     */
    public Formatter getFormatter()
    {
        return m_handler.getFormatter();
    }

    /**
     * Return the character encoding for this <tt>Handler</tt>.
     *
     * @return The encoding name.  May be null, which indicates the
     *	    	default encoding should be used.
     */
    public String getEncoding()
    {
        return m_handler.getEncoding();
    }

    /**
     * Get the current <tt>Filter</tt> for this <tt>Handler</tt>.
     *
     * @return a </tt>Filter</tt> object (may be null)
     */
    public Filter getFilter()
    {
        return m_handler.getFilter();
    }

    /**
     * Retrieves the ErrorManager for this Handler.
     *
     * @return
     */
    public ErrorManager getErrorManager()
    {
        return m_handler.getErrorManager();
    }

    /**
     * Get the log level specifying which messages will be
     * logged by this <tt>Handler</tt>.  Message levels lower
     * than this level will be discarded.
     *
     * @return the level of messages being logged.
     */
    public Level getLevel()
    {
        return m_handler.getLevel();
    }

    /**
     * Check if this <tt>Handler</tt> would actually log a given <tt>LogRecord</tt>.
     * <p>
     * This method checks if the <tt>LogRecord</tt> has an appropriate
     * <tt>Level</tt> and  whether it satisfies any <tt>Filter</tt>.  It also
     * may make other <tt>Handler</tt> specific checks that might prevent a
     * handler from logging the <tt>LogRecord</tt>.
     * <p>
     *
     * @param record a <tt>LogRecord</tt>
     * @return true if the <tt>LogRecord</tt> would be logged.
     */
    public boolean isLoggable(LogRecord record)
    {
        return m_handler.isLoggable(record);
    }

    /**
     * Set a <tt>Formatter</tt>.  This <tt>Formatter</tt> will be used
     * to format <tt>LogRecords</tt> for this <tt>Handler</tt>.
     * <p>
     * Some <tt>Handlers</tt> may not use <tt>Formatters</tt>, in
     * which case the <tt>Formatter</tt> will be remembered, but not used.
     * <p>
     *
     * @param newFormatter the <tt>Formatter</tt> to use (may not be null)
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    public void setFormatter(Formatter newFormatter)
        throws SecurityException
    {
        m_handler.setFormatter(newFormatter);
    }

    /**
     * Set the character encoding used by this <tt>Handler</tt>.
     * <p>
     * The encoding should be set before any <tt>LogRecords</tt> are written
     * to the <tt>Handler</tt>.
     *
     * @param encoding The name of a supported character encoding.
     *	      May be null, to indicate the default platform encoding.
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     * @exception java.io.UnsupportedEncodingException
     */
    public void setEncoding(String encoding)
        throws SecurityException, java.io.UnsupportedEncodingException
    {
        m_handler.setEncoding(encoding);
    }

    /**
     * Set a <tt>Filter</tt> to control output on this <tt>Handler</tt>.
     * <P>
     * For each call of <tt>publish</tt> the <tt>Handler</tt> will call
     * this <tt>Filter</tt> (if it is non-null) to check if the
     * <tt>LogRecord</tt> should be published or discarded.
     *
     * @param newFilter a <tt>Filter</tt> object (may be null)
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    public void setFilter(Filter newFilter)
        throws SecurityException
    {
        m_handler.setFilter(newFilter);
    }

    /**
     * Define an ErrorManager for this Handler.
     * <p>
     * The ErrorManager's "error" method will be invoked if any
     * errors occur while using this Handler.
     *
     * @param em the new ErrorManager
     */
    public void setErrorManager(ErrorManager em)
    {
        m_handler.setErrorManager(em);
    }

    /**
     * Set the log level specifying which message levels will be
     * logged by this <tt>Handler</tt>.  Message levels lower than this
     * value will be discarded.
     * <p>
     * The intention is to allow developers to turn on voluminous
     * logging, but to limit the messages that are sent to certain
     * <tt>Handlers</tt>.
     *
     * @param newLevel the new value for the log level
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    public void setLevel(Level newLevel)
        throws SecurityException
    {
        m_handler.setLevel(newLevel);
    }

    /**
     * Publish a <tt>LogRecord</tt>.
     * <p>
     * The logging request was made initially to a <tt>Logger</tt> object,
     * which initialized the <tt>LogRecord</tt> and forwarded it here.
     * <p>
     * The <tt>Handler</tt>  is responsible for formatting the message, when and
     * if necessary.  The formatting should include localization.
     *
     * @param record description of the log event
     */
    public void publish(LogRecord record)
    {
        m_handler.publish(record);
    }

    /**
     * Flush any buffered output.
     */
    public void flush()
    {
        m_handler.flush();
    }

    /**
     * Close the <tt>Handler</tt> and free all associated resources.
     * <p>
     * The close method will perform a <tt>flush</tt> and then close the
     * <tt>Handler</tt>.   After close has been called this <tt>Handler</tt>
     * should no longer be used.  Method calls may either be silently
     * ignored or may throw runtime exceptions.
     *
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    public void close()
        throws SecurityException
    {
        m_handler.close();
    }
}
