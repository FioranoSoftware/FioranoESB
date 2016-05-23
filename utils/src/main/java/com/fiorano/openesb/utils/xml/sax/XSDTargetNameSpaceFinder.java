/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.xml.sax;

import com.fiorano.openesb.utils.SAXUtil;
import com.fiorano.openesb.utils.sax.NoMoreSAXParsingException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class XSDTargetNameSpaceFinder extends DefaultHandler {
    private XSDTargetNameSpaceFinder(){}

    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException {
        String targetNamespace = attrs.getValue("targetNamespace"); //NOI18N
        throw new NoMoreSAXParsingException(targetNamespace);
    }

    // returns null if no targetNamespace is found
    public static String findTargetNamespace(InputSource inputSource)
            throws SAXException, ParserConfigurationException, SAXNotRecognizedException, IOException {
        SAXParser parser = SAXUtil.createSAXParserFactory(true, true, false).newSAXParser();
        try{
            parser.parse(inputSource, new XSDTargetNameSpaceFinder());
            return null;
        } catch(NoMoreSAXParsingException e){
            return (String)e.getData();
        }
    }

    public static String findTargetNamespace(Reader reader)
            throws SAXException, ParserConfigurationException, SAXNotRecognizedException, IOException{
        return findTargetNamespace(new InputSource(reader));
    }

    public static String findTargetNamespace(InputStream stream)
            throws SAXException, ParserConfigurationException, SAXNotRecognizedException, IOException{
        return findTargetNamespace(new InputSource(stream));
    }
}
