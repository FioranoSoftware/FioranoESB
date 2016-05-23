/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MonitorableModule extends DmiObject
{
    int             m_iMaxTraceLevel;
    int             m_iDefaultTraceLevel;
    String          m_strModuleName;


    /**
     *  Constructor for the MonitorableModule object
     *
     * @since Tifosi2.0
     */
    public MonitorableModule()
    {
        reset();
    }


    /**
     *  This interface method is called to get the name of module, for which this object
     *  of <code>MonitorableModule</code> provides monitoring information.
     *
     * @return The name of monitoring module
     * @see #setModuleName(String)
     * @since Tifosi2.0
     */
    public String getModuleName()
    {
        return m_strModuleName;
    }


    /**
     *  This interface method is called to get the maximum trace level for module,
     *  for which this object of <code>MonitorableModule</code> provides monitoring
     *  information.
     *
     * @return The maximum traceLevel for module.
     * @see #setMaxTraceLevel(int)
     * @since Tifosi2.0
     */
    public int getMaxTraceLevel()
    {
        return m_iMaxTraceLevel;
    }

    /**
     *  This interface method is called to get the default trace level for module,
     *  for which this object of <code>MonitorableModule</code> provides monitoring
     *  information.
     *
     * @return The default traceLevel for module.
     * @see #setDefaultTraceLevel(int)
     * @since Tifosi2.0
     */
    public int getDefaultTraceLevel()
    {
        return m_iDefaultTraceLevel;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.MONITORABLEMODULE;
    }


    /**
     *  This interface method is called to set the specified string as name of module,
     *  for which this object of <code>MonitorableModule</code> provides
     *  monitoring information.
     *
     * @param moduleName The string to be set as name of monitoring module.
     * @see #getModuleName()
     * @since Tifosi2.0
     */
    public void setModuleName(String moduleName)
    {
        m_strModuleName = moduleName;
    }


    /**
     *  This interface method is called to set the specified integer as maximum trace
     *  level for module, for which this object of <code>MonitorableModule</code>
     *  provides monitoring information.
     *
     * @param maxTraceLevel The integer to be set as maximum traceLevel
     * @see #getMaxTraceLevel()
     * @since Tifosi2.0
     */
    public void setMaxTraceLevel(int maxTraceLevel)
    {
        m_iMaxTraceLevel = maxTraceLevel;
    }

    /**
     *  This interface method is called to set the specified integer as default trace
     *  level for module, for which this object of <code>MonitorableModule</code>
     *  provides monitoring information.
     *
     * @param defaultTraceLevel The integer to be set as default traceLevel
     * @see #getDefaultTraceLevel()
     * @since Tifosi2.0
     */

    public void setDefaultTraceLevel(int defaultTraceLevel)
    {
        m_iDefaultTraceLevel = defaultTraceLevel;
    }


    /**
     *  This interface method is called to set all the fieldValues of this
     *  object of <code>MonitorableModule</code>, using the specified XML
     *  string.
     *
     * @param module
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element module)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element module = doc.getDocumentElement();
        if (module != null)
        {
            m_iMaxTraceLevel = Integer.parseInt(module.getAttribute("maxTraceLevel"));
            if (module.getAttribute("defaultTraceLevel") != null && !module.getAttribute("defaultTraceLevel").equalsIgnoreCase(""))
                m_iDefaultTraceLevel = Integer.parseInt(module.getAttribute("defaultTraceLevel"));
            m_strModuleName = module.getAttribute("name");
        }
        validate();
    }

    /**
     *  This method tests whether this object of <code>MonitorableModule</code>
     *  has the required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strModuleName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method is called to write this object of <code>MonitorableModule</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeInt(m_iMaxTraceLevel);
        out.writeInt(m_iDefaultTraceLevel);
        writeUTF(out, m_strModuleName);
    }


    /**
     *  This is called to read this object <code>MonitorableModule</code> from
     *  the specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_iMaxTraceLevel = is.readInt();
        m_iDefaultTraceLevel = is.readInt();
        m_strModuleName = readUTF(is);
    }


    /**
     *  This utility method is used to compare this object of <code>MonitorableModule</code>
     *  with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof MonitorableModule))
        {
            return false;
        }

        MonitorableModule rcvObj = (MonitorableModule) obj;

        if ((rcvObj.getMaxTraceLevel() == m_iMaxTraceLevel) && (rcvObj.getDefaultTraceLevel() == m_iDefaultTraceLevel)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getModuleName(), m_strModuleName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>MonitorableModule</code>.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Monitorablemodule Details ");
        strBuf.append("[");
        strBuf.append("Default Trace Level = ");
        strBuf.append(String.valueOf(m_iDefaultTraceLevel));
        strBuf.append(", ");
        strBuf.append("Maximum trace level = ");
        strBuf.append(String.valueOf(m_iMaxTraceLevel));
        strBuf.append(", ");
        strBuf.append("Module Name = ");
        strBuf.append(m_strModuleName);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Retruns the xml string equivalent of this object
     *
     * @param document instance of Xml Document.
     * @return org.w3c.dom.Node
     * @exception FioranoException thrown in case of error.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Module");

        ((Element) root0).setAttribute("name", m_strModuleName);
        ((Element) root0).setAttribute("maxTraceLevel", String.valueOf(m_iMaxTraceLevel));
        ((Element) root0).setAttribute("defaultTraceLevel", String.valueOf(m_iDefaultTraceLevel));
        return root0;
    }
}
