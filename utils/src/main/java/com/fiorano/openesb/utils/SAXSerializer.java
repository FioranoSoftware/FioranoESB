/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import org.xml.sax.*;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SAXSerializer extends DefaultHandler
        implements LexicalHandler, DeclHandler, SerializerOptions
{
    private XMLBuilder doc = new XMLBuilder();
    private ArrayList ns = new ArrayList();
    private HashMap nsmap = new HashMap();
    private PrintStream out = System.out;
    private StringBuffer content = new StringBuffer();
    private Locator locator = null;
    private StringBuffer dtd = new StringBuffer();

    public SAXSerializer(Properties options){
        if(options==null)
           return;
       doc.setIndent(options.getProperty(INDENT_STRING, "  "));
       if(options.getProperty(OMIT_XML_DECLERATION, "yes").equalsIgnoreCase("yes"))
           doc.startDocument(null, null);
       doc.setLineSeparator(options.getProperty(LINE_SEPARATOR, "\n"));
   }

    public SAXSerializer(XMLReader reader, InputSource source, Properties options)
            throws Exception
    {
        if(options != null) {
            doc.setIndent(options.getProperty(INDENT_STRING, "  "));
            if(options.getProperty(OMIT_XML_DECLERATION, "yes").equalsIgnoreCase("yes"))
                doc.startDocument(null, null);
            doc.setLineSeparator(options.getProperty(LINE_SEPARATOR, "\n"));
        }

        reader.setContentHandler(this);
        reader.setDTDHandler(this);
        reader.setEntityResolver(this);
        reader.setErrorHandler(this);
        reader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        reader.setProperty("http://xml.org/sax/properties/declaration-handler", this);
        reader.parse(source);
    }

    public SAXSerializer(String xml, Properties options) throws Exception{
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        parser.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        parser.setProperty("http://xml.org/sax/properties/declaration-handler", this);
        parser.parse(new InputSource(new StringReader(xml)), this);
    }

    public String toString(){
        return doc.getXMLDocument();
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() throws SAXException {
        doc.startDocument();
    }

    public void startPrefixMapping(String p0, String p1) throws SAXException {
        ns.add(new String[]{p0, p1});
        nsmap.put(p1,p0);
    }


    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        String text = content.toString().trim();
        content.setLength(0);
        if(text.length() > 0)
            doc.addText(text, false);

        String eName = qName;
        if("".equals(eName))
            eName = sName;

        if (dtd.length() > 0) {
          doc.addDTD(eName, dtd.toString());
          dtd.setLength(0);
        }

        doc.startElement(eName);
        if(attrs != null) {
            for(int i = 0, len = attrs.getLength(); i < len; i++) {
                String aName = attrs.getLocalName(i);
                String aURI = attrs.getURI(i);
                if("".equals(aName))
                    aName = attrs.getQName(i);
                if(aURI!=null && nsmap.get(aURI)!=null)
                {
                    aName = nsmap.get(aURI) + ":" + aName;
                }
                doc.addAttribute(aName, attrs.getValue(i));
            }
        }

        if(ns.size() > 0) {
            for(int i = 0; i < ns.size(); i++) {
                String str[] = (String[])ns.get(i);
                if(str[0].trim().length()>0)
                    doc.addAttribute("xmlns:" + str[0], str[1]);
                else
                    doc.addAttribute("xmlns", str[1]);
            }
            ns.clear();
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        String text = content.toString().trim();
        content.setLength(0);
        if(text.length() > 0)
            doc.addText(text, false);

        String eName = qName;
        if("".equals(qName))
            eName = sName;
        doc.endElement(eName);
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        content.append(buf, offset, len);
    }

    public void startDTD(String string, String string1, String string2) throws SAXException { }

    public void endDTD() throws SAXException { }

    public void startEntity(String string) throws SAXException { }

    public void endEntity(String string) throws SAXException { }

    public void startCDATA() throws SAXException {
        String text = content.toString().trim();
        if(text.length() > 0)
            doc.addText(text, false);
        content.setLength(0);
    }

    public void endCDATA() throws SAXException {
        doc.addText(content.toString(), true);
        content.setLength(0);
    }

    public void comment(char[] charArray, int int1, int int2) throws SAXException { }

    public void elementDecl(String name, String model) throws SAXException{
      dtd.append("<!ELEMENT "+name+" "+model+">\n");
    }

    public void attributeDecl(String elementName, String attributeName, String type, String defaultValue, String value) throws SAXException{
      dtd.append("<!ATTLIST "+elementName+" "+attributeName+" "+type+" "+
                         (defaultValue==null?"":defaultValue)+" "+
                         (value==null?"":value)+">\n");
    }

    public void internalEntityDecl(String name, String value) throws SAXException{
//      System.err.println("intercalEntityDecl*******");
//      System.err.println("name:"+name);
//      System.err.println("value:"+value);
    }

    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException{
//      System.err.println("externalentityDecl**********");
//      System.err.println("name:"+name);
//      System.err.println("publicid:"+publicId);
//      System.err.println("systemid:"+systemId);
    }

    public void notationDecl(String name, String publicId, String systemId) throws SAXException{
//      System.err.println("notationDecl**********");
//      System.err.println("name:"+name);
//      System.err.println("publicid:"+publicId);
//      System.err.println("systemid:"+systemId);
    }

     public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException{
//       System.err.println("unparsedentityDecl**********");
//       System.err.println("name:"+name);
//       System.err.println("publicid:"+publicId);
//       System.err.println("systemid:"+systemId);
//       System.err.println("notationname:"+notationName);
     }

//     public InputSource resolveEntity (String publicId, String systemId)  throws SAXException {
//       System.err.println("resolveEntity*********");
//       System.err.println("publicid:"+publicId);
//       System.err.println("systemid:"+systemId);
//       return super.resolveEntity(publicId, systemId);
//     }

}
