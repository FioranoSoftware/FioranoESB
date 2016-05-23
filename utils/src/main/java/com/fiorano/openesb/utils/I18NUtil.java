/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.util.MissingResourceException;

public final class I18NUtil {
    // searches in super classes and the interfaces
    public static String getMessage(Class clazz, String key, Object[] params){
        Class givenClass = clazz;
        while(true){
            try{
                return params==null
                        ? RBUtil.getMessage(clazz, key)
                        : RBUtil.getMessage(clazz, key, params);
            } catch(MissingResourceException ex){
                clazz = clazz.getSuperclass();
                if(clazz==null || clazz==Object.class){
                    // now try interfaces
                    Class interfaces[] = givenClass.getInterfaces();
                    for(int i = 0; i<interfaces.length; ++i){
                        while(true){
                            try{
                                return params==null
                                        ? RBUtil.getMessage(interfaces[i], key)
                                        : RBUtil.getMessage(interfaces[i], key, params);
                            } catch(MissingResourceException ex1){
                                interfaces[i] = interfaces[i].getSuperclass();
                                if(interfaces[i]==null)
                                    break;
                            }
                        }
                    }
                    throw ex;
                }
            }
        }
    }

    // searches only in super classes
    public static String getQualifiedMessage(Class clazz, String name){
        while(true){
            try{
                String clazzName = ClassUtil.getShortClassName(clazz);
                return RBUtil.getMessage(clazz, clazzName+"."+name);
            } catch(MissingResourceException ex){
                clazz = clazz.getSuperclass();
                if(clazz==null || clazz==Object.class)
                    throw ex;
                continue;
            }
        }
    }

    // searches only in super classes
    public static String getFullyQualifiedMessage(Class clazz, String name){
        while(true){
            try{
                return RBUtil.getMessage(clazz, clazz.getName()+"."+name);
            } catch(MissingResourceException ex){
                clazz = clazz.getSuperclass();
                if(clazz==null || clazz==Object.class)
                    throw ex;
                continue;
            }
        }
    }

    /*-------------------------------------------------[ Helpers ]---------------------------------------------------*/

    public static String getMessage(Class clazz, String key){
        return getMessage(clazz, key, (Object[])null);
    }

    public static String getMessage(Class clazz, String key, Object param){
        return getMessage(clazz, key, new Object[]{param});
    }

    public static String getMessage(Class clazz, String key, Object param1, Object param2){
        return getMessage(clazz, key, new Object[]{param1, param2});
    }

    public static String getMessage(Class clazz, String key, Object param1, Object param2, Object param3){
        return getMessage(clazz, key, new Object[]{param1, param2, param3});
    }

    public static String getMessage(Class clazz, String key, Object param1, Object param2, Object param3, Object param4){
        return getMessage(clazz, key, new Object[]{param1, param2, param3, param4});
    }

    public static String getMessage(Class clazz, String key, Object param1, Object param2, Object param3, Object param4, Object param5){
        return getMessage(clazz, key, new Object[]{param1, param2, param3, param4, param5});
    }

    public static String getMessage(Class clazz, String key, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6){
        return getMessage(clazz, key, new Object[]{param1, param2, param3, param4, param5, param6});
    }
}