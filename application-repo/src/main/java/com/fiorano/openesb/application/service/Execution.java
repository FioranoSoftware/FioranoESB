/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.NamedObject;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.InPort;
import com.fiorano.openesb.application.sps.OutPort;
import com.fiorano.openesb.application.sps.RuntimeArg;
import com.fiorano.openesb.application.sps.ServicePropertySheet;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

public class Execution extends InflatableDMIObject{
    /**
     * element execution in ServiceDescriptor xml
     */
    public static final String ELEM_EXECUTION = "execution";

    /**
     * Returns the id of this object. This is used internally to identify different types of DMI objects.
     * @return the id of this object.
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_EXECUTION;
    }

    /*-------------------------------------------------[ Type ]---------------------------------------------------*/
    /**
     * Execution type of service
     */
    public static final String ATTR_TYPE = "type";
    /**
     * Execution type java
     */
    public final static int TYPE_JAVA = 0;
    /**
     * execution type other than java
     */
    public final static int TYPE_EXECUTABLE = 1;

    private int type = TYPE_JAVA;

    /**
     * Returns execution type of this service   i.e java, non-java
     * @return int
     */
    public int getType(){
        return type;
    }

    /**
     * Sets execution type of this service
     * @param type execution type to be set
     */
    public void setType(int type){
        this.type = type;
    }

    /*-------------------------------------------------[ Type ]---------------------------------------------------*/
    /**
     * Attribute JCA
     */
    public static final String ATTR_JCA = "subtype";

    private boolean jca = true;

    private String subtype = SUBTYPE_JCA;
    /**
     * subtype jca
     */
    public static final String SUBTYPE_JCA = "jca";  //default value
    /**
     * subtype edbc
     */
    public static final String SUBTYPE_NON_JCA = "edbc";
    /**
     * subtype c
     */
    public static final String SUBTYPE_C ="c";
    /**
     * subtype cpp
     */
    public static final String SUBTYPE_CPP="cpp";
    /**
     * subtype CSharp
     */
    public static final String SUBTYPE_CSHARP ="CSharp";


    /**
     * Returns true if this is a JCA service
     * @return boolean
     */
    public boolean isJCA(){
        return jca;
    }

    /**
     * Sets a boolean specifying whether this is a JCA service
     * @param jca boolean
     */
    public void setJCA(boolean jca){
        this.jca = jca;
    }

    /**
     * Returns subtype of this service
     * @return String
     */
    public String getSubtype(){
        return subtype;
    }

    /**
     * Sets sub tpe for this service i.e jca, edbc,c, cpp , Csharp
     * @param subtype subtype to be set
     */
    public void setSubtype(String subtype){
        this.subtype = subtype;
    }

    /*-------------------------------------------------[ CCP ]---------------------------------------------------*/

    /**
     * element ccp under execution element
     */
    public static final String ELEM_CCP = "ccp";
    /**
     *  Attribute CCP minimum version
     */
    public static final String ATTR_CCP_MIN_VERSION = "min-version-supported";
    /**
     * Attribute CCP maximum version
     */
    public static final String ATTR_CCP_MAX_VERSION = "max-version-supported";

    private boolean ccpSupported = false;
    private float minCCPVersionSupported = 1.0f;
    private float maxCCPVersionSupported = 1.0f;

    /**
     * Sets whether CCP Supported
     * @param ccpSupported
     */
    public void setCCPSupported(boolean ccpSupported) {
        this.ccpSupported = ccpSupported;
    }

    /**
     * Sets minimum CCP Version Supported
     * @param minCCPVersionSupported
     */
    public void setMinCCPVersionSupported(float minCCPVersionSupported) {
        this.minCCPVersionSupported = minCCPVersionSupported;
    }

    /**
     * Sets maximum Version Supported
     * @param maxCCPVersionSupported
     */
    public void setMaxCCPVersionSupported(float maxCCPVersionSupported) {
        this.maxCCPVersionSupported = maxCCPVersionSupported;
    }

    /**
     * Sets Whether CCP Supported
     * @return
     */
    public boolean isCCPSupported() {
        return ccpSupported;
    }

    /**
     * Gets Minimum CCP Version Supported
     * @return
     */
    public float getMinCCPVersionSupported() {
        return minCCPVersionSupported;
    }

    /**
     * Gets Maximum CCP Version Supported
     * @return
     */
    public float getMaxCCPVersionSupported() {
        return maxCCPVersionSupported;
    }

