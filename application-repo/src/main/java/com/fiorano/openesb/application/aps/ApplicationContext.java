/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
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
import java.io.Serializable;
import java.util.*;

public class ApplicationContext extends DmiObject implements Serializable
{
    //  Structural definition of Application Context
    String          m_structure;

    // Application Context can either be XSD or DTD.
    int          m_structureType = PortInst.TYPE_DTD;

    //  Default values of Application Context
    String          m_defaultInstance;

    //any params
    Vector m_params = new Vector();

    String m_rootElement;

    String m_rootElementNamespace;

    /**
     *  This is the constructor of the <code>ApplicationContext</code> class.
     *
     * @since Tifosi2.0
     */
    public ApplicationContext()
    {
    }

    /**
     *  Gets Structure (DTD) of the application context.
     *
     * @return Structure (DTD) of the application context.
     * @since Tifosi2.0
     */
    public String getStructure()
    {
        return m_structure;
    }

    /**
     *  Gets the Type of the Structure of the application context.
     *
     * @return Structure (DTD) of the application context.
     * @since Tifosi2.0
     */
    public int getStructureType()
    {
        return m_structureType;
    }

    /**
     * @return Default instance of the application context.
     * @since Tifosi2.0
     */
    public String getDefaultInstance()
    {
        return m_defaultInstance;
    }

    /**
     * @return Root Element of the application context.
     * @since Tifosi2.0
     */
    public String getRootElement()
    {
        return m_rootElement;
    }

    /**
     * @return Root Element Namespace of the application context.
     * @since Tifosi2.0
     */
    public String getRootElementNamespace()
    {
        return m_rootElementNamespace;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APPLICATION_CONTEXT;
    }

     /**
     * Gets the externalStructure attribute of the OutPort object
     *
     * @param namespace Description of the Parameter
     * @return The externalXSD value
     */
    public String getExternalStructure(String namespace){
        return getParamValue("External_"+namespace);
    }

     /**
     * Returns external Structures for object
     *
     * @return Map
     */
    public Map getAllExternalStructures(){
        Map map = new HashMap();
        Enumeration enumerator = getExternalStructureNames();

        while(enumerator.hasMoreElements()){
            String namespace = (String)enumerator.nextElement();

            map.put(namespace, getExternalStructure(namespace));
        }
        return map;
    }

    /**
     * Gets the extNames of Structures of ApplicationContext object
     *
     * @return The extNamespaces value
     */
    public Enumeration getExternalStructureNames(){
        Vector v = new Vector();
        int length = "External_".length();

        int size = m_params.size();

        for(int i = 0; i<size; i++){
            Param param = (Param)m_params.get(i);

            if(param.getParamName().startsWith("External_")){
                v.add(param.getParamName().substring(length));
            }
        }

        return v.elements();
    }

     /**
     * Adds an External Structure attribute
     *
     * @param namespace The feature to be added to the ExternalXSD attribute
     * @param extStructure The feature to be added to the ExternalXSD attribute
     */
    public void addExternalStructure(String namespace, String extStructure) {
        setParamValue("External_"+namespace,extStructure);

    }

     /**
     * Removes all External Structures of the ApplicationContext object
     *
     */
    public void removeAllExternalXSDs(){

        Enumeration enumerator = getExternalStructureNames();

        while(enumerator.hasMoreElements()){

            String namespace = (String)enumerator.nextElement();
            if(m_params!=null){
                removeParam(Param.getParamWithName(m_params,"External_"+namespace));
            }
        }
    }
    /**
     *  Set Structure (DTD) of the application context.
     *
     * @param structure
     * @since Tifosi2.0
     */
    public void setStructure(String structure)
    {
        m_structure = structure;
    }

    /**
     *  Set the Structure Type of the application context.
     *
     * @param type
     * @since Tifosi2.0
     */
    public void setStructureType(int type)
    {
        m_structureType = type;
    }

