/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import com.fiorano.openesb.utils.ClarkName;
import com.fiorano.openesb.utils.NamespaceSupportReader;
import com.fiorano.openesb.utils.SAXUtil;
import com.fiorano.openesb.utils.XNamespaceSupport;
import org.xml.sax.*;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TreeSet;

public abstract class AbstractXMLReader implements XMLReader, SAXFeatures, SAXProperties, ContentHandler, ErrorHandler, DTDHandler, EntityResolver, LexicalHandler, DeclHandler {

    public static final String SUGGESTED_NAMESPACE_PREFIXES = "SuggestedNamespacePrefixes"; //NOI18N

    /*-------------------------------------------------[ Handlers ]---------------------------------------------------*/
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private LexicalHandler lexicalHandler;
    private DeclHandler declHandler;

    protected AbstractXMLReader(){
        currentFeatures.add(SAXFeatures.NAMESPACES);
        featureRules.put(SAXFeatures.NAMESPACES, MUST_FEATURE);
        featureRules.put(SAXFeatures.NAMESPACE_PREFIXES, FLAG_FEATURE);
    }

    public void setContentHandler(ContentHandler contentHandler){
        this.contentHandler = contentHandler;
    }
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public void setDTDHandler(DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }
    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }

    public void setDeclHandler(DeclHandler declHandler) {
        this.declHandler = declHandler;
    }
    public DeclHandler getDeclHandler() {
        return this.declHandler;
    }

    /*-------------------------------------------------[ Features ]---------------------------------------------------*/

    protected static final Integer MUST_FEATURE = new Integer(1);
    protected static final Integer FLAG_FEATURE = new Integer(2);

    protected final TreeSet currentFeatures = new TreeSet();
    protected final HashMap featureRules = new HashMap();

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        Object rule = featureRules.get(name);
        if(rule==null)
            throw new SAXNotRecognizedException(name);
        return rule.equals(MUST_FEATURE)
                ? true
                : currentFeatures.contains(name);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        Object rule = featureRules.get(name);
        if(rule==null)
            throw new SAXNotRecognizedException(name);
        if(rule.equals(MUST_FEATURE) && value==false)
            throw new SAXNotSupportedException(name);
        if(value)
            currentFeatures.add(name);
        else
            currentFeatures.remove(name);
    }

    /*-------------------------------------------------[ Properties ]---------------------------------------------------*/

    public Object getProperty(String name)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if ((LEXICAL_HANDLER.equals(name)) ||
            (LEXICAL_HANDLER_ALT.equals(name))) {
            return this.getLexicalHandler();
        }
        else {
            if ((DECL_HANDLER.equals(name)) ||
                (DECL_HANDLER_ALT.equals(name))) {
                return this.getDeclHandler();
            }else if(SUGGESTED_NAMESPACE_PREFIXES.equals(name))
                return nsSupport.getSuggestedPrefixes();
            else
                throw new SAXNotRecognizedException(name);
        }
    }

    public void setProperty(String name, Object value)
                throws SAXNotRecognizedException, SAXNotSupportedException {
        if ((LEXICAL_HANDLER.equals(name)) ||
            (LEXICAL_HANDLER_ALT.equals(name))) {
            setLexicalHandler((LexicalHandler)value);
        }
        else {
            if ((DECL_HANDLER.equals(name)) ||
                (DECL_HANDLER_ALT.equals(name))) {
                setDeclHandler((DeclHandler)value);
            }else if(SUGGESTED_NAMESPACE_PREFIXES.equals(name))
                nsSupport.setSuggestedPrefixes((Hashtable)value);
            else
                throw new SAXNotRecognizedException(name);
        }
    }

    /*-------------------------------------------------[ Content Handler Interface ]---------------------------------------------------*/

    protected boolean skipDocumentStartEnd = false;

    public void setDocumentLocator (Locator locator){
        if(contentHandler!=null)
            contentHandler.setDocumentLocator(locator);
    }
    public void startDocument () throws SAXException {
        nsReader.startDocument();
        if(skipDocumentStartEnd)
            return;
        if(contentHandler!=null)
            contentHandler.startDocument();
    }
    public void endDocument() throws SAXException {
        if(skipDocumentStartEnd)
            return;
        if(contentHandler!=null)
            contentHandler.endDocument();
    }
    public void startPrefixMapping (String prefix, String uri) throws SAXException {
        nsReader.startPrefixMapping(prefix, uri);
        if(contentHandler!=null)
            contentHandler.startPrefixMapping(prefix, uri);

        if(getFeature(SAXFeatures.NAMESPACE_PREFIXES)){
            String attrQName = "xmlns" + (prefix.length()>0 ? ":" : "") + prefix;  //NOI18N
            atts.addAttribute("", prefix, attrQName, "CDATA", uri); //NOI18N
        }
    }
    public void endPrefixMapping(String prefix) throws SAXException {
        if(contentHandler!=null)
            contentHandler.endPrefixMapping(prefix);
    }

    public void startElement (String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//        Enumeration enum = nsSupport.getDeclaredPrefixes();
//        while(enum.hasMoreElements()){
//            String prefix = (String) enum.nextElement();
//            String uri = nsSupport.getURI(prefix);
//            if(getFeature(SAXFeatures.NAMESPACE_PREFIXES)){
//                String attrQName = "xmlns" + (prefix.length()>0 ? ":" : "") + prefix;
//                this.atts.addAttribute("", prefix, attrQName, "CDATA", uri);
//            }
//        }
        nsReader.startElement(namespaceURI, localName, qName, atts);

        if(contentHandler!=null)
            contentHandler.startElement(namespaceURI, localName, qName, atts);
        stack.push(new String[]{namespaceURI, localName, qName});
        this.atts.clear();
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        nsReader.endElement(namespaceURI, localName, qName);
        if(contentHandler!=null)
            contentHandler.endElement(namespaceURI, localName, qName);
        stack.pop();
    }

    public void characters (char ch[], int start, int length) throws SAXException {
        if(contentHandler!=null)
            contentHandler.characters(ch, start, length);
    }

    public void ignorableWhitespace (char ch[], int start, int length) throws SAXException {
        if(contentHandler!=null)
            contentHandler.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if(contentHandler!=null)
            contentHandler.processingInstruction(target, data);
    }

    public void skippedEntity (String name) throws SAXException {
        if(contentHandler!=null)
            contentHandler.skippedEntity(name);
    }

    /*-------------------------------------------------[ Error Handler Interface ]---------------------------------------------------*/

    public void warning (SAXParseException exception) throws SAXException {
        if(errorHandler!=null)
            errorHandler.warning(exception);
    }
    public void error (SAXParseException exception) throws SAXException {
        if(errorHandler!=null)
            errorHandler.error(exception);
    }
    public void fatalError (SAXParseException exception) throws SAXException {
        if(errorHandler!=null)
            errorHandler.fatalError(exception);
    }

    /*-------------------------------------------------[ DTD Hanlder Interface]---------------------------------------------------*/

    public void notationDecl (String name, String publicId, String systemId) throws SAXException {
        if(dtdHandler!=null)
            dtdHandler.notationDecl(name, publicId, systemId);
    }
    public void unparsedEntityDecl (String name, String publicId, String systemId, String notationName) throws SAXException {
        if(dtdHandler!=null)
            dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
    }

    /*-------------------------------------------------[ Entity Resolver Interface ]---------------------------------------------------*/

    public InputSource resolveEntity (String publicId, String systemId) throws SAXException, IOException {
        if(entityResolver!=null)
            entityResolver.resolveEntity(publicId, systemId);
        return null;
    }

    /*-------------------------------------------------[ Lexical Handler Interface ]---------------------------------------------------*/

    public void startDTD (String name, String publicId, String systemId) throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.startDTD(name, publicId, systemId);
    }
    public void endDTD () throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.endDTD();
    }
    public void startEntity (String name) throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.startEntity(name);
    }
    public void endEntity (String name) throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.endEntity(name);
    }
    public void startCDATA () throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.startCDATA();
    }
    public void endCDATA() throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.endCDATA();
    }
    public void comment (char ch[], int start, int length) throws SAXException {
        if(lexicalHandler!=null)
            lexicalHandler.comment(ch, start, length);
    }

    /*-------------------------------------------------[ DeclHandler ]---------------------------------------------------*/

    public void elementDecl (String name, String model) throws SAXException {
        if(declHandler!=null)
            declHandler.elementDecl(name, model);
    }
    public void attributeDecl (String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        if(declHandler!=null)
            declHandler.attributeDecl(eName, aName, type, valueDefault, value);
    }
    public void internalEntityDecl (String name, String value) throws SAXException {
        if(declHandler!=null)
            declHandler.internalEntityDecl(name, value);
    }
    public void externalEntityDecl (String name, String publicId, String systemId) throws SAXException {
        if(declHandler!=null)
            declHandler.externalEntityDecl(name, publicId, systemId);
    }

    /*-------------------------------------------------[ Helper methods ]---------------------------------------------------*/

    private AttributesImpl atts = new AttributesImpl();
    protected Stack stack = new Stack();

    public AttributesImpl getAttributes(){
        return atts;
    }

    public AbstractXMLReader startElement(String name) throws SAXException {
        startElement("", name);  //NOI18N
        return this;
    }

    public AbstractXMLReader startElement(String namespaceURI, String name) throws SAXException {
        startElement(namespaceURI, name, declareQName(namespaceURI, name), atts);
        return this;
    }

    public AbstractXMLReader addText(String text, boolean escape) throws SAXException {
        if(!escape)
            processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, ""); //NOI18N
        ensureCompletion();
        if(text!=null){
            char ch[] = text.toCharArray();
            characters(ch, 0, ch.length);
        }
        if(!escape)
            processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, ""); //NOI18N
        return this;
    }

    public AbstractXMLReader addText(String text) throws SAXException {
        return addText(text, true);
    }

    public AbstractXMLReader addCDATA(String text) throws SAXException {
        if(text!=null){
            startCDATA();
            addText(text);
            endCDATA();
        }
        return this;
    }

    public AbstractXMLReader endElement() throws SAXException {
        ensureCompletion();
        String info[] = (String[]) stack.peek();
        endElement(info[0], info[1], info[2]);
        return this;
    }

    // note: element will be added even if text is null
    public AbstractXMLReader addElement(String namespaceURI, String name, String text) throws SAXException {
        startElement(namespaceURI, name);
        addText(text);
        return endElement();
    }

    // note: element will be not be added even if text is null
    public AbstractXMLReader smartElement(String namespaceURI, String name, String text) throws SAXException {
        if(text!=null)
            addElement(namespaceURI, name, text);
        return this;
    }

    public AbstractXMLReader addElement(String name, String text) throws SAXException {
        return addElement("", name, text);  //NOI18N
    }

    // ensures the completion in case splitting
    // stardard callback to mulple method calls
    private void ensureCompletion() throws SAXException {
    }

    public void addAttribute(String namespaceURI, String localName, String qName, String type, String value){
        atts.addAttribute(namespaceURI, localName, qName, type, value!=null?value:""); //NOI18N
    }

    public void addAttribute(String namespaceURI, String localName, String value) throws SAXException {
        addAttribute(namespaceURI, localName, declareQName(namespaceURI, localName), "CDATA", value);  //NOI18N
    }

    public void addAttribute(String name, String value){
        addAttribute("", name, name, "CDATA", value);  //NOI18N
    }

    // note: attribute will not be added if text is null
    public void smartAttribute(String name, String value){
        if(value!=null)
            addAttribute(name, value);
    }

    public void addClarkAttribute(String clarkName, String value) throws SAXException {
        String parts[] = ClarkName.getParts(clarkName);
        addAttribute(parts[0], parts[1], value);
    }
    
    public void comment(String comment) throws SAXException {
        comment(comment.toCharArray(), 0, comment.length());
    }

    /*-------------------------------------------------[ NameSpaceSupport ]---------------------------------------------------*/

    private XNamespaceSupport nsSupport = new XNamespaceSupport(){
        protected String suggestPrefix(String namespaceURI){
            return AbstractXMLReader.this.suggestPrefix(namespaceURI);
        }
    };
    NamespaceSupportReader nsReader = new NamespaceSupportReader(nsSupport);

    public NamespaceSupport getNamespaceSupport(){
        return nsSupport;
    }


    public void declarePrefix(String prefix, String namespaceURI) throws SAXException {
          startPrefixMapping(prefix, namespaceURI);
    }

    protected String suggestPrefix(String namespaceURI){
        return null;
    }

    // declare a suitable prefix for the given uri if not already defined.
    public String declarePrefix(String namespaceURI) throws SAXException {
        String prefix = nsSupport.findPrefix(namespaceURI);
        if(prefix==null){
            prefix = nsSupport.getSuggestedPrefix(namespaceURI);
            startPrefixMapping(prefix, namespaceURI);
        }
        return prefix;
    }

    public String declareQName(String namespaceURI, String name) throws SAXException {
//        if(namespaceURI==null || namespaceURI.equals(""))  //NOI18N
//            return name;
        String prefix = declarePrefix(namespaceURI);
        return prefix==null || prefix.length()==0 ? name : prefix+":"+name;  //NOI18N
    }
    
    public String getPrefix(String uri){
        if(uri==null)
            return null;
        return nsSupport.findPrefix(uri);
    }

    public String getQName(String clarkName){
        String[] parts = ClarkName.getParts(clarkName);
        return getQName(parts[0], parts[1]);
    }

    public String getQName(String namespaceURI, String name){
        if(namespaceURI==null || namespaceURI.equals(""))  //NOI18N
            return name;
        String prefix = getPrefix(namespaceURI);
        if(prefix==null)
            throw new IllegalArgumentException("Namespace \""+namespaceURI+"\" is not bound to any prefix");
        return prefix==null || prefix.length()==0 ? name : prefix+":"+name;  //NOI18N
    }

    /*-------------------------------------------------[ xml snippet support ]---------------------------------------------------*/

    public AbstractXMLReader addXML(InputSource inputSource, boolean ignoreRootElement) throws SAXException {
        try{
            NestedElementReader nestedReader = new NestedElementReader(ignoreRootElement);
            nestedReader.setContentHandler(this);
            nestedReader.setDTDHandler(this);
            nestedReader.setEntityResolver(this);
            nestedReader.setErrorHandler(this);
            nestedReader.parse(inputSource);
            return this;
        } catch(ParserConfigurationException e){
            throw new SAXException(e);
        } catch(IOException e){
            throw new SAXException(e);
        }
    }

    private class NestedElementReader extends XMLFilterImpl {
        int depth = 0;
        boolean ignoreRootElement = false;

        NestedElementReader(boolean ignoreRootElement) throws ParserConfigurationException, SAXException {
            boolean nsAware = currentFeatures.contains(SAXFeatures.NAMESPACES);
            boolean nsPrefixes = currentFeatures.contains(SAXFeatures.NAMESPACE_PREFIXES);
            boolean validating = currentFeatures.contains(SAXFeatures.VALIDATION);
            SAXParserFactory factory = SAXUtil.createSAXParserFactory(nsAware, nsPrefixes, validating);
            SAXParser saxParser = factory.newSAXParser();
            setParent(saxParser.getXMLReader());
            this.ignoreRootElement = ignoreRootElement;
        }

        public void setDocumentLocator(Locator locator){}

        public void startDocument(){}

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            depth++;
            if(ignoreRootElement && depth==1)
                return;
            super.startElement(uri, localName, qName, atts);
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            depth--;
            if(ignoreRootElement && depth==0)
                return;
            super.endElement(uri, localName, qName);
        }

        public void endDocument(){}
    };
}