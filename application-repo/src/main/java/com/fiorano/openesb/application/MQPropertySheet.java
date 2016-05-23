/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class MQPropertySheet extends DmiObject
{
    private String  m_mqName;
    private String  m_mqUrl;
    private String  m_userName;
    private String  m_passwd;
    private boolean m_isFioranoMQ = false;


    /**
     *  Constructor for the MQPropertySheet object
     */
    public MQPropertySheet()
    {
    }


    /**
     *  Gets the mqName attribute of the MQPropertySheet object
     *
     * @return The mqName value
     */
    public String getMqName()
    {
        return m_mqName;
    }

    /**
     *  Gets the mqURL attribute of the MQPropertySheet object
     *
     * @return The mqURL value
     */
    public String getMqURL()
    {
        return m_mqUrl;
    }

    /**
     *  Gets the fioranoMQ attribute of the MQPropertySheet object
     *
     * @return The fioranoMQ value
     */
    public boolean isFioranoMQ()
    {
        return m_isFioranoMQ;
    }

    /**
     *  Gets the userName attribute of the MQPropertySheet object
     *
     * @return The userName value
     */
    public String getUserName()
    {
        return m_userName;
    }

    /**
     *  Gets the passwd attribute of the MQPropertySheet object
     *
     * @return The passwd value
     */
    public String getPasswd()
    {
        return m_passwd;
    }

    /**
     *  Gets the objectID attribute of the MQPropertySheet object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.MQ_PROPERTY_SHEET;
    }

    /**
     *  Sets the fioranoMQ attribute of the MQPropertySheet object
     *
     * @param fioranoMq The new fioranoMQ value
     */
    public void setFioranoMQ(boolean fioranoMq)
    {
        m_isFioranoMQ = fioranoMq;
    }


    /**
     *  Sets the mqName attribute of the MQPropertySheet object
     *
     * @param mqName The new mqName value
     */
    public void setMqName(String mqName)
    {
        m_mqName = mqName;
    }

    /**
     *  Sets the mqURL attribute of the MQPropertySheet object
     *
     * @param mqUrl The new mqURL value
     */
    public void setMqURL(String mqUrl)
    {
        m_mqUrl = mqUrl;
    }


    /**
     *  Sets the userName attribute of the MQPropertySheet object
     *
     * @param userName The new userName value
     */
    public void setUserName(String userName)
    {
        m_userName = userName;
    }


    /**
     *  Sets the passwd attribute of the MQPropertySheet object
     *
     * @param passwd The new passwd value
     */
    public void setPasswd(String passwd)
    {
        m_passwd = passwd;
    }


    /**
     *  Description of the Method
     */
    public void reset()
    {
    }


    /**
     *  Description of the Method
     *
     * @exception FioranoException Description of the Exception
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method is called to write this <code>MQPropertySheet</code>
     *  object to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception java.io.IOException IOException that might be thrown while writing to stream
     * @since Tifosi2.0
     */
    public void toStream(java.io.DataOutput out, int versionNo)
        throws java.io.IOException
    {
        writeUTF(out, m_mqName);
        writeUTF(out, m_mqUrl);
        writeUTF(out, m_userName);
        writeUTF(out, m_passwd);
        out.writeBoolean(m_isFioranoMQ);
    }


    /**
     *  This method reads this object <code>MQPropertySheet</code> from the
     *  specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception java.io.IOException IOException that might be thrown while reading from stream
     * @since Tifosi2.0
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        m_mqName = readUTF(is);
        m_mqUrl = readUTF(is);
        m_userName = readUTF(is);
        m_passwd = readUTF(is);
        m_isFioranoMQ = is.readBoolean();
    }


    /**
     *  This method returns the String representation of this <code>MQPropertySheet</code>
     *  object.
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
        strBuf.append("Mq Property Sheet.");
        strBuf.append("[");
        strBuf.append("MQ Name = ");
        strBuf.append(m_mqName);
        strBuf.append(", ");
        strBuf.append("Mq URL = ");
        strBuf.append(m_mqUrl);
        strBuf.append(", ");
        strBuf.append("User Name = ");
        strBuf.append(m_userName);
        strBuf.append(", ");
        strBuf.append("Passwd = ");
        strBuf.append(m_passwd);
        strBuf.append(", ");
        strBuf.append("Is FioranoMQ = ");
        strBuf.append(m_isFioranoMQ);

        strBuf.append("]");

        return strBuf.toString();
    }
}
