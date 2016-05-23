/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.common.data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

public class ManageableProperties extends Data {

    private HashMap<String, ConfigurationProperty> manageableProperties;

    public ManageableProperties() {
    }

    @Override
    public DataType getDataType() {
        return DataType.MANAGEABLE_PROPERTIES;
    }

    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        int numConfigs = bytesMessage.readInt();
        if(numConfigs < 1)
            return;

        manageableProperties = new HashMap<>();
        for(int i = 0; i < numConfigs; i++){
            String name = bytesMessage.readUTF();
            String value = bytesMessage.readUTF();
            boolean isEncrypted = bytesMessage.readBoolean();
            String type = bytesMessage.readUTF();
            String configurationType = bytesMessage.readUTF();
            manageableProperties.put(name, new ConfigurationProperty(name,value,isEncrypted,type,configurationType));
        }
    }

    /**
     * Writes this data object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writin value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        if(manageableProperties == null){
            bytesMessage.writeInt(-1);
            return;
        }

        bytesMessage.writeInt(manageableProperties.size());
        for(ConfigurationProperty property : manageableProperties.values()){
            bytesMessage.writeUTF(property.getName());
            bytesMessage.writeUTF(property.getValue());
            bytesMessage.writeBoolean(property.isEncrypted());
            bytesMessage.writeUTF(property.getType());
            bytesMessage.writeUTF(property.getConfigurationType() == null ? "" : property.getConfigurationType());
        }
    }

    /**
     * Reads the values from the data input stream and sets the properties of this data object.
     * @param in Input data stream
     * @throws IOException If an exception occurs while reading values from the stream
     * @see #toStream(java.io.DataOutput)
     */
    public void fromStream(DataInput in) throws IOException {
        int numConfigs = in.readInt();
        if(numConfigs < 1)
            return;

        manageableProperties = new HashMap<>();
        for(int i = 0; i < numConfigs; i++){
            String name = in.readUTF();
            String value = in.readUTF();
            boolean isEncryted = in.readBoolean();
            String type = in.readUTF();
            String configurationType = in.readUTF();
            manageableProperties.put(name, new ConfigurationProperty(name,value, isEncryted, type, configurationType));
        }
    }

    /**
     * Writes this data object to the data stream.
     * @param out Output data stream
     * @throws IOException If an exception occurs while writing value to the stream
     * @see #fromStream(java.io.DataInput)
     */
    public void toStream(DataOutput out) throws IOException {
        if(manageableProperties == null){
            out.writeInt(-1);
            return;
        }

        out.writeInt(manageableProperties.size());
        for(ConfigurationProperty property : manageableProperties.values()){
            out.writeUTF(property.getName());
            out.writeUTF(property.getValue());
            out.writeBoolean(property.isEncrypted());
            out.writeUTF(property.getType());
            out.writeUTF(property.getConfigurationType());
        }
    }

    public HashMap<String, ConfigurationProperty> getManageableProperties() {
        return manageableProperties;
    }

    public void setManageableProperties(HashMap<String, ConfigurationProperty> manageableProperties) {
        this.manageableProperties = manageableProperties;
    }
}
