/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.xml.stax;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.*;
import java.util.Hashtable;
import java.util.Stack;

public class FioranoStaxParser extends StreamReaderDelegate {
    // This is the internal parser instance, which will be move forward while parsing.
    private XMLStreamReader cursor = null;

    // Current marked element.
    private Marker marker;

    // Holds all the markers.
    private Stack markers = null;

    //True, if parser reached end of the marker.
    private boolean halt = false;

    //True, if document is parsed completely
    private boolean documentParsed = false;

    private boolean consumeMarkedEvent = false;

    private final String FIORANO_EXCEPTION_DESC = "[Fiorano Marker Exception] ";
    private InputStream inputStream = null;
    private Reader reader = null;

    //STAX XML Input factory instance.
    private static XMLInputFactory staxInputFactory = null;

    //STAX XML Output factory instance.
    private static XMLOutputFactory staxOutputFactory = null;

    /**
     * This will create the STAX parser for the given Input Stream.
     *
     * @param is Input stream that will be parsed
     * @throws javax.xml.stream.XMLStreamException
     *
     */
    public FioranoStaxParser(InputStream is) throws XMLStreamException {
        this(getStaxInputFactory().createXMLStreamReader(is));
        this.inputStream = is;

    }

    /**
     * This will create the STAX parser for the given Input Stream.
     *
     * @param reader reader that will be parsed
     * @throws XMLStreamException
     */
    public FioranoStaxParser(Reader reader) throws XMLStreamException {
        this(getStaxInputFactory().createXMLStreamReader(reader));
        this.reader = reader;
    }

    /**
     * Initialize the parser with the specified cursor.
     *
     * @param cursor Reader on which parser wil parse data.
     */
    public FioranoStaxParser(XMLStreamReader cursor) {
        super(cursor);
        this.cursor = cursor;
        markers = new Stack();
    }

    /**
     * This will move the cursor to the specified element in the document.
     *
     * @param elementName Element to be marked.
     * @return true, if element can be marked or located else return false.
     * @throws XMLStreamException Throws excdeption if the given element desn't exist.
     */
    public boolean markCursor(String elementName) throws XMLStreamException {
        while (hasNext()) {
            if (cursor.getEventType() == XMLStreamConstants.START_DOCUMENT)
                next();

            boolean val = cursor.isStartElement();
            if (val && cursor.getLocalName().equalsIgnoreCase(elementName)) {
                //This will be removed from the stack when it reaches ist End Tag.
                marker = new Marker(elementName, depth);
                markers.push(marker);
                return true;
            } else
                next();
        }
        return false;
    }


    /**
     * This will reset the cursor and allow cursor to move furthur.
     */
    public void resetCursor() {
        //No markers.
        if (markers.empty())
            return;
        //Marker is valid
        if (markers.peek().equals(marker)) {
            markers.pop();
            if (!markers.empty())
                marker = (Marker) markers.peek();
            else
                marker = null;
        }
        //Already reached the mark. So let parser to continue furthur.
        halt = false;
    }

    /**
     * This will cheeck if the cursor reached end of docuement.
     *
     * @return true, if document is parsed. Else return false.
     */
    public boolean allElementsParsed() {
        return documentParsed;
    }

    /**
     * Will check if there are any more events that can be parsed.
     *
     * @return true, if cursor can be moved forward.
     *         false, if the cursor is already at end of marked tag or document.
     * @throws XMLStreamException
     */
    public boolean hasNext() throws XMLStreamException {
        // Already reached END_OF_MARKER, so it will return false.
        if (halt)
            return false;

        // Will return false, only when the cursor is already at END_OF_DOCUMENT.
        return super.hasNext();
    }

    int depth = 0;

