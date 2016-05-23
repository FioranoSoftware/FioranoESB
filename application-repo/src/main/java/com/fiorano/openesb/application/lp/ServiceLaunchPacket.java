/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.lp;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.NamedObject;
import com.fiorano.openesb.application.application.LogManager;
import com.fiorano.openesb.application.application.ManageableProperty;
import com.fiorano.openesb.application.application.PortInstance;
import com.fiorano.openesb.application.application.SchemaReference;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.ServiceRef;
import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class ServiceLaunchPacket extends DmiObject
{
    int             launchType;
    boolean         m_isVersionLocked;
    boolean         m_bIsDebugMode;
    boolean         m_bKillPrimaryOnSecondaryLaunch;
    int             m_iDebugPort;
    int             launchOrderSequence;
    String          m_version;
    String          m_servInstName;
    String          m_servGUID;
    String          m_longDescription;
    String          m_shortDescription;
    List            m_runtimeArgs = new ArrayList();
    List            inputPorts = new ArrayList();
    List            outputPorts = new ArrayList();
    List            m_logModules = new ArrayList();
    List <SchemaReference>   schemaReferences = new ArrayList<SchemaReference>();
    String          m_strNodeName;
    List <ManageableProperty>manageableProperties = new ArrayList<ManageableProperty>();//used by eStudio[EPLCM.]
    String          m_uniqueRunningInstID;
    String          configuration;
    Hashtable<String, String>       connectionFactoryProps = new Hashtable<String, String>();

    // Logging specific parameters
    LogManager logManager;

    //  Event specific parameters.
    long            m_dBufferSizePerPort;

    // Fixed N_3366 runtime dependencies
    Vector          m_runtimeDependencies = new Vector();
    private Vector  m_allRouteLP = new Vector();

    //  Flag indicating whether service needs to be refetched during
    //  prepareLaunch
    private boolean m_bNoCache = false;

    //Specifies if this service instance is a remote service instance
    private boolean remoteServiceInstance;

    //Application GUID of the remote application. This will be set only if this service instance is a remote service instance.
    private String remoteApplicationGUID;
    private String remoteAppVersion;
    private String remoteServiceInstanceName;
    private HashMap<String, String> namedConfigurations = new HashMap<String, String>();

    /**
     *  Constructor for the ServiceLaunchPacket object
     *
     * @since Tifosi2.0
     */
    public ServiceLaunchPacket()
    {
        m_dBufferSizePerPort = -1;
        m_bKillPrimaryOnSecondaryLaunch = true;
        launchOrderSequence = -1;
    }

    public String getConfiguration(){
        return configuration;
    }

    public void setConfiguration(String configuration){
        this.configuration = configuration;
    }

    public Hashtable<String, String> getConnectionFactoryProps() {
        return connectionFactoryProps;
    }

    public void setConnectionFactoryProps(Hashtable<String, String> connectionFactoryProps) {
        this.connectionFactoryProps = connectionFactoryProps;
    }

    public HashMap<String, String> getNamedConfigurations() {
        return namedConfigurations;
    }

    public void setNamedConfigurations(HashMap<String, String> namedConfigurations) {
        this.namedConfigurations = namedConfigurations;
    }

    /**
     * setter for manageable properties.
     * [in case of nStudio - value is set to an empty list and for eStudio it sets it to an arraylist with 0 or more elements.] 
     * @param manageableProperties List of manageable properties.
     */
    public void setManageableProperties(List manageableProperties){
        this.manageableProperties = manageableProperties;
    }

    /**
     * getter for manageable properties.
     * @return List empty list in case of event process being deployed through nStudio.
     */
    public List getManageableProperties(){
        return manageableProperties;
    }


    /**
     *  This interface method is for enumeration containing all runtime dependencies, for
     *  the service about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @return
     */
    public Enumeration getRuntimeDependencies()
    {
        // Fixed N_3366
        return m_runtimeDependencies.elements();
    }

    /**
     *  This method returns the maximum buffer size that can be stored
     *  per port.
     *
     * @return
     * @see #setBufferSizePerPort(long)
     * @since Tifosi2.0
     */
    public long getBufferSizePerPort()
    {
        return m_dBufferSizePerPort;
    }

    /**
     *  This interface method is called to get name of the user who is trying to
     *  launch the application, using this object of <code>ApplicationLaunchPacket</code>
     *
     * @return The name of user
     * @since Tifosi2.0
     */
    public boolean getNoCache()
    {
        return m_bNoCache;
    }

    /**
     *  This method gets the logManager used by this ServiceInstance.
     *
     * @return The logManager used by this ServiceInstance.
     */
    public LogManager getLogManager()
    {
        return logManager;
    }

    /**
     *  This method checks whether or not launch of this service instance is
     *  in debug mode.
     *
     * @return boolean specifying whether or not launch of this service
     *      instance is in debug mode.
     * @see #setDebugMode(boolean)
     * @since Tifosi2.0
     */
    public boolean isDebugMode()
    {
        return m_bIsDebugMode;
    }

    /**
     *  This method gets the debug port for this service instance. This port
     *  will be used by JBuilder(R) tool for remote debugging.
     *
     * @return debugging port to be used for remote debugging.
     * @see #setDebugPort(int)
     * @since Tifosi2.0
     */
    public int getDebugPort()
    {
        return m_iDebugPort;
    }

    /**
     * This method returns the sequence number of launch order for this service instance
     * @return launch order sequence number
     */
    public int getLaunchOrderSequence() {
        return launchOrderSequence;
    }

    /**
     * This method responsible for setting the launch order sequence number for this service instance
     * @param launchOrderSequence launch order sequence number
     */
    public void setLaunchOrderSequence(int launchOrderSequence) {
        this.launchOrderSequence = launchOrderSequence;
    }

    /**
     *  If a service is bound to a FPS and then another service with the same
     *  name tries to bind to the same FPS, then we have two options. If this
     *  boolean is set to true then the primary service will be killed and
     *  secondary service will take its place.If set to false, the secondary
     *  service will kill itself.This method checks the condition of this variable.
     *
     * @return boolean specifying whether or not the first running service
     *      will be killed on secondary launch or not.
     * @see #setKillPrimaryOnSecondaryLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isKillPrimaryOnSecondaryLaunch()
    {
        return m_bKillPrimaryOnSecondaryLaunch;
    }


    /**
     *  This interface method is called to get the instance name of service instance,
     *  with this object as <code>ServiceLaunchPacket</code>.
     *
     * @return The serviceInstName
     * @see #setServiceInstName(String)
     * @since Tifosi2.0
     */
    public String getServiceInstName()
    {
        return m_servInstName;
    }


    /**
     *  This interface method is called to get serviceGUID of the service instance,
     *  with this object as <code>ServiceLaunchPacket</code>.
     *
     * @return The serviceGUID value for this service instance
     * @see #setServiceGUID(String)
     * @since Tifosi2.0
     */
    public String getServiceGUID()
    {
        return m_servGUID;
    }

    /**
     * Returns unique running inst ID for object
     *
     * @return
     */
    public String getUniqueRunningInstID()
    {
        return m_uniqueRunningInstID;
    }


    /**
     *  This interface method is called to get the specified string as service version
     *  of the service instance with this object as <code>ServiceLaunchPacket</code>.
     *
     * @return Version of service for this service instance
     * @see #setVersion(String)
     * @since Tifosi2.0
     */
    public String getVersion()
    {
        return m_version;
    }


    /**
     *  This interface method is called to check whether or not service version
     *  of the service instance, with this object as <code>ServiceLaunchPacket</code>
     *  is locked.
     *
     * @return True if service version is locked, false otherwise
     * @see #setIsVersionLocked(boolean)
     * @since Tifosi2.0
     */
    public boolean isVersionLocked()
    {
        return m_isVersionLocked;
    }


    /**
     *  This interface method is called to get long description of the service
     *  instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @return The long description for this service instance
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_longDescription;
    }


    /**
     *  This interface method is called to get short description of the service
     *  instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @return The short description for this service instance
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_shortDescription;
    }

    /**
     *  This interface method is called to get the node name, over which the service
     *  instance with this object as <code>ServiceLaunchPacket</code>, is to be
     *  launched.
     *
     * @return The node name for this service instance
     * @see #setNodeName(String)
     * @since Tifosi2.0
     */
    public String getNodeName()
    {
        return m_strNodeName;
    }

    /**
     *  This interface method is called to get the object of <code>RuntimeArgs</code>,
     *  set as runtime arguments for this service instance.
     *
     * @return Object of RuntimeArgs for this service instance
     * @see #setRuntimeArgs(List)
     * @since Tifosi2.0
     */
    public List getRuntimeArgs()
    {
        return m_runtimeArgs;
    }

    /**
     *  This interface method is called to get the object of <code>Schema References</code>,
     *
     * @return Object of schema references for this service instance
     */
    public List getSchemaReferences() {
        return schemaReferences;
    }

    /**
     *  This interface method is called to set the specified object
     *  of schema references for this service instance.
     *
     * @param schemaReferences Object of schemaReferences to be set for this service
     *                     instance
     */
    public void setSchemaReferences(List schemaReferences) {
        this.schemaReferences = schemaReferences;
    }

    /**
     *  This interface method is called to get the object of <code>LogModules</code>, set
     *  for logging for the service instance with this object
     *  as <code>ServiceLaunchPacket</code>.
     *
     * @return Object of LogModules for this service instance
     * @see #setLogModules(List)
     * @since Tifosi2.0
     */
    public List getLogModules()
    {
        return m_logModules;
    }

    /**
     * This interface method is called to obtain a vector of all the objects
     * of <code>RouteLaunchPacket</code> from the list of route launch packets.
     * This is done for the service instance, with this object
     * as <code>ServiceLaunchPacket</code>.
     *
     * @return Vector containing objects of RouteLaunchPacket for this service
     *           instance
     * @see #addRouteLP(RouteLaunchPacket)
     * @see #removeRouteLP(RouteLaunchPacket)
     * @see #getRouteLPEnumeration()
     * @see #clearRouteLPVector()
     * @since Tifosi2.0
     */
    public Vector getRouteLPVector()
    {
        return m_allRouteLP;
    }

    /**
     * This interface method is called to get Enumeration of all the objects
     * of <code>RouteLaunchPacket</code> from the list of route launch packets.
     * This is done for the service instance with this object
     * as <code>ServiceLaunchPacket</code>.
     *
     * @return Enumeration containing objects of RouteLaunchPacket for this service instance.
     * @see #addRouteLP(RouteLaunchPacket)
     * @see #getRouteLPVector()
     * @see #removeRouteLP(RouteLaunchPacket)
     * @see #clearRouteLPVector()
     * @since Tifosi2.0
     */
    public Enumeration getRouteLPEnumeration()
    {
        return m_allRouteLP.elements();
    }

    /**
     * Returns a list of Input Ports.  A clone of the original list is returned.
     * @return List
     */
    public List getInputPorts(){
        return (List)((ArrayList)inputPorts).clone();   //return clone to prevent modification of original list
    }
    /**
     * Returns a list of the OutPut Ports. A clone of the original list is returned.
     * @return List
     */
    public List getOutputPorts(){
        return (List)((ArrayList)outputPorts).clone(); //return clone to prevent modification of original list
    }

    public PortInstance getPort(String name){
        NamedObject dmi = DmiObject.findNamedObject(inputPorts, name);
        return (PortInstance)(dmi!=null ? dmi : DmiObject.findNamedObject(outputPorts, name));
    }

   /* public boolean isSBWEnabled(){
        Iterator iter = new SequenceIterator(inputPorts.iterator(), outputPorts.iterator());
        while(iter.hasNext()){
            if(((PortInstance)iter.next()).getWorkflow()!=PortInstance.WORKFLOW_NONE)
                return true;
        }
        return false;
    }

    public boolean isMessageFilterSet(){
        Iterator itr = new SequenceIterator(inputPorts.iterator(), outputPorts.iterator());
        while(itr.hasNext()){
            if(((PortInstance)itr.next()).getIsMessageFilterSet())
                return true;
        }
        return false;
    }
*/
    /**
     *  This API returns the id of this object.
     *
     * @return The id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_LAUNCHPACKET;
    }

    /**
     *  This interface method is called to set the specified string as name of
     *  the user who is trying to launch the application, using this object of
     *  <code>ApplicationLaunchPacket</code>.
     *
     * @param bNoCache
     * @since Tifosi2.0
     */
    public void setNoCache(boolean bNoCache)
    {
        m_bNoCache = bNoCache;
    }

    /**
     *  This method sets the maximum buffer size that are to be stored
     *  per port. The FPS starts generating alerts when the number of
     *  messages come close to this limit
     *
     * @param bufferSize
     * @see #getBufferSizePerPort()
     * @since Tifosi2.0
     */
    public void setBufferSizePerPort(long bufferSize)
    {
        m_dBufferSizePerPort = bufferSize;
    }


    /**
     *  This method sets the logManager which is to be used by this
     *  ServiceInstance.
     *
     * @param logManager The new logManager value
     */
    public void setLogManager(LogManager logManager)
    {
        this.logManager = logManager;
    }

    public int getLaunchType(){
        return this.launchType;
    }

    public void setLaunchType(int launchType){
        this.launchType = launchType;
    }

    /**
     *  This method sets whether or not launch of this service instance is
     *  in debug mode. If in debug mode then it is compulsory to specify
     *  the debug port.
     *
     * @param isDebugMode
     * @see #isDebugMode()
     * @since Tifosi2.0
     */
    public void setDebugMode(boolean isDebugMode)
    {
        m_bIsDebugMode = isDebugMode;
    }

    /**
     *  This method sets the debug port for this service instance. This port
     *  will be used by JBuilder(R) tool for remote debugging.
     *
     * @param debugPort
     * @see #getDebugPort()
     * @since Tifosi2.0
     */
    public void setDebugPort(int debugPort)
    {
        m_iDebugPort = debugPort;
    }

    /**
     *  If a service is bound to a FPS and then another service with the same
     *  name tries to bind to the same FPS, then we have two options. If this
     *  boolean is set to true then the primary service will be killed and
     *  secondary service will take its place.If set to false, the secondary
     *  service will kill itself.
     *
     * @param bool boolean specifying whether or not the first running service
     *      will be killed on secondary launch or not.
     * @see #isKillPrimaryOnSecondaryLaunch()
     * @since Tifosi2.0
     */
    public void setKillPrimaryOnSecondaryLaunch(boolean bool)
    {
        m_bKillPrimaryOnSecondaryLaunch = bool;
    }

    /**
     *  This interface method is called to set the specified string as instance name of
     *  service instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @param servInstName The string to be set as serviceInstName
     * @see #getServiceInstName()
     * @since Tifosi2.0
     */
    public void setServiceInstName(String servInstName)
    {
        m_servInstName = servInstName;
    }


    /**
     *  This interface method is called to set the specified string as serviceGUID
     *  of the service instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @param servGUID The string to be set as serviceGUID for this service
     *                  instance
     * @see #getServiceGUID()
     * @since Tifosi2.0
     */
    public void setServiceGUID(String servGUID)
    {
        m_servGUID = servGUID;
    }

    /**
     * Sets unique running inst ID for object
     *
     * @param uniqueAppInstID
     */
    public void setUniqueRunningInstID(String uniqueAppInstID)
    {
        m_uniqueRunningInstID = uniqueAppInstID;
    }

    /**
     *  This interface method is called to set the specified string as service version
     *  of the service instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @param version The string to be set as version of service for this
     *                 service instance
     * @see #getVersion()
     * @since Tifosi2.0
     */
    public void setVersion(String version)
    {
        m_version = version;
    }


    /**
     *  This interface method is called to set a boolean specifying whether or not
     *  service version of the service instance, with this object
     *  as <code>ServiceLaunchPacket</code>, is locked.
     *
     * @param isVersionLocked Boolean specifying whether or not the service version is
     *                         locked.
     * @see #isVersionLocked()
     * @since Tifosi2.0
     */
    public void setIsVersionLocked(boolean isVersionLocked)
    {
        m_isVersionLocked = isVersionLocked;
    }


    /**
     *  This interface method is called to set the specified string as long description
     *  of service instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @param longDescription The string to be set as long description for the
     *                         service instance
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String longDescription)
    {
        m_longDescription = longDescription;
    }


    /**
     *  This interface method is called to set the specified string as short
     *  description of the service instance, with this object
     *  as <code>ServiceLaunchPacket</code>.
     *
     * @param shortDescription The string to be set as short description for
     *                          this service instance
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String shortDescription)
    {
        m_shortDescription = shortDescription;
    }

    /**
     *  This interface method is called to set the specified string as node name,
     *  over which the service instance with this object as <code>ServiceLaunchPacket</code>,
     *  is to be launched.
     *
     * @param nodeName The string to be set as node name over which this service
     *                  instance is to be launched.
     * @see #getNodeName()
     * @since Tifosi2.0
     */
    public void setNodeName(String nodeName)
    {
        m_strNodeName = nodeName;
    }

    /**
     *  This interface method is called to set the specified object
     *  of <code>RuntimeArgs</code>, as runtime arguments for this service instance.
     *
     * @param runtimeArgs Object of RuntimeArgs to be set for this service
     *                     instance
     * @see #getRuntimeArgs()
     * @since Tifosi2.0
     */
    public void setRuntimeArgs(List runtimeArgs)
    {
        m_runtimeArgs = runtimeArgs;
    }

    /**
     *  This interface method is called to set the specified object of
     *  <code>LogModules</code> for logging for the service instance,
     *  with this object as <code>ServiceLaunchPacket</code>.
     *
     * @param logModules The object of LogModules to be set for this service instance
     * @see #getLogModules()
     * @since Tifosi2.0
     */
    public void setLogModules(List logModules)
    {
        m_logModules = logModules;
    }

    public void setInputPorts(List inputPorts){
        this.inputPorts = inputPorts;
    }

    public void setOutputPorts(List outputPorts){
        this.outputPorts = outputPorts;
    }

    public boolean isRemoteServiceInstance() {
        return remoteServiceInstance;
    }

    public String getRemoteServiceInstanceName() {
        return remoteServiceInstanceName;
    }

    public String getRemoteApplicationGUID() {
        return remoteApplicationGUID;
    }

    public String getRemoteAppVersion() {
        return remoteAppVersion;
    }

    public void setRemoteServiceInstance(boolean remoteServiceInstance) {
        this.remoteServiceInstance = remoteServiceInstance;
    }

    public void setRemoteServiceInstanceName(String remoteServiceInstanceName) {
        this.remoteServiceInstanceName = remoteServiceInstanceName;
    }

    public void setRemoteApplicationName(String remoteApplicationGUID) {
        this.remoteApplicationGUID = remoteApplicationGUID;
    }

    public void setRemoteAppVersion(String remoteAppVersion) {
        this.remoteAppVersion = remoteAppVersion;
    }

    /**
     *  This interface method is called to add the specified object
     *  of <code>RuntimeDependency</code> to the list of service dependencies.
     *  This is for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @param servDependencyInfo object of  RuntimeDependency to be added.
     * @see #removeRuntimeDependency(com.fiorano.openesb.application.service.ServiceRef)
     * @see #clearRuntimeDependency()
     * @since Tifosi2.0
     */
    public void addRuntimeDependency(ServiceRef servDependencyInfo)
    {
        // Fixed N_3366
        if (m_runtimeDependencies == null)
        {
            m_runtimeDependencies = new Vector();
        }
        if (!m_runtimeDependencies.contains(servDependencyInfo))
            m_runtimeDependencies.add(servDependencyInfo);
    }


    /**
     *  This interface method is called to remove the specified object
     *  of <code>RuntimeDependency</code> from the list of service dependencies.
     *  This is for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @param serv object of  RuntimeDependency to be removed.
     * @see #addRuntimeDependency(com.fiorano.openesb.application.service.ServiceRef)
     * @see #clearRuntimeDependency()
     * @since Tifosi2.0
     */
    public void removeRuntimeDependency(ServiceRef serv)
    {
        // Fixed N_3366
        if (m_runtimeDependencies != null)
        {
            m_runtimeDependencies.remove(serv);
        }
    }

    /**
     *  This interface method is called to clear the list of service dependencies, for
     *  the service about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @see #addRuntimeDependency(com.fiorano.openesb.application.service.ServiceRef)
     * @see #removeRuntimeDependency(com.fiorano.openesb.application.service.ServiceRef)
     * @since Tifosi2.0
     */
    public void clearRuntimeDependency()
    {
        // Fixed N_3366
        if (m_runtimeDependencies != null)
        {
            m_runtimeDependencies.clear();
        }
    }

    /**
     *  This interface method is called to add the specified object
     *  of <code>RouteLaunchPacket</code> to the list of route launch packets.
     *  This is done for the service instance, with this object
     *  as <code>ServiceLaunchPacket</code>.
     *
     * @param routeLP Object of RouteLaunchPacket to be added
     * @see #getRouteLPVector()
     * @see #removeRouteLP(RouteLaunchPacket)
     * @see #getRouteLPEnumeration()
     * @see #clearRouteLPVector()
     * @since Tifosi2.0
     */
    public void addRouteLP(RouteLaunchPacket routeLP)
    {
        m_allRouteLP.add(routeLP);
    }

    /**
     *  This interface method is called to remove the specified object
     *  of <code>RouteLaunchPacket</code> from the list of route launch packets.
     *  This is done for the service instance with this object
     *  as <code>ServiceLaunchPacket</code>.
     *
     * @param routeLP Object of RouteLaunchPacket to be removed
     * @see #addRouteLP(RouteLaunchPacket)
     * @see #getRouteLPVector()
     * @see #getRouteLPEnumeration()
     * @see #clearRouteLPVector()
     * @since Tifosi2.0
     */
    public void removeRouteLP(RouteLaunchPacket routeLP)
    {
        m_allRouteLP.remove(routeLP);
    }

    /**
     *   This interface method is called to clear the list of route launch packets
     *   for the service instance, with this object as <code>ServiceLaunchPacket</code>.
     *
     * @see #addRouteLP(RouteLaunchPacket)
     * @see #getRouteLPVector()
     * @see #removeRouteLP(RouteLaunchPacket)
     * @see #getRouteLPEnumeration()
     * @since Tifosi2.0
     */
    public void clearRouteLPVector()
    {
        m_allRouteLP.clear();
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_runtimeDependencies = new Vector();
    }


    /**
     *  This method tests whether this object of <code>ServiceLaunchPacket</code>
     *  has the required (mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_runtimeDependencies != null)
        {
            Enumeration _enum = m_runtimeDependencies.elements();

            while (_enum.hasMoreElements())
            {
                ServiceRef serv = (ServiceRef) _enum.nextElement();

                serv.validate();
            }
        }
    }


    /**
     *  This method is called to write this object of <code>ServiceLaunchPacket</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.
     * @see #fromStream(DataInput, int)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeBoolean(m_bKillPrimaryOnSecondaryLaunch);

        out.writeBoolean(m_isVersionLocked);

        out.writeBoolean(m_bIsDebugMode);

        out.writeInt(m_iDebugPort);

        out.writeInt(launchOrderSequence);

        out.writeInt(launchType);

        out.writeBoolean(m_bNoCache);

        if (m_version != null)
            UTFReaderWriter.writeUTF(out, m_version);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (configuration != null)
            UTFReaderWriter.writeUTF(out, configuration);
        else
            UTFReaderWriter.writeUTF(out, "");

        if(connectionFactoryProps != null && connectionFactoryProps.size() > 0){
            int size = connectionFactoryProps.size();

            out.writeInt(size);
            for(String propName : connectionFactoryProps.keySet()){
                UTFReaderWriter.writeUTF(out, propName);
                UTFReaderWriter.writeUTF(out, connectionFactoryProps.get(propName));
            }
        }else
            out.writeInt(0);

        if(namedConfigurations != null && namedConfigurations.size() > 0){
            int size = namedConfigurations.size();

            out.writeInt(size);
            for(String propName : namedConfigurations.keySet()){
                UTFReaderWriter.writeUTF(out, propName);
                UTFReaderWriter.writeUTF(out, namedConfigurations.get(propName));
            }
        }else
            out.writeInt(0);

        out.writeLong(m_dBufferSizePerPort);

        if (m_servInstName != null)
            UTFReaderWriter.writeUTF(out, m_servInstName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_servGUID != null)
            UTFReaderWriter.writeUTF(out, m_servGUID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_uniqueRunningInstID != null)
            UTFReaderWriter.writeUTF(out, m_uniqueRunningInstID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_longDescription != null)
            UTFReaderWriter.writeUTF(out, m_longDescription);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_shortDescription != null)
            UTFReaderWriter.writeUTF(out, m_shortDescription);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_strNodeName != null)
            UTFReaderWriter.writeUTF(out, m_strNodeName);
        else
            UTFReaderWriter.writeUTF(out, "");

        out.writeBoolean(remoteServiceInstance);

        if (remoteServiceInstanceName != null)
            UTFReaderWriter.writeUTF(out, remoteServiceInstanceName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (remoteApplicationGUID != null)
            UTFReaderWriter.writeUTF(out, remoteApplicationGUID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (remoteAppVersion != null)
            UTFReaderWriter.writeUTF(out, remoteAppVersion);
        else
            UTFReaderWriter.writeUTF(out, "");

        // Service Dependency
        if (m_runtimeDependencies != null && m_runtimeDependencies.size() > 0)
        {
            int num = m_runtimeDependencies.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                ServiceRef serv = (ServiceRef) m_runtimeDependencies.elementAt(i);

                serv.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if(manageableProperties.size() >0){

            int num = manageableProperties.size() ;
            out.writeInt(num);

            for(int i =0; i< num ; i++){
                ManageableProperty temp =(ManageableProperty)manageableProperties.get(i);
                temp.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        if (schemaReferences.size() > 0){
            int num = schemaReferences.size();
            out.writeInt(num);

            for (int i=0; i<num; i++){
                SchemaReference tmpSchemaRef = (SchemaReference)schemaReferences.get(i);
                tmpSchemaRef.toStream(out, versionNo);
            }
        }else
            out.writeInt(0);

        toStream(m_runtimeArgs, out, versionNo);

        toStream(inputPorts, out, versionNo);
        toStream(outputPorts, out, versionNo);

        toStream(m_logModules, out, versionNo);

        if (m_allRouteLP != null)
        {
            int size = m_allRouteLP.size();

            out.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                ((RouteLaunchPacket) m_allRouteLP.get(i)).toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        toStream(logManager, out, versionNo);
    }


    /**
     *  This is called to read this object <code>ServiceLaunchPacket</code> from
     *  the specified object of input stream.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.
     * @see #toStream(DataOutput, int)
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_bKillPrimaryOnSecondaryLaunch = is.readBoolean();

        m_isVersionLocked = is.readBoolean();

        m_bIsDebugMode = is.readBoolean();

        m_iDebugPort = is.readInt();

        launchOrderSequence = is.readInt();

        launchType = is.readInt();

        m_bNoCache = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_version = null;
        else
            m_version = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            configuration = null;
        else
            configuration = temp;

        int tempSize = is.readInt();
        for(int i = 0; i < tempSize; i++){
            String propName = UTFReaderWriter.readUTF(is);
            String propValue = UTFReaderWriter.readUTF(is);
            connectionFactoryProps.put(propName, propValue);
        }

        tempSize = is.readInt();
        for(int i = 0; i < tempSize; i++){
            String propName = UTFReaderWriter.readUTF(is);
            String propValue = UTFReaderWriter.readUTF(is);
            namedConfigurations.put(propName, propValue);
        }

        m_dBufferSizePerPort = is.readLong();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_servInstName = null;
        else
            m_servInstName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_servGUID = null;
        else
            m_servGUID = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_uniqueRunningInstID = null;
        else
            m_uniqueRunningInstID = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_longDescription = null;
        else
            m_longDescription = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_shortDescription = null;
        else
            m_shortDescription = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_strNodeName = null;
        else
            m_strNodeName = temp;

        remoteServiceInstance = is.readBoolean();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            remoteServiceInstanceName = null;
        else
            remoteServiceInstanceName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            remoteApplicationGUID = null;
        else
            remoteApplicationGUID = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            remoteAppVersion = null;
        else
            remoteAppVersion = temp;

        // Service Dependency
        int tempInt = is.readInt();

        for (int i = 0; i < tempInt; ++i)
        {
            ServiceRef serv = new ServiceRef();

            serv.fromStream(is, versionNo);
            m_runtimeDependencies.addElement(serv);
        }

        tempInt = is.readInt();

        for(int i = 0; i<tempInt; ++i){
            ManageableProperty config = new ManageableProperty();

            config.fromStream(is, versionNo);
            manageableProperties.add(config);
        }

        tempInt = is.readInt();

        for (int i=0; i<tempInt; i++){
            SchemaReference tmpSchemaRef = new SchemaReference();
            tmpSchemaRef.fromStream(is, versionNo);
            schemaReferences.add(tmpSchemaRef);
        }


        fromStream(m_runtimeArgs, DmiObjectTypes.NEW_RUNTIME_ARGUMENT, is, versionNo);
        fromStream(inputPorts, DmiObjectTypes.NEW_INPUT_PORT_INSTANCE, is, versionNo);
        fromStream(outputPorts, DmiObjectTypes.NEW_OUTPUT_PORT_INSTANCE, is, versionNo);

        fromStream(m_logModules, DmiObjectTypes.NEW_LOGMODULE, is, versionNo);

        tempInt = is.readInt();

        if (tempInt != 0)
        {
            m_allRouteLP = new Vector();
            for (int i = 0; i < tempInt; i++)
            {
                RouteLaunchPacket routeLP = new RouteLaunchPacket();

                routeLP.fromStream(is, versionNo);
                m_allRouteLP.add(routeLP);
            }
        }

        logManager = (LogManager)fromStream(DmiObjectTypes.NEW_LOG_MANAGER, is, versionNo);
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>ServiceLaunchPacket</code>.
     *
     * @return The String representation of this object.
     * @since Tifosi 2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Service Instance Details ");
        strBuf.append("[");
        strBuf.append("Is Primary Killed On Secondary Launch = ");
        strBuf.append(m_bKillPrimaryOnSecondaryLaunch);
        strBuf.append(", ");
        strBuf.append("Is Version Locked = ");
        strBuf.append(m_isVersionLocked);
        strBuf.append(", ");
        strBuf.append("LaunchType = ");
        strBuf.append(launchType);
        strBuf.append(", ");
        strBuf.append("Version = ");
        strBuf.append(m_version);
        strBuf.append(", ");
        strBuf.append("Service Instance Name = ");
        strBuf.append(m_servInstName);
        strBuf.append(", ");
        strBuf.append("GUID = ");
        strBuf.append(m_servGUID);
        strBuf.append(", ");
        strBuf.append("Unique RunningInstanceID = ");
        strBuf.append(m_uniqueRunningInstID);
        strBuf.append(", ");
        strBuf.append("Short Description = ");
        strBuf.append(m_shortDescription);
        strBuf.append(", ");
        strBuf.append("Long Description = ");
        strBuf.append(m_longDescription);
        strBuf.append(", ");
        strBuf.append("Node Name = ");
        strBuf.append(m_strNodeName);
        strBuf.append(", ");
        strBuf.append("Is Remote Service Instance = ");
        strBuf.append(remoteServiceInstance);
        strBuf.append(", ");
        strBuf.append("Remote Application Name = ");
        strBuf.append(remoteApplicationGUID);
        strBuf.append(", ");
        strBuf.append("Remote Application Version = ");
        strBuf.append(remoteAppVersion);
        strBuf.append(", ");
        strBuf.append("Is Debug Mode = ");
        strBuf.append(m_bIsDebugMode);
        strBuf.append(", ");
        strBuf.append("Debug Port specified = ");
        strBuf.append(m_iDebugPort);
        strBuf.append(", ");
        strBuf.append("Buffer Size Per Port = ");
        strBuf.append(m_dBufferSizePerPort);
        strBuf.append(", ");
        strBuf.append("NoCache = ");
        strBuf.append(m_bNoCache);
        strBuf.append(", ");

        if (m_runtimeDependencies != null)
        {
            strBuf.append("Runtime dependencies = ");
            for (int i = 0; i < m_runtimeDependencies.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append((m_runtimeDependencies.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }

        if (m_runtimeArgs != null)
        {
            strBuf.append("RunTime Arguments = ");
            strBuf.append(m_runtimeArgs.toString());
            strBuf.append(", ");
        }

        if (m_allRouteLP != null)
        {
            strBuf.append("Route Launch Packet = ");
            for (int i = 0; i < m_allRouteLP.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((RouteLaunchPacket) m_allRouteLP.get(i)));
                strBuf.append(", ");
            }
        }
        strBuf.append(inputPorts);
        strBuf.append(outputPorts);
        if (logManager != null)
        {
            strBuf.append("LogManager = ");
            strBuf.append(logManager);
        }
        if (m_logModules != null)
        {
            strBuf.append("LogModules = ");
            strBuf.append(m_logModules.toString());
            strBuf.append(", ");
        }
        strBuf.append("]");
        return strBuf.toString();
    }
}
