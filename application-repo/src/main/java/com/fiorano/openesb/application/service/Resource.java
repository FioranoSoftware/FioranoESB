/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.NamedObject;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;
import com.fiorano.openesb.utils.Util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;

public class Resource extends InflatableDMIObject implements NamedObject{
    /**
     * Element resource in service descriptor xml
     */
    public static final String ELEM_RESOURCE = "resource";

    public int getObjectID(){
        return DmiObjectTypes.NEW_RESOURCE;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";
    private String name;

    /**
     * Returns name of this resource
     * @return String - Name of the resource
     */
    public String getName(){
        return name;
    }

    /**
     * Sets resource name
     * @param name Resource name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ RequiredForConfiguration ]---------------------------------------------------*/
    /**
     * Element required in service descriptor xml
     */
    public static final String ELEM_REQUIRED = "required";
    /**
     * Attribute configuration
     */
    public static final String ATTR_REQUIRED_FOR_CONFIGURATOIN = "configuration";

    private boolean requiredForConfiguration = true;

    /**
     * Returns true if this resource is required for configuration of service
     * @return boolean - true if the resource is required for configuration, false otherwise
     */
    public boolean isRequiredForConfiguration(){
        return requiredForConfiguration;
    }

    /**
     * Sets a boolean specifying whether this resource is required for configuration of service
     * @param requiredForConfiguration true if the resource is required for configuration, false otherwise
     */
    public void setRequiredForConfiguration(boolean requiredForConfiguration){
        this.requiredForConfiguration = requiredForConfiguration;
    }

    /*-------------------------------------------------[ RequiredForExecution ]---------------------------------------------------*/
    /**
     * Attribute execution
     */
    public static final String ATTR_REQUIRED_FOR_EXECUTION = "execution";

    private boolean requiredForExecution = true;

    /**
     * Returns true if this resource is required for execution of service
     * @return boolean - true if resource is required for execution, false otherwise
     */
    public boolean isRequiredForExecution(){
        return requiredForExecution;
    }

    /**
     * Sets a boolean specifying whether this resource is required for execution of service
     * @param requiredForExecution true if resource is required for execution, false otherwise
     */
    public void setRequiredForExecution(boolean requiredForExecution){
        this.requiredForExecution = requiredForExecution;
    }

    /*-------------------------------------------------[ TransferMode ]---------------------------------------------------*/
    /**
     * Attribute transfer-mode
     */
    public static final String ATTR_TRANSFER_MODE = "transfer-mode";
    /**
     * Transfer mode ASCII
     */
    public static final int TRANSFER_MODE_ASCII  = 0;
    /**
     * Transfer mode BINARY
     */
    public static final int TRANSFER_MODE_BINARY = 1;
    private int transferMode = TRANSFER_MODE_BINARY;

    /**
     * Returns transfer mode of this resource
     * @return int - Transfer mode
     */
    public int getTransferMode(){
        return transferMode;
    }

    /**
     * Sets transfer mode of this resource
     * @param transferMode Transfer mode i.e 0 for ASCII, 1 for BINARY
     */
    public void setTransferMode(int transferMode){
        this.transferMode = transferMode;
    }

    /*-------------------------------------------------[ OS ]---------------------------------------------------*/
    /**
     * Attribute os
     */
    public static final String ATTR_OS = "os";

    private int supportedOperatingSystems = Deployment.OS_ALL;

    /**
     * Returns supported operating Systems for this resource
     * @return int - Supported OS
     */
    public int getSupportedOperatingSystems(){
        return supportedOperatingSystems;
    }

    /**
     * Sets supported os for this resource
     * @param supportedOperatingSystems Supported OS
     */
    public void setSupportedOperatingSystems(int supportedOperatingSystems){
        this.supportedOperatingSystems = supportedOperatingSystems;
    }

    /**
     * Sets a boolean specifying whether this resource is supported on specified operating sytem <code>os</code>
     * @param os integer corresponding to operating system
     * @param supported true if resource is supported on specified OS, false otherwise
     */
    public void setOSSupported(int os, boolean supported){
        if(supported)
            supportedOperatingSystems |= os;
        else
            supportedOperatingSystems &= ~os;
    }

    /**
     * Resets supported operating system for this resource
     */
    public void resetSupportedOperatingSystems(){
        supportedOperatingSystems = Deployment.OS_ALL;
    }

    /**
     * Returns true if specified operating system is supported for this resource
     * @param os operating system
     * @return boolean - true is resource is supported on the OS, false otherwise
     */
    public boolean isOperatingSystemSupported(int os){
        return supportedOperatingSystems==Deployment.OS_ALL || (supportedOperatingSystems & os) == os;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <resource name="string" transfer-mode="int"? os="int"?>
     *      <required configuration="boolean" execution="boolean"/>?
     * </resource>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement(ELEM_RESOURCE);
        {
            writer.writeAttribute(ATTR_NAME, name);
            if(transferMode!=TRANSFER_MODE_BINARY)
                writer.writeAttribute(ATTR_TRANSFER_MODE, Integer.toString(transferMode));
            if(supportedOperatingSystems!=Deployment.OS_ALL)
                writer.writeAttribute(ATTR_OS, Integer.toString(supportedOperatingSystems));

            if(!requiredForConfiguration || !requiredForExecution){ // if not requried atleast in one case
                writer.writeStartElement(ELEM_REQUIRED);
                {
                    if(!requiredForConfiguration)
                        writer.writeAttribute(ATTR_REQUIRED_FOR_CONFIGURATOIN, "false");
                    if(!requiredForExecution)
                        writer.writeAttribute(ATTR_REQUIRED_FOR_EXECUTION, "false");
                }
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_RESOURCE)){
            name = cursor.getAttributeValue(null, ATTR_NAME);
            transferMode = getIntegerAttribute(cursor, ATTR_TRANSFER_MODE, TRANSFER_MODE_BINARY);
            supportedOperatingSystems = getIntegerAttribute(cursor, ATTR_OS, Deployment.OS_ALL);

            String nextElem = cursor.nextElement() ? cursor.getLocalName() : null;
            if(ELEM_REQUIRED.equals(nextElem)){
                requiredForConfiguration = getBooleanAttribute(cursor, ATTR_REQUIRED_FOR_CONFIGURATOIN, true);
                requiredForExecution = getBooleanAttribute(cursor, ATTR_REQUIRED_FOR_EXECUTION, true);
            }else
                requiredForConfiguration = requiredForExecution = true;
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(com.fiorano.openesb.application.sps.Resource that){
        name = that.getResourceName();

        resetSupportedOperatingSystems();
        Enumeration enumer = that.getApplicableOperatingSystems();
        while(enumer.hasMoreElements()){
            String os = (String)enumer.nextElement();
            if("Linux".equalsIgnoreCase(os))
                setOSSupported(Deployment.OS_LINUX, true);
            else if("Macintosh".equalsIgnoreCase(os))
                setOSSupported(Deployment.OS_MACINTOSH, true);
            else if("Win32".equalsIgnoreCase(os))
                setOSSupported(Deployment.OS_WIN32, true);
            else if("Solaris".equalsIgnoreCase(os))
                setOSSupported(Deployment.OS_SOLARIS, true);
        }

        requiredForConfiguration = that.isRequiredForConfiguration();
        requiredForExecution = that.isRequiredForExecution();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        name = null;
        requiredForConfiguration = true;
        requiredForExecution = true;
        transferMode = TRANSFER_MODE_BINARY;
        supportedOperatingSystems = Deployment.OS_ALL;
    }

    /**
     * @bundle RESOURCE_NAME_UNSPECIFIED=Resource name is not specified
     * @bundle INVALID_TRANSFER_MODE=Invalid Transfer Mode "{0}"
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(name))
            throw new FioranoException( "RESOURCE_NAME_UNSPECIFIED");
        if(transferMode!=TRANSFER_MODE_ASCII && transferMode!=TRANSFER_MODE_BINARY)
            throw new FioranoException("INVALID_TRANSFER_MODE");
    }

    /**
     * Overrides equals method in Object
     * @param obj Object
     * @return boolean - true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if(obj instanceof Resource){
            Resource that = (Resource)obj;
            return Util.equals(name, that.getName())
                    && requiredForConfiguration==that.isRequiredForConfiguration()
                    && requiredForExecution==that.isRequiredForExecution()
                    && transferMode==that.getTransferMode()
                    && supportedOperatingSystems==that.getSupportedOperatingSystems();
        }else
            return false;
    }

    /**
     * Returns key
     * @return String - Key
     */
    public String getKey(){
        return getName();
    }
}
