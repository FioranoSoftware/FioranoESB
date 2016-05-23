/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public class ApsLogModule extends DmiObject
{
    String          m_strModuleName;
    String          m_iTraceLevel;

    /**
     *  This is called to construct object of <code>ApsLogModule</code>
     *
     * @since Tifosi2.0
     */
    public ApsLogModule()
    {
    }

    /**
     *  This method gets the name of service module represented
     *  by this object of <code>ApsLogModule</code>.
     *
     * @return The name of this module
     * @see #setName(String)
     * @since Tifosi2.0
     */
    public String getName()
    {
        return m_strModuleName;
    }

    /**
     *  This method gets the tracelevel for the service module
     *  represented by this object of <code>ApsLogModule</code>
     *
     * @return The traceLevel value
     * @see #setTraceLevel(int)
     * @since Tifosi2.0
     */
    public String getTraceLevel()
    {
        return m_iTraceLevel;
    }

    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APS_LOG_MODULE;
    }

    /**
     *  This method sets the specified string as name of service
     *  module, represented by this object of <code>ApsLogModule</code>.
     *
     * @param moduleName The string to be set as name of this module
     * @see #getName()
     * @since Tifosi2.0
     */
    public void setName(String moduleName)
    {
        m_strModuleName = moduleName;
    }

    /**
     *  This method sets the specified integer as tracelevel for
     *  the service module, represented by this object of <code>ApsLogModule</code>
     *
     * @param traceLevel integer to be set as traceLevel for this module.
     * @see #getTraceLevel()
     * @since Tifosi2.0
     */
    public void setTraceLevel(String traceLevel)
    {
        m_iTraceLevel = traceLevel;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>ApsLogModule</code>, using the specified XML string.
     *
     * @param moduleElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element moduleElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element moduleElement = doc.getDocumentElement();
        m_iTraceLevel = moduleElement.getAttribute("traceLevel");
        m_strModuleName = moduleElement.getAttribute("name");
        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.MODULE))
        {
            Hashtable attributes = parser.getAttributes();

            m_iTraceLevel = (String)attributes.get(APSConstants.TRACE_LEVEL);//moduleElement.getAttribute("traceLevel");
            m_strModuleName = (String)attributes.get(APSConstants.MODULE_NAME);//moduleElement.getAttribute("name");
            validate();
        }
    }

    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method is called to write this object of <code>ApsLogModule</code>
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
        writeUTF(out, m_strModuleName);
        writeUTF(out, m_iTraceLevel);
    }

    /**
     *  This is called to read this object <code>ApsLogModule</code> from the
     *  specified object of input stream.
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
        m_strModuleName = readUTF(is);
        m_iTraceLevel = readUTF(is);

        if (m_iTraceLevel == null)
            m_iTraceLevel = "INFO";
    }


    /**
     *  This method tests whether this object of <code>ApsLogModule</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strModuleName == null || m_strModuleName.equalsIgnoreCase(""))
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>ApsLogModule</code>.
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
        strBuf.append("ApsLogModule Details ");
        strBuf.append("[");
        strBuf.append("Name = ");
        strBuf.append(m_strModuleName);
        strBuf.append(", ");
        strBuf.append("Trace Level = ");
        strBuf.append(String.valueOf(m_iTraceLevel));
        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("Module");

        ((Element) root0).setAttribute("name", m_strModuleName);
        ((Element) root0).setAttribute("traceLevel", String.valueOf(m_iTraceLevel));

        return root0;
    }
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Module
        writer.writeStartElement("Module");

        writer.writeAttribute("name", m_strModuleName);
        writer.writeAttribute("traceLevel", String.valueOf(m_iTraceLevel));

        //End Module
        writer.writeEndElement();

    }
}
