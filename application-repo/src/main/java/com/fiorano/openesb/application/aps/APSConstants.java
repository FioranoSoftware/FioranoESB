/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

public class APSConstants
{
    //Application
    public static final String	APPLICATION = "Application";

    //Schemas Element
    public static final String	SCHEMAS = "Schemas";
    public static final String	SCHEMA = "Schema";
    public static final String	SCHEMA_ID = "id";

    //ApplicationHeader
    public static final String	APPLICATION_HEADER = "ApplicationHeader";
    public static final String	ATTR_IS_SUBGRAPHABLE = "isSubgraphable";
    public static final String	ATTR_SCOPE = "Scope";

    //ServiceInstances Element
    public static final String	SERVICE_INSTANCE = "ServiceInstance";
    public static final String	SERVICE_INSTANCES = "ServiceInstances";
    public static final String	SERVICE_GUID = "ServiceGUID";	
    public static final String	BUFFER_SIZE_PER_PORT = "BufferSizePerPort";	
    public static final String	VERSION = "Version";	
	
	

    //Routes Element
    public static final String	APP_ROUTE = "Route";
    public static final String	ROUTES = "Routes";

    //Layout Element
    public static final String	LAYOUT = "Layout";


    // WorkflowPorts Elements
    public static final String	WORKFLOW_START_PORTS = "WorkflowStartPorts";
    public static final String	WORKFLOW_IN_PORT = "WfInPort";
    public static final String	WORKFLOW_IN_PORT_NAME = "Name";
    public static final String	SERVICE_INSTANCE_NAME = "ServiceInstanceName";

    public static final String	WORKFLOW_EXIT_PORTS = "WorkflowExitPorts";
    public static final String	WORKFLOW_OUT_PORT = "WfOutPort";
    public static final String	WORKFLOW_OUT_PORT_NAME = "Name";


    //Common If any


