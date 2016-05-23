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
import com.fiorano.openesb.application.sps.ServiceDependency;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

public class Deployment extends InflatableDMIObject{
    /**
     * element deployment in servicedescriptor xml
     */
    public static final String ELEM_DEPLOYMENT = "deployment";

    /**
     * Returns ID of this object. This is used internally to identify different types of DMI objects.
     * @return the id of this object.
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_DEPLOYMENT;
    }

    /*-------------------------------------------------[ Label ]---------------------------------------------------*/
    /**
     * deployment label
     */
    public static final String ATTR_LABEL = "label";
    /**
     * deployment label production
     */
    public final static String PRODUCT = "PRODUCTION";
    /**
     * deployment label qa
     */
    public final static String QA = "QA";
    /**
     * deployment label development
     */
    public final static String DEVELOPMENT = "DEVELOPMENT";

    private String label;

    /**
     * Returns deployment label
     * @return String
     */
    public String getLabel(){
        return label;
    }

    /**
     * Sets deployment label
     * @param label label to be set
     */
    public void setLabel(String label){
        this.label = label;
    }

    /*-------------------------------------------------[ OS ]---------------------------------------------------*/
    /**
     * attribute os
     */
    public static final String ATTR_OS = "os";
    /**
     * operating system All. Its Value is 0
     */
    public static final int OS_ALL = 0;
    /**
     * operating system Linux. Its value is 1
     */
    public static final int OS_LINUX = 1;
    /**
     * operating system macintosh. Its value is 2
     */
    public static final int OS_MACINTOSH = 2;
    /**
     * operating system windows. Its value is 4
     */
    public static final int OS_WIN32 = 4;
    /**
     * operating system solaris. Its value is 8
     */
    public static final int OS_SOLARIS = 8;

    private int supportedOperatingSystems = OS_ALL;

    /**
     * Returns supported Operating Systems
     * @return int
     */
    public int getSupportedOperatingSystems(){
        return supportedOperatingSystems;
    }

    /**
     * Sets supported operating systems
     * @param supportedOperatingSystems supported operationg systems i.e. 0 for all, 1 for Linux,
     * 2 for Macintosh, 4 for Win32 and 8 for Solaris
     */
    public void setSupportedOperatingSystems(int supportedOperatingSystems){
        this.supportedOperatingSystems = supportedOperatingSystems;
    }

    /**
     * Sets a boolean specifying whether specified operating sysyem <code>os</code> is supported
     * @param os operating system
     * @param supported boolean
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
        supportedOperatingSystems = OS_ALL;
    }

    /**
     * Returns true if specified operating system <code>os</code> is supported
     * @param os operating system
     * @return boolean
     */
    public boolean isOperatingSystemSupported(int os){
        return supportedOperatingSystems==OS_ALL || (supportedOperatingSystems & os) == os;
    }

    /**
     * Returns an integer corresponding to specified operating system <code>currentOS</code>
     * @param currentOS operating system
     * @return int
     */
    public int getOperatingSystem(String currentOS){
        if(currentOS.toLowerCase().indexOf("linux") != -1)
            return OS_LINUX;
        else if(currentOS.toLowerCase().indexOf("win") != -1)
            return OS_WIN32;
        else if(currentOS.toLowerCase().indexOf("sun") != -1)
            return OS_SOLARIS;
        else if(currentOS.toLowerCase().indexOf("mac") != -1)
            return OS_MACINTOSH;
        return 0;
    }

    /*-------------------------------------------------[ AutoInstallable ]---------------------------------------------------*/
    /**
     * Specifies whether this service is Auto Installable or not
     */
    public static final String ATTR_AUTO_INSTALLABLE = "auto-installable";

    private boolean autoInstallable = true;

    /**
     * Returns true if this service is Auto Installable or not
     * @return boolean
     */
    public boolean isAutoInstallable(){
        return autoInstallable;
    }

    /**
     * Sets a boolean specifying whether this service is auto installable
     * @param autoInstallable boolean
     */
    public void setAutoInstallable(boolean autoInstallable){
        this.autoInstallable = autoInstallable;
    }

    /*-------------------------------------------------[ Resources ]---------------------------------------------------*/
    /**
     * Resources element in servicedescriptor xml
     */
    public static final String ELEM_RESOURCES = "resources";

    private List<Resource> resources = new ArrayList<>();

    /**
     * Adds a specified resource <code>resource</code> to this service
     * @param resource resource to be added
     */
    public void addResource(Resource resource){
        resources.add(resource);
    }

    /**
     * Removes specified <code>resource</code> from this service
     * @param resource resource to be removed
     */
    public void removeResource(Resource resource){
        resources.remove(resource);
    }

    /**
     * Returns resources of this service
     * @return List
     */
    public List<Resource> getResources(){
        return resources;
    }

    /**
     * Sets specified list <code>resources</code> as resources of this service
     * @param resources list of resources
     */
    public void setResources(List<Resource> resources){
        this.resources = resources;
    }

