/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RuntimeArg extends DmiObject
{
    String          m_strArgName;

    String          m_strType;

    boolean         m_bIsRequired;

    boolean         m_isInMemorySupported = true;

    String          m_strValue;

    boolean         m_isProfiled = false;


    /**
     *  Constructor for the RuntimeArgs object
     *
     * @since Tifosi2.o
     */
    public RuntimeArg()
    {
        reset();
    }


    /**
     *  This interface method is called to get the name of runtime argument, represented
     *  by this object of <code>RuntimeArg</code>.
     *
     * @return The name of runtime argument
     * @see #setArgName(String)
     * @since Tifosi2.o
     */
    public String getArgName()
    {
        return m_strArgName;
    }


    /**
     *   This interface method is called to get the type of data contained in this
     *   runtime argument, represented by this object of <code>RuntimeArg</code>.
     *
     * @return type of runtime argument
     * @see #setType(String)
     * @since Tifosi2.o
     * @jmx.descriptor name="legalValues" value="Number,Integer,Boolean,Text"
     */
    public String getType()
    {
        return m_strType;
    }


    /**
     *  This interface method is called to check whether this runtime argument is
     *  required or not for the service.
     *
     * @return true if this runtime argument is required, false otherwise
     * @see #setRequired(boolean)
     * @since Tifosi2.o
     */
    public boolean isRequired()
    {
        return m_bIsRequired;
    }

    /**
     * Determines whether the support of this argument is available when the compoent is launched in-memory
     * @return boolean
     */
    public boolean isInMemorySupported()
    {
        return m_isInMemorySupported;
    }

    /**
     *  This interface method is called to check whether this runtime argument is
     * profiled. Which means this argument is a deployment specific argument.
     *
     * @return true if this runtime argument is required, false otherwise
     * @see #setRequired(boolean)
     * @since Tifosi2.o
     */
    public boolean isProfiled()
    {
        return m_isProfiled;
    }

    /**
     *  This interface method is called to get the value of runtime argument represented
     *  by this object of <code>RuntimeArg</code>.
     *
     * @return The value of runtime argument
     * @see #setValue(String)
     * @since Tifosi2.o
     */
    public String getValue()
    {
        return m_strValue;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.RUNTIME_ARG;
    }


    /**
     *  This interface method is called to set the specified string as name of runtime
     *  argument, represented by this object of <code>RuntimeArg</code>.
     *
     * @param argName The string to be set as runtime argument
     * @see #getArgName()
     * @since Tifosi2.o
     */
    public void setArgName(String argName)
    {
        m_strArgName = argName;
    }


    /**
     *  This interface method is called to set the specified string as type of data
     *  contained in this runtime argument, represented by this object of
     *  <code>RuntimeArg</code>.
     *
     * @param type The string to be set as type of runtime argument
     * @see #getType()
     * @since Tifosi2.o
     */
    public void setType(String type)
    {
        m_strType = type;
    }


    /**
     *  This interface method is called to set boolean specifying whether this runtime
     *  argument, is required or not for the service.
     *
     * @param isRequired The new isRequired value
     * @see #isRequired()
     * @since Tifosi2.o
     */
    public void setRequired(boolean isRequired)
    {
        m_bIsRequired = isRequired;
    }

    /**
     * Determines whether the support for this argument is valid when the component is launched in-memory.
     * @param isInMemorySupported
     */
    public void setInMemorySupported(boolean isInMemorySupported)
    {
        m_isInMemorySupported = isInMemorySupported;
    }

    /**
     *  This interface method is called to set boolean specifying whether this runtime
     *  argument, is required or not for the service.
     *
     * @param isProfiled The new isRequired value
     * @see #isRequired()
     * @since Tifosi2.o
     */
    public void setProfiled(boolean isProfiled)
    {
        m_isProfiled = isProfiled;
    }



    /**
     *  This interface method is called to set the specified string as value of runtime
     *  argument, represented by this object of <code>RuntimeArg</code>.
     *
     * @param value The string to be set as value of runtime argument
     * @see #getValue()
     * @since Tifosi2.o
     */
    public void setValue(String value)
    {
        m_strValue = value;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>RuntimeArg</code>, using the specified XML string.
     *
     * @param arg
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element arg)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element arg = doc.getDocumentElement();
        if (arg != null)
        {
            m_strArgName = arg.getAttribute("name");
            m_strType = arg.getAttribute("type");
            m_bIsRequired = XMLDmiUtil.getAttributeAsBoolean(arg, "isRequired");

           if ( m_strArgName.equalsIgnoreCase("JVM_PARAMS")){
                m_isInMemorySupported = false;
           }
           else{
                if ( arg.getAttributes().getNamedItem("isInMemorySupported") == null)
                    m_isInMemorySupported = true;
                else
                    m_isInMemorySupported = XMLDmiUtil.getAttributeAsBoolean(arg, "isInMemorySupported", m_isInMemorySupported);
           }
            m_isProfiled = XMLDmiUtil.getAttributeAsBoolean(arg, "isProfiled");
            m_strValue = XMLUtils.getNodeValueAsString(arg);
        }
        validate();
    }


    /**
     *  This method tests whether this object of <code>RuntimeArg</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strArgName == null || m_strType == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
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
        m_isInMemorySupported = true;
    }

    /**
     *  This method is called to write this object of <code>RuntimeArg</code>
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

        UTFReaderWriter.writeUTF(out, m_strArgName);
        UTFReaderWriter.writeUTF(out, m_strType);
        UTFReaderWriter.writeUTF(out, m_strValue);
        out.writeBoolean(m_bIsRequired);
        out.writeBoolean(m_isInMemorySupported);
        out.writeBoolean(m_isProfiled);

    }


    /**
     *  This is called to read this object <code>RuntimeArg</code>
     *  from the specified object of input stream.
     *
     * @param is DataInput object
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

        m_strArgName = UTFReaderWriter.readUTF(is);
        m_strType = UTFReaderWriter.readUTF(is);
        m_strValue = UTFReaderWriter.readUTF(is);
        m_bIsRequired = is.readBoolean();
        m_isInMemorySupported = is.readBoolean();
        m_isProfiled = is.readBoolean();
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>RuntimeArg</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof RuntimeArg))
        {
            return false;
        }

        RuntimeArg rcvObj = (RuntimeArg) obj;

        return DmiEqualsUtil.checkStringEquality(rcvObj.getArgName(), m_strArgName)
                && DmiEqualsUtil.checkStringEquality(rcvObj.getType(), m_strType)
                && DmiEqualsUtil.checkStringEquality(rcvObj.getValue(), m_strValue);
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>RuntimeArg</code>.
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
        strBuf.append("Runtime Arguments Details ");
        strBuf.append("[");
        strBuf.append("Is Required = ");
        strBuf.append(m_bIsRequired);
        strBuf.append(", ");
        strBuf.append("Is In Memory Supported = ");
        strBuf.append(m_isInMemorySupported);
        strBuf.append(", ");
        strBuf.append("Is Profiled = ");
        strBuf.append(m_isProfiled);
        strBuf.append(", ");
        strBuf.append("Argument name = ");
        strBuf.append(m_strArgName);
        strBuf.append(", ");
        strBuf.append("type = ");
        strBuf.append(m_strType);
        strBuf.append(", ");
        strBuf.append("value = ");
        strBuf.append(m_strValue);
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
        Node root0 = document.createElement("Argument");

        ((Element) root0).setAttribute("name", m_strArgName);
        ((Element) root0).setAttribute("type", m_strType);
        ((Element) root0).setAttribute("isRequired", "" + m_bIsRequired);
        ((Element) root0).setAttribute("isInMemorySupported", "" + m_isInMemorySupported);
        ((Element) root0).setAttribute("isProfiled", "" + m_isProfiled);

        Node pcdata = document.createTextNode(m_strValue);

        if (pcdata != null)
            root0.appendChild(pcdata);
        return root0;
    }
}
