/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Execution extends DmiObject
{
    String          m_strExecType;

    boolean         m_bIsFailOverSupported;

    boolean         m_bIsExternallyLaunchable;

    boolean         m_bIsAutoLaunchable;

    boolean         m_bIsInMemoryLaunchable;

    String          m_strTransactionType;

    String          m_strExecDir;

    String          m_strExecutableName;

    Vector          m_LogModules = new Vector();

    Vector          m_eventModules = new Vector();

    boolean         m_bIsStatusTrackable;

    Vector          m_trackableStates = new Vector();

    boolean         m_bIsVariableArgsSupported;

    boolean         m_bIsRuntimeArgsChangeSupported;

    String          m_strUserDefinedPropertySheet;

    boolean         m_bIsConfigurationRequired = false;

    String          m_strUserDefinedPropertySheetValue;

    Vector          m_runtimeArgs = new Vector();

    Vector          m_params = new Vector();

    // default values for manual launch and inMemoryLaunch
    private boolean m_bDefaultManualLaunch = false;
    private boolean m_bDefaultInMemoryLaunch = false;

    String          m_inMemoryLaunchImpl;


    /**
     *  Constructor for the Execution object
     *
     * @since Tifosi2.0
     */
    public Execution()
    {
        reset();
    }


    /**
     *   This interface method is called to get the execution type for the service, about
     *   which this object of <code>Execution </code> represents execution information.
     *
     * @return execution type for the service
     * @see #setExecType(String)
     * @since Tifosi2.0
     */
    public String getExecType()
    {
        return m_strExecType;
    }


    /**
     *   This interface method is called to check whether failover is supported or not
     *   for the service, about which this object of <code>Execution </code>
     *   represents execution information.
     *
     * @return true if  failOver is Supported for service, false otherwise.
     * @see #setFailOverSupported(boolean)
     * @since Tifosi2.0
     */
    public boolean isFailOverSupported()
    {
        return m_bIsFailOverSupported;
    }

    /**
     * This interface method is called to get the value of the boolean
     * specifying the default value for the external launch for this service.
     *
     * @return true if service is to be launched manually by default else false
     * @see #setDefaultForManualLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isDefaultManualLaunch()
    {
        return m_bDefaultManualLaunch;
    }

    /**
     * This interface method is called to get the value of the boolean
     * specifying the default value for the in memory launch for this service.
     *
     * @return true if service is to be launched in memory by default else false
     * @see #setDefaultForInMemoryLaunch(boolean)
     * @since Tifosi2.0
     */
    public boolean isDefaultInMemoryLaunch()
    {
        return m_bDefaultInMemoryLaunch;
    }

    /**
     * This Method fetches the Impl class implementing the IInMemoryShutdownable interface.
     * This impl is used to start/stop the component when the component is launched in-memory.
     *
     * @return Impl class which will be used to start/ stop the component in case the component is launched in-memory.
     */
    public String getInMemoryLaunchImpl()
    {
        return m_inMemoryLaunchImpl;
    }


    /**
     *  This interface method is called to check whether the service about which this
     *  object of <code>Execution </code> represents execution information, is
     *  externally launchable or not.
     *
     * @return true if service is externally launchable, false otherwise.
     * @see #setExternallyLaunchable(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isExternallyLaunchable()
    {
        return m_bIsExternallyLaunchable;
    }
    /**
     *  This interface method is called to check whether the service about which this
     *  object of <code>Execution </code> represents execution information, is
     *  externally launchable or not.
     *
     * @return true if service is externally launchable, false otherwise.
     * @see #setExternallyLaunchable(boolean)
     * @since Tifosi2.0
     */
    public boolean isManuallyLaunchable()
    {
        return m_bIsExternallyLaunchable;
    }


    /**
     *  This interface method is called to check whether the service about which this
     *  object of <code>Execution </code> represents execution information, is
     *  auto launchable or not.
     *
     * @return true if service is auto launchable, false otherwise.
     * @see #setAutoLaunchable(boolean)
     * @since Tifosi2.0
     */
    public boolean isAutoLaunchable()
    {
        return m_bIsAutoLaunchable;
    }

    /**
     *  This interface method is called to check whether the service about which this
     *  object of <code>Execution </code> represents execution information, is
     *  launchable in memory or not
     *
     * @return true if service is launchable in memory, false otherwise.
     * @see #setInMemoryLaunchable(boolean)
     * @since Tifosi2.0
     */
    public boolean isInMemoryLaunchable()
    {
        return m_bIsInMemoryLaunchable;
    }


    /**
     *  This interface method is called to get the type of transaction supported
     *  by the service, about which this object of <code>Execution </code> represents
     *  execution information.
     *
     * @return The transaction Type supported by service
     * @see #setTransactionType(String)
     * @since Tifosi2.0
     */
    public String getTransactionType()
    {
        return m_strTransactionType;
    }


    /**
     *  This interface method is called to get the execution directory for the service,
     *  about which this object of <code>Execution </code> represents execution
     *  information.
     *
     * @return The execution directory for the service
     * @see #setExecDir(String)
     * @since Tifosi2.0
     */
    public String getExecDir()
    {
        return m_strExecDir;
    }


    /**
     * This interface method is called to get the executable for the service, about
     * which this object of <code>Execution </code> represents execution
     * information.
     *
     * @return The executable for string
     * @see #setExecutable(String)
     * @since Tifosi2.0
     */
    public String getExecutable()
    {
        return m_strExecutableName;
    }

    /**
     *  This interface method is called to get enumeration of list of log modules,
     *  for the service about which this object of <code>Execution </code> represents
     *  execution information.
     *
     * @return Enumeration of  LogModules for this service.
     * @see #addLogModuleInfo(LogModule)
     * @see #removeLogModuleInfo(LogModule)
     * @see #clearLogModuleInfo()
     * @since Tifosi2.0
     */
    public Enumeration getLogModules()
    {
        if (m_LogModules == null)
        {
            m_LogModules = new Vector();
        }
        return m_LogModules.elements();
    }

    /**
     *  This interface method is called to get enumeration of list of event modules,
     *  for the service about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @return Enumeration of  EventModules for this service.
     * @see #addEventModuleInfo(EventModule)
     * @see #removeEventModuleInfo(EventModule)
     * @see #clearEventModuleInfo()
     * @since Tifosi2.0
     */
    public Enumeration getEventModules()
    {
        if (m_eventModules == null)
        {
            m_eventModules = new Vector();
        }
        return m_eventModules.elements();
    }


    /**
     *  This interface method is called to check whether the status of service
     *  about which this object of <code>Execution </code> represents
     *  execution information, is trackable or not.
     *
     * @return true if the service is statusTrackable, false otherwise
     * @see #setStatusTrackable(boolean)
     * @since Tifosi2.0
     */
    public boolean isStatusTrackable()
    {
        return m_bIsStatusTrackable;
    }


    /**
     *  This interface method is called to get the enumeration of the list of
     *  trackable states, for the service about which this object of
     *  <code>Execution</code> represents execution information.
     *
     * @return Enumeration of trackable States for service
     * @see #addTrackableState(String)
     * @see #removeTrackableState(String)
     * @see #clearTrackableState()
     * @since Tifosi2.0
     */
    public Enumeration getTrackableStates()
    {
        if (m_trackableStates == null)
        {
            m_trackableStates = new Vector();
        }
        return m_trackableStates.elements();
    }


    /**
     *  This interface method is called to check whether variable arguments are
     *  supported or not for the service, about which this object of <code>Execution</code>
     *  represents execution information, is trackable or not.
     *
     * @return true if variable Arguments are Supported by service, false otherwise.
     * @see #setVariableArgsSupported(boolean)
     * @since Tifosi2.0
     */
    public boolean isVariableArgsSupported()
    {
        return m_bIsVariableArgsSupported;
    }

    /**
     *  This interface method is called to check the boolean specifying whether
     *  runtime changes to the argument is supported in the service
     *  about which this object of <code>Execution</code> represents execution
     *  information.
     *
     * @return true if runtimearg change is supported else false
     * @see #setRuntimeArgsChangeSupport(boolean)
     * @since Tifosi2.0
     */
    public boolean isRuntimeArgsChangeSupported()
    {
        return m_bIsRuntimeArgsChangeSupported;
    }


    /**
     * This interface method is called to get enumeration of all the runtime arguments
     * defined for the service about which this object of <code>Execution</code>
     * represents execution information.
     *
     * @return Enumeration of Runtime arguments
     * @see #addRuntimeArg(RuntimeArg)
     * @see #removeRuntimeArg(RuntimeArg)
     * @see #clearRuntimeArg()
     * @see #getNumberOfRuntimeArgs()
     * @since Tifosi2.0
     */
    public Enumeration getRuntimeArgs()
    {
        if (m_runtimeArgs == null)
        {
            m_runtimeArgs = new Vector();
        }
        return m_runtimeArgs.elements();
    }

    /**
     *  This interface is used to get the total number of runtime arguments specified
     *  for the service about which this object of <code>Execution</code>
     *  represents execution information.
     *
     * @return The total number of Runtime arguments
     * @see #addRuntimeArg(RuntimeArg)
     * @see #removeRuntimeArg(RuntimeArg)
     * @see #clearRuntimeArg()
     * @see #getRuntimeArgs()
     * @since Tifosi2.0
     */
    public int getNumberOfRuntimeArgs()
    {
        return m_runtimeArgs.size();
    }


    /**
     *  This interface method is called to get the user-defined property sheet for the
     *  service, about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @return The userDefinedPropertySheet value for service
     * @see #setUserDefinedPropertySheet(String)
     * @since Tifosi2.0
     */
    public String getUserDefinedPropertySheet()
    {
        return m_strUserDefinedPropertySheet;
    }

    /**
     * @return true to indicate that CPS configuration is mandatory before
     *         using this component in a flow.
     */
    public boolean isConfigurationRequired()
    {
        return m_bIsConfigurationRequired;
    }

    /**
     *  This interface method is called to get the value of user-defined property sheet
     *  for the service, about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @return The value of userDefinedPropertySheet
     * @see #setUserDefinedPropertySheetValue(String)
     * @since Tifosi2.0
     */
    public String getUserDefinedPropertySheetValue()
    {
        return m_strUserDefinedPropertySheetValue;
    }


    /**
     *  Interface to get vector of all extra parameters defined for the
     *  service, about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @return Vector of objects of Param
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #clearParams()
     * @since Tifosi2.0
     */
    public Vector getParams()
    {
        return m_params;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.EXECUTION;
    }


    /**
     *  This interface method is called to set the specified string as execution type for
     *  the service, about which this object of <code>Execution </code> represents
     *  execution information.
     *
     * @param execType The string to be set as execution type for the service
     * @see #getExecType()
     * @since Tifosi2.0
     */
    public void setExecType(String execType)
    {
        m_strExecType = execType;
    }


    /**
     *  This interface method is called to set the boolean specifying whether failover
     *  is supported or not for the service, about which this object of
     *  <code>Execution </code> represents execution information.
     *
     * @param isFailOverSupported boolean specifying whether failover is supported
     *                              or not for service.
     * @see #isFailOverSupported()
     * @since Tifosi2.0
     */
    public void setFailOverSupported(boolean isFailOverSupported)
    {
        m_bIsFailOverSupported = isFailOverSupported;
    }


    /**
     * This interface method is called to set the boolean specifying the default
     * value for the external launch for this service.
     *
     * @param defForManualLaunch if this is true, then the service by default is
     *                          to be launched externally else automatically.
     * @see #isDefaultManualLaunch()
     * @since Tifosi2.0
     */
    public void setDefaultForManualLaunch(boolean defForManualLaunch)
    {
        m_bDefaultManualLaunch = defForManualLaunch;
    }


    /**
     * This interface method is called to set the boolean specifying the default
     * value for the in memory launch for this service.
     *
     * @param defForInMemoryLaunch if this is true, then the service by default
     *                is to be launched in memory else out process.
     * @see #isDefaultInMemoryLaunch()
     * @since Tifosi2.0
     */
    public void setDefaultForInMemoryLaunch(boolean defForInMemoryLaunch)
    {
        m_bDefaultInMemoryLaunch = defForInMemoryLaunch;
    }

    /**
     * This methos sets the impl class of the component implmenting the InMemoryLaunchable interface.
     * This impl class will be used for starting / stopping the component if th component is launched in-memory.
     *
     * @param impl
     */
    public void setInMemoryLaunchImpl(String impl)
    {
        m_inMemoryLaunchImpl = impl;
    }

    /**
     * This interface method is called to set the boolean specifying whether the service
     * about which this object of <code>Execution </code> represents execution
     * information, is externally launchable or not.
     *
     * @param externallyLaunchable boolean specifying whether service is
     *                                  externally launchable or not.
     * @see #isExternallyLaunchable()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setExternallyLaunchable(boolean externallyLaunchable)
    {
        m_bIsExternallyLaunchable = externallyLaunchable;
    }
    /**
     * This interface method is called to set the boolean specifying whether the service
     * about which this object of <code>Execution </code> represents execution
     * information, is externally launchable or not.
     *
     * @param externallyLaunchable boolean specifying whether service is
     *                                  externally launchable or not.
     * @see #isExternallyLaunchable()
     * @since Tifosi2.0
     */
    public void setManuallyLaunchable(boolean externallyLaunchable)
    {
        m_bIsExternallyLaunchable = externallyLaunchable;
    }


    /**
     * This interface method is called to set the boolean specifying whether the service
     * about which this object of <code>Execution </code> represents execution
     * information, is auto launchable or not.
     *
     * @param autoLaunchPossible boolean specifying whether service is
     *                             auto launchable or not.
     * @see #isAutoLaunchable()
     * @since Tifosi2.0
     */
    public void setAutoLaunchable(boolean autoLaunchPossible)
    {
        m_bIsAutoLaunchable = autoLaunchPossible;
    }

    /**
     * This interface method is called to set the boolean specifying whether the service
     * about which this object of <code>Execution </code> represents execution
     * information, can be launched in-memory or not
     *
     * @param inMemoryLaunchable boolean specifying whether service is
     *                             launchable in memory or not
     * @see #isInMemoryLaunchable()
     * @since Tifosi2.0
     */
    public void setInMemoryLaunchable(boolean inMemoryLaunchable)
    {
        m_bIsInMemoryLaunchable = inMemoryLaunchable;
    }


    /**
     *  This interface method is called to set the type of transaction to be supported
     *  by the service, about which this object of <code>Execution </code>
     *  represents execution information.
     *
     * @param transactionType The string to be set as transaction type for service.
     * @see #getTransactionType()
     * @since Tifosi2.0
     * @see fiorano.esb.adapter.jca.ra.dmi.RAConstants
     * @jmx.descriptor name="legalValues" value="RAConstants.NoTransaction,RAConstants.LocalTransaction,RAConstants.XATransaction"
     */
    public void setTransactionType(String transactionType)
    {
        m_strTransactionType = transactionType;
    }


    /**
     *  This interface method is called to set the specified string as execution
     *  directory for the service, about which this object
     *  of <code>Execution </code> represents execution information.
     *
     * @param execDir The string to be set as execution directory.
     * @see #getExecDir()
     * @since Tifosi2.0
     */
    public void setExecDir(String execDir)
    {
        m_strExecDir = execDir;
    }


    /**
     *  This interface method is called to set the specified string as executable for
     *  the service, about which this object of <code>Execution </code> represents
     *  execution information.
     *
     * @param executable The string to be set as executable for the service.
     * @see #getExecutable()
     * @since Tifosi2.0
     */
    public void setExecutable(String executable)
    {
        m_strExecutableName = executable;
    }


    /**
     *  This interface method is called to set boolean specifying whether the status of
     *  service about which this object of <code>Execution </code> represents
     *  execution information, is trackable or not.
     *
     * @param flag boolean specifying whether the status of service is
     *                  trackable or not
     * @see #isStatusTrackable()
     * @since Tifosi2.0
     */
    public void setStatusTrackable(boolean flag)
    {
        m_bIsStatusTrackable = flag;
    }


    /**
     *  This interface method is called to set a boolean specifying whether variable arguments
     *  are supported or not for the service, about which this object of <code>Execution</code>
     *  represents execution information, is trackable or not.
     *
     * @param flag boolean specifying whether the variable arguments are
     *                  supported by the service or not
     * @see #isVariableArgsSupported()
     * @since Tifosi2.0
     */
    public void setVariableArgsSupported(boolean flag)
    {
        m_bIsVariableArgsSupported = flag;
    }


    /**
     *  This interface method is called to set a boolean specifying whether
     *  runtime changes to the argument is supported in the service
     *  about which this object of <code>Execution</code> represents execution
     *  information.
     *
     * @param flag boolean specifying whether runtime change to the
     * arguments is supported or not.
     * @see #isRuntimeArgsChangeSupported()
     * @since Tifosi2.0
     */
    public void setRuntimeArgsChangeSupport(boolean flag)
    {
        m_bIsRuntimeArgsChangeSupported = flag;
    }


    /**
     * This interface method is called to set the specified string as user defined
     * propertysheet for the service, about which this object of <code>Execution</code>
     * represents execution information.
     *
     * @param ups The string to be set as userDefinedPropertySheet for the service
     * @see #getUserDefinedPropertySheet()
     * @since Tifosi2.0
     */
    public void setUserDefinedPropertySheet(String ups)
    {
        m_strUserDefinedPropertySheet = ups;
    }

    /**
     * Sets configuration required for object
     *
     * @param value
     */
    public void setConfigurationRequired(boolean value)
    {
        m_bIsConfigurationRequired = value;
    }

    /**
     * This interface method is called to set the specified string as value of
     * user defined propertysheet for the service, about which this object
     * of <code>Execution</code> represents execution information.
     *
     * @param val The string to be set as value of userDefinedPropertySheet
     * @see #getUserDefinedPropertySheetValue()
     * @since Tifosi2.0
     */
    public void setUserDefinedPropertySheetValue(String val)
    {
        m_strUserDefinedPropertySheetValue = val;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>Execution</code>, using the specified XML string.
     *
     * @param execution
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element execution)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element execution = doc.getDocumentElement();

        if (execution != null)
        {
            m_strExecType = execution.getAttribute("type");
            m_bIsFailOverSupported = XMLDmiUtil.getAttributeAsBoolean(
                execution, "isFailoverSupported");
            m_bIsExternallyLaunchable = XMLDmiUtil.getAttributeAsBoolean(
                execution, "isExternalLaunchAvailable");
            m_bIsAutoLaunchable = XMLDmiUtil.getAttributeAsBoolean(
                execution, "isAutoLaunchAvailable");
            m_bDefaultInMemoryLaunch = XMLDmiUtil.getAttributeAsBoolean(
                execution, "inMemoryLaunchDefault");
            m_bDefaultManualLaunch = XMLDmiUtil.getAttributeAsBoolean(
                execution, "manualLaunchDefault");

            //  Putting this check here for backward compatibility.
            //  Otherwise none of the older services will be able to run in-memory.
            String temporary = execution.getAttribute("isInMemoryLaunchable");

            if (temporary != null && !temporary.equalsIgnoreCase(""))
            {
                m_bIsInMemoryLaunchable = XMLDmiUtil.getAttributeAsBoolean(
                    execution, "isInMemoryLaunchable");
            }
            m_strTransactionType = execution.getAttribute("transaction");

            NodeList children = execution.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("ExecutionDir"))
                {
                    m_strExecDir = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("Executable"))
                {
                    m_strExecutableName = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("InMemoryLaunchImpl"))
                {
                    m_inMemoryLaunchImpl = XMLUtils.getNodeValueAsString(child);
                }

                if (nodeName.equalsIgnoreCase("Monitor"))
                {
                    NodeList moduleList = child.getChildNodes();
                    Node module = null;

                    for (int j = 0; moduleList != null && j < moduleList.getLength(); ++j)
                    {
                        module = moduleList.item(j);
                        if (module.getNodeType() == module.TEXT_NODE)
                        {
                            continue;
                        }

                        MonitorableModule moduleDmi = new MonitorableModule();

                        moduleDmi.setFieldValues((Element) module);

                        EventModule eventModule = new EventModule();

                        eventModule.setDefaultTraceLevel(moduleDmi.getDefaultTraceLevel());
                        eventModule.setMaxTraceLevel(moduleDmi.getMaxTraceLevel());
                        eventModule.setModuleName(moduleDmi.getModuleName());
                        addEventModuleInfo(eventModule);
                    }
                }

                if (nodeName.equalsIgnoreCase("EventModules"))
                {
                    NodeList moduleList = child.getChildNodes();
                    Node module = null;

                    for (int j = 0; moduleList != null && j < moduleList.getLength(); ++j)
                    {
                        module = moduleList.item(j);
                        if (module.getNodeType() == module.TEXT_NODE)
                        {
                            continue;
                        }

                        EventModule moduleDmi = new EventModule();

                        moduleDmi.setFieldValues((Element) module);
                        addEventModuleInfo(moduleDmi);
                    }
                }

                if (nodeName.equalsIgnoreCase("LogModules"))
                {
                    NodeList moduleList = child.getChildNodes();
                    Node module = null;

                    for (int j = 0; moduleList != null && j < moduleList.getLength(); ++j)
                    {
                        module = moduleList.item(j);
                        if (module.getNodeType() == module.TEXT_NODE)
                        {
                            continue;
                        }

                        LogModule moduleDmi = new LogModule();

                        moduleDmi.setFieldValues((Element) module);
                        addLogModuleInfo(moduleDmi);
                    }
                }

                if (nodeName.equalsIgnoreCase("StatusTracking"))
                {
                    m_bIsStatusTrackable = XMLDmiUtil.getAttributeAsBoolean(
                        (Element) child, "isSupported");

                    NodeList statusList = child.getChildNodes();
                    Node status = null;

                    for (int j = 0; statusList != null && j < statusList.getLength(); ++j)
                    {
                        status = statusList.item(j);
                        if (status.getNodeType() == status.TEXT_NODE)
                        {
                            continue;
                        }
                        addTrackableState(XMLUtils.getNodeValueAsString(status));
                    }
                }

                if (nodeName.equalsIgnoreCase("RuntimeArguments"))
                {
                    m_bIsVariableArgsSupported = XMLDmiUtil.getAttributeAsBoolean(
                        (Element) child, "isVariableArgumentsSupported");
                    m_bIsRuntimeArgsChangeSupported = XMLDmiUtil.getAttributeAsBoolean(
                        (Element) child, "supportsRuntimeArgsChange");

                    NodeList argsList = child.getChildNodes();
                    Node args = null;

                    for (int j = 0; argsList != null && j < argsList.getLength(); ++j)
                    {
                        args = argsList.item(j);
                        if (args.getNodeType() == args.TEXT_NODE)
                        {
                            continue;
                        }
                        if (args.getNodeName().equals("UserDefinedPropertySheet"))
                        {
                            m_strUserDefinedPropertySheetValue = XMLUtils.getNodeValueAsString(args);
                            m_strUserDefinedPropertySheet = ((Element) args).getAttribute("name");
                            m_bIsConfigurationRequired =
                                XMLDmiUtil.getAttributeAsBoolean(
                                (Element) args, "isConfigurationRequired");
                        }
                        else
                        {
                            RuntimeArg argsDmi = new RuntimeArg();

                            argsDmi.setFieldValues((Element) args);
                            addRuntimeArg(argsDmi);
                        }
                    }
                }

                if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param paramDmi = new Param();

                    paramDmi.setFieldValues((Element) child);
                    addParam(paramDmi);
                }
            }
        }
        validate();
    }

    /**
     *  This interface method is called to add the specified object
     *  of <code>LogModule</code> to the list of logging modules.
     *  This is for the service about which this object
     *  of <code>Execution </code> represents execution information.
     *
     * @param moduleInfo Object of LogModule to be added to this object.
     * @see #removeLogModuleInfo(LogModule)
     * @see #clearLogModuleInfo()
     * @see #getLogModules()
     * @since Tifosi2.0
     */
    public void addLogModuleInfo(LogModule moduleInfo)
    {
        if (m_LogModules == null)
        {
            m_LogModules = new Vector();
        }

        Enumeration enums = m_LogModules.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            LogModule lm = (LogModule) enums.nextElement();
            String nameOfLM = lm.getModuleName();

            if (nameOfLM.equalsIgnoreCase(moduleInfo.getModuleName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_LogModules.add(moduleInfo);
    }

    /**
     *  This interface method is called to add the specified object
     *  of <code>EventModule</code> to the list of event modules.
     *  This is for the service about which this object
     *  of <code>Execution</code> represents execution information.
     *
     * @param moduleInfo Object of EventModule to be added to this object.
     * @see #removeEventModuleInfo(EventModule)
     * @see #clearEventModuleInfo()
     * @see #getEventModules()
     * @since Tifosi2.0
     */
    public void addEventModuleInfo(EventModule moduleInfo)
    {
        if (m_eventModules == null)
        {
            m_eventModules = new Vector();
        }

        Enumeration enums = m_eventModules.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            EventModule em = (EventModule) enums.nextElement();
            String nameOfEM = em.getModuleName();

            if (nameOfEM.equalsIgnoreCase(moduleInfo.getModuleName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_eventModules.add(moduleInfo);
    }

    /**
     *  This interface method is called to remove the specified object
     *  of <code>LogModule</code> from the list of logging modules.
     *  This is for the service about which this object
     *  of <code>Execution </code> represents execution information.
     *
     * @param info object of LogModule to be removed
     * @see #addLogModuleInfo(LogModule)
     * @see #clearLogModuleInfo()
     * @see #getLogModules()
     * @since Tifosi2.0
     */
    public void removeLogModuleInfo(LogModule info)
    {
        if (m_LogModules != null)
        {
            m_LogModules.remove(info);
        }
    }

    /**
     *  This interface method is called to remove the specified object
     *  of <code>EventModule</code> from the list of event modules.
     *  This is for the service about which this object
     *  of <code>Execution</code> represents execution information.
     *
     * @param info object of EventModule to be removed
     * @see #addEventModuleInfo(EventModule)
     * @see #clearEventModuleInfo()
     * @see #getEventModules()
     * @since Tifosi2.0
     */
    public void removeEventModuleInfo(EventModule info)
    {
        if (m_eventModules != null)
        {
            m_eventModules.remove(info);
        }
    }

    /**
     *  This interface method is called to clear the list of logging modules, for
     *  the service about which this object of <code>Execution </code> represents
     *  execution information.
     *
     * @see #addLogModuleInfo(LogModule)
     * @see #removeLogModuleInfo(LogModule)
     * @see #getLogModules()
     * @since Tifosi2.0
     */
    public void clearLogModuleInfo()
    {
        if (m_LogModules != null)
        {
            m_LogModules.clear();
        }
    }

    /**
     *  This interface method is called to clear the list of event modules, for
     *  the service about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @see #addEventModuleInfo(EventModule)
     * @see #removeEventModuleInfo(EventModule)
     * @see #getEventModules()
     * @since Tifosi2.0
     */
    public void clearEventModuleInfo()
    {
        if (m_eventModules != null)
        {
            m_eventModules.clear();
        }
    }


    /**
     *  This interface method is called to add the specified string to the list of
     *  trackable states, for the service about which this object of
     *  <code>Execution</code> represents execution information.
     *
     * @param trackableState string to be added to the list of TrackableState for
     *                         service
     * @see #removeTrackableState(String)
     * @see #clearTrackableState()
     * @see #getTrackableStates()
     * @since Tifosi2.0
     */
    public void addTrackableState(String trackableState)
    {
        if (m_trackableStates == null)
        {
            m_trackableStates = new Vector();
        }

        Enumeration enums = m_trackableStates.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            String trState = (String) enums.nextElement();

            if (trState.equalsIgnoreCase(trackableState))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_trackableStates.add(trackableState);
    }


    /**
     *  This interface method is called to remove the specified string from the
     *  list of trackable states, for the service about which this object of
     *  <code>Execution</code> represents execution information.
     *
     * @param state string to be removed from the list of TrackableState for
     *                         service
     * @see #addTrackableState(String)
     * @see #clearTrackableState()
     * @see #getTrackableStates()
     * @since Tifosi2.0
     */
    public void removeTrackableState(String state)
    {
        if (m_trackableStates != null)
        {
            m_trackableStates.remove(state);
        }
    }


    /**
     *  This interface method is called to clear the list of trackable states, for the
     *  service about which this object of <code>Execution</code> represents
     *  execution information.
     *
     * @see #addTrackableState(String)
     * @see #removeTrackableState(String)
     * @see #getTrackableStates()
     * @since Tifosi2.0
     */
    public void clearTrackableState()
    {
        if (m_trackableStates != null)
        {
            m_trackableStates.clear();
        }
    }

    /**
     *  This interface method is called to add specified object of <code>RuntimeArg</code>
     *  to the list of runtime arguments defined for the service, about which this
     *  object of <code>Execution</code> represents execution information.
     *
     * @param arg object of RuntimeArg to be added
     * @see #removeRuntimeArg(RuntimeArg)
     * @see #clearRuntimeArg()
     * @see #getRuntimeArgs()
     * @see #getNumberOfRuntimeArgs()
     * @since Tifosi2.0
     */
    public void addRuntimeArg(RuntimeArg arg)
    {
        if (m_runtimeArgs == null)
        {
            m_runtimeArgs = new Vector();
        }

        Enumeration enums = m_runtimeArgs.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            RuntimeArg runtimeArg = (RuntimeArg) enums.nextElement();
            String nameOfRA = runtimeArg.getArgName();

            if (nameOfRA.equalsIgnoreCase(arg.getArgName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if ((!alreadyExists) && (!m_runtimeArgs.contains(arg)))
            m_runtimeArgs.add(arg);
    }


    /**
     *  This interface method is called to remove specified object of <code>RuntimeArg</code>
     *  from the list of runtime arguments defined for the service, about which
     *  this object of <code>Execution</code> represents execution information.
     *
     * @param arg object of RuntimeArg to be removed
     * @see #addRuntimeArg(RuntimeArg)
     * @see #clearRuntimeArg()
     * @see #getRuntimeArgs()
     * @see #getNumberOfRuntimeArgs()
     * @since Tifosi2.0
     */
    public void removeRuntimeArg(RuntimeArg arg)
    {
        if (m_runtimeArgs != null)
        {
            m_runtimeArgs.remove(arg);
        }
    }


    /**
     *  This interface method is called to clear the list of runtime arguments defined
     *  for the service about which this object of <code>Execution</code>
     *  represents execution information.
     *
     * @see #addRuntimeArg(RuntimeArg)
     * @see #removeRuntimeArg(RuntimeArg)
     * @see #getRuntimeArgs()
     * @see #getNumberOfRuntimeArgs()
     * @since Tifosi2.0
     */
    public void clearRuntimeArg()
    {
        if (m_runtimeArgs != null)
        {
            m_runtimeArgs.clear();
        }
    }


    /**
     *  This interface method is called to add the specified object of <code>Param</code>
     *  to the list of all extra parameters defined for the service, about which
     *  this object of <code>Execution</code> represents execution information.
     *
     * @param param object of Param to be added to the list of extra parameters
     * @see #removeParam(Param)
     * @see #clearParams()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void addParam(Param param)
    {
        if (m_params == null)
        {
            m_params = new Vector();
        }
        m_params.addElement(param);
    }


    /**
     *  This interface method is called to remove the specified object of <code>Param</code>
     *  from the list of all extra parameters defined for the service, about which
     *  this object of <code>Execution</code> represents execution information.
     *
     * @param param object of Param to be removed from the list of extra Parameters.
     * @see #addParam(Param)
     * @see #clearParams()
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void removeParam(Param param)
    {
        if (m_params != null)
        {
            m_params.remove(param);
        }
    }


    /**
     *  This interface method is called to clear the list of all extra parameters
     *  defined for the service, about which this object of <code>Execution</code>
     *  represents execution information.
     *
     * @see #addParam(Param)
     * @see #removeParam(Param)
     * @see #getParams()
     * @since Tifosi2.0
     */
    public void clearParams()
    {
        if (m_params != null)
        {
            m_params.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>Execution</code>
     *  has the required (mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_bIsAutoLaunchable || m_bIsExternallyLaunchable)
        {
            if (
            /*
             * m_strExecDir == null ||
             */
                m_strExecutableName == null
                || m_strExecType == null)
            {
                throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
            }
        }

        if (m_LogModules != null)
        {
            Enumeration _enum = m_LogModules.elements();

            while (_enum.hasMoreElements())
            {
                LogModule module = (LogModule) _enum.nextElement();

                module.validate();
            }
        }

        if (m_eventModules != null)
        {
            Enumeration _enum = m_eventModules.elements();

            while (_enum.hasMoreElements())
            {
                EventModule module = (EventModule) _enum.nextElement();

                module.validate();
            }
        }

        if (m_runtimeArgs != null)
        {
            Enumeration _enum = m_runtimeArgs.elements();

            while (_enum.hasMoreElements())
            {
                RuntimeArg arg = (RuntimeArg) _enum.nextElement();

                arg.validate();
            }
        }

        if (m_params != null)
        {
            Enumeration _enum = m_params.elements();

            while (_enum.hasMoreElements())
            {
                Param param = (Param) _enum.nextElement();

                param.validate();
            }
        }
    }


    /**
     *  Resets the values of the data members of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_bIsStatusTrackable = true;
        m_LogModules = new Vector();
        m_eventModules = new Vector();
        m_params = new Vector();
        m_runtimeArgs = new Vector();
        m_trackableStates = new Vector();
        m_bIsAutoLaunchable = true;
        m_bIsInMemoryLaunchable = true;
        m_bIsExternallyLaunchable = true;
        m_bIsFailOverSupported = true;
        m_strTransactionType = "Simple";
        m_bDefaultInMemoryLaunch = false;
        m_bDefaultManualLaunch = false;
    }


    /**
     *  This method is called to write this object of <code>Execution</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        writeUTF(out, m_strExecType);
        out.writeBoolean(m_bIsFailOverSupported);
        out.writeBoolean(m_bIsExternallyLaunchable);
        out.writeBoolean(m_bIsAutoLaunchable);
        out.writeBoolean(m_bIsInMemoryLaunchable);
        if(m_inMemoryLaunchImpl==null)
            out.writeUTF("");
        else
            out.writeUTF(m_inMemoryLaunchImpl);
        out.writeBoolean(m_bDefaultInMemoryLaunch);
        out.writeBoolean(m_bDefaultManualLaunch);
        writeUTF(out, m_strTransactionType);
        writeUTF(out, m_strExecDir);
        writeUTF(out, m_strExecutableName);

        // Log modules
        if (m_LogModules != null && m_LogModules.size() > 0)
        {
            int num = m_LogModules.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                LogModule module = (LogModule) m_LogModules.elementAt(i);

                module.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        // Event modules
        if (m_eventModules != null && m_eventModules.size() > 0)
        {
            int num = m_eventModules.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                EventModule module = (EventModule) m_eventModules.elementAt(i);

                module.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        out.writeBoolean(m_bIsStatusTrackable);

        // Trackable states
        if (m_trackableStates != null && m_trackableStates.size() > 0)
        {
            int num = m_trackableStates.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                String state = (String) m_trackableStates.elementAt(i);

                writeUTF(out, state);
            }
        }
        else
        {
            out.writeInt(0);
        }

        out.writeBoolean(m_bIsVariableArgsSupported);
        out.writeBoolean(m_bIsRuntimeArgsChangeSupported);
        writeUTF(out, m_strUserDefinedPropertySheet);
        out.writeBoolean(m_bIsConfigurationRequired);
        writeUTF(out, m_strUserDefinedPropertySheetValue);

        // Runtime Args
        if (m_runtimeArgs != null && m_runtimeArgs.size() > 0)
        {
            int num = m_runtimeArgs.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                RuntimeArg arg = (RuntimeArg) m_runtimeArgs.elementAt(i);

                arg.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        // Params
        if (m_params != null && m_params.size() > 0)
        {
            int num = m_params.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                Param param = (Param) m_params.elementAt(i);

                param.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>Execution</code>
     *  from the specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_strExecType = readUTF(is);
        m_bIsFailOverSupported = is.readBoolean();
        m_bIsExternallyLaunchable = is.readBoolean();
        m_bIsAutoLaunchable = is.readBoolean();
        m_bIsInMemoryLaunchable = is.readBoolean();
        m_inMemoryLaunchImpl = is.readUTF();
        if(m_inMemoryLaunchImpl.trim().equalsIgnoreCase(""))
            m_inMemoryLaunchImpl = null;
        m_bDefaultInMemoryLaunch = is.readBoolean();
        m_bDefaultManualLaunch = is.readBoolean();
        m_strTransactionType = readUTF(is);
        m_strExecDir = readUTF(is);
        m_strExecutableName = readUTF(is);

        // Log modules
        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            LogModule module = new LogModule();

            module.fromStream(is, versionNo);
            m_LogModules.addElement(module);
        }

        // Event modules
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            EventModule module = new EventModule();

            module.fromStream(is, versionNo);
            m_eventModules.addElement(module);
        }

        m_bIsStatusTrackable = is.readBoolean();

        // Trackable states
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            String state = readUTF(is);

            m_trackableStates.addElement(state);
        }

        m_bIsVariableArgsSupported = is.readBoolean();
        m_bIsRuntimeArgsChangeSupported = is.readBoolean();
        m_strUserDefinedPropertySheet = readUTF(is);
        m_bIsConfigurationRequired = is.readBoolean();
        m_strUserDefinedPropertySheetValue = readUTF(is);

        // Runtime Args
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            RuntimeArg arg = new RuntimeArg();

            arg.fromStream(is, versionNo);
            m_runtimeArgs.addElement(arg);
        }

        // Params
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            Param param = new Param();

            param.fromStream(is, versionNo);
            m_params.addElement(param);
        }
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>Execution</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Execution))
        {
            return false;
        }

        Execution rcvObj = (Execution) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getExecDir(), m_strExecDir)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getExecType(), m_strExecType)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getExecutable(), m_strExecutableName)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getTransactionType(), m_strTransactionType)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getUserDefinedPropertySheet(), m_strUserDefinedPropertySheet)
            && rcvObj.m_bIsConfigurationRequired == m_bIsConfigurationRequired
            && DmiEqualsUtil.checkStringEquality(rcvObj.getUserDefinedPropertySheetValue(), m_strUserDefinedPropertySheetValue)
            && rcvObj.getEventModules().equals(getEventModules())
            && rcvObj.getLogModules().equals(getLogModules())
            && rcvObj.getRuntimeArgs().equals(getRuntimeArgs())
            && rcvObj.getTrackableStates().equals(getTrackableStates()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>Execution</code>
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
        strBuf.append("Execution Details ");
        strBuf.append("[");
        strBuf.append("Is auto launchable = ");
        strBuf.append(m_bIsAutoLaunchable);
        strBuf.append(", ");
        strBuf.append("Is InMemory Launchable = ");
        strBuf.append(m_bIsInMemoryLaunchable);
        strBuf.append(", ");
        strBuf.append("InMemory Launchable Impl = ");
        strBuf.append(m_inMemoryLaunchImpl);
        strBuf.append(", ");
        strBuf.append("Is Externally Launchable = ");
        strBuf.append(m_bIsExternallyLaunchable);
        strBuf.append(", ");
        strBuf.append("Is Fail Over Supported = ");
        strBuf.append(m_bIsFailOverSupported);
        strBuf.append(", ");
        strBuf.append("Is Status Trackable = ");
        strBuf.append(m_bIsStatusTrackable);
        strBuf.append(", ");
        strBuf.append("Is Variable Arguments Supported = ");
        strBuf.append(m_bIsVariableArgsSupported);
        strBuf.append(", ");
        strBuf.append("Is Runtime Args Change Supported = ");
        strBuf.append(m_bIsRuntimeArgsChangeSupported);
        strBuf.append(", ");
        strBuf.append("Execution dir = ");
        strBuf.append(m_strExecDir);
        strBuf.append(", ");
        strBuf.append("Execution type = ");
        strBuf.append(m_strExecType);
        strBuf.append(", ");
        strBuf.append("Execution name = ");
        strBuf.append(m_strExecutableName);
        strBuf.append(", ");
        strBuf.append("Transaction type = ");
        strBuf.append(m_strTransactionType);
        strBuf.append(", ");
        strBuf.append("user defined property sheet = ");
        strBuf.append(m_strUserDefinedPropertySheet);
        strBuf.append(", ");
        strBuf.append("Configuration Required = ");
        strBuf.append(m_bIsConfigurationRequired);
        strBuf.append(", ");
        strBuf.append("user defined property sheet value= ");
        strBuf.append(m_strUserDefinedPropertySheetValue);
        strBuf.append(", ");
        strBuf.append("trackable states = ");
        strBuf.append(m_trackableStates);
        strBuf.append(", ");
        if (m_LogModules != null)
        {
            strBuf.append("Log modules = ");
            for (int i = 0; i < m_LogModules.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((LogModule) m_LogModules.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_eventModules != null)
        {
            strBuf.append("Event modules = ");
            for (int i = 0; i < m_eventModules.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((EventModule) m_eventModules.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_params != null)
        {
            strBuf.append("Param = ");
            for (int i = 0; i < m_params.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((Param) m_params.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_runtimeArgs != null)
        {
            strBuf.append("Run time Arguments = ");
            for (int i = 0; i < m_runtimeArgs.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((RuntimeArg) m_runtimeArgs.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Retruns the xml string equivalent of this object
     *
     * @param document instance of Xml Document.
     * @return org.w3c.dom.Node
     * @exception FioranoException thrown in case of error.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Execution");

        if (m_strExecType != null)
            ((Element) root0).setAttribute("type", m_strExecType);
        ((Element) root0).setAttribute("isFailoverSupported", "" + m_bIsFailOverSupported);
        ((Element) root0).setAttribute("isExternalLaunchAvailable", "" + m_bIsExternallyLaunchable);
        ((Element) root0).setAttribute("isAutoLaunchAvailable", "" + m_bIsAutoLaunchable);
        ((Element) root0).setAttribute("isInMemoryLaunchable", "" + m_bIsInMemoryLaunchable);
        ((Element) root0).setAttribute("manualLaunchDefault", "" + m_bDefaultManualLaunch);
        ((Element) root0).setAttribute("inMemoryLaunchDefault", "" + m_bDefaultInMemoryLaunch);
        if (m_strTransactionType != null)
            ((Element) root0).setAttribute("transaction", m_strTransactionType);

        Node child = null;

        child = XMLDmiUtil.getNodeObject("ExecutionDir", m_strExecDir, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("Executable", m_strExecutableName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("InMemoryLaunchImpl", m_inMemoryLaunchImpl, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        if (m_LogModules != null && m_LogModules.size() > 0)
        {
            Enumeration _enum = m_LogModules.elements();
            Node moduleRoot = document.createElement("LogModules");

            while (_enum.hasMoreElements())
            {
                LogModule module = (LogModule) _enum.nextElement();
                Node moduleNode = module.toJXMLString(document);

                moduleRoot.appendChild(moduleNode);
            }
            root0.appendChild(moduleRoot);
        }

        if (m_eventModules != null && m_eventModules.size() > 0)
        {
            Enumeration _enum = m_eventModules.elements();
            Node moduleRoot = document.createElement("EventModules");

            while (_enum.hasMoreElements())
            {
                EventModule module = (EventModule) _enum.nextElement();
                Node moduleNode = module.toJXMLString(document);

                moduleRoot.appendChild(moduleNode);
            }
            root0.appendChild(moduleRoot);
        }

        if (m_trackableStates != null && m_trackableStates.size() > 0)
        {
            Node statusTracking = document.createElement("StatusTracking");

            ((Element) statusTracking).setAttribute("isSupported", "" + m_bIsStatusTrackable);
            // adding trackable states
            XMLDmiUtil.addVectorValues("Status", m_trackableStates, document, statusTracking);
            root0.appendChild(statusTracking);
        }

        Node runtimeArgs = document.createElement("RuntimeArguments");

        ((Element) runtimeArgs).setAttribute("isVariableArgumentsSupported",
            "" + m_bIsVariableArgsSupported);
        ((Element) runtimeArgs).setAttribute("supportsRuntimeArgsChange",
            "" + m_bIsRuntimeArgsChangeSupported);

        if (m_strUserDefinedPropertySheet != null)
        {
            Element elem = document.createElement("UserDefinedPropertySheet");

            elem.setAttribute("name", m_strUserDefinedPropertySheet);

            elem.setAttribute("isConfigurationRequired", "" + m_bIsConfigurationRequired);
            if (m_strUserDefinedPropertySheetValue != null)
            {
                CDATASection cdata = document.createCDATASection(m_strUserDefinedPropertySheetValue);

                elem.appendChild(cdata);
            }
            runtimeArgs.appendChild(elem);
        }

        if (m_runtimeArgs != null && m_runtimeArgs.size() > 0)
        {
            Enumeration _enum = m_runtimeArgs.elements();

            while (_enum.hasMoreElements())
            {
                RuntimeArg arg = (RuntimeArg) _enum.nextElement();
                Node argument = arg.toJXMLString(document);

                runtimeArgs.appendChild(argument);
            }
        }
        if (runtimeArgs != null)
            root0.appendChild(runtimeArgs);

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration _enum = m_params.elements();

            while (_enum.hasMoreElements())
            {
                Param param = (Param) _enum.nextElement();
                Node paramNode = param.toJXMLString(document);

                root0.appendChild(paramNode);
            }
        }
        return root0;
    }

}
