/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.configuration.data;

import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;

import java.io.Serializable;

public enum PortType implements Serializable {
    INPUT, OUTPUT;

    /**
     * Returns String converted port type
     * @return String - Port type
     */
    public String toString() {
        switch(this){
            case INPUT: return ConfigurationRepoConstants.INPUT_PORT;
            case OUTPUT: return ConfigurationRepoConstants.OUTPUT_PORT;
            default:return null;
        }
    }

    /**
     * Returns port type
     * @param value Value
     * @return PortType - Port type as object of this class
     */
    public static PortType getPortType(String value) {
        if(ConfigurationRepoConstants.INPUT_PORT.equals(value))
            return INPUT;
        else if(ConfigurationRepoConstants.OUTPUT_PORT.equals(value))
            return OUTPUT;
        else
            return null;
    }
}
