/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;


import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;

public final class LoggerUtil {
    static String JNDI_CONSTANT = "__";
    static String LOG_NAME_SEPARATOR = ".";

    /**
     * returns instance based logger name for a given logger name
     *
     * @param loggerName - Logger name mentioned in the Service Descriptor
     * @param appGUID - Application's GUID
     * @param serviceGUID - Service's GUID
     * @param serviceInstanceName - Service's instance name
     * @return instance based logger name for service if all the parameters are not empty else the logger name as it is
     */
    public static String getServiceLoggerName(String loggerName, String appGUID, String appVersion, String serviceGUID,
                                              String serviceInstanceName) {
        if (StringUtil.isEmpty(loggerName) || StringUtil.isEmpty(appGUID) || StringUtil.isEmpty(serviceGUID)
                || StringUtil.isEmpty(serviceInstanceName) || StringUtil.isEmpty(appVersion)) {
            return loggerName;
        }
        String enclosedServiceGUID = LOG_NAME_SEPARATOR + serviceGUID.toLowerCase() + LOG_NAME_SEPARATOR;
        String logName;
        if (loggerName.equals(serviceGUID) || loggerName.toLowerCase().indexOf(enclosedServiceGUID) == -1) {
            logName = LookUpUtil.getApplicationLookupName(appGUID.toLowerCase(), appVersion) + LOG_NAME_SEPARATOR +
                    serviceGUID.toLowerCase() + LOG_NAME_SEPARATOR +
                    serviceInstanceName.toLowerCase() + LOG_NAME_SEPARATOR + loggerName;
            return logName.toUpperCase();
        } else {
            logName = LookUpUtil.getApplicationLookupName(appGUID.toLowerCase(), appVersion) + LOG_NAME_SEPARATOR +
                    getPrefix(loggerName.toLowerCase(), enclosedServiceGUID) + enclosedServiceGUID +
                    serviceInstanceName.toLowerCase() + LOG_NAME_SEPARATOR +
                    getSuffix(loggerName.toLowerCase(), enclosedServiceGUID);
            return logName.toUpperCase();
        }
    }

    /**
     * returns instance based logger name for a given logger
     *
     * @param loggerName - Logger name mentioned in the Service Descriptor
     * @param connFactoryName - connection factory name
     * @param serviceGUID - Service's GUID
     * @return instance based logger name for service if all parameters are not empty and connFactoryName contains __ else the logger name as it is
     */
    public static String getServiceLoggerName(String loggerName, String connFactoryName, String serviceGUID) {
        String appGUID=getApplicationGUID(connFactoryName);
        String serviceInstanceName=getServiceInstanceName(connFactoryName);
        String appVersionString = getAppVersionString(connFactoryName);
        return getServiceLoggerName(loggerName, appGUID, appVersionString, serviceGUID, serviceInstanceName);
    }

