/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.application.aps.ApplicationInfo;
import com.fiorano.openesb.application.aps.ApplicationStateDetails;
import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.events.ApplicationEvent;
import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.microservice.ccp.CCPEventManager;
import com.fiorano.openesb.microservice.ccp.IEventListener;
import com.fiorano.openesb.microservice.ccp.event.CCPEventType;
import com.fiorano.openesb.microservice.ccp.event.ComponentCCPEvent;
import com.fiorano.openesb.microservice.ccp.event.ControlEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataEvent;
import com.fiorano.openesb.microservice.ccp.event.common.DataRequestEvent;
import com.fiorano.openesb.microservice.ccp.event.common.data.*;
import com.fiorano.openesb.microservice.ccp.event.peer.CommandEvent;
import com.fiorano.openesb.microservice.launch.impl.EventStateConstants;
import com.fiorano.openesb.microservice.launch.impl.MicroServiceLauncher;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.namedconfig.NamedConfigRepository;
import com.fiorano.openesb.namedconfig.NamedConfigurationUtil;
import com.fiorano.openesb.route.RouteOperationType;
import com.fiorano.openesb.route.RouteService;
import com.fiorano.openesb.route.impl.SenderSelectorConfiguration;
import com.fiorano.openesb.route.impl.TransformationConfiguration;
import com.fiorano.openesb.route.impl.XmlSelectorConfiguration;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.TransportConfig;
import com.fiorano.openesb.utils.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.security.SecurityManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.*;

public class ApplicationController {
    private NamedConfigRepository namedConfigRepository;
    private Logger logger = LoggerFactory.getLogger(Activator.class);
    private TransportService transport;
    private ApplicationRepository applicationRepository;
    private MicroServiceLauncher microServiceLauncher;
    private Map<String, ApplicationHandle> applicationHandleMap = new HashMap<>();
    private Map<String, Application> savedApplicationMap = new HashMap<>();
    private RouteService routeService;
    private SecurityManager securityManager;
    private MicroServiceRepoManager microServiceRepoManager;
    private CCPEventManager ccpEventManager;
    private ApplicationLogManager applicationLogManager;
    private ChainLaunchHelper chainLaunchHelper;

    public ApplicationController(ApplicationRepository applicationRepository, BundleContext context) throws Exception {
        logger.info("Initializing Application Controller.");
        this.applicationRepository = applicationRepository;
        ServiceReference<RouteService> serviceReference = context.getServiceReference(RouteService.class);
        if(serviceReference == null) {
            logger.error("Route service is not available for Applications module.");
            System.out.println("Startup Failed. Route service is not available for Microservice.");
            return;
        }
        routeService = context.getService(serviceReference);
        microServiceLauncher = context.getService(context.getServiceReference(MicroServiceLauncher.class));
        ccpEventManager = context.getService(context.getServiceReference(CCPEventManager.class));
        applicationLogManager = new ApplicationLogManager(this, ccpEventManager);
        namedConfigRepository = context.getService(context.getServiceReference(NamedConfigRepository.class));
        microServiceRepoManager = context.getService(context.getServiceReference(MicroServiceRepoManager.class));
        registerConfigRequestListener(ccpEventManager);
        transport = context.getService(context.getServiceReference(TransportService.class));
        securityManager = context.getService(context.getServiceReference(SecurityManager.class));
        chainLaunchHelper = new ChainLaunchHelper(this);
        String [] appIds = applicationRepository.getApplicationIds();
        for(String appid:appIds){
            float[] appVersions = new float[0];
            if(!appid.startsWith(".")){
                appVersions = applicationRepository.getAppVersions(appid);
            }
            for(float ver : appVersions){
                Application application = applicationRepository.readApplication(appid, String.valueOf(ver));
               savedApplicationMap.put(appid + "__" + ver, application);
                if (chainLaunchHelper.cyclicDependencyExists(application)) {//for app that are already in the repo before fix 25838
                    logger.error(RBUtil.getMessage(Bundle.class, Bundle.ERROR_CYCLIC_DEPENDENCY_REFERRED_APPS, application.getGUID(), String.valueOf(application.getVersion())));
                } else {
                    chainLaunchHelper.updateChainLaunchDS(application);
                }
            }
        }
        RestoreAppState restoreAppStateThread= new RestoreAppState(1000);
        restoreAppStateThread.setPriority(Thread.MAX_PRIORITY);
        restoreAppStateThread.start();
        logger.info("Initialized Application Controller.");
    }

    private void registerConfigRequestListener(final CCPEventManager ccpEventManager) throws Exception {
        ccpEventManager.registerListener(new IEventListener() {
            @Override
            public void onEvent(ComponentCCPEvent event) throws Exception {
                ControlEvent controlEvent = event.getControlEvent();
                if(controlEvent instanceof DataRequestEvent && controlEvent.isReplyNeeded()) {
                    String serviceInstanceName = ApplicationUtil.getInstanceName(event);
                    String appName = ApplicationUtil.getAppName(event);
                    String appVersion = ApplicationUtil.getAppVersion(event);
                    Application application = savedApplicationMap.get(appName + Constants.NAME_DELIMITER + appVersion);
                    DataEvent dataEvent = new DataEvent();
                    Map<DataRequestEvent.DataIdentifier,Data> data = new HashMap<>();
                    for(DataRequestEvent.DataIdentifier request: ((DataRequestEvent) controlEvent).getDataIdentifiers()) {
                        if(request == DataRequestEvent.DataIdentifier.COMPONENT_CONFIGURATION) {
                            logger.info("Processing the "+DataRequestEvent.DataIdentifier.COMPONENT_CONFIGURATION.name()+ " Data Request CCP Event coming from Service: "+serviceInstanceName+ "of Application: "+appName+":"+appVersion);
                            MicroserviceConfiguration microserviceConfiguration = new MicroserviceConfiguration();
                            String configuration = application.getServiceInstance(serviceInstanceName).getConfiguration();
                            if(configuration==null){
                                configuration="";
                            }
                            microserviceConfiguration.setConfiguration(configuration);
                            data.put(request, microserviceConfiguration);
                        }
                        if(request == DataRequestEvent.DataIdentifier.NAMED_CONFIGURATION) {
                            logger.info("Processing the "+DataRequestEvent.DataIdentifier.NAMED_CONFIGURATION.name()+ " Data Request CCP Event coming from Service: "+serviceInstanceName+ "of Application: "+appName+":"+appVersion);
                            NamedConfiguration namedConfiguration = new NamedConfiguration();
                            HashMap<String, String> map = ApplicationNamedConfigUtil.resolveNamedConfigurations(getApplicationHandle(appName, Float.parseFloat(appVersion)), serviceInstanceName, namedConfigRepository);
                            namedConfiguration.setConfiguration(map);
                            data.put(request,namedConfiguration);
                        }
                        if(request == DataRequestEvent.DataIdentifier.PORT_CONFIGURATION) {
                            logger.info("Processing the "+DataRequestEvent.DataIdentifier.PORT_CONFIGURATION.name()+ " Data Request CCP Event coming from Service: "+serviceInstanceName+ "of Application: "+appName+":"+appVersion);
                            PortConfiguration portConfiguration = new PortConfiguration();
                            Map portInstances = new HashMap();
                            List<InputPortInstance> inPortInstanceList = new ArrayList();
                            inPortInstanceList.addAll(application.getServiceInstance(serviceInstanceName).getInputPortInstances());
                            List<OutputPortInstance> outPortInstanceList = new ArrayList();
                            outPortInstanceList.addAll(application.getServiceInstance(serviceInstanceName).getOutputPortInstances());
                            portInstances.put("IN_PORTS",inPortInstanceList);
                            portInstances.put("OUT_PORTS", outPortInstanceList);
                            portConfiguration.setPortInstances(portInstances);
                            data.put(request,portConfiguration);
                        }
                        if(request == DataRequestEvent.DataIdentifier.MANAGEABLE_PROPERTIES) {
                            logger.info("Processing the "+DataRequestEvent.DataIdentifier.MANAGEABLE_PROPERTIES.name()+ " Data Request CCP Event coming from Service: "+serviceInstanceName+ "of Application: "+appName+":"+appVersion);
                            ManageableProperties manageableProperties = new ManageableProperties();
                            ServiceInstance serviceInstance = application.getServiceInstance(serviceInstanceName);

                            manageableProperties.setManageableProperties(getManageablePropertiesToBind(serviceInstance.getManageableProperties()));
                            data.put(request,manageableProperties);
                        }
                    }
                    dataEvent.setData(data);
                    ccpEventManager.getCcpEventGenerator().sendEvent(dataEvent,event.getComponentId());
                }
            }

            @Override
            public String getId() {
                return "Components' Requests Listener";
            }
        }, CCPEventType.DATA_REQUEST);
    }

