/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.aps.OutPortInst;
import com.fiorano.openesb.application.aps.PortInst;
import com.fiorano.openesb.application.aps.PortInstConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.Port;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.apache.commons.lang3.StringUtils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

public class PortInstance extends Port{
    /**
     * Element port-instance in event process xml
     */
    public static final String ELEM_PORT_INSTANCE = "port-instance";
    /**
     * On exception port name
     */
    public static final String EXCEPTION_PORT_NAME = "ON_EXCEPTION";

    // Custom db call out params list
    private List<DBCallOutParameter> dbCallOutParameterList = new ArrayList<DBCallOutParameter>();

    private boolean calloutEnabled = false;

    /*
    * The Application Context Value can be STORE or RESTORE. When it is STORE, the PortMessageListener copies the application context present inside Carry Forward context to the property APPLICATION_NAME__APPLICATION_VERSION__APPLICATION_CONTEXT
    * When it is RESTORE it copies it back to Carry Forward context
    */


    public int getObjectID(){
        return DmiObjectTypes.NEW_PORT_INSTANCE;
    }

    public void setDbCallOutParameterList(List<DBCallOutParameter> dbCallOutParameterList){
        this.dbCallOutParameterList = dbCallOutParameterList;
    }

    public List<DBCallOutParameter> getDbCallOutParameterList(){
        return dbCallOutParameterList;
    }

    /*-------------------------------------------------[ SecurityManager ]---------------------------------------------------*/
    /**
     * Element jms in event process xml
     */
    public static final String ELEM_JMS = "jms";
    /**
     * Element authentication in event process xml
     */
    public static final String ELEM_AUTHENTICATION = "authentication";
    /**
     * Element security-manager in event process xml
     */
    public static final String ATTR_SECURITY_MANAGER = "security-manager";


    private String securityManager;

    /**
     * Returns security manager for this port.
     * @return String - Security manager for this port
     */
    public String getSecurityManager(){
        return securityManager;
    }

    /**
     * Sets specified security manager <code>securityManager</code> for this port
     * @param securityManager Security manager to be set
     */
    public void setSecurityManager(String securityManager){
        this.securityManager = securityManager;
    }

    /*-------------------------------------------------[ ProxyUsed ]---------------------------------------------------*/
    /**
     * Element proxy in event process xml
     */
    public static final String ELEM_PROXY = "proxy";
    /**
     * Attribute used
     */
    public static final String ATTR_PROXY_USED = "used";

    private boolean proxyUsed = false;

    /**
     * Specifies whether proxy is used for this port
     * @return boolean - true if proxy is to be used, false otherwise
     */
    public boolean isProxyUsed(){
        return proxyUsed;
    }

    /**
     * Sets a boolean specifying whether proxy is used for this port
     * @param proxyUsed true if proxy is to be used, false otherwise
     */
    public void setProxyUsed(boolean proxyUsed){
        this.proxyUsed = proxyUsed;
    }

    /*-------------------------------------------------[ ProxyURL ]---------------------------------------------------*/
    /**
     * Attribute url
     */
    public static final String ATTR_PROXY_URL = "url";

    private String proxyURL;

    /**
     * Returns proxy URL for this port
     * @return String - Proxy URL
     */
    public String getProxyURL(){
        return proxyURL;
    }

    /**
     * Sets specified proxy URL <code>proxyURL</code> for this port
     * @param proxyURL Proxy URL to be set
     */
    public void setProxyURL(String proxyURL){
        this.proxyURL = proxyURL;
    }

    /*-------------------------------------------------[ User ]---------------------------------------------------*/
    /**
     * Attribute user
     */
    public static final String ATTR_PROXY_USER = "user";
    /**
     * User anonymous
     */
    public static final String ANONYMOUS = "anonymous";

    private String proxyUser = ANONYMOUS; //NOI18N

    /**
     * Returns user name used to connect to Proxy Server
     * @return String - Username
     */
    public String getProxyUser(){
        return proxyUser;
    }

    /**
     * Sets specified proxy User name <code>proxyUser</code> for this port
     * @param proxyUser Proxy User name
     */
    public void setProxyUser(String proxyUser){
        this.proxyUser = proxyUser;
    }

    /*-------------------------------------------------[ Password ]---------------------------------------------------*/
    /**
     * Attribute password
     */
    public static final String ATTR_PROXY_PASSWORD = "password";

    private String proxyPassword = ANONYMOUS; //NOI18N

    /**
     * Returns Password used to connect Proxy Server
     * @return String - Password
     */
    public String getProxyPassword(){
        return proxyPassword;
    }

