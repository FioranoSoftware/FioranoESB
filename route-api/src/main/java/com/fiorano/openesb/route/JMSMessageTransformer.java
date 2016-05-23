/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route;

import javax.jms.Message;

public interface JMSMessageTransformer {

    /**
     * The function takes in a message and returns the transformed message.
     * Usage : Route provides an api which allows this call back to be set.
     * On receipt of any message a transformation can be applied.
     */
    public String transform(Message msg)
            throws Exception;
}
