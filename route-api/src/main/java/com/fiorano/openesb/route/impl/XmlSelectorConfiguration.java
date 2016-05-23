/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.SelectorConfiguration;

import java.util.HashMap;
import java.util.Map;

public class XmlSelectorConfiguration extends SelectorConfiguration {

    private String xpath;
    private Map<String,String> nsPrefixMap = new HashMap<>();
    private String target;

    public XmlSelectorConfiguration(String target) {
        this.target = target;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Map<String, String> getNsPrefixMap() {
        return nsPrefixMap;
    }

    public void setNsPrefixMap(Map<String, String> nsPrefixMap) {
        this.nsPrefixMap = nsPrefixMap;
    }

    public String getTarget() {
        return target;
    }
}
