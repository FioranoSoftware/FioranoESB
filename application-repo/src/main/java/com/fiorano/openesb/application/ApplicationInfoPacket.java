/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class ApplicationInfoPacket extends DmiObject
{
    private String  m_strAppGUID;

    private String  m_strAppName;

    /**
     *  Gets the objectID attribute of the AckPacket object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APLLICATION_INFO_PACKET;
    }

    /**
     *  Gets the applicationGUID attribute of the ApplicationInfoPacket object
     *
     * @return The applicationGUID value
     */
    public String getApplicationGUID()
    {
        return m_strAppGUID;
    }

    /**
     *  Gets the applicationName attribute of the ApplicationInfoPacket object
     *
     * @return The applicationName value
     */
    public String getApplicationName()
    {
        return m_strAppName;
    }

    /**
     *  Sets the applicationGUID attribute of the ApplicationInfoPacket object
     *
     * @param appGUID The new applicationGUID value
     */
    public void setApplicationGUID(String appGUID)
    {
        m_strAppGUID = appGUID;
    }

    /**
     *  Sets the applicationName attribute of the ApplicationInfoPacket object
     *
     * @param appName The new applicationName value
     */
    public void setApplicationName(String appName)
    {
        m_strAppName = appName;
    }

    /**
     *  Checks Validity of this ApplicationInfo Object.
     *
     * @exception FioranoException thrown in case of validation failure
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  resets ApplicationInfo object to default values.
     */
    public void reset()
    {
    }

    /**
     *  writes this Application object to DataOutput stream.
     *
     * @param os Data output stream.
     * @param versionNo
     * @exception java.io.IOException thrown in case of error writing data
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);

        writeUTF(os, m_strAppGUID);
        writeUTF(os, m_strAppName);
    }


    /**
     *  reads ApplicationInfo object from DataInputStream
     *
     * @param is DataInput Stream
     * @param versionNo
     * @exception java.io.IOException thrown in case of error while reading ALP
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);

        m_strAppGUID = readUTF(is);
        m_strAppName = readUTF(is);
    }
}
