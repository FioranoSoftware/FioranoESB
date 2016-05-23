/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
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

public class RuntimeDependency extends DmiObject
{

    String          m_strServiceGUID;

    String          m_strVersion;


    /**
     *  Constructor for the RuntimeDependency object
     *
     * @since Tifois2.0
     */
    public RuntimeDependency()
    {
    }


    /**
     *  This interface method is called to get the serviceGUID for
     *  this <code>RuntimeDependency</code>.
     *
     * @return The serviceGUID for this RuntimeDependency.
     * @see #setServiceGUID(String)
     * @since Tifois2.0
     */
    public String getServiceGUID()
    {
        return m_strServiceGUID;
    }


    /**
     *  This interface method is called to get the version for
     *  this <code>RuntimeDependency</code>.
     *
     * @return The version for this RuntimeDependency.
     * @see #setVersion(String)
     * @since Tifois2.0
     */
    public String getVersion()
    {
        return m_strVersion;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_DEPENDENCY;
    }


    /**
     *  This interface method is called to set the specified string as serviceGUID
     *  for this <code>RuntimeDependency</code>.
     *
     * @param servGUID The string to be set as serviceGUID
     * @see #getServiceGUID()
     * @since Tifois2.0
     */
    public void setServiceGUID(String servGUID)
    {
        m_strServiceGUID = servGUID;
    }


    /**
     *  This interface method is called to set the specified string as version for this
     *  <code>RuntimeDependency</code>.
     *
     * @param version The string to be set as Version for this RuntimeDependency
     * @see #getVersion()
     * @since Tifois2.0
     */
    public void setVersion(String version)
    {
        m_strVersion = version;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>RuntimeDependency</code>, using the specified XML string.
     *
     * @param depHdr
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element depHdr)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML( xmlString );
//        Element depHdr = doc.getDocumentElement();
        if (depHdr != null)
        {
            NodeList children = depHdr.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(child);

                if (nodeName.equalsIgnoreCase("ServiceGUID"))
                {
                    m_strServiceGUID = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("Version"))
                {
                    m_strVersion = nodeValue;
                }
            }
        }
        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.RUNTIME_DEPENDENCY) )
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                String nodeValue = parser.getText();
                if (nodeName.equalsIgnoreCase("ServiceGUID"))
                {
                    System.out.print("ServiceGUID:"+nodeValue);
                    m_strServiceGUID = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("Version"))
                {
                    m_strVersion = nodeValue;

                }
            }

        }
        validate();
        
    }


    /**
     *  This method tests whether this object of <code>RuntimeDependency</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strServiceGUID == null || m_strVersion == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  Resets the values of the data members of this object. Not supported in this version
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    public Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("RuntimeDependency");

        Node child = null;

        child = XMLDmiUtil.getNodeObject("ServiceGUID", m_strServiceGUID, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("Version", m_strVersion, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start RuntimeDependency
        writer.writeStartElement("RuntimeDependency");

        FioranoStackSerializer.writeElement("ServiceGUID", m_strServiceGUID, writer);

        FioranoStackSerializer.writeElement("Version", m_strVersion, writer);

        //End RuntimeDependency
        writer.writeEndElement();
    }

    /**
     *  This method is called to write this object of <code>RuntimeDependency</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        writeUTF(out, m_strServiceGUID);
        writeUTF(out, m_strVersion);
    }


    /**
     *  This is called to read this object <code>RuntimeDependency</code>
     *  from the specified object of input stream.
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

        m_strServiceGUID = readUTF(is);
        m_strVersion = readUTF(is);
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>RuntimeDependency</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof RuntimeDependency))
        {
            return false;
        }

        RuntimeDependency rcvObj = (RuntimeDependency) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getServiceGUID(), m_strServiceGUID)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getVersion(), m_strVersion))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>RuntimeDependency</code>.
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
        strBuf.append("Service Dependency Details ");
        strBuf.append("[");
        strBuf.append("Service GUID = ");
        strBuf.append(m_strServiceGUID);
        strBuf.append(", ");
        strBuf.append("Version = ");
        strBuf.append(m_strVersion);
        strBuf.append("]");
        return strBuf.toString();
    }
}
