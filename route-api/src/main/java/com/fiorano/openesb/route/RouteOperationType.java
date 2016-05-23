/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route;

public enum RouteOperationType {
    MESSAGE_CREATE,
    SRC_CARRY_FORWARD_CONTEXT,
    APP_CONTEXT_TRANSFORM,
    SENDER_SELECTOR,
    APP_CONTEXT_XML_SELECTOR,
    BODY_XML_SELECTOR,
    ROUTE_TRANSFORM,
    TGT_CARRY_FORWARD_CONTEXT,
    SEND;
}
