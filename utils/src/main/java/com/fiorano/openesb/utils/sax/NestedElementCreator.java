/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import com.fiorano.openesb.utils.NamespaceSupportReader;
import com.fiorano.openesb.utils.Namespaces;
import com.fiorano.openesb.utils.XNamespaceSupport;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerConfigurationException;
import java.util.Enumeration;

public class NestedElementCreator extends XMLCreator{
    protected int depth = 0;

    public NestedElementCreator(Result result, boolean indent, boolean omitXMLDeclaration) throws TransformerConfigurationException {
        super(result, indent, omitXMLDeclaration);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        depth++;
        super.startElement(namespaceURI, localName, qName, atts);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);
        depth--;
        if(depth==0)
            stop(namespaceURI, localName, qName);
    }

    protected XMLReader reader;
    protected ContentHandler prevContentHandler = null;
    protected XNamespaceSupport nsSupport = null;

    public void start(XMLReader reader, String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        this.reader = reader;
        prevContentHandler = reader.getContentHandler();
        startDocument();
        if(reader instanceof NamespaceSupportReader)
            nsSupport = ((NamespaceSupportReader)reader).getNamespaceSupport();

        if(nsSupport!=null){
            Enumeration prefixes = nsSupport.getPrefixes();
            while(prefixes.hasMoreElements()){
                String prefix = (String)prefixes.nextElement();
                String nsuri = nsSupport.getURI(prefix);
                if(nsuri.equals(Namespaces.URI_XML))
                    continue;
                startPrefixMapping(prefix, nsuri);
            }
            if(nsSupport.getURI("") != null)                    //NOI18N
                startPrefixMapping("", nsSupport.getURI("")); //NOI18N
        }

        reader.setContentHandler(this);
        startElement(namespaceURI, localName, qName, atts);
    }

    protected void stop(String namespaceURI, String localName, String qName) throws SAXException {
        if(nsSupport!=null){
            Enumeration prefixes = nsSupport.getPrefixes();
            while(prefixes.hasMoreElements()){
                String prefix = (String)prefixes.nextElement();
                String uri = nsSupport.getURI(prefix);
                if(uri.equals(Namespaces.URI_XML))
                    continue;
                endPrefixMapping(prefix);
            }
            if(nsSupport.getURI("")!=null)                   //NOI18N
                endPrefixMapping("");                        //NOI18N
        }
        endDocument();
        reader.setContentHandler(prevContentHandler);
        prevContentHandler.endElement(namespaceURI, localName, qName);
        return;
    }
}