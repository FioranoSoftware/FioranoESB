/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.LogModule;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;
import java.util.regex.Pattern;

public class Route extends InflatableDMIObject implements NamedObject{
    /**
     * Element route in event process xml
     */
    public static final String ELEM_ROUTE = "route";

    public int getObjectID(){
        return DmiObjectTypes.NEW_ROUTE;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns name of this route
     * @return String - Name of the route
     */
    public String getName(){
        return name;
    }

    /**
     * Sets name of this route
     * @param name Name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ ShortDescription ]---------------------------------------------------*/
    /**
     * Element short-description in event process xml
     */
    public static final String ELEM_SHORT_DESCRIPTION = "short-description";

    private String shortDescription;

    /**
     * Returns short description for this route
     * @return String - Short description
     */
    public String getShortDescription(){
        return shortDescription;
    }

    /**
     * Sets short description for this route
     * @param shortDescription Short description to be set
     */
    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    /*-------------------------------------------------[ LongDescription ]---------------------------------------------------*/
    /**
     * Element long-description in event process xml
     */
    public static final String ELEM_LONG_DESCRIPTION = "long-description";

    /**
     * @bundle $class.$member.displayName=Long Description
     * @bundle $class.$member.shortDescription=Long Description for this Route
     */
    private String longDescription;

    /**
     * Returns long description for this route
     * @return String - Long description
     */
    public String getLongDescription(){
        return longDescription;
    }

    /**
     * Sets long description for this route
     * @param longDescription Long description to be set
     */
    public void setLongDescription(String longDescription){
        this.longDescription = longDescription;
    }


    /*-------------------------------------------------[ durable ]---------------------------------------------------*/
    /**
     * Attribute durable
     */
    public static final String ATTR_DURABLE = "durable";

    private boolean durable = false;

    /**
     * Returns true if it is durable route, false otherwise
     * @return boolean - true if routes are durable, false otherwise
     */
    public boolean isDurable(){
        return durable;
    }

    /**
     * Sets a boolean specifying whether this route is durable
     * @param durable true if routes are to be durable, false otherwise
     */
    public void setDurable(boolean durable){
        this.durable = durable;
    }

    /*-------------------------------------------------[ durability_override ]---------------------------------------------------*/
    /**
     * Attribute priority for route durability. If ROUTE is set then application level flag is ignored
     */
    public static final String ATTR_ROUTE_DURABLE_PRIORITY = "durabilitySource";
    //Todo: feasibility of having FES flag in the PRIORITY_LEVEL
    public enum PRIORITY_LEVEL {Application, Route;
        public boolean equals(String val){
            return this.toString().equals(val);
        }
    };


    /**
     * If Value is Application get the flag from application level
     * If Value is Route  then takes the route level flag
     * Default value is application
     */
    private PRIORITY_LEVEL durabilitySource = PRIORITY_LEVEL.Application;

    /**
     * Returns source for route Durability
     * @return {ROUTE, APPLICATION}
     */
    public String getDurabilitySource(){
        return durabilitySource.toString();
    }

    /**
     * Sets a source for route durability
     * @param source {ROUTE, APPLICATION}
     */
    public void setDurabilitySource(String source){
        this.durabilitySource = PRIORITY_LEVEL.valueOf(source);
    }
    /*-------------------------------------------------[ source ]---------------------------------------------------*/
    /**
     * Element source in event process xml
     */
    public static final String ELEM_SOURCE = "source";
    /**
     * Attribute inst
     */
    public static final String ATTR_SOURCE_SERVICE_INSTANCE = "inst";
    /**
     * Attribute port
     */
    public static final String ATTR_SOURCE_PORT_INSTANCE = "port";

    private String sourceServiceInstance;

    /**
     * Returns name of source service instance for this route
     * @return String - Name of Source service instance
     */
    public String getSourceServiceInstance(){
        return sourceServiceInstance;
    }

    /**
     * Sets name of source service instance for this route
     * @param sourceServiceInstance Source service instance name to be set
     */
    public void setSourceServiceInstance(String sourceServiceInstance){
        this.sourceServiceInstance = sourceServiceInstance;
    }

    private String sourcePortInstance;

    /**
     * Returns name of source port instance for this route
     * @return String - Name of the source port instance
     */
    public String getSourcePortInstance(){
        return sourcePortInstance;
    }

    /**
     * Sets name of source port instance for this route
     * @param sourcePortInstance Name of source port instance to be set
     */
    public void setSourcePortInstance(String sourcePortInstance){
        this.sourcePortInstance = sourcePortInstance;
    }

    /*-------------------------------------------------[ Target ]---------------------------------------------------*/
    /**
     * Element target in event process xml
     */
    public static final String ELEM_TARGET = "target";
    /**
     * Attribute inst
     */
    public static final String ATTR_TARGET_SERVICE_INSTANCE = "inst";
    /**
     * Attribute port
     */
    public static final String ATTR_TARGET_PORT_INSTANCE = "port";

    private String targetServiceInstance;

    /**
     * Returns name of target service instance for this route
     * @return String - Name of target service instance
     */
    public String getTargetServiceInstance(){
        return targetServiceInstance;
    }

    /**
     * Sets target service instance name for this route
     * @param targetServiceInstance Name of target service instance
     */
    public void setTargetServiceInstance(String targetServiceInstance){
        this.targetServiceInstance = targetServiceInstance;
    }

    private String targetPortInstance;

    /**
     * Returns name of target port instance for this route
     * @return String - Name of target port instance
     */
    public String getTargetPortInstance(){
        return targetPortInstance;
    }

    /**
     * Sets target port instance name for this route
     * @param targetPortInstance Name of target port instance to be set
     */
    public void setTargetPortInstance(String targetPortInstance){
        this.targetPortInstance = targetPortInstance;
    }
    /*-------------------------------------------------[ Transformation ]---------------------------------------------------*/

    private MessageTransformation messageTransformation = null;

    /**
     * Returns the message transformation used
     * @return MessageTransformation - Message transformation object
     */
    public MessageTransformation getMessageTransformation(){
        return messageTransformation;
    }

    /**
     * Sets the message transformation to be used
     * @param messageTransformation Message transformation to be set
     */
    public void setMessageTransformation(MessageTransformation messageTransformation){
        this.messageTransformation = messageTransformation;
    }

    /*-------------------------------------------------[ Selectors ]---------------------------------------------------*/
    /**
     * Element selectors in event process xml
     */
    public static final String ELEM_SELECTORS = "selectors";
    /**
     * Element selector in event process xml
     */
    public static final String ELEM_SELECTOR = "selector";
    /**
     * Attribute type
     */
    public static final String ATTR_TYPE = "type";

    private Map selectors = new HashMap();
    /**
     * Sender selector
     */
    public static final String SELECTOR_SENDER = "sender";
    /**
     * JMS selector
     */
    public static final String SELECTOR_JMS = "jms";
    /**
     * Body selector
     */
    public static final String SELECTOR_BODY = "body";
    /**
     * Application-context selector
     */
    public static final String SELECTOR_APPLICATION_CONTEXT = "application-context";

    /*-------------------------------------------------[ selector-config-name ]---------------------------------------------------*/
    /**
     * Element messaging-config-name in event process xml
     */
    public static final String ELEM_SELECTOR_CONFIG_NAME = "selector-config-name";

    private String selectorConfigName;

    /**
     * Returns configuration name of this selector element
     * @return String - Selector configuration name
     */
    public String getSelectorConfigName(){
        return selectorConfigName;
    }

    /**
     * Sets configuration name of this selector element
     * @param selectorConfigName Configuration name
     */
    public void setSelectorConfigName(String selectorConfigName){
        this.selectorConfigName = selectorConfigName;
    }

    /**
     * Sets sender selector on this route which selects message coming only from service instance having name <code>sender</code>
     * @param sender Service instance name
     */
    public void setSenderSelector(String sender){
        if(sender!=null)
            selectors.put(SELECTOR_SENDER, sender);
        else
            selectors.remove(SELECTOR_SENDER);
    }

    /**
     * Sets jms selector on this route having specified jms query <code>jmsquery</code>
     * @param jmsquery JMS query
     */
    public void setJMSSelector(String jmsquery){
        if(jmsquery!=null)
            selectors.put(SELECTOR_JMS, jmsquery);
        else
            selectors.remove(SELECTOR_JMS);
    }

    /**
     * Sets specified message body xpathselector <code>selector</code> on this route
     * @param selector Xpath selector
     */
    public void setBodySelector(XPathSelector selector){
        if(selector!=null)
            selectors.put(SELECTOR_BODY, selector);
        else
            selectors.remove(SELECTOR_BODY);
    }

    /**
     * Sets application context selector on this route
     * @param selector Application context selector
     */
    public void setApplicationContextContextSelector(XPathSelector selector){
        if(selector!=null)
            selectors.put(SELECTOR_APPLICATION_CONTEXT, selector);
        else
            selectors.remove(SELECTOR_APPLICATION_CONTEXT);
    }

    /**
     * Returns sender selector on this route
     * @return String - Sender selector
     */
    public String getSenderSelector(){
        return (String)selectors.get(SELECTOR_SENDER);
    }

    /**
     * Returns JMS selector query on this route
     * @return String - JMS selector
     */
    public String getJMSSelector(){
        return (String)selectors.get(SELECTOR_JMS);
    }

    /**
     * Returns Message Body XPath selector on this route
     * @return XPathSelector - XPath selector
     */
    public XPathSelector getBodySelector(){
        return (XPathSelector)selectors.get(SELECTOR_BODY);
    }

    /**
     * Returns application context selector on this route
     * @return XPathSelector - XPath selector
     */
    public XPathSelector getApplicationContextSelector(){
        return (XPathSelector)selectors.get(SELECTOR_APPLICATION_CONTEXT);
    }

    /**
     * Returns all selectors on this route
     * @return Map - Map of all selectors on the route
     */
    public Map getSelectors(){
        return selectors;
    }

    /**
     * Sets specified <code>selectors</code> as selectors on this route
     * @param selectors Selectors on this route
     */
    public void setSelectors(Map selectors){
        this.selectors = selectors;
    }

    /*-------------------------------------------------[ LogManager ]---------------------------------------------------*/

    private LogManager logManager;

    /**
     * Returns log manager
     * @return LogManager - Log manager used
     */
    public LogManager getLogManager(){
        return null;
    }

    /**
     * Sets log manager for this route
     * @param logManager Log manager to be set
     * @deprecated
     */
    public void setLogManager(LogManager logManager){
        this.logManager = logManager;
    }

    /*-------------------------------------------------[ LogModules ]---------------------------------------------------*/
    /**
     * Element logmodules in event process xml
     */
    public static final String ELEM_LOGMODULES = "logmodules";

    private List logModules = new ArrayList();

    /**
     * Adds specified <code>logModule</code> to log modules of this route
     * @param logModule Log module to be added
     */
    public void addLogModule(LogModule logModule){
        logModules.add(logModule);
    }

    /**
     * Removes specified <code>logModule</code> from log modules of this route
     * @param logModule Log module to be removed
     */
    public void removeLogModule(LogModule logModule){
        logModules.remove(logModule);
    }

    /**
     * Returns a list of log modules for this route
     * @return List - List of log modules
     */
    public List getLogModules(){
        return new ArrayList();
    }

    /**
     * Sets specified <code>logModules</code> as log modules for this route
     * @param logModules Log modules to be set
     * @deprecated
     */
    public void setLogModules(List logModules){
        this.logModules = logModules;
    }

    /**
     * Clears log modules for this route
     */
    public void clearLogModules(){
        this.logModules.clear();
    }

    /*-------------------------------------------------[ Compression ]---------------------------------------------------*/
    /**
     * Element messages in event process xml
     */
    public static final String ELEM_MESSAGES = "messages";
    /**
     * Attribute compress
     */
    public static final String ATTR_COMPRESS = "compress";

    private boolean compressed = false;

    /**
     * Returns true if the messages are compressed by route
     * @return boolean - true if messages are compressed, false otherwise
     */
    public boolean isMessageCompressed(){
        return compressed;
    }

    /**
     * Sets a boolean specifying whether messages have to be compressed on this route
     * @param compressed true if messages are compressed, false otherwise
     */
    public void setMessageCompression(boolean compressed){
        this.compressed = compressed;
    }

    /*-------------------------------------------------[ Encryption ]---------------------------------------------------*/
    /**
     * Attribute encrypt
     */
    public static final String ATTR_ENCRYPT = "encrypt";

    private boolean encrypted = false;

    /**
     * Returns true if the messages are encrypted by route
     * @return boolean - true if messages are encrypted, false otherwise
     */
    public boolean isMessageEncrypted(){
        return encrypted;
    }

    /**
     * Sets a boolean specifying whether messages have to be encrypted on this route
     * @param encrypted true if messages are encrypted, false otherwise
     */
    public void setMessageEncryption(boolean encrypted){
        this.encrypted = encrypted;
    }

    /*-------------------------------------------------[ messaging-config-name ]---------------------------------------------------*/
    /**
     * Element messaging-config-name in event process xml
     */
    public static final String ELEM_MESSAGING_CONFIG_NAME = "messaging-config-name";

    private String messagingConfigName;

    /**
     * Returns configuration name of this messaging element
     * @return String - Configuration name
     */
    public String getMessagingConfigName(){
        return messagingConfigName;
    }

    /**
     * Sets configuration name of this messaging element
     * @param messagingConfigName Configuration name
     */
    public void setMessagingConfigName(String messagingConfigName){
        this.messagingConfigName = messagingConfigName;
    }

    /*-------------------------------------------------[ Ignore Absence Of Transformation ]---------------------------------------------------*/
    public static final String ATTR_IGNORE_ABSENCE_TRANSFORMATION = "ignoreAbsenceOfTransformation";

    boolean ignoreAbsenceOfTransformation = false;

    public boolean getIgnoreAbsenceOfTransformation(){
        return ignoreAbsenceOfTransformation;
    }

    public void setIgnoreAbsenceOfTransformation(boolean ignoreAbsenceOfTransformation){
        this.ignoreAbsenceOfTransformation = ignoreAbsenceOfTransformation;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, true);
    }

