/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch;

import java.io.File;

public class LaunchConstants {

    public final static String URL = "-url";
    public final static String BACKUP_URL = "-backupurl";

    public final static String ICF = "-icf";
    public final static String FES_URL = "-fesurl";
    public final static String PRODUCT_NAME = "-productname";
    public final static String USERNAME = "-username";
    public final static String PASSWORD = "-password";
    public final static String CONN_FACTORY = "-connfactory";
    public final static String CLIENT_ID = "-clientid";
    public final static String EVENT_PROC_NAME = "-eventprocessname";
    public final static String EVENT_PROC_VERSION = "-eventprocessversion";
    public final static String COMP_INSTANCE_NAME = "-instancename";
    public final static String EVENTS_TOPIC = "-eventstopic";
    public final static String TRANSPORT_PROTOCOL = "-transportprotocol";
    public final static String SECURITY_PROTOCOL = "-securityprotocol";
    public final static String SECURITY_MANAGER = "-securitymanager";
    public final static String CONFIG_FILE_PATH = "-configfile";
    public final static String MAX_NUMBER_OF_THREADS = "-maxThreads";
    public final static String MIN_NUMBER_OF_THREADS = "-minThreads";
    public final static String MAX_NUMBER_OF_JOBS = "-maxJobs";
    public final static String IS_TRANSACTED_SESSION = "-isTransacted";
    public final static String IS_DURABLE_SUBSCRIBER = "isDurable";
    public final static String IS_MULTITHREADED = "-isMultiThreaded";
    public final static String COMPONENT_REPO_PATH = "-componentrepopath";
    public final static String COMPONENT_GUID = "-serviceGUID";
    public final static String COMPONENT_VERSION = "-serviceversion";
    public static final String LOG_HANDLERS = "-loggers";
    public static final String LOG_MANAGER = "-logManager";
    public final static String NODE_NAME = "-nodename";
    public final static String CCP_ENABLED = "-ccpEnabled";
    public final static String IS_IN_MEMORY = "-isinmemory";

    public final static String JETTY_URL = "-jettyurl";
    public final static String JETTY_URL_SSL = "-jettyurlssl";

    public final static String USERNAME_DEF = "anonymous";
    public final static String PASSWORD_DEF = "anonymous";
    public final static String URL_DEF = "http://localhost:1856";
    public final static String BACKUP_URL_DEF = "http://localhost:1856";
    public final static String CONN_FACTORY_DEF = "primaryTCF";
    public final static String B2B_KEYSTORE_PATH = "-keystorerepopath";

    //if the nulls are causing problem can be changed to empty strings
    public final static String EVENT_PROC_NAME_DEF = null;
    public final static String EVENT_PROC_VERSION_DEF = null;
    public final static String COMP_INSTANCE_NAME_DEF = null;
    public final static String EVENTS_TOPIC_DEF = null;
    public final static String CONFIG_FILE_PATH_DEF = null;
    public final static String SECURITY_PROTOCOL_DEF = null;
    public final static String SECURITY_MANAGER_DEF = null;
    public final static String COMPONENT_REPO_PATH_DEF = null;
    public final static String TRANSPORT_PROTOCOL_DEF = "TCP";
    public final static String ESB_REPOSITORY_PROP_NAME = "ESB_REPOSITORY_DIRECTORY";
    public final static String REL_ESB_REPOSITORY_PATH = System.getProperty(ESB_REPOSITORY_PROP_NAME, "esb" + File.separator + "server" + File.separator + "repository");
    public final static int MAX_NUMBER_OF_THREADS_DEF = 1;
    public final static int MIN_NUMBER_OF_THREADS_DEF = 1;
    public final static int MAX_NUMBER_OF_JOBS_DEF = 1;
    public final static boolean IS_TRANSACTED_SESSION_DEF = true;
    public final static boolean IS_MULTITHREADED_DEF = false;
    public final static boolean IS_IN_MEMORY_DEF = false;

    public final static String INPUT_PORTS_SUFFIX = "INPUT_PORTS";
    public final static String OUTPUT_PORTS_SUFFIX = "OUTPUT_PORTS";
    public final static String DEFAULT_CONFIGURATION_OBJECT_SUFFIX = "RUNTIMEARGS";
    public final static String MANAGEABLE_PROPERTIES_SUFFIX = "MANAGEABLEPROPERTIES";
    public final static String PROFILE_OBJECT_SUFFIX = "PROFILE";
    public final static String JNDI_CONSTANT = "__";
    public final static String CPSHELPER_MBEAN_NAME = "CPS:name=cpsHelper";

    public final static String JCA_INTERACTION_SPEC = "javax.resource.cci.InteractionSpec";
    public final static String JCA_ACTIVATION_SPEC = "javax.resource.cci.ActivationSpec";
    public final static String PROTOCOL_LPC = "LPC";
    public final static String REPOSITORY = "repository";
    public final static String CERTS_DIR = "-DCERTS_DIR";
    public final static String TRUST_STORE = "-Djavax.net.ssl.trustStore";
    public static final String USER_DEFINED_JAVA_HOME = "user.java.home";
}
