/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.sax.FioranoCatalogResolver;
import com.fiorano.openesb.utils.sax.SAXFeatures;
import com.fiorano.openesb.utils.sax.XMLRootElementFinder;
import org.apache.xerces.util.XMLCatalogResolver;
import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xml.resolver.CatalogManager;
import org.xml.sax.*;
import org.w3c.dom.ls.LSInput;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.*;
import java.util.*;

public final class SAXUtil {
    private static final String USER_CATALOG = "user/xml-catalog.xml";

    public static final EntityResolver NO_DTD_RESOLVER = new EntityResolver() {
        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new ByteArrayInputStream(new byte[0]));
        }
    };

    public static SAXParserFactory createSAXParserFactory(boolean nsAware, boolean nsPrefixes, boolean validating) throws SAXNotSupportedException, ParserConfigurationException, SAXNotRecognizedException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setFeature(SAXFeatures.NAMESPACES, nsAware);
        factory.setNamespaceAware(nsAware);
        factory.setFeature(SAXFeatures.NAMESPACE_PREFIXES, nsPrefixes);
        factory.setValidating(validating);
        return factory;
    }

    public static SAXParser createSAXParser(boolean nsAware, boolean nsPrefixes, boolean validating) throws SAXException, ParserConfigurationException {
        SAXParser parser = createSAXParserFactory(nsAware, nsPrefixes, validating).newSAXParser();
        parser.setProperty("http://apache.org/xml/properties/internal/entity-resolver", getCatalogResolver()); //NOI18N
        return parser;
    }

    /*-------------------------------------------------[ XML-Catalog ]---------------------------------------------------*/

    private static XMLCatalogResolver catalogResolver;
    private static XMLCatalogResolver oldCatalogResolver;
    private static CatalogManager catalogManager;
    private static Map catalogTimeStamps = new HashMap();

    private static void createCatalogs() {
        catalogManager = new CatalogManager();
        String catalogFiles[] = (String[]) catalogManager.getCatalogFiles().toArray(new String[0]);
        catalogResolver = new CustomXMLCatalogResolver(catalogFiles, catalogManager.getPreferPublic());
        catalogTimeStamps.clear();
        for (String catalogFile : catalogFiles) {
            try {
                File file = new File(new URL(catalogFile).getPath());
                catalogTimeStamps.put(file, file.lastModified());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createOldResolver() {
        catalogManager = new CatalogManager();
        String catalogFiles[] = (String[]) catalogManager.getCatalogFiles().toArray(new String[0]);
        oldCatalogResolver = new XMLCatalogResolver(catalogFiles, catalogManager.getPreferPublic());
    }

    private static boolean isCatalogModified() {
        if (catalogManager == null || catalogResolver == null)
            return true;
        Iterator iter = catalogTimeStamps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            File file = (File) entry.getKey();
            long lastModified = ((Long) entry.getValue()).longValue();
            if (file.lastModified() > lastModified)
                return true;
        }
        return false;
    }

    public static void unintializeCatalogs() {
        catalogManager = null;
        catalogResolver = null;
    }

    public static CatalogManager getCatalogManager() {
        if (isCatalogModified())
            createCatalogs();
        return catalogManager;
    }

    // NOTE: if you are intended to use this as LSResourceResolver, instead of this
    // use XSDResourceResolver.getCatalogResolver()
    public static synchronized XMLCatalogResolver getCatalogResolver() {
        if (isCatalogModified())
            createCatalogs();
        return catalogResolver;
    }

    public static synchronized XMLCatalogResolver getOldCatalogResolver() {
        if (isCatalogModified() || oldCatalogResolver == null)
            createOldResolver();
        return oldCatalogResolver;
    }

    /*-------------------------------------------------[ Schema Validation ]---------------------------------------------------*/

    // use it to validate with schemas with targetnamespace
    public static void enableSchemaValidation(SAXParser parser, Properties/*<namespace, uri>*/ schemas, boolean fullChecking) throws SAXException {
        StringBuffer buf = new StringBuffer();
        Iterator iter = schemas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String namespace = String.valueOf(entry.getKey());
            String uri = String.valueOf(entry.getValue());
            if (uri.indexOf(' ') != -1)
                throw new IllegalArgumentException("uri shouldn't contain space");
            if (buf.length() != 0)
                buf.append(' ');
            buf.append(namespace).append(' ').append(uri);
        }

        parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema", true);  //NOI18N
        if (fullChecking)
            parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", //NOI18N
                buf.toString());
    }

    // use it to validate with schema with no targetnamespace
    public static void enableSchemaValidation(SAXParser parser, String schemaURI, boolean fullChecking) throws SAXException {
        parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema", true);  //NOI18N;
        if (fullChecking)
            parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", //NOI18N
                schemaURI);
    }

    /*-------------------------------------------------[ Generic ]---------------------------------------------------*/

    public static boolean isXML(InputSource source) throws IOException, ParserConfigurationException {
        try {
            XMLRootElementFinder.findRootElement(source);
            return true;
        } catch (SAXException e) {
            return false;
        }
    }

    private static class CustomXMLCatalogResolver extends FioranoCatalogResolver {
        private File userCatalogFolder;
        private String[] catalogFolders;
        // private FioranoCatalog fCatalog = null;


        public CustomXMLCatalogResolver(String[] catalogFiles, boolean preferPublic) {
            super(catalogFiles, preferPublic);

            this.catalogFolders = new String[catalogFiles.length];
            for (int i = 0; i < catalogFiles.length; i++) {
                String catalogFilePath = catalogFiles[i];
//                if (catalogFilePath.endsWith(USER_CATALOG)) {
                try {
//                        userCatalogFolder = new File(new URI(catalogFilePath).getPath()).getParentFile();
                    catalogFolders[i] = new File(new URI(catalogFilePath).getPath()).getParent();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
//                    }
                    break;
                }
            }
        }

        public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

            LSInput lsInput = null;

            if (systemId != null && baseURI != null && !baseURI.toLowerCase().startsWith("file") && !baseURI.toLowerCase().startsWith("file")) {
                lsInput = new DOMInputImpl();
                lsInput.setBaseURI(baseURI);
                lsInput.setSystemId(systemId);
                return lsInput;
            }

            if (systemId != null && (systemId.toLowerCase().startsWith("http")) || (baseURI != null && baseURI.toLowerCase().startsWith("http"))) {
                if (systemId.toLowerCase().startsWith("http")) {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        httpURLConnection = (HttpURLConnection) new URL(systemId).openConnection();
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == 200) {
                            InputStream in = httpURLConnection.getInputStream();
                            if (in != null) {
                                lsInput = new DOMInputImpl();
                                lsInput.setSystemId(systemId);
                                ByteArrayOutputStream content = new ByteArrayOutputStream();
                                byte b[] = new byte[2048];
                                int i = -1;
                                while ((i = in.read(b)) != -1) {
                                    content.write(b, 0, i);
                                }
                                lsInput.setByteStream(new ByteArrayInputStream(content.toByteArray()));
                                return lsInput;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                if (baseURI != null && baseURI.toLowerCase().startsWith("http") && !systemId.toLowerCase().startsWith("http")) {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        StringBuffer full = new StringBuffer();
                        String[] parts = baseURI.split("/");
                        parts[parts.length] = systemId;
                        for (int i = 0; i < parts.length; i++)
                            full.append(parts[i]);
                        httpURLConnection = (HttpURLConnection) new URL(full.toString()).openConnection();
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == 200) {
                            InputStream in = httpURLConnection.getInputStream();
                            if (in != null) {
                                lsInput = new DOMInputImpl();
                                lsInput.setSystemId(full.toString());
                                ByteArrayOutputStream content = new ByteArrayOutputStream();
                                byte b[] = new byte[2048];
                                int i = -1;
                                while ((i = in.read(b)) != -1) {
                                    content.write(b, 0, i);
                                }
                                lsInput.setByteStream(new ByteArrayInputStream(content.toByteArray()));
                                return lsInput;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }

            // Getting a schema with systemId was not handled in Xml-Catalog.xml, so taken care here itself
            if (systemId != null) {


                if (new File(systemId).isAbsolute()) {
                    if (new File(systemId).exists()) {
                        lsInput = new DOMInputImpl();
                        try {
                            lsInput.setSystemId(new File(systemId).toURL().toExternalForm());
                            lsInput.setBaseURI(new File(systemId).getParentFile().toURL().toExternalForm());
                            return lsInput;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        return lsInput;
                    }
                }

                if (baseURI != null) {
                    try {
                        File parent = new File(new URI(baseURI).getPath());
                        if (parent.isFile())
                            parent = parent.getParentFile();
                        File file = new File(parent.getParent(), systemId);
                        if (file.exists()) {
                            lsInput = new DOMInputImpl();

                            lsInput.setSystemId(file.toURL().toExternalForm());
                            lsInput.setBaseURI(file.getParentFile().toURL().toExternalForm());
                            return lsInput;
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }


                //Check in catalog using the system Id
                for (int i = 0; i < catalogFolders.length; i++) {

                    File file = new File(catalogFolders[i], systemId);

                    if (file.exists()) {
                        lsInput = new DOMInputImpl();
                        try {
                            lsInput.setSystemId(file.toURL().toExternalForm());
                            lsInput.setBaseURI(file.getParentFile().toURL().toExternalForm());
                            return lsInput;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            //No system Id and use namespace


            if (baseURI == null && namespaceURI != null) {  // baseuri is null then no other option present send the first entry of namespace
                lsInput = new DOMInputImpl();
                try {
                    String sysId = resolveNamespace(namespaceURI);
                    if (sysId != null) {
                        lsInput.setSystemId(sysId);
                        String base = new File(new URL(sysId).getPath()).getParentFile().toURL().toExternalForm();
                        lsInput.setBaseURI(base);
                        return lsInput;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                try {
                    String sysId = resolveURI(namespaceURI);
                    if (sysId != null) {
                        lsInput.setSystemId(sysId);
                        String base = new File(new URL(sysId).getPath()).getParentFile().toURL().toExternalForm();
                        lsInput.setBaseURI(base);
                        return lsInput;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

            //namespace and base uri both are present

            if (baseURI != null && namespaceURI != null) {
                lsInput = new DOMInputImpl();
                try {
                    List sysIds = resolveURIs(namespaceURI);
                    if (sysIds != null && sysIds.size() != 0) {

                        File baseFile = new File(new URL(baseURI).getPath());
                        String baseFolderName = baseFile.isFile() ? baseFile.getParentFile().getName() : baseFile.getName();

                        for (Iterator iterator = sysIds.iterator(); iterator.hasNext(); ) {
                            Object entry = iterator.next();
                            String fName = new File(new URL((String) entry).getPath()).getParentFile().getName();
                            if (baseFolderName.equals(fName)) {
                                lsInput.setSystemId((String) entry);
                                String base = new File(new URL((String) entry).getPath()).getParentFile().toURL().toExternalForm();
                                lsInput.setBaseURI(base);
                                return lsInput;
                            }
                        }

                        lsInput.setSystemId((String) sysIds.get(0));
                        String base = new File(new URL((String) sysIds.get(0)).getPath()).getParentFile().toURL().toExternalForm();
                        lsInput.setBaseURI(base);
                        return lsInput;

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


            lsInput = super.resolveResource(type, namespaceURI, publicId, systemId, baseURI);
            return lsInput;
        }


        public LSInput resolveResource(String systemId, String baseURI) {

            LSInput lsInput = null;

            if (systemId != null && baseURI != null && !baseURI.toLowerCase().startsWith("file") && !baseURI.toLowerCase().startsWith("http")) {
                lsInput = new DOMInputImpl();
                lsInput.setBaseURI(baseURI);
                lsInput.setSystemId(systemId);
                return lsInput;
            }

            if (systemId != null && (systemId.toLowerCase().startsWith("http")) || (baseURI != null && baseURI.toLowerCase().startsWith("http"))) {
                if (systemId.toLowerCase().startsWith("http")) {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        httpURLConnection = (HttpURLConnection) new URL(systemId).openConnection();
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == 200) {
                            InputStream in = httpURLConnection.getInputStream();
                            if (in != null) {
                                lsInput = new DOMInputImpl();
                                lsInput.setSystemId(systemId);
                                ByteArrayOutputStream content = new ByteArrayOutputStream();
                                byte b[] = new byte[2048];
                                int i = -1;
                                while ((i = in.read(b)) != -1) {
                                    content.write(b, 0, i);
                                }
                                lsInput.setByteStream(new ByteArrayInputStream(content.toByteArray()));
                                return lsInput;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                if (baseURI != null && baseURI.toLowerCase().startsWith("http") && !systemId.toLowerCase().startsWith("http")) {
                    HttpURLConnection httpURLConnection = null;
                    try {
                        StringBuffer full = new StringBuffer();
                        String[] parts = baseURI.split("/");
                        parts[parts.length] = systemId;
                        for (int i = 0; i < parts.length; i++)
                            full.append(parts[i]);
                        httpURLConnection = (HttpURLConnection) new URL(full.toString()).openConnection();
                        httpURLConnection.connect();
                        if (httpURLConnection.getResponseCode() == 200) {
                            InputStream in = httpURLConnection.getInputStream();
                            if (in != null) {
                                lsInput = new DOMInputImpl();
                                lsInput.setSystemId(full.toString());
                                ByteArrayOutputStream content = new ByteArrayOutputStream();
                                byte b[] = new byte[2048];
                                int i = -1;
                                while ((i = in.read(b)) != -1) {
                                    content.write(b, 0, i);
                                }
                                lsInput.setByteStream(new ByteArrayInputStream(content.toByteArray()));
                                return lsInput;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (httpURLConnection != null) {
                            try {
                                httpURLConnection.disconnect();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }


            // Getting a schema with systemId was not handled in Xml-Catalog.xml, so taken care here itself
            if (systemId != null) {


//                if (new File(systemId).isAbsolute()) {  //absolute
                if (new File(systemId).exists()) {
                    lsInput = new DOMInputImpl();
                    try {
                        lsInput.setSystemId(new File(systemId).toURL().toExternalForm());
                        lsInput.setBaseURI(new File(systemId).getParentFile().toURL().toExternalForm());
                        return lsInput;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return lsInput;
                }

                if (baseURI != null) {  // use baseURI and systemID
                    try {
                        File parent = new File(new URI(baseURI).getPath());
                        if (parent.isFile())
                            parent = parent.getParentFile();
                        File file = new File(parent.getParent(), systemId);
                        if (file.exists()) {
                            lsInput = new DOMInputImpl();

                            lsInput.setSystemId(file.toURL().toExternalForm());
                            lsInput.setBaseURI(file.getParentFile().toURL().toExternalForm());
                            return lsInput;
                        }

                        return lsInput;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }


                //Check in catalog using the system Id
                for (int i = 0; i < catalogFolders.length; i++) {

                    File file = new File(catalogFolders[i], systemId);

                    if (file.exists()) {
                        lsInput = new DOMInputImpl();
                        try {
                            lsInput.setSystemId(file.toURL().toExternalForm());
                            lsInput.setBaseURI(file.getParentFile().toURL().toExternalForm());
                            return lsInput;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            return lsInput;
        }
    }
}
