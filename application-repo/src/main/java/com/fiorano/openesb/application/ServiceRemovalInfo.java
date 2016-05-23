/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ServiceRemovalInfo extends DmiObject
{

    String          m_strServGUID;
    String          m_strServVersion;
    String          m_strRemovalStatus;

    /**
     *  This method returns the <code>serviceGUID</code> of the service from
     *  this <code>ServiceRemovalInfo</code> object.
     *
     * @return serviceGUID for this object
     * @see #setServiceGUID(String)
     * @since Tifosi2.0
     */
    public String GetServiceGUID()
    {
        return m_strServGUID;
    }

    /**
     *   This method returns the version of service from this
     *   <code>ServiceRemovalInfo</code> object.
     *
     * @return Service version for this object
     * @see #setServiceVersion(String)
     * @since Tifosi2.0
     */
    public String getServiceVersion()
    {
        return m_strServVersion;
    }

    /**
     *  This method returns the removal status from this
     *  <code>ServiceRemovalInfo</code> object.
     *
     * @return removal status for this object
     * @see #setServiceRemovalStatus(String)
     * @since Tifosi2.0
     */
    public String getServiceRemovalStatus()
    {
        return m_strRemovalStatus;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_REMOVAL_INFO;
    }

    /**
     *  This method sets the specified string as <code>serviceGUID</code> of the service for
     *  this <code>ServiceRemovalInfo</code> object.
     *
     * @param servGUID The string to be set as serviceGUID.
     * @see #GetServiceGUID()
     * @since Tifosi2.0
     */
    public void setServiceGUID(String servGUID)
    {
        m_strServGUID = servGUID;
    }

    /**
     *  This method sets the version of the service for this
     *  <code>ServiceRemovalInfo</code> object.
     *
     * @param servVersion The string to be set as service version for this object
     * @see #getServiceVersion()
     * @since Tifosi2.0
     */
    public void setServiceVersion(String servVersion)
    {
        m_strServVersion = servVersion;
    }


    /**
     *  This method sets the specified string as removal status for this
     *  <code>ServiceRemovalInfo</code> object.
     *
     * @param removalStatus The string  to be set as removal status for this object
     * @see #getServiceRemovalStatus()
     * @since Tifosi2.0
     */
    public void setServiceRemovalStatus(String removalStatus)
    {
        m_strRemovalStatus = removalStatus;
    }


    /**
     *  This method has not been implemented in this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method is called to read values for this <code>ServiceRemovalInfo</code> object
     *  from the specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        m_strServGUID = readUTF(is);
        m_strServVersion = readUTF(is);
        m_strRemovalStatus = readUTF(is);
    }


    /**
     *  This method is called to write this <code>ServiceRemovalInfo</code> object
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        writeUTF(out, m_strServGUID);
        writeUTF(out, m_strServVersion);
        writeUTF(out, m_strRemovalStatus);
    }


    /**
     *  This method tests whether this <code>ServiceRemovalInfo</code> object
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     * This method is used returns the String representation of this
     * <code>ServiceRemovalInfo</code> object.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Service Removable Info Details ");
        strBuf.append("[");
        strBuf.append("Removal status = ");
        strBuf.append(m_strRemovalStatus);
        strBuf.append(", ");
        strBuf.append("Service GUID = ");
        strBuf.append(m_strServGUID);
        strBuf.append(", ");
        strBuf.append("Service version = ");
        strBuf.append(m_strServVersion);
        strBuf.append("]");
        return strBuf.toString();
    }
}
