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
import java.util.Enumeration;
import java.util.Vector;

public class StatusTracking extends DmiObject
{

    Vector          m_ports = new Vector();
    Vector          m_states = new Vector();


    /**
     *  This the constructor of <code>StatusTracking</code> the class.
     *
     * @since Tifosi2.0
     */
    public StatusTracking()
    {
    }

    /**
     *  This method gets enumeration of names of ports, over which
     *  this object of <code>StatusTracking</code> has to track the status of
     *  corresponding service instance
     *
     * @return Enumeration of names of all the ports
     * @see #addPortName(String)
     * @since Tifosi2.0
     */
    public Enumeration getPortNames()
    {
        return m_ports.elements();
    }


    /**
     *  This method gets enumeration of all the states specified for status tracking
     *  of corresponding service instance, in this object of
     *  <code>StatusTracking</code>.
     *
     * @return Enumeration of State objects
     * @see #addState(State)
     * @since Tifosi2.0
     */
    public Enumeration getStates()
    {
        return m_states.elements();
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.STATUS_TRACKING;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>StatusTracking</code>, using the specified XML string.
     *
     * @param statusTrackingElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element statusTrackingElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element statusTrackingElement = doc.getDocumentElement();

        if (statusTrackingElement != null)
        {
            NodeList list = statusTrackingElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Port"))
                {
                    addPortName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("Status"))
                {
                    State state = new State();

                    state.setFieldValues((Element) child);
                    addState(state);
                }
            }
        }

        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.STATUS_TRACKING) )
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase("Port"))
                {
                    addPortName(parser.getText());
                }
                else if (nodeName.equalsIgnoreCase("Status"))
                {
                    State state = new State();

                    state.setFieldValues(parser);
                    addState(state);
                }
            }
        }
        validate();
    }
    /**
     * Checks if a port is already marked for status tracking.
     *
     * @param portName The name of the port to be tested.
     * @return True if the port is marked, false otherwise.
     */
    public boolean doesPortExist(String portName)
    {
        if (m_ports.contains(portName))
            return true;

        return false;
    }

    /**
     *  This method adds the name of new port, over which this object
     *  of <code>StatusTracking</code> has to track the status of the corresponding
     *  service instance.
     *
     * @param portName Name of port to be added
     * @see #getPortNames()
     * @since Tifosi2.0
     */
    public void addPortName(String portName)
    {
        m_ports.add(portName);
    }

    /**
     *  This method removes the name of port, over which this object
     *  of <code>StatusTracking</code> has to track the status of the corresponding
     *  service instance.
     *
     * @param portName Name of port to be added
     * @see #getPortNames()
     * @since Tifosi2.0
     */
    public void removePortName(String portName)
    {
        m_ports.remove(portName);
    }


    /**
     * This method adds the specified object of <code>State</code>,
     * to this object of <code>StatusTracking</code>.
     *
     * @param state Object of State to be added
     * @see #getStates()
     * @since Tifosi2.0
     */
    public void addState(State state)
    {
        m_states.add(state);
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
     *  This method tests whether this object of <code>StatusTracking</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_states == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        if (m_ports == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        Enumeration stateEnum = m_states.elements();

        while (stateEnum.hasMoreElements())
        {
            State state = (State) stateEnum.nextElement();

            state.validate();
        }
    }


    /**
     *  This method writes this object of <code>StatusTracking</code>
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

        if (m_ports != null)
        {
            int length = m_ports.size();

            out.writeInt(length);

            Enumeration ports = m_ports.elements();

            while (ports.hasMoreElements())
            {
                String port = (String) ports.nextElement();

                UTFReaderWriter.writeUTF(out, port);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if (m_states != null)
        {
            int length = m_states.size();

            out.writeInt(length);

            Enumeration states = m_states.elements();

            while (states.hasMoreElements())
            {
                State state = (State) states.nextElement();

                state.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This method reads the object <code>StatusTracking</code> from the
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

        int tempInt = 0;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                String port = UTFReaderWriter.readUTF(is);

                m_ports.add(port);
            }
        }

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                State state = new State();

                state.fromStream(is, versionNo);
                m_states.add(state);
            }
        }
    }

    /**
     * This utility method gets the String representation of this object
     * of <code>StatusTracking</code>.
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
        strBuf.append("Status Tracking Details ");
        strBuf.append("[");
        strBuf.append("Ports = ");
        strBuf.append(", ");
        if (m_ports != null)
        {
            strBuf.append("Ports = ");
            for (int i = 0; i < m_ports.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_ports.elementAt(i));
                strBuf.append(", ");
            }
        }
        if (m_states != null)
        {
            strBuf.append("States = ");
            for (int i = 0; i < m_states.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_states.elementAt(i));
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
     *                             node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("StatusTracking");

        XMLDmiUtil.addVectorValues("Port", m_ports, document, root0);

        Element child = null;

        if (m_states != null && m_states.size() > 0)
        {
            Enumeration enums = m_states.elements();

            while (enums.hasMoreElements())
            {
                State state = (State) enums.nextElement();
                Node pcData = state.toJXMLString(document);

                root0.appendChild(pcData);
            }
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start StatusTracking
        writer.writeStartElement("StatusTracking");

        FioranoStackSerializer.writeVector("Port", m_ports, writer);

        if (m_states != null && m_states.size() > 0)
        {
            Enumeration enums = m_states.elements();

            while (enums.hasMoreElements())
            {
                State state = (State) enums.nextElement();
                state.toJXMLString(writer);
            }
        }
        //End StatusTracking
        writer.writeEndElement();
    }
}
