/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class LogPacket extends DmiObject
{
    private String  m_strSrcServiceInstName;
    private String  m_strSrcApplInstanceID;
    private String  m_strSrcPort;
    private String  m_strSrcNode;

    private String  m_strTgtServiceInstName;
    private String  m_strTgtApplInstanceID;
    private String  m_strTgtPort;
    private String  m_strTgtNode;

    private String  m_replyToChannel;

    //Carries forward context information
    private DmiCarryForwardContext m_carryFwdContext;

    //SBW specific:  Stores the workflowInstID.
    private String  m_strWorkflowInstID;

    private String  m_strUserDefinedDocID;
    private String  m_strComment;

    // Text message
    private String  m_strTextMessage;

    // Correlation ID
    private String  m_strCorrelationID;
    // to handle syc request reply
    private DmiRequesterInfo m_requesterInfo;

    private Hashtable m_hashOfValues;

    private String  m_strStoreName;

    /**
     *  Gets the srcServiceInstanceName attribute of the LogPacket object
     *
     * @return The srcServiceInstanceName value
     */
    public String getSrcServiceInstanceName()
    {
        return m_strSrcServiceInstName;
    }

    /**
     *  Gets the srcAppInstanceID attribute of the LogPacket object
     *
     * @return The srcAppInstanceID value
     */
    public String getSrcAppInstanceID()
    {
        return m_strSrcApplInstanceID;
    }

    /**
     *  Gets the srcPort attribute of the LogPacket object
     *
     * @return The srcPort value
     */
    public String getSrcPort()
    {
        return m_strSrcPort;
    }

    /**
     *  Gets the srcNode attribute of the LogPacket object
     *
     * @return The srcNode value
     */
    public String getSrcNode()
    {
        return m_strSrcNode;
    }

    /**
     *  Gets the tgtServiceInstanceName attribute of the LogPacket object
     *
     * @return The tgtServiceInstanceName value
     */
    public String getTgtServiceInstanceName()
    {
        return m_strTgtServiceInstName;
    }

    /**
     *  Gets the tgtAppInstanceID attribute of the LogPacket object
     *
     * @return The tgtAppInstanceID value
     */
    public String getTgtAppInstanceID()
    {
        return m_strTgtApplInstanceID;
    }

    /**
     *  Gets the tgtPort attribute of the LogPacket object
     *
     * @return The tgtPort value
     */
    public String getTgtPort()
    {
        return m_strTgtPort;
    }

    /**
     *  Gets the tgtNode attribute of the LogPacket object
     *
     * @return The tgtNode value
     */
    public String getTgtNode()
    {
        return m_strTgtNode;
    }

    /**
     *  Gets the replyToChannel attribute of the LogPacket object
     *
     * @return The replyToChannel value
     */
    public String getReplyToChannel()
    {
        return m_replyToChannel;
    }

    /**
     *  Gets the carryForwardContext attribute of the LogPacket object
     *
     * @return The carryForwardContext value
     */
    public DmiCarryForwardContext getCarryForwardContext()
    {
        return m_carryFwdContext;
    }

    /**
     *  Gets the workflowInstID attribute of the LogPacket object
     *
     * @return The workflowInstID value
     */
    public String getWorkflowInstID()
    {
        return m_strWorkflowInstID;
    }

    /**
     *  Gets the userDefinedDocID attribute of the LogPacket object
     *
     * @return The userDefinedDocID value
     */
    public String getUserDefinedDocID()
    {
        return m_strUserDefinedDocID;
    }

    /**
     *  Gets the comment attribute of the LogPacket object
     *
     * @return The comment value
     */
    public String getComment()
    {
        return m_strComment;
    }

    /**
     *  Gets the textMessage attribute of the LogPacket object
     *
     * @return The textMessage value
     */
    public String getTextMessage()
    {
        return m_strTextMessage;
    }

    /**
     *  Gets the correlationID attribute of the LogPacket object
     *
     * @return The correlationID value
     */
    public String getCorrelationID()
    {
        return m_strCorrelationID;
    }

    /**
     *  Gets the requesterInfo attribute of the LogPacket object
     *
     * @return The requesterInfo value
     */
    public DmiRequesterInfo getRequesterInfo()
    {
        return m_requesterInfo;
    }


    /**
     *  Gets the value attribute of the LogPacket object
     *
     * @param name Description of the Parameter
     * @return The value value
     */
    public byte[] getValue(String name)
    {
        return (byte[]) m_hashOfValues.get(name);
    }

    /**
     *  Gets the storeName attribute of the LogPacket object
     *
     * @return The storeName value
     */
    public String getStoreName()
    {
        return m_strStoreName;
    }

    /**
     *  Gets the objectID attribute of the LogPacket object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.LOG_PACKET;
    }


    /**
     *  Gets the bytes attribute of the LogPacket object
     *
     * @param versionNo
     * @return The bytes value
     * @exception FioranoException Description of the Exception
     */
    public byte[] getBytes(int versionNo)
        throws FioranoException
    {

        try
        {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            writeUTF(dos, m_strSrcServiceInstName);
            writeUTF(dos, m_strSrcApplInstanceID);
            writeUTF(dos, m_strSrcPort);
            writeUTF(dos, m_strSrcNode);

            writeUTF(dos, m_strTgtServiceInstName);
            writeUTF(dos, m_strTgtApplInstanceID);
            writeUTF(dos, m_strTgtPort);
            writeUTF(dos, m_strTgtNode);

            writeUTF(dos, m_replyToChannel);

            if (m_carryFwdContext != null)
            {
                dos.writeInt(1);
                m_carryFwdContext.toStream(dos, versionNo);
            }
            else
                dos.writeInt(0);

            writeUTF(dos, m_strWorkflowInstID);
            writeUTF(dos, m_strUserDefinedDocID);
            writeUTF(dos, m_strComment);
            writeUTF(dos, m_strTextMessage);
            writeUTF(dos, m_strCorrelationID);

            if (m_requesterInfo != null)
            {
                dos.writeInt(1);
                m_requesterInfo.toStream(dos, versionNo);
            }
            else
                dos.writeInt(0);

            if (m_hashOfValues != null && m_hashOfValues.size() > 0)
            {
                int num = m_hashOfValues.size();

                dos.writeInt(num);

                Enumeration keys = m_hashOfValues.keys();

                while (keys.hasMoreElements())
                {
                    String key = (String) keys.nextElement();

                    UTFReaderWriter.writeUTF(dos, key);

                    byte[] value = (byte[]) m_hashOfValues.get(key);

                    dos.writeInt(value.length);
                    dos.write(value);
                }
            }
            else
            {
                dos.writeInt(0);
            }
            writeUTF(dos, m_strStoreName);
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            throw new FioranoException( e);
        }
    }

    /**
     *  Sets the srcServiceInstanceName attribute of the LogPacket object
     *
     * @param servInst The new srcServiceInstanceName value
     */
    public void setSrcServiceInstanceName(String servInst)
    {
        m_strSrcServiceInstName = servInst;
    }

    /**
     *  Sets the srcAppInstanceID attribute of the LogPacket object
     *
     * @param appInst The new srcAppInstanceID value
     */
    public void setSrcAppInstanceID(String appInst)
    {
        m_strSrcApplInstanceID = appInst;
    }

    /**
     *  Sets the srcPort attribute of the LogPacket object
     *
     * @param port The new srcPort value
     */
    public void setSrcPort(String port)
    {
        m_strSrcPort = port;
    }

    /**
     *  Sets the srcNode attribute of the LogPacket object
     *
     * @param node The new srcNode value
     */
    public void setSrcNode(String node)
    {
        m_strSrcNode = node;
    }

    /**
     *  Sets the tgtServiceInstanceName attribute of the LogPacket object
     *
     * @param tgtServiceInst The new tgtServiceInstanceName value
     */
    public void setTgtServiceInstanceName(String tgtServiceInst)
    {
        m_strTgtServiceInstName = tgtServiceInst;
    }

    /**
     *  Sets the tgtAppInstanceID attribute of the LogPacket object
     *
     * @param tgtAppInst The new tgtAppInstanceID value
     */
    public void setTgtAppInstanceID(String tgtAppInst)
    {
        m_strTgtApplInstanceID = tgtAppInst;
    }

    /**
     *  Sets the tgtPort attribute of the LogPacket object
     *
     * @param port The new tgtPort value
     */
    public void setTgtPort(String port)
    {
        m_strTgtPort = port;
    }

    /**
     *  Sets the tgtNode attribute of the LogPacket object
     *
     * @param node The new tgtNode value
     */
    public void setTgtNode(String node)
    {
        m_strTgtNode = node;
    }

    /**
     *  Sets the replyToChannel attribute of the LogPacket object
     *
     * @param channel The new replyToChannel value
     */
    public void setReplyToChannel(String channel)
    {
        m_replyToChannel = channel;
    }

    /**
     *  Sets the carryForwardContext attribute of the LogPacket object
     *
     * @param ctxt The new carryForwardContext value
     */
    public void setCarryForwardContext(DmiCarryForwardContext ctxt)
    {
        m_carryFwdContext = ctxt;
    }

    /**
     *  Sets the workflowInstID attribute of the LogPacket object
     *
     * @param id The new workflowInstID value
     */
    public void setWorkflowInstID(String id)
    {
        m_strWorkflowInstID = id;
    }

    /**
     *  Sets the userDefinedDocID attribute of the LogPacket object
     *
     * @param docId The new userDefinedDocID value
     */
    public void setUserDefinedDocID(String docId)
    {
        m_strUserDefinedDocID = docId;
    }

    /**
     *  Sets the comment attribute of the LogPacket object
     *
     * @param comment The new comment value
     */
    public void setComment(String comment)
    {
        m_strComment = comment;
    }

    /**
     *  Sets the textMessage attribute of the LogPacket object
     *
     * @param msg The new textMessage value
     */
    public void setTextMessage(String msg)
    {
        m_strTextMessage = msg;
    }

    /**
     *  Sets the correlationID attribute of the LogPacket object
     *
     * @param id The new correlationID value
     */
    public void setCorrelationID(String id)
    {
        m_strCorrelationID = id;
    }

    /**
     *  Sets the requesterInfo attribute of the LogPacket object
     *
     * @param info The new requesterInfo value
     */
    public void setRequesterInfo(DmiRequesterInfo info)
    {
        m_requesterInfo = info;
    }

    /**
     *  Sets the storeName attribute of the LogPacket object
     *
     * @param name The new storeName value
     */
    public void setStoreName(String name)
    {
        m_strStoreName = name;
    }

    /**
     *  Sets the bytes attribute of the LogPacket object
     *
     * @param bytes The new bytes value
     * @param versionNo
     * @exception FioranoException Description of the Exception
     */
    public void setBytes(byte[] bytes, int versionNo)
        throws FioranoException
    {
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bis);

            m_strSrcServiceInstName = readUTF(dis);
            m_strSrcApplInstanceID = readUTF(dis);
            m_strSrcPort = readUTF(dis);
            m_strSrcNode = readUTF(dis);

            m_strTgtServiceInstName = readUTF(dis);
            m_strTgtApplInstanceID = readUTF(dis);
            m_strTgtPort = readUTF(dis);
            m_strTgtNode = readUTF(dis);

            m_replyToChannel = readUTF(dis);

            if (dis.readInt() == 1)
            {
                m_carryFwdContext = new DmiCarryForwardContext();
                m_carryFwdContext.fromStream(dis, versionNo);
            }

            m_strWorkflowInstID = readUTF(dis);
            m_strUserDefinedDocID = readUTF(dis);
            m_strComment = readUTF(dis);
            m_strTextMessage = readUTF(dis);
            m_strCorrelationID = readUTF(dis);

            if (dis.readInt() == 1)
            {
                m_requesterInfo = new DmiRequesterInfo();
                m_requesterInfo.fromStream(dis, versionNo);
            }

            int dataSize = dis.readInt();

            if (dataSize > 0)
            {
                for (int i = 0; i < dataSize; ++i)
                {
                    String key = UTFReaderWriter.readUTF(dis);
                    int length = dis.readInt();
                    byte[] value = new byte[length];

                    dis.read(value);
                    m_hashOfValues.put(key, value);
                }
            }
            m_strStoreName = readUTF(dis);
        }
        catch (Exception ex)
        {
            throw new FioranoException( ex);
        }
    }

    /**
     *  Adds a feature to the Value attribute of the LogPacket object
     *
     * @param name The feature to be added to the Value attribute
     * @param value The feature to be added to the Value attribute
     */
    public void addValue(String name, byte[] value)
    {
        m_hashOfValues.put(name, value);
    }

    /**
     *  Description of the Method
     *
     * @param name Description of the Parameter
     */
    public void removeValue(String name)
    {
        m_hashOfValues.remove(name);
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
}
