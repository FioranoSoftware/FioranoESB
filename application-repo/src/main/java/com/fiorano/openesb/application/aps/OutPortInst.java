/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class OutPortInst extends PortInst
{
    String          m_strContextXSL = null;
    String          m_strContextInfo = null;

    // SPecifies whether to use saxon/xalan etc
    String          m_transformerType = null;

    /**
     * Returns transformer type for object
     *
     * @return String
     */
    public String getTransformerType()
    {
        return m_transformerType;
    }

    /**
     *  This interface method is called to get the description of the
     *  OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @return The description of OutPortInst
     * @see #setDescription(String)
     * @since Tifosi2.0
     */
    public String getContextXSL()
    {
        return m_strContextXSL;
    }

    /**
     * Returns JMS destination type for object
     *
     * @return String
     */
    public String getJMSDestinationType(){
        return getParamValue(PortInstConstants.DESTINATION_TYPE, PortInstConstants.JMSDESTINATION_TOPIC);
    }

    /**
     *  This interface method is called to get the description of the
     *  OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @return The description of OutPortInst
     * @see #setDescription(String)
     * @since Tifosi2.0
     */
    public String getContextInfo()
    {
        return m_strContextInfo;
    }

    /**
     * Output port cannot have any subscription and hence it will have durable subscription as false.
     * @return boolean
     * @deprecated
     */
    public boolean isDurableSubscription() {
        return false;
    }

    /**
     * Gives the Subscription Name if Port has a durable subscription.
     *
     * @return The rootElemName value
     * @deprecated
     */
    public String getSubscriptionName(){
        return Param.getParamValue(m_params, PortInstConstants.SUBSCRIPTION_NAME, PortInstConstants.DEF_SUBSCRIPTION_NAME);
    }

    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.OUT_PORT_INSTANCE;
    }

    /**
     * Sets transformer type for object
     *
     * @param transformerType
     */
    public void setTransformerType(String transformerType)
    {
        m_transformerType = transformerType;
    }


    /**
     *  This interface method is called to set the specified string as
     *  description of the OutPortInst, specified by this object of <code>OutPortInst</code>
     *  .
     *
     * @param contextXSL The new contextXSL value
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setContextXSL(String contextXSL)
    {
        m_strContextXSL = contextXSL;
    }

    /**
     *  This interface method is called to set the specified string as
     *  description of the OutPortInst, specified by this object of <code>OutPortInst</code>
     *  .
     *
     * @param strContextInfo The new contextInfo value
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setContextInfo(String strContextInfo)
    {
        m_strContextInfo = strContextInfo;
    }


    /**
     *  This interface method is called to set all the fieldValues of this
     *  object of <code>OutPortInst</code>, using the specified XML string.
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
            m_bisDisabled = XMLDmiUtil.getAttributeAsBoolean(port, "isDisabled");

            NodeList children = port.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    if (child != null)
                    {
                        m_strPortName = XMLUtils.getNodeValueAsString(child).toUpperCase();
                    }
                }

                if (nodeName.equalsIgnoreCase("Description"))
                {
                    m_strDscription = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("StandardXSD"))
                {
                    if ("ON_EXCEPTION".equals(m_strPortName))
                        m_strXSD = CommonSchemas.ERROR_XSD;
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSD)){
                    m_strXSD = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSDREF))
                {
                    m_strXSDRef = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("SetContextXSL"))
                {
                    m_strContextXSL = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("SetContextInfo"))
                {
                    m_strContextInfo = XMLUtils.getNodeValueAsString(child);
                }
                if (nodeName.equalsIgnoreCase("SetTransformationType"))
                {
                    m_transformerType = XMLUtils.getNodeValueAsString(child);
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

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        if ( parser.markCursor(APSConstants.OUTPORT_INST) )
        {
            // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = parser.getAttributes();
            m_bIsSyncRequestType = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_SYNC_REQUEST_TYPE));//XMLDmiUtil.getAttributeAsBoolean(port, "isSyncRequestType");
            m_bisDisabled = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_DISABLED));//XMLDmiUtil.getAttributeAsBoolean(port, "isSyncRequestType");
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase("Name"))
                {
                    m_strPortName = parser.getText();//XMLUtils.getNodeValueAsString(child).toUpperCase();

                }

                else if (nodeName.equalsIgnoreCase("Description"))
                {
                    m_strDscription = parser.getText();//XMLUtils.getNodeValueAsString(child);

                }

                else if (nodeName.equalsIgnoreCase("StandardXSD"))
                {
                    if ("ON_EXCEPTION".equals(m_strPortName))
                        m_strXSD = CommonSchemas.ERROR_XSD;
                }

                else if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSD)){
                    m_strXSD = parser.getCData();//XMLUtils.getNodeValueAsString(child);

                }

                else if (nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSDREF))
                {
                    m_strXSDRef = parser.getText();//XMLUtils.getNodeValueAsString(child);

                }

                else if (nodeName.equalsIgnoreCase("SetContextXSL"))
                {
                    m_strContextXSL = parser.getCData();//XMLUtils.getNodeValueAsString(child);

                }

                else if (nodeName.equalsIgnoreCase("SetContextInfo"))
                {
                    m_strContextInfo = parser.getCData();//XMLUtils.getNodeValueAsString(child);

                }
                else if (nodeName.equalsIgnoreCase("SetTransformationType"))
                {
                    m_transformerType = parser.getText();//XMLUtils.getNodeValueAsString(child);

                }
                else if (nodeName.equalsIgnoreCase("JavaClass"))
                {
                    m_strJavaClass = parser.getText();//XMLUtils.getNodeValueAsString(child);

                }

                else if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param paramDmi = new Param();

                    paramDmi.setFieldValues(parser);
                    addParam(paramDmi);
                }
            }
            if ( attributes != null)
            {
                attributes.clear();
                attributes = null;
            }


        }
        validate();
    }
    /**
     *  Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    public Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("OutPortInst");

        ((Element) root0).setAttribute("isSyncRequestType", "" + isSyncRequestType());

        Node child = null;

        child = XMLDmiUtil.getNodeObject("Name", m_strPortName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        if(!StringUtils.isEmpty(m_strDscription)){
            child = XMLDmiUtil.getNodeObject("Description", m_strDscription, document);
            if (child != null)
            {
                root0.appendChild(child);
            }
        }

        boolean standardXSD = false;

        if ("ON_EXCEPTION".equals(m_strPortName))
            standardXSD =  CommonSchemas.ERROR_XSD==null
                    ? m_strXSD==null
                    : m_strXSD!=null && CommonSchemas.ERROR_XSD.equals(m_strXSD);

        if (standardXSD)
        {
            Element elem = document.createElement("StandardXSD");

            root0.appendChild(elem);
        }
        else
            if (m_strXSDRef != null)
        {
            child = XMLDmiUtil.getNodeObject(PortInstConstants.PORT_XSDREF, m_strXSDRef, document);
            root0.appendChild(child);
        }

        if (m_strContextXSL != null)
        {
            Element elem = document.createElement("SetContextXSL");
            CDATASection cdata = document.createCDATASection(m_strContextXSL);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if (m_strContextInfo != null)
        {
            Element elem = document.createElement("SetContextInfo");
            CDATASection cdata = document.createCDATASection(m_strContextInfo);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if (m_transformerType != null)
        {
            Element elem = document.createElement("SetTransformationType");
            CDATASection cdata = document.createCDATASection(m_transformerType);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if(!StringUtils.isEmpty(m_strJavaClass)){
            child = XMLDmiUtil.getNodeObject("JavaClass", m_strJavaClass, document);
            if (child != null)
            {
                root0.appendChild(child);
            }
        }

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration _enum = m_params.elements();

            while (_enum.hasMoreElements())
            {
                Param param = (Param) _enum.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                {
                    if(!checkIfDefaultValue(param.getParamName(), param.getParamValue()))
                    {
                        Node paramNode = param.toJXMLString(document);
                        root0.appendChild(paramNode);
                    }
                }
            }
        }

        return root0;
    }
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {

        //Start OutPortInst
        writer.writeStartElement("OutPortInst");

        writer.writeAttribute("isSyncRequestType", "" + isSyncRequestType());

        FioranoStackSerializer.writeElement("Name", m_strPortName, writer);

        if(!StringUtils.isEmpty(m_strDscription)){
            FioranoStackSerializer.writeElement("Description", m_strDscription, writer);
        }

        boolean standardXSD = false;

        if ("ON_EXCEPTION".equals(m_strPortName))
            standardXSD =  CommonSchemas.ERROR_XSD==null
                    ? m_strXSD==null
                    : m_strXSD!=null && CommonSchemas.ERROR_XSD.equals(m_strXSD);

        if (standardXSD)
        {
            writer.writeStartElement("StandardXSD");

            writer.writeEndElement();
        }
        else
            if (m_strXSDRef != null)
        {
            FioranoStackSerializer.writeElement(PortInstConstants.PORT_XSDREF, m_strXSDRef, writer);

        }

        if (m_strContextXSL != null)
        {
            writer.writeStartElement("SetContextXSL");
            writer.writeCData(m_strContextXSL);
            writer.writeEndElement();
        }

        if (m_strContextInfo != null)
        {
            writer.writeStartElement("SetContextInfo");
            writer.writeCData(m_strContextInfo);
            writer.writeEndElement();
        }

        if (m_transformerType != null)
        {
            writer.writeStartElement("SetTransformationType");
            writer.writeCData(m_transformerType);
            writer.writeEndElement();

        }

        if(!StringUtils.isEmpty(m_strJavaClass)){
            FioranoStackSerializer.writeElement("JavaClass", m_strJavaClass, writer);
        }

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                {
                    if(!checkIfDefaultValue(param.getParamName(), param.getParamValue()))
                    {
                        param.toJXMLString(writer);
                    }
                }
            }
        }
        //End OutPortInst
        writer.writeEndElement();

    }

    /**
     *  This method is called to write this object of <code>OutPortInst</code>
     *  to the specified output stream object.
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
        writeUTF(out, m_strContextXSL);
        writeUTF(out, m_strContextInfo);

        writeUTF(out, m_transformerType);
        writeUTF(out, m_strJavaClass);
        out.writeBoolean(m_bIsSyncRequestType);
        out.writeBoolean(m_bisDisabled);

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
     *  This is called to read this object <code>OutPortInst</code> from the
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
        m_strContextXSL = readUTF(is);
        m_strContextInfo = readUTF(is);
        m_transformerType = readUTF(is);
        m_strJavaClass = readUTF(is);
        m_bIsSyncRequestType = is.readBoolean();
        m_bisDisabled = is.readBoolean();

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            Param param = new Param();

            param.fromStream(is, versionNo);
            m_params.addElement(param);
        }
    }


    /**
     *  This utility method is used to compare this object of <code>OutPortInst</code>
     *  with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof OutPortInst))
        {
            return false;
        }

        OutPortInst rcvObj = (OutPortInst) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getDescription(), m_strDscription)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getJavaClass(), m_strJavaClass)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getPortName(), m_strPortName)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getXSD(), m_strXSD)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getContextXSL(), m_strContextXSL)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getContextInfo(), m_strContextInfo)
            && rcvObj.getParamsVector().equals(m_params))
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
     *  object of <code>OutPortInst</code>.
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
        strBuf.append("sps OutPortInst Details ");
        strBuf.append("[");
        strBuf.append("is sync request type = ");
        strBuf.append(m_bIsSyncRequestType);
        strBuf.append(", ");
        strBuf.append("is disabled = ");
        strBuf.append(m_bisDisabled);
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
        strBuf.append("SetContextXSL = ");
        strBuf.append(m_strContextXSL);
        strBuf.append(", ");
        strBuf.append("SetContextInfo = ");
        strBuf.append(m_strContextInfo);
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

    /****************************** Deprecated properties for outport *****************************************************/
    // TODO: REQUIRES A CLENAUP OF INPORTINST / OUTPORTINST / PORTINST. Only properties common to both inport inst and outportinst should be in port inst. right noe everything is there
    /**
     * Gets the Message Selector.
     *
     * @return The rootElemName value
     * @deprecated
     */
    public String getMessageSelector()
    {
        return null;
    }

    /**
     * sets the MessageSelector for a port.
     *
     * @param selector
     * @deprecated
     */
    public void setMessageSelector(String selector){
        // do nothing.
    }
    /****************************** Deprecated properties for outport *****************************************************/

}
