/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

public interface Bundle {
    /**
     * @msg.message msg="Failed to export RMI managers."
     */
    public final static String FAILED_TO_EXPORT_RMI_MANAGERS = "failed_to_export_rmi_managers";

    /**
     * @msg.message msg="Server was unable to get the routes for Application {0}:{1}. Reason: {2}"
     */
    public final static String UNABLE_TO_GET_ROUTES_FOR_APPLICATION = "unable_to_get_routes_for_application";

    /**
     * @msg.message msg="Server was unable to get the ports for Application {0}:{1}."
     */
    public final static String UNABLE_TO_GET_PORTS_FOR_APPLICATION = "unable_to_get_ports_for_application";

    /**
     * @msg.message msg="Server was unable to get the ports for service instance {0} in application {1}:{2}. Reason: {3}"
     */
    public final static String UNABLE_TO_GET_PORTS_FOR_SERVICE_INSTANCE= "unable_to_get_ports_for_service_instance";

    /**
     * @msg.message msg="Server was unable to get the service instances for Application {0}:{1}. Reason: {2}"
     */
    public final static String UNABLE_TO_GET_SERVICE_INSTANCES_FOR_APPLICATION = "unable_to_get_service_instances_for_application";

    /**
     * @msg.message msg="Server was unable to bind the RMI stub for RMI manager."
     */
    public final static String UNABLE_TO_BIND_RMI_STUB_FOR_RMI_MANAGER = "unable_to_bind_rmi_stub_for_rmi_manager";

    /**
     * @msg.message msg="RMI stub is already bound."
     */
    public final static String RMI_MANAGER_STUB_ALREADY_BOUND = "rmi_manager_stub_already_bound";

    /**
     * @msg.message msg="Server was unable to unbind the RMI stub for IService manager."
     */
    public final static String UNABLE_TO_UNBIND_RMI_STUB_FOR_ISERVICE_MANAGER = "unable_to_unbind_rmi_stub_for_iservice_manager";

    /**
     * @msg.message msg="Error in Fetching the Event process {0}:{1} from repository."
     */
    public final static String ERROR_FETCHING_EVENT_PROCESS_FROM_REPOSITORY = "error_fetching_event_process_from_repository";

    /**
     * @msg.message msg="Error in sending the Zip Contents of application {0}:{1} to client."
     */
    public final static String ERROR_SENDING_CONTENTS_OF_APP_ZIPFILE = "error_sending_contents_of_app_zipfile";

    /**
     * @msg.message msg="Error in sending the Zip Contents of logs of {0} : {1} to client."
     */
    String ERROR_SENDING_CONTENTS_OF_LOGS = "error_sending_contents_of_logs";

    /**
     * @msg.message msg="Unable to create Application Zip file, to deploy event process."
     */
    public final static String UNABLE_TO_CREATE_APP_ZIPFILE = "unable_to_create_app_zipfile";

    /**
     * @msg.message msg="Error exporting Service. Service {0} with version {1} does not exist in the repository."
     */
    public final static String SERVICE_DOESNT_EXIST_IN_REPO = "service_doesnt_exist_in_repo";

    /**
     * @msg.message msg="Unable to export service {0} with version {1} from the repository."
     */
    public final static String UNABLE_TO_EXPORT_SERVICE = "unable_to_export_service";

    /**
     * @msg.message msg="Failed to find Service Descriptor files in the zip. Unable to import services. Check server logs for more info"
     */
    public final static String SERVICE_DESCRIPTOR_NOT_FOUND = "service_descriptor_not_found";

    /**
     * @msg.message msg="Failed to import Service. Check server logs for more info"
     */
    public final static String FAILED_TO_IMPORT_SERVICE = "failed_to_import_service";

    /**
     * @msg.message msg="Failed to find Service Descriptor files in zip file location : {0}. Unable to import services."
     */
    public final static String SERVICE_DESCRIPTOR_NOT_FOUND_FILE = "service_descriptor_not_found_file";

    /**
     * @msg.message msg="Unable to create Schema reference Zip file, to deploy schema references."
     */
    public final static String UNABLE_TO_CREATE_SCHEMA_REFERENCE_ZIPFILE = "unable_to_create_schema_reference_zipfile";
    /**
     * @msg.message msg="Unable to create Service Zip file, to deploy a service"
     */
    public final static String UNABLE_TO_CREATE_SERVICE_ZIPFILE = "unable_to_create_service_zipfile";

    /**
     * @msg.message msg="Unable to create Service Zip file, to deploy a service initiated by User : {0} using client : {1} with IP : {2}"
     */
    public final static String UNABLE_TO_CREATE_SERVICE_ZIPFILE_IP = "unable_to_create_service_zipfile_ip";

    /**
     * @msg.message msg="Create Service Zip file to deploy a service, initiated by User :{0} using client : {1} with IP : {2}."
     */
    public final static String CREATE_SERVICE_ZIPFILE = "create_service_zipfile";

    /**
     * @msg.message msg="Extract Service Zip file to deploy a service, initiated by User :{0} using client : {1} with IP : {2}."
     */
    public final static String EXTRACT_SERVICE_ZIPFILE = "extract_service_zipfile";

