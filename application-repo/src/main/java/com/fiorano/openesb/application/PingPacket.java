/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;


public class PingPacket extends DmiObject
{
    private String  m_pingSource;


    /**
     *  Constructor for the PingPacket object
     */
    public PingPacket()
    {
        m_pingSource = null;
    }

    /**
     *  Constructor for the PingPacket object
     *
     * @param pingSource Node from which ping packet is generated.
     */
    public PingPacket(String pingSource)
    {
        m_pingSource = pingSource;
    }

    /**
     *  Gets the objectID attribute of the PingPacket object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.PING_PACKET;
    }

    /**
     *  Gets the pingSource attribute of the PingPacket object
     *
     * @return The pingSource value
     */
    public String getPingSource()
    {
        return m_pingSource;
    }

    /**
     *  Gets the bytes attribute of the PingPacket object
     *
     * @param versionNo
     * @return The bytes value
     */
    public byte[] getBytes(int versionNo)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try
        {
            toStream(dos, versionNo);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return bos.toByteArray();
    }

    /**
     *  Sets the pingSource attribute of the PingPacket object
     *
     * @param nodeName The new pingSource value
     */
    public void setPingSource(String nodeName)
    {
        m_pingSource = nodeName;
    }

    /**
     *  Check for the validity of the Ping packet.
     *
     * @exception FioranoException thrown if Ping packet found invalid.
     */
    public void validate()
        throws FioranoException
    {
        if (m_pingSource == null)
            throw new FioranoException("ping source is null");
    }

    /**
     *  resets the ping packet to default values.
     */
    public void reset()
    {
        m_pingSource = null;
    }

    /**
     *  Writes PingPacket to DataOutputStream.
     *
     * @param dos DataOutput Stream
     * @param versionNo
     * @exception IOException thrown in case of error
     */
    public void toStream(DataOutput dos, int versionNo)
        throws IOException
    {
        writeUTF(dos, m_pingSource);
    }

    /**
     *  Reads PingPacket from DataInput Stream.
     *
     * @param dis DataInput Stream.
     * @param versionNo
     * @exception IOException thrown if error occurs
     */
    public void fromStream(DataInput dis, int versionNo)
        throws IOException
    {
        m_pingSource = readUTF(dis);
    }

}
