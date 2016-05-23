/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.utils;

import java.io.*;

public class SourceContext implements Serializable
{
    private static final long serialVersionUID = 7823419103284366919L;
    // Stores the service instance name for the source.
    private String  m_strSrcServInstName;

    // Stores the application instance name for the source.
    private String  m_strSrcAppInstName;

    // Stores the node name for the source.
    private String  m_strSrcNodeName;

    // Stores the application instance name for the source.
    private String  m_strSrcAppInstVersion;

    //this flag is used as a check for the old sbw documents which do not contain m_strSrcAppInstVersion.
    private static final String APP_VER_FLAG = "FIORANO_APP_VER_FLAG";

    /**
     *  This constructor constructs an empty <code>SourceContext</code> object.
     *
     * @see #SourceContext(String, String,
     *      String, String)
     * @since Tifosi 2.0
     */
    public SourceContext()
    {
    }

    /**
     *  This constructor constructs an object of the <code>SourceContext</code>
     *  class based on a service instance identified by the <code>srcServInstName<code>
     *  <code>srcAppInstName<code> and <code>srcNodeName</code> arguments.
     *
     * @param srcServInstName The instance name of the data sender tifosi
     *      service instance
     * @param srcAppInstName The application instance name of the application
     *      to which the data sender instance belongs
     * @param srcNodeName The node on which the source instance is running
     * @see #SourceContext()
     * @since Tifosi 2.0
     */
    public SourceContext(String srcServInstName, String srcAppInstName, String srcAppInstVersion, String srcNodeName)
    {
        m_strSrcServInstName = srcServInstName;
        m_strSrcAppInstName = srcAppInstName;
        m_strSrcNodeName = srcNodeName;
        m_strSrcAppInstVersion = srcAppInstVersion;

    }


    /**
     *  This interface method returns the service instance name from this <code>SourceContext</code>
     *  object.
     *
     * @return The instance name of source instance
     * @see #setSrvInstName(String)
     * @since Tifosi 2.0
     */
    public String getSrvInstName()
    {
        return m_strSrcServInstName;
    }


    /**
     *  This interface method returns the instance name of the application to
     *  which the source service instance belongs.
     *
     * @return The instance name of application to which data sender tifosi
     *      service instance belongs.
     * @see #setAppInstName(String)
     * @since Tifosi 2.0
     */
    public String getAppInstName()
    {
        return m_strSrcAppInstName;
    }

    /**
     *  This interface method returns the instance version of the application to
     *  which the source service instance belongs.
     *
     * @return The instance version of application to which data sender tifosi
     *      service instance belongs.
     * @see #setAppInstVersion(String)
     * @since Tifosi 2.0
     */
    public String getAppInstVersion()
    {
        return m_strSrcAppInstVersion;
    }


    /**
     *  This interface method returns the name of the node on which the source
     *  service instance is running.
     *
     * @return The name of node on which the source service instance is
     *      running
     * @see #setNodeName(String)
     * @since Tifosi 2.0
     */
    public String getNodeName()
    {
        return m_strSrcNodeName;
    }

    /**
     *  This interface method sets the service instance name for this <code>SourceContext</code>
     *  object to the specified <code>sourceServiceInstName</code>.
     *
     * @param sourceServiceInstName The instance name of the data sender tifosi
     *      service instance
     * @see #getSrvInstName()
     * @since Tifosi 2.0
     */
    public void setSrvInstName(String sourceServiceInstName)
    {
        m_strSrcServInstName = sourceServiceInstName;
    }


    /**
     *  This interface method sets the instance name of the application to which
     *  the service instance belongs using the specified <code>sourceAppInstName</code>
     *  argument.
     *
     * @param sourceAppInstName The instance name of application to which the
     *      data sender Tifosi service instance belongs.
     * @see #getAppInstName()
     * @since Tifosi 2.0
     */
    public void setAppInstName(String sourceAppInstName)
    {
        m_strSrcAppInstName = sourceAppInstName;
    }

    /**
     *  This interface method sets the instance Version of the application to which
     *  the service instance belongs using the specified <code>sourceAppInstName</code>
     *  argument.
     *
     * @param sourceAppInstVersion The instance Version of application to which the
     *      data sender Tifosi service instance belongs.
     * @see #getAppInstVersion()
     * @since Tifosi 2.0
     */
    public void setAppInstVersion(String sourceAppInstVersion)
    {
        m_strSrcAppInstVersion = sourceAppInstVersion;
    }


    /**
     *  This interface method sets the name of the node on which the Source
     *  service instance is running to the specified <code>nodeName</code>
     *  argument.
     *
     * @param nodeName The name of node on which the source service instance is
     *      running
     * @see #getNodeName()
     * @since Tifosi 2.0
     */
    public void setNodeName(String nodeName)
    {
        m_strSrcNodeName = nodeName;
    }

    /**
     *  checks for equality of two SourceContexts.
     *
     * @param obj Passed object
     * @return if passed object equals invoking object
     */
    public boolean equals(Object obj)
    {
        if (!(obj instanceof SourceContext))
            return false;

        SourceContext srcContext = (SourceContext) obj;

        if (checkStringEquality(srcContext.getAppInstName(), getAppInstName())
            && checkStringEquality(srcContext.getNodeName(), getNodeName())
            && checkStringEquality(srcContext.getSrvInstName(), getSrvInstName()))
            return true;
        else
            return false;
    }

    /**
     * @return
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
        strBuf.append("source service instance = ");
        strBuf.append(m_strSrcServInstName);
        strBuf.append(", ");
        strBuf.append("source node name = ");
        strBuf.append(m_strSrcNodeName);
        strBuf.append(", ");
        strBuf.append("source application instance Version = ");
        strBuf.append(m_strSrcAppInstVersion);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  check if two string are equal
     *
     * @param str1 first string.
     * @param str2 second string.
     * @return return true if strings are equal.
     */
    private boolean checkStringEquality(String str1, String str2)
    {
        if (str1 == null && str2 == null)
            return true;

        if ((str1 != null && str2 == null) || (str1 == null && str2 != null))
            return false;

        if (str1.equalsIgnoreCase(str2))
            return true;
        else
            return false;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        //[Dashboard]Source Context not showing properly for migrated sbw documents.
        //As we have added a new field m_strSrcAppInstVersion for multiple app version implementation, there is a problem while reading the old
        //sbw documents which do not contain this filed. So adding a check to know if the documents are prior to app version implementation.
        out.writeObject(APP_VER_FLAG);
        out.writeObject(m_strSrcServInstName);
        out.writeObject(m_strSrcAppInstName);
        out.writeObject(m_strSrcNodeName);
        out.writeObject(m_strSrcAppInstVersion);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String identifier = (String)in.readObject();
        if(identifier.equals(APP_VER_FLAG)){
            m_strSrcServInstName = (String)in.readObject();
        }else {
            m_strSrcServInstName = identifier;
        }
        m_strSrcAppInstName = (String)in.readObject();
        m_strSrcNodeName = (String)in.readObject();
        if(identifier.equals(APP_VER_FLAG)){
            m_strSrcAppInstVersion = (String)in.readObject();
        }

    }
}
