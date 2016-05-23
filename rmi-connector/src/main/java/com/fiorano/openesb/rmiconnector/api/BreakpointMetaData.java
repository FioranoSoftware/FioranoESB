/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;


import java.util.Hashtable;
import java.io.Serializable;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

public class BreakpointMetaData implements Serializable {
    private static final long serialVersionUID = 5893167129302990073L;
    private Hashtable<String, String> connectionProperties;
    private String connFactoryName;
    private String sourceQName;
    private String targetQName;

    /**
     * Default Constructor
     */
    public BreakpointMetaData() {
        connectionProperties = new Hashtable<String, String>();
    }

    /**
     * This constructor creates a breakpointmetadata object
     * @param connectionProperties properties for connection to enterprise server
     * @param connFactoryName name of connection factory
     * @param sourceQName source queue name
     * @param targetQName target queue name
     */
    public BreakpointMetaData(Hashtable<String, String> connectionProperties, String connFactoryName, String sourceQName, String targetQName) {
        this.connectionProperties = connectionProperties;
        this.connFactoryName = connFactoryName;
        this.sourceQName = sourceQName;
        this.targetQName = targetQName;
    }

    /**
     * This method returns properties for connection to enterprise server
     * @return Hashtable - connection properties
     */
    public Hashtable<String, String> getConnectionProperties(){
        return connectionProperties;
    }

    /**
     * This method returns connection factory name
     * @return String - connection factory name
     */
    public String getConnectionFactoryName(){
        return connFactoryName;
    }

    /**
     * This method returns source queue name
     * @return String - source queue name
     */
    public String getSourceQueueName(){
        return sourceQName;
    }

    /**
     * This method returns target queue name
     * @return String - target queue name
     */
    public String getTargetQueueName(){
        return targetQName;
    }

    /**
     * This method sets properties for connection to enterprise server
     * @param connectionProperties connection properties
     */
    public void setConnectionProperties(Hashtable connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    /**
     * This method sets connection factory name
     * @param connFactoryName connection factory name
     */
    public void setConnFactoryName(String connFactoryName) {
        this.connFactoryName = connFactoryName;
    }

    /**
     * This method sets source queue name
     * @param sourceQName name of source queue
     */
    public void setSourceQName(String sourceQName) {
        this.sourceQName = sourceQName;
    }

    /**
     * This method sets target queue name
     * @param targetQName name of target queue
     */
    public void setTargetQName(String targetQName) {
        this.targetQName = targetQName;
    }

    /**
     * This method returns metadata from Input Data Stream
     * @param dataInput data input object containing breakpoint metadata
     */
    public void fromStream(DataInput dataInput) throws IOException {
        connFactoryName = dataInput.readUTF();
        sourceQName = dataInput.readUTF();
        targetQName = dataInput.readUTF();

        int propertyTableSize = dataInput.readInt();
        for(int i = 0; i < propertyTableSize; i++){
            String propName = dataInput.readUTF();
            String propValue = dataInput.readUTF();
            connectionProperties.put(propName, propValue);
        }
    }

    /**
     * This method sends metadata to Output Data Stream
     * @param out data output object containing breakpoint metadata
     */
    public void toStream(DataOutput out) throws IOException {
        out.writeUTF(connFactoryName);
        out.writeUTF(sourceQName);
        out.writeUTF(targetQName);

        out.writeInt(connectionProperties.size());
        for(String propName : connectionProperties.keySet()){
            out.writeUTF(propName);
            out.writeUTF(connectionProperties.get(propName));
        }
    }
}
