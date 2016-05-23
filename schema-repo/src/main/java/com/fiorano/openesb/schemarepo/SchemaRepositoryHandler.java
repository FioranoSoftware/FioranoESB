/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.schemarepo;

import com.fiorano.openesb.application.application.SchemaReference;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.sax.XMLCreator;
import com.fiorano.openesb.utils.xml.sax.XSDTargetNameSpaceFinder;
import com.fiorano.openesb.utils.xml.stax.FioranoStaxParser;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.sax.NoMoreSAXParsingException;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SchemaRepositoryHandler {
    private static final String USER_CATALOG = "user/xml-catalog.xml";
    public static File userCatalogFolder;
    private static SchemaRepositoryHandler INSTANCE;
    private static Long lastParsedTime = -1l;
    private static File catalogFile;
    private static Map<String, Set<String>> uriMap = new HashMap<String, Set<String>>();
    private static String userCatalogPath = null;

    private SchemaRepositoryHandler() throws FioranoException {
        try {
            Vector catalogFiles = SAXUtil.getCatalogManager().getCatalogFiles();
            for (Object catalogFileObj : catalogFiles) {
                String catalogFilePath = (String) catalogFileObj;
                if (catalogFilePath != null && catalogFilePath.endsWith(USER_CATALOG)) {
                    userCatalogPath = catalogFilePath;
                    catalogFile = new File(new URI(userCatalogPath).getPath());
                    userCatalogFolder = catalogFile.getParentFile();
                    break;
                }
            }
            if(userCatalogPath == null)
                throw new Exception("The Catalog file in user folder does not exist or might be corrupted");
            reParseCatalog();
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    public static synchronized SchemaRepositoryHandler getInstance() throws FioranoException {
        if (INSTANCE == null) {
            INSTANCE = new SchemaRepositoryHandler();
        }
        return INSTANCE;
    }

    public static synchronized SchemaRepositoryHandler getInstance(String catalogPath, String fioranoHome) throws
            FioranoException {
        try {
            File xmlCatalogPath = new File(catalogPath);
            File targetLocation = new File(new File(xmlCatalogPath.getParent()).getParent());
            if (!xmlCatalogPath.exists()) {
                //copy xml-catalog folders from installer to runtimedata repository
                File xmlCatalog = new File(fioranoHome + File.separator + "xml-catalog");
                if (xmlCatalog.exists()) {
                    FileUtil.copyDirectory(xmlCatalog, targetLocation);
                }
            }
            //set xml-catalog properties
            setCatalogProperties(targetLocation.getAbsolutePath());
            if (INSTANCE == null && userCatalogPath == null) {
                INSTANCE = new SchemaRepositoryHandler();
            } else{
                return INSTANCE;
            }
        } catch (FioranoException e) {
            throw new FioranoException(e);
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        return INSTANCE;
    }


    @SuppressWarnings({"UnusedDeclaration"})
    public InputStream getSchema(String namespace, String location) throws FileNotFoundException {

        File file = FileUtil4.url2File(location2url(location));
        try {
            if (file != null) {
                file = file.getCanonicalFile();
            }
        } catch (IOException ignore) {
            // ignore
        }

        return new FileInputStream(file);
    }

    public String getSchemaAsString(String namespace, String location) throws FileNotFoundException {
        File file = FileUtil4.url2File(location2url(location));
        try {
            if (file != null) {
                file = file.getCanonicalFile();
            }
        } catch (IOException ignore) {
            // ignore
        }

        return getContentAsString(new FileInputStream(file));
    }

    /**
     * @param zipFile byte[] of a zip of schemas
     * @throws FioranoException FioranoException
     */
    public synchronized void addSchema(byte[] zipFile) throws FioranoException {

        ZipInputStream zipInputStream = null;
        try {
            zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipFile));

            ByteArrayOutputStream byteArrayOutputStream = null;
            ZipEntry zipEntry;
            while (true) {
                try {
                    zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) {
                        break;
                    }

                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = zipInputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }

                    addSchema(byteArrayOutputStream.toString(), zipEntry.getName());

                } catch (IOException e) {
                    throw new FioranoException(e);
                } finally {
                    try {
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        if (zipInputStream != null) {
                            zipInputStream.closeEntry();
                        }
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        } finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    //
                }
            }

        }
    }

    private boolean compareSchemaXMLsStax(String xsd, String xsd2) throws XMLStreamException, FileNotFoundException {
        if (xsd.startsWith("<?xml")) {
            int i = xsd.indexOf("?>\n");
            if (i != -1)
                xsd = xsd.substring(i + 3);
            else {
                i = xsd.indexOf("?>");
                xsd = xsd.substring(i + 2);
            }
        }
        if (xsd2.startsWith("<?xml")) {
            int i = xsd2.indexOf("?>\n");
            if (i != -1)
                xsd2 = xsd2.substring(i + 3);
            else {
                i = xsd2.indexOf("?>");
                xsd2 = xsd2.substring(i + 2);
            }
        }

        int startelementIndex = xsd.indexOf("<");
        if(startelementIndex != 0){
            xsd = xsd.substring(startelementIndex);
        }

        int startelementIndex2 = xsd2.indexOf("<");
        if(startelementIndex2 != 0){
            xsd2 = xsd2.substring(startelementIndex2);
        }

        XMLInputFactory factory = FioranoStaxParser.getStaxInputFactory();
        XMLEventReader eventReader = factory.createXMLEventReader(new StringReader(xsd));
        XMLEventReader eventReader2 = factory.createXMLEventReader(new StringReader(xsd2));
        while (eventReader.hasNext() && eventReader2.hasNext()) {
            XMLEvent e = (XMLEvent) eventReader.next();
            XMLEvent e2 = (XMLEvent) eventReader2.next();

            QName importQName = new QName("http://www.w3.org/2001/XMLSchema", "import");
            QName schemaQName = new QName("http://www.w3.org/2001/XMLSchema", "schema");
            if (e.isStartElement() && e2.isStartElement()) {

                StartElement startElement = e.asStartElement();
                QName startName = startElement.getName();
                boolean break1 = false;
                while(importQName.equals(startName) || schemaQName.equals(startName)){
                    if(eventReader.hasNext()){
                        e = (XMLEvent) eventReader.next();
                        if(!e.isStartElement())
                            continue;
                    }else{
                        break1 = true;
                    }
                    startElement = e.asStartElement();
                    startName = startElement.getName();
                }

                StartElement startElement2 = e2.asStartElement();
                QName startName2 = startElement2.getName();

                while(importQName.equals(startName2) || schemaQName.equals(startName2)){
                    if(eventReader2.hasNext()){
                        e2 = (XMLEvent) eventReader2.next();
                        if(!e2.isStartElement())
                            continue;
                    }else{
                        break1 = true;
                    }
                    startElement2 = e2.asStartElement();
                    startName2 = startElement2.getName();
                }

                if(break1)
                    break;

                if (!startName.equals(importQName) && !startName.equals(schemaQName)) {
                    if (startElement.getName().equals(startElement2.getName())) {
                        Iterator attributes1 = startElement.getAttributes();
                        Iterator attributes2 = startElement2.getAttributes();
                        while (attributes1.hasNext() && attributes2.hasNext()) {
                            Attribute attribute1 = (Attribute) attributes1.next();
                            Attribute attribute2 = (Attribute) attributes2.next();
                            String value1 = getWOPrefix(attribute1.getValue());
                            String value2 = getWOPrefix(attribute2.getValue());

                            if (!(attribute1.getName().equals(attribute2.getName()) && value1.equals(value2))) {
                                return false;
                            }
                        }
                        if (attributes1.hasNext() || attributes2.hasNext()) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return !(eventReader.hasNext() || eventReader2.hasNext());
    }

    private static String getWOPrefix(String value) {
        return value.contains(":") ? value.substring(value.indexOf(":")) : value;
    }

    public synchronized Map<String, String> addServiceSchemas(ESBRecordDefinition esbRecordDefinition, String serviceName) throws FioranoException {
        Map<String, String> schemaLocations = new HashMap<String, String>();
        try {
            if (esbRecordDefinition.hasImportedStructures()) {
                Map importedStructures = esbRecordDefinition.getImportedStructures();
                for (Object mapEntry : importedStructures.entrySet()) {
                    Map.Entry<String, Object> importedSchemaEntry = (Map.Entry<String, Object>) mapEntry;
                    String importedSchema = getContent(importedSchemaEntry.getValue());
                    if (importedSchema != null) {
                        schemaLocations.put(importedSchemaEntry.getKey(), getSchemaFileName(importedSchema, true, importedStructures));
                    }
                }
                for (Map.Entry<String, String> locationEntry : schemaLocations.entrySet()) {
                    if (locationEntry.getValue() == null) {
                        String[] importsForNS = esbRecordDefinition.getImportedStructures(locationEntry.getKey());
                        if (importsForNS.length > 0) {
                            String content = importsForNS[0];
                            String newFileName = createFile(content, serviceName + ".xsd", true);
                            locationEntry.setValue(newFileName);
                        }
                    }
                }
                for (Map.Entry<String, String> locationEntry : schemaLocations.entrySet()) {
                    if (locationEntry.getValue() != null) {
                        String modified = SchemaUtil.updateImports(esbRecordDefinition.getImportedStructures(locationEntry.getKey())[0], schemaLocations);
                        createFile(modified, locationEntry.getValue(), false);
                    }
                }

            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        return schemaLocations;
    }

    private String getContent(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof String[]) {
            String[] array = (String[]) value;
            if (array.length > 0) {
                return array[0];
            }
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            if (list.size() > 0) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * @param content  byte[] of schema
     * @param fileName File name with which schema is saved in the catalog
     * @return File name with which schema is saved in the catalog
     * @throws FioranoException FioranoException
     */
    public synchronized String addSchema(byte[] content, String fileName) throws FioranoException {

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(content);
            return addSchema(byteArrayInputStream, fileName);
        } finally {
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    public synchronized String addSchema(InputStream content, String fileName) throws FioranoException {
        try {

            String contentAsString = getContentAsString(content);
            return addSchema(contentAsString, fileName);
        } catch (Exception e) {
            throw new FioranoException(e);
        }

    }

    public synchronized String addSchema(String content, String fileName, boolean overwrite) throws FioranoException {
        try {
            if (!overwrite) {
                String existingFile = getSchemaFileName(content, false, new HashMap());
                if (existingFile != null) {
                    return existingFile;
                } else {
                    return createFile(content, fileName, true);
                }
            } else {
                return createFile(content, fileName, !overwrite);
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    public synchronized String addSchema(String content, String fileName, boolean overwrite, Map map) throws FioranoException {
        try {
            map = map == null ? new HashMap() : map;
            if (!overwrite) {
                String existingFile = getSchemaFileName(content, false, map);
                if (existingFile != null) {
                    return existingFile;
                } else {
                    return createFile(content, fileName, true);
                }
            } else {
                return createFile(content, fileName, !overwrite);
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    public synchronized String addSchema(String content, String fileName, Map map) throws FioranoException {
        return addSchema(content, fileName, false, map);
    }

    public synchronized String addSchema(String content, String fileName) throws FioranoException {
        return addSchema(content, fileName, false);
    }

    public synchronized String getSchemaFileName(String content, boolean serviceAdd, Map map) throws FioranoException {
        try {
            reParseCatalog();
            String targetNS = XSDTargetNameSpaceFinder.findTargetNamespace(new StringReader(content));
            //todo handle no namespace
            if (targetNS == null) {
                throw new Exception("No target namespace");
            }
            Set<String> existingFiles = uriMap.get(targetNS);
            if (existingFiles != null && existingFiles.size() != 0) {
                for (String existingFile : existingFiles) {
                    String existingSchema = getContentAsString(getSchema(targetNS, existingFile));
                    Map<String, List<String>> importsMap = SchemaUtil.extractImports(existingSchema);
                    boolean areSame = isSameSchema(content, serviceAdd, map, existingSchema);
                    if (areSame) {
                        if (map != null && map.size() > 0) {
                            boolean isImportedSchemaSame = true;
                            for (Map.Entry<String, List<String>> entry : importsMap.entrySet()) {
                                String nameSpace = entry.getKey();
                                if (entry.getValue().get(0) == null) {
                                    throw new FioranoException("Can not find schema location for imported namespace -" + nameSpace);
                                }
                                String existingFileContent = getContentAsString(getSchema(nameSpace, entry.getValue().get(0)));
                                String newContent = getContent(map.get(nameSpace));
                                if(newContent == null){
                                    isImportedSchemaSame = isImportedSchemaSame && false;
                                    break;
                                }
                                isImportedSchemaSame = isImportedSchemaSame && isSameSchema(newContent, serviceAdd, map, existingFileContent);
                                if (!isImportedSchemaSame)
                                    break;
                            }
                            if (isImportedSchemaSame)
                                return existingFile;
                        } else {
                            return existingFile;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        return null;
    }

    private boolean isSameSchema(String content, boolean serviceAdd, Map map, String existingSchema) throws Exception {
        boolean areSame;
        if ("true".equals(System.getProperty("STAX_XML_COMPARISION"))) {
            areSame = compareSchemaXMLsStax(existingSchema, content);
        } else {
            if(map != null && map.size() > 0){
                areSame=true;
                //todo:
                // areSame = (compareSchemaXMLsStax(existingSchema, content) && XSDComparator.compareSchema(existingSchema, null, content, map));
            }
            else{
                areSame = (("true".equals(System.getProperty("TRANSFER_XML_COMPARISION_STAX"))) ? compareSchemaXMLsStax(existingSchema, content)
                        : compareSchemaXMLsDom(content, existingSchema));
            }

//            areSame = serviceAdd ? (compareSchemaXMLsStax(existingSchema, content) && XSDComparator.compareSchema(existingSchema, null, content, map)) :
//                    (("true".equals(System.getProperty("TRANSFER_XML_COMPARISION_STAX"))) ? compareSchemaXMLsStax(existingSchema, content)
//                            : compareSchemaXMLsDom(content, existingSchema));
        }
        return areSame;
    }

    private boolean compareSchemaXMLsDom(String content, String existingSchema) throws Exception {
        org.custommonkey.xmlunit.XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setXSLTVersion("2.0");
        Diff diff = XMLUnit.compareXML(content, existingSchema);
        return diff.similar();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String createFile(String content, String fileName, boolean computeNewName) throws Exception {
        int i = fileName.lastIndexOf('/');
        if (i == -1) {
            i = fileName.lastIndexOf('\\');
        }
        String parentName = fileName.substring(0, i + 1);   //for relative path schemas are added

        File file = new File(catalogFile.getParentFile(), fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (computeNewName) {
            file = computeFileName(file);
        }
        file.createNewFile();
        String targetNS = XSDTargetNameSpaceFinder.findTargetNamespace(new StringReader(content));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        new StreamPumper(new ByteArrayInputStream(content.getBytes()), fileOutputStream, true).run();

        if (!uriMap.containsKey(targetNS)) {
            uriMap.put(targetNS, new LinkedHashSet<String>());
        }
        uriMap.get(targetNS).add(parentName + file.getName());
        regenerateCatalog();
        if (!StringUtil.isEmpty(parentName))
            return parentName + file.getName();
        else
            return file.getName();
    }

    private File computeFileName(File file) {
        while (file.exists()) {
            String presentName = file.getName();
            int dotIndex = presentName.lastIndexOf(".");
            String nameWOExt = dotIndex != -1 ? presentName.substring(0, dotIndex) :
                    presentName;
            String extension = dotIndex != -1 ? presentName.substring(dotIndex) : ".xsd";
            int scoreIndex = nameWOExt.lastIndexOf("_");
            String namePrefix = scoreIndex != -1 ? nameWOExt.substring(0, scoreIndex)
                    : nameWOExt;
            int counter = 1;
            try {
                counter = scoreIndex != -1 ? Integer.parseInt(nameWOExt.substring(scoreIndex + 1)) + 1
                        : 1;
            } catch (NumberFormatException nfe) {
                //ignore
            }
            file = new File(file.getParentFile(), namePrefix + "_" + counter + extension);
        }
        return file;
    }

    public synchronized void removeSchema(String namespace, String locationHint) throws FioranoException {
        removeSchema(namespace, locationHint, true);
    }

    public synchronized void removeSchema(String namespace, String locationHint, boolean deleteFile) throws FioranoException {
        try {
            reParseCatalog();
            if (uriMap.containsKey(namespace)) {
                if (!StringUtil.isEmpty(locationHint)) {
                    Set<String> locationSet = uriMap.get(namespace);
                    if (locationSet.contains(locationHint)) {
                        if (locationSet.size() > 1) {
                            uriMap.get(namespace).remove(locationHint);
                        } else {
                            uriMap.remove(namespace);
                        }
                        if (deleteFile) {
                            File file = FileUtil4.url2File(location2url(locationHint));
                            try {
                                if (file != null) {
                                    file = file.getCanonicalFile();
                                }

                                if (file.getAbsolutePath().contains(catalogFile.getParent()))
                                    file.delete();
                                else if (new File(catalogFile.getParent() + File.separator + locationHint).exists()) {
                                    new File(catalogFile.getParent() + File.separator + locationHint).delete();
                                }

                            } catch (IOException ignore) {
                                // ignore
                            }
                        }
                        regenerateCatalog();
                    }
                }
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    private URL location2url(String location) {
        try {
            return new URL(location);
        } catch (Exception e) {
            return FileUtil4.file2URL(FileUtil.resolve(catalogFile.getParentFile(), location));
        }
    }

    public void resetLastParsedTimeforReparsing(){
        lastParsedTime = -1L;
        uriMap.clear();
    }

    public boolean hasSchema(String namespace, String location) throws FioranoException {

        try {
            if (StringUtil.isEmpty(location)) {
                return false;
            }
            reParseCatalog();
            if (uriMap.containsKey(namespace)) {
                if (uriMap.get(namespace).contains(location)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        return false;
    }

    public String getLocation(String serviceInstanceName) {

        for (int i = 0; ; i++) {
            String fileName = serviceInstanceName + "." + i + ".xsd";
            if (!new File(userCatalogFolder, fileName).exists()) {
                return fileName;
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void modifySchemaLocations(Reader reader) throws FioranoException {
        getImportLocationMap(reader);
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, String> getImportLocationMap(Reader reader) throws FioranoException {
        try {
            SAXParser parser = SAXUtil.createSAXParserFactory(true, true, false).newSAXParser();
            parser.parse(new InputSource(reader), new ImportLocationsParser());
            return new HashMap<String, String>();
        } catch (NoMoreSAXParsingException e) {
            return (Map<String, String>) e.getData();
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    public List<SchemaReference> listOfAllSchemas() throws FioranoException {
        try {
            reParseCatalog();
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        List<SchemaReference> schemaReferenceList = new ArrayList<SchemaReference>();

        for (Map.Entry<String, Set<String>> stringSetEntry : uriMap.entrySet()) {
            for (String location : stringSetEntry.getValue()) {
                SchemaReference sr = new SchemaReference();
                sr.setNamespace(stringSetEntry.getKey());
                sr.setLocation(location);
                sr.setRequiredAtRuntime(false);
                schemaReferenceList.add(sr);
            }
        }
        return schemaReferenceList;
    }

    private static void reParseCatalog() throws SAXException, IOException, ParserConfigurationException {
        if (((Long) catalogFile.lastModified()).compareTo(lastParsedTime) > 0) {
            SAXUtil.createSAXParser(true, false, false).parse(userCatalogPath, new CatalogHandler(uriMap));
            lastParsedTime = System.currentTimeMillis();
        }
    }

    private static void regenerateCatalog() throws Exception {
        StreamResult result = null;
        try{
            result = new StreamResult(userCatalogPath);
            XMLCreator xml = new XMLCreator(result, true, false);
            xml.setOuputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//OASIS//DTD XML Catalogs V1.0//EN"); //NOI18N
            xml.setOuputProperty(OutputKeys.VERSION, "1.1");
            xml.setOuputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd"); //NOI18N
            xml.startDocument();
            xml.addAttribute("prefer", "public"); //NOI18N
            xml.startElement("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog"); //NOI18N
            for (Map.Entry<String, Set<String>> stringListEntry : uriMap.entrySet()) {
                for (String location : stringListEntry.getValue()) {
                    xml.addAttribute("name", stringListEntry.getKey()); //NOI18N
                    xml.addAttribute("uri", location); //NOI18N
                    xml.addElement("urn:oasis:names:tc:entity:xmlns:xml:catalog", "uri", null); //NOI18N
                }
            }
            xml.endElement();
            xml.endDocument(); }
        finally {
            if (result != null && result.getOutputStream() != null) {
                result.getOutputStream().flush();
                result.getOutputStream().close();
            }
        }
    }

    private static String getContentAsString(InputStream schema) throws FileNotFoundException {
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        new StreamPumper(schema, bAOS, true).run();
        return bAOS.toString().trim();
    }

    public static class CatalogHandler extends DefaultHandler {
        Map<String, Set<String>> uriMap;

        public CatalogHandler(Map<String, Set<String>> uriMap) {
            this.uriMap = uriMap;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("uri")) {//NOI18N
                String name = attributes.getValue("name");    //NOI18N
                String location = attributes.getValue("uri"); //NOI18N
                if (!uriMap.containsKey(name)) {
                    uriMap.put(name, new LinkedHashSet<String>());
                }
                uriMap.get(name).add(location);
            }
        }

        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new ByteArrayInputStream(new byte[0]));
        }
    }

    private class ImportLocationsParser extends DefaultHandler {
        private Map<String, String> importLocationsMap = new HashMap<String, String>();

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("import".equalsIgnoreCase(localName) && "http://www.w3.org/2001/XMLSchema".equalsIgnoreCase(uri)) {
                String namespace = attributes.getValue("namespace");
                String schemaLocation = attributes.getValue("schemaLocation");
                if (schemaLocation != null) {
                    importLocationsMap.put(namespace, schemaLocation);
                }
            }
        }

        public void endDocument() throws SAXException {
            throw new NoMoreSAXParsingException(importLocationsMap);
        }
    }

    private static void setCatalogProperties(String xmlCatalogPath) throws FioranoException{
        Properties props = new Properties();
        InputStream openStream = null;
        try {
            openStream = new File(xmlCatalogPath//$NON-NLS-1$
                    + File.separator + "CatalogManager.properties").toURL() //$NON-NLS-1$
                    .openStream();
            props.load(openStream);

            String initialCatalogString = props.getProperty("catalogs"); //$NON-NLS-1$

            if (initialCatalogString != null) {
                String[] catalogs = initialCatalogString.split(";"); //$NON-NLS-1$
                String finalCatalogString = ""; //$NON-NLS-1$
                for (String catalog : catalogs) {
                    String string = xmlCatalogPath + File.separator + catalog; //$NON-NLS-1$
                    URI uri = new File(string).toURI();
                    finalCatalogString = finalCatalogString + ";" //$NON-NLS-1$
                            + uri.toString();
                }
                finalCatalogString = finalCatalogString.substring(1);
                System.setProperty("xml.catalog.files", finalCatalogString); //$NON-NLS-1$
            }
        } catch (MalformedURLException e) {
            throw new FioranoException(e);
        } catch (IOException e) {
            throw new FioranoException(e);
        } finally {
            try {
                if (openStream != null)
                    openStream.close();
            } catch (IOException e) {
                //Ignore
            }
            String preferedIdentifier = props.getProperty("prefer"); //$NON-NLS-1$
            if(preferedIdentifier == null)
                preferedIdentifier = "system"; //$NON-NLS-1$
            System.setProperty("xml.catalog.prefer", preferedIdentifier); //$NON-NLS-1$
        }
    }

}

