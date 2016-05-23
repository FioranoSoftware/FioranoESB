/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class ConfigReader {
    public static void readConfigFromPropertiesFile(File configFile, Object configObject) throws IOException, InvocationTargetException, IllegalAccessException {
       if(!configFile.exists()){
           return;
       }
        Properties properties = new Properties();
        readPropertiesFromFile(configFile, properties);
            Method[] methods = configObject.getClass().getMethods();
            for (Method m : methods) {
                if (m.getName().startsWith("set") && !m.getName().startsWith("setClass")) {
                    String key = m.getName().substring(m.getName().indexOf("set")+3);
                    if(properties.get(key)!=null){
                        m.invoke(configObject, properties.getProperty(key));
                    }
                }
            }
    }

    public static void readPropertiesFromFile(File configFile, Properties properties) throws IOException, InvocationTargetException, IllegalAccessException {
        if(!configFile.exists()){
            return;
        }
        try (FileInputStream inStream = new FileInputStream(configFile)){
            properties.load(inStream);
        }
    }
}
