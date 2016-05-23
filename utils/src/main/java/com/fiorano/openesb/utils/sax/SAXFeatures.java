/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

public interface SAXFeatures {

    String NAMESPACES = "http://xml.org/sax/features/namespaces"; //NOI18N

    String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes"; //NOI18N

    String VALIDATION = "http://xml.org/sax/features/validation"; //NOI18N

    String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities"; //NOI18N

    String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities"; //NOI18N

    String IS_STANDALONE = "http://xml.org/sax/features/is-standalone"; //NOI18N

    String LEXICAL_HANDLER_PARAMETER_ENTITIES = "http://xml.org/sax/features/lexical-handler/parameter-entities"; //NOI18N

    String RESOLVE_DTD_URIS = "http://xml.org/sax/features/resolve-dtd-uris"; //NOI18N

    String STRING_INTERNING = "http://xml.org/sax/features/string-interning"; //NOI18N

    String UNICODE_NORMALIZATION_CHECKING = "http://xml.org/sax/features/unicode-normalization-checking"; //NOI18N

    String USE_ATTRIBUTES2 = "http://xml.org/sax/features/use-attributes2"; //NOI18N

    String USE_LOCATOR2 = "http://xml.org/sax/features/use-locator2"; //NOI18N

    String USE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2"; //NOI18N

    String XML_URIS = "http://xml.org/sax/features/xmlns-uris"; //NOI18N

    String XML_1_1 = "http://xml.org/sax/features/xml-1.1"; //NOI18N
}
