/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.application.application.ManageableProperty;
import com.fiorano.openesb.application.aps.*;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.application.common.RouteInfo;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.lp.ApplicationLaunchPacket;
import com.fiorano.openesb.application.sps.*;
import com.fiorano.openesb.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DmiObject implements Serializable
{
    private static boolean initialized;


    static {
        initialized = true;


        String[] invalidCharArray = {"\\\\", "/", "\\+", "\\(", "\\)", "~", "`", "=",
                "\\{", "\\}", "\\[", "\\]", "\\^", "\\*", "\\?", "\\$", "\\|", "\\s",
                "!", "@", "#", "%", "\"", "'", ",", ";", ":", "<", ">", "&", "."};
        //Arun: adding '.' to the list as its prescence in names breaks applications, services and routes

        StringBuffer sb = new StringBuffer("[");

        for (String anInvalidCharArray : invalidCharArray) {
            sb.append(anInvalidCharArray);
        }

        sb.append("]+");

        INVALID_INPUT_CHARS_REGEX = Pattern.compile(sb.toString());

    }

    /**
     *  Returns the datamodelObject for the specified type
     *
     * @param type type
     * @return DmiObject
     * @exception FioranoException FioranoException
     */
    public static DmiObject getDatamodelObject(int type)
            throws FioranoException
    {
        switch (type)
        {
            // Added for Tifosi 2.0
            case DmiObjectTypes.CERTIFICATE:
                return new Certificate();
            case DmiObjectTypes.DEPLOYMENT:
                return new Deployment();
            case DmiObjectTypes.EXECUTION:
                return new Execution();
            case DmiObjectTypes.INPORT:
                return new InPort();
            case DmiObjectTypes.EVENT_MODULE:
                return new EventModule();
            case DmiObjectTypes.OUTPORT:
                return new OutPort();
            case DmiObjectTypes.PORT_DESCRIPTOR:
                return new PortDescriptor();
            case DmiObjectTypes.RESOURCE:
                return new Resource();
            case DmiObjectTypes.RUNTIME_ARG:
                return new RuntimeArg();
            case DmiObjectTypes.SECURITY:
                return new Security();
            case DmiObjectTypes.SERVICE_DEPENDENCY:
                return new ServiceDependency();
            case DmiObjectTypes.SERVICE_HEADER:
                return new ServiceHeader();
            case DmiObjectTypes.SERVICE_PROPERTIES:
                return new ServicePropertySheet();
            case DmiObjectTypes.APP_STATE_INFO:
                return new ApplicationStateInfo();
            case DmiObjectTypes.APP_STATE_DETAILS:
                return new ApplicationStateDetails();
            case DmiObjectTypes.SERVICE_STATE_DETAILS:
                return new ServiceInstanceStateDetails();
            case DmiObjectTypes.APPLICATION_HEADER:
                return new ApplicationHeader();
            case DmiObjectTypes.APPLICATION_CONTEXT:
                return new ApplicationContext();
            case DmiObjectTypes.APPLICATION_PROPERTIES:
                return new ApplicationPropertySheet();
            case DmiObjectTypes.APPLICATION_LAUNCHPACKET:
                return new ApplicationLaunchPacket();
            case DmiObjectTypes.ARGUMENT:
                return new Argument();
            case DmiObjectTypes.EXT_SERVICE_INSTANCE:
                return new ExternalServiceInstance();
            case DmiObjectTypes.ON_EXCEPTION:
                return new OnException();
            case DmiObjectTypes.APP_ROUTE:
                return new Route();
            case DmiObjectTypes.ROUTES:
                return new Routes();
            case DmiObjectTypes.MANAGEABLE_PROPERTY:
                return new ManageableProperty();
            case DmiObjectTypes.APP_RUNTIME_ARGS:
                return new RuntimeArgs();
            case DmiObjectTypes.SERVICE_INSTANCE:
                return new ServiceInstance();
            case DmiObjectTypes.SERVICE_INSTANCES:
                return new ServiceInstances();
            case DmiObjectTypes.STATE:
                return new com.fiorano.openesb.application.aps.State();
            case DmiObjectTypes.STATUS_TRACKING:
                return new StatusTracking();
            case DmiObjectTypes.WF_INPORT:
                return new WfInPort();
            case DmiObjectTypes.WF_OUTPORT:
                return new WfOutPort();
            case DmiObjectTypes.WF_EXIT_PORTS:
                return new WorkflowExitPorts();
            case DmiObjectTypes.WF_START_PORTS:
                return new WorkflowStartPorts();
            case DmiObjectTypes.MONITOR:
                return new Monitor();
            case DmiObjectTypes.APS_EVENT_MODULE:
                return new ApsEventModule();
            case DmiObjectTypes.APS_LOG_MODULE:
                return new ApsLogModule();
            case DmiObjectTypes.PARAM:
                return new Param();
            case DmiObjectTypes.SERVICE_SEARCH_CONTEXT:
                return new ServiceSearchContext();
            case DmiObjectTypes.RESOURCE_DATA:
                return new DmiResourceData();
            case DmiObjectTypes.ROUTE_INFO:
                return new RouteInfo();
            case DmiObjectTypes.DATA_PACKET:
                return new DataPacket();
            case DmiObjectTypes.TARGET_PORT:
                return new TargetPort();
            case DmiObjectTypes.CARRY_FORWARD_CONTEXT:
                return new DmiCarryForwardContext();
            case DmiObjectTypes.SOURCE_CONTEXT:
                return new DmiSourceContext();
            case DmiObjectTypes.ALTERNATE_DESTINATION:
                return new AlternateDestination();
            case DmiObjectTypes.END_STATE:
                return new EndState();
            case DmiObjectTypes.MONITORING_INFO:
                return new Monitor();
            case DmiObjectTypes.LOGIN_RESPONSE:
                return new LoginResponse();

            // new service dmi
            case DmiObjectTypes.NEW_CPS:
                return new com.fiorano.openesb.application.service.CPS();
            case DmiObjectTypes.NEW_DEPLOYMENT:
                return new com.fiorano.openesb.application.service.Deployment();
            case DmiObjectTypes.NEW_EXECUTION:
                return new com.fiorano.openesb.application.service.Execution();
            case DmiObjectTypes.NEW_LOGMODULE:
                return new com.fiorano.openesb.application.service.LogModule();
            case DmiObjectTypes.NEW_PORT:
                return new com.fiorano.openesb.application.service.Port();
            case DmiObjectTypes.NEW_RESOURCE:
                return new com.fiorano.openesb.application.service.Resource();
            case DmiObjectTypes.NEW_RUNTIME_ARGUMENT:
                return new com.fiorano.openesb.application.service.RuntimeArgument();
            case DmiObjectTypes.NEW_SCHEMA:
                return new com.fiorano.openesb.application.service.Schema();
            case DmiObjectTypes.NEW_SERVICE:
                return new com.fiorano.openesb.application.service.Service();
            case DmiObjectTypes.NEW_SERVICE_REF:
                return new com.fiorano.openesb.application.service.ServiceRef();
            case DmiObjectTypes.NEW_SERVICE_REFERENCE:
                return new com.fiorano.openesb.application.service.ServiceReference();

            // new application dmi
            case DmiObjectTypes.NEW_APPLICATION:
                return new com.fiorano.openesb.application.application.Application();
            case DmiObjectTypes.NEW_APPLICATION_CONTEXT:
                return new com.fiorano.openesb.application.application.ApplicationContext();
            case DmiObjectTypes.NEW_APPLICATION_REFERENCE:
                return new com.fiorano.openesb.application.application.ApplicationReference();
            case DmiObjectTypes.NEW_INPUT_PORT_INSTANCE:
                return new com.fiorano.openesb.application.application.InputPortInstance();
            case DmiObjectTypes.NEW_LOG_MANAGER:
                return new com.fiorano.openesb.application.application.LogManager();
            case DmiObjectTypes.NEW_MESSAGE_TRANSFORMATION:
                return new com.fiorano.openesb.application.application.MessageTransformation();
            case DmiObjectTypes.NEW_OUTPUT_PORT_INSTANCE:
                return new com.fiorano.openesb.application.application.OutputPortInstance();
            case DmiObjectTypes.NEW_PORT_INSTANCE:
                return new com.fiorano.openesb.application.application.PortInstance();
            case DmiObjectTypes.NEW_REMOTE_SERVICE_INSTANCE:
                return new com.fiorano.openesb.application.application.RemoteServiceInstance();
            case DmiObjectTypes.NEW_ROUTE:
                return new com.fiorano.openesb.application.application.Route();
            case DmiObjectTypes.NEW_SCHEMA_INSTANCE:
                return new com.fiorano.openesb.application.application.SchemaInstance();
            case DmiObjectTypes.NEW_SERVICE_INSTANCE:
                return new com.fiorano.openesb.application.application.ServiceInstance();
            case DmiObjectTypes.NEW_TRANSFORMATION:
                return new com.fiorano.openesb.application.application.Transformation();
            case DmiObjectTypes.NEW_XPATH_SELECTOR:
                return new com.fiorano.openesb.application.application.XPathSelector();

            default:
                throw new FioranoException(DmiErrorCodes.ERR_INVALID_OBJECT_TYPE);
        }
    }


    /**
     * Returns ID of this object.
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public abstract int getObjectID();

    /**
     *  Sets all the fieldValues of this object using the specified XML string.
     * @param xmlElement Element object.
     * @exception FioranoException If an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element xmlElement)
            throws FioranoException
    {
    }


    /**
     *  This method sets all the fieldValues of this object using the specified
     *  <code>ResultSet</code> object.
     * @param resultSet ResultSet
     * @exception FioranoException If an error occurs while processing resultSet
     * @since Tifosi2.0
     */
    public void setFieldValues(ResultSet resultSet)
            throws FioranoException
    {
    }


    /**
     *  Resets the values of the data members of the object. This
     *  may possibly be used to help the Dmifactory reuse Dmi objects.
     * @since Tifosi2.0
     */
    public abstract void reset();


    /**
     *  This method reads this <code>DmiObject</code> object from the specified
     *  input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException Description of the Exception

     * @since Tifosi2.0
     */
//    public void fromStream(java.io.DataInput is)
//             throws java.io.IOException { }

    /**
     *  Reads this object from specified stream <code>is</code>
     *
     * @param is input stream
     * @param versionNo version
     * @exception IOException If an error occurs while reading from stream
     */
    public void fromStream(DataInput is, int versionNo)
            throws IOException
    {
    }

    /**
     *  This method writes this <code>DmiObject</code> object to the specified
     *  output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException Description of the Exception

     * @since Tifosi2.0
     */
//    public void toStream(java.io.DataOutput out)
//             throws java.io.IOException { }

    /**
     *  Writes this object to specified output stream <code>out</code>
     *
     * @param out output stream
     * @param versionNo version
     * @exception IOException If an error occurs while writing to stream
     */
    public void toStream(DataOutput out, int versionNo)
            throws IOException
    {
    }

    /**
     *  Tests whether this <code>DmiObject</code> object has the
     *  required(mandatory) fields set. This method must be called before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public abstract void validate()
            throws FioranoException;


    /**
     *  This utility method is used to compare this <code>DmiObject</code>
     *  object with the specified <code>DmiObject</code> object.
     *
     * @param obj The object with which comparison is to be
     *      made
     * @return true if the objects are equal.
     * @exception FioranoException FioranoException
     * @since Tifosi2.0
     */
    public boolean equalValues(DmiObject obj)
            throws FioranoException
    {
        return this.hashCode() == obj.hashCode();
    }


    /**
     *  This method returns the String representation of this <code>DmiObject</code>
     *  object.
     * @return String representation of this object
     */
    public String toString()
    {
        try
        {
            return toXMLString(11);
        }
        catch (FioranoException fe)
        {
                fe.printStackTrace();
        }
        return null;
    }

    /**
     *  This method returns the XML String representation for this object of
     *  <code>DmiObject</code>. It has not been implemented in this version.
     *
     * @param versionNo version
     * @return XML String for this object
     * @exception FioranoException If the calls fails
     * @since Tifosi2.0
     */

    public String toXMLString(int versionNo)
            throws FioranoException
    {
        try{
            StringWriter writer = new StringWriter();
            toXMLString(writer);
            writer.close();
            return writer.toString();
        } catch(IOException e){
            throw new FioranoException( e);
        }
    }


    /**
     *  This method writes the string specified by the <code>str</code> argument
     *  to the output stream specified by the <code>os</code> argument.
     *
     * @param os output stream
     * @param str string to be written
     * @exception IOException IOException
     */
    protected void writeUTF(DataOutput os, String str)
            throws IOException
    {
        if (str == null)
        {
            os.writeInt(-1);
        }
        else
        {
            byte[] bytes = str.getBytes("UTF-8");

            os.writeInt(bytes.length);
            os.write(bytes);
        }
    }


    /**
     *  This method reads the specified input stream and returns it as a <code>String</code>
     *  object.
     *
     * @param is input stream
     * @return String
     * @exception IOException IOException
     */
    protected String readUTF(DataInput is)
            throws IOException
    {
        int length = is.readInt();

        if (length == -1)
            return null;
        byte[] buff = new byte[length];

        is.readFully(buff);
        return new String(buff, "UTF-8");
    }



    /**
     * This will populate the DMI object from the InputStream using a new instance of STAX parser.
     *
     * @param is InputStream to the XML file to be loaded.
     * @param validate boolean to specify whether or not to validate the DMI
     * @throws FioranoException FioranoException
     */
    public void setFieldValues(InputStream is, boolean validate) throws FioranoException{
        try{
            FioranoStaxParser cursor = new FioranoStaxParser(is);
            setFieldValues(cursor, validate);
            try{
                cursor.disposeParser();
            }
            catch(XMLStreamException e){
                //nothing to do here.
            }
        }
        catch(XMLStreamException xse){
            throw new FioranoException( xse);
        }
    }

    /**
     * Populates the DMI object from the InputStream using a new instance of STAX parser. And also validates the DMI
     *
     * @param is InputStream to the XML file to be loaded.
     * @throws FioranoException FioranoException
     */
    public void setFieldValues(InputStream is) throws FioranoException{
        setFieldValues(is, false);
    }

    /**
     * This will populate the DMI object from the InputStream using a new instance of STAX parser.
     *
     * @param reader reader to the XML file to be loaded.
     * @param validate boolean to specify whether or not to validate the DMI
     * @throws FioranoException FioranoException
     */
    public void setFieldValues(Reader reader, boolean validate) throws FioranoException{
        try{
            FioranoStaxParser cursor = new FioranoStaxParser(reader);
            setFieldValues(cursor, validate);
            try{
                cursor.disposeParser();
            }
            catch(XMLStreamException e){
                //nothing to do here.
            }
        }
        catch(XMLStreamException xse){
            throw new FioranoException(xse);
        }
    }

    /**
     * Populates the DMI object from the InputStream using a new instance of STAX parser and validates this DMI
     *
     * @param reader reader to the XML file to be loaded.
     * @throws FioranoException FioranoException
     */

    public void setFieldValues(Reader reader) throws FioranoException{
        setFieldValues(reader, false);
    }

    /**
     * This will populate the DMI from the given parser instanace.
     *
     * @param cursor STAX cursor
     * @param validate boolean to specify whether or not to validate the DMI
     * @throws FioranoException FioranoException
     */
    public void setFieldValues(FioranoStaxParser cursor, boolean validate) throws FioranoException{
        try
        {
            populate(cursor);
            if(validate)
                validate();
        }catch(XMLStreamException ex)
        {
            throw new FioranoException(ex);
        }
        finally
        {
            cursor.resetCursor();

        }
    }

    /**
     * Populates the DMI from the given parser instanace. And also validates the DMI
     *
     * @param cursor STAX cursor
     * @throws FioranoException FioranoException
     */

    public void setFieldValues(FioranoStaxParser cursor) throws FioranoException{
        setFieldValues(cursor, false);
    }

    /**
     * Populates the DMI, starting from the cursor passed to it.
     * @param cursor STAX cursor
     * @throws FioranoException FioranoException
     * @throws XMLStreamException XMLStreamException
     */
    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        // NOTHING TO DO AT THIS LEVEL
    }

    /**
     * Writes this object to specified filename
     * @param fileName file name
     * @throws FioranoException FioranoException
     */
    public void toXMLString(String fileName) throws FioranoException
    {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        FileOutputStream fos = null;
        XMLStreamWriter writer = null;
        try{
            try {
                fos = new FileOutputStream(fileName);
                writer =  outputFactory.createXMLStreamWriter(fos);
                toJXMLString(writer);
                writer.flush();
            } finally {
                try {
                    if(writer != null)
                        writer.close();
                } catch (XMLStreamException e) {
                    // Ignore
                }
                try {
                    if(fos != null)
                        fos.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }catch(XMLStreamException e){
            throw new FioranoException(e);
        }
        catch(IOException e){
            throw new FioranoException(e);
        }

    }

    public void toXMLString(String fileName, boolean writeCDataSections) throws FioranoException {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        FileOutputStream fos = null;
        XMLStreamWriter writer = null;
        try {
            try{
                fos = new FileOutputStream(fileName);
                writer = outputFactory.createXMLStreamWriter(fos);
                toJXMLString(writer, writeCDataSections);
                writer.flush();
            } finally {
                try {
                    if(writer != null)
                        writer.close();
                } catch (XMLStreamException e) {
                    // Ignore
                }
                try {
                    if(fos != null)
                        fos.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        } catch (XMLStreamException e) {
            throw new FioranoException(e);
        }
        catch (IOException e) {
            throw new FioranoException( e);
        }
    }

    public void toXMLString(Writer _writer) throws FioranoException
    {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        try{
            XMLStreamWriter writer =  outputFactory.createXMLStreamWriter(_writer);
            toJXMLString(writer);
            writer.flush();
        }catch(XMLStreamException e){
            throw new FioranoException(e);
        }

    }

    /**
     * Writes this object to the specified output stream
     * @param _out output stream
     * @throws FioranoException FioranoException
     */
    public void toXMLString(OutputStream _out) throws FioranoException
    {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        try{
            XMLStreamWriter writer =  outputFactory.createXMLStreamWriter(_out);
            toJXMLString(writer);
            writer.flush();
        }catch(XMLStreamException e){
            throw new FioranoException(e);
        }

    }

    public void toXMLString(OutputStream _out, boolean writeCDataSections ) throws FioranoException {
        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        try {
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(_out);
            toJXMLString(writer, writeCDataSections);
            writer.flush();
        } catch (XMLStreamException e) {
            throw new FioranoException( e);
        }
    }

    protected void toJXMLString(XMLStreamWriter writer, boolean writeCDataSections) throws XMLStreamException, FioranoException {
        //default implementation
        toJXMLString(writer);
    }
    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
    }

    /*-------------------------------------------------[ To XML Utilities ]---------------------------------------------------*/

    // writes attribute if and only if content is non-null and non-empty
    protected static void writeAttribute(XMLStreamWriter writer, String attribute, String content) throws XMLStreamException{
        if(content!=null && !content.trim().equals(""))
            writer.writeAttribute(attribute, content);
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, boolean value) throws XMLStreamException{
        writer.writeAttribute(attribute, String.valueOf(value));
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, int value) throws XMLStreamException{
        writer.writeAttribute(attribute, String.valueOf(value));
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, long value) throws XMLStreamException{
        writer.writeAttribute(attribute, String.valueOf(value));
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, float value) throws XMLStreamException{
        writer.writeAttribute(attribute, String.valueOf(value));
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, String[] value) throws XMLStreamException{
        if(value!=null && value.length>0)
            writer.writeAttribute(attribute, StringUtils.join(value, ","));
    }

    protected static void writeAttribute(XMLStreamWriter writer, String attribute, List<String> value) throws XMLStreamException {
        if (value != null && value.size() > 0)
            writer.writeAttribute(attribute, StringUtils.join(value.toArray(new String[value.size()]), ","));
    }

    public static final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    protected static void writeAttribute(XMLStreamWriter writer, String attribute, Date date) throws XMLStreamException{
        if(date!=null){
            String value;
            synchronized(XML_DATE_FORMAT){
                value = XML_DATE_FORMAT.format(date);
            }
            writer.writeAttribute(attribute, value);
        }
    }

    protected static void writeElement(XMLStreamWriter writer, String element, String content) throws XMLStreamException{
        if(!StringUtils.isEmpty(content)){
            writer.writeStartElement(element);
            writer.writeCharacters(content);
            writer.writeEndElement();
        }
    }

    protected static void writeCDATAElement(XMLStreamWriter writer, String element, String content) throws XMLStreamException{
        if(!StringUtils.isEmpty(content)){
            writer.writeStartElement(element);
            writer.writeCData(content);
            writer.writeEndElement();
        }
    }

    protected static void writeCollection(XMLStreamWriter writer, Collection/*<DmiObject>*/ c, String element, boolean writeCDataSections) throws XMLStreamException, FioranoException {
        if(c.size()>0){
            if(element!=null)
                writer.writeStartElement(element);
            for (Object obj : c) {
                DmiObject dmi = (DmiObject)obj;
                dmi.toJXMLString(writer, writeCDataSections);
            }
            if(element!=null)
                writer.writeEndElement();
        }
    }

    protected static void writeCollection(XMLStreamWriter writer, Collection/*<DmiObject>*/ c, String element) throws XMLStreamException, FioranoException {
        writeCollection(writer, c, element, true);
    }

    /*-------------------------------------------------[ From XML Utilities ]---------------------------------------------------*/

    protected static String getStringAttribute(FioranoStaxParser cursor, String attr, String defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? value : defaultValue;
    }

    protected static boolean getBooleanAttribute(FioranoStaxParser cursor, String attr, boolean defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? Boolean.valueOf(value) : defaultValue;
    }

    protected static int getIntegerAttribute(FioranoStaxParser cursor, String attr, int defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? Integer.parseInt(value) : defaultValue;
    }

    protected static float getFloatAttribute(FioranoStaxParser cursor, String attr, float defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? Float.parseFloat(value) : defaultValue;
    }

    protected static long getLongAttribute(FioranoStaxParser cursor, String attr, long defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? Long.parseLong(value) : defaultValue;
    }

    protected static Date getDateAttribute(FioranoStaxParser cursor, String attr, Date defaultValue) throws ParseException{
        String value = cursor.getAttributeValue(null, attr);
        if(value!=null){
            synchronized(XML_DATE_FORMAT){
                return XML_DATE_FORMAT.parse(value);
            }
        }else
            return defaultValue;
    }

    protected static String[] getStringArrayAttribute(FioranoStaxParser cursor, String attr, String[] defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? value.split(",") : defaultValue;
    }

    protected static List/*<String>*/ getListAttribute(FioranoStaxParser cursor, String attr, List defaultValue){
        String value = cursor.getAttributeValue(null, attr);
        return value!=null ? Arrays.asList((value.split(","))) : defaultValue;
    }

    /*-------------------------------------------------[ To Stream Utilities ]---------------------------------------------------*/
    /**
     * Utility method to write a list to output stream
     * @param list list
     * @param out output stream
     * @param versionNo version
     * @throws IOException
     */
    public static void toStream(List list, DataOutput out, int versionNo) throws IOException{
        try {
            if(list!=null){
                out.writeInt(list.size());
                for (Object aList : list) {
                    DmiObject dmi = (DmiObject) aList;
                    dmi.toStream(out, versionNo);
                }
            }else
                out.writeInt(0);
        } finally {
            // Not clearing this will lead to invalid objects in memory
            MapThreadLocale.getInstance().getMap().clear();
        }
    }

    /**
     * Utility method to write a DmiObject to output stream
     * @param dmi dmi object
     * @param out output stream
     * @param versionNo version
     * @throws IOException IOException
     */
    public static void toStream(DmiObject dmi, DataOutput out, int versionNo) throws IOException{
        if(dmi!=null){
            out.writeInt(1);
            dmi.toStream(out, versionNo);
        }else
            out.writeInt(0);
    }

    /*-------------------------------------------------[ From Stream Utilities ]---------------------------------------------------*/
    /**
     * Utility method to read a list from input stream
     * @param list list
     * @param dmiType type of dmi object
     * @param in input stream
     * @param versionNo version
     * @throws IOException IOException
     */
    public static void fromStream(List list, int dmiType, DataInput in, int versionNo) throws IOException{
        try{
            int size = in.readInt();
            for(int i = 0; i<size; i++){
                DmiObject dmi = DmiObject.getDatamodelObject(dmiType);
                dmi.fromStream(in, versionNo);
                list.add(dmi);
            }
        } catch(FioranoException ex){
            throw (IOException)new IOException().initCause(ex);
        }
    }

    /**
     * Utility method to read a DmiObject from input stream
     * @param dmiType type of dmi object
     * @param in input stream
     * @param versionNo version
     * @throws IOException IOException
     */
    public static DmiObject fromStream(int dmiType, DataInput in, int versionNo) throws IOException{
        try{
            if(in.readInt()!=0){
                DmiObject dmi = DmiObject.getDatamodelObject(dmiType);
                dmi.fromStream(in, versionNo);
                return dmi;
            }else
                return null;
        } catch(FioranoException ex){
            throw (IOException)new IOException().initCause(ex);
        }
    }

    /*-------------------------------------------------[ Validation Utilities ]---------------------------------------------------*/

    /**
     * Assumption that the instanceClass is a DmiClass and implements NamedObject
     * @param list The List containing the Dmis
     * @throws FioranoException FioranoException
     * @bundle DMI_ALREADY_EXISTS=Duplicate Entry "{0}" Found in the list.
     * @throws com.fiorano.openesb.utils.exception.FioranoException If the validation fails
     */
    protected void validateList(List/*<DmiObject>*/ list) throws FioranoException {
        Set<String> names = new HashSet<String>();
        for (Object object : list) {
            DmiObject dmiObject = (DmiObject) object;
            dmiObject.validate();
            if (dmiObject instanceof NamedObject) {
                if (!names.add(((NamedObject) dmiObject).getKey()))
                    throw new FioranoException("DMI_ALREADY_EXISTS");
            }
        }
    }

    /*-------------------------------------------------[ Utilities ]---------------------------------------------------*/

    public static NamedObject findNamedObject(List list, String name) {

        for (Object aList : list) {
            NamedObject dmi = (NamedObject) aList;
            if (name.equals(dmi.getKey()))
                return dmi;
        }
        return null;
    }

    /**
     * Characterswhich the users should not enter in the name field. Note that since a Regex matching
     * is done, all meta chars are pre-pended with a \\
     */
    public static final Pattern INVALID_INPUT_CHARS_REGEX;



    public static String correctName(String name) {
        Matcher matcher = INVALID_INPUT_CHARS_REGEX.matcher(name);
        if(matcher.find())
            return matcher.replaceAll("_");
        return name;

    }


    public  static String getContents(InputStream inputStream) throws IOException {

        StringBuilder builder = new StringBuilder(200);
        InputStreamReader reader = new InputStreamReader(inputStream);
        char[] temp = new char[8*1024];
        int readBytes;
        do{
            readBytes = reader.read(temp);
            if(readBytes > 0)
                builder.append(temp, 0, readBytes);
        }while(readBytes > 0);

        return builder.toString();
    }


}
