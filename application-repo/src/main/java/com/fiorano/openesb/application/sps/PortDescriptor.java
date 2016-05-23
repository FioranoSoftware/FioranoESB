/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class PortDescriptor extends DmiObject
{
    Vector          m_inPorts = new Vector();
    Vector          m_outPorts = new Vector();

    /**
     *  Constructor for the PortDescriptor object
     *
     * @since Tifosi2.0
     */
    public PortDescriptor()
    {
    }

    /**
     *  This interface method is called to get enumeration of all <code>OutPort</code>
     *  objects stored for this <code>PortDescriptor</code> object.
     *
     * @return Enumeration of OutPort objects.
     * @see #addOutPort(OutPort)
     * @see #removeOutPort(OutPort)
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public Enumeration getOutPorts()
    {
        return m_outPorts.elements();
    }

    /**
     *  This interface method is called to get enumeration of all <code>InPort</code>
     *  objects stored for this <code>PortDescriptor</code> object.
     *
     * @return Enumeration of InPort objects.
     * @see #addInPort(InPort)
     * @see #removeInPort(InPort)
     * @see #clearInPorts()
     * @since Tifosi2.0
     */
    public Enumeration getInPorts()
    {
        return m_inPorts.elements();
    }

    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.PORT_DESCRIPTOR;
    }

    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>PortDescriptor</code>, using the specified XML string.
     *
     * @param ports
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element ports)
        throws FioranoException
    {
        if (ports != null)
        {
            NodeList children = ports.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equals("InPort"))
                {
                    InPort inPort = new InPort();

                    inPort.setFieldValues((Element) child);
                    addInPort(inPort);
                }
                if (nodeName.equals("OutPort") || nodeName.equals("ErrPort"))
                {
                    OutPort outPort = new OutPort();

                    outPort.setFieldValues((Element) child);
                    addOutPort(outPort);
                }
            }
        }
        validate();
    }

    /**
     *  This interface method is called to add the specified object of <code>OutPort</code>
     *  to the list of outports, for this <code>PortDescriptor</code> object.
     *
     * @param outPort Object of OutPort to be added.
     * @see #removeOutPort(OutPort)
     * @see #getOutPorts()
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public void addOutPort(OutPort outPort)
    {
        Enumeration enums = m_outPorts.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            OutPort op = (OutPort) enums.nextElement();
            String nameOfOP = op.getPortName();

            if (nameOfOP.equalsIgnoreCase(outPort.getPortName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_outPorts.add(outPort);
    }

    /**
     *  This interface method is called to remove the specified object
     *  of <code>OutPort</code> from the list of outports, for
     *  this <code>PortDescriptor</code> object.
     *
     * @param port
     * @see #addOutPort(OutPort)
     * @see #getOutPorts()
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public void removeOutPort(OutPort port)
    {
        if (m_outPorts != null)
            m_outPorts.remove(port);
    }

    /**
     *  This interface method is called to clear the list of outports from this object
     *  of  <code>PortDescriptor</code>.
     *
     * @see #addOutPort(OutPort)
     * @see #removeOutPort(OutPort)
     * @see #getOutPorts()
     * @since Tifosi2.0
     */
    public void clearOutPorts()
    {
        if (m_outPorts != null)
            m_outPorts.clear();
    }

    /**
     *  This interface method is called to add the specified object of <code>InPort</code>
     *  to the list of inports, for this <code>PortDescriptor</code> object.
     *
     * @param inPort The feature to be added to the InPort attribute
     * @see #removeInPort(InPort)
     * @see #getInPorts()
     * @see #clearInPorts()
     * @since Tifosi2.0
     */
    public void addInPort(InPort inPort)
    {
        Enumeration enums = m_inPorts.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            InPort ip = (InPort) enums.nextElement();
            String nameOfIP = ip.getPortName();

            if (nameOfIP.equalsIgnoreCase(inPort.getPortName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_inPorts.add(inPort);
    }

    /**
     *  This interface method is called to remove the specified object of <code>InPort</code>
     *  from the list of inports, for this <code>PortDescriptor</code> object.
     *
     * @param port
     * @see #addInPort(InPort)
     * @see #getInPorts()
     * @see #clearInPorts()
     * @since Tifosi2.0
     */
    public void removeInPort(InPort port)
    {
        if (m_inPorts != null)
            m_inPorts.remove(port);
    }

    /**
     *  This interface method is called to clears the list of inports from this object
     *  of  <code>PortDescriptor</code>.
     *
     * @see #addInPort(InPort)
     * @see #removeInPort(InPort)
     * @see #getInPorts()
     * @since Tifosi2.0
     */
    public void clearInPorts()
    {
        if (m_inPorts != null)
            m_inPorts.clear();
    }

    /**
     *  This method tests whether this object of <code>PortDescriptor</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_inPorts != null)
        {
            Enumeration _enum = m_inPorts.elements();

            while (_enum.hasMoreElements())
            {
                InPort inPort = (InPort) _enum.nextElement();

                inPort.validate();
            }
        }
        if (m_outPorts != null)
        {
            Enumeration _enum = m_outPorts.elements();

            while (_enum.hasMoreElements())
            {
                OutPort outPort = (OutPort) _enum.nextElement();

                outPort.validate();
            }
        }
    }

    /**
     *  Resets the default values for this object
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_inPorts = new Vector();
        m_outPorts = new Vector();
    }

    /**
     *  This method is called to write this object of <code>PortDescriptor</code>
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

        if (m_inPorts != null && m_inPorts.size() > 0)
        {
            int num = m_inPorts.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                InPort port = (InPort) m_inPorts.elementAt(i);

                port.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            int num = m_outPorts.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                OutPort port = (OutPort) m_outPorts.elementAt(i);

                port.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }

    /**
     *  This is called to read this object <code>PortDescriptor</code>
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

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            InPort port = new InPort();

            port.fromStream(is, versionNo);
            m_inPorts.addElement(port);
        }

        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            OutPort port = new OutPort();

            port.fromStream(is, versionNo);
            m_outPorts.addElement(port);
        }
    }

    /**
     *  This utility method is used to compare this object of
     *  <code>PortDescriptor</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof PortDescriptor))
            return false;

        PortDescriptor rcvObj = (PortDescriptor) obj;

        if (rcvObj.getInPorts().equals(getInPorts())
            && rcvObj.getOutPorts().equals(getOutPorts()))
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
     * of <code>PortDescriptor</code>.
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
        strBuf.append("Port Descriptor");
        strBuf.append("[");
        if (m_inPorts != null)
        {
            strBuf.append("In ports = ");
            for (int i = 0; i < m_inPorts.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((InPort) m_inPorts.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_outPorts != null)
        {
            strBuf.append("Out ports = ");
            for (int i = 0; i < m_outPorts.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((OutPort) m_outPorts.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  Retruns the xml string equivalent of this object
     *
     * @param document instance of Xml Document.
     * @return org.w3c.dom.Node
     * @exception FioranoException thrown in case of error.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("PortDescriptor");

        if (m_inPorts != null && m_inPorts.size() > 0)
        {
            Enumeration _enum = m_inPorts.elements();

            while (_enum.hasMoreElements())
            {
                InPort inPort = (InPort) _enum.nextElement();
                Node inportNode = inPort.toJXMLString(document);

                root0.appendChild(inportNode);
            }
        }

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            Enumeration _enum = m_outPorts.elements();

            while (_enum.hasMoreElements())
            {
                OutPort outPort = (OutPort) _enum.nextElement();
                Node outportNode = outPort.toJXMLString(document);

                root0.appendChild(outportNode);
            }
        }
        return root0;
    }
}
