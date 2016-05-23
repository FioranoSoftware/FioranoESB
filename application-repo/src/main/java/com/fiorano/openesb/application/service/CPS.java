/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.ServicePropertySheet;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class CPS extends InflatableDMIObject{
    /**
     * element cps in servicedescriptor xml
     */
    public static final String ELEM_CPS = "cps";

    /**
     * Returns ID of this object. This is used internally to identify different types of DMI objects.
     * @return the id of this object.
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_CPS;
    }

    /*-------------------------------------------------[ Launcher ]---------------------------------------------------*/
    /**
     * launcher class of CPS
     */
    public static final String ELEM_LAUNCHER = "launcher";

    private String launcher = null;

    /**
     * Returns Fully-Qualified class name of launcher
     * @return String launcher class name
     */
    public String getLauncher(){
        return launcher;
    }

    /**
     * Sets specified launcher class name for CPS
     * @param launcher launcher class name
     */
    public void setLauncher(String launcher){
        this.launcher = launcher;
    }

    /*-------------------------------------------------[ InMemoryLaunchable ]---------------------------------------------------*/
    /**
     * Specifies if cps is launched in memory of tool JVM
     */
    public static final String ATTR_IN_MEMRORY_LAUNCHABLE = "inmemory-launchable";

    private boolean inMemoryLaunchable = true;

    /**
     * Returns true if CPS can be launched in memory of tool JVM
     * @return boolean
     */
    public boolean isInMemoryLaunchable(){
        return inMemoryLaunchable;
    }

    /**
     * Sets a boolean specifying whether CPS can be launched in memory
     * @param inMemoryLaunchable boolean
     */
    public void setInMemoryLaunchable(boolean inMemoryLaunchable){
        this.inMemoryLaunchable = inMemoryLaunchable;
    }

    /*-------------------------------------------------[ Mandatory ]---------------------------------------------------*/
    /**
     * Specifies whether this service requires to be configured before execution
     */
    public static final String ATTR_MANDATORY = "mandatory";

    private boolean mandatory = true;

    /**
     * Returns true if this service requires to be configured before execution
     * @return boolean
     */
    public boolean isMandatory(){
        return mandatory;
    }

    /**
     * Sets a boolean specifying whether configuration is required before execution
     * @param mandatory boolean
     */
    public void setMandatory(boolean mandatory){
        this.mandatory = mandatory;
    }

    /*-------------------------------------------------[ DefaultValue ]---------------------------------------------------*/
    /**
     * Default value of CPS
     */
    public static final String ELEM_DEFAULT_VALUE = "default-value";

    private String defaultValue = null;

    /**
     * Returns Default Value of cps if any
     * @return String
     */
    public String getDefaultValue(){
        return defaultValue;
    }

    /**
     * Sets default value of CPS
     * @param defaultValue default value to be set
     */
    public void setDefaultValue(String defaultValue){
        this.defaultValue = defaultValue;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <cps mandatory="boolean"? inmemory-launchable="boolean"?>
     *      <launcher>string</launcher>
     *      <default-value>string</default-value>?
     * </cps>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_CPS);
        {
            if(!mandatory)
                writer.writeAttribute(ATTR_MANDATORY, String.valueOf(mandatory));
            if(!inMemoryLaunchable)
                writer.writeAttribute(ATTR_IN_MEMRORY_LAUNCHABLE, String.valueOf(inMemoryLaunchable));
            writeElement(writer, ELEM_LAUNCHER, launcher);
            writeElement(writer, ELEM_DEFAULT_VALUE, defaultValue);
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_CPS)){
            mandatory = getBooleanAttribute(cursor, ATTR_MANDATORY, true);
            inMemoryLaunchable = getBooleanAttribute(cursor, ATTR_IN_MEMRORY_LAUNCHABLE, true);

            launcher = defaultValue = null;
            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_LAUNCHER.equals(elemName))
                    launcher = cursor.getText();
                else if(ELEM_DEFAULT_VALUE.equals(elemName))
                    defaultValue = cursor.getText();
            }
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI structure
     * @param that old service DMI
     */
    public void convert(ServicePropertySheet that){
        mandatory = that.getExecutionInfo().isConfigurationRequired();
        inMemoryLaunchable = that.getServiceHeader().isCPSLaunchableInmemory();
        launcher = that.getExecutionInfo().getUserDefinedPropertySheet();
        defaultValue = that.getExecutionInfo().getUserDefinedPropertySheetValue();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        launcher = null;
        inMemoryLaunchable = true;
        mandatory = true;
        defaultValue = null;
    }

    /**
     * @bundle LAUNCHER_UNSPECIFIED==Laucher is not specified
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(launcher))
            throw new FioranoException("LAUNCHER_UNSPECIFIED");
    }
}
