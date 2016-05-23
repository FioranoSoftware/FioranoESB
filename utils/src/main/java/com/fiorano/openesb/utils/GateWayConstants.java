/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public class GateWayConstants {

    //hash table properties of WSStub/HttpStub instance
    public static final String STUB_INSTANCE_NAME = "stubInstanceName";

    public static final String STATUS = "status";

    public static final String APP_GUID = "eventProcessGUID";

    public static final String APP_VERSION = "eventProcessVersion";

    public static final String CONTEXT_NAME = "contextName";

    public static final String CONTEXT_DESCRIPTION = "contextDescription";

    public static final String NODE_NAME = "nodeName";

    public static final String OPERATION_CONFIG = "operationConfiguration";

    public static final String OPERATION_NAME = "name";

    public static final String OPERATION_DESCRIPTION = "description";

    public static final String HTTP_CONTEXT_URL = "httpContextURL";

    public static final String WSDL_URL = "wsdlURL";

    public static final String HttpStub_CONTEXT = "/bchttpgateway/contexts/";

    public static final String WSStub_CONTEXT = "/bcwsgateway/services/";

    public static final String ENDPOINT_URL_TEST = "endPointURLTest";

    public static final String ENDPOINT_URI_DISPLAY = "endPointURIDisplay";

    public static final String WSSTUB = "WSStub";

    public static final String HTTPSTUB = "HttpStub";

    public static final String WSSTUB_PM = "com.fiorano.edbc.WsStub.model.WsStubPM";

    public static final String HTTPSTUBPM = "com.fiorano.bc.httpstub.model.HttpStubPM";
    
    public static final String HTTP_GATEWAY_SEGMENT = "/bchttpgateway/contexts/AdminContext";
    public static final String HS_BASIC_AUTH_ENABLED = "basicAuthCheckBox";
    public static final String HS_BASIC_AUTH_USERNAME = "basicAuthUserNameTextField";
    public static final String HS_BASIC_AUTH_PASSWORD = "basicAuthPasswordField";

 /*---------------------------------------------------------------------------------------------*/

    /*-------------------------Proxy-SSL Constants--------------------------------------------*/

    public static final String SUPPORT_SSL = "supportSSL";

    public static final String AXIS_SOCKET_SECURE_FACTORY = "axis.socketSecureFactory";

    public final static String HTTPS_USE = "https.IsUse";
    public final static String HTTPS_TRUST_STORE = "javax.net.ssl.trustStore";
    public final static String HTTPS_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    public final static String HTTPS_TRUST_STORE_TYPE = "javax.net.ssl.trustStoreType";

    public final static String HTTPS_KEY_STORE = "javax.net.ssl.keyStore";
    public final static String HTTPS_KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    public final static String HTTPS_KEY_STORE_TYPE = "javax.net.ssl.keyStoreType";
    public final static String HTTPS_KEY_CLIENT_PASSWORD = "javax.net.ssl.keyPassword";

    public final static String HTTPS_SECURITY_PROVIDER_CLASS = "security.provider";
    public final static String HTTPS_PROTOCOL_HANDLER_PACKAGES = "java.protocol.handler.pkgs";

    public static final String KEY_CLIENT_PASSWORD = "keyClientPassword";

    public static final String KEY_STORE_LOCATION = "keyStoreLocation";

    public static final String KEY_STORE_PASSWORD = "keyStorePassword";

    public static final String TRUST_STORE_LOCATION = "trustStoreLocation";

    public static final String TRUST_STORE_PASSWORD = "trustStorePassword";

    public static final String KEY_STORE_TYPE  = "keyStoreType";

    public static final String TRUST_STORE_TYPE = "trustStoreType";

    public static final String BASIC_AUTH_USER = "username";

    public static final String BASIC_AUTH_PASSWD = "password";

    public static final String FES_AUTH_USER = "FESUsername";

    public static final String FES_AUTH_PASSWD = "FESPassword";

    public static final String CONTEXT_NAME_MANAGEABLE_PROP = "Context Name";

    public static final String FES_AUTH_USER_MANAGEABLE_PROP = "FES Username";

    public static final String FES_AUTH_PASSWD_MANAGEABLE_PROP = "FES Password";

/*-----------------------------------------------------------------------------------------------------*/

    /*------------------------------- New Model -------------------------------------------------*/

    public static final String WSSTUB_PM_NEW = "com.fiorano.services.wsstub.configuration.WSStubPM";

    public static final String ERROR_HANDLING_CONFIG = "errorHandlingConfiguration";

    public static final String TRANSPORT_SECURTIY_CONFIG = "transportSecurityConfiguration";

    public static final String WSDEFINITION_CONFIG = "wsDefinitionConfiguration";

    public static final String WSSTANDARD_DETAILS = "wsStandardDetails";

    public static final String BASEURI = "baseURI";

    public static final String WSDL = "wsdl";

    public static final String HTTP_AUTH_CONFIG = "httpAuthConfiguration";

    public static final String SSL_CONFIG = "sslConfiguration";

    public static final String DOMAIN = "domain";

    public static final String HOSTNAME = "hostname";

    public static final String REALM = "realm";

    public static final String PORT = "port";

    public static final String USE_HTTP_AUTH="useHTTPAuth";

    public static final String USE_SSL = "useSSL";

    public static final String ACCEPT_SERVER_CERTIFICATE="acceptServerCertificate";

    public static final String IGNORE_HOSTNAME_MISMATCH="ignoreHostnameMismatch";

    public static final String STORE_IMPORTS_LOCALLY="storeImportsLocally";

    public static final String SCHEMA_LOCATIONS = "SchemaLocations";

    /*---------------------------------------------------------------------------------------------*/


    /*------------------------------- REST Constants -------------------------------------------------*/

    public static final String RESTSTUB_CONFIG_CLASSNAME = "com.fiorano.services.reststub.configuration.RESTStubConfiguration";

    public static final String WADL_CONFIGURATION = "wadlConfiguration";

    public static final String RESTFUL_SERVICE_NAME = "serviceName";

    public static final String RESTFUL_SERVICE_DESC = "serviceDescription";

    public static final String WADL = "wadl";

    public static final String RESTSTUB_GUID = "RESTStub";

    public static final String RESTStub_CONTEXT = "/restgateway/services/";

    public static final String WADL_URL = "wadlURL";
    
    public static final String REST_METHOD_CALL_DETAILS = "RESTMethodCallDetails";

    public static final String RESTGATEWAY_SEGMENT = "/restgateway/services/AdminService?action=undeploy";

    /*---------------------------------------------------------------------------------------------*/

    public static final String CONFIG_NAME = "configName";

    public static final String NAMED_CONFIGURATION = "namedConfiguration";

    /* Password Encryption Changes ......................*/

    public static final String CUSTOM_CLASS = "customClass";

    public static final String PASSWORD_FROM_VAULT = "passwordFromVault";
    
    public static final String CUSTOM_PASSWORD_ENCRYPTION = "customPasswordEncryption";

    public static final String IS_BASIC_AUTH_PASSWD_ENCRYPTED = "basicAuthPasswdEncrypted";

    public static final String IS_SSL_PASSWD_ENCRYPTED = "sslPasswdEncrypted";

}

