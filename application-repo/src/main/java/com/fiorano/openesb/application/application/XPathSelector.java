/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.XPathDmi;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XPathSelector extends InflatableDMIObject{
    /**
     * Element xpath in event process xml
     */
    public static final String ELEM_XPATH_SELECTOR = "xpath";

    /**
     * Default Constructor
     */
    public XPathSelector(){
    }

    /**
     * Constructs a xpath selector
     * @param xpath xpath
     * @param namespaces namespaces
     */
    public XPathSelector(String xpath, Map namespaces){
        this.xpath = xpath;
        if(namespaces==null)
            this.namespaces = new HashMap();
        else
            this.namespaces = namespaces;
    }

    /**
     * Returns the object ID of the XPathSelector
     * @return int - object ID of the XPathSelector
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_XPATH_SELECTOR;
    }

    /*-------------------------------------------------[ namespaces ]---------------------------------------------------*/
    /**
     * Element namespaces in event process xml
     */
    public static final String ELEM_NAMESPACES = "namespaces";
    /**
     * Element namespace in event process xml
     */
    public static final String ELEM_NAMESPACE = "namespace";
    /**
     * Attribute prefix
     */
    public static final String ATTR_PREFIX = "prefix";

    private Map<String,String> namespaces = new HashMap<>();

    /**
     * Returns namespaces of this selector
     * @return Map - Namespaces of this selector
     */
    public Map<String,String> getNamespaces(){
        return namespaces;
    }

    /**
     * Sets namespaces of this selector
     * @param namespaces Namespace to be set
     */
    public void setNamespaces(Map namespaces){
        this.namespaces = namespaces;
    }

    /**
     * Adds a namespace with specified name and value to the namespaces of this service instance
     * @param name Name of the namespace
     * @param value Value
     */
    public void addNameSpace(String name,String value){
        namespaces.put(name,value);
    }

    /*-------------------------------------------------[ xpath ]---------------------------------------------------*/
    /**
     * Attribute query
     */
    public static final String ATTR_XPATH = "query";

    private String xpath;

    /**
     * Returns xpath query of this selector
     * @return String - Xpath Query
     */
    public String getXPath(){
        return xpath;
    }

    /**
     * Sets xpath query of this selector
     * @param xpath Xpath query to be set for this selector
     */
    public void setXPath(String xpath){
        this.xpath = xpath;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <xpath query="string">
     *      <namespaces>
     *          <namespace prefix="string">uri</namespace>+
     *      </namespaces>?
     * </xpath>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_XPATH_SELECTOR);
        {
            writer.writeAttribute(ATTR_XPATH, xpath);
            if(namespaces.size()>0){
                writer.writeStartElement(ELEM_NAMESPACES);
                {
                    Iterator iter = namespaces.entrySet().iterator();
                    while(iter.hasNext()){
                        Map.Entry entry = (Map.Entry)iter.next();
                        writer.writeStartElement(ELEM_NAMESPACE);
                        {
                            writer.writeAttribute(ATTR_PREFIX, (String)entry.getKey());
                            writer.writeCharacters((String)entry.getValue());
                        }
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_XPATH_SELECTOR)){
            xpath = cursor.getAttributeValue(null, ATTR_XPATH);
            while(cursor.nextElement()){
                if(ELEM_NAMESPACES.equals(cursor.getLocalName())){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(ELEM_NAMESPACE.equals(cursor.getLocalName()))
                            addNameSpace(cursor.getAttributeValue(null, ATTR_PREFIX), cursor.getText());
                    }
                    cursor.resetCursor();
                }
            }
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(XPathDmi that){
        xpath = that.getXPath();
        if(that.getNameSpace()!=null)
            namespaces.putAll(that.getNameSpace());
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        namespaces.clear();
        xpath = null;
    }

    /**
     * @bundle XPATH_QUERY_UNSPECIFIED=XPath Query is not specified
     */
    public void validate() throws FioranoException{

        if(StringUtil.isEmpty(xpath))
            throw new FioranoException( "XPATH_QUERY_UNSPECIFIED");
    }
}
