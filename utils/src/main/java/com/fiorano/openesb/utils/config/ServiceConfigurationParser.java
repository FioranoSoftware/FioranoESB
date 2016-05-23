/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.config;

import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.ibm.wsdl.extensions.PopulatedExtensionRegistry;
import org.xml.sax.InputSource;

import javax.wsdl.Definition;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fiorano.openesb.utils.config.ServiceConfigurationParser.ConfigurationMarkups.*;

//todo replace this with loading the configuration with custom classloader with required dependencies?
public class ServiceConfigurationParser {

    private ServiceConfigurationParser() {
    }

    public Hashtable<String, Object> parseRESTStubConfiguration(String configuration) throws FioranoException {

        if (configuration == null)
            return null;
        Hashtable<String, Object> webInstanceData = new Hashtable<>();

        FioranoStaxParser parser = null;
        try {
            parser = new FioranoStaxParser(new StringReader(configuration));
            parser.markCursor("java");
            while (parser.nextElement()) {
                if (parser.getLocalName().equalsIgnoreCase("object")) {
                    String className = parser.getAttributeValue(null, "class");
                    parser.markCursor(parser.getLocalName());
                    if (className.equalsIgnoreCase(RESTSTUB_CONFIG_CLASSNAME)) {
                        while (parser.nextElement()) {
                            if (parser.getLocalName().equalsIgnoreCase("void")) {
                                String propertyName = parser.getAttributeValue(null, "property");
                                if (propertyName.equalsIgnoreCase(RESTFUL_SERVICE_NAME)) {
                                    parser.markCursor(parser.getLocalName());
                                    while (parser.nextElement()) {
                                        String value = parser.getText();
                                        webInstanceData.put(propertyName, value);
                                    }
                                    parser.resetCursor();
                                }
                                if (propertyName.equalsIgnoreCase(WADL_CONFIGURATION)) {
                                    parser.markCursor(parser.getLocalName());

                                    while (parser.nextElement()) {
                                        if (parser.getLocalName().equalsIgnoreCase("void")) {
                                            propertyName = parser.getAttributeValue(null, "property");
                                            if (propertyName.equalsIgnoreCase(WADL)) {
                                                parser.markCursor(parser.getLocalName());
                                                while (parser.nextElement()) {
                                                    String value = parser.getText();
                                                    webInstanceData.put(propertyName, value);
                                                }
                                                parser.resetCursor();
                                            } else if (propertyName.equalsIgnoreCase("grammars")) {
                                                Map<String, String> locations = new LinkedHashMap<>();
                                                if (parser.markCursor(parser.getLocalName())) {
                                                    while (parser.nextElement()) {
                                                        if (parser.getLocalName().equalsIgnoreCase("object")) {
                                                            String location = null;
                                                            String reference = null;
                                                            if (parser.markCursor("object")) {
                                                                while (parser.nextElement()) {
                                                                    if (parser.getLocalName().equalsIgnoreCase("void")) {
                                                                        propertyName = parser.getAttributeValue(null, "property");
                                                                        if ("location".equalsIgnoreCase(propertyName)) {
                                                                            parser.markCursor("string");
                                                                            location = parser.getText();
                                                                            parser.resetCursor();
                                                                        } else if ("reference".equalsIgnoreCase(propertyName)) {
                                                                            parser.markCursor("string");
                                                                            reference = parser.getText();
                                                                            parser.resetCursor();
                                                                        }
                                                                    }
                                                                }
                                                                locations.put(reference, location);
                                                                parser.resetCursor();
                                                                parser.resetCursor();
                                                                parser.resetCursor();
                                                            }
                                                        }
                                                        parser.resetCursor();
                                                    }
                                                    webInstanceData.put(SCHEMA_LOCATIONS, locations);
                                                } else {
                                                    parser.skipElement(parser.getLocalName());
                                                }
                                            }
                                        }
                                        parser.resetCursor();
                                    }
                                } else {
                                    parser.skipElement(parser.getLocalName());
                                }
                            }
                        }
                    }
                    parser.resetCursor();
                }
            }
        } catch (XMLStreamException e) {
            throw new FioranoException("");
        } finally {
            if (parser != null) {
                parser.resetCursor();
            }
        }
        return webInstanceData;
    }