    /**
     * @msg.message msg="Save Service to repository, initiated by User :{0} using client : {1} with IP : {2}."
     */
    public final static String SAVE_SERVICE_TO_REPO = "save_service_to_repo";

    /**
     * @msg.message msg="Unable to create Service Zip file, to deploy multiple services"
     */
    public final static String UNABLE_TO_CREATE_MULTIPLE_SERVICE_ZIPFILE = "unable_to_create_multiple_service_zipfile";

    /**
     * @msg.message msg="Unable to create Zip file for service {0}, to export it to client. {1}"
     */
    public final static String UNABLE_TO_CREATE_ZIPFILE_GET_SERVICE = "unable_to_create_zipfile_get_service";

    /**
     * @msg.message msg="Unable to create Zip file for service {0}, to export initiated by user : {1} using client : {2} with IP : {3}"
     */
    public final static String UNABLE_TO_CREATE_ZIPFILE_GET_SERVICE_IP = "unable_to_create_zipfile_get_service_ip";

    /**
     * @msg.message msg="Unable to update service in the repository, as service {0}:{1} already exists and overwrite flag is set to false."
     */
    public final static String SERVICE_ALREADY_EXISTS_OVERWRITE_FALSE = "service_already_exists_overwrite_false";

    /**
     * @msg.message msg="Unable to create Zip file for multiple services to export them to client."
     */
    public final static String UNABLE_TO_CREATE_ZIPFILE_FOR_MULTIPLE_SERVICES = "unable_to_create_zipfile_for_multiple_services";

    /**
     * @msg.message msg="Unable to save event flow process : Error occured while extracting zipped contents."
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_EVENT_FLOW_PROCESS = "error_extracting_zipfile_unable_to_save_event_flow_process";

    /**
     * @msg.message msg="Unable to save transformation on route {0} which belongs to event flow process {1}:{2} : Error occured while extracting zipped contents."
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_TRANSFORMATION = "error_extracting_zipfile_unable_to_save_transformation";

    /**
     * @msg.message msg="Unable to create Transformation Zip file, to save transformation content for route {0} in event flow process {1}:{2}."
     */
    public final static String UNABLE_TO_CREATE_TRANS_ZIPFILE = "unable_to_create_trans_zipfile";

    /**
     * @msg.message msg="Unable to save AppContext Transformation on port {0} of service {1} which belongs to event flow process {2}:{3} : Error occured while extracting zipped contents."
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_APPCONTEXT_TRANSFORMATION = "error_extracting_zipfile_unable_to_save_appcontext_transformation";

    /**
     * @msg.message msg="Unable to create AppContext Zip file, to save AppContext content for Port {0} of service {1} in event flow process {2}:{3}."
     */
    public final static String UNABLE_TO_CREATE_APPCONTEXT_ZIPFILE = "unable_to_create_appcontext_zipfile";

    /**
     * @msg.message msg="Error changing AppContext transformation on Port {0} of service {1} of application {2}:{3}. Reason : Script files dont exist in zip sent to server."
     */
    public final static String ERROR_CHANGE_APPCONTEXT_TRANSFORMATION = "error_change_appcontext_transformation";

    /**
     * @msg.message msg="Error changing AppContext transformation on Port {0} of service {1} of application {2}:{3}. Reason : Project Directory could not be located."
     */
    public final static String ERROR_CHANGE_APPCONTEXT_TRANSFORMATION2 = "error_change_appcontext_transformation2";

    /**
     * @msg.message msg="Error changing AppContext transformation on Port {0} of service {1} of application {2}:{3}. Reason : {4}."
     */
    public final static String ERROR_CHANGE_APPCONTEXT_TRANSFORMATION3 = "error_change_appcontext_transformation3";

    /**
     * @msg.message msg="Error clearing AppContext transformation on Port {0} of service {1} in application {2}:{3}. Reason : {4}."
     */
    public final static String ERROR_CLEAR_APPCONTEXT_TRANS = "error_clear_appcontext_trans";

    /**
     * @msg.message msg="Error changing transformation on route: {0} of application {1}:{2}. Reason : {3}."
     */
    String ERROR_CHANGE_ROUTE_TRANS = "error_change_route_trans";
    /**
     * @msg.message msg="Error changing transformation on route: {0} of application {1}:{2}. Reason : Script files dont exist in zip sent to server."
     */
    String ERROR_CHANGE_ROUTE_TRANS2 = "error_change_route_trans2";
    /**
     * @msg.message msg="Error changing transformation on route: {0} of application {1}:{2}. Reason : Project Directory could not be located."
     */
    String ERROR_CHANGE_ROUTE_TRANS3 = "error_change_route_trans3";

    /**
     * @msg.message msg="Error clearing transformation on route: {0} of application {1}:{2}. Reason : {2}."
     */
    String ERROR_CLEAR_ROUTE_TRANS = "error_clear_route_trans";

    /**
     * @msg.message msg="Application GUID cannot be null"
     */
    String INVALID_APP_ID = "invalid_app_id";