    /*-------------------------------------------------[ PreferredLaunchType ]---------------------------------------------------*/
    /**
     * element launchtype in ServiceDescriptor xml
     */
    public static final String ELEM_LAUNCH_TYPE = "launchtype";
    /**
     * preferred launch type
     */
    public static final String ATTR_PREFERRED_LAUNCH_TYPE = "preferred";
    /**
     * launch type none
     */
    public static final int LAUNCH_TYPE_NONE = 0;
    /**
     * launch type separate process
     */
    public static final int LAUNCH_TYPE_SEPARATE_PROCESS = 1;
    /**
     * launch type in memory
     */
    public static final int LAUNCH_TYPE_IN_MEMORY = 2;
    /**
     * launch type manual
     */
    public static final int LAUNCH_TYPE_MANUAL = 4;

    private int preferredLaunchType = LAUNCH_TYPE_SEPARATE_PROCESS;

    /**
     * Returns launch type of this service
     * @return int
     */
    public int getPreferredLaunchType(){
        return preferredLaunchType;
    }

    /**
     * Sets launch type for this service
     * @param preferredLaunchType launch type i.e 1 for separate process, 2 for in memory, 4 for manual, 0 for none of them
     */
    public void setPreferredLaunchType(int preferredLaunchType){
        this.preferredLaunchType = preferredLaunchType;
    }

    /*-------------------------------------------------[ supportedLaunchTypes ]---------------------------------------------------*/
    /**
     * supported launch types
     */
    public static final String ATTR_SUPPORTED_LAUNCH_TYPES = "supported";

    private int supportedLaunchTypes = LAUNCH_TYPE_IN_MEMORY | LAUNCH_TYPE_SEPARATE_PROCESS;

    /**
     * Sets a boolean whether specified launch type is supported for this service
     * @param launchType any launch type other than NONE
     * @param supported boolean
     */
    public void setLaunchTypeSupported(int launchType, boolean supported){
        if(supported)
            supportedLaunchTypes |= launchType;
        else
            supportedLaunchTypes &= ~launchType;
    }

    /**
     * Resets supported launch type for this service to none
     */
    public void resetSupportedLaunchTypes(){
        supportedLaunchTypes = LAUNCH_TYPE_NONE;
    }

    /**
     * Returns true if specified launch type is supported for this service
     * @param launchType launch type
     * @return boolean
     */
    public boolean isLaunchTypeSupported(int launchType){
        return launchType==LAUNCH_TYPE_NONE
                ? supportedLaunchTypes==LAUNCH_TYPE_NONE
                : (supportedLaunchTypes & launchType) == launchType;
    }

    /**
     * Returns supported launch types for this service
     * @return int
     */
    public int getSupportedLaunchTypes(){
        return supportedLaunchTypes;
    }

    /**
     * Sets supported launch type for this service
     * @param supportedLaunchTypes supported launch type
     */
    public void setSupportedLaunchTypes(int supportedLaunchTypes){
        this.supportedLaunchTypes = supportedLaunchTypes;
    }

    /*-------------------------------------------------[ FailoverSupported ]---------------------------------------------------*/
    /**
     * failover attribute
     */
    public static final String ATTR_FAILOVER_SUPPORTED = "failover";

    private boolean failoverSupported = true;

    /**
     * Returns true if fail-over is supported for this service
     * @return boolean
     */
    public boolean isFailoverSupported(){
        return failoverSupported;
    }

    /**
     * Sets a boolean specifying whether failover is supported for this service
     * @param failoverSupported boolean
     */
    public void setFailoverSupported(boolean failoverSupported){
        this.failoverSupported = failoverSupported;
    }

    /*-------------------------------------------------[ TransactionSupported ]---------------------------------------------------*/
    /**
     * transaction attribute
     */
    public static final String ATTR_TRANSACTION_SUPPORTED = "transaction";

    private boolean transactionSupported;

    /**
     * Returns true if transaction is supported for this service
     * @return boolean
     */
    public boolean isTransactionSupported(){
        return transactionSupported;
    }

    /**
     * Sets a boolean specifying whether a transaction is supported for this service
     * @param transactionSupported boolean
     */
    public void setTransactionSupported(boolean transactionSupported){
        this.transactionSupported = transactionSupported;
    }

