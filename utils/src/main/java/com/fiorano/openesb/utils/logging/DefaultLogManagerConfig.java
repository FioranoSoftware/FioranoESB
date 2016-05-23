/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;

import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.Locations;

import java.io.File;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class DefaultLogManagerConfig {
    private String logDir;

    // character encoding
    private String strEncoding;

    // log level
    private int logLevel;

    // filter for logs
    private String filter;

    // formatter object
    private String formatter;

    // Size Limit for file Handler
    private int nFileSizeLimit;

    // max number of log files that can be created
    private int nFileCount;

    // can append to existing file
    private boolean bAppendToFile;

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Base Directory where logs will be stored"
     * @jmx.descriptor name="restart" value="true"
     * @jmx.descriptor name="defaultValue" value=""
     */
    public String getLogDir() {
        String baseDir = System.getProperty(Locations.RUN_DIR, ".") +
                File.separator + "logs";

        if ((logDir == null) || (logDir.equalsIgnoreCase("")))
            return baseDir;

        //Making log path configuable.
        if (FileUtil.isAbsolutePath(logDir))
            return logDir;

        return baseDir + File.separator + logDir;
    }

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Log level that is used by newly constructed Handler objects."
     * @jmx.descriptor name="defaultValue" value="UTF-8"
     */
    public String getEncoding() {
        return strEncoding;
    }

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Log level that is used by newly constructed Handler objects."
     * @jmx.descriptor name="defaultValue" value="2"
     */
    public int getLevel() {
        return logLevel;
    }

    /**
     * Returns log level for object
     *
     * @return
     */
    public Level getLogLevel() {
        if (logLevel <= -1)
            return Level.OFF;

        switch (logLevel) {
            case 0:
                return Level.SEVERE;
            case 1:
                return Level.WARNING;
            case 2:
                return Level.INFO;
            case 3:
                return Level.CONFIG;
            case 4:
                return Level.FINE;
            case 5:
                return Level.FINER;
            case 6:
                return Level.FINEST;
            default:
                return Level.CONFIG;
        }
    }

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Maximum size of each log file. 0 => infinite.
     * This is used by FileHandler"
     * @jmx.descriptor name="defaultValue" value="0"
     * @jmx.descriptor name="minValue" value="0"
     */
    public int getFileSizeLimit() {
        return nFileSizeLimit;
    }

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Maximum number of log files that should be created. This is used by FileHandler"
     * @jmx.descriptor name="defaultValue" value="1"
     * @jmx.descriptor name="minValue" value="1"
     */
    public int getFileCount() {
        return nFileCount;
    }

    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Specifies append mode for file handler. By default it is appended"
     * @jmx.descriptor name="defaultValue" value="true"
     */
    public boolean getAppendToFile() {
        return bAppendToFile;
    }

//    /**
//     * @return
//     * @jmx.managed-attribute access="read-write"
//     * description="Specifies the filter. By default filter is null"
//     */
//    public String getFilter()
//    {
//        return filter;
//    }
//
//    /**
//     * @return
//     * @jmx.managed-attribute
//     */
//    public void setFilter(String  filter)
//    {
//       filter = filter;
//    }


    /**
     * @return
     * @jmx.managed-attribute access="read-write"
     * description="Specifies the formatter."
     * @jmx.descriptor name="defaultValue" value="fiorano.jms.log2.def.CompactFormatter"
     */
    public String getFormatter() {
        return formatter;
    }

    /**
     * @return
     */
    public Formatter getFormatterObject() {
        if (formatter == null)
            formatter = "fiorano.jms.log2.def.CompactFormatter";

        try {
            return (Formatter) Class.forName(formatter).newInstance();
        } catch (Throwable ex) {
            return new CompactFormatter();
        }
    }

    /**
     * @param logDir
     * @jmx.managed-attribute
     */
    public void setLogDir(String logDir) {
        logDir = logDir;
    }

    /**
     * @param encoding
     * @jmx.managed-attribute
     */
    public void setEncoding(String encoding) {
        strEncoding = encoding;
    }

    /**
     * @param logLevel
     * @jmx.managed-attribute
     */
    public void setLevel(int logLevel) {
        logLevel = logLevel;
    }

    /**
     * @param fileSizeLimit
     * @jmx.managed-attribute
     */
    public void setFileSizeLimit(int fileSizeLimit) {
        nFileSizeLimit = fileSizeLimit;
    }

    /**
     * @param fileCount
     * @jmx.managed-attribute
     */
    public void setFileCount(int fileCount) {
        nFileCount = fileCount;
    }

    /**
     * @param appendMode
     * @jmx.managed-attribute
     */
    public void setAppendToFile(boolean appendMode) {
        bAppendToFile = appendMode;
    }

    /**
     * @param formatter
     * @jmx.managed-attribute
     */
    public void setFormatter(String formatter) {
        formatter = formatter;
    }
}
