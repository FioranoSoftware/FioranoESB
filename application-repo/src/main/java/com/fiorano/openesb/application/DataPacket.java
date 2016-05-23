/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.util.Enumeration;

public class DataPacket extends DmiObject implements  Cloneable
{
    private short   m_dDocumentType;

    //
    // Stores the value.
    private byte[]  m_bValue = null;

    //
    //  Stores the type.
    private boolean m_bisPersistant = false;

    //Stores the GMSIndex into the Persistant store
    //for the persistant messages on the FPS
    //
    private long    m_tpsGMSIndex = -1;

    //
    //  Stores the info about the sender of this packet
    private String  m_strSrcServiceInstanceName;

    //
    // Stores the src application instance ID
    private String  m_strSrcApplicationInstID;

    //
    // Stroes the source node
    private String  m_strSrcNodeName;

    //  Stores the port.
    private String  m_port;

    //  Stores the channel on which reply is to be sent.
    private String  m_replyToPort;

    private DmiCarryForwardContext m_carryFwdContext;

    //SBW specific:  Stores the workflowInstID.
    private String  m_strWorkflowInstID;

    //SBW specific:  Stores the document ID
    private String  m_strUserDefinedDocID;

    private String  m_strComment;

    private String  m_strSysRequest;

    private String  m_strCorrelationID;

    private DmiRequesterInfo m_requesterInfo;

    private boolean m_bIsRedelivered;

    private long    m_lTTL;

    private long    m_lInTime = -1;

    private long    m_lOutTime = -1;

    private long    m_lTotalTime = -1;

    private long    m_lQInsertionTime = -1;

    private String  m_strRunningInstID = null;

    /**
     *  Constructs an object of the type <code>DataPacket</code>.
     *
     * @since Tifosi2.0
     */
    public DataPacket()
    {
    }

    /**
     *  Gets the uniqueRunningInstID attribute of the DataPacket object
     *
     * @return The uniqueRunningInstID value
     */
    public String getUniqueRunningInstID()
    {
        return m_strRunningInstID;
    }

    /**
     *  Sets the inTime attribute of the DataPacket object
     *
     * @return The queueInsertionTime value
     */
    public long getQueueInsertionTime()
    {
        return m_lQInsertionTime;
    }

    /**
     *  Gets the inTime attribute of the DataPacket object
     *
     * @return The inTime value
     */
    public long getInTime()
    {
        return m_lInTime;
    }

    /**
     *  Gets the outTime attribute of the DataPacket object
     *
     * @return The outTime value
     */
    public long getOutTime()
    {
        return m_lOutTime;
    }

    /**
     *  Gets the totalTime attribute of the DataPacket object
     *
     * @return The totalTime value
     */
    public long getTotalTime()
    {
        return m_lTotalTime;
    }


    /**
     *  This method returns the correlation ID for this <code>DataPacket</code>
     *  object.
     *
     * @return The correlation ID for this object
     * @see #setCorrelationID(String)
     * @since Tifosi2.0
     */
    public String getCorrelationID()
    {
        return m_strCorrelationID;
    }

    /**
     *  This method returns the <code>DmiRequesterInfo</code> object for the
     *  service that requested this <code>DataPacket</code>.
     *
     * @return Object of DmiRequesterInfo
     * @see #setRequesterInfo(DmiRequesterInfo)
     * @since Tifosi2.0
     */
    public DmiRequesterInfo getRequesterInfo()
    {
        return m_requesterInfo;
    }


    /**
     *  This method returns the workflow instance ID for this <code>DataPacket</code>
     *  object.
     *
     * @return The workflowInstID for this DataPacket
     * @see #setWorkFlowInstID(String)
     * @since Tifosi2.0
     */
    public String getWorkflowInstID()
    {
        return m_strWorkflowInstID;
    }


    /**
     *  This method returns the comment string of this <code>DataPacket</code>
     *  object.
     *
     * @return The comment for this object
     * @see #setComment(String)
     * @since Tifosi2.0
     */
    public String getComment()
    {
        return m_strComment;
    }


    /**
     *  This method returns the synchronous Request to be sent, using this
     *  <code>DataPacket</code> object.
     *
     * @return The synchronous request for this object.
     * @see #setSysRequest(String)
     * @since Tifosi2.0
     */

    public String getSysRequest()
    {
        return m_strSysRequest;
    }


