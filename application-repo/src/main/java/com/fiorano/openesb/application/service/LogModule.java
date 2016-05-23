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
import com.fiorano.openesb.application.aps.ApsLogModule;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.logging.Level;

public class LogModule extends InflatableDMIObject implements NamedObject{
    /**
     * element logmodule in service descriptor xml
     */
    public static final String ELEM_LOGMODULE = "logmodule";

    public int getObjectID(){
        return DmiObjectTypes.NEW_LOGMODULE;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns name of this service
     * @return Name of the service
     */
    public String getName(){
        return name;
    }

    /**
     * Sets name of this log module
     * @param name log module name
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ Level ]---------------------------------------------------*/
    /**
     * attribute level
     */
    public static final String ATTR_TRACE_LEVEL = "level";

    private Level traceLevel = Level.SEVERE;

    /**
     * Returns Trace Level for this log module
     * @return Trace Level
     */
    public Level getTraceLevel(){
        return traceLevel;
    }

    /**
     * Sets trace level for this log module
     * @param level level to be set
     */
    public void setTraceLevel(Level level){
        this.traceLevel = level;
    }

    /**
     * Returns trace level of this log module
     * @return trace level as String
     */
    public String getTraceLevelAsString(){
        if(this.traceLevel == null)
            return "";
        else
            return this.traceLevel.toString();
    }

    /**
     * Sets trace level of this log module
     * @param levelAsString trace level
     */
    public void setTraceLevel(String levelAsString){
        if(StringUtil.isEmpty(levelAsString))
            this.traceLevel = null;
        else
            try{
                this.traceLevel = Level.parse(levelAsString);
            } catch(IllegalArgumentException e){
                this.traceLevel = null;
            }
    }

    /**
     * Converts specified string to Level
     * @param levelAsString level as string
     * @return Level
     */
    public static Level parseLevel(String levelAsString){
        if(StringUtil.isEmpty(levelAsString))
            return null;
        else
            try{
                return Level.parse(levelAsString);
            } catch(IllegalArgumentException e){
                return null;
            }
    }

    /*-------------------------------------------------[ UniqueID ]---------------------------------------------------*/
    /**
     * attribute level
     */
    public static final String ATTR_UNIQUE_NAME = "uniqueNameRequired";

    private boolean uniqueNameRequired = true;

    /**
     * Returns the boolean value denoting the unique identifier requirement for this logModule
     *
     * @return  true if required
     */
    public boolean isUniqueNameRequired() {
        return uniqueNameRequired;
    }

    /**
     * Sets the boolean value to determine whether a unique identifier is required for this logModule
     *
     * @param uniqueNameRequired  boolean value to determine whether a unique identifier is required
     */
    public void setUniqueNameRequired(boolean uniqueNameRequired) {
        this.uniqueNameRequired = uniqueNameRequired;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <logmodule name="string" level="string"/>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeStartElement(ELEM_LOGMODULE);
        {
            writer.writeAttribute(ATTR_NAME, name);
            if(traceLevel!=Level.SEVERE)
                writer.writeAttribute(ATTR_TRACE_LEVEL, getTraceLevelAsString());
            writer.writeAttribute(ATTR_UNIQUE_NAME, String.valueOf(uniqueNameRequired));
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_LOGMODULE)){
            name = cursor.getAttributeValue(null, ATTR_NAME);
            setTraceLevel(getStringAttribute(cursor, ATTR_TRACE_LEVEL, Level.SEVERE.toString()));
            uniqueNameRequired = getBooleanAttribute(cursor, ATTR_UNIQUE_NAME, true);
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that  old DMI
     */
    public void convert(com.fiorano.openesb.application.sps.LogModule that){
        name = that.getModuleName();
        setTraceLevel(that.getDefaultTraceLevel());
        uniqueNameRequired = true;
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(ApsLogModule that){
        name = that.getName();
        setTraceLevel(that.getTraceLevel());
        uniqueNameRequired = true;
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        name = null;
        traceLevel = Level.SEVERE;
        uniqueNameRequired = true;
    }

    /**
     * @bundle LOGMODULE_NAME_UNSPECIFIED==LogModule name is not specified
     * @bundle TRACE_LEVEL_UNSPECIFIED==Trace Level is not specified
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(name))
            throw new FioranoException( "LOGMODULE_NAME_UNSPECIFIED");
    }

    /**
     * Returns key for this log module
     * @return Name of this log module
     */
    public String getKey(){
        return getName();
    }
}
