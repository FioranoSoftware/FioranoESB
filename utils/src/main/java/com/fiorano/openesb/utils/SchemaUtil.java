/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.ctc.wstx.evt.CompactStartElement;
import com.ctc.wstx.evt.SimpleStartElement;
import com.ctc.wstx.evt.WAttribute;
import com.ctc.wstx.evt.WNamespace;
import com.ctc.wstx.util.BaseNsContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import com.fiorano.openesb.utils.xml.stax.FioranoStaxParser;

import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class SchemaUtil {

    public static void main(String[] args) {
        Map map = SchemaUtil.extractImports("<?xml version=\"1.0\"?>\n" +
                "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "                      targetNamespace=\"http://www.books.org\"\n" +
                "                      xmlns=\"http://www.books.org\" xmlns:rr=\"http://www.travel.org\" \n" +
                "                      elementFormDefault=\"qualified\">\n" +
                "\t<xsd:import namespace=\"http://www.travel.org\"/>\t\t\t\t  \n" +
                "    <xsd:element name=\"BookStore\">\n" +
                "        <xsd:complexType>\n" +
                "            <xsd:sequence>\n" +
                "                <xsd:element ref=\"Book\" minOccurs=\"1\" maxOccurs=\"unbounded\"/>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "    <xsd:element name=\"Book\">\n" +
                "        <xsd:complexType>\n" +
                "            <xsd:sequence>\n" +
                "                <xsd:element ref=\"Title\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "                <xsd:element ref=\"Author\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "                <xsd:element ref=\"Date\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "                <xsd:element ref=\"ISBN\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "                <xsd:element ref=\"Publisher\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "\t\t\t\t<xsd:element ref=\"rr:transportation\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
                "            </xsd:sequence>\n" +
                "        </xsd:complexType>\n" +
                "    </xsd:element>\n" +
                "    <xsd:element name=\"Title\" type=\"xsd:string\"/>\n" +
                "    <xsd:element name=\"Author\" type=\"xsd:string\"/>\n" +
                "    <xsd:element name=\"Date\" type=\"xsd:string\"/>\n" +
                "    <xsd:element name=\"ISBN\" type=\"xsd:string\"/>\n" +
                "    <xsd:element name=\"Publisher\" type=\"xsd:string\"/>\n" +
                "</xsd:schema>");

        System.out.println(map);
    }

    public static String updateImports(String xsd, Map<String,String> nsLocMap) {
        StringWriter stream = new StringWriter();
        try {
            XMLInputFactory factory = FioranoStaxParser.getStaxInputFactory();
            XMLEventReader eventReader = factory.createXMLEventReader(new StringReader(xsd));
            XMLEventWriter writer = FioranoStaxParser.getStaxOutputFactory().createXMLEventWriter(stream);
            while (eventReader.hasNext()) {
                XMLEvent e = (XMLEvent) eventReader.next();

                QName importQName = new QName("http://www.w3.org/2001/XMLSchema", "import");
                if (e.isStartElement()) {
                    StartElement startElement = e.asStartElement();
                    QName startName = startElement.getName();
                    if (startName.equals(importQName)) {
                        Map<String,Attribute> attributes = new LinkedHashMap<String,Attribute>();
                        Attribute schemaLocation = startElement.getAttributeByName(new QName("schemaLocation"));
                        Attribute namespace = startElement.getAttributeByName(new QName("namespace"));
                        String value = nsLocMap.get(namespace.getValue());
                        attributes.put("namespace",namespace);
                        String location = StringUtil.isEmpty(value) ? (schemaLocation == null ? null : schemaLocation.getValue()) : value;
                        if(!StringUtil.isEmpty(location)) {
                        attributes.put("schemaLocation", new WAttribute(schemaLocation == null ? startElement.getLocation() : schemaLocation.getLocation(),
                                schemaLocation == null ? new QName("schemaLocation") : schemaLocation.getName(), location, false));
                        }

                        StartElement impoElement = new SimpleStartElement(e.getLocation(), startElement.getName(),
                                (BaseNsContext) startElement.getNamespaceContext(),attributes){};
                        writer.add(impoElement);
                    } else
                        writer.add(e);
                }  else {
                    writer.add(e);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    public static Map<String, List<String>> extractImports(String xsd){
        ImportExtractor importExtractor = new ImportExtractor();
        SAXParser parser = null;
        try {
             parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (ParserConfigurationException e) {
            return importExtractor.getImport();
        } catch (SAXException e) {
            return importExtractor.getImport();
        }


        try {
            parser.parse(new InputSource(new StringReader(xsd)),importExtractor);
        } catch (SAXException e) {
            if("OVER".equalsIgnoreCase(e.getMessage())){
                return importExtractor.getImport();
            }
        } catch (IOException e) {
            return importExtractor.getImport();
        }
        return importExtractor.getImport();
    }


    static class ImportExtractor extends DefaultHandler {

        private Hashtable<String, List<String>> imports = new Hashtable<String, List<String>>();
        private int iterator = 0;
        private boolean importsStarted = false;

        public Hashtable<String, List<String>> getImport(){
            return imports;
        }

        public void setDocumentLocator(Locator locator) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void startDocument() throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void endDocument() throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void endPrefixMapping(String prefix) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if(qName.endsWith("import")){
                String schemaLoc = atts.getValue("schemaLocation");
                String namespace = atts.getValue("namespace");
                List list = imports.get(namespace);
                if(list == null){
                    list = new ArrayList();
                    imports.put(namespace, list);
                }
                list.add(schemaLoc);

            }else if (qName.endsWith("include")){

            }else{
                if(importsStarted){
                    throw new SAXException("OVER");
                }
                iterator++;
                if(iterator == 6)
                    throw new SAXException("OVER");
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void processingInstruction(String target, String data) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void skippedEntity(String name) throws SAXException {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}