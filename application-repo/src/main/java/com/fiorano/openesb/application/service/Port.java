/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.MapThreadLocale;
import com.fiorano.openesb.application.NamedObject;
import com.fiorano.openesb.application.aps.PortInst;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.InPort;
import com.fiorano.openesb.application.sps.OutPort;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Port extends InflatableDMIObject  implements NamedObject{
    /**
     * element port in service descriptor xml
     */
    public static final String ELEM_PORT = "port";

    public int getObjectID(){
        return DmiObjectTypes.NEW_PORT;
    }

    /*-------------------------------------------------[ Name ]---------------------------------------------------*/
    /**
     * attribute name
     */
    public static final String ATTR_NAME = "name";

    private String name;

    /**
     * Returns port name
     * @return Name of port
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the port name
     * @param name port name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /*-------------------------------------------------[ Description ]---------------------------------------------------*/
    /**
     * element description in service descriptor xml
     */
    public static final String ELEM_DESCRIPTION = "description";

    private String description;

    /**
     * Returns Port Description
     * @return port description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Sets the Port Description
     * @param description description to be set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /*-------------------------------------------------[ isRequestReply ]---------------------------------------------------*/
    /**
     * attribute request-reply
     */
    public static final String ATTR_REQUEST_REPLY = "request-reply";

    private boolean requestReply = false;

    /**
     * Sets a boolean specifying whether request reply is enabled for this port
     * @return boolean specifying whether request reply is enabled for this port
     */
    public boolean isRequestReply(){
        return requestReply;
    }

    /**
     * Sets a boolean specifying whether request reply is enabled for this port
     * @param requestReply  boolean specifying whether request reply is enabled for this port
     */
    public void setRequestReply(boolean requestReply){
        this.requestReply = requestReply;
    }

    /*-------------------------------------------------[ Schema ]---------------------------------------------------*/

    private Schema schema = null;

    /**
     * Returns schema of this port
     * @return Schema of this port
     */
    public Schema getSchema(){
        return schema;
    }

    /**
     * Sets schema of this port
     * @param schema schema to be set
     */
    public void setSchema(Schema schema){
        this.schema = schema;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <port name="string" request-reply="boolean"?>
     *      <description>string</description>?
     *      ...schema?...
     *      ...subclass_1...
     * </port>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_PORT, writeSchema);
    }
    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, true);
    }

    protected void toJXMLString(XMLStreamWriter writer, String rootElement, boolean writeSchema) throws XMLStreamException, FioranoException{
        writer.writeStartElement(rootElement);
        {
            writer.writeAttribute(ATTR_NAME, name);
            MapThreadLocale.getInstance().getMap().put(ELEM_PORT, name);

            if(requestReply)
                writer.writeAttribute(ATTR_REQUEST_REPLY, String.valueOf(requestReply));
            if(!StringUtil.isEmpty(description)){
                writer.writeStartElement(ELEM_DESCRIPTION);
                writer.writeCharacters(description);
                writer.writeEndElement();
            }
            if(schema!=null)
                schema.toJXMLString(writer, writeSchema);
            toJXMLString_1(writer, writeSchema);
        }
        writer.writeEndElement();
    }

    // subclasses can add additional content using this
    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        populate(cursor, ELEM_PORT);
    }

    protected void populate(FioranoStaxParser cursor, String rootElement) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(rootElement)){
            name = cursor.getAttributeValue(null,ATTR_NAME);
            MapThreadLocale.getInstance().getMap().put(ELEM_PORT, name);
            requestReply = getBooleanAttribute(cursor,ATTR_REQUEST_REPLY,false);

            while(cursor.nextElement()){
                String elemName = cursor.getLocalName();
                if(ELEM_DESCRIPTION.equals(elemName))
                    description = cursor.getText();
                else if(Schema.ELEM_SCHEMA.equals(elemName)){
                    schema = new Schema();
                    schema.setFieldValues(cursor);
                }else
                    populate_1(cursor);
            }
        }
    }

    // subclasses can read additional content using this
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/
    /**
     * Converts to new DMI
     * @param that old  inport DMI
     */
    public void convert(InPort that){
        name = that.getPortName();
        requestReply = that.isSyncRequestType();
        description = that.getDescription();

        if(!StringUtil.isEmpty(that.getXSD())){
            schema = new Schema();
            schema.convert(that);
        }
    }

    /**
     * Coverts to new DMI
     * @param that old Outport DMI
     */
    public void convert(OutPort that){
        name = that.getPortName();
        requestReply = that.isSyncRequestType();
        description = that.getDescription();

        if(!StringUtil.isEmpty(that.getXSD())){
            schema = new Schema();
            schema.convert(that);
        }
    }

    /**
     * Converts to new DMI
     * @param that old PortInstance DMI
     */
    public void convert(PortInst that){
        name = that.getPortName();
        requestReply = that.isSyncRequestType();
        description = that.getDescription();

        if(!StringUtil.isEmpty(that.getXSD())){
            schema = new Schema();
            schema.convert(that);
        }

        convert_1(that);
    }

    /**
     * Converts to new DMI
     * @param that old Port instance DMI
     */
    public void convert_1(PortInst that){}

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        name = null;
        description = null;
        requestReply = false;
        schema = null;
    }

    /**
     * @bundle PORT_NAME_UNSPECIFIED==Name of Port is not specified
     */
    public void validate() throws FioranoException{
        if(StringUtil.isEmpty(name))
            throw new FioranoException( "PORT_NAME_UNSPECIFIED");
        if(schema!=null)
            schema.validate();
    }

    /**
     * Returns key
     * @return Name of the port
     */
    public String getKey(){
        return getName();
    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeUTF(name);
        bytesMessage.writeUTF(description);
        bytesMessage.writeBoolean(requestReply);
        if(schema==null){
            bytesMessage.writeBoolean(false);
            return;
        }else{
            bytesMessage.writeBoolean(true);
            schema.toMessage(bytesMessage);
        }
    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        name=bytesMessage.readUTF();
        description=bytesMessage.readUTF();
        requestReply=bytesMessage.readBoolean();
        if(bytesMessage.readBoolean()){
            schema=new Schema();
            schema.fromMessage(bytesMessage);
        }
    }
}
