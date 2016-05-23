/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

public interface Bundle {


    /*--------------------------------------Info Statements ----------------------------------------------*/

    /**
     * @msg.message msg="Application controller manager starting up"
     */
    String START_UP = "start_up";

    /**
     * @msg.message msg="Application controller manager Started successfully"
     */
    String APPLICATION_CONTROLLER_STARTED = "application_controller_started";

    /**
     * @msg.message msg="Application controller is switching to active mode"
     */
    String SWITCH_TO_ACTIVE_MODE = "switch_to_active_mode";

    /**
     * @msg.message msg="Application controller is in active state"
     */
    String SWITCHED_TO_ACTIVE_MODE = "switched_to_active_mode";

    /**
     * @msg.message msg="Restoring the state of the applications "
     */
    String RESTORE_APPLICATIONS_STATE = "restore_applications_state";

    /**
     * @msg.message msg="application controller switching to passive mode"
     */
    String SWITCH_TO_PASSIVE_MODE = "switch_to_passive_mode";

    /**
     * @msg.message msg="application controller is in passive state"
     */
    String SWITCHED_TO_PASSIVE_MODE = "switched_to_passive_mode";

    /**
     * @msg.message msg="application {0} is saved to repository successfully."
     */
    String APPLICATION_SAVED = "application_saved";

    /**
     * @msg.message msg="Application {0} is saved to repository successfully by user : {1} running client- {2} with IP : {3}."
     */
    String APPLICATION_SAVED_IP = "application_saved_ip";

    /**
     * @msg.message msg="Exception occurred while saving application {0} to application repository."
     */
    String APPLICATION_SAVE_EXCEPTION = "application_save_exception";

    /**
     * @msg.message msg="application is renamed from {0} to {1} successfully."
     */
    String APPLICATION_RENAMED = "application_renamed";

    /**
     * @msg.message msg="The application: {0} with version {1} is deleted successfully"
     */
    String APPLICATION_DELETED = "application_deleted";

    /**
     * @msg.message msg="The application: {0} with version {1} is deleted successfully by user : {2} running client- {3} with IP: {4}"
     */
    String APPLICATION_DELETED_IP = "application_deleted_ip";

    /**
     * @msg.message msg="Exception occurred while deleting application {0} with version {1}. Exception details : {2}"
     */
    String APPLICATION_DELETE_EXCEPTION = "application_delete_exception";

    /**
     * @msg.message msg="Resource and connectivity check for Application {0} is successful by user : {1} running client - {2} with IP : {3}. Application is ready for launch"
     */
    String APPLICATION_COMPILED = "application_compiled";

    /**
     * @msg.message msg=" Document cannot be re-injected. Application name is not defined for this document."
     */
    String APPLICATION_NAME_NOT_DEFINED = "application_name_not_defined";
    /**
     * @msg.message msg="Application {0} with version {1} is launched successfully."
     */
    String APPLICATION_LAUNCHED = "application_launched";

    /**
     * @msg.message msg="Application {0} with version {1} is launched successfully by user : {2} running client - {3} with IP : {4}."
     */
    String APPLICATION_LAUNCHED_IP = "application_launched_ip";

    /**
     * @msg.message msg="Exception occurred while launching application {0} with version {1}. Exception Details : {2}"
     */
    String APPLICATION_LAUNCH_EXCEPTION = "application_launch_exception";

    /**
     * @msg.message msg="Application {0} with version {1} is restarted"
     */
    String APPLICATION_RESTARTED = "application_restarted";

    /**
     * @msg.message msg="Application {0} with version {1} is restarted by user : {2} running client- {3} with IP : {4}"
     */
    String APPLICATION_RESTARTED_IP = "application_restarted_ip";

    /**
     * @msg.message msg="Exception occurred while restarting event process {0} with version {1}. Exception details : {1}"
     */
    String APPLICATION_RESTART_EXCEPTION = "application_restart_exception";

    /**
     * @msg.message msg="Application {0} is Synchronized"
     */
    String APPLICATION_SYNCHRONIZED = "application_synchronized";

    /**
     * @msg.message msg="Application {0} is Synchronized by user : {1} running client - {2} with IP : {3}"
     */
    String APPLICATION_SYNCHRONIZED_IP = "application_synchronized_ip";

    /**
     * @msg.message msg="Exception occurred while synchronizing event process {0} with version {1}. Exception details : {2}"
     */
    String APPLICATION_SYNCHRONIZATION_EXCEPTION = "application_synchronization_exception";

    /**
     * @msg.message msg="All service instances of Application {0} are started"
     */
    String STARTED_SERVICES = "started_services";

    /**
     * @msg.message msg="All service instances of Application {0} are started by user : {1} running client - {2} with IP : {3}"
     */
    String STARTED_SERVICES_IP = "started_services_ip";

    /**
     * @msg.message msg="All service instances of Application {0} are stopped by user : {1} running client - {2} with IP : {3}"
     */
    String STOP_ALL_SERVICES = "stop_all_services";

    /**
     * @msg.message msg="Application {0} is stopped by user : {1} running client - {2} with IP : {3}"
     */
    String APPLICATION_STOPPED = "application_stopped";

    /**
     * @msg.message msg="service Instance : {0} of application : {1} is stopped by user : {2} running client- {3} with IP : {4}"
     */
    String STOP_SERVICE = "stop_service";

    /**
     * @msg.message msg="Set Debugger call on route {0} of application {1} is successful by user : {2} running client- {3} with IP {4}"
     */
    String SET_DEBUGGER = "set_debugger";


    /**
     * @msg.message msg="Remove Debugger call on route {0} of application {1} is successful by user : {2} running client- {3} with IP : {4}"
     */
    String REMOVE_DEBUGGER = "remove_debugger";

    /**
     * @msg.message msg="Error breakpoint is already removed or No breakpoint set on route:{0}"
     */
    String ERROR_DEBUGGER_ALREADY_REMOVED = "error_debugger_already_removed";
    /**
     * @msg.message msg="State Based Workflow (SBW) is enabled for the port {0} of service instance {1} in application {2}."
     */
    String ENABLE_SBW = "enable_sbw";
    /**
     * @msg.message msg="Message Filter is enabled for the port {0} of service instance {1} in application {2}."
     */
    String ENABLE_MESSAGE_FILTER = "enable_message_filter";
    /**
     * @msg.message msg="State Based Workflow (SBW) is disabled for the port {0} of service instance {1} in application {2}."
     */
    String DISABLE_SBW = "disable_sbw";
    /**
     * @msg.message msg="Message Filter is disabled for the port {0} of service instance {1} in application {2}."
     */
    String DISABLE_MESSAGE_FILTER = "disable_message_filter";
    /**
     * @msg.message msg="Restoring application: {0}"
     */
    String APPLICATION_RESTORED = "application_restored";

    /**
     * @msg.message msg="Application {0} , Status {1}"
     */
    String APPLICATION_STATUS = "application_status";

    /**
     * @msg.message msg="Error restoring the application {0}"
     */
    String ERROR_RESTORING_APPLICATION = "error_restoring_application";

    /**
     * @msg.message msg="Error Restoring the Application states"
     */
    String ERROR_RESTORING_STATES = "error_restoring_states";

    /**
     * @msg.message msg="Requesting Proxy Call : {0} to Peer: {1}"
     */
    String EXECUTING_PROXY_CALL = "executing_proxy_call";
    /**
     * @msg.message msg="Processing the Peer Server Kill Event for Peer Server: {0}"
     */
    String PEER_SERVER_KILL_EVENT = "peer_server_kill_event";

    /**
     * @msg.message msg="Processing Peer Server available event for Peer Sever: {0}"
     */
    String TPS_AVAILABLE_EVENT = "tps_available_event";

    /**
     * @msg.message msg="Application: {0} handling FPS:{0} Launch Event."
     */
    String APPLICATION_TPS_AVAILABLE_EVENT = "application_tps_available_event";

    /**
     * @msg.message msg="FES Details sent to Peer:{0}. Status of the peer is: {1}"
     */
    String FES_DETAILS_SENT_TO_PEER = "fes_details_sent_to_peer";

    /*-------------------------------------Info Statements --------------------------------------------------*/


    /*-------------------------------------Debug Statements --------------------------------------------------*/

    /**
     * @msg.message msg="Executing Call : {0} in Application Controller"
     */
    String EXECUTING_CALL = "executing_call";

    /**
     * @msg.message msg="Executing Call : {0} in Application Controller by user : {1} using client - {2} with IP : {3}"
     */
    String EXECUTING_CALL_IP = "executing_call_ip";

    /**
     * @msg.message msg="ApplicationController processing the Event:  {0} "
     */
    String APPLICATION_CONTROLLER_PROCESSING_THE_EVENT = "application_controller_processing_the_event";


