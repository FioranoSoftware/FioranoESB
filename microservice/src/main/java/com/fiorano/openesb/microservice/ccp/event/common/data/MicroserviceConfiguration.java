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

public class MicroserviceConfiguration extends Data {
    private String configuration;

    public MicroserviceConfiguration() {
    }

    @Override
    public DataType getDataType() {
        return DataType.COMPONENT_CONFIGURATION;
    }

    @Override
    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
       configuration =  bytesMessage.readUTF();
    }

    @Override
    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeUTF(configuration);
    }

    @Override
    public void fromStream(DataInput in) throws IOException {
        configuration = in.readUTF();
    }

    @Override
    public void toStream(DataOutput out) throws IOException {
        out.writeUTF(configuration);
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