    /**
     *  This method returns the userdefined documentID for this <code>DataPacket</code>
     *  object.
     *
     * @return The userdefined documentID for this object
     * @see #setUserDefinedDocID(String)
     * @since Tifosi2.0
     */
    public String getUserDefinedDocID()
    {
        return m_strUserDefinedDocID;
    }


    /**
     *  This method returns the object of <code>DmiCarryForwardContext</code>
     *  set for this object of <code>DataPacket</code>.
     *
     * @return Object of DmiCarryForwardContext
     * @see #addContext(DmiSourceContext)
     * @since Tifosi2.0
     */
    public DmiCarryForwardContext getContext()
    {
        return m_carryFwdContext;
    }


    /**
     *  This method returns the document type for this <code>DataPacket</code>
     *  object.
     *
     * @return The documentType for this DataPacket.
     * @see #setDocumentType(short)
     * @since Tifosi2.0
     */
    public short getDocumentType()
    {
        return m_dDocumentType;
    }


    /**
     *  This method returns the byte array data received through this object of
     *  <code>DataPacket</code>.
     *
     * @return The bytearray data
     * @see #setValue(byte[])
     * @since Tifosi2.0
     */
    public byte[] getValue()
    {
        return m_bValue;
    }


    /**
     *  This method returns the instance name of the source service instance
     *  which sends this <code>DataPacket</code>.
     *
     * @return The instance name of sender service instance
     * @see #setSrcServiceInstance(String)
     * @since Tifosi2.0
     */
    public String getSrcServiceInstance()
    {
        return m_strSrcServiceInstanceName;
    }


    /**
     *  This method returns the instance ID of the application to which the
     *  service instance that sent this <code>DataPacket</code> object belongs.
     *
     * @return The source application Instance ID of this DataPacket object
     * @see #setSrcApplicationInstID(String)
     * @since Tifosi2.0
     */
    public String getSrcApplicationInstID()
    {
        return m_strSrcApplicationInstID;
    }


    /**
     *  This method returns the name of the node on which the service instance
     *  that sent this <code>DataPacket</code> object is running.
     *
     * @return The node over which is running the source service instance of
     *      this object
     * @see #setSrcNodeName(String)
     * @since Tifosi2.0
     */
    public String getSrcNodeName()
    {
        return m_strSrcNodeName;
    }


    /**
     *  This method returns the channel over which this <code>DataPacket</code>
     *  is to be sent.
     *
     * @return The channel on which this object is to be sent
     * @see #setPort(String)
     * @since Tifosi2.0
     */
    public String getPort()
    {
        return m_port;
    }


    /**
     *  This method returns the channel on which the sender of this <code>DataPacket</code>
     *  , is listening for reply.
     *
     * @return The channel on which this object is waiting for reply
     * @see #setReplyToPort(String)
     * @since Tifosi2.0
     */
    public String getReplyToPort()
    {
        return m_replyToPort;
    }


    /**
     *  This method returns a boolean specifying the persistence property of the
     *  route, over which this <code>DataPacket</code> object is to be sent.
     *
     * @return boolean : True if route is persistant
     * @see #setPersistant(boolean)
     * @since Tifosi2.0
     */
    public boolean isPersistant()
    {
        return m_bisPersistant;
    }


    /**
     *  This method returns the GMS Index for this message.
     *
     * @return GMS Index or -1 if no index is assigned
     * @see #setGMSIndex(long)
     * @since Tifosi2.0
     */
    public long getGMSIndex()
    {
        return m_tpsGMSIndex;
    }

    /**
     *  This method returns a boolean specifying whether or not this <code>DataPacket</code>
     *  has been delivered previously.
     *
     * @return True if DataPacket is already delivered, false otherwise.
     * @see #setRedelivered(boolean)
     * @since Tifosi2.0
     */
    public boolean isRedelivered()
    {
        return m_bIsRedelivered;
    }


    /**
     *  This method returns the TTL (time to live) for this message.
     *
     * @return the TTL value
     * @see #setTTL(long)
     * @since Tifosi2.0
     */
    public long getTTL()
    {
        return m_lTTL;
    }


    /**
     *  This method returns the id of the object.
     *
     * @return The objectID value
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.DATA_PACKET;
    }

    /**
     *  Returns the approximate amount of memory consumed by this object.
     *
     * @return int size to indicate the length of this object.
     */
    public int getBufferSize()
    {
        return getValue().length;
    }


