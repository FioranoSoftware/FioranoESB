/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.configuration.data;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NamedObject extends DmiObject {
    private String name;
    private Label label;
    private ObjectCategory objectCategory;

    public NamedObject() {}

    /**
     * Constructs a named object using name,Label,object category
     * @param name Name
     * @param label Label that can be Development,Production,Testing,Staging
     * @param objectCategory Object Category
     */
    public NamedObject(String name, Label label, ObjectCategory objectCategory) {
        this.name = name;
        this.label = label;
        this.objectCategory = objectCategory;
    }

    /**
     * Gets name of the object
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the environment label
     * @return environment Label eg:Development/Staging/Production/Testing
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Gets ObjectCategory
     * @return category of object
     */
    public ObjectCategory getObjectCategory() {
        return objectCategory;
    }

    /**
     * Sets name of the object
     * @param name Name of the object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets label
     * @param label Environment Label of the object
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    /**
     * Sets ObjectCategory
     * @param objectCategory Category of the object
     */
    public void setObjectCategory(ObjectCategory objectCategory) {
        this.objectCategory = objectCategory;
    }

    /**
     * Returns ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID() {
        return DmiObjectTypes.NAMED_CONFIGURATION_OBJECT;
    }

    /**
     * Resets the values of the data members of the object. This
     * may possibly be used to help the Dmifactory reuse Dmi objects.
     *
     * @since Tifosi2.0
     */
    public void reset() {
        name = null;
        label = null;
        objectCategory = null;
    }

    /**
     * Tests whether this <code>DmiObject</code> object has the
     * required(mandatory) fields set. This method must be called before
     * inserting values in the database.
     *
     * @throws com.fiorano.openesb.utils.exception.FioranoException
     *          if the object is not valid
     * @since Tifosi2.0
     */
    public void validate() throws FioranoException {
        //Nothing to do
    }

    /**
     * Writes this object to specified output stream <code>out</code>
     *
     * @param out       output stream
     * @param versionNo version
     * @throws IOException If an error occurs while writing to stream
     */
    public void toStream(DataOutput out, int versionNo) throws IOException {
        super.toStream(out, versionNo);
        writeUTF(out, name);
        writeUTF(out, label != null ? label.toString() : null);
        writeUTF(out, objectCategory != null ? objectCategory.toString() : null);
    }

    /**
     * Reads this object from specified stream <code>is</code>
     *
     * @param is        input stream
     * @param versionNo version
     * @throws IOException If an error occurs while reading from stream
     */
    public void fromStream(DataInput is, int versionNo) throws IOException {
        super.fromStream(is, versionNo);
        name = readUTF(is);
        label = readLabel(readUTF(is));
        objectCategory = readObjectCategory(readUTF(is));
    }

    private ObjectCategory readObjectCategory(String category) {
        if(category == null)
            return null;

        return ObjectCategory.valueOf(category);
    }

    private Label readLabel(String label) {
        if(label == null)
            return null;

        return Label.valueOf(label.toUpperCase());
    }
}
