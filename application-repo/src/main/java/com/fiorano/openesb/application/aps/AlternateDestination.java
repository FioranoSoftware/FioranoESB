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
import java.util.Hashtable;

public class AlternateDestination extends DmiObject
{

    String          m_strServiceIntName;
    String          m_strPortName;
    String          m_strNodeName;
    String          m_strAppInstName;


    /**
     *  Gets the application instance to which the service instance specified by
     *  this <code>AlternateDestination</code> object belongs.
     *
     * @return application instance name for this object
     * @see #setAppInstName(String)
     * @since Tifosi2.0
     */

    public String getAppInstName()
    {
        return m_strAppInstName;
    }


    /**
     *  Returns the name of the node on which the service instance, represented
     *  by this <code>AlternateDestination</code> object is running.
     *
     * @return the name of node for this object
     * @see #setNodeName(String)
     * @since Tifosi2.0
     */

    public String getNodeName()
    {
        return m_strNodeName;
    }


    /**
     *  Gets the name of port on which the service instance, represented by this
     *  <code>AlternateDestination</code> object has to receive the message.
     *
     * @return The port name for this object
     * @see #setPortName(String)
     * @since Tifosi2.0
     */

    public String getPortName()
    {
        return m_strPortName;
    }


    /**
     *  Gets the instance name for the service instance represented by this
     *  <code>AlternateDestination</code> object belongs.
     *
     * @return The name of service instance for this object.
     * @see #setServiceInstName(String)
     * @since Tifosi2.0
     */