    /*-------------------------------------------------[ Executable ]---------------------------------------------------*/
    /**
     * element separate-process in ServiceDescriptor xml
     */
    public static final String ELEM_SEPARATE_PROCESS = "separate-process";
    /**
     * executable attribute
     */
    public static final String ATTR_EXECUTABLE = "executable";

    private String executable;

    /**
     * Returns Executable JavaClass/File used to launch service in separator process or manual launch
     * @return String
     */
    public String getExecutable(){
        return executable;
    }

    /**
     * Sets executable used to launch service in separator process or manual launch
     * @param executable executable
     */
    public void setExecutable(String executable){
        this.executable = executable;
    }

    /*-------------------------------------------------[ WorkingDirectory ]---------------------------------------------------*/
    /**
     * working directory attribute
     */
    public static final String ATTR_WORKING_DIRECTORY = "workingDirectory";

    private String workingDirectory = "."; //NOI18N

    /**
     * Returns working Directory of this service
     * @return String
     */
    public String getWorkingDirectory(){
        return workingDirectory;
    }

    /**
     * Sets working directory of this service
     * @param workingDirectory working directory to be set
     */
    public void setWorkingDirectory(String workingDirectory){
        this.workingDirectory = workingDirectory;
    }

    /*-------------------------------------------------[ InMemoryExecutable ]---------------------------------------------------*/
    /**
     * element inmemory in ServiceDescriptor xml
     */
    public static final String ELEM_IN_MEMORY = "inmemory";
    /**
     * executable attribute
     */
    public static final String ATTR_IN_MEMORY_EXECUTABLE = "executable";

    private String inMemoryExecutable;

    /**
     * Returns executable JavaClass used to launch service in In-Memory. This class must implement IInMemoryShutdownable interface
     * @return String
     */
    public String getInMemoryExecutable(){
        return inMemoryExecutable;
    }

    /**
     * Sets executable JavaClass used to launch this service in In-Memory
     * @param inMemoryExecutable class name
     */
    public void setInMemoryExecutable(String inMemoryExecutable){
        this.inMemoryExecutable = inMemoryExecutable;
    }

    /*-------------------------------------------------[ CPS ]---------------------------------------------------*/

    private CPS cps = null;

    /**
     * Returns CPS for this service
     * @return CPS
     */
    public CPS getCPS(){
        return cps;
    }

    /**
     * Sets CPS of this service
     * @param cps CPS to be set
     */
    public void setCPS(CPS cps){
        this.cps = cps;
    }

    /*-------------------------------------------------[ errorHandlingSupported ]---------------------------------------------------*/
    /**
     * exception-port attribute
     */
    public static final String ATTR_ERROR_HANDLING_SUPPORTED = "exception-port";

    private boolean errorHandlingSupported = true;

    /**
     * Sets a boolean specifying whether error handling is supported for this service
     * @return boolean
     */
    public boolean isErrorHandlingSupported(){
        return errorHandlingSupported;
    }

    /**
     * Sets a boolean specifying whether error handling is supported for this service
     * @param errorHandlingSupported boolean
     */
    public void setErrorHandlingSupported(boolean errorHandlingSupported){
        this.errorHandlingSupported = errorHandlingSupported;
    }

    /*-------------------------------------------------[ InputPorts ]---------------------------------------------------*/
    /**
     * element input-ports in ServiceDescriptor xml
     */
    public static final String ELEM_INPUT_PORTS = "input-ports";

    private List <Port>inputPorts = new ArrayList<Port>();

    /**
     * Adds specified input port to inports of this service
     * @param inputport port to be added
     */
    public void addInputPort(Port inputport){
        inputPorts.add(inputport);

    }

    /**
     * Removes specified input port from input ports of this service
     * @param inputPort input port
     */
    public void removeInputPort(Port inputPort){
        inputPorts.remove(inputPort);
    }

    /**
     * Returns input ports of this service
     * @return list of in ports
     */
    public List<Port>getInputPorts(){
        return inputPorts;
    }

    /**
     * Sets specified list <code>inputPorts</code> of this service
     * @param inputPorts list of input ports
     */
    public void setInputPorts(List inputPorts){
        this.inputPorts = inputPorts;
    }

    /*-------------------------------------------------[ OutputPorts ]---------------------------------------------------*/
    /**
     * element output-ports in ServiceDescriptor xml
     */
    public static final String ELEM_OUTPUT_PORTS = "output-ports";

    private List<Port> outputPorts = new ArrayList<Port>();

