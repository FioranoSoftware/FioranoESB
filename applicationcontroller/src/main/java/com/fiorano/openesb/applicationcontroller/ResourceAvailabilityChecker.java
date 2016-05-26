/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.application.service.Deployment;
import com.fiorano.openesb.application.service.Service;
import com.fiorano.openesb.application.service.ServiceRef;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.I18NUtil;
import com.fiorano.openesb.utils.RBUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ResourceAvailabilityChecker {

    private static Logger logger = LoggerFactory.getLogger(Activator.class);

    public static void checkResourceAndConnectivity(ApplicationController applicationController, MicroServiceRepoManager microServiceRepoManager, String handleID,  Map<String, Boolean> orderedList) throws FioranoException{
        for(String app_version: orderedList.keySet()){
            String[] current_AppGUIDAndVersion = ApplicationUtil.returnAppGUIDAndVersion(app_version);
            String currentGUID = current_AppGUIDAndVersion[0];
            Float currentVersion = Float.valueOf(current_AppGUIDAndVersion[1]);
            Application currentApplication = applicationController.getSavedApplication(currentGUID, currentVersion);
            checkResourceAndConnectivity(currentApplication, microServiceRepoManager);
            }
    }

    public static void checkResourceAndConnectivity(Application application, MicroServiceRepoManager microServiceRepoManager) throws FioranoException{
        for(ServiceInstance si:application.getServiceInstances()){
            validateServicesBeforeLaunch(application, microServiceRepoManager, si);
        }
    }

    private static void validateServicesBeforeLaunch(Application application,  MicroServiceRepoManager microServiceRepoManager, ServiceInstance instance) throws FioranoException {
        String servGUID = instance.getGUID();
        String version = String.valueOf(instance.getVersion());
        //  If any of the services doesn't exist in SP-Repository then throw an exception.
        Service service;
        try {
            service = microServiceRepoManager.getServiceInfo(servGUID, version);
        } catch (Throwable thr) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.SERVICE_NOT_FOUND, instance.getGUID()), thr);
            throw new FioranoException(I18NUtil.getMessage(Bundle.class, Bundle.SERVICE_NOT_FOUND, instance.getGUID()));
        }

        // Ensure that if the Configuration is Required, then we must ensure
        // that the corresponding component instance has non-null value for
        // configuration data set using CPS.
        // Even if the Configuration is not required, if it is partially configured,
        // Exception must be thrown
        if (service.getExecution().getCPS() != null) {
            if (instance.isPartiallyConfigured()) {
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_COMPONENT_NOT_FULLY_CONFIGURED_ERROR, servGUID, application.getGUID()+ Constants.NAME_DELIMITER+Float.toString(application.getVersion())));
                throw new FioranoException(I18NUtil.getMessage(Bundle.class, Bundle.ERROR_COMPONENT_NOT_FULLY_CONFIGURED_ERROR, servGUID, application.getGUID()+Constants.NAME_DELIMITER+Float.toString(application.getVersion())));
            }

            String config = instance.getConfiguration();
            if ((config == null || config.trim().length() == 0) && service.getExecution().getCPS().isMandatory()) {
                logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_COMPONENT_NOT_CONFIGURED_ERROR, servGUID, application.getGUID()+Constants.NAME_DELIMITER+Float.toString(application.getVersion())));
                throw new FioranoException(I18NUtil.getMessage(Bundle.class, Bundle.ERROR_COMPONENT_NOT_CONFIGURED_ERROR, servGUID, application.getGUID()+Constants.NAME_DELIMITER+Float.toString(application.getVersion())));
            }
        }

        // We also need to check if the zip file of the service exist or not.
        try {
            microServiceRepoManager.checkServiceResourceFiles(instance.getGUID(),
                    String.valueOf(instance.getVersion()));
        } catch (Throwable thr) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_RESOURCE_NOT_EXISTS, servGUID, ""), thr);
            throw new FioranoException(I18NUtil.getMessage(Bundle.class, Bundle.ERROR_RESOURCE_NOT_EXISTS, servGUID, thr.getMessage()));
        }

        //  Ensure that this service is LAUNCHABLE
        if (service.getExecution() == null) {
            logger.error(RBUtil.getMessage(Bundle.class, Bundle.SERVICE_NOT_LAUNCHABLE, servGUID));
            throw new FioranoException( I18NUtil.getMessage(Bundle.class, Bundle.SERVICE_NOT_LAUNCHABLE, servGUID));
        }

        // check the service dependencies.
        checkServiceResources(microServiceRepoManager, service, service);

        //  Changes related to debugMode.
        if (instance.isDebugMode())
            checkUniquenessOfDebugPorts(instance, application);
    }

    private static void checkServiceResources(MicroServiceRepoManager microServiceRepoManager,Service service, Service originalService) throws FioranoException {
        Deployment dep = service.getDeployment();
        if (dep != null) {
            for (Object obj : dep.getServiceRefs()) {
                ServiceRef dependency = (ServiceRef) obj;
                if (dependency != null) {
                    String depServiceGUID = dependency.getGUID();
                    float depServiceVersion = dependency.getVersion();
                    Service depSps;
                    try {

                        depSps = microServiceRepoManager.getServiceInfo(
                                depServiceGUID, String.valueOf(depServiceVersion));
                        if (depSps != null)
                            microServiceRepoManager.checkServiceResourceFiles(
                                    depServiceGUID, String.valueOf(depServiceVersion));

                    } catch (Exception e) {
                        throw new FioranoException(e.getMessage()+ " required for the Service Instance " + originalService.getGUID()+ " : "+ originalService.getVersion());
                    }
                    checkServiceResources(microServiceRepoManager, depSps, originalService);
                }
            }
        }
    }

    private static void checkUniquenessOfDebugPorts(ServiceInstance instance, Application application) throws FioranoException {
        for (ServiceInstance localInstance : application.getServiceInstances()) {
            //  If it's the same instance then continue.
            if (localInstance.getName().equalsIgnoreCase(instance.getName()))
                continue;
            //  If this instance doesn't have debugging ON then the port number doesn't matter.
            if (!localInstance.isDebugMode())
                continue;
            int localPort = localInstance.getDebugPort();
            int globalPort = instance.getDebugPort();
            if (localPort == globalPort) {
                throw new FioranoException(I18NUtil.getMessage(Bundle.class, Bundle.ERROR_SERVICE_DEBUG_PORT_INUSE, instance.getName(),
                        String.valueOf(globalPort), localInstance.getName()));

            }
        }
    }
}