    /**
     *  Sets the uniqueRunningInstID attribute of the DataPacket object
     *
     * @param runningInstID The new uniqueRunningInstID value
     */
    public void setUniqueRunningInstID(String runningInstID)
    {
        m_strRunningInstID = runningInstID;
    }

    /**
     *  Sets the inTime attribute of the DataPacket object
     *
     * @param insertTime The new queueInsertionTime value
     */
    public void setQueueInsertionTime(long insertTime)
    {
        m_lQInsertionTime = insertTime;
    }

    /**
     *  Sets the inTime attribute of the DataPacket object
     *
     * @param inTime The new inTime value
     */
    public void setInTime(long inTime)
    {
        m_lInTime = inTime;
    }

    /**
     *  Sets the outTime attribute of the DataPacket object
     *
     * @param outTime The new outTime value
     */
    public void setOutTime(long outTime)
    {
        m_lOutTime = outTime;
    }

    /**
     *  Sets the totalTime attribute of the DataPacket object
     *
     * @param totalTime The new totalTime value
     */
    public void setTotalTime(long totalTime)
    {
        m_lTotalTime = totalTime;
    }

    /**
     *  This method is called to set the specified string as correlation ID for
     *  this <code>DataPacket</code>.
     *
     * @param correlationID The string to be set as correlationID
     * @see #getCorrelationID()
     * @since Tifosi2.0
     */
    public void setCorrelationID(String correlationID)
    {
        m_strCorrelationID = correlationID;
    }

    /**
     *  This method is called to set the service that requested this <code>DataPacket</code>
     *  to the specified <code>DmiRequesterInfo</code> argument.
     *
     * @param requesterInfo Object of DmiRequesterInfo to be set
     * @see #getRequesterInfo()
     * @since Tifosi2.0
     */
    public void setRequesterInfo(DmiRequesterInfo requesterInfo)
    {
        m_requesterInfo = requesterInfo;
    }

    /**
     *  This method sets the specified string as workflow instance ID for this
     *  <code>DataPacket</code> object.
     *
     * @param workflowInstID The string to be set as workFlowInstID for this
     *      object
     * @see #getWorkflowInstID()
     * @since Tifosi2.0
     */
    public void setWorkFlowInstID(String workflowInstID)
    {
        m_strWorkflowInstID = workflowInstID;
    }


    /**
     *  This method sets the specified string as comment for this <code>DataPacket</code>
     *  object.
     *
     * @param comment The string to be set as comment for this object
     * @see #getComment()
     * @since Tifosi2.0
     */
    public void setComment(String comment)
    {
        m_strComment = comment;
    }


    /**
     *  This method sets the synchronous Request to be sent using this <code>DataPacket</code>
     *  object.
     *
     * @param sysRequest The string to be set as synchronous request for this
     *      object.
     * @see #getSysRequest()
     * @since Tifosi2.0
     */
    public void setSysRequest(String sysRequest)
    {
        m_strSysRequest = sysRequest;
    }

    /**
     *  This method sets the string as the specified userdefined documentID for
     *  this <code>DataPacket</code> object.
     *
     * @param documentID The string to be set as userdefined documentID for
     *      this object.
     * @see #getUserDefinedDocID()
     * @since Tifosi2.0
     */
    public void setUserDefinedDocID(String documentID)
    {
        m_strUserDefinedDocID = documentID;
    }


    /**
     *  This method sets the document type to the specified <code>docType</code>
     *  for this <code>DataPacket</code> object.
     *
     * @param docType The short to be set as documentType for this DataPacket.
     * @see #getDocumentType()
     * @since Tifosi2.0
     */
    public void setDocumentType(short docType)
    {
        m_dDocumentType = docType;
    }


    /**
     *  This method sets the specified byte array as data to be sent using this
     *  object of <code>DataPacket</code>.
     *
     * @param value The bytearray to be sent by this DataPacket
     * @see #getValue()
     * @since Tifosi2.0
     */
    public void setValue(byte[] value)
    {
        m_bValue = value;
    }