    /**
     * @msg.message msg="Service Instance id cannot be null"
     */
    String INVALID_SERVICE_INSTANCE_ID = "invalid_service_instance_id";
    /**
     * @msg.message msg="Route GUID cannot be null"
     */
    String INVALID_ROUTE_ID = "invalid_route_id";

    /**
     * @msg.message msg="PortName cannot be null"
     */
    String INVALID_PORT_NAME = "invalid_port_NAME";

    /**
     * @msg.message msg="Unable to deploy service : Error occurred while extracting zipped contents"
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE = "error_extracting_zipfile_unable_to_save_service";
    /**
     * @msg.message msg="Unable to deploy service : Error occurred while extracting zipped contents initiated by user : {0} using client : {1} with IP : {2} "
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE_IP = "error_extracting_zipfile_unable_to_save_service_ip";

    /**
     * @msg.message msg="Error occurred while synchronizing the dependent application for service: "
     */
    public final static String ERROR_SYNCHRONIZING_DEPENDENT_APPLICATION = "error_synchronizing_dependent_application";

    /**
     * @msg.message msg="Error occurred while synchronizing the dependent applications by user : {0} using client : {1} with IP : {2} "
     */
    public final static String ERROR_SYNCHRONIZING_DEPENDENT_APPLICATION_IP = "error_synchronizing_dependent_application_ip";

    /**
     * @msg.message msg="Unable to deploy multiple service : Error occurred while extracting zipped contents"
     */
    public final static String ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_MULTIPLE_SERVICE = "error_extracting_zipfile_unable_to_save_multiple_service";

    /**
     * @msg.message msg="The Event Process {0}:{1} is not in running state."
     */
    public final static String EVENT_PROCESS_NOT_IN_RUNNING_STATE = "event_process_not_in_running_state";


    /**
     * @msg.message msg="The Event Process {0}:{1} is in running state."
     */
    public final static String EVENT_PROCESS_IN_RUNNING_STATE = "event_process_in_running_state";

    /**
     * @msg.message msg="Error in Fetching the Resource {0} from Service."
     */
    public final static String ERROR_FETCHING_RESOURCE_FROM_SERVICE = "error_fetching_resource_from_service";
    /**
     * @msg.message msg="Server is in Active Transition state."
     */
    public final static String SERVER_IS_IN_ACTIVE_TRANSITION = "server_is_in_active_transition";

    /**
     * @msg.message msg="Error reading bytes from Resource File {0} in fetchResourceForService Method."
     */
    public final static String ERROR_READING_BYTES_FROM_RESOURCEFILE = "error_reading_bytes_from_resourcefile";

    /**
     * @msg.message msg="Error occured while trying to delete Service {0}."
     */
    public final static String ERROR_DELETE_SERVICE = "error_delete_service";

    /**
     * @msg.message msg="Error occurred while trying to delete Service {0} by user : {1} using client : {2} with IP : {3}."
     */
    public final static String ERROR_DELETE_SERVICE_IP = "error_delete_service_ip";

    /**
     * @msg.message msg="Failed to Check if service/eventProcess has dependencies exists in server repository"
     */
    public static final String ERROR_DEPENDENCIES_EXISTS = "error_dependencies_exists";

    /**
     * @msg.message msg="Error occurred while getting dependencies for Service {0} with version {1}"
     */
    public static final String ERROR_GETTING_SERVICE_DEPENDENCIES = "error_getting_service_dependencies";

    /**
     * @msg.message msg="Error occured while deleting the EventProcess {0}:{1}."
     */
    public final static String ERROR_DELETE_EVENTPROCESS = "error_delete_eventprocess";
    /**
     * @msg.message msg="Failed to check if Event Process : {0}:{1} exists in the repository or not."
     */
    public final static String ERROR_EVENTPROCESS_EXISTS = "error_eventprocess_exists";
    /**
     * @msg.message msg="Application does not exists in the Repository : {0}:{1}"
     */
    public final static String ERROR_EVENTPROCESS_DOES_NOT_EXISTS = "error_eventprocess_does_not_exists";
    /**
     * @msg.message msg="Failed to get GUIDs of saved Event Processes."
     */
    public final static String ERROR_GET_EVENTPROCESS_IDS = "error_get_eventprocess_ids";

    /**
     * @msg.message msg="Failed to get Versions of Event process {0}."
     */
    public final static String ERROR_GET_VERSIONS_OF_EVENTPROCESS = "error_get_versions_of_eventprocess";

    /**
     * @msg.message msg="Failed to get Highest Version of Event process {0}. "
     */
    public final static String ERROR_GET_HIGHEST_VERSION_OF_EVENTPROCESS = "error_get_highest_version_of_eventprocess";

    /**
     * @msg.message msg="Application View permission denied for User : {0}, App GUID: {1}"
     */
    public final static String ERROR_VIEW_PERMISSION_DENIED = "error_view_permission_denied";

    /**
     * @msg.message msg="Failed to deploy Event process. Error Details : {0}."
     */
    public final static String ERROR_DEPLOY_EVENT_FLOW_PROCESS = "error_deploy_event_flow_process";

