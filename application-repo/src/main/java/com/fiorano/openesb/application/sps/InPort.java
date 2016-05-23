/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class InPort extends DmiObject
{

    //BudId 12697
    public final static int TYPE_DTD = 0;
    public final static int TYPE_XSD = 1;
    public final static int TYPE_NONE = -1;
    // name of the port
    String          m_strPortName;
    //description
    String          m_strDscription;
    //input xsd
    String          m_strXSD;
    //any params
    Vector          m_params = new Vector();
    //java class
    String          m_strJavaClass;
    boolean         m_bIsSyncRequestType;

    /**
     *  Constructor for the InPortInfo object
     *
     * @since Tifosi2.0
     */
    public InPort()
    {
        reset();
    }

    /**
     *  Gets the rootElemName attribute of the InPort object
     *
     * @return The rootElemName value
     */
    public String getRootElemName()
    {
        return getParamValue("rootElementName");
    }

    /**
     *  Gets the structureType attribute of the InPort object
     *
     * @return The structureType value
     */
    public int getStructureType()
    {
        String type = getParamValue("structureType");

        if (type == null)
        {
            return TYPE_NONE;
        }
        return Integer.parseInt(type);
    }

    /**
     *  Gets the rootElemNS attribute of the InPort object
     *
     * @return The rootElemNS value
     */
    public String getRootElemNS()
    {
        return getParamValue("rootElementNS");
    }

    /**
     *  Gets the externalXSD attribute of the InPort object
     *
     * @param namespace Description of the Parameter
     * @return The externalXSD value
     */
    public String getExternalXSD(String namespace)
    {
        return getParamValue("External_" + namespace);
    }

    /**
     * Returns external XS ds for object
     *
     * @return
     */
    public Map getExternalXSDs()
    {
        Map map = new HashMap();
        Enumeration enumerator = getExtNamespaces();

        while (enumerator.hasMoreElements())
        {
            String namespace = (String) enumerator.nextElement();

            map.put(namespace, getExternalXSD(namespace));
        }
        return map;
    }

    /**
     *  Gets the extNamespaces attribute of the InPort object
     *
     * @return The extNamespaces value
     */
    public Enumeration getExtNamespaces()
    {
        Vector v = new Vector();
        int length = "External_".length();

        int size = m_params.size();

        for (int i = 0; i < size; i++)
        {
            Param param = (Param) m_params.get(i);

            if (param.getParamName().startsWith("External_"))
            {
                v.add(param.getParamName().substring(length));
            }
        }

        return v.elements();
    }

    /**
     *  This interface method is called to check whether synchronous request
     *  reply is supported or not by the inport specified by this object of
     *  <code>InPort</code>.
     *
     * @return true if synchronous request reply is supported by this InPort
     * @see #setIsSyncRequestType(boolean)
     * @since Tifosi2.0
     */
    public boolean isSyncRequestType()
    {
        return m_bIsSyncRequestType;
    }

    /**
     *  This interface method is called to get name of the inport specified by
     *  this object of <code>InPort</code>.
     *
     * @return the name of inport
     * @see #setPortName(String)
     * @since Tifosi2.0
     */
    public String getPortName()
    {
        return m_strPortName;
    }

    /**
     *  This interface method is called to get the description of the inport
     *  specified by this object of <code>InPort</code>.
     *
     * @return The description of inport
     * @see #setDescription(String)
     * @since Tifosi2.0
     */
    public String getDescription()
    {
        return m_strDscription;
    }

    /**
     *  This interface method is called to get the XSD set for inport, specified
     *  by this object of <code>InPort</code>.
     *
     * @return The xSD specified for this port
     * @see #setXSD(String)
     * @since Tifosi2.0
     */
    public String getXSD()
    {
        return m_strXSD;
    }

    /**
     *  This interface method is called to get enumeration of all the objects of
     *  <code>Param</code>, for this object of <code>InPort</code>.
     *
     * @return Enumeration of all Param objects
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #clearParam()
     * @since Tifosi2.0
     */
    public Enumeration getParams()
    {
        if (m_params == null)
        {
            m_params = new Vector();
        }
        return m_params.elements();
    }

    /**
     *  This interface method is called to get the vector of all the bjects of
     *  <code>Param</code> for this object of <code>InPort</code>.
     *
     * @return Vector of all Param objects
     * @see #addParam(Param)
     * @see #getParams()
     * @see #removeParam(Param)
     * @see #clearParam()
     * @since Tifosi3.0
     */
    public Vector getParamsVector()
    {
        return m_params;
    }

    /**
     *  This interface method is called to get the javaClass set for this <code>InPort</code>
     *  .
     *
     * @return javaClass for this OutPort.
     * @see #setJavaClass(String)
     * @since Tifosi2.0
     */
    public String getJavaClass()
    {
        return m_strJavaClass;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.INPORT;
    }

    /**
     *  Gets the paramWithName attribute of the InPort object
     *
     * @param name Description of the Parameter
     * @return The paramWithName value
     */
    public Param getParamWithName(String name)
    {
        return Param.getParamWithName(m_params, name);
    }

    /**
     *  Gets the paramValue attribute of the InPort object
     *
     * @param name Description of the Parameter
     * @return The paramValue value
     */
    public String getParamValue(String name)
    {
        return Param.getParamValue(m_params, name);
    }

    /**
     *  Sets the rootElemName attribute of the InPort object
     *
     * @param value The new rootElemName value
     */
    public void setRootElemName(String value)
    {
        setParamValue("rootElementName", value);
    }

    /**
     *  Sets the structureType attribute of the InPort object
     *
     * @param type The new structureType value
     */
    public void setStructureType(int type)
    {
        setParamValue("structureType", "" + type);
    }

    /**
     *  Sets the rootElemNS attribute of the InPort object
     *
     * @param value The new rootElemNS value
     */
    public void setRootElemNS(String value)
    {
        setParamValue("rootElementNS", value);
    }

    /**
     *  This interface method is called to set the boolean specifying whether
     *  synchronous request reply is supported or not, by the inport specified
     *  by this object of <code>InPort</code>.
     *
     * @param isSyncRequestType The boolean specifying if synchronous
     *      request_Reply is supported by this InPort or not.
     * @see #isSyncRequestType()
     * @since Tifosi2.0
     */
    public void setIsSyncRequestType(boolean isSyncRequestType)
    {
        m_bIsSyncRequestType = isSyncRequestType;
    }

    /**
     *  This interface method is called to set the specified string as the name
     *  of inport, specified by this object of <code>InPort</code>.
     *
     * @param prtName The string to be set as the name of this inport
     * @see #getPortName()
     * @since Tifosi2.0
     */
    public void setPortName(String prtName)
    {
        m_strPortName = prtName.toUpperCase();
    }

    /**
     *  This interface method is called to set the specified string as
     *  description of the inport, specified by this object of <code>InPort</code>
     *
     * @param description The string to be set as description of inport
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setDescription(String description)
    {
        m_strDscription = description;
    }

    /**
     *  This interface method is called to set the specified string as XSD for
     *  inport, specified by this object of <code>InPort</code>.
     *
     * @param xsd The string to be set as xSD for this port
     * @see #getXSD()
     * @since Tifosi2.0
     */
    public void setXSD(String xsd)
    {
        m_strXSD = xsd;
    }

    /**
     *  This interface method is called to set the specified string as javaClass
     *  for this <code>InPort</code>.
     *
     * @param javaClass the string to be set as JavaClass
     * @see #getJavaClass()
     * @since Tifosi2.0
     */
    public void setJavaClass(String javaClass)
    {
        m_strJavaClass = javaClass;
    }


    /**
     *  This interface method is called to set all the fieldValues of this
     *  object of <code>InPort</code>, using the specified XML string.
     *
     * @param port The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element port)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element port = doc.getDocumentElement();

        if (port != null)
        {
            m_bIsSyncRequestType = XMLDmiUtil.getAttributeAsBoolean(port, "isSyncRequestType");

            NodeList children = port.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    m_strPortName = XMLUtils.getNodeValueAsString(child).toUpperCase();
                }

                if (nodeName.equalsIgnoreCase("Description"))
                {
                    m_strDscription = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("XSD"))
                {
                    m_strXSD = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("JavaClass"))
                {
                    m_strJavaClass = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param paramDmi = new Param();

                    paramDmi.setFieldValues((Element) child);
                    addParam(paramDmi);
                }
            }
        }
        validate();
    }

    /**
     *  Sets the paramValue attribute of the InPort object
     *
     * @param name The new paramValue value
     * @param value The new paramValue value
     */
    public void setParamValue(String name, String value)
    {
        Param.setParamValue(m_params, name, value);
    }

    /**
     * Sets external XS ds for object
     *
     * @param map
     */
    public void setExternalXSDs(Map map)
    {
        clearExternalXSDs();

        Iterator iter = map.entrySet().iterator();

        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();

            addExternalXSD((String) entry.getKey(), (String) entry.getValue());
        }
    }

    /**
     */
    public void clearExternalXSDs()
    {
        if (m_params == null)
            return;

        for (int i = 0; i < m_params.size(); i++)
        {
            Param param = (Param) m_params.get(i);

            if (param.getParamName().startsWith("External_"))
                removeParam(param);
        }
    }

    /**
     *  Adds a feature to the ExternalXSD attribute of the InPort object
     *
     * @param namespace The feature to be added to the ExternalXSD attribute
     * @param xsd The feature to be added to the ExternalXSD attribute
     */
    public void addExternalXSD(String namespace, String xsd)
    {
        setParamValue("External_" + namespace, xsd);
    }

    /**
     *  This interface method is called to add specified object of <code>Param</code>
     *  , representing extra parameter for inport, to this object of <code>InPort</code>
     *  .
     *
     * @param param Object of Param to be added to InPort
     * @see #removeParam(Param)
     * @see #clearParam()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void addParam(Param param)
    {
        if (m_params == null)
        {
            m_params = new Vector();
        }
        m_params.addElement(param);
    }

    /**
     *  This interface method is called to remove specified object of <code>Param</code>
     *  , representing extra parameter for inport, from this object of <code>InPort</code>
     *  .
     *
     * @param param Object of Param to be removed from InPort
     * @see #addParam(Param)
     * @see #clearParam()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void removeParam(Param param)
    {
        if (m_params != null)
        {
            m_params.remove(param);
        }
    }

    /**
     * @param name
     */
    public void removeParamWithName(String name)
    {
        Param param = getParamWithName(name);

        if (param != null)
            removeParam(param);
    }

    /**
     *  This interface method is called to clear all the objects of <code>Param</code>
     *  , from this object of <code>InPort</code>.
     *
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void clearParam()
    {
        if (m_params != null)
        {
            m_params.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>InPort</code> has the
     *  required(mandatory) fields set, before inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strPortName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        if (m_params != null)
        {
            Enumeration _enum = m_params.elements();

            while (_enum.hasMoreElements())
            {
                Param param = (Param) _enum.nextElement();

                param.validate();
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
        m_params = new Vector();
    }


    /**
     *  This method is called to write this object of <code>InPort</code> to the
     *  specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo Description of the Parameter
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        writeUTF(out, m_strPortName);
        writeUTF(out, m_strDscription);
        writeUTF(out, m_strXSD);
        writeUTF(out, m_strJavaClass);
        out.writeBoolean(m_bIsSyncRequestType);

        if (m_params != null && m_params.size() > 0)
        {
            int num = m_params.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                Param param = (Param) m_params.elementAt(i);

                param.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>InPort</code> from the
     *  specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo Description of the Parameter
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_strPortName = readUTF(is);
        m_strDscription = readUTF(is);
        m_strXSD = readUTF(is);
        m_strJavaClass = readUTF(is);
        m_bIsSyncRequestType = is.readBoolean();

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            Param param = new Param();

            param.fromStream(is, versionNo);
            m_params.addElement(param);
        }
    }


    /**
     *  This utility method is used to compare this object of <code>InPort</code>
     *  with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof InPort))
        {
            return false;
        }

        InPort rcvObj = (InPort) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getDescription(), m_strDscription)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getJavaClass(), m_strJavaClass)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getPortName(), m_strPortName)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getXSD(), m_strXSD)
            && DmiEqualsUtil.checkObjectEquality(rcvObj.getParamsVector(), m_params))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>InPort</code>
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
        strBuf.append("sps inport Details ");
        strBuf.append("[");
        strBuf.append("is sync request type = ");
        strBuf.append(m_bIsSyncRequestType);
        strBuf.append(", ");
        strBuf.append("Description = ");
        strBuf.append(m_strDscription);
        strBuf.append(", ");
        strBuf.append("Java Class = ");
        strBuf.append(m_strJavaClass);
        strBuf.append(", ");
        strBuf.append("Port name = ");
        strBuf.append(m_strPortName);
        strBuf.append(", ");
        strBuf.append("XSD = ");
        strBuf.append(m_strXSD);
        strBuf.append(", ");
        if (m_params != null)
        {
            strBuf.append("Aliases = ");
            for (int i = 0; i < m_params.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((Param) m_params.elementAt(i)).toString());
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
        Node root0 = document.createElement("InPort");

        ((Element) root0).setAttribute("isSyncRequestType", "" + isSyncRequestType());

        Node child = null;

        child = XMLDmiUtil.getNodeObject("Name", m_strPortName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("Description", m_strDscription, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        if (m_strXSD != null)
        {
            Element elem = document.createElement("XSD");
            CDATASection cdata = document.createCDATASection(m_strXSD);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        child = XMLDmiUtil.getNodeObject("JavaClass", m_strJavaClass, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration _enum = m_params.elements();

            while (_enum.hasMoreElements())
            {
                Param param = (Param) _enum.nextElement();
                if(!StringUtil.isEmpty(param.getParamName()) && !StringUtil.isEmpty(param.getParamValue())){
                    Node paramNode = param.toJXMLString(document);

                    root0.appendChild(paramNode);
                }
            }
        }

        return root0;
    }
}
