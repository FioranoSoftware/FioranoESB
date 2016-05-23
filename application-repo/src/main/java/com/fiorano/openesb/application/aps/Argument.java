/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public class Argument extends DmiObject
{
    String          m_name;
    String          m_value;
    boolean         m_isAdvanced;
    boolean         m_isRequired;
    String          m_contentValue;
    boolean         m_isInMemorySupported;


    /**
     *  This is called to construct object of <code>Argument</code>
     *
     * @since Tifosi2.0
     */
    public Argument()
    {
        m_isAdvanced = false;
        m_isRequired = true;
        m_contentValue = "text";
        m_isInMemorySupported = true;
    }


    /**
     *  This method gets the "name" of runtime argument,
     *  specified by this object of <code>Argument</code>
     *
     * @return The name of runtime argument.
     * @see #setName(String)
     * @since Tifosi2.0
     */
    public String getName()
    {
        return m_name;
    }


    /**
     *  This method gets the value of runtime argument to
     *  be specified by this object of <code>Argument</code>
     *
     * @return value of runtime argument.
     * @see #setValue(String)
     * @since Tifosi2.0
     */
    public String getValue()
    {
        return m_value;
    }


    /**
     *  This method checks if this object of <code>Argument</code>
     *  contains information to be handled by the service instance
     *  independently.
     *
     * @return true if this object contains information to be handled by
     *      service instance, false otherwise.
     * @see #setIsAdvanced(boolean)
     * @since Tifosi2.0
     */
    public boolean getIsAdvanced()
    {
        return m_isAdvanced;
    }


    /**
     *  This method checks whether specification of this
     *  runtime argument at the time of service instance launch, is compulsory
     *  or not.
     *
     * @return true if this object is required, false otherwise.
     * @see #setIsRequired(boolean)
     * @since Tifosi2.0
     */
    public boolean getIsRequired()
    {
        return m_isRequired;
    }

    /**
     * This value determines whetehr this argument is enabled when the component is launched
     * in-memory.
     * @return boolean
     */
    public boolean isInMemorySupported()
    {
        return m_isInMemorySupported;
    }


    /**
     *  This method gets the content of the runtime
     *  argument, specified by this object of <code>Argument</code>.
     *
     * @return the content of this object
     * @see #setContentValue(String)
     * @since Tifosi2.0
     */
    public String getContentValue()
    {
        return m_contentValue;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.ARGUMENT;
    }


    /**
     *  This method sets the specified string as the name
     *  for the runtime argument, specified by this object of <code>Argument</code>
     *  .
     *
     * @param name the string to be set as name of runtime argument.
     * @see #getName()
     * @since Tifosi2.0
     */
    public void setName(String name)
    {
        m_name = name;
    }


    /**
     *  This method sets the "value" of runtime argument,
     *  specified by this object of <code>Argument</code>
     *
     * @param value string to be set as "value" for this runtime argument.
     * @see #getValue()
     * @since Tifosi2.0
     */
    public void setValue(String value)
    {
        m_value = value;
    }


    /**
     *  This method sets whether this object of <code>Argument</code>
     *  contains information to be handled by the service instance
     *  independently.
     *
     * @param isAdvanced boolean specifying if this object contains some
     *      information to be handled by service instance
     * @see #getIsAdvanced()
     * @since Tifosi2.0
     */
    public void setIsAdvanced(boolean isAdvanced)
    {
        m_isAdvanced = isAdvanced;
    }


    /**
     *  This method sets whether specification of this
     *  object of <code>Argument</code> at runtime for service instance launch,
     *  is compulsory or not.
     *
     * @param isRequired boolean specifying whether this runtime argument is
     *      compulsory or not.
     * @see #getIsRequired()
     * @since Tifosi2.0
     */
    public void setIsRequired(boolean isRequired)
    {
        m_isRequired = isRequired;
    }

    /**
     * Sets whether this argument is enabled when the component is launched in-memory.
     * @param inMemorySupported
     */
    public void setInMemorySupported(boolean inMemorySupported)
    {
       m_isInMemorySupported = inMemorySupported;
    }


    /**
     *  This method sets the specified string as content
     *  for the runtime argument, specified by this object of <code>Argument</code>
     *  .
     *
     * @param contentValue String to be set as content for this object.
     * @see #getContentValue()
     * @since Tifosi2.0
     */
    public void setContentValue(String contentValue)
    {
        m_contentValue = contentValue;
    }


    /**
     *  This method sets all the fieldValues of this
     *  object of <code>Argument</code>, using the specified XML string.
     *
     * @param argElement
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element argElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element argElement = doc.getDocumentElement();

        if (argElement != null)
        {
            String name = argElement.getAttribute("name");
            setName(name);

            boolean isAdvanced = XMLDmiUtil.getAttributeAsBoolean(argElement, "isAdvanced");
            setIsAdvanced(isAdvanced);

            boolean isRequired = XMLDmiUtil.getAttributeAsBoolean(argElement, "isRequired");
            setIsRequired(isRequired);

            boolean isInMemorySupported = XMLDmiUtil.getAttributeAsBoolean(argElement, "isInMemorySupported", m_isInMemorySupported);
            setInMemorySupported(isInMemorySupported);

            String content = argElement.getAttribute("content");
            setContentValue(content);
            setValue(XMLUtils.getNodeValueAsString(argElement));
        }

        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException , FioranoException
    {

        if ( parser.markCursor(APSConstants.ARGUMENT) )
        {

            // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = parser.getAttributes();

            String name = (String)attributes.get(APSConstants.ARGUMENT_NAME);//argElement.getAttribute("name");
            setName(name);

            boolean isAdvanced = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_ADVANCED));//XMLDmiUtil.getAttributeAsBoolean(argElement, "isAdvanced");
            setIsAdvanced(isAdvanced);

            boolean isRequired = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_REQUIRED));//XMLDmiUtil.getAttributeAsBoolean(argElement, "isRequired");
            setIsRequired(isRequired);

            if(attributes.get(APSConstants.IS_IN_MEMORY_SUPPORTED) != null)
            {
                boolean isInMemorySupported = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_IN_MEMORY_SUPPORTED));//XMLDmiUtil.getAttributeAsBoolean(argElement, "isRequired");
                setInMemorySupported(isInMemorySupported);
            }

            String content = (String)attributes.get(APSConstants.CONTENT);//argElement.getAttribute("content");
            setContentValue(content);
            String nodeValue = parser.getCData();
            setValue(nodeValue);

            attributes.clear();
            attributes = null;
            nodeValue = null;
        }
        validate();

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
     *  This method tests whether this object of <code>Argument</code> has the
     *  required(mandatory) fields set, before inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_name == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_value == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  This method writes this object of <code>Argument</code> to
     *  the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        UTFReaderWriter.writeUTF(out, m_name);
        UTFReaderWriter.writeUTF(out, m_value);

        out.writeBoolean(m_isAdvanced);
        out.writeBoolean(m_isRequired);
        out.writeBoolean(m_isInMemorySupported);
        UTFReaderWriter.writeUTF(out, m_contentValue);
    }


    /**
     *  This method reads this object <code>Argument</code> from the
     *  specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_name = UTFReaderWriter.readUTF(is);
        m_value = UTFReaderWriter.readUTF(is);

        m_isAdvanced = is.readBoolean();
        m_isRequired = is.readBoolean();
        m_isInMemorySupported = is.readBoolean();
        m_contentValue = UTFReaderWriter.readUTF(is);
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>Argument</code>
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
        strBuf.append("Argument ");
        strBuf.append("[");
        strBuf.append("Name = ");
        strBuf.append(m_name);
        strBuf.append(", ");
        strBuf.append("Value = ");
        strBuf.append(m_value);
        strBuf.append(", ");
        strBuf.append("Advanced = ");
        strBuf.append(m_isAdvanced);
        strBuf.append(", ");
        strBuf.append("Required = ");
        strBuf.append(m_isRequired);
        strBuf.append(", ");
        strBuf.append("IsInMemorySupported = ");
        strBuf.append(m_isInMemorySupported);
        strBuf.append(", ");
        strBuf.append("Content Value = ");
        strBuf.append(m_contentValue);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Argument");

        if (m_name != null)
        {
            ((Element) root0).setAttribute("name", m_name);
        }
        else
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        ((Element) root0).setAttribute("isAdvanced", "" + m_isAdvanced);
        ((Element) root0).setAttribute("isRequired", "" + m_isRequired);
        ((Element) root0).setAttribute("isInMemorySupported", "" + m_isInMemorySupported);

        if (m_contentValue != null && !m_contentValue.equals(""))
        {
            ((Element) root0).setAttribute("content", m_contentValue);
        }

        if (m_value != null && !m_value.equals(""))
        {
            Node pcData = document.createCDATASection(m_value);

            root0.appendChild(pcData);
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException,FioranoException
    {
        //Start Argument
        writer.writeStartElement("Argument");

        if (m_name != null)
        {
            writer.writeAttribute("name", m_name);
        }
        else
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        writer.writeAttribute("isAdvanced", "" + m_isAdvanced);
        writer.writeAttribute("isRequired", "" + m_isRequired);
        writer.writeAttribute("isInMemorySupported", "" + m_isInMemorySupported);

        if (m_contentValue != null && !m_contentValue.equals(""))
        {
            writer.writeAttribute("content", m_contentValue);
        }

        if (m_value != null && !m_value.equals(""))
        {
            writer.writeCData(m_value);

        }

        //End Argument
        writer.writeEndElement();

    }
}
