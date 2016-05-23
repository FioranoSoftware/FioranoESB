/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.helpers.PublicId;
import org.apache.xml.resolver.readers.OASISXMLCatalogReader;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.apache.xml.resolver.readers.TR9401CatalogReader;

import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class FioranoCatalog  extends Catalog {

    public FioranoCatalog(){}

    public FioranoCatalog(CatalogManager fResolverCatalogManager) {
        catalogManager = fResolverCatalogManager;
    }


    public void setupReaders() {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXCatalogReader saxReader = new SAXCatalogReader(spf);

            saxReader.setCatalogParser(null, "XMLCatalog",
                    "org.apache.xml.resolver.readers.XCatalogReader");

            saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName,
                    "catalog",
                    "org.apache.xml.resolver.readers.OASISXMLCatalogReader");

        addReader("application/xml", saxReader);
        addReader("text/xml", saxReader);

        TR9401CatalogReader textReader = new TR9401CatalogReader();
        addReader("text/plain", textReader);
    }

    public String resolveURI(String uri)
            throws MalformedURLException, IOException {

        catalogManager.debug.message(3, "resolveURI("+uri+")");

        uri = normalizeURI(uri);

        if (uri != null && uri.startsWith("urn:publicid:")) {
            uri = PublicId.decodeURN(uri);
            return resolvePublic(uri, null);
        }

        // If there's a URI entry in this catalog, use it
        if (uri != null) {
            String resolved = resolveLocalURI(uri);
            if (resolved != null) {
                return resolved;
            }
        }

        return resolveSubordinateCatalogs(URI,
                null,
                null,
                uri);
    }

    protected String resolveLocalURI(String uri) throws MalformedURLException, IOException {
        Enumeration en = catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e = (CatalogEntry) en.nextElement();
            if (e.getEntryType() == URI
                    && (e.getEntryArg(0).equals(uri))) {
                return e.getEntryArg(1);
            }
        }
        return null;
    }

    protected List getURIList(String uri) throws MalformedURLException, IOException {
        ArrayList list = new ArrayList();
        Enumeration en = catalogEntries.elements();
        while (en.hasMoreElements()) {
            CatalogEntry e = (CatalogEntry) en.nextElement();
            if (e.getEntryType() == URI
                    && (e.getEntryArg(0).equals(uri))) {
                list.add(e.getEntryArg(1));
            }
        }
        return list;
    }
}