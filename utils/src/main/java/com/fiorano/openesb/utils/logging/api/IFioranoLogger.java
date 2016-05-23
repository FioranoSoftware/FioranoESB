/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging.api;


import com.fiorano.openesb.utils.exception.FioranoException;

public interface IFioranoLogger
{
    public static final int TRACE=1;
    public static final int DEBUG=2;
    public static final int INFO=3;
    public static final int WARNING =4;
    public static final int FATAL =5;

    //Initialize the Logger
    /**
     * @exception FioranoException If it is not able to configure the logger.
     */
    public void configure()
        throws FioranoException;

    /**
     * @exception FioranoException if its not able to reconfigure the logger.
     */
    public void reconfigure()
        throws FioranoException;

    /**
     *  Check to see if the TRACE level is enabled for this logger.
     *
     * @return true if a {@link #trace(Object)} method invocation would pass
     *      the msg to the configured appenders, false otherwise.
     */
    public boolean isTraceEnabled();

    /**
     *  Issue a log msg with a level of TRACE. Invokes log.log(XLevel.TRACE,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void trace(Throwable t);

    /**
     * @param obj
     */
    public void trace(Object obj);

    /**
     * @param message String
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String message);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param obj6 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Object obj6);

    /**
     *  Check to see if the TRACE level is enabled for this logger.
     *
     * @return true if a {@link #trace(Object)} method invocation would pass
     *      the msg to the configured appenders, false otherwise.
     */
    public boolean isDebugEnabled();

    /**
     *  Issue a log msg with a level of DEBUG. Invokes log.log(Level.DEBUG,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void debug(Throwable t);

    /**
     * @param obj
     */
    public void debug(Object obj);

    /**
     * @param msgCode String
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     *  Check to see if the INFO level is enabled for this logger.
     *
     * @return true if a {@link #info(Object)} method invocation would pass
     *      the msg to the configured appenders, false otherwise.
     */
    public boolean isInfoEnabled();

    /**
     *  Issue a log msg with a level of INFO. Invokes log.log(Level.INFO,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void info(Throwable t);

    /**
     * @param obj
     */
    public void info(Object obj);

    /**
     * @param message String
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String message);

    /**
     * @param message String
     * @param obj Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String message, Object obj);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     *  Issue a log msg with a level of WARN. Invokes log.log(Level.WARN,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void warn(Throwable t);

    /**
     * @param obj
     */
    public void warn(Object obj);

    /**
     * @param msgCode String
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     *  Issue a log msg with a level of ERROR. Invokes log.log(Level.ERROR,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void error(Throwable t);

    /**
     * @param obj
     */
    public void error(Object obj);

    /**
     * @param msgCode String
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     *  Issue a log msg with a level of FATAL. Invokes log.log(Level.FATAL,
     *  message);
     *
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     */
    public void fatal(Throwable t);

    /**
     * @param obj
     */
    public void fatal(Object obj);

    /**
     * @param msgCode String
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param clazz Name of the class for which logging has to be done
    */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5);

    /**
     * @param message String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String message, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param t Throwable toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param obj6 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void trace(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Object obj6, Throwable t);

    /**
     * @param msgCode String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
    */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void debug(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Throwable t);

    /**
     * @param message String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String message, Throwable t);

    /**
     * @param message String
     * @param obj Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String message, Object obj, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void info(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Object obj5,
                     Throwable t);

    /**
     * @param msgCode String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void warn(Class clazz, String msgCode, Object obj1,
                     Object obj2, Object obj3, Object obj4, Object obj5,
                     Throwable t);

    /**
     * @param msgCode String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
    */
    public void error(Class clazz, String msgCode, Object obj1, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void error(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Throwable t);

    /**
     * @param msgCode String
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Throwable t);

    /**
     * @param msgCode String
     * @param obj1 Object
     * @param obj2 Object
     * @param obj3 Object
     * @param obj4 Object
     * @param obj5 Object
     * @param t toString() of the Throwable cause, containing the class name as well as the localized Message
     * @param clazz Name of the class for which logging has to be done
     */
    public void fatal(Class clazz, String msgCode, Object obj1,
                      Object obj2, Object obj3, Object obj4, Object obj5,
                      Throwable t);

    // Sawant

    /**
     *  Sets the level attribute of the Logger object
     *
     * @param level The new level value
     *      logging level"
     */
    public void setLevel(int level);

    /**
     *  Gets the level attribute of the Logger object
     *
     * @return The level value
     *      logging level"
     */
    public int getLevel();

    /**
     * Returns error enabled for object
     *
     * @return Boolean indicating whether Error is enabled on the logger or not.
     */
    public boolean isErrorEnabled();

    /**
     * Returns fatal enabled for object
     *
     * @return  Boolean indicating whether Fatal is enabled on the logger or not.
     */
    public boolean isFatalEnabled();

    /**
     * Returns warn enabled for object
     *
     * @return Boolean indicating whether Warn is enabled on the logger or not.
     */
    public boolean isWarnEnabled();

    /**
     * Returns enabled for for object
     *
     * @param level Log Level
     * @return Boolean  
     */
    public boolean isEnabledFor(int level);

    void log(int level, Class clazz, String msgCode, Throwable t);

    void log(int level, Class clazz, String msgCode, Object obj1, Throwable t);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Throwable t);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t);

    void log(int level, Throwable t);

    void log(int level, Object obj);

    void log(int level, Class clazz, String message);

    void log(int level, Class clazz, String message, Object obj);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4);

    void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5);
}
