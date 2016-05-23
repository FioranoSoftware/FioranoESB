/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.StringReader;

public class NamespaceSupportReader extends XMLFilterImpl {
    private XNamespaceSupport nsSupport;
    private boolean needNewContext = true;

    public NamespaceSupportReader(XNamespaceSupport nsSupport){
        this.nsSupport = nsSupport;
    }

    public NamespaceSupportReader(){
    }

    public XNamespaceSupport getNamespaceSupport(){
        return nsSupport;
    }

    public void startDocument() throws SAXException {
        if(nsSupport==null)
            nsSupport = new XNamespaceSupport();
        super.startDocument();
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if(needNewContext){
            nsSupport.pushContext();
            needNewContext = false;
        }
        nsSupport.declarePrefix(prefix, uri);
        super.startPrefixMapping(prefix, uri);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        nsSupport.popContext();
        super.endElement(uri, localName, qName);
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
        if(needNewContext) nsSupport.pushContext();
        super.startElement(namespaceURI, localName, qualifiedName, atts);
        needNewContext = true;
    }

    public static void main(String[] args) throws Exception {
        String xml = "<test xmlns:a=\"hai\" xmlns:b=\"hello\"></test>";  //NOI18N
        XMLReader reader = SAXUtil.createSAXParserFactory(true, true, false).newSAXParser().getXMLReader();
        NamespaceSupportReader filter = new NamespaceSupportReader();
        filter.setParent(reader);
        filter.parse(new InputSource(new StringReader(xml)));
    }
}