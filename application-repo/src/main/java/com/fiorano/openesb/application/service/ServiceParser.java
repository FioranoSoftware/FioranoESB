/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;


import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.MarkableInputStream;
import com.fiorano.openesb.utils.MarkableReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceParser extends DefaultHandler{

    /**
     * Schemas folder name
     */
    public static final String SCHEMAS_FOLDER = "schemas";
    /**
     * Service descriptor xml
     */
    public static final String SERVICE_DESCRIPTOR_XML = "ServiceDescriptor.xml";

    /**
     * Path of the configuration repository where Enterprise Server stores its named configurations
     */
    private static String configurationRepoPath;

    /**
     * Sets path of configuration repository where Enterprise Server stores its named configurations
     * @param repositoryPath - Path of the repository
     */
    public static void setConfigurationRepoPath(String repositoryPath) {
        ServiceParser.configurationRepoPath = repositoryPath;
    }

    /**
     * Creates Service DMI by reading contents of specified service descriptor xml <code>file</code>
     * @param file Service descriptor xml
     * @return Service - An object of Service having properties as mentioned in the service descriptor
     * @throws FioranoException FioranoException
     */
    public static Service readService(File file) throws FioranoException{
        return readService(file, false);
    }

    /**
     * Creates Service DMI by reading contents of specified service descriptor xml <code>file</code>
     * @param file Service descriptor xml
     * @param checkForNamedConfigurations If set to true, a check is made for existence of all named configurations. If some named configuration
     * does not exist, then an exception will be thrown to indicate the error situation.
     * @return Service - An object of Service having properties as mentioned in the service descriptor
     * @throws FioranoException FioranoException
     */
    public static Service readService(File file, boolean checkForNamedConfigurations) throws FioranoException{
        try{
            FileReader reader = new FileReader(file);
            FileInputStream stream = null;
            try{
                    Service service = new Service();
                    stream = new FileInputStream(file);
                    service.setFieldValues(stream);

                    resolveSchemaContentFromFiles(service, file, checkForNamedConfigurations);

                    return service;

            }finally{
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    // Ignore
                }
                try {
                    if(stream != null)
                        stream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }

    /**
     * Sets schema content for this service by parsing specified file
     * @param file Schema file
     * @throws FioranoException FioranoException
     */
    private static void resolveSchemaContentFromFiles(Service service, File file, boolean checkForNamedConfigurations) throws FioranoException, IOException {
        File parentFolder = new File(file.getParent());

        List<Port> ports = new ArrayList<Port>();
        if(service.getExecution()!=null){
            ports.addAll(service.getExecution().getInputPorts());
            ports.addAll(service.getExecution().getOutputPorts());
        }

        for (Port port : ports) {
            String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
            if (schemaConfigName != null && configurationRepoPath != null) {
                File schemaConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator + ConfigurationRepoConstants.RESOURCES + File.separator + schemaConfigName);

                if (schemaConfigFile.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(schemaConfigFile);
                        port.getSchema().setContent(DmiObject.getContents(fis));
                    } finally {
                        try {
                            if (fis != null)
                                fis.close();
                        } catch (IOException e) {
                            //Ignore
                        }
                    }
                } else if (checkForNamedConfigurations)
                    throw new FioranoException("SCHEMA_CONFIGURATION_NOT_FOUND_FOR_SERVICE");

                Map schemaRefMap = port.getSchema().getSchemaRefConfigs();
                for (Object obj : schemaRefMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) obj;
                    String uri = (String) entry.getKey();
                    String schemaRefConfigName = (String) entry.getValue();

                    File schemaRefFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator + ConfigurationRepoConstants.RESOURCES + File.separator + schemaRefConfigName);
                    if (schemaRefFile.exists()) {
                        FileInputStream schemaRefFileStream = null;
                        try {
                            schemaRefFileStream = new FileInputStream(schemaRefFile);
                            port.getSchema().addSchemaRef(uri, DmiObject.getContents(schemaRefFileStream));
                        } finally {
                            try {
                                if (schemaRefFileStream != null)
                                    schemaRefFileStream.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("SCHEMA_REF_CONFIGURATION_NOT_FOUND_FOR_SERVICE");
                }
            } else {
                String schemaFileName = (port.getSchema() == null) ? null : port.getSchema().getFile();
                if (schemaFileName != null) {
                    File schemaFile = new File(getSchemaFileName(parentFolder.getPath(), schemaFileName));

                    if (schemaFile.exists()) {
                        FileInputStream schemaFileStream = new FileInputStream(schemaFile);
                        try {
                            port.getSchema().setContent(DmiObject.getContents(schemaFileStream));
                        } finally {
                            try {
                                if (schemaFileStream != null)
                                    schemaFileStream.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    }

                    Map schemaRefMap = port.getSchema().getSchemaRefFiles();
                    for (Object obj : schemaRefMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) obj;
                        String uri = (String) entry.getKey();
                        String schemaRefFileName = (String) entry.getValue();

                        File schemaRefFile = new File(getSchemaFileName(parentFolder.getPath(), schemaRefFileName));
                        if (schemaRefFile.exists()) {
                            FileInputStream schemaRefFileStream = new FileInputStream(schemaRefFile);
                            try {
                                port.getSchema().addSchemaRef(uri, DmiObject.getContents(schemaRefFileStream));
                            } finally {
                                try {
                                    if (schemaRefFileStream != null)
                                        schemaRefFileStream.close();
                                } catch (IOException e) {
                                    //Ignore
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates Service DMI by reading contents from specified input stream <code>is</code> to service descriptor xml
     * @param is Input stream to service descriptor xml
     * @return Service  - An object of Service having properties as mentioned in the service descriptor
     * @throws FioranoException FioranoException
     */
    public static Service readService(InputStream is) throws FioranoException{

        Service serviceRead;

        if(!is.markSupported()){
            is = new MarkableInputStream(is){
                boolean closeCalled = false;
                public void close() throws IOException{
                    if(closeCalled)
                        super.close();
                    else
                        closeCalled = true;
                }
            };
        }else{
            is = new FilterInputStream(is){
                boolean closeCalled = false;
                public void close() throws IOException{
                    if(closeCalled)
                        super.close();
                    else
                        closeCalled = true;
                }
            };
        }
        is.mark(Integer.MAX_VALUE);
        try{

                is.reset();
                Service service = new Service();
                service.setFieldValues(is);
                serviceRead = service;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
        return serviceRead;
    }

    /**
     * Creates Service DMI by reading contents from specified reader <code>reader</code> to service descriptor xml
     * @param reader Reader to service descriptor xml
     * @return Service - An object of Service having properties as mentioned in the service descriptor
     * @throws FioranoException FioranoException
     */
    public static Service readService(Reader reader) throws FioranoException{
        if(!reader.markSupported()){
            reader = new MarkableReader(reader){
                boolean closeCalled = false;
                public void close() throws IOException{
                    if(closeCalled)
                        super.close();
                    else
                        closeCalled = true;
                }
            };
        }else{
            reader = new FilterReader(reader){
                boolean closeCalled = false;
                public void close() throws IOException{
                    if(closeCalled)
                        super.close();
                    else
                        closeCalled = true;
                }
            };
        }
        try{
            reader.mark(Integer.MAX_VALUE);
                reader.reset();
                Service service = new Service();
                service.setFieldValues(reader);
                return service;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }

    /**
     * Creates ServiceReference DMI by reading contents of specified service descriptor xml <code>file</code>
     * @param file Service descriptor xml
     * @return ServiceReference -  - An object of ServiceReference having properties as mentioned in the service descriptor
     * @throws FioranoException FioranoException
     */
    public static ServiceReference readServiceReference(File file) throws FioranoException{
        try{
            FileReader reader = new FileReader(file);
            FileInputStream stream = null;
            try{

                    ServiceReference serviceRef = new ServiceReference();
                    stream = new FileInputStream(file);
                    serviceRef.setFieldValues(stream);
                    return serviceRef;

            }finally{
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    // Ignore
                }
                try {
                    if(stream != null)
                        stream.close();
                } catch (IOException e) {
                    // Ignore
                }    
            }
        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }

    /**
     * Writes a service to the specified folder
     * @param service Service DMI
     * @param serviceFolder Folder to write
     * @throws FioranoException FioranoException
     */
    public static void writeService(Service service, File serviceFolder) throws FioranoException{
        if(!serviceFolder.exists())
            serviceFolder.mkdirs();

        try{
            HashMap<String, String> schemas = new HashMap<String, String>();
            HashMap<String, String> schemaRefsTable = new HashMap<String, String>();

            List<Port> ports = new ArrayList<Port>();
            if(service.getExecution()!=null){
                ports.addAll(service.getExecution().getInputPorts());
                ports.addAll(service.getExecution().getOutputPorts());
            }

            int count = 1;
            for(Port port : ports){
                Schema schema = port.getSchema();
                if(schema == null)
                    continue;

                String schemaContent = schema.getContent();
                if(schemaContent == null)
                    continue;

                if(schemas.containsKey(schemaContent)){
                    String schemaFileName = schemas.get(schemaContent);
                    port.getSchema().setFile(schemaFileName);
                } else{
                    String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
                    if (schemaConfigName == null) {
                        String schemaFileName = port.getName() + Application.EXTENSION_SCHEMA;
                        schemas.put(schemaContent, schemaFileName);
                        port.getSchema().setFile(schemaFileName);

                        String schemaFilePath = getSchemaFileName(serviceFolder.getPath(), schemaFileName);

                        File schemaFile = new File(schemaFilePath);

                        if (!schemaFile.exists()) {
                            schemaFile.getParentFile().mkdirs();
                        }
                        //write the schemaContent
                        FileOutputStream schemaFileStream = new FileOutputStream(schemaFile);
                        try {
                            schemaFileStream.write(schemaContent.getBytes());
                        } finally {
                            try {
                                schemaFileStream.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    }
                }

                Map schemaRefs = port.getSchema().getSchemaRefs();
                if (schemaRefs != null && !schemaRefs.isEmpty()) {
                    for (Object uriObject : schemaRefs.keySet()) {
                        String uri = (String) uriObject;
                        String schemaRefContent = (String) schemaRefs.get(uri);
                        if (schemaRefsTable.containsKey(schemaRefContent)) {
                            String schemaRefFileName = schemaRefsTable.get(schemaRefContent);
                            port.getSchema().addSchemaRefFile(uri, schemaRefFileName);
                        } else {
                            Map schemaRefConfigMap = port.getSchema().getSchemaRefConfigs();
                            if (schemaRefConfigMap.size() == 0) {
                                String schemaRefFileName = port.getName() + "." + count + Application.EXTENSION_SCHEMA;
                                count++;
                                schemaRefsTable.put(schemaRefContent, schemaRefFileName);
                                port.getSchema().addSchemaRefFile(uri, schemaRefFileName);

                                String schemaRefFilePath = getSchemaFileName(serviceFolder.getPath(), schemaRefFileName);
                                File schemaRefFile = new File(schemaRefFilePath);
                                if (!schemaRefFile.exists())
                                    schemaRefFile.getParentFile().mkdirs();

                                //write the schemaContent
                                FileOutputStream schemaRefFileStream = new FileOutputStream(schemaRefFile);
                                try {
                                    schemaRefFileStream.write(schemaRefContent.getBytes());
                                } finally {
                                    try {
                                        schemaRefFileStream.close();
                                    } catch (IOException e) {
                                        //Ignore
                                    }
                                }
                            }
                        }
                    }
                }
            }

            service.toXMLString(serviceFolder + File.separator + SERVICE_DESCRIPTOR_XML, false);
        } catch(IOException e){
            throw new FioranoException(e);
        }
    }

    private static String getSchemaFileName(String serviceFolderName, String schemaFileName){
        return serviceFolderName + File.separator + schemaFileName;
    }
}