    public String getComponentStats(String appGuid, float version, String serviceName, String handleId) throws FioranoException {
        ApplicationHandle applicationHandle = getApplicationHandle(appGuid, version, handleId);
        if(applicationHandle!=null){
           return applicationHandle.getComponentStats(serviceName).getValue();
        }
        return null;
    }

    private HashMap<String, ConfigurationProperty> getManageablePropertiesToBind(List manageablePropertiesList) {
        HashMap<String, ConfigurationProperty> configurationProps = new HashMap<>();
        if (manageablePropertiesList != null) {
            for (Object aManageablePropertiesList : manageablePropertiesList) {
                ManageableProperty prop = (ManageableProperty) aManageablePropertiesList;
                configurationProps.put(prop.getName(),  new ConfigurationProperty(prop.getName(), prop.getValue(), prop.isEncrypted(), prop.getPropertyType(), prop.getConfigurationType()));
            }
        }
        return configurationProps;
    }

    public void saveApplication(File appFileFolder, String handleID, byte[] zippedContents) throws FioranoException {
        String userName = securityManager.getUserName(handleID);
        logger.info("Reading Application from "+ appFileFolder.getName());
        Application application = ApplicationParser.readApplication(appFileFolder, Application.Label.none.toString(), false, false);
        try {
            application.validate();
        } catch (FioranoException e3){
            //this would led some corrupted application to enter into the repository, which could be deleted
            logger.error("error occured while validating application",e3);
            //we can fail this step if we want
        }
        String appGuid = application.getGUID();
        float version = application.getVersion();
        Application oldApp = savedApplicationMap.get(appGuid+Constants.NAME_DELIMITER+version);
        applicationRepository.saveApplication(application, appFileFolder, userName, zippedContents, handleID);
        savedApplicationMap.put(application.getGUID() + Constants.NAME_DELIMITER + application.getVersion(), application);
        if (oldApp != null) {
            chainLaunchHelper.removeChainLaunchDS(oldApp);
            Vector<String> deletedComponents = new Vector<>();
            Vector<String> deletedConfigComponents = new Vector<>();
            HashMap<String, String> deletedPorts = new HashMap<>();

            for (Object o1 : oldApp.getServiceInstances()) {
                boolean check = false;
                ServiceInstance oldInst = (ServiceInstance) o1;

                for (Object o : application.getServiceInstances()) {
                    ServiceInstance newInst = (ServiceInstance) o;
                    if (oldInst.getName().equals(newInst.getName())) {
                        check = false;

                        List<OutputPortInstance> oldOutputPortInstances = oldInst.getOutputPortInstances();
                        for (OutputPortInstance oldPort : oldOutputPortInstances) {
                            boolean deletePort = false;

                            List<OutputPortInstance> newOutputPortInstances = newInst.getOutputPortInstances();
                            for (OutputPortInstance newPort : newOutputPortInstances) {
                                if (oldPort.getName().equals(newPort.getName())) {
                                    deletePort = false;
                                    break;
                                } else {
                                    deletePort = true;
                                }
                            }

                            if (newInst.getOutputPortInstances().size() == 0)
                                deletePort = true;

                            if (deletePort)
                                deletedPorts.put(oldInst.getName(), oldPort.getName());
                        }

                        break;
                    } else {
                        check = true;
                    }
                }

                if (application.getServiceInstances().size() == 0) {
                    check = true;
                }

                if (check) {
                    deletedConfigComponents.add(oldInst.getName());
                    String[] nodes = oldInst.getNodes();
                    if (nodes != null)
                        deletedComponents.add(oldInst.getName());
                }
            }

            applicationLogManager.deleteLogs(oldApp, deletedComponents);
            if (!deletedConfigComponents.isEmpty())
                deleteConfigurations(oldApp, deletedConfigComponents);

            if (!deletedPorts.isEmpty())
                deletePortTransformations(oldApp, deletedPorts);

            Vector<String> deletedRoutes = new Vector<>();
            for (Route oldRoute : oldApp.getRoutes()) {
                boolean check = false;

                for (Route newRoute : application.getRoutes()) {
                    if (oldRoute.getName().equals(newRoute.getName())) {
                        check = false;
                        break;
                    } else {
                        check = true;
                    }
                }

                if (application.getRoutes().size() == 0)
                    check = true;

                if (check)
                    deletedRoutes.add(oldRoute.getName());
            }

            if (!deletedRoutes.isEmpty())
                deleteRouteConfigurations(oldApp, deletedRoutes);
        }
        chainLaunchHelper.updateChainLaunchDS(application);
        ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_SAVED, Event.EventCategory.INFORMATION,
                appGuid, application.getDisplayName(),String.valueOf(version), "Application saved Successfully");
    }

    public void saveApplication(Application application, boolean skipManagableProps, String handleID) throws FioranoException {
        String userName = securityManager.getUserName(handleID);
        Application oldApp = savedApplicationMap.get(application.getGUID() + Constants.NAME_DELIMITER + String.valueOf(application.getVersion()));
        applicationRepository.saveApplication(application, userName, handleID, skipManagableProps);

        if (oldApp != null) {
            Vector<String> deletedComponents = new Vector<>();
            Vector<String> deletedConfigComponents = new Vector<>();
            HashMap<String, String> deletedPorts = new HashMap<>();

            for (Object o1 : oldApp.getServiceInstances()) {
                boolean check = false;
                ServiceInstance oldInst = (ServiceInstance) o1;

                for (Object o : application.getServiceInstances()) {
                    ServiceInstance newInst = (ServiceInstance) o;
                    if (oldInst.getName().equals(newInst.getName())) {
                        check = false;

                        List<OutputPortInstance> oldOutputPortInstances = oldInst.getOutputPortInstances();
                        for (OutputPortInstance oldPort : oldOutputPortInstances) {
                            boolean deletePort = false;

                            List<OutputPortInstance> newOutputPortInstances = newInst.getOutputPortInstances();
                            for (OutputPortInstance newPort : newOutputPortInstances) {
                                if (oldPort.getName().equals(newPort.getName())) {
                                    deletePort = false;
                                    break;
                                } else {
                                    deletePort = true;
                                }
                            }

                            if (newInst.getOutputPortInstances().size() == 0)
                                deletePort = true;

                            if (deletePort)
                                deletedPorts.put(oldInst.getName(), oldPort.getName());
                        }

                        break;
                    } else {
                        check = true;
                    }
                }

                if (application.getServiceInstances().size() == 0) {
                    check = true;
                }

                if (check) {
                    deletedConfigComponents.add(oldInst.getName());
                    String[] nodes = oldInst.getNodes();
                    if (nodes != null)
                        deletedComponents.add(oldInst.getName());
                }
            }
            applicationLogManager.deleteLogs(oldApp, deletedComponents);
            if (!deletedConfigComponents.isEmpty())
                deleteConfigurations(oldApp, deletedConfigComponents);

            if (!deletedPorts.isEmpty())
                deletePortTransformations(oldApp, deletedPorts);

            Vector<String> deletedRoutes = new Vector<>();
            for (Route oldRoute : oldApp.getRoutes()) {
                boolean check = false;

                for (Route newRoute : application.getRoutes()) {
                    if (oldRoute.getName().equals(newRoute.getName())) {
                        check = false;
                        break;
                    } else {
                        check = true;
                    }
                }

                if (application.getRoutes().size() == 0)
                    check = true;

                if (check)
                    deletedRoutes.add(oldRoute.getName());
            }

            if (!deletedRoutes.isEmpty())
                deleteRouteConfigurations(oldApp, deletedRoutes);
        }

        savedApplicationMap.put(application.getGUID() + Constants.NAME_DELIMITER + application.getVersion(), application);
        chainLaunchHelper.updateChainLaunchDS(application);
        ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_SAVED, Event.EventCategory.INFORMATION,
                application.getGUID(), application.getDisplayName(), String.valueOf(application.getVersion()), "Application saved Successfully");
    }

    /**
     * Deletes port transformations for specified ports. This method is used when some of the output ports of a service
     * instance are deleted at the time of orchestration. For example, deleting an output port of CBR component.
     *
     * @param oldApp       Application DMI
     * @param deletedPorts HashMap of (ServiceInstanceName, PortName)
     */
    private void deletePortTransformations(Application oldApp, HashMap<String, String> deletedPorts) {
        try {
            applicationRepository.deletePortTransformations(oldApp, deletedPorts);
        } catch (FioranoException e) {
            logger.error("Error occurred while deleting port transformations.", e);
        }
    }

    private void deleteConfigurations(Application oldApp, Vector<String> deletedConfigComponents) {
        try {
            applicationRepository.deleteConfigs(oldApp, deletedConfigComponents);
        } catch (FioranoException e) {
            logger.error("Error occurred while deleting named configurations.", e);
        }
    }

    private void deleteRouteConfigurations(Application oldApp, Vector<String> deletedRoutes) {
        try {
            applicationRepository.deleteRouteConfigs(oldApp, deletedRoutes);
        } catch (FioranoException e) {
            logger.error("Error occurred while deleting route configurations.");
        }
    }

    public Set<String> getListOfRunningApplications(){
        return applicationHandleMap.keySet();
    }

    public boolean launchApplication(String appGuid, String version, String handleID) throws Exception {
        logger.info("Launching application : " + appGuid + ":" + version);
        Map<String, Boolean> orderedListOfApplications = getApplicationChainForLaunch(appGuid, Float.parseFloat(version), handleID);
        ResourceAvailabilityChecker.checkResourceAndConnectivity(this, microServiceRepoManager, handleID, orderedListOfApplications);
        for (String app_version: orderedListOfApplications.keySet()) {
            String[] current_AppGUIDAndVersion = ApplicationUtil.returnAppGUIDAndVersion(app_version);
            String currentGUID = current_AppGUIDAndVersion[0];
            Float currentVersion = Float.valueOf(current_AppGUIDAndVersion[1]);
            Application currentApplication = savedApplicationMap.get(currentGUID + Constants.NAME_DELIMITER + String.valueOf(currentVersion));
            if (!isApplicationRunning(currentGUID, currentVersion, handleID)) {
                    ApplicationHandle appHandle = new ApplicationHandle(this, currentApplication, microServiceLauncher, routeService,transport, securityManager.getUserName(handleID), securityManager.getPassword(handleID));
                    appHandle.createRoutes();
                applicationHandleMap.put(app_version, appHandle);
                ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_LAUNCHED, Event.EventCategory.INFORMATION,
                        currentGUID, currentApplication.getDisplayName(), current_AppGUIDAndVersion[1], "Application launched Successfully");

                appHandle.startAllMicroServices();
                    updateApplicationHandleMap(appHandle);
                    logger.info("Launched application: " + currentGUID + ":" + current_AppGUIDAndVersion[1]);
            }
        }
        return true;
    }

    public boolean stopApplication(String appGuid, String version, String handleID) throws Exception {
        logger.info("Stopping application: " + appGuid + ":" + version);
        Map<String, Boolean> orderedListOfApplications = getApplicationChainForShutdown(appGuid, Float.parseFloat(version), handleID);
        orderedListOfApplications.put(appGuid + Constants.NAME_DELIMITER + version, isApplicationRunning(appGuid, Float.parseFloat(version), handleID));
        for (String app_version: orderedListOfApplications.keySet()) {
            String[] appGUIDAndVersion = ApplicationUtil.returnAppGUIDAndVersion(app_version);
            String currentGUID = appGUIDAndVersion[0];
            Float currentVersion = Float.valueOf(appGUIDAndVersion[1]);
            if (isApplicationRunning(currentGUID, currentVersion, handleID)) {
                ApplicationHandle applicationHandle = getApplicationHandle(currentGUID, currentVersion, handleID);
                applicationHandle.stopApplication();
                removeApplicationHandleFromMap(currentGUID, String.valueOf(currentVersion));
                ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_STOPPED, Event.EventCategory.INFORMATION,
                        currentGUID, applicationHandle.getApplication().getDisplayName(), String.valueOf(currentVersion), "Application stopped Successfully");
            }

        }
        logger.info("Stopped application: " + appGuid + ":" + version);
        return true;
    }

    public boolean synchronizeApplication(String appGuid, String version, String handleID) throws FioranoException{
        logger.debug("synchronizing Application " + appGuid + ":" + version);
        Map<String, Boolean> orderedList = getApplicationChainForLaunch(appGuid, Float.parseFloat(version), handleID);
        ResourceAvailabilityChecker.checkResourceAndConnectivity(this, microServiceRepoManager, handleID, orderedList);
        for (String app_version : orderedList.keySet()) {
            String[] appGUIDAndVersion = ApplicationUtil.returnAppGUIDAndVersion(app_version);
            String currentGUID = appGUIDAndVersion[0];
            Float currentVersion = Float.valueOf(appGUIDAndVersion[1]);
            if (!isApplicationRunning(currentGUID, currentVersion, handleID)){
                try {
                    launchApplication(currentGUID, String.valueOf(currentVersion), handleID);
                } catch (Exception e) {
                    logger.error("APPLICATION_LAUNCH_EXCEPTION", e);
                }
            }
        }
        String key = appGuid + Constants.NAME_DELIMITER + version;
        ApplicationHandle applicationHandle = applicationHandleMap.get(key);
        applicationHandle.synchronizeApplication(savedApplicationMap.get(key));
        if (chainLaunchHelper.getReferringAppsList().containsKey(key)){
            for (String  app_version:chainLaunchHelper.getReferringAppsList().get(key)){
                if(!app_version.equals(key)) {
                    applicationHandle = applicationHandleMap.get(app_version);
                    if (applicationHandle!=null)
                        applicationHandle.synchronizeApplication(savedApplicationMap.get(app_version));
                }
            }
        }

        logger.info("APPLICATION_SYNCHRONIZED");
        return true;
    }

    public boolean startAllMicroServices(String appGuid, String version, String handleID) throws FioranoException{
        logger.trace("User " + securityManager.getUserName(handleID) + " starting all services in the application " + ApplicationUtil.getKey(appGuid, version));
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            appHandle.startAllMicroServices();
            persistApplicationState(appHandle);
        }
        return true;
    }

    public boolean stopAllMicroServices(String appGuid, String version, String handleID) throws FioranoException{
        logger.trace("User "+ securityManager.getUserName(handleID) +" stopping all services in the application " + ApplicationUtil.getKey(appGuid, version));
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            appHandle.stopAllMicroServices();
            persistApplicationState(appHandle);
            return true;
        }
        return false;
    }

    public boolean startMicroService(String appGuid, String version, String microServiceName, String handleID) throws FioranoException{
        logger.trace("User " + securityManager.getUserName(handleID) + " starting service " + microServiceName + " in the application " + ApplicationUtil.getKey(appGuid, version));
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            appHandle.startMicroService(microServiceName);
            persistApplicationState(appHandle);
            return true;
        }
        return false;
    }

    public boolean isMicroserviceRunning(String appGuid, String version, String microServiceName) throws FioranoException{
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            return appHandle.isMicroserviceRunning(microServiceName);
        }
        return false;
    }
   /* public MemoryUsage getMemoryUsage(String appGuid, String version, String microServiceName, String handleID) throws FioranoException{
        logger.trace("User "+ securityManager.getUserName(handleID) +" trying to access memory usage of the component "+ microServiceName+" of the application " + getKey(appGuid, version));
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            return appHandle.getMemoryUsage(microServiceName);
        }
        return null;
    }*/
    public boolean stopMicroService(String appGuid, String version, String microServiceName, String handleID) throws FioranoException{
        logger.trace("User "+ securityManager.getUserName(handleID) +" stopping the service" + microServiceName+" in the application " + ApplicationUtil.getKey(appGuid, version));
        String key = appGuid+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            ApplicationHandle appHandle = applicationHandleMap.get(key);
            appHandle.stopMicroService(microServiceName);
            persistApplicationState(appHandle);
            return true;
        }
        return false;
    }

    public void deleteApplication(String appGUID, String version, String handleID) throws FioranoException {
        logger.trace("User "+ securityManager.getUserName(handleID) +" deleting the application " + ApplicationUtil.getKey(appGUID, version));
        String key = appGUID+Constants.NAME_DELIMITER+version;
        if(applicationHandleMap.containsKey(key)){
            throw new FioranoException("Cannot delete running Application. Stop the Application and then delete");
        }
        applicationRepository.deleteApplication(appGUID, version);
        try{
            applicationLogManager.clearApplicationLogs(appGUID, Float.parseFloat(version));
        }catch (Throwable th){
            logger.error(th.getMessage());
        }
        savedApplicationMap.remove(key);
        chainLaunchHelper.removeChainLaunchDS(key);
        ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_DELETED, Event.EventCategory.INFORMATION,
                appGUID, null, version, "Application Deleted Successfully");

    }

    public ApplicationHandle getApplicationHandle(String appGUID, float appVersion, String handleID) {
        logger.trace("User " + securityManager.getUserName(handleID) + " checking if the applicaiton " + ApplicationUtil.getKey(appGUID, String.valueOf(appVersion)) + " is running.");
        return applicationHandleMap.get(appGUID+"__"+appVersion);
    }

    public ApplicationHandle getApplicationHandle(String appGUID, float appVersion) {
        return applicationHandleMap.get(appGUID + "__" + appVersion);
    }

    public boolean isApplicationRunning(String appGUID, float version, String handleID) throws FioranoException {
        return (getApplicationHandle(appGUID, version, handleID) != null);
    }

    public void changePortAppContext(String appGUID, float appVersion, String serviceName, String portName, String newTransformation, String newJMSTransformation, String transformerType, String transformationProject, String handleId) throws FioranoException{

        Application application = savedApplicationMap.get(appGUID+Constants.NAME_DELIMITER+appVersion);
        ServiceInstance serviceInstance = application.getServiceInstance(serviceName);
        OutputPortInstance port = serviceInstance.getOutputPortInstance(portName);
        Transformation transformation = port.getApplicationContextTransformation();

        if (transformation != null) {
            if (transformation.getTransformationConfigName() != null && (!StringUtil.isEmpty(newTransformation) || !StringUtil.isEmpty(newJMSTransformation)))
                throw new FioranoException("ERR_APPCONTEXT_TRANSFORMATION_CHANGE_NOT_ALLOWED");
        }

        Transformation trans;
        // set the new transformation
        if (StringUtil.isEmpty(newTransformation) && StringUtil.isEmpty(newJMSTransformation))
            port.setApplicationContextTransformation(null);
        else {
            trans = port.getApplicationContextTransformation();
            if (trans == null) {
                if (newJMSTransformation != null) {
                    trans = new MessageTransformation();
                    ((MessageTransformation) trans).setJMSScript(newJMSTransformation);
                } else {
                    trans = new Transformation();
                }
            }
            trans.setScript(newTransformation);
            trans.setFactory(transformerType);
            trans.setProject(transformationProject);
            port.setApplicationContextTransformation(trans);
        }

        ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleId);
        if (appHandle != null) {
            for(final Route route: application.getRoutes()) {
                if(route.getSourcePortInstance().equals(portName)){
                    TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                    transformationConfiguration.setXsl(newTransformation);
                    transformationConfiguration.setTransformerType(transformerType);
                    transformationConfiguration.setJmsXsl(newJMSTransformation);
                    transformationConfiguration.setRouteOperationType(RouteOperationType.APP_CONTEXT_TRANSFORM);
                    try {
                        appHandle.changeRouteOperationHandler(route.getName(), transformationConfiguration);
                    } catch (Exception e) {
                        logger.error("Error occurred while changing port application context. ",e);
                    }
                }
            }
        }
    }

    public void changePortAppContextConfiguration(String appGUID, float appVersion, String serviceName, String portName, String configurationName, String handleId) throws FioranoException{
        Application application = savedApplicationMap.get(appGUID + Constants.NAME_DELIMITER + appVersion);
        ServiceInstance serviceInstance = application.getServiceInstance(serviceName);
        OutputPortInstance port = serviceInstance.getOutputPortInstance(portName);
        Transformation transformation = port.getApplicationContextTransformation();

        String configurationRepoPath = namedConfigRepository.getConfigurationRepositoryPath();
        String transformationDirPath = NamedConfigurationUtil.getConfigurationPath(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, ConfigurationRepoConstants.TRANSFORMATIONS, application.getLabel() + File.separator + configurationName);
        if (!new File(transformationDirPath).exists())
            throw new FioranoException("APPCONTEXT_TRANSFORMATION_CONFIGURATION_NOT_FOUND");

        String metaDataFilePath = NamedConfigurationUtil.getConfigurationPath(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, ConfigurationRepoConstants.TRANSFORMATIONS, application.getLabel() + File.separator + ConfigurationRepoConstants.METADATA_XML);
        ApplicationParser.TransformationConfig transformationConfig;
        try {
            transformationConfig = ApplicationParser.getTransformationParams(metaDataFilePath, configurationName);
        } catch (FileNotFoundException e) {
            throw new FioranoException("METADATA_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND", e);
        } catch (XMLStreamException e) {
            throw new FioranoException("ERR_READING_TRANSFORMATION_METADATA_CONFIGURATION", e);
        }

        try {
            if (transformationConfig != null) {
                String scriptFileName = transformationConfig.getScriptFileName();
                String jmsScriptFileName = transformationConfig.getJmsScriptFileName();
                String projectFileName = transformationConfig.getProjectFileName();

                if (transformation == null) {
                    transformation = (jmsScriptFileName != null ? new MessageTransformation() : new Transformation());
                    transformation.setTransformationConfigName(configurationName);
                }
                String factoryName = transformationConfig.getFactoryName();
                transformation.setFactory(factoryName);

                if (scriptFileName != null) {
                    File scriptFile = new File(transformationDirPath, scriptFileName);
                    if (scriptFile.exists()) {
                        try (FileInputStream scriptFileStream = new FileInputStream(scriptFile)){
                            String script = DmiObject.getContents(scriptFileStream);
                            transformation.setScript(script);
                        }
                    } else
                        throw new FioranoException("SCRIPT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND");
                }

                if (jmsScriptFileName != null) {
                    File jmsScriptFile = new File(transformationDirPath, jmsScriptFileName);
                    if (jmsScriptFile.exists()) {
                        try(FileInputStream jmsScriptFileStream = new FileInputStream(jmsScriptFile)) {
                            String jmsScript = DmiObject.getContents(jmsScriptFileStream);
                            ((MessageTransformation) transformation).setJMSScript(jmsScript);
                        }
                    } else
                        throw new FioranoException("JMS_SCRIPT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND");
                }

                if (projectFileName != null) {
                    File projectFile = new File(transformationDirPath, projectFileName);
                    if (projectFile.exists()) {
                        if (projectFile.isFile()) {
                            try(FileInputStream projectFileStream = new FileInputStream(projectFile)) {
                                String project = DmiObject.getContents(projectFileStream);
                                transformation.setProject(project);
                            }
                        } else {
                            String project = ApplicationParser.toXML(projectFile);
                            transformation.setProject(project);
                        }
                    } else
                        throw new FioranoException("PROJECT_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND");
                }

                port.setApplicationContextTransformation(transformation);

                ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleId);
                if (appHandle != null) {
                    for(final Route route: application.getRoutes()) {
                        if(route.getSourcePortInstance().equals(portName)){
                            TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                            transformationConfiguration.setXsl(transformation.getScript());
                            transformationConfiguration.setTransformerType(transformation.getFactory());
                            transformationConfiguration.setJmsXsl(transformation.getJMSScript());
                            transformationConfiguration.setRouteOperationType(RouteOperationType.APP_CONTEXT_TRANSFORM);
                            try {
                                appHandle.changeRouteOperationHandler(route.getName(), transformationConfiguration);
                            } catch (Exception e) {
                                logger.error("Error occurred while changing port application context. ",e);
                            }
                        }
                    }
                }
            } else
                throw new FioranoException("METADATA_FOR_APPCONTEXT_TRANSFORMATION_NOT_FOUND");
        } catch (IOException | XMLStreamException e) {
            throw new FioranoException("ERR_READING_TRANSFORMATION_CONFIGURATION", e);
        }
    }

    public void changeRouteTransformation(String appGUID, float appVersion, String routeGUID, String newTransformation, String newJMSTransformation, String transformerType, String transformationProject, String handleId) throws FioranoException{

        Application application = savedApplicationMap.get(appGUID+Constants.NAME_DELIMITER+appVersion);

        // get the route. This throws an exception if route is not present
        Route route = getRoute(routeGUID, application);
        MessageTransformation transformation = route.getMessageTransformation();

        if (transformation != null) {
            if (transformation.getTransformationConfigName() != null && (!StringUtil.isEmpty(newTransformation) || !StringUtil.isEmpty(newJMSTransformation)))
                throw new FioranoException("ERR_ROUTE_TRANSFORMATION_CHANGE_NOT_ALLOWED");
        }

        ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleId);
        // set the new route transformation
        if (StringUtil.isEmpty(newTransformation) && StringUtil.isEmpty(newJMSTransformation))
            route.setMessageTransformation(null);
        else {
            MessageTransformation trans = route.getMessageTransformation();
            if (trans == null)
                route.setMessageTransformation(trans = new MessageTransformation());
            trans.setScript(newTransformation);
            trans.setFactory(transformerType);
            trans.setJMSScript(newJMSTransformation);
            trans.setProject(transformationProject);
        }

        saveApplication(application, false, handleId);
        if (appHandle != null) {
            appHandle.setApplication(application);
            try {
                TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                transformationConfiguration.setXsl(newTransformation);
                transformationConfiguration.setTransformerType(transformerType);
                transformationConfiguration.setJmsXsl(newJMSTransformation);
                transformationConfiguration.setRouteOperationType(RouteOperationType.ROUTE_TRANSFORM);
               appHandle.changeRouteOperationHandler(routeGUID, transformationConfiguration);
            } catch (Exception e) {
                throw new FioranoException(e);
            }
        }

    }

    public void changeRouteTransformationConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName, String handleId) throws FioranoException{
        Application application = savedApplicationMap.get(appGUID+Constants.NAME_DELIMITER+appVersion);

        // get the route. This throws an exception if route is not present
        Route route = getRoute(routeGUID, application);
        MessageTransformation transformation = route.getMessageTransformation();
        String configurationRepoPath = namedConfigRepository.getConfigurationRepositoryPath();

        if (transformation == null)
            transformation = new MessageTransformation();

        transformation.setTransformationConfigName(configurationName);
        route.setMessageTransformation(transformation);

        String transformationDirPath = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, ConfigurationRepoConstants.TRANSFORMATIONS, application.getLabel(), configurationName).getAbsolutePath();
        if (!new File(transformationDirPath).exists())
            throw new FioranoException("ROUTE_TRANSFORMATION_CONFIGURATION_NOT_FOUND");

        String metaDataFilePath = NamedConfigurationUtil.getConfigurationPath(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, ConfigurationRepoConstants.TRANSFORMATIONS, application.getLabel() + File.separator + ConfigurationRepoConstants.METADATA_XML);
        ApplicationParser.TransformationConfig transformationConfig;
        try {
            transformationConfig = ApplicationParser.getTransformationParams(metaDataFilePath, configurationName);
        } catch (FileNotFoundException e) {
            throw new FioranoException("METADATA_FOR_ROUTE_TRANSFORMATION_NOT_FOUND", e);
        } catch (XMLStreamException e) {
            throw new FioranoException("ERR_READING_TRANSFORMATION_METADATA_CONFIGURATION", e);
        }

        try {
            if (transformationConfig != null) {
                String scriptFileName = transformationConfig.getScriptFileName();
                String jmsScriptFileName = transformationConfig.getJmsScriptFileName();
                String projectFileName = transformationConfig.getProjectFileName();

                String factoryName = transformationConfig.getFactoryName();
                transformation.setFactory(factoryName);

                if (scriptFileName != null) {
                    File scriptFile = new File(transformationDirPath, scriptFileName);

                    if (scriptFile.exists()) {
                        try(FileInputStream scriptFileStream = new FileInputStream(scriptFile)) {
                            String script = DmiObject.getContents(scriptFileStream);
                            transformation.setScript(script);
                        }
                    } else
                        throw new FioranoException("SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                }

                if (jmsScriptFileName != null) {
                    File jmsScriptFile = new File(transformationDirPath, jmsScriptFileName);

                    if (jmsScriptFile.exists()) {
                        try(FileInputStream jmsScriptFileStream = new FileInputStream(jmsScriptFile)) {
                            String jmsScript = DmiObject.getContents(jmsScriptFileStream);
                            transformation.setJMSScript(jmsScript);
                        }
                    } else
                        throw new FioranoException("JMS_SCRIPT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                }

                if (projectFileName != null) {
                    File projectFile = new File(transformationDirPath, projectFileName);

                    if (projectFile.exists()) {
                        if (projectFile.isFile()) {
                            try(FileInputStream projectFileStream = new FileInputStream(projectFile)) {
                                String project = DmiObject.getContents(projectFileStream);
                                transformation.setProject(project);
                            }
                        } else {
                            String project = ApplicationParser.toXML(projectFile);
                            transformation.setProject(project);
                        }
                    } else
                        throw new FioranoException("PROJECT_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
                }

                ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleId);
                if (appHandle != null) {
                    try {
                        TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                        transformationConfiguration.setXsl(transformation.getScript());
                        transformationConfiguration.setTransformerType(transformationConfig.getFactoryName());
                        transformationConfiguration.setJmsXsl( transformation.getJMSScript());
                        transformationConfiguration.setRouteOperationType(RouteOperationType.ROUTE_TRANSFORM);
                        appHandle.changeRouteOperationHandler(routeGUID, transformationConfiguration);
                    } catch (Exception e) {
                        throw new FioranoException(e);
                    }
                }

                saveApplication(application,false, handleId);

            } else
                throw new FioranoException("METADATA_FOR_ROUTE_TRANSFORMATION_NOT_FOUND");
        } catch (IOException | XMLStreamException e) {
            throw new FioranoException("ERR_READING_TRANSFORMATION_CONFIGURATION", e);
        }
    }

    private Route getRoute(String routeGUID, Application application) throws FioranoException {
        for (Route o : application.getRoutes()) {
            if (o.getName().equalsIgnoreCase(routeGUID)) {
                return o;
            }
        }
        logger.error("ROUTE_NOT_FOUND");
        throw new FioranoException("ROUTE_NOT_FOUND");
    }

    public void changeRouteSelector(String appGUID, float appVersion, String routeGUID, HashMap modifiedSelectors, String handleID) throws FioranoException {
        Application application = savedApplicationMap.get(appGUID + Constants.NAME_DELIMITER + appVersion);

        // get the route. This throws an exception if route is not present
        Route route = getRoute(routeGUID, application);

        ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleID);
        if (appHandle != null) {
            if(modifiedSelectors.containsKey(Route.SELECTOR_SENDER)){
                SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
                senderSelectorConfiguration.setSourceName((String) modifiedSelectors.get("sender"));
                senderSelectorConfiguration.setAppID(application.getGUID() + ":" + application.getVersion());
                senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
                try {
                    appHandle.changeRouteOperationHandler(routeGUID, senderSelectorConfiguration);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE MODIFYING SENDER SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE MODIFYING SENDER SELECTOR");
                }
            }
            if(modifiedSelectors.containsKey(Route.SELECTOR_APPLICATION_CONTEXT)) {
                XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
                XPathSelector xPathSelector = (XPathSelector) modifiedSelectors.get("application-context");
                appContextSelectorConfig.setXpath(xPathSelector.getXPath());
                appContextSelectorConfig.setNsPrefixMap(xPathSelector.getNamespaces());
                appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
                try {
                    appHandle.changeRouteOperationHandler(routeGUID, appContextSelectorConfig);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE MODIFYING APPLICATION CONTEXT SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE MODIFYING APPLICATION CONTEXT SELECTOR");
                }
            }

            if(modifiedSelectors.containsKey(Route.SELECTOR_BODY)) {
                XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
                XPathSelector xPathSelector = (XPathSelector) modifiedSelectors.get("body");
                bodySelectorConfig.setXpath(xPathSelector.getXPath());
                bodySelectorConfig.setNsPrefixMap(xPathSelector.getNamespaces());
                bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
                try {
                    appHandle.changeRouteOperationHandler(routeGUID, bodySelectorConfig);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE MODIFYING MESSAGE BODY SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE MODIFYING MESSAGE BODY SELECTOR");
                }
            }

            if(route.getApplicationContextSelector()!=null && !modifiedSelectors.containsKey("application-context")){
                XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
                appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
                try {
                    appHandle.removeRouteOperationHandler(routeGUID, appContextSelectorConfig);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE REMOVING APPLICATION CONTEXT SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE REMOVING APPLICATION CONTEXT SELECTOR");
                }

            }
            if(route.getSenderSelector()!=null && !modifiedSelectors.containsKey("sender")){
                SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
                senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
                try {
                    appHandle.removeRouteOperationHandler(routeGUID, senderSelectorConfiguration);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE REMOVING SENDER SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE REMOVING SENDER SELECTOR");
                }

            }
            if(route.getBodySelector()!=null && !modifiedSelectors.containsKey("body")){
                XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
                bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
                try {
                    appHandle.removeRouteOperationHandler(routeGUID, bodySelectorConfig);
                } catch (Exception e) {
                    logger.error("EXCEPTION WHILE REMOVING MESSAGE BODY SELECTOR");
                    throw new FioranoException("EXCEPTION WHILE REMOVING MESSAGE BODY SELECTOR");
                }

            }
        }

        // set the selector type and its value in the route
        route.setSelectors(modifiedSelectors);
        route.setSelectorConfigName(null);

        saveApplication(application, false, handleID);
    }

    public void changeRouteSelectorConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName, String handleID) throws FioranoException {

        Application application = savedApplicationMap.get(appGUID + Constants.NAME_DELIMITER + appVersion);

        // get the route. This throws an exception if route is not present
        Route route = getRoute(routeGUID, application);

        String configurationRepoPath = namedConfigRepository.getConfigurationRepositoryPath();

        FileInputStream fis = null;
        FioranoStaxParser cursor = null;
        try {
            File selectorConfigPath = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, ConfigurationRepoConstants.SELECTORS, application.getLabel(), configurationName);
            fis = new FileInputStream(selectorConfigPath);
            cursor = new FioranoStaxParser(fis);
            route.populateSelectorsConfiguration(cursor);
            route.setSelectorConfigName(configurationName);
            ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleID);
            Map selectors = route.getSelectors();
            if (appHandle != null) {
                if (selectors.containsKey(Route.SELECTOR_SENDER)) {
                    SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
                    senderSelectorConfiguration.setSourceName(route.getSenderSelector());
                    senderSelectorConfiguration.setAppID(application.getGUID() + ":" + application.getVersion());
                    senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
                    try {
                        appHandle.changeRouteOperationHandler(routeGUID, senderSelectorConfiguration);
                    } catch (Exception e) {
                        logger.error("EXCEPTION WHILE MODIFYING SENDER SELECTOR CONFIGURATION");
                        throw new FioranoException("EXCEPTION WHILE MODIFYING SENDER SELECTOR CONFIGURATION");
                    }
                }
                if (selectors.containsKey(Route.SELECTOR_APPLICATION_CONTEXT)) {
                    XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
                    appContextSelectorConfig.setXpath(route.getApplicationContextSelector().getXPath());
                    appContextSelectorConfig.setNsPrefixMap(route.getApplicationContextSelector().getNamespaces());
                    appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
                    try {
                        appHandle.changeRouteOperationHandler(routeGUID, appContextSelectorConfig);
                    } catch (Exception e) {
                        logger.error("EXCEPTION WHILE MODIFYING APPLICATION CONTEXT SELECTOR CONFIGURATION");
                        throw new FioranoException("EXCEPTION WHILE MODIFYING APPLICATION CONTEXT SELECTOR CONFIGURATION");
                    }
                }

                if (selectors.containsKey(Route.SELECTOR_BODY)) {
                    XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
                    bodySelectorConfig.setXpath(route.getBodySelector().getXPath());
                    bodySelectorConfig.setNsPrefixMap(route.getBodySelector().getNamespaces());
                    bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
                    try {
                        appHandle.changeRouteOperationHandler(routeGUID, bodySelectorConfig);
                    } catch (Exception e) {
                        logger.error("EXCEPTION WHILE MODIFYING MESSAGE BODY SELECTOR CONFIGURATION");
                        throw new FioranoException("EXCEPTION WHILE MODIFYING MESSAGE BODY SELECTOR CONFIGURATION");
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new FioranoException("ROUTE_SELECTOR_TRANSFORMATION_NOT_FOUND", e);
        } finally {
            try {
                if (cursor != null)
                    cursor.disposeParser();
            } catch (XMLStreamException ignore) {
                //Ignore
            }

            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                //Ignore
            }
        }

        saveApplication(application, false, handleID);

    }

    public Enumeration<ApplicationReference> getHeadersOfRunningApplications(String handleId) throws FioranoException{
        logger.trace("User "+ securityManager.getUserName(handleId) +" getting headers of the running applications.");
        Vector<ApplicationReference> toReturn = new Vector<>();
        // get the running application handles and fetch the application info packet from the handles.
        for (ApplicationHandle appHandle:applicationHandleMap.values()) {

            try {
                toReturn.addElement(new ApplicationReference(appHandle.getApplication()));
            } catch (Exception e) {
                logger.error("error occured while getting the header of running application", e);
            }
        }
        return toReturn.elements();
    }

    public Enumeration<ApplicationReference> getHeadersOfSavedApplications(String handleId) throws FioranoException{
        logger.trace("User " + securityManager.getUserName(handleId) + " getting headers of saved applications.");
        Vector<ApplicationReference> toReturn = new Vector<>();
        // get the running application handles and fetch the application info packet from the handles.
        for (Application app:savedApplicationMap.values()) {

            try {
                toReturn.addElement(new ApplicationReference(app));
            } catch (Exception e) {
                logger.error("error occured while getting the header of saved application", e);
            }
        }
        return toReturn.elements();
    }

    public Map<String, Boolean> getApplicationChainForShutdown(String appGUID, float version, String handleId) throws FioranoException{
        Set<String> applicationChain = new LinkedHashSet<>();
        String app_version = appGUID + Constants.NAME_DELIMITER + version;
        chainLaunchHelper.populateReferringList(app_version, applicationChain);
        applicationChain.add(app_version);
        Map<String, Boolean> result = new LinkedHashMap<>();
        for (String appDetails : applicationChain){
            String[] details = ApplicationUtil.returnAppGUIDAndVersion(appDetails);
            String GUID = details[0];
            result.put(appDetails, isApplicationRunning(GUID, Float.valueOf(details[1]), handleId));
        }
        return result;
    }

    public Map<String, Boolean> getApplicationChainForLaunch(String appGUID, float appVersion, String handleId) throws FioranoException{
        Set<String> orderedListOfApplicationsForLaunch = new  LinkedHashSet<>();
        String appGUID_version = appGUID + Constants.NAME_DELIMITER + appVersion;
        orderedListOfApplicationsForLaunch = chainLaunchHelper.populateDependencyList(appGUID_version, orderedListOfApplicationsForLaunch, appGUID_version);
        orderedListOfApplicationsForLaunch.add(appGUID_version);
        Map<String, Boolean> result = new LinkedHashMap<>();
        for (String app_version : orderedListOfApplicationsForLaunch){
            String[] appDetails = ApplicationUtil.returnAppGUIDAndVersion(app_version);
            result.put(app_version, isApplicationRunning(appDetails[0], Float.valueOf(appDetails[1]), handleId));
        }
        return result;
    }

    public void checkResourceAndConnectivity(String appGuid, float version, String handleID) throws FioranoException{
        //validate services in all applications
        Map<String, Boolean> orderedList = getApplicationChainForLaunch(appGuid, version, handleID);
        ResourceAvailabilityChecker.checkResourceAndConnectivity(this, microServiceRepoManager, handleID, orderedList);
    }

    public ApplicationStateDetails getCurrentStateOfApplication(String appGUID, float appVersion, String handleId) throws FioranoException{
        ApplicationHandle appHandle = getApplicationHandle(appGUID, appVersion, handleId);
        if (appHandle == null) {
            ApplicationStateDetails apsd = new ApplicationStateDetails();
            Application application = savedApplicationMap.get(appGUID+Constants.NAME_DELIMITER+appVersion);
            apsd.setAppGUID(appGUID);
            apsd.setAppVersion(String.valueOf(application.getVersion()));
            apsd.setDisplayName(application.getDisplayName());
            List<ServiceInstance> sis = application.getServiceInstances();
            for(ServiceInstance si:sis){
                ServiceInstanceStateDetails sisd = new ServiceInstanceStateDetails();
                sisd.setServiceInstanceName(si.getName());
                sisd.setServiceGUID(si.getGUID());
                sisd.setRunningVersion(String.valueOf(si.getVersion()));
                sisd.setLaunchType(si.getLaunchType());
                sisd.setStatusString(EventStateConstants.SERVICE_HANDLE_UNBOUND);
                apsd.addServiceStatus(si.getName(), sisd);
            }
            return apsd;
        } else {
            return appHandle.getApplicationDetails();
        }
    }

    public ApplicationReference getHeaderOfSavedApplication(String appGUID, float version, String handleId) {
        logger.trace("User " + securityManager.getUserName(handleId) + " getting header of the saved application " + ApplicationUtil.getKey(appGUID, String.valueOf(version)));
        return savedApplicationMap.get(appGUID + "__" + version);
    }

    public boolean isServiceRunning(String eventProcessName, float appVersion, String servInstanceName) {
        ApplicationHandle applicationHandle = applicationHandleMap.get(eventProcessName + Constants.NAME_DELIMITER + appVersion);

        return applicationHandle != null && applicationHandle.isMicroserviceRunning(servInstanceName);
    }

    public void Stop() {
        for(ApplicationHandle appHandle: applicationHandleMap.values()){
            appHandle.stopAllMicroServices();
            appHandle.stopAllRoutes();
        }
    }

    public ServiceInstance getServiceInstance(String eventProcessName, float appVersion, String servInstanceName) throws FioranoException{
        Application application = savedApplicationMap.get(eventProcessName+Constants.NAME_DELIMITER+appVersion);
        return application.getServiceInstance(servInstanceName);
    }

    public Properties getServerConfig() {
        return ServerConfig.getConfig().getProperties();
    }

    public Properties getTransportConfig(){
        return TransportConfig.getInstance().getProperties();
    }

    public void flushMessages(String appGUID, float appVersion, String servInstName, String handleId) throws Exception {
        logger.trace("User " + securityManager.getUserName(handleId) + " flushing messages of the service " + servInstName +" of the application" + ApplicationUtil.getKey(appGUID, String.valueOf(appVersion)));
        CommandEvent flushCommand = new CommandEvent();
        flushCommand.setCommand(CommandEvent.Command.FLUSH_MESSAGES);
        flushCommand.setReplyNeeded(false);
        ccpEventManager.getCcpEventGenerator().sendEvent(flushCommand, LookUpUtil.getServiceInstanceLookupName(appGUID, appVersion, servInstName));
    }

    public Application getSavedApplication(String appGUID, float appVersion) {
        return savedApplicationMap.get(appGUID + Constants.NAME_DELIMITER + appVersion);
    }

    public Map<String, ApplicationHandle> getApplicationHandles() {
        return applicationHandleMap;
    }

    public Set<String> getReferringRunningApplications(String appGUID, float appVersion, String servInstName) throws FioranoException {
        return chainLaunchHelper.getReferringRunningApplications(appGUID, appVersion, servInstName);
    }

    public Set<String> getAllReferringApplications(String appGUID, float appVersion, String serviceInstName) throws FioranoException {
        return chainLaunchHelper.getAllReferringApplications(appGUID, appVersion, serviceInstName);
    }

    public boolean isApplicationReferred(String appGUID, float appVersion) throws FioranoException {
        return chainLaunchHelper.isApplicationReferred(appGUID, appVersion);
    }

     /*----------------------start of [Application Restore Thread]----------------------------------------*/

    private class RestoreAppState extends Thread {

        long waitTime = 1000;

        RestoreAppState(long waitTime) {
            this.waitTime = waitTime;
        }

        public void run() {
            restoreApplicationStates(waitTime);
        }
    }

    private void restoreApplicationStates(long waitTime) {
        logger.info("Starting Application state restore thread.");
        try {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                //ignore
            }

            //get the Runinng Application List Backup
            Hashtable appStates = null;
            Object obj = NamingManagerImpl.GETINSTANCE().lookup(ApplicationControllerConstants.RUNNING_APPLICATION_LIST);
            if (obj instanceof Hashtable)
                appStates = (Hashtable) obj;

            if (appStates == null) {
                return;
            }

            Enumeration appIds = appStates.keys();

            // First all the applications are started
            while (appIds.hasMoreElements()) {
                String appGuidAndVersion = (String) appIds.nextElement();
              //  logger.info(Bundle.class, Bundle.APPLICATION_RESTORED, appGuidAndVersion);
                ApplicationInfo appInfo = (ApplicationInfo) appStates.get(appGuidAndVersion);
                ApplicationStateDetails appStateDetails = appInfo.getAppStateDetails();
                try {
                    ApplicationHandle applicationHandle = new ApplicationHandle(this, savedApplicationMap.get(appGuidAndVersion),microServiceLauncher, routeService, transport, appInfo.getUserName(), appInfo.getPassword() );
                    applicationHandle.createRoutes();
                    ApplicationEventRaiser.generateApplicationEvent(ApplicationEvent.ApplicationEventType.APPLICATION_LAUNCHED, Event.EventCategory.INFORMATION,
                            appStateDetails.getAppGUID(), null, appStateDetails.getAppVersion(), "Application launched Successfully");

                    Enumeration enumeration = appStateDetails.getAllServiceNames();
                    while(enumeration.hasMoreElements()){
                        String serviceName = (String) enumeration.nextElement();
                        ServiceInstanceStateDetails instanceStateDetails = appStateDetails.getServiceStatus(serviceName);
                        if(!instanceStateDetails.isGracefulKill()){
                            applicationHandle.startMicroService(serviceName);
                        }
                    }
                    //create breakpoints
                    Iterator debugRoutes = appStateDetails.getDebugRoutes();
                    while(debugRoutes.hasNext()){
                        String routeName = (String) debugRoutes.next();
                        applicationHandle.addBreakPoint(routeName);
                    }
                    updateApplicationHandleMap(applicationHandle);
                } catch (FioranoException ex1) {
                    //logger.error(Bundle.class, Bundle.ERROR_RESTORING_APPLICATION, appGuidAndVersion, ex1);
                    logger.error("", ex1);
                    // Log and continue with other applications
                }
            }
        } catch (Throwable ex) {
           // logger.error(Bundle.class, Bundle.ERROR_RESTORING_STATES, ex);
            logger.error("", ex);
        }
        logger.info("Restored Application state Successfully.");
    }

    private void updateApplicationHandleMap(ApplicationHandle applicationHandle) {
        applicationHandleMap.put(applicationHandle.getAppGUID()+Constants.NAME_DELIMITER+applicationHandle.getVersion(), applicationHandle);
        persistApplicationState(applicationHandle);
    }

    public void persistApplicationState(ApplicationHandle appHandle) {

        String appID = appHandle.getApplication().getGUID().toUpperCase() +Constants.NAME_DELIMITER+ appHandle.getApplication().getVersion();
            try {
                @SuppressWarnings("unchecked")
                Hashtable<String, ApplicationInfo> appStates = (Hashtable) NamingManagerImpl.GETINSTANCE().lookup(ApplicationControllerConstants.RUNNING_APPLICATION_LIST);
                if (appStates == null)
                    appStates = new Hashtable<>();

                ApplicationInfo appInfo = new ApplicationInfo();

                appInfo.setAppStateDetails(appHandle.getApplicationDetails());
                appInfo.setUserName(appHandle.getUserName());
                appInfo.setPassword(appHandle.getPasswd());
                appInfo.setAppVersion(String.valueOf(appHandle.getVersion()));
                appStates.put(appID, appInfo);
                NamingManagerImpl.GETINSTANCE().rebind(ApplicationControllerConstants.RUNNING_APPLICATION_LIST, appStates, true);
            } catch (Exception ex) {
                // there can be a NPE in very special case that while FES HA is failing over
                // while passing the adminservice=null check and before the lookup can be made
                // the naming manager is shutdown..
                if (!(ex instanceof NullPointerException))
                    logger.error("", ex);
                    //logger.error(Bundle.class, Bundle.EXCEPTION_UPDATING_STATE_IN_RUNNING_LIST, ex);

            }
    }

    private void removeApplicationHandleFromMap(String appGuid, String  version) {
        applicationHandleMap.remove(appGuid+Constants.NAME_DELIMITER+version);
        try {
            // look up the admin object where we store the list of running application.
            Hashtable appStates = (Hashtable) NamingManagerImpl.GETINSTANCE().lookup(ApplicationControllerConstants.RUNNING_APPLICATION_LIST);
            if (appStates != null) {
                appStates.remove(appGuid.toUpperCase() + Constants.NAME_DELIMITER + version);
                // Admin service will rebind after making the changes.
                NamingManagerImpl.GETINSTANCE().rebind(ApplicationControllerConstants.RUNNING_APPLICATION_LIST, appStates, true);
            }
        } catch (FioranoException ex) {
            //logger.error(Bundle.class, Bundle.EXCEPTION_WHILE_REMOVING_APPHANDLE, appGUID+Constants.NAME_DELIMITER+Float.toString(appVersion), ex.toString());
            logger.error("", ex);
        }

    }

    public void setApplicationLogManager(ApplicationLogManager applicationLogManager) {
        this.applicationLogManager = applicationLogManager;
    }

    public ApplicationLogManager getApplicationLogManager() {
        return applicationLogManager;
    }
}
