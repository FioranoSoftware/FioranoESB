/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.configuration.data.DestinationType;
import com.fiorano.openesb.application.configuration.data.ObjectCategory;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.Schema;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.MarkableInputStream;
import com.fiorano.openesb.utils.MarkableReader;
import com.fiorano.openesb.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ApplicationParser{
    /**
     * event process xml name
     */
    public static final String EVENT_PROCESS_XML = "EventProcess.xml";
    /**
     * configuration folder name
     */
    public static final String CONFIG_FOLDER = "config";
    /**
     * configuration folder name
     */
    public static final String TRANSFORMATION_FOLDER = "transformations";
    /**
     * The sub-directory of transformations directory where route transformations are stored
     */
    public static final String ROUTES_FOLDER = "routes";
    /**
     * The sub-directory of transformations directory where port application context transformations are stored
     */
    public static final String PORTS_FOLDER = "ports";
    /**
     * schemas folder name
     */
    public static final String SCHEMAS_FOLDER="schemas";
    /**
     * help folder name
     */
    public static final String HELP_FOLDER="help";
    /**
     * help file name
     */
    public static final String HELP_FILE_NAME="help.html";
    /**
     * element documentation in event process xml
     */
    public static final String ELEM_DOCUMENTATION = "documentation";
    /**
     * xml extension
     */
    private static final String XML_EXTENSION = ".xml";
    /**
     * xml extension
     */
    private static final String FMP_EXTENSION = ".fmp";

    private static final String FOLDER_CONTENTS = "folder-contents";
    private static final String FOLDER = "folder";
    private static final String FILE = "file";
    private static final String NAME = "name";

    /**
     * Default version for Schema
     */
    public static final String DEFAULT_SCHEMA_VERSION = "1.0";
    /**
     * Default version for Named Configuration Schema
     */
    public static final String NAMED_CONFIGURATION_SCHEMA_VERSION = "2.0";

    /**
     * Path of the configuration repository where Enterprise Server stores its named configurations
     */
    private static String configurationRepoPath;

    /**
     * Sets configuration repository path
     * @param configurationRepoPath String
     */
    public static void setConfigurationRepoPath(String configurationRepoPath) {
        ApplicationParser.configurationRepoPath = configurationRepoPath;
    }

    /**
     * Gets configuration repository path
     * @return configuration repository path String
     */
    public static String getConfigurationRepoPath() {
        return ApplicationParser.configurationRepoPath;
    }

    /**
     * application repository folder name
     */
    public static final String APPLICATION_DIR_DEF = "applications";

    /**
     * Path of the application repository where Enterprise Server stores its applications
     */
    private static String userApplicationRepoPath;
    private static String systemApplicationRepoPath;

    /**
     * Sets Application repository paths
     * @param userApplicationRepoPath User defined repository path
     * @param systemApplicationRepoPath internal Application repository path
     */
    public static void setApplicationRepoPath(String userApplicationRepoPath, String systemApplicationRepoPath) {
        ApplicationParser.userApplicationRepoPath = userApplicationRepoPath;
        ApplicationParser.systemApplicationRepoPath = systemApplicationRepoPath;
    }

    /**
     * Creates Application DMI by reading contents of specified event process xml <code>file</code>
     * @param file event process xml file
     * @return Application
     * @throws FioranoException FioranoException
     */
    public static Application readApplication(File file) throws FioranoException{
        try{
            FileReader reader = new FileReader(file);
            FileInputStream stream = null;
            try {

                    Application app = new Application();
                    stream = new FileInputStream(file);
                    app.setFieldValues(stream);
                    return app;

            } finally{
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
            throw new FioranoException( ex);
        }
    }

    /**
     * Creates Application DMI by reading contents of specified event process folder and its label
     * @param applicationFolderName File representing the directory containing the event process.
     * @param validate boolean indicating whether to validate the application dmi object created or not.
     * @return
     * @throws FioranoException
     */
    public static Application readApplication(File applicationFolderName, boolean validate) throws FioranoException {
        return readApplication(applicationFolderName, null, validate);
    }

    /**
     * Creates Application DMI by reading contents of specified event process folder and its label
     * @param applicationFolderName File representing the directory containing the event process.
     * @param validate boolean indicating whether to validate the application dmi object created or not.
     * @param checkForNamedConfigurations If set to true, a check is made for existence of all named configurations. If some named configuration
     * doe not exist, then an exception will be thrown to indicate the error situation.
     * @return
     * @throws FioranoException
     */
    public static Application readApplication(File applicationFolderName, boolean validate, boolean checkForNamedConfigurations) throws FioranoException {
        return readApplication(applicationFolderName, null, validate, checkForNamedConfigurations);
    }

    /**
     * Creates Application DMI by reading contents of specified event process folder and its label
     * @param applicationFolderName File representing the directory containing the event process.
     * @param label Label of Event Process
     * @param validate boolean indicating whether to validate the application dmi object created or not.
     * @throws FioranoException FioranoException
     * @return Application
     */
    public static Application readApplication(File applicationFolderName, String label, boolean validate) throws FioranoException {
        return readApplication(applicationFolderName, label, validate, false);
    }

    /**
     * Creates Application DMI by reading contents of specified event process folder and its label
     * @param applicationFolderName File representing the directory containing the event process.
     * @param label Label of Event Process
     * @param validate boolean indicating whether to validate the application dmi object created or not.
     * @param checkForNamedConfigurations If set to true, a check is made for existence of all named configurations. If some named configuration
     * doe not exist, then an exception will be thrown to indicate the error situation.
     * @throws FioranoException FioranoException
     * @return Application
     */
    public static Application readApplication(File applicationFolderName, String label, boolean validate, boolean checkForNamedConfigurations) throws FioranoException {
        if (applicationFolderName == null)
            throw new IllegalArgumentException();
        if (!applicationFolderName.isDirectory())
            throw new IllegalArgumentException();

        if (label != null) {
            try {
                Application.Label.valueOf(label);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException();
            }
        }

        FileInputStream stream = null;
        try {
            Application application = new Application();
            File epFile = new File(applicationFolderName.getPath(), EVENT_PROCESS_XML);
            if(! epFile.isFile())
                throw new FioranoException("NO_APP_FOUND");

            stream = new FileInputStream(epFile);
            application.setFieldValues(stream, false);

            //load the maneageable properties
            final String applicationLabel = application.getLabel();
            if(label != null)
                application.readManageableProperties(applicationFolderName, Application.Label.valueOf(label));
            else
                application.readManageableProperties(applicationFolderName, Application.Label.valueOf(applicationLabel));

            ApplicationContext context = application.getApplicationContext();
            if (context != null) {
                String appContextConfigName = context.getConfigName();
                if(appContextConfigName != null && configurationRepoPath != null){
                    File appContextFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel + File.separator + appContextConfigName);
                    if (appContextFile.exists()) {
                        FileInputStream appContextFileStream = null;
                        try {
                            appContextFileStream = new FileInputStream(appContextFile);
                            context.setContent(DmiObject.getContents(appContextFileStream));
                        } finally {
                            try {
                                if(appContextFileStream != null)
                                    appContextFileStream.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("APP_CONTEXT_CONFIGURATION_NOT_FOUND");
                }else{
                    String appContextFileName = context.getFile();
                    if (appContextFileName != null) {
                        File appContextSchemaFile = new File(getSchemaFileName(applicationFolderName.getPath(), appContextFileName));

                        if (appContextSchemaFile.exists()) {
                            FileInputStream schemaFileStream = new FileInputStream(appContextSchemaFile);
                            try {
                                context.setContent(DmiObject.getContents(schemaFileStream));
                            } finally {
                                schemaFileStream.close();
                            }
                        }
                    }
                }
            }

            List<ServiceInstance> services = application.getServiceInstances();
            for (ServiceInstance service : services) {
                if (checkForNamedConfigurations) {
                    ArrayList<NamedConfigurationProperty> namedConfigurations = service.getNamedConfigurations();
                    checkForPresenceOfNamedConfigurations(namedConfigurations, service, applicationLabel);
                }

                //Load Configurations
                // String configurationFileName = service.getConfigFile(); // TODO :: Replace below line with this after changes
                String configurationFileName = service.getConfigFile() != null ? service.getConfigFile() : service.getName() + XML_EXTENSION;
                if(configurationFileName != null){
                    File configurationFile = new File(getConfigurationFileName(applicationFolderName.getPath(), configurationFileName));

                    if (configurationFile.exists()) {
                        FileInputStream configsFileStream = new FileInputStream(configurationFile);
                        try {
                            service.setConfiguration(DmiObject.getContents(configsFileStream));
                        } finally {
                            configsFileStream.close();
                        }
                    }
                }

                String runtimeArgumentsConfigName = service.getRuntimeArgumentsConfigName();
                if (runtimeArgumentsConfigName != null && configurationRepoPath != null) {
                    File runtimeArgumentsConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                            .separator + ConfigurationRepoConstants.RUNTIME_ARGS + File.separator + applicationLabel + File.separator +
                            runtimeArgumentsConfigName);

                    if (runtimeArgumentsConfigFile.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(runtimeArgumentsConfigFile);
                            cursor = new FioranoStaxParser(fis);
                            service.populateRuntimeArgumentsConfigurations(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("RUNTIME_ARGS_CONFIGURATION_NOT_FOUND");
                }

                String connectionFactoryConfigName = service.getConnectionFactoryConfigName();
                if (connectionFactoryConfigName != null && configurationRepoPath != null) {
                    File connectionFactoryConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                            .separator + ConfigurationRepoConstants.CONNECTION_FACTORY + File.separator + applicationLabel + File.separator +
                            connectionFactoryConfigName);

                    if (connectionFactoryConfigFile.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(connectionFactoryConfigFile);
                            cursor = new FioranoStaxParser(fis);
                            service.populateConnectionFactoryConfigurations(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("CONNECTION_FACTORY_CONFIGURATION_NOT_FOUND");
                }

                //Load Port Schemas
                List<PortInstance> ports = new ArrayList<PortInstance>();
                ports.addAll(service.getInputPortInstances());
                ports.addAll(service.getOutputPortInstances());
                for (PortInstance port : ports) {
                    String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
                    if(schemaConfigName != null && configurationRepoPath != null){
                        File schemaConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                                .separator + ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel + File.separator + schemaConfigName);

                        if (schemaConfigFile.exists()) {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(schemaConfigFile);
                                port.getSchema().setContent(DmiObject.getContents(fis));
                            } finally {
                                try {
                                    if(fis != null)
                                        fis.close();
                                } catch (IOException e) {
                                    //Ignore
                                }
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("SCHEMA_CONFIGURATION_NOT_FOUND");

                        Map schemaRefMap =  port.getSchema().getSchemaRefConfigs();
                        for (Object obj : schemaRefMap.entrySet()) {
                            Map.Entry entry = (Map.Entry) obj;
                            String uri = (String) entry.getKey();
                            String schemaRefConfigName = (String) entry.getValue();

                            File schemaRefFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                                    .separator + ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel + File.separator + schemaRefConfigName);
                            if (schemaRefFile.exists()) {
                                FileInputStream schemaRefFileStream = null;
                                try {
                                    schemaRefFileStream = new FileInputStream(schemaRefFile);
                                    port.getSchema().addSchemaRef(uri, DmiObject.getContents(schemaRefFileStream));
                                } finally {
                                    try {
                                        if(schemaRefFileStream != null)
                                            schemaRefFileStream.close();
                                    } catch (IOException e) {
                                        //Ignore
                                    }
                                }
                            } else if (checkForNamedConfigurations)
                                throw new FioranoException("SCHEMA_REF_CONFIGURATION_NOT_FOUND");
                        }
                    }else{
                        String schemaFileName = (port.getSchema() == null) ? null : port.getSchema().getFile();
                        if (schemaFileName != null){
                            File schemaFile = new File(getSchemaFileName(applicationFolderName.getPath(), schemaFileName));

                            if (schemaFile.exists()) {
                                FileInputStream schemaFileStream = new FileInputStream(schemaFile);
                                try {
                                    port.getSchema().setContent(DmiObject.getContents(schemaFileStream));
                                } finally {
                                    schemaFileStream.close();
                                }
                            }

                            Map schemaRefMap =  port.getSchema().getSchemaRefFiles();
                            for (Object obj : schemaRefMap.entrySet()) {
                                Map.Entry entry = (Map.Entry) obj;
                                String uri = (String) entry.getKey();
                                String schemaRefFileName = (String) entry.getValue();

                                File schemaRefFile = new File(getSchemaFileName(applicationFolderName.getPath(), schemaRefFileName));
                                if (schemaRefFile.exists()) {
                                    FileInputStream schemaRefFileStream = new FileInputStream(schemaRefFile);
                                    try {
                                        port.getSchema().addSchemaRef(uri, DmiObject.getContents(schemaRefFileStream));
                                    } finally {
                                        schemaRefFileStream.close();
                                    }
                                }
                            }
                        }
                    }

                    String workflowConfigName = port.getWorkflowConfigName();
                    if(workflowConfigName != null && configurationRepoPath != null){
                        File workflowConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                ConfigurationRepoConstants.WORKFLOWS + File.separator + applicationLabel + File.separator + workflowConfigName);

                        if (workflowConfigFile.exists()) {
                            FileInputStream fis = null;
                            FioranoStaxParser cursor = null;
                            try {
                                fis = new FileInputStream(workflowConfigFile);
                                cursor = new FioranoStaxParser(fis);
                                port.populateWorkflowConfiguration(cursor);
                            } finally {
                                try {
                                    if(cursor != null)
                                        cursor.disposeParser();
                                } catch (XMLStreamException ignore) {
                                    //Ignore
                                }

                                try {
                                    if(fis != null)
                                        fis.close();
                                } catch (IOException e) {
                                    //Ignore
                                }
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("WORKFLOW_CONFIGURATION_NOT_FOUND");
                    }

                    String messageFilterConfigName = port.getMessageFilterConfigName();
                    if(messageFilterConfigName != null && configurationRepoPath != null){
                        File messageFilterConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                ConfigurationRepoConstants.MESSAGEFILTERS + File.separator + applicationLabel + File.separator + messageFilterConfigName);

                        if (messageFilterConfigFile.exists()) {
                            FileInputStream fis = null;
                            FioranoStaxParser cursor = null;
                            try {
                                fis = new FileInputStream(messageFilterConfigFile);
                                cursor = new FioranoStaxParser(fis);
                                port.populateMessageFilterConfiguration(cursor);
                            } finally {
                                try {
                                    if(cursor != null)
                                        cursor.disposeParser();
                                } catch (XMLStreamException ignore) {
                                    //Ignore
                                }

                                try {
                                    if(fis != null)
                                        fis.close();
                                } catch (IOException e) {
                                    //Ignore
                                }
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("MESSAGE_FILTER_CONFIGURATION_NOT_FOUND");
                    }


                    String destinationConfigName = port.getDestinationConfigName();
                    if(destinationConfigName != null && configurationRepoPath != null){
                        File destinationConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                ConfigurationRepoConstants.DESTINATION + File.separator + applicationLabel + File.separator + destinationConfigName);

                        if (destinationConfigFile.exists()) {
                            FileInputStream fis = null;
                            FioranoStaxParser cursor = null;
                            try {
                                fis = new FileInputStream(destinationConfigFile);
                                cursor = new FioranoStaxParser(fis);
                                port.populateDestinationConfiguration(cursor);
                            } finally {
                                try {
                                    if(cursor != null)
                                        cursor.disposeParser();
                                } catch (XMLStreamException ignore) {
                                    //Ignore
                                }

                                try {
                                    if(fis != null)
                                        fis.close();
                                } catch (IOException e) {
                                    //Ignore
                                }
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("DESTINATION_CONFIGURATION_NOT_FOUND");
                    }

                    if(port instanceof InputPortInstance){
                        InputPortInstance inputPortInstance = (InputPortInstance) port;
                        String subscriberConfigName = inputPortInstance.getSubscriberConfigName();
                        if(subscriberConfigName != null && configurationRepoPath != null){
                            File subscriberConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                    ConfigurationRepoConstants.PORTS + File.separator + ConfigurationRepoConstants.INPUT_PORT + File.separator + applicationLabel
                                    + File.separator + subscriberConfigName);

                            if (subscriberConfigFile.exists()) {
                                String metaDataFilePath = null;
                                boolean portTypeCheck = false;

                                    metaDataFilePath = subscriberConfigFile.getParent() + File.separator + ConfigurationRepoConstants.METADATA_XML;
                                    Hashtable configurationParams = getConfigurationParams(metaDataFilePath, subscriberConfigName);
                                    if (!(((String ) configurationParams.get(ConfigurationRepoConstants.ATTR_DESTINATION_TYPE)).
                                            equalsIgnoreCase(port.getDestinationType()==0 ? DestinationType.QUEUE.toString() : DestinationType.TOPIC.toString()))){
                                        portTypeCheck = true;
                                    }

                                if (portTypeCheck){
                                    throw new FioranoException("SUBSCRIBER_CONFIGURATION_NOT_FOUND");
                                }
                                FileInputStream fis = null;
                                FioranoStaxParser cursor = null;
                                try {
                                    fis = new FileInputStream(subscriberConfigFile);
                                    cursor = new FioranoStaxParser(fis);
                                    inputPortInstance.populateSubscriberConfiguration(cursor);
                                } finally {
                                    try {
                                        if(cursor != null)
                                            cursor.disposeParser();
                                    } catch (XMLStreamException ignore) {
                                        //Ignore
                                    }

                                    try {
                                        if(fis != null)
                                            fis.close();
                                    } catch (IOException e) {
                                        //Ignore
                                    }
                                }
                            } else if (checkForNamedConfigurations)
                                throw new FioranoException("SUBSCRIBER_CONFIGURATION_NOT_FOUND");
                        }
                    }

                    if(port instanceof OutputPortInstance){
                        OutputPortInstance outputPortInstance = (OutputPortInstance) port;
                        Transformation transformation = outputPortInstance.getApplicationContextTransformation();

                        String transformationConfigName = null;
                        if(transformation != null)
                            transformationConfigName = transformation.getTransformationConfigName();

                        String publisherConfigName = outputPortInstance.getPublisherConfigName();
                        if(publisherConfigName != null && configurationRepoPath != null){
                            File publisherConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                    ConfigurationRepoConstants.PORTS + File.separator + ConfigurationRepoConstants.OUTPUT_PORT + File.separator + applicationLabel
                                    + File.separator + publisherConfigName);

                            if (publisherConfigFile.exists()) {
                                String metaDataFilePath = null;
                                boolean portTypeCheck = false;

                                    metaDataFilePath = publisherConfigFile.getParent() + File.separator + ConfigurationRepoConstants.METADATA_XML;
                                    Hashtable configurationParams = getConfigurationParams(metaDataFilePath, publisherConfigName);
                                    if (!(((String ) configurationParams.get(ConfigurationRepoConstants.ATTR_DESTINATION_TYPE)).
                                            equalsIgnoreCase(port.getDestinationType()==0 ? DestinationType.QUEUE.toString() : DestinationType.TOPIC.toString()))){
                                        portTypeCheck = true;
                                    }

                                if (portTypeCheck){
                                    throw new FioranoException("SUBSCRIBER_CONFIGURATION_NOT_FOUND");
                                }
                                FileInputStream fis = null;
                                FioranoStaxParser cursor = null;
                                try {
                                    fis = new FileInputStream(publisherConfigFile);
                                    cursor = new FioranoStaxParser(fis);
                                    outputPortInstance.populatePublisherConfiguration(cursor);
                                } finally {
                                    try {
                                        if(cursor != null)
                                            cursor.disposeParser();
                                    } catch (XMLStreamException ignore) {
                                        //Ignore
                                    }

                                    try {
                                        if(fis != null)
                                            fis.close();
                                    } catch (IOException e) {
                                        //Ignore
                                    }
                                }
                            } else if (checkForNamedConfigurations)
                                throw new FioranoException("PUBLISHER_CONFIGURATION_NOT_FOUND");
                        }

                        if(transformationConfigName != null && configurationRepoPath != null){
                            String transformationDirPath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                                    .separator + ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel  + File.separator + transformationConfigName;

                            if (new File(transformationDirPath).exists()) {
                                String metaDataFilePath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                                        .separator + ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel  + File.separator + ConfigurationRepoConstants.METADATA_XML;
                                TransformationConfig transformationConfig = getTransformationParams(metaDataFilePath, transformationConfigName);

                                if (transformationConfig != null) {
                                    String scriptFileName = transformationConfig.getScriptFileName();
                                    String projectFileName = transformationConfig.getProjectFileName();

                                    String factoryName = transformationConfig.getFactoryName();
                                    transformation.setFactory(factoryName);

                                    if(scriptFileName != null){
                                        File scriptFile = new File(transformationDirPath, scriptFileName);

                                        if(scriptFile.exists()) {
                                            FileInputStream scriptFileStream = new FileInputStream(scriptFile);
                                            try {
                                                transformation.setScript(DmiObject.getContents(scriptFileStream));
                                            } finally {
                                                scriptFileStream.close();
                                            }
                                        }else if (checkForNamedConfigurations)
                                            throw new FioranoException("SCRIPT_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                                    }

                                    if(projectFileName != null){
                                        File projectFile = new File(transformationDirPath, projectFileName);

                                        if(projectFile.exists()) {
                                            if(projectFile.isFile()){
                                                FileInputStream projectFileStream = new FileInputStream(projectFile);
                                                try {
                                                    transformation.setProject(DmiObject.getContents(projectFileStream));
                                                } finally {
                                                    projectFileStream.close();
                                                }
                                            }else{
                                                transformation.setProject(toXML(projectFile));
                                            }
                                        }else if (checkForNamedConfigurations)
                                            throw new FioranoException("PROJECT_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                                    }
                                } else if (checkForNamedConfigurations)
                                    throw new FioranoException("METADATA_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                            } else if (checkForNamedConfigurations)
                                throw new FioranoException("PORT_TRANSFORMATION_CONFIGURATION_NOT_FOUND");
                        }else{
                            if(transformation != null){
                                String scriptFileName = transformation.getScriptFile() != null ? transformation.getScriptFile() : null;
                                if(scriptFileName != null){
                                    File scriptFile = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), scriptFileName));

                                    if(scriptFile.exists()) {
                                        FileInputStream scriptFileStream = new FileInputStream(scriptFile);
                                        try {
                                            transformation.setScript(DmiObject.getContents(scriptFileStream));
                                        } finally {
                                            scriptFileStream.close();
                                        }
                                    }
                                }

                                String projectFileName = transformation.getProjectFile() != null ? transformation.getProjectFile() : null;
                                if(projectFileName != null){
                                    File projectFile = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), projectFileName));

                                    if(projectFile.exists()) {
                                        if(projectFile.isFile()){
                                            FileInputStream projectFileStream = new FileInputStream(projectFile);
                                            try {
                                                transformation.setProject(DmiObject.getContents(projectFileStream));
                                            } finally {
                                                projectFileStream.close();
                                            }
                                        }else{
                                            transformation.setProject(toXML(projectFile));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<Route> routes = application.getRoutes();
            for(Route route : routes){
                String messagingConfigName = route.getMessagingConfigName();
                String selectorConfigName = route.getSelectorConfigName();
                String transformationConfigName = null;
                MessageTransformation messageTransformation = route.getMessageTransformation();

                if(messageTransformation != null)
                    transformationConfigName = messageTransformation.getTransformationConfigName();

                if(messagingConfigName != null && configurationRepoPath != null){
                    File routeConfigPath = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.ROUTES + File.separator + applicationLabel  + File.separator + messagingConfigName);

                    if (routeConfigPath.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(routeConfigPath);
                            cursor = new FioranoStaxParser(fis);
                            route.populateMessagingConfiguration(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("ROUTE_MESSAGING_TRANSFORMATION_NOT_FOUND");
                }

                if(selectorConfigName != null && configurationRepoPath != null){
                    File selectorConfigPath = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File
                            .separator + ConfigurationRepoConstants.SELECTORS + File.separator + applicationLabel  + File.separator + selectorConfigName);

                    if (selectorConfigPath.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(selectorConfigPath);
                            cursor = new FioranoStaxParser(fis);
                            route.populateSelectorsConfiguration(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("ROUTE_SELECTOR_TRANSFORMATION_NOT_FOUND");
                }

                if(transformationConfigName != null && configurationRepoPath != null){
                    String transformationDirPath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel +  File.separator + transformationConfigName;

                    if (new File(transformationDirPath).exists()) {
                        String metaDataFilePath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                                ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel  + File.separator + ConfigurationRepoConstants.METADATA_XML;
                        TransformationConfig transformationConfig = getTransformationParams(metaDataFilePath, transformationConfigName);

                        if (transformationConfig != null) {
                            String scriptFileName = transformationConfig.getScriptFileName();
                            String jmsScriptFileName = transformationConfig.getJmsScriptFileName();
                            String projectFileName = transformationConfig.getProjectFileName();

                            String factoryName = transformationConfig.getFactoryName();
                            messageTransformation.setFactory(factoryName);

                            if(scriptFileName != null){
                                File scriptFile = new File(transformationDirPath, scriptFileName);

                                if(scriptFile.exists()) {
                                    FileInputStream scriptFileStream = new FileInputStream(scriptFile);
                                    try {
                                        messageTransformation.setScript(DmiObject.getContents(scriptFileStream));
                                    } finally {
                                        scriptFileStream.close();
                                    }
                                }else if (checkForNamedConfigurations)
                                    throw new FioranoException("SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                            }

                            if(jmsScriptFileName != null){
                                File jmsScriptFile = new File(transformationDirPath, jmsScriptFileName);

                                if(jmsScriptFile.exists()) {
                                    FileInputStream jmsScriptFileStream = new FileInputStream(jmsScriptFile);
                                    try {
                                        messageTransformation.setJMSScript(DmiObject.getContents(jmsScriptFileStream));
                                    } finally {
                                        jmsScriptFileStream.close();
                                    }
                                }else if (checkForNamedConfigurations)
                                    throw new FioranoException("JMS_SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                            }

                            if(projectFileName != null){
                                File projectFile = new File(transformationDirPath, projectFileName);

                                if(projectFile.exists()) {
                                    if(projectFile.isFile()){
                                        FileInputStream projectFileStream = new FileInputStream(projectFile);
                                        try {
                                            messageTransformation.setProject(DmiObject.getContents(projectFileStream));
                                        } finally {
                                            projectFileStream.close();
                                        }
                                    }else{
                                        messageTransformation.setProject(toXML(projectFile));
                                    }
                                }else if (checkForNamedConfigurations)
                                    throw new FioranoException("PROJECT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("METADATA_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("ROUTE_TRANSFORMATION_CONFIGURATION_NOT_FOUND");
                }else{
                    if(messageTransformation != null){
                        String scriptFileName = messageTransformation.getScriptFile() != null ? messageTransformation.getScriptFile() : null;
                        if(scriptFileName != null){
                            File scriptFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), scriptFileName));

                            if(scriptFile.exists()) {
                                FileInputStream scriptFileStream = new FileInputStream(scriptFile);
                                try {
                                    messageTransformation.setScript(DmiObject.getContents(scriptFileStream));
                                } finally {
                                    scriptFileStream.close();
                                }
                            }
                        }

                        String jmsScriptFileName = messageTransformation.getJMSScriptFile() != null ? messageTransformation.getJMSScriptFile() : null;
                        if(jmsScriptFileName != null){
                            File jmsScriptFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), jmsScriptFileName));

                            if(jmsScriptFile.exists()) {
                                FileInputStream jmsScriptFileStream = new FileInputStream(jmsScriptFile);
                                try {
                                    messageTransformation.setJMSScript(DmiObject.getContents(jmsScriptFileStream));
                                } finally {
                                    jmsScriptFileStream.close();
                                }
                            }
                        }

                        String projectFileName = messageTransformation.getProjectFile() != null ? messageTransformation.getProjectFile() : null;
                        if(projectFileName != null){
                            File projectFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), projectFileName));

                            if(projectFile.exists()) {
                                if(projectFile.isFile()){
                                    FileInputStream projectFileStream = new FileInputStream(projectFile);
                                    try {
                                        messageTransformation.setProject(DmiObject.getContents(projectFileStream));
                                    } finally {
                                        projectFileStream.close();
                                    }
                                }else{
                                    messageTransformation.setProject(toXML(projectFile));
                                }
                            }
                        }
                    }
                }
            }

            File appHelpFile = new File(getHelpFileName(applicationFolderName.getPath()));

            if (appHelpFile.exists()) {
                FileInputStream helpFileStream = new FileInputStream(appHelpFile);
                try {
                    String documentation = DmiObject.getContents(helpFileStream);

                    //Remove documentation element from layout (if already present, then add new content)
                    String layout = application.getLayout();
                    layout = removeDocumentation(layout);

                    layout = addDocumentation(layout, documentation);
                    application.setLayout(layout);
                } finally {
                    helpFileStream.close();
                }
            }

            if (validate)
                application.validate();

            return application;
        } catch (IOException ioe) {
            throw new FioranoException( ioe);
        } catch (XMLStreamException xmle) {
            throw new FioranoException( xmle);
        } finally {
            try {
                if(stream != null)
                    stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private static void checkForPresenceOfNamedConfigurations(ArrayList<NamedConfigurationProperty> namedConfigurations,
                                                              ServiceInstance serviceInstance, String applicationLabel) throws FioranoException {
        if (namedConfigurations != null && namedConfigurations.size() > 0) {
            for(NamedConfigurationProperty configurationProperty : namedConfigurations){
                String configurationName = configurationProperty.getConfigurationName();
                String configurationType = configurationProperty.getConfigurationType();
                checkConfiguration(configurationName, configurationType, serviceInstance, applicationLabel);
            }
        }
    }

    private static void checkConfiguration(String configurationName, String configurationType, ServiceInstance servInstance, String applicationLabel) throws FioranoException {
        if (configurationType != null) {
            ObjectCategory objectCategory = ObjectCategory.getObjectCategory(configurationType);
            switch (objectCategory) {
                case SERVICE_CONFIGURATION:
                    File serviceConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator + ConfigurationRepoConstants.COMPONENTS +
                            File.separator + applicationLabel + File.separator+ servInstance.getGUID() + File.separator + ensureDecimalPoint(servInstance.getVersion()) + File.separator + configurationName);

                    checkConfigurationFile(serviceConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case MISCELLANEOUS:
                    File miscConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.MISC + File.separator + applicationLabel  + File.separator + configurationName);

                    checkConfigurationFile(miscConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case RESOURCE_CONFIGURATION:
                    File resourceConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel  + File.separator + configurationName);

                    checkConfigurationFile(resourceConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case ROUTE:
                    File routeConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.ROUTES + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(routeConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case SELECTOR:
                    File selectorConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.SELECTORS + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(selectorConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case TRANSFORMATION:
                    File transformationConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(transformationConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case WORKFLOW:
                    File workflowConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.WORKFLOWS + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(workflowConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case MESSAGEFILTERS:
                    File messageFilterConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.MESSAGEFILTERS + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(messageFilterConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case RUNTIME_ARG_CONFIGURATION:
                    File runtimeConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.RUNTIME_ARGS + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(runtimeConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case CONNECTION_FACTORY_CONFIGURATION:
                    File connectionFactoryConfigurationFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.CONNECTION_FACTORY + File.separator + applicationLabel + File.separator + configurationName);

                    checkConfigurationFile(connectionFactoryConfigurationFile, configurationName, configurationType, servInstance);
                    break;
                case PORT_CONFIGURATION:
                    throw new FioranoException("PORT_CONFIGURATION_TYPE_NOT_SUPPORTED");
                case DESTINATION:
                    throw new FioranoException("DESTINATION_CONFIGURATION_TYPE_NOT_SUPPORTED");
                default:
                    throw new FioranoException("CONFIGURATION_TYPE_INVALID");
            }
        } else
            throw new FioranoException("CONFIGURATION_TYPE_INVALID");
    }

    private static void checkConfigurationFile(File file, String configurationName, String configurationType, ServiceInstance servInstance) throws FioranoException {
        if (!file.exists())
            throw new FioranoException("CONFIGURATION_NOT_PRESENT");
    }

    private static String ensureDecimalPoint(float version) {
        String originalValue = String.valueOf(version);
        if (!originalValue.contains("."))
            originalValue = originalValue + ".0";

        return originalValue;
    }

    /**
     * Checks for existence of all Named Configuration files for the specified <code>service</code>
     * @param service Service Instance
     * @param appGUID Application GUID of the service
     * @param applicationLabel Application environment
     * @param checkForNamedConfigurations Option to check for the Named Configurations
     * @throws IOException
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public static void resolveAndUpdateNamedConfigurations(ServiceInstance service, String appGUID, String applicationLabel, boolean checkForNamedConfigurations) throws IOException, XMLStreamException, FioranoException {
        String runtimeArgumentsConfigName = service.getRuntimeArgumentsConfigName();
        if (runtimeArgumentsConfigName != null && configurationRepoPath != null) {
            File runtimeArgumentsConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                    ConfigurationRepoConstants.RUNTIME_ARGS + File.separator + applicationLabel + File.separator + runtimeArgumentsConfigName);

            if (runtimeArgumentsConfigFile.exists()) {
                FileInputStream fis = null;
                FioranoStaxParser cursor = null;
                try {
                    fis = new FileInputStream(runtimeArgumentsConfigFile);
                    cursor = new FioranoStaxParser(fis);
                    service.populateRuntimeArgumentsConfigurations(cursor);
                } finally {
                    try {
                        if(cursor != null)
                            cursor.disposeParser();
                    } catch (XMLStreamException ignore) {
                        //Ignore
                    }

                    try {
                        if(fis != null)
                            fis.close();
                    } catch (IOException e) {
                        //Ignore
                    }
                }
            } else if (checkForNamedConfigurations)
                throw new FioranoException("RUNTIME_ARGS_CONFIGURATION_NOT_FOUND");
        }

        String connectionFactoryConfigName = service.getConnectionFactoryConfigName();
        if (connectionFactoryConfigName != null && configurationRepoPath != null) {
            File connectionFactoryConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                    ConfigurationRepoConstants.CONNECTION_FACTORY + File.separator + applicationLabel + File.separator + connectionFactoryConfigName);

            if (connectionFactoryConfigFile.exists()) {
                FileInputStream fis = null;
                FioranoStaxParser cursor = null;
                try {
                    fis = new FileInputStream(connectionFactoryConfigFile);
                    cursor = new FioranoStaxParser(fis);
                    service.populateConnectionFactoryConfigurations(cursor);
                } finally {
                    try {
                        if(cursor != null)
                            cursor.disposeParser();
                    } catch (XMLStreamException ignore) {
                        //Ignore
                    }

                    try {
                        if(fis != null)
                            fis.close();
                    } catch (IOException e) {
                        //Ignore
                    }
                }
            } else if (checkForNamedConfigurations)
                throw new FioranoException("CONNECTION_FACTORY_CONFIGURATION_NOT_FOUND");
        }

        //Load Port Schemas
        List<PortInstance> ports = new ArrayList<PortInstance>();
        ports.addAll(service.getInputPortInstances());
        ports.addAll(service.getOutputPortInstances());
        for (PortInstance port : ports) {
            String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
            if(schemaConfigName != null && configurationRepoPath != null){
                File schemaConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                        ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel  + File.separator + schemaConfigName);

                if (schemaConfigFile.exists()) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(schemaConfigFile);
                        port.getSchema().setContent(DmiObject.getContents(fis));
                    } finally {
                        try {
                            if(fis != null)
                                fis.close();
                        } catch (IOException e) {
                            //Ignore
                        }
                    }
                } else if (checkForNamedConfigurations)
                    throw new FioranoException("SCHEMA_CONFIGURATION_NOT_FOUND");

                Map schemaRefMap =  port.getSchema().getSchemaRefConfigs();
                for (Object obj : schemaRefMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) obj;
                    String uri = (String) entry.getKey();
                    String schemaRefConfigName = (String) entry.getValue();

                    File schemaRefFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.RESOURCES + File.separator + applicationLabel  + File.separator + schemaRefConfigName);
                    if (schemaRefFile.exists()) {
                        FileInputStream schemaRefFileStream = null;
                        try {
                            schemaRefFileStream = new FileInputStream(schemaRefFile);
                            port.getSchema().addSchemaRef(uri, DmiObject.getContents(schemaRefFileStream));
                        } finally {
                            try {
                                if(schemaRefFileStream != null)
                                    schemaRefFileStream.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("SCHEMA_REF_CONFIGURATION_NOT_FOUND");
                }
            }

            String workflowConfigName = port.getWorkflowConfigName();
            if(workflowConfigName != null && configurationRepoPath != null){
                File workflowConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                        ConfigurationRepoConstants.WORKFLOWS + File.separator + applicationLabel + File.separator + workflowConfigName);

                if (workflowConfigFile.exists()) {
                    FileInputStream fis = null;
                    FioranoStaxParser cursor = null;
                    try {
                        fis = new FileInputStream(workflowConfigFile);
                        cursor = new FioranoStaxParser(fis);
                        port.populateWorkflowConfiguration(cursor);
                    } finally {
                        try {
                            if(cursor != null)
                                cursor.disposeParser();
                        } catch (XMLStreamException ignore) {
                            //Ignore
                        }

                        try {
                            if(fis != null)
                                fis.close();
                        } catch (IOException e) {
                            //Ignore
                        }
                    }
                } else if (checkForNamedConfigurations)
                    throw new FioranoException("WORKFLOW_CONFIGURATION_NOT_FOUND");
            }

            String messageFilterConfigName = port.getMessageFilterConfigName();
            if(messageFilterConfigName != null && configurationRepoPath != null){
                File messageFilterConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                        ConfigurationRepoConstants.MESSAGEFILTERS + File.separator + applicationLabel + File.separator + messageFilterConfigName);

                if (messageFilterConfigFile.exists()) {
                    FileInputStream fis = null;
                    FioranoStaxParser cursor = null;
                    try {
                        fis = new FileInputStream(messageFilterConfigFile);
                        cursor = new FioranoStaxParser(fis);
                        port.populateMessageFilterConfiguration(cursor);
                    } finally {
                        try {
                            if(cursor != null)
                                cursor.disposeParser();
                        } catch (XMLStreamException ignore) {
                            //Ignore
                        }

                        try {
                            if(fis != null)
                                fis.close();
                        } catch (IOException e) {
                            //Ignore
                        }
                    }
                } else if (checkForNamedConfigurations)
                    throw new FioranoException("MESSAGE_FILTER_CONFIGURATION_NOT_FOUND");
            }

            String destinationConfigName = port.getDestinationConfigName();
            if(destinationConfigName != null && configurationRepoPath != null){
                File destinationConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                        ConfigurationRepoConstants.DESTINATION + File.separator + applicationLabel + File.separator + destinationConfigName);

                if (destinationConfigFile.exists()) {
                    FileInputStream fis = null;
                    FioranoStaxParser cursor = null;
                    try {
                        fis = new FileInputStream(destinationConfigFile);
                        cursor = new FioranoStaxParser(fis);
                        port.populateDestinationConfiguration(cursor);
                    } finally {
                        try {
                            if(cursor != null)
                                cursor.disposeParser();
                        } catch (XMLStreamException ignore) {
                            //Ignore
                        }

                        try {
                            if(fis != null)
                                fis.close();
                        } catch (IOException e) {
                            //Ignore
                        }
                    }
                } else if (checkForNamedConfigurations)
                    throw new FioranoException("DESTINATION_CONFIGURATION_NOT_FOUND");
            }

            if(port instanceof InputPortInstance){
                InputPortInstance inputPortInstance = (InputPortInstance) port;
                String subscriberConfigName = inputPortInstance.getSubscriberConfigName();
                if(subscriberConfigName != null && configurationRepoPath != null){
                    File subscriberConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.PORTS + File.separator + applicationLabel + File.separator + ConfigurationRepoConstants.INPUT_PORT + File.separator + subscriberConfigName);

                    if (subscriberConfigFile.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(subscriberConfigFile);
                            cursor = new FioranoStaxParser(fis);
                            inputPortInstance.populateSubscriberConfiguration(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("SUBSCRIBER_CONFIGURATION_NOT_FOUND");
                }
            }

            if(port instanceof OutputPortInstance){
                OutputPortInstance outputPortInstance = (OutputPortInstance) port;
                Transformation transformation = outputPortInstance.getApplicationContextTransformation();

                String transformationConfigName = null;
                if(transformation != null)
                    transformationConfigName = transformation.getTransformationConfigName();

                String publisherConfigName = outputPortInstance.getPublisherConfigName();
                if(publisherConfigName != null && configurationRepoPath != null){
                    File publisherConfigFile = new File(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator +
                            ConfigurationRepoConstants.PORTS + File.separator + applicationLabel + File.separator + ConfigurationRepoConstants.OUTPUT_PORT + File.separator + publisherConfigName);

                    if (publisherConfigFile.exists()) {
                        FileInputStream fis = null;
                        FioranoStaxParser cursor = null;
                        try {
                            fis = new FileInputStream(publisherConfigFile);
                            cursor = new FioranoStaxParser(fis);
                            outputPortInstance.populatePublisherConfiguration(cursor);
                        } finally {
                            try {
                                if(cursor != null)
                                    cursor.disposeParser();
                            } catch (XMLStreamException ignore) {
                                //Ignore
                            }

                            try {
                                if(fis != null)
                                    fis.close();
                            } catch (IOException e) {
                                //Ignore
                            }
                        }
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("PUBLISHER_CONFIGURATION_NOT_FOUND");
                }

                if(transformationConfigName != null && configurationRepoPath != null){
                    String transformationDirPath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator + ConfigurationRepoConstants.TRANSFORMATIONS +
                            File.separator + applicationLabel + File.separator + transformationConfigName;

                    if (new File(transformationDirPath).exists()) {
                        String metaDataFilePath = configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR + File.separator + ConfigurationRepoConstants.TRANSFORMATIONS + File.separator + applicationLabel
                                + File.separator + ConfigurationRepoConstants.METADATA_XML;
                        TransformationConfig transformationConfig = getTransformationParams(metaDataFilePath, transformationConfigName);

                        if (transformationConfig != null) {
                            String scriptFileName = transformationConfig.getScriptFileName();
                            String projectFileName = transformationConfig.getProjectFileName();

                            String factoryName = transformationConfig.getFactoryName();
                            transformation.setFactory(factoryName);

                            if(scriptFileName != null){
                                File scriptFile = new File(transformationDirPath, scriptFileName);

                                if(scriptFile.exists()) {
                                    FileInputStream scriptFileStream = new FileInputStream(scriptFile);
                                    try {
                                        transformation.setScript(DmiObject.getContents(scriptFileStream));
                                    } finally {
                                        scriptFileStream.close();
                                    }
                                }else if (checkForNamedConfigurations)
                                    throw new FioranoException("SCRIPT_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                            }

                            if(projectFileName != null){
                                File projectFile = new File(transformationDirPath, projectFileName);

                                if(projectFile.exists()) {
                                    if(projectFile.isFile()){
                                        FileInputStream projectFileStream = new FileInputStream(projectFile);
                                        try {
                                            transformation.setProject(DmiObject.getContents(projectFileStream));
                                        } finally {
                                            projectFileStream.close();
                                        }
                                    }else{
                                        transformation.setProject(toXML(projectFile));
                                    }
                                }else if (checkForNamedConfigurations)
                                    throw new FioranoException("PROJECT_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                            }
                        } else if (checkForNamedConfigurations)
                            throw new FioranoException("METADATA_FOR_PORT_TRANSFORMATION_NOT_FOUND");
                    } else if (checkForNamedConfigurations)
                        throw new FioranoException("PORT_TRANSFORMATION_CONFIGURATION_NOT_FOUND");
                }
            }
        }
    }

    /**
     * Checks Named Configuration for the <code>application</code> and set the Schema Version
     * @param application Application Instance
     * @throws FileNotFoundException
     * @throws FioranoException
     */
    public static void checkAndSetNamedConfigurationIdentifiers(Application application) throws FileNotFoundException, FioranoException {
        if(application == null)
            return;

        if (userApplicationRepoPath != null && systemApplicationRepoPath != null) {
            for (Object instance : application.getRemoteServiceInstances()) {
                RemoteServiceInstance remoteServiceInstance = (RemoteServiceInstance) instance;
                String remoteAppGUID = remoteServiceInstance.getApplicationGUID();
                float remoteAppVersion = remoteServiceInstance.getApplicationVersion();

                File remoteApplicationFile = getApplicationFile(remoteAppGUID, remoteAppVersion);
                if (remoteApplicationFile != null) {
                    FileInputStream fis = new FileInputStream(remoteApplicationFile);
                    ApplicationReference applicationReference = readApplicationReference(fis);
                    if(applicationReference.getSchemaVersion().equals(NAMED_CONFIGURATION_SCHEMA_VERSION))
                        remoteServiceInstance.setApplicationSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                }
            }
        }

        ApplicationContext appContext = application.getApplicationContext();
        if(appContext != null && appContext.getConfigName() != null) {
            application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
            return;
        }

        List<ServiceInstance> serviceInstances = application.getServiceInstances();
        for(ServiceInstance serviceInstance : serviceInstances) {
            ArrayList<NamedConfigurationProperty> namedConfigurations = serviceInstance.getNamedConfigurations();
            if(namedConfigurations != null && namedConfigurations.size() > 0) {
                application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                return;
            }

            List<PortInstance> ports = new ArrayList<PortInstance>();
            ports.addAll(serviceInstance.getInputPortInstances());
            ports.addAll(serviceInstance.getOutputPortInstances());
            for (PortInstance port : ports) {
                Schema portSchema = port.getSchema();
                if(portSchema != null) {
                    String schemaConfigName = portSchema.getConfigName();
                    if(schemaConfigName != null) {
                        application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                        return;
                    }

                    Map schemaRefMap = portSchema.getSchemaRefConfigs();
                    if(schemaRefMap.size() > 0) {
                        application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                        return;
                    }
                }

                String workflowConfigName = port.getWorkflowConfigName();
                if(workflowConfigName != null) {
                    application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                    return;
                }

                String messageFilterConfigName = port.getMessageFilterConfigName();
                if(messageFilterConfigName != null) {
                    application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                    return;
                }

                String destinationConfigName = port.getDestinationConfigName();
                if(destinationConfigName != null) {
                    application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                    return;
                }

                if(port instanceof InputPortInstance){
                    InputPortInstance inputPortInstance = (InputPortInstance) port;
                    String subscriberConfigName = inputPortInstance.getSubscriberConfigName();
                    if(subscriberConfigName != null) {
                        application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                        return;
                    }
                }

                if(port instanceof OutputPortInstance){
                    OutputPortInstance outputPortInstance = (OutputPortInstance) port;
                    String publisherConfigName = outputPortInstance.getPublisherConfigName();
                    if(publisherConfigName != null) {
                        application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                        return;
                    }

                    Transformation transformation = outputPortInstance.getApplicationContextTransformation();
                    String transformationConfigName = transformation == null ? null : transformation.getTransformationConfigName();
                    if(transformationConfigName != null) {
                        application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                        return;
                    }
                }
            }
        }

        List<Route> routes = application.getRoutes();
        for (Route route : routes) {
            String messagingConfigName = route.getMessagingConfigName();
            String selectorConfigName = route.getSelectorConfigName();

            MessageTransformation messageTransformation = route.getMessageTransformation();
            String transformationConfigName = messageTransformation == null ? null :messageTransformation.getTransformationConfigName();

            if(messagingConfigName != null || selectorConfigName != null || transformationConfigName != null) {
                application.setSchemaVersion(NAMED_CONFIGURATION_SCHEMA_VERSION);
                return;
            }
        }

        application.setSchemaVersion(DEFAULT_SCHEMA_VERSION);
    }

    private static File getApplicationFile(String appGUID, float appVersion) {
        File appFile = new File(userApplicationRepoPath + File.separator + APPLICATION_DIR_DEF + File.separator + appGUID + File.separator + appVersion + File.separator + EVENT_PROCESS_XML);
        if(appFile.exists())
            return appFile;

        appFile = new File(systemApplicationRepoPath + File.separator + APPLICATION_DIR_DEF + File.separator + appGUID + File.separator + appVersion + File.separator + EVENT_PROCESS_XML);
        if(appFile.exists())
            return appFile;

        return null;
    }

    /**
     * This Class stores the Configuration Information of transformation
     *
     * @author FSTPL
     * @version 10
     */
    public static class TransformationConfig {
        private String scriptFileName;
        private String jmsScriptFileName;
        private String projectFileName;
        private String factoryName;

        /**
         * Default Constructor
         */
        public TransformationConfig() {}

        /**
         * Constructor for TransformationConfig which sets the value for class member
         * @param scriptFileName  Script File Name
         * @param jmsScriptFileName JMS Script File name
         * @param projectFileName Project File Name
         * @param factoryName Transformation Factory Name
         */
        public TransformationConfig(String scriptFileName, String jmsScriptFileName, String projectFileName, String factoryName) {
            this.scriptFileName = scriptFileName;
            this.jmsScriptFileName = jmsScriptFileName;
            this.projectFileName = projectFileName;
            this.factoryName = factoryName;
        }

        /**
         * Gets Script File Name
         * @return Script File Name
         */
        public String getScriptFileName() {
            return scriptFileName;
        }

        /**
         * Gets Jms Script File Name
         * @return Jms Script File Name
         */
        public String getJmsScriptFileName() {
            return jmsScriptFileName;
        }

        /**
         * Gets Project File Name
         * @return Project File Name
         */
        public String getProjectFileName() {
            return projectFileName;
        }

        /**
         * Gets Transformation Factory Name
         * @return Transformation Factory Name
         */
        public String getFactoryName() {
            return factoryName;
        }

        /**
         * Sets Script File Name
         * @param scriptFileName Script File Name
         */
        public void setScriptFileName(String scriptFileName) {
            this.scriptFileName = scriptFileName;
        }

        /**
         * Sets Jms Script File Name
         * @param jmsScriptFileName Jms Script File Name
         */
        public void setJmsScriptFileName(String jmsScriptFileName) {
            this.jmsScriptFileName = jmsScriptFileName;
        }

        /**
         * Sets Project File Name
         * @param projectFileName Project File Name
         */
        public void setProjectFileName(String projectFileName) {
            this.projectFileName = projectFileName;
        }

        /**
         * Sets Transformation Factory Name
         * @param factoryName Transformation Factory Name
         */
        public void setFactoryName(String factoryName) {
            this.factoryName = factoryName;
        }
    }

    /**
     * Gets Transformation Configuration for <code>transformationName</code> listed in the file <code>metaDataFilePath</code>
     * @param metaDataFilePath MetaData File Path
     * @param transformationName Transformation Name
     * @return Transformation Configuration Instance
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public static TransformationConfig getTransformationParams(String metaDataFilePath, String transformationName) throws FileNotFoundException, XMLStreamException {
        FileInputStream fis = null;
        FioranoStaxParser parser = null;
        try {
            fis = new FileInputStream(metaDataFilePath);
            parser = new FioranoStaxParser(fis);
            if(parser.markCursor(ConfigurationRepoConstants.CONFIGURATIONS_DIR)){
                while (parser.nextElement()) {
                    String elemName = parser.getLocalName();
                    if (ConfigurationRepoConstants.ELEM_CONFIGURATION.equals(elemName)) {
                        parser.markCursor(elemName);
                        String configName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_NAME);
                        if (configName != null && configName.equals(transformationName)) {
                            String scriptFileName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_SCRIPT_FILE_NAME);
                            String jmsScriptFileName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_JMS_SCRIPT_FILE_NAME);
                            String projectFileName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_PROJECT_NAME);
                            String factoryName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_FACTORY_NAME);
                            return new TransformationConfig(scriptFileName, jmsScriptFileName, projectFileName, factoryName);
                        }
                        parser.resetCursor();
                    }
                }
            }
        } finally {
            try {
                if(parser != null)
                    parser.disposeParser();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                //Ignore
            }
        }

        return null;
    }

    /**
     * Returns managable properties for specified label for the specified application.
     * @param applicationFolderName File representing the directory containing the event process.
     * @param label Environment Label specifying which environment properties should be picked up.
     * @throws FioranoException Any exception occuring while retrieving managable properties
     * @return Application
     */
    public static HashMap getManagableProperties(File applicationFolderName, String label) throws FioranoException {
        if (applicationFolderName == null)
            throw new IllegalArgumentException();
        if (!applicationFolderName.isDirectory())
            throw new IllegalArgumentException( );
        if (label==null)//making null check, as Application.Label.valueOf(null) would throw an excpetion saying 'Name is null: java.nullpointer exception' which would be difficult for the developer to understand once he sees the logs.
            throw new IllegalArgumentException();

        Application.Label appLabel;
        try {
            appLabel = Application.Label.valueOf(label);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }

        if(appLabel != null && appLabel.equals(Application.Label.none))
            return null;

        File manageablePropertiesFile = getManageablePropertiesFile(applicationFolderName, appLabel);
        if(!manageablePropertiesFile.exists())
            return null;

        FileInputStream fis = null;
        FioranoStaxParser cursor = null;
        try {
            HashMap<String, HashMap<String, String>> managableProperties = new HashMap<String, HashMap<String, String>>();

            //2. Get the stax parser for the file
            fis = new FileInputStream(manageablePropertiesFile);
            cursor = new FioranoStaxParser(fis);
            //3. For each element that is a service instance
            // Populate the manageable properties for the service
            while (cursor.nextElement()) {
                String elemName = cursor.getLocalName();
                if (ServiceInstance.ELEM_INSTANCE.equalsIgnoreCase(elemName)) {
                    String instanceName = cursor.getAttributeValue(null, ServiceInstance.ATTR_NAME);
                    managableProperties.put(instanceName, getManagableProperties(cursor));
                }
            }

            return managableProperties;
        } catch (FileNotFoundException e) {
            throw new FioranoException(e);
        } catch (XMLStreamException e) {
            throw new FioranoException(e);
        } catch (IOException e) {
            throw new FioranoException(e);
        } finally {
            try {
                if (cursor != null)
                    cursor.close();
            } catch (XMLStreamException e) {
                // Ignore
            }
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    private static File getManageablePropertiesFile(File applicationFolderName, Application.Label label) {
        String fileName = applicationFolderName + File.separator + Application.FOLDER_ENVIRONMENT + File.separator + label.name() + Application.EXTENSION_ENVIRONMENT;
        return new File(fileName);
    }

    private static HashMap<String, String> getManagableProperties(FioranoStaxParser cursor) throws XMLStreamException {
        //1. The cursor represents the instance node
        //look for the instance properties and the managable properties
        //for instace properties populate the deployment node
        //for managable properties populate the properties object

        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        cursor.markCursor(ServiceInstance.ELEM_INSTANCE);

        while (cursor.nextElement()) {
            String elemName = cursor.getLocalName();
            if (ServiceInstance.ELEM_INSTANCE_PROPERTIES.equals(elemName)) {
                cursor.markCursor(ServiceInstance.ELEM_INSTANCE_PROPERTIES);

                while (cursor.nextElement()) {
                        elemName = cursor.getLocalName();
                    if (ServiceInstance.ELEM_DEPLOYMENT.equals(elemName)) {
                        String[] nodes = cursor.getAttributeValue(null, ServiceInstance.ATTR_NODES).split(",");
                        StringBuffer buff = new StringBuffer();
                        for(int i = 0; i < nodes.length; i++){
                            buff.append(nodes[i]);
                            if(i != nodes.length - 1)
                                buff.append(";");
                        }
                        propertiesMap.put("nodes", buff.toString());
                    }
                }

                cursor.resetCursor();

            } else if (ServiceInstance.ELEM_CONFIGURATION_PROPERTIES.equals(elemName)) {
                cursor.markCursor(ServiceInstance.ELEM_CONFIGURATION_PROPERTIES);

                while(cursor.nextElement()){
                    String elementName = cursor.getLocalName();
                    if(ServiceInstance.ELEM_PROPERTY.equalsIgnoreCase(elementName)){
                        String propertyName = cursor.getAttributeValue(null, ServiceInstance.ATTR_NAME);
                        // String encryptedProperty = cursor.getAttributeValue(null, ServiceInstance.ATTR_ENCRYPT);
                        // boolean isEncrypted = encryptedProperty != null ? Boolean.valueOf(encryptedProperty) : false;
                        // String propertyType = cursor.getAttributeValue(null, ServiceInstance.ATTR_PROPERTYTYPE);
                        String propertyValue = cursor.getText();

                        propertiesMap.put(propertyName, propertyValue);
                    }
                }
                cursor.resetCursor();
            }
        }
        cursor.resetCursor();

        return propertiesMap;
    }

    /**
     * Writes to the directory specified by <code>applicationFolderName</code>, the eventProcess.xml and other files related to the event Process.
     * @param application Application
     * @param applicationFolderName destination directory
     * @throws FioranoException FioranoException
     * @throws IOException IOException
     * @throws XMLStreamException XMLStreamException
     */
    public static void writeApplication(Application application, File applicationFolderName, boolean skipmanagableprops) throws FioranoException, IOException, XMLStreamException {
        if (!applicationFolderName.exists())
            applicationFolderName.mkdirs();
        try {
            //write the maneageable properties
            if(!skipmanagableprops)
            application.writeManageableProperties(applicationFolderName, Application.Label.valueOf(application.getLabel()));

            ApplicationContext context = application.getApplicationContext();
            if(context != null){
                String appContextConfigName = context.getConfigName();
                if (appContextConfigName == null) {
                    String appContext = context.getContent();
                    String appContextFileName = application.getGUID() + Application.EXTENSION_SCHEMA;
                    context.setFile(appContextFileName);

                    String appContextFilePath = getSchemaFileName(applicationFolderName.getPath(), appContextFileName);
                    File appContextSchemaFile = new File(appContextFilePath);

                    if (!appContextSchemaFile.exists())
                        appContextSchemaFile.getParentFile().mkdirs();

                    //write the appContextSchemaContent
                    FileOutputStream appContextSchemaFileStream = new FileOutputStream(appContextSchemaFile);
                    try {
                        appContextSchemaFileStream.write(appContext.getBytes());
                    } finally {
                        appContextSchemaFileStream.close();
                    }
                }
            }

            String layout = application.getLayout();
            String documentation = getDocumentation(layout);
            if(documentation != null){
                layout = removeDocumentation(layout);
                application.setLayout(layout);

                String helpFilePath = getHelpFileName(applicationFolderName.getPath());

                File helpFile = new File(helpFilePath);
                if(!helpFile.exists()){
                    helpFile.getParentFile().mkdirs();
                }

                //Write the help content
                FileOutputStream helpFileStream = new FileOutputStream(helpFile);
                try {
                    helpFileStream.write(documentation.getBytes());
                } finally {
                    helpFileStream.close();
                }
            }

            List<ServiceInstance> services = application.getServiceInstances();
            for (ServiceInstance service : services) {
                HashMap<String, String> schemas = new HashMap<String, String>();
                Hashtable<String, String> schemaRefsTable = new Hashtable<String, String>();

                //write the configuration
                String configuration = service.getConfiguration();
                String configFileName = service.getName() + XML_EXTENSION;
                File configFile = new File(getConfigurationFileName(applicationFolderName.getPath(), configFileName));
                if (configuration != null) {
                        service.setConfigFile(configFileName);

                        if (!configFile.exists())
                            configFile.getParentFile().mkdirs();

                        FileOutputStream configFileStream = new FileOutputStream(configFile);
                        try {
                            configFileStream.write(configuration.getBytes());
                        } finally {
                            configFileStream.close();
                        }
                    }
                else{
                    // check if config file is present
                    if(configFile.exists())
                        configFile.delete();
                }


                List<PortInstance> ports = new ArrayList<PortInstance>();
                ports.addAll(service.getInputPortInstances());
                ports.addAll(service.getOutputPortInstances());
                int count = 1;
                for(PortInstance port : ports){

                    String schemaContent = (port.getSchema()==null) ? null : port.getSchema().getContent();
                    if(schemaContent==null){
                        File schemaDir = new File(applicationFolderName.getPath() + File.separator + SCHEMAS_FOLDER);
                        if (schemaDir.exists()) {
                            for(File schemaFile : schemaDir.listFiles()){
                                if(schemaFile.getName().startsWith(service.getName() + "." + port.getName()))
                                    schemaFile.delete();
                            }
                        }
                    }else{
                        if(schemas.containsKey(schemaContent)){
                            String schemaFileName = schemas.get(schemaContent);
                            port.getSchema().setFile(schemaFileName);
                        } else{
                            String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
                            if (schemaConfigName == null) {
                                String schemaFileName = service.getName() + "." + port.getName() + Application.EXTENSION_SCHEMA;
                                schemas.put(schemaContent, schemaFileName);
                                port.getSchema().setFile(schemaFileName);

                                String schemaFilePath = getSchemaFileName(applicationFolderName.getPath(), schemaFileName);
                                File schemaFile = new File(schemaFilePath);
                                if (!schemaFile.exists())
                                    schemaFile.getParentFile().mkdirs();

                                //write the schemaContent
                                FileOutputStream schemaFileStream = new FileOutputStream(schemaFile);
                                try {
                                    schemaFileStream.write(schemaContent.getBytes());
                                } finally {
                                    schemaFileStream.close();
                                }
                            }
                        }

                        Map schemaRefs = port.getSchema().getSchemaRefs();
                        if(schemaRefs != null && !schemaRefs.isEmpty()){
                            for (Object uriObject : schemaRefs.keySet()) {
                                String uri = (String) uriObject;
                                String schemaRefContent = (String) schemaRefs.get(uri);
                                if (schemaRefsTable.containsKey(schemaRefContent)) {
                                    String schemaRefFileName = schemaRefsTable.get(schemaRefContent);
                                    port.getSchema().addSchemaRefFile(uri, schemaRefFileName);
                                } else {
                                    Map schemaRefConfigMap =  port.getSchema().getSchemaRefConfigs();
                                    if (schemaRefConfigMap.size() == 0) {
                                        String schemaRefFileName = service.getName() + "." + port.getName() + "." + count + Application.EXTENSION_SCHEMA;
                                        count++;
                                        schemaRefsTable.put(schemaRefContent, schemaRefFileName);
                                        port.getSchema().addSchemaRefFile(uri, schemaRefFileName);

                                        String schemaRefFilePath = getSchemaFileName(applicationFolderName.getPath(), schemaRefFileName);
                                        File schemaRefFile = new File(schemaRefFilePath);
                                        if (!schemaRefFile.exists())
                                            schemaRefFile.getParentFile().mkdirs();

                                        //write the schemaContent
                                        FileOutputStream schemaRefFileStream = new FileOutputStream(schemaRefFile);
                                        try {
                                            schemaRefFileStream.write(schemaRefContent.getBytes());
                                        } finally {
                                            schemaRefFileStream.close();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Handle application context transformation on output port
                    if(port instanceof OutputPortInstance){
                        OutputPortInstance outputPortInstance = (OutputPortInstance) port;
                        Transformation transformation = outputPortInstance.getApplicationContextTransformation();

                        String transformationConfigName = transformation != null ? transformation.getTransformationConfigName() : null;
                        if (transformationConfigName == null) {
                            String script = transformation != null ? transformation.getScript() : null;
                            String scriptFileName = "script" + XML_EXTENSION;
                            File scriptFile = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), scriptFileName));
                            if (script != null) {
                                transformation.setScriptFile(scriptFileName);
                                writeConfigurationFile(scriptFile, script);
                            }else{
                                // check if script file is present
                                if(scriptFile.exists())
                                    scriptFile.delete();
                            }

                            String project = transformation != null ? transformation.getProject() : null;
                            boolean isFolderRepresentation = isFolderXML(project);
                            String projectFileName = !isFolderRepresentation ? "project" + FMP_EXTENSION : "project";

                            //Delete the project first (both single file and split file format) and then save the new content
                            //Delete split file project (if exists)
                            File oldProjectDir = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), "project"));
                            if(oldProjectDir.exists() && !oldProjectDir.isFile())
                                deleteDirExceptHiddenFiles(oldProjectDir);

                            //Delete single file project (if exists)
                            File oldProjectFile = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), "project" + FMP_EXTENSION));
                            if(oldProjectFile.exists())
                            oldProjectFile.delete();

                            if (project != null) {
                                transformation.setProjectFile(projectFileName);
                                if(!isFolderRepresentation){
                                    File projectFile = new File(getPortTransformationFileName(applicationFolderName.getPath(), service.getName(), port.getName(), projectFileName));
                                    writeConfigurationFile(projectFile, project);
                                }else{
                                    File projectFile = new File(getPortTransformationDirName(applicationFolderName.getPath(), service.getName(), port.getName()));
                                    toFolder(projectFile, project);
                                }
                            }

                            File portTransformationDir = new File(getPortTransformationDirName(applicationFolderName.getPath(), service.getName(), port.getName()));
                            if(portTransformationDir.exists() && portTransformationDir.listFiles().length == 0)
                                portTransformationDir.delete();
                        }
                    }
                }
            }

            List<Route> routes = application.getRoutes();
            for(Route route : routes) {
                //write the transformation
                MessageTransformation messageTransformation = route.getMessageTransformation();

                String transformationConfigName = messageTransformation != null ? messageTransformation.getTransformationConfigName() : null;
                if (transformationConfigName == null) {
                    String script = messageTransformation != null ? messageTransformation.getScript() : null;
                    String scriptFileName = "script" + XML_EXTENSION;
                    File scriptFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), scriptFileName));
                    if (script != null) {
                        messageTransformation.setScriptFile(scriptFileName);
                        writeConfigurationFile(scriptFile, script);
                    }else{
                        // check if script file is present
                        if(scriptFile.exists())
                            scriptFile.delete();
                    }

                    String jmsScript = messageTransformation != null ? messageTransformation.getJMSScript() : null;
                    String jmsScriptFileName = "jmsScript" + XML_EXTENSION;
                    File jmsScriptFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), jmsScriptFileName));
                    if (jmsScript != null) {
                        messageTransformation.setJMSScriptFile(jmsScriptFileName);
                        writeConfigurationFile(jmsScriptFile, jmsScript);
                    }else{
                        // check if jms-script file is present
                        if(jmsScriptFile.exists())
                            jmsScriptFile.delete();
                    }

                    String project = messageTransformation != null ? messageTransformation.getProject() : null;
                    boolean isFolderRepresentation = isFolderXML(project);
                    String projectFileName = !isFolderRepresentation ? "project" + FMP_EXTENSION : "project";

                    //Delete the project first (both single file and split file format) and then save the new content
                    //Delete split file project (if exists)
                    File oldProjectDir = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), "project"));
                    if(oldProjectDir.exists() && !oldProjectDir.isFile())
                        deleteDirExceptHiddenFiles(oldProjectDir);

                    //Delete single file project (if exists)
                    File oldProjectFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), "project" + FMP_EXTENSION));
                    if(oldProjectFile.exists())
                    oldProjectFile.delete();

                    if (project != null) {
                        messageTransformation.setProjectFile(projectFileName);
                        if(!isFolderRepresentation){
                            File projectFile = new File(getRouteTransformationFileName(applicationFolderName.getPath(), route.getName(), projectFileName));
                            writeConfigurationFile(projectFile, project);
                        }else{
                            File projectFile = new File(getRouteTransformationDirName(applicationFolderName.getPath(), route.getName()));
                            toFolder(projectFile, project);
                        }
                    }

                    File routeTransformationDir = new File(getRouteTransformationDirName(applicationFolderName.getPath(), route.getName()));
                    if(routeTransformationDir.exists() && routeTransformationDir.listFiles().length == 0)
                        routeTransformationDir.delete();
                }
            }

            checkAndSetNamedConfigurationIdentifiers(application);
            application.toXMLString(applicationFolderName + File.separator + EVENT_PROCESS_XML, false);
        } catch (IOException ioe) {
            throw new FioranoException(ioe);
        } catch (XMLStreamException xmle) {
            throw new FioranoException( xmle);
        }

    }

    /**
     * delete a given directory recursively, If a hidden file/folder is found it wont be deleted and as well the parent folders structure
     * (mainly to ignore deletion of .svn folders during import)
     * @param file
     * @return true if the directory id fully deleted
     *         false if the directory contains any hidden files or folders or if it is unable to delete the files
     */
    private static boolean deleteDirExceptHiddenFiles(File file) {
        boolean success = true;

        File[] files = file.listFiles();
        for(int i = 0; i<files.length; i++){
            if(files[i].isHidden()){//ignore if it file or folder name starts with '.', considering it a hidden
                //marking as false to intimate some of its dir contents are not deleted
                success = false;
                continue;
            }
            if(files[i].isDirectory())
                success &= deleteDirExceptHiddenFiles(files[i]);
            else
                success &= files[i].delete();
        }
        //if anyone of its child is not deleted this folder won't be deleted
        return success && file.delete();
    }

    public static void writeApplication(Application application, File applicationFolderName) throws FioranoException, IOException, XMLStreamException {
        writeApplication(application, applicationFolderName, false);
    }

    private static void writeConfigurationFile(File toFile, String content) throws IOException {
        if (!toFile.exists())
            toFile.getParentFile().mkdirs();

        FileOutputStream scriptFileStream = new FileOutputStream(toFile);
        try {
            scriptFileStream.write(content.getBytes());
        } finally {
            scriptFileStream.close();
        }
    }

    private static String addDocumentation(String layout, String documentation){
        if(layout == null)
            return null;

        try {
            Document doc = XMLUtils.createDocument(layout);
            Element element = (Element) doc.getElementsByTagName(ELEM_DOCUMENTATION).item(0);

            // Remove the element if it already exists. Then replace it with new element
            if (element != null) {
                element.getParentNode().removeChild(element);
                doc.normalize();
            }

            Element docElement = doc.createElement(ELEM_DOCUMENTATION);
            docElement.setTextContent(documentation);
            doc.getFirstChild().appendChild(docElement);

            Transformer tFormer = TransformerFactory.newInstance().newTransformer();
            tFormer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            Source source = new DOMSource(doc);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);

            try {
                Result dest = new StreamResult(dos);
                tFormer.transform(source, dest);

                layout = new String(bos.toByteArray());
            } finally {
                try {
                    dos.close();
                    bos.close();
                } catch (IOException e) {
                    //Ignore
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        return layout;
    }

    /**
     * Creates and writes the XMLRepresentation of the folder into provided writer. The folder passed in the folder
     * parameter must exist.
     *
     * @param folder folder to be read
     * @throws XMLStreamException exception
     * @throws IOException exception
     * @throws NullPointerException if the folder does not exist.
     * @return String representation of the folder
     */
    public static String toXML(File folder) throws XMLStreamException, IOException {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        XMLStreamWriter xmlWriter = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            xmlWriter = outputFactory.createXMLStreamWriter(dos);
            xmlWriter.writeStartDocument();
            xmlWriter.writeStartElement(FOLDER_CONTENTS);
            toXML(folder, xmlWriter);
            xmlWriter.writeEndElement();
            xmlWriter.writeEndDocument();

            return bos.toString();
        } finally {
            if (xmlWriter != null) {
                xmlWriter.close();
            }

            try {
                dos.close();
                bos.close();
            } catch (IOException e) {
                //Ignore
            }
        }
    }

    private static void toXML(File currentFile, XMLStreamWriter xmlWriter) throws XMLStreamException, IOException {
        //svn info not required in launch packet
        if (currentFile.isDirectory()) {
            if (currentFile.getName().equalsIgnoreCase(".svn")) {
                return;
            }
        }
        xmlWriter.writeStartElement(currentFile.isFile() ? FILE : FOLDER);
        xmlWriter.writeAttribute(NAME, currentFile.getName());
        if (currentFile.isFile()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(currentFile);
                String contents = DmiObject.getContents(inputStream);
                xmlWriter.writeCData(contents);
            } catch (FileNotFoundException e) {
                // this should not happen
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            File[] folderContents = currentFile.listFiles();
            for (File file : folderContents) {
                toXML(file, xmlWriter);
            }
        }
        xmlWriter.writeEndElement();
    }

    /**
     * Parses the XML representation of folder passed and creates the required directory structure in the folder passed in
     * the folder parameter. If the folder represented by folder parameter does not exist, it creates the directory and any
     * parent directories required.
     *
     * Note: This method does not check if the XML representation is valid or not. If a proper XML representation is not
     * provided behavior is undefinied.
     *
     * @param folder - folder into which folder represented by XML representation should be created.
     * @param contents - data containing the XML representation.
     * @throws XMLStreamException exception
     * @throws IOException exception
     */
    public static void toFolder(File folder, String contents) throws XMLStreamException, IOException {
        if(contents == null)
            return;

        ByteArrayInputStream bis = new ByteArrayInputStream(contents.getBytes());
        DataInputStream dis = new DataInputStream(bis);

        if (!folder.exists()) {
            folder.mkdirs();
        }
        FioranoStaxParser parser = null;
        try {
            parser = new FioranoStaxParser(dis);
            if (parser.markCursor(FOLDER_CONTENTS)) {
                if (parser.nextElement()) {
                    toFolder(folder, parser);
                }
            }
        } finally {
            if (parser != null) {
                parser.disposeParser();
            }

            try {
                dis.close();
                bis.close();
            } catch (IOException e) {
                //Ignore
            }
        }
    }

    private static void toFolder(File folder, FioranoStaxParser parser) throws IOException, XMLStreamException {
        String fileName = parser.getAttributeValue(null, NAME);
        if (FILE.equals(parser.getLocalName())) {
            writeConfigurationFile(new File(folder, fileName), parser.getTextContent());
        } else {
            File newFolder = new File(folder, fileName);
            newFolder.mkdir();
            while (parser.hasNext()) {
                switch (parser.next()) {
                    case XMLStreamConstants.START_ELEMENT:
                        toFolder(newFolder, parser);
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        return;
                }
            }
        }
    }

    /**
     * checks whether a contents of the reader is XML representation of folder or not. Data is consumed from the reader, so
     * the same reader may not be used for subsequent calls unless Reader supports.
     *
     * @param contents - data
     * @return true, if a valid XML representation of folder can be read from the reader, false otherwise
     * @exception NullPointerException, if reader is null
     */
    public static boolean isFolderXML(String contents) {
        if(contents == null)
            return false;

        ByteArrayInputStream bis = new ByteArrayInputStream(contents.getBytes());
        DataInputStream dis = new DataInputStream(bis);

        try {
            return FOLDER_CONTENTS.equals(XMLUtils.getDocumentBuilder().parse(dis).getDocumentElement().getNodeName());
        } catch (Exception e) {
            return false;
        } finally {
            try {
                dis.close();
                bis.close();
            } catch (IOException e) {
                //Ignore
            }
        }
    }

    private static String removeDocumentation(String layout){
        if(layout == null)
            return null;

        try {
            Document doc = XMLUtils.createDocument(layout);
            Element element = (Element) doc.getElementsByTagName(ELEM_DOCUMENTATION).item(0);

            // Remove the element if it exists
            if (element != null) {
                element.getParentNode().removeChild(element);
                doc.normalize();

                Transformer tFormer = TransformerFactory.newInstance().newTransformer();
                tFormer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                Source source = new DOMSource(doc);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(bos);

                try {
                    Result dest = new StreamResult(dos);
                    tFormer.transform(source, dest);

                    layout = new String(bos.toByteArray());
                } finally {
                    try {
                        dos.close();
                        bos.close();
                    } catch (IOException e) {
                        // Ignore
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        return layout;
    }

    private static String getDocumentation(String layout){
        String documentation = null;

        if(layout == null)
            return null;

        ByteArrayInputStream bis = new ByteArrayInputStream(layout.getBytes());
        DataInputStream dis = new DataInputStream(bis);

        try {
            FioranoStaxParser parser = new FioranoStaxParser(dis);
            try {
                if (parser.markCursor(Application.ELEM_LAYOUT)) {
                    while (parser.nextElement()) {
                        String elemName = parser.getLocalName();
                        if (ELEM_DOCUMENTATION.equals(elemName)) {
                            documentation = parser.getText();
                        }
                    }
                }
            } finally {
                parser.disposeParser();
            }
        } catch (XMLStreamException e) {
            // Ignore
        } finally {
            try {
                dis.close();
                bis.close();
            } catch (IOException e) {
                //Ignore
            }
        }

        return documentation;
    }

    private static String getSchemaFileName(String applicationFolderName, String schemaFileName) {

        return applicationFolderName + File.separator + SCHEMAS_FOLDER + File.separator + schemaFileName;
    }

    private static String getHelpFileName(String applicationFolderName) {
        return applicationFolderName + File.separator + HELP_FOLDER + File.separator + HELP_FILE_NAME;
    }

    private static String getConfigurationFileName(String applicationFolderName, String configurationFileName) {
        return applicationFolderName + File.separator + CONFIG_FOLDER + File.separator + configurationFileName;
    }

    private static String getRouteTransformationFileName(String applicationFolderName, String subDirectoryName, String configurationFileName) {
        return getRouteTransformationDirName(applicationFolderName, subDirectoryName) + configurationFileName;
    }

    private static String getRouteTransformationDirName(String applicationFolderName, String subDirectoryName) {
        return applicationFolderName + File.separator + TRANSFORMATION_FOLDER + File.separator + ROUTES_FOLDER + File.separator + (subDirectoryName != null ? subDirectoryName + File.separator : "");
    }

    private static String getPortTransformationFileName(String applicationFolderName, String serviceInstanceName, String portName, String configurationFileName) {
        return getPortTransformationDirName(applicationFolderName, serviceInstanceName, portName) + configurationFileName;
    }

    private static String getPortTransformationDirName(String applicationFolderName, String serviceInstanceName, String portName) {
        return applicationFolderName + File.separator + TRANSFORMATION_FOLDER + File.separator + PORTS_FOLDER + File.separator + serviceInstanceName + File.separator + (portName != null ? portName + File.separator : "");
    }

    /**
     * Creates Application DMI by reading contents from specified inputstream <code>is</code> to an event process xml
     * @param is inputstream to event process xml file
     * @return Application
     * @throws FioranoException FioranoException
     */
    public static Application readApplication(InputStream is) throws FioranoException{

        try{

            if(is instanceof ZipInputStream){
                ZipInputStream inputStream = (ZipInputStream) is;
                ZipEntry entry = inputStream.getNextEntry();
                while(entry !=null){

                    String name = entry.getName();
                    if(EVENT_PROCESS_XML.equals(name)){
                        break;
                    }
                    entry = inputStream.getNextEntry();
                }
            }

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

                is.reset();
                Application app = new Application();
                app.setFieldValues(is);
                return app;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException( ex);
        }
    }

    /**
     * Creates Application DMI by reading contents from specified reader <code>reader</code> to an event process xml
     * @param reader reader to event process xml
     * @return Application
     * @throws FioranoException FioranoException
     */
    public static Application readApplication(Reader reader) throws FioranoException{
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
                Application app = new Application();
                app.setFieldValues(reader);
                return app;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }

    /**
     * Creates ApplicationReference DMI by reading contents of specified event process xml <code>file</code>
     * @param file event process xml to be read
     * @return ApplicationReference
     * @throws FioranoException FioranoException
     */
    public static ApplicationReference readApplicationReference(File file) throws FioranoException{
        try{
            FileReader reader = new FileReader(file);
            FileInputStream stream = null;
            try {

                    ApplicationReference appRef = new ApplicationReference();
                    stream = new FileInputStream(file);
                    appRef.setFieldValues(stream);
                    return appRef;

            } finally {
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
     * Creates ApplicationReference DMI by reading contents of specified event process xml <code>file</code>
     * @param is inputstream to event process xml
     * @return ApplicationReference
     * @throws FioranoException FioranoException
     */
    public static ApplicationReference readApplicationReference(InputStream is) throws FioranoException{
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
                ApplicationReference appRef = new ApplicationReference();
                appRef.setFieldValues(is);
                return appRef;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException( ex);
        }
    }

    /**
     * Creates ApplicationReference DMI by reading contents from specified inputstream <code>is</code> to an event process xml
     * @param inputStream inputstream to event process xml file
     * @return ApplicationReference
     * @throws FioranoException FioranoException
     */
    public static ApplicationReference readApplicationReference(ZipInputStream inputStream) throws FioranoException{
        ApplicationReference appRef = null;
        try {
            ZipEntry entry = inputStream.getNextEntry();
            while(entry !=null){

                String name = entry.getName();
                if(EVENT_PROCESS_XML.equals(name)){
                    Reader reader = null;
                    try {
                        reader = new InputStreamReader(inputStream);
                        appRef = readApplicationReference(reader);
                        break;
                    } finally {
                        try {
                            if(reader != null)
                                reader.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                }
                entry = inputStream.getNextEntry();
            }
            if(appRef == null)
                throw new FioranoException();
        } catch (IOException e) {
            throw new FioranoException( e);
        }
        return appRef;
    }

    /**
     * Creates ApplicationReference DMI by reading contents from specified reader <code>reader</code> to an event process xml
     * @param reader reader to event process xml
     * @return ApplicationReference
     * @throws FioranoException FioranoException
     */
    public static ApplicationReference readApplicationReference(Reader reader) throws FioranoException{
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
                ApplicationReference appRef = new ApplicationReference();
                appRef.setFieldValues(reader);
                return appRef;

        } catch(FioranoException ex){
            throw ex;
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }

    /**
     * Gets configuration parameter for the <code>transformationName</code>
     * @param metaDataFilePath Metadata file path for the configuration
     * @param configurationName Configuration Name
     * @return Configuration Parameters
     * @throws FileNotFoundException
     * @throws FioranoException
     */
    public static  Hashtable getConfigurationParams(String metaDataFilePath, String configurationName) throws FileNotFoundException, FioranoException {
        FileInputStream fis = null;
        FioranoStaxParser parser = null;
        try {
            fis = new FileInputStream(metaDataFilePath);
            parser = new FioranoStaxParser(fis);
            parser.nextElement();
            if (ConfigurationRepoConstants.CONFIGURATIONS_DIR.equalsIgnoreCase(parser.getLocalName())) {
                parser.markCursor(ConfigurationRepoConstants.CONFIGURATIONS_DIR);
                while (parser.nextElement()) {
                    String elemName = parser.getLocalName();
                    if (ConfigurationRepoConstants.ELEM_CONFIGURATION.equals(elemName)) {
                        parser.markCursor(elemName);
                        String configName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_NAME);
                        if (configName != null && configName.equals(configurationName))
                            return parser.getAttributes();
                        parser.resetCursor();
                    }
                }
            }
        } catch(XMLStreamException e){
            throw new FioranoException(e);
        } finally {
            try {
                if(parser != null)
                    parser.disposeParser();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                //Ignore
            }
        }

        return null;
    }
}