    /**
     * @msg.message msg="Starting Application recovery thread after {0} Ms."
     */
    String APPLICATION_RECOVERY_LAG = "application_recovery_lag";

    /**
     * @msg.message msg="Changing the target of the Debugger set Route: {0}"
     */
    String CHANGE_ROUTE_TARGET = "change_route_target";

    /**
     * @msg.message msg="Events Rasied. {0}"
     */
    String EVENT_RAISED = "event_raised";

    /**
     * @msg.message msg="Service Killed: {0}"
     */
    String SERVICE_KILLED = "service_killed";

    /**
     * @msg.message msg="Registered for receiving Application Events. Selector String {0}."
     */
    String REGISTERED_EVENT_LISTENER = "registered_event_listener";

    /**
     * @msg.message msg="Unregistered from receiving events as Module is in passive transition."
     */
    String UNREGISTERED_EVENT_LISTENER = "unregistered_event_listener";

    /**
     * @msg.message msg="Application Launch Packet for {0} is framed as below."
     */
    String APPLICATION_LAUNCH_PACKET = "application_launch_packet";

    /**
     * @msg.message msg="Service {0} is configured to launch on node {1}"
     */
    String SERVICE_LAUNCH_PACKET = "service_launch_packet";

    /**
     * @msg.message msg="Route Details: Route GUID: {0}. Target Service Instamce {1}. Actual target service instance {2}, Target application GUID {3}, node Name of target Service instance {4}"
     */
    String ROUTE_LAUNCH_PACKET = "route_launch_packet";

    /**
     * @msg.message msg="Service {0} of application {1} is being launched at Peer {2}. "
     */
    String SERVICE_LAUNCH_NODE = "service_launch_node";

    /*-------------------------------------Debug Statements --------------------------------------------------*/


    /*-------------------------------------Error Statements --------------------------------------------------*/

    /**
     * @msg.message msg="Configuration is Invalid"
     */
    String INVALID_CONFIGURATION = "invalid_configuration";

    /**
     * @msg.message msg="Exception occurred while processing Event: {0}"
     */
    String ERROR_EVENT_PROCESSING = "error_event_processing";

    /**
     *  @msg.message msg="Exception occurred while forwarding event to : {2}. Event ID: {0}, Event description: {1}."
     */
    String ERROR_FWD_EVENT = "error_fwd_event";

    /**
     * @msg.message msg="Error occurred while removing fmq notification listener"
     */
    String ERROR_REMOVING_NOTIFICATION_LISTENER = "error_removing_notification_listener";

    /**
     * @msg.message msg="Failed to remove the application {0} from Running Application List."
     */
    String FAILED_TO_REMOVE_APP_FROM_RUNNING_APPLIST = "failed_to_remove_app_from_running_applist";

    /**
     * @msg.message msg="Exception occurred while removing Application Handle: {0}. Problem while looking up and rebinding the RUNNING_APP_LIST."
     */
    public static final String EXCEPTION_WHILE_REMOVING_APPHANDLE = "exception_while_removing_apphandle";

    /**
     * @msg.message msg="Exception occurred while deleting config file for service instance(s)"
     */
    public static final String ERROR_DELETING_CONFIG_FILE_FOR_SERVICE_INSTANCE = "error_deleting_config_file_for_service_instance";

    /**
     * @msg.message msg="Exception occurred while deleting transformation file for output port instance(s)"
     */
    public static final String ERROR_DELETING_CONFIG_FILE_FOR_PORT = "error_deleting_config_file_for_port";

    /**
     * @msg.message msg="Exception occurred while deleting transformation file for route(s)"
     */
    public static final String ERROR_DELETING_CONFIG_FILE_FOR_ROUTE = "error_deleting_config_file_for_route";

    /**
     * @msg.message msg="Exception occurred while clearing the logs for service {0} "
     */
    public static final String ERROR_CLEARING_LOGS_FOR_SERVICE = "error_clearing_logs_for_service";

    /**
     * @msg.message msg="The specified version: {0} of application: {1} is running currently. You cannot delete or rename a  running version of an application."
     */
    public static final String RUNNING_APPLICATION_ERROR = "running_application_error";

    /**
     * @msg.message msg="Some version of the specified application {0} is running currently. You cannot delete all versions of  this application. If you want to delete some version other  than the running version, please specify the version."
     */
    public static final String APPLICATION_RUNNING_VERSION_DELETION_ERROR = "application_running_version_deletion_error";

    /**
     * @msg.message msg = "Not able to delete the application: {0}__{1} as one or more Applications GUIDs are referring components of {0}__{1}"
     */
    public static final String REFERRING_APPLICATION_DELETION_ERROR = "referring_application_deletion_error";

    /**
     * @msg.message msg=" The version  {0} of this application is already running. Only one version  of an application can be run at a time."
     */
    public static final String RUNNING_APPLICATION_LAUNCH_ERROR = "running_application_launch_error";

    /**
     * @msg.message msg="Service Instance name found as NULL."
     */
    public static final String SERVICE_START_ERROR = "service_start_error";

    /**
     * @msg.message msg="Service Instance name is illegal (Name can''t start with an ''_'')."
     */
    public static final String SERVICE_NAME_ERROR= "service_name_error";

    /**
     * @msg.message msg="Service Instance : {0} of the application : {1} is started by user : {2} running client- {3} with IP : {4}"
     */
    public static final String START_SERVICE = "start_service";

    /**
     * @msg.message msg="Naming manager is shutdown while trying to look up RUNNING_APPLICATION_LIST."
     */
    public static final String LOOK_UP_ERROR = "look_up_error";

    /**
     * @msg.message msg="Failed to look up {0}."
     */
    public static final String LOOKUP_FAILED = "lookup_failed";

    /**
     * @msg.message msg="Failed to bind {0} to naming service."
     */
    public static final String FAILED_BIND = "failed_bind";

    /**
     * @msg.message msg="Failed to post rest authorization to peer server : {0}"
     */
    public static final String REST_POST_FAILED = "rest_post_failed";

    /**
     * @msg.message msg="Exception occurred while updating application state in running application list. Problem while looking up and rebinding the RUNNING_APP_LIST."
     */
    public static final String EXCEPTION_UPDATING_STATE_IN_RUNNING_LIST = "exception_updating_state_in_running_list";

    /**
     * @msg.message msg="Application: {0} is NOT running currently."
     */
    public static final String APPHANDLE_NOT_PRESENT = "apphandle_not_present";

    /**
     * @msg.message msg="Error retrieving information from service repository, Reason : Either the appGUID OR Service instance Name specified is NULL."
     */
    public static final String ERROR_RETRIEVING_INFO_FROM_SERVICE_REPOSITORY = "error_retrieving_info_from_service_repository";

    /**
     * @msg.message msg="Error stopping service, Reason : Service Instance Name found as NULL."
     */
    public static final String SERVICE_STOP_ERROR = "service_stop_error";

    /**
     * @msg.message msg="Remote exception in un-registering the debugger."
     */
    public static final String REMOTE_EXCEPTION_IN_UNREGISTERING_THE_DEBUGGER = "remote_exception_in_unregistering_the_debugger";

    /**
     * @msg.message msg="Remote exception in getting RMI registry"
     */
    String ERROR_GETTING_REGISTRY = "error_getting_registry";

    /**
     * @msg.message msg="Remote exception in registering the debugger."
     */
    String REMOTE_EXCEPTION_IN_REGISTERING_THE_DEBUGGER = "remote_exception_in_registering_the_debugger";

    /**
     * @msg.message msg="Debugger already exists"
     */
    String DEBUGGER_ALREADY_EXISTS = "debugger_already_exists";

    /**
     * @msg.message msg="Error occurred while saving the Event Process :{0}."
     */
    public static final String ERROR_SAVE_EVENT_PROCESS = "error_save_event_process";

    /**
     * @msg.message msg="Error occurred while parsing the Event Process {0}:{1}."
     */
    public static final String ERROR_PARSING_APPLICATION = "error_parsing_application";

    /**
     * @msg.message msg="Error getting nodename for service, Reason : Peer Server specified for the service instance {0} of Application {1} is not running."
     */
    public static final String ERROR_GETTING_NODENAME_FOR_SERVICE_PEER_NOT_RUNNING = "error_getting_nodename_for_service_peer_not_running";

    /**
     * @msg.message msg="Failed to calculate if application: {0} contains  Peer Server: {1}."
     */
    String ERROR_PEER_AVAILABILITY = "error_peer_availability";

    /**
     * @msg.message msg="Failed to calculate if external services in application: {0} contains Peer Server: {1}"
     */
    String ERROR_PEER_AVAILABILITY_EXT_SERVICES = "error_peer_availability_ext_services";