    // Need to update when we update the DMI
/*	
	public static final String  NATIVE_OBJECT = "-1";
    public static final String	CERTIFICATE = "200";
    public static final String	DEPLOYMENT = "201";
    public static final String	EXECUTION = "202";
    public static final String	INPORT = "203";
    public static final String	MONITORABLEMODULE = "204";
    public static final String	OUTPORT = "205";
    public static final String	PORT_DESCRIPTOR = "206";
    public static final String	RESOURCE = "207";
    public static final String	RUNTIME_ARG = "208";
    public static final String	SECURITY = "209";
    public static final String	SERVICE_DEPENDENCY = "211";
    public static final String	SERVICE_HEADER = "212";
    public static final String	SERVICE_PROPERTIES = "213";
    public static final String	APP_STATE_INFO = "214";
    public static final String	APP_STATE_DETAILS = "215";
    public static final String	SERVICE_STATE_DETAILS = "216";
    public static final String	USER_EVENT = "217";

    public static final String	APPLICATION_PROPERTIES = "221";
    public static final String	ARGUMENT = "222";
    public static final String	EXT_SERVICE_INSTANCE = "223";
    public static final String	ON_EXCEPTION = "224";
    public static final String	APP_RUNTIME_ARGS = "227";
    public static final String	STATE = "230";
    public static final String	STATUS_TRACKING = "231";
    public static final String	WF_INPORT = "232";
    public static final String	WF_OUTPORT = "233";
    public static final String	WF_START_PORTS = "234";
    public static final String	WF_EXIT_PORTS = "235";
    public static final String	END_STATE = "236";
    public static final String	MONITOR = "237";
    public static final String	APS_EVENT_MODULE = "238";

    public static final String	PARAM = "239";
    public static final String	APS_LOG_MODULE = "240";


    //STE
    public static final String	WORKFLOW_INSTANCE_INFO = "242";
    public static final String	STATE_INFO = "243";
    public static final String	STATE_INSTANCE_INFO = "244";
    public static final String	DOCUMENT_INFO = "245";
    public static final String	EVENT_INFO = "246";

    public static final String	SERVICE_SEARCH_CONTEXT = "247";
    public static final String	STRING = "248";
    public static final String	RESOURCE_DATA = "249";

    //COMMON
    public static final String	ROUTE_INFO = "250";
    public static final String	DATA_PACKET = "251";
    public static final String	TARGET_PORT = "252";

    public static final String	CARRY_FORWARD_CONTEXT = "253";
    public static final String	SOURCE_CONTEXT = "254";

    public static final String	TPS_PROPERTIES = "255";
    public static final String	ALTERNATE_DESTINATION = "256";

    public static final String	EVENT_SEARCH_CONTEXT = "257";
    public static final String	MONITORING_INFO = "259";

    public static final String	LOGIN_RESPONSE = "260";
    public static final String	ACKNOWLEDGEMENT_PACKET = "261";
    public static final String	MESSAGE_PACKET = "262";
    public static final String	REQUESTER_INFO = "263";
    public static final String	TPS_PERFORMANCE_STATS = "264";
    public static final String	TRACE_CONFIGURATION = "265";
    public static final String	SERVICES_REMOVAL_STATUS = "266";
    public static final String	SERVICE_REMOVAL_INFO = "267";
    public static final String	SERVICE_INSTANCE_INFO = "268";
    public static final String	SERVICE_INFO = "269";
    public static final String	SYSTEM_INFO = "270";

    public static final String	SERVICE_STATUS = "271";
    public static final String	SERVICES_STATUS_PACKET = "272";
    public static final String	PUBSUB_DATA_PACKET = "273";
    public static final String	TRANSACTED_PACKET = "274";
    public static final String	LOGIN_INFO = "275";
    public static final String	RUNTIME_ARGS_REQ = "276";
    public static final String	BIND_REQ = "277";
    public static final String	READ_DATA_REQ = "278";
    public static final String	SEND_DATA_REQ = "279";
    public static final String	COMMIT_REQ = "280";

    //events
    public static final String	TIFOSI_EVENT = "301";
    public static final String	APPLICATION_EVENT = "302";
    public static final String	SERVICE_EVENT = "303";
    public static final String	TPS_EVENT = "305";
    public static final String	SECURITY_EVENT = "306";
    public static final String	SBW_EVENT = "307";
    public static final String	TES_EVENT = "308";
    public static final String	DEBUG_EVENT = "309";
    public static final String	TES_STATE_EVENT = "324";
    public static final String	ROUTE_EVENT = "310";    

    public static final String	APPLICATION_LAUNCHPACKET = "312";
    public static final String	SERVICE_LAUNCHPACKET = "313";
    public static final String	ROUTE_LAUNCHPACKET = "314";

    public static final String	LOG_PACKET = "315";

    // for NAT
    public static final String	TES_PROPERTY_SHEET = "316";
    public static final String	MQ_PROPERTY_SHEET = "317";
    public static final String	TES_PERFORMANCE_STATS = "318";

    // New Logging additons to ServiceDescriptor
    public static final String	LOG_MODULE = "319";
    public static final String	EVENT_MODULE = "320";
    public static final String	SERVICE_LOG_MODULES = "321";

    //for updating app composer on commit/remove of services
    public static final String	SERVICE_REPOSITORY_UPDATION_EVENT = "323";

    public static final String	PING_PACKET = "324";

    public static final String	APLLICATION_INFO_PACKET = "325";

    public static final String	PORT_DESCRIPTOR_INSTANCE = "326";
    public static final String	IN_PORT_INSTANCE = "327";
    public static final String	OUT_PORT_INSTANCE = "328";

    //  Application Context
    public static final String	APPLICATION_CONTEXT = "329";
    public static final String	XPATH_DMI = "330";

    public static final String	HA_STATUS_EVENT = "331";
    public static final String	HA_SYNC_EVENT = "332";
*/
   //Service Instance Constants
    public static final String IS_MANUAL_LAUNCH = "isManualLaunch";
    public static final String IS_STATEFUL = "isStateful";
    public static final String IS_DELAYED_LAUNCH = "isDelayedLaunch";
    public static final String DELAYED_PORT = "delayedPort";
    public static final String MAX_RETRIES = "maxRetries";
    public static final String IS_TRANSACTED = "isTransacted";
    public static final String IS_ERROR_HANDLING_ENABLED = "isErrorHandlingEnabled";
    public static final String IS_INMEMORY_LAUNCH = "isInMemoryLaunch";
    public static final String IS_TRANSPORT_LPC = "isTransportLPC";
    public static final String IS_END_OF_WORKFLOW = "isEndOfWorkflow";
    public static final String PREFER_LAUNCH_ON_HIGHER_LEVEL_NODE = "preferLaunchOnHigherLevelNode";
    public static final String KILL_PRIMARY_ON_SECONDARY_LAUNCH = "killPrimaryOnSecondaryLaunch";
    public static final String IS_DEBUG_MODE = "isDebugMode";
    public static final String DEBUG_PORT = "debugPort";
    public static final String IS_LOCKED = "isLocked";
    public static final String EVENT_HANDLER = "EventHandler";	
    public static final String NODE = "Node";
    public static final String LONG_DESCRIPTION = "LongDescription";
    public static final String SHORT_DESCRIPTION = "ShortDescription";
    public static final String LOG_MANAGER = "LogManager";
    public static final String STATUS_TRACKING = "StatusTracking";
    public static final String EVENT_MODULES = "EventModules";
	
	

