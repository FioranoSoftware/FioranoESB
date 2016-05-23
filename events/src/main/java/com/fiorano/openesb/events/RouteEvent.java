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

public class RouteEvent extends ApplicationEvent{
    private String m_routeGUID;

    /**
     *  Gets the Route GUID of the Application Event ( Route Event ) used for Debugger.
     *
     *@return    The applicationGUID value
     *@see       #setApplicationGUID(java.lang.String)
     */
    public String getRouteGUID()
    {
        return m_routeGUID;
    }


    /**
     *  Sets the Route GUID of the Application Event ( Route Event ) used for Debugger.
     */
    public void setRouteGUID(String guid)
    {
        m_routeGUID = guid;
    }

    /**
     *  This method sets all the values of this <code>RouteEvent</code>
     *  from the given byte array.
     *
     *@param  bytes            The byte array from which this object's values
     *      are to be read.
     *@exception  java.io.IOException  If any error occurs while reading the values from
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

        m_routeGUID = readUTF(dis);
    }


    /**
     *  This method converts the <code>RoutenEvent</code> object to a byte
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

        writeUTF(dos, m_routeGUID);
        return bos.toByteArray();
    }


    /**
     *  This method gets the eventType of the <code>RouteEvent</code>
     *  object.
     *
     *@return    The type of this event.
     */
    public EventType getEventType()
    {
        return EventType.APPLICATION_EVENT;
    }


    /**
     *  Sets the fieldValues attribute of the RouteEvent object
     *
     *@param  resultSet            The new fieldValues value
     *@exception  com.fiorano.openesb.utils.exception.FioranoException
     */
    public void setFieldValues(ResultSet resultSet)
            throws FioranoException
    {
        try
        {
            super.setFieldValues(resultSet);
            m_routeGUID = resultSet.getString("ROUTE_GUID");
        }
        catch (SQLException se)
        {
            throw new FioranoException( se);
        }
    }


    /**
     *  Returns the String representation of this event.
     *
     *@return  String representation of this event
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(baseString);
        strBuf.append("\n");
        strBuf.append("RouteEvent Details ");
        strBuf.append("\n");
        strBuf.append("=================");
        strBuf.append("\n\t");
        strBuf.append("RouteGUID = ");
        strBuf.append(m_routeGUID);
        return strBuf.toString();
    }


    /**
     *  This method gets the XML string representation of <code>RouteEvent</code>
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
        strBuf.append("<RouteEvent>");
        strBuf.append("<RouteGUID>");
        strBuf.append(m_routeGUID);
        strBuf.append("</RouteGUID>");
        strBuf.append("</RouteEvent>");
        strBuf.append("</Event>");
        return strBuf.toString();
    }

}