    /**
     * Sets specified password used to connect to proxy server
     * @param proxyPassword Password to be set
     */
    public void setProxyPassword(String proxyPassword){
        this.proxyPassword = proxyPassword;
    }

    /*-------------------------------------------------[ User ]---------------------------------------------------*/
    /**
     * Attribute user
     */
    public static final String ATTR_USER = "user";

    private String user = ANONYMOUS; //NOI18N

    /**
     * Return user name used to create JMS Connection
     * @return String - Username
     */
    public String getUser(){
        return user;
    }

    /**
     * Sets username to create JMS connection
     * @param user Username to be set
     */
    public void setUser(String user){
        this.user = user;
    }

    /*-------------------------------------------------[ Password ]---------------------------------------------------*/
    /**
     * Attribute password
     */
    public static final String ATTR_PASSWORD = "password";

    private String password = ANONYMOUS; //NOI18N

    /**
     * Returns Password used to create JMS Connection
     * @return String - Password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets password used to create JMS connection
     * @param password Password to be set
     */
    public void setPassword(String password){
        this.password = password;
    }

    /*-------------------------------------------------[ UseSpecifiedDestination ]---------------------------------------------------*/
    /**
     * Element destination in event process xml
     */
    public static final String ELEM_DESTINATION = "destination";
    /**
     * Custom element in event process xml
     */
    public static final String ATTR_SPECIFIED_DESTINATION_USED = "custom";

    private boolean specifiedDestinationUsed = false;

    /**
     * Specifies whether to Use Specified Destination
     * @return boolean - true if specified destination is to be used, false otherwise
     */
    public boolean isSpecifiedDestinationUsed(){
        return specifiedDestinationUsed;
    }

    /**
     * Sets a boolean which specifies whether to use specified destination
     * @param specifiedDestinationUsed true if specified destination is to be used, false otherwise
     */
    public void setSpecifiedDestinationUsed(boolean specifiedDestinationUsed){
        this.specifiedDestinationUsed = specifiedDestinationUsed;
    }

    /*-------------------------------------------------[ destination ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_DESTINATION = "name";

    private String destination = null;

    /**
     * Returns destination Name of this port
     * @return String - Destination name
     */
    public String getDestination(){
        return destination;
    }

    /**
     * Sets destination name for this port
     * @param destination Destination Name for this port
     */
    public void setDestination(String destination){
        this.destination = destination;
    }

    /*-------------------------------------------------[ destinationType ]---------------------------------------------------*/
    /**
     * Attribute type
     */
    public static final String ATTR_DESTINATION_TYPE = "type";
    /**
     * Destination type queue
     */
    public static final int DESTINATION_TYPE_QUEUE = 0;
    /**
     * Destination type topic
     */
    public static final int DESTINATION_TYPE_TOPIC = 1;
    /**
     * Attribute encryption boolean
     */
    public static final String IS_DESTINATION_ENCRYPTED = "encrypted";
    /**
     * Attribute encryption algorithm
     */
    public static final String DESTINATION_ENCRYPT_ALGO = "encryptionAlgo";
    /**
     * Attribute encryption key
     */
    public static final String DESTINATION_ENCRYPT_KEY = "encryptionKey";
    /**
     * Attribute allow padding to key boolean
     */
    public static final String DESTINATION_ALLOW_PADDING_TO_KEY = "allowPaddingToKey";
    /**
     * Initialization vector
     */
    public static final String DESTINATION_INITIALIZATION_VECTOR = "initializationVector";
    /**
     * Default destination type
     */
    public int destinationType = getDefaultDestinationType();

    private boolean isDestinationEncrypted;
    private String encryptionKey;
    private String encryptionAlgorithm;
    private boolean allowPaddingToKey;
    private String initializationVector;

    /**
     * Returns destination type i.e Queue or Topic of this port
     * @return int - Destination type
     */
    public int getDestinationType(){
        return destinationType;
    }

    /**
     * Sets destination type for this port
     * @param destinationType Destination type to be set i.e. 0 for Queue and 1 for topic
     */
    public void setDestinationType(int destinationType){
        this.destinationType = destinationType;
    }

    /**
     * Returns if the destination is encrypted
     * @return boolean - true if destination is encrypted, false otherwise
     */
    public boolean isDestinationEncrypted() {
        return isDestinationEncrypted;
    }

    /**
     * Sets if the destination is encrypted
     * @param destinationEncrypted true if destination is encrypted, false otherwise
     */
    public void setDestinationEncrypted(boolean destinationEncrypted) {
        isDestinationEncrypted = destinationEncrypted;
    }

