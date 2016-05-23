/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
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

public class TransformationConfigurationNamedObject extends NamedObject {
    private String scriptFileName;
    private String jmsScriptFileName;
    private String projectFileName;
    private String factoryName;

    /**
     * Default Constructor
     */
    public TransformationConfigurationNamedObject() {}

    /**
     * Constructor to initialize objects of this class
     * @param name Name of the transformation
     * @param label Label of the transformation
     * @param objectCategory Object category of the object
     * @param scriptFileName Script file name
     * @param jmsScriptFileName JMS Script file name
     * @param projectFileName Project file name
     * @param factoryName Factory name
     */
    public TransformationConfigurationNamedObject(String name, Label label, ObjectCategory objectCategory, String scriptFileName, String jmsScriptFileName, String projectFileName, String factoryName) {
        super(name, label, objectCategory);
        this.scriptFileName = scriptFileName;
        this.jmsScriptFileName = jmsScriptFileName;
        this.projectFileName = projectFileName;
        this.factoryName = factoryName;
    }

    /**
     * Returns the name of the script file
     * @return String - Script file name
     */
    public String getScriptFileName() {
        return scriptFileName;
    }

    /**
     * Returns the JMS Script file name
     * @return String - JMS Script file name
     */
    public String getJmsScriptFileName() {
        return jmsScriptFileName;
    }

    /**
     * Returns the project file name
     * @return String - Project file name
     */
    public String getProjectFileName() {
        return projectFileName;
    }

    /**
     * Returns the factory name
     * @return String - Factory name
     */
    public String getFactoryName() {
        return factoryName;
    }

    /**
     * Sets the script file name
     * @param scriptFileName Script file name
     */
    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    /**
     * Sets the JMS Script file name
     * @param jmsScriptFileName JMS Script file name
     */
    public void setJmsScriptFileName(String jmsScriptFileName) {
        this.jmsScriptFileName = jmsScriptFileName;
    }

    /**
     * Sets the project file name
     * @param projectFileName Project file name
     */
    public void setProjectFileName(String projectFileName) {
        this.projectFileName = projectFileName;
    }

    /**
     * Sets the factory name
     * @param factoryName Factory name
     */
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    /**
     * Returns the Object Category
     * @return ObjectCategory - An object of this class representing the category of the object
     */
    public ObjectCategory getObjectCategory() {
        return ObjectCategory.TRANSFORMATION;
    }

    /**
     * Sets the object category
     * @param objectCategory Object category
     * @throws UnsupportedOperationException
     */
    public void setObjectCategory(ObjectCategory objectCategory) throws UnsupportedOperationException {
        if(objectCategory == null || !objectCategory.equals(ObjectCategory.TRANSFORMATION))
            throw new UnsupportedOperationException("OBJECT_CATEGORY_READ_ONLY");
    }

    /**
     * Returns ID of this object.
     *
     * @return int - ID of this object.
     * @since Tifosi2.0
     */
    public int getObjectID() {
        return DmiObjectTypes.TRANSFORMATION_CONFIGURATION_NAMED_OBJECT;
    }

    /**
     * Resets the values of the data members of the object. This
     * may possibly be used to help the DMIfactory reuse Dmi objects.
     *
     * @since Tifosi2.0
     */
    public void reset() {
        super.reset();
        scriptFileName = null;
        jmsScriptFileName = null;
        projectFileName = null;
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
        writeUTF(out, scriptFileName);
        writeUTF(out, jmsScriptFileName);
        writeUTF(out, projectFileName);
        writeUTF(out, factoryName);
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
        scriptFileName = readUTF(is);
        jmsScriptFileName = readUTF(is);
        projectFileName = readUTF(is);
        factoryName = readUTF(is);
    }
}
