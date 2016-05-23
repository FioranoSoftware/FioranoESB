/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.aps.OutPortInst;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Transformation extends InflatableDMIObject{

    public static final String XALAN_TRANSFORMER_FACTORY = "org.apache.xalan.processor.TransformerFactoryImpl";
    public static final String SAXON_TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";
    public static final String XSLTC_TRANSFORMER_FACTORY = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
    /**
     * Element transformation
     */
    public static final String ELEM_TRANSFORMATION = "transformation";
    protected String jmsScript;

    /**
     * Returns object ID reference of the new transformation
     * @return int - Object ID
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_TRANSFORMATION;
    }

    /*-------------------------------------------------[ factory ]---------------------------------------------------*/
    /**
     * Attribute factory
     */
    public static final String ATTR_FACTORY = "factory";
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";

    /**
     * Attribute org.apache.xalan.processor.TransformerFactoryImpl
     */
    public static final String TRANSFORMER_FACTORY_XALAN = XALAN_TRANSFORMER_FACTORY;

    /**
     * Attribute net.sf.saxon.TransformerFactoryImpl
     */
    public static final String TRANSFORMER_FACTORY_SAXON = SAXON_TRANSFORMER_FACTORY;

    private String factory = TRANSFORMER_FACTORY_XALAN;

    /**
     * Returns the transformation factory to be used for the transformation
     * @return String - Transformation Factory
     */
    public String getFactory(){
        return factory;
    }

    /**
     * Sets the transformation factory to be used for the transformation
     * @param factory Transformation Factory
     */
    public void setFactory(String factory){
        this.factory = factory;
    }

    /*-------------------------------------------------[ script ]---------------------------------------------------*/
    /**
     * Element script
     */
    public static final String ELEM_SCRIPT = "script";

    private String script;

    /**
     * Returns the script for transformation
     * @return String - Script
     */
    public String getScript(){
        return script;
    }

    /**
     * Sets the script for transformation
     * @param script Script
     */
    public void setScript(String script){
        this.script = script;
    }

    /**
     * Element script-file
     */
    public static final String ELEM_SCRIPT_FILE = "script-file";

    private String scriptFile;

    /**
     * Returns the name of the script file
     * @return String - Script file name
     */
    public String getScriptFile() {
        return scriptFile;
    }

    /**
     * Sets the script file name
     * @param scriptFile Script file name
     */
    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    /*-------------------------------------------------[ project ]---------------------------------------------------*/
    /**
     * Element project
     */
    public static final String ELEM_PROJECT = "project";

    private String project;

    /**
     * Returns the project for transformation
     * @return String - Project
     */
    public String getProject(){
        return project;
    }

    /**
     * Sets the project for transformation
     * @param project Project
     */
    public void setProject(String project){
        this.project = project;
    }

    /**
     * Element project-file
     */
    public static final String ELEM_PROJECT_FILE = "project-file";

    private String projectFile;

    /**
     * Returns the project file name
     * @return String - Project file name
     */
    public String getProjectFile() {
        return projectFile;
    }

    /**
     * Sets the project file name
     * @param projectFile Project file name
     */
    public void setProjectFile(String projectFile) {
        this.projectFile = projectFile;
    }

    /*-------------------------------------------------[ transformation-config-name ]---------------------------------------------------*/
    /**
     * Element messaging-config-name in event process xml
     */
    public static final String ELEM_TRANSFORMATION_CONFIG_NAME = "transformation-config-name";

    private String transformationConfigName;

    /**
     * Returns configuration name of this transformation element
     * @return String - Name of the configuration
     */
    public String getTransformationConfigName(){
        return transformationConfigName;
    }

    /**
     * Sets configuration name of this transformation element
     * @param transformationConfigName Name of the configuration
     */
    public void setTransformationConfigName(String transformationConfigName){
        this.transformationConfigName = transformationConfigName;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * Gets JMS Script which is used in transformation
     * @return  JMS Script
     */
    public String getJMSScript(){
        return jmsScript;
    }

    /**
     * <transformation factory="string">
     *      <script>string</script>
     *      <project>string</project>?
     * </transformation>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_TRANSFORMATION);
    }

    protected void toJXMLString(XMLStreamWriter writer, String rootElement) throws XMLStreamException, FioranoException{
        toJXMLString(writer, rootElement, true);
    }

    protected void toJXMLString(XMLStreamWriter writer, String rootElement, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        writer.writeStartElement(rootElement);
        {
            if(!TRANSFORMER_FACTORY_XALAN.equals(factory))
                writer.writeAttribute(ATTR_FACTORY, factory);
            if(writeCDataSections){
                writeCDATAElement(writer, ELEM_SCRIPT, script);
                writeCDATAElement(writer, ELEM_PROJECT, project);
                //bug#23438: temporary fix. eStudio eventProcess.xml has only script file name unlike nstudio xml file which contains script.
                if(scriptFile != null){
                    writer.writeStartElement(ELEM_SCRIPT_FILE);
                    writer.writeAttribute(ATTR_NAME, scriptFile);
                    writer.writeEndElement();
                }

                if(projectFile != null){
                    writer.writeStartElement(ELEM_PROJECT_FILE);
                    writer.writeAttribute(ATTR_NAME, projectFile);
                    writer.writeEndElement();
                }
            }else{
                if(transformationConfigName != null){
                    writer.writeStartElement(ELEM_TRANSFORMATION_CONFIG_NAME);
                    {
                        writer.writeAttribute(ATTR_NAME, transformationConfigName);
                    }
                    writer.writeEndElement();
                }else{
                    if(scriptFile != null){
                        writer.writeStartElement(ELEM_SCRIPT_FILE);
                        writer.writeAttribute(ATTR_NAME, scriptFile);
                        writer.writeEndElement();
                    }

                    if(projectFile != null){
                        writer.writeStartElement(ELEM_PROJECT_FILE);
                        writer.writeAttribute(ATTR_NAME, projectFile);
                        writer.writeEndElement();
                    }
                }
            }
            toJXMLString_1(writer, writeCDataSections);
        }
        writer.writeEndElement();
    }

    // subclasses can add additional content using this
    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        populate(cursor, ELEM_TRANSFORMATION);
    }
    protected void populate(FioranoStaxParser cursor, String rootElement) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(rootElement)){
            factory = getStringAttribute(cursor, ATTR_FACTORY, TRANSFORMER_FACTORY_XALAN);
            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_SCRIPT.equals(elemName))
                    script = cursor.getCData();
                else if(ELEM_PROJECT.equals(elemName))
                    project = cursor.getCData();
                else if(ELEM_SCRIPT_FILE.equals(elemName))
                    scriptFile = cursor.getAttributeValue(null, ATTR_NAME);
                else if(ELEM_PROJECT_FILE.equals(elemName))
                    projectFile = cursor.getAttributeValue(null, ATTR_NAME);
                else if(ELEM_TRANSFORMATION_CONFIG_NAME.equals(elemName))
                    transformationConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                else
                    populate_1(cursor);
            }
        }
    }

    // allow subclasses to read additonal content using this
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    public void convert(com.fiorano.openesb.application.aps.Route that){
        factory = "Saxon".equals(that.getTransformationType()) ? SAXON_TRANSFORMER_FACTORY : XALAN_TRANSFORMER_FACTORY;
        script = that.getTransformationXSL();
        project = that.getTransformationProject();
        convert_1(that);
    }

    public void convert_1(com.fiorano.openesb.application.aps.Route that){}

    public void convert(OutPortInst that){
        factory = "Saxon".equals(that.getTransformerType()) ? SAXON_TRANSFORMER_FACTORY : XALAN_TRANSFORMER_FACTORY;
        script = that.getContextXSL();
        project = that.getContextInfo();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        factory = TRANSFORMER_FACTORY_XALAN;
        script = null;
        project = null;
        scriptFile = null;
        projectFile = null;
        transformationConfigName = null;
    }

    /**
     * @bundle INVALID_TRANSFORMATION_SPECIFIED=Transformation Factory cannot be null
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(factory))
            throw new FioranoException( "INVALID_TRANSFORMATION_SPECIFIED");
        validateScript();
    }

    /**
     * @bundle SCRIPT_UNSPECIFIED=Transformation script is not specified
     */
    protected void validateScript() throws FioranoException{
        if(StringUtil.isEmpty(script))
            throw new FioranoException("SCRIPT_UNSPECIFIED");
    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeUTF(factory);
        bytesMessage.writeUTF(jmsScript);
        bytesMessage.writeUTF(project);
        bytesMessage.writeUTF(projectFile);
        bytesMessage.writeUTF(script);
        bytesMessage.writeUTF(scriptFile);
        bytesMessage.writeUTF(transformationConfigName);
    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        factory=bytesMessage.readUTF();
        jmsScript=bytesMessage.readUTF();
        project=bytesMessage.readUTF();
        projectFile=bytesMessage.readUTF();
        script=bytesMessage.readUTF();
        scriptFile=bytesMessage.readUTF();
        transformationConfigName=bytesMessage.readUTF();
    }
}

