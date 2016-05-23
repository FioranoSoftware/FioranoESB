/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.rb;

public class Bundle {
    
    /**
     * @msg.message msg="User/Group table was found empty"
     */
    public final static String TABLE_FOUND_EMPTY = "table_found_empty";

    /**
     * @msg.message msg="Number of {0}s is not same"
     */
    public final static String UNEQUAL_ELEMENTS_IN_TABLE = "unequal_elements_in_table";

    /**
     * @msg.message msg="Details of {0}: {1} are not same"
     */
    public final static String TABLE_DETAIL_NOT_SAME = "table_detail_not_same";

    /**
     * @msg.message msg="{0}: {1} not present in one of the Principal Stores"
     */
    public final static String ELEMENT_NOT_IN_SYNC = "element_not_in_sync";

    /**
     * @msg.message msg="ClassCastException occurred while comparing {0} table"
     */
    public final static String CLASSCAST_EXCEPTION = "classcast_exception";

    /**
     * @msg.message msg="NullPointerException occurred while comparing {0} table"
     */
    public final static String NULLPOINTER_EXCEPTION = "nullpointer_exception";

    /**
     * @msg.message msg="Number of {0}s for {1}: {2} is not same"
     */
    public final static String UNEQUAL_MEMBERS_IN_LIST = "unequal_members_in_list";

    /**
     * @msg.message msg="Password for user: {0} is not same"
     */
    public final static String PASSWORD_NOT_SAME = "password_not_same";

    /**
     * @msg.message msg="{0} is not a {1} of {2}: {3}"
     */
    public final static String ELEMENT_NOT_MEMBER = "element_not_member";

    /**
     * @msg.message msg="Rule type is null"
     */
    public final static String RULE_TYPE_NULL = "rule_type_null";

    /**
     * @msg.message msg="Rule pattern is null"
     */
    public final static String RULE_STRING_NULL = "rule_string_null";

    /**
     * @msg.message msg="The arguments passed are invalid. The argument should not be null."
     */
    public final static String NULL_ARGUMENT = "null_argument";

    /**
     * @msg.message msg="String length should not be greater than {0}."
     */
    public final static String LENGTH_GREATER = "length_greater";

    /**
     * @msg.message msg="String length should not be less than {0}."
     */
    public final static String LENGTH_LESSER = "length_lesser";

    /**
     * @msg.message msg="String length should not equal {0}."
     */
    public final static String LENGTH_EQUALS = "length_equals";

    /**
     * @msg.message msg="String length should be greater than {0}."
     */
    public final static String LENGTH_NOT_GREATER = "length_not_greater";

    /**
     * @msg.message msg="String length should be less than {0}."
     */
    public final static String LENGTH_NOT_LESSER = "length_not_lesser";

    /**
     * @msg.message msg="String length should equal {0}."
     */
    public final static String LENGTH_NOT_EQUALS = "length_not_equals";

    /**
     * @msg.message msg="String should not match {0} pattern."
     */
    public static final String PATTERN_MATCHES = "pattern_matches";

    /**
     * @msg.message msg="String should not contain {0}."
     */
    public static final String PATTERN_CONTAINS = "pattern_contains";

    /**
     * @msg.message msg="String should not be {0}."
     */
    public static final String PATTERN_EQUALS = "pattern_equals";

    /**
     * @msg.message msg="String should match {0} pattern."
     */
    public static final String PATTERN_NOT_MATCHES = "pattern_not_matches";

    /**
     * @msg.message msg="String should contain {0}."
     */
    public static final String PATTERN_NOT_CONTAINS = "pattern_not_contains";

    /**
     * @msg.message msg="String should be {0}."
     */
    public static final String PATTERN_NOT_EQUALS = "pattern_not_equals";

    /**
     * @msg.message msg="Event type : {0} is not a valid repository event type."
     */
    public static final String INVALID_REPOSITORY_EVENT_TYPE = "invalid_repository_event_type";

    /**
     * @msg.message msg="Destinations status : {0} is not a valid destinations status."
     */
    public static final String INVALID_DESTINATIONS_STATUS = "invalid_destinations_status";

    /**
     * @msg.message msg="There is a mismatch between the event object and the corresponding filter object for audit event type: {0}."
     */
    public static final String EVENT_TYPE_MISMATCH = "event_type_mismatch";

    /**
     * @msg.message msg="List should not be null."
     */
    public static final String LIST_SHOULD_NOT_BE_NULL = "list_should_not_be_null";

    /**
     * @msg.message msg="Security entity type : {0} is not a valid security entity type."
     */
    public static final String INVALID_SECURITY_ENTITY = "invalid_security_entity";

