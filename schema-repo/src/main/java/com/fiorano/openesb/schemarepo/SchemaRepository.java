/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.schemarepo;

import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.SchemaReference;
import com.fiorano.openesb.utils.exception.FioranoException;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

public class SchemaRepository {

    // SingleTon Instance
    private static SchemaRepository m_schemaRepository;
    private static String META_INF_FILE_NAME;

    private String schemaRepositoryFolder;

    private SchemaRepositoryHandler srh;

    //  Security Manager instance for this TES
    private com.fiorano.openesb.security.SecurityManager securityManager;

    private SchemaRepository() {
        try {
            startup();
        } catch (FioranoException e) {
            e.printStackTrace();
        }
    }

    // fetch the SingleTon Instance of the schema repository
    public static synchronized SchemaRepository getSingletonInstance() {
        if(m_schemaRepository == null)
            m_schemaRepository = new SchemaRepository();
        return SchemaRepository.m_schemaRepository;
    }



    /*******************************************************************************************************/
    //@START Component State
    /*******************************************************************************************************/

    /**
     *  This will be used for starting up the schema repository.
     * @exception FioranoException Description of the Exception
     */
    public void startup()
            throws FioranoException
    {
        File repoFile;
            schemaRepositoryFolder = getRepositoryLocation();
            repoFile = new File( schemaRepositoryFolder);
            if (!repoFile.exists()){
                repoFile.mkdirs();
            }
        META_INF_FILE_NAME = schemaRepositoryFolder + File.separator + SchemaRepoConstants.SCHEMA_USER_REPOSITORY +
                File.separator + SchemaRepoConstants.SCHEMA_XML_CATALOG_FILE;
        try {
            srh = SchemaRepositoryHandler.getInstance(META_INF_FILE_NAME, System.getProperty("user.dir"));
        } catch (FioranoException e) {
            //logger.error(Bundle.class, Bundle.FAIL_FETCH_SCHEMA_REPOSITORY_HANDLER_INSTANCE, e);
            throw new FioranoException(SchemaErrorCodes.FAIL_FETCH_SCHEMA_REPO_HANDLER_INSTANCE, e);
        }
    }


    public String getRepositoryLocation() {
        return ServerConfig.getConfig().getRepositoryPath() + File.separator + SchemaRepoConstants.SCHEMA_REPOSITORY_NAME;
    }

    public String getModuleName() {
        return "Schema Repository Manager";
    }

    /*******************************************************************************************************/
    //@END Component State
    /*******************************************************************************************************/

    public void addSchema(byte[] zippedContent, String handleID) throws FioranoException {

        try {
            srh.addSchema(zippedContent);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }

    }

    public String addSchema(byte[] content, String fileName, String handleID) throws FioranoException {

        try {
            return srh.addSchema(content, fileName);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }

    }

    public String addSchema(String schemaAsString, String fileName, String handleID) throws FioranoException {

        try {
            return srh.addSchema(schemaAsString, fileName);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }
    }

    public boolean removeSchema(String nameSpace, String locationHint, String handleID) throws FioranoException {

        try {
            if(srh.hasSchema(nameSpace, locationHint)){
                srh.removeSchema(nameSpace, locationHint);
            }
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_REMOVE_SCHEMA, e);
        }
        return true;
    }

    
    public List<SchemaReference> listOfAllSchemas() throws FioranoException {
        try {
            return srh.listOfAllSchemas();
        } catch (FioranoException e) {
           e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.FAIL_FETCH_LIST_ALL_SCHEMA_REFERENCES, e);
        }
    }
    
    public File getUserSchemaRepositoryPath() {
        return new File(schemaRepositoryFolder + File.separator + SchemaRepoConstants.SCHEMA_USER_REPOSITORY);
    }

    public byte[] getSchema(String nameSpace, String locationHint) throws FioranoException {
        try {
            return srh.getSchemaAsString(nameSpace, locationHint).getBytes("UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.FAIL_FETCH_SCHEMA, e);
        } catch (IOException e ){
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.FAIL_FETCH_SCHEMA, e);
        }
    }

    
    public String getSchemaAsString(String namespace, String location) throws FioranoException {
        try {
            return srh.getSchemaAsString(namespace, location);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.FAIL_FETCH_SCHEMA, e);
        }
    }

    
    public String addSchema(String content, String fileName, boolean overwrite) throws FioranoException {
        try {
            return srh.addSchema(content, fileName, overwrite);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }

    }

    
    public String addSchema(String content, String fileName, boolean overwrite, Map map) throws FioranoException {
        try {
            return srh.addSchema(content, fileName, overwrite, map);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }
    }

    
    public String addSchema(String content, String fileName, Map map) throws FioranoException {
        try {
            return srh.addSchema(content, fileName, map);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new FioranoException(SchemaErrorCodes.ERROR_ADDING_SCHEMA, e);
        }
    }

}