    /**
     * @msg.message msg="The specified application: {0} with version {1}, doesn''t exist in the repository."
     */
    String APPLICATION_NOT_PRESENT = "application_not_present";
    /**
     * @msg.message msg="The specified route:{2} in the application: {0}:{1}, doesn''t exist in the repository."
     */
    String ROUTE_NOT_PRESENT = "route_not_present";
    /**
     * @msg.message msg="The remote application: {0} does not contain the specified service instance {1}."
     */
    String REMOTE_INSTANCE_NOT_PRESENT = "remote_instance_not_present";

    /**
     * @msg.message msg="The specified application: {0} doesn''t exist in the repository."
     */
    String APPLICATION_NOT_SAVED = "application_not_saved";

    /**
     * @msg.message msg="Failed to synchronize Key Store with Peer Servers."
     */
    String FAILED_TO_SYNC_KEYSTORE_WITH_PEERS = "failed_to_sync_keystore_with_peers";

    /**
     * @msg.message msg="Connectivity and Resource Check failed for application {0}. error details : {1}"
     */
    String ERROR_COMPILE_APPLICATION = "error_compile_application";

    /**
     * @msg.message msg="No peer node is configured for any of the services of application :: {0}."
     */
    String NO_PEER_NODE_CONFIGURED_FOR_ANY_SERVICE = "no_peer_node_configured_for_any_service";

    /**
     * @msg.message msg="Peer Servers required for launching the application {0} are not running."
     */
    String PEER_SERVERS_NOT_RUNNING = "peer_servers_not_running";

    /**
     * @msg.message msg="CRC failed for application : {0}. No information about Principal Realm Store of Peer Server {1}. Check Peer Server logs for any exceptions occurring in the past. Also, consider synchronizing the Principal Store manually."
     */
    String PRINCIPAL_STORE_NOT_SYNCHRONIZED_UNKNOWN_REASON = "principal_store_not_synchronized_unknown_reason";

    /**
     * @msg.message msg="CRC failed for application : {0}. Principal Realm Store of Peer Server {1} is not synchronized with that of Enterprise Server. Reason :: {2}. Consider synchronizing the Principal Store manually."
     */
    String PRINCIPAL_STORE_NOT_SYNCHRONIZED = "principal_store_not_synchronized";

    /**
     * @msg.message msg="Connectivity and Resource Check is not done for Application: {0}. This is mandatory before launching an event process."
     */
    String CONNECTIVITY_RESOURCE_CHECK_NOT_DONE = "connectivity_resource_check_not_done";

    /**
     * @msg.message msg="Exception occurred while launching application {0}. Error Details : {1}"
     */
    String EXCEPTION_WHILE_LAUNCHING_APPLICATION = "exception_while_launching_application";

    /**
     * @msg.message msg="Exception occurred while getting memory usage for component {0} in application {1}."
     */
    String EXCEPTION_WHILE_GETTING_MEMORY_USAGE = "exception_while_getting_memory_usage";

    /**
     * @msg.message msg="Exception occurred in peer server {0} while getting component statistics. Error Details : {1}."
     */
    String EXCEPTION_WHILE_GETTING_COMPONENT_STATS_WITH_PEER_NAME = "exception_while_getting_component_stats_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while getting statistics  for component {0} in application {1}."
     */
    String EXCEPTION_WHILE_GETTING_COMPONENT_STATS = "exception_while_getting_component_stats";

    /**
     * @msg.message msg="Exception occurred in peer server {0} while getting memory usage statistics. Error Details : {1}."
     */
    String EXCEPTION_WHILE_GETTING_MEMORY_USAGE_WITH_PEER_NAME = "exception_while_getting_memory_usage_with_peer_name";

    /**
     * @msg.message msg="Stream corruption encountered while reading response for method call {0}."
     */
    String STREAM_CORRUPTION = "stream_corruption";

    /**
     * @msg.message msg="Couldnot start service: {0} for application: {1} as the FES is NOT connected to underlying MQ server. (possible reason: FES is Passive)."
     */
    String ERROR_SERVICE_START_ERROR = "error_service_start_error";

    /**
     * @msg.message msg="Could not stop all services of application {0}. Reason: FES is not connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_STOP_ALL_SERVICES = "error_stop_all_services";

    /**
     * @msg.message msg="Failed to stop services in order for application {0}__{1}"
     */
    String ERROR_FAIL_TO_STOP_SERVICES_IN_ORDER = "error_fail_to_stop_services_in_order";

    /**
     * @msg.message msg="Could not stop all services of application {0} with version {1} . Reason: FES is not connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_STOPPING_LAUNCH_ORDER_COMPONENTS = "error_stopping_launch_order_components";

    /**
     * @msg.message msg="Couldnot stop service: {0} for application: {1} as the FES is NOT connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_STOP_SERVICE = "error_stop_service";

    /**
     * @msg.message msg="Could not start all services of application {0}__{1}. Reason: FES is not connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_START_ALL_SERVICES = "error_start_all_services";

    /**
     * @msg.message msg="Could not start services of application {0}__{1} in launch order. Exception : {2} "
     */
    String ERROR_START_SERVICES_LAUNCH_ORDER = "error_start_services_launch_order";

    /**
     *  @msg.message msg="Could not start services of application {0}__{1} in launch order. Either peers servers necessary for starting services are not available or services with launch order: {2} are not started in a specified launch order wait time."
     */
    String ERROR_STARTING_SERVICES_LAUNCH_ORDER = "error_starting_services_launch_order";

    /**
     * @msg.message msg="Could not kill application {0}. Reason: FES is not connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_KILL_APPLICATION = "error_kill_application";

    /**
     * @msg.message msg="Cannot kill service: {0} for application: {1} as the FES is NOT connected to underlying MQ server. (possible reason: FES Passive)."
     */
    String ERROR_SERVICE_KILL_ERROR = "error_service_kill_error";

    /**
     * @msg.message msg="None of the peer servers were found to be running for Application {0} while doing operation {1}."
     */
    String ERROR_SET_LOGGER_APPLICATION = "error_set_logger_application";

    /**
     * @msg.message msg="Service Instance cannot be found for service {0} in application {1} while executing call {2}."
     */
    String SERVICE_INSTANCE_NOT_FOUND = "service_instance_not_found";

    /**
     * @msg.message msg="Could not set LogLevel for module {0} in service {1} for application {2} as the loglevel is NULL for this module."
     */
    String ERROR_LOG_LEVEL_NOT_FOUND = "error_log_level_not_found";

    /**
     * @msg.message msg="Failed to parse Log level {0} in setLogLevel() for service {1} in application {2}."
     */
    String ERROR_PARSE_LOGLEVEL = "error_parse_loglevel";

    /**
     * @msg.message msg="Could not set LogLevel for module {0} in Route: {1} of application {2} as the loglevel is NULL for this module."
     */
    String ERROR_ROUTELOG_LEVEL_NOT_FOUND = "error_routelog_level_not_found";

    /**
     * @msg.message msg="Failed to parse Log level {0} in setLogLevel() for Route {1} in application {2}."
     */
    String ERROR_PARSE_ROUTE_LOGLEVEL = "error_parse_route_loglevel";

    /**
     * @msg.message msg="Route with GUID {0} can''t be found in application {1}"
     */
    String ROUTE_NOT_FOUND = "route_not_found";

    /**
     * @msg.message msg="Debuuger Route is created with routeID : {0} in application : {1}"
     */
    String DEBUGGER_ROUTE_SET = "debugger_route_set";

    /**
     * @msg.message msg="Debuuger Route is removed for routeID : {0} in an application : {1}"
     */
    String DEBUGGER_ROUTE_REMOVE = "debugger_route_remove";

    /**
     * @msg.message msg="Message with id : {0} is modifed on Debuuger Route  with routeID : {1} in an application : {2}"
     */
    String DEBUGGER_ROUTE_MESSAGE_MODIFIED = "debugger_route_message_modified";

    /**
     * @msg.message msg="Message with id : {0} is deleted on Debuuger Route  with routeID : {1} in an application : {2}"
     */
    String DEBUGGER_ROUTE_MESSAGE_DELETED = "debugger_route_message_deleted";

    /**
     * @msg.message msg="Failed to modify route Selector for route : {0} of application : {1} as no configured Peer Server for source service {2} is running."
     */
    String ERROR_CHANGING_ROUTE_SELECTOR = "error_changing_route_selector";

    /**
     * @msg.message msg="Cannot set the debugger on route: {0} as the peer server {1} of the route source is not running."
     */
    String ERROR_DEBUGGER_SET_ERROR = "error_debugger_set_error";

    /**
     * @msg.message msg="Cannot set the debugger on route: {0} as the peer server of the route target is not running."
     */
    String ERROR_DEBUGGER_SET_ERROR2 = "error_debugger_set_error2";

