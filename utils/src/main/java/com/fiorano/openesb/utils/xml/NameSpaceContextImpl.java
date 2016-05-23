/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.utils.xml;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NameSpaceContextImpl implements NamespaceContext {

    private Map<String, String> namespaceMap = new HashMap<>();

    public NameSpaceContextImpl(Map<String, String> namespaces) {
        namespaceMap.putAll(namespaces);
    }

    public String getNamespaceURI(String arg0) {
        return namespaceMap.get(arg0);
    }

    public void putPrefix(String prefix, String namespace) {
        namespaceMap.put(prefix, namespace);
    }

    public String getPrefix(String namespaceURI) {
        for (String key : namespaceMap.keySet()) {
            String value = namespaceMap.get(key);
            if (value.equals(namespaceURI)) {
                return key;
            }
        }
        return null;
    }

    public Iterator<String> getPrefixes(String arg0) {
        return namespaceMap.keySet().iterator();
    }
}
