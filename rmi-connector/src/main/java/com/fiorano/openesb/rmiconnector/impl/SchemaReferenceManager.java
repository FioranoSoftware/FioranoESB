/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ApplicationParser;
import com.fiorano.openesb.application.application.SchemaReference;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.rmiconnector.api.ISchemaReferenceManager;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.schemarepo.SchemaRepository;
import com.fiorano.openesb.security.SecurityManager;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.ExceptionUtil;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.ZipUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class SchemaReferenceManager extends AbstractRmiManager implements ISchemaReferenceManager {

    //Reference to instanceHandler instace.
    private InstanceHandler handler;

    private SchemaRepository schemaRepository;
    private ApplicationRepository applicationRepository;
    private SecurityManager userSecurityManager;
    //maintains a hashMap to keep references to files, that are being creating when executing operations addSchema, get all schemas from repository
    private Map<String, File> schemaMaps = new HashMap<String, File>(8);
    public SchemaReferenceManager(RmiManager rmiManager, InstanceHandler handler) {
        super(rmiManager);
        this.schemaRepository = rmiManager.getSchemaRepository();
        this.applicationRepository = rmiManager.getApplicationRepository();
        this.userSecurityManager = rmiManager.getSecurityManager();
        this.handler = handler;
        setHandleID(handler.getHandleID());
    }

    protected String getHighestVersion(String id, String handleID) throws ServiceException {
        throw new UnsupportedOperationException("This operation is not supported for this instance");
    }

    /**
     * Adds Schema Reference to Schema Repository
     *
     *
     * @param zippedContent
     * @param completed Notifies server that sending zip contents completed
     * @throws ServiceException Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public void addSchemaReferences(byte[] zippedContent, boolean completed) throws RemoteException, ServiceException {
        File tempZipFile = null;
        FileOutputStream outstream = null;
        String key = "__DEPLOYED";
        boolean successfulzip = true;

        //get the Schema references zip as byte array from server. keep writing to a zip file until client notifies completed
        try {
            tempZipFile = schemaMaps.get(key);
            if (tempZipFile == null) {
                tempZipFile = getTempFile("SchemaReference", "zip");
                schemaMaps.put(key, tempZipFile);
            }
            outstream = new FileOutputStream(tempZipFile, true);
            outstream.write(zippedContent);
        } catch (IOException ioe) {
            successfulzip = false;
            ioe.printStackTrace();
            throw new ServiceException("UNABLE_TO_CREATE_SCHEMA_REFERENCE_ZIPFILE");
        }
        finally {
            try {
                if (outstream != null) {
                    outstream.close();
                }
                if (!successfulzip && tempZipFile != null) {
                    tempZipFile.delete();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        if (!completed) {
            return;
        }

        //save to fes repository.
        try {
            schemaRepository.addSchema(getBytesFromFile(tempZipFile), handleId);
        } catch (FioranoException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        finally {
            // Temporaray application Folder is being deleted in Application Repository Code
//            FileUtil.deleteDir(appFileTempFolder);
            schemaMaps.remove(key);
            tempZipFile.delete();
        }
    }

    /**
     * Adds Schema reference to Schema Repository
     *
     *
     * @param filecontent
     * @param fileName File name which content is sent
     * @return FileName returns the File Name with which server had persisted the schema in Schema Repository
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchemaReference(byte[] filecontent, String fileName) throws RemoteException, ServiceException {
        try {
            return schemaRepository.addSchema(filecontent, fileName, handleId);
        } catch (FioranoException e) {
            throw new ServiceException("ERR_ADDING_SCHEMA_REFERENCE", e);
        }
    }

    public String addSchemaReference(String schemaAsString, String fileName) throws RemoteException, ServiceException {
        try {
            return schemaRepository.addSchema(schemaAsString, fileName, handleId);
        } catch (FioranoException e) {
            throw new ServiceException("ERR_ADDING_SCHEMA_REFERENCE", e);
        }
    }

    /**
     * Method to retrieves all Schema References associated with services in particular Application
     * @param appGUID  application GUID for which the Schema References should be retrieved
     * @param version  application Version for which the Schema References should be retrieved
     * @return Retrieves List of Schema References
     * @throws ServiceException  Thrown if the failed to get schema reference for application
     */
    public List<SchemaReference> getSchemaReferences(String appGUID, float version) throws RemoteException, ServiceException {
        List<SchemaReference> listSchemaReferencesApp = new ArrayList<SchemaReference>();
        try {
            File applicationDir = applicationRepository.getAppDir(appGUID, version);
            Application application = new Application();
            try {
                application = ApplicationParser.readApplication(applicationDir, true, true);
            } catch (Exception e) {
                throw new ServiceException("ERR_READING_EVENT_PROCESS", e);
            }
            List<ServiceInstance> services = application.getServiceInstances();
            for (ServiceInstance service : services) {
                List<SchemaReference> schemaReferences = service.getSchemaReferences();
                if(schemaReferences != null && schemaReferences.size() > 0){
                    listSchemaReferencesApp.addAll(schemaReferences);
                }
            }
            return listSchemaReferencesApp;
        }catch (Exception e) {
            throw new ServiceException("ERR_SEARCHING_APP_CONFIGURATIONS", e);
        }
    }

    public boolean removeSchemaReferences(String nameSpace, String locationHint) throws RemoteException, ServiceException {
        try{
            return schemaRepository.removeSchema(nameSpace, locationHint, handleId);
        }catch (FioranoException e){
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public List<SchemaReference> listOfAllSchemaReferences() throws RemoteException, ServiceException {
        try {
            return schemaRepository.listOfAllSchemas();
        } catch (FioranoException e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public byte[] getAllSchemasPresentInServer(long index) throws RemoteException, ServiceException {
        File userSchemaRepositoryPath = schemaRepository.getUserSchemaRepositoryPath();
        File tempdir = null;
        File zipTempFile = null;
        ZipOutputStream zipStream = null;
        BufferedInputStream bis = null;
        boolean completed = false;

        if (index == 0 ) {
            try {
                //Create Temporary Directory
                tempdir = getTempFile("application", "tmp");
                if (!tempdir.exists())
                    tempdir.mkdir();

                copyDirectory(userSchemaRepositoryPath, tempdir);
                zipTempFile = getTempFile("application", "zip");
                if (!zipTempFile.exists())
                    zipTempFile.exists();
                zipStream = new ZipOutputStream(new FileOutputStream(zipTempFile));
                ZipUtil.zipDir(tempdir, tempdir, zipStream);
                schemaMaps.put(Constants.SCHEMA_ZIP_LOCATION, zipTempFile);
            } catch (Exception e) {
                completed = true;
                e.printStackTrace();
                throw new ServiceException("FAILED_TO_FETCH_SCHEMAS_FROM_REPOSITORY", e);
            } finally {

                try {
                    if (zipStream != null)
                        zipStream.close();
                    if (tempdir != null)
                        FileUtil.deleteDir(tempdir);
                } catch (IOException e) {
                    //ignore
                }
            }
        }else {
            zipTempFile = schemaMaps.get(Constants.SCHEMA_ZIP_LOCATION);
            if (zipTempFile == null)
                return null;
        }

        byte[] result = new byte[Constants.CHUNK_SIZE];

        try {
            bis = new BufferedInputStream(new FileInputStream(zipTempFile));
            bis.skip(index);
            int read = bis.read(result);
            if (read < 0){
                completed   = true;
            }
        } catch (IOException e) {
            completed = true;
            e.printStackTrace();
            throw new ServiceException("ERROR_WHILE_SENDING_ZIPPED_SCHEMA_CONTENT", e);
        }finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (completed) {
                    schemaMaps.remove(Constants.SCHEMA_ZIP_LOCATION);
                    if (zipTempFile != null) {
                        zipTempFile.delete();
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }

        return result;
    }

    /**
     * Method to get Schema Reference file Content as byte array from Schema Repository
     * @param nameSpace nameSpace associated with Schema
     * @param locationHint Schema Reference File Name.
     * @return returns byte array of schema Reference file
     * @throws ServiceException Thrown if the failed to get Schema Reference from Schema Repository
     */
    public byte[] getSchemaReference(String nameSpace, String locationHint) throws RemoteException, ServiceException {

        try {
            return schemaRepository.getSchema(nameSpace, locationHint);
        } catch (FioranoException e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public String getSchemaAsString(String namespace, String location) throws RemoteException, ServiceException {
        try {
            return schemaRepository.getSchemaAsString(namespace, location);
        } catch (FioranoException e){
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public String addSchema(String content, String fileName, boolean overwrite) throws RemoteException, ServiceException {
        try {
            return schemaRepository.addSchema(content, fileName, overwrite);
        } catch (FioranoException e){
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public String addSchema(String content, String fileName, boolean overwrite, Map map) throws RemoteException, ServiceException {
        try {
            return schemaRepository.addSchema(content, fileName, overwrite, map);
        } catch (FioranoException e){
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    @Override
    public String addSchema(String content, String fileName, Map map) throws RemoteException, ServiceException {
        try {
            return schemaRepository.addSchema(content, fileName, map);
        } catch (FioranoException e){
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());

    }

    public String toString() {
        return Constants.SCHEMA_REFERENCE_MANAGER;
    }
    private ISchemaReferenceManager clientProxyInstance;

    void setClientProxyInstance(ISchemaReferenceManager clientProxyInstance) {
        this.clientProxyInstance = clientProxyInstance;
    }

    ISchemaReferenceManager getClientProxyInstance() {
        return clientProxyInstance;
    }
}

