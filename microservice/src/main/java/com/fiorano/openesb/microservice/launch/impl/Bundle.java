/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.microservice.launch.impl;

public interface Bundle
{
    /**
     * @msg.message msg="Component {0} version {1} missing from component repository required for the Service Instance {2}"
     */
    public final static String COMPONENT_NOT_PRESENT = "component_not_present";

    /**
     * @msg.message msg="Unable to resolve Hostname :: {0}"
     */
    public final static String INVALID_HOST_NAME = "invalid_host_name";

    /**
     * @msg.message msg="Unable to set debugger on Route {0} in event process {1}"
     */
    public final static String SET_DEBUGGER_FAILURE = "set_debugger_failure";

    /**
     * @msg.message msg="Unable to remove debugger from Route {0} in event process {1}"
     */
    public final static String REMOVE_DEBUGGER_FAILURE = "remove_debugger_failure";

    /**
     * @msg.message msg="Event Process {0} is already in stopped state"
     */
    public final static String EVENT_PROCESS_NOT_FOUND = "event_process_not_found";

    /**
     * @msg.message msg="Ignoring call to kill Event Process {0} as it is not in running state"
     */
    public final static String IGNORE_EVENT_PROCESS_KILL_CALL = "ignore_event_process_kill_call";

    /**
     * @msg.message msg="Exception while launching components of application : {0}. Error Message : {1}"
     */
    public final static String ERR_LAUNCHING_COMPONENTS = "err_launching_components";

    /**
     * @msg.message msg="Unable to find service instance with the specified name :: {0}"
     */
    public final static String SERVICE_INSTANCE_NOT_FOUND = "service_instance_not_found";

    /**
     * @msg.message msg="Prepare Launch Error :: To be filled here ::"
     */
    public static String PREPARE_LAUNCH_ERROR = "prepare_launch_error";

    /**
     * @msg.message msg="Failed to start component :: {0} of event Process :: {1}"
     */
    public static String FAILED_TO_START_COMPONENT = "failed_to_start_component";

    /**
     * @msg.message msg="Failed to kill component {0} of event process {1}"
     */
    public static String FAILED_TO_KILL_COMPONENT = "failed_to_kill_component";

    /**
     * @msg.message msg="Failed to delete inport {0} of event process {1}"
     */
    public static String FAILED_TO_DELETE_INPORT = "failed_to_delete_inport";

    /**
     * @msg.message msg="Failed to kill component {0} of event process {1}. Reason : component handle was found as null."
     */
    public static String FAILED_TO_KILL_COMPONENT_NO_HANDLE = "failed_to_kill_component_no_handle";

    /**
     * @msg.message msg="Unable to register Mbean :: {0}"
     */
    public static String MBEAN_REGISTRATION_ERROR = "mbean_registration_error";


    /**
     * @msg.message msg="Unable to close connections for the component :: {0} in event process:: {1}"
     */
    public static String CONNECTION_CLOSE_ERROR = "connection_close_error";

    /**
     * @msg.message msg="Resource Connectivity Check for event process  :: {0}"
     */
    public static String INITIATING_PREPARE_LAUNCH = "initiating_prepare_launch";

    /**
     * @msg.message msg="Resources Connectivity check successful for Event process  :: {0}"
     */
    public static String ENDING_PREPARE_LAUNCH = "ending_prepare_launch";

    /**
     * @msg.message msg="Launching event process  :: {0}"
     */
    public static String INITIATING_EVENT_PROCESS_LAUNCH = "initiating_event_process_launch";

    /**
     * @msg.message msg="Event process  :: {0} launched"
     */
    public static String END_EVENT_PROCESS_LAUNCH = "end_event_process_launch";

    /**
     * @msg.message msg="Unable to launch on Operating System :: {0} as it is not supported for service {1}"
     */
    public static String OS_LAUNCH_ERROR = "os_launch_error";

    /**
     * @msg.message msg="Unable to launch as operating system is not supported for service {0} "
     */
    public static String LAUNCH_ERROR = "launch_error";

    /**
     * @msg.message msg="Updating route information for component  :: {0}"
     */
    public static String UPDATE_COMPONENT_ROUTE = "update_component_route";

    /**
     * @msg.message msg="Adding route {0} for component  :: {1}"
     */
    public static String ADD_COMPONENT_ROUTE = "add_component_route";

    /**
     * @msg.message msg="Updating route {0} target for setting debugger in appGUID {1}"
     */
    public static String UPDATE_ROUTE_DEBUGGER = "update_route_debugger";

    /**
     * @msg.message msg="Removing route {0} for component  :: {1} in event process {2}"
     */
    public static String REMOVE_COMPONENT_ROUTE = "remove_component_route";

    /**
     * @msg.message msg="Removing route {0} for component  :: {1} in event process {2}. Reason: {3}"
     */
    public static String REMOVE_COMPONENT_ROUTE_1 = "remove_component_route_1";

    /**
     * @msg.message msg="Removing routes for component  :: {0} in event process {1}"
     */
    public static String REMOVING_COMPONENT_ROUTES = "removing_component_routes";

    /**
     * @msg.message msg="Removed routes for component  :: {0} in event process {1}"
     */
    public static String REMOVED_COMPONENT_ROUTES = "removed_component_routes";

    /**
     * @msg.message msg="Synchronizing event process {0}"
     */
    public static String INITIATE_EVENT_PROCESS_SYNCHRONIZATION = "initiate_event_process_synchronization";

    /**
     * @msg.message msg="Synchronization successful for event process:: {0}"
     */
    public static String END_EVENT_PROCESS_SYNCHRONIZATION = "end_event_process_synchronization";

    /**
     * @msg.message msg="Exception while synchronizing Event Process :: {0}"
     */
    public static String SYNCHRONIZATION_EXCEPTION = "synchronization_exception";

    /**
     * @msg.message msg=" Exception while stopping component :: {0} for Event Process :: {1}"
     */
    public static String ERROR_STOP_COMPONENT = "error_stop_component";

    /**
     * @msg.message msg="Initiating launch for business component  :: {0} in the Event Process :: {1}"
     */
    public static String INITIATING_COMPONENT_LAUNCH = "initiating_component_launch";

