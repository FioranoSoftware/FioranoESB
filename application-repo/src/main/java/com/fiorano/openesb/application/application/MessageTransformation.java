/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.application.aps.Route;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class MessageTransformation extends Transformation{

    public static final String ELEM_MESSAGE_TRANSFORMATION = "message-transformation";

    public int getObjectID(){
        return DmiObjectTypes.NEW_MESSAGE_TRANSFORMATION;
    }

    /*-------------------------------------------------[ script ]---------------------------------------------------*/

    public static final String ELEM_JMS_SCRIPT = "jms-script";

    /**
     * Sets Jms script which is used in transformation
     * @param jmsScript  script used in transformation
     */
    public void setJMSScript(String jmsScript){
        this.jmsScript = jmsScript;
    }

    public static final String ELEM_JMS_SCRIPT_FILE = "jms-script-file";

    private String jmsScriptFile;

    /**
     * Gets JMS Script file
     * @return JMS Script file name
     */
    public String getJMSScriptFile() {
        return jmsScriptFile;
    }

    /**
     * Sets JMS Script file
     * @param jmsScriptFile  Name of jms script file
     */
    public void setJMSScriptFile(String jmsScriptFile) {
        this.jmsScriptFile = jmsScriptFile;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <message-transformation>
     *      ...superclass...
     *      <jms-script>string</jms-script>?
     * </message-transformation>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_MESSAGE_TRANSFORMATION);
    }

    protected void toJXMLString(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_MESSAGE_TRANSFORMATION, writeCDataSections);
    }

    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        if(writeCDataSections)
            writeCDATAElement(writer, ELEM_JMS_SCRIPT, jmsScript);
        else{
            if (jmsScriptFile != null) {
                writer.writeStartElement(ELEM_JMS_SCRIPT_FILE);
                writer.writeAttribute(ATTR_NAME, jmsScriptFile);
                writer.writeEndElement();
            }
        }
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        populate(cursor, ELEM_MESSAGE_TRANSFORMATION);
    }

    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        String elemName = cursor.getLocalName();
        if(ELEM_JMS_SCRIPT.equals(elemName))
            jmsScript = cursor.getCData();
        else if(ELEM_JMS_SCRIPT_FILE.equals(elemName))
            jmsScriptFile = cursor.getAttributeValue(null, ATTR_NAME);
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    /**
     * Gets message tranformation XSL from the Route object
     *
     * @param that Route object
     */
    public void convert_1(Route that){
        jmsScript = that.getMessageTransformationXSL();
    }
    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        super.reset();
        jmsScript = null;
        jmsScriptFile = null;
    }

    /**
     * @bundle NEITHER_SCRIPT_SPECIFIED=Neither body nor message transformation script is not specified
     */
    protected void validateScript() throws FioranoException{
        if((StringUtils.isEmpty(getScriptFile()) && StringUtils.isEmpty(getScript())) && StringUtils.isEmpty(jmsScript))
            throw new FioranoException( "NEITHER_SCRIPT_SPECIFIED");
    }
}

