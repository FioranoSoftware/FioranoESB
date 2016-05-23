/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ServiceInstance extends DmiObject
{
    /**
     *  Description of the Field
     */
    public final static String PARAM_SERVICE_NO_CACHE = "ServiceNoCache";

    boolean         m_isManualLaunch;
    boolean         m_isStateful;
    boolean         m_isDelayedLaunch;
    String          m_delayedPortName;
    boolean         m_isTransacted;
    boolean         m_isErrorHandlingEnabled;
    boolean         m_isVersionLocked;
    boolean         m_isInMemoryLaunch;
    boolean         m_isTransportLPC = true;
    boolean         m_isEndOfWorkflow;
    boolean         m_bPreferLaunchOnHigherLevelNode;
    boolean         m_bIsDurableSubscription;
    boolean         m_bIsDurableConnection = true;
    boolean         m_bKillPrimaryOnSecondaryLaunch;
    boolean         m_bIsDebugMode;
    int             m_iDebugPort;
    long            m_dBufferSizePerPort;
    int             m_maxRetries;
    String          m_version;
    String          m_servInstName;
    String          m_servGUID;
    String          m_longDescription;
    String          m_shortDescription;
    RuntimeArgs     m_runtimeArgs;
    PortInstDescriptor m_portInstDescriptor;
    Hashtable       m_nodes = new Hashtable();
    Vector          m_params = new Vector();
    StatusTracking  m_statusTracking;
    Vector          m_vecEndStates = new Vector();
    Monitor         m_monitor;
    LogModules      m_logModules;

    // Logging specific parameters
    String          m_logManager;
    Vector          m_logParams;

    //  Event specific parameters.
    String          m_eventDeliveryMode;
    long            m_eventExpiryTime;
    int             m_eventHandler;

    // Fixed N_3366 runtime dependencies
    Vector          m_runtimeDependencies = new Vector();

    String          m_profile;

    /**
     *  This is the constructor of the <code>ServiceInstance</code> class.
     *
     * @since Tifosi2.0
     */
    public ServiceInstance()
    {
        m_isStateful = true;
        m_isVersionLocked = false;
        m_bPreferLaunchOnHigherLevelNode = false;
        m_bIsDurableSubscription = false;
        m_bIsDurableConnection = false;
        m_bKillPrimaryOnSecondaryLaunch = true;
        m_bIsDebugMode = false;
        m_iDebugPort = 4000;
        m_logParams = new Vector();
        m_logManager = "java.util.logging.FileHandler";
        m_dBufferSizePerPort = -1;
        m_delayedPortName = "";
        m_profile = "";
    }

    /**
     *  This interface method is for enumeration containing all runtime dependencies, for
     *  the service about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @return Enumeration
     */
    public Enumeration getRuntimeDependencies()
    {
        // Fixed N_3366
        return m_runtimeDependencies.elements();
    }

    /**
     *  This method returns the log manager that is to be used for doing logging
     *  for the service instance
     *
     * @return The logManager for the service instance
     * @see #setLogManager(String)
     * @since Tifosi2.0
     */
    public String getLogManager()
    {
        return m_logManager;
    }

    /**
     *  This method returns the profile name in which the service instance is launched.
     *
     * @return The logManager for the service instance
     * @see #setLogManager(String)
     * @since Tifosi2.0
     */
    public String getProfile()
    {
        return m_profile;
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
     *  Gets the eventDeliveryMode attribute of the ServiceInstance object
     *
     * @return The eventDeliveryMode value
     * @deprecated
     */
    public String getEventDeliveryMode()
    {
        return m_eventDeliveryMode;
    }

    /**
     *  Gets the eventExpiryTime attribute of the ServiceInstance object
     *
     * @return The eventExpiryTime value
     * @deprecated
     */
    public long getEventExpiryTime()
    {
        return m_eventExpiryTime;
    }

    /**
     *  Gets the eventHandlerIndex attribute of the ServiceInstance object
     *
     * @return The eventHandlerIndex value
     * @deprecated
     */
    public int getEventHandlerIndex()
    {
        return m_eventHandler;
    }

    /**
     *  This method gets vector of all the logging parameters defined for
     *  service instance, as objects of Param, from this object of <code>serviceInstance</code>
     *
     * @return Vector of Param objects
     * @see #addLogParameter(Param)
     * @since Tifosi2.0
     */
    public Enumeration getLogParameters()
    {
        return m_logParams.elements();
    }

    /**
     *  This method sets whether or not launch of this service instance is
     *  always preferred on highest level of node configured for it to run.
     *
     * @return boolean specifying whether or not launch of this service
     *      instance is preferred on highest level
     * @see #isLaunchOnHigherLevelNodePreferred()
     * @since Tifosi2.0
     */
    public boolean isLaunchOnHigherLevelNodePreferred()
    {
        return m_bPreferLaunchOnHigherLevelNode;
    }

    /**
     * Returns durable subscription for object
     *
     * @return
     * @deprecated
     */
    public boolean isDurableSubscription()
    {
        return m_bIsDurableSubscription;
    }

    /**
     * This property is used for while creating a CF for the component.
     * AllowDurableSubscriptions
     *
     * @return
     * @deprecated
     */
    public boolean isDurableConnection()
    {
        return m_bIsDurableConnection;
    }


    /**
     *  Checks if the primary service has to be killed on launch of secondary service with the same name.
     *  If a service is bound to a FPS and then another service with the same name tries to bind to the same FPS, then we have two options. If this
     *  boolean is set to true then the primary service will be killed and
     *  secondary service will take its place.If set to false, the secondary
     *  service will kill itself.This method checks the condition of this variable.
     *
     * @return boolean - true if running service will be killed, false otherwise
     * @see #setKillPrimaryOnSecondaryLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isKillPrimaryOnSecondaryLaunch()
    {
        return m_bKillPrimaryOnSecondaryLaunch;
    }

    /**
     *  This method checks whether or not launch of this service instance is in
     *  debug mode.
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
     *  This method checks whether or not this service instance is the end of
     *  work flow.
     *
     * @return true if this instance is end of workflow, false otherwise.
     * @see #setIsEndOfWorkflow(boolean)
     * @since Tifosi2.0
     */
    public boolean isEndOfWorkflow()
    {
        return m_isEndOfWorkflow;
    }

    /**
     *  This method checks if this service instance is to be launched manually
     *  or not.
     *
     * @return true if instance is to be launched manually, false otherwise.
     * @see #setIsManualLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isManualLaunch()
    {
        return m_isManualLaunch;
    }

    /**
     *  This method checks if it is a stateful service instance or not.
     *
     * @return true if instance is stateful, false otherwise.
     * @see #setIsStateful(boolean)
     * @since Tifosi2.0
     */
    public boolean isStateful()
    {
        return m_isStateful;
    }


    /**
     *  This method checks whether or not this service instance is to be
     *  launched inMemory.
     *
     * @return true if instance is to be launched inMemory, false otherwise.
     * @see #setIsInMemoryLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isInMemoryLaunch()
    {
        return m_isInMemoryLaunch;
    }

    /**
     *  This method checks whether or not this service instance is to have
     *  transport as LPC.
     *
     * @return true if instance is to be launched inMemory, false otherwise.
     * @see #setIsInMemoryLaunch(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isTransportLPC()
    {
        return m_isTransportLPC;
    }
    /**
     *  This method checks whether or not this service instance is to be
     *  launched delayed. Delayed launch implies launch after receiving some
     *  message over input channel.
     *
     * @return true if instance is to be launched delayed, false otherwise.
     * @see #setIsDelayedLaunch(boolean)
     * @deprecated
     * @since Tifosi2.0
     */
    public boolean isDelayedLaunch()
    {
        return m_isDelayedLaunch;
    }

    /**
     *  This method gets the delayed port name. If a message comes to this port,
     *  only then the service will be launched. This variable will be used only
     *  when isDelayedLaunch is set to true otherwise it will be ignored.The
     *  possible values for the delayedPortName are any of the input port names
     *  or "All".
     *
     * @return The portName at which message arrival will cause service to be
     *      launched.
     * @see #setDelayedPortName(String)
     * @see #isDelayedLaunch()
     * @see #setIsDelayedLaunch(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public String getDelayedPortName()
    {
        return m_delayedPortName;
    }

    /**
     *  This method gets the maximum limit for retrials of this service instance
     *  launch, when the Tifosi peer server goes down. This is not supported in
     *  the current version of Tifosi.
     *
     * @return Maximum retrial limit
     * @see #setMaxRetries(int)
     * @since Tifosi2.0
     * @deprecated
     */
    public int getMaxRetries()
    {
        return m_maxRetries;
    }

    /**
     *  Gets the transacted attribute of the ServiceInstance object
     *
     * @return The transacted value
     * @see #setIsTransacted(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isTransacted()
    {
        return m_isTransacted;
    }


    /**
     *  This method checks if error handling is enabled or not for this service
     *  instance.
     *
     * @return Returns true if error handling is enabled else false.
     * @see #setIsErrorHandlingEnabled(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isErrorHandlingEnabled()
    {
        return m_isErrorHandlingEnabled;
    }


    /**
     * @return boolean
     * @jmx.managed-attribute access="read-write" description="Specify to override Message Properties"
     * @jmx.descriptor name="displayName" value="Override Message Properties"
     * @jmx.descriptor name="defaultValue" value="false"
     * @jmx.descriptor name="index" value="1"
     */
    public String getServiceInstName()
    {
        return m_servInstName;
    }


    /**
     *  This method gets the service GUID from this object of <code>ServiceInstance</code>
     *  .
     *
     * @return The GUID of service
     * @see #setServiceGUID(String)
     * @since Tifosi2.0
     */
    public String getServiceGUID()
    {
        return m_servGUID;
    }


    /**
     *  This method gets the service version from this object of <code>ServiceInstance</code>
     *  .
     *
     * @return Returns version of the service
     * @see #setVersion(String)
     * @since Tifosi2.0
     */
    public String getVersion()
    {
        return m_version;
    }


    /**
     *  This method checks if the version of service in this object of <code>ServiceInstance</code>
     *  , is locked or not.
     *
     * @return true if the service version is locked, false otherwise.
     * @see #setIsVersionLocked(boolean)
     * @see #getVersion()
     * @see #setVersion(String)
     * @since Tifosi2.0
     */
    public boolean isVersionLocked()
    {
        return m_isVersionLocked;
    }


    /**
     *  This method gets the long description about service, from this object of
     *  <code>ServiceInstance</code>.
     *
     * @return Long description about service
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_longDescription;
    }


    /**
     *  This method gets the short description about service, from this object
     *  of <code>ServiceInstance</code>.
     *
     * @return Short description about service
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_shortDescription;
    }

    /**
     *  Gets the nodeInfo attribute of the ServiceInstance object
     *
     * @return The nodeInfo value
     * @see #getNodeNames()
     * @see #addNodes(String, String)
     * @since Tifosi2.0
     */
    public Hashtable getNodeInfo()
    {
        return m_nodes;
    }


    /**
     *  This method gets names of all the nodes specified for service instance
     *  in this object of <code>ServiceInstance</code>.
     *
     * @return Enumeration of names of all the nodes.
     * @see #getNodeNameForThisLevel(String)
     * @see #getNodeInfo()
     * @see #getNodeLevels()
     * @since Tifosi2.0
     */
    public Enumeration getNodeNames()
    {
        return m_nodes.keys();
    }


    /**
     *  This method gets all the node levels specified for service instance in
     *  this object of <code>ServiceInstance</code>.
     *
     * @return Enumeration of all node levels.
     * @see #getNodeNameForThisLevel(String)
     * @see #addNodes(String, String)
     * @see #getNodeNames()
     * @since Tifosi2.0
     */
    public Enumeration getNodeLevels()
    {
        return m_nodes.elements();
    }

    /**
     *  This method gets the name of node for specified nodelevel in this object
     *  of <code>ServiceInstance</code>.
     *
     * @param nodeLevel Level of node for which to get the name of node.
     * @return The node name for specified nodelevel.
     * @see #getNodeLevels()
     * @see #getNodeNames()
     * @see #addNodes(String, String)
     * @since Tifosi2.0
     */
    public String getNodeNameForThisLevel(String nodeLevel)
    {
        Enumeration enums = m_nodes.keys();

        while (enums.hasMoreElements())
        {
            String nodeName = (String) enums.nextElement();
            String level = (String) m_nodes.get(nodeName);

            if (level.equalsIgnoreCase(nodeLevel))
                return nodeName;
        }

        return null;
    }

    /**
     *  This method gets all the runtime arguments of service instance as object
     *  of <code>RuntimeArgs</code>, from this object of <code>ServiceInstance</code>
     *  .
     *
     * @return object of RuntimeArgs.
     * @see #setRuntimeArgs(RuntimeArgs)
     * @since Tifosi2.0
     */
    public RuntimeArgs getRuntimeArgs()
    {
        return m_runtimeArgs;
    }

    /**
     *  This method gets the port descriptors of service instance as object
     *  of <code>PortInstDescriptor</code>, from this object of
     *  <code>ServiceInstance</code>.
     *
     * @return object of PortInstDescriptor.
     * @see #setPortInstDescriptor(PortInstDescriptor)
     * @since Tifosi2.0
     */
    public PortInstDescriptor getPortInstDescriptor()
    {
        return m_portInstDescriptor;
    }

    /**
     *  This method gets the object of <code>EndStates</code>, specifying end
     *  state information with respect to workflows, from this object of <code>ServiceInstance</code>
     *  .
     *
     * @return Vector containing objects of EndState
     * @see #addEndState(EndState)
     * @see #setEndStates(Vector)
     * @since Tifosi2.0
     */
    public Vector getEndStates()
    {
        return m_vecEndStates;
    }

    /**
     *  This method gets object of <code>Monitor</code>, containing module name
     *  versus tracelevel information, from this object of <code>ServiceInstance</code>
     *
     * @return Object of Monitor.
     * @see #setMonitor(Monitor)
     * @since Tifosi2.0
     * @deprecated
     */
    public Monitor getMonitor()
    {
        return m_monitor;
    }

    /**
     *  This method gets object of <code>LogModules</code>, containing log
     *  module name versus log Level information, from this object of <code>ServiceInstance</code>
     *
     * @return Object of LogModules.
     * @see #setLogModules(LogModules)
     * @since Tifosi2.0
     */
    public LogModules getLogModules()
    {
        return m_logModules;
    }


    /**
     *  This method gets vector of all the extra parameters defined for service
     *  instance, as objects of Param, from this object of <code>serviceInstance</code>
     *  .
     *
     * @return Vector of Param objects
     * @see #addParam(Param)
     * @since Tifosi2.0
     */
    public Vector getParams()
    {
        return m_params;
    }


    /**
     *  This method gets the object of <code>StatusTracking</code>, required
     *  while tracking the service instance status, from this object of <code>ServiceInstance</code>
     *  .
     *
     * @return object of StatusTracking
     * @see #setStatusTracking(StatusTracking)
     * @since Tifosi2.0
     */
    public StatusTracking getStatusTracking()
    {
        return m_statusTracking;
    }

    /**
     * Returns service no cache for object
     *
     * @return boolean
     */
    public boolean isServiceNoCache()
    {
        return Param.getParamValueAsBoolean(m_params, PARAM_SERVICE_NO_CACHE);
    }
    /**
     *  This method returns the ID of this object.
     *
     * @return the id of the object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_INSTANCE;
    }

    /**
     *  This method sets the log manager that is to be used for doing logging
     *  for the service instance
     *
     * @param logManager The logManager to be used
     * @see #getLogManager()
     * @since Tifosi2.0
     */
    public void setLogManager(String logManager)
    {
        m_logManager = logManager;
    }

    /**
     *  This method sets the profile in which the service is launched.
     *
     * @param profile The logManager to be used
     * @see #getLogManager()
     * @since Tifosi2.0
     */
    public void setProfile(String profile)
    {
        m_profile = profile;
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
     *  Sets the eventDeliveryMode attribute of the ServiceInstance object
     *
     * @param type The new eventDeliveryMode value
     */
    public void setEventDeliveryMode(String type)
    {
        m_eventDeliveryMode = type;
    }

    /**
     *  Sets the eventExpiryTime attribute of the ServiceInstance object
     *
     * @param expiryTime The new eventExpiryTime value
     */
    public void setEventExpiryTime(long expiryTime)
    {
        m_eventExpiryTime = expiryTime;
    }

    /**
     *  Sets the eventHandlerIndex attribute of the ServiceInstance object
     *
     * @param handlerIndex The new eventHandlerIndex value
     */
    public void setEventHandlerIndex(int handlerIndex)
    {
        m_eventHandler = handlerIndex;
    }

    /**
     *  This method sets whether or not launch of this service instance is
     *  preferred on highest level.
     *
     * @param bool boolean specifying whether or not launch of this service
     *      instance is preferred on highest level
     * @see #isLaunchOnHigherLevelNodePreferred()
     * @since Tifosi2.0
     */
    public void setPreferLaunchOnHigherLevelNode(boolean bool)
    {
        m_bPreferLaunchOnHigherLevelNode = bool;
    }

    /**
     * Sets is durable subscription for object
     *
     * @param bool
     * @deprecated
     */
    public void setIsDurableSubscription(boolean bool)
    {
        m_bIsDurableSubscription = bool;
    }

    /**
     * Sets is durable Connection for object
     *
     * @param bool
     * @deprecated
     */
    public void setIsDurableConnection(boolean bool)
    {
        m_bIsDurableConnection = bool;
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
     *  This method sets whether or not launch of this service instance is in
     *  debug mode. If in debug mode then it is compulsory to specify the debug
     *  port.
     *
     * @param isDebugMode The new debugMode value
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
     * @param debugPort The new debugPort value
     * @see #getDebugPort()
     * @since Tifosi2.0
     */
    public void setDebugPort(int debugPort)
    {
        m_iDebugPort = debugPort;
    }

    /**
     *  This method is called to set whether or not this service instance is the
     *  end of workflow.
     *
     * @param isEndOfWorkflow boolean specifying whether or not this instance
     *      is the end of workflow.
     * @see #isEndOfWorkflow()
     * @since Tifosi2.0
     */
    public void setIsEndOfWorkflow(boolean isEndOfWorkflow)
    {
        m_isEndOfWorkflow = isEndOfWorkflow;
    }

    /**
     *  This method sets whether or not this service instance is to be launched
     *  manually.
     *
     * @param isManualLaunch boolean specifying whether or not it is to be
     *      launched manually.
     * @see #isManualLaunch()
     * @since Tifosi2.0
     */
    public void setIsManualLaunch(boolean isManualLaunch)
    {
        m_isManualLaunch = isManualLaunch;
    }


    /**
     *  This method sets if this service instance is stateful or not.
     *
     * @param isStateful boolean specifying if it is a stateful service or not.
     * @see #isStateful()
     * @since Tifosi2.0
     */
    public void setIsStateful(boolean isStateful)
    {
        m_isStateful = isStateful;
    }

    /**
     *  This method sets whether or not this service instance is to be launched
     *  clrep.
     *
     * @param isInMemory boolean specifying if instance is to be launched
     *      inMemory or not.
     * @see #isInMemoryLaunch()
     * @since Tifosi2.0
     */
    public void setIsInMemoryLaunch(boolean isInMemory)
    {
        m_isInMemoryLaunch = isInMemory;
    }

    /**
     *  This method sets whether or not this service instance is to  have
     *  transport as LPC
     *
     * @param isForLPC boolean specifying if instance is to have
     *      transport as LPC or not.
     * @see #isTransportLPC()
     * @since Tifosi2.0
<<<<<<< ServiceInstance.java
     * @deprecated
=======
     * @deprecated
>>>>>>> 1.28
     */
    public void setIsTransportLPC(boolean isForLPC)
    {
        m_isTransportLPC = isForLPC;
    }
    /**
     *  This method sets whether or not this service instance is to be launched
     *  delayed. Delayed launch implies launch after receiving some message over
     *  input channel.
     *
     * @param isDelayedLaunch boolean specifying if instance is to be launched
     *      delayed or not.
     * @see #isDelayedLaunch()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setIsDelayedLaunch(boolean isDelayedLaunch)
    {
        m_isDelayedLaunch = isDelayedLaunch;
    }

    /**
     *  This method sets the delayed port name. If a message comes to this port,
     *  only then the service will be launched. This variable will be used only
     *  when isDelayedLaunch is set to true otherwise it will be ignored.The
     *  possible values for the delayedPortName are any of the input port names
     *  or "All".
     *
     * @param delayedPortName The portName at which message arrival will cause
     *      service to be launched.
     * @see #getDelayedPortName()
     * @see #isDelayedLaunch()
     * @see #setIsDelayedLaunch(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public void setDelayedPortName(String delayedPortName)
    {
        m_delayedPortName = delayedPortName;
    }

    /**
     *  This method sets the maximum limit for retrials of service instance
     *  launch, when the Tifosi peer server goes down. This is not supported in
     *  the current version of Tifosi.
     *
     * @param maxRetries integer value to be set as Maximum retrial limit
     * @see #getMaxRetries()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setMaxRetries(int maxRetries)
    {
        m_maxRetries = maxRetries;
    }

    /**
     *  This method sets whether or not the service instance is to be launched.
     *
     * @param isTransacted The new isTransacted value
     * @see #isTransacted()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setIsTransacted(boolean isTransacted)
    {
        m_isTransacted = isTransacted;
    }

    /**
     *  This method sets value of the attribute specifying if error handling is
     *  enabled or not for this service instance.
     *
     * @param isErrorHandlingEnabled is a boolean specifying if error handling is
     *      enabled or not for this service instance.
     * @see #isErrorHandlingEnabled()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setIsErrorHandlingEnabled(boolean isErrorHandlingEnabled)
    {
        m_isErrorHandlingEnabled = isErrorHandlingEnabled;
    }


    /**
     * This method sets the specified string as service instance name for this
     *  object of <code>ServiceInstance</code>.
     *
     * @param servInstName
     * @jmx.managed-attribute access="read-write"
     */
    public void setServiceInstName(String servInstName)
    {
        m_servInstName = servInstName;
    }


    /**
     *  This method sets the specified string as service GUID for this object of
     *  <code>ServiceInstance</code>.
     *
     * @param servGUID string to be set as service GUID
     * @see #getServiceGUID()
     * @since Tifosi2.0
     */
    public void setServiceGUID(String servGUID)
    {
        m_servGUID = servGUID;
    }


    /**
     *  This method sets the specified string as service version for this object
     *  of <code>ServiceInstance</code>.
     *
     * @param version of the service.
     * @see #getVersion()
     * @since Tifosi2.0
     */
    public void setVersion(String version)
    {
        m_version = version;
    }


    /**
     *  This method sets if this version of service is be locked, in this object
     *  of <code>ServiceInstance</code>.
     *
     * @param isVersionLocked boolean specifying if the service version is
     *      locked.
     * @see #isVersionLocked()
     * @see #getVersion()
     * @see #setVersion(String)
     * @since Tifosi2.0
     */
    public void setIsVersionLocked(boolean isVersionLocked)
    {
        m_isVersionLocked = isVersionLocked;
    }


    /**
     *  This method sets the specified string as long description about the
     *  service, for this object of <code>ServiceInstance</code>.
     *
     * @param longDescription string to be set as long description.
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String longDescription)
    {
        m_longDescription = longDescription;
    }


    /**
     *  This method sets the specified string as short description about
     *  service, for this object of <code>ServiceInstance</code>.
     *
     * @param shortDescription string to be set as short description
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String shortDescription)
    {
        m_shortDescription = shortDescription;
    }

    /**
     *  This method sets the specified object of <code>RuntimeArgs</code> as
     *  runtime arguments of service instance in this object of <code>ServiceInstance</code>
     *  .
     *
     * @param runtimeArgs Object of RuntimeArgs
     * @see #getRuntimeArgs()
     * @since Tifosi2.0
     */
    public void setRuntimeArgs(RuntimeArgs runtimeArgs)
    {
        m_runtimeArgs = runtimeArgs;
    }

    /**
     *  This method sets the specified object of <code>PortInstDescriptor</code>
     *  as poortDescriptor of service instance in this object of
     *  <code>ServiceInstance</code>.
     *
     * @param portInstDescriptor
     * @see #getPortInstDescriptor()
     * @since Tifosi2.0
     */
    public void setPortInstDescriptor(PortInstDescriptor portInstDescriptor)
    {
        m_portInstDescriptor = portInstDescriptor;
    }

    /**
     *  This method sets the specified object of <code>EndState</code>. It
     *  specifies the end state information with respect to workflows, in this
     *  object <code>ServiceIntsance</code>.
     *
     * @param endStates Vector containing endState information.
     * @see #addEndState(EndState)
     * @see #getEndStates()
     * @since Tifosi2.0
     */
    public void setEndStates(Vector endStates)
    {
        m_vecEndStates = endStates;
    }

    /**
     *  This method sets the specified object of <code>Monitor</code> for this
     *  object of <code>ServiceInstance</code>. Object of <code>Monitor</code>
     *  contains module name versus tracelevel information about this service
     *  instance, used while monitoring the service instance.
     *
     * @param monitor Object of Monitor
     * @see #getMonitor()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setMonitor(Monitor monitor)
    {
        m_monitor = monitor;
    }

    /**
     *  This method sets the specified object of <code>LogModules</code> for
     *  this object of <code>ServiceInstance</code>. Object of <code>LogModules</code>
     *  contains module name versus logLevel information about this service
     *  instance, used while monitoring the service instance.
     *
     * @param logModules The new logModules value
     * @see #getLogModules()
     * @since Tifosi2.0
     */
    public void setLogModules(LogModules logModules)
    {
        m_logModules = logModules;
    }


    /**
     *  This method sets the specified object of <code>StatusTracking</code>,
     *  required while tracking the service instance status, in this object of
     *  <code>ServiceInstance</code>.
     *
     * @param statusTracking object of StatusTracking
     * @see #getStatusTracking()
     * @since Tifosi2.0
     */
    public void setStatusTracking(StatusTracking statusTracking)
    {
        m_statusTracking = statusTracking;
    }


    /**
     *  This method sets all the fieldValues of this object of <code>ServiceInstance</code>
     *  , using the specified XML string.
     *
     * @param serInstanceElement
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element serInstanceElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element serInstanceElement = doc.getDocumentElement();

        if (serInstanceElement != null)
        {
            boolean isManual = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isManualLaunch");

            setIsManualLaunch(isManual);

            boolean isStateful = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isStateful");

            setIsStateful(isStateful);

            boolean isDelayedLaunch = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isDelayedLaunch");

            setIsDelayedLaunch(isDelayedLaunch);

            String delayedPortName = serInstanceElement.getAttribute("delayedPort");

            if (delayedPortName != null && !delayedPortName.equals(""))
                setDelayedPortName(delayedPortName);

            String maxRetries = serInstanceElement.getAttribute("maxRetries");

            setMaxRetries(new Integer(maxRetries).intValue());

            boolean isTransacted = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isTransacted");

            setIsTransacted(isTransacted);

            boolean isErrorHandling = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isErrorHandlingEnabled");

            setIsErrorHandlingEnabled(isErrorHandling);

            boolean isInMemoryLaunch = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isInMemoryLaunch");

            setIsInMemoryLaunch(isInMemoryLaunch);

            boolean isTransportLPC = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isTransportLPC");

            setIsTransportLPC(isTransportLPC);

            String profile = serInstanceElement.getAttribute("profile");

            setProfile(profile);

            boolean isEndOfWorkflow = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isEndOfWorkflow");

            setIsEndOfWorkflow(isEndOfWorkflow);

            boolean preferLaunchOnPrimaryNode = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "preferLaunchOnHigherLevelNode");
            //boolean durableSubscription = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "durableSubscription");
            //boolean durableConnection = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "durableConnection");

            setPreferLaunchOnHigherLevelNode(preferLaunchOnPrimaryNode);
            //setIsDurableSubscription(durableSubscription);
            //setIsDurableConnection(durableConnection);

            boolean killPrimaryOnSecondaryLaunch = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "killPrimaryOnSecondaryLaunch");

            setKillPrimaryOnSecondaryLaunch(killPrimaryOnSecondaryLaunch);

            boolean isDebugMode = XMLDmiUtil.getAttributeAsBoolean(serInstanceElement, "isDebugMode");

            setDebugMode(isDebugMode);

            String debugPort = serInstanceElement.getAttribute("debugPort");

            if (debugPort != null && !debugPort.equals(""))
                setDebugPort(new Integer(debugPort).intValue());

            NodeList list = serInstanceElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("ServiceInstanceName"))
                    setServiceInstName(XMLUtils.getNodeValueAsString(child));
                else if (nodeName.equalsIgnoreCase("ServiceGUID"))
                    setServiceGUID(XMLUtils.getNodeValueAsString(child));
                else if (nodeName.equalsIgnoreCase("BufferSizePerPort"))
                    setBufferSizePerPort(XMLUtils.getNodeValueAsLong(child));
                else if (nodeName.equalsIgnoreCase("Version"))
                {
                    boolean isLocked = XMLDmiUtil.getAttributeAsBoolean(((Element) child), "isLocked");

                    setIsVersionLocked(isLocked);
                    setVersion(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("Node"))
                {
                    String name = XMLUtils.getNodeValueAsString(child);
                    name = name.toLowerCase();

                    if (name == null)
                        throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

                    String value = ((Element) child).getAttribute("level");

                    if (value.equals(""))
                        value = "0";

                    m_nodes.put(name, value);
                }
                else if (nodeName.equalsIgnoreCase("EventHandler"))
                {
                    int handlerIndex = XMLUtils.getNodeValueAsInt(child);

                    if (handlerIndex <= 0)
                        throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

                    setEventHandlerIndex(handlerIndex);

                    String value = ((Element) child).getAttribute("deliveryMode");

                    if (value.equals(""))
                        value = ServiceInstanceConstants.EVENT_DELIVERY_MODE_PERSISTANT;
                    setEventDeliveryMode(value);

                    String expString = ((Element) child).getAttribute("expiryTime");
                    long expiryTime = 0;

                    if (expString != null)
                        expiryTime = new Long(expString).longValue();
                    setEventExpiryTime(expiryTime);
                }
                else if (nodeName.equalsIgnoreCase("RuntimeArguments"))
                {
                    RuntimeArgs args = new RuntimeArgs();

                    args.setFieldValues((Element) child);
                    setRuntimeArgs(args);
                }
                else if (nodeName.equalsIgnoreCase("RuntimeDependency"))
                {
                    RuntimeDependency rd = new RuntimeDependency();

                    rd.setFieldValues((Element) child);
                    addRuntimeDependency(rd);
                }
                else if (nodeName.equalsIgnoreCase("PortInstDescriptor"))
                {
                    PortInstDescriptor portInstDesc = new PortInstDescriptor();

                    portInstDesc.setFieldValues((Element) child);
                    setPortInstDescriptor(portInstDesc);
                }
                else if (nodeName.equalsIgnoreCase("LongDescription"))
                    setLongDescription(XMLUtils.getNodeValueAsString(child));
                else if (nodeName.equalsIgnoreCase("ShortDescription"))
                    setShortDescription(XMLUtils.getNodeValueAsString(child));
                else if (nodeName.equalsIgnoreCase("LogManager"))
                {
                    NodeList childList = child.getChildNodes();
                    Node grandChild = null;

                    for (int j = 0; j < childList.getLength(); j++)
                    {
                        grandChild = childList.item(j);

                        String name = grandChild.getNodeName();

                        if (name.equalsIgnoreCase("Name"))
                            setLogManager(XMLUtils.getNodeValueAsString(grandChild));
                        else if (name.equalsIgnoreCase("Param"))
                        {
                            Param param = new Param();

                            param.setFieldValues((Element) grandChild);
                            m_logParams.add(param);
                        }
                    }
                }
                else if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param param = new Param();

                    param.setFieldValues((Element) child);
                    m_params.add(param);
                }
                else if (nodeName.equalsIgnoreCase("StatusTracking"))
                {
                    StatusTracking tracking = new StatusTracking();

                    tracking.setFieldValues((Element) child);
                    setStatusTracking(tracking);
                }
                else if (nodeName.equalsIgnoreCase("EndState"))
                {
                    EndState endState = new EndState();

                    endState.setFieldValues((Element) child);
                    addEndState(endState);
                }
                else if (nodeName.equalsIgnoreCase("Monitor"))
                {
                    Monitor monitor = new Monitor();

                    monitor.setFieldValues((Element) child);
                    setMonitor(monitor);
                }
                else if (nodeName.equalsIgnoreCase("EventModules"))
                {
                    Monitor monitor = new Monitor();

                    monitor.setFieldValues((Element) child);
                    setMonitor(monitor);
                }
                else if (nodeName.equalsIgnoreCase("LogModules"))
                {
                    LogModules logModules = new LogModules();

                    logModules.setFieldValues((Element) child);
                    setLogModules(logModules);
                }
            }
        }
        validate();
    }


    protected void populate(FioranoStaxParser parser)throws XMLStreamException , FioranoException
    {

        //todo remove this variable
        if ( parser.markCursor(APSConstants.SERVICE_INSTANCE) )
        {

            // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = parser.getAttributes();
            boolean isManual = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_MANUAL_LAUNCH));//serInstanceElement, "isManualLaunch");

            setIsManualLaunch(isManual);

            boolean isStateful = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_STATEFUL));//serInstanceElement, "isStateful");

            setIsStateful(isStateful);

            boolean isDelayedLaunch = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_DELAYED_LAUNCH));//serInstanceElement, "isDelayedLaunch");

            setIsDelayedLaunch(isDelayedLaunch);

            String delayedPortName =(String) attributes.get(APSConstants.DELAYED_PORT);//serInstanceElement.getAttribute("delayedPort");

            if (delayedPortName != null && !delayedPortName.equals(""))
                setDelayedPortName(delayedPortName);

            String maxRetries = (String) attributes.get(APSConstants.MAX_RETRIES);//serInstanceElement.getAttribute("maxRetries");

            setMaxRetries(new Integer(maxRetries).intValue());

            boolean isTransacted = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_TRANSACTED));//serInstanceElement, "isTransacted");

            setIsTransacted(isTransacted);

            boolean isErrorHandling = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_ERROR_HANDLING_ENABLED));//serInstanceElement, "isErrorHandlingEnabled");

            setIsErrorHandlingEnabled(isErrorHandling);

            boolean isInMemoryLaunch = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_INMEMORY_LAUNCH));//serInstanceElement, "isInMemoryLaunch");

            setIsInMemoryLaunch(isInMemoryLaunch);

            boolean isTransportLPC = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_TRANSPORT_LPC));//serInstanceElement, "isTransportLPC");

            setIsTransportLPC(isTransportLPC);

            String profile = (String) attributes.get(APSConstants.SERVICE_PROFILE);

            setProfile(profile);

            boolean isEndOfWorkflow = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_END_OF_WORKFLOW));//serInstanceElement, "isEndOfWorkflow");

            setIsEndOfWorkflow(isEndOfWorkflow);

            boolean preferLaunchOnPrimaryNode = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.PREFER_LAUNCH_ON_HIGHER_LEVEL_NODE));//serInstanceElement, "preferLaunchOnHigherLevelNode");
            boolean durableSubscription = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.DURABLE_SUBSCRIPTION));//serInstanceElement, "durableSubscription");
            boolean durableConnection = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.DURABLE_SUBSCRIPTION));//serInstanceElement, "durableConnection");

            setPreferLaunchOnHigherLevelNode(preferLaunchOnPrimaryNode);
            setIsDurableSubscription(durableSubscription);
            setIsDurableConnection(durableConnection);

            boolean killPrimaryOnSecondaryLaunch = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.KILL_PRIMARY_ON_SECONDARY_LAUNCH));//serInstanceElement, "killPrimaryOnSecondaryLaunch");

            setKillPrimaryOnSecondaryLaunch(killPrimaryOnSecondaryLaunch);

            boolean isDebugMode = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_DEBUG_MODE));//serInstanceElement, "isDebugMode");

            setDebugMode(isDebugMode);

            String debugPort = (String)attributes.get(APSConstants.DEBUG_PORT);//serInstanceElement.getAttribute("debugPort");

            if (debugPort != null && !debugPort.equals(""))
                setDebugPort(new Integer(debugPort).intValue());

            // Get Child Elements
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();
                if (nodeName.equalsIgnoreCase(APSConstants.SERVICE_INSTANCE_NAME))
                    setServiceInstName(parser.getText());
                else if (nodeName.equalsIgnoreCase(APSConstants.SERVICE_GUID))
                    setServiceGUID(parser.getText());
                else if (nodeName.equalsIgnoreCase(APSConstants.BUFFER_SIZE_PER_PORT))
                    setBufferSizePerPort(XMLUtils.getStringAsLong(parser.getText()));
                else if (nodeName.equalsIgnoreCase(APSConstants.VERSION))
                {
                    Hashtable versionAttributes=parser.getAttributes();
                    boolean isLocked = XMLUtils.getStringAsBoolean((String)versionAttributes.get(APSConstants.IS_LOCKED));//((Element) child), "isLocked");
                    setIsVersionLocked(isLocked);
                    setVersion(parser.getText());
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.NODE))
                {
                    Hashtable nodeAttributes = parser.getAttributes();
                    String value = (String)nodeAttributes.get(APSConstants.NODE_LEVEL);//(Element) child).getAttribute("level");

                    if (value.equals(""))
                        value = "0";


                    String name = parser.getText();//XMLUtils.getNodeValueAsString(child);
                    name = name.toLowerCase();

                    if (name == null)
                        throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

                    m_nodes.put(name, value);

                }
                else if (nodeName.equalsIgnoreCase(APSConstants.EVENT_HANDLER))
                {
                    Hashtable eventHandlerAttributes = parser.getAttributes();
                    String value = (String)eventHandlerAttributes.get(APSConstants.DELIVERY_MODE);//((Element) child).getAttribute("deliveryMode");

                    if (value.equals(""))
                        value = ServiceInstanceConstants.EVENT_DELIVERY_MODE_PERSISTANT;
                    setEventDeliveryMode(value);
                    String expString = (String)eventHandlerAttributes.get(APSConstants.EXPIRY_TIME);//((Element) child).getAttribute("expiryTime");
                    long expiryTime = 0;

                    if (expString != null)
                        expiryTime = new Long(expString).longValue();
                    value = value+"\tExpiryTime:"+expiryTime;
                    setEventExpiryTime(expiryTime);

                    int handlerIndex = XMLUtils.getStringAsInt(parser.getText());

                    if (handlerIndex <= 0)
                        throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

                    setEventHandlerIndex(handlerIndex);
                    value=value+"\tHandlerIndex:"+handlerIndex;

                }
                else if (nodeName.equalsIgnoreCase(APSConstants.RUNTIME_ARGS))
                {
                    RuntimeArgs args = new RuntimeArgs();

                    args.setFieldValues(parser);
                    setRuntimeArgs(args);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.RUNTIME_DEPENDENCY))
                {
                    RuntimeDependency rd = new RuntimeDependency();

                    rd.setFieldValues(parser);
                    addRuntimeDependency(rd);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.PORT_INST_DESCRIPTOR))
                {
                    PortInstDescriptor portInstDesc = new PortInstDescriptor();

                    portInstDesc.setFieldValues(parser);
                    setPortInstDescriptor(portInstDesc);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.LONG_DESCRIPTION))
                {

                    setLongDescription(parser.getText());
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.SHORT_DESCRIPTION))
                   setShortDescription(parser.getText());
                else if (nodeName.equalsIgnoreCase(APSConstants.LOG_MANAGER))
                {
                    // No DMI, but need to extract child elements. So mark & reset parser
					parser.markCursor("LogManager");
                    while( parser.nextElement())
                    {

                        String name = parser.getLocalName();

                        if (name.equalsIgnoreCase(APSConstants.PARAM_NAME))
                            setLogManager(parser.getText());
                        else if (name.equalsIgnoreCase(APSConstants.PARAM))
                        {
                            Param param = new Param();
                            param.setFieldValues(parser);
                            m_logParams.add(param);
                        }
                    }
                    parser.resetCursor();
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.PARAM))
                {
                    Param param = new Param();

                    param.setFieldValues(parser);
                    m_params.add(param);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.STATUS_TRACKING))
                {
                    StatusTracking tracking = new StatusTracking();

                    tracking.setFieldValues(parser);
                    setStatusTracking(tracking);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.END_STATE))
                {
                    EndState endState = new EndState();

                    endState.setFieldValues(parser);
                    addEndState(endState);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.MONITOR))
                {
                    Monitor monitor = new Monitor();

                    monitor.setFieldValues(parser);
                    setMonitor(monitor);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.EVENT_MODULES))
                {
                    //Don't know why we are using same class for both tags.
                    Monitor monitor = new Monitor(APSConstants.EVENT_MODULES);
                    monitor.setFieldValues(parser);
                    setMonitor(monitor);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.LOG_MODULES))
                {
                    LogModules logModules = new LogModules();

                    logModules.setFieldValues(parser);
                    setLogModules(logModules);
                }

            }
        }
        validate();
    }

    /**
     * Sets is service no cache for object
     *
     * @param isServiceNoCache
     */
    public void setIsServiceNoCache(boolean isServiceNoCache)
    {
        Param.setParamValue(m_params, PARAM_SERVICE_NO_CACHE, isServiceNoCache);
    }
    /**
     *  This interface method is called to add the specified object
     *  of <code>RuntimeDependency</code> to the list of service dependencies.
     *  This is for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @param servDependencyInfo object of  RuntimeDependency to be added.
     * @see #removeRuntimeDependency(RuntimeDependency)
     * @see #clearRuntimeDependency()
     * @since Tifosi2.0
     */
    public void addRuntimeDependency(RuntimeDependency servDependencyInfo)
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
     * @see #addRuntimeDependency(RuntimeDependency )
     * @see #clearRuntimeDependency()
     * @since Tifosi2.0
     */
    public void removeRuntimeDependency(RuntimeDependency serv)
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
     * @see #addRuntimeDependency(RuntimeDependency )
     * @see #removeRuntimeDependency(RuntimeDependency)
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
     *  This method adds parameters for the log manager. This parameter is used
     *  for initialization of the log manager.
     *
     * @param param object of Param
     * @see #getLogParameters()
     * @since Tifosi2.0
     */
    public void addLogParameter(Param param)
    {
        m_logParams.addElement(param);
    }

    /**
     *  This method clears the list of parameters specified for log manager.
     *
     * @see #addLogParameter(Param)
     * @see #getLogParameters()
     * @since Tifosi2.0
     */
    public void clearLogParameters()
    {
        m_logParams.clear();
    }

    /**
     *  This method adds the node name and its level of preference, to this
     *  object of <code>ServiceInstance</code>. Using this method, failover
     *  nodes can be specified, for service instances, along with the level of
     *  preferences. Please specify a valid integer as String for nodeLevel
     *  eg. "1","2"
     *  Also note that two nodes cannot have same level.
     *
     * @param nodeName string to be set as node name
     * @param nodeLevel string specifying level for specified node.
     * @since Tifosi2.0
     * @deprecated
     */
    public void addNodes(String nodeName, String nodeLevel)
    {
        if (m_nodes == null)
            return;

        Enumeration levels = m_nodes.elements();

        while (levels.hasMoreElements())
        {
            String level = (String) levels.nextElement();

            if (level.equalsIgnoreCase(nodeLevel))
                return;
        }
        m_nodes.put(nodeName, nodeLevel);
    }

    /**
     *  This method adds the node name and its level of preference, to this
     *  object of <code>ServiceInstance</code>. Using this method, failover
     *  nodes can be specified, for service instances, along with the level of
     *  preferences. Please ensure that Level is a non-negative integer. Also
     *  two nodes cannot have same level
     *
     * @param nodeName string to be set as node name
     * @param nodeLevel integer specifying level for specified node.
     */
    public void addNode(String nodeName, int nodeLevel)
    {
        String newLevel = "" + nodeLevel;

        if (nodeLevel < 0)
            return;

        if (m_nodes == null)
            return;

        Enumeration levels = m_nodes.elements();

        while (levels.hasMoreElements())
        {
            String level = (String) levels.nextElement();

            if (level.equalsIgnoreCase(newLevel))
                return;
        }
        m_nodes.put(nodeName, newLevel);
    }

    /**
     *  This method removes Nodename given from this
     *  object of <code>ServiceInstance</code>.
     *
     * @param nodeName nodeName to be removed
     * @since Tifosi2.0
     */
    public void removeNode(String nodeName)
    {
        if (nodeName != null)
            m_nodes.remove(nodeName);
    }

    /**
     *  This method adds the specified object of <code>EndState</code>, to this
     *  object of <code>ServiceInstance</code>.
     *
     * @param endState Object of EndState
     * @see #setEndStates(Vector)
     * @see #getEndStates()
     * @since Tifosi2.0
     */
    public void addEndState(EndState endState)
    {
        if (m_vecEndStates == null)
            m_vecEndStates = new Vector();

        if (Isduplicate(endState))
        {
            return;
        }

        m_vecEndStates.add(endState);
    }

    /**
     *  This method adds the specified object of <code>EndState</code>, to this
     *  object of <code>ServiceInstance</code>.
     *
     * @param endState Object of EndState
     * @see #setEndStates(Vector)
     * @see #getEndStates()
     * @since Tifosi2.0
     */
    public void removeEndState(EndState endState)
    {
        if (m_vecEndStates != null)
        {
            m_vecEndStates.remove(endState);
        }
    }

    /**
     *  This method adds some extra parameters for service instance, as objects
     *  of <code>Param</code>, to this object of <code>ServiceInstance</code>.
     *
     * @param param object of Param
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void addParam(Param param)
    {
        m_params.add(param);
    }

    /**
     *  This utility method is used to get XML String representation of this
     *  <code>ApplicationPropertySheet</code> object.
     *
     * @return XML String for this object
     * @exception FioranoException if the calls fails to succeed.
     * @since Tifosi2.0
     */
    public String toXMLString()
        throws FioranoException
    {
        com.fiorano.openesb.utils.DocumentFactoryImpl
            m_documentFactory = new DocumentFactoryImpl();
        Document document = m_documentFactory.createDocument();
        Node node = toJXMLString(document);

        return XMLUtils.serializeDocument(node);
    }


    /**
     *  This method resets the values of the data members of this object. Not
     *  supported in this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_runtimeDependencies = new Vector();
    }


    /**
     *  This method tests whether this object of <code>ServiceInstance</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_nodes == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_servGUID == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_servInstName == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_version == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_isManualLaunch && m_isInMemoryLaunch)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_params != null)
        {
            Enumeration paramEnum = m_params.elements();

            while (paramEnum.hasMoreElements())
            {
                Param param = (Param) paramEnum.nextElement();

                param.validate();
            }
        }

        if (m_statusTracking != null)
            m_statusTracking.validate();

        if (m_vecEndStates != null)
        {
            for (int i = 0; i < m_vecEndStates.size(); i++)
            {
                ((EndState) m_vecEndStates.get(i)).validate();
            }
        }

        if (m_runtimeDependencies != null)
        {
            Enumeration _enum = m_runtimeDependencies.elements();

            while (_enum.hasMoreElements())
            {
                RuntimeDependency serv = (RuntimeDependency) _enum.nextElement();

                serv.validate();
            }
        }
    }


    /**
     *  This method tests whether this object of <code>ServiceInstance</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if the object is not valid
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeBoolean(m_isManualLaunch);

        out.writeBoolean(m_isStateful);

        out.writeBoolean(m_isDelayedLaunch);

        UTFReaderWriter.writeUTF(out, m_delayedPortName);

        out.writeBoolean(m_isTransacted);

        out.writeBoolean(m_isErrorHandlingEnabled);

        out.writeBoolean(m_isVersionLocked);

        out.writeBoolean(m_isEndOfWorkflow);

        out.writeBoolean(m_isInMemoryLaunch);

        out.writeBoolean(m_isTransportLPC);

        out.writeBoolean(m_bPreferLaunchOnHigherLevelNode);

        out.writeBoolean(m_bIsDurableSubscription);

        out.writeBoolean(m_bIsDurableConnection);

        out.writeBoolean(m_bKillPrimaryOnSecondaryLaunch);

        out.writeBoolean(m_bIsDebugMode);

        out.writeInt(m_iDebugPort);

        out.writeInt(m_maxRetries);

        if (m_version != null)
            UTFReaderWriter.writeUTF(out, m_version);
        else
            UTFReaderWriter.writeUTF(out, "");

        out.writeLong(m_dBufferSizePerPort);

        if (m_servInstName != null)
            UTFReaderWriter.writeUTF(out, m_servInstName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_servGUID != null)
            UTFReaderWriter.writeUTF(out, m_servGUID);
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

        // Service Dependency
        if (m_runtimeDependencies != null && m_runtimeDependencies.size() > 0)
        {
            int num = m_runtimeDependencies.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                RuntimeDependency serv = (RuntimeDependency) m_runtimeDependencies.elementAt(i);

                serv.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if (m_runtimeArgs != null)
        {
            out.writeInt(1);
            m_runtimeArgs.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_portInstDescriptor != null)
        {
            out.writeInt(1);
            m_portInstDescriptor.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_params != null)
        {
            int length = m_params.size();

            out.writeInt(length);

            Enumeration params = m_params.elements();

            while (params.hasMoreElements())
            {
                Param param = (Param) params.nextElement();

                param.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        if (m_statusTracking != null)
        {
            out.writeInt(1);
            m_statusTracking.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_vecEndStates != null)
        {
            int size = m_vecEndStates.size();

            out.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                ((EndState) m_vecEndStates.get(i)).toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        if (m_nodes != null)
        {
            int size = m_nodes.size();

            out.writeInt(size);

            Enumeration keys = m_nodes.keys();
            Enumeration values = m_nodes.elements();

            while (keys.hasMoreElements())
            {
                UTFReaderWriter.writeUTF(out, (String) keys.nextElement());
                UTFReaderWriter.writeUTF(out, (String) values.nextElement());
            }
        }
        else
            out.writeInt(0);

        if (m_monitor != null)
        {
            out.writeInt(1);
            m_monitor.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_logModules != null)
        {
            out.writeInt(1);
            m_logModules.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        // Log Manager and log params
        writeUTF(out, m_logManager);
        writeUTF(out, m_profile);
        if (m_logParams != null)
        {
            int length = m_logParams.size();

            out.writeInt(length);

            Enumeration logParams = m_logParams.elements();

            while (logParams.hasMoreElements())
            {
                Param logParam = (Param) logParams.nextElement();

                logParam.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        //  Event Parameters.
        writeUTF(out, m_eventDeliveryMode);
        out.writeLong(m_eventExpiryTime);
        out.writeInt(m_eventHandler);

   }


    /**
     *  This method reads this object <code>ServiceInstance</code> from the
     *  specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_isManualLaunch = is.readBoolean();

        m_isStateful = is.readBoolean();

        m_isDelayedLaunch = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp == null)
            m_delayedPortName = "";
        else
            m_delayedPortName = temp;

        m_isTransacted = is.readBoolean();

        m_isErrorHandlingEnabled = is.readBoolean();

        m_isVersionLocked = is.readBoolean();

        m_isEndOfWorkflow = is.readBoolean();

        m_isInMemoryLaunch = is.readBoolean();

        m_isTransportLPC = is.readBoolean();

        m_bPreferLaunchOnHigherLevelNode = is.readBoolean();

        m_bIsDurableSubscription = is.readBoolean();

        m_bIsDurableConnection = is.readBoolean();

        m_bKillPrimaryOnSecondaryLaunch = is.readBoolean();

        m_bIsDebugMode = is.readBoolean();

        m_iDebugPort = is.readInt();

        m_maxRetries = is.readInt();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_version = null;
        else
            m_version = temp;

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
            m_longDescription = null;
        else
            m_longDescription = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_shortDescription = null;
        else
            m_shortDescription = temp;

        // Service Dependency
        int tempInt = is.readInt();

        for (int i = 0; i < tempInt; ++i)
        {
            RuntimeDependency serv = new RuntimeDependency();

            serv.fromStream(is, versionNo);
            m_runtimeDependencies.addElement(serv);
        }

        tempInt = is.readInt();

        if (tempInt != 0)
        {
            m_runtimeArgs = new RuntimeArgs();
            m_runtimeArgs.fromStream(is, versionNo);
        }

        tempInt = is.readInt();
        if (tempInt != 0)
        {
            m_portInstDescriptor = new PortInstDescriptor();
            m_portInstDescriptor.fromStream(is, versionNo);
        }

        if ((tempInt = is.readInt()) != 0)
            for (int i = 0; i < tempInt; i++)
            {
                Param param = new Param();

                param.fromStream(is, versionNo);
                m_params.add(param);
            }

        tempInt = is.readInt();
        if (tempInt != 0)
        {
            m_statusTracking = new StatusTracking();
            m_statusTracking.fromStream(is, versionNo);
        }

        tempInt = is.readInt();

        if (tempInt != 0)
        {
            m_vecEndStates = new Vector();
            for (int i = 0; i < tempInt; i++)
            {
                EndState endState = new EndState();

                endState.fromStream(is, versionNo);
                m_vecEndStates.add(endState);

            }
        }

        tempInt = is.readInt();
        if (tempInt != 0)
            for (int i = 0; i < tempInt; i++)
                m_nodes.put(UTFReaderWriter.readUTF(is), UTFReaderWriter.readUTF(is));

        tempInt = is.readInt();
        if (tempInt != 0)
        {
            m_monitor = new Monitor();
            m_monitor.fromStream(is, versionNo);
        }

        tempInt = is.readInt();
        if (tempInt != 0)
        {
            m_logModules = new LogModules();
            m_logModules.fromStream(is, versionNo);
        }

        // Log Manager and log params
        m_logManager = readUTF(is);
        m_profile = readUTF(is);

        if ((tempInt = is.readInt()) != 0)
            for (int i = 0; i < tempInt; i++)
            {
                Param param = new Param();

                param.fromStream(is, versionNo);
                m_logParams.add(param);
            }

        //  Event Parameters.
        m_eventDeliveryMode = readUTF(is);
        m_eventExpiryTime = is.readLong();
        m_eventHandler = is.readInt();
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>ServiceInstance</code>.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Service Instance Details ");
        strBuf.append("[");
        strBuf.append("Is Manual Launch = ");
        strBuf.append(m_isManualLaunch);
        strBuf.append(", ");
        strBuf.append("Is StateFule = ");
        strBuf.append(m_isStateful);
        strBuf.append(", ");
        strBuf.append("Is Delayed Launch = ");
        strBuf.append(m_isDelayedLaunch);
        strBuf.append(", ");
        strBuf.append("Delayed Port = ");
        strBuf.append(m_delayedPortName);
        strBuf.append(", ");
        strBuf.append("Is Transacted = ");
        strBuf.append(m_isTransacted);
        strBuf.append(", ");
        strBuf.append("Is Error handling Enabled = ");
        strBuf.append(m_isErrorHandlingEnabled);
        strBuf.append(", ");
        strBuf.append("Is Version Locked = ");
        strBuf.append(m_isVersionLocked);
        strBuf.append(", ");
        strBuf.append("Is in Memory launch = ");
        strBuf.append(m_isInMemoryLaunch);
        strBuf.append(", ");
        strBuf.append("Is Primary Node Preferred for launch = ");
        strBuf.append(m_bPreferLaunchOnHigherLevelNode);
        strBuf.append(", ");
        strBuf.append("Is Durable Subscription = ");
        strBuf.append(m_bIsDurableSubscription);
        strBuf.append(", ");
        strBuf.append("Is Durable Connection = ");
        strBuf.append(m_bIsDurableConnection);
        strBuf.append(", ");

        strBuf.append("Is Primary Killed On Secondary Launch = ");
        strBuf.append(m_bKillPrimaryOnSecondaryLaunch);
        strBuf.append(", ");
        strBuf.append("Is Debug Mode = ");
        strBuf.append(m_bIsDebugMode);
        strBuf.append(", ");
        strBuf.append("Debug Port specified = ");
        strBuf.append(m_iDebugPort);
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
        strBuf.append("Short Description = ");
        strBuf.append(m_shortDescription);
        strBuf.append(", ");
        strBuf.append("Long Description = ");
        strBuf.append(m_longDescription);
        strBuf.append(", ");
        strBuf.append("Log Manager = ");
        strBuf.append(m_logManager);
        strBuf.append(", ");
        strBuf.append("Profile Name = ");
        strBuf.append(m_profile);
        strBuf.append(", ");
        strBuf.append("Event Handler Index = ");
        strBuf.append(m_eventHandler);
        strBuf.append(", ");
        strBuf.append("Event DeliveryMode = ");
        strBuf.append(m_eventDeliveryMode);
        strBuf.append(", ");
        strBuf.append("Event Expiry Time = ");
        strBuf.append(m_eventExpiryTime);
        strBuf.append(", ");
        strBuf.append("BufferSizePerPort = ");
        strBuf.append(m_dBufferSizePerPort);
        strBuf.append(", ");
        strBuf.append("Maximum retries = ");
        strBuf.append(String.valueOf(m_maxRetries));
        strBuf.append(", ");

        if (m_runtimeDependencies != null)
        {
            strBuf.append("Runtime dependencies = ");
            for (int i = 0; i < m_runtimeDependencies.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((RuntimeDependency) m_runtimeDependencies.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }

        if (m_runtimeArgs != null)
        {
            strBuf.append("RunTime Arguments = ");
            strBuf.append(m_runtimeArgs);
            strBuf.append(", ");
        }

        if (m_portInstDescriptor != null)
        {
            strBuf.append("Port Instance Descriptor = ");
            strBuf.append(m_portInstDescriptor);
            strBuf.append(", ");
        }

        if (m_params != null)
        {
            strBuf.append("Parameters = ");
            for (int i = 0; i < m_params.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_params.elementAt(i));
                strBuf.append(", ");
            }
        }
        if (m_vecEndStates != null)
        {
            strBuf.append("End State = ");
            for (int i = 0; i < m_vecEndStates.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_vecEndStates.get(i));
                strBuf.append(", ");
            }
        }
        if (m_statusTracking != null)
        {
            strBuf.append("Status Tracking = ");
            strBuf.append(m_statusTracking);
            strBuf.append(", ");
        }
        if (m_monitor != null)
        {
            strBuf.append("EventModules = ");
            strBuf.append(m_monitor);
            strBuf.append(", ");
        }
        if (m_logModules != null)
        {
            strBuf.append("LogModules = ");
            strBuf.append(m_logModules);
            strBuf.append(", ");
        }
        if (m_nodes != null)
        {
            int size = m_nodes.size();
            int i = 0;

            strBuf.append("Nodes =");

            Enumeration keys = m_nodes.keys();
            Enumeration values = m_nodes.elements();

            while (keys.hasMoreElements())
            {
                i++;
                strBuf.append(i + "key = ");
                strBuf.append(keys.nextElement());
                strBuf.append(", version =");
                strBuf.append(values.nextElement());
            }
        }
        if (m_logManager != null)
        {
            strBuf.append("LogManager = ");
            strBuf.append(m_logManager);
            strBuf.append(", ");
            if (m_logParams != null && m_logParams.size() > 0)
            {
                strBuf.append("LogManager Parameters = ");
                for (int j = 0; j < m_logParams.size(); j++)
                {
                    strBuf.append((j + 1) + ". ");
                    strBuf.append(m_logParams.elementAt(j));
                    strBuf.append(", ");
                }
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  Returns the XML string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("ServiceInstance");

        ((Element) root0).setAttribute("isManualLaunch", "" + m_isManualLaunch);
        ((Element) root0).setAttribute("isStateful", "" + m_isStateful);
        ((Element) root0).setAttribute("isDelayedLaunch", "" + m_isDelayedLaunch);
        ((Element) root0).setAttribute("delayedPort", "" + m_delayedPortName);
        ((Element) root0).setAttribute("maxRetries", "" + m_maxRetries);
        ((Element) root0).setAttribute("isTransacted", "" + m_isTransacted);
        ((Element) root0).setAttribute("isErrorHandlingEnabled", "" + m_isErrorHandlingEnabled);
        ((Element) root0).setAttribute("isInMemoryLaunch", "" + m_isInMemoryLaunch);
        ((Element) root0).setAttribute("isEndOfWorkflow", "" + m_isEndOfWorkflow);
        ((Element) root0).setAttribute("preferLaunchOnHigherLevelNode", "" + m_bPreferLaunchOnHigherLevelNode);
        //((Element) root0).setAttribute("durableSubscription", "" + m_bIsDurableSubscription);
        //((Element) root0).setAttribute("durableConnection", "" + m_bIsDurableConnection);
        ((Element) root0).setAttribute("killPrimaryOnSecondaryLaunch", "" + m_bKillPrimaryOnSecondaryLaunch);
        ((Element) root0).setAttribute("isDebugMode", "" + m_bIsDebugMode);
        ((Element) root0).setAttribute("debugPort", "" + m_iDebugPort);
        ((Element) root0).setAttribute("isTransportLPC", "" + m_isTransportLPC);
        ((Element) root0).setAttribute("profile", "" + m_profile);

        Node node = XMLDmiUtil.getNodeObject("ServiceInstanceName", m_servInstName, document);

        if (node != null)
            root0.appendChild(node);

        Node node1 = XMLDmiUtil.getNodeObject("ServiceGUID", m_servGUID, document);

        if (node1 != null)
            root0.appendChild(node1);

        Node nodeN = XMLDmiUtil.getNodeObject("BufferSizePerPort", m_dBufferSizePerPort + "", document);

        if (nodeN != null)
            root0.appendChild(nodeN);

        Element child = null;

        if (m_version != null)
        {
            child = document.createElement("Version");
            ((Element) child).setAttribute("isLocked", "" + m_isVersionLocked);

            Node pcData = (Node) document.createTextNode(m_version);

            child.appendChild(pcData);
            root0.appendChild(child);
        }
        if (m_nodes != null && m_nodes.size() > 0)
        {
            Enumeration nodeNameEnum = m_nodes.keys();
            Enumeration nodeLevelEnum = m_nodes.elements();

            while (nodeNameEnum.hasMoreElements())
            {
                String nodeName = (String) nodeNameEnum.nextElement();
                String nodeLevel = (String) nodeLevelEnum.nextElement();

                child = document.createElement("Node");
                ((Element) child).setAttribute("level", nodeLevel);

                Node pcData = (Node) document.createTextNode(nodeName);

                child.appendChild(pcData);
                root0.appendChild(child);
            }
        }
        if (m_eventHandler > 0)
        {
            child = document.createElement("EventHandler");
            ((Element) child).setAttribute("deliveryMode", "" + m_eventDeliveryMode);
            ((Element) child).setAttribute("expiryTime", "" + m_eventExpiryTime);

            Node pcData = (Node) document.createTextNode("" + m_eventHandler);

            child.appendChild(pcData);
            root0.appendChild(child);
        }
        if (m_runtimeDependencies != null && m_runtimeDependencies.size() > 0)
        {
            Enumeration _enum = m_runtimeDependencies.elements();

            while (_enum.hasMoreElements())
            {
                RuntimeDependency runtimeDependency = (RuntimeDependency) _enum.nextElement();
                Node serviceDepNode = runtimeDependency.toJXMLString(document);

                root0.appendChild(serviceDepNode);
            }
        }
        if (m_runtimeArgs != null)
        {
            Node argsNode = m_runtimeArgs.toJXMLString(document);

            root0.appendChild(argsNode);
        }
        if (m_portInstDescriptor != null)
        {
            Node argsNode = m_portInstDescriptor.toJXMLString(document);

            root0.appendChild(argsNode);
        }

        if(!StringUtils.isEmpty(m_longDescription)){
            Node node2 = XMLDmiUtil.getNodeObject("LongDescription", m_longDescription, document);

            if (node2 != null)
                root0.appendChild(node2);
        }
        if(!StringUtils.isEmpty(m_shortDescription)){
            Node node3 = XMLDmiUtil.getNodeObject("ShortDescription", m_shortDescription, document);

            if (node3 != null)
                root0.appendChild(node3);
        }
        // Log Manager
        if (m_logManager != null)
        {
            Node node12 = (Node) document.createElement("LogManager");

            if (node12 != null)
            {
                Node nameNode = XMLDmiUtil.getNodeObject("Name", m_logManager, document);

                node12.appendChild(nameNode);

                if (m_logParams != null && m_logParams.size() > 0)
                {
                    Enumeration enums = m_logParams.elements();

                    while (enums.hasMoreElements())
                    {
                        Param param = (Param) enums.nextElement();
                        if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                            node12.appendChild(param.toJXMLString(document));
                    }
                }
                root0.appendChild(node12);
            }
        }

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    root0.appendChild(param.toJXMLString(document));
            }
        }
        if (m_statusTracking != null)
        {
            Node pcData = m_statusTracking.toJXMLString(document);

            root0.appendChild(pcData);
        }

        if (m_vecEndStates != null)
        {
            for (int i = 0; i < m_vecEndStates.size(); i++)
            {
                EndState endState = (EndState) m_vecEndStates.get(i);
                Node pcData = endState.toJXMLString(document);

                root0.appendChild(pcData);
            }
        }
        if (m_monitor != null)
        {
            Node pcData = m_monitor.toJXMLString(document);

            root0.appendChild(pcData);
        }
        if (m_logModules != null)
        {
            Node pcData = m_logModules.toJXMLString(document);

            root0.appendChild(pcData);
        }
        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException,
      FioranoException
    {

        //Start ServiceInstance
        writer.writeStartElement("ServiceInstance");

        //Write Attributes
        writer.writeAttribute("isManualLaunch", "" + m_isManualLaunch);
        writer.writeAttribute("isStateful", "" + m_isStateful);
        writer.writeAttribute("isDelayedLaunch", "" + m_isDelayedLaunch);
        writer.writeAttribute("delayedPort", "" + m_delayedPortName);
        writer.writeAttribute("maxRetries", "" + m_maxRetries);
        writer.writeAttribute("isTransacted", "" + m_isTransacted);
        writer.writeAttribute("isErrorHandlingEnabled", "" + m_isErrorHandlingEnabled);
        writer.writeAttribute("isInMemoryLaunch", "" + m_isInMemoryLaunch);
        writer.writeAttribute("isEndOfWorkflow", "" + m_isEndOfWorkflow);
        writer.writeAttribute("preferLaunchOnHigherLevelNode", "" + m_bPreferLaunchOnHigherLevelNode);
        writer.writeAttribute("killPrimaryOnSecondaryLaunch", "" + m_bKillPrimaryOnSecondaryLaunch);
        writer.writeAttribute("isDebugMode", "" + m_bIsDebugMode);
        writer.writeAttribute("debugPort", "" + m_iDebugPort);
        writer.writeAttribute("isTransportLPC", "" + m_isTransportLPC);
        writer.writeAttribute("profile", "" + m_profile);


        FioranoStackSerializer.writeElement("ServiceInstanceName", m_servInstName, writer);
        FioranoStackSerializer.writeElement("ServiceGUID", m_servGUID, writer);
        FioranoStackSerializer.writeElement("BufferSizePerPort", m_dBufferSizePerPort + "", writer);

        if (m_version != null)
        {
            writer.writeStartElement("Version");
            writer.writeAttribute("isLocked", "" + m_isVersionLocked);
            writer.writeCharacters(m_version);
            writer.writeEndElement();
        }
        if (m_nodes != null && m_nodes.size() > 0)
        {
            Enumeration nodeNameEnum = m_nodes.keys();
            Enumeration nodeLevelEnum = m_nodes.elements();

            while (nodeNameEnum.hasMoreElements())
            {
                String nodeName = (String) nodeNameEnum.nextElement();
                String nodeLevel = (String) nodeLevelEnum.nextElement();
                writer.writeStartElement("Node");
                writer.writeAttribute("level", nodeLevel);
                writer.writeCharacters(nodeName);
                writer.writeEndElement();
            }
        }
        if (m_eventHandler > 0)
        {
            writer.writeStartElement("EventHandler");
            writer.writeAttribute("deliveryMode", "" + m_eventDeliveryMode);
            writer.writeAttribute("expiryTime", "" + m_eventExpiryTime);
            writer.writeCharacters("" + m_eventHandler);
            writer.writeEndElement();
        }
        if (m_runtimeDependencies != null && m_runtimeDependencies.size() > 0)
        {
            Enumeration enums = m_runtimeDependencies.elements();

            while (enums.hasMoreElements())
            {
                RuntimeDependency runtimeDependency = (RuntimeDependency) enums.nextElement();
                runtimeDependency.toJXMLString(writer);
            }
        }
        if (m_runtimeArgs != null)
        {
            m_runtimeArgs.toJXMLString(writer);
        }
        if (m_portInstDescriptor != null)
        {
            m_portInstDescriptor.toJXMLString(writer);
        }

        if(!StringUtils.isEmpty(m_longDescription)){
            FioranoStackSerializer.writeElement("LongDescription", m_longDescription, writer);

        }
        if(!StringUtils.isEmpty(m_shortDescription)){
            FioranoStackSerializer.writeElement("ShortDescription", m_shortDescription, writer);

        }
        //LogManager
        if (m_logManager != null)
        {
            //Start LogMangaer
            writer.writeStartElement("LogManager");

            FioranoStackSerializer.writeElement("Name", m_logManager, writer);

            if (m_logParams != null && m_logParams.size() > 0)
            {
                Enumeration enums = m_logParams.elements();
                while (enums.hasMoreElements())
                {
                    Param param = (Param) enums.nextElement();
                    if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    {
                        param.toJXMLString(writer);
                    }
                }
            }
            //End LogManager
            writer.writeEndElement();
        }
        if (m_params != null && m_params.size() > 0)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    param.toJXMLString(writer);
            }
        }
        if (m_statusTracking != null)
        {
            m_statusTracking.toJXMLString(writer);
        }
        if (m_vecEndStates != null)
        {
            for (int i = 0; i < m_vecEndStates.size(); i++)
            {
                EndState endState = (EndState) m_vecEndStates.get(i);
                endState.toJXMLString(writer);

            }
        }
        if (m_monitor != null)
        {
            m_monitor.toJXMLString(writer);
        }
        if (m_logModules != null)
        {
            m_logModules.toJXMLString(writer);

        }

        //End ServiceInstance
        writer.writeEndElement();


    }

    /**
     *  This method finds out if the passed EndState is added already Or not
     *
     * @param state
     * @return boolean stating whether end state already exist or not.
     */
    private boolean Isduplicate(EndState state)
    {
        Enumeration _enum = m_vecEndStates.elements();

        while (_enum.hasMoreElements())
        {
            EndState end = (EndState) _enum.nextElement();

            if (end.getStateID().equalsIgnoreCase(state.getStateID()))
            {
                return true;
            }
        }
        return false;
    }

}
