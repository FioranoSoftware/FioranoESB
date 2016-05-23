/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

public class ApplicationRepositoryConstants
{
    public static final String APPLICATION_DIR_DEF = "applications";
    public static final String EVENT_PROCESS_XML = "EventProcess.xml";
    public static final String XML_EXTN = ".xml";
    public static final String HTML_EXTN = ".html";
    public static final String XSD_EXTN = ".xsd";
    public static final String ZIP_EXTN = ".zip";
    public static final String FMP_EXTN = ".fmp";
    public static final String DTD_EXTN = ".dtd";
    public static final String DECIMAL_FORMAT_DEF = "#.0";

    //  Difference between two consecutive version numbers
    //  used while auto-upgrading applications
    public static final float VERSION_AUTO_UPDAGRADE_BY = 0.1f;

    public static final String UTF8_ENCODING = "UTF-8";

    public static final String TEMP_SUFFIX = ".tmp";
    public static final String SYNC_TEMP_DIR = "applications_temp";

    public static boolean isEventProcessFile(String fileName){
        return fileName.endsWith(XML_EXTN);
    }
}