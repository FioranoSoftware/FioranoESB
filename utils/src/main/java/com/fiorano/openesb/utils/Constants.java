/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public class Constants {

    public static final int INITIAL_CAPACITY = 5;
    public final static String SYSTEM_ID_PREFIX = "ESBX__SYSTEM";
    public final static String NAME_DELIMITER = "__";
    // The size of the chunk of data that is to be transferred from the RTL
    // to the server at the time of the upload of the resource
    public static final int CHUNK_SIZE = 1024 * 40;

    public static final int MAX_ZIP_SIZE = 1 * 1024 * 1024; // 1 MB is the max size for now
    public static final String SD_XML_FILE_NAME = "ServiceDescriptor.xml";
    public static final String RESOURCES_FOLDER_NAME = "Resources";

    public static final String EVENT_PROCESS_MANAGER = "Event_Process_Manager";

    public static final String MICRO_SERVICE_MANAGER = "micro_service_manager";
    public static final String APPLICATION_MANAGER = "application_manager";

    public static final String SERVICE_MANAGER = "Service_Manager";

    public static final String FPS_MANAGER = "FPS_Manager";

    public static final String CONFIGURATION_MANAGER = "CONFIGURATION_Manager";

    public static final String SERVICE_PROVIDER_MANAGER = "Service_Provider_Manager";
    public static final String BREAKPOINT_MANAGER = "Breakpoint_Manager";
    public static final String USER_SECURITY_MANAGER = "User_Security_Manager";
    public static final String KEYSTORE_MANAGER = "KeyStore_Manager";
    public static final String SCHEMA_REFERENCE_MANAGER = "schema_reference_manager";
    public static final String SCHEMA_ZIP_LOCATION = "SCHEMA_ZIP_LOCATION";
    public static final String API_PROJECTS_MANAGER = "API_Projects_Manager";

    public static final String CONNECTION_FACTORY_CAT="connectionfactory";
    public static final String CONNECTION_FACTORY_REPO="connectionfactory";
    public static final String DESTINATION_CAT="destination";
    public static final String DESTINATION_REPO="destination";
    public static final String MSG_FILTERS_CAT="messagefilters";
    public static final String MSG_FILTERS_REPO="messagefilters";
    public static final String PORT_CAT="port";
    public static final String PORT_REPO="ports";
    public static final String RESOURCE_CAT="resource";
    public static final String RESOURCE_REPO="resources";
    public static final String ROUTE_CAT="route";
    public static final String ROUTE_REPO="routes";
    public static final String RUN_ARGS_CAT="runtimearguments";
    public static final String RUN_ARGS_REPO="runtimeargs";
    public static final String SELECTOR_CAT="selector";
    public static final String SELECTOR_REPO="selectors";
    public static final String SERVICE_CAT="service";
    public static final String SERVICE_REPO="components";
    public static final String TRANS_CAT="transformation";
    public static final String TRANS_REPO="transformations";
    public static final String WRKFLW_CAT="workflow";
    public static final String WRKFLW_REPO="workflows";
}
