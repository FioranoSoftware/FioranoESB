/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

//import java.util.*;

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

public class EndState extends DmiObject
{

    String          m_stateID;


    /**
     *  This is called to construct object of <code>EndState</code>
     *
     * @since Tifosi2.0
     */
    public EndState()
    {
    }


    /**
     *  This method gets the stateID for this object of
     *  <code>EndState</code>.
     *
     * @return The stateID  for this object
     * @see #setStateID(String)
     * @since Tifosi2.0
     */
    public String getStateID()
    {
        return m_stateID;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.END_STATE;
    }


    /**
     *  This method sets the specified string as stateID
     *  for this object of <code>EndState</code>.
     *
     * @param stateID the string to be set as stateID
     * @see #getStateID()
     * @since Tifosi2.0
     */
    public void setStateID(String stateID)
    {
        m_stateID = stateID;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>EndState</code>, using the specified XML string.
     *
     * @param endStateElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element endStateElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element endStateElement = doc.getDocumentElement();

        if (endStateElement != null)
        {
            String state = endStateElement.getAttribute("name");

            setStateID(state);
        }
    }


    protected void populate(FioranoStaxParser parser)throws XMLStreamException
    {
        if ( parser.markCursor(APSConstants.END_STATE ))
        {
            Hashtable attributes = parser.getAttributes();
            String state = (String)attributes.get(APSConstants.END_STATE_NAME);//endStateElement.getAttribute("name");
            
            setStateID(state);
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
     *  This method tests whether this object of <code>EndState</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method is called to write this object of <code>EndState</code>
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
        writeUTF(out, m_stateID);
    }


    /**
     *  This is called to read this object <code>EndState</code> from the
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
        m_stateID = readUTF(is);
    }


    /**
     * This utility method is used to get the String representation of this object
     * of <code>EndState</code>
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
        strBuf.append("End State ");
        strBuf.append("[");
        strBuf.append("State ID = ");
        strBuf.append(m_stateID);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *                             node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("EndState");

        if (m_stateID != null)
        {
            ((Element) root0).setAttribute("name", m_stateID);
        }
        else
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException
    {
        //Start EndState
        writer.writeStartElement("EndState");

        if (m_stateID != null)
        {
            writer.writeAttribute("name", m_stateID);
        }
        else
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        //End EndState
        writer.writeEndElement();

    }
}
