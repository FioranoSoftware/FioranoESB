/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.DmiErrorCodes;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
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
import java.util.*;

public class DeploymentProfile  extends DmiObject
{
    // name of the deployment profile
    private String m_deploymentLabel;

    //List of arguments. Array of <Argument>
    private Vector m_deploymentConfigurations = new Vector();

    /**
     *  This method gets an enumeration of all the objects of <code>Argument</code>,
     *  contained in this <code>RuntimeArgs</code> object.
     *
     * @return Enumeration of Argument objects
     * @see #addDeploymentConfiguration(Argument)
     * @since Tifosi2.0
     */
    public ListIterator getDeploymentConfigurations()
    {
        return m_deploymentConfigurations.listIterator();
    }
    /**
     *  This method gets the name of the profile
     *
     * @return Enumeration of Argument objects
     * @see #addDeploymentConfiguration(Argument)
     * @since Tifosi2.0
     */
    public String getDeploymentLabel()
    {
        return m_deploymentLabel;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APP_DEPLOYMENT_PROFILE;
    }

    /**
     *  This method adds object of <code>Argument</code> to this
     *  <code>RuntimeArgs</code> object.
     *
     * @param arg Object of Argument that is to be added
     * @see #getDeploymentConfigurations()
     * @since Tifosi2.0
     */
    public void addDeploymentConfiguration(Argument arg)
    {
        m_deploymentConfigurations.addElement(arg);
    }

    /**
     * sets the deployment label.
     * @param label
     */
    public void setDeploymentLabel(String label)
    {
        m_deploymentLabel = label;
    }


    /**
     *  This method tests whether this object of <code>RuntimeArgs</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @exception com.fiorano.openesb.utils.exception.FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_deploymentConfigurations == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        Iterator iterator = m_deploymentConfigurations.iterator();

        while (iterator.hasNext())
        {
            Argument arg = (Argument) iterator.next();

            arg.validate();
        }
    }

    /**
     *  This method sets all the fieldValues of this object of
     *  <code>RuntimeArgs</code>, using the specified XML string.
     * This uses DOM Parsing.
     *
     * @param runtimeArgsElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element runtimeArgsElement)
        throws FioranoException
    {
        if (runtimeArgsElement != null)
        {
            NodeList list = runtimeArgsElement.getChildNodes();
            Node child;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Argument"))
                {
                    Argument arg = new Argument();
                    arg.setFieldValues((Element) child);
                    m_deploymentConfigurations.addElement(arg);
                }
                else if (nodeName.equalsIgnoreCase("Label"))
                {
                    m_deploymentLabel = XMLUtils.getNodeValueAsString(child);
                }
            }
        }

        validate();
    }

    /**
     * Populate the values of the DMI using the Stax PArser.
     *
     * @param parser
     * @throws XMLStreamException
     * @throws FioranoException
     */
    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.DEPLOYMENT_PROFILE) )
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase("Argument"))
                {
                    Argument arg = new Argument();

                    arg.setFieldValues(parser);
                    m_deploymentConfigurations.addElement(arg);
                }
                else if (nodeName.equalsIgnoreCase("Label"))
                {
                    m_deploymentLabel = parser.getCData();

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
     *  This method writes this object of <code>RuntimeArgs</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.
     * @see #fromStream(DataInput, int version)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        if (m_deploymentConfigurations != null)
        {
            int length = m_deploymentConfigurations.size();
            out.writeInt(length);
            Iterator arguments = m_deploymentConfigurations.iterator();
            while (arguments.hasNext())
            {
                Argument argument = (Argument) arguments.next();
                argument.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
        if(m_deploymentLabel == null)
            writeUTF(out, "");
        else
            writeUTF(out, m_deploymentLabel);
    }


    /**
     *  This method reads this object <code>RuntimeArgs</code> from the
     *  specified input stream object.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.
     * @see #toStream(DataOutput, int)
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        int tempInt;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                Argument argument = new Argument();
                argument.fromStream(is, versionNo);
                m_deploymentConfigurations.addElement(argument);
            }
        }
        String utf = readUTF(is);
        if(utf.trim().length() > 0)
            m_deploymentLabel = utf;
        else
            m_deploymentLabel = null;
    }

    /**
     *  This utility method is used to get the hashtable equivalent of this
     *  <code>RuntimeArgs</code> object. All the runtime arguments are Strings.
     *
     * @return The hashOfRuntimeArgs value
     * @see #toString()
     * @since Tifosi2.0
     */
    public Hashtable getHashOfDeploymentConfigurations()
    {
        ListIterator args = getDeploymentConfigurations();
        Hashtable retVal = new Hashtable();

        while (args.hasNext())
        {
            Argument arg = (Argument) args.next();

            if (arg == null || arg.getName() == null || arg.getValue() == null)
                continue;
            String name = arg.getName();
            String value = arg.getIsAdvanced() + "::" + arg.getValue();

            retVal.put(name, value);
        }
        return retVal;
    }

    /**
     * This utility method is used to get the String representation of this
     * <code>RuntimeArgs</code> object.
     *
     * @return The String representation of this object.
     * @see #getHashOfDeploymentConfigurations()
     * @since Tifosi2.0
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        Hashtable hash = getHashOfDeploymentConfigurations();

        if (hash == null)
        {
            buf.append("Runtime arguments are null");
            return buf.toString();
        }
        Enumeration keys = hash.keys();
        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            buf.append(key).append(" : ").append(hash.get(key));
        }
        return buf.toString();
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
        Node root0 = document.createElement(APSConstants.DEPLOYMENT_PROFILE);

        if (m_deploymentLabel != null)
            ((Element) root0).setAttribute("label", m_deploymentLabel);
        else
           ((Element) root0).setAttribute("label", "null");

        if (m_deploymentConfigurations != null && m_deploymentConfigurations.size() > 0)
        {
            Iterator arguments = m_deploymentConfigurations.iterator();
            while (arguments.hasNext())
            {
                Argument arg = (Argument) arguments.next();
                Node pcData = arg.toJXMLString(document);
                root0.appendChild(pcData);
            }
        }
        return root0;
    }

    /**
     *  Returns the XML string equivalent of this object.
     *
     * @param writer The input XML Stream writer.
     * @exception FioranoException if an error occurs while creating the element
     *                             node.
     */
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException
    {
        writer.writeStartElement(APSConstants.DEPLOYMENT_PROFILE);

        if (m_deploymentLabel != null)
            writer.writeAttribute("label", m_deploymentLabel);
        else
            writer.writeAttribute("label", "null");

        if (m_deploymentConfigurations != null && m_deploymentConfigurations.size() > 0)
        {
            Iterator configurations = m_deploymentConfigurations.iterator();
            while (configurations.hasNext())
            {
                Argument arg = (Argument) configurations.next();
                arg.toJXMLString(writer);
            }
        }
        writer.writeEndElement();
    }
}
