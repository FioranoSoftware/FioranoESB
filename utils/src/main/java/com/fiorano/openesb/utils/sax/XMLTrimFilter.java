/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.CharArrayWriter;

public class XMLTrimFilter extends XMLFilterImpl {
    protected final CharArrayWriter contents = new CharArrayWriter();

    public XMLTrimFilter(){
    }

    public XMLTrimFilter(XMLReader parent){
        super(parent);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        writeContents();
        super.startElement(uri, localName, qName, atts);
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        contents.write(ch, start, length);
    }

    public void ignorableWhitespace(char ch[], int start, int length){}

    public void endElement(String uri, String localName, String qName) throws SAXException {
        writeContents();
        super.endElement(uri, localName, qName);
    }

    private void writeContents() throws SAXException {
        char ch[] = contents.toCharArray();
        if(!isWhiteSpace(ch))
            super.characters(ch, 0, ch.length);
        contents.reset();
    }

    private boolean isWhiteSpace(char ch[]){
        for(int i = 0; i<ch.length; i++){
            if(!Character.isWhitespace(ch[i]))
                return false;
        }
        return true;
    }
}