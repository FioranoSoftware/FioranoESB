/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;

import java.util.Map;

public interface ISchema extends LSResourceResolver, EntityResolver {
    /**
     * Null structure
     */
    int NONE = 0;
    /**
     * Format definition is XSD schema (default)
     */
    int XSD = 1;
    /**
     * Format definition is DTD
     */
    int DTD = 2;

    int getDefinitionType();

    String getStructure();

    String getRootElementName();

    String getTargetNamespace();

    Map getImportedStructures();

    void setDefinitionType(int defType);

    void setStructure(String schema);

    void setRootElementName(String name);

    void setTargetNamespace(String targetNS);

    void addImportedStructure(String ns, String xsd);

    Object clone();

    void addImportedStructures(ISchema schema);
}
