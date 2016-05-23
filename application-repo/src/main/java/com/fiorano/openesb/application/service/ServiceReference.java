/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.ServiceHeader;
import com.fiorano.openesb.application.sps.ServicePropertySheet;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.Util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;

public class ServiceReference extends ServiceMetadata{

    public ServiceReference(){}

    /**
     * Constructs a service reference from a service
     * @param service Service
     */
    public ServiceReference(Service service){
        setGUID(service.getGUID());
        setVersion(service.getVersion());
        setLicensed(service.isLicensed());

        setDisplayName(service.getDisplayName());
        setCategories(service.getCategories());

        setIcon16(service.getIcon16());
        setIcon32(service.getIcon32());

        setCreationDate(service.getCreationDate());
        setLastModifiedDate(service.getLastModifiedDate());
        setAuthors(service.getAuthors());

        setShortDescription(service.getShortDescription());
        setLongDescription(service.getLongDescription());

        setLabel(service.getDeployment().getLabel());
        setSupportedOperatingSystems(service.getDeployment().getSupportedOperatingSystems());
        setAutoInstallable(service.getDeployment().isAutoInstallable());

        launchable = service.getExecution()!=null;
        if(launchable){
            setType(service.getExecution().getType());
            setJCA(service.getExecution().isJCA());
        }
    }

    /**
     * Returns ID of this object.
     * @return int - Object ID
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_SERVICE_REFERENCE;
    }

    /*-------------------------------------------------[ label ]---------------------------------------------------*/

    private String label;

    /**
     * Returns deployment label for this service
     * @return String - Label for this service
     */
    public String getLabel(){
        return label;
    }

    /**
     * Sets deployment label for this service
     * @param label Label to be set
     */
    public void setLabel(String label){
        this.label = label;
    }

    /*-------------------------------------------------[ OS ]---------------------------------------------------*/

    private int supportedOperatingSystems = Deployment.OS_ALL;

    /**
     * Returns supported Operating Systems for this service
     * @return int - Supported OS in integer
     */
    public int getSupportedOperatingSystems(){
        return supportedOperatingSystems;
    }

    /**
     * Sets supporting operating systems for this service
     * @param supportedOperatingSystems integer for supported os
     */
    public void setSupportedOperatingSystems(int supportedOperatingSystems){
        this.supportedOperatingSystems = supportedOperatingSystems;
    }

    /**
     * Sets a boolean which checks whether specified os is supported for this service
     * @param os Operating system
     * @param supported true if the operating system is supported for the service, false otherwise
     */
    public void setOSSupported(int os, boolean supported){
        if(supported)
            supportedOperatingSystems |= os;
        else
            supportedOperatingSystems &= ~os;
    }

    /**
     * Resets supported operating system to ALL
     */
    public void resetSupportedOperatingSystems(){
        supportedOperatingSystems = Deployment.OS_ALL;
    }

    /**
     * Returns true if specified operating system is supported for this service, otherwise false
     * @param os Operating system
     * @return boolean - true if operating system is supported for this service, false otherwise
     */
    public boolean isOperatingSystemSupported(int os){
        return supportedOperatingSystems==Deployment.OS_ALL || (supportedOperatingSystems & os) == os;
    }

    /*-------------------------------------------------[ AutoInstallable ]---------------------------------------------------*/

    private boolean autoInstallable = true;

    /**
     * Specifies whether this service is Auto Installable or not
     * @return boolean - true if the service is auto installable, false otherwise
     */
    public boolean isAutoInstallable(){
        return autoInstallable;
    }

    /**
     * Sets a boolean specifying whether this service is auto installable
     * @param autoInstallable true if the service is auto installable, false otherwise
     */
    public void setAutoInstallable(boolean autoInstallable){
        this.autoInstallable = autoInstallable;
    }

    /*-------------------------------------------------[ allowed to launch ]---------------------------------------------------*/

    private boolean launchable;

    /**
     * Returns a boolean specifying if this service is allowed to launch
     * @return boolean - true if the service is allowed to launch, false otherwise
     */
    public boolean isLaunchable(){
        return launchable;
    }

    /**
     * Sets a boolean specifying if this service is allowed to launch
     * @param launchable true if the service is allowed to launch, false otherwise
     */
    public void setLaunchable(boolean launchable){
        this.launchable = launchable;
    }

    /*-------------------------------------------------[ Type ]---------------------------------------------------*/

