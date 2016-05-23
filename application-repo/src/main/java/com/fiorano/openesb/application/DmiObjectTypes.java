/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

public interface DmiObjectTypes
{
    public int      NATIVE_OBJECT = -1;

    // Data structures for Tifosi 2.0
    // start from 200
    public int      CERTIFICATE = 200;
    public int      DEPLOYMENT = 201;
    public int      EXECUTION = 202;
    public int      INPORT = 203;
    public int      MONITORABLEMODULE = 204;
    public int      OUTPORT = 205;
    public int      PORT_DESCRIPTOR = 206;
    public int      RESOURCE = 207;
    public int      RUNTIME_ARG = 208;
    public int      SECURITY = 209;
    public int      SERVICE_DEPENDENCY = 211;
    public int      SERVICE_HEADER = 212;
    public int      SERVICE_PROPERTIES = 213;
    public int      APP_STATE_INFO = 214;
    public int      APP_STATE_DETAILS = 215;
    public int      SERVICE_STATE_DETAILS = 216;
    public int      USER_EVENT = 217;

    public int      APPLICATION_HEADER = 220;
    public int      APPLICATION_PROPERTIES = 221;
    public int      ARGUMENT = 222;
    public int      EXT_SERVICE_INSTANCE = 223;
    public int      ON_EXCEPTION = 224;
    public int      APP_ROUTE = 225;
    public int      ROUTES = 226;
    public int      APP_RUNTIME_ARGS = 227;
    public int      SERVICE_INSTANCE = 228;
    public int      SERVICE_INSTANCES = 229;
    public int      STATE = 230;
    public int      STATUS_TRACKING = 231;
    public int      WF_INPORT = 232;
    public int      WF_OUTPORT = 233;
    public int      WF_START_PORTS = 234;
    public int      WF_EXIT_PORTS = 235;
    public int      END_STATE = 236;
    public int      MONITOR = 237;
    public int      APS_EVENT_MODULE = 238;

    public int      PARAM = 239;
    public int      APS_LOG_MODULE = 240;

    //STE
    public int      WORKFLOW_INSTANCE_INFO = 242;
    public int      STATE_INFO = 243;
    public int      STATE_INSTANCE_INFO = 244;
    public int      DOCUMENT_INFO = 245;
    public int      EVENT_INFO = 246;

    public int      SERVICE_SEARCH_CONTEXT = 247;
    public int      STRING = 248;
    public int      RESOURCE_DATA = 249;

    //COMMON
    public int      ROUTE_INFO = 250;
    public int      DATA_PACKET = 251;
    public int      TARGET_PORT = 252;

    public int      CARRY_FORWARD_CONTEXT = 253;
    public int      SOURCE_CONTEXT = 254;

    public int      TPS_PROPERTIES = 255;
    public int      ALTERNATE_DESTINATION = 256;

    public int      EVENT_SEARCH_CONTEXT = 257;
    public int      MONITORING_INFO = 259;

    public int      LOGIN_RESPONSE = 260;
    public int      ACKNOWLEDGEMENT_PACKET = 261;
    public int      MESSAGE_PACKET = 262;
    public int      REQUESTER_INFO = 263;
    public int      TPS_PERFORMANCE_STATS = 264;
    public int      TRACE_CONFIGURATION = 265;
    public int      SERVICES_REMOVAL_STATUS = 266;
    public int      SERVICE_REMOVAL_INFO = 267;
    public int      SERVICE_INSTANCE_INFO = 268;
    public int      SERVICE_INFO = 269;
    public int      SYSTEM_INFO = 270;

    public int      SERVICE_STATUS = 271;
    public int      SERVICES_STATUS_PACKET = 272;
    public int      PUBSUB_DATA_PACKET = 273;
    public int      TRANSACTED_PACKET = 274;
    public int      LOGIN_INFO = 275;
    public int      RUNTIME_ARGS_REQ = 276;
    public int      BIND_REQ = 277;
    public int      READ_DATA_REQ = 278;
    public int      SEND_DATA_REQ = 279;
    public int      COMMIT_REQ = 280;

    //events
    public int      TIFOSI_EVENT = 301;
    public int      APPLICATION_EVENT = 302;
    public int      SERVICE_EVENT = 303;
    public int      TPS_EVENT = 305;
    public int      SECURITY_EVENT = 306;
    public int      SBW_EVENT = 307;
    public int      TES_EVENT = 308;
    public int      DEBUG_EVENT = 309;
    public int      TES_STATE_EVENT = 324;
    public int      ROUTE_EVENT = 310;

    public int      APPLICATION_LAUNCHPACKET = 312;
    public int      SERVICE_LAUNCHPACKET = 313;
    public int      ROUTE_LAUNCHPACKET = 314;

    public int      LOG_PACKET = 315;

    // for NAT
    public int      TES_PROPERTY_SHEET = 316;
    public int      MQ_PROPERTY_SHEET = 317;
    public int      TES_PERFORMANCE_STATS = 318;

    // New Logging additons to ServiceDescriptor
    public int      LOG_MODULE = 319;
    public int      EVENT_MODULE = 320;
    public int      SERVICE_LOG_MODULES = 321;

    //for updating app composer on commit/remove of services
    public int      SERVICE_REPOSITORY_UPDATION_EVENT = 323;

