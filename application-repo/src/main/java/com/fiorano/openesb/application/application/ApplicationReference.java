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
import com.fiorano.openesb.application.aps.ApplicationHeader;
import com.fiorano.openesb.application.aps.ApplicationPropertySheet;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.text.ParseException;
import java.util.*;

public class ApplicationReference extends InflatableDMIObject{
    /**
     * element application in event process xml
     */
    public static final String ELEM_APPLICATION = "application";


    public static final String ELEM_SERVER_VERSION = "Metadata";
    /**
     * Returns ID of this object. This is used internally to identify different types of DMI objects.
     * @return the id of this object.
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_APPLICATION_REFERENCE;
    }

    /**
     * Default constructor
     */
    public ApplicationReference(){
        super();
        serverVersion = "0.1.0";
        serverCategory = "openESB";
        buildNo = "1";
    }
    public enum APP_CORE_CONFIG_FLAG {Yes,No,Fetch_From_FES;
        public boolean equals(String str){
            return this == APP_CORE_CONFIG_FLAG.valueOf(str);
        }
    };

    /**
     * Converts an Application DMI to ApplicationReference DMI
     * @param application Application DMI
     */
    public ApplicationReference(Application application){
        this.guid = application.getGUID();
        this.schemaVersion = application.getSchemaVersion();
        this.version = application.getVersion();
        this.deleteDestination= APP_CORE_CONFIG_FLAG.valueOf(application.getDeleteDestination());
        this.appRouteDurability= APP_CORE_CONFIG_FLAG.valueOf(application.getAppRouteDurability());
        this.creationDate = application.getCreationDate();
        this.displayName = application.getDisplayName();
        this.serverVersion = "11";
        this.serverCategory="os";
        this.buildNo = "1";
        this.shortDescription = application.getShortDescription();
        this.longDescription = application.getLongDescription();
        this.authors = application.getAuthors();
        this.categories = application.getCategories();
        this.label = application.getLabel();
        this.componentCached = application.isComponentCached();
        this.typeName = application.getTypeName();
        this.subType = application.getSubType();
        this.componentLaunchOrderEnabled = application.isComponentLaunchOrderEnabled();
    }
    /*-------------------------------------------------[ GUID ]---------------------------------------------------*/
    /**
     * attribute guid
     */
    public static final String ATTR_GUID = "guid";

    private String guid;

    private  String serverVersion;

    private String serverCategory;

    private String buildNo;

    private String userName="admin";

    /**
     * Returns the Global Unique Identifier of this event process
     * @return String
     */
    public String getGUID(){
        return guid;
    }

    /**
     * Sets the Global Unique Identifier for this event process
     * @param guid GUID to be set
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
     * Returns version of this event process
     * @return float
     */
    public float getVersion(){
        return version;
    }

    /**
     * Sets specified <code>version</code> for this event process
     * @param version version to be set
     */
    public void setVersion(float version){
        this.version = version;
    }

    public static final String ATTR_SERVER_VERSION = "productVersion";

    public static final String ATTR_SERVER_CATEGORY = "productCategory";

    public static final String ATTR_BUILD_NO = "buildNo";

    public static final String ATTR_USER_NAME = "createdBy";

    /**
     * Returns product version
     * @return float
     */
    public String getServerVersion(){
        return serverVersion;
    }

    /**
     * Returns product version
     * @return float
     */
    public String getServerCategory(){
        return serverCategory;
    }

    /*-------------------------------------------------[ creationDate ]---------------------------------------------------*/
    /**
     * element creation in event process xml
     */
    public static final String ELEM_CREATION="creation";
    /**
     * attribute date
     */
    public static final String ATTR_CREATION_DATE = "date";

    private Date creationDate = new Date();

    /**
     * Returns date on which this event process is created
     * @return Date
     */
    public Date getCreationDate(){
        return creationDate;
    }

