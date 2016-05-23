/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

public class PortInstConstants
{
    public static final String SESSION_COUNT = "Session Count";
    public static final String MESSAGE_SELECTOR = "Message Selector";
    public final static String DESTINATION = "Destination";
    public final static String COMPRESSION = "Compression";
    public final static String DESTINATION_TYPE = "Destination Type";

    public final static String TRANSPORT_PROTOCOL = "Transport Protocol";
    public final static String SECURITY_PROTOCOL = "Security Protocol";
    public final static String SECURITY_MANAGER = "Security Manager";

    public final static String USE_PROXY = "Use Proxy";
    public final static String PROXY_URL = "Proxy URL";

    public final static String PROXY_AUTHENTICATION_REALM = "Proxy Authentication Realm";
    public final static String PROXY_PRINCIPAL = "Proxy Principal";
    public final static String PROXY_CREDENTIALS = "Proxy Credentials";

    public final static String USER_PRINCIPAL = "Username";
    public final static String USER_CREDENTIALS = "Password";

    public final static String JMSDESTINATION_QUEUE = "Queue";
    public final static String JMSDESTINATION_TOPIC = "Topic";

    public final static String CLIENT_ID = "Client ID";

    public final static String TRANSACTED = "Transacted";
    public final static String ROOT_ELEMENT_NAME = "rootElementName";
    public final static String STRUCTURE_TYPE = "structureType";
    public final static String ROOT_ELEMENT_NS = "rootElementNS";
    public final static String USE_SPECIFIED_DESTINATION = "Use specified Destination";


    public final static String PORT_NAME = "Name";
    public final static String PORT_DESCRIPTION = "Description";
    public final static String PORT_XSD = "XSD";
    public final static String PORT_XSDREF = "XSDRef";    
    public final static String PORT_CONTEXT_XSL = "SetContextXSL";
    public final static String PORT_CONTEXT_INFO = "SetContextInfo";
    public final static String PORT_JAVACLASS = "JavaClass";
    public final static String PORT_PARAM = "Param";


    public final static String ENABLE_REQUEST_REPLY = "Enable Request Reply";
    public final static String ACKNOWLEDGE_MODE = "Acknowledge Mode";
    public final static String DURABLE_SUBSCRIPTION = "Durable Subscription";
    public final static String SUBSCRIPTION_NAME = "Subscription Name";
    public final static String PERSISTANT = "Persistant";
    public final static String MESSAGE_PRIORITY = "Message Priority";
    public final static String MESSAGE_TTL = "Message TTL";
    public final static String USERNAME = "Username";
    public final static String PASSWORD = "Password";

    public static String Transaction_Size= " Transaction Size";


    // Default values for params
    public static final int DEF_STRUCTURE_TYPE= 1;
    public static final boolean DEF_IS_REQUEST_REPLY= false;
    public static final boolean DEF_IS_USE_PROXY= false;
    public static final String DEF_PROXY_PRINCIPAL = "anonymous";
    public static final String DEF_PROXY_CREDENTIALS = "anonymous";
    public static final String DEF_USER_PRINCIPAL = "anonymous";
    public static final String DEF_USER_CREDENTIALS = "anonymous";
    public static final boolean DEF_IS_BOUND_TO_JMS = false;
    public static final boolean DEF_IS_TRANSACTED = false;
    public static final int DEF_TRANSACTION_SIZE = 0;
    public static final int DEF_ACK_MODE = 3;
    public static final boolean DEF_IS_DURABLE_SUBSCRIPTION = false;
    public static final String DEF_SUBSCRIPTION_NAME = "null";
    public static final boolean DEF_IS_COMPRESSION_ENABLED = false;
    public static final int DEF_SESSION_COUNT = 1;
    public static final int DEF_MESSAGE_PRIORITY = 0;
    public static final long DEF_MESSAGE_TTL = 0L;
    public static final boolean DEF_IS_PERSISTANT = false;
}