    /**
     * @msg.message msg="Business component  :: {0} for the Event process :: {1} launched"
     */
    public static String END_COMPONENT_LAUNCH = "end_component_launch";

    /**
     * @msg.message msg="Unable to component  :: {0} in Event Process {1}"
     */
    public static String COMPONENT_KILL_ERROR = "component_kill_error";

    /**
     * @msg.message msg="Unable to component  :: {0} in Event Process {1}"
     */
    public static String COMPONENT_KILL_ERROR_LAUNCH_APP = "component_kill_error_launch_app";

    /**
     * @msg.message msg="Unable to stop Business component  :: {0} in Event Process {1}"
     */
    public static String COMPONENT_STOP_ERROR = "component_stop_error";

    /**
     * @msg.message msg="Binding connection factory :: {0} with URL :: {1}"
     */
    public static String BIND_CONNECTION_FACTORY = "bind_connection_factory";

    /**
     * @msg.message msg="Unable to bind {0} as :: {1} = {2} for service {3}"
     */
    public static String BIND_FAILURE = "bind_failure";

    /**
     * @msg.message msg="No manageable properties to bind {0} :: {1} = {2} for component {3}"
     */
    public static String NO_DATA_FOR_BOUND_OBJECT = "no_data_for_bound_object";

    /**
     * @msg.message msg="Bound {0} :: {1} = {2}"
     */
    public static String BIND_SUCCESS = "bind_success";

    /**
     * @msg.message msg="Bound Named Configuration for service instance {0}. Configuration bound: {1} = {2}"
     */
    public static String BIND_NAMED_CONFIGURATION_SUCCESS = "bind_named_configuration_success";

    /**
     * @msg.message msg="Launching component {0} in event process {1}"
     */
    public static String LAUNCH_COMPONENT = "launch_component";

    /**
     * @msg.message msg="Launching component {0} in event process {1} with system properties {0} and command line args {1}"
     */
    public static String LAUNCH_COMMAND = "launch_command";

    /**
     * @msg.message msg="Deleting Object {0} from naming repository"
     */
    public static String DELETE_OBJECT = "delete_object";

    /**
     * @msg.message msg="Object {0} already exists with type {1} in the naming server"
     */
    public static String OBJECT_ALREADY_EXISTS = "object_already_exists";

    /**
     * @msg.message msg="Exception in deleting Object {0} from naming repository"
     */
    public static String ERROR_DELETE_OBJECT = "error_delete_object";

    /**
     * @msg.message msg="Exception while adding Object {0} from naming repository"
     */
    public static String ERROR_ADD_OBJECT = "error_add_object";

    /**
     * @msg.message msg="Exception occurred while deleting {0}: {1}. Message is {2}"
     */
    public static String ERROR_DELETE_DESTINATION = "error_delete_destination";

    /**
     * @msg.message msg="Exception in deleting component {0} version {1} from local repository"
     */

    public static String ERROR_DELETE_COMPONENT ="error_delete_component";
    /**
     * @msg.message msg="Exception in fetching component {0} version {1} from FES repository"
     */
    public static String ERROR_FETCH_COMPONENT ="error_fetch_component";


    /**
     * @msg.message msg="Adding route :: {0} "
     */
    public static String ERROR_ADDING_ROUTE = "error_adding_route";

    /**
     * @msg.message msg="Creating route :: {0} in event process {1}"
     */
    public static String ROUTE_CREATION = "route_creation";

    /**
     * @msg.message msg="Created route :: {0} in event process {1}"
     */
    public static String ROUTE_CREATED = "route_created";

    /**
     * @msg.message msg="Applying xsl :: {0} on route :: {1} "
     */
    public static String ROUTE_XSL = "route_xsl";

    /**
     * @msg.message msg="Applying jms xsl :: {0} on route :: {1} "
     */
    public static String ROUTE_JMS_XSL = "route_jms_xsl";

    /**
     * @msg.message msg="TPS Information :: {0} "
     */
    public static String TPS_REPOSITORY_INFO = "tps_repository_info";

    /**
     * @msg.message msg="Unable to create service as event publisher is null"
     */
    public final static String EVENT_PUBLISHER_NULL = "event_publisher_null";

    /**
     * @msg.message msg="Added shutdown event listener"
     */
    public final static String ADDED_SHUTDOWN_HOOK = "added_shutdown_hook";

    /**
     * @msg.message msg="Failed to register shutdown listener"
     */
    public final static String SHUTDOWN_REGISTRATION_FAILURE = "shutdown_registration_failure";

    /**
     * @msg.message msg="Received container shutdown notification :: {0}"
     */
    public final static String SHUTDOWN_LISTENER_INVOKED = "shutdown_listener_invoked";

    /**
     * @msg.message msg="Exception while raising event "
     */
    public final static String ERROR_RAISING_EVENT = "error_raising_event";

    /**
     * @msg.message msg="Exception while invoking close connection for connection factory :: {0}"
     */
    public final static String ERROR_CLOSING_CONNECTION = "error_closing_connection";

    /**
     * @msg.message msg="Exception while stopping route :: {0}"
     */
    public final static String ERROR_STOPPING_ROUTE = "error_stopping_route";

    /**
     * @msg.message msg="Exception while removing debug route :: {0}"
     */
    public final static String ERROR_REMOVING_DEBUG_ROUTE = "error_removing_debug_route";

    /**
     * @msg.message msg="Exception while removing route :: {0} in event process {1}"
     */
    public final static String ERROR_REMOVING_ROUTE = "error_removing_route";

    /**
     * @msg.message msg="Exception occured while creating route :: {0} for service :: {1} in event process {2}"
     */
    public static String ERR_CREATING_CONNECTION = "err_creating_connection";

    /**
     * @msg.message msg="Exception {2} occured while trying to created route :: {0} in event process {1}, Retrying to create route again"
     */
    public static final String LEAST_LOADED_SERVER_CALCULATION_ERROR = "least_loaded_server_calculation_error";

    /**
     * @msg.message msg="Destination does not exist (could not look it up from MBeanServer) :: {0} "
     */
    public static String DESTINATION_DOES_NOT_EXIST = "destination_does_not_exist";

    /**
     * @msg.message msg="Error while looking up object from MBeanServer :: {0} "
     */
    public static String LOOKUP_ERROR = "lookup_error";

