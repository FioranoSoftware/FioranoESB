/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.route.RouteOperationConfiguration;

public class TransformationConfiguration extends RouteOperationConfiguration {
    private String xsl;
    private String jmsXsl;
    private String transformerType;

    public String getXsl() {
        return xsl;
    }

    public String getTransformerType() {
        return transformerType;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    public void setTransformerType(String transformerType) {
        this.transformerType = transformerType;
    }

    public String getJmsXsl() {
        return jmsXsl;
    }

    public void setJmsXsl(String jmsXsl) {
        this.jmsXsl = jmsXsl;
    }

}
