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

public class ApplicationEvent extends SystemEvent{
    private String m_appGUID;
    private String m_appName;
    private String m_version;

    private String handleID;
    private String applicationType;

    public enum ApplicationEventType {
        APPLICATION_LAUNCHED, APPLICATION_STOPPED, APPLICATION_SYNCHRONIZED, APPLICATION_LAUNCH_STARTED, APPLICATION_SAVED, APPLICATION_UPDATED, APPLICATION_DELETED, ROUTE_BP_ADDED, ROUTE_BP_REMOVED, APPLICATION_RENAMED
    }

    private ApplicationEventType applicationEventType;

    public ApplicationEventType getApplicationEventType() {
        return applicationEventType;
    }

    public void setApplicationEventType(ApplicationEventType applicationEventType) {
        this.applicationEventType = applicationEventType;
    }

    /**
     *  Constructor for the ApplicationEvent object
     */
    public ApplicationEvent() { }


    /**
     *  This method sets the applicationGUID of the <code>ApplicationEvent</code>
     *  object.
     *
     *@param  appGUID  The new applicationGUID value
     *@see             #getApplicationGUID()
     */
    public void setApplicationGUID(String appGUID)
    {
        m_appGUID = appGUID;
    }


    /**
     *  Sets the applicationName attribute of the ApplicationEvent object
     *
     *@param  appName  The new applicationName value
     */
    public void setApplicationName(String appName)
    {
        m_appName = appName;
    }

    public void setHandleID(String handleID) {
        this.handleID = handleID;
    }

    /**
     *  This method gets the applicationGUID of the <code>ApplicationEvent</code>
     *  object.
     *
     *@return    The applicationGUID value
     *@see       #setApplicationGUID(java.lang.String)
     */
    public String getApplicationGUID()
    {
        return m_appGUID;
    }


    /**
     *  Gets the applicationName attribute of the ApplicationEvent object
     *
     *@return    The applicationName value
     */
    public String getApplicationName()
    {
        return m_appName;
    }

    public String getHandleID() {
        return handleID;
    }

    /**
     *  Gets the Application version from the application event.
     *
     *@return    The applicationGUID value
     *@see       #setApplicationGUID(java.lang.String)
     */
    public String getApplicationVersion()
    {
        return m_version;
    }


    /**
     *  sets the Application version to the Application event
     */
    public void setApplicationVersion(String version)
    {
        m_version = version;
    }


    /**
     *  This method sets all the values of this <code>ApplicationEvent</code>
     *  from the given byte array.
     *
     *@param  bytes            The byte array from which this object's values
     *      are to be read.
     *@exception IOException  If any error occurs while reading the values from
     *      the byte array results in throwing of IOException.
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

        m_appGUID = readUTF(dis);
        m_appName = readUTF(dis);
        m_version = readUTF(dis);
        handleID = readUTF(dis);
    }


    /**
     *  This method converts the <code>ApplicationEvent</code> object to a byte
     *  array.
     *
     *@return                  The byte array representation of this object.
     *@exception  IOException  If any error occurs while converting the values
     *      to the byte array results in throwing of IOException.
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

        writeUTF(dos, m_appGUID);
        writeUTF(dos, m_appName);
        writeUTF(dos, m_version);
        writeUTF(dos, handleID);

        return bos.toByteArray();
    }

    /**
     *  This method gets the eventType of the <code>ApplicationEvent</code>
     *  object.
     *
     *@return    The type of this event.
     */
    public EventType getEventType()
    {
        return EventType.APPLICATION_EVENT;
    }


    /**
     *  Sets the fieldValues attribute of the ApplicationEvent object
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
            m_appGUID = resultSet.getString("APP_GUID");
            m_appName = resultSet.getString("APP_NAME");
            try{
                m_version = resultSet.getString("APP_VERSION");
            }catch(SQLException e)
            {
                // ignore. App version has been added later. To support BC.
            }
        }
        catch (SQLException se)
        {
            throw new FioranoException(se);
        }
    }

    /** Checks if this event is identical to event passed as parameter.
     * @param 'event' to be compared to this event
     * @return 'true' if the events are identical else 'false'
     */
    public boolean isIdentical(Event event) {
        try
        {
            ApplicationEvent thatEvent = (ApplicationEvent)event;
            return getApplicationGUID().equals(thatEvent.getApplicationGUID())
                    && getApplicationVersion().equals(thatEvent.getApplicationVersion())
                    && this.getEventID() == thatEvent.getEventID();
        }
        catch(ClassCastException e)
        {
            //ignore the exception
            //return that it is not same
        }
        return false;
    }



    /**
     *  Returns the String representation of this event.
     *
     *@return    Description of the Return Value
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(baseString);
        strBuf.append("\n");
        strBuf.append("ApplicationEvent Details ");
        strBuf.append("\n");
        strBuf.append("=================");
        strBuf.append("\n\t");
        strBuf.append("ApplicationGUID = ");
        strBuf.append(m_appGUID);
        strBuf.append("\n\t");
        strBuf.append("Application Name = ");
        strBuf.append(m_appName);
        strBuf.append("\n\t");
        strBuf.append("Application Version No = ");
        strBuf.append(m_version);
        return strBuf.toString();
    }


    /**
     *  This method gets the XML string representation of <code>ApplicationEvent</code>
     *  object.
     *
     *@return    The xml string representing this object.
     */
    public String toXML()
    {
        String baseString = super.toXML();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<Event>");
        strBuf.append(baseString);
        strBuf.append("<ApplicationEvent>");
        strBuf.append("<ApplicationGUID>");
        strBuf.append(m_appGUID);
        strBuf.append("</ApplicationGUID>");
        strBuf.append("<ApplicationName>");
        strBuf.append(m_appName);
        strBuf.append("</ApplicationName>");
        strBuf.append("<ApplicationVersion>");
        strBuf.append(m_version);
        strBuf.append("</ApplicationVersion>");
        strBuf.append("<ApplicationType>");
        strBuf.append(applicationType != null ? applicationType : "");
        strBuf.append("</ApplicationType>");
        strBuf.append("</ApplicationEvent>");
        strBuf.append("</Event>");
        return strBuf.toString();
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getApplicationType() {
        return applicationType;
    }
}