    /**
     * @msg.message msg="Failed to deploy schema references folder. Error Details : {0}."
     */
    public final static String ERROR_ADD_SCHEMA_REFERENCE_FOLDER = "error_add_schema_reference_folder";
    /**
     * @msg.message msg="Failed to get Running Event process List."
     */
    public final static String ERROR_GET_RUNNING_EVENTPROCESSES = "error_get_running_eventprocesses";

    /**
     * @msg.message msg="Failed to get Saved Event process List"
     */
    public final static String ERROR_GET_SAVED_EVENTPROCESSES = "error_get_saved_eventprocesses";

    /**
     * @msg.message msg="Failed to restart Event process {0}:{1}"
     */
    public final static String ERROR_RESTART_EVENTPROCESS = "error_restart_eventprocess";

    /**
     * @msg.message msg="CRC failed for event Process {0}:{1}. Error Details : {2}"
     */
    public final static String CRC_ERROR = "crc_error";

    /**
     * @msg.message msg="Error in fetching the WSDL URL of the webservice deployed with Event Process {0}:{1}"
     */
    public final static String ERROR_FETCH_WSDL_URL = "error_fetch_wsdl_url";

    /**
     * @msg.message msg="Error in fetching the status for the component {0} of the Event Process {1}:{2}"
     */
    public final static String ERROR_FETCH_COMPONENT_STATS = "error_fetch_component_stats";

    /**
     * @msg.message msg="Error in fetching the status for the component {0} of the Event Process {1}:{2}"
     */
    public final static String ERROR_FLUSH_MESSAGES = "error_flush_messages";

    /**
     * @msg.message msg="Error in fetching the URL of the HTTP Context deployed with Event Process {0}:{1}."
     */
    public final static String ERROR_FETCH_HTTP_CONTEXT = "error_fetch_http_context";

    /**
     * @msg.message msg="Error in Starting Event Process {0}:{1}. Error Details : {2}"
     */
    public final static String ERROR_START_EVENTPROCESS = "error_start_eventprocess";

    /**
     * @msg.message msg="Error in Synchronizing Event Process {0}:{1}. Error Details : {2}."
     */
    public final static String ERROR_SYNCHRONIZE_EVENTPROCESS = "error_synchronize_eventprocess";

    /**
     * @msg.message msg="Error in stopping Event Process {0}:{1}. Error Details : {2}"
     */
    public final static String ERROR_STOP_EVENTPROCESS = "error_stop_eventprocess";

    /**
     * @msg.message msg="Error starting the service instance {0} of event process {1}:{2}. Reason: {3}."
     */
    public final static String ERROR_START_SERVICEINSTANCE = "error_start_serviceinstance";

    /**
     * @msg.message msg="Error stopping the service instance {0} of event process {1}:{2}. Reason: {3}."
     */
    public final static String ERROR_STOP_SERVICEINSTANCE = "error_stop_serviceinstance";

    /**
     * @msg.message msg="Error stopping All service instances of event process {0}:{1}. Reason: {2}."
     */
    public static final String ERROR_STOP_ALL_SERVICEINSTANCES = "error_stop_all_serviceinstances";
    /**
     * @msg.message msg="Error deleting the service instance {0} of event process {1}:{1}. Reason: {3}."
     */
    public final static String ERROR_DELETE_SERVICEINSTANCE = "error_delete_serviceinstance";


    /**
     * @msg.message msg="Error getting debugger Key for event process {0}:{1}."
     */
    public final static String ERROR_GET_DEBUGGER_KEY = "error_get_debugger_key";

    /**
     * @msg.message msg="Error starting services of event process {0}:{1}."
     */
    public final static String ERROR_START_ALL_SERVICE = "error_start_all_service";

    /**
     * @msg.message msg="Error getting Application state Details for event process {0}:{1}."
     */
    public final static String ERROR_GET_APPLICATION_STATE_DETAILS = "error_get_application_state_details";

    /**
     * @msg.message msg="Failure to add the server state listener : Invalid handle ID"
     */
    public final static String FAILURE_TO_ADD_SERVER_STATE_LISTENER = "failure_to_add_server_state_listener";

    /**
     * @msg.message msg="Failure to remove the server state listener : Invalid handle ID"
     */
    public final static String FAILURE_TO_REMOVE_SERVER_STATE_LISTENER = "failure_to_remove_server_state_listener";


    /**
     * @msg.message msg="Error getting the service Ids of registered services in repository."
     */
    String ERROR_GET_ALL_SERVICE_IDS = "error_get_all_service_ids";

    /**
     * @msg.message msg="Error getting the service References for registered and unregisterd services in repository."
     */
    String ERROR_GET_SERVICE_REFERENCES = "error_get_service_references";

    /**
     * @msg.message msg="Error getting all the versions of the service {0}."
     */
    String ERROR_GET_ALL_VERSIONS_SERVICE = "error_get_all_versions_service";

    /**
     * @msg.message msg="Error uploading resource {0} for service {1} to the repository."
     */
    String ERROR_UPLOAD_RESOURCE = "error_upload_resource";

