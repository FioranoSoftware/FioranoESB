/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.configuration.data;

import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;

import java.io.Serializable;

public enum Label implements Serializable {
    DEVELOPMENT, TESTING, STAGING, PRODUCTION;


    /**
     * This method returns the label in String format
     * @return String - Label
     */
    @Override
    public String toString() {
        switch (this){
            case DEVELOPMENT: return ConfigurationRepoConstants.DEVELOPMENT;
            case TESTING: return ConfigurationRepoConstants.TESTING;
            case STAGING: return ConfigurationRepoConstants.STAGING;
            case PRODUCTION: return ConfigurationRepoConstants.PRODUCTION;
            default: return null;
        }
    }

}
