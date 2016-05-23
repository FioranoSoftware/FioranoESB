/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamRedirector
        extends Thread
{
    // Input Stream
    private InputStream m_inputStream;

    // Fiorano Logger
    private Logger m_logger;

    // default level
    private Level m_level;

    // Prefix for all log statememts (Used in differentiating between error/outlog)
    private String  m_prefix;

    // state listener
    private StreamStateListener m_stateListener;

    /**
     * Constructs a stream redirector
     *
     * @param prefix
     * @param is
     * @param logger
     * @param level
     */
    public StreamRedirector(String prefix, InputStream is, Logger logger, Level level)
    {
        this(prefix, is, logger, level, null);
    }

    /**
     * Constructs a stream redirector
     *
     * @param prefix
     * @param is
     * @param logger
     * @param stateListener
     * @param level
     */
    public StreamRedirector(String prefix, InputStream is, Logger logger, Level level, StreamStateListener stateListener)
    {
        m_prefix = prefix;
        m_logger = logger;
        m_level = level;
        m_inputStream = is;
        m_stateListener = stateListener;

        if (is != null)
            start();
    }


    /**
     *  Main processing method for the StreamRedirector object
     */
    public void run()
    {
        InputStreamReader streamReader = new InputStreamReader(m_inputStream);
        BufferedReader reader = new BufferedReader(streamReader);

        String str = "";

        //  manually redirect the process's input to the console
        try
        {
            while ((str = reader.readLine()) != null)
            {
                log(str);
            }
        }
        catch (Throwable thr)
        {
            log(thr.getMessage());
        }
        finally
        {
            if (m_stateListener != null)
                m_stateListener.onClose();
        }
    }

    /**
     * Log the parameter string
     *
     * @param logStr
     */
    private void log(String logStr)
    {
        if (m_logger == null)
            return;

        // if (m_prefix != null)
        //     logStr = " >>" + " " + logStr;//Removed Stream/Error Redirector prefix
        // Prefix is no longer used and ">>" is removed as it is anyway appended while using Default Formatter.

        m_logger.log(m_level, logStr);
    }

}

