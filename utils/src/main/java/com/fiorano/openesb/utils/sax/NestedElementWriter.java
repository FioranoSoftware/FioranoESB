/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class NestedElementWriter extends AbstractXMLWriter{

    // return false if can't be handled
    protected boolean _startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try{
            StringWriter writer = new StringWriter();
            NestedElementCreator creator = new NestedElementCreator(new StreamResult(writer), true, true);
            creator.start(reader, uri, localName, qName, attributes);
            setObject(writer);
            return true;
        } catch(TransformerConfigurationException e){
            throw new SAXException(e);
        }
    }

    public void _endElement(String uri, String localName, String qName) throws SAXException {
        super._endElement(uri, localName, qName);
    }
}