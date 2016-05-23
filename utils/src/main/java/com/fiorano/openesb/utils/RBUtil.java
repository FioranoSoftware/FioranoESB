/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class RBUtil {
    private static String getPackage(Class clazz){
        String name = clazz.getName();
        int index = name.lastIndexOf('.');
        return index==-1 ? name : name.substring(0, index);
    }

    private static synchronized ResourceBundle getBundle(Class clazz){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) {
            classLoader = ClassUtil.getClassLoader(clazz);
        }
        return ResourceBundle.getBundle(getPackage(clazz)+".Bundle", Locale.getDefault(), classLoader); //NOI18N
    }

    public static String getLocalizedMessage(Class clazz, String key){
        try {
            return getBundle(clazz).getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public static String getLocalizedMessage(Class clazz, String key, Object[] params){
        try {                                                                                   
            return MessageFormat.format(getBundle(clazz).getString(key), params);
        } catch (Exception e) {
            return key;
        }
    }

    // searches in super classes and the interfaces
    public static String getMessage(Class clazz, String key, Object... params){
        Class givenClass = clazz;
        while(true){
            try{
                return params==null
                        ? getLocalizedMessage(clazz, key)
                        : getLocalizedMessage(clazz, key, params);
            } catch(MissingResourceException ex){
                clazz = clazz.getSuperclass();
                if(clazz==null || clazz==Object.class){
                    // now try interfaces
                    Class[] interfaces = givenClass.getInterfaces();
                    for(int i = 0; i<interfaces.length; ++i){
                        while(true){
                            try{
                                return params==null
                                        ? getLocalizedMessage(interfaces[i], key)
                                        : getLocalizedMessage(interfaces[i], key, params);
                            } catch(MissingResourceException ex1){
                                interfaces[i] = interfaces[i].getSuperclass();
                                if(interfaces[i]==null)
                                    break;
                            } catch(Exception e){
                                return key;
                            }
                        }
                    }
                    String errMessage = key;
                    if(params != null) {
                        errMessage += "{";
                        for (int i = 0; i < params.length; i++) {
                            errMessage += params[i];
                            if(i != params.length - 1) {
                                errMessage += ",";
                            }
                        }
                        errMessage += "}";
                    }
                    return errMessage;
                }
            } catch (Exception e){
                return key;
            }
        }
    }

    // searches in super classes and the interfaces
    public static String getMessage(Class clazz, String key){
        Class givenClass = clazz;
        while(true){
            try{
                return getLocalizedMessage(clazz, key);
            } catch(MissingResourceException ex){
                clazz = clazz.getSuperclass();
                if(clazz==null || clazz==Object.class){
                    // now try interfaces
                    Class[] interfaces = givenClass.getInterfaces();
                    for(int i = 0; i<interfaces.length; ++i){
                        while(true){
                            try{
                                return getLocalizedMessage(interfaces[i], key);
                            } catch(MissingResourceException ex1){
                                interfaces[i] = interfaces[i].getSuperclass();
                                if(interfaces[i]==null)
                                    break;
                            } catch (Exception e){
                                return key;
                            }
                        }
                    }
                    return key;
                }
            } catch (Exception e){
                return key;
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(getMessage(RBUtil.class, "sdfds", new String[]{"dsf","fdgdfg","dfss"}));
    }


}