    /**
     * Sets specified <code>creationDate</code> for this event process
     * @param creationDate creation date of this event process
     */
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }

    /*-------------------------------------------------[ DisplayName ]---------------------------------------------------*/
    /**
     * element display in event process xml
     */
    public static final String ELEM_DISPLAY="display";
    /**
     * attribute name
     */
    public static final String ATTR_DISPLAY_NAME = "name";

    private String displayName;

    /**
     * Returns display name used for this event process
     * @return String
     */
    public String getDisplayName(){
        return displayName;
    }

    /**
     * Sets specified <code>displayName</code> for this event process
     * @param displayName display Name to be set
     */
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    /*-----------------------------------------------[ Type ]------------------------------------------------------------*/

    /**
     * element display in event process xml
     */
    public static final String ELEM_TYPE = "type";

    /**
     * attribute name
     */
    public static final String ATTR_TYPE_NAME = "name";

    /**
     * attribute sub-type
     */
    public static final String ATTR_SUB_TYPE = "sub-type";

    private String typeName;

    private String subType;

    /**
     * Returns <code>subType</code> for this event process
     * @return String  SubType
     */
    public String getSubType() {
        return subType != null ? subType.toLowerCase() : null;
    }

    /**
     * Sets <code>subType</code> for this event process
     * @param subType String SubType
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * Returns type <code>name</code> for this event process
     * @return String
     */
    public String getTypeName() {
        return typeName != null ? typeName.toLowerCase() : null;
    }

    /**
     * Sets event process type <code>name</code>
     * @param name TypeName
     */
    public void setTypeName(String name) {
        this.typeName = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Types 1)b2b 2)mdm 3)default
     *
     * Denotes the module type of the application
     *
     * @author FSTPL
     * @version 10
     */
    public enum Type {
        /**
         * Denotes Business-to-business Application Type
         */
        b2b,
        /**
         * Denotes Master Data Management Application Type (Not Used Currently)
         */
        mdm,
        /**
         * Denotes Default Application Type
         */
        Default;

        /**
         * Returns String representation of Application Type
         * @return String Type
         */
        @Override
        public String toString() {
            switch (this){
                case b2b: return "b2b";
                case mdm: return "mdm";
                case Default: return "default";
                default: return null;
            }
        }
    }

    /**
     *
     * Types 1)internal 2)userDefined
     * Denotes the SubType of an application
     * @author FSTPL
     * @version 10
     */
    public enum SubType {
        /**
         * Denotes sample applications
         */
        internal,
        /**
         * Denotes user defined application
         */
        userDefined;

        /**
         * Return String representation of SubType
         * @return String SubType
         */
        @Override
        public String toString() {
            switch (this){
                case internal: return "internal";
                case userDefined: return  "user-defined";
                default: return null;
            }
        }
    }

    /*-------------------------------------------------[ ShortDescription ]---------------------------------------------------*/
    /**
     * element short-description in event process xml
     */
    public static final String ELEM_SHORT_DESCRIPTION = "short-description";

    private String shortDescription;

    /**
     * Returns short description of this event process (can be null)
     * @return String
     */
    public String getShortDescription(){
        return shortDescription;
    }

    /**
     * Sets specified <code>shortDescription</code> of this event process
     * @param shortDescription short description to be set
     */
    public void setShortDescription(String shortDescription){
        this.shortDescription = shortDescription;
    }

    /*-------------------------------------------------[ LongDescription ]---------------------------------------------------*/
    /**
     * element long-description in event process xml
     */
    public static final String ELEM_LONG_DESCRIPTION = "long-description";

    private String longDescription;

    /**
     * Returns long description of this event process (can be null)
     * @return String
     */
    public String getLongDescription(){
        return longDescription;
    }

    /**
     * Sets long description for this event process
     * @param longDescription long description to be set
     */
    public void setLongDescription(String longDescription){
        this.longDescription = longDescription;
    }

    /*-------------------------------------------------[ Authors ]---------------------------------------------------*/
    /**
     * attribute authors
     */
    public static final String ATTR_AUTHORS = "authors";

    private String authors[] = new String[0];

    /**
     * Returns names of the authors who created this event process
     * @return String[]
     */
    public String[] getAuthors(){
        return authors;
    }

    /**
     * Sets authors for this event process
     * @param authors authors to be set
     */
    public void setAuthors(String[] authors){
        this.authors = authors;
    }

    /*-------------------------------------------------[ Schema-Version ]---------------------------------------------------*/
    /**
     * attribute authors
     */
    public static final String ATTR_SCHEMA_VERSION = "schema-version";

    private String schemaVersion = "1.0";

    /**
     * Returns schema-version for this event process
     * @return String
     */
    public String getSchemaVersion(){
        return schemaVersion;
    }

    /**
     * Sets schema-version for this event process
     * @param schemaVersion schema-version to be set
     */
    public void setSchemaVersion(String schemaVersion){
        this.schemaVersion = schemaVersion;
    }

    /*-------------------------------------------------[ Categories ]---------------------------------------------------*/
    /**
     * attribute categories
     */
    public static final String ATTR_CATEGORIES = "categories";

    private List categories = Collections.EMPTY_LIST;

    /**
     * Returns categories in which this event process is shown
     * @return List
     */
    public List getCategories(){
        return categories;
    }

    /**
     * Sets categories in which this event process is shown
     * @param categories categories to be set
     */
    public void setCategories(List categories){
        this.categories = categories;
    }

    /*-------------------------------------------------[ Label ]---------------------------------------------------*/
    /**
     * element deployment in event process xml
     */
    public static final String ELEM_DEPLOYMENT="deployment";
    /**
     * attribute label
     */
    public static final String ATTR_LABEL = "label";

    private String label;

    /**
     * Returns Event Process label which determines the stage to which the Event Process
     * has evolved to. This is one of the building blocks for rules created with Deployment Manager
     * @return String
     */
    public String getLabel(){
        return label.toLowerCase();
    }

    /**
     * Sets label for this event process
     * @param label label
     */
    public void setLabel(String label){
        this.label = label;
    }
    /*-------------------------------------------------[ deleteDestinationAfterStop ]---------------------------------------------------*/
    /**
     * element deleteDestinationAfterStop in event process xml
     */
    public static final String ELEM_DL_DST="deleteDestinationAfterStop";
    /**
     * attribute label
     */
    public static final String ATTR_DL_DST = "value";


    /**
     * attribute priority in event process xml
     */
    public APP_CORE_CONFIG_FLAG deleteDestination = APP_CORE_CONFIG_FLAG.Fetch_From_FES;


    public String getDeleteDestination() {
        return deleteDestination.toString();
    }

    public void setDeleteDestinationPriority(String deleteDestination) {
        this.deleteDestination = APP_CORE_CONFIG_FLAG.valueOf(deleteDestination);
    }