    /**
     *  This method sets the specified string as instance name of the source
     *  service instance which sends this <code>DataPacket</code>.
     *
     * @param serviceInstance The string to be set as datapacket sender service
     *      instance name.
     * @see #getSrcServiceInstance()
     * @since Tifosi2.0
     */
    public void setSrcServiceInstance(String serviceInstance)
    {
        m_strSrcServiceInstanceName = serviceInstance;
    }


    /**
     *  This method sets the instance ID of the application to which the service
     *  instance that sent this <code>DataPacket</code> object belongs.
     *
     * @param appID The string to be set as source application Instance ID for
     *      this DataPacket
     * @see #getSrcApplicationInstID()
     * @since Tifosi2.0
     */
    public void setSrcApplicationInstID(String appID)
    {
        m_strSrcApplicationInstID = appID;
    }


    /**
     *  This method sets the name of the node on which the service instance that
     *  sent this <code>DataPacket</code> object is running.
     *
     * @param nodeName The string to be set as name of the node, over which is
     *      running the source service instance of this object.
     * @see #getSrcNodeName()
     * @since Tifosi2.0
     */
    public void setSrcNodeName(String nodeName)
    {
        m_strSrcNodeName = nodeName;
    }


    /**
     *  This method sets the channel name over which this <code>DataPacket</code>
     *  object is to be sent.
     *
     * @param port The string to be set as name of the channel
     * @see #getPort()
     * @since Tifosi2.0
     */
    public void setPort(String port)
    {
        m_port = port;
    }


    /**
     *  This method sets the port over which the sender of this <code>DataPacket</code>
     *  is listening for reply.
     *
     * @param port The string to be set as the channel on which this object is
     *      waiting for reply
     * @see #getReplyToPort()
     * @since Tifosi2.0
     */
    public void setReplyToPort(String port)
    {
        m_replyToPort = port;
    }


    /**
     *  This method sets a boolean specifying whether or not this <code>DataPacket</code>
     *  is to be sent over the persistant route.
     *
     * @param isPersistant Boolean specifying whether or not this packet is to
     *      be sent over the persistent route.
     * @see #isPersistant()
     * @since Tifosi2.0
     */
    public void setPersistant(boolean isPersistant)
    {
        m_bisPersistant = isPersistant;
    }


    /**
     *  This method sets the GMS Index for this message.
     *
     * @param gmsIndex The new gMSIndex value
     * @see #getGMSIndex()
     * @since Tifosi2.0
     */
    public void setGMSIndex(long gmsIndex)
    {
        m_tpsGMSIndex = gmsIndex;
    }

    /**
     *  This method sets a boolean specifying whether or not this <code>DataPacket</code>
     *  has been delivered previously.
     *
     * @param flag The boolean specifying whether or not the DataPacket is
     *      redelivered.
     * @see #isRedelivered()
     * @since Tifosi2.0
     */
    public void setRedelivered(boolean flag)
    {
        m_bIsRedelivered = flag;
    }


    /**
     *  This method sets the TTL (time to live) for this message.
     *
     * @param ttl the TTL value
     * @see #getTTL()
     * @since Tifosi2.0
     */
    public void setTTL(long ttl)
    {
        m_lTTL = ttl;
    }


    /**
     *  This method sets all the fieldValues of this <code>DataPacket</code>
     *  object, using the specified XML string.
     *
     * @param xml The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(String xml)
        throws FioranoException
    {
    }


    /**
     *  This method adds the specified object of <code>DmiCarryForwardContext</code>
     *  to this <code>DataPacket</code>.
     *
     * @param context Represents a DmiCarryForwardContext object
     * @since Tifosi2.0
     */
    public void carryForwardContext(DmiCarryForwardContext context)
    {
        if (context == null)
            return;
        Enumeration enu = context.getContexts();

        m_carryFwdContext = new DmiCarryForwardContext();

        m_carryFwdContext.setAppContext(context.getAppContext());
        while (enu.hasMoreElements())
            m_carryFwdContext.addContext((DmiSourceContext) enu.nextElement());

        m_carryFwdContext.setCarryFwdProps(context.getCarryFwdProps());
    }


    /**
     *  The method adds the specified object of <code>DmiSourceContext</code> to
     *  this object of <code>DataPacket</code>.
     *
     * @param sourceContext Object of DmiSourcecontext to be added.
     * @exception FioranoException if unable to add the given DmiSourceContext
     *      information
     * @see #getContext()
     * @since Tifosi2.0
     */
    public void addContext(DmiSourceContext sourceContext)
        throws FioranoException
    {
        if (m_carryFwdContext == null)
        {
            m_carryFwdContext = new DmiCarryForwardContext();
        }

        m_carryFwdContext.addContext(sourceContext);
    }


