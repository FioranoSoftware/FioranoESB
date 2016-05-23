/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
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
 */

/**
 * Created by chaitanya on 11-05-2016.
 */
package com.fiorano.openesb.mq;

import com.fiorano.openesb.transport.ConnectionProvider;
import com.fiorano.openesb.transport.impl.jms.AbstractJMSConnectionProvider;
import com.fiorano.openesb.transport.impl.jms.JMSConnectionConfiguration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.util.Properties;

public class MQConnectionProvider extends AbstractJMSConnectionProvider implements ConnectionProvider<Connection, JMSConnectionConfiguration> {

    private MQDriver driver;

    public MQConnectionProvider(Properties properties, MQDriver driver) {
        super(properties);
        this.driver = driver;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ConnectionFactory getConnectionFactory(String name) throws Exception {
        return driver.getConnectionFactory(name);
    }
}
