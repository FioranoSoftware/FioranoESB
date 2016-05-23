/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;

import com.fiorano.openesb.utils.ExceptionUtil;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CompactFormatter extends Formatter
{
    // Line separator string.  This is the value of the line.separator
    // property at the moment that the SimpleFormatter was created.
    private String lineSeparator = System.getProperty("line.separator");

    public String format(LogRecord record) {
        String logMessage = formatMessage(record);

        if (!logMessage.endsWith(lineSeparator)) {
            logMessage += lineSeparator;
        }

        if (record.getThrown() != null) {
            logMessage += ExceptionUtil.getStackTrace(record.getThrown()) + lineSeparator;
        }
        return logMessage;
    }
}
