/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public class DmiEqualsUtil
{
    /**
     *  Tests if two specified strings are equal or not.
     *
     * @param str1 The first string.
     * @param str2 The second string which is to be compared.
     * @return True if strings are equal, false otherwise.
     */
    public static boolean checkStringEquality(String str1, String str2)
    {
        if (str1 == null && str2 == null)
            return true;

        if ((str1 != null && str2 == null) || (str1 == null && str2 != null))
            return false;

        if (str1.equalsIgnoreCase(str2))
            return true;
        else
            return false;
    }

    /**
     * @param bool1
     * @param bool2
     * @return
     */
    public static boolean checkBooleanEquality(boolean bool1, boolean bool2)
    {
        if (bool1 && bool2)
            return true;

        if (!bool1 & !bool2)
            return true;

        return false;
    }

    /**
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean checkObjectEquality(Object obj1, Object obj2)
    {
        if (obj1 == null && obj2 == null)
            return true;

        if ((obj1 != null && obj2 == null) || (obj1 == null && obj2 != null))
            return false;

        if (obj1.equals(obj2))
            return true;

        return false;
    }
}