    /**
     * @msg.message msg="Enabled SBW on port :: {0} "
     */
    public static String ENABLED_SBW = "enabled_sbw";

    /**
     * @msg.message msg="Deregistered the server message consumer on destination :: {0} "
     */
    public static String DEREGISTERED_SRVR_MSG_CONSUMER = "deregistered_srvr_msg_consumer";

    /**
     * @msg.message msg="Failed to publish SBW event: document ID :: {0} "
     */
    public static String SBW_EVENT_PUBLISH_ERROR = "sbw_event_publish_error";

    /**
     * @msg.message msg="Successfully published SBW event: document ID :: {0} "
     */
    public static String PUBLISHED_SBW_EVENT = "published_sbw_event";

    /**
     * @msg.message msg="Error occurred while serializing message "
     */
    public static String ERROR_SERIALIZING_MSG = "error_serializing_msg";

    /**
     * @msg.message msg="Identified [{0}] as {1} port of service :: {2} "
     */
    public static String IDENTIFIED_IN_OUT_PORT = "identified_in_out_port";

    /**
     * @msg.message msg="Route {0} Exception {1} occured on JMS Route"
     */
    public static String EXCEPTION_ON_JMS_ROUTE = "exception_on_jms_route";

    /**
     * @msg.message msg="Route {0} Exception {1} occured on closing JMS Route"
     */
    public static String EXCEPTION_ON_CLOSING_JMS_ROUTE = "exception_on_closing_jms_route";

    /**
     * @msg.message msg="Route {0} :: is getting removed in event process {1}"
     */
    public static String REMOVED_OLD_ROUTE = "removed_old_route";

    /**
     * @msg.message msg="Route ::{0} in event process:: {1} is retained"
     */
    public static String EXISTING_ROUTE_IS_VALID = "existing_route_is_valid";

    /**
     * @msg.message msg="Checking route existence for route :: {3} having target source instance as :: {1} in the event process :: {0} on the peer server :: {2} "
     */
    public static String CHECKING_ROUTE_EXISTENCE = "checking_route_existence";

    /**
     * @msg.message msg="Failed to update service launch packet for service :: {0} in event process: {1}"
     */
    public static String SLP_UPDATE_FAILURE = "slp_update_failure";

    /**
     * @msg.message msg="Error while enabling SBW for port {0} of service instance {1} in event process {2} "
     */
    public static String ERROR_ENABLE_SBW = "error_enable_sbw";

    /**
     * @msg.message msg="Error while enabling message filter for port {0} of service instance {1} in event process {2} "
     */
    public static String ERROR_ENABLE_MESSGAE_FILTER = "error_enable_message_filter";

    /**
     * @msg.message msg="Unable to add port message listener as destination {0} does not exists"
     */
    public static String FAILED_TO_ADD_PORT_LISTENER = "failed_to_add_port_listener";

    /**
     * @msg.message msg="Failed to create route transformer for xsl = {0}"
     */
    public static String FAILED_TO_CREATE_ROUTE_TRANSFORMER = "failed_to_create_route_transformer";

    /**
     * @msg.message msg="JMS Destination specified for port {0} is null"
     */
    public static String JMS_DESTINATION_NULL = "jms_destination_null";

    /**
     * @msg.message msg="Failed to remove service {0}. Service not found in service repository"
     */
    public static String FAILED_TO_REMOVE_SERVICE_NOT_PRESENT = "failed_to_remove_service_not_present";

    /**
     * @msg.message msg="Route {0} in event process {1} does not exist"
     */
    public static String ROUTE_DOES_NOT_EXIST = "route_does_not_exist";

    /**
     * @msg.message msg="Error occurred while handling FPS launch event"
     */
    public static String ERR_HANDLING_SP_LAUNCH_EVENT = "err_handling_sp_launch_event";

    /**
     * @msg.message msg="Route {0} in event process {1} has been paused "
     */
    public static String PAUSE_ROUTE_LOG = "pause_route_log";

    /**
     * @msg.message msg="Target for Route {0} has been updated "
     */
    public static String UPDATE_ROUTE_TARGET = "update_route_target";

    /**
     * @msg.message msg="Failed to update Target for Route {0} "
     */
    public static String UPDATE_ROUTE_TARGET_FAILURE = "update_route_target_failure";

    /**
     * @msg.message msg="Route Info will be updated for port {0}"
     */
    public static String UPDATE_TARGET_FOR_PORT = "update_target_for_port";

    /**
     * @msg.message msg="New Transformation {0} applied on route {1}. Transformer type {2}"
     */
    public static String TRANSFORMATION_LOG = "transformation_log";

    /**
     * @msg.message msg="Error occurred while trying to set log level for service [{0}]"
     */
    public static String ERR_SETTING_LOGLEVEL = "err_setting_loglevel";

    /**
     * @msg.message msg="Unable to connect to client [{0}]. It seems client connection is not start"
     */
    public static String FAILED_TO_CONNECT_TO_CLIENT_LOGGER = "failed_to_connect_to_client_logger";

    /**
     * @msg.message msg="Exception occured while closing jmx connector for service instance {0} in event process {1}"
     */
    public static String FAILED_TO_CLOSE_JMX_CONNECTOR = "failed_to_close_jmx_connector";

    /**
     * @msg.message msg="Error parsing Profile XML"
     */
    public final static String PROFILE_PARSE_ERROR = "profile_parse_error";

    /**
     * @msg.message msg="Setting the Fiorano Logger Factory for the launcher of service :: {0} "
     */
    public final static String SETTING_LAUNCHER_LOGGER = "setting_launcher_logger";

    /**
     * @msg.message msg="Error occured in synchronizing route :: {0} in event process :: {1} "
     */
    public final static String ERROR_SYNCHRONIZING_ROUTE = "error_synchronizing_route";

    /**
     * @msg.message msg="Killing event process {0} as it is already launched"
     */
    public static String KILL_RUNNING_EVENT_PROCESS = "kill_running_event_process";

    /**
     * @msg.message msg="Initializing event process handle for event process : {0}"
     */
    public static String INITIALIZE_EVENT_PROCESS_HANDLE = "initialize_event_process_handle";

    /**
     * @msg.message msg="Event process {0} killed"
     */
    public static String EVENT_PROCESS_KILLED = "event_process_killed";

