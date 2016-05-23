/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class DmiRequesterInfo extends DmiObject
{
    private String  m_replyToPort;
    private String  m_replyToServInstName;
    private String  m_replyToAppName;
    private String  m_replyToNode;

    /**
     *  Gets the channel name over which the requester service is listening for
     *  reply.
     *
     * @return Channel name over which reply is required to be sent.
     * @see #setReplyToPort(String)
     * @since Tifosi2.0
     */
    public String getReplyToPort()
    {
        return m_replyToPort;
    }

    /**
     *  Gets the instance name of the service which will be receiving the reply
     *  for the synchronous request.
     *
     * @return Instance name of the reply receiver service instance
     * @see #setReplyToServInstName(String)
     * @since Tifosi2.0
     */
    public String getReplyToServInstName()
    {
        return m_replyToServInstName;
    }

    /**
     *  Gets the instance name of the application to which the requester service
     *  belongs.
     *
     * @return Instance name of the application to which belongs the reply
     *      receiver service instance.
     * @see #setReplyToAppName(String)
     * @since Tifosi2.0
     */
    public String getReplyToAppName()
    {
        return m_replyToAppName;
    }

    /**
     *  Gets the name of the node over which the requester service is running.
     *
     * @return Name of the node over which requester service is running.
     * @see #setReplyToNode(String)
     * @since Tifosi2.0
     */
    public String getReplyToNode()
    {
        return m_replyToNode;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.REQUESTER_INFO;
    }

    /**
     *  Sets the specified string as channel name over which the requester
     *  service is listening for reply.
     *
     * @param replyToPort The string to be set as channel name over which reply
     *      is required to be sent.
     * @see #getReplyToPort()
     * @since Tifosi2.0
     */
    public void setReplyToPort(String replyToPort)
    {
        m_replyToPort = replyToPort;
    }


    /**
     *  Sets the name of the service instance which will be receiving the reply
     *  for the synchronous request to the specified <code>replyToServInstName</code>
     *  argument.
     *
     * @param replyToServInstName The string to be set as instance name of the
     *      reply receiver service instance.
     * @see #getReplyToServInstName()
     * @since Tifosi2.0
     */
    public void setReplyToServInstName(String replyToServInstName)
    {
        m_replyToServInstName = replyToServInstName;
    }


    /**
     *  Sets the name of the application instance to which the requester service
     *  belongs and to which it is expected to receive reply. specified string
     *  as
     *
     * @param replyToAppName The string to be set as instance name of the
     *      application, to which belongs the reply receiver service instance.
     * @see #getReplyToAppName()
     * @since Tifosi2.0
     */
    public void setReplyToAppName(String replyToAppName)
    {
        m_replyToAppName = replyToAppName;
    }

    /**
     *  Sets the name of the node over which the requester service is running.
     *
     * @param replyToNode The string to be set as name of the node over which
     *      requester service is running.
     * @see #getReplyToNode()
     * @since Tifosi2.0
     */
    public void setReplyToNode(String replyToNode)
    {
        m_replyToNode = replyToNode;
    }

    /**
     *  This method resets the values of the data members of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_replyToPort = null;
        m_replyToServInstName = null;
        m_replyToAppName = null;
        m_replyToNode = null;
    }

    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void fromStream(java.io.DataInput is, int clientVersionNo)
        throws java.io.IOException
    {
        super.fromStream(is, clientVersionNo);
        m_replyToPort = readUTF(is);
        m_replyToServInstName = readUTF(is);
        m_replyToAppName = readUTF(is);
        m_replyToNode = readUTF(is);
    }

    /**
     *  Description of the Method
     *
     * @param out Description of the Parameter
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void toStream(java.io.DataOutput out, int clientVersionNo)
        throws java.io.IOException
    {
        super.toStream(out, clientVersionNo);
        writeUTF(out, m_replyToPort);
        writeUTF(out, m_replyToServInstName);
        writeUTF(out, m_replyToAppName);
        writeUTF(out, m_replyToNode);
    }

    /**
     *  This method tests whether this object of <code>DmiRequesterInfo</code>
     *  has the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  This method is used to get the String representation of this <code>DmiRequesterInfo</code>
     *  object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("DMI request info Details ");
        strBuf.append("[");
        strBuf.append("Reply to application name = ");
        strBuf.append(m_replyToAppName);
        strBuf.append(", ");
        strBuf.append("Reply to node = ");
        strBuf.append(m_replyToNode);
        strBuf.append(", ");
        strBuf.append("Reply to port = ");
        strBuf.append(m_replyToPort);
        strBuf.append(", ");
        strBuf.append("Reply to Service instance name = ");
        strBuf.append(m_replyToServInstName);
        strBuf.append("]");
        return strBuf.toString();
    }
}
