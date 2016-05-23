/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;


import com.fiorano.openesb.utils.ExceptionUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DefaultFormatter extends Formatter
{
    private boolean m_bUseTimeStamp;
    private String dateFormat = "MM/dd/yyyy HH:mm:ss SSS";
    /**
     * @param props
     */
    public void configure(Properties props)
    {
        String cname = DefaultFormatter.class.getName();
        m_bUseTimeStamp = DefaultLogManager.getBooleanProperty(props, cname +".includetimestamp", true);
        dateFormat = DefaultLogManager.getStringProperty(props,cname+".dateformat","MM/dd/yyyy HH:mm:ss SSS");
    }

    /**
     * @param enable
     */
    public void useTimeStamp(boolean enable){
        m_bUseTimeStamp = enable;
    }

    public void useDateFormat(String format){
        dateFormat = format;
    }

    private String lineSeparator = System.getProperty("line.separator");
    /**
     * Format the given log record and return the formatted string.
     * <p>
     * The resulting formatted String will normally include a
     * localized and formated version of the LogRecord's message field.
     * The Formatter.formatMessage convenience method can (opti`    onally)
     * be used to localize and format the message field.
     *
     * @param logRecord
     * @return the formatted log record
     */
    public String format(LogRecord logRecord)
    {
        String strRecord="";

        if (m_bUseTimeStamp)
        {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            String date = formatter.format(new Date());

            strRecord = date + " : ";
        }
        strRecord = "!ENTRY \n" + strRecord + logRecord.getLevel() + " : ";
        strRecord = strRecord + logRecord.getMessage() + lineSeparator;

        if(logRecord.getThrown()!=null)
            strRecord = strRecord+ ExceptionUtil.getStackTrace(logRecord.getThrown());
        return strRecord;
    }
}