    /**
     * @msg.message msg="Killing service instance {0} in event process {1}"
     */
    public static String KILLING_COMPONENT = "killing_component";

    /**
     * @msg.message msg="Service instance {0} in event process {1} killed"
     */
    public static String COMPONENT_KILLED = "component_killed";

    /**
     * @msg.message msg="Starting service instance {0} in event process {1}"
     */
    public static String STARTING_COMPONENT = "starting_component";

    /**
     * @msg.message msg="Service instance {0} in event process {1} started"
     */
    public static String COMPONENT_STARTED = "component_started";

    /**
     * @msg.message msg="Service instance {0} in event process {1} is starting"
     */
    public static String COMPONENT_LAUNCHING = "component_launching";

    /**
     * @msg.message msg="Service instance {0} in event process {1} acknowledged the start command. It is in the process of starting up."
     */
    public static String COMPONENT_STARTING = "component_starting";

    /**
     * @msg.message msg="Service instance {0} in event process {1} is now connected to peer server."
     */
    public static String COMPONENT_CONNECTED = "component_connected";

    /**
     * @msg.message msg="Starting service instances in event process {0}"
     */
    public static String STARTING_COMPONENTS = "starting_components";

    /**
     * @msg.message msg="Service instances in event process {0} started"
     */
    public static String COMPONENTS_STARTED = "components_started";

    /**
     * @msg.message msg="Stopping service instance {0} in event process {1}"
     */
    public static String STOPPING_COMPONENT = "stopping_component";

    /**
     * @msg.message msg="Service instance {0} in event process {1} stopped"
     */
    public static String COMPONENT_STOPPED = "component_stopped";

    /**
     * @msg.message msg="Waiting for component {0} in event process {1} to shutdown."
     */
    public static String COMPONENT_STOP_WAIT = "component_stop_wait";

    /**
     * @msg.message msg="Wait for shutdown of component {0} in event process {1} is over."
     */
    public static String COMPONENT_STOP_WAIT_OVER = "component_stop_wait_over";

    /**
     * @msg.message msg="Component {0} in event process {1} did not stop gracefully within configured wait time. Starting with force shutdown."
     */
    public static String START_FORCE_SHUTDOWN = "start_force_shutdown";

    /**
     * @msg.message msg="Issuing force shutdown for component {0} in event process {1}. Attempt : {2}"
     */
    public static String FORCE_SHUTDOWN_ATTEMPT = "force_shutdown_attempt";

    /**
     * @msg.message msg="Cleaning up server-side resources used by the component {0} in event process {1}."
     */
    public static String COMPONENT_RESOURCE_CLEANUP = "component_resource_cleanup";

    /**
     * @msg.message msg="Service instance {0} in event process {1} sent stopped event. All resources used by the component have been closed. Component will now proceed to close its connection with peer server."
     */
    public static String COMPONENT_STOPPED_EVENT = "component_stopped_event";

    /**
     * @msg.message msg="Component process object for component {0} in event process {1} is null. Component state is unknown"
     */
    public static String COMPONENT_PROCESS_NULL = "component_process_null";

    /**
     * @msg.message msg="Component {0} in event process {1} could not be shutdown within configured interval. Ignoring the component"
     */
    public static String COMPONENT_SHUTDOWN_FAILED = "component_shutdown_failed";

    /**
     * @msg.message msg="Service instance {0} in event process {1} acknowledged the shutdown command. It is in the process of shutdown."
     */
    public static String COMPONENT_STOP_TIME_WAIT = "component_stop_time_wait";

    /**
     * @msg.message msg="Service instance {0} in event process {1} has cleaned up all the resources/external connections used."
     */
    public static String COMPONENT_STOP_CLEANUP = "component_stop_cleanup";

    /**
     * @msg.message msg="Service instance {0} in event process {1} will now close its connection to peer server."
     */
    public static String COMPONENT_DISCONNECTING = "component_disconnecting";

    /**
     * @msg.message msg="Service instance {0} in event process {1} is in the process of shutdown. Try re-launching the component after some time."
     */
    public static String CCP_COMPONENT_SHUTDOWN_IN_PROGRESS = "ccp_component_shutdown_in_progress";

    /**
     * @msg.message msg="Stopping service instances in event process {0}"
     */
    public static String STOPPING_COMPONENTS = "stopping_components";

    /**
     * @msg.message msg="Service instances in event process {0} stopped"
     */
    public static String COMPONENTS_STOPPED = "components_stopped";

    /**
     * @msg.message msg="Setting debugger on route {0} in event process {1}"
     */
    public static String SETTING_DEBUGGER = "setting_debugger";

    /**
     * @msg.message msg="Setting debugger on route {0} in event process {1} successful"
     */
    public static String SET_DEBUGGER = "set_debugger";

    /**
     * @msg.message msg="Removing debugger on route {0} in event process {1}"
     */
    public static String REMOVING_DEBUGGER = "removing_debugger";

    /**
     * @msg.message msg="Debugger on route {0} in event process {1} removed"
     */
    public static String DEBUGGER_REMOVED = "debugger_removed";

    /**
     * @msg.message msg=" Removing all installed services from peer server : {0}"
     */
    public final static String REMOVING_ALL_INSTALLED_SERVICES = "removing_all_installed_services";

    /**
     * @msg.message msg=" Removed all installed services from peer server : {0}"
     */
    public final static String REMOVED_ALL_INSTALLED_SERVICES = "removed_all_installed_services";

    /**
     * @msg.message msg=" Removing installed service : {0} from peer server : {1}"
     */
    public final static String REMOVING_INSTALLED_SERVICE = "removing_installed_service";

    /**
     * @msg.message msg=" Removed installed service : {0} from peer server : {1}"
     */
    public final static String REMOVED_INSTALLED_SERVICE = "removed_installed_service";

    /**
     * @msg.message msg=" Deleting component {0} version {1} from peer server : {2}"
     */
    public final static String DELETE_COMPONENT = "delete_component";

    /**
     * @msg.message msg=" Deleted component {0} version {1} from peer server : {2}"
     */
    public final static String DELETED_COMPONENT = "deleted_component";

    /**
     * @msg.message msg=" Fetching component {0} version {1} from component repository"
     */
    public final static String FETCHING_COMPONENT = "fetching_component";