    /*-------------------------------------------------[ ServiceRefs ]---------------------------------------------------*/
    /**
     * Service refs element in service descriptor xml
     */
    public static final String ELEM_SERVICEREFS = "servicerefs";

    private List<ServiceRef> serviceRefs = new ArrayList<>();

    /**
     * Adds specified service reference <code>serviceRef</code> to this service
     * @param serviceRef service reference
     */
    public void addServiceRef(ServiceRef serviceRef){
        serviceRefs.add(serviceRef);
    }

    /**
     * Removes specified service reference <code>serviceRef</code> from this service
     * @param serviceRef service reference to be removed
     */
    public void removeServiceRef(ServiceRef serviceRef){
        serviceRefs.remove(serviceRef);
    }

    /**
     * Returns a list of service references of this service
     * @return list of service references
     */
    public List<ServiceRef> getServiceRefs(){
        return serviceRefs;
    }

    /**
     * Sets specified list <code>serviceRefs</code> as service references of this service
     * @param serviceRefs list of service references
     */
    public void setServiceRefs(List<ServiceRef> serviceRefs){
        this.serviceRefs = serviceRefs;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <deployment label="string" os="int"? auto-installable="boolean"?>
     *      <resources>
     *          ...resource+...
     *      </resources>?
     *      <servicerefs>
     *          ...serviceref+...
     *      </servicerefs>?
     * </deployment>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_DEPLOYMENT);
        {
            if(label!=null)
                writer.writeAttribute(ATTR_LABEL, label);
            if(supportedOperatingSystems!=OS_ALL)
                writer.writeAttribute(ATTR_OS, String.valueOf(supportedOperatingSystems));
            if(!autoInstallable)
                writer.writeAttribute(ATTR_AUTO_INSTALLABLE, String.valueOf(autoInstallable));
            writeCollection(writer, resources, ELEM_RESOURCES);
            writeCollection(writer, serviceRefs, ELEM_SERVICEREFS);
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_DEPLOYMENT)){
            label = cursor.getAttributeValue(null, ATTR_LABEL);
            supportedOperatingSystems = getIntegerAttribute(cursor, ATTR_OS, OS_ALL);
            autoInstallable = getBooleanAttribute(cursor, ATTR_AUTO_INSTALLABLE, true);

            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_RESOURCES.equals(elemName)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        if(Resource.ELEM_RESOURCE.equals(cursor.getLocalName())){
                            Resource resource = new Resource();
                            resource.setFieldValues(cursor);
                            resources.add(resource);
                        }
                    }
                    cursor.resetCursor();
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
                }
            }
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI structure
     * @param that old DMI
     */
    public void convert(com.fiorano.openesb.application.sps.Deployment that){
        label = that.getDeploymentStage();

        resetSupportedOperatingSystems();
        Enumeration enumer = that.getApplicableOperatingSystems();
        while(enumer.hasMoreElements()){
            String os = (String)enumer.nextElement();
            if("Linux".equalsIgnoreCase(os))
                setOSSupported(OS_LINUX, true);
            else if("Macintosh".equalsIgnoreCase(os))
                setOSSupported(OS_MACINTOSH, true);
            else if("Win32".equalsIgnoreCase(os))
                setOSSupported(OS_WIN32, true);
            else if("Solaris".equalsIgnoreCase(os))
                setOSSupported(OS_SOLARIS, true);
        }

        autoInstallable = that.isAutoInstallable();

        enumer = that.getResources();
        while(enumer.hasMoreElements()){
            Resource resource = new Resource();
            resource.convert((com.fiorano.openesb.application.sps.Resource)enumer.nextElement());
            resources.add(resource);
        }

        enumer = that.getServiceDependencies();
        while(enumer.hasMoreElements()){
            ServiceRef serviceRef = new ServiceRef();
            serviceRef.convert((ServiceDependency)enumer.nextElement());
            serviceRefs.add(serviceRef);
        }
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    /**
     * Reset the values of this object data members
     */
    public void reset(){
        label = null;
        supportedOperatingSystems = OS_ALL;
        autoInstallable = true;
        resources.clear();
        serviceRefs.clear();
    }

    /**
     * @bundle LABEL_UNSPECIFIED==Label is not specified
     * @bundle RESOURCE_ALREADY_EXISTS=Resource "{0}" already exists
     * @bundle SERVICE_REF_ALREADY_EXISTS=Service Reference "{0}" already exists
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(label))
            throw new FioranoException( "LABEL_UNSPECIFIED");

        Set<String> names = new HashSet<String>();
        Iterator iter = resources.iterator();
        while(iter.hasNext()){
            Resource resource = (Resource)iter.next();
            resource.validate();
            if(!names.add(resource.getName()))
                throw new FioranoException("RESOURCE_ALREADY_EXISTS");
        }

        names.clear();
        iter = serviceRefs.iterator();
        while(iter.hasNext()){
            ServiceRef ref = (ServiceRef)iter.next();
            ref.validate();
            if(!names.add(ref.getGUID()+':'+ref.getVersion()))
                throw new FioranoException("SERVICE_REF_ALREADY_EXISTS");
        }
    }
}
