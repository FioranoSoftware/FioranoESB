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

public class LogModules extends DmiObject
{
    private Vector  m_logModules;

    /**
     *  Constructor for the LogModules object
     */
    public LogModules()
    {
        m_logModules = new Vector();
    }

    /**
     *  This method gets the enumeration of all the modules of
     *  service instance, contained in this object of <code>LogModules</code>. Each
     *  LogModules object is associated with a service instance and stores
     *  information about modules of that service instance.
     *
     * @return Enumeration of modules.
     * @see #addLogModule(ApsLogModule)
     * @since Tifosi2.0
     */
    public Enumeration getLogModules()
    {
        return m_logModules.elements();
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_LOG_MODULES;
    }

    /**
     *  This method sets all the fieldValues of this object of
     *  <code>LogModules</code>, using the specified XML string.
     *
     * @param monitorElement Element object specifying the LogModules
     * @exception FioranoException if an error occurs while parsing the
     *                             monitorElement
     * @since Tifosi2.0
     */
    public void setFieldValues(Element monitorElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element monitorElement = doc.getDocumentElement();

        NodeList list = monitorElement.getChildNodes();
        Node child = null;

        for (int i = 0; i < list.getLength(); i++)
        {
            child = list.item(i);

            String nodeName = child.getNodeName();

            if (nodeName.equalsIgnoreCase("Module"))
            {
                ApsLogModule module = new ApsLogModule();

                module.setFieldValues((Element) child);
                m_logModules.add(module);
            }
        }
        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException, FioranoException
    {

        if ( parser.markCursor(APSConstants.LOG_MODULES) )
        {
            while(parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase("Module"))
                {
                    ApsLogModule module = new ApsLogModule();

                    module.setFieldValues(parser);
                    m_logModules.add(module);
                }
            }
        }
        validate();
    }

    /**
     *  This method adds specified object of <code>LogModule</code>
     *  to this object of <code>LogModules</code>. Each LogModules object is
     *  associated with a service instance and stores information about modules
     *  of that service instance.
     *
     * @param module object of Module to be added.
     * @since Tifosi2.0
     */
    public void addLogModule(ApsLogModule module)
    {
        m_logModules.add(module);
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_logModules.clear();
    }


    /**
     *  This method tests whether this object of <code>LogModules</code> has the
     *  required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method is called to write this object of <code>LogModules</code> to the
     *  specified output stream object.
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

        int size = m_logModules.size();

        out.writeInt(size);
        for (int i = 0; i < size; i++)
        {
            ApsLogModule module = (ApsLogModule) m_logModules.get(i);

            module.toStream(out, versionNo);
        }
    }

    /**
     *  This is called to read this object <code>LogModules</code> from the specified
     *  object of input stream.
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

        int size = is.readInt();

        for (int i = 0; i < size; i++)
        {
            ApsLogModule module = new ApsLogModule();

            module.fromStream(is, versionNo);
            m_logModules.add(module);
        }
    }

    /**
     *  This utility method is used to get the String representation of this object
     *  of <code>LogModules</code>
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
        strBuf.append("Log Modules  ");
        strBuf.append("[");

        if (m_logModules != null)
        {
            strBuf.append("Modules = ");
            for (int i = 0; i < m_logModules.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_logModules.elementAt(i));
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
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("LogModules");

        for (int i = 0; i < m_logModules.size(); i++)
        {
            ApsLogModule module = (ApsLogModule) m_logModules.get(i);
            Node node = module.toJXMLString(document);

            root0.appendChild(node);
        }

        return root0;
    }
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start LogModules
        writer.writeStartElement("LogModules");

        for (int i = 0; i < m_logModules.size(); i++)
        {
            ApsLogModule module = (ApsLogModule) m_logModules.get(i);
            module.toJXMLString(writer);

        }
        //End LogModules
        writer.writeEndElement();
    }
}
