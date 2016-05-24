/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.repository;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiResourceData;
import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.service.*;
import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.events.MicroServiceRepoUpdateEvent;
import com.fiorano.openesb.microservice.bundle.Activator;
import com.fiorano.openesb.microservice.launch.MicroServiceRepoEventRaiser;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MicroServiceRepoManager {

    private static final MicroServiceRepoManager MICRO_SERVICE_REPOSITORY_MANAGER = new MicroServiceRepoManager();
    private Logger logger;
    public static final String TEMP_DOWNLOAD_DIR = "tmp";
    private Hashtable<String, Service> m_committedServiceVsProperties;
    private Hashtable<String, Service> m_nonCommittedServiceVsProperties;
    private static final Map favorites = Collections.singletonMap("FIORANO_HOME", System.getProperty("user.dir"));
    public static String COMPONENTS_REPOSITORY_FOLDER = System.getProperty("user.dir") + "/esb/server/repository/components";
    private boolean msRepoInSync = false;
    private final Object waitObject;

    private MicroServiceRepoManager() {
        waitObject = new Object();
        logger = LoggerFactory.getILoggerFactory().getLogger(Activator.class.getName());
        // Create new hashtables for the properties
        m_committedServiceVsProperties = new Hashtable<>();
        m_nonCommittedServiceVsProperties = new Hashtable<>();

        COMPONENTS_REPOSITORY_FOLDER = getRepositoryLocation();
        try {
            initialize();
        } catch (FioranoException e) {
            logger.error("Error occurred while initializing " +
                    "Microservice repository", e);
        }
    }

    public static MicroServiceRepoManager getInstance() {
        return MICRO_SERVICE_REPOSITORY_MANAGER;
    }

    public String getRepositoryLocation() {
        return ServerConfig.getConfig().getRepositoryPath() + File.separator + MicroServiceConstants.MICRO_SERVICE_REPOSITORY_NAME;
    }

    /**
     * Returns service property sheet for the parameter component.
     * Returns null if the component is not present in the  repository
     *
     * @param microServiceId specifies a microservice uniquely.
     * @param version        specifies the version of microservice.
     * @return ServicePropertySheet
     * @throws FioranoException
     */
    public Service readMicroService(String microServiceId, String version)
            throws FioranoException {
        File file = new File(getMicroServiceBase(microServiceId, version)
                + File.separator + "ServiceDescriptor.xml");
        if (!file.exists()) {
            throw new FioranoException("Component " + microServiceId + ":" + version + " is not present in repository");
        }
        return ServiceParser.readService(file);
    }

    public String getMicroServiceBase(String microServiceId, String version) {
        return getRepositoryLocation() + File.separator + microServiceId + File.separator + version;
    }

    protected void initialize() throws FioranoException {
        msRepoInSync = true;
        reloadRepository();
        msRepoInSync = false;
        synchronized (waitObject) {
            waitObject.notifyAll();
        }
    }

    /**
     * This API is used to get the resource in parts. Chunks of numBytes
     * and if the last chunk is not of the size and less than it then
     * create a appropriate sized byte array and return it inside DMIResourceData.
     * Else the chunk ofsize numBytes is returned as DMIResourceData.
     *
     * @param serviceGUID Description of the Parameter
     * @param version     Description of the Parameter
     * @param resName     Description of the Parameter
     * @param index       Description of the Parameter
     * @param numBytes    Description of the Parameter
     * @return The dataForResource value
     * @throws FioranoException Description of the Exception
     */
    public DmiResourceData getDataForResource(String serviceGUID, String version,
                                              String resName, long index, int numBytes)
            throws FioranoException {
        try {
            getServiceInfo(serviceGUID, version);
            File resFile;
            File serviceDescriptorFile = null;
            BufferedInputStream bis;
            long fileLength;
            // fetch the resource file
            if (resName.equals(MicroServiceConstants.SERVICE_DESCRIPTOR_FILE_NAME)) {
                Service serv = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
                if (serv == null) {
                    throw new FioranoException("Error occurred while reading service");
                }
                serviceDescriptorFile = File.createTempFile("Fiorano.ServiceDescriptor", "xml");
                FileOutputStream outstream = new FileOutputStream(serviceDescriptorFile);
                serv.toXMLString(outstream);
                outstream.close();
                bis = new BufferedInputStream(new FileInputStream(serviceDescriptorFile));
                fileLength = serviceDescriptorFile.length();
            } else {
                resFile = fetchResourceFile(serviceGUID, version, resName);
                bis = new BufferedInputStream(new FileInputStream(resFile));
                fileLength = resFile.length();
            }

            int numBytesRead;
            byte[] bytes;
            DmiResourceData dmiResData;

            try {
                bytes = new byte[numBytes];
                bis.skip(index);
                numBytesRead = bis.read(bytes);
            } finally {
                bis.close();
                if (serviceDescriptorFile != null)
                    serviceDescriptorFile.delete();
            }

            dmiResData = new DmiResourceData();
            if (numBytesRead != numBytes) {
                if (numBytesRead < 0)
                    numBytesRead = 0;

                byte[] newByteArray = new byte[numBytesRead];

                System.arraycopy(bytes, 0, newByteArray, 0, numBytesRead);
                dmiResData.setData(newByteArray);
            } else
                dmiResData.setData(bytes);

            dmiResData.setStartOffset(index);
            dmiResData.setEndOffset(index + numBytesRead);
            dmiResData.setResourceLength(fileLength);
            dmiResData.setResourceName(resName);
            return dmiResData;
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }


    /**
     * Gets list of all the resources for a service.
     *
     * @param serviceGUID The GUID of the service for which resource
     *                    info is to be retrieved
     * @param version     The version of the service for which resource
     *                    info is to be retrieved
     * @return An enumeration containing Resource objects
     * for all the resources of the service
     * @throws FioranoException Description of the Exception
     */
    public Enumeration<Resource> getAllResourcesForService(String serviceGUID, String version)
            throws FioranoException {
        try {
            Service sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if(sps == null) throw new FioranoException("Error fetching service " + getUniqueKey(serviceGUID,version));
            Deployment deployment = sps.getDeployment();
            return Collections.enumeration(deployment.getResources());
        } catch (Exception ex) {
            throw new FioranoException(ex);
        }

    }


    /**
     * Gets info about all the service that are present in the service
     * repository
     *
     * @return Enumeration of ServicePropertySheet objects
     */
    public Enumeration<Service> getAllServicesInRepository() {
        synchronized (waitObject) {
            try {
                if (msRepoInSync)
                    waitObject.wait();
            } catch (InterruptedException e) {
                //Ignore
            }
        }
        return m_committedServiceVsProperties.elements();
    }

    public Enumeration getAllUnCommittedServices() {
        synchronized (waitObject) {
            try {
                if (msRepoInSync)
                    waitObject.wait();
            } catch (InterruptedException e) {
                //Ignore
            }
        }
        return m_nonCommittedServiceVsProperties.elements();
    }


    public Enumeration getAllVersionsOfService(String serviceGUID, boolean nonCommittedAlso) {
        Vector<ServiceReference> allVersionsOfServices = new Vector<>();
        Service sps;

        if (nonCommittedAlso) {
            Enumeration nonCommittedServices = m_nonCommittedServiceVsProperties.keys();

            while (nonCommittedServices.hasMoreElements()) {
                String key = (String) nonCommittedServices.nextElement();
                if (!MSRepoUtil.getGUIDfromUniqueKey(key).equalsIgnoreCase(serviceGUID))
                    continue;
                sps = m_nonCommittedServiceVsProperties.get(key);
                allVersionsOfServices.addElement(new ServiceReference(sps));
            }
        }

        Enumeration committedServices = m_committedServiceVsProperties.keys();

        while (committedServices.hasMoreElements()) {
            String key = (String) committedServices.nextElement();
            if (!MSRepoUtil.getGUIDfromUniqueKey(key).equalsIgnoreCase(serviceGUID))
                continue;
            sps = m_committedServiceVsProperties.get(key);
            allVersionsOfServices.addElement(new ServiceReference(sps));

        }
        // sort
        return sortAccordingToVersionNumber(allVersionsOfServices).elements();
    }

    /**
     * Returns the highest available version for a service
     *
     * @param serviceGUID Description of the Parameter
     * @return The highestVersionOfService value
     */
    public String getHighestVersionOfService(String serviceGUID) {
        String highestVersion = null;
        Enumeration _enum = getAllVersionsOfService(serviceGUID, false);

        while (_enum.hasMoreElements()) {
            ServiceReference hdr = (ServiceReference) _enum.nextElement();

            if (highestVersion == null) {
                highestVersion = hdr.getVersion() + "";
                continue;
            }
            if (highestVersion.compareTo(hdr.getVersion() + "") < 0)
                highestVersion = hdr.getVersion() + "";
        }
        return highestVersion;
    }


    /**
     * Gets information about a service from the service repository
     *
     * @param serviceGUID The GUID of the service for which properties
     *                    are to be retrieved
     * @param version     The version of the service for which
     *                    properties are to be retrived
     * @return ServicePropertySheet object containing info
     * about the service
     * @throws FioranoException Description of the Exception
     */
    public Service getServiceInfo(String serviceGUID, String version)
            throws FioranoException {
        Service sps;

        if (serviceGUID == null)
            throw new FioranoException("service guid null");

        if (version == null)
            version = getHighestVersionOfService(serviceGUID);
        synchronized (waitObject) {
            try {
                if (msRepoInSync)
                    waitObject.wait();
            } catch (InterruptedException e) {
                //Ignore
            }
        }
        sps = m_nonCommittedServiceVsProperties.get(getUniqueKey(serviceGUID, version));
        if (sps == null)
            sps = m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version));

        if (sps == null)
            throw new FioranoException("sps null");
            //LogHelper.getErrMessage(ILogModule.SERVICE_REPOSITORY, 14, serviceGUID, version));

        else
            return sps;
    }

    /**
     * Returns a byte array consisting the icon for a particular service
     *
     * @param serviceGUID The GUID of the service for which icon is to
     *                    be returned
     * @param version     The version of the service for which icon is
     *                    to be returned
     * @return The serviceIcon value
     * @throws FioranoException Description of the Exception
     */
    public byte[] getServiceIcon(String serviceGUID, String version)
            throws FioranoException {
        try {
            if (version == null || version.trim().equals(""))
                version = getHighestVersionOfService(serviceGUID);

            Service sps = getServiceInfo(serviceGUID, version);

            String iconName = sps.getIcon16();
            File icon = MSRepoUtil.getIcon(serviceGUID, version, iconName);
            byte[] b = MSRepoUtil.getBytesFromFile(icon);
            if (b == null)
                logger.trace("Icon file is empty");
            return b;
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }


    /**
     * Returns a byte array consisting the icon for a particular service
     *
     * @param serviceGUID The GUID of the service for which icon is to
     *                    be returned
     * @param version     The version of the service for which icon is
     *                    to be returned
     * @return The serviceIcon value
     * @throws FioranoException Description of the Exception
     */
    public byte[] getServiceIcon32(String serviceGUID, String version)
            throws FioranoException {
        try {
            if (version == null || version.trim().equals(""))
                version = getHighestVersionOfService(serviceGUID);

            Service sps = getServiceInfo(serviceGUID, version);

            String iconName = sps.getIcon32();

            // Assign the small icon in case the icon is not found.
            if (iconName == null || iconName.equalsIgnoreCase("")) {
                //if(TifTrace.ServiceRepository > TraceLevels.Info)
                //    LogHelper.logErr(ILogModule.SERVICE_REPOSITORY,18 ,serviceGUID, version);
                //logger.info(Bundle.class,Bundle.SERVICE_32_ICON_NOT_FOUND,serviceGUID,version);
                iconName = sps.getIcon16();
            }

            File icon = MSRepoUtil.getIcon(serviceGUID, version, iconName);
            byte[] b = MSRepoUtil.getBytesFromFile(icon);
            if (b == null)
                System.out.println("file found empty");
            return b;

        } catch (Exception e) {
//            logger.error(Bundle.class,Bundle.ERROR_FETCH_SERVICE_ICON,serviceGUID,version,e);        //logging in FESStubManager
            throw new FioranoException(e);
            //throw new FioranoException("error getting service icon32",e);
        }
    }

    /**
     * Updates the resources for a service. The bytes are added on to the file
     * for which they are meant. As soon as the final packet is added into the
     * file, the file is moved from the "partial" folder to the complete
     * folder. If any packet for this file is received even after this, it is
     * not updated.
     *
     * @param serviceGUID  Description of the Parameter
     * @param version      Description of the Parameter
     * @param resName      Description of the Parameter
     * @param startByte    Description of the Parameter
     * @param bytes        Description of the Parameter
     * @param isLastPacket Description of the Parameter
     * @param handleID     Description of the Parameter
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("UnusedParameters")
    public void updateServiceResources(String serviceGUID, String version,
                                       String resName, long startByte,
                                       byte[] bytes, boolean isLastPacket, String handleID)
            throws FioranoException {
        try {
            Service sps = getServiceInfo(serviceGUID, version);
            Iterator resources = sps.getDeployment().getResources().iterator();
            boolean fileAlreadyCompleted = false;
            String servicePath = MSRepoUtil.getServicePath(serviceGUID, version);
            String compFolderName = MSRepoUtil.getCompletedResourceFolder(serviceGUID, version);
            boolean validResource = false;

            while (resources.hasNext()) {
                Resource res = (Resource) resources.next();
                String resourceName = res.getName();

                if (resourceName == null || !resourceName.equalsIgnoreCase(resName))
                    continue;

                // resource name specified is valid
                validResource = true;

                // check for the existance of the file in the folder
                // which contains completed files
                String fileName = compFolderName + File.separator + resourceName;
                File file = new File(fileName);

                if (file.exists())
                    fileAlreadyCompleted = true;
                break;
            }

            // raise exception in case of invalid resource
            if (!validResource)
                throw new FioranoException("invalid resource name");
            //LogHelper.getErrMessage(ILogModule.SERVICE_REPOSITORY, 31, resName, serviceGUID));

            // if the file for which data is to be written is already completely
            // written, we need not write the data that we have received

            // Added another condition that if the file is complete and
            // the service has been committed, then we need to move the file
            // from "tmp" folder to the service folder
            if (fileAlreadyCompleted && m_committedServiceVsProperties.containsKey(getUniqueKey(serviceGUID, version))) {
                //LogHelper.logErr(ILogModule.SERVICE_REPOSITORY,21 ,serviceGUID, version, resName, servicePath);
                //logger.debug(Bundle.class,Bundle.MOVE_TEMP_SERVICE,serviceGUID, version, resName, servicePath);
                moveCompletedResourceFilesToRepository(serviceGUID, version);
                new File(servicePath + File.separator + TEMP_DOWNLOAD_DIR).delete();
            }

            updatePartialResourceData(serviceGUID, version, resName, startByte, bytes);

            // if this was the last packet for this file, then we need
            // to move this file from the "partial" folder to
            // "complete" folder
            if (isLastPacket) {
                moveResourceFromPartialToCompleted(serviceGUID, version, resName);
                // again, if the service is committed, then move the file from
                // tmp to the service folder
                if (m_committedServiceVsProperties.containsKey(getUniqueKey(serviceGUID, version))) {
                    moveCompletedResourceFilesToRepository(serviceGUID, version);
                    new File(servicePath + File.separator + TEMP_DOWNLOAD_DIR).delete();
                }
                //generateMicroServiceRepoUpdateEvent(serviceGUID, version, resName, MicroServiceRepoUpdateEvent.RESOURCE_UPLOADED);
                MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, resName,
                        MicroServiceRepoUpdateEvent.RESOURCE_UPLOADED, Event.EventCategory.INFORMATION, "");
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }


    /**
     * This method checks if a resource exists in a specified service in the
     * TES repository.
     *
     * @param serviceGUID The GUID of the service from which the
     *                    resource's existance is to be checked.
     * @param version     The version of the service from which the
     *                    resource's existance is to be checked.
     * @param resName     The name of the resource to be checked.
     * @return The boolean value indicating if the resource
     * file is present in the service in TES repository.
     * @throws FioranoException if the task of checking resource from service
     *                          repository fails to succeed.
     * @since Tifosi2.0
     */
    public boolean hasResource(String serviceGUID, String version, String resName)
            throws FioranoException {
        try {
            // check the existance of the service
            try {
                getServiceInfo(serviceGUID, version);
            } catch (FioranoException e) {
                return false;
            }
            String resFilePath = getResourcePath(serviceGUID, version, resName);

            File resFile = new File(resFilePath);
            if (resFile.exists())
                return true;
        } catch (Exception e) {
//            logger.error(Bundle.class,Bundle.ERROR_CHECK_IF_RES_PRESENT,serviceGUID,version,resName,e);          //logging in FESStubManager
            throw new FioranoException(e);
        }

        return false;
    }


    /**
     * Saves a service using the service property sheet.
     *
     * @param sps                  The service property sheet that needs to
     *                             be saved in the service repository sheet
     * @param deleteOldService     delete old service if exists
     * @param killRunningInstances option considered only if above is true
     *                             All the running instances will be stopped
     * @param handleID             HandleId holds the information about authentication
     * @return New header of the service that has been
     * edited
     * @throws FioranoException Exception if, specified GUID and version
     *                          already exist.
     */
    @SuppressWarnings("UnusedParameters")
    public ServiceReference saveService(Service sps, boolean deleteOldService, boolean killRunningInstances, String handleID)
            throws FioranoException {
        //SECURITY CHECK
        boolean serviceDeleted = false;
        try {
            String serviceGUID = sps.getGUID();

            if (sps.getVersion() <= 0)
                throw new FioranoException("component version invalid");

            String version = String.valueOf(sps.getVersion());

            if (m_committedServiceVsProperties.get(getUniqueKey(sps.getGUID(), sps.getVersion() + "")) != null) {
                if (!deleteOldService)
                    throw new FioranoException("service with same version already exists");
                //LogHelper.getErrMessage(ILogModule.SERVICE_REPOSITORY, 48, sps.getGUID(), "" + sps.getVersion()));
                //delete if service is present in the committed list
                File fileVersion = new File(MSRepoUtil.getServicePath(serviceGUID, version));
                if (!fileVersion.exists())
                    throw new FioranoException("ERROR_SERVICE_VERSION_DOES_NOT_EXIST");

                removeServiceVersion(serviceGUID, version);

                serviceDeleted = true;
            }

            String servicePath = MSRepoUtil.getServicePath(sps.getGUID(), version);
            new File(servicePath).mkdirs();
            MSRepoUtil.saveServiceDescriptor(sps);

            // Now we create the directory structure in the partial and
            // complete folder in the tmp folder where the directory structure for the component will be created.
            String partFileFolder = MSRepoUtil.getPartialResourceFolder(serviceGUID, version);
            new File(partFileFolder).mkdirs();
            String compFileFolder = MSRepoUtil.getCompletedResourceFolder(serviceGUID, String.valueOf(sps.getVersion()));
            new File(compFileFolder).mkdirs();

            try {
                MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            } catch (Exception e) {
                removeService(serviceGUID, version, true, null);
                throw new FioranoException(e);
            }

            // add the sps info into the in memory table
            m_nonCommittedServiceVsProperties.put(getUniqueKey(sps.getGUID(), sps.getVersion() + ""), sps);
            if (serviceDeleted) {
                // generate service updation event
                //generateMicroServiceRepoUpdateEvent(sps.getGUID(), "" + sps.getVersion(), MicroServiceRepoUpdateEvent.SERVICE_OVERWRITTEN);
                MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                        MicroServiceRepoUpdateEvent.SERVICE_OVERWRITTEN, Event.EventCategory.INFORMATION, "");

            } else {
                // generate service updation event
                //generateMicroServiceRepoUpdateEvent(sps.getGUID(), "" + sps.getVersion(), MicroServiceRepoUpdateEvent.SERVICE_CREATED);
                MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                        MicroServiceRepoUpdateEvent.SERVICE_CREATED, Event.EventCategory.INFORMATION, "");

            }


        } catch (Exception ex) {
            throw new FioranoException(ex);
        }

        // All the place holders for the service have been created. Returning from
        // here. The updateResources API will be called now on to fill up the data
        // in the repository files.

        return new ServiceReference(sps);
    }


    /**
     * Edits a saved but unpublished service with the new values from the
     * service property sheet that is passed as parameter
     *
     * @param serviceGUID The GUID of the service that is to be edited
     * @param version     The version of the service that is to be
     *                    edited
     * @param sps         The new service property sheet that needs to
     *                    be saved in the service repository sheet
     * @param handleID    HandleId holds the information about authentication
     * @return New header of the service that has been
     * edited
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("unused")
    public ServiceReference editService(String serviceGUID, String version, Service sps, String handleID) throws FioranoException {
        if (m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) != null)
            throw new FioranoException("ERROR_EDITING_SERVICE");
        if (m_nonCommittedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) == null)
            throw new FioranoException("ERROR_EDITING_SERVICE");
        if (!sps.getGUID().equalsIgnoreCase(serviceGUID) || sps.getVersion() != Float.parseFloat(version)) {
            throw new FioranoException("ERROR_EDITING_SERVICE");
        }
        try {
            MSRepoUtil.saveServiceDescriptor(sps);
        } catch (Exception e) {
            throw new FioranoException("ERROR_EDITING_SERVICE");
        }
        m_nonCommittedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
        MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                MicroServiceRepoUpdateEvent.UNREGISTERED_SERVICE_EDITED, Event.EventCategory.INFORMATION, "");
        return new ServiceReference(sps);
    }

    /**
     * This method edits a registered service identified by the
     * <code>serviceGUID</code> and <code>version</code> arguments with
     * new values from the <code>ServicePropertySheet</code> argument.
     * It returns the modified <code>ServiceHeader</code>. This only edits
     * those regsitered services which are in QA or Development Stage.
     *
     * @param serviceGUID The GUID of the service that is to be edited
     * @param version     The version of the service that is to be
     *                    edited
     * @param sps         The new service property sheet that needs to
     *                    be saved in the service repository sheet
     * @return New header of the service that has been
     * edited
     * @throws FioranoException if the process of editing service fails to
     *                          complete successfully.
     */
    @SuppressWarnings("unused")
    public ServiceReference editRegisteredService(String serviceGUID, String version,
                                                  Service sps, String handleID)
            throws FioranoException {
        if (m_nonCommittedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) != null)
            throw new FioranoException("ERROR_EDITING_REGISTERED_SERVICE");
        if (m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) == null)
            throw new FioranoException("ERROR_EDITING_REGISTERED_SERVICE");
        if (!sps.getGUID().equalsIgnoreCase(serviceGUID)
                || sps.getVersion() != Float.parseFloat(version)) {
            throw new FioranoException("ERROR_EDITING_REGISTERED_SERVICE");
        }
        Service oldSPS =
                m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version));
        Service spsToWrite = sps;
        Deployment oldDeployment = oldSPS.getDeployment();
        Deployment newDeployment = sps.getDeployment();
        if (oldDeployment != null && oldDeployment.getLabel() != null
                && oldDeployment.getLabel().equalsIgnoreCase(Deployment.QA)
                && newDeployment != null && newDeployment.getLabel() != null
                && newDeployment.getLabel().equalsIgnoreCase(Deployment.DEVELOPMENT))
            throw new FioranoException("ERROR_EDITING_REGISTERED_SERVICE");
        if (oldDeployment != null && oldDeployment.getLabel() != null
                && oldDeployment.getLabel().equalsIgnoreCase(Deployment.QA)) {
            spsToWrite = oldSPS;
            oldDeployment.getServiceRefs().clear();
            if (newDeployment != null) {

                for (ServiceRef serDep : newDeployment.getServiceRefs()) {
                    oldDeployment.addServiceRef(serDep);
                }

                if (newDeployment.getLabel() != null
                        && newDeployment.getLabel().equalsIgnoreCase(Deployment.PRODUCT))
                    oldDeployment.setLabel(Deployment.PRODUCT);
            }
            spsToWrite.setDeployment(oldDeployment);
        } else if (oldDeployment != null && oldDeployment.getLabel() != null
                && oldDeployment.getLabel().equalsIgnoreCase(Deployment.DEVELOPMENT)) {
            spsToWrite = sps;
        }
        spsToWrite.setLastModifiedDate(new Date(System.currentTimeMillis()));
        try {
            Enumeration old_resources = getAllResourcesForService(serviceGUID, version);
            MSRepoUtil.saveServiceDescriptor(sps);
            while (old_resources != null && old_resources.hasMoreElements()) {
                Resource res = (Resource) old_resources.nextElement();
                if (hasResource(serviceGUID, version, res.getName()) && !(MSRepoUtil.isPresentInNewDeployment(res, newDeployment))) {
                    removeResource(serviceGUID, version, res, handleID);
                }
            }
        } catch (Exception e) {
            throw new FioranoException("ERROR_EDITING_REGISTERED_SERVICE", e);
        }
        m_committedServiceVsProperties.put(getUniqueKey(serviceGUID, version), spsToWrite);
        MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                MicroServiceRepoUpdateEvent.REGISTERED_SERVICE_EDITED, Event.EventCategory.INFORMATION, "");
        return new ServiceReference(spsToWrite);
    }


    /**
     * Overwrites on to an existing version.Adds the resource info to the
     * service referenced by the ServiceHandle.
     *
     * @param serviceGUID The feature to be added to the Resource
     *                    attribute
     * @param version     The feature to be added to the Resource
     *                    attribute
     * @param resrcInfo   The feature to be added to the Resource
     *                    attribute
     * @param handleID    The feature to be added to the Resource
     *                    attribute
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("UnusedParameters")
    public void addResource(String serviceGUID, String version, Resource resrcInfo, String handleID)
            throws FioranoException {

        try {
            Service sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if(sps == null) {
                throw new Exception("Error reading Service - " + getUniqueKey(serviceGUID, version));
            }
            Deployment deployment = sps.getDeployment();
            if (deployment != null) {
                for (Resource res : deployment.getResources()) {
                    if (!res.equals(resrcInfo))
                        continue;
                    if (res.equals(resrcInfo)) {
                        deployment.removeResource(res);
                        deployment.addResource(resrcInfo);
                        sps.setDeployment(deployment);
                        return;
                    }

                    deployment.removeResource(res);
                    break;
                }
            }

            if (deployment == null)
                deployment = new Deployment();
            deployment.addResource(resrcInfo);
            sps.setDeployment(deployment);

            // we need to put the updated date also in service header.
            sps.setLastModifiedDate(new Date(System.currentTimeMillis()));

            // Write back the modified xml file for the service
            MSRepoUtil.saveServiceDescriptor(sps);

            // Added for staging. Now addResource can also be called for
            // committed services.
            if (m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) != null)
                m_committedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
            else
                m_nonCommittedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);

            //generateMicroServiceRepoUpdateEvent(serviceGUID, version, resrcInfo.getName(), MicroServiceRepoUpdateEvent.RESOURCE_CREATED);
            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, resrcInfo.getName(),
                    MicroServiceRepoUpdateEvent.RESOURCE_CREATED, Event.EventCategory.INFORMATION, "");
        } catch (Exception e) {
            throw new FioranoException("ERROR_ADDING_RESOURCE", e);
        }
    }


    /**
     * Adds a feature to the Icon attribute of the IServiceRepository object
     *
     * @param serviceGUID   The feature to be added to the Icon attribute
     * @param version       The feature to be added to the Icon attribute
     * @param iconFileName  The feature to be added to the Icon attribute
     * @param iconFileBytes The feature to be added to the Icon attribute
     * @param handleID      The feature to be added to the Icon attribute
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("UnusedParameters")
    public void addIcon(String serviceGUID, String version,
                        String iconFileName, byte[] iconFileBytes, String handleID)
            throws FioranoException {

        try {
            Service sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if(sps == null) {
                throw new Exception("Error reading Service - " + getUniqueKey(serviceGUID, version));
            }
            if (!iconFileName.equals(sps.getIcon16())) {
                sps.setIcon16(iconFileName);
                MSRepoUtil.saveServiceDescriptor(sps);

                m_nonCommittedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
            }

            // Write the icon file
            File icon = MSRepoUtil.getIcon(serviceGUID, version, iconFileName);
            MSRepoUtil.writeFileData(icon, iconFileBytes);

            // Generate an event.
            //generateMicroServiceRepoUpdateEvent(serviceGUID, version, iconFileName, MicroServiceRepoUpdateEvent.RESOURCE_CREATED);
            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, iconFileName,
                    MicroServiceRepoUpdateEvent.RESOURCE_CREATED, Event.EventCategory.INFORMATION, "");
        } catch (Exception e) {
            throw new FioranoException("ERROR_ADD_ICON", e);
        }
    }

    /**
     * Changes the Icon for the given service
     *
     * @param serviceGUID   The Service name for which the icon is to be changed
     * @param version       The version of the service
     * @param iconFileName  The name of the icon to be added
     * @param iconFileBytes Icon file as byte array
     * @param handleID      The security handle
     * @throws FioranoException If there is error in changing the icon
     */

    @SuppressWarnings("unused")
    public void updateIcon32(String serviceGUID, String version,
                             String iconFileName, byte[] iconFileBytes, String handleID)
            throws FioranoException {
        String oldIcon = getServiceInfo(serviceGUID, version).getIcon32();

        try {
            File icon = MSRepoUtil.getIcon(serviceGUID, version, oldIcon);
            icon.delete();
        } catch (Exception e) {
            throw new FioranoException("ERROR_RESOURCE_UPDATE_ERROR", e);
        }

        if (iconFileBytes != null)
            addIcon32(serviceGUID, version, iconFileName, iconFileBytes, handleID);
        else {
            Service sps;
            try {
                sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
                if(sps == null) {
                    throw new Exception("Error reading service - " + getUniqueKey(serviceGUID, version));
                }
                sps.setIcon32(null);
                MSRepoUtil.saveServiceDescriptor(sps);
            } catch (Exception e) {
                throw new FioranoException("ERROR_RESOURCE_UPDATE_ERROR", e);
            }

            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, iconFileName,
                    MicroServiceRepoUpdateEvent.RESOURCE_CREATED, Event.EventCategory.INFORMATION, "");
        }
    }

    /**
     * Changes the Icon for the given service
     *
     * @param serviceGUID   The Service name for which the icon is to be changed
     * @param version       The version of the service
     * @param iconFileName  The name of the icon to be added
     * @param iconFileBytes Icon file as byte array
     * @param handleID      The security handle
     * @throws FioranoException If there is error in changing the icon
     */
    @SuppressWarnings("unused")
    public void updateIcon(String serviceGUID, String version,
                           String iconFileName, byte[] iconFileBytes, String handleID)
            throws FioranoException {

        String oldIcon = getServiceInfo(serviceGUID, version).getIcon16();

        try {
            File icon = MSRepoUtil.getIcon(serviceGUID, version, oldIcon);
            icon.delete();
        } catch (Exception e) {
            throw new FioranoException("ERROR_RESOURCE_UPDATE_ERROR", e);
        }

        if (iconFileBytes != null)
            addIcon(serviceGUID, version, iconFileName, iconFileBytes, handleID);
        else {
            Service sps;
            try {
                sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
                if(sps == null) {
                    throw new Exception("Error reading Service - " + getUniqueKey(serviceGUID, version));
                }
                sps.setIcon16(null);
                MSRepoUtil.saveServiceDescriptor(sps);
            } catch (Exception e) {
                throw new FioranoException("ERROR_RESOURCE_UPDATE_ERROR", e);
            }

            // Generate an event.
            //generateMicroServiceRepoUpdateEvent(serviceGUID, version, iconFileName, MicroServiceRepoUpdateEvent.RESOURCE_CREATED);
            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, iconFileName,
                    MicroServiceRepoUpdateEvent.RESOURCE_CREATED, Event.EventCategory.INFORMATION, "");
        }

    }

    /**
     * Adds a feature to the Icon attribute of the IServiceRepository object
     *
     * @param serviceGUID   The feature to be added to the Icon attribute
     * @param version       The feature to be added to the Icon attribute
     * @param iconFileName  The feature to be added to the Icon attribute
     * @param iconFileBytes The feature to be added to the Icon attribute
     * @param handleID      The feature to be added to the Icon attribute
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("UnusedParameters")
    public void addIcon32(String serviceGUID, String version,
                          String iconFileName, byte[] iconFileBytes, String handleID)
            throws FioranoException {

        try {
            Service sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if(sps == null) {
                throw new Exception("Error reading Service - " + getUniqueKey(serviceGUID, version));
            }
            if (!iconFileName.equals(sps.getIcon32())) {
                sps.setIcon32(iconFileName);
                MSRepoUtil.saveServiceDescriptor(sps);
                m_nonCommittedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
            }
            File icon = MSRepoUtil.getIcon(serviceGUID, version, iconFileName);
            MSRepoUtil.writeFileData(icon, iconFileBytes);
            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, iconFileName,
                    MicroServiceRepoUpdateEvent.RESOURCE_CREATED, Event.EventCategory.INFORMATION, "");
        } catch (Exception e) {
            throw new FioranoException("ERROR_ADD_ICON32", e);
        }
    }


    /**
     * Deletes the resource info from the service referenced by the
     * ServiceHandle.
     *
     * @param resrcInfo   ResourceInfo
     * @param serviceGUID Description of the Parameter
     * @param version     Description of the Parameter
     * @param handleID    Description of the Parameter
     * @throws FioranoException Exception
     */
    @SuppressWarnings("UnusedParameters")
    public void removeResource(String serviceGUID, String version, Resource resrcInfo, String handleID)
            throws FioranoException {
        try {
            boolean isCommittedService = false;
            Service existingSPS = m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version));
            if (existingSPS != null)
                isCommittedService = true;
            Service sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if (sps == null) {
                throw new Exception("Service not found - " + getUniqueKey(serviceGUID, version));
            }
            if(existingSPS != null) {
                Deployment deployment = existingSPS.getDeployment();
                Resource actualResource = (Resource) DmiObject.findNamedObject(deployment.getResources(), resrcInfo.getName());
                if (actualResource == null)
                    return;
                deployment.removeResource(actualResource);
                sps.setDeployment(deployment);
            }

            String resPath = getResourcePath(serviceGUID, version, resrcInfo.getName());
            File toBeDeleted = new File(resPath);
            toBeDeleted.delete();

            File fo = MSRepoUtil.getServiceDescriptor(serviceGUID, version);
            sps.toXMLString(new FileOutputStream(fo));

            if (isCommittedService)
                m_committedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
            else
                m_nonCommittedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);

            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, resrcInfo.getName(),
                    MicroServiceRepoUpdateEvent.RESOURCE_REMOVED, Event.EventCategory.INFORMATION, "");

        } catch (Exception e) {
            throw new FioranoException("ERROR_RESOURCE_DELETE_FAILURE_ERROR", e);
        }
    }


    /**
     * Copies a published service to another service/version in the service
     * repository. This new service can be edited and published later
     *
     * @param srcServiceGUID The source service GUID
     * @param srcVersion     The source version from which the service is
     *                       to be copied
     * @param tgtServiceGUID The target service GUID to which the service
     *                       is to be copied. If this is passed as null, the service is copied
     *                       with the same service GUID as the source and the different version
     *                       number
     * @param handleID       Description of the Parameter
     * @return Service Header for the new version of the
     * service
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("unused")
    public ServiceReference copyServiceVersion(String srcServiceGUID, String srcVersion, String tgtServiceGUID,
                                               String strTgtVersion, String handleID) throws FioranoException {
        float tgtVersionVal = 0;
        try {
            tgtVersionVal = Float.parseFloat(strTgtVersion);
        } catch (Exception ex) {
            //Do nothing.
        }
        Enumeration existingVersions = getAllVersionsOfService(tgtServiceGUID, false);
        while (existingVersions.hasMoreElements()) {
            ServiceReference header = (ServiceReference) existingVersions.nextElement();
            float versionNumber = header.getVersion();
            if (versionNumber == tgtVersionVal)
                throw new FioranoException("ERROR_SERVICE_UPDATE_ERROR");
        }

        try {
            Service sps = MSRepoUtil.getServicePropertySheet(srcServiceGUID, srcVersion);
            if(sps == null) {
                throw new Exception("Error reading Service - " + getUniqueKey(srcServiceGUID, srcVersion));
            }
            if (tgtServiceGUID != null)
                sps.setGUID(tgtServiceGUID);

            float tgtVersion;

            try {
                tgtVersion = Float.parseFloat(strTgtVersion);
            } catch (Exception e) {
                throw new FioranoException("INVALID_VERSION");
            }
            sps.setVersion(tgtVersion);

            if (srcServiceGUID.equalsIgnoreCase(sps.getGUID())
                    && srcVersion.equalsIgnoreCase(strTgtVersion))
                throw new FioranoException("ERROR_COPY_SERVICE_2");


            String srcFolder = MSRepoUtil.getServicePath(srcServiceGUID, srcVersion);
            File src = new File(srcFolder);
            String tgtFolder = MSRepoUtil.getCompletedResourceFolder(tgtServiceGUID, "" + tgtVersion);
            File tgt = new File(tgtFolder);
            MSRepoUtil.copyChildrenToTgtFolder(src, tgt);
            new File(tgt, "ServiceDescriptor.xml").delete();
            MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(tgtServiceGUID, strTgtVersion, null,
                    MicroServiceRepoUpdateEvent.SERVICE_CREATED, Event.EventCategory.INFORMATION, "");
            return saveService(sps, false, false, handleID);
        } catch (Exception e) {
            throw new FioranoException("ERROR_SERVICE_SAVE_ERROR", e);
        }
    }


    /**
     * Publishes a the service becomes available to all the peers on the
     * network
     *
     * @param serviceGUID The GUID of the service to be finalizes
     * @param version     The version of the service to be finalized
     * @param handleID    Description of the Parameter
     * @return The header of the service that has been
     * published
     * @throws FioranoException Description of the Exception
     */
    @SuppressWarnings("unused")
    public ServiceReference publishService(String serviceGUID, String version, String handleID)
            throws FioranoException {
        return commit(serviceGUID, version, Deployment.PRODUCT, handleID);
    }

    /**
     * Publishes a the service becomes available to all the peers on the
     * network
     *
     * @param serviceGUID The GUID of the service to be finalizes
     * @param version     The version of the service to be finalized
     * @param handleID    Description of the Parameter
     * @return The header of the service that has been
     * published
     * @throws FioranoException Description of the Exception
     */
    public ServiceReference publishService(String serviceGUID, String version, String stage, String handleID)
            throws FioranoException {
        if (stage == null)
            return commit(serviceGUID, version, Deployment.PRODUCT, handleID);
        else
            return commit(serviceGUID, version, stage, handleID);
    }

    /**
     * Removes the specified service from the service repository
     *
     * @param serviceGUID The GUID of the service which is to be
     *                    removed
     * @param version     The version of the service that is to be
     *                    removed. If null is passed as version all the versions of the
     *                    service are removed from the repository
     * @param handleID    Description of the Parameter
     * @return An enumeration containing the ServiceHeader
     * values of all the services removed
     * @throws FioranoException Description of the Exception
     */
    public Enumeration removeService(String serviceGUID, String version, boolean killRunningInstances, String handleID)
            throws FioranoException {
        try {
            Vector<ServiceReference> allRemovedServices = new Vector<>();

            if (version == null) {
                Enumeration versions = MSRepoUtil.getAllServiceVersions(serviceGUID);
                while (versions.hasMoreElements()) {
                    ServiceReference header = _removeService(serviceGUID, (String) versions.nextElement(), killRunningInstances, handleID);
                    if (header != null)
                        allRemovedServices.addElement(header);
                }
            } else {
                ServiceReference header = _removeService(serviceGUID, version, killRunningInstances, handleID);
                if (header != null)
                    allRemovedServices.addElement(header);
            }


            return allRemovedServices.elements();
        } catch (Exception e) {
            throw new FioranoException("ERROR_REMOVING_SERVICE", e);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private ServiceReference _removeService(String serviceGUID, String version, boolean killRunningInstances, String handleID)
            throws FioranoException, IOException {
        if (serviceGUID == null || version == null)
            throw new FioranoException("INVALID_GUID_VERSION");
        File fileVersion = new File(MSRepoUtil.getServicePath(serviceGUID, version));
        if (!fileVersion.exists())
            throw new FioranoException("ERROR_SERVICE_VERSION_DOES_NOT_EXIST");
        ServiceReference header = removeServiceVersion(serviceGUID, version);
        MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                MicroServiceRepoUpdateEvent.SERVICE_REMOVED, Event.EventCategory.INFORMATION, "");
        return header;
    }


    public void checkServiceResourceFiles(String serviceGUID, String version) throws FioranoException {
        Service sps = m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version));

        if (sps == null || sps.getDeployment() == null)
            return;
        Deployment deploymentInfo = sps.getDeployment();
        int currentOS = deploymentInfo.getOperatingSystem(System.getProperty("os.name"));

        for (Resource resource : deploymentInfo.getResources()) {
            if (!resource.isRequiredForConfiguration() && !resource.isRequiredForExecution())
                continue;
            if (!(resource.isOperatingSystemSupported(currentOS)))
                continue;
            String resName = resource.getName();
            String resFileName = getResourcePath(serviceGUID, version, resName);
            File resFile = new File(resFileName);
            if (!resFile.exists())
                throw new FioranoException("ERROR_SERVICE_DETAILS_FETCH_ERROR");
        }
    }

    private String getUniqueKey(String serviceGUID, String version)
            throws FioranoException {
        String modifiedVersion;

        try {
            float modVersion = Float.parseFloat(version);

            modifiedVersion = "" + modVersion;
        } catch (NumberFormatException e) {
            throw new FioranoException("INVALID_SERVICE_VERSION_SPECIFIED", e);
        }
        return serviceGUID + ";" + modifiedVersion;
    }

    private String getUniqueKey(String serviceGUID, float version) {
        return serviceGUID + ";" + version;
    }


    /**
     * Given the service path and resource data the partial resource is
     * updated.If the resource directory path does not exist in the partial
     * folder it is created before writing data to the resource file.
     *
     * @param serviceGUID Description of the Parameter
     * @param version     Description of the Parameter
     * @param resName     Description of the Parameter
     * @param startByte   Description of the Parameter
     * @param bytes       Description of the Parameter
     * @throws IOException Description of the Exception
     */
    private void updatePartialResourceData(String serviceGUID, String version,
                                           String resName, long startByte, byte[] bytes)
            throws IOException {
        String fileToWrite = MSRepoUtil.getPartialResourceFolder(serviceGUID, version) + File.separator;
        // file folders need to be created
        File directories = new File(fileToWrite);

        if (!directories.exists()) {
            directories.mkdirs();
        }
        fileToWrite = fileToWrite + File.separator + resName;

        File fo = new File(fileToWrite);
        boolean append = startByte > 0;
        OutputStream fos = new FileOutputStream(fo, append);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        if ((startByte >= 0) && (bytes != null)) {
            bos.write(bytes);
        }
        bos.close();
        fos.close();
    }

    private void moveResourceFromPartialToCompleted(String serviceGUID, String version, String resName)
            throws FioranoException {
        try {
            File src = new File(MSRepoUtil.getPartialResourceFolder(serviceGUID, version) + File.separator + resName);
            File target = new File(MSRepoUtil.getCompletedResourceFolder(serviceGUID, version) + File.separator + resName);
            src.renameTo(target);
        } catch (Exception e) {
            throw new FioranoException("ERROR_MOVING_RESOURCE", e);
        }
    }

    private void moveCompletedResourceFilesToRepository(String serviceGUID, String version)
            throws FioranoException {
        try {
            File src = new File(MSRepoUtil.getCompletedResourceFolder(serviceGUID, version));
            File tgt = new File(MSRepoUtil.getServicePath(serviceGUID, version));
            MSRepoUtil.moveChildrenToTgtFolder(src, tgt);
        } catch (Exception e) {
            throw new FioranoException("ERROR_MOVING_RESOURCES", e);
        }
    }

    public boolean reloadRepository() throws FioranoException {

        m_committedServiceVsProperties.clear();
        m_nonCommittedServiceVsProperties.clear();
        try {
            boolean toReturn = true;
            File[] allServices = new File(COMPONENTS_REPOSITORY_FOLDER).listFiles();
            if (allServices != null) {
                for (File allService : allServices)
                    if (allService.isDirectory())
                        toReturn = loadAllVersionsOfService(allService.getName());
            }
            return toReturn;
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    private boolean loadAllVersionsOfService(String serviceGUID) throws FioranoException {
        try {
            File serviceHome = new File(MSRepoUtil.getServiceRootPath(serviceGUID));
            File[] allVersions = serviceHome.listFiles();
            boolean toReturn = true;
            if (allVersions != null) {
                for (File srvDir : allVersions) {
                    if (srvDir.isDirectory())
                        toReturn = loadServiceVersion(serviceGUID, srvDir.getName());
                }
            }
            return toReturn;
        } catch (Exception e) {
            throw new FioranoException("ERROR_LOADING_ALL_VERSIONS", e);
        }
    }

    private boolean loadServiceVersion(String serviceGUID, String version) throws FioranoException {
        File serviceVersionHome;
        String serviceDirName;
        try {
            serviceVersionHome = new File(MSRepoUtil.getServicePath(serviceGUID, version));
            serviceDirName = serviceVersionHome.getParentFile().getName();
        } catch (Exception e) {
            throw new FioranoException(e);
        }

        Service sps;
        try {
            sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if (sps == null)
                return false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

        String guidOfThisService = sps.getGUID();
        if (!serviceDirName.equalsIgnoreCase(guidOfThisService)) {
            return false;
        }

        String versionNum = String.valueOf(sps.getVersion());

        if (!versionNum.equalsIgnoreCase(serviceVersionHome.getName())) {
            return false;
        }

        try {
            // Check for the existence of Complete and Partial folder.
            // If these exist, the service has not
            // been published yet
            File compFile = new File(MSRepoUtil.getCompletedResourceFolder(serviceGUID, version));
            File partFile = new File(MSRepoUtil.getPartialResourceFolder(serviceGUID, version));

            // if both the complete and partial folders exist for the specified service version,
            // the service is stored as an un-commited service.
            if (compFile.exists() && partFile.exists()) {
                m_nonCommittedServiceVsProperties.put(getUniqueKey(sps.getGUID(), sps.getVersion()), sps);
            } else {
                // None of the temp folders exist, so add it to the committed properties
                m_committedServiceVsProperties.put(getUniqueKey(sps.getGUID(), sps.getVersion()), sps);
            }
        } catch (Exception e) {
            throw new FioranoException("ERROR_LOADING_SERVICE_VERSION", e);
        }

        return true;
    }


    /**
     * This function sorts the vector of ServiceHeader objects according to
     * their version number
     *
     * @param vectorOfServiceHeader Vector containing objects of ServiceHeader
     * @return Vector containing objects of ServiceHeader
     * according to their version number
     */
    private Vector sortAccordingToVersionNumber(Vector<ServiceReference> vectorOfServiceHeader) {
        Vector<ServiceReference> toReturn = new Vector<>();
        float toSort[] = new float[vectorOfServiceHeader.size()];
        Enumeration<ServiceReference> _enum = vectorOfServiceHeader.elements();
        Hashtable<Float, ServiceReference> tempHT = new Hashtable<>();
        int i = 0;
        while (_enum.hasMoreElements()) {
            ServiceReference hdr = _enum.nextElement();
            float versionNum = hdr.getVersion();
            tempHT.put(versionNum, hdr);
            toSort[i++] = versionNum;
        }
        Arrays.sort(toSort);

        for (i = 0; i < toSort.length; i++) {
            ServiceReference header = tempHT.get(toSort[i]);

            toReturn.add(header);
        }
        return toReturn;
    }


    /**
     * Description of the Method
     *
     * @param serviceGUID Description of the Parameter
     * @param version     Description of the Parameter
     * @return Description of the Return Value
     * @throws FioranoException
     */
    private ServiceReference removeServiceVersion(String serviceGUID, String version) throws FioranoException {
        File serviceVersionHome;
        Service sps = null;
        serviceVersionHome = new File(MSRepoUtil.getServicePath(serviceGUID, version));
        try {
            sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
        } catch (Exception e) {
            //ignore
        }
        try {
            serviceVersionHome.delete();
            if (serviceVersionHome.getParentFile().list().length == 0)
                serviceVersionHome.getParentFile().delete();
        } catch (Exception fse) {
            throw new FioranoException("ERROR_DELETE_SERVICE", fse);
        }

        try {
            m_nonCommittedServiceVsProperties.remove(getUniqueKey(serviceGUID, version));
            m_committedServiceVsProperties.remove(getUniqueKey(serviceGUID, version));
        } catch (FioranoException te) {
            logger.error("INVALID_SPS - " + te.getMessage());
        }

        return sps != null ? new ServiceReference(sps) : null;
    }

    @SuppressWarnings("UnusedParameters")
    private ServiceReference commit(String serviceGUID, String version, String stage, String handleID)
            throws FioranoException {
        if (m_committedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) != null)
            throw new FioranoException("ERROR_SERVICE_COMMIT_ERROR");
        if (m_nonCommittedServiceVsProperties.get(getUniqueKey(serviceGUID, version)) == null)
            throw new FioranoException("SERVICE_NOT_PRESENT");
        if (!stage.equalsIgnoreCase(Deployment.DEVELOPMENT)
                && stage.equalsIgnoreCase(Deployment.PRODUCT)
                && stage.equalsIgnoreCase(Deployment.QA))
            throw new FioranoException("ERROR_SERVICE_SAVE_ERROR");
        String servicePath = MSRepoUtil.getServicePath(serviceGUID, version);
        try {
            checkServiceResources(serviceGUID, version);
            moveCompletedResourceFilesToRepository(serviceGUID, version);
            new File(servicePath + File.separator + TEMP_DOWNLOAD_DIR).delete();
        } catch (Exception e) {
            throw new FioranoException("ERROR_SERVICE_SAVE_ERROR", e);
        }
        Service sps;
        try {
            sps = MSRepoUtil.getServicePropertySheet(serviceGUID, version);
            if (sps == null) {
                throw new FioranoException("ERROR_SERVICE_DETAILS_FETCH_ERROR");
            }
        } catch (Exception e) {
            throw new FioranoException("ERROR_SERVICE_DETAILS_FETCH_ERROR", e);
        }
        Deployment dep = sps.getDeployment();
        if (dep == null)
            dep = new Deployment();
        dep.setLabel(stage);
        sps.setDeployment(dep);
        try {
            MSRepoUtil.saveServiceDescriptor(sps);
        } catch (Exception e) {
            throw new FioranoException("ERROR_SERVICE_SAVE_ERROR", e);
        }
        m_nonCommittedServiceVsProperties.remove(getUniqueKey(serviceGUID, version));
        m_committedServiceVsProperties.put(getUniqueKey(serviceGUID, version), sps);
        MicroServiceRepoEventRaiser.generateServiceRepositoryEvent(serviceGUID, version, null,
                MicroServiceRepoUpdateEvent.SERVICE_REGISTERED, Event.EventCategory.INFORMATION, "");
        return new ServiceReference(sps);
    }

    private void checkServiceResources(String serviceGUID, String version)
            throws FioranoException {
        Service sps = m_nonCommittedServiceVsProperties.get(getUniqueKey(serviceGUID, version));
        if (sps == null || sps.getDeployment() == null)
            return;

        for (Resource resource : sps.getDeployment().getResources()) {
            String resName = resource.getName();
            String completedResourceFolder = MSRepoUtil.getCompletedResourceFolder(serviceGUID, version);
            String resPath = MSRepoUtil.resolve(completedResourceFolder, resName, favorites);
            try {
                File resFile = new File(resPath);
                if (!resFile.exists())
                    throw new FioranoException("SERVICE_CORRUPTED");
            } catch (Exception e) {
                throw new FioranoException("ERROR_READING_RESOURCE_FILE", e);
            }
        }

        // check the icon file now
        String iconFileName = sps.getIcon16();
        if (iconFileName == null)
            return;

        File icon;
        try {
            icon = MSRepoUtil.getIcon(serviceGUID, version, iconFileName);
            if (!icon.exists()) {
                String completedResourceFolder = MSRepoUtil.getCompletedResourceFolder(serviceGUID, version);
                String resPath = MSRepoUtil.resolve(completedResourceFolder, iconFileName, favorites);
                icon = new File(resPath);
                if (!icon.exists())
                    throw new FioranoException("SERVICE_CORRUPTED");
            }
        } catch (Exception e) {
            throw new FioranoException("ERROR_READING_ICON", e);
        }
    }

    private File getFile(String serviceGUID, String version, String resource) throws IOException {
        if (version == null || version.trim().equals(""))
            version = getHighestVersionOfService(serviceGUID);
        String servicePath = MSRepoUtil.getServicePath(serviceGUID, version);
        String resourceFile = servicePath + File.separator + resource;
        return new File(resourceFile);
    }

    @SuppressWarnings("unused")
    public long getSize(String serviceGUID, String version, String resource) throws FioranoException {
        try {
            File file = getFile(serviceGUID, version, resource);
            if (file.exists())
                return file.length();
            else return -1;
        } catch (IOException e) {
            throw new FioranoException("ERROR_READING_FILE_SIZE", e);
        }
    }

    @SuppressWarnings("unused")
    public long getLastModified(String serviceGUID, String version, String resource) throws FioranoException {
        try {
            File file = getFile(serviceGUID, version, resource);
            if (file.exists())
                return file.lastModified();
            else return -1;
        } catch (IOException e) {
            throw new FioranoException("ERROR_GET_LAST_MODF_DATE", e);
        }
    }

    public Enumeration getAllServiceGUIDs() throws FioranoException {
        return MSRepoUtil.getGUIDS(m_committedServiceVsProperties);
    }

    @SuppressWarnings("unused")
    public int getServicesCount() {
        int count = 0;
        count += m_committedServiceVsProperties.size();
        count += m_nonCommittedServiceVsProperties.size();
        return count;
    }

    private String getResourcePath(String serviceGUID, String version, String resName)
            throws FioranoException {
        String resFilePath = "";
        String servicePath = MSRepoUtil.getServicePath(serviceGUID, version);

        boolean bFind = true;

        if (m_nonCommittedServiceVsProperties.containsKey(getUniqueKey(serviceGUID, version))) {
            resFilePath = MSRepoUtil.getCompletedResourceFolder(serviceGUID, version) + File.separator + resName;
            try {
                File f = new File(resFilePath);
                if (f.exists())
                    bFind = false;
            } catch (Exception e) {
                throw new FioranoException("FILE_SYSTEM_ERROR_RESOLVING_RESOURCE_PATH", e);
            }
        }

        if (bFind) {
            resFilePath = MSRepoUtil.resolve(servicePath, resName, favorites);
        }
        return resFilePath;
    }

    private File fetchResourceFile(String serviceGUID, String version, String resName)
            throws FioranoException {
        File resFile;

        String resFilePath = getResourcePath(serviceGUID, version, resName);
        resFile = new File(resFilePath);
        if (!resFile.exists()) {
            throw new FioranoException("RESOURCE_FILE_NOTPRESENT");
        }
        return resFile;
    }
}