    /**
     * @msg.message msg="Killing discontinued services of event process : {0}"
     */
    public final static String KILL_DISCONTINUED_SERVICES = "kill_discontinued_services";

    /**
     * @msg.message msg="Launching services in event process {0}"
     */
    public final static String LAUNCHING_SERVICES = "launching_services";

    /**
     * @msg.message msg="Updating routes in event process {0}"
     */
    public final static String UPDATING_ROUTES = "updating_routes";

    /**
     * @msg.message msg="Setting log level for event process {0}"
     */
    public final static String SETTING_LOG_LEVEL = "setting_log_level";

    /**
     * @msg.message msg="Log level set for event process {0}"
     */
    public final static String LOG_LEVEL_SET = "log_level_set";

    /**
     * @msg.message msg="Setting log level for service instance {0}"
     */
    public final static String SETTING_LOG_LEVEL_COMPONENT = "setting_log_level_component";

    /**
     * @msg.message msg="Log level set for service instance {0}"
     */
    public final static String LOG_LEVEL_SET_COMPONENT = "log_level_set_component";

    /**
     * @msg.message msg="Updating process logger for service instance {0} in event process {1}"
     */
    public final static String PROCESS_LOGGER = "process_logger";

    /**
     * @msg.message msg="Setting loggers for various modules of service instance {0} in event process {1}"
     */
    public final static String SET_MODULE_LOGGER = "set_module_logger";

    /**
     * @msg.message msg="Enabling state based workflow for {0} port of service instance {1} in event process {2}"
     */
    public final static String ENABLE_SBW = "enable_sbw";

    /**
     * @msg.message msg="Disabling state based workflow for {0} port of service instance {1} in event process {2}"
     */
    public final static String DISABLE_SBW = "disable_sbw";

    /**
     * @msg.message msg="Enabling message filter for {0} port of service instance {1} in event process {2}"
     */
    public final static String ENABLE_MESSAGE_FILTER = "enable_message_filter";

    /**
     * @msg.message msg="Disabling message filter for {0} port of service instance {1} in event process {2}"
     */
    public final static String DISABLE_MESSAGE_FILTER = "disable_message_filter";

    /**
     * @msg.message msg="Destroying process for service instance {0} in event process {1}"
     */
    public static String DESTROY_PROCESS = "destroy_process";

    /**
     * @msg.message msg="Closing JMX Connector for service instance {0} in event process {1}"
     */
    public static String CLOSING_JMX_CONNECTOR = "closing_jmx_connector";

    /**
     * @msg.message msg="Deleting JMX destinations created for service instance {0} in event process {1}"
     */
    public static String DELETE_DESTINATIONS = "delete_destinations";

    /**
     * @msg.message msg="Creating system properties for launching service instance {0} in event process {1}"
     */
    public static String CREATE_SYSTEM_PROPS = "create_system_props";

    /**
     * @msg.message msg="Creating command line arguments for launching service instance {0} in event process {1}"
     */
    public static String CREATE_COMMAND_LINE_ARGS = "create_command_line_args";

    /**
     * @msg.message msg="Binding Resources for service instance {0} in event process {1}"
     */
    public static String BIND_RESOURCES = "bind_resources";

    /**
     * @msg.message msg="Closing route {0}"
     */
    public static String CLOSING_ROUTE = "closing_route";

    /**
     * @msg.message msg="Closed route {0}"
     */
    public static String CLOSED_ROUTE = "closed_route";

    /**
     * @msg.message msg="MBean Server connection created for changing log level of component {0} in appGUID {1}"
     */
    public static String MBEAN_SERVER_CONNECTION_CREATED = "mbean_server_connection_created";

    /**
     * @msg.message msg="Error while registering for queue depth notifications in Queue subsystem"
     */
    public static String ERROR_REGISTERING_QUEUE_DEPTH_NOTIFICATIONS = "error_registering_queue_depth_notifications";

    /**
     * @msg.message msg="Event Process {0} not running. Cannot apply policy {1}. The policy will be re-applied automatically whenever the event process is launched."
     */
    public static String EVENT_PROCESS_NOT_RUNNING = "event_process_not_running";

    /**
     * @msg.message msg="Could not apply Policy {1}. Depth monitoring not enabled on Peer {0}."
     */
    public static String DEPTH_MONITORING_NOT_ENABLED = "depth_monitoring_not_enabled";

    /**
     * @msg.message msg="Policy {0} could not be suspended, because it was not applied in the first place."
     */
    public static String POLICY_NOT_APPLIED = "policy_not_applied";

    /**
     * @msg.message msg="Doc Tracking enabled at Peer Server Level"
     */
    public static String ENABLE_DOC_TRACKING = "enable_doc_tracking";

    /**
     * @msg.message msg="Doc Tracking disabled at Peer Server Level"
     */
    public static String DISABLE_DOC_TRACKING = "disable_doc_tracking";

    /**
     * @msg.message msg="Callout enabled at Peer Server Level"
     */
    public static String ENABLE_CALLOUT = "enable_callout";

    /**
     * @msg.message msg="Callout disabled at Peer Server Level"
     */
    public static String DISABLE_CALLOUT = "disable_callout";

    /**
     * @msg.message msg="Could not delete Route :{0} ,as the route has  already been closed.[Route could have been closed via JMX action.]"
     */
    String ROUTE_ALREADY_CLOSED="route_already_closed";

    /**
     * @msg.message msg="Error occured while starting component {0} of event process {1}. Also, error occured while generating event for the same."
     */
    String FAILED_TO_GENERATE_START_EVENT="failed_to_generate_start_event";
    /**
     * @msg.message msg="Error occured while stopping component {0} of event process {1}. Also, error occured while generating event for the same."
     */
    String FAILED_TO_GENERATE_STOP_EVENT="failed_to_generate_stop_event";

    /**
     * @msg.message msg="Component stop wait thread for component {0} of event process {1} interrupted while waiting for the process to terminate."
     */
    String COMPONENT_STOP_WAIT_THREAD_INTERRUPTED = "component_stop_wait_thread_interrupted";

    /**
     * @msg.message msg="Component inmemory force shutdown thread for component {0} of event process {1} interrupted while waiting for the process to terminate."
     */
    String COMPONENT_INMEMORY_FORCE_SHUTDOWN_THREAD_INTERRUPTED = "component_inmemory_force_shutdown_thread_interrupted";

