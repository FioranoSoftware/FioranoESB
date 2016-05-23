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

public class FioranoOutHandler
     extends FioranoBaseHandler
{
    /**
     * @param handler
     * @param props
     */
    public FioranoOutHandler(Handler handler, Properties props)
    {
        super(handler, props);
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
        if (!isOutRecord(record))
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
        if (!isOutRecord(record))
            return;

        super.publish(record);
    }


    private boolean isOutRecord(LogRecord logRecord)
    {
        if (logRecord == null)
            return false;

        Level level = logRecord.getLevel();

        if (level == null)
            return false;

        if (
            (level == Level.CONFIG) ||
            (level == Level.FINE) ||
            (level == Level.FINER) ||
            (level == Level.FINEST) ||
            (level == Level.INFO) ||
            (level == Level.ALL)
            )
            return true;

        return false;
    }
}