    public int      PING_PACKET = 324;

    public int      APLLICATION_INFO_PACKET = 325;

    public int      PORT_DESCRIPTOR_INSTANCE = 326;
    public int      IN_PORT_INSTANCE = 327;
    public int      OUT_PORT_INSTANCE = 328;

    //  Application Context
    public int      APPLICATION_CONTEXT = 329;
    public int      XPATH_DMI = 330;

    public int HA_STATUS_EVENT = 331;
    public int HA_SYNC_EVENT = 332;

    public int APP_DEPLOYMENT_PROFILE = 333;

    // new service dmi
    public int NEW_CPS = 334;
    public int NEW_DEPLOYMENT = 335;
    public int NEW_EXECUTION = 336;
    public int NEW_LOGMODULE =337;
    public int NEW_PORT = 338;
    public int NEW_RESOURCE = 339;
    public int NEW_RUNTIME_ARGUMENT = 340;
    public int NEW_SCHEMA = 341;
    public int NEW_SERVICE = 342;
    public int NEW_SERVICE_METADATA = 343;
    public int NEW_SERVICE_REF = 344;
    public int NEW_SERVICE_REFERENCE =345;

    public int NEW_APPLICATION = 346;
    public int NEW_APPLICATION_CONTEXT = 347;
    public int NEW_APPLICATION_REFERENCE = 348;
    public int NEW_INPUT_PORT_INSTANCE = 349;
    public int NEW_LOG_MANAGER = 350;
    public int NEW_MESSAGE_TRANSFORMATION = 351;
    public int NEW_OUTPUT_PORT_INSTANCE = 352;
    public int NEW_PORT_INSTANCE = 353;
    public int NEW_REMOTE_SERVICE_INSTANCE = 354;
    public int NEW_ROUTE = 355;
    public int NEW_SCHEMA_INSTANCE = 356;
    public int NEW_SERVICE_INSTANCE = 357;
    public int NEW_TRANSFORMATION = 358;
    public int NEW_XPATH_SELECTOR = 359;

    public int SBW_SEARCH_CONTEXT = 360;
    public int MANAGEABLE_PROPERTY= 361;
    
    public int PRINCIPAL_REALM_STORE = 362;
    public int PRINCIPAL_STORE_SYNCHRONIZATION_STATUS = 363;
    public int AUDIT_POLICY_SYNCHRONIZATION_STATUS = 364;
    public int SECURITY_DATASTORE_RESET_STATUS = 365;

    public int NAMED_CONFIGURATION_PROPERTY= 381;
    public int SCHEMA_REFERENCE_PROPERTY = 382;
    public int      DB_CALL_OUT_PARAM = 329;

    public int NEW_BACKLOG_MON_RULE_ENVELOPE = 400;
    public int NEW_BACKLOG_MON_RULE = 401;
    public int BACKLOG_MON_RULE_STATUS = 402;
    public int BACKLOG_MONITOR_EVENT = 403;
    public int LOWMEMORY_EVENT =404;
    public int MEMORY_MON_RULE=405;
    public int MEMORY_MON_RULE_FOR_COMPONENT=407;
    public int MEMORY_MON_RULE_STATUS=406;
    public int COMPONENT_MEMORY_MON_RULE_STATUS=409;
    public int COMPONENT_LOWMEMORY_EVENT=408;
    public int SBW_MON_RULE=410;
    public int SBW_MON_RULE_STATUS=411;
    public int DISK_MON_RULE_STATUS=412;
    public int LOWDISK_EVENT =413;
    public int DISK_MON_RULE=414;
    public int ALERT_EVENT = 500;
    public int AUDIT_EVENT = 600;

    public int NAMED_CONFIGURATION_OBJECT = 701;
    public int PORT_CONFIGURATION_NAMED_OBJECT = 702;
    public int SERVICE_CONFIGURATION_NAMED_OBJECT = 703;
    public int RESOURCE_CONFIGURATION_NAMED_OBJECT = 704;
    public int DATA_CONFIGURATION_OBJECT = 710;
    public int PORT_CONFIGURATION_DATA_OBJECT = 711;
    public int SERVICE_CONFIGURATION_DATA_OBJECT = 712;
    public int RESOURCE_CONFIGURATION_DATA_OBJECT = 713;
    public int TRANSFORMATION_CONFIGURATION_DATA_OBJECT = 714;
    public int TRANSFORMATION_CONFIGURATION_NAMED_OBJECT = 715;
    public int RUNTIMEARG_CONFIGURATION_NAMED_OBJECT = 716;
    public int RUNTIMEARG_CONFIGURATION_DATA_OBJECT = 717;

    public int NEW_EMAIL_NOTIFICATION_MODEL = 501;
    public int NEW_JMS_NOTIFICATION_MODEL = 502;

    public int APP_LAUNCH_KILL_ALERT = 801;
    public int SERVICE_BOUND_UNBOUND_ALERT = 802;
    public int FES_START_STOP_ALERT = 803;
    public int FPS_START_STOP_ALERT = 804;
    public int SECURITY_ALERT = 805;
    public int GATEWAY_ALERT = 999;

    public int SERVER_LICENSE_DETAILS = 101;

    //API
    public int API_PROJECT_EVENT = 1001;
    int API_ANALYTICS_EVENT = 1002;
}
