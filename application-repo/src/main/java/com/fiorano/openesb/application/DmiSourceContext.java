/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.DmiEqualsUtil;

public class DmiSourceContext extends DmiObject
{
    private String  m_strSrcServInstName;
    private String  m_strSrcAppInstName;
    private String  m_strSrcAppInstVersion;
    private String  m_strSrcNodeName;

    /**
     *  Constructor for the <code>DmiSourceContext</code> object.
     *
     * @since Tifosi2.0
     */
    public DmiSourceContext()
    {
    }

    /**
     *  Constructor for the <code>DmiSourceContext</code> object, given the
     *  source service instance name, source application instance name and
     *  source node name.
     *
     * @param srcServInstName source service instance name
     * @param srcAppInstName application instance name to which belongs the
     *      source service instance.
     * @param srcNodeName node on which source service instance is running.
     * @since Tifosi2.0
     */
    public DmiSourceContext(String srcServInstName, String srcAppInstName, String srcAppInstVersion, String srcNodeName)
    {
        m_strSrcServInstName = srcServInstName;
        m_strSrcAppInstName = srcAppInstName;
        m_strSrcAppInstVersion = srcAppInstVersion;
        m_strSrcNodeName = srcNodeName;
    }

    /**
     *  Gets the instance name of source service instance, for this <code>DmiSourceContext</code>
     *  object.
     *
     * @return The source service instance name
     * @see #setSrvInstName(String)
     * @since Tifosi2.0
     */

    public String getSrvInstName()
    {
        return m_strSrcServInstName;
    }

    /**
     *  Gets the application instance name that contains the source service
     *  instance of this <code>DmiSourceContext</code> object.
     *
     * @return Instance name of the application to which belongs the source
     *      service instance.
     * @see #setAppInstName(String)
     * @since Tifosi2.0
     */

    public String getAppInstName()
    {
        return m_strSrcAppInstName;
    }

    /**
     *  Gets the application instance version that contains the source service
     *  instance of this <code>DmiSourceContext</code> object.
     *
     * @return Instance version of the application to which belongs the source
     *      service instance.
     * @see #setAppInstVersion(String)
     * @since Tifosi2.0
     */

    public String getAppInstVersion()
    {
        return m_strSrcAppInstVersion;
    }

    /**
     *  Gets the node name over which the source service instance of this <code>DmiSourceContext</code>
     *  object is running.
     *
     * @return The name of node over which source service instance is running.
     * @see #setNodeName(String)
     * @since Tifosi2.0
     */

    public String getNodeName()
    {
        return m_strSrcNodeName;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SOURCE_CONTEXT;
    }

    /**
     *  Sets the specified string as instance name of the source service
     *  instance, for this <code>DmiSourceContext</code> object.
     *
     * @param sourceServiceInstName String to be set as source service instance
     *      name
     * @see #getSrvInstName()
     * @since Tifosi2.0
     */
    public void setSrvInstName(String sourceServiceInstName)
    {
        m_strSrcServInstName = sourceServiceInstName;
    }

    /**
     *  Sets the application instance name that contains the source service
     *  instance of this <code>DmiSourceContext</code> object.
     *
     * @param sourceAppInstName The new sourceAppInstName value
     * @see #getAppInstName()
     * @since Tifosi2.0
     */

    public void setAppInstName(String sourceAppInstName)
    {
        m_strSrcAppInstName = sourceAppInstName;
    }

    /**
     *  Sets the application instance Version that contains the source service
     *  instance of this <code>DmiSourceContext</code> object.
     *
     * @param sourceAppInstVersion The new sourceAppInstName value
     * @see #getAppInstVersion()
     * @since Tifosi2.0
     */

    public void setAppInstVersion(String sourceAppInstVersion)
    {
        m_strSrcAppInstVersion = sourceAppInstVersion;
    }

    /**
     *  Sets the node name on which the source service instance of this <code>DmiSourceContext</code>
     *  object is running.
     *
     * @param nodeName The string to be set as name of the node over which
     *      source service is running
     * @see #getNodeName()
     * @since Tifosi2.0
     */

    public void setNodeName(String nodeName)
    {
        m_strSrcNodeName = nodeName;
    }

    /**
     *  This method resets the source context information held by this <code>DmiSourceContext</code>
     *  object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_strSrcServInstName = null;
        m_strSrcAppInstName = null;
        m_strSrcAppInstVersion = null;
        m_strSrcNodeName = null;
    }

    /**
     *  This method tests whether this <code>DmiSourceContext</code> object has
     *  the required(mandatory) fields set. It should be invoked before
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

        m_strSrcServInstName = readUTF(is);
        m_strSrcAppInstName = readUTF(is);
        m_strSrcAppInstVersion = readUTF(is);
        m_strSrcNodeName = readUTF(is);
    }

    /**
     *  Description of the Method
     *
     * @param os Description of the Parameter
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void toStream(java.io.DataOutput os, int clientVersionNo)
        throws java.io.IOException
    {
        super.toStream(os, clientVersionNo);
        writeUTF(os, m_strSrcServInstName);
        writeUTF(os, m_strSrcAppInstName);
        writeUTF(os, m_strSrcAppInstVersion);
        writeUTF(os, m_strSrcNodeName);
    }

    /**
     *  This method check the equality of the given object with this object. All
     *  the fields of the object are verified.
     *
     * @param obj The object whose equality is to be checked
     * @return true is the passed object is same as this object else false
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof DmiSourceContext))
            return false;

        DmiSourceContext srcContext = (DmiSourceContext) obj;

        if (DmiEqualsUtil.checkStringEquality(srcContext.getAppInstName(), getAppInstName())
            && DmiEqualsUtil.checkStringEquality(srcContext.getNodeName(), getNodeName())
            && DmiEqualsUtil.checkStringEquality(srcContext.getSrvInstName(), getSrvInstName()))
            return true;
        else
            return false;
    }

    /**
     *  This method returns the String representation of this <code>DmiSourceContext</code>
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
        strBuf.append("DMI source context Details ");
        strBuf.append("[");
        strBuf.append("source application instance name = ");
        strBuf.append(m_strSrcAppInstName);
        strBuf.append(", ");
        strBuf.append("source application instance version = ");
        strBuf.append(m_strSrcAppInstVersion);
        strBuf.append(", ");
        strBuf.append("source service instance = ");
        strBuf.append(m_strSrcServInstName);
        strBuf.append(", ");
        strBuf.append("source node name = ");
        strBuf.append(m_strSrcNodeName);
        strBuf.append("]");
        return strBuf.toString();
    }
}
