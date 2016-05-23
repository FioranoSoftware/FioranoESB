/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.sax.FioranoCatalogResolver;
import com.fiorano.openesb.utils.xml.sax.XSDTargetNameSpaceFinder;
import org.apache.xerces.dom.DOMInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class ESBRecordDefinition implements LSResourceResolver, EntityResolver, ISchema {

    //default is XSD
    private int m_definitionType = XSD;

    private String m_structure = null;
    private String m_targetNamespace = null;
    private String m_nsPrefix = null;
    private String m_rootElementName = null;
    private Map m_hashImportedStructures = null;

    /**
     * Constructs the ESBRecordDefinition object.
     */
    public ESBRecordDefinition() {
    }

    /**
     * Constructs the ESBRecordDefinition object with the specified params.
     *
     * @param definitionType - type of format definition (XSD or DTD)
     * @param structure string representing the structure.
     * @param rootElementName name of the root element, if multiple root elements are present in the schema
     * @param importedStructures external Schemas imported in the main schema
     */
    public ESBRecordDefinition(int definitionType, String structure, String rootElementName, Map importedStructures) {
        setDefinitionType(definitionType);
        setStructure(structure);
        setRootElementName(rootElementName);
        setImportedStructures(importedStructures);
    }

    /**
     * Returns type of the format.
     * Valid values are {@link #XSD} and {@link #DTD}.
     * Default value is {@link #XSD}
     */
    public int getDefinitionType() {
        return m_definitionType;
    }

    /**
     * Returns string representation of the format structure
     *
     * @return schema
     */
    public String getStructure() {
        return m_structure;
    }

    /**
     * Returns the namespaces of the imported XSDs
     *
     * @return iterator of imported XSD Namespaces
     */
    public Iterator getImportedStructureNS() {
        if (m_hashImportedStructures == null) {
            return CollectionUtil.EMPTY_ITERATOR;
        }
        return m_hashImportedStructures.keySet().iterator();
    }

    /**
     * Returns the Schemas for a given namespace.
     * A definition may import one or more XSDs having the same target namespace.
     */
    public String[] getImportedStructures(String namespace) {
        if (namespace == null || m_hashImportedStructures == null) {
            return new String[0];
        }
        Object schemas = m_hashImportedStructures.get(namespace);

        if (schemas == null) {
            return new String[0];
        }
        if (schemas instanceof String) {
            return new String[]{(String) schemas};
        }
        if (schemas instanceof String[]) {
            return (String[]) schemas;
        } else {
            List schemasList = (List) schemas;

            if (schemasList.size() == 0) {
                return new String[0];
            }
            String[] schemaStrs = (String[]) schemasList.toArray(new String[0]);

            return schemaStrs;
        }
    }

    /**
     * Returns root element name to be used in case the schema has multiple root elements
     *
     * @return root elements representing the definition
     */
    public String getRootElementName() {
        return m_rootElementName;
    }

    /**
     * Returns target namespace for the schema
     *
     * @return targetNamespace
     */
    public String getTargetNamespace() {
        return m_targetNamespace;
    }

    /**
     * Returns NS prefix to be used.
     *
     * @return namespace prefix.
     */
    public String getNSPrefix() {
        return m_nsPrefix;
    }

    /**
     * Returns the map containing imported extenal structures.
     * Map stores values as "ns" vs "string XSD"
     * This is valid only in case the format type is XSD.
     *
     * @return imported schemas
     */
    public Map getImportedStructures() {
        return m_hashImportedStructures;
    }

    /**
     * Sets format type of this definition
     * Valid values are {@link #XSD} and {@link #DTD}.
     */
    public void setDefinitionType(int defType) {
        m_definitionType = defType;
    }

    /**
     * Sets the schema structure
     */
    public void setStructure(String schema) {
        if (!Util.equals(schema, m_structure)) {
            setSchemaTargetNamespace(findTargetNamespace(schema));
        }
        m_structure = schema;
    }

    /**
     * Sets schemas to be importd.
     */
    public void setImportedStructures(Map importedStructures) {
        m_hashImportedStructures = importedStructures;
    }

    /**
     * Sets the root element name representing the definition of the record.
     */
    public void setRootElementName(String name) {
        m_rootElementName = name;
    }

    /**
     * Sets target namespace of the schema
     */
    public void setTargetNamespace(String targetNS) {
        m_targetNamespace = targetNS;
    }

    /**
     * Sets NS prefix to be used for the targetNamespace.
     */
    public void setNSPrefix(String prefix) {
        m_nsPrefix = prefix;
    }

    /**
     * Adds a feature to the ImportedStructure attribute of the ESBRecordDefinition object
     *
     * @param ns The feature to be added to the ImportedStructure attribute
     * @param xsd The feature to be added to the ImportedStructure attribute
     */
    public void addImportedStructure(String ns, String xsd) {
        if (m_hashImportedStructures == null) {
            m_hashImportedStructures = new Hashtable();
        }

        Object schema = m_hashImportedStructures.get(ns);
        List schemaList = null;

        if (schema == null) {
            schemaList = new ArrayList();
        } else if (schema instanceof String) {
            schemaList = new ArrayList();
            //add the existing xsd
            schemaList.add(schema);
        } else if (schema instanceof String[]) {
            String[] schemaArray = (String[]) schema;

            schemaList = new ArrayList(schemaArray.length);
            for (int i = 0; i < schemaArray.length; i++) {
                String sch = schemaArray[i];

                schemaList.add(sch);
            }
        } else {
            schemaList = (List) schema;
        }

        if (!schemaList.contains(xsd)) {
            schemaList.add(xsd);
        }
        m_hashImportedStructures.put(ns, schemaList);
    }

    /**
     * Returns whether the  Record Definition has any imported structures.
     *
     * @return true of there are imported XSDs available.
     */
    public boolean hasImportedStructures() {
        if (m_hashImportedStructures == null || m_hashImportedStructures.size() == 0) {
            return false;
        }
        return true;
    }

    public void addImportedStructures(ISchema recordDefinition) {
        Map importedStructures = recordDefinition.getImportedStructures();
        if (importedStructures == null || importedStructures.isEmpty()) {
            return;
        }

        for (Object object : importedStructures.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            if (entry.getValue() instanceof String) {
                addImportedStructure((String)entry.getKey(), (String)entry.getValue());
            } else if (entry.getValue() instanceof String[]) {
                String[] schemaArray = (String[]) entry.getValue();
                for (String schema : schemaArray) {
                    addImportedStructure((String) entry.getKey(), schema);
                }
            } else if (entry.getValue() instanceof Collection){
                Collection<String> schemaCollection = (Collection<String>) entry.getValue();
                for (String schema : schemaCollection) {
                    addImportedStructure((String) entry.getKey(), schema);
                }
            }
        }
    }

    /**
     * Returns the String representation of the ESBRecordDefintion object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("ESBRecordDefiniton : [");
        buffer.append("\n\t Type : " + getDefinitionType());
        buffer.append("\n\t Namespace : " + getTargetNamespace());
        buffer.append("\n\t RootElement : " + getRootElementName());
        buffer.append("\n\t NSPrefix : " + getNSPrefix());
        buffer.append("\n\t Structure : " + getStructure());
        if (m_hashImportedStructures != null) {
            buffer.append("\n\t ImportedSchemas : { ");

            Iterator importItr = m_hashImportedStructures.keySet().iterator();

            while (importItr.hasNext()) {
                String ns = (String) importItr.next();
                String[] schemas = getImportedStructures(ns);

                if (schemas == null) {
                    continue;
                }
                buffer.append("\n\t\t ");
                for (int i = 0; i < schemas.length; i++) {
                    String schema = schemas[i];

                    buffer.append(i + " :: ");
                    buffer.append(ns);
                    buffer.append(" : ");
                    buffer.append(schema);
                    buffer.append("\n");
                }
            }
            buffer.append("\n\t }");
        }
        buffer.append("\n]");
        return buffer.toString();
    }

    /**
     * Clones the ESBRecordDefinition.
     *
     * @return cloned ESBRecordDefinition object
     */
    public Object clone() {
        ESBRecordDefinition recordDefinition = new ESBRecordDefinition();

        recordDefinition.setDefinitionType(getDefinitionType());
        recordDefinition.setStructure(getStructure());
        recordDefinition.setRootElementName(getRootElementName());
        recordDefinition.setNSPrefix(getNSPrefix());
        recordDefinition.setTargetNamespace(getTargetNamespace());

        if (getImportedStructures() != null) {
            Map clonedStructures = null;

            if (getImportedStructures() instanceof HashMap) {
                clonedStructures = (Map) ((HashMap) getImportedStructures()).clone();
            } else if (getImportedStructures() instanceof Hashtable) {
                clonedStructures = (Map) ((Hashtable) getImportedStructures()).clone();
            } else {
                clonedStructures = getImportedStructures();
            }
            recordDefinition.setImportedStructures(clonedStructures);
        }
        return recordDefinition;
    }


    /**
     * @param type
     * @param namespaceURI
     * @param publicId
     * @param systemId
     * @param baseURI
     * @return
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {

        if(systemId != null){
            FioranoCatalogResolver catalogResolver = (FioranoCatalogResolver)SAXUtil.getCatalogResolver();

            // delegate to default resolving mechanism
            LSInput lsInput = catalogResolver.resolveResource(systemId, baseURI);
            if(lsInput != null)
                return lsInput;
        }

        String value = null;

        if(systemId != null){
            String[] schemas = getImportedStructures(systemId);

            if (schemas != null && schemas.length > 0) {
                value = schemas[0];
            }
        }
        if(value == null && namespaceURI != null) {
            String[] schemas = getImportedStructures(namespaceURI);

            if (schemas != null && schemas.length > 0) {
                value = schemas[0];
            }
        }

        if(value == null){
            return SAXUtil.getCatalogResolver().resolveResource(type, namespaceURI, publicId, systemId, baseURI);
        }

        LSInput input = new DOMInputImpl();

        input.setPublicId(publicId);
        input.setSystemId(systemId);
        input.setBaseURI(baseURI);
        input.setCharacterStream(new StringReader(value));
        return input;
    }


    private static String findTargetNamespace(String schema) {
        try {
            return XSDTargetNameSpaceFinder.findTargetNamespace(new StringReader(schema));
        } catch (SAXException e) {
            return null;
        } catch (ParserConfigurationException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private String schemaTargetNamespace;

    private String getSchemaTargetNamespace() {
        return schemaTargetNamespace;
    }

    private void setSchemaTargetNamespace(String schemaTargetNamespace) {
        this.schemaTargetNamespace = schemaTargetNamespace;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String str = null;

        if (systemId.startsWith("file:///") && !new File(systemId).exists()) {
            systemId = systemId.substring(systemId.lastIndexOf("/") + 1);
        }

        if (systemId.equalsIgnoreCase(getSchemaTargetNamespace())) {
            str = getStructure();
        } else {
            String[] schemas = getImportedStructures(systemId);

            if (schemas == null || schemas.length == 0) {
                String uri = null;

                if (!new File(systemId).exists()) {
                    uri = SAXUtil.getCatalogResolver().resolveURI(systemId);
                }
                /*if (systemId.startsWith("file:///")) {
                    if (!new File(systemId).exists()) {
                        systemId = systemId.substring(systemId.lastIndexOf("/") + 1);
                    }
                    uri = SAXUtil.getCatalogResolver().resolveURI(systemId);
                }*/

                if (uri == null) {
                    //try again for the file uri
                    if (systemId.startsWith("file:")) {
                        uri = systemId;
                    }
                }

                if (uri != null) {
                    InputSource is = new InputSource(uri);
                    is.setPublicId(publicId);
                    return is;
                }
            } else {
                str = schemas[0];
            }
        }
        return str != null ? new InputSource(new StringReader(str)) : null;
    }


}

