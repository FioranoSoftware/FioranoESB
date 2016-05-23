/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class ServiceReference implements Serializable {


    private static final long serialVersionUID = -69380180714255424L;
    private String id;
	private float version;
    private String displayName;

    /**
     * Dafault Constructor
     */
    public ServiceReference() {

	}

    /**
     * This constructor is used to construct a service reference object from service GUID and its version
     * @param id service GUID
     * @param displayName display name
     * @param version version of service
     */
	public ServiceReference(String id, String displayName, float version) {
		this.id = id;
		this.displayName = displayName;
        this.version = version;
    }

	/**
     * This method returns service GUID of this Service reference
	 * @return String - Service GUID
	 */
	public String getId() {
		return id;
	}

	/**
     * This method sets Service GUID of this Service reference
	 * @param id Service GUID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
     * This method returns version of this Service
	 * @return float - Version of this Service
	 */
	public float getVersion() {
		return version;
	}

	/**
     * This method sets version of this Service
	 * @param version Version number
	 */
	public void setVersion(float version) {
		this.version = version;
	}

    /**
     * This method returns the display name of this Service
     * @return String - The service's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * This method sets the display name of this Service
     * @param displayName Display name of the Service
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Overrides toString() method in Object
     * @return String - Service Reference object as a String
     */
    public String toString() {
		return "Service Reference[" + id + "," + version + "]";
	}

}
