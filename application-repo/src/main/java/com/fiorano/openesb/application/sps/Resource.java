/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Resource extends DmiObject
{
    //the name of the resource
    String          m_strResourceName;
    //deploy path of the resource
    boolean         m_bIsRequiredForConfiguration;
    // whether req for execution
    boolean         m_bIsRequiredForExecution;
    // compatible platforms
    Vector          m_strApplicableOperatingSystem = new Vector();


    /**
     *  Constructor for the ResourceInfo object
     *
     * @since Tifosi2.0
     */
    public Resource()
    {
        reset();
    }


    /**
     *  Interface method to get the resource name for this object of <code>Resource</code>.
     *
     * @return the name of the resource
     * @see #setResourceName(String)
     * @since Tifosi2.0
     */
    public String getResourceName()
    {
        return m_strResourceName;
    }


    /**
     *  This interface method is called to get enumeration of the list of
     *  ApplicableOperatingSystem, for the resource specified by this object of
     *  <code>Resource</code>.
     *
     * @return Enumeration of ApplicableOperatingSystem for this resource
     * @see #addApplicableOperatingSystem(String)
     * @see #removeApplicableOperatingSystem(String)
     * @see #clearApplicableOperatingSystem()
     * @since Tifosi2.0
     */
    public Enumeration getApplicableOperatingSystems()
    {
        if (m_strApplicableOperatingSystem == null)
        {
            m_strApplicableOperatingSystem = new Vector();
        }
        return m_strApplicableOperatingSystem.elements();
    }


    /**
     *  Interface method to check whether or not this object of <code>Resource</code>
     *  is required for configuration.
     *
     * @return true if this resource is required for configuration, false
     *           otherwise.
     * @see #setRequiredForConfiguration(boolean)
     * @since Tifosi2.0
     */
    public boolean isRequiredForConfiguration()
    {
        return m_bIsRequiredForConfiguration;
    }


    /**
     *  Interface method to check whether or not this object of <code>Resource</code>
     *  is required for Execution.
     *
     * @return true if this resource is required for execution, false
     *           otherwise.
     * @see #setRequiredForExecution(boolean)
     * @since Tifosi2.0
     */
    public boolean isRequiredForExecution()
    {
        return m_bIsRequiredForExecution;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.RESOURCE;
    }


    /**
     *  Interface method to set the specified string as resource name for this
     *  object of <code>Resource</code>.
     *
     * @param resName The string to be set as resource name
     * @see #getResourceName()
     * @since Tifosi2.0
     */
    public void setResourceName(String resName)
    {
        m_strResourceName = resName;
    }


    /**
     *  Interface method to set boolean, specifying whether or not this object
     *  of <code>Resource</code> is required for configuration.
     *
     * @param flag boolean specifying whether or not this resource is required
     *                      for configuration
     * @see #isRequiredForConfiguration()
     * @since Tifosi2.0
     */
    public void setRequiredForConfiguration(boolean flag)
    {
        m_bIsRequiredForConfiguration = flag;
    }


    /**
     *  Interface method to set boolean specifying whether or not this object
     *  of <code>Resource</code> is required for Execution.
     *
     * @param flag boolean specifying whether or not this resource is required
     *              for Execution
     * @see #isRequiredForExecution()
     * @since Tifosi2.0
     */
    public void setRequiredForExecution(boolean flag)
    {
        m_bIsRequiredForExecution = flag;
    }


    /**
     *  This interface method is called to set all the fieldValues of this
     *  object of <code>Resource</code>, using the specified XML string.
     *
     * @param resource XML String.
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element resource)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element resource = doc.getDocumentElement();
        if (resource != null)
        {
            m_bIsRequiredForConfiguration = XMLDmiUtil.getAttributeAsBoolean(
                resource, "requiredForConfiguration");
            m_bIsRequiredForExecution = XMLDmiUtil.getAttributeAsBoolean(
                resource, "requiredForExecution");

            NodeList children = resource.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(child);

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    m_strResourceName = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("OS"))
                {
                    addApplicableOperatingSystem(nodeValue);
                }
            }
        }
        validate();
    }


    /**
     *  This interface method is called to add the specified string to the list of
     *  ApplicableOperatingSystem, for the resource specified by this object of
     *  <code>Resource</code>.
     *
     * @param opSys The string to be added to the list of ApplicableOperatingSystem
     * @see #removeApplicableOperatingSystem(String)
     * @see #getApplicableOperatingSystems()
     * @see #clearApplicableOperatingSystem()
     * @since Tifosi2.0
     */
    public void addApplicableOperatingSystem(String opSys)
    {
        if (m_strApplicableOperatingSystem == null)
        {
            m_strApplicableOperatingSystem = new Vector();
        }
        m_strApplicableOperatingSystem.add(opSys);
    }


    /**
     *  This interface method is called to remove the specified OS from the list of
     *  ApplicableOperatingSystem, for the resource specified by this object of
     *  <code>Resource</code>.
     *
     * @param os The os to be removed from the list of ApplicableOperatingSystem
     * @see #addApplicableOperatingSystem(String)
     * @see #getApplicableOperatingSystems()
     * @see #clearApplicableOperatingSystem()
     * @since Tifosi2.0
     */
    public void removeApplicableOperatingSystem(String os)
    {
        if (m_strApplicableOperatingSystem != null)
        {
            m_strApplicableOperatingSystem.remove(os);
        }
    }


    /**
     *  This interface method is called to clear the list of ApplicableOperatingSystem,
     *  for the resource specified by this object of <code>Resource</code>.
     *
     * @see #addApplicableOperatingSystem(String)
     * @see #removeApplicableOperatingSystem(String)
     * @see #getApplicableOperatingSystems()
     * @since Tifosi2.0
     */
    public void clearApplicableOperatingSystem()
    {
        if (m_strApplicableOperatingSystem != null)
        {
            m_strApplicableOperatingSystem.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>Resource</code> has the
     *  required(mandatory) fields set, before inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strResourceName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  Resets the attributes of this object
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_strApplicableOperatingSystem = new Vector();
        m_bIsRequiredForExecution = true;
    }


    /**
     *  This method is called to write this object of <code>Resource</code> to
     *  the specified output stream object.
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

        writeUTF(out, m_strResourceName);
        out.writeBoolean(m_bIsRequiredForConfiguration);
        out.writeBoolean(m_bIsRequiredForExecution);

        if (m_strApplicableOperatingSystem != null && m_strApplicableOperatingSystem.size() > 0)
        {
            int num = m_strApplicableOperatingSystem.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                writeUTF(out, (String) m_strApplicableOperatingSystem.elementAt(i));
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>Resource</code> from the
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

        m_strResourceName = readUTF(is);
        m_bIsRequiredForConfiguration = is.readBoolean();
        m_bIsRequiredForExecution = is.readBoolean();

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            String name = readUTF(is);

            m_strApplicableOperatingSystem.addElement(name);
        }
    }


    /**
     *  This utility method is used to compare this object of <code>Resource</code>
     *  with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Resource))
        {
            return false;
        }

        Resource rcvObj = (Resource) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getResourceName(), m_strResourceName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *  Compares the attribute of this <code>Resource</code> object to the
     *  specified <code>Resource</code> object.
     *
     * @param res Resource object to be compared.
     * @return True if the attributes match, false otherwise.
     * @since Tifosi2.0
     */
    public boolean compareResourceAttributes(Resource res)
    {
        boolean equal = false;
        Vector thisOS = m_strApplicableOperatingSystem;
        Vector rcvdOS = new Vector();
        Enumeration _enum = res.getApplicableOperatingSystems();

        while (_enum.hasMoreElements())
        {
            rcvdOS.addElement(_enum.nextElement());
        }

        if (!rcvdOS.equals(thisOS))
            return false;

        if ((m_bIsRequiredForConfiguration && res.isRequiredForConfiguration())
            || (!m_bIsRequiredForConfiguration && !res.isRequiredForConfiguration()))
            equal = true;

        if (equal)
        {
            if ((m_bIsRequiredForExecution && res.isRequiredForExecution())
                || (!m_bIsRequiredForExecution && !res.isRequiredForExecution()))
                return true;
        }
        else
            return false;

        return false;
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>Resource</code>.
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
        strBuf.append("Resource Details ");
        strBuf.append("[");
        strBuf.append("Is Required For Configuration = ");
        strBuf.append(m_bIsRequiredForConfiguration);
        strBuf.append(", ");
        strBuf.append("Is Required For Execution = ");
        strBuf.append(m_bIsRequiredForExecution);
        strBuf.append(", ");
        strBuf.append("Resource name = ");
        strBuf.append(m_strResourceName);
        strBuf.append(", ");
        if (m_strApplicableOperatingSystem != null)
        {
            strBuf.append("Appplicable operating system = ");
            for (int i = 0; i < m_strApplicableOperatingSystem.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append((String) m_strApplicableOperatingSystem.elementAt(i));
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
        Node root0 = document.createElement("Resource");

        ((Element) root0).setAttribute("requiredForConfiguration",
            "" + m_bIsRequiredForConfiguration);
        ((Element) root0).setAttribute("requiredForExecution",
            "" + m_bIsRequiredForExecution);

        Node child = null;

        child = XMLDmiUtil.getNodeObject("Name", m_strResourceName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        // adding OSs
        XMLDmiUtil.addVectorValues("OS", m_strApplicableOperatingSystem, document, root0);

        return root0;
    }

}