    /**
     *  This method resets the values of the data members of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_bValue = null;
        m_strSrcServiceInstanceName = null;
        m_port = null;
        m_bisPersistant = false;
        m_replyToPort = null;
        m_strComment = null;
        m_strSysRequest = null;
        m_strCorrelationID = null;
        m_strRunningInstID = null;
        m_requesterInfo = null;
        m_lTTL = -1;

        m_lInTime = -1;
        m_lOutTime = -1;
        m_lTotalTime = -1;
    }

    /**
     *  This method returns this <code>DataPacket</code> object from the
     *  specified input stream object.
     *
     * @param is DataInput object
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException thrown in case of error
     * @since Tifosi2.0
     */
    public void fromStream(java.io.DataInput is, int clientVersionNo)
        throws java.io.IOException
    {
//        System.out.println("Inside fromStream - clientVersionNo ::" + clientVersionNo);
        super.fromStream(is, clientVersionNo);
        m_dDocumentType = is.readShort();

        int temp = is.readInt();

        if (temp == 1)
        {
            int length = is.readInt();

            m_bValue = new byte[length];
            is.readFully(m_bValue);
        }
        m_strSrcServiceInstanceName = readUTF(is);
        m_strSrcApplicationInstID = readUTF(is);
        m_strSrcNodeName = readUTF(is);
        m_port = readUTF(is);
        m_replyToPort = readUTF(is);

        byte persistance = is.readByte();

        m_bisPersistant = (persistance == 1);
        m_tpsGMSIndex = is.readLong();
        m_lTTL = is.readLong();

        if (is.readInt() == 1)
        {
            m_carryFwdContext = new DmiCarryForwardContext();
            m_carryFwdContext.fromStream(is, clientVersionNo);
        }

        m_strWorkflowInstID = readUTF(is);
        m_strUserDefinedDocID = readUTF(is);
        m_strComment = readUTF(is);
        m_strSysRequest = readUTF(is);
        m_strCorrelationID = readUTF(is);
        m_strRunningInstID = readUTF(is);
        temp = is.readInt();
        if (temp == 1)
        {
            m_requesterInfo = new DmiRequesterInfo();
            m_requesterInfo.fromStream(is, clientVersionNo);
        }
        m_bIsRedelivered = is.readBoolean();

        m_lInTime = is.readLong();
        m_lOutTime = is.readLong();
        m_lTotalTime = is.readLong();
        m_lQInsertionTime = is.readLong();

    }

    /**
     *  This method writes this <code>DataPacket</code> object to the specified
     *  output stream object.
     *
     * @param os Data outputstream
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException thrown in case of error while writing
     *      DataPacket on Data outputstream.
     * @since Tifosi2.0
     */
    public void toStream(java.io.DataOutput os, int clientVersionNo)
        throws java.io.IOException
    {
//        System.out.println("Inside toStream - clientVersionNo ::" + clientVersionNo);
        super.toStream(os, clientVersionNo);
        os.writeShort(m_dDocumentType);
        if (m_bValue == null)
        {
            os.writeInt(0);
        }
        else
        {
            os.writeInt(1);
            os.writeInt(m_bValue.length);
            os.write(m_bValue);
        }
        writeUTF(os, m_strSrcServiceInstanceName);
        writeUTF(os, m_strSrcApplicationInstID);
        writeUTF(os, m_strSrcNodeName);
        writeUTF(os, m_port);
        writeUTF(os, m_replyToPort);

        if (m_bisPersistant)
        {
            os.writeByte(1);
        }
        else
        {
            os.writeByte(0);
        }

        os.writeLong(m_tpsGMSIndex);
        os.writeLong(m_lTTL);

        if (m_carryFwdContext != null)
        {
            os.writeInt(1);
            m_carryFwdContext.toStream(os, clientVersionNo);
        }
        else
        {
            os.writeInt(0);
        }

        writeUTF(os, m_strWorkflowInstID);
        writeUTF(os, m_strUserDefinedDocID);
        writeUTF(os, m_strComment);
        writeUTF(os, m_strSysRequest);
        writeUTF(os, m_strCorrelationID);
        writeUTF(os, m_strRunningInstID);
        if (m_requesterInfo != null)
        {
            os.writeInt(1);
            m_requesterInfo.toStream(os, clientVersionNo);
        }
        else
            os.writeInt(0);

        os.writeBoolean(m_bIsRedelivered);

        os.writeLong(m_lInTime);
        os.writeLong(m_lOutTime);
        os.writeLong(m_lTotalTime);
        os.writeLong(m_lQInsertionTime);

    }