    /**
     * Returns encryption key for the port
     * @return String - Encryption key
     */
    public String getEncryptionKey() {
        return encryptionKey;
    }

    /**
     * Sets encryption key for the port
     * @param encryptionKey Encryption key
     */
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    /**
     * Returns the encryption algorithm to be used
     * @return String - Encryption algorithm
     */
    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
     * Sets the encryption algorithm to be used
     * @param encryptionAlgorithm Encryption algorithm
     */
    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    /**
     * Returns whether padding to the key is allowed
     * @return boolean - true is padding is allowed, false otherwise
     */
    public boolean isAllowPaddingToKey() {
        return allowPaddingToKey;
    }

    /**
     * Set allow padding to key
     * @param allowPaddingToKey true is padding is to be allowed, false otherwise
     */
    public void setAllowPaddingToKey(boolean allowPaddingToKey) {
        this.allowPaddingToKey = allowPaddingToKey;
    }

    /**
     * Returns the initialization vector
     * @return String - Initialization vector
     */
    public String getInitializationVector() {
        return initializationVector;
    }

    /**
     * Sets the initialization vector
     * @param initializationVector Initialization vector
     */
    public void setInitializationVector(String initializationVector) {
        this.initializationVector = initializationVector;
    }/*-------------------------------------------------[ ClientID ]---------------------------------------------------*/
    /**
     * Attribute client id
     */
    public static final String ATTR_CLIENT_ID = "client-id";

    private String clientID;

    /**
     * Returns JMS connection's client id
     * @return String - Client ID
     */
    public String getClientID(){
        return clientID;
    }

    /**
     * Sets client id for this port
     * @param clientID Client ID to be set
     */
    public void setClientID(String clientID){
        this.clientID = clientID;
    }

    /*-------------------------------------------------[ enabled ]---------------------------------------------------*/
    /**
     * Attribute enabled
     */
    public static final String ATTR_ENABLED = "enabled";

    private boolean enabled = true;

    /**
     * Returns a boolean specifying if the port is enabled
     * @return boolean - true if port is enabled, false otherwise
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     * Sets a boolean specifying if this port is enabled
     * @param enabled true if port is enabled, false otherwise
     */
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    /*-------------------------------------------------[ message filter ]---------------------------------------------------*/
    /**
     * Element messagefilters in event process xml
     */
    public static final String ELEM_MESSAGE_FILTERS = "messagefilters";
    /**
     * Element messagefilter for every instance in event process xml
     */
    public static final String ELEM_MESSAGE_FILTER = "messagefilter";
    /**
     * Attribute message filter value
     */
    public static final String ATTR_MESSAGE_FILTER_VALUE = "value";
    /**
     * Attribute message filter name
     */
    public static final String ATTR_MESSAGE_FILTER_NAME = "name";
     /**
     * Message filter name value pairs
     */
    private HashMap messageFilters = new HashMap();

