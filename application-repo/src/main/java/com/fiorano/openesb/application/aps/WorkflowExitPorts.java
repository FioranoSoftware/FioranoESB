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
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class WorkflowExitPorts extends DmiObject
{
    Vector          m_outPorts = new Vector();


    /**
     *  This is the constructor of the <code>WorkflowExitPorts</code> class.
     *
     * @since Tifosi2.0
     */
    public WorkflowExitPorts()
    {
    }


    /**
     *  This method gets enumeration of <code>WfOutPort</code> from this object of
     *  <code>WorkflowExitPorts</code>.
     *
     * @return Enumeration of objects of WfOutPort
     * @see #addOutPort(WfOutPort)
     * @since Tifosi2.0
     */
    public Enumeration getOutPorts()
    {
        return m_outPorts.elements();
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.WF_EXIT_PORTS;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>WorkflowExitPorts</code>, using the specified XML string.
     *
     * @param exitElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element exitElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element exitElement = doc.getDocumentElement();

        if (exitElement != null)
        {
            NodeList list = exitElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("WfOutPort"))
                {
                    WfOutPort outPort = new WfOutPort();

                    outPort.setFieldValues((Element) child);
                    m_outPorts.add(outPort);
                }
            }
        }

        validate();
    }

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException
    {

        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if(cursor.markCursor(APSConstants.WORKFLOW_EXIT_PORTS)){

            // Get Child Elements
            while(cursor.nextElement()){

                String nodeName = cursor.getLocalName();

                if(nodeName.equalsIgnoreCase(APSConstants.WORKFLOW_OUT_PORT)){
                    WfOutPort outPort = new WfOutPort();
                    outPort.setFieldValues(cursor);
                    m_outPorts.add(outPort);
                }
            }
        }
        validate();
    }


    /**
     *  This method adds the specified object of <code>WfOutPort</code> to
     *  this object of <code>WorkflowExitPorts</code>.
     *
     * @param outPort The feature to be added to the OutPort attribute
     * @see #getOutPorts()
     * @since Tifosi2.0
     */
    public void addOutPort(WfOutPort outPort)
    {
        m_outPorts.add(outPort);
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
     *  This method tests whether this object of <code>WorkflowExitPorts</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_outPorts == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        Enumeration enums = m_outPorts.elements();

        while (enums.hasMoreElements())
        {
            WfOutPort outPort = (WfOutPort) enums.nextElement();

            outPort.validate();
        }
    }


    /**
     *  This method writes this object of <code>WorkflowExitPorts</code>
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

        if (m_outPorts != null)
        {
            int length = m_outPorts.size();

            out.writeInt(length);

            Enumeration outPorts = m_outPorts.elements();

            while (outPorts.hasMoreElements())
            {
                WfOutPort outPort = (WfOutPort) outPorts.nextElement();

                outPort.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);
    }


    /**
     *  This method reads this object <code>WorkflowExitPorts</code> from the
     *  specified object of input stream.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        int tempInt = 0;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                WfOutPort outPort = new WfOutPort();

                outPort.fromStream(is, versionNo);
                m_outPorts.add(outPort);
            }
        }
    }

    /**
     * This utility method gets the String representation of this object
     * of <code>WorkflowExitPorts</code>.
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
        strBuf.append("Work Flow Exit Port Details ");
        strBuf.append("[");
        if (m_outPorts != null)
        {
            strBuf.append("Port = ");
            for (int i = 0; i < m_outPorts.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_outPorts.elementAt(i));
                strBuf.append(", ");
            }
        }
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
        Node root0 = (Node) document.createElement("WorkflowExitPorts");

        Element child = null;

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            Enumeration enums = m_outPorts.elements();

            while (enums.hasMoreElements())
            {
                WfOutPort port = (WfOutPort) enums.nextElement();
                Node pcData = port.toJXMLString(document);

                root0.appendChild(pcData);
            }
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start WorkflowExitPorts
        writer.writeStartElement("WorkflowExitPorts");

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            Enumeration enums = m_outPorts.elements();

            while (enums.hasMoreElements())
            {
                WfOutPort port = (WfOutPort) enums.nextElement();
                port.toJXMLString(writer);


            }
        }
        //End WorkflowExitPorts
        writer.writeEndElement();

    }
}