    /**
     * @msg.message msg="Peer Synchronization Status : {0} does not represent a valid peer synchronization status."
     */
    public static final String INVALID_PEER_SYNC_STATUS = "invalid_peer_sync_status";

    /**
     * @msg.message msg="List should not be null."
     */
    public static final String LIST_FOUND_NULL = "list_found_null";

    /**
     * @msg.message msg="Object Category value for this class is a read-only variable. Original value: {0} is not allowed to be modified."
     */
    public static final String OBJECT_CATEGORY_READ_ONLY = "object_category_read_only";

    /**
     * @msg.message msg="The resource configuration : {0} used for application context in application {1} was not found in configuration repository."
     */
    public static final String APP_CONTEXT_CONFIGURATION_NOT_FOUND = "app_context_configuration_not_found";

    /**
     * @msg.message msg="The resource configuration : {0} used for schema of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String SCHEMA_CONFIGURATION_NOT_FOUND = "schema_configuration_not_found";

    /**
     * @msg.message msg="The resource configuration : {0} used for schema of port {1} for service GUID {2} was not found in configuration repository."
     */
    public static final String SCHEMA_CONFIGURATION_NOT_FOUND_FOR_SERVICE = "schema_configuration_not_found_for_service";

    /**
     * @msg.message msg="The runtime arguments configuration : {0} used for service instance {1} in application {2} was not found in configuration repository."
     */
    public static final String RUNTIME_ARGS_CONFIGURATION_NOT_FOUND = "runtime_args_configuration_not_found";

    /**
     * @msg.message msg="The connection factory configuration : {0} used for service instance {1} in application {2} was not found in configuration repository."
     */
    public static final String CONNECTION_FACTORY_CONFIGURATION_NOT_FOUND = "connection_factory_configuration_not_found";

    /**
     * @msg.message msg="The resource configuration : {0} referenced within schema of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String SCHEMA_REF_CONFIGURATION_NOT_FOUND = "schema_ref_configuration_not_found";

    /**
     * @msg.message msg="The resource configuration : {0} referenced within schema of port {1} for service GUID {2} was not found in configuration repository."
     */
    public static final String SCHEMA_REF_CONFIGURATION_NOT_FOUND_FOR_SERVICE = "schema_ref_configuration_not_found_for_service";

    /**
     * @msg.message msg="The workflow configuration : {0} used for defining workflow configuration of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String WORKFLOW_CONFIGURATION_NOT_FOUND = "workflow_configuration_not_found";

    /**
     * @msg.message msg="The message filter configuration : {0} used for defining message filter configuration of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String MESSAGE_FILTER_CONFIGURATION_NOT_FOUND = "message_filter_configuration_not_found";
    /**
     * @msg.message msg="The Destination configuration : {0} used for defining Destination configuration of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String DESTINATION_CONFIGURATION_NOT_FOUND = "destination_configuration_not_found";

    /**
     * @msg.message msg="The port configuration : {0} used for defining subscriber configuration of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String SUBSCRIBER_CONFIGURATION_NOT_FOUND = "subscriber_configuration_not_found";

    /**
     * @msg.message msg="Error while parsing named configuration meta data file: {0}"
     */
    public static final String ERROR_PARSING_METADATA_XML = "error_parsing_metadata_xml";

    /**
     * @msg.message msg="The port configuration : {0} used for defining publisher configuration of port {1} for service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String PUBLISHER_CONFIGURATION_NOT_FOUND = "publisher_configuration_not_found";

    /**
     * @msg.message msg="The transformation configuration : {0} used for defining output port transformation on port {1} of service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String PORT_TRANSFORMATION_CONFIGURATION_NOT_FOUND = "port_transformation_configuration_not_found";
    
    /**
     * @msg.message msg="The metadata for transformation configuration : {0} used for defining output port transformation on port {1} of service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String METADATA_FOR_PORT_TRANSFORMATION_NOT_FOUND = "metadata_for_port_transformation_not_found";

    /**
     * @msg.message msg="The script file for transformation configuration : {0} used for defining output port transformation on port {1} of service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String SCRIPT_FOR_PORT_TRANSFORMATION_NOT_FOUND = "script_for_port_transformation_not_found";

    /**
     * @msg.message msg="The project file for transformation configuration : {0} used for defining output port transformation on port {1} of service instance {2} in application {3} was not found in configuration repository."
     */
    public static final String PROJECT_FOR_PORT_TRANSFORMATION_NOT_FOUND = "project_for_port_transformation_not_found";