    /**
     * This will move the cursor to next event.
     *
     * @return Event type.
     * @throws XMLStreamException
     */
    public int next() throws XMLStreamException {
        // Cannot iterate till the cursor is reset.
        if (halt)
            throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Cursor reached end of the marked element.");

        // ONETIME CHECK: This will consume the event, to compensate the event used by calling markParser
        if (consumeMarkedEvent) {
            consumeMarkedEvent = false;
            return cursor.getEventType();
        }

        // This will throw XMLStreamException if the cursor already at the END_OF_DOCUMENT.
        int eventType = super.next();

        if (eventType == XMLStreamConstants.END_ELEMENT) {
            // Check if it reaches end of marked Element
            if (marker != null && super.getLocalName().equalsIgnoreCase(marker.name) && depth == marker.depth)
                halt = true;
        }
        if (eventType == XMLStreamConstants.START_ELEMENT) {
            depth++;
        } else if (eventType == XMLStreamConstants.END_ELEMENT) {
            depth--;
        }

        if (eventType == XMLStreamConstants.END_DOCUMENT) {
            halt = true;
            documentParsed = true;
        }

        return eventType;
    }

    /**
     * This will move cursor to next tag (To start or end element)
     *
     * @return Returns event type (Start/End element)
     * @throws XMLStreamException Throws exception, if cursor reaches either marked element or end of document.
     */
    public int nextTag() throws XMLStreamException {
        // Will retrun false if cursor is at end of the marked element or document.
        if (hasNext()) {
            int eventType = next();
            while (!halt && ((eventType == XMLStreamConstants.CHARACTERS && isWhiteSpace()) // skip whitespace
                    || (eventType == XMLStreamConstants.CDATA && isWhiteSpace())
                    // skip whitespace
                    || eventType == XMLStreamConstants.SPACE
                    || eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
                    || eventType == XMLStreamConstants.COMMENT)
                    ) {
                eventType = next();
            }
            return eventType;

        } else
            throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Cursor reached either end of marked element or document.");
    }

    /**
     * This will move the cursor to the start of next element.
     *
     * @return true, if the cursor moved to start or next element. false, if the cursor is already halted (reached end of marked element) or reached end of document.
     * @throws XMLStreamException Will throw exception if this is invoked when cursor already at end of document or marked element.
     */
    public boolean nextElement() throws XMLStreamException {
        if (!hasNext()) {
            return false;
        }

        int eventType = nextTag();
        while ((eventType != XMLStreamConstants.START_ELEMENT) && !halt) {
            if (hasNext())
                eventType = nextTag();
        }
        return !halt;
    }

    /**
     * This will move cursor to the specified element in the XML tree.
     *
     * @param elementName Next cursor location.
     * @return true, if cursor moved to specified element. false, if cursor cannot locate given element or reaced end of marked or end of document.
     */
    public boolean getNextElement(String elementName) {
        try {
            markCursor(elementName);
            return nextElement();
        }
        catch (XMLStreamException xse) {
            xse.printStackTrace();
            return false;
        }
    }

    /**
     * This will skip the given element structure and place the cursor atnext element.
     *
     * @param elementName
     */
    public void skipElement(String elementName) throws XMLStreamException {
        Stack nestedElements = new Stack();
        if (markCursor(elementName)) {
            nestedElements.push(elementName);
            while (nestedElements.size() > 0) {
                while (nextElement()) {
                    if (cursor.getLocalName().equalsIgnoreCase(elementName))
                        nestedElements.push(elementName);
                }
                nestedElements.pop();
                // Reset Halt
                halt = false;
            }
            resetCursor();
        } else
            throw new XMLStreamException(FIORANO_EXCEPTION_DESC + "Element " + elementName + " doesn't exist in the XML tree.");

    }

