/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.io.Serializable;

public class PortInstanceMetaData implements Serializable {
    private static final long serialVersionUID = 6950037429655829896L;

    private String appGUID = null;
    private Float appVersion = -1f;

    private String serviceInstanceName;
    private String displayName;
    private String actualDestinationName;

    public static enum DestinationType {TOPIC, QUEUE}

    private DestinationType destinationType;

    private boolean isDestinationEncrypted;
    private String encryptionKey;
    private String encryptionAlgorithm;
    private boolean allowPaddingToKey;
    private String initializationVector;

    /**
     *  Constructor to initialize values for PortInstanceMetaData
     *  @param serviceInstanceName Name of the Service instance
     *  @param displayName Display Name of the Port
     *  @param actualDestinationName Name of the actual destination
     *  @param destinationType Type of the destination
     */
    public PortInstanceMetaData(String serviceInstanceName, String displayName, String actualDestinationName, DestinationType destinationType) {
        this.serviceInstanceName = serviceInstanceName;
        this.displayName = displayName;
        this.actualDestinationName = actualDestinationName;
        this.destinationType = destinationType;
    }

    /**
     *  Constructor to initialize values for PortInstanceMetaData
     *  @param serviceInstanceName Name of the Service instance
     *  @param displayName Display Name of the Port
     *  @param actualDestinationName Name of the actual destination
     *  @param destinationType Type of the destination
     *  @param isDestinationEncrypted true is destination is encrypted, false otherwise
     *  @param encryptionKey Encryption key to be used if destination is encrypted
     *  @param encryptionAlgorithm Algorithm to be used for the encryption
     *  @param allowPaddingToKey true is padding of Key is allowed, false otherwise
     *  @param initializationVector
     */
    public PortInstanceMetaData(String serviceInstanceName, String displayName, String actualDestinationName, DestinationType destinationType, boolean isDestinationEncrypted, String encryptionKey, String encryptionAlgorithm, boolean allowPaddingToKey, String initializationVector ) {
        this.serviceInstanceName = serviceInstanceName;
        this.displayName = displayName;
        this.actualDestinationName = actualDestinationName;
        this.destinationType = destinationType;
        this.isDestinationEncrypted = isDestinationEncrypted;
        this.encryptionKey = encryptionKey;
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.allowPaddingToKey = allowPaddingToKey;
        this.initializationVector = initializationVector;
    }

    /**
     *  This method checks whether a destination is encrypted
     *  @return boolean - true is destination is encrypted, false otherwise
     */
    public boolean isDestinationEncrypted() {
        return isDestinationEncrypted;
    }

    /**
     *  This method sets whether the destination is encrypted
     *  @param destinationEncrypted true if destination is encrypted, false otherwise
     */
    public void setDestinationEncrypted(boolean destinationEncrypted) {
        isDestinationEncrypted = destinationEncrypted;
    }

    /**
     *  This method returns the encryption key being used
     *  @return String - Encryption Key
     */
    public String getEncryptionKey() {
        return encryptionKey;
    }

    /**
     *  This method sets the encryption key to be used
     *  @param encryptionKey Encryption Key
     */
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    /**
     *  This method returns the encryption algorithm being used
     *  @return String - Encryption Algorithm
     */
    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    /**
     *  This method sets the encryption algorithm to be used
     *  @param encryptionAlgorithm Encryption Algorithm
     */
    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    /**
     *  This method checks whether padding of Keys is allowed in the encryption
     *  @return boolean - true if padding to key is allowed, false otherwise
     */
    public boolean isAllowPaddingToKey() {
        return allowPaddingToKey;
    }

    /**
     *  This method sets AllowPaddingToKey value
     *  @param allowPaddingToKey true is padding to key is allowed, false otherwise
     */
    public void setAllowPaddingToKey(boolean allowPaddingToKey) {
        this.allowPaddingToKey = allowPaddingToKey;
    }

    /**
     *  This method returns the Initialization Vector for the destination
     *  @return String - Initialization Vector
     */
    public String getInitializationVector() {
        return initializationVector;
    }

    /**
     *  This method sets the Initialization Vector for the destination
     *  @param initializationVector Initialization Vector
     */
    public void setInitializationVector(String initializationVector) {
        this.initializationVector = initializationVector;
    }

    /**
     *  This method returns the name of the service instance of which the destination is a part
     *  @return String - Name of the Service Instance
     */
    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    /**
     *  This method returns the display name of the destination
     *  @return String - Display Name of the Destination
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *  This method returns the actual name of the destination
     *  @return String - Actual Name of the Destination
     */
    public String getActualDestinationName() {
        return actualDestinationName;
    }

    /**
     *  This method returns the destination type
     *  @return DestinationType - Object containing Type of the Destination
     */
    public DestinationType getDestinationType() {
        return destinationType;
    }

    /**
     *  This method sets the service instance name
     *  @param serviceInstanceName Name of the Service instance of which the destination is a part
     */
    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    /**
     *  This method sets the display name of the destination to be used
     *  @param displayName Display name of the destination
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     *  This method sets the actual destination name
     *  @param actualDestinationName Actual name of the destination
     */
    public void setActualDestinationName(String actualDestinationName) {
        this.actualDestinationName = actualDestinationName;
    }

    /**
     *  This method sets the type of destination
     *  @param destinationType Type of the destination
     */
    public void setDestinationType(DestinationType destinationType) {
        this.destinationType = destinationType;
    }

    /**
     *  This method returns the name of the Event Process containing the destination.
     *  @return String - Name of the Event Process of which the destination is a part
     */
    public String getApplicationName() {
        return appGUID;
    }

    /**
     *  This method returns the version of the Event Process containing the destination.
     *  @return Float - Version of the Event Process of which the destination is a part
     */
    public Float getApplicationVersion() {
        return appVersion;
    }

    /**
     *  This method sets the name of the Event Process containing the destination
     *  @param tmpAppName Name of the Event Process of which the destination is a part
     */
    public void setApplicationName(String tmpAppName) {
        appGUID = tmpAppName;
    }

    /**
     *  This method sets the version of the Event Process containing the destination
     *  @param tmpAppVersion Version of the Event Process of which the destination is a part
     */
    public void setApplicationVersion(Float tmpAppVersion){
        appVersion = tmpAppVersion;
    }
}
