/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.BreakpointMetaData;
import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.events.ApplicationEvent;
import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.microservice.ccp.event.common.data.MemoryUsage;
import com.fiorano.openesb.microservice.ccp.event.common.data.ComponentStats;
import com.fiorano.openesb.microservice.launch.java.JavaLaunchConfiguration;
import com.fiorano.openesb.microservice.launch.LaunchConstants;
import com.fiorano.openesb.microservice.launch.impl.EventStateConstants;
import com.fiorano.openesb.microservice.launch.impl.SeparateProcessRuntimeHandle;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.route.Route;
import com.fiorano.openesb.application.aps.ApplicationStateDetails;
import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.jmsroute.impl.JMSRouteConfiguration;
import com.fiorano.openesb.microservice.launch.MicroServiceRuntimeHandle;
import com.fiorano.openesb.microservice.launch.impl.MicroServiceLauncher;
import com.fiorano.openesb.route.*;
import com.fiorano.openesb.route.impl.*;
import com.fiorano.openesb.schemarepo.SchemaRepoConstants;
import com.fiorano.openesb.transport.TransportService;
import com.fiorano.openesb.transport.impl.jms.JMSPortConfiguration;
import com.fiorano.openesb.transport.impl.jms.TransportConfig;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.LookUpUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationHandle {
    private Logger logger = LoggerFactory.getLogger(Activator.class);
    private Application application;
    private MicroServiceLauncher service;
    private RouteService<RouteConfiguration> routeService;
    private TransportService transport;
    Map<String, MicroServiceRuntimeHandle> microServiceHandleList = new ConcurrentHashMap<>();
    private Map<String, Route> routeMap = new ConcurrentHashMap<>();
    private Map<String, Route> breakPointRoutes = new ConcurrentHashMap<>();
    private Map<String, BreakpointMetaData> breakpoints = new ConcurrentHashMap<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Map<String, BreakpointMetaData> pendingBreakpointsForClosure = new ConcurrentHashMap<>();
    private ApplicationController applicationController;


    Vector<String> pendingQueueIDs;
    //hash table of handle IDs of routes on which debuggers are set.[key -routeID Vs object -handleID]
    private Hashtable debugHandleIDs = new Hashtable();

    //  Unique AppGUID.
    private String appGUID;

    //  version.
    private float version;

    private String environmentLabel;

    //  LaunchTime.
    private long launchTime;

    // List of RouteGUID's in the application
    //private Vector<String> debugrouteGUIDS;

    private String passwd;

    private String userName;

    public ApplicationHandle(ApplicationController applicationController, Application application, MicroServiceLauncher service, RouteService<RouteConfiguration> routeService, TransportService transport, String userName, String passwd) {
        this.applicationController = applicationController;
        this.application = application;
        this.service = service;
        this.routeService = routeService;
        this.transport = transport;
        this.appGUID = application.getGUID();
        this.version = application.getVersion();
        this.launchTime = System.currentTimeMillis();
        this.environmentLabel = application.getLabel();
        this.userName = userName;
        this.passwd = passwd;
    }

    public Application getApplication() {
        return application;
    }

    public MicroServiceLauncher getService() {
        return service;
    }

    public void setService(MicroServiceLauncher service) {
        this.service = service;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getAppGUID() {
        return appGUID;
    }

    public void setAppGUID(String appGUID) {
        this.appGUID = appGUID;
    }

    public void createRoutes() throws Exception {
        for(final com.fiorano.openesb.application.application.Route route: application.getRoutes()) {

            String sourcePortInstance = route.getSourcePortInstance();
            JMSPortConfiguration sourceConfiguration = new JMSPortConfiguration();
            String sourceServiceInstanceName = route.getSourceServiceInstance();
            OutputPortInstance sourcePort = null;
            String sourcePortName = null;
            ServiceInstance sourceServiceInstance = application.getServiceInstance(sourceServiceInstanceName);
            if (sourceServiceInstance == null) {
                RemoteServiceInstance remoteSourceServiceInstance = application.getRemoteServiceInstance(sourceServiceInstanceName);
                if (remoteSourceServiceInstance != null) {
                    String remoteAppGuid = remoteSourceServiceInstance.getApplicationGUID();
                    float remoteAppVersion = remoteSourceServiceInstance.getApplicationVersion();
                    ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                    if (remoteAppHandle == null) {
                        throw new FioranoException("Remote Application not running");
                    }
                    Application remoteApplication = remoteAppHandle.getApplication();
                    sourceServiceInstance = remoteApplication.getServiceInstance(remoteSourceServiceInstance.getRemoteName());
                    sourcePort = sourceServiceInstance.getOutputPortInstance(sourcePortInstance);
                    if (sourcePort.isSpecifiedDestinationUsed()) {
                        sourcePortName = sourcePort.getDestination();
                    } else {
                        sourcePortName = getPortName(remoteAppGuid, remoteAppVersion, sourcePortInstance, remoteSourceServiceInstance.getRemoteName());
                    }
                }
            } else {
                sourcePort = sourceServiceInstance.getOutputPortInstance(sourcePortInstance);
                if (sourcePort.isSpecifiedDestinationUsed()) {
                    sourcePortName = sourcePort.getDestination();
                } else {
                    sourcePortName = getPortName(appGUID, version, sourcePortInstance, sourceServiceInstanceName);
                }
            }
            sourceConfiguration.setName(sourcePortName);
            @SuppressWarnings("ConstantConditions") int type = sourcePort.getDestinationType();
            sourceConfiguration.setPortType(type == PortInstance.DESTINATION_TYPE_QUEUE ?
                    JMSPortConfiguration.PortType.QUEUE : JMSPortConfiguration.PortType.TOPIC);

            String destPortInstance = route.getTargetPortInstance();
            JMSPortConfiguration destinationConfiguration = new JMSPortConfiguration();
            String targetServiceInstanceName = route.getTargetServiceInstance();
            ServiceInstance targetServiceInstance = application.getServiceInstance(targetServiceInstanceName);
            InputPortInstance targetPort = null;
            String targetPortName = null;
            if (targetServiceInstance == null) {
                RemoteServiceInstance remoteTgtServiceInstance = application.getRemoteServiceInstance(targetServiceInstanceName);
                if (remoteTgtServiceInstance != null) {
                    String remoteAppGuid = remoteTgtServiceInstance.getApplicationGUID();
                    float remoteAppVersion = remoteTgtServiceInstance.getApplicationVersion();
                    ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                    Application remoteApplication = remoteAppHandle.getApplication();
                    targetPort = remoteApplication.getServiceInstance(remoteTgtServiceInstance.getRemoteName()).getInputPortInstance(destPortInstance);
                    if (targetPort.isSpecifiedDestinationUsed()) {
                        targetPortName = targetPort.getDestination();
                    } else {
                        targetPortName = getPortName(remoteAppGuid, remoteAppVersion, destPortInstance, remoteTgtServiceInstance.getRemoteName());
                    }
                }

            } else {
                targetPort = application.getServiceInstance(targetServiceInstanceName).getInputPortInstance(destPortInstance);
                if (targetPort.isSpecifiedDestinationUsed()) {
                    targetPortName = targetPort.getDestination();
                } else {
                    targetPortName = getPortName(appGUID, version, destPortInstance, targetServiceInstanceName);
                }
            }
            destinationConfiguration.setName(targetPortName);
            if(targetPort == null) {
                throw new FioranoException("Unexpected error encountered.");
            }
            int inputPortInstanceDestinationType = targetPort.getDestinationType();
            destinationConfiguration.setPortType(inputPortInstanceDestinationType == PortInstance.DESTINATION_TYPE_QUEUE ?
                    JMSPortConfiguration.PortType.QUEUE : JMSPortConfiguration.PortType.TOPIC);

            if(routeMap.containsKey(route.getName())){
                // check if the source or destination component is renamed. if renamed, change the source port or target port name accordingly in the route configuration
                Route routeInstance = routeMap.get(route.getName());
                if(!routeInstance.getSourceDestinationName().equals(sourcePortName)){
                    routeInstance.changeSourceDestination(sourceConfiguration);
                }
                if(!routeInstance.getTargetDestinationName().equals(targetPortName)){
                    routeInstance.changeTargetDestination(destinationConfiguration);
                }
                continue;
            }

            JMSRouteConfiguration routeConfiguration = new JMSRouteConfiguration(sourceConfiguration, destinationConfiguration, route.getJMSSelector());

            MessageCreationConfiguration messageCreationConfiguration = new MessageCreationConfiguration();
            messageCreationConfiguration.setTransportService(transport);
            messageCreationConfiguration.setRouteOperationType(RouteOperationType.MESSAGE_CREATE);
            routeConfiguration.getRouteOperationConfigurations().add(messageCreationConfiguration);

            CarryForwardContextConfiguration srcCFC = new CarryForwardContextConfiguration();
            srcCFC.setApplication(application);
            srcCFC.setPortInstance(sourcePort);
            srcCFC.setServiceInstanceName(sourceServiceInstanceName);
            srcCFC.setRouteOperationType(RouteOperationType.SRC_CARRY_FORWARD_CONTEXT);
            routeConfiguration.getRouteOperationConfigurations().add(srcCFC);

            Transformation applicationContextTransformation = sourcePort.getApplicationContextTransformation();
            if (applicationContextTransformation != null) {
                TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                transformationConfiguration.setXsl(applicationContextTransformation.getScript());
                transformationConfiguration.setTransformerType(applicationContextTransformation.getFactory());
                transformationConfiguration.setJmsXsl(applicationContextTransformation.getJMSScript());
                transformationConfiguration.setRouteOperationType(RouteOperationType.APP_CONTEXT_TRANSFORM);
                routeConfiguration.getRouteOperationConfigurations().add(transformationConfiguration);
            }

            if (route.getSenderSelector() != null) {
                SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
                senderSelectorConfiguration.setSourceName(route.getSenderSelector());
                senderSelectorConfiguration.setAppID(application.getGUID() + ":" + application.getVersion());
                senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
                routeConfiguration.getRouteOperationConfigurations().add(senderSelectorConfiguration);
            }

            if (route.getApplicationContextSelector() != null) {
                XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
                appContextSelectorConfig.setXpath(route.getApplicationContextSelector().getXPath());
                appContextSelectorConfig.setNsPrefixMap(route.getApplicationContextSelector().getNamespaces());
                appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
                routeConfiguration.getRouteOperationConfigurations().add(appContextSelectorConfig);
            }

            if (route.getBodySelector() != null) {
                XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
                bodySelectorConfig.setXpath(route.getBodySelector().getXPath());
                bodySelectorConfig.setNsPrefixMap(route.getBodySelector().getNamespaces());
                bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
                routeConfiguration.getRouteOperationConfigurations().add(bodySelectorConfig);
            }

            if (route.getMessageTransformation() != null) {
                TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                transformationConfiguration.setXsl(route.getMessageTransformation().getScript());
                transformationConfiguration.setTransformerType(route.getMessageTransformation().getFactory());
                transformationConfiguration.setJmsXsl(route.getMessageTransformation().getJMSScript());
                transformationConfiguration.setRouteOperationType(RouteOperationType.ROUTE_TRANSFORM);
                routeConfiguration.getRouteOperationConfigurations().add(transformationConfiguration);
            }

            CarryForwardContextConfiguration targetCFC = new CarryForwardContextConfiguration();
            targetCFC.setApplication(application);
            targetCFC.setPortInstance(targetPort);
            targetCFC.setServiceInstanceName(targetServiceInstanceName);
            targetCFC.setRouteOperationType(RouteOperationType.TGT_CARRY_FORWARD_CONTEXT);
            routeConfiguration.getRouteOperationConfigurations().add(targetCFC);

            com.fiorano.openesb.route.Route route1 = routeService.createRoute(route.getName(), routeConfiguration);
            route1.start();
            routeMap.put(route.getName(), route1);
        }
    }

    private String getSourcePortName(com.fiorano.openesb.application.application.Route route) throws FioranoException {
        String sourcePortInstance = route.getSourcePortInstance();
        String sourceServiceInstanceName = route.getSourceServiceInstance();
        OutputPortInstance sourcePort;
        String sourcePortName = null;
        ServiceInstance sourceServiceInstance = application.getServiceInstance(sourceServiceInstanceName);
        if (sourceServiceInstance == null) {
            RemoteServiceInstance remoteSourceServiceInstance = application.getRemoteServiceInstance(sourceServiceInstanceName);
            if (remoteSourceServiceInstance != null) {
                String remoteAppGuid = remoteSourceServiceInstance.getApplicationGUID();
                float remoteAppVersion = remoteSourceServiceInstance.getApplicationVersion();
                ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                if (remoteAppHandle == null) {
                    throw new FioranoException("Remote Application not running");
                }
                Application remoteApplication = remoteAppHandle.getApplication();
                sourceServiceInstance = remoteApplication.getServiceInstance(remoteSourceServiceInstance.getRemoteName());
                sourcePort = sourceServiceInstance.getOutputPortInstance(sourcePortInstance);
                if (sourcePort.isSpecifiedDestinationUsed()) {
                    sourcePortName = sourcePort.getDestination();
                } else {
                    sourcePortName = getPortName(remoteAppGuid, remoteAppVersion, sourcePortInstance, remoteSourceServiceInstance.getRemoteName());
                }
            }
        } else {
            sourcePort = sourceServiceInstance.getOutputPortInstance(sourcePortInstance);
            if (sourcePort.isSpecifiedDestinationUsed()) {
                sourcePortName = sourcePort.getDestination();
            } else {
                sourcePortName = getPortName(appGUID, version, sourcePortInstance, sourceServiceInstanceName);
            }
        }
        return sourcePortName;
    }

    private String getTargetPortName(com.fiorano.openesb.application.application.Route route) throws FioranoException {
        String destPortInstance = route.getTargetPortInstance();
        String targetServiceInstanceName = route.getTargetServiceInstance();
        ServiceInstance targetServiceInstance = application.getServiceInstance(targetServiceInstanceName);
        InputPortInstance targetPort;
        String targetPortName = null;
        if (targetServiceInstance == null) {
            RemoteServiceInstance remoteTgtServiceInstance = application.getRemoteServiceInstance(targetServiceInstanceName);
            if (remoteTgtServiceInstance != null) {
                String remoteAppGuid = remoteTgtServiceInstance.getApplicationGUID();
                float remoteAppVersion = remoteTgtServiceInstance.getApplicationVersion();
                ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                if (remoteAppHandle == null) {
                    throw new FioranoException("Remote Application not running");
                }
                Application remoteApplication = remoteAppHandle.getApplication();
                targetPort = remoteApplication.getServiceInstance(remoteTgtServiceInstance.getRemoteName()).getInputPortInstance(destPortInstance);
                if (targetPort.isSpecifiedDestinationUsed()) {
                    targetPortName = targetPort.getDestination();
                } else {
                    targetPortName = getPortName(remoteAppGuid, remoteAppVersion, destPortInstance, remoteTgtServiceInstance.getRemoteName());
                }
            }

        } else {
            targetPort = application.getServiceInstance(targetServiceInstanceName).getInputPortInstance(destPortInstance);
            if (targetPort.isSpecifiedDestinationUsed()) {
                targetPortName = targetPort.getDestination();
            } else {
                targetPortName = getPortName(appGUID, version, destPortInstance, targetServiceInstanceName);
            }
        }
        return targetPortName;
    }

    private String getPortName(String appGUID, float version, String portInstance, String sourceServiceInstance) {
        return LookUpUtil.getServiceInstanceLookupName(appGUID, version, sourceServiceInstance) + Constants.NAME_DELIMITER + portInstance;
    }

    public void startAllMicroServices() {
        logger.info("Starting all micro services of the Application " + appGUID + ":" + version);
        for (ServiceInstance instance : application.getServiceInstances()) {
            try {
                startMicroService(instance.getName());
            } catch (FioranoException e) {
                logger.error("Error occured while starting the Service: " + instance.getName() + " of Application: " + appGUID + ":" + version, e);
            }
        }
        logger.info("Started all micro services of the Application " + appGUID + ":" + version);
    }

    public void stopApplication() {
        stopAllMicroServices();
        stopAllRoutes();
        disableAllPorts();
    }

    public void stopAllRoutes() {
        for (String routeName : routeMap.keySet()) {
            try {
                stopRoute(routeMap.remove(routeName));
            } catch (Exception e) {
                logger.error("Error occurred while stopping the route: " + routeName + " of Application: " + appGUID + ":" + version, e);
            }
        }
        for (String bpRouteName : breakPointRoutes.keySet()) {
            try {
                stopRoute(breakPointRoutes.remove(bpRouteName));
            } catch (Exception e) {
                logger.error("Error occurred while stopping the breakpoint route: " + bpRouteName + " of Application: " + appGUID + ":" + version, e);
            }
        }
    }

    public void stopRoute(Route route) throws Exception {
        if(route!=null){
            route.stop();
        }
    }

    public void stopRoute(String routeName) throws Exception {
        Route route = routeMap.get(routeName);
        if(route!=null){
            route.stop();
        }
        route = breakPointRoutes.get(routeName);
        if(route!=null){
            route.stop();
        }
    }

    public void disableAllPorts() {
        for (ServiceInstance serviceInstance : application.getServiceInstances()) {
            disableServicePorts(serviceInstance);
        }
    }

    private void disableServicePorts(ServiceInstance serviceInstance) {
        for (PortInstance portInstance : serviceInstance.getInputPortInstances()) {
            JMSPortConfiguration portConfiguration = getPortConfiguration(serviceInstance, portInstance);
            disablePort(portConfiguration);
        }
        for (PortInstance portInstance : serviceInstance.getOutputPortInstances()) {
            JMSPortConfiguration portConfiguration = getPortConfiguration(serviceInstance, portInstance);
            disablePort(portConfiguration);
        }
    }

    private JMSPortConfiguration getPortConfiguration(ServiceInstance serviceInstance, PortInstance portInstance) {
        JMSPortConfiguration portConfiguration = new JMSPortConfiguration();
        int type = portInstance.getDestinationType();
        portConfiguration.setPortType(type == PortInstance.DESTINATION_TYPE_QUEUE ?
                JMSPortConfiguration.PortType.QUEUE : JMSPortConfiguration.PortType.TOPIC);
        String portName;
        if (portInstance.isSpecifiedDestinationUsed()) {
            portName = portInstance.getDestination();
        } else {
            portName = getPortName(appGUID, version, portInstance.getName(), serviceInstance.getName());
        }
        portConfiguration.setName(portName);
        return portConfiguration;
    }

    public BreakpointMetaData addBreakPoint(String routeName) throws Exception {
        com.fiorano.openesb.route.Route route = routeMap.get(routeName);
        if (route == null) {
            throw new FioranoException("Route with name: " + routeName + " does not exist in the Application: " + application.getGUID());
        }
        String bpSourceDestName = getBPSourceDestName(routeName);
        String bpTargetdDestName = getBPTargetDestinationName(routeName);
        com.fiorano.openesb.application.application.Route routePS = null;
        for (final com.fiorano.openesb.application.application.Route rPS : application.getRoutes()) {
            if (rPS.getName().equals(routeName)) {
                routePS = rPS;
                break;
            }
        }

        //create route from Outport to C and start
        JMSPortConfiguration outPortConfiguration = new JMSPortConfiguration();
        @SuppressWarnings("ConstantConditions") String outPortName = routePS.getSourcePortInstance();
        String sourceServiceInstanceName = routePS.getSourceServiceInstance();
        OutputPortInstance outputPortInstance = null;
        String outPortFullName = null;
        ServiceInstance sourceServiceInstance = application.getServiceInstance(sourceServiceInstanceName);
        if (sourceServiceInstance == null) {
            RemoteServiceInstance remoteSourceServiceInstance = application.getRemoteServiceInstance(sourceServiceInstanceName);
            if (remoteSourceServiceInstance != null) {
                String remoteAppGuid = remoteSourceServiceInstance.getApplicationGUID();
                float remoteAppVersion = remoteSourceServiceInstance.getApplicationVersion();
                ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                if (remoteAppHandle == null) {
                    throw new FioranoException("Remote Application not running");
                }
                Application remoteApplication = remoteAppHandle.getApplication();
                sourceServiceInstance = remoteApplication.getServiceInstance(remoteSourceServiceInstance.getRemoteName());
                outputPortInstance = sourceServiceInstance.getOutputPortInstance(outPortName);
                if (outputPortInstance.isSpecifiedDestinationUsed()) {
                    outPortFullName = outputPortInstance.getDestination();
                } else {
                    outPortFullName = getPortName(remoteAppGuid, remoteAppVersion, outPortName, remoteSourceServiceInstance.getRemoteName());
                }
            }
        } else {
            outputPortInstance = sourceServiceInstance.getOutputPortInstance(outPortName);
            if (outputPortInstance.isSpecifiedDestinationUsed()) {
                outPortFullName = outputPortInstance.getDestination();
            } else {
                outPortFullName = getPortName(appGUID, version, outPortName, sourceServiceInstanceName);
            }
        }
        if(outputPortInstance == null) {
            throw new FioranoException("Unexpected error encountered.");
        }
        int portType = outputPortInstance.getDestinationType();
        outPortConfiguration.setPortType(portType == PortInstance.DESTINATION_TYPE_QUEUE ?
                JMSPortConfiguration.PortType.QUEUE : JMSPortConfiguration.PortType.TOPIC);
        outPortConfiguration.setName(outPortFullName);
        JMSPortConfiguration tgtCConfiguration = new JMSPortConfiguration();
        tgtCConfiguration.setName(bpSourceDestName);
        tgtCConfiguration.setPortType(JMSPortConfiguration.PortType.QUEUE);

        JMSRouteConfiguration routeToCConfiguration = new JMSRouteConfiguration(outPortConfiguration, tgtCConfiguration, routePS.getJMSSelector());

        MessageCreationConfiguration messageCreationConfiguration = new MessageCreationConfiguration();
        messageCreationConfiguration.setTransportService(transport);
        messageCreationConfiguration.setRouteOperationType(RouteOperationType.MESSAGE_CREATE);
        routeToCConfiguration.getRouteOperationConfigurations().add(messageCreationConfiguration);

        CarryForwardContextConfiguration srcCFC = new CarryForwardContextConfiguration();
        srcCFC.setApplication(application);
        srcCFC.setPortInstance(outputPortInstance);
        srcCFC.setServiceInstanceName(outPortName);
        srcCFC.setRouteOperationType(RouteOperationType.SRC_CARRY_FORWARD_CONTEXT);
        routeToCConfiguration.getRouteOperationConfigurations().add(srcCFC);

        Transformation applicationContextTransformation = outputPortInstance.getApplicationContextTransformation();
        if (applicationContextTransformation != null) {
            TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
            transformationConfiguration.setXsl(applicationContextTransformation.getScript());
            transformationConfiguration.setTransformerType(applicationContextTransformation.getFactory());
            transformationConfiguration.setJmsXsl(applicationContextTransformation.getJMSScript());
            transformationConfiguration.setRouteOperationType(RouteOperationType.APP_CONTEXT_TRANSFORM);
            routeToCConfiguration.getRouteOperationConfigurations().add(transformationConfiguration);
        }

        if (routePS.getSenderSelector() != null) {
            SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
            senderSelectorConfiguration.setSourceName(routePS.getSenderSelector());
            senderSelectorConfiguration.setAppID(application.getGUID() + ":" + application.getVersion());
            senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
            routeToCConfiguration.getRouteOperationConfigurations().add(senderSelectorConfiguration);
        }

        if (routePS.getApplicationContextSelector() != null) {
            XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
            appContextSelectorConfig.setXpath(routePS.getApplicationContextSelector().getXPath());
            appContextSelectorConfig.setNsPrefixMap(routePS.getApplicationContextSelector().getNamespaces());
            appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
            routeToCConfiguration.getRouteOperationConfigurations().add(appContextSelectorConfig);
        }

        if (routePS.getBodySelector() != null) {
            XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
            bodySelectorConfig.setXpath(routePS.getBodySelector().getXPath());
            bodySelectorConfig.setNsPrefixMap(routePS.getBodySelector().getNamespaces());
            bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
            routeToCConfiguration.getRouteOperationConfigurations().add(bodySelectorConfig);
        }

        if (routePS.getMessageTransformation() != null) {
            TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
            transformationConfiguration.setXsl(routePS.getMessageTransformation().getScript());
            transformationConfiguration.setTransformerType(routePS.getMessageTransformation().getFactory());
            transformationConfiguration.setJmsXsl(routePS.getMessageTransformation().getJMSScript());
            transformationConfiguration.setRouteOperationType(RouteOperationType.ROUTE_TRANSFORM);
            routeToCConfiguration.getRouteOperationConfigurations().add(transformationConfiguration);
        }

        com.fiorano.openesb.route.Route routeToC = routeService.createRoute(bpSourceDestName, routeToCConfiguration);
        routeToC.start();
        breakPointRoutes.put(bpSourceDestName, routeToC);
        //create route from D to inport and start
        JMSPortConfiguration inPortConfiguration = new JMSPortConfiguration();
        String inPortName = routePS.getTargetPortInstance();
        String targetServiceInstanceName = routePS.getTargetServiceInstance();
        ServiceInstance targetServiceInstance = application.getServiceInstance(targetServiceInstanceName);
        InputPortInstance inPortInstnace = null;
        String inPortFullName = null;
        if (targetServiceInstance == null) {
            RemoteServiceInstance remoteTgtServiceInstance = application.getRemoteServiceInstance(targetServiceInstanceName);
            if (remoteTgtServiceInstance != null) {
                String remoteAppGuid = remoteTgtServiceInstance.getApplicationGUID();
                float remoteAppVersion = remoteTgtServiceInstance.getApplicationVersion();
                ApplicationHandle remoteAppHandle = applicationController.getApplicationHandle(remoteAppGuid, remoteAppVersion);
                Application remoteApplication = remoteAppHandle.getApplication();
                inPortInstnace = remoteApplication.getServiceInstance(remoteTgtServiceInstance.getRemoteName()).getInputPortInstance(inPortName);
                if (inPortInstnace.isSpecifiedDestinationUsed()) {
                    inPortFullName = inPortInstnace.getDestination();
                } else {
                    inPortFullName = getPortName(remoteAppGuid, remoteAppVersion, inPortName, remoteTgtServiceInstance.getRemoteName());
                }
            }

        } else {
            inPortInstnace = application.getServiceInstance(targetServiceInstanceName).getInputPortInstance(inPortName);
            if (inPortInstnace.isSpecifiedDestinationUsed()) {
                inPortFullName = inPortInstnace.getDestination();
            } else {
                inPortFullName = getPortName(appGUID, version, inPortName, targetServiceInstanceName);
            }
        }
        if(inPortInstnace == null) {
            throw new FioranoException("Unexpected error encountered");
        }
        int inPortType = inPortInstnace.getDestinationType();
        inPortConfiguration.setPortType(inPortType == PortInstance.DESTINATION_TYPE_QUEUE ?
                JMSPortConfiguration.PortType.QUEUE : JMSPortConfiguration.PortType.TOPIC);

        inPortConfiguration.setName(inPortFullName);
        JMSPortConfiguration srcDConfiguration = new JMSPortConfiguration();
        srcDConfiguration.setName(bpTargetdDestName);
        srcDConfiguration.setPortType(JMSPortConfiguration.PortType.QUEUE);

        JMSRouteConfiguration routeFromDConfiguration = new JMSRouteConfiguration(srcDConfiguration, inPortConfiguration, null);

        MessageCreationConfiguration d2OutPortmessageCreationConfig = new MessageCreationConfiguration();
        d2OutPortmessageCreationConfig.setTransportService(transport);
        d2OutPortmessageCreationConfig.setRouteOperationType(RouteOperationType.MESSAGE_CREATE);
        routeFromDConfiguration.getRouteOperationConfigurations().add(d2OutPortmessageCreationConfig);

        CarryForwardContextConfiguration targetCFC = new CarryForwardContextConfiguration();
        targetCFC.setApplication(application);
        targetCFC.setPortInstance(inPortInstnace);
        targetCFC.setServiceInstanceName(routePS.getTargetServiceInstance());
        targetCFC.setRouteOperationType(RouteOperationType.TGT_CARRY_FORWARD_CONTEXT);
        routeFromDConfiguration.getRouteOperationConfigurations().add(targetCFC);
        com.fiorano.openesb.route.Route routeFromD = routeService.createRoute(bpTargetdDestName, routeFromDConfiguration);
        routeFromD.start();
        breakPointRoutes.put(bpTargetdDestName, routeFromD);
        //stop original route
        route.stop();

        BreakpointMetaData breakpointMetaData = new BreakpointMetaData();
        breakpointMetaData.setConnectionProperties(TransportConfig.getInstance().getConnectionProperties());
        breakpointMetaData.setSourceQName(bpSourceDestName);
        breakpointMetaData.setTargetQName(bpTargetdDestName);
        breakpoints.put(routeName, breakpointMetaData);
        applicationController.persistApplicationState(this);
        ApplicationEventRaiser.generateRouteEvent(ApplicationEvent.ApplicationEventType.ROUTE_BP_ADDED, Event.EventCategory.INFORMATION, appGUID, application.getDisplayName(), String.valueOf(version), routeName, "Successfully added breakpoint to the Route");
        return breakpointMetaData;
    }

    public void removeBreakPoint(String routeName) throws Exception {
        com.fiorano.openesb.route.Route route = routeMap.get(routeName);
        route.start();
        //remove breakpoint routes C and D
        String bpSourceDestName = getBPSourceDestName(routeName);
        String bpTargetdDestName = getBPTargetDestinationName(routeName);
        Route routeToC = breakPointRoutes.remove(bpSourceDestName);
        if(routeToC!=null){
            routeToC.stop();
        }
        JMSPortConfiguration portConfiguration = new JMSPortConfiguration();
        portConfiguration.setPortType(JMSPortConfiguration.PortType.QUEUE);
        portConfiguration.setName(bpSourceDestName);
        transport.disablePort(portConfiguration);
        Route routeFromD = breakPointRoutes.remove(bpTargetdDestName);
        if(routeFromD!=null){
            routeFromD.stop();
        }
        portConfiguration.setName(bpTargetdDestName);
        transport.disablePort(portConfiguration);
        breakpoints.remove(routeName);
        applicationController.persistApplicationState(this);
        ApplicationEventRaiser.generateRouteEvent(ApplicationEvent.ApplicationEventType.ROUTE_BP_REMOVED, Event.EventCategory.INFORMATION, appGUID, application.getDisplayName(), String.valueOf(version), routeName, "Successfully removed breakpoint to the Route");
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void stopAllMicroServices() {
        logger.info("Stopping all micro services of the Application " + appGUID + ":" + version);
        for (MicroServiceRuntimeHandle handle : microServiceHandleList.values()) {
            stopMicroService(handle.getServiceInstName());
        }
        microServiceHandleList = new ConcurrentHashMap<>();
        logger.info("Stopped all micro services of the Application " + appGUID + ":" + version);
    }

    public void startMicroService(String microServiceName) throws FioranoException {
        ServiceInstance instance = application.getServiceInstance(microServiceName);
        if (isMicroserviceRunning(microServiceName)) {
            logger.info("MicroService: " + microServiceName + " of Application " + appGUID + ":" + version + " is already running");
            return;
        }
        logger.info("Starting MicroService: " + microServiceName + " of Application " + appGUID + ":" + version);
        TransportConfig transportConfig = TransportConfig.getInstance();
        JavaLaunchConfiguration javaLaunchConfiguration = new JavaLaunchConfiguration(instance.isDebugMode(),
                instance.getDebugPort(), transportConfig.getProviderURL(), MicroServiceRepoManager.getInstance().getRepositoryLocation(), ServerConfig.getConfig().getRepositoryPath() + File.separator + SchemaRepoConstants.SCHEMA_REPOSITORY_NAME,
                ServerConfig.getConfig().getJettyUrl(), ServerConfig.getConfig().getJettySSLUrl(),
                Boolean.valueOf(transportConfig.getValue("WatchForControlEvents")), transportConfig.getValue("MS_JAVA_HOME"),
                transportConfig.getValue(LaunchConstants.USER_DEFINED_JAVA_HOME), transportConfig.getValue("java.naming.factory.initial"));
        MicroServiceLaunchConfiguration mslc = new MicroServiceLaunchConfiguration(application.getGUID(), String.valueOf(application.getVersion()), transportConfig.getUserName(), transportConfig.getPassword(), instance, javaLaunchConfiguration);
        try {
            microServiceHandleList.put(microServiceName, service.launch(mslc, instance.getConfiguration()));
        } catch (Throwable e) {
            logger.error("Error occurred while starting the Service: " + microServiceName + " of Application: " + appGUID + ":" + version, e);
        }
        logger.info("Started MicroService: " + microServiceName + " of Application " + appGUID + ":" + version);
    }

    public void stopMicroService(String microServiceName) {
        if (!isMicroserviceRunning(microServiceName)) {
            logger.warn("Microservice: " + microServiceName + " of Application " + appGUID + ":" + version + " is not running");
            return;
        }
        try {
            logger.info("Stoping MicroService: " + microServiceName + " of Application " + appGUID + ":" + version);
            microServiceHandleList.get(microServiceName).stop();
            if (application.getServiceInstance(microServiceName).getLaunchType() < 3) {
                microServiceHandleList.remove(microServiceName);
            }
            logger.info("Stopped MicroService: " + microServiceName + " of Application " + appGUID + ":" + version);
        } catch (Throwable e) {
            logger.error("Error occured while stopping the Service: " + microServiceName + " of Application: " + appGUID + ":" + version, e);
        }
    }

    public boolean isMicroserviceRunning(String microServiceName) {
        MicroServiceRuntimeHandle handle = microServiceHandleList.get(microServiceName);
        return handle != null && handle.isRunning();
    }

    public MemoryUsage getMemoryUsage(String microServiceName) {
        MicroServiceRuntimeHandle handle = microServiceHandleList.get(microServiceName);
        if (handle == null) {
            return null;
        }
        if (handle instanceof SeparateProcessRuntimeHandle) {
            return ((SeparateProcessRuntimeHandle) handle).getMemoryUsage();
        } else {
            return null;
        }
    }

    public void setLogLevel(String microServiceName, HashMap<String,String> modules) throws Exception {
        MicroServiceRuntimeHandle handle = microServiceHandleList.get(microServiceName);
        handle.setLogLevel(modules);
    }
    public String getLaunchMode(String name) {
        return isMicroserviceRunning(name) ? microServiceHandleList.get(name).getLaunchMode().name() : "Not Running";
    }


    public ApplicationStateDetails getApplicationDetails() throws FioranoException {


        // logger.debug(Bundle.class, Bundle.EXECUTING_CALL, "getApplicationDetails()");

        ApplicationStateDetails appDetails = new ApplicationStateDetails();

        appDetails.setAppGUID(appGUID);
        appDetails.setAppVersion(String.valueOf(application.getVersion()));
        appDetails.setKillTime(-1);
        appDetails.setLaunchTime(launchTime);
        appDetails.setApplicationLabel(environmentLabel);

        List<ServiceInstance> serviceInstances = application.getServiceInstances();
        for (ServiceInstance serviceInstance : serviceInstances) {
            String serviceName = serviceInstance.getName();
            ServiceInstanceStateDetails stateDetails;
            MicroServiceRuntimeHandle serviceHandle = microServiceHandleList.get(serviceName);
            if (serviceHandle == null) {
                stateDetails = new ServiceInstanceStateDetails();
                stateDetails.setServiceGUID(serviceInstance.getGUID());
                stateDetails.setServiceInstanceName(serviceName);
                stateDetails.setRunningVersion(String.valueOf(serviceInstance.getVersion()));
                stateDetails.setStatusString(EventStateConstants.SERVICE_HANDLE_UNBOUND);
            } else {
                stateDetails = serviceHandle.getServiceStateDetails();
                String exceptionTrace = serviceHandle.getExceptionTrace();
                if (exceptionTrace != null)
                    appDetails.addServiceExceptionTrace(serviceName, exceptionTrace);
            }
            appDetails.addServiceStatus(serviceName, stateDetails);
        }

        //  Get the details of External Services too.

        for (Object o : application.getRemoteServiceInstances()) {
            RemoteServiceInstance extInstance = (RemoteServiceInstance) o;
            String extAppGUID = extInstance.getApplicationGUID();
            ApplicationHandle extAppHandle = applicationController.getApplicationHandle(extAppGUID, extInstance.getApplicationVersion());

            if (extAppHandle == null) {
                // logger.error(Bundle.class, Bundle.APPHANDLE_NOT_PRESENT, appGUID+ITifosiConstants.APP_VERSION_DELIM+Float.toString(application.getVersion()));
                continue;
            }

            // If the External service is actually configured to a application of version different of that
            // of the one which is running. then do not proceed.
            if (extAppHandle.getApplication().getVersion() != extInstance.getApplicationVersion())
                continue;

            String extInstName = extInstance.getRemoteName();
            MicroServiceRuntimeHandle extServiceHandle = extAppHandle.getMicroServiceHandle(extInstName);

            if (extServiceHandle == null)
                continue;

            String localInstName = extInstance.getName();
            ServiceInstanceStateDetails stateDetails = extServiceHandle.getServiceStateDetails();

            appDetails.addServiceStatus(localInstName, stateDetails);

            String exceptionTrace = extServiceHandle.getExceptionTrace();

            if (exceptionTrace != null)
                appDetails.addServiceExceptionTrace(localInstName, exceptionTrace);
        }

        // add the routes with breakpoint to app state details

        if (breakpoints != null && breakpoints.size() > 0) {
            for (String aDebugrouteGUIDS : breakpoints.keySet()) {
                appDetails.addDebugRoute(aDebugrouteGUIDS);
            }
        }

        if (pendingBreakpointsForClosure != null && pendingBreakpointsForClosure.size() > 0) {
            for (String pendingDebugRouteGUID : pendingBreakpointsForClosure.keySet()) {
                appDetails.addPendingDebugRoutesForClosure(pendingDebugRouteGUID);
            }
        }
        return appDetails;
    }

    private MicroServiceRuntimeHandle getMicroServiceHandle(String serviceName) {
        return microServiceHandleList.get(serviceName);
    }

    public void synchronizeApplication(Application newApplication) throws FioranoException {
        Application oldApplication = this.application;
        this.application = newApplication;

        //create any extra routes that are added
        try {
            createRoutes();
        } catch (Exception e) {
            throw new FioranoException(e);
        }

        // kill service which no longer remain as part of the ep
        killExcludedServices(newApplication, oldApplication);

        //launch or modify the rest of the services
        logger.debug("launching the applicaiton with new properties");
        startAllMicroServices();

        //  Update routes for all remaining services. This should remove extra routes and add new routes and UPDATE existing route configuration.
        logger.debug("syncing routes");
        try {
            synchronizeRoutes();
        } catch (Exception e) {
            throw new FioranoException(e);
        }


       /* // remove the debug routes for components that no longer exist
        if (oldApplication != null) {
            appHandle.removeNonExistingRoutes(oldApplication, handleID);
            // All the debug routes will be updated
            debugHandler.synchronizeDebugRoutes(appHandle);
            Map<String, BreakpointMetaData> allPendingBreakpoints = appHandle.getPendingBreakpointsForClouser();
            List<String> routes = new ArrayList<String>(allPendingBreakpoints.keySet());
            for (String routeGUID : routes) {
                String handleId = appHandle.getHandleID(routeGUID);
                removeBreakpoint(routeGUID, handleId);
            }
        }*/
    }

    /**
     * kill all the extra services that are running on this TPS but which are
     * not part of the new ApplicationLaunchPacket
     *
     * @param newAlp new application launch packet
     * @throws FioranoException If an exception occurs
     */
    private void killExcludedServices(Application newAlp, Application oldApp) throws FioranoException {
        // set this to all running components initially
        Set<String> toBeKilledComponents = new HashSet<>();
        for (String serviecName : microServiceHandleList.keySet()) {
            toBeKilledComponents.add(serviecName);
        }

        Set<String> tobeRunningComponents = new HashSet<>();
        for (ServiceInstance serv : newAlp.getServiceInstances()) {
            tobeRunningComponents.add(serv.getName());
        }

        toBeKilledComponents.removeAll(tobeRunningComponents);
        for (String killcomp : toBeKilledComponents) {
            try {
                stopMicroService(killcomp);
                ServiceInstance serviceInstance = oldApp.getServiceInstance(killcomp);
                //delete ports
                disableServicePorts(serviceInstance);
                //delete logs
                applicationController.getApplicationLogManager().clearServiceOutLogs(killcomp, appGUID, version);
                applicationController.getApplicationLogManager().clearServiceErrLogs(killcomp, appGUID, version);
            } catch (Exception e) {
                logger.error("error occured while stopping the component " + killcomp + "of Application " + appGUID + ":" + version);
            }
        }
    }

    private void disablePort(JMSPortConfiguration portConfiguration) {
        try {
            transport.disablePort(portConfiguration);
        } catch (Exception e) {
            logger.error("Error occurred while disabling the port: " + portConfiguration.getName() + " of Application: " + appGUID + ":" + version, e);
        }
    }

    public void synchronizeRoutes() throws Exception {
        Collection<String> toDelete = new ArrayList<>();
        for (String routeName : routeMap.keySet())
            if (!checkForRouteExistanceAndUpdateRoute(routeMap.get(routeName)))
                toDelete.add(routeName);
        for (String routeName : toDelete) {
            Route route = routeMap.remove(routeName);
            if(route!=null){
                route.stop();
                route.delete();
            }
        }
    }

    private boolean checkForRouteExistanceAndUpdateRoute(Route rInfo) throws Exception {
        boolean found = false;
        String srcPortName = rInfo.getSourceDestinationName();
        String tgtPortName = rInfo.getTargetDestinationName();

         List<com.fiorano.openesb.application.application.Route> routes = application.getRoutes();
        for (com.fiorano.openesb.application.application.Route route : routes) {
            if (route.getName().equals(rInfo.getRouteName())) {
                found = true;
                String sourcePortInstance = route.getSourcePortInstance();
                String sourceServiceInstanceName = route.getSourceServiceInstance();
                ServiceInstance sourceServiceInstance = application.getServiceInstance(sourceServiceInstanceName);
                if (sourceServiceInstance == null) {
                    RemoteServiceInstance remoteServiceInstance = application.getRemoteServiceInstance(sourceServiceInstanceName);
                    if (remoteServiceInstance != null) {
                        sourceServiceInstance = applicationController.getApplicationHandle(remoteServiceInstance.getApplicationGUID(), remoteServiceInstance.getApplicationVersion()).getApplication().getServiceInstance(remoteServiceInstance.getRemoteName());
                    }
                }
                @SuppressWarnings("ConstantConditions") OutputPortInstance sourcePort = sourceServiceInstance.getOutputPortInstance(sourcePortInstance);
                //check and update all route operation handlers
                Transformation applicationContextTransformation = sourcePort.getApplicationContextTransformation();
                if (applicationContextTransformation != null) {
                    TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                    transformationConfiguration.setXsl(applicationContextTransformation.getScript());
                    transformationConfiguration.setTransformerType(applicationContextTransformation.getFactory());
                    transformationConfiguration.setJmsXsl(applicationContextTransformation.getJMSScript());
                    transformationConfiguration.setRouteOperationType(RouteOperationType.APP_CONTEXT_TRANSFORM);
                    rInfo.modifyHandler(transformationConfiguration);
                }

                if (route.getSenderSelector() != null) {
                    SenderSelectorConfiguration senderSelectorConfiguration = new SenderSelectorConfiguration();
                    senderSelectorConfiguration.setSourceName(route.getSenderSelector());
                    senderSelectorConfiguration.setAppID(application.getGUID() + ":" + application.getVersion());
                    senderSelectorConfiguration.setRouteOperationType(RouteOperationType.SENDER_SELECTOR);
                    rInfo.modifyHandler(senderSelectorConfiguration);
                }

                if (route.getApplicationContextSelector() != null) {
                    XmlSelectorConfiguration appContextSelectorConfig = new XmlSelectorConfiguration("AppContext");
                    appContextSelectorConfig.setXpath(route.getApplicationContextSelector().getXPath());
                    appContextSelectorConfig.setNsPrefixMap(route.getApplicationContextSelector().getNamespaces());
                    appContextSelectorConfig.setRouteOperationType(RouteOperationType.APP_CONTEXT_XML_SELECTOR);
                    rInfo.modifyHandler(appContextSelectorConfig);
                }

                if (route.getBodySelector() != null) {
                    XmlSelectorConfiguration bodySelectorConfig = new XmlSelectorConfiguration("Body");
                    bodySelectorConfig.setXpath(route.getBodySelector().getXPath());
                    bodySelectorConfig.setNsPrefixMap(route.getBodySelector().getNamespaces());
                    bodySelectorConfig.setRouteOperationType(RouteOperationType.BODY_XML_SELECTOR);
                    rInfo.modifyHandler(bodySelectorConfig);
                }

                if (route.getMessageTransformation() != null) {
                    TransformationConfiguration transformationConfiguration = new TransformationConfiguration();
                    transformationConfiguration.setXsl(route.getMessageTransformation().getScript());
                    transformationConfiguration.setTransformerType(route.getMessageTransformation().getFactory());
                    transformationConfiguration.setJmsXsl(route.getMessageTransformation().getJMSScript());
                    transformationConfiguration.setRouteOperationType(RouteOperationType.ROUTE_TRANSFORM);
                    rInfo.modifyHandler(transformationConfiguration);
                }
                break;
            }
        }
        return found;
    }

    public BreakpointMetaData getBreakpointMetaData(String routeName) {
        return breakpoints.get(routeName);
    }

    public String[] getRoutesWithDebugger() {
        return breakpoints.keySet().toArray(new String[breakpoints.size()]);
    }

    public void removeAllBreakpoints() throws Exception {
        Set<String> routesWithBreakPoint = breakpoints.keySet();
        for (String routeName : routesWithBreakPoint) {
            removeBreakPoint(routeName);
        }
    }

    public void changeRouteOperationHandler(String routeGUID, RouteOperationConfiguration configuration) throws Exception {
        com.fiorano.openesb.route.Route route = routeMap.get(routeGUID);
        if (route == null) {
            throw new FioranoException("route: " + routeGUID + " does not exists in the Application " + appGUID + ":" + version);
        }
        route.modifyHandler(configuration);

        com.fiorano.openesb.route.Route bpRoute = breakPointRoutes.get(getBPSourceDestName(routeGUID));
        if (bpRoute != null) {
            bpRoute.modifyHandler(configuration);
        }
    }

    public void removeRouteOperationHandler(String routeGUID, RouteOperationConfiguration configuration) throws Exception {
        com.fiorano.openesb.route.Route route = routeMap.get(routeGUID);
        if (route == null) {
            throw new FioranoException("route: " + routeGUID + " does not exists in the Application " + appGUID + ":" + version);
        }
        route.removeHandler(configuration);

        com.fiorano.openesb.route.Route bpRoute = breakPointRoutes.get(getBPSourceDestName(routeGUID));
        if (bpRoute != null) {
            bpRoute.removeHandler(configuration);
        }
    }

    private String getBPSourceDestName(String routeName) {
        return application.getGUID() + "__" + application.getVersion() + routeName + "__C";
    }

    private String getBPTargetDestinationName(String routeName) {
        return application.getGUID() + "__" + application.getVersion() + routeName + "__D";
    }

    public ComponentStats getComponentStats(String serviceName) throws FioranoException {
        MicroServiceRuntimeHandle serviceRuntimeHandle = microServiceHandleList.get(serviceName);
        if (serviceRuntimeHandle != null) {
            return serviceRuntimeHandle.getComponentStats();
        } else {
            return null;
        }
    }
}
