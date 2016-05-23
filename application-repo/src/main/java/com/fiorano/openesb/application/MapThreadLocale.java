/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import java.util.HashMap;

public class MapThreadLocale extends ThreadLocal{
    private static MapThreadLocale SINGLETON = new MapThreadLocale();

    public static MapThreadLocale getInstance(){
        return SINGLETON;
    }

    private MapThreadLocale(){}

    protected Object initialValue(){
        return new HashMap();
    }

    public HashMap getMap(){
        return (HashMap)get();
    }
}
