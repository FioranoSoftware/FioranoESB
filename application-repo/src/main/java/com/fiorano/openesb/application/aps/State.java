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
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;

public class State extends DmiObject
{
    String          m_name;
    boolean         m_isStatusTrackingOn;


    /**
     * This is the constructor of the <code>State</code> class.
     *
     * @since Tifosi2.0
     */
    public State()
    {
        m_name = "";
    }


    /**
     *  This method gets the specified string as name of state for this object of
     * <code>State</code>.
     *
     * @return The name of state
     * @see #setName(String)
     * @since Tifosi2.0
     */
    public String getName()
    {
        return m_name;
    }


    /**
     *  This method checks whether state corresponding to this object of
     *  <code>State</code>, is trackable or not for associated workflow.
     *
     * @return true if state is trackable, false otherwise
     * @see #setIsStatusTrackingOn(boolean)
     * @since Tifosi2.0
     */
    public boolean isStatusTrackingOn()
    {
        return m_isStatusTrackingOn;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.STATE;
    }


    /**
     * This method sets the specified string as name of state for this object of
     * <code>State</code>.
     *
     * @param name string to be set as name of state.
     * @see #getName()
     * @since Tifosi2.0
     */
    public void setName(String name)
    {
        m_name = name;
    }


    /**
     *  This method sets whether state corresponding to this object of
     *  <code>State</code>, is trackable or not for associated workflow.
     *
     * @param isStatusTrackingOn boolean specifying if state is trackable
     * @see #isStatusTrackingOn()
     * @since Tifosi2.0
     */
    public void setIsStatusTrackingOn(boolean isStatusTrackingOn)
    {
        m_isStatusTrackingOn = isStatusTrackingOn;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>State</code>, using the specified XML string.
     *
     * @param stateElement
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element stateElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element stateElement = doc.getDocumentElement();

        if (stateElement != null)
        {
            boolean isStatusTrackingOn = XMLDmiUtil.getAttributeAsBoolean(stateElement, "isStatusTrackingOn");

            setIsStatusTrackingOn(isStatusTrackingOn);
            setName(stateElement.getAttribute("name"));
        }

        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException, FioranoException
    {
        if ( parser.markCursor(APSConstants.STATUS) )
        {
            Hashtable attributes= parser.getAttributes();
            boolean isStatusTrackingOn = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_STATUS_TRACKING_ON));// XMLDmiUtil.getAttributeAsBoolean(stateElement, "isStatusTrackingOn");
            setIsStatusTrackingOn(isStatusTrackingOn);
            String name = (String)attributes.get(APSConstants.STATE_NAME);
            setName(name);

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
     *  This method tests whether this object of <code>State</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
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
    }


    /**
     *  This method writes this object of <code>State</code>
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

        out.writeBoolean(m_isStatusTrackingOn);

        if (m_name != null)
            UTFReaderWriter.writeUTF(out, m_name);
        else
            UTFReaderWriter.writeUTF(out, "");
    }


    /**
     *  This method reads this object <code>State</code> from the
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

        m_isStatusTrackingOn = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_name = null;
        else
            m_name = temp;
    }

    /**
     * This utility method gets the String representation of this object
     * of <code>State</code>.
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
        strBuf.append("State Details ");
        strBuf.append("[");
        strBuf.append("Name = ");
        strBuf.append(m_name);
        strBuf.append(", ");
        strBuf.append("Is Status Trackingon = ");
        strBuf.append(m_isStatusTrackingOn);
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
        Node root0 = (Node) document.createElement("Status");

        ((Element) root0).setAttribute("name", m_name);
        ((Element) root0).setAttribute("isStatusTrackingOn", "" + m_isStatusTrackingOn);

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Status
        writer.writeStartElement("Status");

        writer.writeAttribute("name", m_name);
        writer.writeAttribute("isStatusTrackingOn", "" + m_isStatusTrackingOn);

        //End Status
        writer.writeEndElement();
    }
}
