/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.events;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MicroServiceEvent extends SystemEvent{
    private String m_strServiceGUID;

    private String m_strServiceInstanceName;

    private float m_fServiceVersion;

    private String m_strAppGUID;

    private String m_strAppVersion;

    public enum MicroServiceEventType{
        SERVICE_LAUNCHED, SERVICE_STOPPED, SERVICE_LAUNCHING, SERVICE_LAUNCH_FAILED, SERVICE_STOP_FAILED;
    }

    private MicroServiceEventType microServiceEventType;

    public MicroServiceEventType getMicroServiceEventType() {
        return microServiceEventType;
    }

    public void setMicroServiceEventType(MicroServiceEventType microServiceEventType) {
        this.microServiceEventType = microServiceEventType;
    }

    /**
     *  Constructor for the ServiceEvent object
     */
    public MicroServiceEvent() { }


    /**
     *  Sets the sourceTPSName attribute of the ServiceEvent object
     *
     *@param  sourceTPSName  The new sourceTPSName value
     */
    public void setSourceTPSName(String sourceTPSName)
    {
        setSource(sourceTPSName.toLowerCase());
    }

    /**
     *  Sets the applicationGUID attribute of the ServiceEvent object
     *
     *@param  appGUID  The new applicationGUID value
     */
    public void setApplicationGUID(String appGUID)
    {
        m_strAppGUID = appGUID;
    }

    /**
     *  Gets the applicationGUID attribute of the ServiceEvent object
     *
     *@return    The applicationGUID value
     */
    public String getApplicationGUID()
    {
        return m_strAppGUID;
    }

    /**
     *  Sets the applicationVersion attribute of the ServiceEvent object
     *
     *@param  appVersion  The new applicationVersion value
     */
    public void setApplicationVersion(String appVersion)
    {
        m_strAppVersion = appVersion;
    }

    /**
     *  Gets the application version attribute of the ServiceEvent object
     *
     *@return    The applicationVersion value
     */
    public String getApplicationVersion()
    {
        return m_strAppVersion;
    }

    /**
     *  Gets the sourceTPSName attribute of the ServiceEvent object
     *
     *@return    The sourceTPSName value
     */
    public String getSourceTPSName()
    {
        return getSource();
    }


    /**
     *  Sets the serviceGUID attribute of the ServiceEvent object
     *
     *@param  serviceGUID  The new serviceGUID value
     */
    public void setServiceGUID(String serviceGUID)
    {
        m_strServiceGUID = serviceGUID;
    }


    /**
     *  Gets the serviceGUID attribute of the ServiceEvent object
     *
     *@return    The serviceGUID value
     */
    public String getServiceGUID()
    {
        return m_strServiceGUID;
    }

    /**
     *  Gets the serviceVersion attribute of the ServiceEvent object
     *
     *@return    The serviceVersion value
     */
    public float getServiceVersion()
    {
        return m_fServiceVersion;
    }

    /**
     *  Sets the serviceVersion attribute of the ServiceEvent object
     *
     *@param  version  The new serviceVersion value
     */
    public void setServiceVersion(float version)
    {
        m_fServiceVersion = version;
    }

    /**
     *  Sets the serviceInstance attribute of the ServiceEvent object
     *
     *@param  serviceInstance  The new serviceInstance value
     */
    public void setServiceInstance(String serviceInstance)
    {
        m_strServiceInstanceName = serviceInstance;
    }


    /**
     *  Gets the serviceInstance attribute of the ServiceEvent object
     *
     *@return    The serviceInstance value
     */
    public String getServiceInstance()
    {
        return m_strServiceInstanceName;
    }


    /**
     *  Sets the eventValues attribute of the ServiceEvent object
     *
     *@param  bytes            The new eventValues value
     *@param  versionNo        The new eventValues value
     *@exception IOException  Description of the Exception
     */
    public void setEventValues(byte[] bytes, int versionNo)
            throws IOException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        int numBytes = dis.readInt();
        if (numBytes > 0)
        {
            byte[] superBytes = new byte[numBytes];
            dis.read(superBytes);
            super.setEventValues(superBytes, versionNo);
        }

        m_strServiceGUID = readUTF(dis);
        m_strServiceInstanceName = readUTF(dis);

        m_strAppGUID = readUTF(dis);
        m_strAppVersion = readUTF(dis);

        if (versionNo <= 3000)
            return;

        m_fServiceVersion = dis.readFloat();
    }


    /**
     *  Gets the eventValues attribute of the ServiceEvent object
     *
     *@param  versionNo        Description of the Parameter
     *@return                  The eventValues value
     *@exception  IOException  Description of the Exception
     */
    public byte[] getEventValues(int versionNo)
            throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] superBytes = super.getEventValues(versionNo);
        if (superBytes != null && superBytes.length > 0)
        {
            dos.writeInt(superBytes.length);
            dos.write(superBytes);
        }
        else
        {
            dos.writeInt(-1);
        }

        writeUTF(dos, m_strServiceGUID);
        writeUTF(dos, m_strServiceInstanceName);
        writeUTF(dos, m_strAppGUID);
        writeUTF(dos, m_strAppVersion);

        if (versionNo > 3000)
            dos.writeFloat(m_fServiceVersion);

        return bos.toByteArray();
    }


    /**
     *  Gets the eventType attribute of the ServiceEvent object
     *
     *@return    The eventType value
     */
    public EventType getEventType()
    {
        return EventType.MICRO_SERVICE_EVENT;
    }

    /** Checks if this event is identical to event passed as parameter.
     * @param that event to be compared
     * @return 'true' if the events are identical else 'false'
     */
    public boolean isIdentical(Event that)
    {
        try
        {
            return (this.getApplicationGUID().equals(((MicroServiceEvent)that).getApplicationGUID()) &&
                    (this.getApplicationVersion().equals(((MicroServiceEvent)that).getApplicationVersion())) &&
                    (this.getServiceGUID().equalsIgnoreCase(((MicroServiceEvent)that).getServiceGUID())) &&
                    (this.getEventID() == that.getEventID()) );
        }
        catch(ClassCastException e)
        {
            //ignore the exception
            //return that it is not same
        }
        return false;
    }

    /**
     *  Sets the fieldValues attribute of the ServiceEvent object
     *
     *@param  resultSet            The new fieldValues value
     *@exception  FioranoException  Description of the Exception
     */
    public void setFieldValues(ResultSet resultSet)
            throws FioranoException
    {
        try
        {
            super.setFieldValues(resultSet);
            m_strServiceGUID = resultSet.getString("SERVICE_GUID");
            m_fServiceVersion = Float.parseFloat(resultSet.getString("SERVICE_VERSION"));
            m_strServiceInstanceName = resultSet.getString("SERVICE_INST_NAME");
            //todo persist this!!!
//            peerName = resultSet.getString("PEER_NAME");
            m_strAppGUID = resultSet.getString("APP_GUID");
            m_strAppVersion = resultSet.getString("APP_VERSION");
        }
        catch (SQLException se)
        {
            throw new FioranoException( se);
        }
    }

    /**
     *  Returns the String representation of this event.
     *
     *@return  String representation of this event.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(baseString);
        strBuf.append("\n");
        strBuf.append("ServiceEvent Details ");
        strBuf.append("\n");
        strBuf.append("=================");
        strBuf.append("\n\t");
        strBuf.append("ServiceGUID = ");
        strBuf.append(m_strServiceGUID);
        strBuf.append("\n\t");
        strBuf.append("ServiceVersion = ");
        strBuf.append(m_fServiceVersion);
        strBuf.append("\n\t");
        strBuf.append("ServiceInstance = ");
        strBuf.append(m_strServiceInstanceName);
        strBuf.append("\n\t");
        strBuf.append("ApplicationGUID = ");
        strBuf.append(m_strAppGUID);
        strBuf.append("\n\t");
        strBuf.append("ApplicationVersion = ");
        strBuf.append(m_strAppVersion);
        return strBuf.toString();
    }


    /**
     * Returns the XML representation of this event.
     *
     *@return  XML representation of this event.
     */
    public String toXML()
    {
        String baseString = super.toXML();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<Event>");
        strBuf.append(baseString);
        strBuf.append("<ServiceEvent>");
        strBuf.append("<ServiceGUID>");
        strBuf.append(m_strServiceGUID);
        strBuf.append("</ServiceGUID>");
        strBuf.append("<ServiceInstance>");
        strBuf.append(m_strServiceInstanceName);
        strBuf.append("</ServiceInstance>");
        strBuf.append("<ApplicationGUID>");
        strBuf.append(m_strAppGUID);
        strBuf.append("</ApplicationGUID>");
        strBuf.append("<ApplicationVersion>");
        strBuf.append(m_strAppVersion);
        strBuf.append("</ApplicationVersion>");
        strBuf.append("</ServiceEvent>");
        strBuf.append("</Event>");
        return strBuf.toString();
    }
}
