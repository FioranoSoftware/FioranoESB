/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.common;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.aps.APSConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.DmiEqualsUtil;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class Param extends DmiObject{

    public Param(String paramName, String paramValue){
        this.m_paramName = paramName;
        this.m_paramValue = paramValue;
    }

    public Param(){
    }

    /**
     * This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID(){
        return DmiObjectTypes.PARAM;
    }

    /*-------------------------------------------------[ ParamName ]---------------------------------------------------*/

    String m_paramName;

    /**
     * This method gets the parameter name attribute of this
     * <code>Param</code> object.
     *
     * @return The paramName value
     * @see #setParamName(String)
     * @since Tifosi2.0
     */
    public String getParamName(){
        return m_paramName;
    }

    /*-------------------------------------------------[ ParamValue ]---------------------------------------------------*/

    String m_paramValue;

    /**
     * This method gets the parameter value attribute of this <code>Param</code>
     * object.
     *
     * @return The Value attribute value
     * @see #setParamValue(String)
     * @since Tifosi2.0
     */
    public String getParamValue(){
        return m_paramValue;
    }

    /**
     * This method sets the parameter name attribute of this
     * <code>Param</code>object.
     *
     * @param paramName The new paramName value
     * @see #getParamName()
     * @since Tifosi2.0
     */
    public void setParamName(String paramName){
        m_paramName = paramName;
    }


    /**
     * This method sets the parameter value attribute of this <code>Param</code>
     * object.
     *
     * @param paramValue The value to be set as Value attribute
     * @see #getParamValue()
     * @since Tifosi2.0
     */
    public void setParamValue(String paramValue){
        m_paramValue = paramValue;
    }

    /*-------------------------------------------------[ Streaming ]---------------------------------------------------*/

    /**
     * This method writes this <code>Param</code> object to the specified
     * output stream object.
     *
     * @param out       DataOutput object
     * @param versionNo
     * @throws IOException if an error occurs while converting data and
     *                     writing it to a binary stream.
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
            throws IOException{
        super.toStream(out, versionNo);

        if(m_paramName!=null)
            UTFReaderWriter.writeUTF(out, m_paramName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if(m_paramValue!=null)
            UTFReaderWriter.writeUTF(out, m_paramValue);
        else
            UTFReaderWriter.writeUTF(out, "");
    }


    /**
     * This method reads this <code>Param</code> object from the
     * specified input stream object.
     *
     * @param is        SDataInput object
     * @param versionNo
     * @throws IOException if error occurs while reading bytes or while
     *                     converting them into specified Java primitive type.
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
            throws IOException{
        super.fromStream(is, versionNo);

        String temp = UTFReaderWriter.readUTF(is);

        if(temp.equals(""))
            m_paramName = null;
        else
            m_paramName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if(temp.equals(""))
            m_paramValue = null;
        else
            m_paramValue = temp;
    }

    /*-------------------------------------------------[ DOM ]---------------------------------------------------*/

    /**
     * This method sets all the fieldValues of this
     * <code>Param</code> object, using the specified XML string.
     *
     * @param paramElement
     * @throws FioranoException if an error occurs while parsing the
     *                         XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element paramElement)
            throws FioranoException{
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element paramElement = doc.getDocumentElement();

        if(paramElement!=null){
            String name = paramElement.getAttribute("name");

            setParamName(name);

            String value = XMLUtils.getNodeValueAsString(paramElement);

            setParamValue(value);
        }
    }

    public void populate(FioranoStaxParser parser) throws XMLStreamException
    {
        if ( parser.markCursor(APSConstants.PARAM) )
        {
            // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = parser.getAttributes();
            String name =(String)attributes.get(APSConstants.PARAM_NAME);// parser.getAttributeValue(null, "name");
            
            setParamName(name);

            String value = parser.getText();

            setParamValue(value);
        }
    }
    /**
     * This method converts the specified <code>Document</code> object to
     * the specified <code>Node</code> object.
     *
     * @param document the Document object
     * @return the element node with the corresponding text node value.
     * @since Tifosi2.0
     */
    public Node toJXMLString(Document document){
        Node root0 = document.createElement("Param");

        ((Element)root0).setAttribute("name", m_paramName);

        Node pcData = document.createTextNode(m_paramValue);

        root0.appendChild(pcData);
        return root0;
    }
    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Param
        writer.writeStartElement("Param");
        writer.writeAttribute("name", m_paramName);
        writer.writeCharacters(m_paramValue);
        //End Param
        writer.writeEndElement();
    }


    /*-------------------------------------------------[ Override ]---------------------------------------------------*/

    /**
     * @return
     */
    public String toString(){
        StringBuffer xmlString = new StringBuffer();

        xmlString.append("<Param");
        xmlString.append("name="+"\"").append(m_paramName).append("\"");
        xmlString.append(">");
        xmlString.append(m_paramValue);
        xmlString.append("</Param>");
        return xmlString.toString();
    }

    /**
     * This method has not been implemented in this version.
     * (Resets the values of the data members of this object.)
     *
     * @since Tifosi2.0
     */
    public void reset(){
    }


    /**
     * This method has not been implemented in this version.
     * (This method tests whether this object has the required(mandatory)
     * fields set, before inserting values in to the database.)
     *
     * @throws FioranoException if validation process fails to succeed.
     * @since Tifosi2.0
     */
    public void validate()
            throws FioranoException{
    }

    public boolean equals(Object paramObj){
        if(paramObj==null || !(paramObj instanceof Param))
            return false;

        Param param = (Param)paramObj;

        return DmiEqualsUtil.checkStringEquality(param.getParamName(), m_paramName)
                && DmiEqualsUtil.checkStringEquality(param.getParamValue(), m_paramValue);
    }

    /*-------------------------------------------------[ Utilities ]---------------------------------------------------*/

    /**
     * Returns param with name for class
     */
    public static Param getParamWithName(Vector params, String name){
        int size = params.size();

        if(size==0)
            return null;
        try{
            for(int i = 0; i<size; i++){

                Param param = (Param)params.get(i);
                //raf.writeBytes(param.toString());
                if(param.getParamName().equals(name))
                    return param;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns param value for class
     */
    public static String getParamValue(Vector params, String name){
        Param param = getParamWithName(params, name);

        return param!=null
                ? param.getParamValue()
                : null;
    }

    /**
     * Returns param value for class
     */
    public static String getParamValue(Vector params, String name, String defValue){
        Param param = getParamWithName(params, name);

        return param!=null
                ? param.getParamValue()
                : defValue;
    }

    /**
     * Sets param value for class
     *
     * @param params
     * @param name
     * @param value
     */
    public static void setParamValue(Vector params, String name, String value){
        Param param = getParamWithName(params, name);

        if(param==null){
            param = new Param();
            param.setParamName(name);
            param.setParamValue(value);
            params.add(param);
        } else
            param.setParamValue(value);
    }

    public static void setParamValue(Vector params, String name, boolean bool){
        setParamValue(params, name, String.valueOf(bool));
    }

    public static boolean getParamValueAsBoolean(Vector params, String name){
        return "true".equals(getParamValue(params, name));
    }

    public static boolean getParamValueAsBoolean(Vector params, String name, boolean defaultValue){
        String value = getParamValue(params, name);
        return StringUtils.isEmpty(value) ? defaultValue : "true".equals(value);
    }

    public static void setParamValue(Vector params, String name, int intValue){
        setParamValue(params, name, String.valueOf(intValue));
    }

    public static int getParamValueAsInt(Vector params, String name, int defaultValue){
        String value = getParamValue(params, name);
        return value==null ? defaultValue : Integer.parseInt(value);
    }

    public static void setParamValue(Vector params, String name, long longValue){
        setParamValue(params, name, String.valueOf(longValue));
    }

    public static long getParamValueAsLong(Vector params, String name, long defaultValue){
        String value = getParamValue(params, name);
        return value==null ? defaultValue : Long.parseLong(value);
    }



}