/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.configuration.data;

import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;

import java.io.Serializable;

public enum DataType implements Serializable {
    /**
     * Represents that this data should be stored as text inside the file name specified.
     */
    TEXT,

    /**
     * Represents that this data is a zip stream and it should be un-zipped and stored inside the folder name specified by the user.
     */
    ZIP_STREAM;

    /**
     * Gives appropriate Data type representation in String
     * @return String
     */
    public String toString() {
        switch(this){
            case TEXT: return ConfigurationRepoConstants.DATA_TYPE_TEXT;
            case ZIP_STREAM: return ConfigurationRepoConstants.DATA_TYPE_FOLDER;
            default: return null;
        }
    }

    /**
     * Gets DataType for the <code>value</code>
     * @param value String representation of Data Type
     * @return DataType
     */
    public static DataType getDataType(String value) {
        if(ConfigurationRepoConstants.DATA_TYPE_TEXT.equals(value))
            return TEXT;
        else if(ConfigurationRepoConstants.DATA_TYPE_FOLDER.equals(value))
            return ZIP_STREAM;
        else
            return null;
    }
}
