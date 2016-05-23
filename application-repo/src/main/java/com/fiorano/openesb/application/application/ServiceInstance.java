/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.aps.*;
import com.fiorano.openesb.application.configuration.data.ObjectCategory;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.Execution;
import com.fiorano.openesb.application.service.LogModule;
import com.fiorano.openesb.application.service.RuntimeArgument;
import com.fiorano.openesb.application.service.ServiceRef;
import com.fiorano.openesb.utils.CollectionUtil;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.Namespaces;
import com.fiorano.openesb.utils.StringUtil;
import com.fiorano.openesb.utils.crypto.StringEncrypter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

import static com.fiorano.openesb.application.application.NamedConfigurationProperty.NO_CONFIGURATION_ID;

public class
ServiceInstance extends InflatableDMIObject implements NamedObject{

    private static final String ELEM_TARGET = "target";
    /**
     * Element inst in event process xml
     */
    public static final String ELEM_SERVICE_INSTANCE = "inst";

    /**
     * Attribute value
     */
    public static final String ATTR_VALUE = "value";
    
    /**
     * Element instance in event process xml
     */
    public static final String ELEM_INSTANCE = "instance";

    public int getObjectID(){
        return DmiObjectTypes.NEW_SERVICE_INSTANCE;
    }

    /*-------------------------------------------------[ GUID ]---------------------------------------------------*/
    /**
     * Element service in event process xml
     */
    public static final String ELEM_SERVICE = "service";
    /**
     * Attribute guid
     */
    public static final String ATTR_GUID = "guid";

    private String guid;

    /**
     * Returns the Global Unique Identifier of this service instance
     * @return String - GUID of the service
     */
    public String getGUID(){
        return guid;
    }

    /**
     * Sets specified <code>GUID</code> for this service instance
     * @param guid GUID to be set
     */
    public void setGUID(String guid){
        this.guid = guid;
    }

    /*-------------------------------------------------[ Version ]---------------------------------------------------*/
    /**
     * Attribute version
     */
    public static final String ATTR_VERSION = "version";

    private float version;

    /**
     * Returns version of this service instance
     * @return float - version number
     *
     */
    public float getVersion(){
        return version;
    }

    /**
     * Sets specified <code>version</code> for this service instance
     * @param version version to be set
     */
    public void setVersion(float version){
        this.version = version;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns name of this service instance
     * @return String - Name of the service
     */
    public String getName(){
        return name;
    }

    /**
     * Sets specified <code>name</code> for this service instance
     * @param name Name to be set
     */
    public void setName(String name){
        this.name = name;
    }

        /*-------------------------------------------------[ Partial Configuration ]---------------------------------------------------*/

    /**
     * Attribute partiallyConfigured
     */
    public static final String ATTR_PARTIALLY_CONFIGURED = "partiallyConfigured";

    private boolean partiallyConfigured;

    /**
     * Returns whether the CPS has been partially configured or not
     * @return boolean - true if the CPS has been partially configured for this service, false otherwise
     */
    public boolean isPartiallyConfigured() {
        return partiallyConfigured;
    }

    /**
     * Sets whether the CPS of this service is partially configured
     * @param partiallyConfigured true if the CPS has been partially configured for this service, false otherwise
     */
    public void setPartiallyConfigured(boolean partiallyConfigured) {
        this.partiallyConfigured = partiallyConfigured;
    }

    /*-------------------------------------------------[ ShortDescription ]---------------------------------------------------*/
    /**
     * Element short-description in event process xml
     */
    public static final String ELEM_SHORT_DESCRIPTION = "short-description";

    private String shortDescription;

    /**
     * Returns ShortDescription of application (can be null)
     * @return String - Short description
     */
    public String getShortDescription(){
        return shortDescription;
    }

    /**
     * Sets short description for this service instance
     * @param shortDescription Short description to be set
     */
    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    /*-------------------------------------------------[ LongDescription ]---------------------------------------------------*/
    /**
     * Element long-description in event process xml
     */
    public static final String ELEM_LONG_DESCRIPTION = "long-description";

    private String longDescription;

    /**
     * Returns Long description of this service instance
     * @return String - Long Description
     */
    public String getLongDescription(){
        return longDescription;
    }

    /**
     * Sets specified <code>longDescription</code> for this service instance
     * @param longDescription Long description to be set
     */
    public void setLongDescription(String longDescription){
        this.longDescription = longDescription;
    }

    /*-------------------------------------------------[ Nodes ]---------------------------------------------------*/
    /**
     * Element deployment in event process xml
     */
    public static final String ELEM_DEPLOYMENT = "deployment";
    /**
     * Attribute nodes
     */
    public static final String ATTR_NODES = "nodes";

    private String nodes[] = new String[0];

    /**
     * Returns names of the peer servers on which this service will be launched as part of this application
     * @return String Array - List of peer servers configured
     */
    public String[] getNodes(){
        return nodes;
    }

    /**
     * Sets specified <code>nodes</code> as the names of peer servers on which this service instance has to be launched
     * @param nodes List of peer servers
     */
    public void setNodes(String nodes[]){
        this.nodes = nodes;
    }

    /*-------------------------------------------------[ versionLocked ]---------------------------------------------------*/
    /**
     * Attribute version-locked
     */
    public static final String ATTR_VERSION_LOCKED = "version-locked";

    private boolean versionLocked = true;

    /**
     * Returns whether to launch selected version or highest version of this service instance
     * @return boolean - true if the service is version locked, false otherwise
     */
    public boolean isVersionLocked(){
        return versionLocked;
    }

    /**
     * Sets a boolean specifying whether to launch selected version or highest version of this service instance
     * @param versionLocked true if the service is version locked, false otherwise
     */
    public void setVersionLocked(boolean versionLocked){
        this.versionLocked = versionLocked;
    }

    /*-------------------------------------------------[ LaunchType ]---------------------------------------------------*/
    /**
     * Element execution in event process xml
     */
    public static final String ELEM_EXECUTION = "execution";
    /**
     * Attribute launch type
     */
    public static final String ATTR_LAUNCH_TYPE = "type";

    private int launchType = Execution.LAUNCH_TYPE_SEPARATE_PROCESS;

    /**
     * Specifies launch type of this service instance
     * @return int - Launch type reference value
     */
    public int getLaunchType(){
        return launchType;
    }

    /**
     * Sets launch type for this service instance
     * @param launchType Launch type i.e separate process, in memory etc.
     */
    public void setLaunchType(int launchType){
        this.launchType = launchType;
    }

    /*-------------------------------------------------[ componentCacheDisabled ]---------------------------------------------------*/
    /**
     * Attribute cache
     */
    public static final String ATTR_COMPONENT_CACHED = "cache";

    private boolean componentCached = true;

    /**
     * Returns whether to refetch the service resources (excluding dependencies
     * eg: Java, RTL, XML, UTIL, etc) each time the application is launched. If the application's cache-component is false,
     * this value has no effect
     * @return boolean - true if component cache is enabled, false otherwise
     */
    public boolean isComponentCached(){
        return componentCached;
    }

    /**
     * Sets a boolean whether to use cached component or to refetch it
     * @param componentCached true if component cache is enabled, false otherwise
     */
    public void setComponentCached(boolean componentCached){
        this.componentCached = componentCached;
    }

    /*-------------------------------------------------[ Resources ]---------------------------------------------------*/
    /**
     * Element resource-properties in event process xml
     */
    public static final String ELEM_RESOURCE_PROPERTIES = "resource-properties";
    /**
     * Element resource-property in event process xml
     */
    public static final String ELEM_RESOURCE_PROPERTY = "resource-property";
    /**
     * Attribute name
     */
    public static final String ATTR_RESOURCE_PROPERTY_NAME = "name";
    private Properties resourceProperties;

    /**
     * Hashtable containing all named configurations used in the component configuration.
     * The property name in the hashtable specifies the configuration ID and the property
     * value is a hashtable whose key specifies the configuration name and the value specifies the type of configuration used.
     *
     * Having configurationID as key for this hashtable makes it easy to merge named configuration with manageable properties of service instances.
     */
    private Hashtable<String, Hashtable<String, String>> namedConfigurations;

    /**
     * Returns resource properties for this service instance
     * @return Properties - Resource properties for the service
     */
    public Properties getResourceProperties() {
        return resourceProperties;
    }

    /**
     * Sets resource properties for this service instance
     * @param resourceProperties Resource properties to be set
     */
    public void setResourcesProperties(Properties resourceProperties) {
        this.resourceProperties = resourceProperties;
    }

    /**
     * Element named-configurations in event process xml
     */
    public static final String ELEM_NAMED_CONFIGURATIONS = "named-configurations";
    /**
     * Element named-configuration in event process xml
     */
    public static final String ELEM_NAMED_CONFIGURATION = "named-configuration";

    /**
     * Attribute named-configuration-type in event process xml
     */
    public static final String ATTR_NAMED_CONFIGURATION_TYPE = "configurationType";

    /**
     * Attribute named-configuration-type in event process xml
     */
    public static final String ATTR_NAMED_CONFIGURATION_ID = "configurationID";

    /**
     * Sets the named configurations for the service
     * @param namedConfigurations ArrayList of named configurations
     */

    public void setNamedConfigurations(ArrayList<NamedConfigurationProperty> namedConfigurations) {
        if (namedConfigurations != null) {
            this.namedConfigurations = new Hashtable<String, Hashtable<String, String>>();

            for (NamedConfigurationProperty configurationProperty : namedConfigurations) {
                String configurationName = configurationProperty.getConfigurationName();
                String configurationType = configurationProperty.getConfigurationType();
                String configurationID = configurationProperty.getConfigurationID();

                addNamedConfiguration(configurationName, configurationType, configurationID);
            }
        }
    }

    private void addNamedConfiguration(String configurationName, String configurationType, String configurationID) {
        if (configurationID == null || configurationID.length() == 0)
            configurationID = NO_CONFIGURATION_ID;

        Hashtable<String, String> configurationSet = namedConfigurations.get(configurationID);
        if (configurationSet == null) {
            configurationSet = new Hashtable<String, String>();
            configurationSet.put(configurationName, configurationType);
            namedConfigurations.put(configurationID, configurationSet);
        } else {
            if (!NO_CONFIGURATION_ID.equals(configurationID))
                configurationSet.clear();

            configurationSet.put(configurationName, configurationType);
        }
    }

    /**
     * Returns the list of named configurations used within this service instance. The returned list a merged form of the configurations
     * specified for the ServiceInstance object and those specified under the environment for which the application has been loaded.
     * @return ArrayList - List of named configurations
     */
    public ArrayList<NamedConfigurationProperty> getNamedConfigurations() {
        ArrayList<NamedConfigurationProperty> toReturn = new ArrayList<NamedConfigurationProperty>();

        if (namedConfigurations != null && namedConfigurations.size() >0) {
            for(String configurationID : namedConfigurations.keySet()){
                Hashtable<String, String> configurationSet = namedConfigurations.get(configurationID);

                for (String configurationName : configurationSet.keySet()) {
                    String configurationType = configurationSet.get(configurationName);
                    NamedConfigurationProperty configurationProperty = new NamedConfigurationProperty(configurationName, configurationType, configurationID);
                    toReturn.add(configurationProperty);
                }
            }
        }

        return toReturn;
    }

    /*public Hashtable<String, String> getNamedConfigurations() {
        return namedConfigurations;
    }*/

    /*-------------------------------------------------[ configuration ]---------------------------------------------------*/
    /**
     * Element configuration in event process xml
     */
    public static final String ELEM_CONFIGURATION = "configuration";

    private String configuration;

    /**
     * Returns Configuration of this service instance
     * @return String - Configuration of this service
     */
    public String getConfiguration(){
        return configuration;
    }

    /**
     * Sets specified <code>configuration</code> for this service instance
     * @param configuration Configuration to be set
     */
    public void setConfiguration(String configuration){
        this.configuration = configuration;
    }

    /*-------------------------------------------------[ config-file ]---------------------------------------------------*/
    /**
     * Element config-file in event process xml
     */
    public static final String ELEM_CONFIG_FILE = "config-file";

    private String configFile;

    /**
     * Returns configuration file name of this service instance
     * @return String - Configuration file name
     */
    public String getConfigFile(){
        return configFile;
    }

    /**
     * Sets configuration file name of this service instance
     * @param configFile Configuration file name
     */
    public void setConfigFile(String configFile){
        this.configFile = configFile;
    }

    /*-------------------------------------------------[ BufferLimit ]---------------------------------------------------*/
    /**
     * Attribute buffer-limit
     */
    public static final String ATTR_BUFFER_LIMIT = "buffer-limit";

    private long DEFAULT_BUFFER_LIMIT = 524288L;
    private long bufferLimit = DEFAULT_BUFFER_LIMIT;

    /**
     * Returns Maximum buffer size that has to be stored per port. The PeerServer starts
     * generating alerts when the number of messages come close to this limit.
     * @return long - Maximum buffer size
     */
    public long getBufferLimit(){
        return bufferLimit;
    }

    /**
     * Sets buffer size to be stored per port of this service instance
     * @param bufferLimit Maximum buffer size to be set
     */
    public void setBufferLimit(long bufferLimit){
        this.bufferLimit = bufferLimit;
    }

  /*-------------------------------------------------[Schema-reference Type ]----------------------------------------------------*/
     /**
      * String containing schema-references used in service instance
      */
    public static final String ELEM_SCHEMA_REFERENCES = "schema-references";

    private  List schemaReferences = new ArrayList();

    /**
     * Returns the schema references of this service
     * @return List - List of schema references
     */
    public List getSchemaReferences() {
        return schemaReferences;
    }

    /**
     * Sets the schema references for this service
     * @param schemaReferences List of schema references
     */
    public void setSchemaReferences(List schemaReferences) {
        this.schemaReferences = schemaReferences;
    }

    /**
     * Adds the schema reference to list
     * @param schemaReference Schema reference to be added
     */
    public void addSchemaReference(SchemaReference schemaReference) {
        schemaReferences.add(schemaReference);
    }

    /**
     * Removes schema reference from the list
     * @param schemaReference Schema reference to be removed
     */
    public void removeSchemaReference(SchemaReference schemaReference){
        schemaReferences.remove(schemaReference);
    }

    /*-------------------------------------------------[ PreferLaunchOnHigherLevelNode ]---------------------------------------------------*/
    /**
     * Element launch in event process xml
     */
    public static final String ELEM_LAUNCH = "launch";
    /**
     * Attribute first-available-node
     */
    public static final String ATTR_PREFER_LAUNCH_ON_FIRST_AVAILABLE_NODE = "first-available-node";

    private boolean preferLaunchOnFirstAvailableNode = true;

    /**
     * Specifies whether or not launch of this service instance is preferred on first available node.
     * @return boolean - true if the first available node is preferred for launch, false otherwise
     */
    public boolean isPreferLaunchOnFirstAvailableNode(){
        return preferLaunchOnFirstAvailableNode;
    }

    /**
     * Sets a boolean specifying whether to launch this service instance on highest level i.e. on the first node
     * specified in the peer server names of this service instance when it becomes available even if this service instance is running on some
     * other peer server
     * @param preferLaunchOnFirstAvailableNode true if the first available node is preferred for launch, false otherwise
     */
    public void setPreferLaunchOnFirstAvailableNode(boolean preferLaunchOnFirstAvailableNode){
        this.preferLaunchOnFirstAvailableNode = preferLaunchOnFirstAvailableNode;
    }

    /*-------------------------------------------------[ Connection Factory Properties ]---------------------------------------------------*/

    /**
     * Element connection factory properties in event process xml
     */
    public static final String ELEM_CONNECTION_FACTORY_PROPERTIES = "connection-factory-properties";


    /**
     * Element connection factory property in event process xml
     */
    public static final String ELEM_CONNECTION_FACTORY_PROPERTY = "connection-factory-property";

    private Hashtable<String, String> connectionFactoryProperties = new Hashtable<String, String>();

    /**
     * Sets the connection factory properties
     * @param connectionFactoryProperties HashTable of connection properties
     */
    public void setConnectionFactoryProperties(Hashtable<String, String> connectionFactoryProperties) {
        this.connectionFactoryProperties = connectionFactoryProperties;
    }

    /**
     * Returns the connection factory properties of this service
     * @return HashTable - Connection factory properties table
     */
    public Hashtable<String, String> getConnectionFactoryProperties() {
        return connectionFactoryProperties;
    }

    /*-------------------------------------------------[ Connection Factory Config Name ]---------------------------------------------------*/

    /**
     * Element connection factory config name in event process xml
     */
    public static final String ELEM_CONNECTION_FACTORY_CONFIG_NAME = "connection-factory-config-name";

    private String connectionFactoryConfigName;

    /**
     * Sets the connection factory configuration name
     * @param connectionFactoryConfigName Name of the configuration
     */
    public void setConnectionFactoryConfigName(String connectionFactoryConfigName) {
        this.connectionFactoryConfigName = connectionFactoryConfigName;
    }

    /**
     * Returns the connection factory configuration name
     * @return HashTable - Name of the configuration
     */
    public String getConnectionFactoryConfigName() {
        return connectionFactoryConfigName;
    }

    /*-------------------------------------------------[ KillPrimaryOnSecondaryLaunch ]---------------------------------------------------*/
    /**
     * Attribute kill-primary-on-secondaryLaunch
     */
    public static final String ATTR_KILL_PRIMARY_ON_SECONDARY_LAUNCH = "kill-primary-on-secondaryLaunch";

    private boolean killPrimaryOnSecondaryLaunch = true;

    /**
     * Specifies whether the primary service will be killed on launch of secondary service with same name on the same peer server or not.
     * If a service is bound to a peer server and then another service with the same
     * name tries to bind to the same peer server, then we have two options. If this boolean is set to true then the
     * primary service will be killed and secondary service will take its place. If set to false, the secondary
     * service will kill itself.
     * @return boolean - true if kill primary on secondary launch is enabled, false otherwise
     */
    public boolean isKillPrimaryOnSecondaryLaunch(){
        return killPrimaryOnSecondaryLaunch;
    }

    /**
     * Sets a boolean specifying whether to launch secondary service with same name or continue with the primary service
     * @param killPrimaryOnSecondaryLaunch true if kill primary on secondary launch is to be enabled, false otherwise
     */
    public void setKillPrimaryOnSecondaryLaunch(boolean killPrimaryOnSecondaryLaunch){
        this.killPrimaryOnSecondaryLaunch = killPrimaryOnSecondaryLaunch;
    }

    /*-------------------------------------------------[ DebugMode ]---------------------------------------------------*/
    /**
     * Element debug in event process xml
     */
    public static final String ELEM_DEBUG = "debug";
    /**
     * Attribute enabled
     */
    public static final String ATTR_DEBUG_MODE = "enabled";

    private boolean debugMode = false;

    /**
     * Specifies whether Launch this Service Instance in debug mode
     * @return boolean - true if the service is to be launched in debug mode, false otherwise
     */
    public boolean isDebugMode(){
        return debugMode;
    }

    /**
     * Sets a boolean specifying whether this service instance can be launched in debug mode
     * @param debugMode true if the service is to be launched in debug mode, false otherwise
     */
    public void setDebugMode(boolean debugMode){
        this.debugMode = debugMode;
    }

    /*-------------------------------------------------[ DebugPort ]---------------------------------------------------*/
    /**
     * Attribute port
     */
    public static final String ATTR_DEBUG_PORT = "port";
    private int DEFAULT_DEBUG_PORT = 5000;
    private int debugPort = DEFAULT_DEBUG_PORT;

    /**
     * Returns port number in combination with peer Server machine name is to be used in connecting the external IDE to remotely debug the service.
     * @return int - Debug port number
     */
    public int getDebugPort(){
        return debugPort;
    }

    /**
     * Sets debug port number for this service instance
     * @param debugPort - Debug port number
     */
    public void setDebugPort(int debugPort){
        this.debugPort = debugPort;
    }
    /*--------------------------------------------------------------------------------------------------------------------------*/

    /*-------------------------------------------------[Launch Order Sequence ]-------------------------------------------------*/

    /**
     * Element launchOrder-Sequence in event process xml
     */
    public static final String ELEM_LAUNCH_ORDER_SEQUENCE = "launchOrder-Sequence";

    /**
     * Attribute sequence
     */
    public static final String ATTR_LAUNCH_SEQUENCE = "sequence";
    private int DEFAULT_SEQUENCE = -1;
    private int launchOrderSequence = DEFAULT_SEQUENCE;

    /**
     * Returns service instance launch order sequence number
     * @return int - Launch order sequence
     */
    public int getLaunchOrderSequence() {
        return launchOrderSequence;
    }

    /**
     * Sets the launch Order Sequence of this service
     * @param launchOrderSequence Order sequence number
     */
    public void setLaunchOrderSequence(int launchOrderSequence) {
        this.launchOrderSequence = launchOrderSequence;
    }

    /*-------------------------------------------------[Stop Order Sequence ]------------------------------------------------*/
    /**
     * Element stopOrder-Sequence in event process xml
     */
    public static final String ELEM_STOP_ORDER_SEQUENCE = "stopOrder-Sequence";
    /**
     * Attribute sequence
     */
    public static final String ATTR_STOP_SEQUENCE = "sequence";
    private int stopOrderSequence = DEFAULT_SEQUENCE;

    /**
     * Returns service instance stop order sequence number
     * @return int - Stop order sequence
     */
    public int getStopOrderSequence() {
        return stopOrderSequence;
    }

    /**
     * This method is responsible for setting the launch Order Sequence
     * @param stopOrderSequence Order sequence number
     */
    public void setStopOrderSequence(int stopOrderSequence) {
        this.stopOrderSequence = stopOrderSequence;
    }

    /*-------------------------------------------------[ InputPortInstances ]---------------------------------------------------*/
    /**
     * Element inputport-instances in event process xml
     */
    public static final String ELEM_INPUTPORT_INSTANCES = "inputport-instances";

    private List<InputPortInstance> inputPortInstances = new ArrayList<InputPortInstance>();

    /**
     * Adds specified input <code>port</code> to this service instance
     * @param port Input port to be added
     */
    public void addInputPortInstance(InputPortInstance port){
        inputPortInstances.add(port);
    }

    /**
     * Removes specified input <code>port</code> from this service instance
     * @param port Input port to be removed
     */
    public void removeInputPortInstance(InputPortInstance port){
        inputPortInstances.remove(port);
    }

    /**
     * Returns input ports of this service instance
     * @return List - List of input ports of the service
     */
    public List<InputPortInstance> getInputPortInstances(){
        return inputPortInstances;
    }

    /**
     * Sets the specified list of <code>inputPortInstances</code> as input ports of this service instance
     * @param inputPortInstances List of input ports
     */
    public void setInputPortInstances(List<InputPortInstance> inputPortInstances){
        this.inputPortInstances = inputPortInstances;
    }

    /**
     * Returns port with the specified <code>name</code> from specified list of ports
     * @param name Name of port
     * @param portInstances List of ports
     * @return PortInstance - Object of PortInstance referring to the port
     */
    public PortInstance getPortInstance(String name, List portInstances){
        for (Object portInstance : portInstances) {
            PortInstance port = (PortInstance) portInstance;
            if (name.equals(port.getName()))
                return port;
        }
        return null;
    }

    /**
     * Returns port with the specified name from the list of ports of this service instance
     * @param name Name of the port
     * @return PortInstance - Object of PortInstance referring to the port
     */
    public PortInstance getPortInstance(String name){
        PortInstance port = getPortInstance(name, inputPortInstances);
        return port!=null ? port : getPortInstance(name, outputPortInstances);
    }

    /**
     * Returns input port with the specified name from the list of input ports of this service instance
     * @param name Input port name
     * @return InputPortInstance - Object of InputPortInstance reffering to the input port
     */
    public InputPortInstance getInputPortInstance(String name){
        return (InputPortInstance)getPortInstance(name, inputPortInstances);
    }

    /*-------------------------------------------------[ OutputPortInstances ]---------------------------------------------------*/
    /**
     * Element outputport-instances in event process xml
     */
    public static final String ELEM_OUTPUTPORT_INSTANCES = "outputport-instances";

    private List<OutputPortInstance> outputPortInstances = new ArrayList<OutputPortInstance>();

    /**
     * Adds specifed port <code>port</code> to the out port of this service instance
     * @param port Output port instance
     */
    public void addOutputPortInstance(OutputPortInstance port){
        outputPortInstances.add(port);
    }

    /**
     * Removes specifed port <code>port</code> from output ports of this service instance
     * @param port Output port instance
     */
    public void removeOutputPortInstance(OutputPortInstance port){
        outputPortInstances.remove(port);
    }

    /**
     * Returns a list of output port instances of this service instance
     * @return List - List of output ports
     */
    public List<OutputPortInstance> getOutputPortInstances(){
        return outputPortInstances;
    }

    /**
     * Sets specified list <code>outputPortInstances</code> as output ports of this service instance
     * @param outputPortInstances List of output ports
     */
    public void setOutputPortInstances(List<OutputPortInstance> outputPortInstances){
        this.outputPortInstances = outputPortInstances;
    }

    /**
     * Returns output port with the specified <code>name</code>
     * @param name Name of output port
     * @return OutputPortInstance - An object of OutputPortInstance referring to the output port
     */
    public OutputPortInstance getOutputPortInstance(String name){
        return (OutputPortInstance)getPortInstance(name, outputPortInstances);
    }

    /*-------------------------------------------------[ LogManager ]---------------------------------------------------*/

    private LogManager logManager;

    /**
     * Returns log manager of this service instance
     * @return LogManager - Log manager of this service
     */
    public LogManager getLogManager(){
        return logManager;
    }

    /**
     * Sets specified <code>logManager</code> as the logManager of this service instance
     * @param logManager Log manager of this service
     */
    public void setLogManager(LogManager logManager){
        this.logManager = logManager;
    }

    /*-------------------------------------------------[ LogModules ]---------------------------------------------------*/
    /**
     * Element logmodules in event process xml
     */
    public static final String ELEM_LOGMODULES = "logmodules";

    private List logModules = new ArrayList();

    /**
     * Adds a log module to logmodules of this service instance
     * @param logModule Log module to be added
     */
    public void addLogModule(LogModule logModule){
        logModules.add(logModule);
    }

    /**
     * Removes a log module from logmodules of this service instance
     * @param logModule Log module to be removed
     */
    public void removeLogModule(LogModule logModule){
        logModules.remove(logModule);
    }

    /**
     * Returns a list of log modules of this service instance
     * @return List - List of log modules
     */
    public List getLogModules(){
        return logModules;
    }

    /**
     * Sets the specified list <code>logModules</code> as log modules of this service instance
     * @param logModules List of log modules
     */
    public void setLogModules(List logModules){
        this.logModules = logModules;
    }

    /**
     * Clears log modules of this service instance
     */
    public void clearLogModules(){
        this.logModules.clear();
    }

    /*-------------------------------------------------[ RuntimeArguments ]---------------------------------------------------*/
    /**
     * Element runtime-arguments in event process xml
     */
    public static final String ELEM_RUNTIME_ARGUMNTS = "runtime-arguments";

    private List runtimeArguments = new ArrayList();

    /**
     * Adds specified runtime argument to runtime arguments of this service instance
     * @param runtimeArgument Runtime argument to be added
     */
    public void addRuntimeArgument(RuntimeArgument runtimeArgument){
        runtimeArguments.add(runtimeArgument);
    }

    /**
     * Removes specified runtime argument from runtime arguments of this service instance
     * @param runtimeArgument Runtime argument to be removed
     */
    public void removeRuntimeArgument(RuntimeArgument runtimeArgument){
        runtimeArguments.remove(runtimeArgument);
    }

    /**
     * Returns runtime arguments for this service instance
     * @return List - List of runtime arguments
     */
    public List<RuntimeArgument> getRuntimeArguments(){
        return runtimeArguments;
    }

    /**
     * Sets specified list <code>runtimeArguments</code> as runtime arguments of this service instance
     * @param runtimeArguments List of runtime arguments
     */
    public void setRuntimeArguments(List runtimeArguments){
        this.runtimeArguments = runtimeArguments;
    }

    /**
     * Returns the runtime argument of this service instance having name as <code>name</code>
     * @param name Name of runtime argument
     * @return RuntimeArgument - The runtime argument object for the specified name
     */
    public RuntimeArgument getRuntimeArgument(String name){
        return (RuntimeArgument)DmiObject.findNamedObject(runtimeArguments, name);
    }

    /*-------------------------------------------------[ Runtime Arguments Config Name ]---------------------------------------------------*/
    /**
     * Element runtime-arguments-config-name in event process xml
     */
    public static final String ELEM_RUNTIME_ARGUMENTS_CONFIG_NAME = "runtime-arguments-config-name";

    private String runtimeArgumentsConfigName;

    /**
     * Returns specified runtime argument config name for this service instance
     * @return String - Runtime argument config name used by this service instance
     */
    public String getRuntimeArgumentsConfigName(){
        return runtimeArgumentsConfigName;
    }

    /**
     * Sets specified parameter as runtime arguments config name for this service instance
     * @param runtimeArgumentsConfigName Runtime argument config name to be st
     */
    public void setRuntimeArgumentsConfigName(String runtimeArgumentsConfigName){
        this.runtimeArgumentsConfigName = runtimeArgumentsConfigName;
    }

    /*-------------------------------------------------[ ServiceRefs ]---------------------------------------------------*/
    /**
     * Element servicerefs in event process xml
     */
    public static final String ELEM_SERVICEREFS = "servicerefs";

    private List<ServiceRef> serviceRefs = new ArrayList<>();

    /**
     * Adds specified service Reference <code>serviceRef</code> to service references of this service instance
     * @param serviceRef Service reference to be added
     */
    public void addServiceRef(ServiceRef serviceRef){
        serviceRefs.add(serviceRef);
    }

    /**
     * Removes specified service Reference <code>serviceRef</code> from service references of this service instance
     * @param serviceRef Service Reference to be removed
     */
    public void removeServiceRef(ServiceRef serviceRef){
        serviceRefs.remove(serviceRef);
    }

    /**
     * Returns service references of this service instance
     * @return List - List of service references
     */
    public List<ServiceRef> getServiceRefs(){
        return serviceRefs;
    }

    /**
     * Sets specified list <code>serviceRefs</code> as service references of this service instances
     * @param serviceRefs List of service References
     */
    public void setServiceRefs(List<ServiceRef> serviceRefs){
        this.serviceRefs = serviceRefs;
    }

    /*-------------------------------------------------[ Configuration Properties ]---------------------------------------------------*/
    /**
     * Element property
     */
    public static final String ELEM_PROPERTY = "property";
    /**
     * Attribute encrypt
     */
    public static final String ATTR_ENCRYPT = "encrypt";
    /**
     * Attribute type
     */
    public static final String ATTR_PROPERTYTYPE = "type";
    /**
     * Attribute configuration type
     */
    public static final String ATTR_PROPERTY_CONFIGURATION_TYPE = "configurationType";
    /**
     * Element instance-properties
     */
    public static final String ELEM_INSTANCE_PROPERTIES = "instance-properties";
    /**
     * Element configuration-properties
     */
    public static final String ELEM_CONFIGURATION_PROPERTIES = "configuration-properties";
    private List<ManageableProperty> manageableProperties = new ArrayList<ManageableProperty>();

    /**
     * Returns a list of manageable properties of this service instance
     * @return List - List of manageable properties
     */
    public List<ManageableProperty> getManageableProperties() {
        return manageableProperties;
    }

    /**
     * Sets manageable properties of this service instance
     * @param manageableProperties List of manageable properties
     */
    public void setManageableProperties(List<ManageableProperty> manageableProperties) {
        this.manageableProperties = manageableProperties;
    }

    /**
     * Adds a manageable property to the list of manageable properties of this service instance
     * @param property Manageable property
     */
    public void addManageableProperty(ManageableProperty property){
        if(manageableProperties.contains(property)){
            throw new IllegalArgumentException();
        }
        manageableProperties.add(property);

        String configurationType = property.getConfigurationType();
        if (configurationType != null && configurationType.length() != 0) {
            try {
                //Check for validity of configuration Type specified
                ObjectCategory.getObjectCategory(configurationType);

                String configurationID = property.getName();
                String configurationName = property.getValue();

                if (namedConfigurations == null)
                    namedConfigurations = new Hashtable<String, Hashtable<String, String>>();

                addNamedConfiguration(configurationName, configurationType, configurationID);
            } catch (IllegalArgumentException e) {
                //Ignore
            }
        }
    }

    /**
     * Removes specified property from the list of manageable properties
     * @param property Manageable property to be removed
     */
    public void removeManageableProperty(ManageableProperty property){
        if(manageableProperties.contains(property))
            manageableProperties.remove(property);
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <inst name="string">
     *      <service guid="string" version="float" version-locked="boolean"?/>
     *      <short-description>string</short-description>?
     *      <long-description>string</long-description>?
     *      <configuration>string</configuration>?
     *      <resource-properties>
     *          <resource-property name="string">string</resource-property>+
     *      </resource-properties>?
     *      <deployment nodes="cvs" cache="boolean"/>
     *      <execution buffer-limit="long"?>
     *          <debug enabled="boolean" port="int"/>
     *          <launchOrder-Sequence sequence="int"/>
     *          <stopOrder_Sequence sequence="int"/>
     *          <launch type="int"? first-available-node="boolean"? kill-primary-on-secondary-launch="boolean"?/>?
     *      </execution>
     *      <inputport-instances>
     *          ...inputport-instance+...
     *      </inputport-instances>?
     *      <outputport-instances>
     *          ...outputport-instance+...
     *      </outputport-instances>?
     *      ...logmanager?...
     *      <logmodules>
     *          ...logmodule+...
     *      <runtime-arguments>
     *          ...runtime-argument+...
     *      </runtime-arguments>?
     *      <servicerefs>
     *          ...serviceref+...
     *      </servicerefs>?
     *      <schemareferences>
     *          <schema ....... />?
     * </inst>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        MapThreadLocale.getInstance().getMap().put(ELEM_SERVICE_INSTANCE, name);

        writer.writeStartElement(ELEM_SERVICE_INSTANCE);
        {
            writer.writeAttribute(ATTR_NAME, name);
            writer.writeStartElement(ELEM_SERVICE);
            {
                writer.writeAttribute(ATTR_GUID, guid);
                writer.writeAttribute(ATTR_VERSION, String.valueOf(version));
                if(!versionLocked)
                    writer.writeAttribute(ATTR_VERSION_LOCKED, String.valueOf(versionLocked));
            }
            writer.writeEndElement();

            writeElement(writer, ELEM_SHORT_DESCRIPTION, shortDescription);
            writeElement(writer, ELEM_LONG_DESCRIPTION, longDescription);

            if(configFile != null){
                writer.writeStartElement(ELEM_CONFIG_FILE);
                {
                    if(partiallyConfigured)
                        writer.writeAttribute(ATTR_PARTIALLY_CONFIGURED, String.valueOf(partiallyConfigured));
                    writer.writeAttribute(ATTR_NAME, configFile);
                }
                writer.writeEndElement();
            }

            if(writeCDataSections)
                writeCDATAElement(writer, ELEM_CONFIGURATION, configuration);

            if(resourceProperties != null && resourceProperties.size()>0){
                writer.writeStartElement(ELEM_RESOURCE_PROPERTIES);
                {
                    for(Iterator iter = resourceProperties.entrySet().iterator(); iter.hasNext();){
                        Map.Entry entry = (Map.Entry)iter.next();
                        writer.writeStartElement(ELEM_RESOURCE_PROPERTY);
                        writer.writeAttribute(ATTR_RESOURCE_PROPERTY_NAME, (String)entry.getKey());
                        writer.writeCharacters((String)entry.getValue());
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }

            if(namedConfigurations != null && namedConfigurations.size() > 0){
                writer.writeStartElement(ELEM_NAMED_CONFIGURATIONS);
                {
                    for(String configurationID : namedConfigurations.keySet()){
                        Hashtable<String, String> configurationSet = namedConfigurations.get(configurationID);

                        for (String configurationName : configurationSet.keySet()) {
                            writer.writeStartElement(ELEM_NAMED_CONFIGURATION);
                            writer.writeAttribute(ATTR_NAME, configurationName);
                            writer.writeAttribute(ATTR_NAMED_CONFIGURATION_TYPE, configurationSet.get(configurationName));

                            if (configurationID != null && !configurationID.equals(NO_CONFIGURATION_ID))
                                writer.writeAttribute(ATTR_NAMED_CONFIGURATION_ID, configurationID);

                            writer.writeEndElement();
                        }
                    }
                }
                writer.writeEndElement();
            }

            //Write managable properties
            if(writeCDataSections){
                writer.writeStartElement(ELEM_CONFIGURATION_PROPERTIES);
                {
                    for (ManageableProperty property : getManageableProperties()) {
                        writer.writeStartElement(ELEM_PROPERTY);
                        {
                            writer.writeAttribute(ATTR_NAME, property.getName());
                            boolean isEncrypted = property.isEncrypted();
                            writer.writeAttribute(ATTR_ENCRYPT, String.valueOf(isEncrypted));

                            String type = property.getPropertyType();
                            if(type != null && type.length() != 0)
                                writer.writeAttribute(ATTR_PROPERTYTYPE, type);

                            String configurationType = property.getConfigurationType();
                            if(configurationType != null && configurationType.length() != 0)
                                writer.writeAttribute(ATTR_PROPERTY_CONFIGURATION_TYPE, configurationType);
                            
                            String propValue = property.getValue();
                            String encryptedValue = null;
                            if(isEncrypted){
                                if(propValue != null && propValue.trim().length() != 0) {
                                    try {
                                        encryptedValue = StringEncrypter.getDefaultInstance().encrypt(propValue);
                                    } catch (StringEncrypter.EncryptionException e) {
                                        //Ignore. Use original password in this case.
                                    }
                                }
                            }
                            writer.writeCharacters(encryptedValue != null ? encryptedValue : (propValue != null ? propValue : ""));
                        }
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }

            writer.writeStartElement(ELEM_DEPLOYMENT);
            {
                writer.writeAttribute(ATTR_NODES, StringUtil.toString(nodes, ",", true));
                if (!componentCached)
                    writer.writeAttribute(ATTR_COMPONENT_CACHED, String.valueOf(componentCached));
            }
            writer.writeEndElement();

            writer.writeStartElement(ELEM_EXECUTION);
            {
                if(bufferLimit != DEFAULT_BUFFER_LIMIT)
                    writer.writeAttribute(ATTR_BUFFER_LIMIT, String.valueOf(bufferLimit));
                writer.writeStartElement(ELEM_DEBUG);
                {
                    writer.writeAttribute(ATTR_DEBUG_MODE, String.valueOf(debugMode));
                    writer.writeAttribute(ATTR_DEBUG_PORT, String.valueOf(debugPort));
                }
                writer.writeEndElement();

                writer.writeStartElement(ELEM_LAUNCH_ORDER_SEQUENCE);
                {
                    writer.writeAttribute(ATTR_LAUNCH_SEQUENCE, String.valueOf(launchOrderSequence));
                }
                writer.writeEndElement();

                writer.writeStartElement(ELEM_STOP_ORDER_SEQUENCE);
                {
                    writer.writeAttribute(ATTR_STOP_SEQUENCE, String.valueOf(stopOrderSequence));
                }
                writer.writeEndElement();

                if(launchType!=Execution.LAUNCH_TYPE_SEPARATE_PROCESS || !preferLaunchOnFirstAvailableNode || !killPrimaryOnSecondaryLaunch){
                    writer.writeStartElement(ELEM_LAUNCH);
                    {
                        if(launchType!=Execution.LAUNCH_TYPE_SEPARATE_PROCESS)
                            writer.writeAttribute(ATTR_LAUNCH_TYPE, String.valueOf(launchType));
                        if(!preferLaunchOnFirstAvailableNode)
                            writer.writeAttribute(ATTR_PREFER_LAUNCH_ON_FIRST_AVAILABLE_NODE, String.valueOf(preferLaunchOnFirstAvailableNode));
                        if(!killPrimaryOnSecondaryLaunch)
                            writer.writeAttribute(ATTR_KILL_PRIMARY_ON_SECONDARY_LAUNCH, String.valueOf(killPrimaryOnSecondaryLaunch));
                    }
                    writer.writeEndElement();
                }

                if (connectionFactoryConfigName != null) {
                    writer.writeStartElement(ELEM_CONNECTION_FACTORY_CONFIG_NAME);
                    {
                        writer.writeAttribute(ATTR_NAME, connectionFactoryConfigName);
                    }
                    writer.writeEndElement();
                } else if(connectionFactoryProperties != null && connectionFactoryProperties.size() > 0){
                    writer.writeStartElement(ELEM_CONNECTION_FACTORY_PROPERTIES);
                    {
                        for(String propertyName : connectionFactoryProperties.keySet()){
                            String propertyValue = connectionFactoryProperties.get(propertyName);
                            writer.writeStartElement(ELEM_CONNECTION_FACTORY_PROPERTY);
                            {
                                writer.writeAttribute(ATTR_NAME, propertyName);
                                writer.writeAttribute(ATTR_VALUE, propertyValue);
                            }
                            writer.writeEndElement();
                        }
                    }
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();

            writeCollection(writer, inputPortInstances, ELEM_INPUTPORT_INSTANCES, writeCDataSections);
            writeCollection(writer, outputPortInstances, ELEM_OUTPUTPORT_INSTANCES, writeCDataSections);
            if(logManager!=null)
                logManager.toJXMLString(writer);
            writeCollection(writer, logModules, ELEM_LOGMODULES);

            if (runtimeArgumentsConfigName != null) {
                writer.writeStartElement(ELEM_RUNTIME_ARGUMENTS_CONFIG_NAME);
                {
                    writer.writeAttribute(ATTR_NAME, runtimeArgumentsConfigName);
                }
                writer.writeEndElement();
            } else {
                writeCollection(writer, runtimeArguments, ELEM_RUNTIME_ARGUMNTS);
            }

            writeCollection(writer, serviceRefs, ELEM_SERVICEREFS);
            if(schemaReferences != null && schemaReferences.size() > 0){
                writeCollection(writer,schemaReferences, ELEM_SCHEMA_REFERENCES);
            }
        }
        writer.writeEndElement();
            }

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        toJXMLString(writer, true);
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_SERVICE_INSTANCE)){
            name = cursor.getAttributeValue(null,ATTR_NAME);
            MapThreadLocale.getInstance().getMap().put(ELEM_SERVICE_INSTANCE, name);

            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_SERVICE.equals(elemName)){
                    guid = cursor.getAttributeValue(null,ATTR_GUID);
                    version = Float.parseFloat(cursor.getAttributeValue(null, ATTR_VERSION));
                    versionLocked = getBooleanAttribute(cursor, ATTR_VERSION_LOCKED, true);
                }else if(ELEM_SHORT_DESCRIPTION.equals(elemName))
                    shortDescription = cursor.getText();
                else if(ELEM_LONG_DESCRIPTION.equals(elemName))
                    longDescription = cursor.getText();
                else if(ELEM_CONFIGURATION.equals(elemName))
                    configuration = cursor.getCData();
                else if(ELEM_CONFIG_FILE.equals(elemName)){
                    partiallyConfigured= Boolean.parseBoolean(cursor.getAttributeValue(null,ATTR_PARTIALLY_CONFIGURED));
                    configFile = cursor.getAttributeValue(null,ATTR_NAME);
                }else if(ELEM_RESOURCE_PROPERTIES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    resourceProperties = new Properties();
                    while(cursor.nextElement()){
                        if(ELEM_RESOURCE_PROPERTY.equals(cursor.getLocalName())){
                            String resourcePropertyName = cursor.getAttributeValue(null, ATTR_RESOURCE_PROPERTY_NAME);
                            String resourcePropertyValue = cursor.getText();
                            resourceProperties.setProperty(resourcePropertyName, resourcePropertyValue);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_NAMED_CONFIGURATIONS.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    namedConfigurations = new Hashtable<String, Hashtable<String, String>>();
                    while(cursor.nextElement()){
                        if(ELEM_NAMED_CONFIGURATION.equals(cursor.getLocalName())){
                            String configurationName = cursor.getAttributeValue(null, ATTR_NAME);
                            String configurationType = getStringAttribute(cursor, ATTR_NAMED_CONFIGURATION_TYPE, ConfigurationRepoConstants.COMPONENT);
                            String configurationID = getStringAttribute(cursor, ATTR_NAMED_CONFIGURATION_ID, null);

                            addNamedConfiguration(configurationName, configurationType, configurationID);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_DEPLOYMENT.equals(elemName)){
                    nodes = StringUtil.getTokens(cursor.getAttributeValue(null, ATTR_NODES), ",", true);
                    componentCached = getBooleanAttribute(cursor, ATTR_COMPONENT_CACHED, true);
                }else if(elemName.equalsIgnoreCase(ELEM_EXECUTION)){
                    bufferLimit = getLongAttribute(cursor, ATTR_BUFFER_LIMIT, DEFAULT_BUFFER_LIMIT);
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(ELEM_DEBUG.equals(cursor.getLocalName())){
                            debugMode = Boolean.valueOf(cursor.getAttributeValue(null, ATTR_DEBUG_MODE));
                            debugPort = getIntegerAttribute(cursor,ATTR_DEBUG_PORT,DEFAULT_DEBUG_PORT);
                        } else if (ELEM_LAUNCH_ORDER_SEQUENCE.equals(cursor.getLocalName())){
                            launchOrderSequence = getIntegerAttribute(cursor, ATTR_LAUNCH_SEQUENCE, DEFAULT_SEQUENCE);
                        } else if (ELEM_STOP_ORDER_SEQUENCE.equals(cursor.getLocalName())){
                            stopOrderSequence = getIntegerAttribute(cursor, ATTR_STOP_SEQUENCE, DEFAULT_SEQUENCE);
                        }else if(ELEM_LAUNCH.equals(cursor.getLocalName())){
                            launchType = getIntegerAttribute(cursor, ATTR_LAUNCH_TYPE, Execution.LAUNCH_TYPE_SEPARATE_PROCESS);
                            preferLaunchOnFirstAvailableNode = getBooleanAttribute(cursor, ATTR_PREFER_LAUNCH_ON_FIRST_AVAILABLE_NODE, true);
                            killPrimaryOnSecondaryLaunch = getBooleanAttribute(cursor, ATTR_KILL_PRIMARY_ON_SECONDARY_LAUNCH, true);
                        } else if(ELEM_CONNECTION_FACTORY_PROPERTIES.equals(cursor.getLocalName())){
                            populateConnectionFactoryConfigurations(cursor);
                        } else if(ELEM_CONNECTION_FACTORY_CONFIG_NAME.equals(cursor.getLocalName())) {
                            connectionFactoryConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_INPUTPORT_INSTANCES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(InputPortInstance.ELEM_INPUT_PORT_INSTANCE.equals(cursor.getLocalName())){
                            InputPortInstance inputPortInst = new InputPortInstance();
                            inputPortInst.setFieldValues(cursor);
                            inputPortInstances.add(inputPortInst);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_OUTPUTPORT_INSTANCES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(OutputPortInstance.ELEM_OUTPUT_PORT_INSTANCE.equals(cursor.getLocalName())){
                            OutputPortInstance outputPortInst = new OutputPortInstance();
                            outputPortInst.setFieldValues(cursor);
                            outputPortInstances.add(outputPortInst);
                        }
                    }
                    cursor.resetCursor();
                }else if(LogManager.ELEM_LOG_MANAGER.equals(elemName)){
                    logManager = new LogManager();
                    logManager.setFieldValues(cursor);
                }else if(ELEM_LOGMODULES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(LogModule.ELEM_LOGMODULE.equals(cursor.getLocalName())){
                            LogModule logModule = new LogModule();
                            logModule.setFieldValues(cursor);
                            logModules.add(logModule);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_RUNTIME_ARGUMNTS.equals(elemName)){
                    populateRuntimeArgumentsConfigurations(cursor);
                }else if(ELEM_RUNTIME_ARGUMENTS_CONFIG_NAME.equals(elemName)){
                    runtimeArgumentsConfigName = cursor.getAttributeValue(null, ATTR_NAME);
                }else if(ELEM_SERVICEREFS.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(ServiceRef.ELEM_SERVICEREF.equals(cursor.getLocalName())){
                            ServiceRef serviceRef = new ServiceRef();
                            serviceRef.setFieldValues(cursor);
                            serviceRefs.add(serviceRef);
                        }
                    }
                    cursor.resetCursor();
                }else if (ELEM_CONFIGURATION_PROPERTIES.equals(elemName)) {
                    cursor.markCursor(ELEM_CONFIGURATION_PROPERTIES);
                    while(cursor.nextElement()){
                        String elementName = cursor.getLocalName();
                        if(ELEM_PROPERTY.equalsIgnoreCase(elementName)){
                            String propertyName = DmiObject.getStringAttribute(cursor, ATTR_NAME, null);
                            boolean isEncrypted = DmiObject.getBooleanAttribute(cursor, ATTR_ENCRYPT, false);
                            String propertyType = DmiObject.getStringAttribute(cursor, ATTR_PROPERTYTYPE, null);
                            String configurationType = DmiObject.getStringAttribute(cursor, ATTR_PROPERTY_CONFIGURATION_TYPE, null);
                            String readPropertyValue = cursor.getText();

                            String originalPropValue = null;
                            if (isEncrypted) {
                                if(readPropertyValue != null && readPropertyValue.trim().length() != 0) {
                                    try {
                                        originalPropValue = StringEncrypter.getDefaultInstance().decrypt(readPropertyValue);
                                    } catch (StringEncrypter.EncryptionException e) {
                                        //Ignore. Use password read from file in this case.
                                    }
                                }
                            }

                            ManageableProperty property = new ManageableProperty(propertyName, originalPropValue != null ? originalPropValue : readPropertyValue, isEncrypted, propertyType, configurationType);
                            addManageableProperty(property);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_SCHEMA_REFERENCES.equals(elemName)){
                    populateSchemaReferences(cursor);
                }
            }
        }
    }

    /**
     * Populates the connection factory configurations
     * @param cursor
     * @throws XMLStreamException
     */
    public void populateConnectionFactoryConfigurations(FioranoStaxParser cursor) throws XMLStreamException {
        if (cursor.markCursor(ELEM_CONNECTION_FACTORY_PROPERTIES)) {
            while(cursor.nextElement()){
                if(ELEM_CONNECTION_FACTORY_PROPERTY.equals(cursor.getLocalName())){
                    String propertyName = getStringAttribute(cursor, ATTR_NAME, null);
                    String propertyValue = getStringAttribute(cursor, ATTR_VALUE, null);
                    if(propertyName != null && propertyValue != null)
                        connectionFactoryProperties.put(propertyName, propertyValue);
                }
            }
            cursor.resetCursor();
        }
    }

    /**
     * Populates the runtime argument configurations
     * @param cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateRuntimeArgumentsConfigurations(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if (cursor.markCursor(ELEM_RUNTIME_ARGUMNTS)) {
            while(cursor.nextElement()){
                if(RuntimeArgument.ELEM_RUNTIME_ARGUMENT.equals(cursor.getLocalName())){
                    RuntimeArgument runtimeArgument = new RuntimeArgument();
                    runtimeArgument.setFieldValues(cursor);
                    runtimeArguments.add(runtimeArgument);
                }
            }
            cursor.resetCursor();
        }
    }

    /**
     * Populates the list of schema references
     * @param cursor
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateSchemaReferences(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if(cursor.markCursor(ServiceInstance.ELEM_SCHEMA_REFERENCES)){
            while (cursor.nextElement()){
                if (SchemaReference.ELEM_SCHEMA_REFERENCE.equals(cursor.getLocalName())){
                    SchemaReference schemaReference = new SchemaReference();
                    schemaReference.setFieldValues(cursor);
                    schemaReferences.add(schemaReference);
                }
            }
            cursor.resetCursor();
        }
    }

    /**
     * Reads manageable properties of this service
     * @param cursor
     * @throws XMLStreamException
     */
    public void readManageableProperties(FioranoStaxParser cursor) throws XMLStreamException {
        //1. The cursor represents the instance node
        //look for the instance propertes and the manageable properties
        //for instace properteis populatete the deployment node
        //for manageable properties populate the properteis object

        cursor.markCursor(ELEM_INSTANCE);

        while (cursor.nextElement()) {
            String elemName = cursor.getLocalName();
            if (ELEM_INSTANCE_PROPERTIES.equals(elemName)) {
                cursor.markCursor(ELEM_INSTANCE_PROPERTIES);

                while (cursor.nextElement()) {
                        elemName = cursor.getLocalName();
                    if (ELEM_DEPLOYMENT.equals(elemName)) {
                        nodes = StringUtil.getTokens(cursor.getAttributeValue(null, ATTR_NODES), ",", true);
                        componentCached = getBooleanAttribute(cursor, ATTR_COMPONENT_CACHED, true);
                    }
                }

                cursor.resetCursor();

            } else if (ELEM_CONFIGURATION_PROPERTIES.equals(elemName)) {
                cursor.markCursor(ELEM_CONFIGURATION_PROPERTIES);

                while(cursor.nextElement()){
                    String elementName = cursor.getLocalName();
                    if(ELEM_PROPERTY.equalsIgnoreCase(elementName)){
                        String propertyName = DmiObject.getStringAttribute(cursor, ATTR_NAME, null);
                        boolean isEncrypted = DmiObject.getBooleanAttribute(cursor, ATTR_ENCRYPT, false);
                        String propertyType = DmiObject.getStringAttribute(cursor, ATTR_PROPERTYTYPE, null);
                        String configurationType = DmiObject.getStringAttribute(cursor, ATTR_PROPERTY_CONFIGURATION_TYPE,null);
                        String readPropertyValue = cursor.getText();

                        String originalPropValue = null;
                        if (isEncrypted) {
                            if(readPropertyValue != null && readPropertyValue.trim().length() != 0) {
                                try {
                                    originalPropValue = StringEncrypter.getDefaultInstance().decrypt(readPropertyValue);
                                } catch (StringEncrypter.EncryptionException e) {
                                    //Ignore. Use password read from file in this case.
                                }
                            }
                        }

                        ManageableProperty property = new ManageableProperty(propertyName, originalPropValue != null ? originalPropValue : readPropertyValue, isEncrypted, propertyType, configurationType);
                        addManageableProperty(property);
                    }
                }
                cursor.resetCursor();
            }
        }
        cursor.resetCursor();
    }

    /**
     * Writes to file the manageable properties of this service
     * @param writer
     * @throws XMLStreamException
     */
    public void writeManageableProperties(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(ELEM_TARGET, ELEM_INSTANCE,Namespaces.URI_TARGET);
        {
            writer.writeAttribute(ATTR_NAME, name);
            writer.writeStartElement(ELEM_TARGET, ELEM_INSTANCE_PROPERTIES,Namespaces.URI_TARGET);
            {
                writer.writeStartElement(ELEM_TARGET,ELEM_DEPLOYMENT,Namespaces.URI_TARGET);
                {
                    writer.writeAttribute(ATTR_NODES, StringUtil.toString(nodes, ",", true));

                    writer.writeAttribute(ATTR_COMPONENT_CACHED, String.valueOf(componentCached));
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeStartElement(ELEM_TARGET,ELEM_CONFIGURATION_PROPERTIES, Namespaces.URI_TARGET);
            {
                for (ManageableProperty property : getManageableProperties()) {
                    writer.writeStartElement(ELEM_TARGET, ELEM_PROPERTY,Namespaces.URI_TARGET);
                    {
                        writer.writeAttribute(ATTR_NAME, property.getName());
                        boolean isEncrypted = property.isEncrypted();
                        writer.writeAttribute(ATTR_ENCRYPT, String.valueOf(isEncrypted));

                        String type = property.getPropertyType();
                        if(type != null && type.length() != 0)
                            writer.writeAttribute(ATTR_PROPERTYTYPE, type);

                        String configurationType = property.getConfigurationType();
                        if(configurationType != null && configurationType.length() != 0)
                            writer.writeAttribute(ATTR_PROPERTY_CONFIGURATION_TYPE, configurationType);

                        String propValue = property.getValue();
                        String encryptedValue = null;
                        if(isEncrypted){
                            if(propValue != null && propValue.trim().length() != 0) {
                                try {
                                    encryptedValue = StringEncrypter.getDefaultInstance().encrypt(propValue);
                                } catch (StringEncrypter.EncryptionException e) {
                                    //Ignore. Use original password in this case.
                                }
                            }
                        }
                        writer.writeCharacters(encryptedValue != null ? encryptedValue : (propValue != null ? propValue : ""));
                    }
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(com.fiorano.openesb.application.aps.ServiceInstance that){
        name = that.getServiceInstName();

        guid = that.getServiceGUID();
        version = Float.parseFloat(that.getVersion());
        versionLocked = that.isVersionLocked();

        shortDescription = that.getShortDescription();
        longDescription = that.getLongDescription();
        configuration = that.getRuntimeArgs()!=null ? that.getRuntimeArgs().getUserDefinedPropertySheet() : null;

        // nodes
        Iterator iter = that.getNodeInfo().entrySet().iterator();
        TreeMap nodesMap = new TreeMap();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String nodeName = (String)entry.getKey();
            int nodeLevel = Integer.parseInt((String)entry.getValue());
            nodesMap.put(new Integer(nodeLevel), nodeName);
        }
        ArrayList nodeList = new ArrayList(nodesMap.size());
        iter = nodesMap.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            int nodeLevel = ((Integer)entry.getKey()).intValue();
            String nodeName = (String)entry.getValue();
            CollectionUtil.set(nodeList, nodeLevel, nodeName);
        }
        nodes = (String[])nodeList.toArray(new String[0]);

        componentCached = !that.isServiceNoCache();

        debugMode = that.isDebugMode();
        debugPort = that.getDebugPort();

        if(that.isInMemoryLaunch())
            launchType = Execution.LAUNCH_TYPE_IN_MEMORY;
        else if(that.isManualLaunch())
            launchType = Execution.LAUNCH_TYPE_MANUAL;
        else
            launchType = Execution.LAUNCH_TYPE_SEPARATE_PROCESS;
        preferLaunchOnFirstAvailableNode = that.isLaunchOnHigherLevelNodePreferred();
        killPrimaryOnSecondaryLaunch = that.isKillPrimaryOnSecondaryLaunch();

        Enumeration enumer = that.getPortInstDescriptor().getInPorts();
        while(enumer.hasMoreElements()){
            InputPortInstance port = new InputPortInstance();
            port.convert((InPortInst)enumer.nextElement());
            inputPortInstances.add(port);
        }

        enumer = that.getPortInstDescriptor().getOutPorts();
        while(enumer.hasMoreElements()){
            OutputPortInstance port = new OutputPortInstance();
            port.convert((OutPortInst)enumer.nextElement());
            outputPortInstances.add(port);
        }

        if(!StringUtil.isEmpty(that.getLogManager())){
            logManager = new LogManager();
            logManager.convert(that);
        }

        if(that.getLogModules()!=null){
            enumer = that.getLogModules().getLogModules();
            while(enumer.hasMoreElements()){
                LogModule module = new LogModule();
                module.convert((ApsLogModule)enumer.nextElement());
                logModules.add(module);
            }
        }

        if(that.getRuntimeArgs()!=null){
            enumer = that.getRuntimeArgs().getRuntimeArgs();
            while(enumer.hasMoreElements()){
                RuntimeArgument arg = new RuntimeArgument();
                arg.convert((Argument)enumer.nextElement());
                runtimeArguments.add(arg);
            }
        }

        enumer = that.getRuntimeDependencies();
        while(enumer.hasMoreElements()){
            ServiceRef serviceRef = new ServiceRef();
            serviceRef.convert((RuntimeDependency)enumer.nextElement());
            serviceRefs.add(serviceRef);
        }

        //workflow end
        if(that.getStatusTracking()!=null){
            ArrayList names = CollectionUtil.toCollection(that.getStatusTracking().getPortNames());
            iter = that.getEndStates().iterator();
            while(iter.hasNext()){
                EndState endState = (EndState)iter.next();
                names.remove(endState.getStateID());
                getPortInstance(endState.getStateID()).setWorkflow(PortInstance.WORKFLOW_END);
            }
            iter = names.iterator();
            while(iter.hasNext())
                getPortInstance((String)iter.next()).setWorkflow(PortInstance.WORKFLOW_ITEM);
        }
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        guid = null;
        version = 0;
        name = null;

        shortDescription = null;
        longDescription = null;

        nodes = new String[0];
        versionLocked = true;

        launchType = Execution.LAUNCH_TYPE_SEPARATE_PROCESS;
        componentCached = true;
        configuration = null;

        configFile = null;

        resourceProperties = null;
        namedConfigurations = null;
        connectionFactoryProperties.clear();
        connectionFactoryConfigName = null;

        bufferLimit = DEFAULT_BUFFER_LIMIT;//524288L;
        preferLaunchOnFirstAvailableNode = true;
        killPrimaryOnSecondaryLaunch = true;

        debugMode = false;
        debugPort = DEFAULT_DEBUG_PORT;//5000;

        launchOrderSequence = DEFAULT_SEQUENCE;

        runtimeArgumentsConfigName = null;

        inputPortInstances.clear();
        outputPortInstances.clear();
        logManager = null;
        logModules.clear();
        runtimeArguments.clear();
        serviceRefs.clear();
        manageableProperties.clear();
    }

    /**
     * Validates the Service DMI. Check whther all the manditory fileds are set.
     *
     * The Possible Error Mesages:
     * @bundle SERVICE_NAME_UNSPECIFIED=Name is not specified
     * @bundle SERVICE_GUID_UNSPECIFIED=GUID is not specified
     * @bundle INVALID_SERVICE_VERSION_SPECIFIED=Version Number must be greater than zero
     * @bundle NO_NODES_SPECIFIED=The node on which the Service should run is not specified
     * @bundle INVALID_BUFFER_LIMIT_SPECIFIED=Buffer Limit must be greater than zero
     * @bundle INVALID_DEBUG_PORT_SPECIFIED=Dubug Port must be greater than zero
     */
    public void validate() throws FioranoException{
        validateServiceName();
        if(StringUtil.isEmpty(guid))
            throw new FioranoException("SERVICE_GUID_UNSPECIFIED");
        if(version<0)
            throw new FioranoException("INVALID_SERVICE_VERSION_SPECIFIED");
        //don't need to check for the node name while saving or creating an application. Fix for BUG::11906
        //if(nodes.length==0)
        //    throw new FioranoException(I18N.getMessage(ServiceInstance.class, "NO_NODES_SPECIFIED"));
        if(bufferLimit<0)
            throw new FioranoException("INVALID_BUFFER_LIMIT_SPECIFIED");
        if(debugPort<0)
            throw new FioranoException("INVALID_DEBUG_PORT_SPECIFIED");

        validateList(inputPortInstances);
        validateList(outputPortInstances);
        if(logManager != null)
            logManager.validate();
        validateList(logModules);
        validateList(runtimeArguments);
        validateList(serviceRefs);
    }
    /**
     * Validates name given a ServiceInstance
     * Possible error messages
     * @bundle SERVICE_NAME_UNSPECIFIED= ServiceInstance Name is not specified
     * @bundle INVALID_SERVICE_NAME= ServiceInstance Name cannot have these characters . ~ ! @ # $ % & ^ ( ) [ ] { } + - * '(single quote) ,(comma) ;
     * @bundle INVALID_SERVICE_NAME_START= ServiceInstance name cannot start with an underscore (_)
     * @bundle INVALID_SERVICE_NAME_DOUBLEUNDER= ServiceInstance name cannot contain a double underscore (__)
     * @throws FioranoException If service instance name is not valid
     */
    private void validateServiceName() throws FioranoException{
        if (StringUtil.isEmpty(name))
            throw new FioranoException("SERVICE_NAME_UNSPECIFIED");
        else if (DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(name).find())
            throw new FioranoException("INVALID_SERVICE_NAME");

        if (name.startsWith("_"))
            throw new FioranoException("INVALID_SERVICE_NAME_START");
        if (name.contains("__"))
            throw new FioranoException("INVALID_SERVICE_NAME_DOUBLEUNDER");
    }

    /**
     * Returns key
     * @return String - Key of the service instance
     */
    public String getKey(){
        return getName();
    }
}
