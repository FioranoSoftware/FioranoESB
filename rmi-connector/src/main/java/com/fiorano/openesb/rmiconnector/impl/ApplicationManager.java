/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.application.aps.ApplicationStateDetails;
import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.applicationcontroller.ApplicationHandle;
import com.fiorano.openesb.applicationcontroller.ApplicationLogManager;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.namedconfig.NamedConfigurationUtil;
import com.fiorano.openesb.rmiconnector.Activator;
import com.fiorano.openesb.rmiconnector.api.*;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.config.ServiceConfigurationParser;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.zip.ZipOutputStream;

public class ApplicationManager extends AbstractRmiManager implements IApplicationManager {
    private ApplicationController applicationController;

    private ApplicationLogManager applicationLogManager;

    private ApplicationRepository applicationRepository;

    private MicroServiceRepoManager microServiceRepoManager;

    private IApplicationManager clientProxyInstance;

    private HashMap<String, File> tempFileNameMap = new HashMap<>(8);

    private InstanceHandler handler;

    private Logger logger = LoggerFactory.getLogger(Activator.class);

    void setClientProxyInstance(IApplicationManager clientProxyInstance) {
        this.clientProxyInstance = clientProxyInstance;
    }

    IApplicationManager getClientProxyInstance() {
        return clientProxyInstance;
    }

    ApplicationManager(RmiManager rmiManager, InstanceHandler instanceHandler) {
        super(rmiManager);
        logger.trace("Initializing Application Manager");
        this.applicationController = rmiManager.getApplicationController();
        this.applicationRepository = rmiManager.getApplicationRepository();
        this.applicationLogManager = applicationController.getApplicationLogManager();
        this.microServiceRepoManager = rmiManager.getMicroServiceRepoManager();
        this.handler = instanceHandler;
        setHandleID(instanceHandler.getHandleID());
        logger.trace("Initialized Application Manager");
    }

    @Override
    public String[] getApplicationIds() throws RemoteException, ServiceException {
        return applicationRepository.getApplicationIds();
    }

    @Override
    public boolean exists(String id, float version) throws RemoteException, ServiceException {
        try {
            return applicationRepository.applicationExists(id, version);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public float[] getVersions(String id) throws RemoteException, ServiceException {
        try {
            return applicationRepository.getAppVersions(id);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GET_VERSIONS_OF_EVENTPROCESS, id), e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void saveApplication(byte[] zippedContents, boolean completed) throws RemoteException, ServiceException {
        //Check the validity of the connection
        validateHandleID(handleId, "Save Application");
        String key = handleId + "__SAVEAPP";
        File tempZipFile = null;
        FileOutputStream outstream = null;
        File appFileTempFolder = null;
        boolean successfulzip = true;

        //get the Application zip as byte array from server. keep writing to a zip file until client notifies completed
        try {
            tempZipFile = tempFileNameMap.get(key);
            if (tempZipFile == null) {
                tempZipFile = getTempFile("Application", "zip");
                tempFileNameMap.put(key, tempZipFile);
            }
            outstream = new FileOutputStream(tempZipFile, true);
            outstream.write(zippedContents);
        } catch (IOException ioe) {
            successfulzip = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_APP_ZIPFILE), ioe);
            throw new ServiceException(Bundle.UNABLE_TO_CREATE_APP_ZIPFILE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_APP_ZIPFILE));

        } finally {
            try {
                if (outstream != null) {
                    outstream.close();
                }
                if (!successfulzip) {
                    boolean delete = tempZipFile.delete();
                    if(!delete){
                        logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }
        if (!completed) {
            return;
        }
        logger.trace("Received Application from client.");
        //extract contents.
        boolean successfulextract = true;
        try {
            appFileTempFolder = getTempFile("application", "tmp");
            boolean make = appFileTempFolder.mkdir();
            if(!make){
                logger.trace("Could not create " + appFileTempFolder.getAbsolutePath());
            }
            //extractZip(appFileTempFolder, tempZipFile);
            ZipUtil.unzip(tempZipFile, appFileTempFolder);
        } catch (Exception e) {
            successfulextract = false;
//            LogHelper.log("Unable to save event flow process::Error occured while extracting zipped contents.", e);
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_EVENT_FLOW_PROCESS), e);
            throw new ServiceException(Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_EVENT_FLOW_PROCESS.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_EVENT_FLOW_PROCESS));
        } finally {
            //Removing the temporary zip entry in hashmap. and deleteing the file
            if (!successfulextract && appFileTempFolder != null) {
                FileUtil.deleteDir(appFileTempFolder);
                tempFileNameMap.remove(key);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                }
            }
        }

