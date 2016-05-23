/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import com.fiorano.openesb.utils.ClarkName;
import com.fiorano.openesb.utils.SAXUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


public class XMLRootElementFinder extends DefaultHandler{
    private XMLRootElementFinder(){}

    public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) throws SAXException{
        throw new NoMoreSAXParsingException(ClarkName.toClarkName(namespaceURI, localName));
    }

    public static String findRootElement(InputSource inputSource)
            throws SAXException, ParserConfigurationException, IOException{
        SAXParser parser = SAXUtil.createSAXParserFactory(true, true, false).newSAXParser();
        try{
            parser.parse(inputSource, new XMLRootElementFinder());
            return null;
        } catch(NoMoreSAXParsingException e){
            return (String)e.getData();
        }
    }

    public static String findRootElement(Reader reader)
            throws SAXException, ParserConfigurationException, IOException{
        return findRootElement(new InputSource(reader));
    }

    public static String findRootElement(InputStream stream)
            throws SAXException, ParserConfigurationException, IOException{
        return findRootElement(new InputSource(stream));
    }
}