    /**
     * @msg.message msg="Cannot set the debugger on route: {0} as the route target has launch type None."
     */
    String ERROR_DEBUGGER_SET_ERROR3 = "error_debugger_set_error3";

    /**
     * @msg.message msg="Cannot pause the debugger on route: {0} as the peer server of the route source is not running and route doesn't exist"
     */
    String ERROR_DEBUGGER_ROUTE_PAUSE = "error_debugger_route_pause";

    /**
     * @msg.message msg="Cannot remove the debugger on route: {0} as the peer server of the route target is not running."
     */
    String ERROR_DEBUGGER_REMOVE_ERROR2 = "error_debugger_remove_error2";

    /**
     * @msg.message msg="Reverting the changes made in setting Debugger on route {0} of event flow process {1}."
     */
    String UNDO_SET_DEBUGGER = "undo_set_debugger";

    /**
     * @msg.message msg="Error occurred while trying to delete Temporary Debugger Queues,which were created while setting debugger on Route {0}."
     */
    String ERROR_DELETE_TEMP_QUEUES = "error_delete_temp_queues";

    /**
     * @msg.message msg="Error occurred while trying to undo changes ,made in setting Debugger on route {0} in FES for Event flow process: {1}. Warning! An Invalid debug route with ID: {2} could exist in FES. "
     */
    String ERROR_UNDO_SET_DEBUGGER = "error_undo_set_debugger";

    /**
     * @msg.message msg ="failed to update outgoing route"
     */
    String ERROR_UPDATE_DEBUGGER = "error_update_debugger";

    /**
     * @msg.message msg="debugger on component launch type none: {0}"
     * */
    String DEBUGGER_ON_NONE_TYPE = "debugger_on_none_type";

    /**
     * @msg.message msg="Breakpoint already set on route: {0}"
     */
    String BREAKPOINT_ALREADY_SET = "breakpoint_already_set";

    /**
     * @msg.message msg="Cannot add breakpoint as route:{0} {1} component:{2} is of none launch type and route does not exist in peer server"
     */
    String ERROR_ADD_DEBUGGER = "error_add_debugger";

    /**
     * @msg.message msg="Failed to set Debugger on route {0}.Exception occurred while changing target of Route in Event flow process: {1}.Server will try to Undo changes made in the process. Error Details {2}"
     */
    String ERROR_SET_DEBUGGER = "error_set_debugger";
    /**
     * @msg.message msg="can''t get state from Peer Servers for application: {0}. Error Details : {1}"
     */
    String ERROR_APPLICATION_DETAILS_FETCH_ERROR = "error_application_details_fetch_error";

    /**
     * @msg.message msg="The remote service instance {0} of application {1} cannot be found in external application {2}."
     */
    String ERROR_EXTERNAL_SERVICE_NOT_FOUND = "error_external_service_not_found";

    /**
     * @msg.message msg="Externally referenced application {2}:{3} is not running. {0}:{1} Process cannot start before {2}:{3} is running. Cyclic external references is not allowed."
     */
    String ERROR_REMOTE_APPLICATION_NOT_RUNNING = "error_remote_application_not_running";

    /**
     * @msg.message msg="Event Process {0}:{1} is not running."
     */
    String ERROR_APPLICATION_NOT_RUNNING = "error_application_not_running";

    /**
     * @msg.message msg="The specified application: {0} doesn''t exist in the repository."
     */
    String APPLICATION_NOT_FOUND = "application_not_found";

    /**
     * @msg.message msg="The service: {0} doesn''t exist in the central repository."
     */
    String SERVICE_NOT_FOUND = "service_not_found";

    /**
     * @msg.message msg="Service Instance {0} of application {1} is not fully configured.  Please configure the service by double-clicking the service icon."
     */
    String ERROR_COMPONENT_NOT_FULLY_CONFIGURED_ERROR = "error_component_not_fully_configured_error";

    /**
     * @msg.message msg="Service Instance {0} of application {1} is not configured.  Please configure the service by double-clicking the service icon."
     */
    String ERROR_COMPONENT_NOT_CONFIGURED_ERROR = "error_component_not_configured_error";

    /**
     * @msg.message msg="One or more resources of the service: {0} don''t exist in the central repository. Error details : {1}"
     */
    String ERROR_RESOURCE_NOT_EXISTS = "error_resource_not_exists";

    /**
     * @msg.message msg="Can''t compile the service: {0} as the ServiceDescriptor states it is NOT launchable."
     */
    String SERVICE_NOT_LAUNCHABLE = "service_not_launchable";

    /**
     * @msg.message msg="Deployment permission denied for  Service GUID: {0}, Service Ver : {1}, App GUID: {2}, App Ver: {3}, Peer Server GUID: {4}"
     */
    String ERROR_DEPLOYMENT_DENIED_ERROR = "error_deployment_denied_error";

    /**
     * @msg.message msg="Application View permission denied for User : {0}, App GUID: {1}"
     */
    String ERROR_VIEW_PERMISSION_DENIED = "error_view_permission_denied";

    /**
     * @msg.message msg="User : {0} is not allowed to compose the application : {1}"
     */
    String ERROR_COMPOSE_PERMISSION_DENIED = "error_compose_permission_denied";

    /**
     * @msg.message msg="ReInject SBW permission denied for User: {0}"
     */
    String REINJECT_SBW_PERMISSION_DENIED = "reinject_sbw_permission_denied";

    /**
     * @msg.message msg="Can''t compile this service: {0}.  Service Dependency : {1} : {2}, not available in repository"
     */
    String ERROR_SERVICE_DEPENDENCY_NOT_EXISTS = "error_service_dependency_not_exists";

    /**
     * @msg.message msg="Service: {0} can''t use port: {1} for remote debugging as this  port is already being used by service: {2}"
     */
    String ERROR_SERVICE_DEBUG_PORT_INUSE = "error_service_debug_port_inuse";

    /**
     * @msg.message msg="You have specified more than one node for the service : {0} BUT the ServiceDescriptor states it doesn''t have failover supported. Please remove the multiple nodes specified for this service and compile the application again."
     */
    String MULTIPLE_NODES_SPECIFIED_FOR_SERVICE = "multiple_nodes_specified_for_service";

    /**
     * @msg.message msg="Can''t launch this service: {0} manually as the ServiceDescriptor  states it cannot be launched externally."
     */
    String SERVICE_MANUAL_LAUNCH_NOT_SUPPORTED = "service_manual_launch_not_supported";

    /**
     * @msg.message msg="Can''t launch this service: {0} in memory as the ServiceDescriptor  states it cannot be launched in memory."
     */
    String SERVICE_INMEMORY_LAUNCH_NOT_SUPPORTED = "service_inmemory_launch_not_supported";

    /**
     * @msg.message msg="Route with the name {0}  already exists in the event flow process."
     */
    String ROUTE_ALREADY_EXISTS = "route_already_exists";

    /**
     * @msg.message msg="Either the srcService: {0} or the tgtService: {1} of route: {2} doesn''t exist in the central repository."
     */
    String ERROR_ROUTE_SERVICES_NOT_EXISTS = "error_route_services_not_exists";

    /**
     * @msg.message msg="Source port :{0} does not exist for Service: {1} in the application: {2}:{3}."
     */
    String ERROR_SOURCE_PORT_OBJECT_NOT_EXISTS = "error_source_port_object_not_exists";

    /**
     * @msg.message msg="Target port: {0} does not exist for Service: {1} in the application: {2}:{3}."
     */
    String ERROR_TARGET_PORT_OBJECT_NOT_EXISTS = "error_target_port_object_not_exists";

    /**
     * @msg.message msg="In route: {0}, the DTD defined at sourcePort: {1} does NOT match with the DTD defined at targetPort: {2}."
     */
    String ERROR_DTD_NOT_MATCHING_ERROR = "error_dtd_not_matching_error";

    /**
     * @msg.message msg="Exception occurred while killing Application {0} : {1}. Reason: Service Instance {2} which belongs to this application is referenced Externally by Applications : {3}. All these applications need to be killed before killing application {0}:{1}."
     */
    String ERROR_REFERENCED_EVENT_PROCESS_RUNNING = "error_referenced_event_process_running";

    /**
     * @msg.message msg="No Logs to export. Reason: Application {0} is Empty"
     */
    String NO_LOGS_FOR_EMPTY_APPLICATION = "no_logs_for_empty_application";

    /**
     * @msg.message msg="The Node:{0} is found disconnected when trying to execute the Call: {1}."
     */
    String PEER_NODE_DISCONNECTED = "peer_node_disconnected";

    /**
     * @msg.message msg="Exception occurred with getting logs for service: {0} of application: {1} from the Peer Server: {2}."
     */
    String ERROR_FECTCH_APPLICATIONLOGS_FROM_PEER = "error_fectch_applicationlogs_from_peer";

