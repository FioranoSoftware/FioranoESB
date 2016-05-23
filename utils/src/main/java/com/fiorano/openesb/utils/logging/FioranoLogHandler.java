/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;

import java.util.Properties;
import java.util.logging.*;

public class FioranoLogHandler
     extends Handler
{
    // internal out handler Object
    private FioranoOutHandler m_outhandler;

    // internal error handler Object
    private FioranoErrorHandler m_errhandler;

    private Level level = Level.ALL ;

    /**
     * @param outhandler
     * @param errhandler
     */
    public FioranoLogHandler(FioranoOutHandler outhandler, FioranoErrorHandler errhandler)
    {
        m_outhandler = outhandler;
        m_errhandler = errhandler;
    }

    /**
     * Returns out log handler for object
     *
     * @return
     */
    public FioranoOutHandler getOutLogHandler()
    {
        return m_outhandler;
    }

    /**
     * Returns err log handler for object
     *
     * @return
     */
    public FioranoErrorHandler getErrLogHandler()
    {
        return m_errhandler;
    }

    /**
     * Get Error Log Type
     *
     * @return int
     */
    public int getLogType()
    {
        return m_errhandler.getType();
    }

    /**
     * Returns properties for object
     *
     * @return
     */
    public Properties getProperties()
    {
        return m_errhandler.getProperties();
    }

    /**
     * Return the <tt>Formatter</tt> for this <tt>Handler</tt>.
     *
     * @return the <tt>Formatter</tt> (may be null).
     */
    public Formatter getFormatter()
    {
        return m_outhandler.getFormatter();
    }

    /**
     * Return the character encoding for this <tt>Handler</tt>.
     *
     * @return The encoding name.  May be null, which indicates the
     *	    	default encoding should be used.
     */
    public String getEncoding()
    {
        return m_outhandler.getEncoding();
    }


    /**
     * Get the current <tt>Filter</tt> for this <tt>Handler</tt>.
     *
     * @return a </tt>Filter</tt> object (may be null)
     */
    public Filter getFilter()
    {
        return m_outhandler.getFilter();
    }

    /**
     * Retrieves the ErrorManager for this Handler.
     *
     * @return
     */
    public ErrorManager getErrorManager()
    {
        return m_outhandler.getErrorManager();
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
        return m_outhandler.getLevel();
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
        if (m_outhandler.isLoggable(record))
            return true;

        if (m_errhandler.isLoggable(record))
            return true;

        return false;
    }

    /**
     * Sets out log handler for object
     *
     * @param oldHandler
     */
    public void setOutLogHandler(FioranoOutHandler oldHandler)
    {
        m_outhandler = oldHandler;
    }

    /**
     * Sets err log handler for object
     *
     * @param errorHandler
     */
    public void setErrLogHandler(FioranoErrorHandler errorHandler)
    {
        m_errhandler = errorHandler;
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
        m_outhandler.setFormatter(newFormatter);
        m_errhandler.setFormatter(newFormatter);
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
        m_outhandler.setEncoding(encoding);
        m_errhandler.setEncoding(encoding);
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
        m_outhandler.setFilter(newFilter);
        m_errhandler.setFilter(newFilter);
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
        m_outhandler.setErrorManager(em);
        m_errhandler.setErrorManager(em);
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
        m_outhandler.setLevel(newLevel);
        m_errhandler.setLevel(newLevel);
    }

    /**
     * Set the log level specifying which message levels will be
     * logged by this <tt>Handler</tt>.  Message levels lower than this
     * value will be discarded.
     * <p>
     * It is different from setLevel function in the way that setLevel function
     * sets the level of out handler and error handler so no filtering is done
     * at FioranoLogHandler level while this function will set the level of FioranoLogHandler which will
     * so that the filtering can be done at the the level of FioranoLogHandler
     * before giving messages to out handler and error hanlder
     * <p>
     * This function also sets the level of out handler and error handler to ALL.
     * <p>
     * The intention is to allow developers to turn on voluminous
     * logging, but to limit the messages that are sent to certain
     * <tt>Handlers</tt>.
     *
     * @param newLevel the new value for the log level
     * @exception SecurityException if a security manager exists and if
     *             the caller does not have <tt>LoggingPermission("control")</tt>.
     */
    public void setLogLevel(Level newLevel)
        throws SecurityException
    {
        this.level = newLevel;
        setLevel(Level.ALL);
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
        /* Filtering will be first done based on the level of this handler .
           this.level is 'ALL' by default so if it is not set to some other value it will pass all the messages  */
        if( record.getLevel().intValue() >= this.level.intValue()){
            m_outhandler.publish(record);
            m_errhandler.publish(record);
        }
    }

    /**
     * Flush any buffered output.
     */
    public void flush()
    {
        m_outhandler.flush();
        m_errhandler.flush();
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
        m_outhandler.close();
        m_errhandler.close();
    }
}
