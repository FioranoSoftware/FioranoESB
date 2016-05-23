/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.lang.reflect.*;
import java.util.*;

public final class ArrayUtil{
    public static Object[] union(Object obj1[], Object obj2[], Class arrayClass){
        Object obj[] = (Object[])Array.newInstance(arrayClass, obj1.length+obj2.length);
        System.arraycopy(obj1, 0, obj, 0, obj1.length);
        System.arraycopy(obj2, 0, obj, obj1.length, obj2.length);
        return obj;
    }

    public static Object[] union(Object obj1[], Object obj2[]){
        Class clazz = ReflectionUtil.findSharedSuperClass(
                obj1.getClass().getComponentType(),
                obj2.getClass().getComponentType());
        return union(obj1, obj2, clazz);
    }

    public static Collection toCollection(Object obj[], Collection c){
        if(c==null)
            c = new ArrayList(obj.length);
        for(int i=0; i<obj.length; ++i)
            c.add(obj[i]);
        return c;
    }

    public static Object[] toArray(Object obj[], Class arrayClass){
        Object arr[] = (Object[])Array.newInstance(arrayClass, obj.length);
        System.arraycopy(obj, 0, arr, 0, obj.length);
        return arr;
    }

    public static Object[] subArray(Object obj[], int from, int to, Class arrayClass){
        if(arrayClass==null)
            arrayClass = obj.getClass().getComponentType();
        Object arr[] = (Object[])Array.newInstance(arrayClass, to-from);
        System.arraycopy(obj, from, arr, 0, arr.length);
        return arr;
    }

    public static Object[] subArray(Object obj[], int from, int to){
        return subArray(obj, from, to, null);
    }

    public static void swap(Object obj[], int i, int j) {
        Object temp = obj[i];
        obj[i] = obj[j];
        obj[j] = temp;
    }

    public static void swap(Object obj[], int i, int j, int len) {
        for(int count=0; count<len; count++, i++, j++)
            swap(obj, i, j);
    }

    public static void reverse(Object obj[], int i, int j){
        while(i<j)
            swap(obj, i++, j--);
    }

    public static void reverse(Object obj[]){
        reverse(obj, 0, obj.length-1);
    }