    /**
     * @msg.message msg="Exception occurred while getting the {0} trace for route {1} of application {2} from peer node {3}"
     */
    String ERROR_FETCH_ROUTE_LOGS = "error_fetch_route_logs";

    /**
     * @msg.message msg="Exception occurred while cleaning the {0} logs for route {1} of application {2} from peer node {3}"
     */
    String ERROR_CLEAR_ROUTE_LOGS = "error_clear_route_logs";

    /**
     * @msg.message msg="Exception occurred when clearing the logs of service {0} of application {1}"
     */
    String ERROR_CLEAR_APPLICATION_LOGS = "error_clear_application_logs";

    /**
     * @msg.message msg="Exception occurred when creating debug route {0}. Error Details : {1}"
     */
    String ERROR_CREATE_DEBUG_ROUTE = "error_create_debug_route";

    /**
     * @msg.message msg="unable to set Debugger on {0} launch."
     */
    String ERROR_SET_DEBUGGER_ON_PEER_LAUNCH = "error_set_debugger_on_peer_launch";

    /**
     * @msg.message msg="Error deleting Queue {0} in call to remove debugger"
     */
    String ERROR_DELETE_DEBUG_QUEUE = "error_delete_debug_queue";

    /**
     * @msg.message msg="Error deleting admin Object {0} in call to remove debugger"
     */
    String ERROR_DELETE_ADMIN_OBJECTS = "error_delete_admin_objects";

    /**
     * @msg.message msg="Error Closing the debug Route {0}. Error details {1}."
     */
    String ERROR_CLOSE_DEBUG_ROUTE = "error_close_debug_route";

    /**
     * @msg.message msg="Error Removing the debug Route {0}. of application {1}, Error details {1}."
     */
    String ERROR_REMOVE_DEBUG_ROUTE = "error_remove_debug_route";

    /**
     * @msg.message msg="Error setting admin credentials to route descriptor of debug route in call to set debugger"
     */
    String ERROR_SETTING_ADMIN_CREDENTIALS = "error_setting_admin_credentials";

    /**
     * @msg.message msg="FES URL Obtained from the transport Manager is null. Unable to create routeDescriptor for route {0}."
     */
    String ERROR_FES_URL_NULL = "error_fes_url_null";

    /**
     * @msg.message msg="Break Point already exists on this Route: {0}"
     */
    String DEBUGGER_ALREADY_SET = "debugger_already_set";

    /**
     * @msg.message msg="Unable to Create Route as the routeDescriptor returned is NULL. Route: {0}"
     */
    String ERROR_DEBUG_ROUTE_CREATION_FAILURE = "error_debug_route_creation_failure";

    /**
     * @msg.message msg="Error occurred while trying to setting permissions on debugger queue :: {0}."
     */
    String ERROR_ADD_PERMISSIONS = "error_add_permissions";

    /**
     * @msg.message msg="Error occurred while trying to create debugger queue :: {0}."
     */
    String ERROR_CREATE_DEBUGGER_QUEUE = "error_create_debugger_queue";

    /**
     * @msg.message msg="Exception occurred on Proxy Call to Peer: {0} while executing call {1}. Error details : {2}"
     */
    String ERROR_EXECUTING_PROXYCALL = "error_executing_proxycall";

    /**
     * @msg.message msg="Exception occurred in Route"
     */
    String ERROR_IN_ROUTE = "error_in_route";

    /**
     * @msg.message msg="Source Instance found as NULL for the route: {0}"
     */
    String SOURCE_SERVICE_INSTANCE_NULL = "source_service_instance_null";

    /**
     * @msg.message msg="Processing Peer Server launch event."
     */
    String TPS_LAUNCH_EVENT = "tps_launch_event";

    /**
     * @msg.message msg="Event Type is :TPS_EVENT, status for: {0} is: {1}"
     */
    String TPS_STATUS = "tps_status";

    /**
     * @msg.message msg="The Peer Server: {0} is already DOWN. Ignoring the Unbound event for: {1}."
     */
    String TPS_ALREADY_DOWN = "tps_already_down";

    /**
     * @msg.message msg="Exception occurred while registering the Event Listener for Application Controller. Selector String: {0}."
     */
    String ERROR_REGISTER_EVENT_LISTENER = "error_register_event_listener";

    /**
     * @msg.message msg="Exception occurred while un-registering the Event Listener for Application Controller. Exception: {0}"
     */
    String ERROR_UNREGISTER_EVENT_LISTENER = "error_unregister_event_listener";

    /**
     * @msg.message msg="Error in {2}.  Service Instance {0} doesn''t exist in Event Process {1}."
     */
    String SERVICE_INSTANCE_NOT_PRESENT = "service_instance_not_present";

    /**
     * @msg.message msg="Error in {2}.  Service Instance {0} of Event Process {1} is not in running state"
     */
    String SERVICE_NOT_RUNNING = "service_not_running";

    /**
     * @msg.message msg="Error in {2}.  Service Instance {0} of Event Process {1} is not bound"
     */
    String SERVICE_NOT_BOUND = "service_not_bound";

    /**
     * @msg.message msg="Error in {1}.  None of the Service instances of Event Process {0} is in running state"
     */
    String NO_SERVICE_RUNNING = "no_service_running";

    /**
     * @msg.message msg="Error in {2}.  Service Instance {0} of Event Process {1} is already in running state"
     */
    String SERVICE_RUNNING = "service_running";

    /**
     * @msg.message msg="No service instances to be started in the Event Process {0}__{1}"
     */
    String NO_SERVICE_TOSTART = "no_service_tostart";

    /**
     * @msg.message msg="Executing call {2}:  All service instances of Event Process {0}__{1} are already in stopped state"
     */
    String NO_SERVICE_TO_STOP = "no_service_to_stop";

    /**
     * @msg.message msg="Service Handle is not present for service with instance name: {0} in application: {1}."
     */
    String SERVICE_HANDLE_NOT_PRESENT = "service_handle_not_present";

    /**
     * @msg.message msg="Failed to fetch Service Handles for application: {0}__{1}, Exception : {2}."
     */
    String ERROR_FAILED_TO_FETCH_SERVICE_HANDLE = "error_failed_to_fetch_service_handle";

    /**
     * @msg.message msg="Error while parsing the service instance: {0} configuration  in application: {1} Exception Details: {2}."
     */
    String ERROR_PARSING_STUB_CONFIGURATION = "error_parsing_stub_configuration";

    /**
     * @msg.message msg="Service instance: {0} in application: {1} does not have a port with name: {2}."
     */
    String PORT_DOES_NOT_EXIST = "port_does_not_exist";

    /**
     * @msg.message msg="Destination with name: {0} not found"
     */
    String ERROR_FINDING_DESTINATION = "error_finding_destination";

    /**
     * @msg.message msg="Peer Nodes: {0} configured for Service instance: {1} in application: {2} are not currently connected to Enterprise Server"
     */
    String PEER_NOT_RUNNING_FOR_SERVICE = "peer_not_running_for_service";

    /**
     * @msg.message msg=" The Route {0} is invalid, one of its ports is disabled "
     */
    String ROUTE_INVALID = "route_invalid";

    /**
     * @msg.message msg=" Peer Server {0} is not in running state"
     */
    String ERROR_NODE_NOT_AVAILABLE_ERROR = "error_node_not_available_error";

    /**
     * @msg.message msg="Peer Server not available"
     */
    String PEER_NOT_AVAILABLE = "peer_not_available";

    /**
     * @msg.message msg="Unable to set port parameters for {2} for Route {0} on application {1}"
     */
    String ERROR_SET_PORT_PARAMS = "error_set_port_params";

    /**
     * @msg.message msg="Error in Synchronizing the application {0}. Application can only be synchronized to either running version or highest version of application."
     */
    String APPLICATION_SYNC_ERROR = "application_sync_error";

    /**
     * @msg.message msg="Exception occured while starting services. See Peer Server logs for more details."
     */
    String ERROR_START_SERVICES = "error_start_services";

    /**
     * @msg.message msg="Exception occurred in Peer Server : {0} while starting services. Message : {1} "
     */
    String ERROR_START_SERVICES_WITH_PEER_NAME = "error_start_services_with_peer_name";

    /**
     * @msg.message msg="Exception occured while removing in-valid routes. {0}. Exception {1}"
     */
    String ERROR_REMOVE_NONEXISTING_ROUTES = "error_remove_nonexisting_routes";

    /*-------------------------------------Error Statements --------------------------------------------------*/


    /*-------------------------------------FATAL Statements --------------------------------------------------*/

    /**
     * @msg.message msg="Mbean Server instance not found"
     */
    String ERROR_MBEAN_SERVER_NOTFOUND = "error_mbean_server_notfound";

