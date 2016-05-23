/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.RBUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.*;

public class ApplicationRepository {

    String applicationRepoPath;

    private Logger logger = LoggerFactory.getLogger(Activator.class);

    public ApplicationRepository() {
        applicationRepoPath =ServerConfig.getConfig().getRepositoryPath() + File.separator + "applications";
    }

    public Application readApplication(String appGuid, String version) {
        try {
            return ApplicationParser.readApplication(new File(applicationRepoPath + File.separator + appGuid + File.separator + version), false);
        } catch (FioranoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getApplicationRepoPath() {
        return applicationRepoPath;
    }

    public boolean applicationExists(String appGuid, float version) throws FioranoException {
        if (appGuid == null)
            throw new FioranoException("APPLICATION_GUID_NULL");

        try {
            File applicationFile = getAppDir(appGuid, version);
            return applicationFile != null && applicationFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveApplication(Application application, File tempAppFolder, String userName, byte[] zippedContents, String handleID) throws FioranoException {
        String appGUID = application.getGUID().toUpperCase();
        float versionNumber = application.getVersion();
        logger.info("Saving Application: " +appGUID+":"+versionNumber);
        File tempAppFolderObject = null;
        try {
            float version = application.getVersion();
            File applicationFolder = new File(getAppRootDirectory(appGUID, version));

            if (!applicationFolder.exists() && !applicationFolder.isDirectory())
                applicationFolder.mkdirs();
            else {
                //Remove redundant config files
                File configFolder = new File(getAppRootDirectory(appGUID, version) + File.separator + "config");
                if (configFolder.exists()) {
                    List<ServiceInstance> serviceInstances = application.getServiceInstances();
                    for (ServiceInstance instance : serviceInstances) {
                        String configuration = instance.getConfiguration();
                        String fileName = instance.getName() + ".xml";
                        File configFile = new File(getConfigurationFileName(appGUID, version, fileName));
                        if (configuration == null) {
                            if (configFile.exists())
                                configFile.delete();
                        }

                        //Remove redundant output port transformation files
                        List<OutputPortInstance> outputPortInstances = instance.getOutputPortInstances();
                        for (OutputPortInstance port : outputPortInstances) {
                            Transformation transformation = port.getApplicationContextTransformation();

                            String script = transformation != null ? transformation.getScript() : null;
                            String scriptFileName = "script" + ".xml";
                            File scriptFile = new File(getPortTransformationFileName(appGUID, instance.getName(), port.getName(), version, scriptFileName));
                            if (script == null && scriptFile.exists())
                                scriptFile.delete();

                            String project = transformation != null ? transformation.getProject() : null;
                            String projectFileName = "project" + ".fmp";
                            File projectFile = new File(getPortTransformationFileName(appGUID, instance.getName(), port.getName(), version, projectFileName));
                            if (project == null) {
                                if (projectFile.exists())
                                    projectFile.delete();

                                File projectDir = new File(getPortTransformationDirName(appGUID, instance.getName(), port.getName(), version));
                                if (projectDir.exists())
                                    projectDir.delete();
                            } else if (!new File(getPortTransformationDir(tempAppFolder.getAbsolutePath(), instance.getName(), port.getName()) + "project" + ".fmp").exists()) {
                                //If new transformation project is not in single file format, delete the already exisiting single file.
                                if (projectFile.exists())
                                    projectFile.delete();
                            }

                            File portTransformationDir = new File(getPortTransformationDirName(appGUID, instance.getName(), port.getName(), version));
                            if (portTransformationDir.exists() && portTransformationDir.list().length == 0)
                                portTransformationDir.delete();
                        }
                    }

                    for (Route route : application.getRoutes()) {
                        MessageTransformation messageTransformation = route.getMessageTransformation();

                        String script = messageTransformation != null ? messageTransformation.getScript() : null;
                        String scriptFileName = "script" + ".xml";
                        File scriptFile = new File(getRouteTransformationFileName(appGUID, route.getName(), version, scriptFileName));
                        if (script == null && scriptFile.exists())
                            scriptFile.delete();

                        String jmsScript = messageTransformation != null ? messageTransformation.getJMSScript() : null;
                        String jmsScriptFileName = "jmsScript" + ".xml";
                        File jmsScriptFile = new File(getRouteTransformationFileName(appGUID, route.getName(), version, jmsScriptFileName));
                        if (jmsScript == null && jmsScriptFile.exists())
                            jmsScriptFile.delete();

                        String project = messageTransformation != null ? messageTransformation.getProject() : null;
                        String projectFileName = "project" + ".fmp";
                        File projectFile = new File(getRouteTransformationFileName(appGUID, route.getName(), version, projectFileName));
                        if (project == null) {
                            if (projectFile.exists())
                                projectFile.delete();

                            File projectDir = new File(getRouteTransformationDirName(appGUID, route.getName(), version));
                            if (projectDir.exists())
                                projectDir.delete();
                        } else if (!new File(getRouteTransformationDir(tempAppFolder.getAbsolutePath(), route.getName()) + "project" + ".fmp").exists()) {
                            //If new transformation project is not in single file format, delete the already exisiting single file.
                            if (projectFile.exists())
                                projectFile.delete();
                        }

                        File routeTransformationDir = new File(getRouteTransformationDirName(appGUID, route.getName(), version));
                        if (routeTransformationDir.exists() && routeTransformationDir.list().length == 0)
                            routeTransformationDir.delete();
                    }
                }

                //Remove redundant schema files
                File schemaFolder = new File(getAppRootDirectory(appGUID, version) + File.separator + "schemas");
                if (schemaFolder.exists()) {
                    File[] children = schemaFolder.listFiles();
                    for (int i = 0; i < children.length; i++) {
                        children[i].delete();
                    }
                }
            }

            File eventProcessXMLFile = null;
            try {

                //As we are blindly copying the Event Process zip sent by tool (eStudio), we should parse the EventProcess.xml and write it again
                //This is being done to write schema-version element correctly into the EventProcess.xml
                Application application2 = ApplicationParser.readApplication(tempAppFolder, null, false);

                ApplicationParser.checkAndSetNamedConfigurationIdentifiers(application2);
                application.setSchemaVersion(application2.getSchemaVersion());
                boolean namedConfigurationUsed = application2.getSchemaVersion().equals(ApplicationParser.NAMED_CONFIGURATION_SCHEMA_VERSION);
                application2.setUserName(userName);

                eventProcessXMLFile = new File(tempAppFolder.getAbsolutePath() + File.separator + ApplicationParser.EVENT_PROCESS_XML);
                application2.toXMLString(new FileOutputStream(eventProcessXMLFile), false);
                FileUtil.copyDirectory(tempAppFolder, applicationFolder);
            } finally {

            }
        } catch (FioranoException e) {
            throw e;
        } catch (Exception e) {
            throw new FioranoException(e);

        } finally {
            try {
                FileUtil.deleteDir(tempAppFolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("Saved Application: " +appGUID+":"+versionNumber);
    }

    public void saveApplication(Application application, String userName, String handleIDForEstudio, boolean skipManagableProps)
            throws FioranoException {
        // If applicationPropertySheet is null throw an Exception
        if (application == null)
            throw new FioranoException("ERROR_APPLICATION_SAVE_FAILURE_ERROR");
        logger.info("Saving Application: "+application.getGUID()+":"+application.getVersion());

        File applicationFolder;

        try {
            applicationFolder = new File(getAppRootDirectory(application.getGUID(), application.getVersion()));
            if(!applicationFolder.exists())
                applicationFolder.mkdirs();
            ApplicationParser.writeApplication(application, applicationFolder, skipManagableProps);
        } catch (Exception e) {
            throw new FioranoException("ERROR_APPLICATION_SAVE_FAILURE_ERROR",e);
            //"Exception in writing the application", e);
        }
        logger.info("Saved Application: "+application.getGUID()+":"+application.getVersion());
    }

    private String getConfigurationFileName(String appGUID, float version, String fileName) {
        return applicationRepoPath + File.separator + appGUID.toUpperCase() + File.separator + version + File.separator + "config" + File.separator + fileName;
    }

    private String getAppRootDirectory(String appGUID, float version) {
        return applicationRepoPath + File.separator + appGUID.toUpperCase() + File.separator + version;
    }


    private String getRouteTransformationFileName(String appGUID, String subDir, float version, String fileName) {
        return getRouteTransformationDirName(appGUID, subDir, version) + fileName;
    }

    private String getRouteTransformationDirName(String appGUID, String subDir, float version) {
        return applicationRepoPath + File.separator + appGUID.toUpperCase() + File.separator + version + File.separator + "transformations" + File.separator + "routes" + File.separator + (subDir != null ? subDir + File.separator : "");
    }

    private String getRouteTransformationDir(String applicationFolderPath, String subDir) {
        return applicationFolderPath + File.separator + "transformations" + File.separator + "routes" + File.separator + (subDir != null ? subDir + File.separator : "");
    }

    private String getPortTransformationFileName(String appGUID, String serviceInstanceName, String portName, float version, String fileName) {
        return getPortTransformationDirName(appGUID, serviceInstanceName, portName, version) + fileName;
    }

    private String getPortTransformationDirName(String appGUID, String serviceInstanceName, String portName, float version) {
        return applicationRepoPath + File.separator + appGUID.toUpperCase() + File.separator + version + File.separator + "transformations" + File.separator + "ports" + File.separator + serviceInstanceName + File.separator + (portName != null ? portName + File.separator : "");
    }

    private String getPortTransformationDir(String applicationFolderPath, String serviceInstanceName, String portName) {
        return applicationFolderPath + File.separator + "transformations" + File.separator + "ports" + File.separator + serviceInstanceName + File.separator + (portName != null ? portName + File.separator : "");
    }

    public void deleteApplication(String appGUID, String version) throws FioranoException {
        File f = new File(getAppRootDirectory(appGUID, Float.valueOf(version)));
        if (f.exists()) {
            FileUtil.deleteDir(f);
            if (f.getParentFile().isDirectory() || f.getParentFile().list().length == 0) {
                f.getParentFile().delete();
            }
        } else {
            throw new FioranoException("Application: " + appGUID + "" + version + " does not exist in the repository");
        }

    }

    public File getAppDir(String appGUID, float version) {
        return new File(getAppRootDirectory(appGUID, version));
    }

    public List<String> getApplicationIdWithVersions() {
        List<String> applicationIdVersions = new ArrayList<>();
        File[] applicationFolders = new File(applicationRepoPath).listFiles();
        if (applicationFolders != null) {
            for (File appFolder : applicationFolders) {
                String applicationGUID = appFolder.getName();
                if(applicationGUID.startsWith(".")){
                    continue;
                }
                File[] versions = appFolder.listFiles();
                if (versions != null) {
                    for (File version : versions) {
                        if(version.getName().startsWith(".")){
                            continue;
                        }
                        applicationIdVersions.add(applicationGUID + ":" + version.getName());
                    }
                }
            }
        }
        return applicationIdVersions;
    }

    public String[] getApplicationIds() {
        logger.trace("getting Applicationids");
        File[] appFolders = new File(applicationRepoPath).listFiles();
        String applicationIds[] = new String[appFolders.length];
        int i = 0;
        for (File f : appFolders) {
            if(appFolders == null || appFolders.length == 0){
                return new String[0];
            }
            applicationIds[i++] = f.getName();
        }
        return applicationIds;
    }

    public float[] getAppVersions(String id) throws FioranoException {
        File appFolder = new File(applicationRepoPath + File.separator + id);
        if (!appFolder.exists()) {
            throw new FioranoException("application does not exists");
        }
        File[] versionFolders = appFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.isHidden()){
                    return false;
                }
                return true;
            }
        });
        int i = 0;
        float[] versionNumbers = new float[0];

        if (versionFolders != null) {
            versionNumbers = new float[versionFolders.length];
            for (File f : versionFolders) {
                versionNumbers[i++] = Float.valueOf(f.getName());
            }
        }
        return versionNumbers;
    }

    public Application getApplicationPropertySheet(String appGUID, float versionNumber, String label) throws FioranoException {
        return getApplicationPropertySheet(appGUID, versionNumber, label, false);
    }

    public Application getApplicationPropertySheet(String appGUID, float versionNumber, String label, boolean checkForNamedConfigurations) throws FioranoException {
        Application application = null;

        try
        {

            File file = getAppDir(appGUID.toUpperCase(), versionNumber);



            //  IMPORTANT NOTE::
            //  If the file does NOT exist then I need to return NULL insteadof
            //  throwing the exception. This method has been used extensively based
            //  on the assumption that the method will return null if the application
            //  is NOT found. So changing it to throw exception will wreck HAVOC.
            //  Please do NOT change it again.
            if (!file.exists())
                return null;


            if (file.getName().endsWith(ApplicationRepositoryConstants.XML_EXTN)) {
                application = ApplicationParser.readApplication(file);
            }  else {
                if(label == null)
                    application = ApplicationParser.readApplication(file, true, checkForNamedConfigurations);
                else
                    application = ApplicationParser.readApplication(file, label, true, checkForNamedConfigurations);
            }


            // Set the APS field values.
            //application.setFieldValues(new FileInputStream(file.getPath()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
            //LogHelper.getErrMessage(ILogModule.APPLICATION, 8, appGUID, "" + versionNumber, e.toString()), e);
        }
        // Check if the GUID specified inside the property sheet and the GUID obtained is the same.
        // It sometimes might happen that these two are not equal if there is exceptions raised in other methods like rename application.
        if (application != null)
        {
            //LogHelper.getErrMessage(ILogModule.APPLICATION, 9, appGUID, application.getGUID()));
            if (!application.getGUID().equalsIgnoreCase(appGUID))
                throw new FioranoException("Mismatch in Application guid.");
        }

        return application;
    }

    public void deleteConfigs(Application application, Vector<String> components) throws FioranoException{
        for(String component : components){
            ServiceInstance service = application.getServiceInstance(component);

            String configFolderPath = getAppRootDirectory(application.getGUID(), application.getVersion()) + File.separator + "config";
            String child = service.getConfigFile();
            if(child != null){
                File configFile = new File(configFolderPath, child);
                if(configFile.exists())
                    configFile.delete();
            }

            String transformationsFolderPath = getPortTransformationDir(getAppRootDirectory(application.getGUID(), application.getVersion()), service.getName(), null);
            File toDel = new File(transformationsFolderPath);
            if(toDel.exists())//delete if present
                FileUtil.deleteDir(toDel);
        }
    }

    public void deleteRouteConfigs(Application application, Vector<String> routes) throws FioranoException {
        for(Route route : application.getRoutes()) {
            if(routes.contains(route.getName())) {
                String transformationsFolderPath = getRouteTransformationDir(getAppRootDirectory(application.getGUID(), application.getVersion()), route.getName());
                File toDel = new File(transformationsFolderPath);
                if(toDel.exists())//delete if present
                    FileUtil.deleteDir(toDel);
            }
        }
    }

    public void deletePortTransformations(Application application, HashMap<String, String> deletedPorts) throws FioranoException {
        if(deletedPorts == null)
            return;

        List<ServiceInstance> serviceInstances = application.getServiceInstances();
        for(ServiceInstance serviceInstance : serviceInstances) {
            if(deletedPorts.containsKey(serviceInstance.getName())) {
                String transformationsFolderPath = getPortTransformationDir(getAppRootDirectory(application.getGUID(), application.getVersion()), serviceInstance.getName(), deletedPorts.get(serviceInstance.getName()));
                File toDel = new File(transformationsFolderPath);
                if(toDel.exists())//delete if present
                    FileUtil.deleteDir(toDel);
            }
        }
    }
}
