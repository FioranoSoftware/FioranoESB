/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ApplicationMetadata implements Serializable {
    private static final long serialVersionUID = -1391094945568735309L;
    private String id;
    private float version;
    private String schemaVersion;
    private String displayName = "name";
    private List categories = Collections.EMPTY_LIST;
    private String shortDescription;
    private String longDescription;
    private String typeName;
    private String subType;

    /**
     * Dafault Constructor
     */
    public ApplicationMetadata() {
    }

    /**
     * This constructor creates an event process reference from appGUID and version
     *
     * @param id      appGUID
     * @param version version
     */
    public ApplicationMetadata(String id, float version) {
        this.id = id;
        this.version = version;
    }

    /**
     * This method returns appGUID of an event process
     *
     * @return String - appGUID of event process
     */
    public String getId() {
        return id;
    }

    /**
     * This method sets appGUID for this event process reference
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method returns version of this event process reference
     *
     * @return float - version number of event process
     */
    public float getVersion() {
        return version;
    }

    /**
     * This method sets version for this event process reference
     *
     * @param version the version to set
     */
    public void setVersion(float version) {
        this.version = version;
    }

    /**
     * This method returns schema-version of this event process reference
     *
     * @return String - schema version of event process
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * This method sets schema-version for this event process reference
     *
     * @param schemaVersion the schema-version to set
     */
    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * This method returns display name used for this event process
     *
     * @return String - display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * This method sets specified <code>displayName</code> for this event process
     *
     * @param displayName display Name to be set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * This method returns categories in which this event process is shown
     *
     * @return List - categories of event process
     */
    public List getCategories() {
        return categories;
    }

    /**
     * This method sets categories in which this event process is shown
     *
     * @param categories categories to be set
     */
    public void setCategories(List categories) {
        this.categories = categories;
    }

    /**
     * This method returns short description of this event process (can be null)
     *
     * @return String - short description of event process
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * This method sets specified <code>shortDescription</code> of this event process
     *
     * @param shortDescription short description to be set
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /**
     * This method returns long description of this event process (can be null)
     *
     * @return String - long description of event process
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * This method sets long description for this event process
     *
     * @param longDescription long description to be set
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * This method returns type of event process
     * @return String - event process type
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * This method sets type of event process
     * @param typeName event process type
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * This method returns sub type of event process
     * @return String - subtype of event process
     */
    public String getSubType() {
        return subType;
    }

    /**
     * This method sets sub type of event process
     * @param subType sub type of event process
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }
}
