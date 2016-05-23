/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class UTFReaderWriter
{
    public static void writeUTF(DataOutput dos, String toWrite)
            throws IOException
    {
        byte[] byteArr = null;
        if (toWrite != null)
        {
            byteArr = toWrite.getBytes("UTF-8");
            dos.writeInt(byteArr.length);
            dos.write(byteArr);
        }
        else
            dos.writeInt(-1);

    }

    public static String readUTF(DataInput dis)
            throws IOException
    {
        byte[] byteArr = null;
        int len = dis.readInt();
        if (len == -1)
            return null;
        byteArr = new byte[len];
        dis.readFully(byteArr);
        return new String(byteArr, "UTF-8");
    }

}