    /**
     * Adds specified output port to this service
     * @param outputport outport to be added
     */
    public void addOutputPort(Port outputport){
        outputPorts.add(outputport);
    }

    /**
     * Removes output port from outports of this service
     * @param outputPort outport to be removed
     */
    public void removeOutputPort(Port outputPort){
        outputPorts.remove(outputPort);
    }

    /**
     * Returns a list of output ports of this service
     * @return list of outports
     */
    public List<Port> getOutputPorts(){
        return outputPorts;
    }

    /**
     * Sets specified list <code>outputPorts</code> as outports of this service
     * @param outputPorts output ports
     */
    public void setOutputPorts(List outputPorts){
        this.outputPorts = outputPorts;
    }

    /**
     * Returns port with the specified name from ports of this service
     * @param name port name
     * @return Port
     */
    public Port findPort(String name){
        NamedObject port = DmiObject.findNamedObject(inputPorts, name);
        return (Port)(port!=null ? port : DmiObject.findNamedObject(outputPorts, name));
    }
    /*-------------------------------------------------[ LogModules ]---------------------------------------------------*/
    /**
     * element logmodules in ServiceDescriptor xml
     */
    public static final String ELEM_LOGMODULES = "logmodules";

    private List logModules = new ArrayList();

    /**
     * Adds specified log module to this service
     * @param logModule log module to be added
     */
    public void addLogModule(LogModule logModule){
        logModules.add(logModule);
    }

    /**
     * Removes specified log module from log modules of this service
     * @param logModule log module to be removed
     */
    public void removeLogModule(LogModule logModule){
        logModules.remove(logModule);
    }

    /**
     * Returns log modules of this service
     * @return list of log modules
     */
    public List getLogModules(){
        return logModules;
    }

    /**
     * Sets specified list <code>logModules</code> as log modules of this service
     * @param logModules log modules to be set
     */
    public void setLogModules(List logModules){
        this.logModules = logModules;
    }

    /*-------------------------------------------------[ RuntimeArguments ]---------------------------------------------------*/
    /**
     * element runtime-arguments in ServiceDescriptor xml
     */
    public static final String ELEM_RUNTIME_ARGUMENTS = "runtime-arguments";
    /**
     * runtime argument
     */
    public static final String RUNTIME_ARGUMENT_INTERACTION_SPEC = "javax.resource.cci.InteractionSpec";
    /**
     * runtime argument
     */
    public static final String RUNTIME_ARGUMENT_BPEL_PROCESS = "BpelProcessFile";

    private List runtimeArguments = new ArrayList();

    /**
     * Adds specified runtime argument to this service
     * @param runtimeArgument runtime argument
     */
    public void addRuntimeArgument(RuntimeArgument runtimeArgument){
        runtimeArguments.add(runtimeArgument);
    }

    /**
     * Removes specified runtime argument from this service
     * @param runtimeArgument runtime argument
     */
    public void removeRuntimeArgument(RuntimeArgument runtimeArgument){
        runtimeArguments.remove(runtimeArgument);
    }

    /**
     * Returns a list of runtime arguments
     * @return list of runtime arguments
     */
    public List getRuntimeArguments(){
        return runtimeArguments;
    }

