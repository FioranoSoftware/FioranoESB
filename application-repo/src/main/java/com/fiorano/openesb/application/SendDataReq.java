/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class SendDataReq extends DmiObject
{
    private String  m_strServInstName;
    private String  m_strAppName;
    private String  m_strPortName;
    private DataPacket m_dataPacket;
    private TargetPort m_tgtPort;


    /**
     *  Constructor for the SendDataReq object
     */
    public SendDataReq()
    {
    }

    /**
     *  Gets the servInstName attribute of the RuntimeArgsReq object
     *
     * @return The servInstName value
     */
    public String getServInstName()
    {
        return m_strServInstName;
    }

    /**
     *  Gets the appName attribute of the RuntimeArgsReq object
     *
     * @return The appName value
     */
    public String getAppName()
    {
        return m_strAppName;
    }


    /**
     *  Gets the timeout attribute of the ReadDataReq object
     *
     * @return The timeout value
     */
    public String getPortName()
    {
        return m_strPortName;
    }

    /**
     *  Gets the dataPacket attribute of the SendDataReq object
     *
     * @return The dataPacket value
     */
    public DataPacket getDataPacket()
    {
        return m_dataPacket;
    }

    /**
     *  Gets the tgtPort attribute of the SendDataReq object
     *
     * @return The tgtPort value
     */
    public TargetPort getTgtPort()
    {
        return m_tgtPort;
    }

    /**
     *  Gets the objectID attribute of the LoginInfo object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SEND_DATA_REQ;
    }

    /**
     *  Sets the servInstName attribute of the RuntimeArgsReq object
     *
     * @param servInstName The new servInstName value
     */
    public void setServInstName(String servInstName)
    {
        m_strServInstName = servInstName;
    }

    /**
     *  Sets the appName attribute of the RuntimeArgsReq object
     *
     * @param appName The new appName value
     */
    public void setAppName(String appName)
    {
        m_strAppName = appName;
    }

    /**
     *  Sets the timeout attribute of the ReadDataReq object
     *
     * @param portName The new portName value
     */
    public void setPortName(String portName)
    {
        m_strPortName = portName;
    }

    /**
     *  Sets the dataPacket attribute of the SendDataReq object
     *
     * @param dataPacket The new dataPacket value
     */
    public void setDataPacket(DataPacket dataPacket)
    {
        m_dataPacket = dataPacket;
    }

    /**
     *  Sets the tgtPort attribute of the SendDataReq object
     *
     * @param tgtPort The new tgtPort value
     */
    public void setTgtPort(TargetPort tgtPort)
    {
        m_tgtPort = tgtPort;
    }


    /**
     *  Description of the Method
     *
     * @param os Description of the Parameter
     * @param versionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);
        writeUTF(os, m_strAppName);
        writeUTF(os, m_strServInstName);
        writeUTF(os, m_strPortName);

        m_dataPacket.toStream(os, versionNo);

        if (m_tgtPort == null)
            os.writeInt(0);
        else
        {
            os.writeInt(1);
            m_tgtPort.toStream(os, versionNo);
        }

    }

    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param versionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);
        m_strAppName = readUTF(is);
        m_strServInstName = readUTF(is);
        m_strPortName = readUTF(is);

        m_dataPacket = new DataPacket();
        m_dataPacket.fromStream(is, versionNo);

        int temp = is.readInt();

        if (temp == 1)
        {
            m_tgtPort = new TargetPort();
            m_tgtPort.fromStream(is, versionNo);
        }
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
     *  Description of the Method
     */
    public void reset()
    {
    }

}
