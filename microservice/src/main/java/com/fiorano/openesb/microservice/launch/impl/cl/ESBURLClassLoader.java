/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl.cl;

import java.net.URL;
import java.net.URLClassLoader;

public class ESBURLClassLoader extends URLClassLoader
{
    /**
     * @param urls
     * @param parent
     */
    public ESBURLClassLoader(URL[] urls, ClassLoader parent)
    {
        super(urls, parent);
    }

    /**
     * Returns name for object
     *
     * @return
     */
    public String getName()
    {
        return toString();
    }
}
