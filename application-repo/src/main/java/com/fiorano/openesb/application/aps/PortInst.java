/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.DmiErrorCodes;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.XMLDmiUtil;
import com.fiorano.openesb.utils.XMLUtils;
import org.apache.xerces.dom.DOMInputImpl;
import org.w3c.dom.*;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public abstract class PortInst extends DmiObject implements LSResourceResolver{

    //BudId 12697
    public final static int TYPE_DTD = 0;
    public final static int TYPE_XSD = 1;
    public final static int TYPE_NONE = -1;

    // name of the port
    String m_strPortName;
    // Description of out port
    String m_strDscription;
    //input xsd
    String m_strXSD;
    //any params
    Vector m_params = new Vector();
    //java class
    String m_strJavaClass;
    boolean m_bIsSyncRequestType;
    boolean m_bisDisabled = false;

    String m_strContextXSL = null;
    String m_strContextInfo = null;

    // Specify whether this is a system port or not.
    // For example: - Error Port or Transaction Failure Port are system ports
    //

    String m_strXSDRef;

    private boolean m_bIsSystemPort;

    /**
     * Constructor for the OutPortInst object
     *
     * @since Tifosi2.0
     */
    public PortInst(){
        reset();
    }

    /**
     * Gets the client id set for the port.
     *
     * @return client id set for the port
     */
    public String getClientID(){
        return getParamValue(PortInstConstants.CLIENT_ID);
    }

    /**
     * Checks if the passed value is the default value for the specified param.
     * @param paramName
     * @param value
     * @return boolean to check if the passed value is the default value for the specified param.
     */
    public boolean checkIfDefaultValue(String paramName, String value)
    {
        if(paramName.equalsIgnoreCase(PortInstConstants.STRUCTURE_TYPE))
            return PortInstConstants.DEF_STRUCTURE_TYPE == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.ENABLE_REQUEST_REPLY))
            return PortInstConstants.DEF_IS_REQUEST_REPLY == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.USE_PROXY))
            return PortInstConstants.DEF_IS_USE_PROXY == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.PROXY_CREDENTIALS))
            return PortInstConstants.DEF_PROXY_CREDENTIALS.equalsIgnoreCase(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.PROXY_PRINCIPAL))
            return PortInstConstants.DEF_PROXY_PRINCIPAL.equalsIgnoreCase(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.USER_CREDENTIALS))
            return PortInstConstants.DEF_USER_CREDENTIALS.equalsIgnoreCase(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.USER_PRINCIPAL))
            return PortInstConstants.DEF_USER_PRINCIPAL.equalsIgnoreCase(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.USE_SPECIFIED_DESTINATION))
            return PortInstConstants.DEF_IS_BOUND_TO_JMS == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.TRANSACTED))
            return PortInstConstants.DEF_IS_TRANSACTED == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.Transaction_Size))
            return PortInstConstants.DEF_TRANSACTION_SIZE == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.ACKNOWLEDGE_MODE))
            return PortInstConstants.DEF_ACK_MODE == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.DURABLE_SUBSCRIPTION))
            return PortInstConstants.DEF_IS_DURABLE_SUBSCRIPTION == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.SUBSCRIPTION_NAME))
            return PortInstConstants.DEF_SUBSCRIPTION_NAME.equalsIgnoreCase(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.COMPRESSION))
            return PortInstConstants.DEF_IS_COMPRESSION_ENABLED == new Boolean(value).booleanValue();
        if(paramName.equalsIgnoreCase(PortInstConstants.SESSION_COUNT))
            return PortInstConstants.DEF_SESSION_COUNT == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.MESSAGE_PRIORITY))
            return PortInstConstants.DEF_MESSAGE_PRIORITY == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.MESSAGE_TTL))
            return PortInstConstants.DEF_MESSAGE_TTL == Integer.parseInt(value);
        if(paramName.equalsIgnoreCase(PortInstConstants.PERSISTANT))
            return PortInstConstants.DEF_IS_PERSISTANT == new Boolean(value).booleanValue();
        return false;
    }
    /**
     * Gets the no of session count.
     *
     * @return no of session count
     */
    public int getSessionCount(){
        return Param.getParamValueAsInt(m_params, PortInstConstants.SESSION_COUNT, PortInstConstants.DEF_SESSION_COUNT);
    }

     /**
     * Gets the whether the RequestReply is enabled on the port.
     *
     * @return boolean specifying whether the RequestReply is enabled on the port
     * @deprecated
     */
    public boolean isEnableRequestReply(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.ENABLE_REQUEST_REPLY, PortInstConstants.DEF_IS_REQUEST_REPLY);
	}

     /**
     * Gets the acknowledgementMode.
     * There are three types of acknowledgement modes.
     * 1 - Auto-acknowledge
     * 2 - Client-acknowledge
     * 3 - Dups-ok-acknowledge
     *
     * @return acknowledgementMode
     */
    public int getAcknowledgementMode(){
        return Param.getParamValueAsInt(m_params, PortInstConstants.ACKNOWLEDGE_MODE, PortInstConstants.DEF_ACK_MODE);
    }

     /**
     * Specifies whether the Port has s durable subscription.
     *
     * @return boolean specifying whether the Port has s durable subscription
     */
    public abstract boolean isDurableSubscription();

    /**
     * Gives the Subscription Name if Port has a durable subscription.
     *
     * @return Subscription Name if Port has a durable subscription
     */
    public String getSubscriptionName(){
        return Param.getParamValue(m_params, PortInstConstants.SUBSCRIPTION_NAME, PortInstConstants.DEF_SUBSCRIPTION_NAME);
    }


     /**
     * Gets the TransactionSize..
     *
     * @return TransactionSize
     */
    public int getTransactionSize(){
        return Param.getParamValueAsInt(m_params, PortInstConstants.Transaction_Size, PortInstConstants.DEF_TRANSACTION_SIZE);
    }

    /**
     * Gets the Message Selector.
     *
     * @return Message Selector
     */
    public String getMessageSelector(){
        return getParamValue(PortInstConstants.MESSAGE_SELECTOR);
    }


    /**
     * Gets whether the port is transacted or not
     *
     * @return boolean checking whether the port is transacted or not
     */
    public boolean isTransacted(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.TRANSACTED, PortInstConstants.DEF_IS_TRANSACTED);
    }


    /**
     * Gets the rootElemName attribute of the OutPort object
     *
     * @return rootElemName attribute of the OutPort object
     */
    public String getRootElemName(){
        return getParamValue(PortInstConstants.ROOT_ELEMENT_NAME);
    }

    /**
     * Gets the structureType attribute of the OutPort object
     *
     * @return The structureType value
     */
    public int getStructureType(){
        return Param.getParamValueAsInt(m_params, PortInstConstants.STRUCTURE_TYPE, PortInstConstants.DEF_STRUCTURE_TYPE);
    }


    /**
     * Gets the rootElemNS attribute of the OutPort object
     *
     * @return The rootElemNS value
     */
    public String getRootElemNS(){
        return getParamValue(PortInstConstants.ROOT_ELEMENT_NS);
    }

    /**
     * Returns bound to JMS for object
     *
     * @return boolean
     */
    public boolean isBoundToJMS(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.USE_SPECIFIED_DESTINATION, PortInstConstants.DEF_IS_BOUND_TO_JMS);
    }


    /**
     * gets the Transport Protocol to be used.
     *
     * @return String
     * @deprecated
     */
    public String getTransportProtocol(){
        return getParamValue(PortInstConstants.TRANSPORT_PROTOCOL);
    }

    /**
     * gets the Security Protocol to be used
     *
     * @return String
     * @deprecated
     */
    public String getSecurityProtocol(){
        return getParamValue(PortInstConstants.SECURITY_PROTOCOL);
    }

    /**
     * Gets the Security Manager Class
     *
     * @return Security Manager Class
     */
    public String getSecurityManagerClass(){
        return getParamValue(PortInstConstants.SECURITY_MANAGER);
    }


    /**
     * Gets the ProxyURL
     * @return the ProxyURL
     */
    public String getProxyURL(){
        return getParamValue(PortInstConstants.PROXY_URL);
    }


    /**
     * @return String
     * @deprecated
     */
    public String getProxyAuthenticationRealm(){
        return getParamValue(PortInstConstants.PROXY_AUTHENTICATION_REALM);
    }

    /**
     * Gets the Proxy Principal
     * @return Proxy Principal
     */
    public String getProxyPrincipal(){
        return getParamValue(PortInstConstants.PROXY_PRINCIPAL, PortInstConstants.DEF_PROXY_PRINCIPAL);
    }


    /**
     * Gets the Proxy Credentials
     * @return Proxy Credentials
     */
    public String getProxyCredentials(){
        return getParamValue(PortInstConstants.PROXY_CREDENTIALS, PortInstConstants.DEF_PROXY_CREDENTIALS);
    }

    /**
     * Gets the User Principal
     * @return User Principal
     */
    public String getUserPrincipal(){
        return getParamValue(PortInstConstants.USER_PRINCIPAL, PortInstConstants.DEF_USER_PRINCIPAL);
    }

    /**
     * Gets tje User Credentials
     * @return User Credentials
     */
    public String getUserCredentials(){
        return getParamValue(PortInstConstants.USER_CREDENTIALS, PortInstConstants.DEF_USER_CREDENTIALS);
    }

    /**
     * Returns JMS destination type for object
     *
     * @return JMS destination type
     */
    public String getJMSDestinationType(){
        return getParamValue(PortInstConstants.DESTINATION_TYPE);
    }

    /**
     * Returns JMS destination for object
     *
     * @return JMS destination
     */
    public String getJMSDestination(){
        return getParamValue(PortInstConstants.DESTINATION);
    }

    /**
     * Returns boolean specifying whether compression is enabled for object
     *
     * @return boolean specifying whether compression is enabled
     */
    public boolean isCompressionEnabled(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.COMPRESSION, PortInstConstants.DEF_IS_COMPRESSION_ENABLED);
    }


    /**
     * Gets the externalXSD attribute of the OutPort object
     *
     * @param namespace Description of the Parameter
     * @return The externalXSD value
     */
    public String getExternalXSD(String namespace){
        return getParamValue("External_"+namespace);
    }

    /**
     * Returns external XSDs for object
     *
     * @return Map of external XSDs
     */
    public Map getExternalXSDs(){
        Map map = new HashMap();
        Enumeration enumerator = getExtNamespaces();

        while(enumerator.hasMoreElements()){
            String namespace = (String)enumerator.nextElement();

            map.put(namespace, getExternalXSD(namespace));
        }
        return map;
    }

    /**
     * Gets the extNamespaces attribute of the OutPort object
     *
     * @return The extNamespaces value
     */
    public Enumeration getExtNamespaces(){
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
     * This interface method is called to check whether synchronous request
     * reply is supported or not, by the OutPortInst specified by this object
     * of <code>OutPortInst</code>.
     *
     * @return true if synchronous request reply is supported by this
     *         OutPortInst.
     * @see #setIsSyncRequestType(boolean)
     * @since Tifosi2.0
     */
    public boolean isSyncRequestType(){
        return m_bIsSyncRequestType;
    }

    /**
     * Check if the Port is Disabled
     * @return true if the port is being disabled
     */
    public boolean isDisabled(){
        return m_bisDisabled;
    }

    /**
     * This interface method is called to get name of the OutPortInst specified
     * by this object of <code>OutPortInst</code>.
     *
     * @return the name of OutPortInst
     * @see #setPortName(String)
     * @since Tifosi2.0
     */
    public String getPortName(){
        return m_strPortName;
    }


    /**
     * This interface method is called to get the description of the
     * OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @return The description of OutPortInst
     * @see #setDescription(String)
     * @since Tifosi2.0
     */
    public String getDescription(){
        return m_strDscription;
    }


    /**
     * This interface method is called to get the XSD set for OutPortInst,
     * specified by this object of <code>OutPortInst</code>.
     *
     * @return The XSD specified for this port
     * @see #setXSD(String)
     * @since Tifosi2.0
     */
    public String getXSD(){
        return m_strXSD;
    }

    /**
     * This interface method is called to get the XSD Ref set for OutPortInst,
     * specified by this object of <code>OutPortInst</code>.
     *
     * @return The XSD Ref specified for this port
     * @see #setXSDRef(String)
     * @since Tifosi2.0
     */
    public String getXSDRef()
    {
        return m_strXSDRef;
    }

    /**
     * This interface method is called to set the specified string as XSD Ref for
     * OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @param ref The string to be set as XSD Ref for this port
     * @see #getXSDRef()
     * @since Tifosi2.0
     */
    public void setXSDRef(String ref)
    {
        m_strXSDRef = ref;
    }


    /**
     * This interface method is called to get the enumeration of all the
     * objects of <code>Param</code> for this object of <code>OutPortInst</code>
     * .
     *
     * @return Enumeration of all Param objects
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #clearParam()
     * @since Tifosi2.0
     */
    public Enumeration getParams(){
        if(m_params==null){
            m_params = new Vector();
        }
        return m_params.elements();
    }

    /**
     * Returns parameters for object
     *
     * @return Vector of parameters
     */
    public Vector getParameters(){
        return m_params;
    }

    /**
     * This interface method is called to get the javaClass set for this <code>OutPortInst</code>
     * .
     *
     * @return javaClass for this OutPortInst.
     * @see #setJavaClass(String)
     * @since Tifosi2.0
     * @deprecated
     */
    public String getJavaClass(){
        return m_strJavaClass;
    }


    /**
     * This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID(){
        return DmiObjectTypes.OUT_PORT_INSTANCE;
    }


    /**
     * Get the system port
     *
     * @return boolean specifying if it is a system port
     */
    public boolean isSystemPort(){
        return m_bIsSystemPort;
    }


    /**
     * sets the client id set for the port.
     *
     * @param id
     */
    public void setClientID(String id){
        addParam(new Param(PortInstConstants.CLIENT_ID, id));
    }


    /**
     * Sets whether the Request Reply is enabled
     * @param enableRequestReply
     */
    public void setEnableRequestReply(boolean enableRequestReply){
        addParam(new Param(PortInstConstants.ENABLE_REQUEST_REPLY, String.valueOf(enableRequestReply)));
    }


    /**
     * Sets the session Count
     * @param count
     */
    public void setSessionCount(int count){
        addParam(new Param(PortInstConstants.SESSION_COUNT, String.valueOf(count)));
    }

    /**
     * Sets the Acknowledgement mode.
     * There are three types of acknowledgement modes are in javax.jms.Session class.
     * @param acknowledgeMode
     */
    public void setAcknowledgementMode(int acknowledgeMode){
        addParam(new Param(PortInstConstants.ACKNOWLEDGE_MODE, String.valueOf(acknowledgeMode)));
    }

     /**
     * Sets a durable Subsription on the Port
     * @param durableSubscription
     */
    public void setDurableSubscription(boolean durableSubscription){
        addParam(new Param(PortInstConstants.DURABLE_SUBSCRIPTION, String.valueOf(durableSubscription)));
    }

     /**
     * Sets durable Subscription Name
     * @param durableSunscriptionName
     */
    public void setSubscriptionName(String durableSunscriptionName){
        addParam(new Param(PortInstConstants.SUBSCRIPTION_NAME, String.valueOf(durableSunscriptionName)));
    }

    /**
     * Sets the Transaction size. The default size is 1
     * @param transactionSize
     */
    public void setTransactionSize(int transactionSize){
        addParam(new Param(PortInstConstants.Transaction_Size, String.valueOf(transactionSize)));
    }

    /**
     * Sets isPortTransacted for a port
     * @param isPortTransacted
     */
    public void setTransacted(boolean isPortTransacted){
        addParam(new Param(PortInstConstants.TRANSACTED, String.valueOf(isPortTransacted)));
    }


    /**
     * sets the MessageSelector for a port.
     *
     * @param selector
     */
    public void setMessageSelector(String selector){
        addParam(new Param(PortInstConstants.MESSAGE_SELECTOR, selector));
    }

    /**
     * sets whether the port is bound to user specified JMS destination
     *
     * @param boundToJMS
     */
    public void setBoundToJMS(boolean boundToJMS){
        Param.setParamValue(m_params, PortInstConstants.USE_SPECIFIED_DESTINATION, boundToJMS);
    }

    /**
     * sets the Transport Protocol to be used.
     *
     * @param protocol
     */
    public void setTransportProtocol(String protocol){
        addParam(new Param(PortInstConstants.TRANSPORT_PROTOCOL, protocol));
    }

    /**
     * sets the Security Protocol to be used
     *
     * @param protocol   Valid Values(TCP_PHAOS,TCP_JSSE,HTTPS_PHAOS,HTTPS_JSSE,HTTPS)
     */
    public void setSecurityProtocol(String protocol){
        addParam(new Param(PortInstConstants.SECURITY_PROTOCOL, protocol));
    }

    /**
     * sets the Security Manager Class
     *
     * @param securityClass
     */

    public void setSecurityManagerClass(String securityClass){
        addParam(new Param(PortInstConstants.SECURITY_MANAGER, securityClass));
    }

    /**
     * sets the proxy URL to use
     *
     * @param url
     */
    public void setProxyURL(String url){
        addParam(new Param(PortInstConstants.PROXY_URL, url));
    }

    /**
     * sets the Proxy authentication Realm to be used.
     *
     * @param realm
     * @deprecated
     */
    public void setProxyAuthenticationRealm(String realm){
        addParam(new Param(PortInstConstants.PROXY_URL, realm));
    }

    /**
     * sets the Proxy principal to be used.
     *
     * @param principal
     */
    public void setProxyPrincipal(String principal){
        addParam(new Param(PortInstConstants.PROXY_PRINCIPAL, principal));
    }

    /**
     * sets the Proxy credentials to be used.
     *
     * @param credentials
     */
    public void setProxyCredentials(String credentials){
        addParam(new Param(PortInstConstants.PROXY_CREDENTIALS, credentials));
    }

    /**
     * sets the Proxy credentials to be used.
     *
     * @param principal
     */
    public void setUserPrincipal(String principal){
        addParam(new Param(PortInstConstants.USER_PRINCIPAL, principal));
    }

    /**
     * sets the Proxy credentials to be used.
     *
     * @param credentials
     */
    public void setUserCredentials(String credentials){
        addParam(new Param(PortInstConstants.USER_CREDENTIALS, credentials));
    }

    /**
     * Sets JMS destination type for object
     *
     * @param type   ValidValues("Topic", "Queue")
     */
    public void setJMSDestinationType(String type){
        // validate type
        if(PortInstConstants.JMSDESTINATION_TOPIC.equalsIgnoreCase(type)
                || PortInstConstants.JMSDESTINATION_QUEUE.equalsIgnoreCase(type))
            addParam(new Param(PortInstConstants.DESTINATION_TYPE, type));
        else
            throw new IllegalArgumentException(" Destination Type valid values are "+
                    PortInstConstants.JMSDESTINATION_QUEUE+" and "+PortInstConstants.JMSDESTINATION_TOPIC+
                    ". "+type+" is not recognized.");
    }

    /**
     * Sets JMS destination for object
     *
     * @param destination
     */
    public void setJMSDestination(String destination){
        addParam(new Param(PortInstConstants.DESTINATION, destination));
    }

    /**
     * Sets compression enabled for object
     *
     * @param enable
     */
    public void setCompressionEnabled(boolean enable){
        addParam(new Param(PortInstConstants.COMPRESSION, String.valueOf(enable)));
    }

    /**
     * Sets parameters for object
     *
     * @param params
     */
    public void setParameters(Vector params){
        m_params = params;
    }

    /**
     * Sets the rootElemName attribute of the OutPort object
     *
     * @param value The new rootElemName value
     */
    public void setRootElemName(String value){
        setParamValue(PortInstConstants.ROOT_ELEMENT_NAME, value);
    }

    /**
     * Sets the structureType attribute of the OutPort object
     *
     * @param type The new structureType value
     */
    public void setStructureType(int type){
        setParamValue(PortInstConstants.STRUCTURE_TYPE, ""+type);
    }

    /**
     * Sets the rootElemNS attribute of the OutPort object
     *
     * @param value The new rootElemNS value
     */
    public void setRootElemNS(String value){
        setParamValue(PortInstConstants.ROOT_ELEMENT_NS, value);
    }

    /**
     * This interface method is called to set the boolean specifying whether
     * synchronous request reply is supported or not by the OutPortInst,
     * specified by this object of <code>OutPortInst</code>.
     *
     * @param isSyncRequestType The boolean specifying whether or not
     *                          synchronous request_Reply is supported by this OutPortInst.
     * @see #isSyncRequestType()
     * @since Tifosi2.0
     */
    public void setIsSyncRequestType(boolean isSyncRequestType){
        m_bIsSyncRequestType = isSyncRequestType;
    }

    /**
     * Set isDisabled parameter of this port
     * @param isDisabled true indicates that this port is disabled
     */
    public void setIsDisabled(boolean isDisabled){
        m_bisDisabled = isDisabled;
    }

    /**
     * This interface method is called to set the specified string as the name
     * of OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @param prtName The string to be set as the name of this OutPortInst
     * @see #getPortName()
     * @since Tifosi2.0
     */
    public void setPortName(String prtName){
        m_strPortName = prtName!=null ? prtName.toUpperCase() : prtName;
    }


    /**
     * This interface method is called to set the specified string as
     * description of the OutPortInst, specified by this object of <code>OutPortInst</code>
     * .
     *
     * @param description The string to be set as description of OutPortInst
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setDescription(String description){
        m_strDscription = description;
    }

    /**
     * This interface method is called to set the specified string as
     * description of the OutPortInst, specified by this object of <code>OutPortInst</code>
     * .
     *
     * @param contextXSL The new contextXSL value
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setContextXSL(String contextXSL){
        m_strContextXSL = contextXSL;
    }

    /**
     * This interface method is called to set the specified string as
     * description of the OutPortInst, specified by this object of <code>OutPortInst</code>
     * .
     *
     * @param strContextInfo The new contextInfo value
     * @see #getDescription()
     * @since Tifosi2.0
     */
    public void setContextInfo(String strContextInfo){
        m_strContextInfo = strContextInfo;
    }

    /**
     * This interface method is called to set the specified string as XSD for
     * OutPortInst, specified by this object of <code>OutPortInst</code>.
     *
     * @param xsd The string to be set as xSD for this port
     * @see #getXSD()
     * @since Tifosi2.0
     */
    public void setXSD(String xsd){
        m_strXSD = xsd;
    }


    /**
     * This interface method is called to set the specified string as javaClass
     * for this <code>OutPortInst</code>.
     *
     * @param javaClass the string to be set as JavaClass
     * @see #getJavaClass()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setJavaClass(String javaClass){
        m_strJavaClass = javaClass;
    }

    /**
     * Set the system port
     *
     * @param isSystemPort
     */
    public void setSystemPort(boolean isSystemPort){
        m_bIsSystemPort = isSystemPort;
    }

    /**
     * This interface method is called to set all the fieldValues of this
     * object of <code>OutPortInst</code>, using the specified XML string.
     *
     * @param port The new fieldValues value
     * @throws FioranoException if an error occurs while parsing the
     *                         XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element port)
            throws FioranoException{
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element port = doc.getDocumentElement();

        if(port!=null){
            m_bIsSyncRequestType = XMLDmiUtil.getAttributeAsBoolean(port, "isSyncRequestType");
            m_bisDisabled = XMLDmiUtil.getAttributeAsBoolean(port, "isDisabled");

            NodeList children = port.getChildNodes();
            Node child;

            for(int i = 0; children!=null && i<children.getLength(); ++i){
                child = children.item(i);

                String nodeName = child.getNodeName();

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_NAME)){
                    m_strPortName = XMLUtils.getNodeValueAsString(child).toUpperCase();
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_DESCRIPTION)){
                    m_strDscription = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSD)){
                    m_strXSD = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_XSDREF)){
                    m_strXSDRef = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_CONTEXT_XSL)){
                    m_strContextXSL = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_CONTEXT_INFO)){
                    m_strContextInfo = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_JAVACLASS)){
                    m_strJavaClass = XMLUtils.getNodeValueAsString(child);
                }

                if(nodeName.equalsIgnoreCase(PortInstConstants.PORT_PARAM)){
                    Param paramDmi = new Param();

                    paramDmi.setFieldValues((Element)child);
                    addParam(paramDmi);
                }
            }
        }
        validate();
    }

    /**
     * Sets use proxy for object
     *
     * @param proxy
     */
    public void setUseProxy(boolean proxy){
        Param.setParamValue(m_params, PortInstConstants.USE_PROXY, proxy);
    }

    /**
     * Checks if Proxy is used
     * @return boolean
     */
    public boolean useProxy(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.USE_PROXY, PortInstConstants.DEF_IS_USE_PROXY);
    }

    /**
     * Adds a feature to the ExternalXSD attribute of the OutPort object
     *
     * @param namespace The feature to be added to the ExternalXSD attribute
     * @param xsd       The feature to be added to the ExternalXSD attribute
     */
    public void addExternalXSD(String namespace, String xsd){
        setParamValue("External_"+namespace, xsd);
    }
    /**
     * Removes all ExternalXSDs of the OutPort object
     *
     */
    public void removeAllExternalXSDs(){

        Enumeration enumerator = getExtNamespaces();

        while(enumerator.hasMoreElements()){

            String namespace = (String)enumerator.nextElement();
            if(m_params!=null){
                removeParam(Param.getParamWithName(m_params,"External_"+namespace));
            }
        }
    }

    /**
     * This interface method is called to add specified object of <code>Param</code>
     * , representing extra parameter for OutPortInst, to this object of <code>OutPortInst</code>
     * .
     *
     * @param param Object of Param to be added to OutPortInst
     * @see #removeParam(Param)
     * @see #clearParam()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void addParam(Param param){
        if(m_params==null){
            m_params = new Vector();
        }
        m_params.addElement(param);
    }


    /**
     * This interface method is called to remove specified object of <code>Param</code>
     * , representing extra parameter for OutPortInst, from this object of
     * <code>OutPortInst</code>.
     *
     * @param param Object of Param to be removed from OutPortInst
     * @see #addParam(Param)
     * @see #clearParam()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void removeParam(Param param){
        if(m_params!=null){
            m_params.remove(param);
        }
    }


    /**
     * This interface method is called to clear all the objects of <code>Param</code>
     * , from this object of <ode>OutPortInst</code>.
     *
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void clearParam(){
        if(m_params!=null){
            m_params.clear();
        }
    }


    /**
     * This method tests whether this object of <code>OutPortInst</code> has
     * the required(mandatory) fields set, before inserting values in the
     * database.
     *
     * @throws FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
            throws FioranoException{
        if(m_strPortName==null){
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        if(m_params!=null){
            Enumeration _enum = m_params.elements();

            while(_enum.hasMoreElements()){
                Param param = (Param)_enum.nextElement();

                param.validate();
            }
        }
    }


    /**
     * Resets the default values for this object
     *
     * @since Tifosi2.0
     */
    public void reset(){
        m_params = new Vector();
    }


    /**
     * Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @throws FioranoException if an error occurs while creating the element
     *                         node.
     *@deprecated
     * This method is already being implemented by the subclasses InPortInst and OutPortInst
     * and this class is being converted to an abstract class.. Bug.7847..Uday.K
     */
    public Node toJXMLString(Document document)
            throws FioranoException{
        Node root0 = document.createElement("OutPortInst");

        ((Element)root0).setAttribute("isSyncRequestType", ""+isSyncRequestType());

        Node child;

        child = XMLDmiUtil.getNodeObject(PortInstConstants.PORT_NAME, m_strPortName, document);
        if(child!=null){
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject(PortInstConstants.PORT_DESCRIPTION, m_strDscription, document);
        if(child!=null){
            root0.appendChild(child);
        }

        if(m_strXSDRef!=null){
            child = XMLDmiUtil.getNodeObject(PortInstConstants.PORT_XSDREF, m_strXSDRef, document);
            root0.appendChild(child);
        }

        if(m_strContextXSL!=null){
            Element elem = document.createElement(PortInstConstants.PORT_CONTEXT_XSL);
            CDATASection cdata = document.createCDATASection(m_strContextXSL);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        if(m_strContextInfo!=null){
            Element elem = document.createElement(PortInstConstants.PORT_CONTEXT_INFO);
            CDATASection cdata = document.createCDATASection(m_strContextInfo);

            elem.appendChild(cdata);
            root0.appendChild(elem);
        }

        child = XMLDmiUtil.getNodeObject(PortInstConstants.PORT_JAVACLASS, m_strJavaClass, document);
        if(child!=null){
            root0.appendChild(child);
        }

        if(m_params!=null && m_params.size()>0){
            Enumeration _enum = m_params.elements();

            while(_enum.hasMoreElements()){
                Param param = (Param)_enum.nextElement();
                Node paramNode = param.toJXMLString(document);

                root0.appendChild(paramNode);
            }
        }

        return root0;
    }

    /**
     * @param type
     * @param namespaceURI
     * @param publicId
     * @param systemId
     * @param baseURI
     * @return LSInput
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId
            , String systemId, String baseURI){
        String schema = getExternalXSD(namespaceURI);

        if(schema!=null){
            LSInput input = new DOMInputImpl();

            input.setPublicId(publicId);
            input.setSystemId(systemId);
            input.setBaseURI(baseURI);
            input.setStringData(schema);
            return input;
        } else
            return null;
    }

    /**
     * This API is used only in PortInst.equals method.
     * Earlier getParams() was being used which every times
     * returns a new Enumeration object thereby failing the
     * comparison.
     *
     * @return Vector of Params
     */

    Vector getParamsVector(){
        return m_params;
    }

    /**
     * Gets the paramValue attribute of the Port object
     *
     * @param name Description of the Parameter
     * @return The paramValue value
     */
    protected String getParamValue(String name){
        return Param.getParamValue(m_params, name);
    }

    /**
     * Gets the paramValue attribute of the Port object
     *
     * @param name Description of the Parameter
     * @return The paramValue value
     */
    protected String getParamValue(String name, String defValue){
        return Param.getParamValue(m_params, name, defValue);
    }

    /**
     * Sets the paramValue attribute of the OutPort object
     *
     * @param name  The new paramValue value
     * @param value The new paramValue value
     */
    protected void setParamValue(String name, String value){
        Param.setParamValue(m_params, name, value);
    }


    /** This method return the time limit after which the received message is lost.
     *  If this is 0 millisecond means infinite.
     * @return TimeToLive is the time limit after which the received  message is lost.
     *         If this is 0 millisecond, which means infinite.
     * @see #setMessageTTL(long)
    */
    public long getMessageTTL(){
        return Param.getParamValueAsLong(m_params, PortInstConstants.MESSAGE_TTL, PortInstConstants.DEF_MESSAGE_TTL);
    }

    /**
     * This will set the TTL for the message. To make it infinite set it to 0 milliseconds.
     * @param messageTTL
     * @see #getMessageTTL()
     */
    public void setMessageTTL(long messageTTL){
        Param.setParamValue(m_params, PortInstConstants.MESSAGE_TTL, messageTTL);
    }

    /**
     * Gets the Message Priority
     * @return Message Priority
     * @see #setMessagePriority(int)
     */
    public int getMessagePriority(){
        return Param.getParamValueAsInt(m_params, PortInstConstants.MESSAGE_PRIORITY, PortInstConstants.DEF_MESSAGE_PRIORITY);
    }

    /**
     * Sets the Message Priority
     * @see #getMessagePriority()
     * @param priority
     */
    public void setMessagePriority(int priority){
        Param.setParamValue(m_params, PortInstConstants.MESSAGE_PRIORITY, priority);
    }

    /**
     * @return isPersistant
     * @deprecated
     */
    public boolean isPersistant(){
        return Param.getParamValueAsBoolean(m_params, PortInstConstants.PERSISTANT, PortInstConstants.DEF_IS_PERSISTANT);
    }

    /**
     *
     * @param persistant
     * @deprecated 
     */
    public void setPersistant(boolean persistant){
        Param.setParamValue(m_params, PortInstConstants.PERSISTANT, persistant);
    }
}
