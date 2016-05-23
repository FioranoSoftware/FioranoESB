/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.ccp.event.peer;

import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;
import com.fiorano.openesb.microservice.ccp.event.common.data.Data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

public class ConfigEvent extends ControlEvent {
    private Map<String, Object> data = new HashMap<>();

    @Override
    public CCPEventType getEventType() {
        return CCPEventType.CONFIG;
    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);
        int size = bytesMessage.readInt();
        for (int i = 0; i < size; i++) {
            String identifier = String.valueOf(bytesMessage.readUTF());
            Data data = Data.getDataObject(Data.DataType.valueOf(bytesMessage.readUTF()));
            data.fromMessage(bytesMessage);
            this.data.put(identifier, data);
        }

    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.writeInt(data.size());
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            bytesMessage.writeUTF(entry.getKey());
            bytesMessage.writeUTF(entry.getValue().toString());
        }
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Config Event Properties");
        builder.append("-------------------------------------");
        builder.append(super.toString());
        builder.append(" Data Sent : ").append(data != null ? data.toString() : "");
        return builder.toString();
    }
}