    /**
     *  Set default instance value of the application context.
     *
     * @param defaultInstance Default instance of application context.
     * @since Tifosi2.0
     */
    public void setDefaultInstance(String defaultInstance)
    {
        m_defaultInstance = defaultInstance;
    }

    /**
     * @param rootElement of the application context.
     * @since Tifosi2.0
     */
    public void setRootElement(String rootElement)
    {
        m_rootElement = rootElement;
    }

    /**
     * @param rootElementNamespace of the application context.
     * @since Tifosi2.0
     */
    public void setRootElementNamespace(String rootElementNamespace)
    {
        m_rootElementNamespace = rootElementNamespace;
    }


    /**
     *  Sets all the fieldValues of this <code>ApplicationContext</code> object
     *  using the specified XML string.
     *
     * @param appContextElement The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element appContextElement)
        throws FioranoException
    {
        if (appContextElement != null)
        {
            NodeList childList = appContextElement.getChildNodes();
            Node appchild = null;

            for (int i = 0; i < childList.getLength(); i++)
            {
                appchild = childList.item(i);
                if (appchild.getNodeType() == appchild.TEXT_NODE)
                    continue;

                String nodeName = appchild.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(appchild);

                if (nodeName.equalsIgnoreCase("Structure"))
                    setStructure(nodeValue);

                else if (nodeName.equalsIgnoreCase("DefaultInstance"))
                    setDefaultInstance(nodeValue);

                else if (nodeName.equalsIgnoreCase("RootElement"))
                    setRootElement(nodeValue);

                else if (nodeName.equalsIgnoreCase("RootElementNamespace"))
                    setRootElementNamespace(nodeValue);

                else if (nodeName.equalsIgnoreCase("StructureType"))
                    setStructureType(Integer.valueOf(nodeValue).intValue());
            }
        }
        validate();
    }


    protected  void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {

         //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if ( cursor.markCursor(APSConstants.APPLICATION_CONTEXT) )
        {
            // Get Child Elements
            while (cursor.nextElement())
            {
                String nodeName = cursor.getLocalName();

                
                String nodeValue = null;

                if (nodeName.equalsIgnoreCase(APSConstants.APPLICATION_CONTEXT_STRUCTURE)) {
                    nodeValue = cursor.getCData();
                    setStructure(nodeValue);
                }

                else if (nodeName.equalsIgnoreCase(APSConstants.APPLICATION_CONTEXT_DEF_INST)) {
                    nodeValue = cursor.getText();
                    setDefaultInstance(nodeValue);
                }

                else if (nodeName.equalsIgnoreCase(APSConstants.APPLICATION_CONTEXT_ROOT_ELEM)){
                     nodeValue = cursor.getText();
                    setRootElement(nodeValue);
                }

                else if (nodeName.equalsIgnoreCase(APSConstants.APPLICATION_CONTEXT_ROOT_ELEM_NS)) {
                    nodeValue = cursor.getText();
                    setRootElementNamespace(nodeValue);
                }

                else if (nodeName.equalsIgnoreCase(APSConstants.APPLICATION_CONTEXT_STRUCTURE_TYPE)) {
                    nodeValue = cursor.getText();
                    setStructureType(Integer.valueOf(nodeValue).intValue());
                }
                else
                {
                    //KEEP QUITE.
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
     *  This method tests whether this <code>ApplicationContext</code> object has
     *  the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  This method writes this <code>ApplicationContext</code> object to the
     *  specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        if (m_structure != null)
            UTFReaderWriter.writeUTF(out, m_structure);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_defaultInstance != null)
            UTFReaderWriter.writeUTF(out, m_defaultInstance);
        else
            UTFReaderWriter.writeUTF(out, "");

        if(m_rootElement != null)
            UTFReaderWriter.writeUTF(out, m_rootElement);
        else
            UTFReaderWriter.writeUTF(out, "");

        if(m_rootElementNamespace != null)
            UTFReaderWriter.writeUTF(out, m_rootElementNamespace);
        else
            UTFReaderWriter.writeUTF(out, "");

        out.writeInt(m_structureType);
    }


    /**
     *  This method reads this <code>ApplicationContexts</code> object from the
     *  specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_structure = null;
        else
            m_structure = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_defaultInstance = null;
        else
            m_defaultInstance = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_rootElement = null;
        else
            m_rootElement = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_rootElementNamespace = null;
        else
            m_rootElementNamespace = temp;

        m_structureType = is.readInt();

    }

    /**
     *  This utility method is used to get the String representation of this
     *  <code>ApplicationContext</code> object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Application Context ");
        strBuf.append("[");
        strBuf.append("AppContext Structure = ");
        strBuf.append(m_structure);
        strBuf.append(", ");
        strBuf.append("AppContext DefaultInstance = ");
        strBuf.append(m_defaultInstance);
        strBuf.append(", ");
        strBuf.append("AppContext Structure Type = ");
        strBuf.append(m_structureType);
        strBuf.append(", ");
        strBuf.append("RootElement = ");
        strBuf.append(m_rootElement);
        strBuf.append(", ");
        strBuf.append("RootElementNamespace = ");
        strBuf.append(m_rootElementNamespace);
        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     * @since Tifosi2.0
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("ApplicationContext");

        Node node;

        if(!StringUtils.isEmpty(m_structure)){
            Element elem = document.createElement("Structure");
            CDATASection cdata = document.createCDATASection(m_structure);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if(!StringUtils.isEmpty(m_defaultInstance)){
            node = XMLDmiUtil.getNodeObject(
                "DefaultInstance", m_defaultInstance, document);
            if (node != null)
                root0.appendChild(node);
        }

        if(!StringUtils.isEmpty(m_rootElement)){
            node = XMLDmiUtil.getNodeObject(
                "RootElement", m_rootElement, document);
            if (node != null)
                root0.appendChild(node);
        }

        if(!StringUtils.isEmpty(m_rootElementNamespace)){
            node = XMLDmiUtil.getNodeObject(
                "RootElementNamespace", m_rootElementNamespace, document);
            if (node != null)
                root0.appendChild(node);
        }

        node = XMLDmiUtil.getNodeObject(
            "StructureType", String.valueOf(m_structureType), document);
        if (node != null)
            root0.appendChild(node);

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Application Context
        writer.writeStartElement("ApplicationContext");

        //Write Structure
        if(!StringUtils.isEmpty(m_structure)){
            writer.writeStartElement("Structure");
            writer.writeCData(m_structure);
            writer.writeEndElement();

        }

        //Write Default Instance
        if(!StringUtils.isEmpty(m_defaultInstance)){
            FioranoStackSerializer.writeElement("DefaultInstance", m_defaultInstance, writer);
        }

        //Write Root Element
        if(!StringUtils.isEmpty(m_rootElement)){
            FioranoStackSerializer.writeElement("RootElement", m_rootElement, writer);

        }

        //Write Root Element Namespace
        if(!StringUtils.isEmpty(m_rootElementNamespace)){
            FioranoStackSerializer.writeElement("RootElementNamespace", m_rootElementNamespace, writer);

        }

        //Write Structure Type
        FioranoStackSerializer.writeElement("StructureType", String.valueOf(m_structureType), writer);

        //End Application Context
        writer.writeEndElement();

    }

    /**
     * Sets the paramValue attribute of the ApplicationContext object
     *
     * @param namespace  The new paramValue value
     * @param value The new paramValue value
     */
    private void setParamValue(String namespace, String value){
        Param.setParamValue(m_params, namespace, value);
    }

     /**
     * Gets the paramValue attribute of the ApplicationContext object
     *
     * @param namespace Description of the Parameter
     * @return The paramValue value
     */
    private String getParamValue(String namespace){
        return Param.getParamValue(m_params, namespace);
    }

    /**
     * Removes the paramValue attribute of the ApplicationContext object
     *
     * @param param  The new paramValue value
     */
    public void removeParam(Param param){
        if(m_params!=null){
            m_params.remove(param);
        }
    }
}
