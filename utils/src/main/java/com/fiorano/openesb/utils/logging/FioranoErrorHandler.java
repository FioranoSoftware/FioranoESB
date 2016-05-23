/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;

import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FioranoErrorHandler
     extends FioranoBaseHandler
{
    /**
     * @param handler
     * @param props
     */
    public FioranoErrorHandler(Handler handler, Properties props)
    {
        super(handler, props);
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
        if (m_handler.getLevel().intValue() < Level.WARNING.intValue())
            return Level.WARNING;

        return super.getLevel();
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
        if (!isErrorRecord(record))
            return false;

        return super.isLoggable(record);
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
        if (!isErrorRecord(record))
            return;

        super.publish(record);
    }


    private boolean isErrorRecord(LogRecord logRecord)
    {
        if (logRecord == null)
            return false;

        Level level = logRecord.getLevel();

        if (level == null)
            return false;

        if ((level == Level.SEVERE) || (level == Level.WARNING))
            return true;

        return false;
    }
}
