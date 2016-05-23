/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;

import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.I18NUtil;
import com.fiorano.openesb.utils.Locations;
import com.fiorano.openesb.utils.logging.api.IFioranoLogger;
import com.fiorano.openesb.utils.logging.api.ILogConstants;
import com.fiorano.openesb.utils.logging.api.ILogIterator;
import com.fiorano.openesb.utils.logging.api.ILogManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.*;

public class DefaultLogManager implements ILogManager {

    IFioranoLogger m_logger;
    // config object
    private DefaultLogManagerConfig m_config;

    // sync object
    private Object m_syncObject = new Object();
    private static HashMap<String, Handler[]> fileHandlerMap = new HashMap<String, Handler[]>();   // this map is added for handling the special case of InMemory component handlers
    //  Map stores the uniqueId vs Handler array pair . Handler array has outFileHanlder at 0th index
    // and errFileHandler at 1st index

    private static HashMap<String, ArrayList<String>> componentLoggerMap = new HashMap<String, ArrayList<String>>();  // this map is added for handling the special case of InMemory component handlers

    /**                                                                                                             //  Map stores the uniqueId vs List of InMemory Component Loggers
     * Get Int property
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public static int getIntProperty(Properties props, String name,
                                     int defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        if (val == null)
            return defaultValue;

        try {
            return Integer.parseInt(val.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Get a boolean property.
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanProperty(Properties props, String name,
                                             boolean defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        if (val == null)
            return defaultValue;

        val = val.toLowerCase();
        if (val.equals("true") || val.equals("1"))
            return true;
        else if (val.equals("false") || val.equals("0"))
            return false;

        return defaultValue;
    }

    /**
     * Get a level property.
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public static Level getLevelProperty(Properties props, String name,
                                         Level defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        if (val == null)
            return defaultValue;

        try {
            return Level.parse(val.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Get a formatter property.
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public static Formatter getFormatterProperty(Properties props, String name,
                                                 Formatter defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        try {
            if (val != null) {
                Class clz = ClassLoader.getSystemClassLoader().loadClass(val);

                return (Formatter) clz.newInstance();
            }
        } catch (Exception ex) {
            // We got one of a variety of exceptions in creating the
            // class or creating an instance.
            // Drop through.
        }
        // We got an exception.  Return the defaultValue.
        return defaultValue;
    }

    /**
     * Get a string property.
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getStringProperty(Properties props, String name,
                                           String defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        if (val == null) {
            return defaultValue;
        }
        return val.trim();
    }

    /**
     * The main program for the DefaultLogManager class
     *
     * @param args The command line arguments
     * @exception Exception
     */
    public static void main(String[] args)
            throws Exception {
        DefaultLogManager manager = new DefaultLogManager();

        manager.test();
    }

