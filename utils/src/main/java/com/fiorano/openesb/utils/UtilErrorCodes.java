/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public class UtilErrorCodes {
    public static final String ERR_SIZE_LIMIT_EXCEEDED_ERROR = "SIZE_LIMIT_EXCEEDED_ERROR";

    public static final String SIZE_LIMIT_EXCEEDED_ERROR =
            "Error encountered because size limit has been crossed.";

    public static final String ERR_LIST_NODE_ALREADY_EXISTS = "LIST_NODE_ALREADY_EXISTS";

    public static final String LIST_NODE_ALREADY_EXISTS =
            "Error encountered while trying to already-added node to the list";

    public static final String ERR_INVALID_ARGUMENTS_ERROR = "INVALID_ARGUMENTS_ERROR";

    public static final String INVALID_ARGUMENTS_ERROR =
            "Error encountered because of invalid arguments.";

    public static final String ERR_SAVE_XML_ERROR = "SAVE_XML_ERROR";

    public static final String SAVE_XML_ERROR =
            "Error encountered while trying to save the XML file.";

    public static final String ERR_EXCEPTION_OCCURRED_ERROR = "EXCEPTION_OCCURRED_ERROR";

    public static final String EXCEPTION_OCCURRED_ERROR =
            "Exception encountered while performing the operation.Details are attached.";
}
