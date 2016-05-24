/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.DmiResourceData;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.application.service.*;
import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.applicationcontroller.ApplicationHandle;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.rmiconnector.api.IRepoEventListener;
import com.fiorano.openesb.rmiconnector.api.IServiceManager;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.rmiconnector.api.ServiceReference;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MicroServiceManager extends AbstractRmiManager implements IServiceManager {
    MicroServiceRepoManager microServiceRepository;
    private Logger logger = LoggerFactory.getLogger(Activator.class);
    public static final Map FAVORITES = Collections.singletonMap("FIORANO_HOME", new File(System.getProperty("user.dir")));

    //maintains a hashmap of the file reference to service zip files that are being created when executing operations getService, deployService, fetchResource
    private HashMap<String, File> SERVICEMAP = new HashMap<String, File>(8);

    //Reference to instanceHandler instace.
    private InstanceHandler handler;

    private ApplicationController applicationController;

    //boolean holding kill running instances value of delete instance method local var, used for synchronising the running Event Process
    private boolean killRunningInstances = false;
    
    protected MicroServiceManager(RmiManager rmiManager, InstanceHandler instanceHandler) {
        super(rmiManager);
        this.microServiceRepository = rmiManager.getMicroServiceRepoManager();
        this.applicationController = rmiManager.getApplicationController();
        this.handler = instanceHandler;
        setHandleID(instanceHandler.getHandleID());
    }

    public String[] getServiceIds() throws ServiceException {

        List<String> serviceIds = new ArrayList<String>();
        try {
            Enumeration<String> serviceIdEnum = microServiceRepository.getAllServiceGUIDs();
            while (serviceIdEnum.hasMoreElements()) {
                serviceIds.add(serviceIdEnum.nextElement());
            }

        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GET_ALL_SERVICE_IDS, e));
            throw new ServiceException(Bundle.ERROR_GET_ALL_SERVICE_IDS.toUpperCase(),I18NUtil.getMessage(Bundle.class, Bundle.ERROR_GET_ALL_SERVICE_IDS));

        }
        return serviceIds.toArray(new String[serviceIds.size()]);
    }

    public boolean exists(String id, float version) throws ServiceException {

        boolean exists = false;
        float[] versions = getVersions(id);
        if (version == ANY_VERSION) {
            exists = versions.length > 0; // any version is fine
        } else {
            // check for correct version
            for (float availableVersion : versions) {
                if (availableVersion == version) {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    public ServiceReference[] getAllServices() throws ServiceException {

        try {
            ArrayList<ServiceReference> serviceReferenceList = new ArrayList<ServiceReference>();
            Enumeration _enum = microServiceRepository.getAllServicesInRepository();
            while (_enum.hasMoreElements()) {
                Service service = (Service) _enum.nextElement();
                serviceReferenceList.add(new ServiceReference(service.getGUID(), service.getDisplayName(), service.getVersion()));
            }

            _enum = microServiceRepository.getAllUnCommittedServices();
            while (_enum.hasMoreElements()) {
                Service service = (Service) _enum.nextElement();
                serviceReferenceList.add(new ServiceReference(service.getGUID(), service.getDisplayName(), service.getVersion()));
            }

            return serviceReferenceList.toArray(new ServiceReference[serviceReferenceList.size()]);
        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GET_SERVICE_REFERENCES, e));
            throw new ServiceException(Bundle.ERROR_GET_SERVICE_REFERENCES.toUpperCase(), I18NUtil.getMessage(Bundle.class, Bundle.ERROR_GET_SERVICE_REFERENCES));

        }
    }

    public float[] getVersions(String id) throws ServiceException {

        List<Float> versionStrings = new ArrayList<Float>();
        try {
            @SuppressWarnings("unchecked")
            Enumeration versionsEnum = microServiceRepository.getAllVersionsOfService(id, false);
            while (versionsEnum.hasMoreElements()) {
                com.fiorano.openesb.application.service.ServiceReference serviceReference = (com.fiorano.openesb.application.service.ServiceReference) versionsEnum.nextElement();
                versionStrings.add(serviceReference.getVersion());
            }
        } catch (Exception e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GET_ALL_VERSIONS_SERVICE, id, e));
            throw new ServiceException(Bundle.ERROR_GET_ALL_VERSIONS_SERVICE.toUpperCase(),I18NUtil.getMessage(Bundle.class, Bundle.ERROR_GET_ALL_VERSIONS_SERVICE, id));
        }

        float versions[] = new float[versionStrings.size()];
        for (int i = 0; i < versionStrings.size(); i++) {
            versions[i] = versionStrings.get(i);
        }

        // Sort it before sending :-)
        Arrays.sort(versions);

        return versions;
    }

    /**
     * Imports Zip file with one or more services. Overwrites the if service already exists, Client should handle if we does not want to overwrite the service.
     * @param zippedContents The contents of the service in zipped form
     * @param completed      boolean specifying if specified <code>zippedContents</code> is the last chunk to be deployed for this event process
     * @param resync        boolean specifying whether to kill all dependent services or not and resync the application related to the service
     * @param servicesToImport List of service(s) to be imported from the zip file, other service will be ignored. If value is 'null' or list is empty no services in the zip file will be imported
     *                         Format of each element in the list is ServiceGUID:ServiceVersion
     * @param retainOldResources boolean specifying whether to retain old unique resources
     * @throws ServiceException
     */
    public void deployService(byte[] zippedContents, boolean completed, boolean resync, boolean retainOldResources, List<String> servicesToImport) throws ServiceException {

        validateHandleID(handleId, "deploy Service");
        File tempZipFile = null;
        FileOutputStream outstream = null;
        File extractedFolder = null;
        Service service = null;
        boolean successfulzip = true;
        String servicekey = handleId+"__DeploySERVICE";

        //get the service zip as byte array from server. keep writing to a zip file until client notifies completed
        try {
            tempZipFile = SERVICEMAP.get(servicekey);
            if (tempZipFile == null) {
                tempZipFile = getTempFile("service", "zip");
                SERVICEMAP.put(servicekey, tempZipFile);
            }
            outstream = new FileOutputStream(tempZipFile, true);
            outstream.write(zippedContents);
        } catch (IOException ioe) {
            //if any exception occures in between this will ensures that temporary files get deleted
            successfulzip = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_SERVICE_ZIPFILE_IP, ioe));
            throw new ServiceException(Bundle.UNABLE_TO_CREATE_SERVICE_ZIPFILE.toUpperCase(),I18NUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_SERVICE_ZIPFILE_IP));

        }
        finally {
            try {
                if (outstream != null)
                    outstream.close();
                if (!successfulzip && tempZipFile != null)
                    tempZipFile.delete();
            } catch (IOException e) {
                //ignore
            }
        }
        if (!completed) {
            return;
        }

        boolean successfulextract = true;
        //when client sends complete zip contents, extract the zip contents into a temporary folder
        try {
            extractedFolder = getTempFile("service", "tmp");
            extractedFolder.mkdir();
//            extractZip(extractedFolder, tempZipFile);
            ZipUtil.unzip(tempZipFile, extractedFolder);
        } catch (Exception e) {
            //if any exception occurs in between this will ensure that temporary files get deleted
            successfulextract = false;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE_IP, e));
            throw new ServiceException(Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE.toUpperCase(),I18NUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE_IP));

        }
        finally {
            //Removing the temporary zip entry in hashmap. and deleting the file
            SERVICEMAP.remove(servicekey);
            tempZipFile.delete();
            if (!successfulextract && extractedFolder != null)
                FileUtil.deleteDir(extractedFolder);
        }
        if(servicesToImport == null || servicesToImport.isEmpty())
            return;//no service to import
        if (resync) {
            for(String appVersion : servicesToImport){
                String appVersionArray[] = appVersion.split(":");
                try {
                    stopAllServices(appVersionArray[0], appVersionArray[1]);
                } catch (FioranoException e) {
                    logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_STOPPING_COMPONENT, e));;
                }
            }
        }
        try{
            int count = 0;
            ArrayList errorServices = new ArrayList();
            ArrayList <File>allServiceDescriptors = new ArrayList();
            allServiceDescriptors.add(extractedFolder);
            findAllFilesInDir(allServiceDescriptors, Constants.SD_XML_FILE_NAME);
            List<String> servicesImported = new ArrayList();
            for(File servDesc: allServiceDescriptors){
                File servDir = servDesc.getParentFile();
                try {
                    //reading the service descriptor.
                    service = ServiceParser.readService(servDesc, true);
                    if(!servicesToImport.contains(service.getGUID() + ":" + service.getVersion())){
                        continue;
                    }
                    List<Resource> newResources = service.getDeployment().getResources();
                    boolean serviceExists  = exists(service.getGUID(), service.getVersion());
                    if(retainOldResources && serviceExists) {
                        try{
                            Service oldService;
                            oldService = microServiceRepository.getServiceInfo(service.getGUID(), ""+service.getVersion());
                            List<Resource> oldResources = oldService.getDeployment().getResources();
                            //remove common resources
                            for (Resource res: newResources)
                                oldResources.remove(res);
                            for(Resource res: oldResources){
                                //copy the old resources (to be retained) to the extracted folder(incoming service)
                                OutputStream writer = null;
                                File resFile = new File(servDir.getAbsolutePath(),res.getName());
                                try{
                                    writer = new FileOutputStream(resFile, true);
                                    addResourceToStream(writer, service.getGUID(), service.getVersion() + "", res.getName());
                                    newResources.add(res);
                                }catch (Exception e){
                                    if(resFile.exists()) //delete unsuccessful resource file
                                        resFile.delete();
                                    //just logging if any error occurs while copying old resources
                                   // //rmi1.warn(Bundle.class, Bundle.ERROR_WHILE_RETAINING_OLD_SPECIFIC_RESOURCES, service.getGUID(), service.getVersion(),res.getName(), e);
                                } finally {
                                    if(writer != null)
                                        writer.close();
                                }
                            }
                        } catch (Exception e){
                            //just logging if any error occurs while copying old resources
                            logger.warn(RBUtil.getMessage(Bundle.class, Bundle.ERROR_WHILE_RETAINING_OLD_RESOURCES, service.getGUID(), service.getVersion(), e));
                        }
                    }
                    //if(serviceExists)
                    //    delete(service.getGUID(), service.getVersion(), resync);

                    service.getDeployment().setResources(new ArrayList());

                    logger.info(RBUtil.getMessage(Bundle.class, Bundle.SAVE_SERVICE_TO_REPO));
                    //saving the service
                    microServiceRepository.saveService(service, true, resync, handleId);

                    //Add the resource and upload the resource files
                    for (Resource resource: newResources) {

                        // resources pointing to file in fioranohome shouldn't be uploaded
                        if (FileUtil4.getFavorite(resource.getName(), FAVORITES) != null){
                            File resFile = new File (resource.getName().replace("$FIORANO_HOME$", ((File)FAVORITES.get("FIORANO_HOME")).getAbsolutePath()));
                            if(resFile.exists()){
                                microServiceRepository.addResource(service.getGUID(), StringUtil.toString(service.getVersion()), resource, handleId);
                                continue;//ignore only if the resource service descriptor is pointing exists
                            }
                        }
                        String oldResourceName = resource.getName();
                        String properResourceName = oldResourceName.substring(oldResourceName.lastIndexOf(File.separatorChar) + 1);
                        resource.setName(properResourceName);
                        microServiceRepository.addResource(service.getGUID(), StringUtil.toString(service.getVersion()), resource, handleId);
                        uploadResource(new File(servDesc.getParentFile(), properResourceName), properResourceName, service.getGUID(), StringUtil.toString(service.getVersion()), handleId);
                    }

                    //Upload Icons if exits.
                    if (service.getIcon16() != null) {
                        File icon16 = new File(servDesc.getParentFile(), service.getIcon16());
                        microServiceRepository.addIcon(service.getGUID(), StringUtil.toString(service.getVersion()), service.getIcon16(), getBytesFromFile(icon16), handleId);
                    }

                    if (service.getIcon32() != null) {
                        File icon32 = new File(servDesc.getParentFile(), service.getIcon32());
                        microServiceRepository.addIcon32(service.getGUID(), StringUtil.toString(service.getVersion()), service.getIcon32(), getBytesFromFile(icon32), handleId);
                    }

                    //Finally commit the service
                    microServiceRepository.publishService(service.getGUID(), StringUtil.toString(service.getVersion()), service.getDeployment().getLabel(), handleId);
                    servicesImported.add(service.getGUID() + "__" + service.getVersion());
                    logger.info(RBUtil.getMessage(Bundle.class, Bundle.DEPLOY_SERVICE_SUCCESSFUL_IP, service.getGUID()));
                    count++;
                } catch (Exception e) {
                    String serviceGUID = (service != null) ?
                            service.getGUID()+":"+service.getVersion() : servDir.getParentFile().getName()+":"+servDir.getName();
                    logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_EXTRACTING_ZIPFILE_UNABLE_TO_SAVE_SERVICE,e));
                    //todo: if service already exists, restore it
                    errorServices.add(serviceGUID);
                } finally {
                    service = null;
                }
            }
            //synchronise the application finally , after importing all the services
            if (count != 0 && resync) {
                for(String appVersion : servicesImported){
                    String appVersionArray[] = appVersion.split("__");
                    try {
                        synchroniseAllDependentApplications(appVersionArray[0], appVersionArray[1]);
                    } catch (FioranoException e) {
                        logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_SYNCHRONIZING_DEPENDENT_APPLICATION, e));
                        errorServices.add(appVersion);
                    }
                }
            }
            if(allServiceDescriptors.size() == 0){
                logger.error(RBUtil.getMessage(Bundle.class,Bundle.SERVICE_DESCRIPTOR_NOT_FOUND));
                throw new ServiceException("SERVICE_DESCRIPTOR_NOT_FOUND");
            }
            if(count == 0){
                logger.error(RBUtil.getMessage(Bundle.class,Bundle.FAILED_TO_IMPORT_SERVICE));
                throw new ServiceException("FAILED_TO_IMPORT_SERVICE");
            } else if(errorServices.size() > 0 ){
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DEPLOY_SERVICE_IP, errorServices.toString()));
                throw new ServiceException("ERROR_DEPLOY_SERVICE");
            }
        }
        finally {
            //deleting the temporary extracted folder
            FileUtil.deleteDir(extractedFolder);
            killRunningInstances = false;
        }
    }

    /**
     * Finds all <code>sdXmlFileName</code> in the given folder and adds the file to <code>allServiceDescriptors</code>
     * @param allServiceDescriptors
     * @param sdXmlFileName
     */
    private static void findAllFilesInDir(ArrayList<File> allServiceDescriptors, final String sdXmlFileName) {
        for (int iterator = 0; iterator<allServiceDescriptors.size();iterator++){
            File currentFile = allServiceDescriptors.get(iterator);
            if(currentFile.getName().equalsIgnoreCase(sdXmlFileName)){
                //found a file name "sdXmlFileName". Proceed to next index
                continue;
            }
            allServiceDescriptors.remove(currentFile);
            if(currentFile.isDirectory()){
                File []files =currentFile.listFiles(new FileFilter(){
                    @Override
                    public boolean accept(File innerFile) {
                        return innerFile.isDirectory() || innerFile.getName().equalsIgnoreCase(sdXmlFileName);
                    }
                });
                allServiceDescriptors.addAll(iterator, Arrays.asList(files));
                //current index is not checked, have to be checked in the next iteration
                --iterator;
            }
        }
    }

    /**
     * Gets zip contents of service(s). In case of multiple service, if one fails to export none of them won't be exported.
     * @param serviceNames  can be a single service name or service name separated by commas like Feeder,Display:4.0,chat:4.0,JMSIn:5.0. If version no mentioned default version will be taken
     * @param defaultVersion
     * @param index Zip contents index
     * @return Zip contents
     * @throws ServiceException
     */
    public byte[] getService(String serviceNames, float defaultVersion, long index) throws ServiceException {

        //when client requests Service, check if the service zip file exists, if so read the contents and send to client in chunks
        //if zip not present, create zip file fetching the contents from server.
        byte[] contents;
        File tempZipFile = null;
        ZipOutputStream zipStream = null;
        BufferedInputStream bis = null;
        boolean completed = false;
        String servicekey = serviceNames.toUpperCase() + "__" + defaultVersion+"__GETSERVICE";

        try {
            if (SERVICEMAP.get(servicekey) == null) {
                //if SERVICEMAP doesnot have the zip file create a new one and add the needed service
                String[] services=serviceNames.trim().split(",");
                tempZipFile = getTempFile("service", "zip");
                zipStream = new ZipOutputStream(new FileOutputStream(tempZipFile));
                for (int i=0;i<services.length;i++) {
                    String[] serviceDetails = services[i].split(":");
                    String id = serviceDetails[0];
                    float version = defaultVersion;
                    if(serviceDetails.length>1) {
                        version = Float.valueOf(serviceDetails[1]);
                    }
                    String versionString = String.valueOf(defaultVersion);
                    Service service = microServiceRepository.getServiceInfo(id, versionString);//will throw exception if service not found
                    //add to zipstream
                    ZipEntry entry = new ZipEntry(id + "/" + versionString + "/" + Constants.SD_XML_FILE_NAME);
                    zipStream.putNextEntry(entry);
                    service.toXMLString(zipStream, false);
                    //zipStream.write(service.toXMLString(ITifConstants.VERSION_NO).getBytes());

                    // Get the icon and add to the zipstream (for launchable services only)
                    if (service.getExecution() != null) {
                        entry = new ZipEntry(id + "/" + versionString + "/" + service.getIcon16());
                        zipStream.putNextEntry(entry);
                        zipStream.write(microServiceRepository.getServiceIcon(id, versionString));

                        if (!(service.getIcon16().equals(service.getIcon32()))) {
                            entry = new ZipEntry(id + "/" + versionString + "/" + service.getIcon32());
                            zipStream.putNextEntry(entry);
                        }
                        zipStream.write(microServiceRepository.getServiceIcon32(id, versionString));

                        List<Port> ports = new ArrayList<Port>();

                        ports.addAll(service.getExecution().getInputPorts());
                        ports.addAll(service.getExecution().getOutputPorts());

                        List<String> schemas = new ArrayList<String>();
                        for (Port port : ports) {
                            Schema schema = port.getSchema();
                            if (schema == null)
                                continue;
                            String schemaContent = schema.getContent();

                            if (schemaContent == null)
                                continue;

                            String schemaFileName = schema.getFile();
                            if (schemaFileName != null && !schemas.contains(schemaFileName)) {
                                schemas.add(schemaFileName);
                                entry = new ZipEntry(id + "/" + versionString + "/" + schemaFileName);
                                zipStream.putNextEntry(entry);
                                zipStream.write(schemaContent.getBytes());
                            }
                        }
                    }

                    //Get the resource files and add to the zipstream
                    @SuppressWarnings("unchecked")
                    Enumeration<Resource> enumRes = Collections.enumeration(service.getDeployment().getResources());
                    while (enumRes.hasMoreElements()) {
                        Resource res = enumRes.nextElement();
                        String resName = res.getName();
                        if (resName.lastIndexOf("/") != -1 && (resName.length() > resName.lastIndexOf("/") + 1))
                            resName = resName.substring(resName.lastIndexOf("/") + 1);
                        entry = new ZipEntry(id + "/" + versionString + "/" + resName);
                        zipStream.putNextEntry(entry);
                        addResourceToStream(zipStream, id, versionString, res.getName());
                    }
                    logger.info(RBUtil.getMessage(Bundle.class,Bundle.EXPORT_SERVICE_SUCCESSFUL_MULTIPLE_IP,id+":"+versionString));
                }
                //when creating zip file completes, add the zip file reference to hashmap with key as services_ID_Version
                SERVICEMAP.put(servicekey, tempZipFile);
            } else
                tempZipFile = SERVICEMAP.get(servicekey);

            //Open buffered input stream to read the zip file as bytes
            bis = new BufferedInputStream(new FileInputStream(tempZipFile));
            bis.skip(index);

            //read the contents in chunks of 1024
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount = bis.read(tempContents);
            if (readCount < 0) {
                //when there is nothing to read notify the client that it is completed.
                completed = true;
                logger.info(RBUtil.getMessage(Bundle.class, Bundle.EXPORT_SERVICE_SUCCESSFUL_IP, serviceNames));
                return null;
            }

            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);

        } catch (Exception e) {
            //if any exception occurs in between this will ensures that temporary files get deleted
            completed = true;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.UNABLE_TO_CREATE_ZIPFILE_GET_SERVICE_IP, serviceNames,e));
            throw new ServiceException("UNABLE_TO_CREATE_ZIPFILE_GET_SERVICE",e.getMessage());
        }
        finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (zipStream != null) {
                    zipStream.closeEntry();
                    zipStream.close();
                }
                if (completed) {
                    SERVICEMAP.remove(servicekey);
                    tempZipFile.delete();
                }

            } catch (IOException ioe) {
                // ignore.
            }
        }
        return contents;
    }

    public Map<String, Float> getDependenciesForService(String id, String version) throws ServiceException {
        Map<String, Float> dependencyMap = new HashMap<String, Float>();
        try {
            Service service = microServiceRepository.getServiceInfo(id, version);
            List references = service.getDeployment().getServiceRefs();
            for (Object ref : references) {
                ServiceRef serviceRef = (ServiceRef) ref;
                _getDependencies(serviceRef.getGUID(), String.valueOf(serviceRef.getVersion()), dependencyMap);
                dependencyMap.put(serviceRef.getGUID(), serviceRef.getVersion());
            }
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_GETTING_SERVICE_DEPENDENCIES, id, version, e));
            throw new ServiceException("ERROR_GETTING_SERVICE_DEPENDENCIES", e);
        }
        return dependencyMap;
    }

    private void _getDependencies(String id, String version, Map<String, Float> dependencyMap) throws FioranoException {
        Service service = microServiceRepository.getServiceInfo(id, version);
        List references = service.getDeployment().getServiceRefs();
        for (Object ref : references) {
            ServiceRef serviceRef = (ServiceRef) ref;
            _getDependencies(serviceRef.getGUID(), String.valueOf(serviceRef.getVersion()), dependencyMap);
            dependencyMap.put(serviceRef.getGUID(), serviceRef.getVersion());
        }
    }

    public void delete(String id, float version, boolean killRunningInstances) throws ServiceException {
        validateHandleID(handleId, "deleteService :" + id);
        try {
            logger.info(RBUtil.getMessage(Bundle.class, Bundle.DELETE_SERVICE_INIT, id));
            String versionString = String.valueOf(version);
            microServiceRepository.removeService(id, versionString, killRunningInstances, handleId);
            this.killRunningInstances = killRunningInstances;
            logger.info(RBUtil.getMessage(Bundle.class, Bundle.DELETE_SERVICE_SUCCESSFUL_IP, id ));
        } catch (FioranoException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DELETE_SERVICE_IP, id), e);
            throw new ServiceException("ERROR_DELETE_SERVICE_IP");
        }
    }
    public boolean dependenciesExists(ServiceReference[] serviceRefs) throws ServiceException {

        boolean exists = true;
        try {
            for (ServiceReference serviceReference : serviceRefs) {
                if (!exists(serviceReference.getId(), serviceReference.getVersion())) {
                    exists = false;
                    break;
                }
            }
        } catch (ServiceException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_DEPENDENCIES_EXISTS), e);
            throw new ServiceException("ERROR_DEPENDENCIES_EXISTS", e);
        }
        return exists;
    }

    public void addServiceRepositoryEventListener(IRepoEventListener listener) throws RemoteException, ServiceException {
        validateHandleID(handleId, "add Service Repository Listner");
        dapiEventManager.registerMicroServiceRepoEventListener(listener, handleId);
    }

    public void removeServiceRepositoryEventListener() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Remove Service Repository Listner");
        dapiEventManager.unRegisterMicroServiceRepoEventListener(handleId);
    }

    public byte[] fetchResourceForService(String id, float version, String resName, long index) throws RemoteException, ServiceException {
        byte[] filecontents = new byte[0];
        //if the version is -1, Get the Highest Version and Convert it to String
        String versionString = String.valueOf(version);
        String servicekey = id.toUpperCase() + "__" + versionString+"__FECTCHRESOURCE";
        File tempFile = null;
        boolean completed = false;
        //If we don't find the any temporary file for resource, Create file
        if (SERVICEMAP.get(servicekey) == null) {
            BufferedOutputStream bos = null;
            try {
                //Create Temporary file
                // Multiple clients if logging-in simultaneously will read temp files created by the other client
                // if they are only named with resource name.
                tempFile = getTempFile("Resource",id+"_"+resName);
                bos = new BufferedOutputStream(new FileOutputStream(tempFile));
                //Add Resource to the File and Add it to the resource Map
                if (resName.equals(Constants.SD_XML_FILE_NAME)) {
                    addServiceDescripterFile(bos, id, versionString);
                } else {
                    addResourceToStream(bos, id, versionString, resName);
                }
                SERVICEMAP.put(servicekey, tempFile);
            } catch (Exception e) {
                //If Any Exception this Marking the file usage as complete and Deleting it
                completed = true;
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_FETCHING_RESOURCE_FROM_SERVICE, id), e);
                throw new ServiceException("ERROR_FETCHING_RESOURCE_FROM_SERVICE");
            } finally {
                try {
                    if (bos != null)
                        bos.close();
                } catch (IOException e) {
                    //ignore
                }
                if (completed && tempFile != null) {
                    SERVICEMAP.remove(servicekey);
                    tempFile.delete();
                }
            }

        } else
            //fetch the file handle from resource Map
            tempFile = SERVICEMAP.get(servicekey);
        BufferedInputStream bis = null;
        try {
            //Open buffered input stream to read the zip file as bytes
            bis = new BufferedInputStream(new FileInputStream(tempFile));
            bis.skip(index);

            //read the contents in chunks of CHUNK_SIZE
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount = bis.read(tempContents);
            if (readCount < 0) {
                //when there is nothing to read notify the client that it is completed.
                completed = true;
                return null;
            }

            filecontents = new byte[readCount];
            System.arraycopy(tempContents, 0, filecontents, 0, readCount);
        } catch (IOException e) {
            //If Any Exception this Marking the file usage as complete and Deleting it
            completed = true;
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_READING_BYTES_FROM_RESOURCEFILE, id), e);
            throw new ServiceException("ERROR_READING_BYTES_FROM_RESOURCEFILE");
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                //ignore
            }
            if (completed && tempFile != null) {
                SERVICEMAP.remove(servicekey);
                tempFile.delete();
            }
        }
        return filecontents;
    }

    @Override
    public String getServiceRepositoryPath() throws RemoteException, ServiceException {
       return microServiceRepository.COMPONENTS_REPOSITORY_FOLDER;
    }

    //adds service Descriptor file to outputStream without CDData Content.
    private void addServiceDescripterFile(OutputStream os, String id, String versionString) throws FioranoException {
        Service service = microServiceRepository.getServiceInfo(id, versionString);
        service.toXMLString(os, false);
    }

    protected String getHighestVersion(String id, String handleID) {
        return microServiceRepository.getHighestVersionOfService(id);
    }
    /*------------------------------------[Private Methods]-------------------------------------------------------*/

    private void addResourceToStream(OutputStream stream, String id, String versionString, String resName)
            throws IOException, FioranoException {
        boolean completed;
        int index = 0;
        do {
            DmiResourceData resourceData = microServiceRepository.getDataForResource(id, versionString, resName, index, Constants.CHUNK_SIZE);
            byte[] data = resourceData.getData();
            stream.write(data);
            index += Constants.CHUNK_SIZE;
            completed = resourceData.getResourceLength() < index;
        } while (!completed);
    }

    private boolean uploadResource(File file, String resName, String serviceGUID, String version, String handleId) throws FioranoException {

        RandomAccessFile ras = null;
        boolean fileComplete = false;
        try {
            ras = new RandomAccessFile(file, "r");
            long fileLength = ras.length();
            // We have to transfer the file in equal chunks and these will be
            // assembled by the server and the file will be created

            long totalBytesRead = 0;

            while (!fileComplete) {
                byte[] bytes = new byte[Constants.CHUNK_SIZE];
                int numbytesRead = ras.read(bytes);

                if (numbytesRead < Constants.CHUNK_SIZE) {
                    if (numbytesRead == -1)
                        numbytesRead = 0;
                    byte[] toSend = new byte[numbytesRead];

                    System.arraycopy(bytes, 0, toSend, 0, numbytesRead);
                    if ((totalBytesRead + numbytesRead) == fileLength) {
                        fileComplete = true;
                    }

                    microServiceRepository.updateServiceResources(serviceGUID, version, resName, totalBytesRead, toSend, fileComplete, handleId);
                    totalBytesRead += numbytesRead;
                } else {
                    if ((totalBytesRead + Constants.CHUNK_SIZE) == fileLength) {
                        fileComplete = true;
                    }

                    microServiceRepository.updateServiceResources(serviceGUID, version, resName, totalBytesRead, bytes, fileComplete, handleId);
                    totalBytesRead += Constants.CHUNK_SIZE;
                }
            }
        }
        catch (IOException e) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_UPLOAD_RESOURCE, resName, serviceGUID), e);
            throw new FioranoException("ERROR_UPLOAD_RESOURCE");
        }
        finally {
            if (ras != null)
                try {
                    ras.close();
                } catch (IOException e) {
                    //ignore
                }
        }
        return fileComplete;
    }

    private void stopAllServices(String serviceGUID, String version) throws FioranoException {
        stopAllDirectDependentServices(serviceGUID, version);
        stopAllOtherDependentServices(serviceGUID, version);
    }

    public void stopAllOtherDependentServices(String serviceGUID, String version) throws FioranoException {
        HashMap<String, ArrayList<String>> directDependencyMap = new HashMap<String, ArrayList<String>>();    // Key : componentGUID , Value : List of all the components which depends directly on component with GUID as componentGUID
        Enumeration<Service> componentList = microServiceRepository.getAllServicesInRepository();

        // The while loop iterates over all the components in peer repository . The component entry is added in the directDependencyMap in the lists corresponding to each of its service references.
        while (componentList.hasMoreElements()) {
            Service service = componentList.nextElement();
            String componentGUID = service.getGUID();
            float componenetVersion = service.getVersion();
            List serviceRefList = microServiceRepository.getServiceInfo(componentGUID, String.valueOf(componenetVersion)).getDeployment().getServiceRefs();
            Iterator itr = serviceRefList.iterator();
            // Go to List corresponding to each serviceRef and add the component in the list if its already not there
            while (itr.hasNext()) {
                ServiceRef serviceRef = ((ServiceRef) itr.next());
                String serviceRefGUID = serviceRef.getGUID();
                float serviceRefVersion = serviceRef.getVersion();
                // get the list of all the components which depends on serviceRefGUID and add componentGUID to the list ,if its not already present in the list
                ArrayList<String> list = directDependencyMap.get(serviceRefGUID + "_" + serviceRefVersion);
                if (list == null) {
                    list = new ArrayList<String>();
                    list.add(componentGUID+"_"+componenetVersion);
                    directDependencyMap.put(serviceRefGUID + "_" + serviceRefVersion, list);
                } else {
                    if (!list.contains(componentGUID))
                        list.add(componentGUID+"_"+componenetVersion);
                }
            }
        }

        if (directDependencyMap.get(serviceGUID + "_" + version) == null)
            return ;

        ArrayList<String> finalDependentsList = new ArrayList<>();  // The list will contain all the services which directly or indirectly depends on serviceGUID,version

        // auxiliary data structres
        ArrayList<String> tempList1 = new ArrayList<>();
        ArrayList<String> tempList2 = new ArrayList<>();
        ArrayList<String>[] tempLists = new ArrayList[2];
        tempList1.addAll(directDependencyMap.get(serviceGUID + "_" + version)); // tempList1 is intialized to all the services which directly depends on 'serviceGUID'
        int i = 0;
        tempLists[0] = tempList1;
        tempLists[1] = tempList2;
        ArrayList<String> dependentsList = new ArrayList<String>();

        // add all the services in tempList[i] to finalDependencyList . Calculate the services which depends on the services in tempList[i] and are not already added to finalependencyList and put them in tempList[(i+1)%2] .
        // Do this till there are no more services to be added from tempList[i]

        // Example : lets say directDependencyMap is  A->L
        //                                            B->M
        //                                            C->A,B
        //                                            D->A
        //                                            E->C,D

        //    We want to find all the dependencies of E
        //    Before the loop ,tempList[i]=C,D and tempList[i+1%2] ,dependencyList ,finalDependencyList are empty
        // Outer loop
        // Iteration1 :  finalDependencyList = C,D
        //
        // Inner Loop
        //   Iteration1 :
        //             service : C
        //             dependencyList : A,B
        //             tempList[(i + 1) % 2] = A,B
        //   Iteration2 :
        //            service : D
        //            dependencyList : A after step  "dependencyList.addAll(directDependencyMap.get(service))"
        //            dependencyList : A after step "dependencyList.removeAll(finalDependencyList)"
        //            dependencyList  : empty after step "dependencyList.removeAll(tempLists[(i + 1) % 2])" since tempList[(i+1)%2] already contains A
        //            tempList[(i + 1) % 2] = A,B

        // Iteration2 (outer loop)
        // tempList[i] = A,B
        // finalDependencyList =C,D,A,B
        // Inner Loop
        //   Iteration1 :
        //             service : A
        //             dependencyList : L
        //             tempList[(i + 1) % 2] = L
        //   Iteration2 :
        //            service : B
        //            dependencyList : M
        //            tempList[(i + 1) % 2] = L,M
        // Iteration3 (outer loop)
        // tempList[i] = L,M
        // finalDependencyList =C,D,A,B,L,M
        // Inner Loop
        //   Iteration1 :
        //             service : L
        //             returns  from check "if(directDependencyMap.get(service) == null)" as there is no entry for "L" in directDependecyMap
        //
        //   Iteration2 :
        //            service : M
        //            returns  from check "if(directDependencyMap.get(service) == null)" as there is no entry for "L" in directDependecyMap
        //   So nothing gets added from tempList[i] to tempList[(i+1)%2] so the outer loop will at the start of next iteration
        //
        // After the execution of outer loop , finalDependencyMap = C,D,A,B,L,M

        while (!tempLists[i].isEmpty()) {
            finalDependentsList.addAll(tempLists[i]);
            Iterator<String> itr = tempLists[i].iterator();
            // The loop iterates over all the services in tempList[i] and feches the list of services which directly depend on the current service and add the services from the list to tempList[i+1%2] which are not present in finalDependencyList
            while (itr.hasNext()) {
                String service = itr.next();
                if (directDependencyMap.get(service) == null)
                    continue;
                dependentsList.addAll(directDependencyMap.get(service));
                dependentsList.removeAll(finalDependentsList);  // removes all the services from dependencyList which are already present in finalDepencyList
                dependentsList.removeAll(tempLists[(i + 1) % 2]); // removes all the services from dependencyList which are already present in tempLists[(i + 1) % 2]
                tempLists[(i + 1) % 2].addAll(dependentsList);
                dependentsList.clear();
            }
            tempLists[i].clear();  // clear the tempList[i]
            i = (i + 1) % 2;
        }



        // adds the ComponentHandle of all the running components which are also prensent in finalDependencyList to dependentRunningComponentList
        for (ApplicationHandle appHandle : applicationController.getApplicationHandles().values()) {
            Enumeration<ServiceInstanceStateDetails> appServiceStateDetails = appHandle.getApplicationDetails().getAllServiceStateDetails().elements();
            while (appServiceStateDetails.hasMoreElements()) {
                ServiceInstanceStateDetails sisd = appServiceStateDetails.nextElement();
                if (finalDependentsList.indexOf( sisd.getServiceGUID()+ "_" + sisd.getRunningVersion()) != -1 && appHandle.isMicroserviceRunning(sisd.getServiceInstanceName()))  // add to the the runningComponentList if the componet is present in finalDependencyList
                    appHandle.stopMicroService(sisd.getServiceInstanceName());
            }
        }
    }

    private void stopAllDirectDependentServices(String serviceGUID, String version) {
        Map<String, ApplicationHandle> applicationHandles = applicationController.getApplicationHandles();
        for(ApplicationHandle appHandle : applicationHandles.values()){
            Hashtable<String, ServiceInstanceStateDetails> allServiceStateDetails = null;
            try {
                allServiceStateDetails = appHandle.getApplicationDetails().getAllServiceStateDetails();
            } catch (FioranoException e) {
                logger.error("Error occurred while getting the application state details.");
            }
            for(ServiceInstanceStateDetails sisd : allServiceStateDetails.values()){
                if(sisd.getServiceGUID().equals(serviceGUID) && sisd.getRunningVersion().equals(version) && appHandle.isMicroserviceRunning(sisd.getServiceInstanceName())){
                    appHandle.stopMicroService(sisd.getServiceInstanceName());
                }
            }
        }
    }

    private void synchroniseAllDependentApplications(String serviceGUID, String version) throws FioranoException{
        Set<String> listOfRunningApplications = applicationController.getListOfRunningApplications();
        HashMap<String, List<String>> directDependencyMap = getDependencyMap();
        for (String appGUID:listOfRunningApplications){
            String[] appGuidAndVer = LookUpUtil.returnAppGUIDAndVersion(appGUID);
            ApplicationHandle applicationHandle = applicationController.getApplicationHandle(appGuidAndVer[0], Float.valueOf(appGuidAndVer[1]), handleId);
            //we need to synchronise the application if any one of the Event process component depends directly or indirectly on this ServiceGUID
            if (isEventProcessDependsOnServiceGUID(applicationHandle, serviceGUID, version, directDependencyMap)) {
                applicationController.synchronizeApplication(applicationHandle.getApplication().getGUID(), String.valueOf(applicationHandle.getApplication().getVersion()), handleId);
            }
        }
    }

    private void synchroniseAllDependentApplicationsMultiple(String serviceGUID, String version) throws FioranoException{
        Set<String> listOfRunningApplications = applicationController.getListOfRunningApplications();
        HashMap<String, List<String>> directDependencyMap = getDependencyMap();
        for(String appGUID: listOfRunningApplications){
            String[] appGuidAndVer = LookUpUtil.returnAppGUIDAndVersion(appGUID);
            ApplicationHandle applicationHandle = applicationController.getApplicationHandle(appGuidAndVer[0], Float.valueOf(appGuidAndVer[1]), handleId);
            //we need to synchronise the application if any one of the Event process component depends directly or indirectly on this ServiceGUID
            if (isEventProcessDependsOnServiceGUID(applicationHandle, serviceGUID, version, directDependencyMap)) {
                applicationController.synchronizeApplication(applicationHandle.getApplication().getGUID(), String.valueOf(applicationHandle.getApplication().getVersion()), handleId);
                applicationController.startAllMicroServices(applicationHandle.getApplication().getGUID(), String.valueOf(applicationHandle.getApplication().getVersion()), handleId);
            }
        }
    }


    public boolean isEventProcessDependsOnServiceGUID(ApplicationHandle applicationHandle, String serviceGUID, String version, HashMap<String, List<String>> directDependencyMap) throws FioranoException{

        if (isAnyDirectService(applicationHandle, serviceGUID, version)) {
            return true;
        }

        if (directDependencyMap.get(serviceGUID + "_" + version) == null)
            return false;

        // The list will contain all the services which directly or indirectly depends on serviceGUID,version
        ArrayList<String> finalDependentsList = new ArrayList<String>();

        // auxiliary data structres
        ArrayList<String> tempList1 = new ArrayList<String>();
        ArrayList<String> tempList2 = new ArrayList<String>();
        ArrayList<String>[] tempLists = new ArrayList[2];
        tempList1.addAll(directDependencyMap.get(serviceGUID + "_" + version)); // tempList1 is initialized to all the services which directly depends on 'serviceGUID'
        int i = 0;
        tempLists[0] = tempList1;
        tempLists[1] = tempList2;
        ArrayList<String> dependentsList = new ArrayList<String>();

        while (!tempLists[i].isEmpty()) {
            finalDependentsList.addAll(tempLists[i]);
            Iterator<String> itr = tempLists[i].iterator();
            // The loop iterates over all the services in tempList[i] and fetches the list of services which directly depend on the current service and add the services from the list to tempList[i+1%2] which are not present in finalDependencyList
            while (itr.hasNext()) {
                String service = itr.next();
                if (directDependencyMap.get(service) == null)
                    continue;
                dependentsList.addAll(directDependencyMap.get(service));
                dependentsList.removeAll(finalDependentsList);  // removes all the services from dependencyList which are already present in finalDepencyList
                dependentsList.removeAll(tempLists[(i + 1) % 2]); // removes all the services from dependencyList which are already present in tempLists[(i + 1) % 2]
                tempLists[(i + 1) % 2].addAll(dependentsList);
                dependentsList.clear();
            }
            tempLists[i].clear();  // clear the tempList[i]
            i = (i + 1) % 2;
        }
        List<ServiceInstance> serviceInstances = applicationHandle.getApplication().getServiceInstances();
        for (ServiceInstance serviceInstance : serviceInstances) {
            if (finalDependentsList.indexOf( serviceInstance.getGUID() + "_" + serviceInstance.getVersion()) != -1) {
                return true;
            }
        }

        return false;
    }

    private HashMap<String, List<String>> getDependencyMap() throws FioranoException {
        HashMap<String, List<String>> directDependencyMap = new HashMap<String, List<String>>();    // Key : componentGUID , Value : List of all the components which depends directly on component with GUID as componentGUID

        Enumeration componentList = microServiceRepository.getAllServicesInRepository();

        // The while loop iterates over all the components in  repository . The component entry is added in the directDependencyMap in the lists corresponding to each of its service references.
        while (componentList.hasMoreElements()) {
            Service service = (Service) componentList.nextElement();
            List serviceRefList = service.getDeployment().getServiceRefs();
            Iterator itr = serviceRefList.iterator();
            // Go to List corresponding to each serviceRef and add the component in the list if its already not there
            while (itr.hasNext()) {
                ServiceRef serviceRef = ((ServiceRef) itr.next());
                String serviceRefGUID = serviceRef.getGUID();
                float serviceRefVersion = serviceRef.getVersion();
                // get the list of all the components which depends on serviceRefGUID and add service to the list ,if its not already present in the list
                List<String> list = directDependencyMap.get(serviceRefGUID + "_" + serviceRefVersion);
                final String serviceName = service.getGUID();
                final float serviceVersion = service.getVersion();
                if (list == null) {
                    list = new ArrayList<String>();
                    list.add(serviceName + "_" + serviceVersion);
                    directDependencyMap.put(serviceRefGUID + "_" + serviceRefVersion, list);
                } else {
                    if (!list.contains(serviceName))
                        list.add(serviceName + "_" + serviceVersion);
                }
            }
        }
        return directDependencyMap;
    }
    /**
     *  Returns true if any of Event Process component depends directly on serviceGUID
     *
     * @param serviceGUID Service GUID
     * @param version Service Version
     * @return boolean Description of return
     */
    private boolean isAnyDirectService(ApplicationHandle applicationHandle, String serviceGUID,
                                       String version) throws FioranoException
    {
            Application alp = applicationHandle.getApplication();
            List<ServiceInstance> serviceInstances = alp.getServiceInstances();
            for (ServiceInstance serInstance : serviceInstances ) {
                if(serInstance.getGUID().equalsIgnoreCase(serviceGUID) &&
                        String.valueOf(serInstance.getVersion()).equalsIgnoreCase(version))
                    return true;
            }
        return false;
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());
    }

    public String toString() {
        return Constants.MICRO_SERVICE_MANAGER;
    }

    private IServiceManager clientProxyInstance;

    void setClientProxyInstance(IServiceManager clientProxyInstance) {
        this.clientProxyInstance = clientProxyInstance;
    }

    IServiceManager getClientProxyInstance() {
        return clientProxyInstance;
    }
 }
