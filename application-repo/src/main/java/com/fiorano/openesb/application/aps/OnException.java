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

public class OnException extends DmiObject
{
    boolean         m_isErrorHandlingSupported;

    String          m_trgtServiceInstance;

    String          m_trgtPortName;


    /**
     *  This is called to construct object of <code>OnException </code>
     *
     * @since Tifosi2.0
     */
    public OnException()
    {
    }


    /**
     *  This is used to check whether error handling is supported or not in the
     *  application associated with this object of <code>OnException</code>
     *
     * @return true if error handling is supported, false otherwise.
     * @see #setIsErrorHandlingSupported(boolean)
     * @since Tifosi2.0
     */
    public boolean isErrorHandlingSupported()
    {
        return m_isErrorHandlingSupported;
    }


    /**
     *  This method gets the instance name of target
     *  service instance, in this object of <code>OnException</code>. The
     *  exception raised during service launch failure, is to be routed to this
     *  instance, while launching the application associated with this object.
     *
     * @return The name of target service instance.
     * @see #setTrgtServiceInstance(String)
     * @since Tifosi2.0
     */
    public String getTrgtServiceInstance()
    {
        return m_trgtServiceInstance;
    }


    /**
     *  This method gets the port name of target service
     *  instance, in this object of <code>OnException</code>. The exception
     *  raised during service launch failure, is to be routed to this port of
     *  specified target service instance, while launching the application
     *  associated with this object.
     *
     * @return The target port name
     * @see #setTrgtPortName(String)
     * @since Tifosi2.0
     */
    public String getTrgtPortName()
    {
        return m_trgtPortName;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.ON_EXCEPTION;
    }


    /**
     *  This is used to set whether error handling is supported or not in the
     *  application associated with this object of <code>OnException</code>
     *
     * @param isErrorHandlingSupported boolean specifying Error Handling
     *      Supported or not.
     * @see #isErrorHandlingSupported()
     * @since Tifosi2.0
     */
    public void setIsErrorHandlingSupported(boolean isErrorHandlingSupported)
    {
        m_isErrorHandlingSupported = isErrorHandlingSupported;
    }


    /**
     *  This method sets the specified string as instance
     *  name of target service instance, in this object of <code>OnException</code>
     *  . The exception raised during service launch failure, is to be routed to
     *  this instance, while launching the application associated with this
     *  object.
     *
     * @param trgtServiceInstance The string to be set as target service
     *      instance name.
     * @see #getTrgtServiceInstance()
     * @since Tifosi2.0
     */
    public void setTrgtServiceInstance(String trgtServiceInstance)
    {
        m_trgtServiceInstance = trgtServiceInstance;
    }


    /**
     *  This method sets the specified string as port name
     *  of target service instance, in this object of <code>OnException</code>.
     *  The exception raised during service launch failure, is to be routed to
     *  this port of specified target service instance, while launching the
     *  application associated with this object.
     *
     * @param trgtPortName The string to be set as target port name
     * @see #getTrgtPortName()
     * @since Tifosi2.0
     */
    public void setTrgtPortName(String trgtPortName)
    {
        m_trgtPortName = trgtPortName;
    }


    /**
     *  This method sets all the fieldValues of this object
     *  of <code>OnException</code>, using the specified XML string.
     *
     * @param onExcElement
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element onExcElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element onExcElement = doc.getDocumentElement();

        if (onExcElement != null)
        {
            boolean isErrorEnabled = XMLDmiUtil.getAttributeAsBoolean
                (onExcElement, "isErrorHandlingSupported");

            setIsErrorHandlingSupported(isErrorEnabled);

            NodeList list = onExcElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("TgtServiceInstance"))
                {
                    setTrgtServiceInstance(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("TgtPort"))
                {
                    setTrgtPortName(XMLUtils.getNodeValueAsString(child));
                }
            }
        }

        validate();
    }


    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException
    {

        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if(cursor.markCursor(APSConstants.ON_EXCEPTION)){

            // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = cursor.getAttributes();
            if(attributes!=null && attributes.containsKey(APSConstants.ATTR_IS_SUBGRAPHABLE)){
                boolean isErrorEnabled = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_ERRORHANDLING_SUPPORTED));
                setIsErrorHandlingSupported(isErrorEnabled);
            }

            // Get Child Elements
            while(cursor.nextElement()){
                String nodeName = cursor.getLocalName();

                // For debugging. Remove this befor chekin...
                
                //String nodeValue = null;

                if(nodeName.equalsIgnoreCase(APSConstants.TGT_SRV_INSTANCE)){
                    String nodeValue = cursor.getText();
                    setTrgtServiceInstance(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase(APSConstants.TGT_PORT)){
                    String nodeValue = cursor.getText();
                    setTrgtPortName(nodeValue);
                }

                else{
                    // WHAT TO DO ???
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
     *  This method tests whether this object of <code>OnExceptions</code> has
     *  the required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_trgtServiceInstance == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_trgtPortName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  This method is called to write this object of <code>OnException</code>
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

        out.writeBoolean(m_isErrorHandlingSupported);

        if (m_trgtServiceInstance != null)
            UTFReaderWriter.writeUTF(out, m_trgtServiceInstance);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_trgtPortName != null)
            UTFReaderWriter.writeUTF(out, m_trgtPortName);
        else
            UTFReaderWriter.writeUTF(out, "");
    }


    /**
     *  This is called to read this object <code>OnException</code> from the
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

        m_isErrorHandlingSupported = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_trgtServiceInstance = null;
        else
            m_trgtServiceInstance = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_trgtPortName = null;
        else
            m_trgtPortName = temp;

    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>OnException</code>
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
        strBuf.append("On Exception ");
        strBuf.append("[");
        strBuf.append("Target Port Name = ");
        strBuf.append(m_trgtPortName);
        strBuf.append(", ");
        strBuf.append("Target Service Instance = ");
        strBuf.append(m_trgtServiceInstance);
        strBuf.append(", ");
        strBuf.append("Is error Handling Supported = ");
        strBuf.append(m_isErrorHandlingSupported);
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
        Node root0 = (Node) document.createElement("OnException");

        ((Element) root0).setAttribute("isErrorHandlingSupported", "" + m_isErrorHandlingSupported);

        Node child = null;

        child = XMLDmiUtil.getNodeObject("TgtServiceInstance", m_trgtServiceInstance, document);
        if (child != null)
            root0.appendChild(child);

        child = XMLDmiUtil.getNodeObject("TgtPort", m_trgtPortName, document);
        if (child != null)
            root0.appendChild(child);

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start OnException
        writer.writeStartElement("OnException");
        writer.writeAttribute("isErrorHandlingSupported", "" + m_isErrorHandlingSupported);

        //Write TgtServiceInstance
        FioranoStackSerializer.writeElement("TgtServiceInstance", m_trgtServiceInstance, writer);

        //Write TgtPort
        FioranoStackSerializer.writeElement("TgtPort", m_trgtPortName, writer);

        //End OnException
        writer.writeEndElement();
    }
}
