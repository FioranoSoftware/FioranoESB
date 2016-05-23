/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorMessage {
    Date date=new Date();
    String pattern = "[dd/MMM/yyyy HH:mm:ss] ";
    private StringBuilder msg;
    ErrorMessage() {
        msg=new StringBuilder("");
    }

    public void append(String errMsg) {
        msg.append(new SimpleDateFormat(pattern).format(date) + " " + errMsg + "\n");
    }
    static void appendStackTrace(StringBuilder str,Exception e){
        for (StackTraceElement element: e.getStackTrace()){
            str.append(element.toString());
            str.append("\n");
        }
    }
    public void append(String errMsg,Exception e) {
        msg.append(new SimpleDateFormat(pattern ).format(date) + " "+errMsg + "\n" + e.getMessage() + "\n");
        appendStackTrace(msg, e);
    }

    public void append(Exception e) {
        msg.append(e.getMessage()+"\n"+new SimpleDateFormat(pattern ).format(date)+" "+e.getMessage()+"\n");
        appendStackTrace(msg, e);
    }

    public void setMessage(String errMsg) {
        msg=new StringBuilder(errMsg);
    }

    public String getMessage(){
        return msg.toString();
    }
}