    /*-------------------------------------FATAL Statements --------------------------------------------------*/

    /**
     * @msg.message msg="Checking if the current user {0} has permission to prepare for launch"
     */
    String SECURITY_CHECK_FOR_PREPARELAUNCH = "security_check_for_preparelaunch";

    /**
     * @msg.message msg="checking for DTD validation on routes of the application {0}"
     */
    String DTD_VALIDATION = "dtd_validation";

    /**
     * @msg.message msg="Hostname is not matched for cert."
     */
    String HOSTNAME_NOT_MATCH = "hostname_not_match";

    /**
     * @msg.message msg="Error setting SSL parameters"
     */
    String ERROR_SET_SSL_PARAMS = "error_set_ssl_params";

    /**
     * @msg.message msg="[{0}] Peer Server {1} is connected to the ESB Network"
     */
    String PEER_CONNECTED="peer_connected";

    /**
     * @msg.message msg="[{0}] Peer Server {1} is disconnected from the ESB Network"
     */
    String PEER_DISCONNECTED="peer_disconnected";
    /**
     * @msg.message msg="Application {0} state being restored."
     */
    String APP_RESTORATION="app_restoration";
    /**
     * @msg.message msg="Application {0} Launch execution on going."
     */
    String APP_LAUNCH_INPROCESS="app_launch_inprocess";
    /**
     * @msg.message msg="Application  {0} Launch successful."
     */
    String APP_LAUNCH_SUCCESS="app_launch_success";
    /**
     * @msg.message msg="Application {0} killed."
     */
    String APP_KILLED="app_killed";
    /**
     * @msg.message msg="Debugger set on Route {0} in Application {1}."
     */
    String DEBUGGER_SET="debugger_set";
    /**
     * @msg.message msg="Debugger removed on Route {0} in Application {1}."
     */
    String DEBUGGER_REM="debugger_rem";
    /**
     * @msg.message msg="FES is not connected to underlying MQ server.(possible reason:FES Passive)."
     */
    String PASSIVE_SERVER="passive_server";
    /**
     * @msg.message msg="No peer server is active."
     */
    String NO_ACTIVE_PEER="no_active_peer";
    /**
     * @msg.message msg="Application {0} Synchronization started"
     */
    String SYNC_STARTED="sync_started";
    /**
     * @msg.message msg="Application {0} Synchronization successful."
     */
    String SYNC_DONE="sync_done";
    /**
     * @msg.message msg="I/O Exception on writing to file."
     */
    String ERROR_WRITE_FILES="error_write_files";

    /**
     * @msg.message msg="FES Name: {0}."
     */
    String FES="fes";

    /**
     * @msg.message msg="FES URL: {0}"
     */
    String FES_URL="fes_url";

    /**
     * @msg.message msg="Specified invalid webinterface type: {0}. Allowed values: WebService: 1, Web/HTTP :2, Worklist: 4"
     */
    String INVALID_WEBINTERFACE="invalid_webinterface";

    /**
     * @msg.message msg="No node is present in Deployment nodes list for Service instance {0}."
     */
    String ERROR_NO_NODE_PRESENT="error_no_node_present";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @msg.message msg="Cannot kill application {0} as it has routes with breakpoints. Routes being debugged are: {1}."
     */
    String CANT_KILL_APP="cant_kill_app";

    /**
     * @msg.message msg="Logging parameters changed for service instance {0} in Event Process {1}."
     */
    String LOGGING_PARAMETERS_CHANGED = "logging_parameters_changed";

    /**
     * @msg.message msg="Exception occurred while changing logging parameters for service instance {0} in Event Process {1}. Exception details : {2}"
     */
    String LOGGING_PARAMETERS_CHANGE_EXCEPTION = "logging_parameters_change_exception";

    /**
     * @msg.message msg="Route selector changed for route {0} in Event Process {1}."
     */
    String ROUTE_SELECTOR_CHANGED = "route_selector_changed";

    /**
     * @msg.message msg="Exception occurred while changing route selector for route {0} in Event Process {1}. Exception details : {2}"
     */
    String ROUTE_SELECTOR_CHANGE_EXCEPTION = "route_selector_change_exception";

    /**
     * @msg.message msg="Route storage changed for route {0} in Event Process {1}."
     */
    String ROUTE_STORAGE_CHANGED = "route_storage_changed";

    /**
     * @msg.message msg="Exception occurred while changing route storage for route {0} in Event Process {1}. Exception details : {2}"
     */
    String ROUTE_STORAGE_CHANGE_EXCEPTION = "route_storage_change_exception";

    /**
     * @msg.message msg="Route time to live changed for route {0} in Event Process {1}. New Value : {2}"
     */
    String ROUTE_TTL_CHANGED = "route_ttl_changed";

    /**
     * @msg.message msg="Exception occurred while changing route time to live property for route {0} in Event Process {1} to value {2}. Exception details : {2}"
     */
    String ROUTE_TTL_CHANGE_EXCEPTION = "route_ttl_change_exception";

    /**
     * @msg.message msg="Route transformtion changed for route {0} in Event Process {1}."
     */
    String ROUE_TRANSFORMATION_CHANGED = "roue_transformation_changed";

    /**
     * @msg.message msg="Exception occurred while changing route transformation for route {0} in Event Process {1}. Exception details : {2}"
     */
    String ROUE_TRANSFORMATION_CHANGE_EXCEPTION = "roue_transformation_change_exception";

    /**
     * @msg.message msg="SBW disabled for port {0} of service instance {1} in Event Process {1}."
     */
    String SBW_DISABLED = "sbw_disabled";

    /**
     * @msg.message msg="Message filter not set for port {0} of service instance {1} in Event Process {1}."
     */
    String MESSAGE_FILTER_DISABLED = "message_filter_disabled";

    /**
     * @msg.message msg="Exception occurred while disabling sbw on port {0} of service instance {1} in Event Process {2}. Exception details : {2}"
     */
    String SBW_DISABLE_EXCEPTION = "sbw_disable_exception";

    /**
     * @msg.message msg="Exception occurred while disabling message filter on port {0} of service instance {1} in Event Process {2}. Exception details : {2}"
     */
    String MESSAGE_FILTER_DISABLE_EXCEPTION = "message_filter_disable_exception";

    /**
     * @msg.message msg="SBW enabled for port {0} of service instance {1} in Event Process {4}. Is Workflow End? : {2}. Tracking Type : {3}"
     */
    String SBW_ENABLED = "sbw_enabled";
    /**
     * @msg.message msg="Message Filter enabled for port {0} of service instance {1} in Event Process {2}."
     */
    String MESSAGE_FILTER_ENABLED = "message_filter_enabled";

    /**
     * @msg.message msg="Exception occurred while enabling sbw on port {0} of service instance {1} in Event Process {4}. Workflow Type : {2}. Tracking Type : {3}. Exception details : {5}"
     */
    String SBW_ENABLE_EXCEPTION = "sbw_enable_exception";

    /**
     * @msg.message msg="Exception occurred while enabling message filter on port {0} of service instance {1} in Event Process {2}. Exception details : {3}"
     */
    String MESSAGE_FILTER_ENABLE_EXCEPTION = "message_filter_enable_exception";

    /**
     * @msg.message msg="Exception occurred while enabling application performance view on port {0} of service instance {1} in Event Process {4}.  End Point? : {2}. Type : {3}. Exception details : {5}"
     */
    String APP_PERF_ENABLE_EXCEPTION = "app_perf_enable_exception";

    /**
     * @msg.message msg="Log levels changed for service instance {0} in Event Process {2}. New Log levels : {1}"
     */
    String LOG_LEVEL_CHANGED = "log_level_changed";

    /**
     * @msg.message msg="Exception occurred while changing log levels to {1} for service instance {0} in Event Process {2}. Exception details : {3}."
     */
    String LOG_LEVEL_CHANGE_EXCEPTION = "log_level_change_exception";

    /**
     * @msg.message msg="Log level changed for all service instances of Event Process {1}. New Log level : {0}"
     */
    String APP_LOG_LEVEL_CHANGED = "app_log_level_changed";

    /**
     * @msg.message msg="Exception occurred while changing log level to {0} for all service instances of Event Process {1}. Exception details : {2}."
     */
    String APP_LOG_LEVEL_CHANGE_EXCEPTION = "app_log_level_change_exception";

    /**
     * @msg.message msg="Log level changed for service instance {0} from Event Process {2}. New Log level : {1}"
     */
    String SERVICE_LOG_LEVEL_CHANGED = "service_log_level_changed";

    /**
     * @msg.message msg="Exception occurred while changing log level to {1} for service instance {0} from Event Process {2}. Exception details : {3}."
     */
    String SERVICE_LOG_LEVEL_CHANGE_EXCEPTION = "service_log_level_change_exception";

