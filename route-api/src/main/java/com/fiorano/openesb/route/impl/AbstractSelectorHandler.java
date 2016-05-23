/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.Selector;
import com.fiorano.openesb.route.SelectorConfiguration;


public abstract class AbstractSelectorHandler<SC extends SelectorConfiguration> implements Selector {
    protected SC selectorConfiguration;
    public AbstractSelectorHandler(SC selectorConfiguration) {
        this.selectorConfiguration = selectorConfiguration;
    }
}