    /**
     * Sets specified list <code>runtimeArguments</code> as runtime arguments of this service
     * @param runtimeArguments list of runtime arguments
     */
    public void setRuntimeArguments(List runtimeArguments){
        this.runtimeArguments = runtimeArguments;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <execution type="int"? subtype="jca/edbc" failover="boolean"? transaction="boolean"?>
     *      <launchtye supported="int" preferred="int"/>
     *      <separate-process executable="string" working-directory="string"?/>?
     *      <inmemory executable="string"/>?
     *      ...cps?...
     *      <input-ports>
     *          ...port+...
     *      </input-ports>?
     *      <output-ports exception-port="boolean"?>
     *          ...port+...
     *      </output-ports>?
     *      <logmodules>
     *          ...logmodule+...
     *      </logmodules>?
     *      <runtime-arguments>
     *          ...runtime-argument+...
     *      </runtime-arguments>?
     * </execution>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_EXECUTION);
        {
            if(type!=TYPE_JAVA)
                writer.writeAttribute(ATTR_TYPE, String.valueOf(type));
            if(!SUBTYPE_JCA.equals(subtype))
                writer.writeAttribute(ATTR_JCA, subtype);

            if(!failoverSupported)
                writer.writeAttribute(ATTR_FAILOVER_SUPPORTED, String.valueOf(failoverSupported));
            if(!transactionSupported)
                writer.writeAttribute(ATTR_TRANSACTION_SUPPORTED, String.valueOf(transactionSupported));

            if(ccpSupported){
                writer.writeStartElement(ELEM_CCP);
                {
                    writeAttribute(writer, ATTR_CCP_MIN_VERSION, minCCPVersionSupported);
                    writeAttribute(writer, ATTR_CCP_MAX_VERSION, maxCCPVersionSupported);
                }
                writer.writeEndElement();
            }

            if(supportedLaunchTypes!=(LAUNCH_TYPE_IN_MEMORY | LAUNCH_TYPE_SEPARATE_PROCESS) || preferredLaunchType!=LAUNCH_TYPE_SEPARATE_PROCESS){
                writer.writeStartElement(ELEM_LAUNCH_TYPE);
                {
                    if(supportedLaunchTypes!=(LAUNCH_TYPE_IN_MEMORY | LAUNCH_TYPE_SEPARATE_PROCESS))
                        writer.writeAttribute(ATTR_SUPPORTED_LAUNCH_TYPES, String.valueOf(supportedLaunchTypes));
                    if(preferredLaunchType!=LAUNCH_TYPE_SEPARATE_PROCESS)
                        writer.writeAttribute(ATTR_PREFERRED_LAUNCH_TYPE, String.valueOf(preferredLaunchType));
                }
                writer.writeEndElement();
            }

            writer.writeStartElement(ELEM_SEPARATE_PROCESS);
            {
                writer.writeAttribute(ATTR_EXECUTABLE, executable);
                if(!".".equals(workingDirectory))
                    writer.writeAttribute(ATTR_WORKING_DIRECTORY, workingDirectory);
            }
            writer.writeEndElement();

            if(!StringUtil.isEmpty(inMemoryExecutable)){
                writer.writeStartElement(ELEM_IN_MEMORY);
                writer.writeAttribute(ATTR_IN_MEMORY_EXECUTABLE, inMemoryExecutable);
                writer.writeEndElement();
            }
            if(cps!=null)
                cps.toJXMLString(writer);
            writeCollection(writer, inputPorts, ELEM_INPUT_PORTS,writeCDataSections);

            if(outputPorts.size()>0){
                writer.writeStartElement(ELEM_OUTPUT_PORTS);
                if(!errorHandlingSupported)
                    writeAttribute(writer, ATTR_ERROR_HANDLING_SUPPORTED, errorHandlingSupported);
                writeCollection(writer, outputPorts, null,writeCDataSections);
                writer.writeEndElement();
            }

            writeCollection(writer, logModules, ELEM_LOGMODULES);
            writeCollection(writer, runtimeArguments, ELEM_RUNTIME_ARGUMENTS);
        }
        writer.writeEndElement();
    }

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, true);
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_EXECUTION)){
            type = getIntegerAttribute(cursor, ATTR_TYPE, TYPE_JAVA);
            subtype = getStringAttribute(cursor, ATTR_JCA, SUBTYPE_JCA);
            jca = !SUBTYPE_NON_JCA.equals(subtype);
            if(type != TYPE_JAVA)
                jca = false;
            failoverSupported = getBooleanAttribute(cursor, ATTR_FAILOVER_SUPPORTED, true);
            transactionSupported = getBooleanAttribute(cursor, ATTR_TRANSACTION_SUPPORTED, true);

            supportedLaunchTypes = LAUNCH_TYPE_IN_MEMORY | LAUNCH_TYPE_SEPARATE_PROCESS;
            preferredLaunchType = LAUNCH_TYPE_SEPARATE_PROCESS;
            workingDirectory = ".";
            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_LAUNCH_TYPE.equals(elemName)){
                    supportedLaunchTypes = getIntegerAttribute(cursor, ATTR_SUPPORTED_LAUNCH_TYPES, supportedLaunchTypes);
                    preferredLaunchType = getIntegerAttribute(cursor, ATTR_PREFERRED_LAUNCH_TYPE, preferredLaunchType);
                }else if(ELEM_SEPARATE_PROCESS.equals(elemName)){
                    executable = cursor.getAttributeValue(null, ATTR_EXECUTABLE);
                    workingDirectory = getStringAttribute(cursor, ATTR_WORKING_DIRECTORY, ".");
                }else if(ELEM_IN_MEMORY.equals(elemName))
                    inMemoryExecutable = cursor.getAttributeValue(null, ATTR_IN_MEMORY_EXECUTABLE);
                else if(CPS.ELEM_CPS.equals(elemName)){
                    cps = new CPS();
                    cps.setFieldValues(cursor);
                }else if(ELEM_INPUT_PORTS.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(Port.ELEM_PORT.equals(cursor.getLocalName())){
                            Port port = new Port();
                            port.setFieldValues(cursor);
                            inputPorts.add(port);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_OUTPUT_PORTS.equals(elemName)){
                    errorHandlingSupported = getBooleanAttribute(cursor, ATTR_ERROR_HANDLING_SUPPORTED, true);
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(Port.ELEM_PORT.equals(cursor.getLocalName())){
                            Port port = new Port();
                            port.setFieldValues(cursor);
                            outputPorts.add(port);
                        }
                    }
                    cursor.resetCursor();
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
                }else if(ELEM_RUNTIME_ARGUMENTS.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(RuntimeArgument.ELEM_RUNTIME_ARGUMENT.equals(cursor.getLocalName())){
                            RuntimeArgument runtimeArgument = new RuntimeArgument();
                            runtimeArgument.setFieldValues(cursor);
                            runtimeArguments.add(runtimeArgument);
                        }
                    }
                    cursor.resetCursor();
                }else if(ELEM_CCP.equals(elemName)){
                    ccpSupported = true;
                    minCCPVersionSupported = getFloatAttribute(cursor, ATTR_CCP_MIN_VERSION, 1.0f);
                    maxCCPVersionSupported = getFloatAttribute(cursor, ATTR_CCP_MAX_VERSION, 1.0f);
                }
            }
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI structure
     * @param that old DMI
     */
    public void convert(ServicePropertySheet that){
        com.fiorano.openesb.application.sps.Execution thatExec = that.getExecutionInfo();

        type = "java".equalsIgnoreCase(thatExec.getExecType()) ? TYPE_JAVA : TYPE_EXECUTABLE;
        jca = Param.getParamWithName(thatExec.getParams(), "BCDK")!=null;
        failoverSupported = thatExec.isFailOverSupported();
        transactionSupported = "Simple".equalsIgnoreCase(thatExec.getTransactionType());
        errorHandlingSupported = that.getServiceHeader().isErrorHandlingSupported();

        resetSupportedLaunchTypes();
        if(thatExec.isAutoLaunchable())
            setLaunchTypeSupported(LAUNCH_TYPE_SEPARATE_PROCESS, true);
        if(thatExec.isInMemoryLaunchable())
            setLaunchTypeSupported(LAUNCH_TYPE_IN_MEMORY, true);
        if(thatExec.isManuallyLaunchable())
            setLaunchTypeSupported(LAUNCH_TYPE_MANUAL, true);

        if(thatExec.isDefaultInMemoryLaunch())
            preferredLaunchType = LAUNCH_TYPE_IN_MEMORY;
        else if(thatExec.isDefaultManualLaunch())
            preferredLaunchType = LAUNCH_TYPE_MANUAL;
        else if(thatExec.isAutoLaunchable())
            preferredLaunchType = LAUNCH_TYPE_SEPARATE_PROCESS;
        else
            preferredLaunchType = LAUNCH_TYPE_NONE;

        workingDirectory = thatExec.getExecDir();
        executable = thatExec.getExecutable();
        inMemoryExecutable = thatExec.getInMemoryLaunchImpl();

        if(!StringUtil.isEmpty(thatExec.getUserDefinedPropertySheet())){
            cps = new CPS();
            cps.convert(that);
        }

        Enumeration enumer = that.getPortDescriptor().getInPorts();
        while(enumer.hasMoreElements()){
            Port port = new Port();
            port.convert((InPort)enumer.nextElement());
            inputPorts.add(port);
        }

        enumer = that.getPortDescriptor().getOutPorts();
        while(enumer.hasMoreElements()){
            Port port = new Port();
            port.convert((OutPort)enumer.nextElement());
            outputPorts.add(port);
        }

        enumer = thatExec.getLogModules();
        while(enumer.hasMoreElements()){
            LogModule logModule = new LogModule();
            logModule.convert((com.fiorano.openesb.application.sps.LogModule)enumer.nextElement());
            logModules.add(logModule);
        }

        enumer = thatExec.getRuntimeArgs();
        while(enumer.hasMoreElements()){
            RuntimeArgument arg = new RuntimeArgument();
            arg.convert((RuntimeArg)enumer.nextElement());
            runtimeArguments.add(arg);
        }

        addParamAsRuntimeArgument(thatExec.getParams(), RUNTIME_ARGUMENT_INTERACTION_SPEC);
        addParamAsRuntimeArgument(thatExec.getParams(), RUNTIME_ARGUMENT_BPEL_PROCESS);
    }

    private void addParamAsRuntimeArgument(Vector params, String paramName){
        String paramValue = Param.getParamValue(params, paramName);
        if(paramValue!=null)
            runtimeArguments.add(new RuntimeArgument(paramName, paramValue));
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        type = TYPE_JAVA;
        jca = true;
        preferredLaunchType = LAUNCH_TYPE_SEPARATE_PROCESS;
        supportedLaunchTypes = LAUNCH_TYPE_IN_MEMORY | LAUNCH_TYPE_SEPARATE_PROCESS;
        failoverSupported = true;
        transactionSupported = false;
        executable = null;
        workingDirectory = ".";
        inMemoryExecutable = null;
        cps = null;
        errorHandlingSupported = true;
        inputPorts.clear();
        outputPorts.clear();
        logModules.clear();
        runtimeArguments.clear();
    }


    /**
     * @bundle INVALID_EXECUTION_TYPE=Invalid Execution Type {0}
     * @bundle PREFERRED_LAUNCH_TYPE_UNSUPPORTED=Preferred Launch Type is Unsupported
     * @bundle EXECUTABLE_UNSPECIFIED=Executable must be specified if separate launch is supported
     * @bundle WORKING_DIRECTORY_UNSPECIFIED=Working Directory must be specified if separate launch is supported
     * @bundle INMEMORY_EXECUTABLE_UNSPECIFIED=InMemory-Executable must be specified if inmemory launch is supported
     * @bundle PORT_ALREADY_EXISTS=Port {0} already exists
     * @bundle LOGMODULE_ALREADY_EXISTS=LogModule {0} already exists
     * @bundle RUNTIME_ARGUMENT_ALREADY_EXISTS=Runtime-Argument {0} already exists
     */
    public void validate() throws FioranoException{
        if(type!=TYPE_JAVA && type!=TYPE_EXECUTABLE)
            throw new FioranoException("INVALID_EXECUTION_TYPE");
        if(!isLaunchTypeSupported(preferredLaunchType))
            throw new FioranoException("PREFERRED_LAUNCH_TYPE_UNSUPPORTED");
        if(isLaunchTypeSupported(LAUNCH_TYPE_SEPARATE_PROCESS)){
            if(StringUtil.isEmpty(executable))
                throw new FioranoException("EXECUTABLE_UNSPECIFIED");
            if(StringUtil.isEmpty(workingDirectory))
                throw new FioranoException("WORKING_DIRECTORY_UNSPECIFIED");
        }
        if(isLaunchTypeSupported(LAUNCH_TYPE_IN_MEMORY)){
            if(StringUtil.isEmpty(inMemoryExecutable))
                throw new FioranoException( "INMEMORY_EXECUTABLE_UNSPECIFIED");
        }
        if(cps!=null)
            cps.validate();

        Set names = new HashSet();
        Iterator iter = inputPorts.iterator();
        while(iter.hasNext()){
            Port port = (Port)iter.next();
            port.validate();
            if(!names.add(port.getName()))
                throw new FioranoException("PORT_ALREADY_EXISTS");
        }
        iter = outputPorts.iterator();
        while(iter.hasNext()){
            Port port = (Port)iter.next();
            port.validate();
            if(!names.add(port.getName()))
                throw new FioranoException("PORT_ALREADY_EXISTS");
        }

        names.clear();
        iter = logModules.iterator();
        while(iter.hasNext()){
            LogModule logModule = (LogModule)iter.next();
            logModule.validate();
            if(!names.add(logModule.getName()))
                throw new FioranoException("LOGMODULE_ALREADY_EXISTS");
        }

        names.clear();
        iter = runtimeArguments.iterator();
        while(iter.hasNext()){
            RuntimeArgument runtimeArgument = (RuntimeArgument)iter.next();
            runtimeArgument.validate();
            if(!names.add(runtimeArgument.getName()))
                throw new FioranoException( "RUNTIME_ARGUMENT_ALREADY_EXISTS");
        }
    }
}