    /**
     * @msg.message msg="Log levels changed for route {0} in Event Process {2}. New Log levels : {1}"
     */
    String ROUTE_LOG_LEVEL_CHANGED = "route_log_level_changed";

    /**
     * @msg.message msg="Exception occurred while changing log levels to {1} for route {0} in Event Process {2}. Exception details : {3}."
     */
    String ROUTE_LOG_LEVEL_CHANGE_EXCEPTION = "route_log_level_change_exception";

    /**
     * @msg.message msg="SBW tracking type changed for port {0} of service instance {1} in Event Process {3}. New Tracking Type : {2}"
     */
    String TRACKED_DATA_TYPE_CHANGED = "tracked_data_type_changed";

    /**
     * @msg.message msg="Exception occurred while enabling sbw on port {0} of service instance {1} in Event Process {2}. New Tracking Type : {3}. Exception details : {4}"
     */
    String TRACKED_DATA_TYPE_CHANGE_EXCEPTION = "tracked_data_type_change_exception";

    /**
     * @msg.message msg="Event Process {0}, version {1} was killed successfully."
     */
    String APPLICATION_KILLED = "application_killed";

    /**
     * @msg.message msg="Exception occurred while killing event process {0}, version {1}. Exception details : {2}"
     */
    String APPLICATION_KILL_EXCEPTION = "application_kill_exception";

    /**
     * @msg.message msg="Could not launch component {0} of Application {1} as none of the configured nodes are running."
     */
    String ERROR_LAUNCH_COMPONENT="error_launch_component";

    /**
     * @msg.message msg="Exception occurred while sending data to port {0} of service {1} for application {2}. See Peer Server logs for more details."
     */
    String ERROR_SENDING_DATA="error_sending_data";

    /**
     * @msg.message msg="Exception occurred while getting delieverable msg count for application {0}. See Peer Server logs for more details."
     */
    String ERROR_GETTING_DELIEVERABLE_MSG_COUNT="error_getting_delieverable_msg_count";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while getting delieverable msg count for application {1}. Message : {2}."
     */
    String ERROR_GETTING_DELIEVERABLE_MSG_COUNT_WITH_PEER_NAME="error_getting_delieverable_msg_count_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while removing debugger for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_REMOVING_DEBUGGER="error_removing_debugger";

    /**
     * @msg.message msg="Exception occurred while setting debugger for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_SETTING_DEBUGGER="error_setting_debugger";

    /**
     * @msg.message msg="Exception occurred while starting paused route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_STARTING_ROUTE="error_starting_route";

    /**
     * @msg.message msg="Exception occurred while changing route TTL for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_CHANGING_ROUTE_TTL="error_changing_route_ttl";

    /**
     * @msg.message msg="Exception occurred while changing route type for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_CHANGING_ROUTETYPE="error_changing_routetype";

    /**
     * @msg.message msg="Exception occurred while changing route storage for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_CHANGING_ROUTE_STORAGE="error_changing_route_storage";

    /**
     * @msg.message msg="Exception occurred while changing route transformation for route {0} of application {1}. See Peer Server logs for more details."
     */
    String ERROR_CHANGING_ROUTE_TRANSFORMATION="error_changing_route_transformation";

    /**
     * @msg.message msg="Exception occurred while setting sbw tracked data type. See Peer Server logs for more details."
     */
    String ERROR_SETTING_TRACKING_TYPE="error_setting_tracking_type";

    /**
     * @msg.message msg="Exception occurred while synchronizing application. See Peer Server logs for more details."
     */
    String ERROR_SYNCHRONIZE_APPLICATION="error_synchronize_application";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while synchronizing application. Message : {1}."
     */
    String ERROR_SYNCHRONIZE_APPLICATION_WITH_PEER_NAME="error_synchronize_application_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while killing application. See Peer Server logs for more details."
     */
    String ERROR_KILLING_APPLICATION="error_killing_application";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while killing application. Message : {1}."
     */
    String ERROR_KILLING_APPLICATION_WITH_PEER_NAME="error_killing_application_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while killing service. See Peer Server logs for more details."
     */
    String ERROR_KILLING_SERVICE="error_killing_service";

    /**
     * @msg.message msg="Exception occurred while stopping service. See Peer Server logs for more details."
     */
    String ERROR_STOPPING_SERVICE="error_stopping_service";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while stopping service. Message : {1}."
     */
    String ERROR_STOPPING_SERVICE_WITH_PEER_NAME="error_stopping_service_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while stopping services. See Peer Server logs for more details."
     */
    String ERROR_STOPPING_SERVICES="error_stopping_services";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while stopping services. Message : {1}."
     */
    String ERROR_STOPPING_SERVICES_WITH_PEER_NAME="error_stopping_services_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while starting service. See Peer Server logs for more details."
     */
    String ERROR_STARTING_SERVICE="error_starting_service";

    /**
     * @msg.message msg="Exception occurred while preparing launch. See Peer Server logs for more details."
     */
    String ERROR_PREPARING_LAUNCH="error_preparing_launch";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while preparing launch. Message : {1}."
     */
    String ERROR_PREPARING_LAUNCH_WITH_PEER_NAME="error_preparing_launch_with_peer_name";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while launching application. Message : {1}."
     */
    String ERROR_LAUNCHING_APPLICATION_WITH_PEER_NAME="error_launching_application_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while getting application state details. See Peer Server logs for more details."
     */
    String ERROR_GETTING_APPLICATION_STATE_DETAILS="error_getting_application_state_details";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while getting application state details. Message : {1}."
     */
    String ERROR_GETTING_APPLICATION_STATE_DETAILS_WITH_PEER_NAME="error_getting_application_state_details_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while setting loglevel for application. See Peer Server logs for more details."
     */
    String ERROR_SETTING_LOGLEVEL_FOR_APP="error_setting_loglevel_for_app";

    /**
     * @msg.message msg="Exception occurred in Peer {0} while setting loglevel for application. Message : {1}."
     */
    String ERROR_SETTING_LOGLEVEL_FOR_APP_WITH_PEER_NAME="error_setting_loglevel_for_app_with_peer_name";

    /**
     * @msg.message msg="Exception occurred while setting loglevel for service. See Peer Server logs for more details."
     */
    String ERROR_SETTING_LOGLEVEL_FOR_SERVICE="error_setting_loglevel_for_service";

    /**
     * @msg.message msg="Exception occurred while getting loglevel. See Peer Server logs for more details."
     */
    String ERROR_GETTING_LOGLEVEL="error_getting_loglevel";

    /**
     * @msg.message msg="Exception occurred while getting last application trace. See Peer Server logs for more details."
     */
    String ERROR_GETTING_APPLICATION_TRACE="error_getting_application_trace";

    /**
     * @msg.message msg="Exception occurred while getting WebService definition for the endPointURL {0}."
     */
    String ERROR_GETTING_WEBSERVICE_DEFINITION="error_getting_webservice_definition";

    /**
     * @msg.message msg="The remote service instance {0} cannot be found in external application {1}."
     */
    String ERROR_EXTERNAL_SERVICE_NOT_FOUND2 = "error_external_service_not_found2";

    /**
     * @msg.message msg="The referred external application {0} cannot be found."
     */
    String ERROR_REMOTE_APPLICATION_NOT_FOUND = "error_remote_application_not_found";

    /**
     * @msg.message msg="Component Instance {0} in Event Process {1} does not support Component Control Protocol (CCP)."
     */
    String CCP_NOT_SUPPORTED = "ccp_not_supported";

    /**
     * @msg.message msg="Memory usage for a component can be obtained only for Separate Process and Manually launched components. Component {0} in Event Process {1} does not fall under any of these categories."
     */
    String LAUNCH_TYPE_INVALID_FOR_MEM_USAGE = "launch_type_invalid_for_mem_usage";

    /**
     * @msg.message msg="Process ID for a component can be obtained only for Separate Process and Manually launched components. Component {0} in Event Process {1} does not fall under any of these categories."
     */
    String LAUNCH_TYPE_INVALID_FOR_PROCESSID = "launch_type_invalid_for_processid";

    /**
     * @msg.message msg="Component {0} in Event Process {1} is not running."
     */
    String COMPONENT_HANDLE_NOT_PRESENT = "component_handle_not_present";

    /**
     * @msg.message msg="Exception occurred while getting memory usage of service instance {0} in Event Process {1}."
     */
    String GET_MEMORY_USAGE_EXCEPTION = "get_memory_usage_exception";

    /**
     * @msg.message msg="Peer Server(s) configured for service instance {0} in Event Process {1} are not running."
     */
    String PEER_NOT_RUNNING = "peer_not_running";

