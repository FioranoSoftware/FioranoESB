/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.utils.SourceContext;

import java.io.Serializable;
import java.util.*;

public class CarryForwardContext implements Serializable {
    private static final long serialVersionUID = -4012981779432749752L;

    Vector vecOfContextsCarriedFwd;

    Hashtable hashCarryFwdProps;

    public ArrayList<SourceContext> getVecOfContextsCarriedFwd() {
        return new ArrayList<SourceContext>(vecOfContextsCarriedFwd);
    }

    public void setVecOfContextsCarriedFwd(ArrayList<SourceContext> vecOfContextsCarriedFwd) {
        this.vecOfContextsCarriedFwd =  new Vector(vecOfContextsCarriedFwd);
    }

    public Hashtable getHashCarryFwdProps() {
        return hashCarryFwdProps;
    }

    public void setHashCarryFwdProps(Hashtable hashCarryFwdProps) {
        this.hashCarryFwdProps = hashCarryFwdProps;
    }

    private String appContext;

    public CarryForwardContext() {
        vecOfContextsCarriedFwd = new Vector();
        hashCarryFwdProps = new Hashtable();
    }

    public static CarryForwardContext getDefault() {
        return new CarryForwardContext();
    }

    public Enumeration getContexts() {
        return vecOfContextsCarriedFwd.elements();
    }


    public String getAppContext() {
        return appContext;
    }

    public Hashtable getCarryFwdProps() {
        return hashCarryFwdProps;
    }

    public String getCarryFwdProperty(String name) {
        return (String) hashCarryFwdProps.get(name);
    }

    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

    public void setCarryFwdProps(Hashtable properties) {
        hashCarryFwdProps = properties;
    }

    public void setCarryFwdProperty(String name, String value) {
        hashCarryFwdProps.put(name, value);
    }

    public void addContext(SourceContext context) {
        if (!vecOfContextsCarriedFwd.contains(context))
            vecOfContextsCarriedFwd.add(context);
    }

}
