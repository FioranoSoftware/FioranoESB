/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging.api;

import com.fiorano.openesb.utils.I18NUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;

public class FioranoSLF4JLogger implements IFioranoLogger {

    Logger logger;
    String strName;

    public FioranoSLF4JLogger(Logger logger) {
        strName = logger.getName();
        this.logger = logger;
    }


    public void configure() throws FioranoException {
        
    }

    public void reconfigure() {
        //dummy implementation
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void trace(Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(" ",t);
    }

    public void trace(Object obj) {
        if (!isTraceEnabled())
            return;

        logger.trace(" {} ", obj);
    }

    public void trace(Class clazz, String message) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, message));
    }

    public void trace(Class clazz, String msgCode, Object obj1) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1));
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, obj6));
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(" ",t);

    }

    public void debug(Object obj) {
        if (!isDebugEnabled())
            return;

        logger.debug(" {} ", obj);
    }

    public void debug(Class clazz, String msgCode) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode));
    }

    public void debug(Class clazz, String msgCode, Object obj1) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1));
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(" ",t);
    }

    public void info(Object obj) {
        if (!isInfoEnabled())
            return;

        logger.info(" {} ", obj);
    }

    public void info(Class clazz, String message) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, message));
    }

    public void info(Class clazz, String message, Object obj) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, message, obj));
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public void warn(Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(" ",t);
    }

    public void warn(Object obj) {
        if (!isWarnEnabled())
            return;

        logger.warn(" {} ", obj);
    }

    public void warn(Class clazz, String msgCode) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode));
    }

    public void warn(Class clazz, String msgCode, Object obj1) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1));
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public void error(Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(" ",t);
    }

    public void error(Object obj) {
        if (!isErrorEnabled())
            return;

        logger.error(" {} ", obj);
    }

    public void error(Class clazz, String msgCode) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode));
    }

    public void error(Class clazz, String msgCode, Object obj1) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1));
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public void fatal(Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(" ",t);
    }

    public void fatal(Object obj) {
        if (!isErrorEnabled())
            return;

        logger.error(" {} ", obj);
    }

    public void fatal(Class clazz, String msgCode) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode));
    }

    public void fatal(Class clazz, String msgCode, Object obj1) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1));
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2));
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3));
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4));
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, null), I18NUtil.getMessage(clazz, msgCode, obj1, obj2, obj3, obj4, obj5));
    }

    public void trace(Class clazz, String message, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, message, ""), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void trace(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Throwable t) {
        if (!isTraceEnabled())
            return;

        logger.trace(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, obj6), t);
    }

    public void debug(Class clazz, String msgCode, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, ""), t);
    }

    public void debug(Class clazz, String msgCode, Object obj1, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, obj1), t);
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void debug(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isDebugEnabled())
            return;

        logger.debug(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void info(Class clazz, String message, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, message, ""), t);
        //logger.info(" [ " + clazz.toString() + " ] " + " {} {} ", I18NUtil.getMessage(clazz, message),t.getStackTrace());
    }

    public void info(Class clazz, String message, Object obj, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, message, obj), t);
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void info(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isInfoEnabled())
            return;

        logger.info(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void warn(Class clazz, String msgCode, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, ""), t);
    }

    public void warn(Class clazz, String msgCode, Object obj1, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, obj1), t);
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void warn(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isWarnEnabled())
            return;

        logger.warn(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void error(Class clazz, String msgCode, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, ""), t);
    }

    public void error(Class clazz, String msgCode, Object obj1, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1), t);
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void error(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void fatal(Class clazz, String msgCode, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, ""), t);
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1), t);
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2), t);
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3), t);
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3, obj4), t);
    }

    public void fatal(Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t) {
        if (!isErrorEnabled())
            return;

        logger.error(toString(clazz, msgCode, obj1, obj2, obj3, obj4, obj5), t);
    }

    public void log(int level, Throwable t){
         switch (level){
            case TRACE:
                trace(t);
                break;
            case DEBUG:
                debug(t);
                break;
            case INFO:
                info(t);
                break;
            case WARNING:
                warn(t);
                break;
            case FATAL:
                fatal(t);
                break;
        }
    }

    public void log(int level, Object obj){
         switch (level){
            case TRACE:
                trace(obj);
                break;
            case DEBUG:
                debug(obj);
                break;
            case INFO:
                info(obj);
                break;
            case WARNING:
                warn(obj);
                break;
            case FATAL:
                fatal(obj);
                break;
        }
    }

    public void log(int level, Class clazz, String message){
        switch (level){
            case TRACE:
                trace(clazz, message);
                break;
            case DEBUG:
                debug(clazz, message);
                break;
            case INFO:
                info(clazz, message);
                break;
            case WARNING:
                warn(clazz, message);
                break;
            case FATAL:
                fatal(clazz, message);
                break;
        }
    }

    public void log(int level, Class clazz, String message, Object obj){
        switch (level){
            case TRACE:
                trace(clazz, message, obj);
                break;
            case DEBUG:
                debug(clazz, message, obj);
                break;
            case INFO:
                info(clazz, message, obj);
                break;
            case WARNING:
                warn(clazz, message, obj);
                break;
            case FATAL:
                fatal(clazz, message, obj);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3, obj4);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3, obj4);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3, obj4);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3, obj4);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3, obj4);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3, obj4, obj5);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3, obj4, obj5);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3, obj4, obj5);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3, obj4, obj5);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3, obj4, obj5);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, t);
                break;
            case INFO:
                info(clazz, msgCode, t);
                break;
            case WARNING:
                warn(clazz, msgCode, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, t);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, t);
                break;
            case INFO:
                info(clazz, msgCode, obj1, t);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, t);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, t);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, t);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, t);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3, t);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3, t);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3, t);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3, obj4, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3, obj4, t);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3, obj4, t);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3, obj4, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3, obj4, t);
                break;
        }
    }

    public void log(int level, Class clazz, String msgCode, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Throwable t){
        switch (level){
            case TRACE:
                trace(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, t);
                break;
            case DEBUG:
                debug(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, t);
                break;
            case INFO:
                info(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, t);
                break;
            case WARNING:
                warn(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, t);
                break;
            case FATAL:
                fatal(clazz, msgCode, obj1, obj2, obj3, obj4, obj5, t);
                break;
        }
    }

    public void setLevel(int level) {
        //dummy implementation
    }

    public int getLevel() {
        return 0;  //dummy implementation
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return isErrorEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isEnabledFor(int level) {
        return  false;
    }

    private String toString(Class clazz, String message, Object... objects ) {
        StringBuilder sb = new StringBuilder();
        sb.append(" [ ");
        sb.append(clazz.getName());

        if(objects.length > 0)  {
            sb.append(" ]  ");
            sb.append(I18NUtil.getMessage(clazz, message, objects));
        }
        else
            sb.append(" ]  {} ");


        return sb.toString();
    }


}