/*-------------------------------------------------[ App Route Durability ]---------------------------------------------------*/
    /**
     * element routeDurable in event process xml
     */
    public static final String ELEM_DURABLE_ROUTE ="durableRoute";
    /**
     * attribute value
     */
    public static final String ATTR_DURABLE_ROUTE_VALUE = "value";

    /**
     * If Yes, No Ignore fes level flag
     * If Fetch_From_FES then takes the fes level flag
     * Default: Fetch_From_FES
     */
    private APP_CORE_CONFIG_FLAG appRouteDurability = APP_CORE_CONFIG_FLAG.Fetch_From_FES;


    public String getAppRouteDurability() {
        return appRouteDurability.toString();
    }

    public void setAppRouteDurability(String routeDurable) {
        this.appRouteDurability = APP_CORE_CONFIG_FLAG.valueOf(routeDurable);
    }

    /*-------------------------------------------------[ componentCacheDisabled ]---------------------------------------------------*/
    /**
     * attribute cache
     */
    public static final String ATTR_COMPONENT_CACHED = "cache";

    private boolean componentCached = true;

    /**
     * Returns a boolean specifying cache component for this event process.
     * If false, re-fetches the service resources (excluding dependencies
     * eg: Java, RTL, XML, UTIL, etc) each time the application is launched. If this is true, then value of
     * service instance is taken into account
     * @return boolean
     */
    public boolean isComponentCached(){
        return componentCached;
    }

    /**
     * Sets a boolean specifying cache component for this event process
     * @param componentCached boolean
     */
    public void setComponentCached(boolean componentCached){
        this.componentCached = componentCached;
    }

    /*-------------------------------------------------[ Component Launch Order ]----------------------------------------------------*/

    /**
     * Component launch order in Event Process Xml
     */
    public static final String ELEM_COMPONENT_LAUNCH_ORDER = "component-launch-order";

    /**
     * Component Launch Order is enabled in Event process Xml
     */
    public static final String ATTR_COMPONENT_LAUNCH_ORDER_ENABLED = "enabled";

    private boolean componentLaunchOrderEnabled = false;

    /**
     * Returns boolean specifying whether the component launch is enabled or not
     * true when enabled and false for disable
     * @return boolean true when enabled
     */
    public boolean isComponentLaunchOrderEnabled() {
        return componentLaunchOrderEnabled;
    }

    /**
     * This method sets component launch order status
     * @param componentLaunchOrderEnabled boolean true or false
     */
    public void setComponentLaunchOrderEnabled(boolean componentLaunchOrderEnabled) {
        this.componentLaunchOrderEnabled = componentLaunchOrderEnabled;
    }

    /*-------------------------------------------------[Component stop order ]------------------------------------------------*/
    /**
     * Component stop order in Event Process Xml
     */
    public static final String ELEM_COMPONENT_STOP_ORDER = "component-stop-order";

    /**
     * Component Stop Order is enabled in Event process Xml
     */
    public static final String ATTR_COMPONENT_STOP_ORDER_ENABLED = "enabled";


    /**
     * Use reverse component launch order for stopping of components
     */
    public static final String ATTR_USE_REVERSE_ORDER_OF_COMPONENT_LAUNCH_ORDER = "useReverseComponentLaunchOrder";

    private boolean componentStopOrderEnabled = false;

    private boolean reverseComponentLaunchOrder = false;

    /**
     * Return boolean specifying whether to use reverse of Component launch order or not
     * @return boolean true when enabled
     */
    public boolean isReverseComponentLaunchOrder() {
        return reverseComponentLaunchOrder;
    }

    /**
     * sets whether to use reverse of component launch order
     * @param reverseComponentLaunchOrder boolean
     */
    public void setReverseComponentLaunchOrder(boolean reverseComponentLaunchOrder) {
        this.reverseComponentLaunchOrder = reverseComponentLaunchOrder;
    }

    /**
     * Returns boolean specifying whether the component launch is enabled or not
     * true when enabled and false for disable
     * @return boolean true when enabled
     */
    public boolean isComponentStopOrderEnabled() {
        return componentStopOrderEnabled;
    }

    /**
     * This method sets component launch order status
     * @param componentStopOrderEnabled boolean true or false
     */
    public void setComponentStopOrderEnabled(boolean componentStopOrderEnabled) {
        this.componentStopOrderEnabled = componentStopOrderEnabled;
    }


    /*-------------------------------------------------[ WEB Interface Support ]---------------------------------------------------*/
    /**
     * element web in event process xml
     */
    public static final String ELEM_WEB ="web";
    /**
     * aatribute exposedInterfaces
     */
    public static final String ATTR_EXPOSED_INTERFACES = "exposedInterfaces";
    /**
     * web interface type none
     */
    public static final int WEB_INTERFACES_NONE =0;
    /**
     * web interface type web service
     */
    public static final int WEB_SERVICE = 1;
    /**
     * web interface type http
     */
    public static final int HTTP_SERVICE =2;
    /**
     * web interface type REST
     */
    public static final int REST_SERVICE = 8;

    /**
     * web interface type worklist
     */
    public static final int WORK_LIST= 4;
    private int exposedWebInterfaces =WEB_INTERFACES_NONE;

    /**
     * Sets a boolean specifying whether given web interface type is supported for this event process.
     * It can be web service, http service, worklist or none of them.
     * @param interfaceType web interface type
     * @param supported boolean specifying if specified <code>interfaceType</code> is supported for this event process
     */
    public void setWebInterfaceSupport(int interfaceType, boolean supported){
        if(interfaceType == WEB_INTERFACES_NONE){
            resetWebInterfaceSupport();
            return;
        }
        if(supported)
            exposedWebInterfaces |= interfaceType;
        else
            exposedWebInterfaces &= ~interfaceType;
    }

    /**
     * Returns boolean specifying whether specified web interface <code>interfaceType</code> is supported for this event process
     * @param interfaceType web interface type
     * @return boolean
     */
    public boolean isWebInterfaceSupported(int interfaceType){
        return interfaceType==WEB_INTERFACES_NONE
                ? exposedWebInterfaces==WEB_INTERFACES_NONE
                : (exposedWebInterfaces & interfaceType) == interfaceType;
    }

    /**
     * Resets webinterface type to none for this event process
     */
    public void resetWebInterfaceSupport(){
        exposedWebInterfaces = WEB_INTERFACES_NONE;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <application guid="string" version="string">
     *      <display name="string" categories="cvs"/>
     *      <creation date="date" authors="cvs"/>
     *      <type name="string" sub-type="string"/>
     *      <short-description>string</short-description/>
     *      <long-description>string</long-description/>
     *      <deployment label="string" cache="boolean"/>
     *      <component-launch-order enabled = "boolean"/>
     *      <component-stop-order  enabled="boolean"  useReverseComponentLaunchOrder="boolean"/>
     * </application>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeManageableProperties) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_APPLICATION);
        {
            writer.writeAttribute(ATTR_GUID, guid);
            writer.writeAttribute(ATTR_VERSION, String.valueOf(version));

            writer.writeStartElement(ELEM_DISPLAY);
            {
                writer.writeAttribute(ATTR_DISPLAY_NAME, displayName);
                writeAttribute(writer, ATTR_CATEGORIES, categories);
            }
            writer.writeEndElement();

            writer.writeStartElement(ELEM_SERVER_VERSION);
            {
                writer.writeAttribute(ATTR_SERVER_VERSION, serverVersion);
                writer.writeAttribute(ATTR_SERVER_CATEGORY, serverCategory );
                writer.writeAttribute(ATTR_BUILD_NO, buildNo);
                writer.writeAttribute(ATTR_USER_NAME, userName);
            }
            writer.writeEndElement();

            writer.writeStartElement(ELEM_CREATION);
            {
                writeAttribute(writer, ATTR_CREATION_DATE, creationDate);
                writeAttribute(writer, ATTR_AUTHORS, authors);
                writeAttribute(writer, ATTR_SCHEMA_VERSION, schemaVersion);
            }
            writer.writeEndElement();

            if (typeName != null){
                writer.writeStartElement(ELEM_TYPE);
                {
                    writeAttribute(writer, ATTR_TYPE_NAME, getTypeName());
                    if (subType != null)
                        writeAttribute(writer, ATTR_SUB_TYPE, getSubType());
                }
                writer.writeEndElement();
            }

            writeElement(writer, ELEM_SHORT_DESCRIPTION, shortDescription);
            writeElement(writer, ELEM_LONG_DESCRIPTION, longDescription);

            writer.writeStartElement(ELEM_DEPLOYMENT);
            {
                writer.writeAttribute(ATTR_LABEL, label);
                if(!componentCached)
                    writer.writeAttribute(ATTR_COMPONENT_CACHED, String.valueOf(componentCached));
            }
            writer.writeEndElement();
            writer.writeStartElement(ELEM_DL_DST);
            {
                writer.writeAttribute(String.valueOf(ATTR_DL_DST),String.valueOf(deleteDestination));
            }
            writer.writeEndElement();
            //write App RouteDurability
            writer.writeStartElement(ELEM_DURABLE_ROUTE);
            {
                writer.writeAttribute(ATTR_DURABLE_ROUTE_VALUE, String.valueOf(appRouteDurability));
            }
            writer.writeEndElement();

            if (componentLaunchOrderEnabled){
                writer.writeStartElement(ELEM_COMPONENT_LAUNCH_ORDER);
                {
                    writer.writeAttribute(ATTR_COMPONENT_LAUNCH_ORDER_ENABLED, String.valueOf(componentLaunchOrderEnabled));
                }
                writer.writeEndElement();
            }

            if (componentStopOrderEnabled){
                writer.writeStartElement(ELEM_COMPONENT_STOP_ORDER);
                {
                    writer.writeAttribute(ATTR_COMPONENT_STOP_ORDER_ENABLED, String.valueOf(componentStopOrderEnabled));
                    writer.writeAttribute(ATTR_USE_REVERSE_ORDER_OF_COMPONENT_LAUNCH_ORDER, String.valueOf(reverseComponentLaunchOrder));
                }
                writer.writeEndElement();
            }

            if(exposedWebInterfaces!=WEB_INTERFACES_NONE){
                writer.writeStartElement(ELEM_WEB);
                {
                    writeAttribute(writer, ATTR_EXPOSED_INTERFACES, exposedWebInterfaces);

                }
                writer.writeEndElement();
            }

            toJXMLString_1(writer, writeManageableProperties);
        }
        writer.writeEndElement();
    }


    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        toJXMLString(writer, true);
    }// subclasses can write any content here
    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeManageableProperties) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException{
        try{
            if(cursor.markCursor(ELEM_APPLICATION)){
                guid = cursor.getAttributeValue(null, ATTR_GUID);
                version = Float.parseFloat(cursor.getAttributeValue(null, ATTR_VERSION));
                while(cursor.nextElement()){
                    String elemName = cursor.getLocalName();
                    if(ELEM_DISPLAY.equals(elemName)){
                        displayName = cursor.getAttributeValue(null, ATTR_DISPLAY_NAME);
                        categories = getListAttribute(cursor, ATTR_CATEGORIES, new ArrayList());
                    }else if(ELEM_CREATION.equals(elemName)){
                        creationDate = getDateAttribute(cursor, ATTR_CREATION_DATE, new Date());
                        authors = getStringArrayAttribute(cursor, ATTR_AUTHORS, new String[0]);
                        schemaVersion = getStringAttribute(cursor, ATTR_SCHEMA_VERSION, "1.0");
                    }else if(ELEM_SHORT_DESCRIPTION.equals(elemName))
                        shortDescription = cursor.getText();
                    else if(ELEM_LONG_DESCRIPTION.equals(elemName))
                        longDescription = cursor.getText();
                    else if(ELEM_DEPLOYMENT.equals(elemName)){
                        label = cursor.getAttributeValue(null, ATTR_LABEL);
                        componentCached = getBooleanAttribute(cursor, ATTR_COMPONENT_CACHED, true);
                    }else if(ELEM_DL_DST.equals(elemName)){
                        String value=  cursor.getAttributeValue(null,ATTR_DL_DST);
                        if(value != null)
                            deleteDestination = APP_CORE_CONFIG_FLAG.valueOf(value);
                        else
                            deleteDestination = APP_CORE_CONFIG_FLAG.Fetch_From_FES;
                    }else if(ELEM_DURABLE_ROUTE.equals(elemName)){
                        String value= cursor.getAttributeValue(null, ATTR_DURABLE_ROUTE_VALUE);
                        if(value != null)
                            appRouteDurability = APP_CORE_CONFIG_FLAG.valueOf(value);
                        else
                            appRouteDurability = APP_CORE_CONFIG_FLAG.Fetch_From_FES;
                    } else if(ELEM_DURABLE_ROUTE.equals(elemName)){
                        appRouteDurability = APP_CORE_CONFIG_FLAG.valueOf(cursor.getAttributeValue(null, ATTR_DURABLE_ROUTE_VALUE));
                    }else if(ELEM_WEB.equals(elemName)){
                        exposedWebInterfaces = getIntegerAttribute(cursor, ATTR_EXPOSED_INTERFACES, WEB_INTERFACES_NONE);
                    }else if (ELEM_TYPE.equals(elemName)){
                        typeName = cursor.getAttributeValue(null, ATTR_TYPE_NAME);
                        subType = cursor.getAttributeValue(null, ATTR_SUB_TYPE);
                    }else if (ELEM_COMPONENT_LAUNCH_ORDER.equals(elemName)){
                        componentLaunchOrderEnabled = getBooleanAttribute(cursor, ATTR_COMPONENT_LAUNCH_ORDER_ENABLED, false);
                    }else if (ELEM_COMPONENT_STOP_ORDER.equals(elemName)){
                        componentStopOrderEnabled = getBooleanAttribute(cursor, ATTR_COMPONENT_STOP_ORDER_ENABLED, false);
                        reverseComponentLaunchOrder = getBooleanAttribute(cursor, ATTR_USE_REVERSE_ORDER_OF_COMPONENT_LAUNCH_ORDER, false);
                    }else
                        populate_1(cursor);
                }
            }
            //validate();
        } catch(ParseException ex){
            throw new FioranoException(ex);
        }
    }

    // allow subclasses to read additional content using this
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException,FioranoException{}

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ApplicationHeader that){
        guid = that.getApplicationGUID();
        version = Float.parseFloat(that.getVersionNumber());

        displayName = that.getApplicationName();
        categories = Arrays.asList(that.getCategoryIn().split(","));


        creationDate = that.getCreationDateAsDate();
        authors = (String[]) Collections.list(that.getAuthors()).toArray();

        shortDescription = that.getShortDescription();
        longDescription = that.getLongDescription();

        label = that.getProfile();
        componentCached = !that.isServiceNoCache();
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ApplicationPropertySheet that){
        convert(that.getApplicationHeader());
        convert_1(that);
    }

    protected void convert_1(ApplicationPropertySheet that){}

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    /**
     * Reset the values of ApplicationReference Instance data members
     */
    public void reset(){
        guid = null;
        version = 0f;
        creationDate = new Date();
        schemaVersion = "1.0";
        displayName = null;
        typeName = null;
        subType = null;
        shortDescription = null;
        longDescription = null;
        authors = new String[0];
        categories.clear();
        label = null;
        componentCached = true;
        componentLaunchOrderEnabled = false;
        exposedWebInterfaces = WEB_INTERFACES_NONE;
    }

    /**
     * Validates this Application DMI. Checks whether all the mandatory fields are set.
     *
     * The Possible Error Mesages:
     * @bundle GUID_UNSPECIFIED=GUID is not specified
     * @bundle GUID_NOT_SUPPORTED=GUID cannot have these characters . ~ ! @ # % ^ ( ) [ ] { } + - * '(single quote) ,(comma) ;
     * @bundle INVALID_VERSION_UNSPECIFIED=Version Number must be greater than zero
     * @bundle DISPLAY_NAME_UNSPECIFIED=Display Name is not specified
     * @bundle CATEGORIES_UNSPECIFIED=Categories are not specified
     * @bundle CREATION_DATE_UNSPECIFIED=Creation Date is not specified
     */
    public void validate() throws FioranoException{

        validateApplicationName();
        if(version<=0)
            throw new FioranoException("INVALID_VERSION_UNSPECIFIED");
        if(StringUtils.isEmpty(displayName))
            throw new FioranoException( "DISPLAY_NAME_UNSPECIFIED");
        if(categories==null || categories.size()==0)
            throw new FioranoException( "CATEGORIES_UNSPECIFIED");
        if(creationDate==null)
            throw new FioranoException( "CREATION_DATE_UNSPECIFIED");
    }

    /**
     * Validates name given a Remote ServiceInstance
     * Possible error messages
     * @bundle APP_NAME_UNSPECIFIED=Application Name is not specified
     * @bundle INVALID_NAME=Remote Application Name cannot have these characters . ~ ! @ # % ^ ( ) [ ] { } + - * '(single quote) ,(comma) ;
     * @bundle INVALID_NAME_START=Application name cannot start with an underscore (_)
     * @bundle INVALID_NAME_DOUBLEUNDER=Application name cannot contain a double underscore (__)
     * @throws FioranoException If name is not valid
     */
    private void validateApplicationName() throws FioranoException{
        if (StringUtils.isEmpty(guid))
            throw new FioranoException("APP_NAME_UNSPECIFIED");
        else if (DmiObject.INVALID_INPUT_CHARS_REGEX.matcher(guid).find())
            throw new FioranoException( "INVALID_NAME");

        if (guid.contains("__"))   //doesn't break application, but '__' is a constant used extensively, so its not allowed here to be safe
            throw new FioranoException( "INVALID_NAME_DOUBLEUNDER");
    }
}