    /**
     *  This method tests whether this <code>DataPacket</code> object has the
     *  required(mandatory) fields set. This method should be invoked before
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
     *  This method returns the String representation of this <code>DataPacket</code>
     *  object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("[");
        strBuf.append("Data = ");
        if (m_bValue != null && m_bValue.length > 0)
            strBuf.append(new String(m_bValue));
        else
            strBuf.append("null");
        strBuf.append(", ");
        strBuf.append("Port Name = ");
        strBuf.append(m_port);
        strBuf.append(", ");
        strBuf.append("ServiceInstance = ");
        strBuf.append(m_strSrcServiceInstanceName);
        strBuf.append(", ");
        strBuf.append("ApplicationInstance = ");
        strBuf.append(m_strSrcApplicationInstID);
        strBuf.append(", ");
        strBuf.append("Node Name = ");
        strBuf.append(m_strSrcNodeName);
        strBuf.append(", ");
        strBuf.append("UserDefinedDocID = ");
        strBuf.append(m_strUserDefinedDocID);
        strBuf.append(", ");
        strBuf.append("GMSIndex = ");
        strBuf.append(m_tpsGMSIndex);
        strBuf.append(", ");
        strBuf.append("TTL = ");
        strBuf.append(m_lTTL);

        strBuf.append(", ");
        strBuf.append("InTime = ");
        strBuf.append(m_lInTime);

        strBuf.append(", ");
        strBuf.append("OutTime = ");
        strBuf.append(m_lOutTime);

        strBuf.append(", ");
        strBuf.append("TotalTime = ");
        strBuf.append(m_lTotalTime);
        strBuf.append(", ");
        strBuf.append("CarryForwaredContext = ");
        strBuf.append(m_carryFwdContext);

        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  This method returns a new <code>DataPacket</code> object that has all
     *  fields initialized to values of this <code>DataPacket</code> object.
     *
     * @return DataPacket object
     * @since Tifosi2.0
     */
    public DataPacket copy()
    {
        DataPacket copy = new DataPacket();

        copy.setComment(m_strComment);
        copy.setCorrelationID(m_strCorrelationID);
        copy.setUniqueRunningInstID(m_strRunningInstID);
        copy.setDocumentType(m_dDocumentType);
        copy.setGMSIndex(m_tpsGMSIndex);
        copy.setPersistant(m_bisPersistant);
        copy.setPort(m_port);
        copy.setRedelivered(m_bIsRedelivered);
        copy.setReplyToPort(m_replyToPort);
        copy.setRequesterInfo(m_requesterInfo);
        copy.setSrcApplicationInstID(m_strSrcApplicationInstID);
        copy.setSrcNodeName(m_strSrcNodeName);
        copy.setSrcServiceInstance(m_strSrcServiceInstanceName);
        copy.setSysRequest(m_strSysRequest);
        copy.setUserDefinedDocID(m_strUserDefinedDocID);
        copy.setWorkFlowInstID(m_strWorkflowInstID);
        copy.setTTL(m_lTTL);
        copy.setValue(m_bValue);
        copy.setInTime(m_lInTime);
        copy.setOutTime(m_lOutTime);
        copy.setTotalTime(m_lTotalTime);
        return copy;
    }

    /**
     *  Creates and returns a copy of DataPacket
     *
     * @return copy of the DataPacket
     */
    public DataPacket deepCopy()
    {
        try
        {
            return ((DataPacket) this.clone());
        }
        catch (CloneNotSupportedException ex)
        {
            return this;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /////////////////////// FioranoRecoverable Methods ////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    /**
     *  This is a dummy implementation and hence return true always.
     *
     * @return boolean to indicate success/failure.
     */
    public boolean recover()
    {
        return true;
    }

    /**
     *  This is a dummy implementation and it does nothing.
     */
    public void delete()
    {
        //  Do nothing.
    }
}