    /**
     * @msg.message msg="Component start command discarded for Component :: {0} of Event Process :: {1} as Service handle is already bound"
     */
    String SERVICE_HANDLE_ALREADY_BOUND="service_handle_already_bound";

    /**
     * @msg.message msg="Component start command discarded for Component :: {0} of Event Process :: {1} as the component process is already running"
     */
    String COMPONENT_PROCESS_ALREADY_RUNNING="component_process_already_running";

    /**
     * @msg.message msg="Error handling backlog monitor event from Port Name {0}"
     */
    String ERROR_HANDLING_BACKLOG_EVENT = "error_handling_backlog_event";

    /**
     * @msg.message msg="JMX Queue: {0} & Component were destroyed simultaneously."
     */
    String SIMULTANEOUS_DESTROY="simultaneous_destroy";

    /**
     * @msg.message msg="Not overriding the user specified custom endorsed libraries. Specified JVM Params : {0}"
     */
    String CUSTOM_ENDORSED_LIBRARIES = "custom_endorsed_libraries";

    /**
     * @msg.message msg="Could not launch Application {0}. The Application was found running already."
     */
    String EVENT_PROCESS_ALREADY_RUNNING = "event_process_already_running";

    /**
     * @msg.message msg="Could not get Deliverable Count for destination {0}."
     */
    String ERROR_GET_DELIVERABLE_COUNT = "error_get_deliverable_count";

    /**
     * @msg.message msg="There are {0} manageable properties found for {2}. {1}."
     */
    String MANAGEABLE_PROPERTIES = "manageable_properties";


    /**
     * @msg.message msg="Could not handle message on port {0} for destination name {1}. Error Occured."
     */
    String ERROR_HANDLING_MESSAGE = "error_handling_message";

    /**
     * @msg.message msg="Could not clone message on port {0}. Error Occured."
     */
    String ERROR_CLONING_MESSAGE = "error_cloning_message";

    /**
     * @msg.message msg="Could not disable encryption on port {0}. Error Occured."
     */
    String ERROR_DISABLE_ENCRYPTION = "error_disable_encryption";

    /**
     * @msg.message msg="It is been found that there is anamoly between the type of destination required and found. Destination: {0}. Required Type {1}. Found {2}"
     */
    String DESTINATION_TYPE_ANAMOLY = "destination_type_anamoly";

    /**
     * @msg.message msg="Component shutdown process already initiated. Cannot issue another shutdown to component {1} in Event Process {0}."
     */
    String COMPONENT_KILL_INPROGRESS = "component_kill_inprogress";

    /**
     * @msg.message msg="Unable to change Route Transformation"
     */
    String ERR_CHANGING_ROUTE_TRANSFORMATION = "err_changing_route_transformation";

    /**
     * @msg.message msg="Route transformation on route {0} can not be changed as this route does not exist."
     */
    String ROUTE_TRANSFORMATION_CAN_NOT_BE_CHANGED = "route_transformation_can_not_be_changed";

    /**
     * @msg.message msg="Route selector on route {0} can not be set as this route does not exist."
     */
    String ROUTE_SELECTOR_CAN_NOT_BE_SET = "route_selector_can_not_be_set";

    /**
     * @msg.message msg="Unable to add permissions on destination {0} for user {1}."
     */
    String ERR_ADDING_DESTINATION_PERMISSIONS = "err_adding_destination_permissions";

    /**
     * @msg.message msg="Invalid JMS selector."
     */
    String INVALID_JMS_SELECTOR = "invalid_jms_selector";

    /**
     * @msg.message msg="Level {0} is already configured for policy : {1}."
     */
    String LEVEL_ALREADY_CONFIGURED = "level_already_configured";

    /**
     * @msg.message msg="Level {0} successfully configured for policy : {1}."
     */
    String LEVEL_SUCCESSFULLY_CONFIGURED = "level_successfully_configured";

    /**
     * @msg.message msg="Level {0} successfully removed for policy : {1}."
     */
    String LEVEL_SUCCESSFULLY_REMOVED = "level_successfully_removed";

    /**
     * @msg.message msg="Level {0} was not configured for policy : {1}."
     */
    String LEVEL_NOT_CONFIGURED = "level_not_configured";

    /**
     * @msg.message msg="Policy not set on port as it is a topic."
     */
    String POLICY_NOT_SET_ON_TOPIC = "policy_not_set_on_topic";

    /**
     * @msg.message msg="Backlog Monitoring can not be enabled on a topic."
     */
    String POLICY_CAN_NOT_BE_APPLIED_ON_TOPIC = "policy_can_not_be_applied_on_topic";

    /**
     * @msg.message msg="Remove Debugger {0} from Event Process : {1}. Maintaing sequence. Pausing route."
     */
    String DEBUGGER_REMOVAL_IN_SEQUENCE = "debugger_removal_in_sequence";

    /**
     * @msg.message msg="Remove Debugger {0} from Event Process : {1}. Sequence Not being maintained. Updating target info."
     */
    String DEBUGGER_REMOVAL_SEQUENCE_IMMATERIAL = "debugger_removal_sequence_immaterial";

    /**
     * @msg.message msg="Error Launching Component {0}:{3} in Phase {1}. Error Message: {2}."
     */
    String ERROR_LAUNCH_COMPONENT = "error_launch_component";

    /**
     * @msg.message msg="Warning Raised while Launching Component {0}:{3} in Phase {1}. Warning Message: {2}. Continuing with component launch"
     */
    String WARNING_LAUNCH_COMPONENT = "warning_launch_component";

    /**
     * @msg.message msg="Component {1} of Application {2} logged the state {0} while launching."
     */
    String LAUNCH_PROCESS_UPDATE = "launch_process_update";

    /**
     * @msg.message msg="Error Stopping Component {0}:{3} in Phase {1}. Error Message: {2}."
     */
    String ERROR_STOPPING_COMPONENT = "error_stopping_component";

    /**
     * @msg.message msg="Warning Raised while Stopping Component {0}:{3} in Phase {1}. Warning Message: {2}. Continuing with component Kill"
     */
    String WARN_STOPPING_COMPONENT = "warn_stopping_component";

