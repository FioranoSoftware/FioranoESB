/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.ServiceHeader;
import com.fiorano.openesb.utils.CollectionUtil;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;
import com.fiorano.openesb.utils.Util;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class ServiceMetadata extends InflatableDMIObject{
    /**
     * Element service in service descriptor xml
     */
    public static final String ELEM_SERVICE = "service";

    public int getObjectID(){
        return DmiObjectTypes.NEW_SERVICE_METADATA;
    }

    /*-------------------------------------------------[ GUID ]---------------------------------------------------*/
    /**
     * Attribute GUID
     */
    public static final String ATTR_GUID = "guid";

    private String guid;

    /**
     * Returns the Global Unique Identifier of this service
     * @return String - GUID of the service
     */
    public String getGUID(){
        return guid;
    }

    /**
     * Sets GUID of this service
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
     * Returns version number of this service
     * @return float - version number of the service
     */
    public float getVersion(){
        return version;
    }

    /**
     * Sets version of this service
     * @param version version to be set
     */
    public void setVersion(float version){
        this.version = version;
    }

    /*-------------------------------------------------[ Licensed ]---------------------------------------------------*/
    /**
     * Attribute licensed
     */
    public static final String ATTR_LICENSED = "licensed";

    private boolean licensed = false;

    /**
     * Returns whether the service is licensed or not
     * @return boolean - true if the service is licensed, false otherwise
     */
    public boolean isLicensed(){
        return licensed;
    }

    /**
     * Sets a boolean specifying if this service is licensed
     * @param licensed true if the service is licensed, false otherwise
     */
    public void setLicensed(boolean licensed){
        this.licensed = licensed;
    }

    /*-------------------------------------------------[ creationDate ]---------------------------------------------------*/
    /**
     * Element creation in service descriptor xml
     */
    public static final String ELEM_CREATION = "creation";
    /**
     * Attribute date
     */
    public static final String ATTR_CREATION_DATE = "date";

    private Date creationDate = new Date();

    /**
     * Returns the date on which this service is created
     * @return Date - Creation date
     */
    public Date getCreationDate(){
        return creationDate;
    }

    /**
     * Sets creation date for this service
     * @param creationDate Creation date
     */
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }

    /*-------------------------------------------------[ creationDate ]---------------------------------------------------*/
    /**
     * Attribute last-modified
     */
    public static final String ATTR_LAST_MODIFIED_DATE = "last-modified";

    private Date lastModifiedDate = new Date();

    /**
     * Returns the date on which this service is last modified
     * @return Date - Last modified date
     */
    public Date getLastModifiedDate(){
        return lastModifiedDate;
    }

    /**
     * Sets last modified date of this service
     * @param lastModifiedDate Last modified date
     */
    public void setLastModifiedDate(Date lastModifiedDate){
        this.lastModifiedDate = lastModifiedDate;
    }

    /*-------------------------------------------------[ DisplayName ]---------------------------------------------------*/
    /**
     * Element display in service descriptor xml
     */
    public static final String ELEM_DISPLAY = "display";
    /**
     * Attribute name
     */
    public static final String ATTR_DISPLAY_NAME = "name";

    private String displayName;

    /**
     * Returns display name used for this service
     * @return String - Display name
     */
    public String getDisplayName(){
        return displayName;
    }

    /**
     * Sets display name for this service
     * @param displayName Display name to be set
     */
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    /*-------------------------------------------------[ ShortDescription ]---------------------------------------------------*/
    /**
     * Element short-description in service descriptor xml
     */
    public static final String ELEM_SHORT_DESCRIPTION = "short-description";

    private String shortDescription;

    /**
     * Returns short description of this service
     * @return String - Short description
     */
    public String getShortDescription(){
        return shortDescription;
    }

    /**
     * Sets short description of this service
     * @param shortDescription Description to be set
     */
    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    /*-------------------------------------------------[ LongDescription ]---------------------------------------------------*/
    /**
     * Element long-description in service descriptor xml
     */
    public static final String ELEM_LONG_DESCRIPTION = "long-description";

    private String longDescription;

    /**
     * Returns long description of this service
     * @return String - Long description
     */
    public String getLongDescription(){
        return longDescription;
    }

    /**
     * Sets long description of this service
     * @param longDescription Long description
     */
    public void setLongDescription(String longDescription){
        this.longDescription = longDescription;
    }

    /*-------------------------------------------------[ Authors ]---------------------------------------------------*/
    /**
     * Attribute authors
     */
    public static final String ATTR_AUTHORS = "authors";

    private String authors[] = new String[0];

    /**
     * Returns names of the authors who created this service
     * @return String Array - Names of authors
     */
    public String[] getAuthors(){
        return authors;
    }

    /**
     * Sets authors of this service
     * @param authors Authors to be set
     */
    public void setAuthors(String[] authors){
        this.authors = authors;
    }

    /*-------------------------------------------------[ Categories ]---------------------------------------------------*/
    /**
     * Attribute categories
     */
    public static final String ATTR_CATEGORIES = "categories";

    private List categories = new ArrayList();

    /**
     * Returns categories in which this service is shown
     * @return List - List of categories
     */
    public List getCategories(){
        return categories;
    }

    /**
     * Sets categories in which this service is shown
     * @param categories Categories in which this service is shown
     */
    public void setCategories(List categories){
        this.categories = categories;
    }

    /*-------------------------------------------------[ Icon16-Name ]---------------------------------------------------*/
    /**
     * Element icon in service descriptor xml
     */
    public static final String ELEM_ICON = "icon";
    /**
     * Attrbute small
     */
    public static final String ATTR_ICON16 = "small";

    /**
     * Returns 16-bit Icon
     */
    private String icon16;

    /**
     * Returns 16 bit icon for this service
     * @return String - 16bit icon name
     */
    public String getIcon16(){
        return icon16;
    }

    /**
     * Sets icon for this service
     * @param icon16 16bit icon name
     */
    public void setIcon16(String icon16){
        this.icon16 = icon16;
    }

    /*-------------------------------------------------[ Icon32-Name ]---------------------------------------------------*/
    /**
     * Attribute large
     */
    public static final String ATTR_ICON32 = "large";

    private String icon32;

    /**
     * Returns 32-bit Icon for this service
     * @return String - 32bit icon name
     */
    public String getIcon32(){
        return icon32;
    }

    /**
     * Sets 32 bit icon for this service
     * @param icon32 32bit icon name
     */
    public void setIcon32(String icon32){
        this.icon32 = icon32;
    }

    /*-------------------------------------------------[ Equality ]---------------------------------------------------*/

    /**
     * Returns hash code of the service
     * @return int - hash code of the service
     */
    public int hashCode(){
        return Util.hashCode(guid)
                + (int)version
                + (licensed ? 1 : 0)
                + Util.hashCode(creationDate)
                + Util.hashCode(lastModifiedDate)
                + Util.hashCode(displayName)
                + Util.hashCode(shortDescription)
                + Util.hashCode(longDescription)
                + Util.hashCode(authors)
                + Util.hashCode(categories)
                + Util.hashCode(icon16)
                + Util.hashCode(icon32);
    }

    /**
     * Overrides equals method in Object
     * @param obj Object
     * @return boolean - returns true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if(obj instanceof ServiceMetadata){
            ServiceMetadata that = (ServiceMetadata)obj;
            return Util.equals(this.guid, that.guid)
                    && this.version == that.version
                    && this.licensed == that.licensed
                    && Util.equals(this.creationDate, that.creationDate)
                    && Util.equals(this.lastModifiedDate, that.lastModifiedDate)
                    && Util.equals(this.displayName, that.displayName)
                    && Util.equals(this.shortDescription, that.shortDescription)
                    && Util.equals(this.longDescription, that.longDescription)
                    && Arrays.equals(this.authors, that.authors)
                    && Util.equals(this.categories, that.categories)
                    && Util.equals(this.icon16, that.icon16)
                    && Util.equals(this.icon32, that.icon32);
        }else
            return false;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <service guid="string" version="float" licensed="boolean">
     *      <display name="string" categories="cvs"/>
     *      <icon small="string"? large="string"?/>?
     *      <creation date="date" last-modified="date" authors="cvs"?/>
     *      <short-description>string</short-description>?
     *      <long-description>string</long-description>?
     *      ... subclass_1 ....
     * </service>
     */

    protected void toJXMLString(XMLStreamWriter writer,boolean writeCDataSections) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_SERVICE);
        {
            writer.writeAttribute(ATTR_GUID, guid);
            writer.writeAttribute(ATTR_VERSION, String.valueOf(version));
            if(licensed)
                writeAttribute(writer, ATTR_LICENSED, licensed);

            writer.writeStartElement(ELEM_DISPLAY);
            {
                writer.writeAttribute(ATTR_DISPLAY_NAME, displayName);
                writeAttribute(writer, ATTR_CATEGORIES, categories);
            }
            writer.writeEndElement();

            if(!StringUtil.isEmpty(icon16) || !StringUtil.isEmpty(icon32)){
                writer.writeStartElement(ELEM_ICON);
                {
                    if(!StringUtil.isEmpty(icon16))
                        writer.writeAttribute(ATTR_ICON16, icon16);
                    if(!StringUtil.isEmpty(icon32))
                        writer.writeAttribute(ATTR_ICON32, icon32);
                }
                writer.writeEndElement();
            }

            writer.writeStartElement(ELEM_CREATION);
            {
                writeAttribute(writer, ATTR_CREATION_DATE, creationDate);
                writeAttribute(writer, ATTR_LAST_MODIFIED_DATE, lastModifiedDate);
                if(authors.length>0)
                    writeAttribute(writer, ATTR_AUTHORS, authors);
            }
            writer.writeEndElement();

            writeElement(writer, ELEM_SHORT_DESCRIPTION, shortDescription);
            writeElement(writer, ELEM_LONG_DESCRIPTION, longDescription);

            toJXMLString_1(writer,writeCDataSections);
        }
        writer.writeEndElement();
    }

    // subclasses can write any content here
    protected void toJXMLString_1(XMLStreamWriter writer,boolean writeCDataSections) throws XMLStreamException, FioranoException{}

     protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        toJXMLString(writer, true);
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        try{
            if(cursor.markCursor(ELEM_SERVICE)){
            guid = cursor.getAttributeValue(null, ATTR_GUID);
                version = Float.parseFloat(cursor.getAttributeValue(null, ATTR_VERSION));
                licensed = getBooleanAttribute(cursor, ATTR_LICENSED, false);

                while(cursor.nextElement()){
                    String elemName = cursor.getLocalName();
                    if(ELEM_DISPLAY.equals(elemName)){
                        displayName = cursor.getAttributeValue(null, ATTR_DISPLAY_NAME);
                        categories = getListAttribute(cursor, ATTR_CATEGORIES, new ArrayList());
                    }else if(ELEM_ICON.equals(elemName)){
                        icon16 = cursor.getAttributeValue(null, ATTR_ICON16);
                        icon32 = cursor.getAttributeValue(null, ATTR_ICON32);
                    }else if(ELEM_CREATION.equals(elemName)){
                        creationDate = getDateAttribute(cursor, ATTR_CREATION_DATE, new Date());
                        lastModifiedDate = getDateAttribute(cursor, ATTR_LAST_MODIFIED_DATE, null);
                        authors = getStringArrayAttribute(cursor, ATTR_AUTHORS, new String[0]);
                    }else if(ELEM_SHORT_DESCRIPTION.equals(elemName))
                        shortDescription = cursor.getText();
                    else if(ELEM_LONG_DESCRIPTION.equals(elemName))
                        longDescription = cursor.getText();
                    else
                        populate_1(cursor);
                }
            }
        } catch(ParseException ex){
            throw new FioranoException( ex);
        }
    }

    // subclasses can read additonal information using this
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ServiceHeader that){
        guid = that.getServiceGUID();
        version = that.getVersion();
        licensed = that.isLicensed();

        displayName = that.getDisplayName();
        categories = CollectionUtil.toCollection(that.getCategories());

        icon16 = that.getIcon();
        icon32 = that.getLargeIcon();

        creationDate = that.getCreationDateAsDate();
        lastModifiedDate = that.getUpdatedDate();
        authors = (String[])CollectionUtil.toCollection(that.getServiceAuthors()).toArray(new String[0]);

        shortDescription = that.getShortDescription();
        longDescription = that.getLongDescription();

        convert_1(that);
    }

    protected void convert_1(ServiceHeader that){}

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(com.fiorano.openesb.application.sps.ServicePropertySheet that){
        convert(that.getServiceHeader());
        convert_1(that);
    }

    protected void convert_1(com.fiorano.openesb.application.sps.ServicePropertySheet that){}

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        guid = null;
        version = 0;
        licensed = true;
        creationDate = new Date();
        lastModifiedDate = new Date();
        displayName = null;
        shortDescription = null;
        longDescription = null;
        authors = new String[0];
        categories.clear();
        icon16 = null;
        icon32 = null;
    }

    /**
     * @bundle GUID_UNSPECIFIED==GUID is not specified
     * @bundle INVALID_VERSION_UNSPECIFIED==Version Number must be greater than zero
     * @bundle DISPLAY_NAME_UNSPECIFIED=Display Name is not specified
     * @bundle CATEGORIES_UNSPECIFIED=Categories are not specified
     * @bundle CREATION_DATE_UNSPECIFIED=Creation Date is not specified
     * @bundle LAST_MODIFIED_DATE_UNSPECIFIED=LastModified Date is not specified
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(guid))
            throw new FioranoException("GUID_UNSPECIFIED");
        if(version<=0)
            throw new FioranoException( "INVALID_VERSION_UNSPECIFIED");
        if(StringUtil.isEmpty(displayName))
            throw new FioranoException( "DISPLAY_NAME_UNSPECIFIED");
        if(categories==null || categories.size()==0)
            throw new FioranoException("CATEGORIES_UNSPECIFIED");
        if(creationDate==null)
            throw new FioranoException("CREATION_DATE_UNSPECIFIED");
        if(lastModifiedDate==null)
            throw new FioranoException( "LAST_MODIFIED_DATE_UNSPECIFIED");
    }
}
