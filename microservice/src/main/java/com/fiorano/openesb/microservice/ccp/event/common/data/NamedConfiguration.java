/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
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

public class NamedConfiguration extends Data {
    private HashMap<String, String> namedConfigurations;

    public NamedConfiguration() {
    }

    @Override
    public DataType getDataType() {
        return DataType.NAMED_CONFIGURATION;
    }

    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        int numConfigs = bytesMessage.readInt();
        if(numConfigs < 1)
            return;

        namedConfigurations = new HashMap<>();
        for(int i = 0; i < numConfigs; i++){
            String key = bytesMessage.readUTF();
            String config = bytesMessage.readUTF();
            namedConfigurations.put(key, config);
        }
    }

    /**
     * Writes this data object to the bytesMessage.
     * @param bytesMessage Bytes message
     * @throws JMSException If an exception occurs while writin value to the message
     * @see #fromMessage(javax.jms.BytesMessage)
     */
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        if(namedConfigurations == null){
            bytesMessage.writeInt(-1);
            return;
        }

        bytesMessage.writeInt(namedConfigurations.size());
        for(String configKey : namedConfigurations.keySet()){
            bytesMessage.writeUTF(configKey);
            bytesMessage.writeUTF(namedConfigurations.get(configKey));
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

        namedConfigurations = new HashMap<>();
        for(int i = 0; i < numConfigs; i++){
            String key = in.readUTF();
            String config = in.readUTF();
            namedConfigurations.put(key, config);
        }
    }

    /**
     * Writes this data object to the data stream.
     * @param out Output data stream
     * @throws IOException If an exception occurs while writing value to the stream
     * @see #fromStream(java.io.DataInput)
     */
    public void toStream(DataOutput out) throws IOException {
        if(namedConfigurations == null){
            out.writeInt(-1);
            return;
        }

        out.writeInt(namedConfigurations.size());
        for(String configKey : namedConfigurations.keySet()){
            out.writeUTF(configKey);
            out.writeUTF(namedConfigurations.get(configKey));
        }
    }

    public HashMap getNamedConfigurations() {
        return namedConfigurations;
    }

    public void setConfiguration(HashMap configuration) {
        this.namedConfigurations = configuration;
    }
}
