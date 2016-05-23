/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GarbageXMLWriter extends AbstractXMLWriter{

    // collect all the garbage
    protected boolean _startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return true;
    }
}