    /*
     * <route name="string">
     *      <source inst="string" port="string"/>
     *      <target inst="string" port="string"/>
     *      <short-description>string</short-description>?
     *      <long-description>string</long-description>?
     *      <messages compress="boolean"? durable="boolean"? encrypt="boolean"? />
     *       ...message-transformation?...
     *      <selectors>
     *          <selector type="sender|jms|body|application-context">string|...xpath...</selector>+
     *      </selectors>?
     * </route>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_ROUTE);
        {
            writer.writeAttribute(ATTR_NAME, name);
            if(ignoreAbsenceOfTransformation){//write only if true
                writer.writeAttribute(ATTR_IGNORE_ABSENCE_TRANSFORMATION, String.valueOf(ignoreAbsenceOfTransformation));
            }
            writer.writeStartElement(ELEM_SOURCE);
            {
                writer.writeAttribute(ATTR_SOURCE_SERVICE_INSTANCE, sourceServiceInstance);
                writer.writeAttribute(ATTR_SOURCE_PORT_INSTANCE, sourcePortInstance);
            }
            writer.writeEndElement();

            writer.writeStartElement(ELEM_TARGET);
            {
                writer.writeAttribute(ATTR_TARGET_SERVICE_INSTANCE, targetServiceInstance);
                writer.writeAttribute(ATTR_TARGET_PORT_INSTANCE, targetPortInstance);
            }
            writer.writeEndElement();

            writeElement(writer, ELEM_SHORT_DESCRIPTION, shortDescription);
            writeElement(writer, ELEM_LONG_DESCRIPTION, longDescription);

            if(messagingConfigName != null){
                writer.writeStartElement(ELEM_MESSAGING_CONFIG_NAME);
                {
                    writer.writeAttribute(ATTR_NAME, messagingConfigName);
                }
                writer.writeEndElement();
            }else{
                writer.writeStartElement(ELEM_MESSAGES);
                {
                    if(compressed)
                        writer.writeAttribute(ATTR_COMPRESS, String.valueOf(true));
                    if(durable)
                        writer.writeAttribute(ATTR_DURABLE, String.valueOf(true));
                    if(encrypted)
                        writer.writeAttribute(ATTR_ENCRYPT, String.valueOf(true));
                    writer.writeAttribute(ATTR_ROUTE_DURABLE_PRIORITY, String.valueOf(durabilitySource));
                }
                writer.writeEndElement();
            }
            
            if(messageTransformation!=null)
                messageTransformation.toJXMLString(writer, writeCDataSections);

            if(selectorConfigName != null){
                writer.writeStartElement(ELEM_SELECTOR_CONFIG_NAME);
                {
                    writer.writeAttribute(ATTR_NAME, selectorConfigName);
                }
                writer.writeEndElement();
            }else if(selectors.size() > 0){
                writer.writeStartElement(ELEM_SELECTORS);
                {
                    Iterator iter = selectors.entrySet().iterator();
                    while(iter.hasNext()){
                        Map.Entry entry = (Map.Entry)iter.next();
                        writer.writeStartElement(ELEM_SELECTOR);
                        {
                            writer.writeAttribute(ATTR_TYPE, (String)entry.getKey());
                            if(entry.getValue() instanceof XPathSelector)
                                ((XPathSelector)entry.getValue()).toJXMLString(writer);
                            else
                                writer.writeCharacters((String)entry.getValue());
                        }
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }

            if(logManager!=null)
                logManager.toJXMLString(writer);
            writeCollection(writer, logModules, ELEM_LOGMODULES);
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_ROUTE)){
            name = cursor.getAttributeValue(null,ATTR_NAME);
            ignoreAbsenceOfTransformation = getBooleanAttribute(cursor, ATTR_IGNORE_ABSENCE_TRANSFORMATION,  false);

            if(DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(name).find()){
                StringBuffer nameBuff = new StringBuffer(name);
                for(int i = 0; i<nameBuff.length(); i++){
                    if(DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(String.valueOf(nameBuff.charAt(i))).find()){
                        nameBuff.setCharAt(i, '_');
                    }
                }
                name = nameBuff.toString();
            }

            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_SOURCE.equals(elemName)){
                    sourceServiceInstance = cursor.getAttributeValue(null, ATTR_SOURCE_SERVICE_INSTANCE);
                    sourcePortInstance = cursor.getAttributeValue(null, ATTR_SOURCE_PORT_INSTANCE);
                }else if(ELEM_TARGET.equals(elemName)) {
                    targetServiceInstance = cursor.getAttributeValue(null, ATTR_SOURCE_SERVICE_INSTANCE);
                    targetPortInstance = cursor.getAttributeValue(null, ATTR_TARGET_PORT_INSTANCE);
                }else if(ELEM_SHORT_DESCRIPTION.equals(elemName))
                    shortDescription = cursor.getText();
                else if(ELEM_LONG_DESCRIPTION.equals(elemName))
                    longDescription = cursor.getText();
                else if(ELEM_MESSAGES.equals(elemName)){
                    populateMessagingConfiguration(cursor);
                }else if(ELEM_MESSAGING_CONFIG_NAME.equals(elemName)){
                    messagingConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                }else if(MessageTransformation.ELEM_MESSAGE_TRANSFORMATION.equals(elemName)){
                    messageTransformation = new MessageTransformation();
                    messageTransformation.setFieldValues(cursor);
                }else if(ELEM_SELECTORS.equals(elemName)){
                    populateSelectorsConfiguration(cursor);
                }else if(ELEM_SELECTOR_CONFIG_NAME.equals(elemName)){
                    selectorConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                }else if(LogManager.ELEM_LOG_MANAGER.equals(elemName)){
                    logManager = new LogManager();
                    logManager.setFieldValues(cursor);
                }else if(ELEM_LOGMODULES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(LogModule.ELEM_LOGMODULE.equals(cursor.getLocalName())){
                            LogModule logModule = new LogModule();
                            logModule.setFieldValues(cursor);
                            logModules.add(logModule);
                        }
                    }
                    cursor.resetCursor();
                }
            }
        }
    }

    /**
     * Populates the messaging configurations
     * @param cursor Stax parser cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateMessagingConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ELEM_MESSAGES)){
            compressed = getBooleanAttribute(cursor, ATTR_COMPRESS, false);
            durable = getBooleanAttribute(cursor, ATTR_DURABLE, false);
            encrypted = getBooleanAttribute(cursor, ATTR_ENCRYPT, false);
            String durability = cursor.getAttributeValue(null, ATTR_ROUTE_DURABLE_PRIORITY);
            if(durability != null)
                durabilitySource = PRIORITY_LEVEL.valueOf(durability);
            else
                durabilitySource = PRIORITY_LEVEL.Application;

            cursor.resetCursor();
        }
    }

    /**
     * Populates the selector configurations
     * @param cursor Stax parser cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateSelectorsConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ELEM_SELECTORS)){
            while(cursor.nextElement()){
                if(ELEM_SELECTOR.equals(cursor.getLocalName())){
                    String type = cursor.getAttributeValue(null, ATTR_TYPE);
                    if(SELECTOR_BODY.equals(type) || SELECTOR_APPLICATION_CONTEXT.equals(type)){
                        XPathSelector selector = new XPathSelector();
                        selector.setFieldValues(cursor);
                        selectors.put(type, selector);
                    } else
                        selectors.put(type, cursor.getTextContent());
                }
            }

            cursor.resetCursor();
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old Route DMI
     */
    public void convert(com.fiorano.openesb.application.aps.Route that){

        name = that.getRouteGUID();
        if(!Pattern.matches("([a-zA-Z0-9_]+)", name)){
            StringBuffer nameBuff = new StringBuffer(name);
            for(int i = 0; i < nameBuff.length(); i++){
                if(!Pattern.matches("([a-zA-Z0-9_]+)", String.valueOf(nameBuff.charAt(i)))){
                    nameBuff.setCharAt(i, '_');
                }
            }
            name = nameBuff.toString();
        }

        sourceServiceInstance = that.getSrcServInst();
        sourcePortInstance = that.getSrcPortName();

        targetServiceInstance = that.getTrgtServInst();
        targetPortInstance = that.getTrgtPortName();

        shortDescription = that.getShortDescription();
        longDescription = that.getLongDescription();
        durable = that.isDurable();

        if(!StringUtils.isEmpty(that.getTransformationXSL()) || !StringUtils.isEmpty(that.getMessageTransformationXSL())){
            messageTransformation = new MessageTransformation();
            messageTransformation.convert(that);
        }
        Iterator iter = that.getSelectors().entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String type = (String)entry.getKey();
            if(com.fiorano.openesb.application.aps.Route.JMS_SELECTOR.equalsIgnoreCase(type))
                selectors.put(SELECTOR_JMS, entry.getValue());
            else if(com.fiorano.openesb.application.aps.Route.SENDER_SELECTOR.equalsIgnoreCase(type))
                selectors.put(SELECTOR_SENDER, entry.getValue());
            else{
                XPathSelector selector = new XPathSelector();
                if(entry.getValue() instanceof XPathDmi)
                    selector.convert((XPathDmi)entry.getValue());
                else
                    selector.setXPath((String)entry.getValue());
                selectors.put(com.fiorano.openesb.application.aps.Route.APP_CONTEXT_XPATH.equalsIgnoreCase(type) ? SELECTOR_APPLICATION_CONTEXT : SELECTOR_BODY, selector);
            }
        }
    }

    /*-------------------------------------------------[ NamedObject ]---------------------------------------------------*/
    /**
     * Returns key
     * @return String - Key
     */
    public String getKey(){
        return getName();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        name = null;
        shortDescription = null;
        longDescription = null;
        durable = true;

        compressed = false;
        encrypted = false;
        messagingConfigName = null;
        selectorConfigName = null;

        sourcePortInstance = null;
        sourceServiceInstance = null;

        targetPortInstance = null;
        targetServiceInstance = null;
        messageTransformation = null;

        selectors.clear();
    }

    /**
     * Validates the Application DMI. Check whether all the mandatory fields are set.
     *
     * The Possible Error Mesages:
     * @bundle ROUTE_NAME_UNSPECIFIED=Route Name is not specified
     * @bundle INVALID_ROUTE_NAME=Route Name cannot have these characters . ~ ! @ # % ^ ( ) [ ] { } + - * '(single quote) ,(comma) ;
     * @bundle SOURCE_PORT_UNSPECIFIED=Source Port Name is not specified
     * @bundle SOURCE_SERVICE_INSTANCE_UNSPECIFIED=Source Service Instance Name is not specified
     * @bundle TARGET_PORT_UNSPECIFIED=Target Port Name is not specified
     * @bundle TARGET_SERVICE_INSTANCE_UNSPECIFIED=Target Service Instance Name is not specified
     * @bundle ERROR_VALIDATING_ROUTE=Error validating route:{0}
     */
    public void validate() throws FioranoException
    {
        validateRouteName();
        try {
            if(StringUtils.isEmpty(sourcePortInstance))
                throw new FioranoException("SOURCE_PORT_UNSPECIFIED");
            if(StringUtils.isEmpty(sourceServiceInstance))
                throw new FioranoException("SOURCE_SERVICE_INSTANCE_UNSPECIFIED");
            if(StringUtils.isEmpty(targetPortInstance))
                throw new FioranoException("TARGET_PORT_UNSPECIFIED");
            if(StringUtils.isEmpty(targetServiceInstance))
                throw new FioranoException("TARGET_SERVICE_INSTANCE_UNSPECIFIED");
            if(selectors.get(SELECTOR_APPLICATION_CONTEXT)!=null)
                ((XPathSelector)selectors.get(SELECTOR_APPLICATION_CONTEXT)).validate();
            if(messageTransformation!=null)
                messageTransformation.validate();
            if(selectors.get(SELECTOR_BODY)!=null)
                ((XPathSelector)selectors.get(SELECTOR_BODY)).validate();
        } catch (FioranoException e) {
            throw new FioranoException( e);
        }
    }

    /**
     * Validates name given a route
     * Possible error messages
     * @bundle ROUTE_NAME_UNSPECIFIED=Route Name is not specified
     * @bundle INVALID_ROUTE_NAME=Route Name cannot have these characters ` . ~ ! @ # $ % ^ & ( ) [ ] { } + - * = '(single quote) ,(comma) : " ? < > ;
     * @throws com.fiorano.openesb.utils.exception.FioranoException If name is not valid
     */
    private void validateRouteName() throws FioranoException
    {
        if (StringUtils.isEmpty(name))
            throw new FioranoException("ROUTE_NAME_UNSPECIFIED");
        else if (DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(name).find())
            throw new FioranoException( "INVALID_ROUTE_NAME");
    }
}
