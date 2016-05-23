/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

public class RouteURIResolver implements URIResolver {
    String data = null;

    RouteURIResolver(String data){
        this.data = data;
    }

    public Source resolve(String href, String base)
            throws TransformerException{
        return new StreamSource(new StringReader(data));
    }
}

