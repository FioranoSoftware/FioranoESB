/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.NamedObject;
import com.fiorano.openesb.application.aps.ExternalServiceInstance;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class RemoteServiceInstance extends InflatableDMIObject implements NamedObject{
    /**
     * Element remote-inst in event process xml
     */
    public static final String ELEM_REMOTE_SERVICE_INSTANCE = "remote-inst";

    public int getObjectID(){
        return DmiObjectTypes.NEW_REMOTE_SERVICE_INSTANCE;
    }

    /*-------------------------------------------------[ applicationGUID ]---------------------------------------------------*/
    /**
     * Attribute application-guid
     */
    public static final String ATTR_APPLICATION_GUID = "application-guid";

    private String applicationGUID;

    /**
     * Returns remote GUID of the application to which this service instance belongs
     * @return String - Remote GUID of application
     */
    public String getApplicationGUID(){
        return applicationGUID;
    }

    /**
     * Sets specified application GUID <code>guid</code> of this remote service instance
     * @param guid application GUID to be set
     */
    public void setApplicationGUID(String guid){
        this.applicationGUID = guid;
    }

    /*-------------------------------------------------[ applicationVersion ]---------------------------------------------------*/
    /**
     * Attribute application-version
     */
    public static final String ATTR_APPLICATION_VERSION = "application-version";

    private float applicationVersion;

    /**
     * Returns version of the application to which this service instance belongs
     * @return float - Application version
     */
    public float getApplicationVersion(){
        return applicationVersion;
    }

    /**
     * Sets specified application version  <code>version</code>  for this remote service instance
     * @param version Application version
     */
    public void setApplicationVersion(float version){
        this.applicationVersion = version;
    }

    /*-------------------------------------------------[ application-schema-version ]---------------------------------------------------*/
    /**
     * Attribute application schema version
     */
    public static final String ATTR_APPLICATION_SCHEMA_VERSION = "application-schema-version";

    private String applicationSchemaVersion = "1.0";

    /**
     * Returns schema-version for this event process
     * @return String - Application schema version
     */
    public String getApplicationSchemaVersion(){
        return applicationSchemaVersion;
    }

    /**
     * Sets application-schema-version for this event process
     * @param schemaVersion Application-schema-version to be set
     */
    public void setApplicationSchemaVersion(String schemaVersion){
        this.applicationSchemaVersion = schemaVersion;
    }

    /*-------------------------------------------------[ internalServiceInstanceName ]---------------------------------------------------*/
    /**
     * Attribute inst-name
     */
    public static final String ATTR_REMOTE_NAME = "inst-name";

    private String remoteName;

    /**
     * Returns name of the service instance in owner application
     * @return String - Remote service name in owner application
     */
    public String getRemoteName(){
        return remoteName;
    }

    /**
     * Sets specified name <code>remoteName</code> of service instance in owner application
     * @param remoteName Name of remote service instance
     */
    public void setRemoteName(String remoteName){
        this.remoteName = remoteName;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns name of the remote service instance
     * @return String - Name of remote service instance
     */
    public String getName(){
        return name;
    }

    /**
     * Sets name of remote service instance
     * @param name Name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <remoteinst name="string" application-guid="string" application-version="float" inst-name="string"/>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_REMOTE_SERVICE_INSTANCE);
        {
            writer.writeAttribute(ATTR_NAME, name);
            writer.writeAttribute(ATTR_APPLICATION_GUID, applicationGUID);
            writer.writeAttribute(ATTR_APPLICATION_VERSION, String.valueOf(applicationVersion));
            writer.writeAttribute(ATTR_APPLICATION_SCHEMA_VERSION, applicationSchemaVersion);
            writer.writeAttribute(ATTR_REMOTE_NAME, remoteName);
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/
    /**
     * populates the RemoteServiceInstance DMI.
     * The Possible Error Mesages:
     * @bundle INVALID_REMOTE_APPLICATIONVERSION_UNSPECIFIED=Version Number of the remote Application should be a float.
     */
    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if (cursor.markCursor(ELEM_REMOTE_SERVICE_INSTANCE)) {
            name = cursor.getAttributeValue(null, ATTR_NAME);
            applicationGUID = cursor.getAttributeValue(null, ATTR_APPLICATION_GUID);
            String s = cursor.getAttributeValue(null, ATTR_APPLICATION_VERSION);
            if (s == null)
                throw new FioranoException("INVALID_REMOTE_APPLICATIONVERSION_UNSPECIFIED");
            applicationVersion = Float.parseFloat(s);
            applicationSchemaVersion = getStringAttribute(cursor, ATTR_APPLICATION_SCHEMA_VERSION, "1.0");
            remoteName = cursor.getAttributeValue(null, ATTR_REMOTE_NAME);
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ExternalServiceInstance that){
        name = that.getInstanceName();
        applicationGUID = that.getApplicationGUID();
        applicationVersion = Float.parseFloat(that.getApplicationVersion());
        remoteName = that.getActualInstanceName();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        applicationGUID = null;
        applicationVersion = 1.0f;
        applicationSchemaVersion = "1.0";
        remoteName = null;
        name = null;
    }

    /**
     * Validates the RemoteServiceInstance DMI. Check whther all the manditory fileds are set.
     *
     * The Possible Error Mesages:
     * @bundle REMOTE_APPLCIATION_GUID_UNSPECIFIED=The GUID of the remote Appliccation is not specified
     * @bundle INVALID_REMOTE_APPLICATIONVERSION_UNSPECIFIED=Version Number of the remote Appliccation must be greater than zero
     * @bundle REMOTE_INSTANCE_NAME_UNSPECIFIED=The Instance name in the remote Appliccation is not specified
     * @bundle LOCAL_INSTANCE_NAME_UNSPECIFIED=The Service Instance Name is not specified
     */
    public void validate() throws FioranoException{
        validateRemoteServiceName();
        if(StringUtils.isEmpty(applicationGUID))
            throw new FioranoException( "REMOTE_APPLCIATION_GUID_UNSPECIFIED");
        if(applicationVersion<0)
            throw new FioranoException("INVALID_REMOTE_APPLICATIONVERSION_UNSPECIFIED");
        if(StringUtils.isEmpty(remoteName))
            throw new FioranoException("REMOTE_INSTANCE_NAME_UNSPECIFIED");
        if(StringUtils.isEmpty(name))
            throw new FioranoException("LOCAL_INSTANCE_NAME_UNSPECIFIED");
    }


    /**
     * Validates name given a Remote ServiceInstance
     * Possible error messages
     * @bundle REMOTE_SERVICE_NAME_UNSPECIFIED=Remote ServiceInstance Name is not specified
     * @bundle INVALID_REMOTE_SERVICE_NAME=Remote ServiceInstance Name cannot have these characters . ~ ! @ # % ^ ( ) [ ] { } + - * '(single quote) ,(comma) ;
     * @bundle INVALID_REMOTE_SERVICE_NAME_START=Remote ServiceInstance name cannot start with an underscore (_)
     * @bundle INVALID_REMOTE_SERVICE_NAME_DOUBLEUNDER=Remote ServiceInstance name cannot contain a double underscore (__)
     * @throws FioranoException If name is invalid
     */
    private void validateRemoteServiceName() throws FioranoException
    {
        //Arun: Remote service 'name' doesn't break application even if it contains specials. Only 'remotename' must be checked for that.
        //These are redundant checks as, if the remote service is running, its name will be valid.
        if (StringUtils.isEmpty(name))
            throw new FioranoException("REMOTE_SERVICE_NAME_UNSPECIFIED");
        else if (DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(remoteName).find())
            throw new FioranoException("INVALID_REMOTE_SERVICE_NAME");

        if (remoteName.startsWith("_"))
            throw new FioranoException("INVALID_REMOTE_SERVICE_NAME_START");
        if (remoteName.contains("__"))
            throw new FioranoException( "INVALID_REMOTE_SERVICE_NAME_DOUBLEUNDER");
    }
    /**
     * Returns key
     * @return String - Key
     */
    public String getKey() {
        return getName();
    }
}