    /**
     * @msg.message msg="Error fetching the list of Peer servers present in the Enterprise Server repository."
     */
    String ERROR_GET_ALL_FPS_NAMES = "error_get_all_fps_names";

    /**
     * @msg.message msg="Error fetching the aliases for the fps {0} from repository, Error Details {1}"
     */
    String ERROR_GET_FPS_ALIASES = "error_get_fps_aliases";

    /**
     * @msg.message msg="Error occurred while checking if the peer {0} is running or not."
     */
    public final static String ERROR_CHECK_FPS_RUNNING = "error_check_fps_running";

    /**
     * @msg.message msg="Error occurred while trying to restart the peer server {0}. Error Details : {1}"
     */
    public final static String ERROR_RESTART_PEER = "error_restart_peer";

    /**
     * @msg.message msg="Error occurred while trying to shut down the {0} server {1}. Error Details : {2}"
     */
    public final static String ERROR_SHUTDOWN_SERVER = "error_shutdown_server";

    /**
     * @msg.message msg="Error occurred while trying to get Connect URL for peer server {0}. Error Details : {1}"
     */
    String ERROR_GET_CONNECT_URL_FOR_PEER = "error_get_connect_url_for_peer";

    /**
     * @msg.message msg="Error occurred while trying to clear logs for application {0} with version {1}. Error Details : {2}"
     */
    String ERROR_CLEAR_LOGS_APPLICATION = "error_clear_logs_application";

    /**
     * @msg.message msg="Error occurred while trying to export the logs of {0}. Error Details : {2} "
     */
    String ERROR_EXPORT_LOGS = "error_export_logs";

    /**
     * @msg.message msg="Unable to authenticate the given user name/password. Reason: {0} "
     */
    String LOGIN_FAILED = "login_failed";

    /**
     * @msg.message msg="Failed to log out.  {0}"
     */
    String LOGOUT_FAILED = "logout_failed";

    /**
     * @msg.message msg="Failed to {0}, Reason : Invalid Handle ID"
     */
    String INVALID_HANDLE_ID = "invalid_handle_id";

    /**
     * @msg.message msg="Server has detected that a client is no longer connected.Forcing logout for user {0}."
     */
    String FORCE_LOGOUT = "force_logout";
    /**
     * @msg.message msg="Forcing logout for user {0}. Server is no longer active."
     */
    String FORCE_LOGOUT1 = "force_logout1";

    /**
     * @msg.message msg="Error Set log level for service Instance {0} of application {1}:{2}. Reason : {3}"
     */
    String ERROR_SET_LOG_LEVEL = "error_set_log_level";

    /**
     * @msg.message msg="Error change selctors for route: {0} of application {1}:{2}. Reason : {3}"
     */
    String ERROR_CHANGE_ROUTE_SELECTOR = "error_change_route_selector";

    /**
     * @msg.message msg="Error enabling State based work flow  for port: {0} of service instance : {1} of application {2}:{3}. Reason : {4}"
     */
    String ERROR_ENABLE_SBW = "error_enable_sbw";

    /**
     * @msg.message msg="Error disabling State based work flow for port: {0} of service instance : {1} of application {2}:{3}. Reason : {4}"
     */
    String ERROR_DISABLE_SBW = "error_disable_sbw";

    /**
     * @msg.message msg="Error setting the tracked datatype of SBW enabled port: {0} of service instance : {1} of application {2}:{3}. Reason : {4}"
     */
    String ERROR_SETTING_TRACKED_DATATYPE ="error_setting_tracked_datatype";

    /*----------------------------------Info Statements -----------------------------------------------*/


    /**
     * @msg.message msg="RMI stub is not bound"
     */
    public final static String RMI_STUB_NOT_BOUND = "rmi_stub_not_bound";

    /**
     * @msg.message msg="Successfully deployed Service {0}"
     */
    public final static String DEPLOY_SERVICE_SUCCESSFUL = "deploy_service_successful";

    /**
     * @msg.message msg="Successfully deployed Service {0} by user : {1} using client : {2} with IP :{3}"
     */
    public final static String DEPLOY_SERVICE_SUCCESSFUL_IP = "deploy_service_successful_ip";

    /**
     * @msg.message msg="Successfully deployed Service {0} with version {1}."
     */
    public final static String DEPLOY_SERVICE_SUCCESSFUL_MULTIPLE = "deploy_service_successful_multiple";

    /**
     * @msg.message msg="Successfully Exported Service {0}."
     */
    public final static String EXPORT_SERVICE_SUCCESSFUL = "export_service_successful";

    /**
     * @msg.message msg="Successfully Exported Service {0} by user : {1} using client : {2} with IP : {3}."
     */
    public final static String EXPORT_SERVICE_SUCCESSFUL_IP = "export_service_successful_ip";

    /**
     * @msg.message msg="Successfully Exported Service {0} with version {1}."
     */
    public final static String EXPORT_SERVICE_SUCCESSFUL_MULTIPLE = "export_service_successful_multiple";

    /**
     * @msg.message msg="Successfully Exported Service {0} by user : {1} using client : {2} with IP : {3} ."
     */
    public final static String EXPORT_SERVICE_SUCCESSFUL_MULTIPLE_IP = "export_service_successful_multiple_ip";

