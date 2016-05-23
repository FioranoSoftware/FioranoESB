/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.UTFReaderWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class PortInstDescriptor extends DmiObject implements Externalizable
{
    Vector          m_inPorts = new Vector();

    Vector          m_outPorts = new Vector();

    // List which contains the list of port name which needs to be hidden when the component is shown in FEPO.
    ArrayList m_inputHiddenPorts = new ArrayList();
    ArrayList m_outputHiddenPorts = new ArrayList();

    /**
     *  Constructor for the PortInstDescriptor object
     *
     * @since Tifosi2.0
     */
    public PortInstDescriptor()
    {
    }

    /**
     * Returns output ports vector for object
     *
     * @return
     */
    public Vector getOutputPortsVector()
    {
        return m_outPorts;
    }

    /**
     * Returns input ports vector for object
     *
     * @return
     */
    public Vector getInputPortsVector()
    {
        return m_inPorts;
    }

    /**
     *  This interface method is called to get enumeration of all <code>OutPortInst</code>
     *  objects stored for this <code>PortInstDescriptor</code> object.
     *
     * @return Enumeration of OutPortInst objects.
     * @see #addOutPort(OutPortInst)
     * @see #removeOutPort(OutPortInst)
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public Enumeration getOutPorts()
    {
        return m_outPorts.elements();
    }

    /**
     *  This interface method is called to get enumeration of all <code>InPortInst</code>
     *  objects stored for this <code>PortInstDescriptor</code> object.
     *
     * @return Enumeration of InPortInst objects.
     * @see #addInPort(InPortInst)
     * @see #removeInPort(InPortInst)
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
        return DmiObjectTypes.PORT_DESCRIPTOR_INSTANCE;
    }

    /**
     *  Returns the port instance with parameter name
     *
     * @param portName
     * @return
     */
    public InPortInst getInPort(String portName)
    {
        Enumeration enums = m_inPorts.elements();

        while (enums.hasMoreElements())
        {
            InPortInst ip = (InPortInst) enums.nextElement();
            String nameOfIP = ip.getPortName();

            if (nameOfIP.equalsIgnoreCase(portName))
                return ip;
        }
        return null;
    }

    /**
     *  Returns the port instance with parameter name
     *
     * @param portName
     * @return
     */
    public OutPortInst getOutPort(String portName)
    {
        Enumeration enums = m_outPorts.elements();

        while (enums.hasMoreElements())
        {
            OutPortInst op = (OutPortInst) enums.nextElement();
            String nameOfOP = op.getPortName();

            if (nameOfOP.equalsIgnoreCase(portName))
                return op;
        }
        return null;
    }

    /**
     * Sets output ports vector for object
     *
     * @param outports
     */
    public void setOutputPortsVector(Vector outports)
    {
        m_outPorts = outports;
    }

    /**
     * Sets input ports vector for object
     *
     * @param inports
     */
    public void setInputPortsVector(Vector inports)
    {
        m_inPorts = inports;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>PortInstDescriptor</code>, using the specified XML string.
     *
     * @param ports
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element ports)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element ports = doc.getDocumentElement();

        if (ports != null)
        {
            NodeList children = ports.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equals("InPortInst"))
                {
                    InPortInst inPort = new InPortInst();

                    inPort.setFieldValues((Element) child);
                    addInPort(inPort);
                }
                if (nodeName.equals("OutPortInst"))
                {
                    OutPortInst outPort = new OutPortInst();

                    outPort.setFieldValues((Element) child);
                    addOutPort(outPort);
                }
            }
        }
        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException, FioranoException
    {
        if ( parser.markCursor(APSConstants.PORT_INST_DESCRIPTOR ))
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();

                if (nodeName.equals("InPortInst"))
                {
                    InPortInst inPort = new InPortInst();

                    inPort.setFieldValues(parser);
                    addInPort(inPort);
                }
                else if (nodeName.equals("OutPortInst"))
                {
                    OutPortInst outPort = new OutPortInst();

                    outPort.setFieldValues(parser);
                    addOutPort(outPort);
                }
                
            }
        }
        validate();
    }
    /**
     * @param out
     * @exception IOException
     */
    public void writeExternal(ObjectOutput out)
        throws IOException
    {
        toStream(out, 11);
    }

    /**
     * @param in
     * @exception IOException
     */
    public void readExternal(ObjectInput in)
        throws IOException
    {
        fromStream(in, 11);
    }

    /**
     *  This interface method is called to add the specified object of <code>OutPortInst</code>
     *  to the list of outports, for this <code>PortInstDescriptor</code> object.
     *
     * @param outPort Object of OutPortInst to be added.
     * @see #removeOutPort(OutPortInst)
     * @see #getOutPorts()
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public void addOutPort(OutPortInst outPort)
    {
        Enumeration enums = m_outPorts.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            OutPortInst op = (OutPortInst) enums.nextElement();
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
     *  of <code>OutPortInst</code> from the list of outports, for
     *  this <code>PortInstDescriptor</code> object.
     *
     * @param port
     * @see #addOutPort(OutPortInst)
     * @see #getOutPorts()
     * @see #clearOutPorts()
     * @since Tifosi2.0
     */
    public void removeOutPort(OutPortInst port)
    {
        if (m_outPorts != null)
        {
            m_outPorts.remove(port);
        }
    }


    /**
     *  This interface method is called to clear the list of outports from this object
     *  of  <code>PortInstDescriptor</code>.
     *
     * @see #addOutPort(OutPortInst)
     * @see #removeOutPort(OutPortInst)
     * @see #getOutPorts()
     * @since Tifosi2.0
     */
    public void clearOutPorts()
    {
        if (m_outPorts != null)
        {
            m_outPorts.clear();
        }
    }

    /**
     *  This interface method is called to add the specified object of <code>InPortInst</code>
     *  to the list of inports, for this <code>PortInstDescriptor</code> object.
     *
     * @param inPort The feature to be added to the InPort attribute
     * @see #removeInPort(InPortInst)
     * @see #getInPorts()
     * @see #clearInPorts()
     * @since Tifosi2.0
     */
    public void addInPort(InPortInst inPort)
    {
        Enumeration enums = m_inPorts.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            InPortInst ip = (InPortInst) enums.nextElement();
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
     *  This interface method is called to remove the specified object of <code>InPortInst</code>
     *  from the list of inports, for this <code>PortInstDescriptor</code> object.
     *
     * @param port
     * @see #addInPort(InPortInst)
     * @see #getInPorts()
     * @see #clearInPorts()
     * @since Tifosi2.0
     */
    public void removeInPort(InPortInst port)
    {
        if (m_inPorts != null)
        {
            m_inPorts.remove(port);
        }
    }


    /**
     *  This interface method is called to clears the list of inports from this object
     *  of  <code>PortInstDescriptor</code>.
     *
     * @see #addInPort(InPortInst)
     * @see #removeInPort(InPortInst)
     * @see #getInPorts()
     * @since Tifosi2.0
     */
    public void clearInPorts()
    {
        if (m_inPorts != null)
        {
            m_inPorts.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>PortInstDescriptor</code>
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
                InPortInst inPort = (InPortInst) _enum.nextElement();

                inPort.validate();
            }
        }
        if (m_outPorts != null)
        {
            Enumeration _enum = m_outPorts.elements();

            while (_enum.hasMoreElements())
            {
                OutPortInst outPort = (OutPortInst) _enum.nextElement();

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
     *  This method is called to write this object of <code>PortInstDescriptor</code>
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
                InPortInst port = (InPortInst) m_inPorts.elementAt(i);

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
                OutPortInst port = (OutPortInst) m_outPorts.elementAt(i);

                port.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        out.writeInt(m_inputHiddenPorts.size());
        Iterator iter = m_inputHiddenPorts.iterator();
        while(iter.hasNext())
            UTFReaderWriter.writeUTF(out, (String) iter.next());

        out.writeInt(m_outputHiddenPorts.size());
        iter = m_outputHiddenPorts.iterator();
        while(iter.hasNext())
            UTFReaderWriter.writeUTF(out, (String)iter.next());



    }


    /**
     *  This is called to read this object <code>PortInstDescriptor</code>
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
            InPortInst port = new InPortInst();

            port.fromStream(is, versionNo);
            m_inPorts.addElement(port);
        }

        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            OutPortInst port = new OutPortInst();

            port.fromStream(is, versionNo);
            m_outPorts.addElement(port);
        }

        size = is.readInt();
        for(int i=0; i<size; i++)
            m_inputHiddenPorts.add(UTFReaderWriter.readUTF(is));

        size = is.readInt();
        for(int i=0; i<size; i++)
            m_outputHiddenPorts.add(UTFReaderWriter.readUTF(is));
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>PortInstDescriptor</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof PortInstDescriptor))
        {
            return false;
        }

        PortInstDescriptor rcvObj = (PortInstDescriptor) obj;

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
     * of <code>PortInstDescriptor</code>.
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
                strBuf.append(((InPortInst) m_inPorts.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_outPorts != null)
        {
            strBuf.append("Out ports = ");
            for (int i = 0; i < m_outPorts.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((OutPortInst) m_outPorts.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }

        strBuf.append("Hidden input Ports = ");
        Iterator iter = m_inputHiddenPorts.iterator();
        while(iter.hasNext())
            strBuf.append(iter.next()).append(", ");

        strBuf.append("Hidden output Ports = ");
        iter = m_outputHiddenPorts.iterator();
        while(iter.hasNext())
            strBuf.append(iter.next()).append(", ");
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document Document to be converted
     * @return org.w3c.dom.Node
     * @exception FioranoException thrown in case of error.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("PortInstDescriptor");

        if (m_inPorts != null && m_inPorts.size() > 0)
        {
            Enumeration _enum = m_inPorts.elements();

            while (_enum.hasMoreElements())
            {
                InPortInst inPort = (InPortInst) _enum.nextElement();
                Node inportNode = inPort.toJXMLString(document);

                root0.appendChild(inportNode);
            }
        }

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            Enumeration _enum = m_outPorts.elements();

            while (_enum.hasMoreElements())
            {
                OutPortInst outPort = (OutPortInst) _enum.nextElement();
                Node outportNode = outPort.toJXMLString(document);

                root0.appendChild(outportNode);
            }
        }
        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start PortInstDescriptor
        writer.writeStartElement("PortInstDescriptor");

        if (m_inPorts != null && m_inPorts.size() > 0)
        {
            Enumeration enums = m_inPorts.elements();

            while (enums.hasMoreElements())
            {
                InPortInst inPort = (InPortInst) enums.nextElement();
                inPort.toJXMLString(writer);
            }
        }

        if (m_outPorts != null && m_outPorts.size() > 0)
        {
            Enumeration enums = m_outPorts.elements();

            while (enums.hasMoreElements())
            {
                OutPortInst outPort = (OutPortInst) enums.nextElement();
                outPort.toJXMLString(writer);
            }
        }
        //End PortInstDescriptor
        writer.writeEndElement();
    }

    /**
     * This API is used to set the Ports which has to be hidden from display when the component
     * is shown in the Tool. Typical Usage: Scheduling requires the input port to be disables or hidden.
     *
     * @param inPortArray
     * @param outPortArray
     */
    public void setHiddenPorts(String[] inPortArray, String[] outPortArray)
    {
        for(int i=0; i<inPortArray.length; i++)
            m_inputHiddenPorts.add(inPortArray[i]);
        for(int i=0; i<outPortArray.length; i++)
            m_outputHiddenPorts.add(outPortArray[i]);
    }

    /**
     * This API return the list of Input port Names as Strings to be hidden from showing in the tool.
     * @return list of Input port Names as Strings to be hidden
     */
    public String[] getInputHiddenPortNames()
    {
        String[] list = new String[m_inputHiddenPorts.size()];
        return (String[]) m_inputHiddenPorts.toArray(list);
    }

    /**
     * This API return the list of Output port Names as Strings to be hidden from showing in the tool.
     * @return list of Output port Names as Strings to be hidden
     */
    public String[] getOutputHiddenPortNames()
    {
        String[] list = new String[m_outputHiddenPorts.size()];
        return (String[]) m_outputHiddenPorts.toArray(list);
    }
}
