/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

public interface NmErrorCodes {
    public final static String ERR_ADMINISTERED_OBJECT_NULL = "ADMINISTERED_OBJECT_NULL";

    public final static String ERR_INVALID_CLASS_NAME = "INVALID_CLASS_NAME";

    public final static String ERR_INVALID_ARGUMENTS_ERROR =
            "INVALID_ARGUMENTS_ERROR";

    public final static String ERR_NAMING_MANAGER_NOT_STARTED_ERROR =
            "NAMING_MANAGER_NOT_STARTED_ERROR";

    public final static String ERR_ADMINISTERED_OBJECT_ALREADY_EXIST =
            "ADMINISTERED_OBJECT_ALREADY_EXIST";

    public final static String ERR_ADMINISTERED_OBJECT_CREATION_ERROR =
            "ADMINISTERED_OBJECT_CREATION_ERROR";

    public final static String ERR_FILE_ACCESS_ERROR = "FILE_ACCESS_ERROR";

    public final static String ERR_FILE_DEFRAGMENTATION_ERROR =
            "FILE_DEFRAGMENTATION_ERROR";

    public final static String ERR_CONFIGURATION_FILE =
            "NAMING_MANAGER_CONFIGURATION_FILE_PARSING_ERROR";

    public final static String ERR_CONFIGURATION_FILE_SAVE_ERROR =
            "CONFIGURATION_FILE_SAVE_ERROR";

    public final static String ERR_XML_PARSE_ERROR =
            "XML_PARSE_ERROR";

    public final static String ERR_SQL_EXCEPTION =
            "SQL_EXCEPTION";

    public final static String ERR_UNSERIALIZABLE_OBJECT =
            "UNSERIALIZABLE_OBJECT";
}