    /*-------------------------------------------------[ Methods copied from org.openide.util.Utilites ]---------------------------------------------------*/
    /**
     * Convert an array of objects to an array of primitive types.
     * E.g. an <code>Integer[]</code> would be changed to an <code>int[]</code>.
     * @param array the wrapper array
     * @return a primitive array
     * @throws IllegalArgumentException if the array element type is not a primitive wrapper
     */
    public static Object toPrimitiveArray (Object[] array) {
        if (array instanceof Integer[]) {
            int[] r = new int [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Integer)array[i]) == null) ? 0 : ((Integer)array[i]).intValue ();
            return r;
        }
        if (array instanceof Boolean[]) {
            boolean[] r = new boolean [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Boolean)array[i]) == null) ? false : ((Boolean)array[i]).booleanValue ();
            return r;
        }
        if (array instanceof Byte[]) {
            byte[] r = new byte [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Byte)array[i]) == null) ? 0 : ((Byte)array[i]).byteValue ();
            return r;
        }
        if (array instanceof Character[]) {
            char[] r = new char [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Character)array[i]) == null) ? 0 : ((Character)array[i]).charValue ();
            return r;
        }
        if (array instanceof Double[]) {
            double[] r = new double [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Double)array[i]) == null) ? 0 : ((Double)array[i]).doubleValue ();
            return r;
        }
        if (array instanceof Float[]) {
            float[] r = new float [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Float)array[i]) == null) ? 0 : ((Float)array[i]).floatValue ();
            return r;
        }
        if (array instanceof Long[]) {
            long[] r = new long [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Long)array[i]) == null) ? 0 : ((Long)array[i]).longValue ();
            return r;
        }
        if (array instanceof Short[]) {
            short[] r = new short [array.length];
            int i, k = array.length;
            for (i = 0; i < k; i++) r [i] = (((Short)array[i]) == null) ? 0 : ((Short)array[i]).shortValue ();
            return r;
        }
        throw new IllegalArgumentException ();
    }

    /**
     * Convert an array of primitive types to an array of objects.
     * E.g. an <code>int[]</code> would be turned into an <code>Integer[]</code>.
     * @param array the primitive array
     * @return a wrapper array
     * @throws IllegalArgumentException if the array element type is not primitive
     */
    public static Object[] toObjectArray (Object array) {
        if (array instanceof Object[]) return (Object[]) array;
        if (array instanceof int[]) {
            int i, k = ((int[])array).length;
            Integer[] r = new Integer [k];
            for (i = 0; i < k; i++) r [i] = new Integer (((int[]) array)[i]);
            return r;
        }
        if (array instanceof boolean[]) {
            int i, k = ((boolean[])array).length;
            Boolean[] r = new Boolean [k];
            for (i = 0; i < k; i++) r [i] = ((boolean[]) array)[i] ? Boolean.TRUE : Boolean.FALSE;
            return r;
        }
        if (array instanceof byte[]) {
            int i, k = ((byte[])array).length;
            Byte[] r = new Byte [k];
            for (i = 0; i < k; i++) r [i] = new Byte (((byte[]) array)[i]);
            return r;
        }
        if (array instanceof char[]) {
            int i, k = ((char[])array).length;
            Character[] r = new Character [k];
            for (i = 0; i < k; i++) r [i] = new Character (((char[]) array)[i]);
            return r;
        }
        if (array instanceof double[]) {
            int i, k = ((double[])array).length;
            Double[] r = new Double [k];
            for (i = 0; i < k; i++) r [i] = new Double (((double[]) array)[i]);
            return r;
        }
        if (array instanceof float[]) {
            int i, k = ((float[])array).length;
            Float[] r = new Float [k];
            for (i = 0; i < k; i++) r [i] = new Float (((float[]) array)[i]);
            return r;
        }
        if (array instanceof long[]) {
            int i, k = ((long[])array).length;
            Long[] r = new Long [k];
            for (i = 0; i < k; i++) r [i] = new Long (((long[]) array)[i]);
            return r;
        }
        if (array instanceof short[]) {
            int i, k = ((short[])array).length;
            Short[] r = new Short [k];
            for (i = 0; i < k; i++) r [i] = new Short (((short[]) array)[i]);
            return r;
        }
        throw new IllegalArgumentException ();
    }

    /*-------------------------------------------------[ toString ]---------------------------------------------------*/

    public static String toString(Object array[]){
        StringBuffer buf = new StringBuffer();
        buf.append(array.getClass().getComponentType()).append('[');
        for(int i=0; i<array.length; i++){
            if(i!=0)
                buf.append(", ");
            buf.append(array[i]==array ? "(this Array)" : String.valueOf(array[i]));
        }
        buf.append("]");
        return buf.toString();
    }

    public static String toString(Object array){
        StringBuffer buf = new StringBuffer();
        buf.append(array.getClass().getComponentType()).append('[');
        int length = Array.getLength(array);
        for(int i=0; i<length; i++){
            if(i!=0)
                buf.append(", ");
            Object obj = Array.get(array, i);
            buf.append(obj==array ? "(this Array)" : String.valueOf(obj));
        }
        buf.append("]");
        return buf.toString();
    }

    /*-------------------------------------------------[ Searching ]---------------------------------------------------*/

    public static int indexOf(Object array[], Object item){
        return indexOf(array, item, 0);
    }

    public static int indexOf(Object array[], Object item, int fromIndex){
        if(item==null){
            for(int i = fromIndex; i<array.length; i++){
                if(array[i]==null)
                    return i;
            }
        }else{
            for(int i = fromIndex; i<array.length; i++){
                if(item.equals(array[i]))
                    return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object array[], Object item){
        return lastIndexOf(array, item, array.length-1);
    }

    public static int lastIndexOf(Object array[], Object item, int fromIndex){
        if(item==null){
            for(int i = fromIndex; i>=0; i--){
                if(array[i]==null)
                    return i;
            }
        }else{
            for(int i = fromIndex; i>=0; i--){
                if(item.equals(array[i]))
                    return i;
            }
        }
        return -1;
    }
}