    /**
     * @msg.message msg="RMI Manager stub is bound successfully."
     */
    public final static String RMI_MANAGER_STUB_BOUND_SUCCESSFULLY = "rmi_manager_stub_bound_successfully";

    /**
     * @msg.message msg="Service manager stub has successfully been unbound."
     */
    public final static String SERVICE_MANAGER_STUB_UNBOUND_SUCCESSFULLY = "service_manager_stub_unbound_successfully";

    /**
     * @msg.message msg="Successfully deleted Service {0}."
     */
    public final static String DELETE_SERVICE_SUCCESSFUL = "delete_service_successful";

    /**
     * @msg.message msg="Successfully deleted Service {0}. Delete initiated by user : {1} using client : {2} with IP : {3}"
     */
    public final static String DELETE_SERVICE_SUCCESSFUL_IP = "delete_service_successful_ip";

    /**
     * @msg.message msg="Initiated delete of Service {0} by user : {1} using client : {2} with IP : {3}"
     */
    public final static String DELETE_SERVICE_INIT = "delete_service_init";

    /**
     * @msg.message msg="Successfully deleted EventProcess {0}:{1}."
     */
    public final static String DELETE_EVENTPROCESS_SUCCESSFUL = "delete_eventprocess_successful";

    /**
     * @msg.message msg="Successfully returned list of running components using the specified named configurations."
     */
    public final static String RUN_COMP_NAMED_CONFIG_LIST_SUCCESSFUL = "run_comp_named_config_list_successful";

    /**
     * @msg.message msg="Error returning list of running components using the specified named configurations. Error Details : ."
     */
    public final static String ERROR_RUN_COMP_NAMED_CONFIG_LIST = "error_run_comp_named_config_list";

    /**
     * @msg.message msg="Successfully moved settings from named configuration {0} to named configuration {1} of category {2} environment {3} ."
     */
    public final static String CHANGE_NAMED_CONFIG_SUCCESSFUL = "change_named_config_successful";

    /**
     * @msg.message msg="Starting moving settings from named configuration {0} to named configuration {1} of category {2} environment {3} ."
     */
    public final static String START_CHANGE_NAMED_CONFIG = "start_change_named_config";

    /**
     * @msg.message msg="Error occurred while changing named configurations."
     */
    public final static String ERROR_CHANGE_NAMED_CONFIG= "error_change_named_config";

    /**
     * @msg.message msg="Error occurred while synchronizing all running event processes.Error is : "
     */
    public final static String ERROR_SYNC_ALL_RUN_EP= "error_sync_all_run_ep";

    /**
     * @msg.message msg="Error occurred while trying to retrieve {0} logs for {1} server {2}. Error Details : {3}"
     */
    public final static String ERROR_GET_LOGS = "error_get_logs";

    /**
     * @msg.message msg="Error occurred while trying to clear {0} logs for {1} server {2}. Error Details : {3}"
     */
    public final static String ERROR_CLEAR_LOGS = "error_clear_logs";

    /**
     * @msg.message msg="MQ Clear Logs Error. Error occurred while trying to clear {0} logs for {1} server {2}. Error Details : {3}"
     */
    public final static String ERROR_CLEAR_MQ_LOGS = "error_clear_mq_logs";


    /**
     * @msg.message msg="Error occurred while trying to get the profile name of FES. Error Details : {0}"
     */
    public final static String ERROR_GET_ESB_NAME = "error_get_esb_name";

    /**
     * @msg.message msg="Error occurred while trying to get details of the FES. Error Details : {0}"
     */
    public final static String ERROR_GET_ESB_DETAILS = "error_get_esb_details";

    /**
     * @msg.message msg="Error occurred while trying to get the status of HA FES. Error Details : {0}"
     */
    public final static String ERROR_GET_HA_SERVER_STATUS = "error_get_ha_server_status";

    /**
     * @msg.message msg="Error occurred while trying to retrieve {0} logs for service {1} of event process {2}:{3}. Error Details : {4}"
     */
    public final static String ERROR_GET_LOGS_SERVICE = "error_get_logs_service";

    /**
     * @msg.message msg="Error occurred while trying to clear {0} logs for service {1} of event process {2}:{3}. Error Details : {4}"
     */
    public final static String ERROR_CLEAR_LOGS_SERVICE = "error_clear_logs_service";
    /*----------------------------------------------------------------------------------------------------------------*/
    /**
     * @msg.message msg="[Breakpoint Manager] Application handle for Event process {0}:{1} was found to be null."
     */
    public final static String APPHANDLE_NULL = "apphandle_null";

    /**
     * @msg.message msg="[Breakpoint Manager] Exception occurred while trying to get Application Controller object for Event process {0}:{1}."
     */
    public final static String ERROR_GET_APPCONTROLLER_OBJ = "error_get_appcontroller_obj";

    /**
     * @msg.message msg="Error occurred while pausing route : {2} message delivery in Event process {0}:{1}. Exception : {3}"
     */
    public final static String ERROR_PAUSE_ROUTE = "error_pause_route";

