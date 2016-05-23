/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.lang.reflect.Method;

public class ClassUtil{

    public static String whichJAR(Class clazz){
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf('.')+1);
        String jar = clazz.getResource(name+".class").toString(); //NOI18N
        return jar.substring(0, jar.indexOf('!'));
    }

    public static String getShortClassName(Class clazz){
        // if it is an array, get short name of element type and append []
        if(clazz.isArray())
            return getShortClassName(clazz.getComponentType())+"[]"; // NOI18N

        String name = clazz.getName().replace('$', '.');
        return name.substring(name.lastIndexOf(".")+1, name.length()); // NOI18N
    }


    public static Class getPropertyType(Class beanClass, String property){
        try{
            return beanClass.getMethod(StringUtil.firstLetterToUpperCase(property, "get"), new Class[0]).getReturnType(); //NOI18N
        } catch(NoSuchMethodException ex){
            try{
                return beanClass.getMethod(StringUtil.firstLetterToUpperCase(property, "is"), new Class[0]).getReturnType(); //NOI18N
            } catch(NoSuchMethodException e){
                return null;
            }
        }
    }

    public static Method getGetterMethod(Class beanClass, String property){
        try{
            return beanClass.getMethod(StringUtil.firstLetterToUpperCase(property, "get"), new Class[0]); //NOI18N
        } catch(NoSuchMethodException ex){
            try{
                return beanClass.getMethod(StringUtil.firstLetterToUpperCase(property, "is"), new Class[0]); //NOI18N
            } catch(NoSuchMethodException e){
                return null;
            }
        }
    }

    public static Method getGetterMethod(Class beanClass, String property, Class propertyType){
        String getName = propertyType==boolean.class || propertyType==Boolean.class
                         ? StringUtil.firstLetterToUpperCase(property, "is")          //NOI18N
                         : StringUtil.firstLetterToUpperCase(property, "get");        //NOI18N

        try{
            return beanClass.getMethod(getName, new Class[0]);
        } catch(NoSuchMethodException ex){
            return null;
        }
    }

    public static Method getSetterMethod(Class beanClass, String property){
        Method getter = getGetterMethod(beanClass, property);
        if(getter==null)
            return null;
        else
            return getSetterMethod(beanClass, property, getter.getReturnType());
    }

    public static Method getSetterMethod(Class beanClass, String property, Class propertyType){
        String setName = StringUtil.firstLetterToUpperCase(property, "set");          //NOI18N
        try{
            return beanClass.getMethod(setName, new Class[]{propertyType});
        } catch(NoSuchMethodException ex){
            return null;
        }
    }

    /*-------------------------------------------------[ ClassLoader Related ]---------------------------------------------------*/

    /**
     * table mapping primitive type names to corresponding class objects
     */
    public static final Map PRIMITIVE_CLASSES/*<className, primitiveClass>*/;
    public static final Map PRIMITIVE_WRAPPER_CLASSES/*<primitiveClass, primitiveWrapperClass>*/;
    static {
        HashMap map = new HashMap();
        map.put("boolean", boolean.class); //NOI18N
        map.put("byte", byte.class);       //NOI18N
        map.put("char", char.class);       //NOI18N
        map.put("short", short.class);     //NOI18N
        map.put("int", int.class);         //NOI18N
        map.put("long", long.class);       //NOI18N
        map.put("float", float.class);     //NOI18N
        map.put("double", double.class);   //NOI18N
        map.put("void", void.class);       //NOI18N
        PRIMITIVE_CLASSES = Collections.unmodifiableMap(map);

        map = new HashMap();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(short.class, Short.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
        map.put(void.class, Void.class);
        PRIMITIVE_WRAPPER_CLASSES = Collections.unmodifiableMap(map);
    }

    public static ClassLoader getClassLoader(Class klass){
        if(klass.getClassLoader()!=null)
            return klass.getClassLoader();
        else
            return ClassLoader.getSystemClassLoader();
    }

    public static Class loadClass(String className, ClassLoader fallback)
            throws ClassNotFoundException{
        ClassLoader systemClassLoader = (ClassLoader)java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction(){
                    public Object run(){
                        ClassLoader cl = Thread.currentThread().
                                getContextClassLoader();
                        return (cl!=null)
                                ? cl
                                : ClassLoader.getSystemClassLoader();
                    }
                }
        );

        try{
            return Class.forName(className, true, systemClassLoader);
        } catch(ClassNotFoundException e2){
            try{
                if(systemClassLoader!=ClassLoader.getSystemClassLoader())
                    return Class.forName(className, true, ClassLoader.getSystemClassLoader());
                else
                    throw e2;
            } catch(ClassNotFoundException e){
                if(fallback!=null){
                    try{
                        return Class.forName(className, true, fallback);
                    } catch(ClassNotFoundException e3){
                        Class clazz = (Class)PRIMITIVE_CLASSES.get(className);
                        if(clazz!=null)
                            return clazz;
                        throw e3;
                    }
                }else{
                    Class clazz = (Class)PRIMITIVE_CLASSES.get(className);
                    if(clazz!=null)
                        return clazz;
                    throw e2;
                }
            }
        }
    }
}
