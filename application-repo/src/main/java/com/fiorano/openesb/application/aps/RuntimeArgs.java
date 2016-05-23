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
import org.w3c.dom.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class RuntimeArgs extends DmiObject
{
    Vector          m_arguments = new Vector();

    String          m_strUserDefinedPropertySheet;

    // Stores the various deployment profile specific configuration parameters.
    // its an arrayr of <class>DeploymentProfileM</class> - Prasanna
    Vector       m_deploymentProfiles = new Vector();

    /**
     * This is the constructor of the <code>RuntimeArgs</code> class.
     *
     * @since Tifosi2.0
     */
    public RuntimeArgs()
    {
    }


    /**
     *  This method gets an enumeration of all the objects of <code>Argument</code>,
     *  contained in this <code>RuntimeArgs</code> object.
     *
     * @return Enumeration of Argument objects
     * @see #addRuntimeArg(Argument)
     * @since Tifosi2.0
     */
    public Enumeration getRuntimeArgs()
    {
        return m_arguments.elements();
    }

    /**
     *  This method gets an iterator of all the deployment configurations for each profile on this service.
     *
     * @return Enumeration of Argument objects
     * @see #addDeploymentProfile(DeploymentProfile)
     * @since Tifosi2.0
     */
    public Enumeration getDeploymentProfiles()
    {
        return m_deploymentProfiles.elements();
    }

    /**
     *  This method get the userdefined propertysheet for corresponding service
     *  instance of this <code>RuntimeArgs</code> object.
     *
     * @return Returns user defned configuration of the service.
     * @see #setUserDefinedPropertySheet(String)
     * @since Tifosi2.0
     */
    public String getUserDefinedPropertySheet()
    {
        return m_strUserDefinedPropertySheet;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APP_RUNTIME_ARGS;
    }


    /**
     *  This utility method is used to get the hashtable equivalent of this
     *  <code>RuntimeArgs</code> object. All the runtime arguments are Strings.
     *
     * @return The hashOfRuntimeArgs value
     * @see #toString()
     * @since Tifosi2.0
     */
    public Hashtable getHashOfRuntimeArgs()
    {
        Enumeration args = getRuntimeArgs();
        Hashtable retVal = new Hashtable();

        while (args.hasMoreElements())
        {
            Argument arg = (Argument) args.nextElement();

            if (arg == null || arg.getName() == null || arg.getValue() == null)
                continue;
            String name = arg.getName();
            String value = arg.getIsAdvanced() + "::" + arg.getValue();

            retVal.put(name, value);
        }
        return retVal;
    }

    /**
     *  This method sets the specified string as userdefined
     *  propertysheet, for corresponding service instance of this
     *  <code>RuntimeArgs</code> object.
     *
     * @param cdata will reperesent the user defined configuration for the service.
     * @see #getUserDefinedPropertySheet()
     * @since Tifosi2.0
     */
    public void setUserDefinedPropertySheet(String cdata)
    {
        m_strUserDefinedPropertySheet = cdata;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>RuntimeArgs</code>, using the specified XML string.
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
                    m_arguments.add(arg);
                }
                else if (nodeName.equalsIgnoreCase("UserDefinedPropertySheet"))
                {
                    m_strUserDefinedPropertySheet = XMLUtils.getNodeValueAsString(child);
//                    System.out.println("In RuntimeArgs :: " + m_strUserDefinedPropertySheet);
                }
                else if(nodeName.equalsIgnoreCase(APSConstants.DEPLOYMENT_PROFILE))
                {
                    DeploymentProfile profile = new DeploymentProfile();
                    profile.setFieldValues((Element)child);
                    m_deploymentProfiles.add(profile);
                }
            }
        }

        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.RUNTIME_ARGS) )
        {
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase("Argument"))
                {
                    Argument arg = new Argument();

                    arg.setFieldValues(parser);
                    m_arguments.add(arg);
                }
                else if (nodeName.equalsIgnoreCase("UserDefinedPropertySheet"))
                {
                    m_strUserDefinedPropertySheet = parser.getCData();//XMLUtils.getNodeValueAsString(child);

                }
                else if(nodeName.equalsIgnoreCase(APSConstants.DEPLOYMENT_PROFILE))
                {
                    DeploymentProfile profile = new DeploymentProfile();
                    profile.setFieldValues(parser);
                    m_deploymentProfiles.add(profile);
                }
            }

        }
        validate();
    }

    /**
     *  This method adds object of <code>Argument</code> to this
     *  <code>RuntimeArgs</code> object.
     *
     * @param arg Object of Argument that is to be added
     * @see #getRuntimeArgs()
     * @since Tifosi2.0
     */
    public void addRuntimeArg(Argument arg)
    {
        m_arguments.add(arg);
    }

    /**
     *  This method adds object of <code>Argument</code> to this
     *  <code>RuntimeArgs</code> object.
     *
     * @param profile Object of Argument that is to be added
     * @see #getRuntimeArgs()
     * @since Tifosi2.0
     */
    public void addDeploymentProfile(DeploymentProfile profile)
    {
        m_arguments.add(profile);
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
     *  This method tests whether this object of <code>RuntimeArgs</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_arguments == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        Enumeration enums = m_arguments.elements();

        while (enums.hasMoreElements())
        {
            Argument arg = (Argument) enums.nextElement();

            arg.validate();
        }

        Enumeration profiles = m_deploymentProfiles.elements();
        while(profiles.hasMoreElements())
        {
            DeploymentProfile profile = (DeploymentProfile) profiles.nextElement();
            profile.validate();
        }
    }


    /**
     *  This method writes this object of <code>RuntimeArgs</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.
     * @see #fromStream(DataInput, int)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        if (m_arguments != null)
        {
            int length = m_arguments.size();
            out.writeInt(length);
            Enumeration arguments = m_arguments.elements();
            while (arguments.hasMoreElements())
            {
                Argument argument = (Argument) arguments.nextElement();
                argument.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        if(m_deploymentProfiles != null)
        {
            int length = m_deploymentProfiles.size();
            out.writeInt(length);
            Enumeration profiles = m_deploymentProfiles.elements();
            while(profiles.hasMoreElements())
            {
                DeploymentProfile profile = (DeploymentProfile) profiles.nextElement();
                profile.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        writeUTF(out, m_strUserDefinedPropertySheet);
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
                m_arguments.add(argument);
            }
        }
        if((tempInt = is.readInt()) != 0)
        {
            for(int i =0; i< tempInt; i++)
            {
                DeploymentProfile profile = new DeploymentProfile();
                profile.fromStream(is, versionNo);
                m_deploymentProfiles.add(profile);
            }
        }
        m_strUserDefinedPropertySheet = readUTF(is);
    }

    /**
     * This utility method is used to get the String representation of this
     * <code>RuntimeArgs</code> object.
     *
     * @return The String representation of this object.
     * @see #getHashOfRuntimeArgs()
     * @since Tifosi2.0
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        Hashtable hash = getHashOfRuntimeArgs();

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
        Node root0 = document.createElement("RuntimeArguments");

        if (m_strUserDefinedPropertySheet != null)
        {
            Element elem = document.createElement("UserDefinedPropertySheet");
            CDATASection cdata = document.createCDATASection(m_strUserDefinedPropertySheet);
            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if (m_arguments != null && m_arguments.size() > 0)
        {
            Enumeration enums = m_arguments.elements();
            while (enums.hasMoreElements())
            {
                Argument arg = (Argument) enums.nextElement();
                Node pcData = arg.toJXMLString(document);
                root0.appendChild(pcData);
            }
        }

        if(m_deploymentProfiles != null && m_deploymentProfiles.size() >0 )
        {
            Enumeration profiles = m_deploymentProfiles.elements();
            while(profiles.hasMoreElements())
            {
                DeploymentProfile profile = (DeploymentProfile) profiles.nextElement();
                Node pcData = profile.toJXMLString(document);
                root0.appendChild(pcData);
            }
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException
    {
        //Start RuntimeArguments
        writer.writeStartElement("RuntimeArguments");

        if (m_strUserDefinedPropertySheet != null)
        {
            writer.writeStartElement("UserDefinedPropertySheet");
            writer.writeCData(m_strUserDefinedPropertySheet);
            writer.writeEndElement();
        }

        if (m_arguments != null && m_arguments.size() > 0)
        {
            Enumeration enums = m_arguments.elements();
            while (enums.hasMoreElements())
            {
                Argument arg = (Argument) enums.nextElement();
                arg.toJXMLString(writer);
            }
        }

        if(m_deploymentProfiles != null && m_deploymentProfiles.size() > 0)
        {
            Enumeration profiles = m_deploymentProfiles.elements();
            while(profiles.hasMoreElements())
            {
                DeploymentProfile profile = (DeploymentProfile) profiles.nextElement();
                profile.toJXMLString(writer);
            }
        }
        //End RuntimeArguments
        writer.writeEndElement();
    }
}