    public static final String NODE_LEVEL = "level";
    public static final String DELIVERY_MODE = "deliveryMode";
    public static final String EXPIRY_TIME = "expiryTime";
    public static final String RUNTIME_ARGS = "RuntimeArguments";
    public static final String DEPLOYMENT_PROFILE = "DeploymentProfile";


    //Argument DMI Constants
    public static final String ARGUMENT_NAME = "name";
    public static final String IS_ADVANCED = "isAdvanced";
    public static final String IS_REQUIRED = "isRequired";
    public static final String IS_IN_MEMORY_SUPPORTED = "isInMemorySupported";
    public static final String CONTENT = "content";
    public static final String IS_SYNC_REQUEST_TYPE = "isSyncRequestType";
    public static final String IS_DISABLED = "isDisabled";
    public static final String ARGUMENT="Argument";
    public static final String RUNTIME_DEPENDENCY = "RuntimeDependency";
    public static final String PORT_INST_DESCRIPTOR = "PortInstDescriptor";
    public static final String INPORT_INST = "InPortInst";
    public static final String OUTPORT_INST = "OutPortInst";
    public static final String STATUS = "Status";
    public static final Object IS_STATUS_TRACKING_ON = "isStatusTrackingOn";
    public static final Object STATE_NAME = "name";
    public static final String END_STATE = "EndState";
    public static final String END_STATE_NAME = "name";
    public static final String MONITOR = "Monitor";
    public static final String MODULE = "Module";
    public static final String TRACE_LEVEL = "traceLevel";
    public static final String MODULE_NAME = "name";
    public static final String GENERATE_ALERT = "generateAlert";
    public static final String LOG_MODULES = "LogModules";
    public static final String PARAM_NAME = "name";
    public static final String PARAM = "Param";

    //Route DMI Constants

    public static final String IS_P2PROUTE= "isP2PRoute";
    public static final String IS_PERSISTANT = "isPersistant";
    public static final String IS_DURABLE = "isDurable";
    public static final String APPLY_TRANSFORMATION_AT_SRC = "applyTransformationAtSrc";
    public static final String ALTERNATE_DESTINATION = "AlternateDestination";
    public static final String EXTERNAL_SERVICE_INSTANCE = "ExternalServiceInstance";
    public static final Object DURABLE_SUBSCRIPTION = "durableSubscription";

    //OnException Element
    public static final String ON_EXCEPTION = "OnException";
    public static final String IS_ERRORHANDLING_SUPPORTED = "isErrorHandlingSupported";
    public static final String TGT_SRV_INSTANCE = "TgtServiceInstance";
    public static final String TGT_PORT = "TgtPort";

    //Application Context
    public static final String APPLICATION_CONTEXT = "ApplicationContext";
    public static final String APPLICATION_CONTEXT_STRUCTURE = "Structure";
    public static final String APPLICATION_CONTEXT_DEF_INST = "DefaultInstance";
    public static final String APPLICATION_CONTEXT_ROOT_ELEM = "RootElement";
    public static final String APPLICATION_CONTEXT_ROOT_ELEM_NS = "RootElementNamespace";
    public static final String APPLICATION_CONTEXT_STRUCTURE_TYPE = "StructureType";
    public static final String SERVICE_PROFILE = "profile";
}
