/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
/**
 * Copyright (c) 1999-2007, Fiorano Software Technologies Pvt. Ltd. and affiliates.
 * Copyright (c) 2008-2014, Fiorano Software Pte. Ltd. and affiliates.
 * <p>
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 * <p>
 * Created by chaitanya on 11-05-2016.
 * <p>
 * Created by chaitanya on 11-05-2016.
 * <p>
 * Created by chaitanya on 11-05-2016.
 * <p>
 * Created by chaitanya on 11-05-2016.
 */

/**
 * Created by chaitanya on 11-05-2016.
 */
package com.fiorano.openesb.mq;

import com.fiorano.openesb.transport.ConnectionProvider;
import com.fiorano.openesb.transport.PortConfiguration;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.AbstractJMSTransportService;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.transport.impl.jms.JMSPort;
import com.fiorano.openesb.transport.impl.jms.JMSPortConfiguration;

import java.util.Properties;

public class MQTransportService extends AbstractJMSTransportService
        implements TransportService<JMSPort, JMSMessage> {

    private final MQDriver mqDriver;

    @SuppressWarnings("unchecked")
    protected MQTransportService(Properties properties) throws Exception {
        super(properties);
        mqDriver = new FioranoMQDriverImpl(properties);
        initialize();
    }

    @Override
    protected void initialize() throws Exception {
        mqDriver.initialize(properties);
        super.initialize();
    }

    @Override
    public ConnectionProvider getConnectionProvider() {
        return new MQConnectionProvider(properties, mqDriver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void disablePort(PortConfiguration portConfiguration) throws Exception {
        JMSPortConfiguration jmsPortConfiguration = (JMSPortConfiguration) portConfiguration;
        mqDriver.deleteDestination(portConfiguration.getName(), jmsPortConfiguration.getPortType().name());
    }

}
