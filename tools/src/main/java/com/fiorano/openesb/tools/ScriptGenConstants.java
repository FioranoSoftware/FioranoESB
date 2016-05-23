/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

public interface ScriptGenConstants {

    //properties
    static final String JVM_PARAMS = "JVM_PARAMS";
    static final String CLASSPATH = "-classpath";
    static final String LOG_MODULE_PREFIX = "LOG_MODULE_";
    public static final String LOG_MODULE_IS_UNIQUE_PREFIX = "LOGGER_IS_UNIQUE_";
    static final String RUNTIME_ARG_PREFIX = "RUNTIME_ARG_";
    static final String LOG_CATEGORIES = "LOG_CATEGORIES";
    static final String ENDORSED_DIRS = "java.endorsed.dirs";
    static final String EXT_DIRS = "java.ext.dirs";
    static final String LIBRARY_PATH = "java.library.path";

    //log manager properties
    static final String INCLUDE_TIMESTAMP = "fiorano.jms.log2.def.DefaultFormatter.includetimestamp";
    static final String DATE_FORMAT = "fiorano.jms.log2.def.DefaultFormatter.dateformat";
    static final String HANDLER_CLASS = "java.util.logging.handler";
    static final String FILE_HANDLER_CLASS = "java.util.logging.FileHandler";
    static final String FILE_HANDLER_COUNT = "java.util.logging.FileHandler.count";
    static final String FILE_HANDLER_LIMIT = "java.util.logging.FileHandler.limit";
    static final String FILE_HANDLER_DIR = "java.util.logging.FileHandler.dir";
    static final String CONSOLE_HANDLER_CLASS = "java.util.logging.ConsoleHandler";

    //others
    static final String FIORANO_HOME = "FIORANO_HOME";
}