    /**
     * @msg.message msg="The route configuration : {0} used for defining messaging configuration on route {1} in application {2} was not found in configuration repository."
     */
    public static final String ROUTE_MESSAGING_TRANSFORMATION_NOT_FOUND = "route_messaging_transformation_not_found";

    /**
     * @msg.message msg="The selector configuration : {0} used for defining selector configuration on route {1} in application {2} was not found in configuration repository."
     */
    public static final String ROUTE_SELECTOR_TRANSFORMATION_NOT_FOUND = "route_selector_transformation_not_found";

    /**
     * @msg.message msg="The transformation configuration : {0} used for route transformation on route {1} in application {2} was not found in configuration repository."
     */
    public static final String ROUTE_TRANSFORMATION_CONFIGURATION_NOT_FOUND = "route_transformation_configuration_not_found";

    /**
     * @msg.message msg="The metadata for transformation configuration : {0} used for defining route transformation on route {1} in application {2} was not found in configuration repository."
     */
    public static final String METADATA_FOR_ROUTE_TRANSFORMATION_NOT_FOUND = "metadata_for_route_transformation_not_found";

    /**
     * @msg.message msg="The script file for transformation configuration : {0} used for defining route transformation on route {1} in application {2} was not found in configuration repository."
     */
    public static final String SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND = "script_for_route_transformation_not_found";

    /**
     * @msg.message msg="The jms script file for transformation configuration : {0} used for defining route transformation on route {1} in application {2} was not found in configuration repository."
     */
    public static final String JMS_SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND = "jms_script_for_route_transformation_not_found";

    /**
     * @msg.message msg="The project file for transformation configuration : {0} used for defining route transformation on route {1} in application {2} was not found in configuration repository."
     */
    public static final String PROJECT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND = "project_for_route_transformation_not_found";

    /**
     * @msg.message msg="The transformation configuration : {0} used for AppContext transformation on port {0} of service {1} in application {2} was not found in configuration repository."
     */
    public static final String APPCONTEXT_TRANSFORMATION_CONFIGURATION_NOT_FOUND = "appcontext_transformation_configuration_not_found";

    /**
     * @msg.message msg="The metadata for transformation configuration : {0} used for defining AppContext transformation on port {0} of service {1} in application {2} was not found in configuration repository."
     */
    public static final String METADATA_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND = "metadata_for_appcontext_transformation_not_found";

    /**
     * @msg.message msg="The script file for transformation configuration : {0} used for defining AppContext transformation on port {0} of service {1} in application {2} was not found in configuration repository."
     */
    public static final String SCRIPT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND = "script_for_appcontext_transformation_not_found";

    /**
     * @msg.message msg="The jms script file for transformation configuration : {0} used for defining AppContext transformation on port {0} of service {1} in application {2} was not found in configuration repository."
     */
    public static final String JMS_SCRIPT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND = "jms_script_for_appcontext_transformation_not_found";

    /**
     * @msg.message msg="The project file for transformation configuration : {0} used for defining AppContext transformation on port {0} of service {1} in application {2} was not found in configuration repository."
     */
    public static final String PROJECT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND = "project_for_appcontext_transformation_not_found";

    /**
     * @msg.message msg="Configuration type: {0} is not a valid configuration type value."
     */
    public static final String INVALID_OBJECT_CATEGORY = "invalid_object_category";

    /**
     * @msg.message msg="A configuration with type: port has been specified for configuration name: {0} for service name: {1} with version: {2}. Configurations with type: port are not supported to be defined as a named configuration for component configuration"
     */
    public static final String PORT_CONFIGURATION_TYPE_NOT_SUPPORTED = "port_configuration_type_not_supported";

    /**
     * @msg.message msg="A configuration with type: destination has been specified for configuration name: {0} for service name: {1} with version: {2}. Configurations with type: Destination are not supported to be defined as a named configuration for component configuration"
     */
    public static final String DESTINATION_CONFIGURATION_TYPE_NOT_SUPPORTED = "destination_configuration_type_not_supported";

    /**
     * @msg.message msg="Configuration type specified for configuration name: {0} for service name: {1} with version: {2} is invalid"
     */
    public static final String CONFIGURATION_TYPE_INVALID = "configuration_type_invalid";

    /**
     * @msg.message msg="Configuration with name: {0}, type: {1} not found for service name: {2} with version: {3}"
     */
    public static final String CONFIGURATION_NOT_PRESENT = "configuration_not_present";

    /**
     * @msg.message msg="No EventProcess is found in the specified location"
     */
    public final static String NO_APP_FOUND = "no_app_found";
}
