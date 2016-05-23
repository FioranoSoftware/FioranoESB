/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl.cl;

import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.logging.api.IFioranoLogger;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.Vector;

public class JoinClassLoader extends ClassLoader
{
    // Has the list of classloaders for this Join class loaders to maintain or merge.
    private final ClassLoader[] m_classLoaderList;

    // The reason for this hashtable is a simple optimisation.
    // Assumtion - If i know a class with package p1 has been loaded by the classloader c among the list
    // it maintains. then if i come accorss a class with the same package p1 then its more propable that
    // i find it in the same classloader.
    // So i store a hashtable of package name and classloaders.
    private Hashtable m_cacheTable;

    private IFioranoLogger classLoaderLogger;

    // Constructor - Checks for nullity in the list and instantiates the cache table
    JoinClassLoader(ClassLoader[] classLoaderList, IFioranoLogger classLoaderLogger)
        throws FioranoException
    {
        this.classLoaderLogger=classLoaderLogger;
        for (int i = 0; i < classLoaderList.length; i++)
        {
            if (null == classLoaderList[i])
            {
                classLoaderLogger.error(LogHelper.getErrMessage("CLM",1,"classLoaders[" + i + "]"));
                throw new FioranoException(LogHelper.getErrMessage("CLM",1,"classLoaders[" + i + "]"));
            }
        }
        this.m_classLoaderList = classLoaderList;
        m_cacheTable = new Hashtable();
    }

    // Overloaded constrcutr with vectors rather than array of classloaders
    JoinClassLoader(Vector classLoaderList, IFioranoLogger classLoaderLogger)
        throws FioranoException
    {
        this.classLoaderLogger=classLoaderLogger;
        ClassLoader[] list = new ClassLoader[classLoaderList.size()];
        Iterator iterator = classLoaderList.iterator();
        int count = 0;

        while (iterator.hasNext())
        {
            if((list[count] = (ClassLoader) iterator.next()) == null)
            {
                classLoaderLogger.error(LogHelper.getErrMessage("CLM",1,"classLoaders[" + count + "]"));
                throw new FioranoException(LogHelper.getErrMessage("CLM",1,"classLoaders[" + count + "]"));
            }
            count++;
        }
        this.m_classLoaderList = list;
        m_cacheTable = new Hashtable();
    }

    /**
     * Overloaded the Objects to String method.
     *
     * @return String
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < m_classLoaderList.length; i++)
            buffer.append(m_classLoaderList[i].toString());
        return buffer.toString();
    }

    // find class is responsible for locating the class amongst the list of classloaders it maintains.
    // loadClass method of classLoader calls findClass to locate the class.

    protected Class findClass(final String className)
        throws ClassNotFoundException
    {
        //System.out.println("Length -> " + classLoaderList.length);
        // Optiimitation - If the package of the class, is present try loading the class
        // with the same classloader.
        String packageName = getPackageName(className);
        if (m_cacheTable.containsKey(packageName))
        {
            //this wont work
//            Package pkg = getPackage(packageName);
//            if(pkg == null)
//                throw new ClassNotFoundException(className);
            Integer cachedInt = (Integer) m_cacheTable.get(packageName);

            classLoaderLogger.trace(LogHelper.getOutMessage("CLM", 10, className, cachedInt.toString()));
            try
            {
                return m_classLoaderList[cachedInt.intValue()].loadClass(className);
            }
            catch (ClassNotFoundException e)
            {
                 classLoaderLogger.trace(LogHelper.getErrMessage("CLM", 2, e.toString()));
                // Optimization assumption failed. Go thru the process of linear searching.
            }
        }

        // Linearly iterate through the classloaders list and try loading the class with all the classloaders
        // possible.
        for (int i = 0; i < m_classLoaderList.length; i++)
        {
            try
            {
                classLoaderLogger.trace(LogHelper.getOutMessage("CLM", 11, className, String.valueOf(i)));

                Class loadClass = m_classLoaderList[i].loadClass(className);

                updateCache(loadClass.getPackage().getName(), i);
                return loadClass;
            }
            catch (final ClassNotFoundException cnfe)
            {
                //Not in that classloader. check for other class loaders.
            }
        }
        return super.findClass(className);
    }

    // Get the package name for the specified class.
    private String getPackageName(String className)
    {
//        return className.substring(className.lastIndexOf(".") + 1); //this will give the classname!!
        int idx = className.lastIndexOf(".");
        if(idx == -1)
            return "";
        else
            return className.substring(0,idx);
    }

    // update the cache.
    private void updateCache(String name, int i)
    {
        m_cacheTable.put(name, new Integer(i));
    }
}