    /**
     * @msg.message msg="Connection Handle does not exist for user requesting to logout."
     */
    public final static String NO_CONNECTION_HANDLE = "no_connection_handle";

    /**
     * @msg.message msg="Error occurred while persisting configuration. Error Message: {0}."
     */
    public final static String ERR_PERSISTING_CONFIGURATION = "err_persisting_configuration";

    /**
     * @msg.message msg="Error occurred while persisting configuration in real time. Error Message: {0}."
     */
    public final static String ERR_REAL_TIME_PERSISTING_CONFIGURATION = "err_real_time_persisting_configuration";

    /**
     * @msg.message msg="Error occurred while deleting configuration. Error Message: {0}."
     */
    public final static String ERR_DELETING_CONFIGURATION = "err_deleting_configuration";

    /**
     * @msg.message msg="Error occurred while searching for configurations. Error Message: {0}."
     */
    public final static String ERR_SEARCHING_CONFIGURATION = "err_searching_configuration";

    /**
     * @msg.message msg="Error occurred while searching for configurations for application {0} with version {1}. Error Message: {2}."
     */
    public final static String ERR_SEARCHING_APP_CONFIGURATIONS = "err_searching_app_configurations";

    /**
     * @msg.message msg="Event Process {0} with version {1} was not found in application repository. Error Message: {2}."
     */
    public final static String EVENT_PROCESS_NOT_FOUND = "event_process_not_found";

    /**
     * @msg.message msg="Error occurred while reading Event Process {0} with version {1}. Error Message: {2}."
     */
    public final static String ERR_READING_EVENT_PROCESS = "err_reading_event_process";

    /**
     * @msg.message msg="Error occurred as Event Process {0} with version {1} does not exist in the repository."
     */
    public final static String ERROR_APP_DOESNT_EXIST = "error_app_doesnt_exist";

    /**
     * @msg.message msg="Error occurred while getting WADL URL for Service Instance {0} of Event Process {1};{2}."
     */
    public final static String ERROR_GETTING_WADL_URL = "error_getting_wadl_url";

    /**
     * @msg.message msg="Error in getting Application shutdown chain for the Application {0}:{1}. Error Details : {2}"
     */
    public final static String ERROR_GETTING_SHUTDOWN_CHAIN = "error_getting_shutdown_chain";

    /**
     * @msg.message msg="Error in getting Application launch chain for the Application {0}:{1}. Error Details : {2}"
     */
    public final static String ERROR_GETTING_LAUNCH_CHAIN = "error_getting_launch_chain";

    /**
     * @msg.message msg="Error in getting the Set of referring AppGUIDs of running applications for the Service {0} of Application {1}:{2}."
     */
    public final static String ERROR_GETTING_REFERRING_APPLICATIONS = "error_getting_referring_applications";
    /**
     * @msg.message msg="Error in getting the Set of referring AppGUIDs  of Application {0}:{1}."
     */
    public final static String ERROR_GETTING_REFERRING_APPLICATIONS_LIST = "error_getting_referring_applications_list";

    /**
     * @msg.message msg="Error in getting the Applications Info."
     */
    public final static String ERROR_GETTING_APPLICATIONS_INFO = "error_getting_applications_info";

    /**
     * @msg.message msg="Error in getting the details of Application {0}:{1} : {2}."
     */
    public final static String ERROR_GETTING_PARTICULAR_APP_INFO = "error_getting_particular_app_info";

    /**
     * @msg.message msg="Error while finding whether Application is being referred {0}:{1} : {2}."
     */
    public final static String ERROR_GETTING_IS_APP_REFERRED = "error_getting_is_app_referred";
    /**
     * @msg.message msg="Error while finding whether Application level flag for route durability is set for {0}:{1} : {2}."
     */
    public final static String ERROR_GETTING_IS_APP_LEVEL_ROUTE_DURABLE_SET = "error_getting_is_app_level_route_durable_set";
    /**
     * @msg.message msg="Error while finding whether route:{2} in the application {0}:{1} is durable. {3}"
     */
    public final static String ERROR_GETTING_IS_ROUTE_DURABLE = "error_getting_is_route_durable";

    /**
     * @msg.message msg="Error in adding the Schema: {0} to Schema Repository Exception Details: {1}."
     */
    public final static String ERR_ADDING_SCHEMA_REFERENCE = "err_adding_schema_reference";

    /**
     * @msg.message msg="Error occurred while trying to read the resource File. Number of bytes read not equal to number of byte available. File : {0}, File Path : {1}."
     */
    public final static String BYTES_NUM_MISMATCH = "bytes_num_mismatch" ;

    /**
     * @msg.message msg="Error fetching Application Handle for Event Process {0}:{1}, error is : {2} "
     */
    public final static String ERROR_APP_HANDLE="error_app_handle";

    /**
     * @msg.message msg="Illegal target environment in method params "
     */
    public static final String ERROR_WRONG_ARGUMENT = "error_wrong_argument";

    /**
     * @msg.message msg="TargetEnv and application environments present in server repository are same : {0}"
     */
    public static final String ERROR_SAME_TARGET_AND_APP_ENVS = "error_same_target_and_app_envs";