    private int type = Execution.TYPE_JAVA;

    /**
     * Returns type of service execution
     * @return int - Service execution type
     */
    public int getType(){
        return type;
    }

    /**
     * Sets type of service execution
     * @param type - type to be set in integer form
     */
    public void setType(int type){
        this.type = type;
    }

    /*-------------------------------------------------[ JCA ]---------------------------------------------------*/

    private boolean jca = true;

    /**
     * Returns a boolean specifying if this is a jca service
     * @return boolean - true if the service is a JCA service, false otherwise
     */
    public boolean isJCA(){
        return jca;
    }

    /**
     * Sets a boolean specifying if this is a jca service
     * @param jca true if the service is a JCA service, false otherwise
     */
    public void setJCA(boolean jca){
        this.jca = jca;
    }

    /*-------------------------------------------------[ Equality ]---------------------------------------------------*/
    /**
     * Returns hashcode of this service
     * @return int - Hash code for the service
     */
    public int hashCode(){
        return super.hashCode()
                + (launchable ? 1 : 0)
                + Util.hashCode(label);
    }

    /**
     * Overrides equals method in Object
     * @param obj Object
     * @return boolean - true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if(obj instanceof ServiceReference){
            ServiceReference that = (ServiceReference)obj;
            return super.equals(obj)
                    && this.launchable==that.launchable
                    && Util.equals(this.label, that.label);
        }else
            return false;
    }

    /*-------------------------------------------------[ XMLization ]---------------------------------------------------*/

    /**
     * <service>
     *      ...execution?...
     *      <deployment label="string"/>
     * </service>
     */

    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException{
        if(launchable)
            writer.writeEmptyElement(Execution.ELEM_EXECUTION);

        writer.writeStartElement(Deployment.ELEM_DEPLOYMENT);
        {
            if(label!=null)
                writer.writeAttribute(Deployment.ATTR_LABEL, label);
            if(supportedOperatingSystems!=Deployment.OS_ALL)
                writer.writeAttribute(Deployment.ATTR_OS, String.valueOf(supportedOperatingSystems));
            if(!autoInstallable)
                writer.writeAttribute(Deployment.ATTR_AUTO_INSTALLABLE, String.valueOf(autoInstallable));
        }
        writer.writeEndElement();

        if(launchable){
            writer.writeStartElement(Execution.ELEM_EXECUTION);
            {
                if(type!=Execution.TYPE_JAVA)
                    writer.writeAttribute(Execution.ATTR_TYPE, String.valueOf(type));
                else if(!jca)
                    writer.writeAttribute(Execution.ATTR_JCA, "edbc");
            }
            writer.writeEndElement();
        }
    }

    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(Deployment.ELEM_DEPLOYMENT.equals(cursor.getLocalName())){
            label = cursor.getAttributeValue(null, Deployment.ATTR_LABEL);
            supportedOperatingSystems = getIntegerAttribute(cursor, Deployment.ATTR_OS, Deployment.OS_ALL);
            autoInstallable = getBooleanAttribute(cursor, Deployment.ATTR_AUTO_INSTALLABLE, true);
        }else if(Execution.ELEM_EXECUTION.equals(cursor.getLocalName())){
            launchable = true;
            type = getIntegerAttribute(cursor, Execution.ATTR_TYPE, Execution.TYPE_JAVA);
            jca = !"edbc".equals(cursor.getAttributeValue(null, Execution.ATTR_JCA));
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    protected void convert_1(ServiceHeader that){
        label = that.getLabel();

        launchable = that.isLaunchable();
    }

    protected void convert_1(ServicePropertySheet that){
        resetSupportedOperatingSystems();
        Enumeration enumer = that.getDeploymentInfo().getApplicableOperatingSystems();
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

        autoInstallable = that.getDeploymentInfo().isAutoInstallable();

        com.fiorano.openesb.application.sps.Execution thatExec = that.getExecutionInfo();
        type = "java".equalsIgnoreCase(thatExec.getExecType()) ? Execution.TYPE_JAVA : Execution.TYPE_EXECUTABLE;
        jca = Param.getParamWithName(thatExec.getParams(), "BCDK")!=null;
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        super.reset();

        label = null;
        supportedOperatingSystems = Deployment.OS_ALL;
        autoInstallable = true;

        launchable = false;
        type = Execution.TYPE_JAVA;
        jca = true;
    }
}
