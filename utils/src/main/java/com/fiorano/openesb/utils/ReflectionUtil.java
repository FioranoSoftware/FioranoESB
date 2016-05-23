/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.ArrayUtil;

import java.util.ArrayList;

public class ReflectionUtil{
    public static int getLevel(Class clazz){
        int level = 0;
        while(clazz!=null){
            clazz = clazz.getSuperclass();
            ++level;
        }
        return level;
    }

    public static final Class findSharedSuperClass(Class class1, Class class2){
        if(class1==class2)
            return class1;
        else if(class1==null || class2==null)
            return null;

        int level1 = getLevel(class1);
        int level2 = getLevel(class2);
        int diff = 0;

        if (level2 > level1) {
            diff = level2 - level1;
            Class temp = class1;
            class1 = class2;
            class2 = temp;
        } else
            diff = level1 - level2;

        // Go up the tree until the nodes are at the same level
        while (diff > 0) {
            class1 = class1.getSuperclass();
            --diff;
        }

        // Move up the tree until we find a common ancestor.  Since we know
        // that both nodes are at the same level, we won't cross paths
        // unknowingly (if there is a common ancestor, both nodes hit it in
        // the same iteration).
        do {
            if(class1==class2)
                return class1;
            class1 = class1.getSuperclass();
            class2 = class2.getSuperclass();
        } while(class1 != null);// only need to check one -- they're at the
        // same level so if one is null, the other is

        return null;
    }

    public static Class[] getAllInterfaces(Class clazz){
        ArrayList list = new ArrayList();
        while(clazz!=null){
            ArrayUtil.toCollection(clazz.getInterfaces(), list);
            clazz = clazz.getSuperclass();
        }
        return (Class[])list.toArray(new Class[0]);
    }
}