    public String getServiceInstName()
    {
        return m_strServiceIntName;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */

    public int getObjectID()
    {
        return DmiObjectTypes.ALTERNATE_DESTINATION;
    }


    /**
     *  Sets the the application instance to which the alternate destination
     *  service instance of this <code>AlternateDestination</code> object
     *  belongs.
     *
     * @param strAppInstName string to be set as application instance name
     * @see #getAppInstName()
     * @since Tifosi2.0
     */

    public void setAppInstName(String strAppInstName)
    {
        m_strAppInstName = strAppInstName;
    }


    /**
     *  Sets the node on which the service instance specified by this <code>AlternateDestination</code>
     *  object is running.
     *
     * @param nodeName the string to be set as node for this object
     * @see #getNodeName()
     * @since Tifosi2.0
     */

    public void setNodeName(String nodeName)
    {
        m_strNodeName = nodeName;
    }


    /**
     *  Sets the specified string as the name of port on which the service
     *  instance represented by this <code>AlternateDestination</code> object
     *  has to receive the message.
     *
     * @param portName The string to be set as port name for this object
     * @see #getPortName()
     * @since Tifosi2.0
     */

    public void setPortName(String portName)
    {
        m_strPortName = portName;
    }


    /**
     *  Sets the specified string as the instance name for the service instance,
     *  represented by this <code>AlternateDestination</code> object belongs.
     *
     * @param servInstName The string to be set as name of service instance for
     *      this object.
     * @see #getServiceInstName()
     * @since Tifosi2.0
     */

    public void setServiceInstName(String servInstName)
    {
        m_strServiceIntName = servInstName;
    }


    /**
     *  Sets all the fieldValues of this <code>AlternateDestination</code>
     *  object using the specified XML string.
     *
     * @param routeElement The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */

    public void setFieldValues(Element routeElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element routeElement = doc.getDocumentElement();

        NodeList list = routeElement.getChildNodes();
        Node child = null;

        for (int i = 0; i < list.getLength(); i++)
        {
            child = list.item(i);

            String nodeName = child.getNodeName();

            if (nodeName.equalsIgnoreCase("ServiceInstName"))
            {
                setServiceInstName(XMLUtils.getNodeValueAsString(child));
            }
            if (nodeName.equalsIgnoreCase("AppInstName"))
            {
                setAppInstName(XMLUtils.getNodeValueAsString(child));
            }
            if (nodeName.equalsIgnoreCase("Node"))
            {
                setNodeName(XMLUtils.getNodeValueAsString(child));
            }
            if (nodeName.equalsIgnoreCase("Port"))
            {
                setPortName(XMLUtils.getNodeValueAsString(child));
            }
        }
    }

    /**
     *  Sets all the fieldValues of this <code>ApplicationHeader</code> object
     *  from the XML using STAX parser.
     */
    public void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException
    {
	//Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
		if ( cursor.markCursor(APSConstants.ALTERNATE_DESTINATION) )
		{
		        // Get Attributes. This MUST be done before accessing any data of element.
			Hashtable attributes = cursor.getAttributes();

			//Get associated Data
			//String nodeVal = cursor.getText();

			// Get Child Elements
			while (cursor.nextElement())
			{

			    String nodeName = cursor.getLocalName();


			    // Get Attributes
			    //attributes = cursor.getAttributes();

			    //String nodeValue = cursor.getText();
			    //if (nodeValue == null)
				//continue;

			    if (nodeName.equalsIgnoreCase("ServiceInstName"))
			    {
				String nodeValue = cursor.getText();
				setServiceInstName(nodeValue);
			    }
			    if (nodeName.equalsIgnoreCase("AppInstName"))
			    {
				String nodeValue = cursor.getText();
				setAppInstName(nodeValue);
			    }
			    if (nodeName.equalsIgnoreCase("Node"))
			    {
				String nodeValue = cursor.getText();
				setNodeName(nodeValue);
			    }
			    if (nodeName.equalsIgnoreCase("Port"))
			    {
				String nodeValue = cursor.getText();
				setPortName(nodeValue);
			    }
			}
		}
    }


    /**
     *  Not supported in this version. (Resets the values of the data members of
     *  this object.)
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method tests whether this <code>AlternateDestination</code> object
     *  has the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method writes this <code>AlternateDestination</code> object to the
     *  specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo Description of the Parameter
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);
        writeUTF(out, m_strServiceIntName);
        writeUTF(out, m_strAppInstName);
        writeUTF(out, m_strNodeName);
        writeUTF(out, m_strPortName);
    }


    /**
     *  This method reads this object <code>AlternateDestination</code> from the
     *  specified input stream object.
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
        m_strServiceIntName = readUTF(is);
        m_strAppInstName = readUTF(is);
        m_strNodeName = readUTF(is);
        m_strPortName = readUTF(is);
    }


    /**
     *  This method is used to get the String representation of this <code>AlternateDestination</code>
     *  object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Alternate Destination Details ");
        strBuf.append("[");
        strBuf.append("Service Instance Name = ");
        strBuf.append(m_strServiceIntName);
        strBuf.append(", ");
        strBuf.append("Port Name = ");
        strBuf.append(m_strPortName);
        strBuf.append(", ");
        strBuf.append("Node Name = ");
        strBuf.append(m_strNodeName);
        strBuf.append(", ");
        strBuf.append("Application Instance Name = ");
        strBuf.append(m_strAppInstName);
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
        Node root0 = (Node) document.createElement("AlternateDestination");

        Node node = null;

        node = XMLDmiUtil.getNodeObject("ServiceInstName", m_strServiceIntName, document);
        if (node != null)
        {
            root0.appendChild(node);
        }

        node = XMLDmiUtil.getNodeObject("AppInstName", m_strAppInstName, document);
        if (node != null)
        {
            root0.appendChild(node);
        }

        node = XMLDmiUtil.getNodeObject("Node", m_strNodeName, document);
        if (node != null)
        {
            root0.appendChild(node);
        }

        node = XMLDmiUtil.getNodeObject("Port", m_strPortName, document);
        if (node != null)
        {
            root0.appendChild(node);
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {

        //Start AlternateDestination
        writer.writeStartElement("AlternateDestination");

        //ServiceInstName
        FioranoStackSerializer.writeElement("ServiceInstName", m_strServiceIntName, writer);

        //AppInstName
        FioranoStackSerializer.writeElement("AppInstName", m_strAppInstName, writer);

        //Node
        FioranoStackSerializer.writeElement("Node", m_strNodeName, writer);

        //Port
        FioranoStackSerializer.writeElement("Port", m_strPortName, writer);

        //End AlternateDestination
        writer.writeEndElement();
    }

}