    /**
     * <p>Get the errLogs for a given uniqueId</p>
     *
     * @param uniqueId
     * @return
     */
    public ILogIterator getErrLogs(String uniqueId) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);


        m_logger.info(DefaultLogManager.class, "getting_err_logs_for_id", uniqueId);

        String dirName = getLogDirName(uniqueId);


        m_logger.info(DefaultLogManager.class, "found_log_dir_name", uniqueId, dirName);

        // create file filter
        String[] posExt = new String[]{".err"};
        String[] negExt = new String[]{".lck"};

        FilenameFilter filter = new LogFileNameFilter(posExt, negExt);

        return new DefaultLogIterator(this, dirName, filter, m_config.getEncoding());
    }

    /**
     * <p>Get the outLogs for a given uniqueId</p>
     *
     * @param uniqueId
     * @return
     */
    public ILogIterator getOutLogs(String uniqueId) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);


        m_logger.info(DefaultLogManager.class, "getting_out_logs_for_id", uniqueId);

        String dirName = getLogDirName(uniqueId);


        m_logger.info(DefaultLogManager.class, "found_log_dir_name", uniqueId, dirName);

        // create file filter
        String[] posExt = new String[]{".out"};
        String[] negExt = new String[]{".lck"};

        FilenameFilter filter = new LogFileNameFilter(posExt, negExt);

        return new DefaultLogIterator(this, dirName, filter, m_config.getEncoding());
    }

    /**
     * Get a filter property.
     *
     * @param props
     * @param name
     * @param defaultValue
     * @return
     */
    public Filter getFilterProperty(Properties props, String name,
                                    Filter defaultValue) {
        if (props == null)
            return defaultValue;

        String val = props.getProperty(name);

        try {
            if (val != null) {
                Class clz = ClassLoader.getSystemClassLoader().loadClass(val);

                return (Filter) clz.newInstance();
            }
        } catch (Exception ex) {
            // We got one of a variety of exceptions in creating the
            // class or creating an instance.
            // Drop through.
        }
        // We got an exception.  Return the defaultValue.
        return defaultValue;
    }


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
    public synchronized Handler createHandler(String uniqueId, Properties properties)
            throws Exception {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        int type = ILogConstants.UNKNOWN_HANDLER_TYPE;

        String handler = properties.getProperty(ILogConstants.LOG_HANDLER, ILogConstants.LOG_HANDLER_DEF);

        if (handler.equalsIgnoreCase(FileHandler.class.getName()))
            type = ILogConstants.FILE_HANDLER_TYPE;

        else if (handler.equalsIgnoreCase(ConsoleHandler.class.
                getName()))
            type = ILogConstants.CONSOLE_HANDLER_TYPE;


        return createHandler(type, uniqueId, properties);
    }

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
            throws Exception {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        // In case handler for this uniqueId exists,
        // then return the already created one.

        // This occurs when application tries to create
        // unified connections. In that case fmq rtl creates
        // one queueConnection and one topic connection. As each connection
        // object has separate handler, then two handlers were getting created
        // at server side.
        //
        // - Aseem Bansal
        //
        FioranoLogHandler logHandler = null;
        if (properties.getProperty("IsInMemComponentLogger") == null) {  // The property IsInMemoryLogger specifies the special case of creating handler for InMemory component
            logHandler = getHandler(type, uniqueId);                     //  If the property is null , it will proceed normally as in all other cases else the case will be treated differently
            if (logHandler != null)
                return logHandler;
        }

        synchronized (m_syncObject) {
            if (properties.getProperty("IsInMemComponentLogger") == null) {
                logHandler = getHandler(type, uniqueId);

                if (logHandler != null)
                    return logHandler;
            }

            logHandler = _createHandler(type, properties, uniqueId);
        }

        if (logHandler == null)
            return null;

        Logger logger = getLogger(uniqueId);

        logger.setUseParentHandlers(false);

        if (properties.getProperty("IsInMemComponentLogger") == null || getHandler(type, uniqueId) == null)
            logger.addHandler(logHandler);

        return logHandler;
    }

    /**
     * Destroy the parameter handler.
     *
     * @param uniqueId
     * @param handler
     */
    public void destroyHandler(String uniqueId, Handler handler) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        Logger logger = getLogger(uniqueId);

        // Remove handler before flush and close so that no more
        // logs are passed to this handler object
        //
        logger.removeHandler(handler);

        handler.flush();
        handler.close();
    }

    /**
     * Destroy all handlers associated with the given id.
     *
     * @param uniqueId
     */
    public void destroyHandlers(String uniqueId) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        Logger logger = getLogger(uniqueId);

        Handler[] handlers = logger.getHandlers();

        for (int i = 0; i < handlers.length; i++)
            destroyHandler(uniqueId, handlers[i]);
    }

    /**
     * Clear error logs stored for a given Id
     *
     * @param uniqueId
     * @return
     */
    public boolean clearErrLogs(String uniqueId) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        // Algo:
        //
        // 1. In case fileHandler is not plugged in for this id, then
        //    return
        //
        //  Otherwise,
        //
        // 2.  Close and remove the fileHandler from logger table
        //    (This will give write access to all log files)
        //
        // 3. Clear the log files
        //
        // 4. Add the handlers
        //
        // Note: - It might be possible that somebody has the reference
        //         of FioranoHandler, so in re-creation, we only
        //         change the internal handlers.
        //

        // 1. In case fileHandler is not plugged in for this id, then
        //    return
        File[] logFiles = getErrLogFiles(uniqueId, true);


        m_logger.info(DefaultLogManager.class, "clearing_error_log_files", uniqueId, logFiles);

        if ((logFiles == null) || (logFiles.length == 0))
            return true;

        return _clearLogs(uniqueId, logFiles);
    }

    /**
     * Clear Out logs stored for a given Id
     *
     * @param uniqueId
     * @return
     */
    public boolean clearOutLogs(String uniqueId) {
        // Fixed N_3656
        // convert all ids to upper case as FMQ's
        // connection factory is in upper case.
        uniqueId = toUpperCase(uniqueId);

        // Check out the algo in clearErrLogs
        //

        File[] logFiles = getOutLogFiles(uniqueId, true);


        m_logger.info(DefaultLogManager.class, "clearing_out_log_files", uniqueId, logFiles);

        if ((logFiles == null) || (logFiles.length == 0))
            return true;

        return _clearLogs(uniqueId, logFiles);
    }

    /**
     * A unit test for JUnit
     *
     * @exception Exception
     */
    public void test()
            throws Exception {
        String uniqueId = "abc";

        m_config = new DefaultLogManagerConfig();

        Properties props = new Properties();

        props.setProperty("java.util.logging.FileHandler.limit", "5000");
        props.setProperty("java.util.logging.FileHandler.count", "2");
        props.setProperty("java.util.logging.FileHandler.level", Level.ALL.intValue() + "");

        System.out.println("Level.ALL.toString() " + Level.ALL.intValue());
        System.out.println("Level.SEVERE.toString() " + Level.SEVERE.intValue());

        Handler handler1 = createHandler(ILogConstants.FILE_HANDLER_TYPE, uniqueId, props);
        Handler handler2 = createHandler(ILogConstants.FILE_HANDLER_TYPE, uniqueId, props);

//        System.out.println("Handler " + handler);

        Logger logger = getLogger(uniqueId);

        logger.setUseParentHandlers(false);
//
        for (int i = 0; i < 3; i++)
            logger.info(I18NUtil.getMessage(this.getClass(), "test.0", i));

        ILogIterator iterator = getOutLogs(uniqueId);

        while (iterator.hasNext())
            System.out.println(iterator.next());

        boolean deleted = clearOutLogs(uniqueId);

        System.out.println(I18NUtil.getMessage(this.getClass(), "cleared.outlogs.0", deleted));

        destroyHandlers(uniqueId);

        handler1 = createHandler(ILogConstants.FILE_HANDLER_TYPE, uniqueId, props);
        handler2 = createHandler(ILogConstants.FILE_HANDLER_TYPE, uniqueId, props);

        for (int i = 0; i < 3; i++)
            logger.info(I18NUtil.getMessage(this.getClass(), "test.0", i));

        iterator = getOutLogs(uniqueId);

        while (iterator.hasNext())
            System.out.println(iterator.next());

        deleted = clearOutLogs(uniqueId);

        System.out.println(I18NUtil.getMessage(this.getClass(), "cleared.outlogs.0", deleted));

        destroyHandlers(uniqueId);

        System.in.read();
//
//        deleted = clearErrLogs(uniqueId);
//        System.out.println("clearErrLogs" + deleted);

    }

    void setLogger(IFioranoLogger logger) {
        m_logger = logger;
    }

    private FioranoLogHandler getHandler(int type, String uniqueId) {
        Logger logger = getLogger(uniqueId);

        Handler[] handlers = logger.getHandlers();

        for (int i = 0; i < handlers.length; i++) {
            if (!(handlers[i] instanceof FioranoLogHandler))
                continue;

            FioranoLogHandler logHandler = (FioranoLogHandler) handlers[i];

            if (type == logHandler.getLogType())
                return logHandler;
        }
        return null;
    }

    private Logger getLogger(String uniqueId) {
        uniqueId = uniqueId.toUpperCase();

        Logger parent = Logger.getLogger(ILogConstants.PARENT_LOGGER);
        Logger logger = parent.getLogger(uniqueId);

        return logger;
    }

    private File[] getErrLogFiles(String uniqueId, boolean inclueLckFiles) {
        // create file filter
        String[] posExt = new String[]{".err"};
        String[] negExt = null;

        if (!inclueLckFiles)
            negExt = new String[]{".lck"};

        FilenameFilter filter = new LogFileNameFilter(posExt, negExt);

        String dirName = getLogDirName(uniqueId);

        File dir = new File(dirName);

        if (!dir.exists())
            return null;

        return dir.listFiles(filter);
    }

    private File[] getOutLogFiles(String uniqueId, boolean inclueLckFiles) {
        // create file filter
        String[] posExt = new String[]{".out"};
        String[] negExt = null;

        if (!inclueLckFiles)
            negExt = new String[]{".lck"};

        FilenameFilter filter = new LogFileNameFilter(posExt, negExt);

        String dirName = getLogDirName(uniqueId);

        File dir = new File(dirName);

        if (!dir.exists())
            return null;

        return dir.listFiles(filter);
    }

    private String getOutFile(String uniqueId, Properties props) {
        return getLogFileName(uniqueId, props, ".out");
    }

    private String getErrFile(String uniqueId, Properties props) {
        return getLogFileName(uniqueId, props, ".err");
    }

    private String getLogFileName(String uniqueId, Properties props, String suffix) {
        String logDirName = getLogDirName(uniqueId, props);

        File logDir = new File(logDirName);

        if (!logDir.exists())
            logDir.mkdirs();

        String outFileName = normalize(uniqueId) + suffix;

        return logDirName + File.separator + outFileName;
    }

    private String getLogDirName(String uniqueId) {
        Properties props = getProperties(uniqueId);

        if (props == null)
            return getLogDirName(m_config.getLogDir(), uniqueId);

        return getLogDirName(uniqueId, props);
    }

    private Properties getProperties(String uniqueId) {
        FioranoLogHandler handler = getHandler(ILogConstants.FILE_HANDLER_TYPE,
                uniqueId);

        if (handler == null)
            return null;

        return handler.getProperties();
    }

    private String getLogDirName(String uniqueId, Properties props) {
        String propName = FileHandler.class.getName() + ".dir";
        String baseLogdir = props.getProperty(propName);

        // Default logs dir
        if ((baseLogdir == null) || (baseLogdir.trim().equalsIgnoreCase("")))
            baseLogdir = m_config.getLogDir();
        else if (!(FileUtil.isAbsolutePath(baseLogdir))) {
            String runDir = System.getProperty(Locations.RUN_DIR);

            if (runDir == null) {
                runDir = ".";
                m_logger.info(DefaultLogManager.class, "base_log_path", runDir);
            }

            baseLogdir = runDir + "/" + baseLogdir;
        }

        return getLogDirName(baseLogdir, uniqueId);
    }

    private String getLogDirName(String baseLogdir, String uniqueId) {
        String handlerDir = uniqueId;

        if (handlerDir.contains("__"))
            handlerDir = handlerDir.replaceAll("__", "/");

        return baseLogdir + File.separator + handlerDir;
    }

    private String toUpperCase(String id) {
        if (id == null)
            return null;

        return id.toUpperCase();
    }

    private boolean _clearLogs(String uniqueId, File[] logFiles)
            throws SecurityException {
        // 2.  Close and remove the fileHandler from logger table
        //    (This will give write access to all log files)
        //
        ArrayList removedHandlers = _removeFioranoLoggers(uniqueId);

        // 3. Clear the log files
        //
        boolean cleared = _clearLogs(logFiles);

        _addHandlers(uniqueId, removedHandlers);

        return cleared;
    }

    /* This method updates the handler of InMemory component loggers*/
    private void updateInMemComponentHandler(String uniqueId, FioranoOutHandler newOutHandler, FioranoErrorHandler newErrHandler)
            throws SecurityException {
        for (String loggerName : componentLoggerMap.get(uniqueId)) {
            Logger logger = getLogger(loggerName);
            FioranoLogHandler oldHandler = (FioranoLogHandler) logger.getHandlers()[0];
            oldHandler.setOutLogHandler(newOutHandler);
            oldHandler.setErrLogHandler(newErrHandler);
        }
        // update entries in fileHandlerMap
        fileHandlerMap.get(uniqueId)[0] = newOutHandler;
        fileHandlerMap.get(uniqueId)[1] = newErrHandler;
    }

    private void _addHandlers(String uniqueId, ArrayList removedHandlers)
            throws SecurityException {
        Iterator iter = removedHandlers.iterator();

        while (iter.hasNext()) {
            FioranoLogHandler oldHandler = (FioranoLogHandler) iter.next();

            int type = oldHandler.getLogType();
            Properties props = oldHandler.getProperties();

            try {
                FioranoLogHandler newHandler = _createHandler(type, props,
                        uniqueId);
                FioranoOutHandler newOutHandler = newHandler.getOutLogHandler();
                FioranoErrorHandler newErrHandler = newHandler.getErrLogHandler();

                oldHandler.setOutLogHandler(newOutHandler);
                oldHandler.setErrLogHandler(newErrHandler);

                if (componentLoggerMap.get(uniqueId) != null)        // If this uniqueId is present in componentLoggerMap , then its the case of InMemory Component Handler, so update the component's logger's handler
                    updateInMemComponentHandler(uniqueId, newOutHandler, newErrHandler);

                m_logger.info(DefaultLogManager.class, "adding_handler", uniqueId, newHandler);

                Logger logger = getLogger(uniqueId);

                logger.addHandler(oldHandler);
            } catch (IOException ex) {
                // Log and continue the good work
                ex.printStackTrace();
            }
        }
    }

    private ArrayList _removeFioranoLoggers(String uniqueId)
            throws SecurityException {
        ArrayList list = new ArrayList();
        Logger logger = getLogger(uniqueId);

        Handler[] handlers = logger.getHandlers();


        m_logger.info(DefaultLogManager.class, "removing_handlers", uniqueId, new Integer(handlers.length));

        for (int i = 0; i < handlers.length; i++) {
            if (!(handlers[i] instanceof FioranoLogHandler))
                continue;

            logger.removeHandler(handlers[i]);
            handlers[i].flush();
            handlers[i].close();


            m_logger.info(DefaultLogManager.class, "removed_handler", uniqueId, handlers[i]);

            list.add(handlers[i]);
        }

        return list;
    }

    private boolean _clearLogs(File[] logFiles) {
        if ((logFiles == null) || logFiles.length == 0)
            return true;

        boolean deleted = true;

        for (int i = 0; i < logFiles.length; i++) {
            boolean result = logFiles[i].delete();

            deleted = deleted && result;


            m_logger.info(DefaultLogManager.class, "cleared_log_file", logFiles[i].getAbsolutePath(), new Boolean(result));

        }

        return deleted;
    }

    private FioranoLogHandler _createHandler(int type, Properties props, String uniqueId)
            throws IOException {
        FioranoLogHandler handler = null;
        String cname = null;

        switch (type) {
            case ILogConstants.FILE_HANDLER_TYPE: {
                String outFile = getOutFile(uniqueId, props);
                String errorFile = getErrFile(uniqueId, props);


                m_logger.info(DefaultLogManager.class, "creating_file_log_handler", uniqueId, outFile, errorFile);
                cname = FileHandler.class.getName();

                Handler outFileHandler = null;
                Handler errFileHandler = null;
                FioranoOutHandler outHandler = null;
                FioranoErrorHandler errorHandler = null;

                try {
                    if (props.getProperty("IsInMemComponentLogger") == null) {
                        outFileHandler = _createFileHandler(outFile, props);
                        errFileHandler = _createFileHandler(errorFile, props);
                        outHandler = new FioranoOutHandler(outFileHandler, props);
                        errorHandler = new FioranoErrorHandler(errFileHandler, props);
                    } else {

                        boolean dirty = false;

                        if (fileHandlerMap.get(uniqueId) != null && fileHandlerMap.get(uniqueId)[0] != null) // if fileHandlerMap has outHandler then fetch it from there else create new outHandler
                        {
                            outHandler = (FioranoOutHandler) fileHandlerMap.get(uniqueId)[0];
                        } else {
                            outFileHandler = _createFileHandler(outFile, props);
                            outHandler = new FioranoOutHandler(outFileHandler, props);
                            dirty = true;
                        }
                        if (fileHandlerMap.get(uniqueId) != null && fileHandlerMap.get(uniqueId)[1] != null) // if fileHandlerMap has errorHandler then fetch it from there else create new errorHandler
                            errorHandler = (FioranoErrorHandler) fileHandlerMap.get(uniqueId)[1];
                        else {
                            errFileHandler = _createFileHandler(errorFile, props);
                            errorHandler = new FioranoErrorHandler(errFileHandler, props);
                            dirty = true;
                        }
                        if (dirty) {       // if new outHandler or errorHandler is created then replace the entry in fileHandlerMap
                            Handler[] fileHandlers = new Handler[2];
                            fileHandlers[0] = outHandler;
                            fileHandlers[1] = errorHandler;
                            fileHandlerMap.put(uniqueId, fileHandlers);
                        }

                        if (props.getProperty("LoggerName") != null) // update the componentLoggerMap if LoggerName property is not null
                        {
                            if (componentLoggerMap.get(uniqueId) == null) {
                                ArrayList<String> loggerList = new ArrayList<String>();
                                loggerList.add(props.getProperty("LoggerName"));
                                componentLoggerMap.put(uniqueId, loggerList);
                            } else {
                                if (!componentLoggerMap.get(uniqueId).contains(props.getProperty("LoggerName")))
                                    componentLoggerMap.get(uniqueId).add(props.getProperty("LoggerName"));
                            }
                        }

                    }


                    handler = new FioranoLogHandler(outHandler, errorHandler);
                } catch (IOException exp) {
                    // Fixed N_5443.
                    //
                    // In case exception is thrown while creating file handler
                    // then close half created file handlers and throw back the exception
                    //
                    if (outFileHandler != null)
                        outFileHandler.close();

                    if (errFileHandler != null)
                        errFileHandler.close();

                    handler = null;

                    throw exp;
                }

                break;
            }
            case ILogConstants.CONSOLE_HANDLER_TYPE: {
                cname = ConsoleHandler.class.getName();

                Handler outConsoleHandler = _createConsoleHandler(props);
                Handler errConsoleHandler = _createConsoleHandler(props);

                FioranoOutHandler outHandler = new FioranoOutHandler(
                        outConsoleHandler, props);
                FioranoErrorHandler errHandler = new FioranoErrorHandler(
                        errConsoleHandler, props);

                handler = new FioranoLogHandler(outHandler, errHandler);

                break;
            }
            default: {
                String handlerClass = props.getProperty(ILogConstants.LOG_HANDLER, ILogConstants.LOG_HANDLER_DEF);
                Handler customHandler = null;
                try {
                    customHandler = (Handler) Class.forName(handlerClass).newInstance();
                } catch (InstantiationException e) {
                    return null;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (ClassNotFoundException e) {
                    return null;
                }

                FioranoOutHandler outHandler = new FioranoOutHandler(customHandler, props);
                FioranoErrorHandler errHandler = new FioranoErrorHandler(customHandler, props);

                handler = new FioranoLogHandler(outHandler, errHandler);


            }
        }

        initialize(handler, cname, props);
        return handler;
    }

    /**
     * Initialize the handler object
     *
     * @param handler
     * @param cname
     * @param props
     */
    public void initialize(Handler handler, String cname, Properties props) {
        if (handler == null)
            return;

        Level level = getLevelProperty(props, cname + ".level",
                m_config.getLogLevel());
        Filter filter = getFilterProperty(props, cname + ".filter", null);
        Formatter formatter = getFormatterProperty(props, cname + ".formatter",
                m_config.getFormatterObject());

        if (formatter instanceof DefaultFormatter)
            ((DefaultFormatter) formatter).configure(props);

        String encoding = getStringProperty(props, cname + ".encoding",
                m_config.getEncoding());


        m_logger.info(DefaultLogManager.class, "initializing_handler", level, encoding, filter, formatter);

        level = Level.ALL;

        handler.setLevel(level);
        handler.setFilter(filter);
        handler.setFormatter(formatter);

        try {
            handler.setEncoding(encoding);
        } catch (Throwable ex) {
            m_logger.warn(DefaultLogManager.class, "err_initializing_handler", level, encoding, filter, formatter, ex);
        }
    }

    /**
     * Create File Handler
     *
     * @param defaultFileName
     * @param props
     * @return
     * @exception SecurityException
     * @exception IOException
     */
    private Handler _createFileHandler(String defaultFileName,
                                       Properties props)
            throws SecurityException, IOException {
        String cname = FileHandler.class.getName();

        String pattern = getStringProperty(props, cname + ".pattern",
                defaultFileName);
        //"%h/java%u.log");

        int limit = getIntProperty(props, cname + ".limit",
                m_config.getFileSizeLimit());
        int count = getIntProperty(props, cname + ".count",
                m_config.getFileCount());
        boolean append = getBooleanProperty(props, cname + ".append",
                m_config.getAppendToFile());


        m_logger.info(DefaultLogManager.class, "create_file_handler", new Integer(limit), new Integer(count), new Boolean(append));

        Handler handler = new FileHandler(pattern, limit, count, append);

        deleteLckOnExist(pattern, count);
        return handler;
    }

    private void deleteLckOnExist(String actualPattern, int count) {
        if (actualPattern == null)
            return;

        deleteOnExist(actualPattern + ".lck");

        for (int i = 0; i < count; i++) {
            deleteOnExist(actualPattern + "." + i + ".lck");
        }
    }

    private void deleteOnExist(String fileName) {
        try {
            File file = new File(fileName);

            file.deleteOnExit();
        } catch (Throwable ex) {
            // Ignore the exception
        }
    }

    /**
     * Create console Handler
     *
     * @param props
     * @return
     * @exception SecurityException
     * @exception IOException
     */
    private Handler _createConsoleHandler(Properties props)
            throws SecurityException, IOException {
        Handler handler = new ConsoleHandler();

        return handler;
    }

    private String normalize(String uniqueId) {
        uniqueId = uniqueId.replace('/', '_');
        uniqueId = uniqueId.replace('\\', '_');
        uniqueId = uniqueId.replace('.', '_');

        return uniqueId;
    }

    public static HashMap<String, Handler[]> getFileHandlerMap() {
        return fileHandlerMap;
    }

    public static HashMap<String, ArrayList<String>> getComponentLoggerMap() {
        return componentLoggerMap;
    }

    private void printHandlers(String uniqueId) {
        Logger logger = getLogger(uniqueId);

        Handler[] handlers = logger.getHandlers();

        System.out.println(I18NUtil.getMessage(this.getClass(), "handler.length.0", handlers.length));

        for (int i = 0; i < handlers.length; i++)
            System.out.println(I18NUtil.getMessage(this.getClass(), "handlers.0", handlers[i]));
    }

    /**
     * <p><strong> </strong> represents </p>
     *
     * @author FSIPL
     * @created June 22, 2005
     * @version 1.0
     */
    class LogFileNameFilter
            implements FilenameFilter {
        private String[] m_positveExts;
        private String[] m_negativeExts;

        /**
         * @param positiveExt
         * @param negativeExt
         */
        public LogFileNameFilter(String[] positiveExt, String[] negativeExt) {
            m_positveExts = positiveExt;
            m_negativeExts = negativeExt;
        }

        /**
         * Tests if a specified file should be included in a file list.
         *
         * @param dir the directory in which the file was found.
         * @param name the name of the file.
         * @return <code>true</code> if and only if the name should be
         * included in the file list; <code>false</code> otherwise.
         */
        public boolean accept(File dir, String name) {
            if (!matchPositive(name))
                return false;

            if (matchNegative(name))
                return false;

            return true;
        }

        private boolean matchNegative(String name) {
            if ((m_negativeExts == null) || m_negativeExts.length == 0)
                return false;

            for (int i = 0; i < m_negativeExts.length; i++) {
                int index = name.indexOf(m_negativeExts[i]);

                if (index != -1)
                    return true;
            }

            return false;
        }

        private boolean matchPositive(String name) {
            if ((m_positveExts == null) || m_positveExts.length == 0)
                return true;

            for (int i = 0; i < m_positveExts.length; i++) {
                int index = name.indexOf(m_positveExts[i]);

                if (index != -1)
                    return true;
            }

            return false;
        }
    }

}
