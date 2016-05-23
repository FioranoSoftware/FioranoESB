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
import com.fiorano.openesb.application.aps.RuntimeDependency;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.ServiceDependency;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ServiceRef extends InflatableDMIObject implements NamedObject{
    /**
     * element serviceref in service descriptor xml
     */
    public static final String ELEM_SERVICEREF = "serviceref";

    /**
     * Returns ID of this object.
     * @return int - Object ID
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_SERVICE_REF;
    }

    /*-------------------------------------------------[ GUID ]---------------------------------------------------*/
    /**
     * attribute GUID
     */
    public static final String ATTR_GUID = "guid";

    private String guid;

    /**
     * Returns the Global Unique Identifier of this service reference
     * @return String - GUID of service reference
     */
    public String getGUID(){
        return guid;
    }

    /**
     * Set GUID of this service reference
     * @param guid GUID of service reference to be set
     */
    public void setGUID(String guid){
        this.guid = guid;
    }

    /*-------------------------------------------------[ Version ]---------------------------------------------------*/
    /**
     * attribute version
     */
    public static final String ATTR_VERSION = "version";

    private float version;

    /**
     * Returns version of this service reference
     * @return float - version number of service reference
     */
    public float getVersion(){
        return version;
    }

    /**
     * Sets version of this service reference
     * @param version version number of service reference to be set
     */
    public void setVersion(float version){
        this.version = version;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <serviceref guid="string" version="float"/>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_SERVICEREF);
        {
            writer.writeAttribute(ATTR_GUID, guid);
            writer.writeAttribute(ATTR_VERSION, String.valueOf(version));
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_SERVICEREF)){
            guid = cursor.getAttributeValue(null, ATTR_GUID);
            version = Float.parseFloat(cursor.getAttributeValue(null, ATTR_VERSION));
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ServiceDependency that){
        guid = that.getServiceGUID();
        version = Float.parseFloat(that.getVersion());
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(RuntimeDependency that){
        guid = that.getServiceGUID();
        version = Float.parseFloat(that.getVersion());
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        guid = null;
        version = 0;
    }

    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(guid))
            throw new FioranoException("GUID_UNSPECIFIED");
        if(version<=0)
            throw new FioranoException("INVALID_VERSION_UNSPECIFIED");
    }

    /**
     * Returns GUID+version for this service reference
     * @return String - Name of the service reference
     */
    public String getName(){
        return guid+":"+version;
    }

    /**
     * Returns key for this service reference
     * @return String - key for the service reference
     */
    public String getKey(){
        return getName();
    }
}