    /**
     * @jmx.managed-attribute access="read-write"
     */
    public HashMap getMessageFilters() {
        return messageFilters;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setMessageFilters(HashMap messagefilter) {
        this.messageFilters = messagefilter;
    }

    public boolean getIsMessageFilterSet() {
        return isMessageFilterSet;
    }

    public void seIstMessageFilterSet(boolean messageFilterSet) {
        this.isMessageFilterSet = messageFilterSet;
    }

    private boolean isMessageFilterSet = false;

    public void clearMessageFilter() {
        messageFilters.clear();
        isMessageFilterSet = false;
    }
    /*-------------------------------------------------[ workflow ]---------------------------------------------------*/
    /**
     * Element workflow in event process xml
     */
    public static final String ELEM_WORKFLOW = "workflow";
    /**
     * Attribute type
     */
    public static final String ATTR_WORKFLOW_TYPE = "type";
    /**
     * Attribute datatype
     */
    public static final String ATTR_WORKFLOW_DATATYPE = "datatype";
    public static final String ATTR_CALLOUT_ENABLED = "enablecallout";
    /**
     * No workflow
     */
    public static final int WORKFLOW_NONE = 0;
    /**
     * Workflow beginning
     */
    public static final int WORKFLOW_ITEM = 1;
    /**
     * Workflow end
     */
    public static final int WORKFLOW_END = 2;

    private int workflow = WORKFLOW_NONE;

    /**
     * Specifies the role of this port in workflow
     * @return int - role as integer
     */
    public int getWorkflow(){
        return workflow;
    }

    /**
     * Sets role of this port in work flow
     * @param workflow Role to be set i.e 1 for workflow_item, 2 for workflow_end and 0 for none of them
     */
    public void setWorkflow(final int workflow){
        this.workflow = workflow;
    }

    // Allowed values for tracked data type
    // Values can be a combination (|) also
    /**
     * Tracked data type message
     */
    public static final int WORKFLOW_DATA_MESSAGE = 0;
    /**
     * Tracked data type message header
     */
    public static final int WORKFLOW_DATA_MESSAGE_HEADER = 1;
    /**
     * Tracked data type message body
     */
    public static final int WORKFLOW_DATA_MESSAGE_BODY = 2;
    /**
     * Tracked data type attachments
     */
    public static final int WORKFLOW_DATA_ATTACHMENTS = 4;
    /**
     * Tracked data type application context
     */
    public static final int WORKFLOW_DATA_APP_CONTEXT = 8;

    private int workflowDataType = WORKFLOW_DATA_MESSAGE_BODY;

    /**
     * Returns the type of data tracked on this port
     * @return int - Type of data tracked
     */
    public int getWorkflowDataType() {
        return workflowDataType;
    }

    /**
     * Sets type of data tracked on this port
     * @param workflowDataType Data type to be tracked i.e 0 for message, 1 for header, 2 for message body, 4 for attachments,
     * 8 for application context
     */
    public void setWorkflowDataType(final int workflowDataType) {
        this.workflowDataType = workflowDataType;
    }


    public boolean isCalloutEnabled() {
        return calloutEnabled;
    }

    public void setCalloutEnabled(boolean calloutEnabled) {
        this.calloutEnabled = calloutEnabled;
    }

    /*-------------------------------------------------[ workflow-config-name ]---------------------------------------------------*/
    /**
     * Element publisher-config-name in event process xml
     */
    public static final String ELEM_WORKFLOW_CONFIG_NAME = "workflow-config-name";

    private String workflowConfigName;

    /**
     * Returns configuration name of this publisher instance
     * @return String - Configuration name
     */
    public String getWorkflowConfigName(){
        return workflowConfigName;
    }

    /**
     * Sets configuration name of this publisher instance
     * @param workflowConfigName Configuration name
     */
    public void setWorkflowConfigName(String workflowConfigName){
        this.workflowConfigName = workflowConfigName;
    }

    /*-------------------------------------------------[ message-filter-config-name ]---------------------------------------------------*/
    /**
     * Element messagefilter-config-name in event process xml
     */
    public static final String ELEM_MESSAGE_FILTER_CONFIG_NAME = "messagefilters-config-name";

    private String messageFilterConfigName;

    /**
     * Returns configuration name of this message filter instance
     * @return String - Configuration name
     */
    public String getMessageFilterConfigName(){
        return messageFilterConfigName;
    }

    /**
     * Sets configuration name of this message filter instance
     * @param messageFilterConfigName Configuration name
     */
    public void setMessageFilterConfigName(String messageFilterConfigName){
        this.messageFilterConfigName = messageFilterConfigName;
    }


    /*-------------------------------------------------[ destination-config-name ]---------------------------------------------------*/
    /**
     * Element destination-config-name in event process xml
     */
    public static final String ELEM_DESTINATION_CONFIG_NAME = "destination-config-name";

    private String destinationConfigName;

    /**
     * Returns configuration name of destination
     * @return String - Configuration name
     */
    public String getDestinationConfigName(){
        return destinationConfigName;
    }

    /**
     * Sets configuration name of this destination
     * @param destinationConfigName Configuration name
     */
    public void setDestinationConfigName(String destinationConfigName){
        this.destinationConfigName = destinationConfigName;
    }

    /*-------------------------------------------------[ Application Context ]---------------------------------------------------*/
    /**
     *  Application Context Action
     */

    public static final String STORE_APP_CONTEXT = "Store Application Context";

    public static final String RESTORE_APP_CONTEXT = "Restore Application Context";
    public static final String SET_DEFAULT_APP_CONTEXT = "Set default Application Context";

    public static final String NO_ACTION_APP_CONTEXT = "No Action";

    public static final String ELEM_APP_CONTEXT_ACTION = "app-context-action";

    private String appContextAction = NO_ACTION_APP_CONTEXT;

    /**
     * Returns Application Context Action - Save/Restore
     * @return String - Application Context Action
     */
    public String getAppContextAction() {
        return appContextAction;
    }

    /**
     * Specifies whether to store/restore the Application Context
     * @param appContextAction Application Context action to be set
     */
    public void setAppContextAction(String appContextAction) {
        this.appContextAction = appContextAction;
    }


    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <port-instance>
     *      ...super-class...
     *      <proxy used="boolean" url="string">
     *          <authentication user="string" password="string"/>?
     *      </proxy>
     *      <jms client-id="string"? enabled="boolean"?>
     *          <destination type="int"? custom="boolean" name="string"/>
     *          <authentication security-manager="string"? user="string" password="string"/>?
     *          ...subclasse_2...
     *      <jms>
     *      <workflow type="int" datatype="int"/>?
     *      ...subclass_3
     * </port-instance>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_PORT_INSTANCE, writeSchema);
    }


    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        toJXMLString(writer, true);
    }

    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        if(proxyUsed || !StringUtils.isEmpty(proxyURL)
                || !ANONYMOUS.equals(proxyUser) || !ANONYMOUS.equals(proxyPassword)){
            writer.writeStartElement(ELEM_PROXY);
            {
                if(proxyUsed)
                    writer.writeAttribute(ATTR_PROXY_USED, String.valueOf(proxyUsed));
                writeAttribute(writer, ATTR_PROXY_URL, proxyURL);

                if(!ANONYMOUS.equals(proxyUser) || !ANONYMOUS.equals(proxyPassword)){
                    writer.writeStartElement(ELEM_AUTHENTICATION);
                    {
                        if(!ANONYMOUS.equals(proxyUser))
                            writer.writeAttribute(ATTR_PROXY_USER, proxyUser);
                        if(!ANONYMOUS.equals(proxyPassword))
                            writer.writeAttribute(ATTR_PROXY_PASSWORD, proxyPassword);
                    }
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }

        writer.writeStartElement(ELEM_JMS);
        {
            writeAttribute(writer, ATTR_CLIENT_ID, clientID);
            if (!enabled)
                writer.writeAttribute(ATTR_ENABLED, String.valueOf(enabled));

            if (writeSchema || destinationConfigName == null) {
                writer.writeStartElement(ELEM_DESTINATION);
                {
                    if (destinationType != getDefaultDestinationType())
                        writer.writeAttribute(ATTR_DESTINATION_TYPE, String.valueOf(destinationType));
                    if (specifiedDestinationUsed)
                        writer.writeAttribute(ATTR_SPECIFIED_DESTINATION_USED, String.valueOf(specifiedDestinationUsed));
                    writeAttribute(writer, ATTR_DESTINATION, destination);
                    if (isDestinationEncrypted) {
                        writer.writeAttribute(IS_DESTINATION_ENCRYPTED, String.valueOf(isDestinationEncrypted));
                        writer.writeAttribute(DESTINATION_ENCRYPT_ALGO, encryptionAlgorithm);
                        if (encryptionKey == null || encryptionKey.equals(""))
                            writer.writeAttribute(DESTINATION_ENCRYPT_KEY, "");
                        else
                            writer.writeAttribute(DESTINATION_ENCRYPT_KEY, encryptionKey);
                        writer.writeAttribute(DESTINATION_ALLOW_PADDING_TO_KEY, String.valueOf(allowPaddingToKey));
                        writer.writeAttribute(DESTINATION_INITIALIZATION_VECTOR, initializationVector);
                    }
                }
                writer.writeEndElement();

            } else if (destinationConfigName != null) {
                writer.writeStartElement(ELEM_DESTINATION_CONFIG_NAME);
                writer.writeAttribute(ATTR_NAME, destinationConfigName);
                writer.writeEndElement();
            }

            if(!StringUtils.isEmpty(securityManager) || !ANONYMOUS.equals(user) || !ANONYMOUS.equals(password)){
                writer.writeStartElement(ELEM_AUTHENTICATION);
                {
                    writeAttribute(writer, ATTR_SECURITY_MANAGER, securityManager);
                    if(!ANONYMOUS.equals(user))
                        writer.writeAttribute(ATTR_USER, user);
                    if(!ANONYMOUS.equals(password))
                        writer.writeAttribute(ATTR_PASSWORD, password);
                }
                writer.writeEndElement();
            }

            toJXMLString_2(writer, writeSchema);
        }
        writer.writeEndElement();

        if ((writeSchema || workflowConfigName == null)){   // We need to write port properties to stream when passing application launch packet to peer
            writer.writeStartElement(ELEM_WORKFLOW);
            writer.writeAttribute(ATTR_WORKFLOW_TYPE, String.valueOf(workflow));
            writer.writeAttribute(ATTR_WORKFLOW_DATATYPE, String.valueOf(workflowDataType));
            writer.writeAttribute(ATTR_CALLOUT_ENABLED,String.valueOf(calloutEnabled));
            for(DBCallOutParameter dbCallOutParameter : dbCallOutParameterList) {
                dbCallOutParameter.toJXMLString(writer);
            }
            writer.writeEndElement();
        } else if (workflowConfigName != null) {
            writer.writeStartElement(ELEM_WORKFLOW_CONFIG_NAME);
            writer.writeAttribute(ATTR_NAME, workflowConfigName);
            writer.writeEndElement();
        }

        if ((writeSchema || messageFilterConfigName == null) && (isMessageFilterSet)){
            writer.writeStartElement(ELEM_MESSAGE_FILTERS);
            for(Iterator itr= messageFilters.entrySet().iterator(); itr.hasNext(); ){
                Map.Entry entry= (Map.Entry) itr.next();
                writer.writeStartElement(ELEM_MESSAGE_FILTER);
                writer.writeAttribute(ATTR_MESSAGE_FILTER_NAME,(String)entry.getKey());
                writer.writeAttribute(ATTR_MESSAGE_FILTER_VALUE,(String)entry.getValue());
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }else if (messageFilterConfigName != null) {
            writer.writeStartElement(ELEM_MESSAGE_FILTER_CONFIG_NAME);
            writer.writeAttribute(ATTR_NAME, messageFilterConfigName);
            writer.writeEndElement();
        }

        if(!appContextAction.equalsIgnoreCase(NO_ACTION_APP_CONTEXT)){
            writer.writeStartElement(ELEM_APP_CONTEXT_ACTION);
            writer.writeCharacters(appContextAction);
            writer.writeEndElement();
        }

        toJXMLString_3(writer, writeSchema);
    }

    // subclasses can add additional content using this
    protected void toJXMLString_2(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{}
    protected void toJXMLString_3(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        populate(cursor, ELEM_PORT_INSTANCE);
    }

    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        String elemName = cursor.getLocalName();
        if(ELEM_PROXY.equals(elemName)){
            cursor.markCursor(ELEM_PROXY);
            proxyUsed = getBooleanAttribute(cursor, ATTR_PROXY_USED, false);
            proxyURL = cursor.getAttributeValue(null, ATTR_PROXY_URL);
            while(cursor.nextElement()){
                if(ELEM_AUTHENTICATION.equals(cursor.getLocalName())){
                    proxyUser = getStringAttribute(cursor, ATTR_PROXY_USER, ANONYMOUS);
                    proxyPassword = getStringAttribute(cursor, ATTR_PROXY_PASSWORD,ANONYMOUS);
                }
            }
            cursor.resetCursor();
        }else if(ELEM_JMS.equals(elemName)){
            cursor.markCursor(ELEM_JMS);
            clientID = cursor.getAttributeValue(null, ATTR_CLIENT_ID);
            enabled = getBooleanAttribute(cursor, ATTR_ENABLED, true);

            while(cursor.nextElement()){
                if(ELEM_DESTINATION.equals(cursor.getLocalName())){
                    destinationType = getIntegerAttribute(cursor, ATTR_DESTINATION_TYPE, getDefaultDestinationType());
                    specifiedDestinationUsed = getBooleanAttribute(cursor, ATTR_SPECIFIED_DESTINATION_USED, false);
                    destination = cursor.getAttributeValue(null, ATTR_DESTINATION);
                    isDestinationEncrypted = getBooleanAttribute(cursor, IS_DESTINATION_ENCRYPTED, false);
                    encryptionAlgorithm = cursor.getAttributeValue(null, DESTINATION_ENCRYPT_ALGO);
                    encryptionKey = cursor.getAttributeValue(null, DESTINATION_ENCRYPT_KEY);
                    if (encryptionKey != null && encryptionKey.equals(""))
                        encryptionKey = null;
                    allowPaddingToKey = getBooleanAttribute(cursor, DESTINATION_ALLOW_PADDING_TO_KEY, true);
                    initializationVector = cursor.getAttributeValue(null, DESTINATION_INITIALIZATION_VECTOR);
                }else if(ELEM_DESTINATION_CONFIG_NAME.equals(cursor.getLocalName())){
                    destinationConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                }else if(ELEM_AUTHENTICATION.equals(cursor.getLocalName())){
                    securityManager = cursor.getAttributeValue(null, ATTR_SECURITY_MANAGER);
                    user = getStringAttribute(cursor, ATTR_USER, ANONYMOUS);
                    password = getStringAttribute(cursor, ATTR_PASSWORD, ANONYMOUS);
                }else
                    populate_2(cursor);
            }
            cursor.resetCursor();
        } else if(ELEM_WORKFLOW.equals(elemName)) {
            populateWorkflowConfiguration(cursor);
        } else if(ELEM_MESSAGE_FILTERS.equals(elemName)) {
            populateMessageFilterConfiguration(cursor);
        } else if(ELEM_WORKFLOW_CONFIG_NAME.equals(elemName)) {
            workflowConfigName = cursor.getAttributeValue(null, ATTR_NAME);
        } else if(ELEM_MESSAGE_FILTER_CONFIG_NAME.equals(elemName)){
            messageFilterConfigName = cursor.getAttributeValue(null,ATTR_NAME);
        } else if(ELEM_APP_CONTEXT_ACTION.equals(elemName)) {
            appContextAction = cursor.getText();
        } else
            populate_3(cursor);
    }

    // allow subclasses read additional content using these
    protected void populate_2(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{}
    protected void populate_3(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{}

    /**
     * Populates workflow configuration
     * @param cursor Stax parser cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateWorkflowConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ELEM_WORKFLOW)) {
            workflow = getIntegerAttribute(cursor, ATTR_WORKFLOW_TYPE, WORKFLOW_NONE);
            workflowDataType = getIntegerAttribute(cursor, ATTR_WORKFLOW_DATATYPE, WORKFLOW_DATA_MESSAGE_BODY);
            calloutEnabled = getBooleanAttribute(cursor, ATTR_CALLOUT_ENABLED,false);
            if (calloutEnabled){
                populateDbCallOutParam(cursor);
            }
            cursor.resetCursor();
        }
    }
    private void populateDbCallOutParam(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        while (cursor.nextElement()) {
            if (DBCallOutParameter.ELEM_DB_CALLOUT_PARAM.equals(cursor.getLocalName())) {
                DBCallOutParameter dbCallOutParameter = new DBCallOutParameter();
                dbCallOutParameter.setFieldValues(cursor);
                dbCallOutParameterList.add(dbCallOutParameter);
            }
        }
    }

    /**
     * Populates message filter configuration
     * @param cursor Stax parser cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateMessageFilterConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ELEM_MESSAGE_FILTERS)) {
            while (cursor.nextElement()){
                String messagefilterName = getStringAttribute(cursor, ATTR_MESSAGE_FILTER_NAME, "");
                String messagefilterValue = getStringAttribute(cursor,ATTR_MESSAGE_FILTER_VALUE,"");
                if(!messagefilterName.equalsIgnoreCase("")&&!messagefilterValue.equalsIgnoreCase(""))
                    messageFilters.put(messagefilterName, messagefilterValue);
            }
            if(messageFilters.isEmpty())
                isMessageFilterSet = false;
            else
                isMessageFilterSet = true;
            cursor.resetCursor();
        }
    }


    /**
     * Populates destination configuration
     * @param cursor Stax parser cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateDestinationConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ELEM_DESTINATION)) {
            destinationType = getIntegerAttribute(cursor, ATTR_DESTINATION_TYPE, getDefaultDestinationType());
            specifiedDestinationUsed = getBooleanAttribute(cursor, ATTR_SPECIFIED_DESTINATION_USED, false);
            destination = cursor.getAttributeValue(null, ATTR_DESTINATION);
            isDestinationEncrypted = getBooleanAttribute(cursor, IS_DESTINATION_ENCRYPTED, false);
            encryptionAlgorithm = cursor.getAttributeValue(null, DESTINATION_ENCRYPT_ALGO);
            encryptionKey = cursor.getAttributeValue(null, DESTINATION_ENCRYPT_KEY);
            if (encryptionKey != null && encryptionKey.equals(""))
                encryptionKey = null;
            allowPaddingToKey = getBooleanAttribute(cursor, DESTINATION_ALLOW_PADDING_TO_KEY, true);
            initializationVector = cursor.getAttributeValue(null, DESTINATION_INITIALIZATION_VECTOR);
            cursor.resetCursor();
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert_1(PortInst that){
        proxyUsed = that.useProxy();
        proxyURL = that.getProxyURL();
        proxyUser = that.getProxyPrincipal();
        proxyPassword = that.getProxyCredentials();

        clientID = that.getClientID();
        enabled = !that.isDisabled();

        destinationType = PortInstConstants.JMSDESTINATION_QUEUE.equalsIgnoreCase(that.getJMSDestinationType()) ? DESTINATION_TYPE_QUEUE : DESTINATION_TYPE_TOPIC;
        specifiedDestinationUsed = that.isBoundToJMS();
        destination = that.getJMSDestination();

        securityManager = that.getSecurityManagerClass();
        user = that.getUserPrincipal();
        password = that.getUserCredentials();

        // note: workflow convertion is handled in ServiceInstnace dmi convertion

        convert_2(that);
        if(that instanceof OutPortInst)
            convert_3((OutPortInst)that);
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert_2(PortInst that){}

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert_3(OutPortInst that){}

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    private int getDefaultDestinationType(){
        return (getClass()==InputPortInstance.class) ? DESTINATION_TYPE_QUEUE : DESTINATION_TYPE_TOPIC;
    }

    public void reset(){
        super.reset();

        securityManager = null;

        proxyUsed = false;
        proxyURL = null;
        proxyUser = ANONYMOUS;
        proxyPassword = ANONYMOUS;

        user = ANONYMOUS;
        password = ANONYMOUS;

        destinationType = getDefaultDestinationType();
        specifiedDestinationUsed = false;
        destination = null;

        clientID = null;
        enabled = true;
        workflow = WORKFLOW_NONE;
        workflowDataType = WORKFLOW_DATA_MESSAGE_BODY;
        workflowConfigName = null;
        isMessageFilterSet = false;
        messageFilters.clear();
        messageFilterConfigName = null;
        destinationConfigName = null;
    }

    /**
     * Validates the Application DMI. Check whther all the manditory fileds are set.
     *
     * The Possible Error Mesages:
     * @bundle DESTINATION_NAME_UNSPECIFIED=The Destination to which the port listens is not specified.
     * @bundle PROXY_URL_UNSPECIFIED=Proxy URL not specified for the proxy being used
     */
    public void validate() throws FioranoException{
        super.validate();

        if(specifiedDestinationUsed && StringUtils.isEmpty(destination))
            throw new FioranoException("DESTINATION_NAME_UNSPECIFIED");
        if(proxyUsed && StringUtils.isEmpty(proxyURL))
            throw new FioranoException("PROXY_URL_UNSPECIFIED");
    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.writeUTF(appContextAction);
        bytesMessage.writeUTF(clientID);
        bytesMessage.writeInt(destinationType);
        bytesMessage.writeUTF(destination);
        bytesMessage.writeUTF(destinationConfigName);
        bytesMessage.writeUTF(encryptionAlgorithm);
        bytesMessage.writeUTF(encryptionKey);
        bytesMessage.writeUTF(initializationVector);
        bytesMessage.writeBoolean(isMessageFilterSet);
        bytesMessage.writeUTF(messageFilterConfigName);
        if(messageFilters == null){
            bytesMessage.writeInt(-1);
        }else{
            bytesMessage.writeInt(messageFilters.size());
            for(Object configKey : messageFilters.keySet()){
                bytesMessage.writeUTF((String) configKey);
                bytesMessage.writeUTF((String) messageFilters.get(configKey));
            }
        }
        bytesMessage.writeUTF(password);
        bytesMessage.writeUTF(user);
        bytesMessage.writeUTF(proxyPassword);
        bytesMessage.writeUTF(proxyURL);
        bytesMessage.writeUTF(proxyUser);
        bytesMessage.writeUTF(securityManager);
    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);
        appContextAction=bytesMessage.readUTF();
        clientID=bytesMessage.readUTF();
        destinationType=bytesMessage.readInt();
        destination=bytesMessage.readUTF();
        destinationConfigName= bytesMessage.readUTF();
        encryptionAlgorithm= bytesMessage.readUTF();
        encryptionKey= bytesMessage.readUTF();
        initializationVector= bytesMessage.readUTF();
        isMessageFilterSet= bytesMessage.readBoolean();
        messageFilterConfigName= bytesMessage.readUTF();

        int numConfigs = bytesMessage.readInt();
        if(numConfigs >0 ){
            messageFilters = new HashMap<>();
            for(int i = 0; i < numConfigs; i++){
                String key = bytesMessage.readUTF();
                String config = bytesMessage.readUTF();
                messageFilters.put(key, config);
            }
        }
        password= bytesMessage.readUTF();
        user= bytesMessage.readUTF();
        proxyPassword= bytesMessage.readUTF();
        proxyURL= bytesMessage.readUTF();
        proxyUser= bytesMessage.readUTF();
        securityManager= bytesMessage.readUTF();
    }
}
