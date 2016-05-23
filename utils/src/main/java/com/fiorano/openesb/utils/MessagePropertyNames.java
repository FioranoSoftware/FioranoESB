/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;


public interface MessagePropertyNames
{
    public final static String PROP_NAME_PREFIX = "ESBX__SYSTEM__";

    // NOTE: the comment before each property name indicates the type of the property value.

    // String
    public final static String WORK_FLOW_INST_ID = PROP_NAME_PREFIX + "WORK_FLOW_INST_ID";

    // String
    public final static String WORK_FLOW_ID = PROP_NAME_PREFIX + "WORK_FLOW_ID";

    // String
    public final static String USER_DEFINED_DOC_ID = PROP_NAME_PREFIX + "USER_DEFINED_DOC_ID";

    // String
    public final static String COMP_INST_NAME = PROP_NAME_PREFIX + "COMP_INST_NAME";

    // String
    public final static String COMMENT = PROP_NAME_PREFIX + "COMMENT";

    // String
    public final static String WORKFLOW_STATUS = PROP_NAME_PREFIX + "WORKFLOW_STATUS";

    // String
    public final static String STATE_ID = PROP_NAME_PREFIX + "STATE_ID";

    // long
    public final static String IN_TIME = PROP_NAME_PREFIX + "IN_TIME";

    // long
    public final static String OUT_TIME = PROP_NAME_PREFIX + "OUT_TIME";

    // long
    public final static String TOTAL_TIME = PROP_NAME_PREFIX + "TOTAL_TIME";

    // String
    public final static String DOCUMENT_ID = PROP_NAME_PREFIX + "DOCUMENT_ID";

    // int
    public final static String EVENT_TYPE = PROP_NAME_PREFIX + "EVENT_TYPE";

    // boolean
    public final static String IS_ALERT = PROP_NAME_PREFIX + "IS_ALERT";

    // int
    public final static String EVENT_ID = PROP_NAME_PREFIX + "EVENT_ID";

    // String
    public final static String EVENT_CATEGORY = PROP_NAME_PREFIX + "EVENT_CATEGORY";

    // String
    public final static String SOURCE = PROP_NAME_PREFIX + "SOURCE";

    // String
    public final static String SINK = PROP_NAME_PREFIX + "SINK";

    // String
    public final static String EVENT_DESCRIPTION = PROP_NAME_PREFIX + "EVENT_DESCRIPTION";

    // String
    public final static String EVENT_STATUS = PROP_NAME_PREFIX + "EVENT_STATUS";

    // long
    public final static String EVENT_GENERATION_DATE = PROP_NAME_PREFIX + "EVENT_GENERATION_DATE";

    // String
    public final static String EVENT_SCOPE = PROP_NAME_PREFIX + "EVENT_SCOPE";

    // String
    public final static String EVENT_MODULE = PROP_NAME_PREFIX + "EVENT_MODULE";

    // long
    public final static String EXPIRY_TIME = PROP_NAME_PREFIX + "EXPIRY_TIME";

    // String
    public final static String FPS_NAME = PROP_NAME_PREFIX + "FPS_NAME";

    // String
    public final static String SERVICE_GUID = PROP_NAME_PREFIX + "SERVICE_GUID";

    // int
    public final static String CUT_OFF_TRACE_LEVEL = PROP_NAME_PREFIX + "CUT_OFF_TRACE_LEVEL";

    // String
    public final static String SOURCE_SERVICE_INSTANCE = PROP_NAME_PREFIX + "SOURCE_SERVICE_INSTANCE";

    // String
    public final static String PORT_NAME = PROP_NAME_PREFIX + "PORT_NAME";

    // String
    public final static String EVENT_PROCESS_NAME = PROP_NAME_PREFIX + "EVENT_PROCESS_NAME";

    //String
    public final static String EVENT_PROCESS_ENVIRONMENT = PROP_NAME_PREFIX + "EVENT_PROCESS_ENVIRONMENT";

    // String
    public final static String EVENT_PROCESS_VERSION = PROP_NAME_PREFIX + "EVENT_PROCESS_VERSION";

	
	// String
	public final static String SOURCE_TOPIC_NAME = PROP_NAME_PREFIX + "SOURCE_TOPIC_NAME";

    // Object: Hashtable
    public final static String ATTACHMENT_TABLE = PROP_NAME_PREFIX + "ATTACHMENT_TABLE";

    // Object: Hashtable
    public final static String DATA_TABLE = PROP_NAME_PREFIX + "DATA_TABLE";

    // Object
    public final static String CARRY_FORWARD_CONTEXT = PROP_NAME_PREFIX + "CARRY_FORWARD_CONTEXT";

    // Object: byte[]
    public final static String BYTES_DATA = PROP_NAME_PREFIX + "BYTES_DATA";

    // Object: string
    public final static String TEXT_DATA = PROP_NAME_PREFIX + "TEXT_DATA";

    //Object: String
    public final static String SOURCE_DESTINATION_NAME = PROP_NAME_PREFIX + "SOURCE_DESTINATION_NAME";

    //String
    public final static String ERROR_SOURCE = PROP_NAME_PREFIX + "ERROR_SOURCE";

    public final static String EXECUTING_INCR = PROP_NAME_PREFIX + "EXECUTING_INCR";

}
