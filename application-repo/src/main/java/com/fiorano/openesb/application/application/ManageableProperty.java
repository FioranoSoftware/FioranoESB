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

public class ManageableProperty extends DmiObject{

    private String name;
    private String value;
    private boolean isEncrypted;
    private String type;
    private String configurationType;


    public int getObjectID(){
        return DmiObjectTypes.MANAGEABLE_PROPERTY;
    }

    /**
     * Default Constructor
     */
    public ManageableProperty(){

    }

    /**
     * Constructs a managable property from name, value and other parameters
     * @param name name
     * @param value value
     * @param encrypted true if encryptrd
     * @param type data type
     * @param configurationType configuration type
     */
    public ManageableProperty(String name, String value, boolean encrypted, String type, String configurationType) {
        this.name = name;
        this.value = value;
        this.isEncrypted = encrypted;
	    this.type = type;
        this.configurationType = configurationType;
    }

    /**
     * Returns property name
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets property name
     * @param name property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns property value
     * @return property value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets property value
     * @param value value to be set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns a boolean specifying whether this property is encrypted
     * @return boolean true if property is encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Sets a boolean specifying whether this property is encrypted
     * @param encrypted boolean specifying whether this property is encrypted
     */
    public void setEncrypted(boolean encrypted){
        isEncrypted = encrypted;
    }

    /**
     * Returns data type of this property
     * @return data type of this property
     */
    public String getPropertyType(){
	    return type;
    }

    /**
     * Sets property data type
     * @param type data type to be set
     */
    public void setPropertyType(String type){
	    this.type = type;
    }

    /**
     * Returns the configuration type attribute of this property
     * @return configuration type attribute of this property
     */
    public String getConfigurationType() {
        return configurationType;
    }

    /**
     * Sets the configuration type attribute
     * @param configurationType configuration type to be set
     */
    public void setConfigurationType(String configurationType) {
        this.configurationType = configurationType;
    }

    public void fromStream(DataInput is, int versionNo)
            throws IOException{
        super.fromStream(is, versionNo);
        this.name = readUTF(is);
        this.value = readUTF(is);
	    this.type = readUTF(is);
	    this.configurationType = readUTF(is);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof ManageableProperty) {
            ManageableProperty manageableProperty = (ManageableProperty) obj;
            return manageableProperty.getName().equals(getName()) && manageableProperty.getValue().equals(getValue())
                    && manageableProperty.getPropertyType().equals(getPropertyType()) && manageableProperty.getConfigurationType().equals(getConfigurationType())
                    && manageableProperty.isEncrypted() == isEncrypted();
        } else
            return false;
    }

    public void toStream(DataOutput out, int versionNo)
            throws IOException{
        super.toStream(out, versionNo);
        writeUTF(out, this.name);
        writeUTF(out, this.value);
	    writeUTF(out, this.type);
	    writeUTF(out, this.configurationType);
    }

    public void validate() throws FioranoException{
        
    }

    public void reset(){
        name = null;
        value = null;
        isEncrypted = false;
	    type = null;
	    configurationType = null;
    }
}
