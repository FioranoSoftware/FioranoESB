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

public class WfInPort extends DmiObject
{
    String          m_servInstName;

    String          m_portName;


    /**
     *  This is the constructor of the <code>WfInPort</code> class.
     *
     * @since Tifosi2.0
     */
    public WfInPort()
    {
    }

    /**
     *  This method gets the name of service instance, to which the Inport
     *  represented by this object <code>WfInPort</code>.
     *
     * @return service instance name.
     * @see #setServiceInstName(String)
     * @since Tifosi2.0
     */
    public String getServiceInstName()
    {
        return m_servInstName;
    }

    /**
     *  This method gets the port name of InPort represented by
     *  this object of <code>WfInPort</code>.
     *
     * @return name of InPort
     * @see #setPortName(String)
     * @since Tifosi2.0
     */
    public String getPortName()
    {
        return m_portName;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.WF_INPORT;
    }


    /**
     *  This method sets the specified string as name of service
     *  instance, to which the corresponding InPort belongs. This Inport is represented
     *  by this object of <code>WfInPort</code>.
     *
     * @param servInstName The string to be set as service instance name.
     * @see #getServiceInstName()
     * @since Tifosi2.0
     */
    public void setServiceInstName(String servInstName)
    {
        m_servInstName = servInstName;
    }

    /**
     *  This method sets the specified string as port name for
     *  InPort, represented by this object of <code>WfInPort</code>.
     *
     * @param portName the string to be set as name of InPort
     * @see #getPortName()
     * @since Tifosi2.0
     */
    public void setPortName(String portName)
    {
        m_portName = portName;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>WfInPort</code>, using the specified XML string.
     *
     * @param inElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element inElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element inElement = doc.getDocumentElement();

        if (inElement != null)
        {
            NodeList list = inElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("ServiceInstanceName"))
                {
                    setServiceInstName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("Name"))
                {
                    setPortName(XMLUtils.getNodeValueAsString(child));
                }
            }
        }

        validate();
    }

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException
    {

        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if(cursor.markCursor(APSConstants.WORKFLOW_IN_PORT)){

            // Get Child Elements
            while(cursor.nextElement()){

                String nodeName = cursor.getLocalName();

                // For debugging. Remove this befor chekin...
                
                String nodeValue = null;
                if(nodeName.equalsIgnoreCase(APSConstants.SERVICE_INSTANCE_NAME)){
                    nodeValue = cursor.getText();
                    setServiceInstName(nodeValue);
                } else if(nodeName.equalsIgnoreCase(APSConstants.WORKFLOW_IN_PORT_NAME)){
                    nodeValue = cursor.getText();
                    setPortName(nodeValue);
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
     *  This method tests whether this object of <code>WfInPort</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_portName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_servInstName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  This method writes this object of <code>WfInPort</code>
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

        if (m_servInstName != null)
            UTFReaderWriter.writeUTF(out, m_servInstName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_portName != null)
            UTFReaderWriter.writeUTF(out, m_portName);
        else
            UTFReaderWriter.writeUTF(out, "");
    }


    /**
     *  This method reads this object <code>WfInPort</code> from the
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

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_servInstName = null;
        else
            m_servInstName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_portName = null;
        else
            m_portName = temp;
    }

    /**
     * This utility method gets the String representation of this object
     * of <code>WfInPort</code>.
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
        strBuf.append("WorkFlow In port Details ");
        strBuf.append("[");
        strBuf.append("Port name = ");
        strBuf.append(m_portName);
        strBuf.append(", ");
        strBuf.append("Service Instance name = ");
        strBuf.append(m_servInstName);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the XML string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("WfInPort");

        Node child = null;

        child = XMLDmiUtil.getNodeObject("ServiceInstanceName", m_servInstName, document);
        if (child != null)
            root0.appendChild(child);

        child = XMLDmiUtil.getNodeObject("Name", m_portName, document);
        if (child != null)
            root0.appendChild(child);

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start WfInPort
        writer.writeStartElement("WfInPort");

        //Write ServiceInstanceName
        FioranoStackSerializer.writeElement("ServiceInstanceName", m_servInstName, writer);

        //Write PortName
        FioranoStackSerializer.writeElement("Name", m_portName, writer);

        //End WfInPort
        writer.writeEndElement();
    }
}
