/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NamedConfigurationProperty extends DmiObject {
    private String configurationName;
    private String configurationType;
    private String configurationID;

    public final static String NO_CONFIGURATION_ID = "NO_CONFIGURATION_ID";

    /**
     * Default constructor
     */
    public NamedConfigurationProperty() {}

    /**
     * Constructs an object of this class using the arguments passed
     * @param configurationName Configuration name to be used
     * @param configurationType Configuration type to be used
     * @param configurationID Configuration ID to be used
     */
    public NamedConfigurationProperty(String configurationName, String configurationType, String configurationID) {
        this.configurationName = configurationName;
        this.configurationType = configurationType;
        this.configurationID = configurationID;
    }

    /**
     * Returns configuration name used for this object
     * @return configuration name used for this object
     */
    public String getConfigurationName() {
        return configurationName;
    }

    /**
     * Returns configuration type used for this object
     * @return configuration type used for this object
     */
    public String getConfigurationType() {
        return configurationType;
    }

    /**
     * Returns configuration ID used for this object
     * @return configuration ID used for this object
     */
    public String getConfigurationID() {
        return configurationID;
    }

    /**
     * Sets configuration name for this object
     * @param configurationName configuration name for this object
     */
    public void setConfigurationName(String configurationName) {
        this.configurationName = configurationName;
    }

    /**
     * Sets configuration type for this object
     * @param configurationType configuration type for this object
     */
    public void setConfigurationType(String configurationType) {
        this.configurationType = configurationType;
    }

    /**
     * Sets configuration ID for this object
     * @param configurationID configuration ID for this object
     */
    public void setConfigurationID(String configurationID) {
        this.configurationID = configurationID;
    }

    public int getObjectID() {
        return DmiObjectTypes.NAMED_CONFIGURATION_PROPERTY;
    }

    public void reset() {
        configurationName = null;
        configurationType = null;
        configurationID = null;
    }

    public void validate() throws FioranoException {

    }

    public void fromStream(DataInput is, int versionNo) throws IOException {
        super.fromStream(is, versionNo);
        configurationName = readUTF(is);
        configurationType = readUTF(is);
	    configurationID = readUTF(is);
    }

    public void toStream(DataOutput out, int versionNo) throws IOException{
        super.toStream(out, versionNo);
        writeUTF(out, configurationName);
        writeUTF(out, configurationType);
	    writeUTF(out, configurationID);
    }
}
