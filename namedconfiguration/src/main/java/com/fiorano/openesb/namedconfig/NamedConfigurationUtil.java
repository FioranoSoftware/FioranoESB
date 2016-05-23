/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.namedconfig;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.application.configuration.data.*;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static com.fiorano.openesb.application.constants.ConfigurationRepoConstants.*;

public class NamedConfigurationUtil {

    private static final  String separator = File.separator;
    private static final String KEYSTORE_RESOURCE = "";

    public static String getPortConfigurationPath(String repositoryPath, String portType, String label) {
        return repositoryPath + separator + PORTS + separator + portType + separator + label;
    }

    public static File getPortConfigurationFile(String repositoryPath, String portType, String label, String configurationName) {
        return new File(getPortConfigurationPath(repositoryPath, portType, label) + separator + configurationName);
    }

    public static String getServiceConfigurationPath(String repositoryPath, String label) {
        return repositoryPath + separator + COMPONENTS + separator + label;
    }

    public static File getServiceConfigurationFile(String repositoryPath, String serviceGUID, String version, String label, String configurationName) {
        return new File(getServiceConfigurationPath(repositoryPath, label)  + separator + serviceGUID + separator + version + separator +
                configurationName);
    }

    public static String getConfigurationPath(String repositoryPath, String configurationType, String label) {
        return repositoryPath + separator + configurationType + separator + label ;
    }

    public static File getConfigurationFile(String repositoryPath, String configurationType, String label, String configurationName) {
        return new File(getConfigurationPath(repositoryPath, configurationType, label) + separator + configurationName);
    }

    //For back-word compatibility searching for configuration in with out labels
    private static String getPortConfigurationPathWithOutLabel(String repositoryPath, String portType) {
        return repositoryPath + separator + PORTS + separator + portType;
    }

    private static String getServiceConfigurationPathWithOutLabel(String repositoryPath) {
        return repositoryPath + separator + COMPONENTS;
    }

    private static String getConfigurationPathWithOutLabel(String repositoryPath, String configurationType) {
        return repositoryPath + separator + configurationType;
    }

