/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.exception.FioranoException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class XMLUtils
{
    // Document builder factory reference
    private static DocumentBuilderFactory m_dbf;

    // Document build reference
    private static DocumentBuilder m_db;

    // added by sudhir used for dtd validation in services
    // Document builder factory reference (include dtd validation)
    private static DocumentBuilderFactory m_vdbf;

    // Validating Document build reference
    private static DocumentBuilder m_vdb;

    // Line Separator string
    private static String m_lineSeparator = "\r\n";


    //Added for Stax Parsing --Sandeep M 9th Nov 2006

        //STAX XML Input factory instance.
    private static XMLInputFactory staxInputFactory = null ;

    //STAX XML Output factory instance.
    private static XMLOutputFactory staxOutputFactory = null ;
    
    /**
     *  Gets the node value as boolean.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsBoolean value
     *@exception  DOMException  Description of the Exception
     */
    public final static boolean getNodeValueAsBoolean(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            return node.getNodeValue() != null
                     && (node.getNodeValue().equalsIgnoreCase("YES") ||
                     node.getNodeValue().equalsIgnoreCase("TRUE"));
        }

        return false;
    }


    /**
     *  Gets the node value as int.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsInt value
     *@exception  DOMException  Description of the Exception
     */
    public final static int getNodeValueAsInt(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            if (node != null)
                return Integer.parseInt(node.getNodeValue());
        }
        return -1;
    }


    /**
     *  Gets the node value as long.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsLong value
     *@exception  DOMException  Description of the Exception
     */
    public final static long getNodeValueAsLong(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            if (node != null)
                return Long.parseLong(node.getNodeValue());
        }
        return -1;
    }


    /**
     *  Gets the node value as float.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsFloat value
     *@exception  DOMException  Description of the Exception
     */
    public final static float getNodeValueAsFloat(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            if (node != null)
                return Float.parseFloat(node.getNodeValue());
        }
        return -1;
    }


    /**
     *  Gets the node value as double.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsDouble value
     *@exception  DOMException  Description of the Exception
     */
    public final static double getNodeValueAsDouble(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            if (node != null)
                return Double.parseDouble(node.getNodeValue());
        }
        return -1;
    }


    /**
     *  Gets the node value as string.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsString value
     *@exception  DOMException  Description of the Exception
     */
    public final static String getNodeValueAsString(Node node)
             throws DOMException
    {
        if (node != null)
        {
            node = node.getFirstChild();
            if (node != null)
                return node.getNodeValue();
        }
        return null;
    }


    /**
     *  Gets the value of the node specified by the given name which lie in the
     *  subtree of the given node.
     *
     *@param  node              Description of the Parameter
     *@param  nodeName          Description of the Parameter
     *@return                   The nodeValueAsString value
     *@exception  DOMException  Description of the Exception
     */
    public final static String getNodeValueAsString(Node node, String nodeName)
             throws DOMException
    {
        if (node != null)
        {
            TreeWalker walker = new TreeWalker(node);
            node = walker.getNextElement(nodeName);
            return getNodeValueAsString(node);
        }
        return null;
    }


    /**
     *  Gets the node value as date.
     *
     *@param  node                          Description of the Parameter
     *@return                               The nodeValueAsDate value
     *@exception  DOMException              Description of the Exception
     *@exception  ParseException  Description of the Exception
     */
    public final static Date getNodeValueAsDate(Node node)
             throws DOMException, ParseException
    {
        if (node == null)
            return null;

        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("DateTimeFormat");

        // Date format
        String format = attr.getNodeValue().trim();
        node = node.getFirstChild();
        if (node != null)
        {
            String date = node.getNodeValue().trim();
            DateFormat df = new SimpleDateFormat(format);
            return df.parse(date);
        }
        return null;
    }


    /**
     *  Gets the node value as time.
     *
     *@param  node              Description of the Parameter
     *@return                   The nodeValueAsTime value
     *@exception  DOMException  Description of the Exception
     */
    public final static long getNodeValueAsTime(Node node)
             throws DOMException
    {
        if (node == null)
            return -1;

        NamedNodeMap attrs = node.getAttributes();
        Node attr = attrs.getNamedItem("Unit");
        int factor = 1;
        String unit = attr.getNodeValue();
        if (unit.equals("sec"))
        {
            factor = 1000;
        }
        else if (unit.equals("min"))
        {
            factor = 60000;
        }
        else if (unit.equals("hr"))
        {
            factor = 3600000;
        }
        else if (unit.equals("day"))
        {
            factor = 86400000;
        }

        node = node.getFirstChild();
        if (node != null)
        {
            String time = node.getNodeValue().trim();
            return Integer.parseInt(time) * factor;
        }
        return -1;
    }


    /**
     *  Gets the node value from the node of the given name lie in the subtree
     *  of each node of the given node list and set it into the node value list.
     *
     *@param  nodeList          Description of the Parameter
     *@param  nodeName          Description of the Parameter
     *@param  nodeValueList     Description of the Parameter
     *@exception  DOMException  Description of the Exception
     */
    public final static void getNodeValues(NodeList nodeList, String nodeName,
            List nodeValueList)
             throws DOMException
    {
        TreeWalker treeWalker = null;
        Node node = null;
        String nodeValue = null;
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            treeWalker = new TreeWalker(nodeList.item(i));
            node = treeWalker.getNextElement(nodeName);
            nodeValue = getNodeValueAsString(node);
            if (nodeValue != null)
                nodeValueList.add(nodeValue);
        }
    }


    /**
     *  Gets the node value of each node of the given list and set it into the
     *  node value list.
     *
     *@param  nodeList          Description of the Parameter
     *@param  nodeValueList     Description of the Parameter
     *@exception  DOMException  Description of the Exception
     */
    public final static void getNodeValues(NodeList nodeList, List nodeValueList)
             throws DOMException
    {
        String nodeValue = null;
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            nodeValue = getNodeValueAsString(nodeList.item(i));
            if (nodeValue != null)
                nodeValueList.add(nodeValue);
        }
    }


    /**
     *  Gets the node value of each node of the given list and set it into the
     *  node value list.
     *
     *@param  nodeList          Description of the Parameter
     *@return                   The nodeValues value
     *@exception  DOMException  Description of the Exception
     */
    public final static String[] getNodeValues(NodeList nodeList)
             throws DOMException
    {
        String[] nodeValueList = new String[nodeList.getLength()];
        String nodeValue = null;
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            nodeValue = getNodeValueAsString(nodeList.item(i));
            if (nodeValue != null)
                nodeValueList[i] = nodeValue;
        }
        return nodeValueList;
    }


    /**
     *  Returns the current DocumentBuilder.
     *
     *@return                                   The documentBuilder value
     *@exception  ParserConfigurationException  Description of the Exception
     */
    public static DocumentBuilder getDocumentBuilder()
             throws ParserConfigurationException
    {
        if (m_db == null)
            m_db = createDocumentBuilder();
        return m_db;
    }


    /**
     *  Creates a new document builder.
     *
     *@return                                   Description of the Return Value
     *@exception  ParserConfigurationException  Description of the Exception
     */
    public static DocumentBuilder createDocumentBuilder()
             throws ParserConfigurationException
    {
        // Create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory
        DocumentBuilder db = m_dbf.newDocumentBuilder();

        OutputStreamWriter errorWriter = new OutputStreamWriter(System.err);
        db.setErrorHandler(new XMLReadErrorHandler(new PrintWriter(errorWriter, true)));

        return db;
    }

    /**
     *  Gets the validatingDocumentBuilder attribute of the XMLUtils class
     *
     *@return                                   The validatingDocumentBuilder
     *      value
     *@exception  ParserConfigurationException  Description of the Exception
     */
    public static DocumentBuilder getValidatingDocumentBuilder()
             throws ParserConfigurationException
    {
        if (m_vdb == null)
            m_vdb = createValidatingDocumentBuilder();
        return m_vdb;
    }

    /**
     *  Description of the Method
     *
     *@return                                   Description of the Return Value
     *@exception  ParserConfigurationException  Description of the Exception
     */
    public static DocumentBuilder createValidatingDocumentBuilder()
             throws ParserConfigurationException
    {
        // Create a DocumentBuilder that satisfies the constraints
        // specified by the DocumentBuilderFactory
        DocumentBuilder db = m_vdbf.newDocumentBuilder();

        OutputStreamWriter errorWriter = new OutputStreamWriter(System.err);
        db.setErrorHandler(new XMLReadErrorHandler(new PrintWriter(errorWriter, true)));

        return db;
    }


    /**
     *  Saves the Given DOM object as an XML file.
     *
     *@param  root                 Description of the Parameter
     *@param  filename             Description of the Parameter
     *@exception  FioranoException  Description of the Exception
     *@throws  FioranoException    , If Error occurs
     */
    public static void saveXML(String root, String filename)
             throws FioranoException
    {
        try
        {
            File file = new File(filename);
            if (file.exists())
                file.delete();

            FileWriter writer = new FileWriter(file);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(root);
            buffer.flush();
            buffer.close();
        }
        catch (IOException ioe)
        {
            throw new FioranoException(ioe);
        }
    }


    /**
     *  Get DOM document from XML String
     *
     *@param  xmlString            Description of the Parameter
     *@return                      The dOMDocumentFromXML value
     *@exception  FioranoException  Description of the Exception
     */
    public synchronized static Document getDOMDocumentFromXML(String xmlString)
             throws FioranoException
    {
        return getDOMDocumentFromXML(xmlString,false, false);
    }

    /**
     *  Get DOM document from XML String
     *
     *@param  xmlString            Description of the Parameter
     *@return                      The dOMDocumentFromXML value
     *@exception  FioranoException  Description of the Exception
     */
    public synchronized static Document getDOMDocumentFromXML(String xmlString,boolean isValidating, boolean trimSpaces)
             throws FioranoException
    {
        try
        {
            return new DOMAdapter().getDocument(xmlString, isValidating,trimSpaces);
        }
        catch (Throwable se)
        {
            throw new FioranoException( se);
        }
    }

    /**
     *  Gets the validatingDOMDocumentFromXML attribute of the XMLUtils class
     *
     *@param  xmlString            Description of the Parameter
     *@return                      The validatingDOMDocumentFromXML value
     *@exception  FioranoException  Description of the Exception
     */
    public synchronized static Document getValidatingDOMDocumentFromXML(String xmlString)
             throws FioranoException
    {
        try
        {
            return new DOMAdapter().getDocument(xmlString, true);
        }
        catch (Throwable se)
        {
            throw new FioranoException(se);
        }

//
//        // Create a DocumentBuilder that satisfies the constraints
//        // specified by the DocumentBuilderFactory
//        DocumentBuilder db = null;
//        try
//        {
//            db = XMLUtils.getValidatingDocumentBuilder();
//        }
//        catch (ParserConfigurationException pce)
//        {
//            throw new FioranoException(UtilErrorCodes.ERR_EXCEPTION_OCCURRED_ERROR,
//            "Error creating document builder", pce);
//        }
//
//        Document doc = null;
//        //ByteArrayInputStream str = new ByteArrayInputStream(xmlString.getBytes());
//        //InputStream inStream = new BufferedInputStream(str);
//
//        StringReader xmlReader = new StringReader(xmlString);
//        InputSource src = new InputSource(xmlReader);
//
//        // parse the input file
//        try
//        {
//            doc=db.parse(src);
//        }
//        catch (Throwable se)
//        {
//            throw new FioranoException(UtilErrorCodes.ERR_EXCEPTION_OCCURRED_ERROR,
//            "Error parsing document", se);
//        }
//        return doc;
    }


    /**
     *  Gets the nodeObject attribute of the XMLUtils class
     *
     *@param  nodeName             Description of the Parameter
     *@param  nodeValue            Description of the Parameter
     *@param  document             Description of the Parameter
     *@return                      The nodeObject value
     *@exception  FioranoException  Description of the Exception
     */
    public static Node getNodeObject(String nodeName, String nodeValue, Document document)
             throws FioranoException
    {
        if (nodeName != null && nodeValue != null && !nodeValue.trim().equals(""))
        {
            Element node = document.createElement(nodeName);
            Node pcdata = document.createTextNode(nodeValue);
            node.appendChild(pcdata);
            return node;
        }
        else
            return null;
    }


    /**
     *  Adds a feature to the VectorValues attribute of the XMLUtils class
     *
     *@param  nodeName             The feature to be added to the VectorValues
     *      attribute
     *@param  vals                 The feature to be added to the VectorValues
     *      attribute
     *@param  document             The feature to be added to the VectorValues
     *      attribute
     *@param  root0                The feature to be added to the VectorValues
     *      attribute
     *@exception  FioranoException  Description of the Exception
     */
    public static void addVectorValues(String nodeName, Vector vals,
            Document document, Node root0)
             throws FioranoException
    {
        if (vals != null && vals.size() > 0)
        {
            Enumeration _enum = vals.elements();
            Element child = null;
            while (_enum.hasMoreElements())
            {
                String name = (String) _enum.nextElement();
                child = document.createElement(nodeName);
                Node pcdata = document.createTextNode(name);
                child.appendChild(pcdata);
                root0.appendChild(child);
            }
        }
    }


    /**
     *  Gets the attributeAsBoolean attribute of the XMLUtils class
     *
     *@param  elem        Description of the Parameter
     *@param  attribName  Description of the Parameter
     *@return             The attributeAsBoolean value
     */
    public static boolean getAttributeAsBoolean(Element elem, String attribName)
            throws FioranoException
    {
        String atribVal = elem.getAttribute(attribName);
        boolean retVal = false;
        try
        {
            retVal = Boolean.valueOf(atribVal).booleanValue();
        }
        catch (Exception e)
        {
            throw new FioranoException( e);
        }
        return retVal;
    }

    /**
     *  Gets the attributeAsBoolean attribute of the XMLUtils class
     *@return             The attributeAsBoolean value
     */
    public static boolean getAttributeAsBoolean(Node node)
            throws FioranoException
    {
        String atribVal = node.getNodeValue();
        boolean retVal = false;
        try
        {
            retVal = Boolean.valueOf(atribVal).booleanValue();
        }
        catch (Exception e)
        {
            throw new FioranoException( e);
        }
        return retVal;
    }

    /**
     *  Gets the attributeAsLong attribute of the XMLUtils class
     *
     *@return             The attributeAsBoolean value
     */
    public static long getAttributeAsLong(Node node)
            throws FioranoException
    {
        String atribVal = node.getNodeValue();
        long retVal = -1;
        try
        {
            retVal = Long.valueOf(atribVal).longValue();
        }
        catch (Exception e)
        {
            throw new FioranoException( e);
        }
        return retVal;
    }

    /**
     *  Gets the attributeAsInt attribute of the XMLUtils class
     *
     *@return             The attributeAsBoolean value
     */
    public static int getAttributeAsInt(Node node)
            throws FioranoException
    {
        String atribVal = node.getNodeValue();
        int retVal = -1;
        try
        {
            retVal = Integer.parseInt(atribVal);
        }
        catch (Exception e)
        {
            throw new FioranoException( e);
        }
        return retVal;
    }

    /**
     *  Gets the attribute value as string.
     *
     *@param  node              the node from which to get the attribute value
     *@return                   The attribute value as a string
     *@exception  DOMException  if an error occurs while getting the attrubute
     *      value
     */
    public final static String getAttributeValueAsString(Node node)
             throws DOMException
    {
        if (node != null)
        {
            return node.getNodeValue();
        }
        return null;
    }

    /**
     *  Gets the attribute of the node.
     *
     *@param  node              the node from which to get the attribute node
     *@param  name              the attribute name for which to get the node
     *@return                   The attribute node
     *@exception  DOMException  if an error occurs while getting the attribute
     *      node
     */
    public final static Node getAttribute(Node node, String name)
             throws DOMException
    {
        if (node != null && node.hasAttributes())
        {
            NamedNodeMap attrs = node.getAttributes();
            return attrs.getNamedItem(name);
        }
        return null;
    }


    static
    {
        // Create a DocumentBuilderFactory and configure it
        m_dbf = DocumentBuilderFactory.newInstance();

        // Set various configuration options
        m_dbf.setValidating(false);
        m_dbf.setIgnoringComments(true);
        //m_dbf.setIgnoringElementContentWhitespace(true);
        m_dbf.setCoalescing(true);

        // The opposite of creating entity ref nodes is expanding them inline
        m_dbf.setExpandEntityReferences(true);

        // validating part , added by sudhir
        // Create a DocumentBuilderFactory and configure it
        m_vdbf = DocumentBuilderFactory.newInstance();

        // Set various configuration options
        m_vdbf.setValidating(true);
        m_vdbf.setIgnoringComments(true);

        // Bug# 9689
        //
        m_dbf.setIgnoringElementContentWhitespace(false);

        m_vdbf.setCoalescing(true);

        // The opposite of creating entity ref nodes is expanding them inline
        m_vdbf.setExpandEntityReferences(true);

        String lineSeparator = System.getProperty("line.separator");
        if (lineSeparator != null)
            m_lineSeparator = lineSeparator;
    }

    public static String serializeDocument(Node node, Properties options) throws FioranoException{
        try{
          StringWriter writer = new StringWriter();
          OutputFormat format = new OutputFormat();
          format.setIndent(options.getProperty(SerializerOptions.INDENT_STRING, "  ").length());
          format.setIndenting(true);
          format.setLineSeparator(options.getProperty(SerializerOptions.LINE_SEPARATOR, System.getProperty("line.separator")));
          format.setOmitXMLDeclaration(options.put(SerializerOptions.OMIT_XML_DECLERATION, "yes").equals("yes"));
          if(node instanceof Document)
            new XMLSerializer(writer, format).serialize((Document)node);
          else
            new XMLSerializer(writer, format).serialize((Element)node);
          writer.close();
          return writer.toString();
        }catch (Throwable thr) {
            throw new FioranoException ( thr);
        }

//        try {
//            return new DOMSerializer(node, options).toString();
//        }catch (Throwable thr) {
//            throw new FioranoException ("ERROR_CONVERTING_DOCUMENT_2_XML", thr);
//        }
    }

    public static String serializeDocument(Node node)
           throws FioranoException
    {
        Properties options = new Properties();
        options.put(SerializerOptions.INDENT_STRING, "  ");
        options.put(SerializerOptions.OMIT_XML_DECLERATION, "yes");
        return serializeDocument(node, options);
    }

    public static String beautifyXML(String xml) throws Exception{
        return beautifyXML(xml, null);
    }

    public static String beautifyXML(String xml, Properties options) throws Exception{
        return new SAXSerializer(xml, options).toString();
    }


    //added for Stax Parsing.  -Sandeep M  9th Nov 2006

        /**
     *  Gets the String value as boolean.
     */
    public final static boolean getStringAsBoolean(String value)
    {
        return value == null ? false : ((value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("TRUE"))) ;
    }


    /**
     *  Gets the node value as int.
     */
    public final static int getStringAsInt(String value)
    {
         return value == null ? -1 : Integer.parseInt(value);
    }

    /**
     *  Gets the String value as long.
     */
    public final static long getStringAsLong(String value)
    {
        return value == null ? -1 : Long.parseLong(value);
    }


    /**
     *  Gets the String value as float.
     */
    public final static float getStringAsFloat(String value)
    {
        return value == null ? -1 : Float.parseFloat(value);
    }


    /**
     *  Gets the String value as double.
     */
    public final static double getStringAsDouble(String value)
    {
        return value == null ? -1 : Double.parseDouble(value);
    }

    /**
     * This will returns the instance of the STAX XMLInputFactory and will initialze the properties to the factory.
     *
     * @return This will return the instance of STAX XmlInputFactory.
     */
    public static XMLInputFactory getStaxInputFactory(){

        if(staxInputFactory == null){
            try{
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
                ClassLoader currentThreadContextCL = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());
                staxInputFactory = XMLInputFactory.newInstance();
                Thread.currentThread().setContextClassLoader(currentThreadContextCL);
                staxInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
                staxInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
                staxInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            }
            catch(Exception ex){
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
    public static XMLOutputFactory getStaxOutputFactory(){

        if(staxOutputFactory == null){
            try{
                // These should be taken from either properties file or from command line arguments.
                System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
                staxOutputFactory = XMLOutputFactory.newInstance();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return staxOutputFactory;
    }


    private static DocumentBuilderFactory m_documentBuilderFactory =
            getDOMFactory();

    /**
     * This function creates a document from a xml string.
     *
     * @param xmlString
     * @return
     */
    public static Document createDocument(String xmlString) throws FioranoException {
        StringReader reader = new StringReader(xmlString);

        return createDocument(reader);
    }

    /**
     * This function creates a document from the parameter string reader object.
     *
     * @param reader
     * @return
     */
    public static Document createDocument(StringReader reader) throws FioranoException {
        InputSource source = new InputSource(reader);

        return createDocument(source);
    }

    /**
     * This function creates a document from the parameter input source object.
     *
     * @param source
     * @return
     */
    public static Document createDocument(InputSource source)
            throws FioranoException
    {
        // create a document builder
        DocumentBuilder db = null;

        if (m_documentBuilderFactory == null)
        {
            /**
             *throw new FioranoException(REZ,UtilErrorCodes.ERR_INVALID_ARGUMENTS_ERROR, "error.xmlutil.createDocument.documentFactoryIsNull");
             */
            throw new FioranoException(UtilErrorCodes.ERR_INVALID_ARGUMENTS_ERROR);
        }

        try
        {
            synchronized (m_documentBuilderFactory)
            {
                db = m_documentBuilderFactory.newDocumentBuilder();
            }
        }
        catch (ParserConfigurationException ex1)
        {
            /**
             * throw new FioranoException(REZ, UtilErrorCodes.ERR_EXCEPTION_OCCURRED_ERROR, "error.xmlutil.createDocument.parseException",ex1);
             */
            throw new FioranoException(ex1);
        }

        try
        {
            // parse the document
            return db.parse(source);
        }
        catch (Exception ex2)
        {
            /**
             *throw new FioranoException(REZ, UtilErrorCodes.ERR_EXCEPTION_OCCURRED_ERROR, "error.xmlutil.createDocument.parseException",ex2);
             */
            throw new FioranoException(ex2);
        }
    }

    /**
     * Create a document builder factory.
     *
     * @return
     */
    private static DocumentBuilderFactory getDOMFactory()
    {
        DocumentBuilderFactory dbf;

        try
        {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
        }
        catch (Exception e)
        {
            dbf = null;
        }

        return dbf;
    }
}
