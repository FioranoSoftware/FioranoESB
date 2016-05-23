/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.UTFReaderWriter;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;

public class LogManager extends InflatableDMIObject{
    /**
     * element logmanager in event process xml
     */
    public static final String ELEM_LOG_MANAGER = "logmanager";
    /**
     * Out handler
     */
    public static final String OUT_HANDLER = "OUT_HANDLER";
    /**
     * error handler
     */
    public static final String ERR_HANDLER = "ERR_HANDLER";

    public int getObjectID(){
        return DmiObjectTypes.NEW_LOG_MANAGER;
    }

    /*-------------------------------------------------[ loggerClass ]---------------------------------------------------*/
    /**
     * attribute logger
     */
    public static final String ATTR_LOGGER_CLASS = "logger";
    /**
     * FileHandler classname
     */
    public static final String LOGGER_CLASS_FILE = FileHandler.class.getName();
    /**
     * route logger classname
     */
    public static final String LOGGER_ROUTE_CLASS_FILE = LogManager.class.getName();
    /**
     * Console Handler classname
     */
    public static final String LOGGER_CLASS_CONSOLE = ConsoleHandler.class.getName();

    private String loggerClass;

    /**
     * Returns logger class name
     * @return String class name
     */
    public String getLoggerClass(){
        return loggerClass;
    }

    /**
     * Sets specified <code>loggerClass</code> for log manager
     * @param loggerClass logger class to be set
     */
    public void setLoggerClass(String loggerClass){
        this.loggerClass = loggerClass;
    }

    /*-------------------------------------------------[ properties ]---------------------------------------------------*/
    /**
     * element property in event process xml
     */
    public static final String ELEM_PROPERTY = "property";
    /**
     * element Param in event process xml
     */
    public static final String ELEM_PARAM = "Param";
    /**
     * attribute name
     */
    public static final String ATTR_PROPERTY_NAME = "name";
    /**
     * include time stamp property
     */
    public static final String PROP_INCLUDE_TIME_STAMP = "fiorano.jms.log2.def.DefaultFormatter.includetimestamp"; //NOI18N
    /**
     * date format property
     */
    public static final String PROP_DATE_FORMAT="fiorano.jms.log2.def.DefaultFormatter.dateformat";
    private static Properties DEFAULT_PROPS_FILE_LOGGER = new Properties();
    private static Properties DEFAULT_PROPS_ROUTE_FILE_LOGGER = new Properties();
    /**
     * log file directory property
     */
    public static final String FILE_PROP_DIRECTORY = "java.util.logging.FileHandler.dir"; //NOI18N
    /**
     * log file size property
     */
    public static final String FILE_PROP_SIZE_LIMIT = "java.util.logging.FileHandler.limit"; //NOI18N
    /**
     * maximum log files property
     */
    public static final String FILE_PROP_MAX_FILE_COUNT = "java.util.logging.FileHandler.count"; //NOI18N

    private static Properties DEFAULT_PROPS_CONSOLE_LOGGER = new Properties();

    static{
        DEFAULT_PROPS_FILE_LOGGER.put(FILE_PROP_DIRECTORY, "log");
        DEFAULT_PROPS_FILE_LOGGER.put(FILE_PROP_SIZE_LIMIT, "1000000");
        DEFAULT_PROPS_FILE_LOGGER.put(FILE_PROP_MAX_FILE_COUNT, "4");
        DEFAULT_PROPS_FILE_LOGGER.put(PROP_INCLUDE_TIME_STAMP, "true");
        DEFAULT_PROPS_FILE_LOGGER.put(PROP_DATE_FORMAT, "MM/dd/yyyy HH:mm:ss");

        DEFAULT_PROPS_ROUTE_FILE_LOGGER.put(FILE_PROP_DIRECTORY, "log");
        DEFAULT_PROPS_ROUTE_FILE_LOGGER.put(FILE_PROP_SIZE_LIMIT, "1000000");
        DEFAULT_PROPS_ROUTE_FILE_LOGGER.put(FILE_PROP_MAX_FILE_COUNT, "4");
        DEFAULT_PROPS_ROUTE_FILE_LOGGER.put(PROP_INCLUDE_TIME_STAMP, "true");
        DEFAULT_PROPS_ROUTE_FILE_LOGGER.put(PROP_DATE_FORMAT, "MM/dd/yyyy HH:mm:ss");


        DEFAULT_PROPS_CONSOLE_LOGGER.put(PROP_INCLUDE_TIME_STAMP, "true");
        DEFAULT_PROPS_CONSOLE_LOGGER.put(PROP_DATE_FORMAT, "MM/dd/yyyy HH:mm:ss");
    }

    private Properties props = new Properties();