    public String getText() {

        try {
            if (hasNext()) {
                //Move cursor to next ... Consume excpetion if any.
                StringBuffer sb = new StringBuffer();
                int event = next();
                while (cursor.getEventType() == XMLStreamConstants.CHARACTERS || cursor.getEventType() == XMLStreamConstants.SPACE) {
                    sb.append(super.getText());
                    event = next();
                }
                return sb.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This will return the CData assciated with the current element.
     *
     * @return Return CData of the current element, or null.
     */
    private String getCDataValue() {
        try {
            if (hasNext()) {
                //Move cursor to next ... Consume excpetion if any.
                int event = next();
                if (event == XMLStreamConstants.CDATA) {
                    StringBuffer sb = new StringBuffer();
                    do {
                        sb.append(super.getText());
                        next();
                    } while (cursor.getEventType() == XMLStreamConstants.CDATA);
                    return sb.toString();
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    return getCDataValue();
                } else
                    return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCData() throws XMLStreamException {
        StringBuffer cdata = new StringBuffer();

        int eventType;
        do {
            String cDataValue = getCDataValue();
            if (cDataValue != null) {
                cdata.append(cDataValue);
            }
            eventType = peak();
        } while (eventType == XMLStreamConstants.CDATA);
        String returnVal = cdata.toString();
        return returnVal.trim().equalsIgnoreCase("null") ? null : returnVal;
    }

    private int peak() throws XMLStreamException {
        if (!hasNext())
            return -1;
        int eventType = next();
        consumeMarkedEvent = true;
        return eventType;

    }

    /**
     * This will return the attributes of the current element.
     * NOTE: This method MUST be called before any other data related methods on this element.
     *
     * @return Hashtabale containing attributes or null.
     * @throws XMLStreamException Exception
     */
    public Hashtable getAttributes() throws XMLStreamException {
        if ((cursor.getEventType() == XMLStreamConstants.START_ELEMENT) || (cursor.getEventType() == XMLStreamConstants.ATTRIBUTE)) {
            if (getAttributeCount() > 0) {
                int attributeCount = cursor.getAttributeCount();
                Hashtable attrs = new Hashtable(attributeCount);
                for (int i = 0; i < attributeCount; i++) {
                    attrs.put(cursor.getAttributeLocalName(i), cursor.getAttributeValue(i));
                }
                return attrs;
            } else
                return null;

        } else
            throw new XMLStreamException("[Fiorano STAX Exception] Current state is not among the states START_ELEMENT , ATTRIBUTEvalid for getAttributes.");
    }


    /**
     * This will close all the open connections.
     *
     * @throws XMLStreamException Exception if any
     */
    public void disposeParser() throws XMLStreamException {
        markers = null;
        marker = null;
        cursor.close();
        cursor = null;
        try {
            if (inputStream != null)
                inputStream.close();
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            //nothing to do here.
        }
    }

    /**
     * This will print the event name for the given event type.
     *
     * @param eventType Event Type.
     */
    public void printEventType(int eventType) {
        printEventType(null, eventType);
    }


    /**
     * This will print the event name alongwith the description provided for the specified event.
     *
     * @param data      Any description to be prnted alongwith the event name
     * @param eventType Event Type
     */
    public void printEventType(String data, int eventType) {
        if (data != null) {
            System.out.print(data);
        }
        switch (eventType) {
            case XMLStreamConstants.START_ELEMENT:
                System.out.println("START_ELEMENT: " + XMLStreamConstants.START_ELEMENT);
                return;
            case XMLStreamConstants.END_ELEMENT:
                System.out.println("END_ELEMENT: " + XMLStreamConstants.END_ELEMENT);
                return;
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                System.out.println("PROCESSING_INSTRUCTION: " + XMLStreamConstants.PROCESSING_INSTRUCTION);
                return;
            case XMLStreamConstants.CHARACTERS:
                System.out.println("CHARACTERS: " + XMLStreamConstants.CHARACTERS);
                return;
            case XMLStreamConstants.COMMENT:
                System.out.println("COMMENT: " + XMLStreamConstants.COMMENT);
                return;
            case XMLStreamConstants.START_DOCUMENT:
                System.out.println("START_DOCUMENT: " + XMLStreamConstants.START_DOCUMENT);
                return;
            case XMLStreamConstants.END_DOCUMENT:
                System.out.println("END_DOCUMENT: " + XMLStreamConstants.END_DOCUMENT);
                return;
            case XMLStreamConstants.ENTITY_REFERENCE:
                System.out.println("ENTITY_REFERENCE: " + XMLStreamConstants.ENTITY_REFERENCE);
                return;
            case XMLStreamConstants.ATTRIBUTE:
                System.out.println("ATTRIBUTE: " + XMLStreamConstants.ATTRIBUTE);
                return;
            case XMLStreamConstants.DTD:
                System.out.println("DTD: " + XMLStreamConstants.DTD);
                return;
            case XMLStreamConstants.CDATA:
                System.out.println("CDATA: " + XMLStreamConstants.CDATA);
                return;
            case XMLStreamConstants.SPACE:
                System.out.println("SPACE: " + XMLStreamConstants.SPACE);
                return;
        }
        System.out.println("UNKNOWN_EVENT_TYPE , " + eventType);
    }

    /**
     * This will parse the complete document.
     *
     * @param fis FileInputStream of the XML.
     * @throws Exception
     */
    public static void parseDocument(FileInputStream fis, String elementName) throws Exception {
        FioranoStaxParser parser = new FioranoStaxParser(fis);
        //Parse Document
        if (elementName != null) {
            if (parser.markCursor(elementName)) {
                while (parser.nextElement()) {
                    parser.printEventType(parser.getEventType());
                }
            }
        }
        parser.disposeParser();
        parser = null;
    }

    /**
     * This class represent the Attribute (Name-Value pair)
     */
    class ElementAttribute {
        String name, value;

        public ElementAttribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }


    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java FioranoStaxParser <XML file>");
            return;
        }
        //FioranoStaxParser.parseDocument( new FileInputStream (args[0]), args[1]);
        FioranoStaxParser parser = new FioranoStaxParser(new FileInputStream(args[0]));
        if (parser.markCursor("b")) {
            System.out.println("Element Name: B: " + parser.getLocalName());
            while (parser.nextElement()) {
                System.out.println("Element Name: C " + parser.getLocalName());
                if (parser.getLocalName().equalsIgnoreCase("c")) {
                    parser.markCursor("c");
                    while (parser.nextElement()) {
                        System.out.println("Element Name: D " + parser.getLocalName());
                        if (parser.getLocalName().equalsIgnoreCase("d")) {
                            parser.markCursor("d");
                            while (parser.nextElement()) {
                                System.out.println("Element Name: E " + parser.getLocalName());
                            }
                            parser.resetCursor();
                        }
                    }
                    parser.resetCursor();
                }
            }
            parser.resetCursor();
        }
    }

    public String getLocalName() {
        String localName = super.getLocalName();
        return localName == null ? "" : localName;
    }

    /**
     * This returns the element node in String format.
     * This should be used if the cursor is marked on a valid XML element.
     * If it is invalid then an exception will be thrown.
     * For Example if the cursor is at point a in the below xml, and you call this method,
     * you will get the whole XML between <a></a> incuding <a> and </a>
     * <a><b>abc</b><c>def</c></a>
     *
     * @param elementName element node name
     * @return
     * @throws XMLStreamException
     */
    public String getElementAsXML(String elementName) throws XMLStreamException {
        XMLEventReader eventReader = staxInputFactory.createXMLEventReader(cursor);
        StringWriter stringWriter = new StringWriter();
        XMLEventWriter eventWriter = getStaxOutputFactory().createXMLEventWriter(stringWriter);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            eventWriter.add(event);
            if (event.isEndElement() && elementName.equals(event.asEndElement().getName().getLocalPart())) {
                eventReader = null;
                eventWriter.flush();
                eventWriter = null;
                break;
            }
        }
        return stringWriter.toString();
    }

    /**
     * This method returns the content (of any type. need not be a valid XML) between a particular start and end tags by taking start tag as a parameter.
     *
     * @param elementName element node name
     * @return
     * @throws XMLStreamException
     */
    public String getElementContent(String elementName) throws XMLStreamException {
        XMLEventReader eventReader = staxInputFactory.createXMLEventReader(cursor);
        StringWriter stringWriter = new StringWriter();
        XMLEventWriter eventWriter = getStaxOutputFactory().createXMLEventWriter(stringWriter);

        int i = 0;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            String startTag = null;
            String endTag = null;
            if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                startTag = event.asStartElement().getName().getLocalPart();
            }
            if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
                endTag = event.asEndElement().getName().getLocalPart();
            }
            if (!(elementName.equals(startTag) || elementName.equals(endTag))) {
                if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    i++;
                }
                if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
                    i--;
                }
                if (i == 0 && event.getEventType() == XMLStreamConstants.CHARACTERS) {
                    stringWriter.write(event.asCharacters().getData());
                    continue;
                }
                eventWriter.add(event);
                if (i == 0) {
                    eventWriter.flush();
                    eventWriter = getStaxOutputFactory().createXMLEventWriter(stringWriter);
                }
            }
            if (event.isEndElement() && elementName.equals(endTag)) {
                eventReader = null;
                eventWriter.flush();
                eventWriter = null;
                break;
            }
        }
        return stringWriter.toString();
    }

    /**
     * Returns the text content. For example if the input message is
     * String inputMessage = "<Query>\n" +
     * "sel&lt;<![CDATA[ec]]>&gt;t *\n" +
     * "</Query>\n";
     * cursor.getTextContent(); will return string "sel<ec>t *"
     * Can be used as an alternative for node.getNodeValue() in DOM API.
     *
     * @return
     * @throws XMLStreamException
     */
    public String getTextContent() throws XMLStreamException {
        try {
            if (markCursor(cursor.getLocalName())) {
                StringBuffer sb = new StringBuffer();
                while (hasNext()) {
                    next();
                    if (cursor.getEventType() == XMLStreamConstants.CHARACTERS || cursor.getEventType() == XMLStreamConstants.SPACE || cursor.getEventType() == XMLStreamConstants.CDATA) {
                        sb.append(super.getText());
                    }
                }
                resetCursor();
                return sb.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Same as getElementAsXML(String elementName) with an option to specify namespace support.
     * For eg: if the input XML is
     * <?xml version="1.0" encoding="UTF-8"?>
     * <ns1:HTTPResponse xmlns:ns1="http://www.lottery.org" xmlns:ns2="http://www.w3.org/2001/XMLSchema">
     * <Headers/>
     * <XMLData>
     * <ns1:LotteryDrawings>
     * <ns1:Drawing>
     * <ns1:Week>Week</ns1:Week>
     * </ns1:Drawing>
     * </ns1:LotteryDrawings>
     * </XMLData>
     * </ns1:HTTPResponse>
     * and the cursor is marked at "LotteryDrawings" then getElementAsXML(true) will return
     * <ns1:LotteryDrawings xmlns:ns1="http://www.lottery.org">
     * <ns1:Drawing>
     * <ns1:Week>Week</ns1:Week>
     * <ns1:Numbers>Numbers</ns1:Numbers>
     * </ns1:Drawing>
     * </ns1:LotteryDrawings>
     *
     * @param nameSpaceSupport
     * @return
     * @throws XMLStreamException
     */
    public String getElementAsXML(boolean nameSpaceSupport) throws XMLStreamException {
        if (nameSpaceSupport) {
            getStaxOutputFactory().setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
        }
        return getElementAsXML(cursor.getLocalName());
    }

    /**
     * This will returns the instance of the STAX XMLInputFactory and will initialze the properties to the factory.
     *
     * @return This will return the instance of STAX XmlInputFactory.
     */
    public static XMLInputFactory getStaxInputFactory() {
        if (staxInputFactory == null) {
            try {
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
                staxInputFactory = XMLInputFactory.newInstance();
                staxInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
                staxInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                staxInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return staxInputFactory;
    }


    /**
     * This will returns the instance of the STAX XMLOutputFactory and will initialze the properties to the factory.
     *
     * @return This will return the instance of STAX XMLOutputFactory.
     */
    public static XMLOutputFactory getStaxOutputFactory() {
        if (staxOutputFactory == null) {
            try {
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
                staxOutputFactory = XMLOutputFactory.newInstance();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return staxOutputFactory;
    }

    private static class Marker {
        private int depth = -1;
        private String name;

        private Marker(String name, int depth) {
            this.depth = depth;
            this.name = name;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Marker marker = (Marker) o;

            if (depth != marker.depth) return false;
            return !(name != null ? !name.equals(marker.name) : marker.name != null);

        }

        public int hashCode() {
            int result;
            result = depth;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }
}

