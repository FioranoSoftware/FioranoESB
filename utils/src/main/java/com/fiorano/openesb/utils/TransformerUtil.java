/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.ClassUtil;
import javax.xml.transform.*;

public class TransformerUtil{
    public static final String XALAN_TRANSFORMER_FACTORY = "org.apache.xalan.processor.TransformerFactoryImpl";
    public static final String SAXON_TRANSFORMER_FACTORY = "net.sf.saxon.TransformerFactoryImpl";
    public static final String XSLTC_TRANSFORMER_FACTORY = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";

    public static TransformerFactory createFactory(String factoryName) throws TransformerConfigurationException{
        TransformerFactory factory = null;
        try{
            factory = factoryName!=null
                    ? (TransformerFactory)ClassUtil.loadClass(factoryName, null).newInstance()
                    : TransformerFactory.newInstance();
        } catch(InstantiationException ex){
            throw new TransformerConfigurationException(ex);
        } catch(IllegalAccessException ex){
            throw new TransformerConfigurationException(ex);
        } catch(ClassNotFoundException ex){
            throw new TransformerConfigurationException(ex);
        }
        boolean isSaxon = factory.getClass().getName().indexOf("saxon")!=-1; //NOI18N

        if(isSaxon)
            factory.setAttribute("http://saxon.sf.net/feature/version-warning"/*FeatureKeys.VERSION_WARNING*/, Boolean.FALSE);
        return factory;
    }

    public static Transformer createTransformer(Source source, boolean indent, boolean omitXMLDeclaration)
            throws TransformerConfigurationException{
        return createTransformer(source, indent, omitXMLDeclaration, null);
    }

    /**
     * creates a transformer with given information
     *
     * @param source  Source pointing to xslt. can be null
     * @param indent  wheter to indent the output or not
     * @param omitXMLDeclaration whether to omit xml declaration or not
     * @param encoding output encoding
     * @return transformer, if source is null it creates Identity Transformer
     */
    public static Transformer createTransformer(Source source, boolean indent, boolean omitXMLDeclaration, String encoding)
            throws TransformerConfigurationException{
        return createTransformer(createFactory(null), source, indent, omitXMLDeclaration, encoding);
    }

    public static Transformer createTransformer(String factoryName, Source source, boolean indent, boolean omitXMLDeclaration, String encoding)
            throws TransformerConfigurationException{
        return createTransformer(createFactory(factoryName), source, indent, omitXMLDeclaration, encoding);
    }

    private static Transformer createTransformer(TransformerFactory factory, Source source, boolean indent, boolean omitXMLDeclaration, String encoding)
            throws TransformerConfigurationException{
        Transformer transformer = source==null
                ? factory.newTransformer()
                : factory.newTransformer(source);
        if(indent){
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");                         //NOI18N
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //NOI18N
        }
        if(omitXMLDeclaration)
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");                         //NOI18N
        if(encoding!=null)
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);

        boolean isSaxon = factory.getClass().getName().indexOf("saxon")!=-1; //NOI18N
        if(isSaxon)
            transformer.setOutputProperty("undeclare-prefixes"/*SaxonOutputKeys.UNDECLARE_PREFIXES*/, "yes"); //NOI18N

        return transformer;
    }
}