        //save to fes repository.
        try {
            applicationController.saveApplication(appFileTempFolder, handleId, getBytesFromFile(tempZipFile));
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DEPLOY_EVENT_FLOW_PROCESS, ""), e);
            throw new ServiceException(e.getMessage());
        } finally {
            // Temporaray application Folder is being deleted in Application Repository Code
//            FileUtil.deleteDir(appFileTempFolder);
            tempFileNameMap.remove(key);
            boolean delete = tempZipFile.delete();
            if(!delete){
                logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
            }
        }
    }

    public void changePortAppContext(String appGUID, float appVersion, String serviceName, String portName, String transformerType, byte[] appContextBytes,
                                     boolean completed, String scriptFileName, String jmsScriptFileName, String projectDirName) throws ServiceException, RemoteException {
        if (!isRunning(appGUID, appVersion)) {
            throw new ServiceException("EVENT_PROCESS_NOT_IN_RUNNING_STATE");
        }
        String key;
        File tempZipFile;
        File appContextFileTempFolder = null;
        key = handleId + "__CHANGE_PORT_APPCONTEXT";
        tempZipFile = null;
        FileOutputStream outstream = null;
        boolean successfulzip = true;
        //get the route transformation zip as byte array from server. keep writing to a zip file until client notifies completed
        try {
            tempZipFile = tempFileNameMap.get(key);
            if (tempZipFile == null) {
                tempZipFile = getTempFile("PortAppContext", "zip");
                tempFileNameMap.put(key, tempZipFile);
            }
            outstream = new FileOutputStream(tempZipFile, true);
            outstream.write(appContextBytes);
        } catch (IOException ioe) {
            successfulzip = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_APPCONTEXT_ZIPFILE, portName, serviceName, appGUID, appVersion), ioe);
            throw new ServiceException(Bundle.UNABLE_TO_CREATE_APPCONTEXT_ZIPFILE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_APPCONTEXT_ZIPFILE, portName, serviceName, appGUID, appVersion));
        } finally {
            try {
                if (outstream != null) {
                    outstream.close();
                }
                if (!successfulzip) {
                    boolean delete = tempZipFile.delete();
                    if(!delete){
                        logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }
        if (!completed) {
            return;
        }

        boolean successfulextract = true;
        try {
            //File objects
            appContextFileTempFolder = getTempFile("PortAppContext", "tmp");
            File scriptFile = null;
            File jmsScriptFile = null;
            File projDirFile = null;
            //extract contents.
            boolean make = appContextFileTempFolder.mkdir();
            if(!make){
                logger.trace("Could not create dir " + appContextFileTempFolder.getAbsolutePath());
            }
            ZipUtil.unzip(tempZipFile, appContextFileTempFolder);

            if (projectDirName != null)
                projDirFile = new File(appContextFileTempFolder.getCanonicalPath() + File.separator + projectDirName);
            if (scriptFileName != null)
                scriptFile = new File(appContextFileTempFolder.getCanonicalPath() + File.separator + scriptFileName);
            if (jmsScriptFileName != null)
                jmsScriptFile = new File(appContextFileTempFolder.getCanonicalPath() + File.separator + jmsScriptFileName);

            //checks for existence of file resources.
            if (projectDirName != null && !projDirFile.exists())
                throw new ServiceException("ERROR_CHANGE_APPCONTEXT_TRANSFORMATION2");
            if ((scriptFile != null && !scriptFile.exists()) || (jmsScriptFile != null && !jmsScriptFile.exists()))
                throw new ServiceException("ERROR_CHANGE_APPCONTEXT_TRANSFORMATION2");

            //read the temp folder for the project content
            String projectContent = null;
            String scriptContent = null;
            String jmsScriptContent = null;

            if (projDirFile != null)
                projectContent = ApplicationParser.toXML(projDirFile);

            FileInputStream fis = null;
            if (scriptFile != null) {
                try {
                    fis = new FileInputStream(scriptFile);
                    scriptContent = DmiObject.getContents(fis);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            if (jmsScriptFile != null) {
                try {
                    fis = new FileInputStream(jmsScriptFile);
                    jmsScriptContent = DmiObject.getContents(fis);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException ignore) {
                    }
                }
            }

            Application application;
            try {
                application = applicationController.getApplicationHandle(appGUID, appVersion, handleId).getApplication();
            } catch (Exception e) {
                throw new ServiceException(Bundle.ERROR_APP_HANDLE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_APP_HANDLE, appGUID, appVersion, e.getMessage()));
            }
            for (ServiceInstance serviceInstance : application.getServiceInstances()) {
                if (!serviceInstance.getName().equals(serviceName))
                    continue;
                Transformation transformation = serviceInstance.getOutputPortInstance(portName).getApplicationContextTransformation();
                if (transformation == null && (scriptFileName != null || projectDirName != null)) {
                    if (jmsScriptFileName != null) {
                        transformation = new MessageTransformation();
                        ((MessageTransformation) transformation).setJMSScriptFile(jmsScriptFileName);
                    } else {
                        transformation = new Transformation();
                    }
                    transformation.setScriptFile(scriptFileName);
                    transformation.setProjectFile(projectDirName);

                } else if (transformation != null) {
                    if (transformation instanceof MessageTransformation) {
                        ((MessageTransformation) transformation).setJMSScriptFile(jmsScriptFileName);
                    }
                    transformation.setScriptFile(scriptFileName);
                    transformation.setProjectFile(projectDirName);
                }
            }

            try {
                applicationController.changePortAppContext(appGUID, appVersion, serviceName, portName, scriptContent, jmsScriptContent, transformerType, projectContent, handleId);
            } catch (FioranoException e) {
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_CHANGE_APPCONTEXT_TRANSFORMATION3, portName, serviceName, appGUID, appVersion), e);
                throw new ServiceException(e.getMessage());
            } finally {
                    tempFileNameMap.remove(key);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                }
                FileUtil.deleteDir(appContextFileTempFolder);
            }

        } catch (IOException | XMLStreamException e) {
            successfulextract = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_APPCONTEXT_TRANSFORMATION, portName, serviceName, appGUID, appVersion), e);
            throw new ServiceException(Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_APPCONTEXT_TRANSFORMATION.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_APPCONTEXT_TRANSFORMATION, portName, serviceName, appGUID, appVersion));
        } finally {
            //Removing the temporary zip entry in hashmap. and deleteing the file
            if (!successfulextract) {
                FileUtil.deleteDir(appContextFileTempFolder);
                tempFileNameMap.remove(key);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                }
            }
        }
    }

    public void changePortAppContextConfiguration(String appGUID, float appVersion, String serviceName, String portName, String configurationName) throws RemoteException, ServiceException {
        try {
            applicationController.changePortAppContextConfiguration(appGUID, appVersion, serviceName, portName, configurationName, handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void clearPortAppContext(String appGUID, float appVersion, String serviceName, String portName) throws RemoteException, ServiceException {
        try {
            applicationController.changePortAppContext(appGUID, appVersion, serviceName, portName, null, null, null, null, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_CLEAR_APPCONTEXT_TRANS, portName, serviceName, appGUID, appVersion, ""), e);
            throw new ServiceException(Bundle.ERROR_CLEAR_APPCONTEXT_TRANS.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_CLEAR_APPCONTEXT_TRANS, portName, serviceName, appGUID, appVersion, e.getMessage()));
        }
    }

    public void changeRouteTransformation(String appGUID, float appVersion, String routeGUID, String transformerType, byte[] transformationProjectBytes, boolean completed, String scriptFileName, String jmsScriptFileName, String projectDirName)
            throws ServiceException, RemoteException {

        if (!isRunning(appGUID, appVersion)) {
            throw new ServiceException(Bundle.EVENT_PROCESS_NOT_IN_RUNNING_STATE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.EVENT_PROCESS_NOT_IN_RUNNING_STATE, appGUID, appVersion));
        }
        String key;
        File tempZipFile;
        File routeFileTempFolder = null;
        key = handleId + "__CHANGE_ROUTE_TRANSFORMATION";
        tempZipFile = null;
        FileOutputStream outstream = null;
        boolean successfulzip = true;
        //get the route transformation zip as byte array from server. keep writing to a zip file until client notifies completed
        try {
            tempZipFile = tempFileNameMap.get(key);
            if (tempZipFile == null) {
                tempZipFile = getTempFile("RouteTransformation", "zip");
                tempFileNameMap.put(key, tempZipFile);
            }
            outstream = new FileOutputStream(tempZipFile, true);
            outstream.write(transformationProjectBytes);
        } catch (IOException ioe) {
            successfulzip = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_TRANS_ZIPFILE, routeGUID, appGUID, appVersion), ioe);
            throw new ServiceException(Bundle.UNABLE_TO_CREATE_TRANS_ZIPFILE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_TRANS_ZIPFILE, routeGUID, appGUID, appVersion));
        } finally {
            try {
                if (outstream != null) {
                    outstream.close();
                }
                if (!successfulzip) {
                    boolean delete = tempZipFile.delete();
                    if(!delete){
                        logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }
        if (!completed) {
            return;
        }

        boolean successfulextract = true;
        try {

            //File objects
            routeFileTempFolder = getTempFile("RouteTransformation", "tmp");
            File scriptFile = null;
            File jmsScriptFile = null;
            File projDirFile = null;
            //extract contents.
            boolean make = routeFileTempFolder.mkdir();
            if(!make){
                logger.trace("Could not create dir " + routeFileTempFolder.getAbsolutePath());
            }
            ZipUtil.unzip(tempZipFile, routeFileTempFolder);

            if (projectDirName != null)
                projDirFile = new File(routeFileTempFolder.getCanonicalPath() + File.separator + projectDirName);
            if (scriptFileName != null)
                scriptFile = new File(routeFileTempFolder.getCanonicalPath() + File.separator + scriptFileName);
            if (jmsScriptFileName != null)
                jmsScriptFile = new File(routeFileTempFolder.getCanonicalPath() + File.separator + jmsScriptFileName);

            //checks for existence of file resources.
            if (projectDirName != null && !projDirFile.exists())
                throw new ServiceException(Bundle.ERROR_CHANGE_ROUTE_TRANS3.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_CHANGE_ROUTE_TRANS3, routeGUID, appGUID, appVersion));
            if ((scriptFile != null && !scriptFile.exists()) || (jmsScriptFile != null && !jmsScriptFile.exists()))
                throw new ServiceException(Bundle.ERROR_CHANGE_ROUTE_TRANS2.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_CHANGE_ROUTE_TRANS2, routeGUID, appGUID, appVersion));

            //read the temp folder for the project content
            String projectContent = null;
            String scriptContent = null;
            String jmsScriptContent = null;

            List<Route> routes = null;
            try {
                //expecting app handle to not be null. check for 'is application running' is at method begining.
                routes = applicationController.getApplicationHandle(appGUID, appVersion, handleId).getApplication().getRoutes();
            } catch (Exception willNeverBeThrown) {
                //ignore
            }

            if (projDirFile != null)
                projectContent = ApplicationParser.toXML(projDirFile);

            FileInputStream fis = null;
            if (scriptFile != null) {
                try {
                    fis = new FileInputStream(scriptFile);
                    scriptContent = DmiObject.getContents(fis);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException ignore) {
                        //
                    }
                }

            }

            if (jmsScriptFile != null) {
                try {
                    fis = new FileInputStream(jmsScriptFile);
                    jmsScriptContent = DmiObject.getContents(fis);
                } finally {
                    try {
                        if (fis != null)
                            fis.close();
                    } catch (IOException ignore) {
                        //ignore
                    }
                }
            }

            if (routes != null) {
                for (Route route : routes) {
                    if (route.getName().equals(routeGUID)) {
                        MessageTransformation transformation = route.getMessageTransformation();
                        if (transformation == null && (jmsScriptFileName != null || scriptFileName != null || projectDirName != null)) {
                            transformation = new MessageTransformation();
                            transformation.setJMSScriptFile(jmsScriptFileName);
                            transformation.setScriptFile(scriptFileName);
                            transformation.setProjectFile(projectDirName);
                            route.setMessageTransformation(transformation);
                            break;
                        } else if (transformation != null) {
                            transformation.setJMSScriptFile(jmsScriptFileName);
                            transformation.setScriptFile(scriptFileName);
                            transformation.setProjectFile(projectDirName);
                            break;
                        }
                    }
                }
            }

            try {
                applicationController.changeRouteTransformation(appGUID, appVersion, routeGUID, scriptContent, jmsScriptContent, transformerType, projectContent, handleId);
            } catch (FioranoException e) {
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_CHANGE_ROUTE_TRANS, routeGUID, appGUID, appVersion, ""), e);
                throw new ServiceException(e.getMessage());
            } finally {
                    tempFileNameMap.remove(key);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                }
                FileUtil.deleteDir(routeFileTempFolder);
            }

        } catch (IOException | XMLStreamException e) {
            successfulextract = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_TRANSFORMATION, routeGUID, appGUID, appVersion), e);
            throw new ServiceException(Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_TRANSFORMATION.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_TRANSFORMATION, routeGUID, appGUID, appVersion));
        } finally {
            //Removing the temporary zip entry in hashmap. and deleteing the file
            if (!successfulextract) {
                FileUtil.deleteDir(routeFileTempFolder);
                tempFileNameMap.remove(key);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                }
            }
        }
    }

    public void changeRouteTransformationConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName) throws RemoteException, ServiceException {
        try {
            applicationController.changeRouteTransformationConfiguration(appGUID, appVersion, routeGUID, configurationName, handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void clearRouteTransformation(String appGUID, float appVersion, String routeGUID) throws ServiceException {
        try {
            applicationController.changeRouteTransformation(appGUID, appVersion, routeGUID, null, null, null, null, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GET_RUNNING_EVENTPROCESSES), e);
            throw new ServiceException(Bundle.ERROR_GET_RUNNING_EVENTPROCESSES.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_GET_RUNNING_EVENTPROCESSES));
        }

    }

    @Override
    public byte[] getApplication(String appGUID, float version, long index) throws RemoteException, ServiceException {
        byte[] contents = new byte[0];
        //if Version is -1, get Highest Version and convert to String
        String versionString = String.valueOf(version);
        String eventProcessKey = appGUID.toUpperCase() + "__" + versionString + "__GETEP";
        File tempZipFile = null;
        BufferedInputStream bis = null;
        boolean completed = false;

        if (tempFileNameMap.get(eventProcessKey) == null) {
            File tempdir = null;
            ZipOutputStream zipStream = null;

            try {
                //Create Temporary Directory
                tempdir = getTempFile("application", "tmp");
                boolean make = tempdir.mkdir();
                if(!make){
                    logger.trace("Could not create dir " + tempdir.getAbsolutePath());
                }
                //if the Application to be Zipped in APPID/Version/AppFiles.. We need to add the Required directories as follows.
//                File appDir = new File(tempdir, appGUID + File.separator + versionString);
//                appDir.mkdirs();
                //fetch the Application directory from Application directory and copy the dir into Temporary location
                File appFile = applicationRepository.getAppDir(appGUID, Float.parseFloat(versionString));
                if (appFile == null)
                    return null;
                copyDirectory(appFile, tempdir);

                //Create Temporary zip file for the Application directory.
                tempZipFile = getTempFile("application", "zip");
                zipStream = new ZipOutputStream(new FileOutputStream(tempZipFile));
                ZipUtil.zipDir(tempdir, tempdir, zipStream);
                tempFileNameMap.put(eventProcessKey, tempZipFile);
            } catch (Exception e) {
                completed = true;
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_FETCHING_EVENT_PROCESS_FROM_REPOSITORY, appGUID, version), e);
                throw new ServiceException(Bundle.ERROR_FETCHING_EVENT_PROCESS_FROM_REPOSITORY.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_FETCHING_EVENT_PROCESS_FROM_REPOSITORY, appGUID, version));
            } finally {
                try {
                    if (zipStream != null)
                        zipStream.close();
                    if (tempdir != null)
                        FileUtil.deleteDir(tempdir);
                    if (completed) {
                        tempFileNameMap.remove(eventProcessKey);
                        if (tempZipFile != null) {
                            boolean delete = tempZipFile.delete();
                            if(!delete){
                                logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                            }
                        }
                    }
                } catch (IOException e) {
                    //ignore
                }
            }

        } else
            tempZipFile = tempFileNameMap.get(eventProcessKey);

        //Now we have Application Zip file, Read the contents of the Zip file by skipping till index.
        try {
            bis = new BufferedInputStream(new FileInputStream(tempZipFile));
            long skip = bis.skip(index);
            logger.trace("skipped "+ skip +" bytes while fetching the application " + appGUID +":"+version);
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount;
            readCount = bis.read(tempContents);
            if (readCount < 0) {
                completed = true;
                return null;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
        } catch (IOException e) {
            completed = true;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_SENDING_CONTENTS_OF_APP_ZIPFILE, appGUID, version), e);
            throw new ServiceException(Bundle.ERROR_SENDING_CONTENTS_OF_APP_ZIPFILE.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_SENDING_CONTENTS_OF_APP_ZIPFILE, appGUID, version));
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (completed) {
                    tempFileNameMap.remove(eventProcessKey);
                    boolean delete = tempZipFile.delete();
                    if(!delete){
                        logger.trace("Could not delete " + tempZipFile.getAbsolutePath());
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return contents;
    }

    public void deleteApplication(String appGUID, String version) throws RemoteException, ServiceException {
        try {
            applicationController.deleteApplication(appGUID, version, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DELETE_EVENTPROCESS, appGUID, version), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean dependenciesExists(ServiceReference[] serviceRefs, ApplicationMetadata[] applicationRefs) throws RemoteException, ServiceException {
        boolean dependenciesExists = true; // assume its all fine
        try {

            for (ServiceReference serviceReference : serviceRefs) {
                @SuppressWarnings("unchecked")
                Enumeration<ServiceReference> allVersionsOfService = microServiceRepoManager.getAllVersionsOfService(serviceReference.getId(), false);
                boolean serviceFound = false;
                while (allVersionsOfService.hasMoreElements()) {
                    ServiceReference aService = allVersionsOfService.nextElement();
                    if (aService.getVersion() == serviceReference.getVersion()) {
                        serviceFound = true;
                        break;
                    }
                }
                if (!serviceFound) {
                    dependenciesExists = false;
                    break;
                }
            }

            if (dependenciesExists) { // proceed with event process only if all service dependencies are satisfied

                for (ApplicationMetadata eventProcessReference : applicationRefs) {

                    boolean eventProcessFound = false;

                    ApplicationReference anAppReference = applicationController.getHeaderOfSavedApplication(eventProcessReference.getId(), eventProcessReference.getVersion(), handleId);
                    if (anAppReference.getVersion() == eventProcessReference.getVersion()) {
                        eventProcessFound = true;
                    }

                    if (!eventProcessFound) {
                        dependenciesExists = false;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DEPENDENCIES_EXISTS), e);
            throw new ServiceException(Bundle.ERROR_DEPENDENCIES_EXISTS.toUpperCase(),I18NUtil.getMessage(Bundle.class, Bundle.ERROR_DEPENDENCIES_EXISTS));
        }
        return dependenciesExists;
    }

    @Override
    public void startApplication(String appGUID, String version) throws RemoteException, ServiceException {
        try {
            applicationController.launchApplication(appGUID, version, handleId);
        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_START_EVENTPROCESS, appGUID, version, ""), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void restartApplication(String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            applicationController.stopApplication(appGUID, String.valueOf(appVersion), handleId);
            applicationController.launchApplication(appGUID, String.valueOf(appVersion), handleId);
        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_RESTART_EVENTPROCESS, appGUID, appVersion), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void stopApplication(String appGUID, String version) throws RemoteException, ServiceException {
        try {
            applicationController.stopApplication(appGUID, version, handleId);
        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_STOP_EVENTPROCESS, appGUID, version, ""), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Map<String, Boolean> getApplicationChainForShutdown(String appGUID, float version) throws RemoteException, ServiceException {
        try {
            return applicationController.getApplicationChainForShutdown(appGUID, version, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GETTING_SHUTDOWN_CHAIN, appGUID, version), e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Map<String, Boolean> getApplicationChainForLaunch(String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            return applicationController.getApplicationChainForLaunch(appGUID, appVersion, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GETTING_LAUNCH_CHAIN, appGUID, appVersion), e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void startServiceInstance(String appGUID, float appVersion, String serviceInstanceName) throws RemoteException, ServiceException {
        try {
            applicationController.startMicroService(appGUID, String.valueOf(appVersion), serviceInstanceName, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_START_SERVICEINSTANCE, serviceInstanceName, appGUID, appVersion), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void stopServiceInstance(String appGUID, float appVersion, String serviceInstanceName) throws RemoteException, ServiceException {
        try {
            applicationController.stopMicroService(appGUID, String.valueOf(appVersion), serviceInstanceName, handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_STOP_SERVICEINSTANCE, serviceInstanceName, appGUID, appVersion), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void stopAllServiceInstances(String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            applicationController.stopAllMicroServices(appGUID, String.valueOf(appVersion), handleId);
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_STOP_ALL_SERVICEINSTANCES, appGUID, appVersion), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public ApplicationMetadata[] getRunningApplications() throws RemoteException, ServiceException {
        List<ApplicationMetadata> runningEventProcesses = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Enumeration<ApplicationReference> runningApplications = applicationController.getHeadersOfRunningApplications(handleId);
            while (runningApplications.hasMoreElements()) {
                ApplicationReference appreference = runningApplications.nextElement();
                ApplicationMetadata epRefernce = new ApplicationMetadata(appreference.getGUID(), appreference.getVersion());
                epRefernce.setCategories(appreference.getCategories());
                epRefernce.setDisplayName(appreference.getDisplayName());
                epRefernce.setSchemaVersion(appreference.getSchemaVersion());
                epRefernce.setShortDescription(appreference.getShortDescription());
                epRefernce.setLongDescription(appreference.getLongDescription());
                runningEventProcesses.add(epRefernce);
            }
        } catch (FioranoException e) {
            // rmiLogger.error(Bundle.class, Bundle.ERROR_GET_RUNNING_EVENTPROCESSES, e);
            throw new ServiceException("ERROR_GET_RUNNING_EVENTPROCESSES");
        }
        return runningEventProcesses.toArray(new ApplicationMetadata[runningEventProcesses.size()]);
    }

    private List<PortInstanceMetaData> getPortsForService(Application application, String serviceInstName) throws ServiceException {
        List<PortInstanceMetaData> portInstMetaData = new ArrayList<>();
        if (application != null) {
            ServiceInstance serInst = application.getServiceInstance(serviceInstName);
            if (serInst != null) {
                List<PortInstance> portInst = new ArrayList<>();
                portInst.addAll(serInst.getInputPortInstances());
                portInst.addAll(serInst.getOutputPortInstances());

                for (PortInstance portInstance : portInst) {
                    String destination = (LookUpUtil.getServiceInstanceLookupName(application.getGUID(), application.getVersion(), serInst.getName()) + "__" + portInstance.getName()).toUpperCase();
                    if (portInstance.getDestinationType() == PortInstance.DESTINATION_TYPE_QUEUE) {
                        if (portInstance.isSpecifiedDestinationUsed())
                            portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), portInstance.getDestination(), PortInstanceMetaData.DestinationType.QUEUE, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                        else
                            portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), destination, PortInstanceMetaData.DestinationType.QUEUE, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                    } else {
                        if (portInstance.isSpecifiedDestinationUsed())
                            portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), portInstance.getDestination(), PortInstanceMetaData.DestinationType.TOPIC, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                        else
                            portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), destination, PortInstanceMetaData.DestinationType.TOPIC, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                    }
                }
            } else {
                RemoteServiceInstance remoteSerInst = application.getRemoteServiceInstance(serviceInstName);
                if (remoteSerInst != null) {
                    String remoteAppGUID = remoteSerInst.getApplicationGUID();
                    String remoteInstanceName = remoteSerInst.getRemoteName();
                    float remoteAppVersion = remoteSerInst.getApplicationVersion();
                    try {
                        application = applicationRepository.readApplication(remoteAppGUID, String.valueOf(remoteAppVersion));
                        serInst = application.getServiceInstance(remoteInstanceName);
                        if (serInst != null) {
                            List<PortInstance> portInst = new ArrayList<>();
                            portInst.addAll(serInst.getInputPortInstances());
                            portInst.addAll(serInst.getOutputPortInstances());

                            for (PortInstance portInstance : portInst) {
                                String destination = (LookUpUtil.getServiceInstanceLookupName(application.getGUID(), application.getVersion(), serInst.getName()) + "__" + portInstance.getName()).toUpperCase();
                                if (portInstance.getDestinationType() == PortInstance.DESTINATION_TYPE_QUEUE) {
                                    if (portInstance.isSpecifiedDestinationUsed())
                                        portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), portInstance.getDestination(), PortInstanceMetaData.DestinationType.QUEUE, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                                    else
                                        portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), destination, PortInstanceMetaData.DestinationType.QUEUE, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                                } else {
                                    if (portInstance.isSpecifiedDestinationUsed())
                                        portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), portInstance.getDestination(), PortInstanceMetaData.DestinationType.TOPIC, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                                    else
                                        portInstMetaData.add(new PortInstanceMetaData(serInst.getName(), portInstance.getName(), destination, PortInstanceMetaData.DestinationType.TOPIC, portInstance.isDestinationEncrypted(), portInstance.getEncryptionKey(), portInstance.getEncryptionAlgorithm(), portInstance.isAllowPaddingToKey(), portInstance.getInitializationVector()));
                                }
                            }
                        }

                    } catch (Exception e) {
                        // rmiLogger.error(Bundle.class, Bundle.UNABLE_TO_GET_PORTS_FOR_SERVICE_INSTANCE, serviceInstName, remoteAppGUID, remoteAppVersion, e.getMessage(), e);
                        throw new ServiceException(e.getMessage());
                    }
                }
            }
        }
        return portInstMetaData;
    }

    @Override
    public List<RouteMetaData> getRoutesOfApplications(String appGUID, float version) throws RemoteException, ServiceException {
        List<RouteMetaData> routes = new ArrayList<>();
        try {
            Application application = applicationRepository.readApplication(appGUID, String.valueOf(version));
            List<Route> dmiRoutes = application.getRoutes();

            for (Route dmiRoute : dmiRoutes) {
                PortInstanceMetaData sourcePortMetaData = null;
                PortInstanceMetaData targetPortMetaData = null;

                List<PortInstanceMetaData> sourcePorts = getPortsForService(application, dmiRoute.getSourceServiceInstance());
                List<PortInstanceMetaData> targetPorts = getPortsForService(application, dmiRoute.getTargetServiceInstance());

                for (PortInstanceMetaData portInstanceMetaData : sourcePorts) {
                    if (portInstanceMetaData.getDisplayName().equals(dmiRoute.getSourcePortInstance())) {
                        sourcePortMetaData = portInstanceMetaData;
                        break;
                    }
                }

                for (PortInstanceMetaData portInstanceMetaData : targetPorts) {
                    if (portInstanceMetaData.getDisplayName().equals(dmiRoute.getTargetPortInstance())) {
                        targetPortMetaData = portInstanceMetaData;
                        break;
                    }
                }
                routes.add(new RouteMetaData(dmiRoute.getName(), dmiRoute.getSourceServiceInstance(), dmiRoute.getTargetServiceInstance(), sourcePortMetaData, targetPortMetaData));
            }
        } catch (Exception e) {
            //rmiLogger.error(Bundle.class, Bundle.UNABLE_TO_GET_ROUTES_FOR_APPLICATION, appGUID, version, e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return routes;
    }

    @Override
    public List<PortInstanceMetaData> getPortsForApplications(String appGUID, float version) throws RemoteException, ServiceException {

        List<PortInstanceMetaData> portInstMetaData = new ArrayList<>();
        try {
            Application application = applicationRepository.readApplication(appGUID, String.valueOf(version));
            if (application != null) {
                for (ServiceInstance serInst : application.getServiceInstances()) {
                    if (serInst != null) {
                        portInstMetaData.addAll(getPortsForService(application, serInst.getName()));
                    }
                }
                for (Object o : application.getRemoteServiceInstances()) {
                    RemoteServiceInstance remoteserInst = (RemoteServiceInstance) o;
                    if (remoteserInst != null) {
                        portInstMetaData.addAll(getPortsForService(application, remoteserInst.getName()));
                    }
                }
            }
        } catch (Exception e) {
            // rmiLogger.error(Bundle.class, Bundle.UNABLE_TO_GET_PORTS_FOR_APPLICATION, appGUID, version, e);
            throw new ServiceException(e.getMessage());
        }
        return portInstMetaData;
    }

    public List<PortInstanceMetaData> getPortsForService(String appGUID, float version, String serviceInstName) throws RemoteException, ServiceException {
        List<PortInstanceMetaData> portInstMetaData = new ArrayList<>();
        try {
            Application application = applicationRepository.readApplication(appGUID, String.valueOf(version));
            ServiceInstance serviceInst = application.getServiceInstance(serviceInstName);
            if (serviceInst != null) {
                portInstMetaData = getPortsForService(application, serviceInstName);
            } else {
                RemoteServiceInstance remoteserInst = application.getRemoteServiceInstance(serviceInstName);
                if (remoteserInst != null)
                    portInstMetaData = getPortsForService(application, remoteserInst.getName());
            }
        } catch (Exception e) {
            // rmiLogger.error(Bundle.class, Bundle.UNABLE_TO_GET_PORTS_FOR_SERVICE_INSTANCE, serviceInstName, appGUID, version, e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return portInstMetaData;
    }


    @Override
    public List<ServiceInstanceMetaData> getServiceInstancesOfApp(String appGUID, float version) throws RemoteException, ServiceException {
        List<ServiceInstanceMetaData> serviceInstances = new ArrayList<>();
        try {
            Application application = applicationRepository.readApplication(appGUID, String.valueOf(version));
            if (application != null) {
                List<ServiceInstance> services = application.getServiceInstances();
                for (ServiceInstance serviceInstance : services) {
                    serviceInstances.add(new ServiceInstanceMetaData(serviceInstance.getName(), serviceInstance.getGUID(), serviceInstance.getVersion(), serviceInstance.getShortDescription(), serviceInstance.getLongDescription(), serviceInstance.getNodes(), serviceInstance.getLaunchType()));
                }

                @SuppressWarnings("unchecked")
                List<ServiceInstance> remoteServices = application.getRemoteServiceInstances();
                if (remoteServices != null) {
                    for (Object o : application.getRemoteServiceInstances()) {
                        RemoteServiceInstance remoteserInst = (RemoteServiceInstance) o;
                        if (remoteserInst != null) {
                            String remoteAppGUID = remoteserInst.getApplicationGUID();
                            String remoteInstanceName = remoteserInst.getRemoteName();
                            application = applicationRepository.readApplication(remoteAppGUID, String.valueOf(remoteserInst.getApplicationVersion()));
                            if (application == null) {
                                throw new FioranoException("ERROR_APP_DOESNT_EXIST");
                            }
                            ServiceInstance actualservice = application.getServiceInstance(remoteInstanceName);
                            serviceInstances.add(new RemoteServiceInstanceMetaData(appGUID, remoteAppGUID, remoteInstanceName, actualservice.getName(), actualservice.getGUID(), actualservice.getVersion(), actualservice.getShortDescription(), actualservice.getLongDescription(), actualservice.getNodes(), actualservice.getLaunchType()));
                        }
                    }
                }
            }
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.UNABLE_TO_GET_SERVICE_INSTANCES_FOR_APPLICATION, appGUID, version, e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        return serviceInstances;
    }

    @Override
    public void addApplicationListener(IApplicationManagerListener listener, String appGUID, float appVersion) throws RemoteException, ServiceException {
        dapiEventManager.registerApplicationEventListener(listener, appGUID, appVersion, handleId);
    }

    @Override
    public void removeApplicationListener(IApplicationManagerListener listener, String appGUID, float appVersion) throws RemoteException, ServiceException {
        dapiEventManager.unRegisterApplicationEventListener(listener, appGUID, appVersion, handleId);
    }

    @Override
    public void addRepositoryEventListener(IRepoEventListener listener) throws RemoteException, ServiceException {
        dapiEventManager.registerApplicationRepoEventListener(listener, handleId);
    }

    @Override
    public void removeRepositoryEventListener() throws RemoteException, ServiceException {
        dapiEventManager.unRegisterApplicationRepoEventListener(handleId);
    }

    @Override
    public boolean isRunning(String appGUID, float appVersion) throws RemoteException, ServiceException {
        boolean running = false;
        ApplicationMetadata[] runningEventProcesses = getRunningApplications();
        for (ApplicationMetadata eventProcessReference : runningEventProcesses) {
            if (eventProcessReference.getId().equals(appGUID) && eventProcessReference.getVersion() == appVersion) {
                running = true;
                break;
            }
        }
        return running;
    }

    @Override
    public void synchronizeApplication(String appGUID, float version) throws RemoteException, ServiceException {
        try {
            applicationController.synchronizeApplication(appGUID, String.valueOf(version), handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void startAllServices(String appGUID, float version) throws RemoteException, ServiceException {
        try {
            applicationController.startAllMicroServices(appGUID, String.valueOf(version), handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void checkResourcesAndConnectivity(String appGUID, float version) throws RemoteException, ServiceException {
        try {
            applicationController.checkResourceAndConnectivity(appGUID, version, handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    public String getComponentStats(String appGUID, float appVersion, String servInstName) throws ServiceException {
        try {
            return applicationController.getComponentStats(appGUID, appVersion, servInstName, handleId);
        } catch (FioranoException e) {
            logger.error("Error occured while getting component Statistics for serviec " + servInstName + "of application " + appGUID + ":" + appVersion);
            throw new ServiceException("Error occured while getting component Statistics for serviec " + servInstName + "of application " + appGUID + ":" + appVersion, e);
        }
    }

    @Override
    public void flushMessages(String appGUID, float appVersion, String servInstName) throws ServiceException {
        try {
            applicationController.flushMessages(appGUID, appVersion, servInstName, handleId);
        } catch (Exception e) {
            logger.error("Error occured while flushing messages in serviec " + servInstName + "of application " + appGUID + ":" + appVersion);
            throw new ServiceException("Error occured while flushing messages in serviec " + servInstName + "of application " + appGUID + ":" + appVersion, e);

        }
    }

    @Override
    public String viewHttpContext(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException {
        return null;
    }

    @Override
    public ApplicationStateData getApplicationStateDetails(String appGUID, float appVersion) throws RemoteException, ServiceException {
        ApplicationStateData eventProcessData = new ApplicationStateData();
        try {
            ApplicationStateDetails asd = applicationController.getCurrentStateOfApplication(appGUID, appVersion, handleId);
            //Add EventProcess ID
            eventProcessData.setEventProcessID(asd.getAppGUID());
            //Add Debug Routes If Any..
            Iterator itr = asd.getDebugRoutes();
            while (itr.hasNext()) {
                eventProcessData.addDebugRoute((String) itr.next());
            }
            //Set Launch Time
            eventProcessData.setLaunchTime(asd.getLaunchTime());
            //Set Kill Time
            eventProcessData.setKillTime(asd.getKillTime());
            //Set Application Label
            eventProcessData.setEventProcessLabel(asd.getApplicationLabel());
            //For Each Service add the Service State Details and Service Exception traces if any.
            Enumeration enums = asd.getAllServiceNames();
            if (enums != null) {
                while (enums.hasMoreElements()) {
                    String instName = (String) enums.nextElement();
                    ServiceStateData servicedata = new ServiceStateData();
                    //Add Service Instance Name
                    servicedata.setServiceInstanceName(instName);
                    ServiceInstanceStateDetails status = asd.getServiceStatus(instName);
                    //Set Service GUID
                    servicedata.setServiceGUID(status.getServiceGUID());
                    //Set GraceFul Kill Variable
                    servicedata.setGracefulKill(status.isGracefulKill());
                    //Set Kill Time
                    servicedata.setKillTime(status.getKillTime());
                    //Set Launch Time
                    servicedata.setLaunchTime(status.getLaunchTime());
                    //Set Running Version
                    servicedata.setRunningVersion(status.getRunningVersion());
                    //Set Node Name for Service
                    servicedata.setServiceNodeName(status.getServiceNodeName());
                    //Set the Current Status of Service
                    servicedata.setStatusString(status.getStatusString());
                    //Set Unique Running Instance ID
                    servicedata.setUniqueRunningInstID(status.getUniqueRunningInstID());
                    eventProcessData.addServiceStatus(instName, servicedata);
                }

                //Add Service Exception traces if any.
                Enumeration traceEnums = asd.getAllServiceWithExceptions();
                while (traceEnums.hasMoreElements()) {
                    String instName = (String) traceEnums.nextElement();
                    eventProcessData.addServiceExceptionTrace(instName, asd.getServiceExceptionTrace(instName));
                }
            }
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
        return eventProcessData;
    }

    @Override
    public ApplicationMetadata[] getAllApplications() throws RemoteException, ServiceException {
        List<ApplicationMetadata> savedEventProcesses = new ArrayList<>();
        try {
            @SuppressWarnings("unchecked")
            Enumeration<ApplicationReference> savedApplications = applicationController.getHeadersOfSavedApplications(handleId);
            while (savedApplications.hasMoreElements()) {
                ApplicationReference appReference = savedApplications.nextElement();
                ApplicationMetadata epReference = new ApplicationMetadata(appReference.getGUID(), appReference.getVersion());
                epReference.setCategories(appReference.getCategories());
                epReference.setDisplayName(appReference.getDisplayName());
                epReference.setSchemaVersion(appReference.getSchemaVersion());
                epReference.setShortDescription(appReference.getShortDescription());
                epReference.setLongDescription(appReference.getLongDescription());
                epReference.setTypeName(appReference.getTypeName());
                epReference.setSubType(appReference.getSubType());
                savedEventProcesses.add(epReference);
            }

        } catch (FioranoException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }

        return savedEventProcesses.toArray(new ApplicationMetadata[savedEventProcesses.size()]);
    }

    @Override
    public ApplicationMetadata getApplication(String appGUID, float version) throws RemoteException, ServiceException {
        ApplicationMetadata epRefernce;
        ApplicationReference appreference = applicationController.getHeaderOfSavedApplication(appGUID, version, handleId);
        if (null == appreference) {
            return null;
        }
        epRefernce = new ApplicationMetadata(appreference.getGUID(), appreference.getVersion());
        epRefernce.setCategories(appreference.getCategories());
        epRefernce.setDisplayName(appreference.getDisplayName());
        epRefernce.setSchemaVersion(appreference.getSchemaVersion());
        epRefernce.setShortDescription(appreference.getShortDescription());
        epRefernce.setLongDescription(appreference.getLongDescription());
        return epRefernce;
    }

    @Override
    public String getLastOutTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            return applicationLogManager.getLastOutTrace(numberOfLines, serviceName, appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String getLastErrTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            return applicationLogManager.getLastErrTrace(numberOfLines, serviceName, appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void clearServiceOutLogs(String serviceInst, String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            applicationLogManager.clearServiceOutLogs(serviceInst, appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void clearServiceErrLogs(String serviceInst, String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            applicationLogManager.clearServiceErrLogs(serviceInst, appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void clearApplicationLogs(String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            applicationLogManager.clearApplicationLogs(appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public byte[] exportServiceLogs(String appGUID, float version, String serviceInst, long index) throws RemoteException, ServiceException {
        try {
            return applicationLogManager.exportServiceLogs(appGUID, version, serviceInst, index);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public byte[] exportApplicationLogs(String appGUID, float version, long index) throws RemoteException, ServiceException {
        try {
            return applicationLogManager.exportApplicationLogs(appGUID, version, index);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void setLogLevel(String appGUID, float appVersion, String serviceInstName, Hashtable modules) throws RemoteException, ServiceException {

    }

    @Override
    public void changeRouteSelector(String appGUID, float appVersion, String routeGUID, HashMap selectors) throws RemoteException, ServiceException {
        try {
            applicationController.changeRouteSelector(appGUID, appVersion, routeGUID, selectors, handleId);
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CHANGE_ROUTE_SELECTOR, routeGUID, appGUID, appVersion, "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void changeRouteSelectorConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName) throws RemoteException, ServiceException {
        try {
            applicationController.changeRouteSelectorConfiguration(appGUID, appVersion, routeGUID, configurationName, handleId);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String getWADLURL(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException {
        ApplicationHandle applicationHandle = applicationController.getApplicationHandle(appGUID, appVersion);
        if (applicationHandle == null) {
            throw new ServiceException("Application " + appGUID + ":" + appVersion + " is not running.");
        }
        ServiceInstance serviceInstance = applicationHandle.getApplication().getServiceInstance(servInstName);
        if (!serviceInstance.getGUID().equalsIgnoreCase("RESTStub")) {
            throw new ServiceException("Service " + servInstName + " in application " + appGUID + ":" + appVersion + " is not a RESTStub");
        }
        String configuration = serviceInstance.getConfiguration();
        try {
            Hashtable<String, Object> stubProperties = ServiceConfigurationParser.INSTANCE().parseRESTStubConfiguration(configuration);
            return ServerConfig.getConfig().getJettyUrl() + "/restgateway/services/AdminService/wadls/" + stubProperties.get(ServiceConfigurationParser.ConfigurationMarkups.RESTFUL_SERVICE_NAME);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String getWSDLURL(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException {
        ApplicationHandle applicationHandle = applicationController.getApplicationHandle(appGUID, appVersion);
        if (applicationHandle == null) {
            throw new ServiceException("Application " + appGUID + ":" + appVersion + " is not running.");
        }
        ServiceInstance serviceInstance = applicationHandle.getApplication().getServiceInstance(servInstName);
        if (!serviceInstance.getGUID().equalsIgnoreCase("WSStub")) {
            throw new ServiceException("Service " + servInstName + " in application " + appGUID + ":" + appVersion + " is not a WSStub");
        }
        String configuration = serviceInstance.getConfiguration();
        try {
            Hashtable<String, Object> stubProperties = ServiceConfigurationParser.INSTANCE().parseWSStubConfiguration(configuration);
            return ServerConfig.getConfig().getJettyUrl() +"/bcwsgateway/services/"+stubProperties.get(ServiceConfigurationParser.ConfigurationMarkups.CONTEXT_NAME)+"?wsdl";
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Set<String> getReferringRunningApplications(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException {
        try {
            return applicationController.getReferringRunningApplications(appGUID, appVersion, servInstName);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<String> getAllReferringApplications(String appGUID, float appVersion, String serviceInstName) throws RemoteException, ServiceException {
        try {
            List<String> temp = new ArrayList<>();
            Set<String> allReferredApps = applicationController.getAllReferringApplications(appGUID, appVersion, serviceInstName);
            if (allReferredApps != null && allReferredApps.size() > 0)
                temp.addAll(allReferredApps);
            return temp;
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @param appGUID application guid
     * @param appVersion application version
     * @return true is application is being referred by some other application i.e. if some other application is to have a remote instance of this application's service
     * @throws RemoteException
     * @throws ServiceException
     */
    public boolean isApplicationReferred(String appGUID, float appVersion) throws RemoteException, ServiceException {
        try {
            return applicationController.isApplicationReferred(appGUID, appVersion);
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private String getRepoCategory(String namedCategory) throws FioranoException {
        String repoCategory;
        switch (namedCategory) {
            case Constants.CONNECTION_FACTORY_CAT:
                repoCategory = Constants.CONNECTION_FACTORY_REPO;
                break;
            case Constants.DESTINATION_CAT:
                repoCategory = Constants.DESTINATION_REPO;
                break;
            case Constants.MSG_FILTERS_CAT:
                repoCategory = Constants.MSG_FILTERS_REPO;
                break;
            case Constants.PORT_CAT:
                repoCategory = Constants.PORT_REPO;
                break;
            case Constants.ROUTE_CAT:
                repoCategory = Constants.ROUTE_REPO;
                break;
            case Constants.SELECTOR_CAT:
                repoCategory = Constants.SELECTOR_REPO;
                break;
            case Constants.RUN_ARGS_CAT:
                repoCategory = Constants.RUN_ARGS_REPO;
                break;
            case Constants.SERVICE_CAT:
                repoCategory = Constants.SERVICE_REPO;
                break;
            case Constants.TRANS_CAT:
                repoCategory = Constants.TRANS_REPO;
                break;
            case Constants.WRKFLW_CAT:
                repoCategory = Constants.WRKFLW_REPO;
                break;
            case Constants.RESOURCE_CAT:
                repoCategory = Constants.RESOURCE_REPO;
                break;
            default:
                throw new FioranoException("Invalid category specified");
        }
        return repoCategory;
    }

    public HashMap getRunningCompUsingNamedConfigs(HashMap<Integer, HashMap<String, String>> configsToChange) throws ServiceException {
        try {
            validateHandleID(handleId, "Get List of Running Components using the named configuration");
            HashMap<Integer, String> namedConfigRef = new HashMap<>();
            int i = 0;
            Enumeration<ApplicationReference> apprefs = applicationController.getHeadersOfRunningApplications(handleId);
            while (apprefs.hasMoreElements()) {
                ApplicationReference currentApp = apprefs.nextElement();
                Application current = applicationRepository.readApplication(currentApp.getGUID(), String.valueOf(currentApp.getVersion()));
                List<ServiceInstance> services = current.getServiceInstances();
                for (ServiceInstance service : services) {
                    Boolean compUsingNamedConfigs = false;
                    ArrayList<NamedConfigurationProperty> listofConfigs = service.getNamedConfigurations();
                    for (NamedConfigurationProperty config : listofConfigs) {
                        String configType;
                        if (config.getConfigurationType().equalsIgnoreCase("component")) {
                            configType = "service";
                        } else configType = config.getConfigurationType();

                        for (int num : configsToChange.keySet()) {
                            HashMap<String, String> configDetails = configsToChange.get(num);
                            if (configDetails.get("category").equalsIgnoreCase(configType) && configDetails.get("target").equalsIgnoreCase(config.getConfigurationName()) && current.getLabel().equalsIgnoreCase(configDetails.get("environment"))) {
                                if (configType.equalsIgnoreCase("service")) {
                                    if (service.getGUID().equalsIgnoreCase(configDetails.get("compGUID")) && service.getVersion() == Float.valueOf(configDetails.get("compVersion"))) {
                                        compUsingNamedConfigs = true;
                                    }
                                } else {
                                    compUsingNamedConfigs = true;
                                }
                            }
                        }
                    }
                    if (compUsingNamedConfigs) {
                        namedConfigRef.put(i, current.getGUID() + ":" + current.getVersion() + ":" + service.getName());
                        i++;
                    }
                }
            }
            return namedConfigRef;
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public String changeNamedConfigurations(HashMap<Integer, HashMap<String, String>> configsToChange) throws ServiceException {
        int failure = 0;
        try {
            String configurationsRepositoryPath = System.getProperty("ESB_USER_DIR") + File.separator + "repository" + File.separator + "configurations";
            if (Boolean.valueOf(System.getProperty("HA_ENABLED"))) {
                if (System.getProperty("PROFILE_NAME").toLowerCase().contains("secondary")) {
                    configurationsRepositoryPath = System.getProperty("ESB_USER_DIR") + File.separator + "repository_backup" + File.separator + "configurations";
                }
            }
            File configDir = new File(configurationsRepositoryPath);
            String[] categoryList = configDir.list();
            ArrayList<String> categoryDir = new ArrayList<>();
            if (!configDir.isDirectory() || categoryList == null || categoryList.length == 0) {
                System.out.println("Named Configurations repository is empty. Please check configuration details");
                throw new ServiceException("Named Configurations repository is empty. Please check configuration details");
            }
            Collections.addAll(categoryDir, categoryList);
            for (int i = 0; i < configsToChange.size(); i++) {
                HashMap<String, String> configDetails = configsToChange.get(i);
                String namedCategory = configDetails.get("category").toLowerCase();
                String namedEnvironment = configDetails.get("environment");
                String source = configDetails.get("source");
                String target = configDetails.get("target");
                String compGUID = configDetails.get("compGUID");
                String repoCategory;
                String compVersion = configDetails.get("compVersion");
                String portType = configDetails.get("portType");
                try {
                    repoCategory = getRepoCategory(namedCategory);
                } catch (Exception e) {
                    System.out.println("Named Configurations category " + namedCategory + " does not exist in the repository.");
                    failure++;
                    continue;
                }
                if (!categoryDir.contains(repoCategory)) {
                    System.out.println("Named Configurations category " + namedCategory + " does not exist in the repository. Please check configuration details");
                    failure++;
                    continue;
                }
                File sourceFile = NamedConfigurationUtil.getConfigurationFile(configurationsRepositoryPath, repoCategory, namedEnvironment, source);
                File targetFile = NamedConfigurationUtil.getConfigurationFile(configurationsRepositoryPath, repoCategory, namedEnvironment, target);
                if (namedCategory.equalsIgnoreCase("service")) {
                    sourceFile = NamedConfigurationUtil.getServiceConfigurationFile(configurationsRepositoryPath, compGUID, compVersion, namedEnvironment, source);
                    targetFile = NamedConfigurationUtil.getServiceConfigurationFile(configurationsRepositoryPath, compGUID, compVersion, namedEnvironment, target);
                }
                if (namedCategory.equalsIgnoreCase("port")) {
                    sourceFile = NamedConfigurationUtil.getPortConfigurationFile(configurationsRepositoryPath, portType, namedEnvironment, source);
                    targetFile = NamedConfigurationUtil.getPortConfigurationFile(configurationsRepositoryPath, portType, namedEnvironment, target);
                }
                if (!sourceFile.exists()) {
                    System.out.println("Source file for configuration type " + namedCategory + ", environment " + namedEnvironment + " with name " + source + " does not exist in the repository, please check configurations details.");
                    failure++;
                    continue;
                }
                if (!targetFile.exists()) {
                    System.out.println("Target file for configuration type " + namedCategory + ", environment " + namedEnvironment + " with name " + target + " does not exist in the repository, please check configurations details.");
                    failure++;
                    continue;
                }
                // rmiLogger.info(Bundle.class, Bundle.START_CHANGE_NAMED_CONFIG,source,target,namedCategory,namedEnvironment);
                try {
                    if (!repoCategory.equalsIgnoreCase("transformations"))
                        FileUtil.copyFileUsingIO(sourceFile, targetFile);
                    else {
                        FileUtil.deleteDir(targetFile);
                        FileUtil.copyDirectory(sourceFile, targetFile);
                    }
                    //rmiLogger.info(Bundle.class, Bundle.CHANGE_NAMED_CONFIG_SUCCESSFUL,source,target,namedCategory,namedEnvironment);
                } catch (Exception e) {
                    //rmiLogger.error(Bundle.class, Bundle.ERROR_CHANGE_NAMED_CONFIG+" source: "+source+" target: "+target+" category: "+namedCategory+" environment: "+namedEnvironment);
                    failure++;
                }
            }
            return String.valueOf(failure);
        } catch (Exception e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CHANGE_NAMED_CONFIG,e);
            throw new ServiceException(e.getMessage());
        }
    }

    public String synchronizeAllRunningEP() throws ServiceException {
        try {
            int failure = 0;
            validateHandleID(handleId, "Synchronize All Running Event Processes");
            Enumeration<ApplicationReference> apprefs = applicationController.getHeadersOfRunningApplications(handleId);
            ApplicationReference currentApp;
            while (apprefs.hasMoreElements()) {
                try {
                    currentApp = apprefs.nextElement();
                    synchronizeApplication(currentApp.getGUID(), currentApp.getVersion());
                    Thread.sleep(2000);
                } catch (Exception e) {
                    //  rmiLogger.error(Bundle.class, Bundle.ERROR_SYNCHRONIZE_EVENTPROCESS,currentApp.getGUID(),currentApp.getVersion(),e);
                    failure++;
                }
            }
            return String.valueOf(failure);
        } catch (Exception e) {
            // rmiLogger.error(Bundle.class, Bundle.ERROR_SYNC_ALL_RUN_EP,e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean isFESLevelRouteDurable() {
        return false;
    }

    @Override
    public boolean isAppLevelRouteDurable(String appGUID, float appVersion) throws RemoteException, ServiceException {
        return false;
    }

    @Override
    public boolean isRouteDurable(String appGUID, float appVersion, String routeID) throws RemoteException, ServiceException {
        return false;
    }

    @Override
    public boolean isDeleteDestinationSetAtApp(String appGUID, float appVersion) throws RemoteException, ServiceException {
        return false;
    }

    @Override
    public boolean isServiceRunning(String eventProcessName, float appVersion, String servInstanceName) {
        return applicationController.isServiceRunning(eventProcessName, appVersion, servInstanceName);
    }

    public ServiceInstance getServiceInstance(String eventProcessName, float appVersion, String servInstanceName) throws ServiceException {
        try {
            return applicationController.getServiceInstance(eventProcessName, appVersion, servInstanceName);
        } catch (FioranoException e) {
            logger.error("error occured while getting the sevice instance " + servInstanceName + "of application " + eventProcessName + ":" + appVersion, e);
            throw new ServiceException(e.getMessage());
        }
    }

    public Properties getServerConfig() throws ServiceException {
        return applicationController.getServerConfig();
    }

    public Properties getTransportConfig() throws ServiceException {
        return applicationController.getTransportConfig();
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());
    }

    public String toString() {
        return Constants.APPLICATION_MANAGER;
    }
}
