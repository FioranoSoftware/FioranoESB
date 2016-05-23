/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
public class InQueuePacket
{
    String          m_portName = null;
    String          m_serviceInstName = null;
    String          m_appGUID = null;

    /**
     *  Constructor for the InQueuePacket object
     */
    public InQueuePacket()
    {
    }

    /**
     *  Gets the portName attribute of the InQueuePacket object
     *
     * @return The portName value
     */
    public String getPortName()
    {
        return m_portName;
    }

    /**
     *  Gets the serviceName attribute of the InQueuePacket object
     *
     * @return The serviceName value
     */
    public String getServiceName()
    {
        return m_serviceInstName;
    }

    /**
     *  Gets the applicationGUID attribute of the InQueuePacket object
     *
     * @return The applicationGUID value
     */
    public String getApplicationGUID()
    {
        return m_appGUID;
    }

    /**
     *  Sets the portName attribute of the InQueuePacket object
     *
     * @param portName The new portName value
     */
    public void setPortName(String portName)
    {
        m_portName = portName;
    }

    /**
     *  Sets the serviceName attribute of the InQueuePacket object
     *
     * @param serviceInstName The new serviceName value
     */
    public void setServiceName(String serviceInstName)
    {
        m_serviceInstName = serviceInstName;
    }

    /**
     *  Sets the applicationGUID attribute of the InQueuePacket object
     *
     * @param appGUID The new applicationGUID value
     */
    public void setApplicationGUID(String appGUID)
    {
        m_appGUID = appGUID;
    }

    /**
     *  Description of the Method
     *
     * @param dos Description of the Parameter
     * @param versionNo
     * @exception IOException Description of the Exception
     */
    public void toStream(DataOutput dos, int versionNo)
        throws IOException
    {
        if (m_appGUID != null)
            UTFReaderWriter.writeUTF(dos, m_appGUID);
        else
            UTFReaderWriter.writeUTF(dos, "");

        if (m_serviceInstName != null)
            UTFReaderWriter.writeUTF(dos, m_serviceInstName);
        else
            UTFReaderWriter.writeUTF(dos, "");

        if (m_portName != null)
            UTFReaderWriter.writeUTF(dos, m_portName);
        else
            UTFReaderWriter.writeUTF(dos, "");
    }

    /**
     *  Description of the Method
     *
     * @param dis Description of the Parameter
     * @param versionNo
     * @exception IOException Description of the Exception
     */
    public void fromStream(DataInput dis, int versionNo)
        throws IOException
    {
        String temp = UTFReaderWriter.readUTF(dis);

        if (temp.equals(""))
            setApplicationGUID(null);
        else
            setApplicationGUID(temp);

        temp = UTFReaderWriter.readUTF(dis);
        if (temp.equals(""))
            setServiceName(null);
        else
            setServiceName(temp);

        temp = UTFReaderWriter.readUTF(dis);
        if (temp.equals(""))
            setPortName(null);
        else
            setPortName(temp);
    }

    /**
     * @param iq
     * @return
     */
    public boolean equals(Object iq)
    {
        if (iq == null || !(iq instanceof InQueuePacket))
            return false;

        InQueuePacket iqp = (InQueuePacket) iq;

        if (m_appGUID != null && m_appGUID.equals(iqp.getApplicationGUID()) &&
            m_serviceInstName != null && m_serviceInstName.equals(iqp.getServiceName()) &&
            m_portName != null && m_portName.equals(iqp.getPortName()))
            return true;

        return false;
    }
}
