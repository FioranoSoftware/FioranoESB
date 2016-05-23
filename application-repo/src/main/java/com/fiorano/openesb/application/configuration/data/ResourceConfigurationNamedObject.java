/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.configuration.data;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ResourceConfigurationNamedObject extends NamedObject {
    private String resourceType;

    /**
     * Default constructor
     */
    public ResourceConfigurationNamedObject() {}

    /**
     * Constructor to initialize objects of this class
     * @param name Name
     * @param label Label
     * @param objectCategory Object category
     * @param resourceType Resource type
     */
    public ResourceConfigurationNamedObject(String name, Label label, ObjectCategory objectCategory, String resourceType) {
        super(name, label, objectCategory);
        this.resourceType = resourceType;
    }

    /**
     * Returns object category of objects of this class
     * @return ObjectCategory - Object Category
     */
    public ObjectCategory getObjectCategory() {
        return ObjectCategory.RESOURCE_CONFIGURATION;
    }

    /**
     * Sets object category for objects of this class
     * @param objectCategory Object category
     * @throws UnsupportedOperationException Exception thrown when an unsupported operation is called
     */
    public void setObjectCategory(ObjectCategory objectCategory) throws UnsupportedOperationException {
        if(objectCategory == null || !objectCategory.equals(ObjectCategory.RESOURCE_CONFIGURATION))
            throw new UnsupportedOperationException("OBJECT_CATEGORY_READ_ONLY");
    }

    /**
     * Returns resource type
     * @return String - Resource type
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Sets resource type
     * @param resourceType Resource type
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Returns ID of this object.
     *
     * @return ID of this object.
     * @since Tifosi2.0
     */
    public int getObjectID() {
        return DmiObjectTypes.RESOURCE_CONFIGURATION_NAMED_OBJECT;
    }

    /**
     * Resets the values of the data members of the object. This
     * may possibly be used to help the Dmifactory reuse Dmi objects.
     *
     * @since Tifosi2.0
     */
    public void reset() {
        super.reset();
        resourceType = null;
    }

    /**
     * Tests whether this <code>DmiObject</code> object has the
     * required(mandatory) fields set. This method must be called before
     * inserting values in the database.
     *
     * @throws com.fiorano.openesb.utils.exception.FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate() throws FioranoException {
        super.validate();
    }

    /**
     * Writes this object to specified output stream <code>out</code>
     *
     * @param out       Output stream
     * @param versionNo Version
     * @throws IOException If an error occurs while writing to stream
     */
    public void toStream(DataOutput out, int versionNo) throws IOException {
        super.toStream(out, versionNo);
        writeUTF(out, resourceType);
    }

    /**
     * Reads this object from specified stream <code>is</code>
     *
     * @param is        Input stream
     * @param versionNo Version
     * @throws IOException If an error occurs while reading from stream
     */
    public void fromStream(DataInput is, int versionNo) throws IOException {
        super.fromStream(is, versionNo);
        resourceType = readUTF(is);
    }
}
