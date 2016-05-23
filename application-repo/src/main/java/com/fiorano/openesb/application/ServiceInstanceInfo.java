/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class ServiceInstanceInfo extends DmiObject
{

    String          m_servInstName;
    String          m_appInstName;
    String          m_appInstVersion;
    String          m_servStatus;

    /**
     *  Gets the servInstName attribute of the ServiceInstanceInfo object
     *
     * @return The servInstName value
     */
    public String getServInstName()
    {
        return m_servInstName;
    }

    /**
     *  Gets the servStatus attribute of the ServiceInstanceInfo object
     *
     * @return The servStatus value
     */
    public String getServStatus()
    {
        return m_servStatus;
    }

    /**
     *  Gets the appInstName attribute of the ServiceInstanceInfo object
     *
     * @return The appInstName value
     */
    public String getAppInstName()
    {
        return m_appInstName;
    }

   /**
    *  Gets the appInstVersion attribute of the ServiceInstanceInfo object
    *
            * @return The appInstVersion value
    */
    public String getAppInstVersion()
    {
        return m_appInstVersion;
    }


    /**
     *  Gets the objectID attribute of the ServiceInstanceInfo object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_INSTANCE_INFO;
    }

    /**
     *  Sets the servInstName attribute of the ServiceInstanceInfo object
     *
     * @param servInstName The new servInstName value
     */
    public void setServInstName(String servInstName)
    {
        m_servInstName = servInstName;
    }


    /**
     *  Sets the servStatus attribute of the ServiceInstanceInfo object
     *
     * @param servStatus The new servStatus value
     */
    public void setServStatus(String servStatus)
    {
        m_servStatus = servStatus;
    }

    /**
     *  Sets the appInstName attribute of the ServiceInstanceInfo object
     *
     * @param appInstName The new appInstName value
     */
    public void setAppInstName(String appInstName)
    {
        m_appInstName = appInstName;
    }

    /**
     *  Sets the appInstVersion attribute of the ServiceInstanceInfo object
     *
     * @param appInstVersion The new appInstVersion value
     */
    public void setAppInstVersion(String appInstVersion)
    {
        m_appInstVersion = appInstVersion;
    }


    /**
     *  Resets the fields of the object to the default values
     */
    public void reset()
    {
    }


    /**
     *  The method is used to read the data from a DataInput object. The fields
     *  of the object are assigned values from the inout stream.
     *
     * @param is The input stream from which the data
     *  is to be read
     * @param versionNo
     * @exception java.io.IOException If there is some error reading data from
     * the stream
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        m_servInstName = readUTF(is);
        m_appInstName = readUTF(is);
        m_appInstVersion = readUTF(is);
        m_servStatus = readUTF(is);
    }


    /**
     *  The method writes out the values of the fields of the object to
     *  the output stream.
     *
     * @param out The output stream to which the data is to be written
     * @param versionNo
     * @exception java.io.IOException If there is some error in writing the data
     * onto the output stream.
     */
    public void toStream(java.io.DataOutput out, int versionNo)
        throws java.io.IOException
    {
        writeUTF(out, m_servInstName);
        writeUTF(out, m_appInstName);
        writeUTF(out, m_appInstVersion);
        writeUTF(out, m_servStatus);

    }


    /**
     *  Validates the fields of the object for correctness.
     *
     * @exception FioranoException Throws exception if the object is not valid
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  Returns the String representation of this event.
     *
     * @return Description of the Return Value
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Service Instance info Details ");
        strBuf.append("[");
        strBuf.append("Application instance name = ");
        strBuf.append(m_appInstName);
        strBuf.append(", ");
        strBuf.append("Application instance Version = ");
        strBuf.append(m_appInstVersion);
        strBuf.append(", ");
        strBuf.append("Service instance name = ");
        strBuf.append(m_servInstName);
        strBuf.append(", ");
        strBuf.append("Service status = ");
        strBuf.append(m_servStatus);
        strBuf.append("]");
        return strBuf.toString();
    }
}
