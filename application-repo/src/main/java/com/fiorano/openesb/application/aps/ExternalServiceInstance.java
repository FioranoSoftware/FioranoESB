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
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ExternalServiceInstance extends DmiObject
{
    private String          m_instName;

    private String          m_appGUID;

    private String          m_actualInstName;

    private String          m_appVersionNo = "1.0";

    /**
     *  This is called to construct object of <code>ExternalServiceInstance</code>
     */
    public ExternalServiceInstance()
    {
    }

    /**
     *  This method gets the service instance name of
     *  externally launched service instance, from this object of <code>ExternalServiceInstance</code>
     *  . This instance name is used to refer this service instance in the
     *  application, in which it is set to be externally launched.
     *
     * @return The service instance name for this object.
     * @see #setInstanceName(String)
     * @since Tifosi2.0
     */
    public String getInstanceName()
    {
        return m_instName;
    }

    /**
     *  This method gets the actual instance name, by
     *  which this externally launched service instance represented by this
     *  object of <code>ExternalServiceInstance</code>, is referred in the
     *  application in which it is set as internal service instance.
     *
     * @return The actual service instance name for this object
     * @see #setActualInstanceName(String)
     * @since Tifosi2.0
     */
    public String getActualInstanceName()
    {
        return m_actualInstName;
    }


    /**
     *  This method gets the GUID of application, to which
     *  the externally launched service instance represented by this object of
     *  <code>ExternalServivcInstance</code>, belongs.
     *
     * @return The GUID of application for this object
     * @see #setApplicationGUID(String)
     * @since Tifosi2.0
     */
    public String getApplicationGUID()
    {
        return m_appGUID;
    }

    /**
     *  This method gets the version of application, to which
     *  the externally launched service instance represented by this object of
     *  <code>ExternalServivcInstance</code>, belongs.
     *
     * @return The GUID of application for this object
     * @see #setApplicationGUID(String)
     * @since Tifosi2.0
     */
    public String getApplicationVersion()
    {
        return m_appVersionNo;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.EXT_SERVICE_INSTANCE;
    }

    /**
     *  This method sets the specified string as
     *  service instance name of externally launched service instance, in this
     *  object of <code>ExternalServiceInstance</code>. This instance name is
     *  used to refer this service instance in the application, in which it is
     *  set to be externally launched.
     *
     * @param instName The string to be set as service instance name.
     * @see #getInstanceName()
     * @since Tifosi2.0
     */
    public void setInstanceName(String instName)
    {
        m_instName = instName;
    }

    /**
     *  This method sets the specified string as the
     *  actual instance name, by which this externally launched service instance
     *  represented by this object of <code>ExternalServiceInstance</code>, is
     *  referred in the application in which it is set as internal service
     *  instance.
     *
     * @param actualInstName The string to be set as actual service instance
     *      name for this object
     * @see #getActualInstanceName()
     * @since Tifosi2.0
     */
    public void setActualInstanceName(String actualInstName)
    {
        m_actualInstName = actualInstName;
    }

    /**
     *  This method sets the specified string as GUID for
     *  application, to which the externally launched service instance
     *  represented by this object of <code>ExternalServivcInstance</code>,
     *  belongs.
     *
     * @param appGUID The string to be set as GUID of application for this
     *      object
     * @see #getApplicationGUID()
     * @since Tifosi2.0
     */
    public void setApplicationGUID(String appGUID)
    {
        m_appGUID = appGUID;
    }


    /**
     *  This method sets the specified string as version for
     *  application, to which the externally launched service instance
     *  represented by this object of <code>ExternalServivcInstance</code>,
     *  belongs.
     *
     * @param version The string to be set as fersion of application for this
     *      object
     * @see #getApplicationGUID()
     * @since Tifosi2.0
     */
    public void setApplicationVersion(String version)
    {
        m_appVersionNo = version;
    }


    /**
     *  This method sets all the fieldValues of this
     *  object of <code>ExternalServiceInstance</code>, using the specified XML
     *  string.
     *
     * @param extInstance
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element extInstance)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element extInstance = doc.getDocumentElement();

        if (extInstance != null)
        {
            NodeList list = extInstance.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("InstanceName"))
                {
                    setInstanceName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("ActualInstanceName"))
                {
                    setActualInstanceName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("ApplicationGUID"))
                {
                    setApplicationGUID(XMLUtils.getNodeValueAsString(child));
                }
                else if(nodeName.equalsIgnoreCase("ApplicationVersion"))
                {
                    setApplicationVersion(XMLUtils.getNodeValueAsString(child));
                }
            }
        }

        validate();
    }


    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.EXTERNAL_SERVICE_INSTANCE ))
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                
               if (nodeName.equalsIgnoreCase("InstanceName"))
                {
                    String value = parser.getText();
                    setInstanceName(value);
                }
                else if (nodeName.equalsIgnoreCase("ActualInstanceName"))
                {
                    String value = parser.getText();
                    setActualInstanceName(value);
                }
                else if (nodeName.equalsIgnoreCase("ApplicationGUID"))
                {
                    String value = parser.getText();
                    setApplicationGUID(value);
                }
                else if(nodeName.equalsIgnoreCase("ApplicationVersion"))
                {
                    String value = parser.getText();
                    setApplicationVersion(value);
                }
            }
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
     *  This method tests whether this object of <code>ExternalServiceInstance</code>
     *  has the required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_appGUID == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_instName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_actualInstName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if(m_appVersionNo == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }

    /**
     *  This method is called to write this object of <code>ExternalServiceInstance</code>
     *  to the specified output stream object.
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

        if (m_instName != null)
            UTFReaderWriter.writeUTF(out, m_instName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_actualInstName != null)
            UTFReaderWriter.writeUTF(out, m_actualInstName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_appGUID != null)
            UTFReaderWriter.writeUTF(out, m_appGUID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if(m_appVersionNo != null)
            UTFReaderWriter.writeUTF(out, m_appVersionNo);
        else
            UTFReaderWriter.writeUTF(out, "");
    }


    /**
     *  This is called to read this object <code>ExternalServiceInstance</code>
     *  from the specified object of input stream.
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

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_instName = null;
        else
            m_instName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_actualInstName = null;
        else
            m_actualInstName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_appGUID = null;
        else
            m_appGUID = temp;

        temp = UTFReaderWriter.readUTF(is);
        if(temp.equals(""))
            m_appVersionNo = null;
        else
            m_appVersionNo = temp;
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>ExternalServiceInstance</code>
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
        strBuf.append("External Service Instance ");
        strBuf.append("[");
        strBuf.append("Instance Name = ");
        strBuf.append(m_instName);
        strBuf.append(", ");
        strBuf.append("ApplicationGUID = ");
        strBuf.append(m_appGUID);
        strBuf.append(", ");
        strBuf.append("ApplicationVersion = ");
        strBuf.append(m_appVersionNo);
        strBuf.append(", ");
        strBuf.append("Actual Instance Name = ");
        strBuf.append(m_actualInstName);
        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("ExternalServiceInstance");

        Node node = null;

        node = XMLDmiUtil.getNodeObject("InstanceName", m_instName, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("ActualInstanceName", m_actualInstName, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("ApplicationGUID", m_appGUID, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("ApplicationVersion", m_appVersionNo, document);
        if (node != null)
            root0.appendChild(node);

        return root0;
    }
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start ExternalServiceInstance
        writer.writeStartElement("ExternalServiceInstance");

        FioranoStackSerializer.writeElement("InstanceName", m_instName, writer);
        FioranoStackSerializer.writeElement("ActualInstanceName", m_actualInstName, writer);
        FioranoStackSerializer.writeElement("ApplicationGUID", m_appGUID, writer);
        FioranoStackSerializer.writeElement("ApplicationVersion", m_appVersionNo, writer);

        //End ExternalServiceInstance
        writer.writeEndElement();

    }
}