    /**
     * @msg.message msg="User {0} is not allowed to save/create an event process with name {1}. Try some other name."
     */
    String USER_NOT_ALLOWED_TO_SAVE_EP = "user_not_allowed_to_save_ep";

    /**
     * @msg.message msg="Application: {0} has been deleted successfully from the application repository but error occurred while trying to delete SBW events for the application."
     */
    String ERROR_DELETE_SBW_EVENTS_FOR_APP = "error_delete_sbw_events_for_app";

    /**
     * @msg.message msg="Application: {0} has been deleted successfully from the application repository but error occurred while trying to suspend policies for the application. Error details: {1}"
     */
    String ERROR_SUSPEND_POLICIES_FOR_APP = "error_suspend_policies_for_app";

    /**
     * @msg.message msg="Error occurred while reading workflow configuration with name : {0} for port {1} of service instance {2} in application {3}."
     */
    String ERR_READING_SBW_CONFIGURATION = "err_reading_sbw_configuration";

    /**
     * @msg.message msg="Error occurred while reading message filter configuration with name : {0} for port {1} of service instance {2} in application {3}."
     */
    String ERR_READING_MESSAGE_FILTER_CONFIGURATION = "err_reading_message_filter_configuration";

    /**
     * @msg.message msg="SBW configuration property changes are not allowed for port {0} of service instance {1} in application {2} as this port is using named configurations. To make changes to SBW configurations used for this port, assign some other named configuration (with desired parameters) to this port."
     */
    String ERR_SBW_CHANGE_NOT_ALLOWED = "err_sbw_change_not_allowed";

    /**
     * @msg.message msg="Message filter configuration property changes are not allowed for port {0} of service instance {1} in application {2} as this port is using named configurations. To make changes to Message filter configurations used for this port, assign some other named configuration (with desired parameters) to this port."
     */
    String ERR_MESSAGE_FILTER_CHANGE_NOT_ALLOWED = "err_message_filter_change_not_allowed";

    /**
     * @msg.message msg="Error occurred while reading metadata for transformation configuration : {0} for route {1} in application {2}."
     */
    String ERR_READING_TRANSFORMATION_METADATA_CONFIGURATION = "err_reading_transformation_metadata_configuration";

    /**
     * @msg.message msg="Error occurred while reading transformation configuration with name : {0} for route {1} in application {2}."
     */
    String ERR_READING_TRANSFORMATION_CONFIGURATION = "err_reading_transformation_configuration";

    /**
     * @msg.message msg="Message transformation changes are not allowed for route {0} in application {1} as this route is using named configurations. To make changes to message transformation used for this route, assign some other named configuration (with desired changes) to this route."
     */
    String ERR_ROUTE_TRANSFORMATION_CHANGE_NOT_ALLOWED = "err_route_transformation_change_not_allowed";

    /**
     * @msg.message msg="Message transformation changes are not allowed for Port {0} of service {1} in application {2} as this port is using named configurations. To make changes to message transformation used for this port, assign some other named configuration (with desired changes) to it."
     */
    String ERR_APPCONTEXT_TRANSFORMATION_CHANGE_NOT_ALLOWED = "err_appcontext_transformation_change_not_allowed";

    /**
     * @msg.message msg="Error occurred while reading selector configuration with name : {0} for route {1} in application {2}."
     */
    String ERR_READING_SELECTOR_CONFIGURATION = "err_reading_selector_configuration";

    /**
     * @msg.message msg="Message selector changes are not allowed for route {0} in application {1} as this route is using named configurations. To make changes to message selector(s) used for this route, assign some other named configuration (with desired changes) to this route."
     */
    String ERR_ROUTE_SELECTOR_CHANGE_NOT_ALLOWED = "err_route_selector_change_not_allowed";

    /**
     * @msg.message msg="Error occurred while resolving and updating named configurations for service instance : {0} in application : {1}."
     */
    String ERROR_UPDATING_NAMED_CONFIGURATIONS = "error_updating_named_configurations";

    /**
     * @msg.message msg="Application launch and synchronization not allowed for application : {0} as it uses named configurations and user : {1} is not allowed to use named objects present in configuration repository"
     */
    String VIEW_AND_USE_NAMED_OBJECTS_NOT_ALLOWED_FOR_APPLICATION = "view_and_use_named_objects_not_allowed_for_application";

    /**
     * @msg.message msg="Service instance launch not allowed for service instance : {0} in application : {1} as the application uses named configurations and user : {2} is not allowed to use named objects present in configuration repository"
     */
    String VIEW_AND_USE_NAMED_OBJECTS_NOT_ALLOWED_FOR_SERVICE = "view_and_use_named_objects_not_allowed_for_service";

    /**
     * @msg.message msg="The disabled port : {0} of Service : {1} has workflow enabled."
     */
    String ERROR_WORKFLOW_EXISTS_FOR_PORT = "error_workflow_exists_for_port";

    /**
     * @msg.message msg="Failed to kill service : {0} as the service instance is referred in other applications."
     */
    String ERROR_SERVICE_INSTANCE_REFERRED_IN_OTHER_APPS = "error_service_instance_referred_in_other_apps";

    /**
     * @msg.message msg="Failed to save Application {0}:{1}, as cyclic dependency occurred. Remote application(s) of current application are already referring the current application."
     */
    String ERROR_CYCLIC_DEPENDENCY_REFERRED_APPS = "error_cyclic_dependency_referred_apps";

    /**
     * @msg.message msg="Specified port : {0} is not a queue."
     */
    String ERROR_SPECIFIED_PORT_NOT_QUEUE = "error_specified_port_not_queue";

    /**
     * @msg.message msg="Specified Service :{0} Not in Running State"
     */
    String ERROR_SPECIFIED_COMPONENT_NOT_RUNNING = "error_specified_component_not_running";

    /**
     * @msg.message msg="Error updating API keys of REST services"
     */
    String ERROR_CHANGING_REST_API_KEYS = "error_changing_rest_api_keys";


    /**
     *  @msg.message msg="Error cannot delete user : {0} present in group : {1} user credentials are being used in running application : {2}:{3}
     *  Make sure applications are stopped before changing the password"
     */
    String ERROR_CAN_NOT_DELETE_GROUP = "error_can_not_delete_group";

    /**
     *  @msg.message msg="Error cannot delete user : {0} as credentials are being used in running application : {1}:{2}.
     *  Make sure applications are stopped before changing the password"
     */
    String ERROR_CAN_NOT_DELETE_USER = "error_can_not_delete_user";

    /**
     *  @msg.message msg="Error cannot change password for user : {0} as credentials are being used in running applications : {1}.
     *  Make sure applications are stopped before changing the password"
     *
     */
    String ERROR_CAN_NOT_CHANGE_PASSWD = "error_can_not_change_passwd";

    /**
     * @msg.message msg="Error occurred while handling peer launch event for Application: {0}  in FPS:{1} "
     */
    String ERROR_APPLICATION_TPS_LAUNCH_EVENT = "error_application_tps_launch_event";
    /**
     * @msg.message msg="Failed to reinject Tifosi document at port :{0} "
     */
    String ERROR_FAILED_TO_REINJECT = "error_failed_to_reinject";
    /**
     * @msg.message msg="Failed to reinject some Tifosi documents.Please check server logs for more information  "
     */
    String ERROR_FAILED_TO_REINJECT_SOME = "error_failed_to_reinject_some";

    /**
     * @msg.message msg="Ignoring peer reconnect event {0}. As peer is already connected to Enterprise server"
     */
    java.lang.String INFO_IGNORING_PEER_RECONNECT_EVENT = "info_ignoring_peer_reconnect_event";
    /**
     * @msg.message msg="Error while validating the application during import. But application will be saved successfully."
     */
    java.lang.String ERROR_WHILE_VALIDATING_APPLICATION_DURING_IMPORT = "error_while_validating_application_during_import";
    /**
     * @msg.message msg="Error Application {0}:{1} does not exists in the repository."
     */
    String ERR_APP_DOES_NOT_EXISTS = "err_app_does_not_exists";

    /**
     * @msg.message msg="User {0} using client {1} with IP {2}, successfully added breakpoint on route {3} of application {4}."
     */
    String BREAKPOINT_ADD_SUCCESS = "breakpoint_add_success";

    /**
     * @msg.message msg="User {0} using client {1} with IP {2}, successfully removed breakpoint on route {3} of application {4}."
     */
    String BREAKPOINT_REM_SUCCESS = "breakpoint_rem_success";

    /**
     * @msg.message msg="Error occured while synchronizing the application: {0} to handle password change of the user: {1} by the user: {2}"
     */
    String ERROR_SYNCHRONIZING_APPLICATION_AFTER_PASSWORD_CHANGE = "error_synchronizing_application_after_password_change";
}