    /**
     * @msg.message msg="Component {1} of Application {2} logged the state {0} while stopping."
     */
    String STOP_COMPONENT_UPDATE = "stop_component_update";

    /**
     * @msg.message msg="Exception occurred while setting setting queue configuration values on queue {0}."
     */
    String ERROR_SETTING_QUEUE_CONFIGURATIONS = "error_setting_queue_configurations";

    /**
     * @msg.message msg="Event process {0} cannot be removed as its service component: {1} is still running."
     */
    String EVENT_PROCESS_ILLEGAL_REMOVAL ="event_process_illegal_removal";

    /**
     * @msg.message msg="Service Handle created for service: {0} of Application {1}."
     */
    String SERVICE_HANDLE_CREATED="service_handle_created";
    /**
     * @msg.message msg="No suitable Launcher found for component with the configured options."
     */
    String NO_LAUNCHER_FOUND="no_launcher_found";
    /**
     * @msg.message msg="Service kill failure on FPS: {0}."
     */
    String SERVICE_KILL_FAILURE="service_kill_failure";

    /**
     * @msg.message msg="Service launch failure on FPS: {0}."
     */
    String SERVICE_LAUNCH_FAILURE="service_launch_failure";

    /**
     * @msg.message msg="Components failed to start with in the specified timeout on FPS: {0}."
     */
    String SERVICE_TENTATIVE_LAUNCH_FAILURE="service_tentative_launch_failure";
    /**

     /**
     * @msg.message msg="Service {0} has been bound to peer {1}."
     */
    String SERVICE_BOUND="service_bound";

    /**
     * @msg.message msg="Service {0} is bounding to peer {1}."
     */
    String SERVICE_BOUNDING = "service_bounding";

    /**
     * @msg.message msg="Service {0} of Application {2} running on peer {1} has been Stoped."
     */
    String SERVICE_UNBOUND1="service_unbound1";
    /**
     * @msg.message msg="Service {0} of Application {2} running on peer {1} has been Stoped :: Reason:: {3}."
     */
    String SERVICE_UNBOUND2="service_unbound2";
    /**
     * @msg.message msg="Destination {0} does not exist."
     */
    String NO_SUCH_DESTINATION="no_such_destination";

    /**
     * @msg.message msg="Policy having id: {0} was not set on port: {1}."
     */
    String NO_POLICY_SET_ON_PORT="no_policy_set_on_port";

    /**
     * @msg.message msg="polict {0} cant be applied as port : {1} is not present"
     */
    String PORT_NOT_PRESENT_FOR_POLICY="port_not_present_for_policy";

    /**
     * @msg.message msg="Failed to generate application event"
     */
    String FAILED_TO_GENERATE_APPLICATION_EVENT="failed_to_generate_application_event";

    /**
     * @msg.message msg="Application {0} was killed."
     */
    String APP_KILLED="app_killed";

    /**
     * @msg.message msg="Error in startup of event consumer."
     */
    String ERROR_START_UP_CONSUMER="error_start_up_consumer";

    /**
     * @msg.message msg="Error registering consumer {0} to listener {1}."
     */
    String ERROR_REGISTER_CONSUMER ="error_register_consumer";
    /**
     * @msg.message msg="Error unregistering listeners for connection ID & Close Events."
     */
    String ERROR_UNREGISTER_LISTENERS="error_unregister_listeners";
    /**
     * @msg.message msg="Error registering listeners for connection ID & Close Events."
     */
    String ERROR_REGISTER_LISTENERS="error_register_listeners";

    /**
     * @msg.message msg="Peer could not handle FES Launch!"
     */
    String UNABLE_TO_HANDLE_FES_LAUNCH="unable_to_handle_fes_launch";

    /**
     * @msg.message msg="Invalid Event."
     */
    String INVALID_EVENT="invalid_event";

    /**
     * @msg.message msg="Document reached port : {0} of component: {1} on Date: {2}."
     */
    String DOC_REACHED_PORT="doc_reached_port";

    /**
     * @msg.message msg="Document was sent from port : {0} of component: {1} on Date: {2}."
     */
    String DOC_SENT="doc_sent";

    /**
     * @msg.message msg="Document Message Type :{0} is not supported!"
     */
    String DOCUMENT_MESSAGE_UNSUPPORTED="document_message_unsupported";

    /**
     * @msg.message msg="Fiorano Server Application Information."
     */
    String APP_INFO_MSG="app_info_msg";

    /**
     * @msg.message msg="Component Control Protocol(CCP) Initiating"
     */
    String INIT_CCP="init_ccp";

    /**
     * @msg.message msg="Error setting SSL parameters"
     */
    String ERROR_SET_SSL_PARAMS = "error_set_ssl_params";

    /**
     * @msg.message msg="Error occurred while undeploying webservice in application {0} for service instance {1}."
     */
    String ERROR_UNDEPLOYING_WEBSERVICE = "error_undeploying_webservice";

    /**
     * @msg.message msg="Error occurred while undeploying webservice in application {0} for service instance {1} while shutting down server. Reason: Connection refused by Jetty Server. Jetty Server might already have stopped."
     */
    String ERROR_UNDEPLOYING_WEBSERVICE_CONNECTION_REFUSED = "error_undeploying_webservice_connection_refused";

    /**
     * @msg.message msg="Error occurred while retrieving config object for jetty service. Config object may not be registered yet with MBean Server."
     */
    String ERROR_GETTING_JETTY_CONFIG_OBJECT_INSTANCE = "error_getting_jetty_config_object_instance";

    /**
     * @msg.message msg="Error occurred while parsing WS Stub configurations in application {0} for service instance {1}."
     */
    String ERROR_PARSING_WSSTUB_CONFIGURATION = "error_parsing_wsstub_configuration";

    /**
     * @msg.message msg="Event Process handle for Application {0} removed from peer {1}. Reason : {2}"
     */
    String APP_KILLED_HANDLE_REMOVED = "app_killed_handle_removed";

    /**
     * @msg.message msg="Launch Type None"
     */
    String LAUNCH_TYPE_NONE= "launch_type_none";

    /**
     * @msg.message msg="Request to get {2} from component {1} in Event Process {0} timed out"
     */
    String CCP_DATA_REQUEST_TIMEOUT = "ccp_data_request_timeout";

