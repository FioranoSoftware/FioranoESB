/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.aps.Argument;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.RuntimeArg;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Arrays;

public class RuntimeArgument extends InflatableDMIObject implements NamedObject{
    /**
     * Element runtime-argument in service descriptor xml
     */
    public static final String ELEM_RUNTIME_ARGUMENT = "runtime-argument";

    /**
     * Dafault Constructor
     */
    public RuntimeArgument(){
    }

    /**
     * Constructs a runtime argument from name and value
     * @param name Name
     * @param value Value
     */
    public RuntimeArgument(String name, Object value){
        this.name = name;
        this.value = value;
        if(value!=null)
            type = value.getClass();
    }

    public int getObjectID(){
        return DmiObjectTypes.NEW_RUNTIME_ARGUMENT;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * Attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns name of this runtime argument
     * @return String - Name of the argument
     */
    public String getName(){
        return name;
    }

    /**
     * Sets name of this runtime argument
     * @param name Name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ Type ]---------------------------------------------------*/
    /**
     * Attribute type
     */
    public static final String ATTR_TYPE = "type";
    /**
     * String data type
     */
    public static final Class TYPE_STRING = String.class;
    /**
     * Integer data type
     */
    public static final Class TYPE_INTEGER = Integer.class;
    /**
     * Boolean data type
     */
    public static final Class TYPE_BOOLEAN = Boolean.class;
    /**
     * String data type
     */
    public static final Class TYPE_STRING_ARRAY = String[].class;

    private Class type = String.class;

    /**
     * Returns data type of this runtime argument
     * @return Class - Data type of the argument
     */
    public Class getType(){
        return type;
    }

    /**
     * Sets data type of this runtime argument
     * @param type Data type to be set
     */
    public void setType(Class type){
        this.type = type;
    }

    /*-------------------------------------------------[ required ]---------------------------------------------------*/
    /**
     * Attribute manadatory
     */
    public static final String ATTR_MANDATORY = "mandatory";

    private boolean mandatory = true;

    /**
     * Returns true if this runtime argument is required, false otherwise
     * @return boolean - true if runtime argument is required, false otherwise
     */
    public boolean isMandatory(){
        return mandatory;
    }

    /**
     * Sets a boolean specifying whether this runtime time argument is mandatory
     * @param mandatory true if runtime argument is required, false otherwise
     */
    public void setMandatory(boolean mandatory){
        this.mandatory = mandatory;
    }

    /*-------------------------------------------------[ inMemorySuported ]---------------------------------------------------*/
    /**
     * Attribute inmemory
     */
    public static final String ATTR_IN_MEMOTY_SUPPORTED = "inmemory";

    private boolean inMemorySupported = true;

    /**
     * Returns true if the service supports inMemory Launch
     * @return boolean - true if the service supports inMemory launch, false otherwise
     */
    public boolean isInMemorySupported(){
        return inMemorySupported;
    }

    /**
     * Sets a boolean specifying whether the service supports in memory launch.
     * @param inMemorySupported  true if the service supports inMemory launch, false otherwise
     */
    public void setInMemorySupported(boolean inMemorySupported){
        this.inMemorySupported = inMemorySupported;
    }

    /*-------------------------------------------------[ DefaultValue ]---------------------------------------------------*/
    /**
     * Element value in service descriptor xml
     */
    public static final String ELEM_VALUE = "value";

    private Object value = null;

    /**
     * Returns Value of this runtime argument
     * @return Object - Value of the runtime argument
     */
    public Object getValue(){
        return value;
    }

    /**
     * Sets value of this runtime argument
     * @param value Value to be set
     */
    public void setValue(Object value){
        this.value = value;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <runtime-argument name="string" type="string" mandatory="boolean"? inmemory="boolean"?>
     *      <value>string</value>?
     * </runtime-argument>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_RUNTIME_ARGUMENT);
        {
            writer.writeAttribute(ATTR_NAME, name);
            if(type!=String.class)
                writer.writeAttribute(ATTR_TYPE, type.getName());
            if(!mandatory)
                writer.writeAttribute(ATTR_MANDATORY, String.valueOf(mandatory));
            if(!inMemorySupported)
                writer.writeAttribute(ATTR_IN_MEMOTY_SUPPORTED, String.valueOf(inMemorySupported));
            if(value!=null){
                if(value instanceof String[])
                    writeElement(writer, ELEM_VALUE, StringUtil.toString((String[])value, ",", true));
                else
                    writeElement(writer, ELEM_VALUE, String.valueOf(value));
            }
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        try{
            if(cursor.markCursor(ELEM_RUNTIME_ARGUMENT)){
                name = cursor.getAttributeValue(null, ATTR_NAME);
                type = Class.forName(getStringAttribute(cursor, ATTR_TYPE, String.class.getName()));
                mandatory = getBooleanAttribute(cursor, ATTR_MANDATORY, true);
                inMemorySupported = getBooleanAttribute(cursor, ATTR_IN_MEMOTY_SUPPORTED, true);

                value = null;
                while(cursor.nextElement()){
                    if(ELEM_VALUE.equals(cursor.getLocalName())){
                        value = cursor.getText();
                        if(type==TYPE_INTEGER)
                            value = new Integer((String)value);
                        else if(type==TYPE_BOOLEAN)
                            value = Boolean.valueOf((String)value);
                        else if(type==TYPE_STRING_ARRAY)
                            value = StringUtil.getTokens((String)value, ",", true);
                    }
                }
            }
        } catch(ClassNotFoundException ex){
            throw new FioranoException( ex);
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Returns value of this runtime argument as String
     * @return String - Value as String
     */
    public String getValueAsString(){
        if(value!=null){
            if(value instanceof String[]){
                String str = Arrays.toString((Object[]) value);
                return str.substring(1, str.length()-1);
            }else
                return value.toString();
        }else
            return null;
    }

    /**
     * Sets value of this runtime argument as String
     * @param valueString Value as String
     */
    public void setValueFromString(String valueString){
        if(valueString==null)
            value = null;
        else{
            try{
                if(Integer.class.equals(type))
                    value = new Integer(valueString);
                else if(Float.class.equals(type))
                    value = new Float(valueString);
                else if(Boolean.class.equals(type))
                    value = Boolean.valueOf(valueString);
                else if(String[].class.equals(type))
                    value = StringUtil.getTokens(valueString, ",", false);
                else
                    value = valueString;
            } catch(NumberFormatException e){
                value = null;
            }
        }
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(RuntimeArg that){
        name = that.getArgName();

        String thatType = that.getType();
        if(thatType.equalsIgnoreCase("Text") || thatType.equalsIgnoreCase("String")) //NOI18N
            type = String.class;
        else if(thatType.equalsIgnoreCase("Integer")) //NOI18N
            type = Integer.class;
        else if(thatType.equalsIgnoreCase("Number")) //NOI18N
            type = Float.class;
        else if(thatType.equalsIgnoreCase("Boolean")) //NOI18N
            type = Boolean.class;
        else if(thatType.equalsIgnoreCase("Multiselect")) //NOI18N
            type = String[].class;
        else{
            try{
                type = Class.forName(that.getType());
            } catch(ClassNotFoundException e){
                e.printStackTrace();
                type = String.class;
            }
        }

        mandatory = that.isRequired();
        inMemorySupported = that.isInMemorySupported();

        setValueFromString(that.getValue());
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(Argument that){
        name = that.getName();

        inMemorySupported = that.isInMemorySupported();

        setValueFromString(that.getValue());
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        name = null;
        type = String.class;
        mandatory = true;
        inMemorySupported = true;
        value = null;
    }

    /**
     * @bundle RUNTIME_ARGUMENT_NAME_UNSPECIFIED=Runtime-Argument name is not specified
     * @bundle RUNTIME_ARGUMENT_TYPE_NAME_UNSPECIFIED=Runtime-Argument type is not specified
     * @bundle RUNTIME_ARGUMENT_VALUE_TYPE_MISMATCH=Runtime-Argument value is not of the type specified
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(name))
            throw new FioranoException("RUNTIME_ARGUMENT_NAME_UNSPECIFIED");
        if(type==null)
            throw new FioranoException( "RUNTIME_ARGUMENT_TYPE_NAME_UNSPECIFIED");
        else if(value!=null && !type.isInstance(value))
            throw new FioranoException("RUNTIME_ARGUMENT_VALUE_TYPE_MISMATCH");
    }

    /**
     * Returns key
     * @return String - Key
     */
    public String getKey(){
        return getName();
    }
}
