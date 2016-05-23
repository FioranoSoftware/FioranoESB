/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public class RandomUtil {
    public static int random(int min, int max){
        return (int) Math.ceil(min + Math.random() * ((long) max - (long) min));
    }

    public static long random(long min, long max){
        return (long) Math.ceil(min + Math.random() * (max - min));
    }

    public static short random(short min, short max){
        return (short) Math.ceil(min + Math.random() * ((long) max - (long) min));
    }

    public static byte random(byte min, byte max){
        return (byte) Math.ceil(min + Math.random() * ((long) max - (long) min));
    }

    public static boolean randomBoolean(){
        return Math.random()<0.5d;
    }

    public static boolean randomBoolean(Boolean bool){
        if(Boolean.TRUE.equals(bool))
            return true;
        else if(Boolean.FALSE.equals(bool))
            return false;
        else // random either 0 or 1
            return randomBoolean();
    }
}
