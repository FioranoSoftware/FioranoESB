/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import org.apache.xerces.util.XMLChar;

import java.util.Stack;

public class XMLBuilder {
    private static final int START_DOCUMENT = 1;
    private static final int END_DOCUMENT = 2;
    private static final int START_ELEMENT = 3;
    private static final int END_ELEMENT = 4;
    private static final int TEXT_ELEMENT = 5;

    private StringBuffer buf = new StringBuffer();
    private Stack stack = new Stack();
    private String indent = "  ";
    private int mode = END_DOCUMENT;
    private int depth = -1;
    private String lineSeparator = "\n";

    /**
     * Changes the string used for indentation
     * default intendation is "  ".
     *
     * @param str     string to be used for indentation
     */
    public void setIndent(String str){
        indent = str;
    }

    public void setLineSeparator(String sep){
        lineSeparator = sep;
    }

    private void printIndent(){
        buf.append(lineSeparator);
        for(int i=0; i<depth; i++) buf.append(indent);
    }

    /**
     * starts the xml document with default version and encoding
     * default version  = 1.0
     * default encoding = UTF-8
     */
    public void startDocument(){
        startDocument("1.0", "UTF-8");
    }

    /**
     * starts the xml document with given version and encoding
     *
     * @param version       version of xml
     * @param encoding      encoding
     */
    public void startDocument(String version, String encoding){
        reset();
        if(mode!=END_DOCUMENT) throw new UnsupportedOperationException("Invalid state in startDocument: "+mode);
        depth=-1;
        buf.setLength(0);
        if(version!=null && encoding!=null)
            buf.append("<?xml version=\""+version+"\" encoding=\""+encoding+"\"?>");
        else if(version!=null && encoding==null)
            buf.append("<?xml version=\""+version+"\"?>");
        else if(version==null && encoding!=null)
            buf.append("<?xml encoding=\""+encoding+"\"?>");
        mode = START_DOCUMENT;
    }

    /**
     * starts the element of given name.
     * Note that only starting tag of element will be added.
     *
     * @param name
     */
    public XMLBuilder startElement(String name){
        if(!XMLChar.isValidName(name))
            throw new IllegalArgumentException("Invalid element name: "+name);

        switch(mode){
            case END_DOCUMENT:
                startDocument("1.0", "UTF-8");
                break;
            case START_ELEMENT:
                buf.append('>');
                break;
        }
        stack.push(name);
        depth++; printIndent();
        buf.append("<"+name);
        mode = START_ELEMENT;
        return this;
    }

    /**
     * adds the attribute to the current element.
     * special character in value will be replaced by their entities
     *
     * @param name        name of attribute
     * @param value       value of attribute.
     */
    public XMLBuilder addAttribute(String name, String value){
        if(!XMLChar.isValidName(name))
            throw new IllegalArgumentException("Invalid attribute name: "+name);

        if(mode!=START_ELEMENT) throw new UnsupportedOperationException("Invalid state in addAttribute: "+name+"=\""+value+"\"");
        buf.append(" "+name+"=\""+normalize(value)+"\"");
        return this;
    }

