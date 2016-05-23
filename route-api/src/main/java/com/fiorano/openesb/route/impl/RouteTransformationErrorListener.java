/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import java.util.logging.Logger;

public class RouteTransformationErrorListener implements ErrorListener {
    //TODO - implement logger
    Logger logger;

    public RouteTransformationErrorListener(Logger logger)
    {
        logger = logger;
    }
    public void warning(TransformerException exception) throws TransformerException {

    }

    public void error(TransformerException exception) throws TransformerException {

    }

    public void fatalError(TransformerException exception) throws TransformerException {

    }
}

