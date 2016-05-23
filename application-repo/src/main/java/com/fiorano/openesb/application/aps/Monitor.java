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

public class Monitor extends DmiObject
{

    Vector          m_modules;
    private String elementName= "Monitor";


    /**
     *  This is called to construct object of <code>Monitor</code>
     *
     * @since Tifosi2.0
     */
    public Monitor()
    {
       this("Monitor");
    }

    public Monitor(String elementName)
    {
       m_modules = new Vector();
       this.elementName = elementName;
    }

    /**
     *  This method gets the enumeration of all the modules of service instance,
     *  contained in this object of <code>Monitor</code>. Each Monitor object is
     *  associated with a service instance and stores information about modules
     *  of that service instance.
     *
     * @return Enumeration of modules.
     * @see #addModule(ApsEventModule)
     * @since Tifosi2.0
     */
    public Enumeration getModules()
    {
        return m_modules.elements();
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.MONITOR;
    }

    /**
     *  This method sets all the fieldValues of this object of <code>Monitor</code>
     *  , using the specified XML string.
     *
     * @param monitorElement The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
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
                ApsEventModule module = new ApsEventModule();

                module.setFieldValues((Element) child);
                m_modules.add(module);
            }
        }
        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException, FioranoException
    {

        if ( parser.markCursor(elementName) )
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                 if (nodeName.equalsIgnoreCase("Module"))
                {
                    ApsEventModule module = new ApsEventModule();

                    module.setFieldValues(parser);
                    m_modules.add(module);
                }

            }
        }
        validate();
    }

    /**
     *  This method adds specified object of <code>EventModule</code> to this
     *  object of <code>Monitor</code>. Each Monitor object is associated with a
     *  service instance and stores information about modules of that service
     *  instance.
     *
     * @param module object of Module to be added.
     * @see #getModules()
     * @since Tifosi2.0
     */
    public void addModule(ApsEventModule module)
    {
        m_modules.add(module);
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_modules.clear();
    }


    /**
     *  This method tests whether this object of <code>Monitor</code> has the
     *  required(mandatory) fields set, before inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  Description of the Method
     *
     * @param out Description of the Parameter
     * @param versionNo
     * @exception IOException Description of the Exception
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        int size = m_modules.size();

        out.writeInt(size);
        for (int i = 0; i < size; i++)
        {
            ApsEventModule module = (ApsEventModule) m_modules.get(i);

            module.toStream(out, versionNo);
        }
    }


    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param versionNo
     * @exception IOException Description of the Exception
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        int size = is.readInt();

        for (int i = 0; i < size; i++)
        {
            ApsEventModule module = new ApsEventModule();

            module.fromStream(is, versionNo);
            m_modules.add(module);
        }
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>Monitor</code>
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
        strBuf.append("Event Modules  ");
        strBuf.append("[");

        if (m_modules != null)
        {
            strBuf.append("ApsEvent Modules = ");
            for (int i = 0; i < m_modules.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_modules.elementAt(i));
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
        Node root0 = (Node) document.createElement("EventModules");

        for (int i = 0; i < m_modules.size(); i++)
        {
            ApsEventModule module = (ApsEventModule) m_modules.get(i);
            Node node = module.toJXMLString(document);

            root0.appendChild(node);
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start EventModules
        writer.writeStartElement("EventModules");

        for (int i = 0; i < m_modules.size(); i++)
        {
            ApsEventModule module = (ApsEventModule) m_modules.get(i);
            module.toJXMLString(writer);
        }

        //End EventModules
        writer.writeEndElement();
    }
}