    public static ServiceConfigurationParser INSTANCE() {
        return new ServiceConfigurationParser();
    }


    public Hashtable<String, Object> parseWSStubConfiguration(String configuration) throws FioranoException {
        // Parse the CDATA section and fetch the required data
        FioranoStaxParser parser = null;
        Hashtable<String, Object> webInstanceData = new Hashtable<>();
        try {
            parser = new FioranoStaxParser(new StringReader(configuration));
            parser.markCursor("java");
            while (parser.nextElement()) {
                if (parser.getLocalName().equalsIgnoreCase("object")) {
                    String className = parser.getAttributeValue(null, "class");
                    parser.markCursor(parser.getLocalName());
                    if (className.equalsIgnoreCase(WSSTUB_PM_NEW)) {
                        while (parser.nextElement()) {
                            if (parser.getLocalName().equalsIgnoreCase("void")) {
                                String propertyName = parser.getAttributeValue(null, "property");
                                if (propertyName.equalsIgnoreCase(TRANSPORT_SECURTIY_CONFIG)
                                        || propertyName.equalsIgnoreCase(WSDEFINITION_CONFIG)) {
                                    if (propertyName.equalsIgnoreCase(WSDEFINITION_CONFIG)) {
                                        parser.markCursor(parser.getLocalName());

                                        while (parser.nextElement()) {
                                            if (parser.getLocalName().equalsIgnoreCase("void")) {
                                                propertyName = parser.getAttributeValue(null, "property");
                                                if (propertyName.equalsIgnoreCase(WSDL)
                                                        || propertyName.equalsIgnoreCase(BASEURI)) {
                                                    parser.markCursor(parser.getLocalName());
                                                    while (parser.nextElement()) {
                                                        // Get the value
                                                        String value = parser.getText();
                                                        webInstanceData.put(propertyName, value);
                                                    }
                                                    parser.resetCursor();
                                                } else parser.skipElement(parser.getLocalName());
                                            }
                                        }
                                        parser.resetCursor();
                                    }
                                } else
                                    parser.skipElement(parser.getLocalName());
                            }
                        }
                    }
                    parser.resetCursor();
                }
            }
        } catch (XMLStreamException e) {
            throw new FioranoException("Error parsing WS configuration");
        } finally {
            if (parser != null)
                parser.resetCursor();
        }
        if (webInstanceData.containsKey(WSDL)) {
            String contextName;
            contextName = getContextName((String) webInstanceData.get(WSDL), (String) webInstanceData.get(BASEURI));
            webInstanceData.put(CONTEXT_NAME, contextName);
        }

        return webInstanceData;
    }
    private String getContextName(String wsdl, String baseUri) throws FioranoException {
        try {
            WSDLReader wsdlReader = WSDLFactory.newInstance().newWSDLReader();
            wsdlReader.setExtensionRegistry(new PopulatedExtensionRegistry());
            wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
            wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_IMPORT_DOCUMENTS, true);
            Definition definition = wsdlReader.readWSDL(baseUri, new InputSource(new StringReader(wsdl)));
            @SuppressWarnings("unchecked") Map<QName,Object> services = definition.getServices();
            return services.keySet().iterator().next().getLocalPart();
        } catch (Exception e) {
            throw new FioranoException(e.getMessage(),e);
        }
    }
    public static class ConfigurationMarkups {

        public static final String SCHEMA_LOCATIONS = "SchemaLocations";

        public static final String RESTSTUB_CONFIG_CLASSNAME = "com.fiorano.services.reststub.configuration.RESTStubConfiguration";

        public static final String WADL_CONFIGURATION = "wadlConfiguration";

        public static final String RESTFUL_SERVICE_NAME = "serviceName";
        public static final String WADL = "wadl";
        public static final String CONTEXT_NAME = "contextName";
        public static final String WSSTUB_PM_NEW = "com.fiorano.services.wsstub.configuration.WSStubPM";
        public static final String TRANSPORT_SECURTIY_CONFIG = "transportSecurityConfiguration";
        public static final String WSDEFINITION_CONFIG = "wsDefinitionConfiguration";
        public static final String BASEURI = "baseURI";
        public static final String WSDL = "wsdl";

    }
}
