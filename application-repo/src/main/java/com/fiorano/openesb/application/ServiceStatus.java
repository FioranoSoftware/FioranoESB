/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;


import com.fiorano.openesb.utils.exception.FioranoException;

public class ServiceStatus extends DmiObject
{
    private String  m_strServInstName;
    private String  m_strAppInstName;
    private String  m_strNodeName;
    private boolean m_isApplicationRunning;

    /**
     *  Returns the service instance name for which the service status is specified
     * by this object
     *
     * @return the name of the service instance
     */
    public String getServInstName()
    {
        return m_strServInstName;
    }

    /**
     * Returns the application instance name for which the service status is specified
     *
     * @return the name of the application
     */
    public String getAppInstName()
    {
        return m_strAppInstName;
    }

    /**
     * Returns the node name on which the service is running
     *
     * @return The name of the node
     */
    public String getNodeName()
    {
        return m_strNodeName;
    }

    /**
     *  Checks whether the application is running or not
     *
     * @return true if the application is running, false otherwise
     */
    public boolean getIsApplicationRunning()
    {
        return m_isApplicationRunning;
    }

    /**
     *  Gets the objectID attribute of the AckPacket object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_STATUS;
    }


    /**
     *  Gets a unique name for this service status
     *
     * @return the name for this service status
     */
    public String getName()
    {
        return m_strAppInstName + " :: " + m_strServInstName;
    }


    /**
     *  Sets the service instance name for which the service status is to be set
     *
     * @param servInstName the name of the service instance
     */
    public void setServInstName(String servInstName)
    {
        m_strServInstName = servInstName;
    }


    /**
     * Sets the application instance name for which the service status is to be set
     *
     * @param appInstName the name of the application
     */
    public void setAppInstName(String appInstName)
    {
        m_strAppInstName = appInstName;
    }


    /**
     * Sets the node name on which the service is running
     *
     * @param nodeName the name of the node
     */
    public void setNodeName(String nodeName)
    {
        m_strNodeName = nodeName;
    }

    /**
     *  Sets the boolean indicating whether the application is running or not
     *
     * @param applicationState boolean indicating whether the application is running or not
     */
    public void setIsApplicationRunning(boolean applicationState)
    {
        m_isApplicationRunning = applicationState;
    }


    /**
     *  Validates the object for the correctness
     *
     * @exception FioranoException Exception if the object is not valid
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  Resets the values of the field to the default values
     */
    public void reset()
    {
    }


    /**
     *  Writes the value of this object to the output stream
     *
     * @param os The output stream on which the object is to be written
     * @param versionNo
     * @exception java.io.IOException Exception, if there is some error in writing the data on the stream
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);
        writeUTF(os, m_strServInstName);
        writeUTF(os, m_strAppInstName);
        writeUTF(os, m_strNodeName);
        os.writeBoolean(m_isApplicationRunning);
    }


    /**
     *  Reads the input stream and sets the values of the fields of the object
     *
     * @param is Input stream from which the data is to be read
     * @param versionNo
     * @exception java.io.IOException Exception if there us some problem in
     * reading the data from the stream.
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);

        m_strServInstName = readUTF(is);
        m_strAppInstName = readUTF(is);
        m_strNodeName = readUTF(is);
        m_isApplicationRunning = is.readBoolean();
    }

}
