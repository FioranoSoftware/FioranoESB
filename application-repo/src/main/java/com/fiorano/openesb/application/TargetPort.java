/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class TargetPort extends DmiObject
{
    String          m_strTgtPortName;
    String          m_strTgtServInstName;
    String          m_strTgtAppInstName;
    String          m_strTgtNodeName;
    boolean         m_bisP2PTransfer;
    boolean         m_bisRoutePersistent = true;
    long            m_lTTL = 0;


    /**
     *  Gets the tgtPortName attribute of the TargetPort object
     *
     * @return The tgtPortName value
     */
    public String getTgtPortName()
    {
        return m_strTgtPortName;
    }


    /**
     *  Gets the tgtNodeName attribute of the TargetPort object
     *
     * @return The tgtNodeName value
     */
    public String getTgtNodeName()
    {
        return m_strTgtNodeName;
    }


    /**
     *  Gets the tgtServInstName attribute of the TargetPort object
     *
     * @return The tgtServInstName value
     */
    public String getTgtServInstName()
    {
        return m_strTgtServInstName;
    }


    /**
     *  Gets the tgtAppInstName attribute of the TargetPort object
     *
     * @return The tgtAppInstName value
     */
    public String getTgtAppInstName()
    {
        return m_strTgtAppInstName;
    }


    /**
     *  Gets the objectID attribute of the TargetPort object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.TARGET_PORT;
    }

    /**
     *  Gets the p2PTransfer attribute of the TargetPort object
     *
     * @return The p2PTransfer value
     */
    public boolean isP2PTransfer()
    {
        return m_bisP2PTransfer;
    }

    /**
     *  Gets the p2PTransfer attribute of the TargetPort object
     *
     * @return The p2PTransfer value
     */
    public boolean isPersistentRoute()
    {
        return m_bisRoutePersistent;
    }


    /**
     *  Returns the TTL (time to live) for the message that is to be delivered
     *  to this target port.
     *
     * @return the TTL value
     * @since Tifosi2.0
     */
    public long getTTL()
    {
        return m_lTTL;
    }


    /**
     *  Sets the tgtPortName attribute of the TargetPort object
     *
     * @param tgtPortName The new tgtPortName value
     */
    public void setTgtPortName(String tgtPortName)
    {
        m_strTgtPortName = tgtPortName;
    }


    /**
     *  Sets the tgtNodeName attribute of the TargetPort object
     *
     * @param tgtNodeName The new tgtNodeName value
     */
    public void setTgtNodeName(String tgtNodeName)
    {
        m_strTgtNodeName = tgtNodeName;
    }


    /**
     *  Sets the tgtServInstName attribute of the TargetPort object
     *
     * @param tgtServInstName The new tgtServInstName value
     */
    public void setTgtServInstName(String tgtServInstName)
    {
        m_strTgtServInstName = tgtServInstName;
    }


    /**
     *  Sets the tgtAppInstName attribute of the TargetPort object
     *
     * @param tgtAppInstName The new tgtAppInstName value
     */
    public void setTgtAppInstName(String tgtAppInstName)
    {
        m_strTgtAppInstName = tgtAppInstName;
    }


    /**
     *  Sets the p2PTransfer attribute of the TargetPort object
     *
     * @param bool The new p2PTransfer value
     */
    public void setP2PTransfer(boolean bool)
    {
        m_bisP2PTransfer = bool;
    }

    /**
     *  Sets the p2PTransfer attribute of the TargetPort object
     *
     * @param bool The new p2PTransfer value
     */
    public void setPersistentRoute(boolean bool)
    {
        m_bisRoutePersistent = bool;
    }

    /**
     *  Sets specified long as TTL (time to live) for the message that is to be
     *  delivered to this target port.
     *
     * @param ttl the TTL value
     */
    public void setTTL(long ttl)
    {
        m_lTTL = ttl;
    }


    /**
     *  Validates the given DmiObject. Not supported in this version.
     *
     * @exception FioranoException
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  Resets the values of the data members of the object. This might be used
     *  to help the Dmifactory reuse dmi objects. Not supported in this version.
     */
    public void reset()
    {
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
        m_strTgtPortName = readUTF(is);
        m_strTgtServInstName = readUTF(is);
        m_strTgtAppInstName = readUTF(is);
        m_strTgtNodeName = readUTF(is);
        m_bisP2PTransfer = is.readBoolean();
        m_bisRoutePersistent = is.readBoolean();
        m_lTTL = is.readLong();
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
        writeUTF(out, m_strTgtPortName);
        writeUTF(out, m_strTgtServInstName);
        writeUTF(out, m_strTgtAppInstName);
        writeUTF(out, m_strTgtNodeName);
        out.writeBoolean(m_bisP2PTransfer);
        out.writeBoolean(m_bisRoutePersistent);
        out.writeLong(m_lTTL);
    }


    /**
     *  Description of the Method
     *
     * @return Description of the Return Value
     */
    public String toString()
    {
        StringBuffer strbuf = new StringBuffer();

        strbuf.append("[ Target Port Name : " + m_strTgtPortName);
        strbuf.append(", ");
        strbuf.append("Target Service Instance Name : " + m_strTgtServInstName);
        strbuf.append(", ");
        strbuf.append("Target Application Instance Name : " + m_strTgtAppInstName);
        strbuf.append(", ");
        strbuf.append("Target Node Name : " + m_strTgtNodeName);
        strbuf.append(", ");
        strbuf.append("Is P2P Route : " + m_bisP2PTransfer);
        strbuf.append(", ");
        strbuf.append("Is Persitent Route : " + m_bisRoutePersistent);
        strbuf.append(", ");
        strbuf.append("TTL : " + m_lTTL);
        strbuf.append(" ]");
        return strbuf.toString();
    }

    /**
     *  Returns a copy of this object
     *
     * @return Description of the Return Value
     */
    public TargetPort copy()
    {
        TargetPort tgtPort = new TargetPort();

        tgtPort.setP2PTransfer(isP2PTransfer());
        tgtPort.setPersistentRoute(isPersistentRoute());
        tgtPort.setTgtAppInstName(getTgtAppInstName());
        tgtPort.setTgtNodeName(getTgtNodeName());
        tgtPort.setTgtPortName(getTgtPortName());
        tgtPort.setTgtServInstName(getTgtServInstName());
        tgtPort.setTTL(getTTL());
        return tgtPort;
    }

}
