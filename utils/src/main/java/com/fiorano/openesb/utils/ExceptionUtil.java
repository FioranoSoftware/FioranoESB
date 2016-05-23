/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import org.xml.sax.SAXParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 *
 * @bundle EXC_MissingResourceException_class_name=Missing resource from class: {0}
 * @bundle EXC_MissingResourceException_key=Key which was not found: {0}
 * @bundle EXC_sax_parse=Parse error in file {1} (PUBLIC {0})
 * @bundle EXC_sax_parse_col_line=Parse error in line {1} column {0}
 */
public final class ExceptionUtil {
    private static Properties NESTS = new Properties();
    static{
        try{
            NESTS.load(ExceptionUtil.class.getResourceAsStream("nested-exceptions.properties")); //NOI18N
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Throwable[] getCauses(Throwable thr){
        ArrayList list = new ArrayList();
        while(thr!=null){
            list.add(thr);
            thr = getCause(thr);
        }
        return (Throwable[])list.toArray(new Throwable[list.size()]);
    }

    public static Throwable getCause(Throwable thr){
        Throwable cause = null;

        try{
            Method method = thr.getClass().getMethod("getCause", null);  //NOI18N
            if(method!=null)
                cause = (Throwable)method.invoke(thr, null);
        } catch(Throwable ignore){
            // the application is running in jdk1.3
        }
        return cause != null ? cause : extractNestedThrowable(thr);
    }

    private static Throwable extractNestedThrowable(Throwable t) {
        for (Class c = t.getClass(); c != Object.class; c = c.getSuperclass()) {
            String getter = (String)NESTS.get(c.getName());
            if (getter != null) {
                try {
                    if (getter.charAt(0) == '.') { // NOI18N
                        Field f = c.getField(getter.substring(1));
                        return (Throwable)f.get(t);
                    } else {
                        Method m = c.getMethod(getter, null);
                        return (Throwable)m.invoke(t, null);
                    }
                } catch (Exception e) {
                    // Should not happen.
                    System.err.println("From throwable class: " + c.getName()); // NOI18N
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static void printStackTrace3(Throwable cause[], PrintStream ps){
        for(int i = 0; i<cause.length; i++){
            if(i>0)
                ps.println("caused by");   //NOI18N
            cause[i].printStackTrace(ps);
        }
    }

    private static boolean checkedPrintStackTrace4 = false;
    private static Method printStackTrace4 = null;
    private static void printStackTrace4(Throwable cause[], PrintStream ps){
        if(!checkedPrintStackTrace4){
            try{
                Class clazz = Class.forName("com.fiorano.util.ExceptionUtil4");
                printStackTrace4 = clazz.getMethod("printStackTrace", new Class[]{ Throwable[].class, PrintStream.class });
            }catch(ClassNotFoundException ignore){
                ignore.printStackTrace();
            }catch(NoSuchMethodException ignore){
                ignore.printStackTrace();
            }finally{
                checkedPrintStackTrace4 = true;
            }
        }

        try{
            if(printStackTrace4!=null)
                printStackTrace4.invoke(null, new Object[] { cause, ps} );
        } catch(Exception e){
            printStackTrace3(cause, ps);
        }
    }

    public static void printStackTrace(Throwable cause[], PrintStream ps){
        boolean jdk13 = System.getProperty("java.version").indexOf("1.3")!=-1;
        if(jdk13)
            printStackTrace3(cause, ps);
        else
            printStackTrace4(cause, ps);
    }

    public static void printStackTrace(Throwable cause[]){
        printStackTrace(cause, System.err);
    }

    public static void printStackTrace(Throwable thr, PrintStream ps){
        printStackTrace(getCauses(thr), ps);
    }

    public static void printStackTrace(Throwable thr){
        printStackTrace(getCauses(thr));
    }

    public static String getStackTrace(Throwable thr){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        printStackTrace(thr, new PrintStream(bos));
        try{
            bos.close();
        } catch(IOException ignore){
            ignore.printStackTrace();
        }
        return bos.toString();
    }

    public static String getStackTrace(Throwable cause[]){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        printStackTrace(cause, new PrintStream(bos));
        try{
            bos.close();
        } catch(IOException ignore){
            ignore.printStackTrace();
        }
        return bos.toString();
    }

    public static String getMessage(Throwable thr){
        Throwable[] cause = getCauses(thr);
        int icause = cause.length-1;

        String message = null;
        while(icause>=0 && (message=toString(cause[icause]))==null)
            --icause;
        if(message==null)
            message = thr.getClass().getName();
        return message;
    }

    public static String toString(Throwable thr){
        if(thr instanceof MissingResourceException)
            return toString((MissingResourceException)thr);
        else if(thr instanceof SAXParseException)
            return toString((SAXParseException)thr);
        else
            return thr.getMessage();
    }

    /**
     * @bundle # {0} - class name from MRE
     * @bundle EXC_MissingResourceException_class_name=Missing resource from class: {0}
     * @bundle # {0} - key from MRE
     * @bundle EXC_MissingResourceException_key=Key which was not found: {0}
     */
    private static String toString(MissingResourceException mre){
        StringBuffer message = new StringBuffer();

        String className = mre.getClassName();
        if(className!=null){
            if(message.length()>0)
                message.append('\n');
            message.append(RBUtil.getMessage(ExceptionUtil.class, "EXC_MissingResourceException_class_name", className));
        }
        String key = mre.getKey();
        if(key!=null){
            if(message.length()>0)
                message.append('\n');
            message.append(RBUtil.getMessage(ExceptionUtil.class, "EXC_MissingResourceException_key", key));
        }
        String msg = mre.getMessage();
        if(msg!=null){
            if(message.length()>0)
                message.append('\n');
            message.append(msg);
        }
        return message.toString();
    }

    /**
     * @bundle # {0} - public ID
     * @bundle # {1] - system ID
     * @bundle EXC_sax_parse=Parse error in file {1} (PUBLIC {0})
     * @bundle # {0} - column #
     * @bundle # {1} - line #
     * @bundle EXC_sax_parse_col_line=Parse error in line {1} column {0}
     */
    private static String toString(SAXParseException spe){
        StringBuffer message = new StringBuffer();

        String pubid = spe.getPublicId();
        String sysid = spe.getSystemId();

        if(pubid!=null || sysid!=null){
            if(message.length()>0)
                message.append('\n');
            message.append(RBUtil.getMessage(ExceptionUtil.class, "EXC_sax_parse", String.valueOf(pubid), String.valueOf(sysid)));
        }

        int col = spe.getColumnNumber();
        int line = spe.getLineNumber();
        if(col!=-1 || line!=-1){
            if(message.length()>0)
                message.append('\n');
            message.append(RBUtil.getMessage(ExceptionUtil.class, "EXC_sax_parse_col_line", new Object[]{new Integer(col), new Integer(line)}));
        }

        String msg = spe.getMessage();
        if(msg!=null){
            if(message.length()>0)
                message.append('\n');
            message.append(msg);
        }
        return message.toString();
    }
}