    /**
     * Returns properties like logs ditectory, log file size etc.
     * @return Properties
     */
    public Properties getProps(){
        return props;
    }

    /**
     * Sets specified properties <code>props</code> for logManager
     * @param props properties to be set
     */
    public void setProps(Properties props){
        this.props = props;
    }

    /**
     * Adds specified property to properties of log manager
     * @param name property name
     * @param value property value
     */
    public void addProperty(String name,String value){
        props.put(name,value);
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <logmanager logger="string">
     *      <property name="string">string</property>*
     * </logmanager>
     */

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{

        writer.writeStartElement(ELEM_LOG_MANAGER);
        {
            writer.writeAttribute(ATTR_LOGGER_CLASS, loggerClass);

            Iterator iter = props.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                    writer.writeStartElement(ELEM_PARAM);
                    {
                        writer.writeAttribute(ATTR_PROPERTY_NAME, (String)entry.getKey());
                        writer.writeCharacters((String)entry.getValue());
                    }
                    writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_LOG_MANAGER)){
            loggerClass = cursor.getAttributeValue(null, ATTR_LOGGER_CLASS);

            // find and initialize default properties
            Properties defaults;
            if(LOGGER_CLASS_FILE.equals(loggerClass))
                defaults = DEFAULT_PROPS_FILE_LOGGER;
            else if(LOGGER_CLASS_CONSOLE.equals(loggerClass))
                defaults = DEFAULT_PROPS_CONSOLE_LOGGER;
            else if(LOGGER_ROUTE_CLASS_FILE.equals(loggerClass))
                defaults = DEFAULT_PROPS_ROUTE_FILE_LOGGER;
            else
                defaults = new Properties();
            String PARAM = "Param";
            while(cursor.nextElement()){
                if(PARAM.equals(cursor.getLocalName())){
                    if(cursor.getAttributeValue(null,"name").equals(PROP_INCLUDE_TIME_STAMP)){
                        String value = cursor.getText();
                        defaults.put(PROP_INCLUDE_TIME_STAMP,value );
                    }
                    else
                    if(cursor.getAttributeValue(null,"name").equals(PROP_DATE_FORMAT)){
                         defaults.put(PROP_DATE_FORMAT,cursor.getText());
                    }
                    else
                    if(cursor.getAttributeValue(null,"name").equals(FILE_PROP_DIRECTORY)){
                         defaults.put(FILE_PROP_DIRECTORY,cursor.getText());
                    }
                    else
                    if(cursor.getAttributeValue(null,"name").equals(FILE_PROP_MAX_FILE_COUNT)){
                         defaults.put(FILE_PROP_MAX_FILE_COUNT,cursor.getText());
                    }
                    else
                    if(cursor.getAttributeValue(null,"name").equals(FILE_PROP_SIZE_LIMIT)){
                         defaults.put(FILE_PROP_SIZE_LIMIT,cursor.getText());
                    }
                }
            }
            props.putAll(defaults);

            while(cursor.nextElement()){
               if(ELEM_PROPERTY.equals(cursor.getLocalName()))
                   addProperty(cursor.getAttributeValue(null, ATTR_PROPERTY_NAME), cursor.getText());
            }
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts old log manager DMI structure to new
     * @param that old service instance DMI
     */
    public void convert(com.fiorano.openesb.application.aps.ServiceInstance that){
        loggerClass = that.getLogManager();

        Enumeration enumer = that.getLogParameters();
        while(enumer.hasMoreElements()){
            Param param = (Param)enumer.nextElement();
            addProperty(param.getParamName(), param.getParamValue());
        }
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        loggerClass = null;
        props.clear();
    }

    /**
     * @bundle LOGGER_CLASS_UNSPECIFIED==The Logger Class Name is not specified
     */
    public void validate() throws FioranoException{

        if(StringUtils.isEmpty(loggerClass))
            throw new FioranoException("LOGGER_CLASS_UNSPECIFIED");
    }
    /************************************************[ Streaming ]************************************************/

    public void fromStream(java.io.DataInput is, int versionNo) throws java.io.IOException{

        super.fromStream(is, versionNo);
        int count = is.readInt();
        if (count != 0)
        {   Properties properties = new Properties();
            String name;
            String value;
            for (int i = 0; i < count; i++) {
                properties.put(UTFReaderWriter.readUTF(is),UTFReaderWriter.readUTF(is));
            }
            this.setProps(properties);
        }
    }

    public void toStream(java.io.DataOutput out, int versionNo) throws java.io.IOException{

        super.toStream(out, versionNo);
        out.writeInt(props.size());
        Iterator iter = props.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            UTFReaderWriter.writeUTF (out, (String)entry.getKey());
            UTFReaderWriter.writeUTF(out, (String)entry.getValue());
        }
    }

}