    /**
     * return the string with the special characters replaced by their entities.
     *
     * @param s        string to be normalized.
     * @return         normalized string
     */
    public static String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for(int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<':
                    str.append("&lt;");
                    break;
                case '>':
                    str.append("&gt;");
                    break;
                case '&':
                    str.append("&amp;");
                    break;
                case '"':
                    str.append("&quot;");
                    break;
                case '\r':
                case '\n':
                    str.append("&#x"+Integer.toHexString(ch)+';');
                    break;
                default:
                    str.append(ch);
            }
        }

        return str.toString();
    }

    public static String normalizeText(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for(int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<':
                    str.append("&lt;");
                    break;
                case '>':
                    str.append("&gt;");
                    break;
                case '&':
                    str.append("&amp;");
                    break;
                case '"':
                    str.append("&quot;");
                    break;
                default:
                    str.append(ch);
            }
        }

        return str.toString();
    }


    /**
     * add text to the current element.
     *
     * @param text         text to be added
     * @param cdata        tell whether to enclose the text in CDATA section.
     *                     if false, then special characters in given text are replaced
     *                     by their entities.
     */
    public XMLBuilder addText(String text, boolean cdata){
        if(text==null) text="";
        if(mode==START_DOCUMENT) throw new UnsupportedOperationException("Invalid state in addText: "+mode);
        if(mode==START_ELEMENT) buf.append('>');
        if(mode==END_ELEMENT) printIndent();
        if(cdata){
            int index = -1;
            while((index=text.indexOf("]]>"))!=-1){
                buf.append("<![CDATA["+text.substring(0, index+2)+"]]>");
                text = text.substring(index+2);
            }
            buf.append("<![CDATA["+text+"]]>");
        }
        else buf.append(normalizeText(text));
        if(mode==END_ELEMENT) printIndent();
        mode = TEXT_ELEMENT;
        return this;
    }

    /**
     * adds and string containing xml to the current element
     * this method does not verify whether the given xml string.
     * it wont replace any special characters in the given text.
     *
     * @param text        string containing xml
     */
    public XMLBuilder addXMLText(String text){
        if(text==null) text="";
        if(mode==START_DOCUMENT) throw new UnsupportedOperationException("Invalid state in addXMLText: "+mode);
        if(mode==START_ELEMENT) buf.append('>');
        buf.append(text);
        mode = TEXT_ELEMENT;
        return this;
    }

    public XMLBuilder addDTD(String root, String dtd){
      if(mode!=START_DOCUMENT) throw new UnsupportedOperationException("Invalid state in addXMLText: "+mode);
      buf.append(lineSeparator+"<!DOCTYPE "+root+"["+lineSeparator+dtd+"]>"+lineSeparator);
      return this;
    }

    /** @todo Need to handle nested comments. */
    public XMLBuilder addComment(String comment){
        if(comment==null) return this;
        printIndent();
        buf.append("<!--"+comment+"-->");
        return this;
    }

    /**
     * ends the given element.
     *
     * @param name     name of the element.
     */
    public XMLBuilder endElement(String name){
        switch(mode){
            case START_DOCUMENT:
                startElement(name);
                break;
            case END_DOCUMENT:
                startDocument();
                startElement(name);
                break;
            case START_ELEMENT:
                buf.append('>');
                break;
        }
        if(mode==END_ELEMENT) printIndent();
        String curelem = (String)stack.pop();
        if(curelem==null || !curelem.equals(name))
            throw new UnsupportedOperationException("Invalid endElement \""+name+"\": \""+curelem+"\" expected.");
        buf.append("</"+name+">");
        depth--;
        mode = END_ELEMENT;
        return this;
    }

    /**
     * tell to complete the document.
     */
    public void endDocument(){
        switch(mode){
            case END_DOCUMENT:
                startDocument();
                break;
        }
        while(stack.size()!=0) endElement((String)stack.peek());
        mode=END_DOCUMENT;
    }

    /**
     * returns the xml string constructed.
     *
     * @return
     */
    public String getXMLDocument(){
        if(mode!=END_DOCUMENT) endDocument();
        return buf.toString();
    }

    /**
     * adds an element with given name. means it will add the starting tag and ending tag.
     *
     * @param name       name of the element.
     * @param atts       attributes, can be null.
     * @param text       text to be added if any. can be null
     * @param cdata      tells whether the text should be enclosed in CDATA section.
     *                   false will replace the special characters by their entities.
     */
    public XMLBuilder addElement(String name, String[][] atts, String text, boolean cdata){
        startElement(name);
        if(atts!=null){
            for(int i=0; i<atts.length; i++) addAttribute(atts[i][0], atts[i][1]);
        }
        if(text!=null) addText(text, cdata);
        endElement(name);
        return this;
    }

    /**
     * starts element of given name along with specified attrs.
     *
     * @param name        name of the element
     * @param atts        attributes. can be null.
     */
    public XMLBuilder startElement(String name, String[][] atts){
        startElement(name);
        if(atts!=null){
            for(int i=0; i<atts.length; i++) addAttribute(atts[i][0], atts[i][1]);
        }
        return this;
    }

    /**
     * resets this builder.
     * if this instance is used to create multiple xml documents then make sure that
     * reset() is called before creating the document.
     */
    public void reset(){
        buf.setLength(0);
        stack.clear();
        mode = END_DOCUMENT;
        depth = -1;
    }

    public int getLength(){
        return buf.length();
    }

    /**
     * Tests the builder
     *
     * @param args          not used.
     */
    public static void main(String[] args){
        XMLBuilder doc = new XMLBuilder();
        doc.startDocument();
        doc.startElement("xyz");
        doc.addAttribute("ddd", "kkkk");
        doc.startElement("abc");
        doc.addText("text", false);
        doc.endElement("abc");
        doc.endElement("xyz");
        doc.endDocument();
        System.out.println(doc.getXMLDocument());
    }
}