    /**
     * @msg.message msg="Error occurred while writing event process : {0}:{1}, error is : {2}"
     */
    public static final String ERROR_WRITING_EVENT_PROCESS = "error_writing_event_process";
    /**
     *@msg.message msg="Error occurred while getting memory usage for a component: {0}, error is : {1}"
     */
    public static final String ERROR_GETTING_COMPONENT_MEMORY_USAGE= "error_getting_component_memory_usage";
    /**
     *@msg.message msg="Error occurred while getting memory usage for peer: {0}, error is : {1}"
     */
    public static final String ERROR_GETTING_PEER_MEMORY_USAGE= "error_getting_peer_memory_usage";

    /**
     * @msg.message msg="Cannot remove Peer {0} from the repository. It is still connected to the FES."
     */
    public String CANNOT_REMOVE_FPS="cannot_remove_fps";

    /**
     * @msg.message msg="Add Schemas to Schema Repository permission denied for User: {0}"
     */
    String ADD_SCHEMAS_TO_SCHEMA_REPOSITORY_PERMISSION_DENIED = "add_schemas_to_schema_repository_permission_denied";

    /**
     * @msg.message msg="Delete Schemas from Schema Repository permission denied for User: {0}"
     */
    String DELETE_SCHEMAS_FROM_SCHEMA_REPOSITORY_PERMISSION_DENIED = "delete_schemas_from_schema_repository_permission_denied";

    /**
     * @msg.message msg="Failed to fetch schemas present in repository"
     */
    String FAILED_TO_FETCH_SCHEMAS_FROM_REPOSITORY = "failed_to_fetch_schemas_from_repository";

    /**
     * @msg.message msg="Error while sending zipped content of schemas"
     */
    String ERROR_WHILE_SENDING_ZIPPED_SCHEMA_CONTENT = "error_while_sending_zipped_schema_content";
    /**
     * @msg.message msg="Error deploying services {0}. Refers server logs for more information"
     */
    String ERROR_DEPLOY_SERVICES = "error_deploy_services";

    /**
     * @msg.message msg="Error deploying service(s) :{0}. Other Service(s) are imported Successfully, please check Server logs for more Info"
     */
    String ERROR_DEPLOY_SERVICE = "error_deploy_service";

    /**
     * @msg.message msg="Error deploying service(s) :{0} by user : {1} running client : {2} with IP : {3}. Other Service(s) are imported Successfully, please check Server logs for more Info"
     */
    String ERROR_DEPLOY_SERVICE_IP = "error_deploy_service_ip";
    /**
     * @msg.message msg = "Error while retaining old resources for service {0}:{1}"
     */
    String ERROR_WHILE_RETAINING_OLD_RESOURCES = "error_while_retaining_old_resources";
    /**
     * @msg.message msg = "Error while retaining old resource:{2} for service {0}:{1}"
     */
    String ERROR_WHILE_RETAINING_OLD_SPECIFIC_RESOURCES ="error_while_retaining_old_specific_resources";
    /**
     * @msg.message msg="Failed to find call out enabled or not"
     */
    String FAILED_FIND_CALL_OUT_ENABLED = "failed_find_call_out_enabled";

    /**
     * @msg.message msg="Error while getting call out config"
     */
    String FAILED_FETCH_CALL_OUT_CONFIG = "failed_fetch_call_out_config";

    /**
     * @msg.message msg="RMI Manager stub is already bound."
     */
    String RMI_STUB_FOR_RMI_MANAGER_ALREADY_BOUND = "rmi_stub_for_rmi_manager_already_bound";

    /**
     * @msg.message msg="Adding breakpoint on route {0} of Application {1} with version {2}."
     */
    String ADD_BREAKPOINT = "add_breakpoint";

    /**
     * @msg.message msg="Removing breakpoint on route {0} of Application {1} with version {2}."
     */
    String REM_BREAKPOINT = "rem_breakpoint";

    /**
     * @msg.message msg="Getting breakpoint metadata of route {0} in Application {1} with version {2}."
     */
    String GET_BRKPNT_META_DATA = "get_brkpnt_meta_data";

    /**
     * @msg.message msg="Fetching all routes with debugger in Application {0} with version {1}."
     */
    String GET_ALL_ROUTES_WITH_DEBUGGER= "get_all_routes_with_debugger";

    /**
     * @msg.message msg="Removing all debugger in Application {0} with version {1}."
     */
    String REM_ALL_DEBUGGERS= "rem_all_debuggers";

    /**
     * @msg.message msg="Modified message with message ID {0} on debugger at route {1} in Application {2} with version {3}."
     */
    String MOD_MSG_DEBUGGER= "mod_msg_debugger";

    /**
     * @msg.message msg="Deleted message with message ID {0} from debugger on route {1} in Application {2} with version {3}."
     */
    String DEL_MSG_DEBUGGER= "del_msg_debugger";

    /**
     * @msg.message msg="Error stopping component: {0} of GUID {1} version {2} present in application {3} version {4}"
     */
    java.lang.String ERROR_STOPPING_COMPONENT = "error_stopping_component";
}

