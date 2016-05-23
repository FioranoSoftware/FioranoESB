/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.application.application.ServiceInstance;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface IApplicationManager extends Remote{

    /**
     * This is a constant used to denote any version.
     *
     * @see IApplicationManager#exists(String, float)
     * @see IApplicationManager#getApplication(String, float,long)
     */
    float ANY_VERSION = -1;

    /**
     * This method returns the IDs of all the available Event Processes
     *
     * @return Array - IDs of available Event Process, never <code>null</code>.
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    String[] getApplicationIds() throws RemoteException, ServiceException;

    /**
     * This method checks whether an event Process exists
     *
     * @param id       The ID of the Event Process
     * @param version  The version of the Event Process
     * @return boolean - true, if exists, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see IApplicationManager#ANY_VERSION
     */
    boolean exists(String id, float version) throws RemoteException, ServiceException;

    /**
     * This method returns the list of versions available for a particular Event Process
     *
     * @param id The ID of the Event Process
     * @return Float Array - Array of versions of particular Event Process, never <code>null</code>
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    float[] getVersions(String id) throws RemoteException, ServiceException;

    /**
     * This method deploys the Event Process. If a Event Process exists with the same ID & version, it will be
     * overwritten
     *
     * @param zippedContents The contents of the Event Process in zipped form
     * @param completed      Notifies server that sending zip contents completed
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    void saveApplication(byte[] zippedContents, boolean completed) throws RemoteException, ServiceException;

    /**
     * This method changes transformation on a route of a running event flow process.
     * Clients are supposed to send transformation project zip file in chunks of bytes and notify the server when done,
     * by setting 'completed' boolean to true.The process of byte deployment is same as the process to be used in deployApplication call.
     * @param appGUID Application GUID
     * @param appVersion Application Version
     * @param routeGUID  Route GUID
     * @param transformerType transformer type - Pass null for default xalan transformer
     * @param transformationProject transformationProject byte array
     * @param completed boolean indicating that client has nothing else to send.
     * @param scriptFile name of script File in the zip
     * @param jmsScriptFile name of jms Script File in the zip
     * @param projectDir name of the project directory in the zip
     * @throws RemoteException RemoteException
     * @throws ServiceException if any error occurs while extracting zip or if event flow process is not running.
     */
    void changeRouteTransformation(String appGUID, float appVersion, String routeGUID, String transformerType, byte[] transformationProject, boolean completed, String scriptFile, String jmsScriptFile, String projectDir) throws RemoteException, ServiceException;

    /**
     * This method changes transformation on a route of a running event flow process.
     * @param appGUID Application GUID
     * @param appVersion Application Version
     * @param routeGUID  Route GUID
     * @param configurationName The name of the new transformation configuration which should be applied to the route
     * @throws ServiceException Thrown when either the transformation configuration does not exist or some exception happens while applying the transformation
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    void changeRouteTransformationConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName) throws RemoteException, ServiceException;

    /**
     * This method clears transformation on route in a running event flow process.
     * @param appGUID Application GUID
     * @param appVersion Application Version
     * @param routeGUID routeGUID
     * @throws RemoteException
     * @throws ServiceException
     */
    void clearRouteTransformation(String appGUID, float appVersion, String routeGUID)throws RemoteException,ServiceException;

    /**
     * This method changes the APP_CONTEXT transformation for a Service Port in a running application.
     * Port is identified by its PortName, ServiceName and the Application to which it belongs.
     *
     * @param appGUID               GUID of the Application
     * @param appVersion            Application Version
     * @param serviceName           ServiceName of the service whose port's transformation is getting changed
     * @param portName              PortName of the port on which transformation is getting changed
     * @param transformerType       transformer type - Pass null for default xalan transformer
     * @param appContextBytes       transformationProject byte array
     * @param completed             boolean indicating that client has nothing else to send.
     * @param scriptFileName        name of script File in the zip
     * @param jmsScriptFileName     name of jms Script File in the zip
     * @param projectDirName        name of the project directory in the zip
     * @throws RemoteException      RemoteException
     * @throws ServiceException     if any error occurs while extracting zip or if event flow process is not running.
     */
    void changePortAppContext(String appGUID, float appVersion, String serviceName, String portName, String transformerType, byte[] appContextBytes,
                              boolean completed, String scriptFileName, String jmsScriptFileName, String projectDirName) throws RemoteException, ServiceException;

    /**
     * This method changes transformation on a route of a running event flow process.
     * @param appGUID               GUID of the Application
     * @param appVersion Application Version
     * @param serviceName           ServiceName of the service whose port's transformation is getting changed
     * @param portName              PortName of the port on which transformation is getting changed
     * @param configurationName     The name of the new transformation configuration which should be applied to the Port APP_CONTEXT
     * @throws ServiceException     Thrown when either the transformation configuration does not exist or some exception happens while applying the transformation
     * @throws RemoteException      A communication-related exception that may occur during the execution of a remote method call
     */
    void changePortAppContextConfiguration(String appGUID, float appVersion, String serviceName, String portName, String configurationName) throws RemoteException, ServiceException;

    /**
     * This method clears transformation on route in a running event flow process.
     * @param appGUID               GUID of the Application
     * @param appVersion Application Version
     * @param serviceName           ServiceName of the service whose port's transformation is getting changed
     * @param portName              PortName of the port on which transformation is getting changed        
     * @throws RemoteException
     * @throws ServiceException
     */
    void clearPortAppContext(String appGUID, float appVersion, String serviceName, String portName)throws RemoteException,ServiceException;

    /**
     * This method returns the Event Process in a zipped form. Clients are expected to extract it and use it.
     * If <code>ANY_VERSION</code> is passed then the latest version is given
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  The version
     * @param index    index of byte[] from where ro read the contents of zip file.starting index would be 0.
     * @return Byte Array - The contents of the Event Process in zipped form.Returns null once all contents have been read.
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see IApplicationManager#ANY_VERSION
     */
    byte[] getApplication(String appGUID, float version, long index) throws RemoteException, ServiceException;

    /**
     * This method deletes the Event Process. If <code>ANY_VERSION</code> is passed then the all the versions
     * are deleted
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  The version of the Event Process
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see IApplicationManager#ANY_VERSION
     */
    void deleteApplication(String appGUID, String version) throws RemoteException, ServiceException;

    /**
     * This method checks for existence of dependencies
     *
     * @param serviceRefs      The service dependencies
     * @param applicationRefs The Event Process depdencies
     * @return boolean - true, if all the dependencies exist, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    boolean dependenciesExists(ServiceReference[] serviceRefs, ApplicationMetadata[] applicationRefs) throws RemoteException, ServiceException;

    /**
     * This method starts the specified Event Process
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  The version of the Event Process
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    void startApplication(String appGUID, String version) throws RemoteException, ServiceException;

    /**
     * This method restarts the specified Event Process
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    void restartApplication(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method stops the specified Event Process
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void stopApplication(String appGUID, String version) throws RemoteException, ServiceException;

    /**
     * This method returns the list of Applications referring the specified Applications
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @return Map - The list of referring Applications
     * @throws RemoteException
     * @throws ServiceException
     */
    Map<String, Boolean> getApplicationChainForShutdown(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method returns the list of Applications referred by the specified Applications
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @return MAp - The list of referring Applications
     * @throws RemoteException
     * @throws ServiceException
     */
    Map<String, Boolean> getApplicationChainForLaunch(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method starts the specified Service Instance. Event Process should be in running state for this. If
     * the service instance is already running, this method call is ignored
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @param serviceInstanceName - The name of the Service Instance
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void startServiceInstance(String appGUID, float appVersion, String serviceInstanceName) throws RemoteException, ServiceException;

    /**
     * This method stops the specified Service Instance. Event Process should be in running state for this. If
     * the service instance is not running, this method call is ignored
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @param serviceInstanceName - The name of the Service Instance
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void stopServiceInstance(String appGUID, float appVersion, String serviceInstanceName) throws RemoteException, ServiceException;

    /**
     * This method stops all Service instances of the specified running Event process
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @throws RemoteException
     * @throws ServiceException
     */
    void stopAllServiceInstances(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method returns the list of running Event Processes
     *
     * @return ApplicationMetadata Array - An array of ApplicationMetadata object, never <code>null</code>
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    ApplicationMetadata[] getRunningApplications() throws RemoteException, ServiceException;

    /**
     * This method returns the list of Routes of Event Processes
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @return List - A List of Routes in an Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    List<RouteMetaData> getRoutesOfApplications(String appGUID, float version) throws RemoteException, ServiceException;

      /**
     * This method returns the list of Ports of Event Processes
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @return List - A List of Ports of an Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
      List<PortInstanceMetaData> getPortsForApplications(String appGUID, float version) throws RemoteException, ServiceException;

      /**
     * This method returns the list of Ports of Service Instance
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @param serviceInstName  Service Instance Name of the Service
     * @return List - A List of Ports of Service Instance
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
      List<PortInstanceMetaData> getPortsForService(String appGUID, float version, String serviceInstName) throws RemoteException, ServiceException;

      /**
     * This method returns the list of ServiceInstances in Application
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @return List - A List of Service Instances of App
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
      List<ServiceInstanceMetaData> getServiceInstancesOfApp(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method adds a listener to the changes of IApplicationManager's object
     *
     * @param listener The listener to add
     * @param appVersion Application Version
     * @param appGUID  application GUID
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void addApplicationListener(IApplicationManagerListener listener, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method removes the listener from the IApplicationManager
     *
     * @param listener The listener to remove
     * @param appGUID  application GUID
     * @param appVersion Application Version
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void removeApplicationListener(IApplicationManagerListener listener, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method adds a listener to the IApplicationManager's changes
     *
     * @param listener The listener to add
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void addRepositoryEventListener(IRepoEventListener listener) throws RemoteException, ServiceException;

    /**
     * This method removes the listener from the IApplicationManager
     *
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void removeRepositoryEventListener() throws RemoteException, ServiceException;

    /**
     * This method returns true if the application is Running
     *
     * @param appGUID
     * @param appVersion Application Version
     * @return boolean - Running status of the application
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isRunning(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method synchronizes the running event process with the specified appGUID and version
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void synchronizeApplication(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method starts all service instances of specified event process
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void startAllServices(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method prepares an application for launch. It essentially fetches all the services required on a FPS for
     * launching a service instance
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void checkResourcesAndConnectivity(String appGUID, float version) throws RemoteException, ServiceException;



    /**
     * This method returns the statistics of the service instance
     *
     * @param appGUID    appGUID
     * @param appVersion Application Version
     * @param servInstName Service instance Name of the WSStub
     * @return String - statistics of the given service instance
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String getComponentStats(String appGUID, float appVersion, String servInstName) throws ServiceException;

    /**
     * This method flushes the messages of the service instance
     *
     * @param appGUID    appGUID
     * @param appVersion Application Version
     * @param servInstName Service instance Name of the WSStub
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void flushMessages(String appGUID, float appVersion, String servInstName) throws ServiceException;

    /**
     * This method returns URL of deployed HTTPContext, when an Event process is exposed as a HttpContext using HttpStub component
     *
     * @param appGUID      appGUID
     * @param appVersion Application Version
     * @param servInstName Service instance Name of the HttpStub
     * @return String - HttpContext URL of the deployed Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String viewHttpContext(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException;

    /**
     * This method returns the currentState of the Application.It will contain all the details of the Application, including the details of its services.
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Application Version
     * @return ApplicationStateData - ApplicationStateData Object that contains all the details of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    ApplicationStateData getApplicationStateDetails(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method returns the list of all Event Processes stored with the enterprise server.
     *
     * @return ApplicationMetadata Array - An array of ApplicationMetadata, never <code>null</code>
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    ApplicationMetadata[] getAllApplications() throws RemoteException, ServiceException;

    /**
     * This method returns ApplicationMetadata of EP with supplied 'appGUID' and 'version' stored with the enterprise server.
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @return ApplicationMetadata - An object of ApplicationMetadata to the Application having ID 'appGUID' and Version 'version', <br><code>null</code> is returned in case Application with 'appGUID' and 'version' is not stored with the enterprise server.</br>
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    ApplicationMetadata getApplication(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method returns the specified amount of numberofLines (records) from the Out logs for the service
     * of an event flow process.
     *
     * @param numberOfLines Number of Lines
     * @param serviceName   Name of the Service
     * @param appGUID       GUID of the event flow process
     * @param appVersion    version no of the event flow process
     * @return String - the specified amount of numberofLines (records) from the Out logs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String getLastOutTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method returns the specified amount of numberofLines (records) from the Error logs for the service
     * of an event flow process.
     *
     * @param numberOfLines Number of Lines
     * @param serviceName   Name of the Service
     * @param appGUID       GUID of the event flow process
     * @param appVersion    version no of the event flow process
     * @return String - The Out logs till the specified number of lines
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String getLastErrTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method clears the out logs of a service in an event flow process.
     *
     * @param serviceInst Name of the Service
     * @param appGUID     event flow process GUID
     * @param appVersion  Version of the event flow process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void clearServiceOutLogs(String serviceInst, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method clears the error logs of a service in an event flow process.
     *
     * @param serviceInst Name of the Service
     * @param appGUID     event flow process GUID
     * @param appVersion  Version of the event flow process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void clearServiceErrLogs(String serviceInst, String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method clears the logs of an application specified by the <code>appGUID</code>
     * argument. This call clears the logs from all those Fiorano Peer Servers
     * to which any service of the specified application is currently
     * deployed on. This clears both Error and out logs of application
     *
     * @param appGUID  Application GUID of the Event Process
     * @param appVersion Version of the Event Process
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void clearApplicationLogs(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     * This method returns the logs of the service specified <code>serviceInst</code> which belongs to the application specified
     * by <code>appGUID</code>.
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @param serviceInst Name of the Service Instance
     * @param index       index of byte[] from where ro read the contents of zip file
     * @return Byte Array - Byte Array of Zipped contents of service logs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    byte[] exportServiceLogs(String appGUID, float version, String serviceInst, long index) throws RemoteException, ServiceException;


    /**
     * This method returns the logs of the specified application by <code>appGUID</code>
     *
     * @param appGUID  Application GUID of the Event Process
     * @param version  Version of the Event Process
     * @param index    index of byte[] from where ro read the contents of zip file
     * @return Byte Array - Byte Array of Zipped contents of Application logs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    byte[] exportApplicationLogs(String appGUID, float version, long index) throws RemoteException, ServiceException;


    /**
     * This method sets the log level for a service log module of an
     * application specified by the <code>appGUID</code>, <code>serviceInstName</code>
     * , and <code>moduleName</code> arguments.
     *
     * @param appGUID         Application Id
     * @param appVersion Application Version
     * @param serviceInstName Name of the Service Instance
     * @param modules         The name log of modules for which log level is to be set.
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    void setLogLevel(String appGUID, float appVersion, String serviceInstName, Hashtable modules) throws RemoteException, ServiceException;


    /**
     * This method changes the message selector for a route instance while the application
     * is running. Route is identified by its route id and application to which
     * it belongs. New values of selector and its type is updated in the route
     * of this running application.
     * Please note that it is meant to be used for RUNNING Applications only
     *
     * @param appGUID  Application GUID of running Event Process
     * @param appVersion Application Version
     * @param routeGUID ID of route of which selector is getting changed
     * @param selectors HashMap of Selectors
     * @throws ServiceException ServiceException
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     */
    void changeRouteSelector(String appGUID, float appVersion, String routeGUID, HashMap selectors) throws RemoteException, ServiceException;

    /**
     * This method changes the message selector configuration for a route instance while the application
     * is running. Route is identified by its route ID and application to which
     * it belongs. New values of selector configuration is updated in the route of this running application.
     * Please note that it is meant to be used for RUNNING Application only
     *
     * @param appGUID   Application GUID of running Event Process
     * @param appVersion Application Version
     * @param routeGUID ID of route of which selector is getting changed
     * @param configurationName The name of the new selector configuration which should be applied to the route
     * @throws ServiceException Thrown when either the selector configuration does not exist or some exception happens while applying the configuration
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     */
    void changeRouteSelectorConfiguration(String appGUID, float appVersion, String routeGUID, String configurationName) throws RemoteException, ServiceException;

    /**
     * This method returns the WADL URL of the deployed REST Service, when an Event process is exposed as a REST service using RESTStub component,
     *
     * @param appGUID      appGUID
     * @param appVersion   Application Version
     * @param servInstName Service instance Name of the RESTStub
     * @return String - WADL URL of the deployed REST Service
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String getWADLURL(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException;

    /**
     * This method returns the WSDL URL of the deployed SOAP Service, when an Event process is exposed as a SOAP service using WSStub component,
     *
     * @param appGUID      appGUID
     * @param appVersion   Application Version
     * @param servInstName Service instance Name of the WSStub
     * @return String - WSDL URL of the deployed SOAP Service
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    String getWSDLURL(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException;

    /**
     * This method returns a Set of running Application GUIDs which are using the specified Service Instance as a remote service
     *
     * @param appGUID      appGUID
     * @param appVersion    appVersion 
     * @param servInstName Service instance Name of the RESTStub
     * @return Set - String Set of running AppGUIDs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    Set<String> getReferringRunningApplications(String appGUID, float appVersion, String servInstName) throws RemoteException, ServiceException;

    /**
     * This method returns a List of Application GUIDs which are using the Service Instance(s) of the current Application  as remote service(s)
     *
     * @param appGUID          appGUID
     * @param appVersion       appVersion
     * @param serviceInstName  SeviceInstanceName
     * @return List - String List of running AppGUIDs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    List<String> getAllReferringApplications(String appGUID, float appVersion, String serviceInstName) throws RemoteException, ServiceException;

   /* *//**
     * This method returns the memory Usage for service in the specified application
     * @param appGUID appGUID
     * @param appVersion Application Version
     * @param componentGUID handle ID
     * @return MemoryUsageMetaData - Object of MemoryUsageMetaData containing memory usage of service in the specified Event Process
     * @throws com.fiorano.openesb.utils.exception.FioranoException
     *//*
    public MemoryUsageMetaData getMemoryUsage(String appGUID, float appVersion, String componentGUID) throws ServiceException;

    *//**
     * This method returns Application List info
     *
     * @return Hashtable - containing Application List info
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     *//*
    public Hashtable<String, ApplicationData> getAppInfo() throws RemoteException, ServiceException;

    *//**
     * This method returns Application info
     * @param appGuid appGUID
     * @param appVersion Application Version
     * @return ApplicationData - An Object of ApplicationData containing Application info
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     *//*
    public ApplicationData getAppInfo(String appGuid, float appVersion) throws RemoteException, ServiceException;*/

    /**
     * @param appGUID
     * @param appVersion
     * @return true is application is being referred by some other application i.e. if some other application is to have a remote instance of this application's service
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isApplicationReferred(String appGUID, float appVersion)throws RemoteException, ServiceException;

    /**
     * @param configsToChange HashMap of all the configurations with their details that need to be changed
     * @throws RemoteException
     * @throws ServiceException
     */
    HashMap getRunningCompUsingNamedConfigs(HashMap<Integer, HashMap<String, String>> configsToChange) throws RemoteException, ServiceException;

    /**
     * @param configsToChange HashMap of all the configurations with their details that need to be changed
     * @throws RemoteException
     * @throws ServiceException
     */
    String changeNamedConfigurations(HashMap<Integer, HashMap<String, String>> configsToChange) throws RemoteException , ServiceException;

    /**
     * @throws RemoteException
     * @throws ServiceException
     */
    String synchronizeAllRunningEP() throws RemoteException,ServiceException;
    /**
     * Returns whether the route is durable at fes level
     * @return
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isFESLevelRouteDurable();

    /**
     * calculates the durability at the application level
     * @param appGUID
     * @param appVersion
     * @return
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isAppLevelRouteDurable(String appGUID, float appVersion) throws RemoteException, ServiceException;

    /**
     *
     * @param appGUID
     * @param appVersion
     * @param routeID
     * @return
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isRouteDurable(String appGUID, float appVersion, String routeID) throws RemoteException, ServiceException;

    /**
     *
     * @param appGUID
     * @param appVersion
     * @return
     * @throws RemoteException
     * @throws ServiceException
     */
    boolean isDeleteDestinationSetAtApp(String appGUID, float appVersion) throws RemoteException, ServiceException;

    boolean isServiceRunning(String eventProcessName, float appVersion, String servInstanceName) throws RemoteException, ServiceException;

    public ServiceInstance getServiceInstance(String eventProcessName, float appVersion, String servInstanceName) throws ServiceException;

    public Properties getServerConfig() throws ServiceException;

    public Properties getTransportConfig() throws ServiceException;
}
