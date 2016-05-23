/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.apache.xerces.util.URI;
import org.apache.xerces.util.XMLCatalogResolver;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.readers.OASISXMLCatalogReader;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FioranoCatalogResolver
        extends XMLCatalogResolver {

    /** Internal catalog manager for Apache catalogs. **/
    private CatalogManager fResolverCatalogManager = null;

    /** Internal catalog structure. **/
    private FioranoCatalog fCatalog = null;

    /** An array of catalog URIs. **/
    private String [] fCatalogsList = null;
    private String [] catalogsList = null;

    /**
     * Indicates whether the list of catalogs has
     * changed since it was processed.
     */
    private boolean fCatalogsChanged = true;

    /** Application specified prefer public setting. **/
    private boolean fPreferPublic = true;

    /**
     * Indicates whether the application desires that
     * the parser or some other component performing catalog
     * resolution should use the literal system identifier
     * instead of the expanded system identifier.
     */
    private boolean fUseLiteralSystemId = true;

    /**
     * <p>Constructs a catalog resolver with a default configuration.</p>
     */
    public FioranoCatalogResolver() {
        this(null, true);
    }

    /**
     * <p>Constructs a catalog resolver with the given
     * list of entry files.</p>
     *
     * @param catalogs an ordered array list of absolute URIs
     */
    public FioranoCatalogResolver(String[] catalogs) {
        this(catalogs, true);
        this.catalogsList = catalogs;
    }

    /**
     * <p>Constructs a catalog resolver with the given
     * list of entry files and the preference for whether
     * system or public matches are preferred.</p>
     *
     * @param catalogs an ordered array list of absolute URIs
     * @param preferPublic the prefer public setting
     */
    public FioranoCatalogResolver(String[] catalogs, boolean preferPublic) {
        this.catalogsList = catalogs;
        init(catalogs, preferPublic);
    }

    public String[] getCatalogs(){
        return catalogsList;
    }


//
//    /**
//     * <p>Returns the initial list of catalog entry files.</p>
//     *
//     * @return the initial list of catalog entry files
//     */
//    public final synchronized String [] getCatalogList () {
//        return (fCatalogsList != null)
//                ? (String[]) fCatalogsList.clone() : null;
//    }
//
//    /**
//     * <p>Sets the initial list of catalog entry files.
//     * If there were any catalog mappings cached from
//     * the previous list they will be replaced by catalog
//     * mappings from the new list the next time the catalog
//     * is queried.</p>
//     *
//     * @param catalogs an ordered array list of absolute URIs
//     */
//    public final synchronized void setCatalogList (String [] catalogs) {
//        fCatalogsChanged = true;
//        fCatalogsList = (catalogs != null)
//                ? (String[]) catalogs.clone() : null;
//    }
//
//    /**
//     * <p>Forces the cache of catalog mappings to be cleared.</p>
//     */
//    public final synchronized void clear () {
//        fCatalog = null;
//    }
//
//    /**
//     * <p>Returns the preference for whether system or public
//     * matches are preferred. This is used in the absence
//     * of any occurence of the <code>prefer</code> attribute
//     * on the <code>catalog</code> entry of a catalog. If this
//     * property has not yet been explicitly set its value is
//     * <code>true</code>.</p>
//     *
//     * @return the prefer public setting
//     */
//    public final boolean getPreferPublic () {
//        return fPreferPublic;
//    }
//
//    /**
//     * <p>Sets the preference for whether system or public
//     * matches are preferred. This is used in the absence
//     * of any occurence of the <code>prefer</code> attribute
//     * on the <code>catalog</code> entry of a catalog.</p>
//     *
//     * @param preferPublic the prefer public setting
//     */
//    public final void setPreferPublic (boolean preferPublic) {
//        fPreferPublic = preferPublic;
//        fResolverCatalogManager.setPreferPublic(preferPublic);
//    }
//
//    /**
//     * <p>Returns the preference for whether the literal system
//     * identifier should be used when resolving system
//     * identifiers when both it and the expanded system
//     * identifier are available. If this property has not yet
//     * been explicitly set its value is <code>true</code>.</p>
//     *
//     * @return the preference for using literal system identifers
//     * for catalog resolution
//     *
//     * @see #setUseLiteralSystemId
//     */
//    public final boolean getUseLiteralSystemId () {
//        return fUseLiteralSystemId;
//    }
//
//    /**
//     * <p>Sets the preference for whether the literal system
//     * identifier should be used when resolving system
//     * identifiers when both it and the expanded system
//     * identifier are available.</p>
//     *
//     * <p>The literal system identifier is the URI as it was
//     * provided before absolutization. It may be embedded within
//     * an entity. It may be provided externally or it may be the
//     * result of redirection. For example, redirection may
//     * have come from the protocol level through HTTP or from
//     * an application's entity resolver.</p>
//     *
//     * <p>The expanded system identifier is an absolute URI
//     * which is the result of resolving the literal system
//     * identifier against a base URI.</p>
//     *
//     * @param useLiteralSystemId the preference for using
//     * literal system identifers for catalog resolution
//     */
//    public final void setUseLiteralSystemId (boolean useLiteralSystemId) {
//        fUseLiteralSystemId = useLiteralSystemId;
//    }

    /**
     * <p>Resolves an external entity. If the entity cannot be
     * resolved, this method should return <code>null</code>. This
     * method returns an input source if an entry was found in the
     * catalog for the given external identifier. It should be
     * overrided if other behaviour is required.</p>
     *
     * @param publicId the public identifier, or <code>null</code> if none was supplied
     * @param systemId the system identifier
     *
     * @throws SAXException any SAX exception, possibly wrapping another exception
     * @throws IOException thrown if some i/o error occurs
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {

        String resolvedId = null;
        if (publicId != null && systemId != null) {
            resolvedId = resolvePublic(publicId, systemId);
        }
        else if (systemId != null) {
            resolvedId = resolveSystem(systemId);
        }

        if (resolvedId != null) {
            InputSource source = new InputSource(resolvedId);
            source.setPublicId(publicId);
            return source;
        }
        return null;
    }

    /**
     * <p>Resolves an external entity. If the entity cannot be
     * resolved, this method should return <code>null</code>. This
     * method returns an input source if an entry was found in the
     * catalog for the given external identifier. It should be
     * overrided if other behaviour is required.</p>
     *
     * @param name the identifier of the external entity
     * @param publicId the public identifier, or <code>null</code> if none was supplied
     * @param baseURI the URI with respect to which relative systemIDs are interpreted.
     * @param systemId the system identifier
     *
     * @throws SAXException any SAX exception, possibly wrapping another exception
     * @throws IOException thrown if some i/o error occurs
     */
    public InputSource resolveEntity(String name, String publicId,
                                     String baseURI, String systemId) throws SAXException, IOException {

        String resolvedId = null;

        if (!getUseLiteralSystemId() && baseURI != null) {
            // Attempt to resolve the system identifier against the base URI.
            try {
                URI uri = new URI(new URI(baseURI), systemId);
                systemId = uri.toString();
            }
            // Ignore the exception. Fallback to the literal system identifier.
            catch (URI.MalformedURIException ex) {}
        }

        if (publicId != null && systemId != null) {
            resolvedId = resolvePublic(publicId, systemId);
        }
        else if (systemId != null) {
            resolvedId = resolveSystem(systemId);
        }

        if (resolvedId != null) {
            InputSource source = new InputSource(resolvedId);
            source.setPublicId(publicId);
            return source;
        }
        return null;
    }

    /**
     * <p>Locates an external subset for documents which do not explicitly
     * provide one. This method always returns <code>null</code>. It
     * should be overrided if other behaviour is required.</p>
     *
     * @param name the identifier of the document root element
     * @param baseURI the document's base URI
     *
     * @throws SAXException any SAX exception, possibly wrapping another exception
     * @throws IOException thrown if some i/o error occurs
     */
    public InputSource getExternalSubset(String name, String baseURI)
            throws SAXException, IOException {
        return null;
    }

    /**
     * <p>Resolves a resource using the catalog. This method interprets that
     * the namespace URI corresponds to uri entries in the catalog.
     * Where both a namespace and an external identifier exist, the namespace
     * takes precedence.</p>
     *
     * @param type the type of the resource being resolved
     * @param namespaceURI the namespace of the resource being resolved,
     * or <code>null</code> if none was supplied
     * @param publicId the public identifier of the resource being resolved,
     * or <code>null</code> if none was supplied
     * @param systemId the system identifier of the resource being resolved,
     * or <code>null</code> if none was supplied
     * @param baseURI the absolute base URI of the resource being parsed,
     * or <code>null</code> if there is no base URI
     */
    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {

        String resolvedId = null;

        try {
            // The namespace is useful for resolving namespace aware
            // grammars such as XML schema. Let it take precedence over
            // the external identifier if one exists.
            if (namespaceURI != null) {
                resolvedId = (String) resolveURI(namespaceURI);   //For now first element
            }

            if (!getUseLiteralSystemId() && baseURI != null) {
                // Attempt to resolve the system identifier against the base URI.
                try {
                    URI uri = new URI(new URI(baseURI), systemId);
                    systemId = uri.toString();
                }
                // Ignore the exception. Fallback to the literal system identifier.
                catch (URI.MalformedURIException ex) {}
            }

            // Resolve against an external identifier if one exists. This
            // is useful for resolving DTD external subsets and other
            // external entities. For XML schemas if there was no namespace
            // mapping we might be able to resolve a system identifier
            // specified as a location hint.
            if (resolvedId == null) {
                if (publicId != null && systemId != null) {
                    resolvedId = resolvePublic(publicId, systemId);
                }
                else if (systemId != null) {
                    resolvedId = resolveSystem(systemId);
                }
            }
        }
        // Ignore IOException. It cannot be thrown from this method.
        catch (IOException ex) {}

        if (resolvedId != null) {
            return new DOMInputImpl(publicId, resolvedId, baseURI);
        }
        return null;
    }


    /**
     * <p>Resolves an external entity. If the entity cannot be
     * resolved, this method should return <code>null</code>. This
     * method only calls <code>resolveIdentifier</code> and returns
     * an input source if an entry was found in the catalog. It
     * should be overrided if other behaviour is required.</p>
     *
     * @param resourceIdentifier location of the XML resource to resolve
     *
     * @throws XNIException thrown on general error
     * @throws IOException thrown if some i/o error occurs
     */
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
            throws XNIException, IOException {

        String resolvedId = resolveIdentifier(resourceIdentifier);
        if (resolvedId != null) {
            return new XMLInputSource(resourceIdentifier.getPublicId(),
                    resolvedId,
                    resourceIdentifier.getBaseSystemId());
        }
        return null;
    }

    public synchronized String resolveNamespace(String uri)
            throws IOException {

        if (fCatalogsChanged) {
            parseCatalogs();
            fCatalogsChanged = false;
        }
        return (fCatalog != null)
                ? fCatalog.resolveURI(uri) : null;
    }

    /**
     * <p>Resolves an identifier using the catalog. This method interprets that
     * the namespace of the identifier corresponds to uri entries in the catalog.
     * Where both a namespace and an external identifier exist, the namespace
     * takes precedence.</p>
     *
     * @param resourceIdentifier the identifier to resolve
     *
     * @throws XNIException thrown on general error
     * @throws IOException thrown if some i/o error occurs
     */
    public String resolveIdentifier(XMLResourceIdentifier resourceIdentifier)
            throws IOException, XNIException {

        String resolvedId = null;
        List resolvedIds;
        // The namespace is useful for resolving namespace aware
        // grammars such as XML schema. Let it take precedence over
        // the external identifier if one exists.
        String namespace = resourceIdentifier.getNamespace();
        if (namespace != null) {
            resolvedId = resolveURI(namespace);

        }

        // Resolve against an external identifier if one exists. This
        // is useful for resolving DTD external subsets and other
        // external entities. For XML schemas if there was no namespace
        // mapping we might be able to resolve a system identifier
        // specified as a location hint.
        if (resolvedId == null) {
            String publicId = resourceIdentifier.getPublicId();
            String systemId = getUseLiteralSystemId()
                    ? resourceIdentifier.getLiteralSystemId()
                    : resourceIdentifier.getExpandedSystemId();
            if (publicId != null && systemId != null) {
                resolvedId = resolvePublic(publicId, systemId);
            }
            else if (systemId != null) {
                resolvedId = resolveSystem(systemId);
            }
        }
        return resolvedId;
    }

    /**
     * <p>Returns the URI mapping in the catalog for the given
     * external identifier or <code>null</code> if no mapping
     * exists. If the system identifier is an URN in the
     * <code>publicid</code> namespace it is converted into
     * a public identifier by URN "unwrapping" as specified
     * in the XML Catalogs specification.</p>
     *
     * @param systemId the system identifier to locate in the catalog
     *
     * @return the mapped URI or <code>null</code> if no mapping
     * was found in the catalog
     *
     * @throws IOException if an i/o error occurred while reading
     * the catalog
     */
//    public final synchronized String resolveSystem (String systemId)
//            throws IOException {
//
//        if (fCatalogsChanged) {
//            parseCatalogs();
//            fCatalogsChanged = false;
//        }
//        return (fCatalog != null)
//                ? fCatalog.resolveSystem(systemId) : null;
//    }

    /**
     * <p>Returns the URI mapping in the catalog for the given
     * external identifier or <code>null</code> if no mapping
     * exists. Public identifiers are normalized before
     * comparison.</p>
     *
     * @param publicId the public identifier to locate in the catalog
     * @param systemId the system identifier to locate in the catalog
     *
     * @return the mapped URI or <code>null</code> if no mapping
     * was found in the catalog
     *
     * @throws IOException if an i/o error occurred while reading
     * the catalog
     */
//    public final synchronized String resolvePublic (String publicId, String systemId)
//            throws IOException {
//
//        if (fCatalogsChanged) {
//            parseCatalogs();
//            fCatalogsChanged = false;
//        }
//        return (fCatalog != null)
//                ? fCatalog.resolvePublic(publicId, systemId) : null;
//    }

    /**
     * <p>Returns the URI mapping in the catalog for the given URI
     * reference or <code>null</code> if no mapping exists.
     * URI comparison is case sensitive. If the URI reference
     * is an URN in the <code>publicid</code> namespace
     * it is converted into a public identifier by URN "unwrapping"
     * as specified in the XML Catalogs specification and then
     * resolution is performed following the semantics of
     * external identifier resolution.</p>
     *
     * @param uri the URI to locate in the catalog
     *
     * @return the mapped URI or <code>null</code> if no mapping
     * was found in the catalog
     *
     * @throws IOException if an i/o error occurred while reading
     * the catalog
     */
    public final synchronized List resolveURIs (String uri)
            throws IOException {

        if (fCatalogsChanged) {
            parseCatalogs();
            fCatalogsChanged = false;
        }
        return (fCatalog != null)
                ? fCatalog.getURIList(uri) : null;
    }


//    public final synchronized String resolveURI (String uri)
//            throws IOException {
//
//        if (fCatalogsChanged) {
//            parseCatalogs();
//            fCatalogsChanged = false;
//        }
//        return (fCatalog != null)
//                ? fCatalog.resolveURI(uri) : null;
//    }

    /**
     * Initialization. Create a CatalogManager and set all
     * the properties upfront. This prevents JVM wide system properties
     * or a property file somewhere in the environment from affecting
     * the behaviour of this catalog resolver.
     */
    private void init (String [] catalogs, boolean preferPublic) {
        fCatalogsList = (catalogs != null) ? catalogs.clone() : null;
        fPreferPublic = preferPublic;
        fResolverCatalogManager = new CatalogManager();
        fResolverCatalogManager.setAllowOasisXMLCatalogPI(false);
        fResolverCatalogManager.setCatalogClassName("org.apache.xml.resolver.Catalog");
        fResolverCatalogManager.setCatalogFiles("");
        fResolverCatalogManager.setIgnoreMissingProperties(true);
        fResolverCatalogManager.setPreferPublic(fPreferPublic);
        fResolverCatalogManager.setRelativeCatalogs(false);
        fResolverCatalogManager.setUseStaticCatalog(false);
        fResolverCatalogManager.setVerbosity(0);
        if (fCatalogsList != null) {
            fCatalog = new FioranoCatalog(fResolverCatalogManager);
            attachReaderToCatalog(fCatalog);
            for (int i = 0; i < fCatalogsList.length; ++i) {
                String catalog = fCatalogsList[i];
                if (catalog != null && catalog.length() > 0) {
                    try {
                        fCatalog.parseCatalog(new URL(catalog));
                    } catch (IOException e) {
                        //
                    }
                }
            }
            fCatalogsChanged = false;
        }

    }

    /**
     * Instruct the <code>Catalog</code> to parse each of the
     * catalogs in the list. Only the first catalog will actually be
     * parsed immediately. The others will be queued and read if
     * they are needed later.
     */
    private void parseCatalogs () throws IOException {
        if (fCatalogsList != null) {
            fCatalog = new FioranoCatalog(fResolverCatalogManager);
            attachReaderToCatalog(fCatalog);
            for (int i = 0; i < fCatalogsList.length; ++i) {
                String catalog = fCatalogsList[i];
                if (catalog != null && catalog.length() > 0) {
                    fCatalog.parseCatalog(catalog);
                }
            }
        }
        else {
            fCatalog = null;
        }
    }

    /**
     * Attaches the reader to the catalog.
     */
    private void attachReaderToCatalog (Catalog catalog) {

        SAXParserFactory spf = new SAXParserFactoryImpl();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXCatalogReader saxReader = new SAXCatalogReader(spf);
        saxReader.setCatalogParser(OASISXMLCatalogReader.namespaceName, "catalog",
                "org.apache.xml.resolver.readers.OASISXMLCatalogReader");
        catalog.addReader("application/xml", saxReader);
    }

    public LSInput resolveResource(String systemId, String baseUR){
        return null;
    }

}
