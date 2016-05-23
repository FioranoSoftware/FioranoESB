/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;

public class XMLCreator extends AbstractXMLReader{

    public static final String ENCODING_PROPERTY_NAME = "fiorano.xml.encoding";

    public XMLCreator(Result result, boolean indent, boolean omitXMLDeclaration) throws TransformerConfigurationException {
        TransformerFactory factory = TransformerFactory.newInstance();
        if(!factory.getFeature(SAXTransformerFactory.FEATURE))
            throw new UnsupportedOperationException(factory.getClass().getName()+" doesn't support the feature: "+ SAXTransformerFactory.FEATURE);

        SAXTransformerFactory saxFactory = (SAXTransformerFactory)factory;

        // workaround for xalan: xalan still uses sax 1.0 rather than 2.0
        if(saxFactory.getClass().getName().indexOf("xalan")!=-1) //NOI18
            currentFeatures.add(SAXFeatures.NAMESPACE_PREFIXES);

        TransformerHandler handler = saxFactory.newTransformerHandler();
        Transformer transformer = handler.getTransformer();
        if(indent){
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //NOI18
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //NOI18N
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXMLDeclaration? "yes" : "no"); //NOI18N
        if(saxFactory.getClass().getName().indexOf("saxon")!=-1) //NOI18
            transformer.setOutputProperty("undeclare-prefixes"/*SaxonOutputKeys.UNDECLARE_PREFIXES*/, "yes"); //NOI18N

        String encoder = System.getProperty(ENCODING_PROPERTY_NAME);
        if((encoder!=null)){
        transformer.setOutputProperty(OutputKeys.ENCODING,encoder);
        }
        setContentHandler(handler);
        setLexicalHandler(handler);
        setDTDHandler(handler);

        handler.setResult(result);
    }

    public void setOuputProperty(String name, String value){
        ((TransformerHandler)getContentHandler()).getTransformer().setOutputProperty(name, value);
    }

    public void parse(InputSource inputSource){
            throw new UnsupportedOperationException();
    }

    public void parse(String s){
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws Exception {
        XMLCreator creator = new XMLCreator(new StreamResult(System.out), true, false);

        creator.startDocument();
        creator.startPrefixMapping("", "http://a");
        creator.startElement("x"); //NOI18N
        creator.endElement();
        creator.endDocument();
    }
    public static void main1(String[] args) throws Exception {
        XMLCreator creator = new XMLCreator(new StreamResult(System.out), true, false);
        creator.startDocument();
        creator.startElement("xuri", "x"); //NOI18N
        creator.startElement("yuri", "y"); //NOI18N
        creator.endElement();
        creator.startElement("zuri", "z"); //NOI18N
        creator.addCDATA("xyz");
        creator.endElement();
        creator.addXML(new InputSource(new StringReader("<test>dfdfd</test>")), false); //NOI18N
        creator.endElement();
        creator.endDocument();
    }
}