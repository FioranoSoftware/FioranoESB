/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.CharArrayWriter;
import java.io.IOException;

public abstract class AbstractXMLWriter extends DefaultHandler {
    protected AbstractXMLWriter parent;

    protected XMLReader reader;
    protected Object object = null;

    // used to track when my job gets completed.
    private int depth = 0;

    // Buffer for collecting data from the "characters" SAX event.
    // subclasses should call contents.reset() to clear contents
    protected final CharArrayWriter contents = new CharArrayWriter();

    public final void characters( char[] ch, int start, int length ){
       contents.write( ch, start, length ); // accumulate the contents into a buffer.
    }

    private Locator locator;

    public void setDocumentLocator(Locator locator){
        this.locator = locator;
    }

    public Locator getDocumentLocator(){
        return locator;
    }

    public AbstractXMLWriter() {
        this(null);
    }

    public AbstractXMLWriter(Object object) {
        setObject(object);
    }

    public final Object getObject(){
        return object;
    }

    public void setObject(Object obj){
        object = obj;
    }

    protected int getDepth(){
        return depth;
    }

    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(!_startElement(uri, localName, qName, attributes)){
            AbstractXMLWriter writer = createXMLWriter(uri, localName, qName, attributes);
            writer.reader = reader;
            writer.parent = this;
            switchWriter(writer);
            writer.startElement(uri, localName, qName, attributes);
        }else
            ++depth;
    }

    public final void endElement (String uri, String localName, String qName) throws SAXException {
        _endElement(uri, localName, qName);
        depth--;
        if(depth==0){ // popup handler
            switchWriter(parent);
        }
    }

    public final void parse(XMLReader reader, InputSource is) throws IOException, SAXException {
        this.reader = reader;
        reader.parse(is);
    }

    protected void switchWriter(AbstractXMLWriter toWriter){
        if(toWriter==null)
            return;
        if(reader.getContentHandler()==this)
            reader.setContentHandler(toWriter);
        if(reader.getDTDHandler()==this)
            reader.setDTDHandler(toWriter);
        if(reader.getEntityResolver()==this)
            reader.setEntityResolver(toWriter);
        if(reader.getErrorHandler()==this)
            reader.setErrorHandler(toWriter);
        /** @todo what about lexical handler */
//        if(handler instanceof LexicalHandler)
//            reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
//        else
//            reader.setProperty("http://xml.org/sax/properties/lexical-handler", null);
        toWriter.switchedFrom(this);
    }

    protected void switchedFrom(AbstractXMLWriter toWriter){}

    /*-------------------------------------------------[ Abstract Methods ]---------------------------------------------------*/

    // return false if can't be handled
    protected boolean _startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return false;
    }

    public void _endElement (String uri, String localName, String qName) throws SAXException {}

    // can return null
    protected AbstractXMLWriter createXMLWriter(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        return new GarbageXMLWriter();
    }
}