    /**
     * @msg.message msg="Data({2}) received from component {1} in Event Process {0} was found as null"
     */
    String CCP_DATA_NULL = "ccp_data_null";

    /**
     * @msg.message msg="Correlation id for the data({2}) received from component {1} in Event Process {0} doesn''t match with request id"
     */
    String CCP_DATA_ID_MISMATCH = "ccp_data_id_mismatch";

    /**
     * @msg.message msg="Component {1} in Event Process {0} does not support CCP (Component Control Protocol)."
     */
    String CCP_NOT_SUPPORTED = "ccp_not_supported";

    /**
     * @msg.message msg="Component {1} in Event Process {0} is not running."
     */
    String COMPONENT_NOT_RUNNING = "component_not_running";

    /**
     * @msg.message msg="Component {1} in Event Process {0} is running in-memory. It''s memory usage/Process ID can not be determined explicitly."
     */
    String INVALID_LAUNCH_TYPE = "invalid_launch_type";

    /**
     * @msg.message msg="Error occurred while parsing REST Stub configurations in application {0} for service instance {1}."
     */
    String ERROR_PARSING_RESTSTUB_CONFIGURATION = "error_parsing_reststub_configuration";

    /**
     * @msg.message msg="Error occurred while parsing Http Stub configurations in application {0} for service instance {1}."
     */
    String ERROR_PARSING_HTTPSTUB_CONFIGURATION = "error_parsing_httpstub_configuration";

    /**
     * @msg.message msg="Error occurred while undeploying RESTful service in application {0} for service instance {1}."
     */
    String ERROR_UNDEPLOYING_RESTSERVICE = "error_undeploying_restservice";

    /**
     * @msg.message msg="Error occurred while undeploying Http service in application {0} for service instance {1}."
     */
    String ERROR_UNDEPLOYING_HTTPSERVICE = "error_undeploying_httpservice";

    /**
     * @msg.message msg="Failed to Bind ConnectionFactory {0}. Reason : {1}."
     */
    String CF_BIND_FAILURE = "cf_bind_failure";

    /**
     * @msg.message msg="A route {0} already exists between these destinations. Multiple routes between same pair of destinations are not allowed."
     */
    String MULTIPLE_ROUTES_NOT_ALLOWED = "multiple_routes_not_allowed";

    /**
     * @msg.message msg="Unable to validate license [{0};{1}]. Reason: {2} Exception: {3}"
     */
    public final static String VALIDATE_LICENSE_FAIL = "validate_license_fail";

    /**
     * @msg.message msg="License Warning :: {0}"
     */
    public final static String LICENSE_WARNING = "license_warning";


    /**
     * @msg.message msg="Error License Expired for component :: {0}__{1}, please contact Fiorano for help. Exception: {1}"
     */
    public static final String LICENSE_EXPIRED = "license_expired";

    /**
     * @msg.message msg="Error occured while binding resources to admin service as the server is in invalid state. Retrying until server comes to valid state."
     */
    String ERROR_INVALID_REPLICATION_STATE = "error_invalid_repliaction_state";
    /**
     * @msg.message msg="Error occurred while validating route {0} in application {1}."
     */
    java.lang.String ERROR_WHILE_VALIDATING_ROUTE = "error_while_validating_route";
    /**
     * @msg.message msg="Error occurred while updating target of route {0} in application {1}."
     */
    java.lang.String ERROR_WHILE_UPDATING_ROUTE_TARGET = "error_while_updating_route_target";
    /**
     * @msg.message msg="Pausing the route {0} in application {1} since route is in invalid state."
     */
    java.lang.String INFO_PAUSING_ROUTE_SINCE_INVALID = "info_pausing_route_since_invalid";
    /**
     * @msg.message msg="Error occurred while launching the component {1} in event process {0}."
     */
    java.lang.String ERR_LAUNCHING_COMPONENT = "err_launching_component";

    /**
     * @msg.message msg="Command to launch the process is null."
     */
    public final static String LAUNCH_COMMAND_IS_NULL = "launch_command_is_null";

    /**
     * @msg.message msg="Unable to launch process as process is launched already."
     */
    public final static String PROCESS_IS_LAUNCHED_ALREADY = "process_is_launched_already";

    /**
     * @msg.message msg="Exception occured while launching process."
     */
    public final static String PROCESS_LAUNCH_FAILED = "process_launch_failed";

    /**
     * @msg.message msg="Exception occured while launching the process in-memory."
     */
    public final static String IN_MEMORY_LAUNCH_FAILED = "in_memory_launch_failed";

    /**
     * @msg.message msg="Exception occured while stopping the in-memory process."
     */
    public final static String IN_MEMORY_PROCESS_SHUTDOWN_FAILED = "in_memory_process_shutdown_failed";

    /**
     * @msg.message msg="Components InMemoryLaunchImpl class does not implement InMemoryLaunchable Interface"
     */
    public final static String COMPONENT_IMPL_INVALID = "component_impl_invalid";

    /**
     * @msg.message msg="Components InMemoryLaunchImpl property is empty and the component is configured for in-memory launch"
     */
    public final static String COMPONENT_IMPL_NOT_PRESENT = "component_impl_not_present";

    /**
     * @msg.message msg="Unable to de-register Mbean :: {0}"
     */
    public static String MBEAN_DEREGISTRATION_ERROR = "mbean_deregistration_error";

    /**
     * @msg.message msg="Creating task for shutting down component : {0} in Event Process : {1}"
     */
    public static String CREATE_COMPONENT_SHUTDOWN_TASK = "create_component_shutdown_task";

    /**
     * @msg.message msg="Trying to shutdown component : {0} in Event Process : {1}. Attempt : {2}"
     */
    public static String COMPONENT_SHUTDOWN_ATTEMPT = "component_shutdown_attempt";

    /**
     * @msg.message msg="Component : {0} in Event Process : {1} was successfully shutdown"
     */
    public static String COMPONENT_SHUTDOWN_SUCCESS = "component_shutdown_success";

    /**
     * @msg.message msg="Could not shutdown component : {0} in EventProcess : {1}"
     */
    public static String COMPONENT_SHUTDOWN_FAILURE = "component_shutdown_failure";
}