    /*------------------------------------------Persist configurations------------------------------------------------------------------------------------- */
    public static void persistConfiguration(DataObject data, boolean update, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws IOException, FioranoException {
        ObjectCategory category = data.getObjectCategory();
        if(category == null)
            throw new FioranoException("NO_CATEGORY_SPECIFIED");

        String configurationName = data.getName();
        if(configurationName != null && configurationName.equalsIgnoreCase(ConfigurationRepoConstants.METADATA_XML))
            throw new FioranoException("ILLEGAL_CONFIGURATION_NAME");

        switch(category){
            case PORT_CONFIGURATION:
                persistPortConfiguration(data, update, repositoryPath, eventListeners);
                break;
            case SERVICE_CONFIGURATION:
                persistServiceConfiguration(data, update, repositoryPath, eventListeners);
                break;
            case ROUTE:
                persistConfiguration(data, ROUTES, update, repositoryPath, eventListeners);
                break;
            case SELECTOR:
                persistConfiguration(data, SELECTORS, update, repositoryPath, eventListeners);
                break;
            case TRANSFORMATION:
                persistConfiguration(data, TRANSFORMATIONS, update, repositoryPath, eventListeners);
                break;
            case RESOURCE_CONFIGURATION:
                persistConfiguration(data, RESOURCES, update, repositoryPath, eventListeners);
                break;
            case WORKFLOW:
                persistConfiguration(data, WORKFLOWS, update, repositoryPath, eventListeners);
                break;
            case MESSAGEFILTERS:
                persistConfiguration(data,MESSAGEFILTERS,update,repositoryPath,eventListeners);
                break;
            case DESTINATION:
                persistConfiguration(data, DESTINATION, update, repositoryPath, eventListeners);
                break;
            case RUNTIME_ARG_CONFIGURATION:
                persistConfiguration(data, RUNTIME_ARGS, update, repositoryPath, eventListeners);
                break;
            case CONNECTION_FACTORY_CONFIGURATION:
                persistConfiguration(data, CONNECTION_FACTORY, update, repositoryPath, eventListeners);
                break;
            case MISCELLANEOUS:
                persistConfiguration(data, MISC, update, repositoryPath, eventListeners);
                break;
        }
    }

    private static void persistPortConfiguration(DataObject dataObject, boolean update, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener>
            eventListeners) throws  IOException, FioranoException {
        String configurationName = dataObject.getName();
        PortType portType = ((PortConfigurationDataObject) dataObject).getPortType();
        byte[] data = dataObject.getData();

        if(configurationName == null || portType == null || data == null || data.length == 0)
            throw new FioranoException("INVALID_PORT_DATA");

        File configurationFile = getPortConfigurationFile(repositoryPath, portType.toString(), dataObject.getLabel().toString(), configurationName);
        if(configurationFile.exists()){
            if(!update){
                //throw new ConfigurationAlreadyExistsException(I18NUtil.getMessage(Bundle.class, Bundle.CONFIGURATION_ALREADY_EXISTS, configurationName, PORTS));
                return; // Since configurations are already present and user chosen not to over write
            }
            else
                deleteConfiguration(configurationFile, configurationName, null, dataObject.getObjectCategory(), eventListeners);
        }

        File configurationDir = new File(configurationFile.getParent());
        if(!configurationDir.exists())
            configurationDir.mkdirs();

        saveConfiguration(dataObject, configurationFile, eventListeners);
    }

    private static  void persistServiceConfiguration(DataObject dataObject, boolean update, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws  IOException,
            FioranoException {
        String configurationName = dataObject.getName();
        String serviceGUID = ((ServiceConfigurationDataObject) dataObject).getServiceGUID();
        String version = ((ServiceConfigurationDataObject) dataObject).getServiceVersion();
        byte[] data = dataObject.getData();

        if(configurationName == null || serviceGUID == null || version == null || data == null || data.length == 0)
            throw new FioranoException("INVALID_SERVICE_DATA");

        File configurationFile = getServiceConfigurationFile(repositoryPath, serviceGUID, ensureDecimalPoint(Float.parseFloat(version)), dataObject.getLabel().toString(),
                configurationName);

        if(configurationFile.exists()){
            if(!update){
                //throw new ConfigurationAlreadyExistsException(I18NUtil.getMessage(Bundle.class, Bundle.CONFIGURATION_ALREADY_EXISTS, configurationName, COMPONENTS));
                return; // Since configurations are already present and user chosen not to over write
            }
            else
                deleteConfiguration(configurationFile, configurationName, null, dataObject.getObjectCategory(), eventListeners);
        }

        File configurationDir = new File(configurationFile.getParent());
        if(!configurationDir.exists())
            configurationDir.mkdirs();

        saveConfiguration(dataObject, configurationFile, eventListeners);
    }

    private static  void persistConfiguration(DataObject dataObject, String repoName, boolean update, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws  IOException,
            FioranoException {
        String configurationName = dataObject.getName();
        byte[] data = dataObject.getData();

        if(configurationName == null || data == null || data.length == 0)
            throw new FioranoException("INVALID_DATA");

        final String label = dataObject.getLabel().toString();
        try {
            Label.valueOf(label.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FioranoException("INVALID_LABEL");
        }

        if (repoName.equalsIgnoreCase(RESOURCES) && !update) {
            if (((ResourceConfigurationDataObject)dataObject).getResourceType().equalsIgnoreCase(KEYSTORE_RESOURCE)) {
                String metaDataFilePath = getConfigurationPath(repositoryPath, repoName, label) + File.separator + METADATA_XML;
                if (new File(metaDataFilePath).exists()) {
                    if (checkForKeystoreConfigPresence(metaDataFilePath)) {
                        throw new FioranoException("ERROR_PERSISTING_KEYSTORE");
                    }
                }
            }
        }

        File configurationFile = getConfigurationFile(repositoryPath, repoName, label, configurationName);
        if(configurationFile.exists()){
            if(!update){
                //throw new ConfigurationAlreadyExistsException(I18NUtil.getMessage(Bundle.class, Bundle.CONFIGURATION_ALREADY_EXISTS, configurationName, repoName));
                return; // Since configurations are already present and user chosen not to over write
            }
            else
                deleteConfiguration(configurationFile, configurationName, null, dataObject.getObjectCategory(), eventListeners);
        }

        File configurationDir = new File(configurationFile.getParent());
        if(!configurationDir.exists())
            configurationDir.mkdirs();

        saveConfiguration(dataObject, configurationFile, eventListeners);
    }

    private static  void saveConfiguration(DataObject dataObject, File configurationFile, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws IOException, FioranoException {
        DataType dataType = dataObject.getDataType();
        byte[] data = dataObject.getData();
        String configurationName = dataObject.getName();

        if(dataType == null)
            throw new FioranoException("DATA_TYPE_NOT_SPECIFIED");

        if(dataType.equals(DataType.TEXT)){
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(configurationFile);
                fos.write(data);
            } finally {
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        //Ignore
                    }
                }
            }
        }else if(dataType.equals(DataType.ZIP_STREAM)){
            if(!configurationFile.exists())
                configurationFile.mkdirs();

            //Unzip the zip stream represented by data in the above directory
            unzip(data, configurationFile);
        }

        //Add entry to metadata file
        String metaDataFilePath = configurationFile.getParent() + separator + METADATA_XML;
        addElementToMetaDataFile(metaDataFilePath, dataObject);
        if (eventListeners != null){
            for (ConfigurationRepositoryEventListener eventListener : eventListeners) {
                try {
                    eventListener.configurationPersisted(convertToNamedObject(dataObject));
                } catch (Throwable e) {
                    //Ignore the exception. Send event to other listeners
                }
            }
        }
    }

    private static  boolean checkForKeystoreConfigPresence(String metaDataFilePath) throws FioranoException, IOException {
        FileInputStream fileInputStream = null;
        FioranoStaxParser parser = null;
        boolean keystorePresent = false;
        try {

            fileInputStream = new FileInputStream(metaDataFilePath);
            parser = new FioranoStaxParser(fileInputStream);
            parser.markCursor(CONFIGURATIONS_DIR);
            while (parser.nextElement()) {
                String resourceType = parser.getAttributeValue(null, ATTR_RESOURCE_TYPE);
                if (resourceType.equalsIgnoreCase(KEYSTORE_RESOURCE)) {
                    keystorePresent = true;
                    break;
                }
            }
            parser.resetCursor();
        } catch (XMLStreamException e) {
            throw new FioranoException("ERROR_CHECKING_KEYSTORE_PRESENCE");
        } finally {
            if (parser != null)
                try {
                    parser.disposeParser();
                } catch (XMLStreamException e) {
                    //Ignore
                }
            if (fileInputStream != null)
                fileInputStream.close();
        }

        return keystorePresent;
    }

    private static  NamedObject convertToNamedObject(DataObject dataObject) {
        if (dataObject == null)
            return null;

        if (dataObject instanceof PortConfigurationDataObject) {
            PortConfigurationNamedObject namedObject = new PortConfigurationNamedObject();
            setCommonParameters(namedObject, dataObject);
            namedObject.setDestinationType(((PortConfigurationDataObject) dataObject).getDestinationType());
            namedObject.setPortType(((PortConfigurationDataObject) dataObject).getPortType());
            return namedObject;
        } else if(dataObject instanceof ResourceConfigurationDataObject) {
            ResourceConfigurationNamedObject namedObject = new ResourceConfigurationNamedObject();
            setCommonParameters(namedObject, dataObject);
            namedObject.setResourceType(((ResourceConfigurationDataObject) dataObject).getResourceType());
            return namedObject;
        } else if(dataObject instanceof ServiceConfigurationDataObject) {
            ServiceConfigurationNamedObject namedObject = new ServiceConfigurationNamedObject();
            setCommonParameters(namedObject, dataObject);
            namedObject.setServiceGUID(((ServiceConfigurationDataObject) dataObject).getServiceGUID());
            namedObject.setServiceVersion(((ServiceConfigurationDataObject) dataObject).getServiceVersion());
            namedObject.setConfigurationType(((ServiceConfigurationDataObject) dataObject).getConfigurationType());
            return namedObject;
        } else if(dataObject instanceof TransformationConfigurationDataObject) {
            TransformationConfigurationNamedObject namedObject = new TransformationConfigurationNamedObject();
            setCommonParameters(namedObject, dataObject);
            namedObject.setScriptFileName(((TransformationConfigurationDataObject) dataObject).getScriptFileName());
            namedObject.setJmsScriptFileName(((TransformationConfigurationDataObject) dataObject).getJmsScriptFileName());
            namedObject.setProjectFileName(((TransformationConfigurationDataObject) dataObject).getProjectFileName());
            namedObject.setFactoryName(((TransformationConfigurationDataObject) dataObject).getFactoryName());
            return namedObject;
        } else if(dataObject instanceof RuntimeargConfigurationDataObject) {
            RuntimeargConfigurationNamedObject namedObject = new RuntimeargConfigurationNamedObject();
            setCommonParameters(namedObject, dataObject);
            namedObject.setServiceGUID(((RuntimeargConfigurationDataObject) dataObject).getServiceGUID());
            namedObject.setServiceVersion(((RuntimeargConfigurationDataObject) dataObject).getServiceVersion());
            return namedObject;
        }

        NamedObject namedObject = new NamedObject();
        setCommonParameters(namedObject, dataObject);
        return namedObject;
    }

    private static  void setCommonParameters(NamedObject targetObject, DataObject sourceObject) {
        targetObject.setName(sourceObject.getName());
        targetObject.setLabel(sourceObject.getLabel());
        targetObject.setObjectCategory(sourceObject.getObjectCategory());
    }

    /*-----------------------------------------Delete Configurations--------------------------------------------------------------------------------------- */

    public static boolean deleteConfiguration(NamedObject namedObject, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners)
            throws  IOException, FioranoException {
        ObjectCategory category = namedObject.getObjectCategory();
        if(category == null)
            throw new FioranoException("NO_CATEGORY_SPECIFIED");

        String configurationName = namedObject.getName();
        if(configurationName == null)
            throw new FioranoException("NO_NAME_SPECIFIED");

        switch(category){
            case PORT_CONFIGURATION:
                deletePortConfiguration(namedObject, repositoryPath, eventListeners);
                return true;
            case SERVICE_CONFIGURATION:
                deleteServiceConfiguration(namedObject, repositoryPath, eventListeners);
                return true;
            case ROUTE:
                deleteConfiguration(namedObject, ROUTES, repositoryPath, eventListeners);
                return true;
            case SELECTOR:
                deleteConfiguration(namedObject, SELECTORS, repositoryPath, eventListeners);
                return true;
            case TRANSFORMATION:
                deleteConfiguration(namedObject, TRANSFORMATIONS, repositoryPath, eventListeners);
                return true;
            case WORKFLOW:
                deleteConfiguration(namedObject, WORKFLOWS, repositoryPath, eventListeners);
                return true;
            case MESSAGEFILTERS:
                deleteConfiguration(namedObject,MESSAGEFILTERS,repositoryPath,eventListeners);
                return true;
            case RESOURCE_CONFIGURATION:
                deleteConfiguration(namedObject, RESOURCES, repositoryPath, eventListeners);
                return true;
            case RUNTIME_ARG_CONFIGURATION:
                deleteConfiguration(namedObject, RUNTIME_ARGS, repositoryPath, eventListeners);
                return true;
            case CONNECTION_FACTORY_CONFIGURATION:
                deleteConfiguration(namedObject, CONNECTION_FACTORY, repositoryPath, eventListeners);
                return true;
            case DESTINATION:
                deleteConfiguration(namedObject, DESTINATION, repositoryPath, eventListeners);
                return true;
            case MISCELLANEOUS:
                deleteConfiguration(namedObject, MISC, repositoryPath, eventListeners);
                return true;
        }

        return false;
    }


    private static void deletePortConfiguration(NamedObject namedObject, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners)
            throws IOException, FioranoException {

        String configurationName = namedObject.getName();
        PortType portType = ((PortConfigurationNamedObject) namedObject).getPortType();

        final Label label = namedObject.getLabel();
        if(portType == null || label == null || Label.valueOf(label.toString().toUpperCase()) == null)
            throw new FioranoException("INVALID_PORT_CONFIGURATION_DELETE_PARAMS");

        File configurationFile = getPortConfigurationFile(repositoryPath, portType.toString(), label.toString(), configurationName);
        if(!configurationFile.exists())
            throw new FioranoException("CONFIGURATION_DOES_NOT_EXIST");
        else
            deleteConfiguration(configurationFile, configurationName, namedObject, namedObject.getObjectCategory(), eventListeners);
    }

    private static void deleteServiceConfiguration(NamedObject namedObject, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners)
            throws FioranoException, IOException, FioranoException {

        String configurationName = namedObject.getName();
        String serviceGUID = ((ServiceConfigurationNamedObject) namedObject).getServiceGUID();
        String version = ((ServiceConfigurationNamedObject) namedObject).getServiceVersion();

        final Label label = namedObject.getLabel();
        if(serviceGUID == null || version == null || label == null || Label.valueOf(label.toString().toUpperCase()) == null)
            throw new FioranoException("INVALID_SERVICE_CONFIGURATION_DELETE_PARAMS");

        File configurationFile = getServiceConfigurationFile(repositoryPath, serviceGUID,
                ensureDecimalPoint(Float.parseFloat(version)), label.toString(), configurationName);
        if(!configurationFile.exists())
            throw new FioranoException("CONFIGURATION_DOES_NOT_EXIST");
        else
            deleteConfiguration(configurationFile, configurationName, namedObject, namedObject.getObjectCategory(), eventListeners);
    }

    private static  void deleteConfiguration(NamedObject namedObject, String repoName, String repositoryPath, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws  IOException, FioranoException {
        final Label label = namedObject.getLabel();
        if(label == null || Label.valueOf(label.toString().toUpperCase()) == null)
            throw new FioranoException("INVALID_CONFIGURATION_LABEL");
        String configurationName = namedObject.getName();
        File configurationFile = getConfigurationFile(repositoryPath, repoName, label.toString(), configurationName);
        if(!configurationFile.exists())
            throw new FioranoException("CONFIGURATION_DOES_NOT_EXIST");
        else
            deleteConfiguration(configurationFile, configurationName, namedObject, namedObject.getObjectCategory(), eventListeners);
    }


    private static  void deleteConfiguration(File configuration, String configurationName, NamedObject namedObject,
                                             ObjectCategory objectCategory, ArrayList<ConfigurationRepositoryEventListener> eventListeners) throws IOException,
            FioranoException {
        if(configuration.exists()){
            if(configuration.isFile())
                configuration.delete();
            else if(configuration.isDirectory())
                FileUtil.deleteDir(configuration);

            //Delete entry from metadata file
            String metaDataFilePath = configuration.getParent() + separator + METADATA_XML;
            deleteElementFromMetaDataFile(metaDataFilePath, configurationName, objectCategory);

            if (namedObject != null) {
                //namedObject will be null when this method is invoked while updating a configuration which results in deleting the configuration first
                for (ConfigurationRepositoryEventListener eventListener : eventListeners) {
                    try {
                        eventListener.configurationDeleted(namedObject);
                    } catch (Throwable e) {
                        //Ignore the exception. Send event to other listeners
                    }
                }
            }
        }
    }

    /*-----------------------------------------Fetch Configurations --------------------------------------------------------------------------------------- */

    public static  ArrayList<DataObject> getConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData, String repositoryPath) throws
            IOException, FioranoException {
        if(namedObject == null)
            namedObject = new NamedObject();

        final ObjectCategory objectCategory = namedObject.getObjectCategory();
        if(objectCategory == null){
            ArrayList<DataObject> toReturn = new ArrayList<DataObject>();
            toReturn.addAll(getPortConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getServiceConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, ROUTES, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.ROUTE, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, SELECTORS, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.SELECTOR, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, TRANSFORMATIONS,
                    DmiObjectTypes.TRANSFORMATION_CONFIGURATION_DATA_OBJECT, ObjectCategory.TRANSFORMATION, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, WORKFLOWS, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.WORKFLOW, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject,MESSAGEFILTERS,DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.MESSAGEFILTERS,retrieveOnlyMetaData,repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, RESOURCES, DmiObjectTypes.RESOURCE_CONFIGURATION_DATA_OBJECT,
                    ObjectCategory.RESOURCE_CONFIGURATION, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, RUNTIME_ARGS,
                    DmiObjectTypes.RUNTIMEARG_CONFIGURATION_DATA_OBJECT, ObjectCategory.RUNTIME_ARG_CONFIGURATION, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, CONNECTION_FACTORY, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.CONNECTION_FACTORY_CONFIGURATION, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, DESTINATION, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.DESTINATION, retrieveOnlyMetaData, repositoryPath));
            toReturn.addAll(getDataConfigurations(namedObject, MISC, DmiObjectTypes.DATA_CONFIGURATION_OBJECT,
                    ObjectCategory.MISCELLANEOUS, retrieveOnlyMetaData, repositoryPath));
            return toReturn;
        }

        switch(objectCategory){
            case PORT_CONFIGURATION:
                return getPortConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
            case SERVICE_CONFIGURATION:
                return getServiceConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
            case ROUTE:
                return getDataConfigurations(namedObject, ROUTES, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.ROUTE, retrieveOnlyMetaData, repositoryPath);
            case SELECTOR:
                return getDataConfigurations(namedObject, SELECTORS, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.SELECTOR, retrieveOnlyMetaData, repositoryPath);
            case TRANSFORMATION:
                return getDataConfigurations(namedObject, TRANSFORMATIONS, DmiObjectTypes.TRANSFORMATION_CONFIGURATION_DATA_OBJECT, ObjectCategory.TRANSFORMATION, retrieveOnlyMetaData, repositoryPath);
            case WORKFLOW:
                return getDataConfigurations(namedObject, WORKFLOWS, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.WORKFLOW, retrieveOnlyMetaData, repositoryPath);
            case MESSAGEFILTERS:
                return getDataConfigurations(namedObject, MESSAGEFILTERS,DmiObjectTypes.DATA_CONFIGURATION_OBJECT,ObjectCategory.MESSAGEFILTERS,retrieveOnlyMetaData,repositoryPath);
            case DESTINATION:
                return getDataConfigurations(namedObject, DESTINATION, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.DESTINATION, retrieveOnlyMetaData, repositoryPath);
            case RESOURCE_CONFIGURATION:
                return getDataConfigurations(namedObject, RESOURCES, DmiObjectTypes.RESOURCE_CONFIGURATION_DATA_OBJECT, ObjectCategory.RESOURCE_CONFIGURATION, retrieveOnlyMetaData, repositoryPath);
            case RUNTIME_ARG_CONFIGURATION:
                return getDataConfigurations(namedObject, RUNTIME_ARGS, DmiObjectTypes.RUNTIMEARG_CONFIGURATION_DATA_OBJECT, ObjectCategory.RUNTIME_ARG_CONFIGURATION, retrieveOnlyMetaData, repositoryPath);
            case CONNECTION_FACTORY_CONFIGURATION:
                return getDataConfigurations(namedObject, CONNECTION_FACTORY, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.CONNECTION_FACTORY_CONFIGURATION, retrieveOnlyMetaData, repositoryPath);
            case MISCELLANEOUS:
                return getDataConfigurations(namedObject, MISC, DmiObjectTypes.DATA_CONFIGURATION_OBJECT, ObjectCategory.MISCELLANEOUS, retrieveOnlyMetaData, repositoryPath);
            default:
                return new ArrayList<DataObject>();
        }
    }


    public static  ArrayList<DataObject> getPortConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData, String repositoryPath) throws FioranoException,
            IOException, FioranoException {
        PortType portType = null;
        if(namedObject instanceof PortConfigurationNamedObject)
            portType = ((PortConfigurationNamedObject) namedObject).getPortType();
        final Label label = namedObject.getLabel();
        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();
        if(portType == null || portType.equals(PortType.INPUT)){
            if (label != null && !label.toString().equals("")) {
                String configurationPath = getPortConfigurationPath(repositoryPath, PortType.INPUT.toString(), label.toString());
                searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.INPUT, label, retrieveOnlyMetaData, toReturn);
            } else {
                for (Label label1 : Label.values()) {
                    String configurationPath = getPortConfigurationPath(repositoryPath, PortType.INPUT.toString(),
                            label1.toString());
                    searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.INPUT, label1, retrieveOnlyMetaData, toReturn);
                }
            }
            //For back-word compatibility
            String configurationPath = getPortConfigurationPathWithOutLabel(repositoryPath, PortType.INPUT.toString());
            searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.INPUT, null, retrieveOnlyMetaData, toReturn);
        }

        if(portType == null || portType.equals(PortType.OUTPUT)){
            if (label != null && !label.toString().equals("")) {
                String configurationPath = getPortConfigurationPath(repositoryPath, PortType.OUTPUT.toString(), label.toString());
                searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.OUTPUT, label, retrieveOnlyMetaData, toReturn);
            } else {
                for (Label label1 : Label.values()) {
                    String configurationPath = getPortConfigurationPath(repositoryPath, PortType.OUTPUT.toString(),
                            label1.toString());
                    searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.OUTPUT, label1, retrieveOnlyMetaData, toReturn);
                }
            }
            //For back-word compatibility
            String configurationPath = getPortConfigurationPathWithOutLabel(repositoryPath, PortType.OUTPUT.toString());
            searchPortConfigurationsBasedOnLabel(namedObject, configurationPath, PortType.OUTPUT, null, retrieveOnlyMetaData, toReturn);
        }



        return toReturn;
    }

    private static  void searchPortConfigurationsBasedOnLabel(NamedObject namedObject,String configurationPath, PortType dataObjectPortType,
                                                              Label label, boolean retrieveOnlyMetaData, List<DataObject> toReturn) throws
            IOException, FioranoException {
        toReturn.addAll(searchPortConfigurations(configurationPath, namedObject, dataObjectPortType, label, retrieveOnlyMetaData));

    }

    private static  ArrayList<DataObject> searchPortConfigurations(String configurationPath, NamedObject namedObject, PortType dataObjectPortType, Label label,
                                                                   boolean retrieveOnlyMetaData) throws IOException, FioranoException {
        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();

        String objectName = namedObject.getName();
        File configurationDir = new File(configurationPath);
        if(configurationDir.exists()){
            for(File persistedConfiguration : configurationDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !new File(dir, name).isDirectory() && !name.equals(ConfigurationRepoConstants.METADATA_XML);
                } })){
                String configurationName = persistedConfiguration.getName();
                if(objectName == null || objectName.equals(configurationName)){
                    PortConfigurationDataObject dataObject = new PortConfigurationDataObject();
                    boolean matches = matchAndFillAttributes(persistedConfiguration, namedObject, configurationDir + separator + ConfigurationRepoConstants.METADATA_XML, dataObject, retrieveOnlyMetaData);

                    if(matches){
                        dataObject.setPortType(dataObjectPortType);
                        if (label != null)
                            dataObject.setLabel(label);
                        toReturn.add(dataObject);
                    }
                }
            }
        }

        return toReturn;
    }

    private static  boolean matchAndFillAttributes(File persistedConfiguration, NamedObject namedObject, String metaDataFilePath, DataObject dataObject,
                                                   boolean retrieveOnlyMetaData) throws IOException, FioranoException {
        String configurationName = persistedConfiguration.getName();

        Label objectLabel = namedObject.getLabel();
        Hashtable metaDataParams = getConfigurationParams(metaDataFilePath, configurationName);
        if(metaDataParams == null)
            return false;

        if(objectLabel != null && metaDataParams.get(ConfigurationRepoConstants.ATTR_LABEL) != null && !objectLabel.toString().equalsIgnoreCase(metaDataParams.get(ConfigurationRepoConstants.ATTR_LABEL).toString()))
            return false;

        //Match configuration specific parameters now
        if(namedObject instanceof PortConfigurationNamedObject){
            PortConfigurationNamedObject lookupObject = (PortConfigurationNamedObject) namedObject;
            DestinationType destinationType = lookupObject.getDestinationType();
            if(destinationType != null && !destinationType.toString().equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_DESTINATION_TYPE)))
                return false;
        }else if(namedObject instanceof ResourceConfigurationNamedObject){
            ResourceConfigurationNamedObject lookupObject = (ResourceConfigurationNamedObject) namedObject;
            String resourceType = lookupObject.getResourceType();
            if(resourceType != null && !resourceType.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_RESOURCE_TYPE)))
                return false;
        }else if(namedObject instanceof ServiceConfigurationNamedObject){
            ServiceConfigurationNamedObject lookupObject = (ServiceConfigurationNamedObject) namedObject;
            ServiceConfigurationType serviceConfigurationType = lookupObject.getConfigurationType();
            if(serviceConfigurationType != null && !serviceConfigurationType.toString().equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_CONFIGURATION_TYPE)))
                return false;
        }else if(namedObject instanceof TransformationConfigurationNamedObject){
            TransformationConfigurationNamedObject lookupObject = (TransformationConfigurationNamedObject) namedObject;
            String scriptFileName = lookupObject.getScriptFileName();
            String jmsScriptFileName = lookupObject.getJmsScriptFileName();
            String projectFileName = lookupObject.getProjectFileName();
            String factoryName = lookupObject.getFactoryName();

            if(scriptFileName != null && !scriptFileName.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_SCRIPT_FILE_NAME)))
                return false;

            if(jmsScriptFileName != null && !jmsScriptFileName.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_JMS_SCRIPT_FILE_NAME)))
                return false;

            if(projectFileName != null && !projectFileName.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_PROJECT_NAME)))
                return false;

            if(factoryName != null && !factoryName.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_FACTORY_NAME)))
                return false;
        }else if(namedObject instanceof RuntimeargConfigurationNamedObject){
            RuntimeargConfigurationNamedObject lookupObject = (RuntimeargConfigurationNamedObject) namedObject;
            String serviceGUID = lookupObject.getServiceGUID();
            String serviceVersion = ensureDecimalPoint(lookupObject.getServiceVersion());

            if(serviceGUID != null && !serviceGUID.equals(metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_GUID)))
                return false;

            if(serviceVersion != null && !serviceVersion.equals(ensureDecimalPoint((String) metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_VERSION))))
                return false;
        }

        if(dataObject != null) {
            //Populate values into data object
            dataObject.setName(configurationName);

            String labelValue = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_LABEL);
            dataObject.setLabel(labelValue != null ? Label.valueOf(labelValue.toUpperCase()) : null);

            if(persistedConfiguration.isFile()){
                dataObject.setDataType(DataType.TEXT);

                if (!retrieveOnlyMetaData) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(persistedConfiguration);
                        byte[] data = new byte[fis.available()];

                        //noinspection ResultOfMethodCallIgnored
                        fis.read(data);
                        dataObject.setData(data);
                    } finally {
                        if (fis != null)
                            fis.close();
                    }
                }
            } else if(persistedConfiguration.isDirectory()){
                dataObject.setDataType(DataType.ZIP_STREAM);

                if (!retrieveOnlyMetaData) {
                    File zipFile = getTempFile("", "");

                    FileOutputStream fos = new FileOutputStream(zipFile, true);
                    ZipOutputStream zipOutputStream = new ZipOutputStream(fos);

                    FilenameFilter filter = new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return true;
                        }
                    };

                    try {
                        // Configuration directory is ready with whatever needed. Zip it up and send it across.
                        String dirToBeZipped = persistedConfiguration.getAbsolutePath();
                        Util.zipDir(dirToBeZipped, filter, zipOutputStream, 1024);
                    } finally {
                        if (zipOutputStream != null)
                            zipOutputStream.close();
                        if (fos != null)
                            fos.close();
                    }

                    BufferedInputStream bis = null;
                    try {
                        bis = new BufferedInputStream(new FileInputStream(zipFile));
                        byte[] tempContents = new byte[bis.available()];

                        //noinspection ResultOfMethodCallIgnored
                        bis.read(tempContents);
                        dataObject.setData(tempContents);

                        if (zipFile.exists())
                            zipFile.delete();
                    } finally {
                        if (bis != null)
                            bis.close();
                    }
                }
            }

            if(dataObject instanceof PortConfigurationDataObject){
                PortConfigurationDataObject portDataObject = (PortConfigurationDataObject) dataObject;
                String destinationTypeValue = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_DESTINATION_TYPE);
                portDataObject.setDestinationType(destinationTypeValue != null ? DestinationType.valueOf(destinationTypeValue) : null);
            }else if(dataObject instanceof ResourceConfigurationDataObject){
                ResourceConfigurationDataObject resourceDataObject = (ResourceConfigurationDataObject) dataObject;
                String resourceType = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_RESOURCE_TYPE);
                resourceDataObject.setResourceType(resourceType);
            }else if(dataObject instanceof ServiceConfigurationDataObject){
                ServiceConfigurationDataObject serviceDataObject = (ServiceConfigurationDataObject) dataObject;
                String serviceConfigurationType = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_CONFIGURATION_TYPE);
                serviceDataObject.setConfigurationType(serviceConfigurationType != null ? ServiceConfigurationType.valueOf(serviceConfigurationType) : null);
            }else if(dataObject instanceof TransformationConfigurationDataObject){
                TransformationConfigurationDataObject transformationDataObject = (TransformationConfigurationDataObject) dataObject;
                String scriptFileName = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_SCRIPT_FILE_NAME);
                String jmsScriptFileName = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_JMS_SCRIPT_FILE_NAME);
                String projectFileName = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_PROJECT_NAME);
                String factoryName = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_FACTORY_NAME);
                transformationDataObject.setScriptFileName(scriptFileName);
                transformationDataObject.setJmsScriptFileName(jmsScriptFileName);
                transformationDataObject.setProjectFileName(projectFileName);
                transformationDataObject.setFactoryName(factoryName);
            }else if(dataObject instanceof RuntimeargConfigurationDataObject){
                RuntimeargConfigurationDataObject runtimeargConfigurationDataObject = (RuntimeargConfigurationDataObject) dataObject;
                String serviceGUID = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_GUID);
                String serviceVersion = (String) metaDataParams.get(ConfigurationRepoConstants.ATTR_SERVICE_VERSION);
                runtimeargConfigurationDataObject.setServiceGUID(serviceGUID);
                runtimeargConfigurationDataObject.setServiceVersion(serviceVersion);
            }
        }

        return true;
    }

    public static  ArrayList<DataObject> getServiceConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData, String repositoryPath) throws IOException,
            FioranoException {
        String serviceGUID = null;
        String serviceVersion = null;

        if(namedObject instanceof ServiceConfigurationNamedObject){
            serviceGUID = ((ServiceConfigurationNamedObject) namedObject).getServiceGUID();
            serviceVersion = ((ServiceConfigurationNamedObject) namedObject).getServiceVersion();
        }

        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();
        String componentRepositoryConfigurationPath;
        final Label label = namedObject.getLabel();
        if (label != null && !label.toString().equals("")) {
            componentRepositoryConfigurationPath = getServiceConfigurationPath(repositoryPath, label.toString());
            File componentRepositoryConfigurationFile = new File(componentRepositoryConfigurationPath);
            searchServiceConfigurationBasedOnLabel(namedObject, componentRepositoryConfigurationFile, serviceGUID, serviceVersion, label,
                    toReturn, retrieveOnlyMetaData);
        } else {
            for (Label label1 : Label.values()) {
                componentRepositoryConfigurationPath = getServiceConfigurationPath(repositoryPath, label1.toString());
                File componentRepositoryConfigurationFile = new File(componentRepositoryConfigurationPath);
                searchServiceConfigurationBasedOnLabel(namedObject, componentRepositoryConfigurationFile, serviceGUID, serviceVersion, label1,
                        toReturn, retrieveOnlyMetaData);
            }
        }
        //For back-word compatibility
        componentRepositoryConfigurationPath = getServiceConfigurationPathWithOutLabel(repositoryPath);
        searchServiceConfigurationBasedOnLabel(namedObject, new File(componentRepositoryConfigurationPath), serviceGUID, serviceVersion,  null, toReturn,
                retrieveOnlyMetaData);

        return toReturn;
    }

    private static  void searchServiceConfigurationBasedOnLabel(NamedObject namedObject, File componentRepositoryConfigurationFile,
                                                                String serviceGUID, String serviceVersion, Label label, List<DataObject> toReturn,
                                                                boolean retrieveOnlyMetaData) throws IOException,FioranoException{
        if (componentRepositoryConfigurationFile.exists()) {
            File[] components = componentRepositoryConfigurationFile.listFiles();
            if (components != null) {
                for(File component : components){
                    if(!component.isDirectory())
                        continue;

                    String componentGUID = component.getName();
                    if(serviceGUID == null || serviceGUID.equals(componentGUID)){
                        File[] componentVersionDirs = component.listFiles();
                        if (componentVersionDirs != null) {
                            for(File componentVersionDir : componentVersionDirs){
                                if(!componentVersionDir.isDirectory())
                                    continue;

                                String componentVersion = componentVersionDir.getName();
                                final float v;
                                try {
                                    v = Float.parseFloat(componentVersion);
                                } catch (NumberFormatException e) {
                                    //Ignore as for back word compatibility
                                    break;
                                }
                                if(serviceVersion == null || (Float.parseFloat(serviceVersion) == v)){
                                    String configurationPath = componentVersionDir.getCanonicalPath();
                                    toReturn.addAll(searchServiceConfigurations(configurationPath, namedObject, componentGUID, componentVersion,
                                            label, retrieveOnlyMetaData));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static  ArrayList<DataObject> searchServiceConfigurations(String configurationPath, NamedObject namedObject, String componentGUID,
                                                                      String componentVersion, Label label, boolean retrieveOnlyMetaData) throws IOException,
            FioranoException {
        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();

        String objectName = namedObject.getName();
        File configurationDir = new File(configurationPath);
        if(configurationDir.exists()){
            File[] persistedConfigurations = configurationDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return !name.equals(ConfigurationRepoConstants.METADATA_XML);
                }
            });

            for(File persistedConfiguration : persistedConfigurations){
                String configurationName = persistedConfiguration.getName();
                if(objectName == null || objectName.equals(configurationName)){
                    ServiceConfigurationDataObject dataObject = new ServiceConfigurationDataObject();
                    boolean matches = matchAndFillAttributes(persistedConfiguration, namedObject, configurationDir + separator + ConfigurationRepoConstants.METADATA_XML, dataObject, retrieveOnlyMetaData);

                    if(matches){
                        dataObject.setServiceGUID(componentGUID);
                        dataObject.setServiceVersion(componentVersion);
                        if ( label != null)
                            dataObject.setLabel(label);
                        toReturn.add(dataObject);
                    }
                }
            }
        }

        return toReturn;
    }

    public static  ArrayList<DataObject> getDataConfigurations(NamedObject namedObject, String repoName, int dataObjectType, ObjectCategory dataObjectCategory,
                                                               boolean retrieveOnlyMetaData, String repositoryPath) throws IOException, FioranoException {
        final Label label = namedObject.getLabel();
        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();
        if (label != null && !label.toString().equals("")) {
            String configurationPath = getConfigurationPath(repositoryPath, repoName, label.toString());
            toReturn.addAll(searchDataConfigurations(configurationPath, namedObject, dataObjectType, dataObjectCategory, label, retrieveOnlyMetaData));
        } else {
            for (Label label1 : Label.values()){
                String configurationPath = getConfigurationPath(repositoryPath, repoName, label1.toString());
                toReturn.addAll(searchDataConfigurations(configurationPath, namedObject, dataObjectType, dataObjectCategory, label1, retrieveOnlyMetaData));
            }
        }
        String configurationPath = getConfigurationPathWithOutLabel(repositoryPath, repoName);
        toReturn.addAll(searchDataConfigurations(configurationPath, namedObject, dataObjectType, dataObjectCategory, null, retrieveOnlyMetaData));
        return toReturn;

    }

    public static ArrayList<DataObject> getApplicationConfigurations(Application application, boolean retrieveOnlyMetaData, String repositoryPath,
                                                                     ArrayList<DataObject> returnList)throws FioranoException{
        HashSet<String> resourceConfigurations = new HashSet<String>();
        HashSet<String> serviceConfigurations = new HashSet<String>();
        HashSet<String> portConfigurations = new HashSet<String>();
        HashSet<String> workflowConfigurations = new HashSet<String>();
        HashSet<String> messageFilterConfigurations = new HashSet<String>();
        HashSet<String> destinationConfigurations = new HashSet<String>();
        HashSet<String> transformationConfigurations = new HashSet<String>();
        HashSet<String> routeConfigurations = new HashSet<String>();
        HashSet<String> selectorConfigurations = new HashSet<String>();
        HashSet<String> connectionFactoryConfigurations = new HashSet<String>();
        HashSet<String> runtimeArgumentsConfigurations = new HashSet<String>();

        ApplicationContext context = application.getApplicationContext();
        if (context != null) {
            String appContextConfigName = context.getConfigName();
            if (appContextConfigName != null) {
                ResourceConfigurationNamedObject namedObject = new ResourceConfigurationNamedObject();
                namedObject.setName(appContextConfigName);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, resourceConfigurations);
            }
        }

        List<ServiceInstance> services = application.getServiceInstances();
        for (ServiceInstance service : services) {
            ArrayList<NamedConfigurationProperty> serviceNamedConfigurations = service.getNamedConfigurations();
            if (serviceNamedConfigurations != null && serviceNamedConfigurations.size() > 0) {
                for (NamedConfigurationProperty configurationProperty : serviceNamedConfigurations) {
                    NamedObject namedObject = new NamedObject();
                    String configurationName = configurationProperty.getConfigurationName();
                    switch (ObjectCategory.getObjectCategory(configurationProperty.getConfigurationType())) {
                        case SERVICE_CONFIGURATION:
                            namedObject = new ServiceConfigurationNamedObject();
                            ((ServiceConfigurationNamedObject) namedObject).setServiceGUID(service.getGUID());
                            ((ServiceConfigurationNamedObject) namedObject).setServiceVersion(String.valueOf(service.getVersion()));
                            break;
                        case RESOURCE_CONFIGURATION:
                            namedObject = new ResourceConfigurationNamedObject();
                            break;
                        case TRANSFORMATION:
                            namedObject = new TransformationConfigurationNamedObject();
                            break;
                    }
                    namedObject.setName(configurationName);
                    ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                    addToReturnList(searchedConfigurations, returnList, serviceConfigurations);
                }
            }

            String connectionFactoryConfigName = service.getConnectionFactoryConfigName();
            if (connectionFactoryConfigName != null) {
                NamedObject namedObject = new NamedObject();
                namedObject.setName(connectionFactoryConfigName);
                namedObject.setObjectCategory(ObjectCategory.CONNECTION_FACTORY_CONFIGURATION);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, connectionFactoryConfigurations);
            }

            String runtimeArgumentsConfigName = service.getRuntimeArgumentsConfigName();
            if (runtimeArgumentsConfigName != null) {
                NamedObject namedObject = new NamedObject();
                namedObject.setName(runtimeArgumentsConfigName);
                namedObject.setObjectCategory(ObjectCategory.RUNTIME_ARG_CONFIGURATION);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, runtimeArgumentsConfigurations);
            }

            List<PortInstance> ports = new ArrayList<PortInstance>();
            ports.addAll(service.getInputPortInstances());
            ports.addAll(service.getOutputPortInstances());
            for (PortInstance port : ports) {
                String schemaConfigName = (port.getSchema() == null) ? null : port.getSchema().getConfigName();
                if (schemaConfigName != null) {
                    ResourceConfigurationNamedObject namedObject = new ResourceConfigurationNamedObject();
                    namedObject.setName(schemaConfigName);
                    ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                    addToReturnList(searchedConfigurations, returnList, resourceConfigurations);

                    Map schemaRefMap = port.getSchema().getSchemaRefConfigs();
                    for (Object obj : schemaRefMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) obj;
                        String schemaRefConfigName = (String) entry.getValue();
                        if (schemaRefConfigName != null) {
                            ResourceConfigurationNamedObject schemaRefNamedObject = new ResourceConfigurationNamedObject();
                            schemaRefNamedObject.setName(schemaRefConfigName);
                            ArrayList<DataObject> schemaRefSearchedConfigurations = getApplicationConfigurations(schemaRefNamedObject, retrieveOnlyMetaData, repositoryPath);
                            addToReturnList(schemaRefSearchedConfigurations, returnList, resourceConfigurations);
                        }
                    }
                }

                String workflowConfigName = port.getWorkflowConfigName();
                if (workflowConfigName != null) {
                    NamedObject namedObject = new NamedObject();
                    namedObject.setName(workflowConfigName);
                    namedObject.setObjectCategory(ObjectCategory.WORKFLOW);
                    ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                    addToReturnList(searchedConfigurations, returnList, workflowConfigurations);
                }

                String messageFilterConfigName = port.getMessageFilterConfigName();
                if (messageFilterConfigName != null) {
                    NamedObject namedObject = new NamedObject();
                    namedObject.setName(messageFilterConfigName);
                    namedObject.setObjectCategory(ObjectCategory.MESSAGEFILTERS);
                    ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                    addToReturnList(searchedConfigurations, returnList, messageFilterConfigurations);
                }

                String destinationConfigName = port.getDestinationConfigName();
                if (destinationConfigName != null) {
                    NamedObject namedObject = new NamedObject();
                    namedObject.setName(destinationConfigName);
                    namedObject.setObjectCategory(ObjectCategory.DESTINATION);
                    ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                    addToReturnList(searchedConfigurations, returnList, destinationConfigurations);
                }

                if (port instanceof InputPortInstance) {
                    InputPortInstance inputPortInstance = (InputPortInstance) port;
                    String subscriberConfigName = inputPortInstance.getSubscriberConfigName();
                    if (subscriberConfigName != null) {
                        PortConfigurationNamedObject namedObject = new PortConfigurationNamedObject();
                        namedObject.setName(subscriberConfigName);
                        namedObject.setPortType(PortType.INPUT);
                        namedObject.setDestinationType(port.getDestinationType() == PortInstance.DESTINATION_TYPE_QUEUE ? DestinationType.QUEUE : DestinationType.TOPIC);
                        ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                        addToReturnList(searchedConfigurations, returnList, portConfigurations);
                    }
                }

                if (port instanceof OutputPortInstance) {
                    OutputPortInstance outputPortInstance = (OutputPortInstance) port;
                    Transformation transformation = outputPortInstance.getApplicationContextTransformation();

                    String transformationConfigName = null;
                    if (transformation != null)
                        transformationConfigName = transformation.getTransformationConfigName();

                    String publisherConfigName = outputPortInstance.getPublisherConfigName();
                    if (publisherConfigName != null) {
                        PortConfigurationNamedObject namedObject = new PortConfigurationNamedObject();
                        namedObject.setName(publisherConfigName);
                        namedObject.setPortType(PortType.OUTPUT);
                        namedObject.setDestinationType(port.getDestinationType() == PortInstance.DESTINATION_TYPE_QUEUE ? DestinationType.QUEUE : DestinationType.TOPIC);
                        ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                        addToReturnList(searchedConfigurations, returnList, portConfigurations);
                    }

                    if (transformationConfigName != null) {
                        TransformationConfigurationNamedObject namedObject = new TransformationConfigurationNamedObject();
                        namedObject.setName(transformationConfigName);
                        ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                        addToReturnList(searchedConfigurations, returnList, transformationConfigurations);
                    }
                }
            }
        }

        List<Route> routes = application.getRoutes();
        for (Route route : routes) {
            String messagingConfigName = route.getMessagingConfigName();
            String selectorConfigName = route.getSelectorConfigName();
            String transformationConfigName = null;
            MessageTransformation messageTransformation = route.getMessageTransformation();

            if (messageTransformation != null)
                transformationConfigName = messageTransformation.getTransformationConfigName();

            if (messagingConfigName != null) {
                NamedObject namedObject = new NamedObject();
                namedObject.setName(messagingConfigName);
                namedObject.setObjectCategory(ObjectCategory.ROUTE);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, routeConfigurations);
            }

            if (selectorConfigName != null) {
                NamedObject namedObject = new NamedObject();
                namedObject.setName(selectorConfigName);
                namedObject.setObjectCategory(ObjectCategory.SELECTOR);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, selectorConfigurations);
            }

            if (transformationConfigName != null) {
                TransformationConfigurationNamedObject namedObject = new TransformationConfigurationNamedObject();
                namedObject.setName(transformationConfigName);
                ArrayList<DataObject> searchedConfigurations = getApplicationConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
                addToReturnList(searchedConfigurations, returnList, transformationConfigurations);
            }
        }

        return returnList;
    }

    private static ArrayList<DataObject> getApplicationConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData, String repositoryPath) throws FioranoException {
        try {
            return getConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath);
        }  catch (Exception ignore) {
            return new ArrayList<DataObject>();
        }
    }

    private static void addToReturnList(ArrayList<DataObject> searchedConfigurations, ArrayList<DataObject> returnList, HashSet<String> checkSet) {
        if(searchedConfigurations == null)         //
            return;

        for(DataObject searchedConfiguration : searchedConfigurations) {
            String configurationName = searchedConfiguration.getName();
            String label= searchedConfiguration.getLabel().toString().toUpperCase();
            if(!checkSet.contains(configurationName+"__"+label)) {
                returnList.add(searchedConfiguration);
                checkSet.add(configurationName+"__"+label);
            }
        }
    }

    private static  ArrayList<DataObject> searchDataConfigurations(String configurationPath, NamedObject namedObject, int dataObjectType,
                                                                   final ObjectCategory dataObjectCategory, Label label, boolean retrieveOnlyMetaData) throws IOException,
            FioranoException {
        ArrayList<DataObject> toReturn = new ArrayList<DataObject>();

        String objectName = namedObject.getName();
        File configurationDir = new File(configurationPath);
        if(configurationDir.exists()){
            File[] persistedConfigurations = configurationDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (ObjectCategory.TRANSFORMATION.equals(dataObjectCategory)) {
                        File configFile = new File(dir, name);
                        boolean isTransformationDir = configFile.isDirectory() && (new File(configFile, "script.xml").exists() || new File(configFile, "jmsScript.xml").exists());
                        return isTransformationDir;
                    } else {
                        return (!name.equals(ConfigurationRepoConstants.METADATA_XML) && !new File(dir, name).isDirectory());
                    }
                }
            });

            for(File persistedConfiguration : persistedConfigurations){
                String configurationName = persistedConfiguration.getName();
                if(objectName == null || objectName.equals(configurationName)){
                    DataObject dataObject;
                        dataObject = (DataObject) ConfigurationDataHelper.getDataModelObject(dataObjectType);

                    boolean matches = matchAndFillAttributes(persistedConfiguration, namedObject, configurationDir + separator + ConfigurationRepoConstants.METADATA_XML, dataObject, retrieveOnlyMetaData);
                    if(matches && dataObject != null){
                        dataObject.setObjectCategory(dataObjectCategory);
                        if ( label != null)
                            dataObject.setLabel(label);
                        toReturn.add(dataObject);
                    }
                }
            }
        }

        return toReturn;
    }

    private static  Hashtable getConfigurationParams(String metaDataFilePath, String transformationName) throws FileNotFoundException, FioranoException {
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
                        if (configName != null && configName.equals(transformationName))
                            return parser.getAttributes();
                        parser.resetCursor();
                    }
                }
            } else {
                // for backward compatibility - Reading metadata saved by eStudio.
                if (parser.markCursor(ConfigurationRepoConstants.METADATA)) {
                    while (parser.nextElement()) {
                        String elemName = parser.getLocalName();
                        if (ConfigurationRepoConstants.ELEM_CONFIGURATION.equals(elemName)) {
                            parser.markCursor(elemName);
                            String configName = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_NAME);
                            if (configName != null && configName.equals(transformationName)) {
                                Hashtable params = new Hashtable();
                                String label = parser.getAttributeValue(null, ConfigurationRepoConstants.ATTR_LABEL);
                                if (label != null)
                                    params.put(ConfigurationRepoConstants.ATTR_LABEL, label.toLowerCase());
                                while (parser.nextElement()) {
                                    if("params".equals(parser.getLocalName())) {
                                        parser.markCursor("params");
                                        String key = null;
                                        String value = null;
                                        while(parser.nextElement()){
                                            String elementName = parser.getLocalName();
                                            if(ConfigurationRepoConstants.ELEM_KEY.equalsIgnoreCase(elementName)){
                                                key = parser.getTextContent();
                                            } else if (ConfigurationRepoConstants.ELEM_VALUE.equalsIgnoreCase(elementName)) {
                                                value = parser.getTextContent();
                                            }
                                        }
                                        parser.resetCursor();
                                        if (key != null && value != null) {
                                            params.put(key,value);
                                        }
                                    }
                                }
                                return params;
                            }
                            parser.resetCursor();
                        }
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

    /*---------------------------------------------------------------------------------------------------------------------------------------------------- */
    /*------------------------------------------------------MetaData file Handling-------------------------------------------------------------------------*/

    private static  void createMetaInfFile(File metaInfFile) throws FioranoException, IOException {
        XMLStreamWriter writer = null;
        OutputStream outputStream = null;
        try {
            XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
            metaInfFile.getParentFile().mkdirs();
            outputStream = new FileOutputStream(metaInfFile);
            writer = outputFactory.createXMLStreamWriter(outputStream);
            writer.writeStartDocument();
            writer.writeStartElement(CONFIGURATIONS_DIR);
            writer.writeEndElement();
            writer.writeEndDocument();
        } catch (IOException e) {
            throw new FioranoException("ERROR_CREATE_META_INF", e);
        } catch (XMLStreamException e) {
            throw new FioranoException("ERROR_CREATE_META_INF", e);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                //Ignore
            }
        }
    }

    private static  void deleteElementFromMetaDataFile(String metaDataFilePath, String configurationName, ObjectCategory objectCategory) throws IOException,
            FioranoException {
        //All logging done at higher levels.
        XMLStreamWriter writer = null;
        FioranoStaxParser parser = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        File tempFile = null;
        File metaInfFile = null;
        boolean exceptionOccurred = false;
        try{
            boolean configurationPresent = false;
            metaInfFile = new File(metaDataFilePath);
            if(!metaInfFile.exists())
                return;

            inputStream = new FileInputStream(metaInfFile);
            parser = new FioranoStaxParser(inputStream);

            tempFile = new File(metaInfFile.getParent() + separator + "" + System.currentTimeMillis());
            tempFile.createNewFile();
            XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
            outputStream = new FileOutputStream(tempFile);
            writer = outputFactory.createXMLStreamWriter(outputStream);
            writer.writeStartDocument();
            writer.writeStartElement(CONFIGURATIONS_DIR);

            parser.markCursor(CONFIGURATIONS_DIR);
            while(parser.nextElement()){
                parser.markCursor(ELEM_CONFIGURATION);

                String name = parser.getAttributeValue(null, ATTR_NAME);
                String dataType = parser.getAttributeValue(null, ATTR_DATA_TYPE);
                String label = parser.getAttributeValue(null, ATTR_LABEL);

                if(name.equals(configurationName)){
                    configurationPresent = true;
                    parser.resetCursor();
                    continue;
                }

                writer.writeStartElement(ELEM_CONFIGURATION);
                writer.writeAttribute(ATTR_NAME, name);
                writer.writeAttribute(ATTR_DATA_TYPE, dataType != null ? dataType : "");
                writer.writeAttribute(ATTR_LABEL, label != null ? label : "");
                writeAdditionalAttributes(objectCategory, parser, writer);
                writer.writeEndElement();

                parser.resetCursor();
            }
            parser.resetCursor();

            writer.writeEndElement();
            writer.writeEndDocument();
        } catch(XMLStreamException e){
            exceptionOccurred = true;
            throw new FioranoException("ERROR_UPDATE_META_INF_DELETE_ELEMENT", e);
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(parser != null)
                    parser.close();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                //Ignore
            }

            try {
                if(outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                //Ignore
            }

            if(!exceptionOccurred && tempFile != null && tempFile.exists()){
                if(metaInfFile != null){
                    if(metaInfFile.exists())
                        metaInfFile.delete();

                    tempFile.renameTo(metaInfFile);
                }
            }else if(exceptionOccurred)
                tempFile.delete();
        }
    }

    private static  void writeAdditionalAttributes(DataObject dataObject, XMLStreamWriter writer) throws XMLStreamException {
        ObjectCategory category = dataObject.getObjectCategory();

        switch(category){
            case PORT_CONFIGURATION:
                PortConfigurationDataObject portObject = (PortConfigurationDataObject) dataObject;
                DestinationType destinationType = portObject.getDestinationType();

                if(destinationType != null)
                    writer.writeAttribute(ATTR_DESTINATION_TYPE, destinationType.toString());

                break;
            case SERVICE_CONFIGURATION:
                ServiceConfigurationDataObject serviceObject = (ServiceConfigurationDataObject) dataObject;
                ServiceConfigurationType configurationType = serviceObject.getConfigurationType();

                if(configurationType != null)
                    writer.writeAttribute(ATTR_SERVICE_CONFIGURATION_TYPE, configurationType.toString());

                break;
            case MISCELLANEOUS:
                break;
            case RESOURCE_CONFIGURATION:
                ResourceConfigurationDataObject resourceObject = (ResourceConfigurationDataObject) dataObject;
                String resourceType = resourceObject.getResourceType();

                if(resourceType != null)
                    writer.writeAttribute(ATTR_RESOURCE_TYPE, resourceType);

                break;
            case ROUTE:
                break;
            case SELECTOR:
                break;
            case TRANSFORMATION:
                TransformationConfigurationDataObject transformationObject = (TransformationConfigurationDataObject) dataObject;
                String scriptFileName = transformationObject.getScriptFileName();
                String jmsScriptFileName = transformationObject.getJmsScriptFileName();
                String projectFileName = transformationObject.getProjectFileName();
                String factoryName = transformationObject.getFactoryName();

                if(scriptFileName != null)
                    writer.writeAttribute(ATTR_SCRIPT_FILE_NAME, scriptFileName);
                if(jmsScriptFileName != null)
                    writer.writeAttribute(ATTR_JMS_SCRIPT_FILE_NAME, jmsScriptFileName);
                if(projectFileName != null)
                    writer.writeAttribute(ATTR_PROJECT_NAME, projectFileName);
                if(factoryName != null)
                    writer.writeAttribute(ATTR_FACTORY_NAME, factoryName);

                break;
            case WORKFLOW:
                break;
            case MESSAGEFILTERS:
                break;
            case DESTINATION:
                break;
            case RUNTIME_ARG_CONFIGURATION:
                RuntimeargConfigurationDataObject runtimeargConfigurationDataObject = (RuntimeargConfigurationDataObject) dataObject;
                String serviceGUID = runtimeargConfigurationDataObject.getServiceGUID();
                String serviceVersion = runtimeargConfigurationDataObject.getServiceVersion();

                if(serviceGUID != null)
                    writer.writeAttribute(ATTR_SERVICE_GUID, serviceGUID);
                if(serviceVersion != null)
                    writer.writeAttribute(ATTR_SERVICE_VERSION, serviceVersion);

                break;
            case CONNECTION_FACTORY_CONFIGURATION:
                break;
        }
    }

    private static  void writeAdditionalAttributes(ObjectCategory category, FioranoStaxParser parser, XMLStreamWriter writer) throws XMLStreamException {
        switch(category){
            case PORT_CONFIGURATION:
                String destinationType = parser.getAttributeValue(null, ATTR_DESTINATION_TYPE);

                if(destinationType != null)
                    writer.writeAttribute(ATTR_DESTINATION_TYPE, destinationType);

                break;
            case SERVICE_CONFIGURATION:
                String configurationType = parser.getAttributeValue(null, ATTR_SERVICE_CONFIGURATION_TYPE);

                if(configurationType != null)
                    writer.writeAttribute(ATTR_SERVICE_CONFIGURATION_TYPE, configurationType);

                break;
            case MISCELLANEOUS:
                break;
            case RESOURCE_CONFIGURATION:
                String resourceType = parser.getAttributeValue(null, ATTR_RESOURCE_TYPE);

                if(resourceType != null)
                    writer.writeAttribute(ATTR_RESOURCE_TYPE, resourceType);

                break;
            case ROUTE:
                break;
            case SELECTOR:
                break;
            case TRANSFORMATION:
                String scriptFileName = parser.getAttributeValue(null, ATTR_SCRIPT_FILE_NAME);
                String jmsScriptFileName = parser.getAttributeValue(null, ATTR_JMS_SCRIPT_FILE_NAME);
                String projectFileName = parser.getAttributeValue(null, ATTR_PROJECT_NAME);
                String factoryName = parser.getAttributeValue(null, ATTR_FACTORY_NAME);

                if(scriptFileName != null)
                    writer.writeAttribute(ATTR_SCRIPT_FILE_NAME, scriptFileName);
                if(jmsScriptFileName != null)
                    writer.writeAttribute(ATTR_JMS_SCRIPT_FILE_NAME, jmsScriptFileName);
                if(projectFileName != null)
                    writer.writeAttribute(ATTR_PROJECT_NAME, projectFileName);
                if(factoryName != null)
                    writer.writeAttribute(ATTR_FACTORY_NAME, factoryName);

                break;
            case WORKFLOW:
                break;
            case MESSAGEFILTERS:
                break;
            case DESTINATION:
                break;
            case RUNTIME_ARG_CONFIGURATION:
                String serviceGUID = parser.getAttributeValue(null, ATTR_SERVICE_GUID);
                String serviceVersion = parser.getAttributeValue(null, ATTR_SERVICE_VERSION);

                if(serviceGUID != null)
                    writer.writeAttribute(ATTR_SERVICE_GUID, serviceGUID);
                if(serviceVersion != null)
                    writer.writeAttribute(ATTR_SERVICE_VERSION, serviceVersion);

                break;
            case CONNECTION_FACTORY_CONFIGURATION:
                break;
        }
    }

    private static  void addElementToMetaDataFile(String metaDataFilePath, DataObject dataObject) throws FioranoException, IOException {
        //All logging done at higher levels.
        XMLStreamWriter writer = null;
        FioranoStaxParser parser = null;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        File tempFile = null;
        File metaInfFile = null;
        boolean exceptionOccurred = false;
        try{
            String configurationName = dataObject.getName();

            boolean configurationPresent = false;
            metaInfFile = new File(metaDataFilePath);
            if(!metaInfFile.exists())
                createMetaInfFile(metaInfFile);

            inputStream = new FileInputStream(metaInfFile);
            parser = new FioranoStaxParser(inputStream);

            tempFile = new File(metaInfFile.getParent() + separator + "" + System.currentTimeMillis());
            tempFile.createNewFile();
            XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
            outputStream = new FileOutputStream(tempFile);
            writer = outputFactory.createXMLStreamWriter(outputStream);
            writer.writeStartDocument();
            writer.writeStartElement(CONFIGURATIONS_DIR);

            parser.markCursor(CONFIGURATIONS_DIR);
            while(parser.nextElement()){
                parser.markCursor(ELEM_CONFIGURATION);

                String name = parser.getAttributeValue(null, ATTR_NAME);
                String dataType = parser.getAttributeValue(null, ATTR_DATA_TYPE);
                String label = parser.getAttributeValue(null, ATTR_LABEL);

                if(name.equals(configurationName))
                    configurationPresent = true;

                writer.writeStartElement(ELEM_CONFIGURATION);
                writer.writeAttribute(ATTR_NAME, name);
                writer.writeAttribute(ATTR_DATA_TYPE, dataType != null ? dataType : "");
                writer.writeAttribute(ATTR_LABEL, label != null ? label : "");
                writeAdditionalAttributes(dataObject.getObjectCategory(), parser, writer);
                writer.writeEndElement();

                parser.resetCursor();
            }
            parser.resetCursor();

            if(!configurationPresent){
                writer.writeStartElement(ELEM_CONFIGURATION);
                writer.writeAttribute(ATTR_NAME, configurationName);
                DataType dataType = dataObject.getDataType();
                writer.writeAttribute(ATTR_DATA_TYPE, dataType != null ? dataType.toString() : "");
                Label label = dataObject.getLabel();
                writer.writeAttribute(ATTR_LABEL, label != null ? label.toString() : "");
                writeAdditionalAttributes(dataObject, writer);
                writer.writeEndElement();
            }

            writer.writeEndElement();
            writer.writeEndDocument();
        } catch(XMLStreamException e){
            exceptionOccurred = true;
            throw new FioranoException("ERROR_UPDATE_META_INF_ADD_ELEMENT", e);
        } finally {
            try {
                if(writer != null)
                    writer.close();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(parser != null)
                    parser.close();
            } catch (XMLStreamException e) {
                //Ignore
            }

            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                //Ignore
            }

            try {
                if(outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                //Ignore
            }

            if(!exceptionOccurred && tempFile != null && tempFile.exists()){
                if(metaInfFile != null){
                    if(metaInfFile.exists())
                        metaInfFile.delete();

                    tempFile.renameTo(metaInfFile);
                }
            }else if(exceptionOccurred)
                tempFile.delete();
        }
    }


    /*------------------------------------------------------------------------------------------------------------------------------------------------------*/



    public static  File getTempFile(String name, String ext) {
        return FileUtil.findFreeFile(FileUtil.TEMP_DIR, name, ext);
    }

    public static  String ensureDecimalPoint(float version) {
        String originalValue = String.valueOf(version);
        if(!originalValue.contains(""))
            originalValue = originalValue + "";

        return originalValue;
    }

    public static  String ensureDecimalPoint(String version) {
        if (version == null)
            return null;

        if(!version.contains(""))
            version = version + "";

        return version;
    }

    private static  void unzip(byte[] data, File targetDir) throws FioranoException {
        boolean successfulzip = true;
        File tempZipFile = null;
        FileOutputStream outstream = null;

        try {
            tempZipFile = getTempFile("", "");
            outstream = new FileOutputStream(tempZipFile);
            outstream.write(data);
        } catch (IOException ioe) {
            successfulzip = false;
            throw new FioranoException("UNABLE_TO_CREATE_CONFIGURATION_ZIPFILE", ioe);
        } finally {
            try {
                if (outstream != null)
                    outstream.close();

                if (!successfulzip && tempZipFile != null)
                    tempZipFile.delete();
            } catch (IOException e) {
                //Ignore
            }
        }

        //Extract contents.
        boolean successfulExtract = true;
        try {
            if(!targetDir.exists())
                targetDir.mkdirs();

            ZipUtil.unzip(tempZipFile, targetDir);
        } catch (Exception e) {
            successfulExtract = false;
            throw new FioranoException("ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_CONFIGURATION", e);
        } finally {
            //Deleting the file
            if (!successfulExtract && targetDir != null)
                FileUtil.deleteDir(targetDir);

            //Delete temporary zip file created
            tempZipFile.delete();
        }
    }

}