    /**
     * returns logger created using instance based logger name for a given logger name
     *
     * @param loggerName - Logger name mentioned in the Service Descriptor
     * @param appGUID - Application's GUID
     * @param serviceGUID - Service's GUID
     * @param serviceInstanceName - Service's instance name
     * @return instance based logger name for service if all the parameters are not empty else the logger name as it is
     */
    public static Logger getServiceLogger(String loggerName, String appGUID, String appVersion, String serviceGUID,
                                          String serviceInstanceName) {
        return Logger.getLogger(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, serviceInstanceName));
    }

    /**
     * returns logger created using instance based logger name for a given logger name
     *
     * @param loggerName - Logger name mentioned in the Service Descriptor
     * @param connFactoryName - connection factory name
     * @param serviceGUID - Service's GUID
     * @return instance based logger name for service if all parameters are not empty and connFactoryName contains __ else the logger name as it is
     */
    public static Logger getServiceLogger(String loggerName, String connFactoryName, String serviceGUID) {
        return Logger.getLogger(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
    }

    public static void addFioranoConsoleHandler(Logger logger, boolean checkForSystemProperty) {
        if (logger == null) {
            return;
        }
        //at any time it is required to have only one FioranoConsoleHandler
        //adding this as there are many cases where FioranoConsoleHandlers are added without removing them first
        removeFioranoConsoleHandlers(logger);
        logger.setUseParentHandlers(false);
        //todo need to do this based on whether to go for log4j logging
//        FioranoConsoleHandler fioranoConsoleHandler = new FioranoConsoleHandler(checkForSystemProperty);
//        fioranoConsoleHandler.setLevel(Level.parse(System.getProperty(logger.getName(), Level.INFO.getName())));
//        logger.addHandler(fioranoConsoleHandler);
        logger.setLevel(Level.ALL);
    }

    /**
     * Setting the log level on handlers. In Fiorano, there will be only one handler at all times usually. Do not the
     * level on the logger. When CCP is not used, the log level changes are handled in the MQ layer and the level is set
     * on the handler. So if log level on logger is set to a value for lesser logging, those logs will not be seen even when
     * logging is increased.
     * @param logger
     * @param level
     */
    public static void setLevel(Logger logger, Level level) {
        for(Handler handler : logger.getHandlers()) {
            handler.setLevel(level);
        }
    }

    /**
     * Just returning the level of lone handler used by components.
     * See #setLevel(Logger,Level) for explanation.
     *
     * @param logger
     * @return
     */
    public static Level getLevel(Logger logger) {
        Handler[] handlers = logger.getHandlers();
        return handlers.length != 0 ? handlers[0].getLevel() : logger.getLevel();
    }

    public static void addFioranoConsoleHandler(Logger logger) {
        addFioranoConsoleHandler(logger, true);
    }

    public static void removeFioranoConsoleHandlers(Logger logger) {
        if (logger == null) {
            return;
        }
        Handler[] handlers = logger.getHandlers();
        if (handlers == null || handlers.length == 0) {
            return;
        }
        for (int i = 0; i < handlers.length; i++) {
            Handler handler = handlers[i];
            //todo
            if (handler.getClass().getName().equals(/*FioranoConsoleHandler.class.getName())*/ "")) {
                //Do not use instanceof as java.util.Logging stores loggers in a static LogManager object.
                //When console handlers are added from different classloaders then identifying them using
                //instance of will not work
                logger.removeHandler(handler);
                try {
                    handler.close();
                } catch (SecurityException e) {
                    //do nothing
                } finally {
                    handler = null;
                }
            }
        }
    }

    private static String getSuffix(String givenString, String searchString) {
        return givenString.substring(givenString.indexOf(searchString) + searchString.length());
    }

    private static String getPrefix(String givenString, String searchString) {
        return givenString.substring(0, givenString.indexOf(searchString));
    }

    /**
     * Returns the Application Instance Name
     */
    private static String getApplicationGUID(String connFactoryName) {
        if (connFactoryName != null && connFactoryName.indexOf(JNDI_CONSTANT) != -1) {
            return getPrefix(connFactoryName, JNDI_CONSTANT);
        } else {
            return null;
        }
    }

    /**
     * Returns the Application Instance Name
     */
    private static String getAppVersionString(String connFactoryName) {
        if (connFactoryName != null && connFactoryName.indexOf(JNDI_CONSTANT) != -1) {
            String conFacWithOutAppGuid = getSuffix(connFactoryName, JNDI_CONSTANT);
            return getPrefix(conFacWithOutAppGuid, JNDI_CONSTANT);
        } else {
            return null;
        }
    }

    /**
     * Returns the Service Instance Name
     */
    private static String getServiceInstanceName(String connFactoryName) {
        if (connFactoryName != null && connFactoryName.indexOf(JNDI_CONSTANT) != -1) {
            String conFacWithOutAppGuid = getSuffix(connFactoryName, JNDI_CONSTANT);
            return getSuffix(conFacWithOutAppGuid, JNDI_CONSTANT);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        String conFactoryName = null;
        System.out.println(getApplicationGUID(conFactoryName));
        System.out.println(getServiceInstanceName(conFactoryName));
        System.out.println("------");
        conFactoryName = "sadfas";
        System.out.println(getApplicationGUID(conFactoryName));
        System.out.println(getServiceInstanceName(conFactoryName));
        System.out.println("------");
        conFactoryName = "sdfds__";
        System.out.println(getApplicationGUID(conFactoryName));
        System.out.println(getServiceInstanceName(conFactoryName));
        System.out.println("------");
        conFactoryName = "__sadfas";
        System.out.println(getApplicationGUID(conFactoryName));
        System.out.println(getServiceInstanceName(conFactoryName));
        System.out.println("------");
        conFactoryName = "asdasf__sadfas";
        System.out.println(getApplicationGUID(conFactoryName));
        System.out.println(getServiceInstanceName(conFactoryName));
        System.out.println("------");

        System.out.println("Logger name tests");
        String serviceGUID = null;
        String componentName = null;
        String connFactoryName = null;
        String loggerName = null;
        String appGUID = null;
        String appVersion = null;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        loggerName = "com.fiorano.bc.DB.DBConnFact";
        appGUID = "app";
        appVersion= "1.0";
        serviceGUID = "DB";
        componentName = "MyDB";
        connFactoryName = appGUID + JNDI_CONSTANT + componentName;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        connFactoryName = appGUID + JNDI_CONSTANT;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        connFactoryName = JNDI_CONSTANT + componentName;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        connFactoryName = JNDI_CONSTANT;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        loggerName = "com.fioranoDB.bc.db.DBConnFact";
        connFactoryName = appGUID + JNDI_CONSTANT + componentName;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        loggerName = "abc";
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
        loggerName = serviceGUID;
        System.out.println(getServiceLoggerName(loggerName, appGUID, appVersion, serviceGUID, componentName));
        System.out.println(getServiceLoggerName(loggerName, connFactoryName, serviceGUID));
        System.out.println("-----");
    }
}