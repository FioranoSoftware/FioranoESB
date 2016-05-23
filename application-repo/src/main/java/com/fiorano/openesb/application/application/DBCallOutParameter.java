/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBCallOutParameter extends DmiObject {

    /**
     * parameter name
     */
    private String name;

    //TODO: Enum

    /**
     * Type of parameter IN|OUT|INOUT
     */
    private String type;

    //TODO: Enum
    /*
     * Data type of parameter VARCHAR|INT|BOOLEAN|DATETIME
     */
    private String dataType;

    //TODO: Enum
    /**
     * data source of parameter Body|BodyXPath|Header|Intime|ProcessingTime|Constant|AppCtxt|AppCtxtXPath
     */
    private String valuesource;

    /**
     * data source value
     */
    private String value;

    private Map<String,String> prefixMap = new LinkedHashMap<String, String>();

    /*-----XML Element and attribute naming------*/

    public static final String ELEM_DB_CALLOUT_PARAM = "dbcallout-parameter";

    public static final String ATTR_PARAM_NAME = "name";

    public static final String ATTR_PARAM_TYPE = "type";

    public static final String ATTR_PARAM_DATATYPE = "datatype";

    public static final String ATTR_PARAM_VALUE_SOURCE = "valuesource";

    public static final String ATTR_PARAM_VALUE = "value";

    public static final String ELEM_NAMESPACE = "paramnamespace";

    public static final String ATTR_NAMESPACE_PREFIX = "prefix";

    public static final String ATTR_NAMESPACE_URI = "uri";

    public static final String ELEM_NAMESPACES = "paramnamespaces";

    public DBCallOutParameter(String  name, String type, String dataType, String valuesource,String value){
        this.name = name;
        this.type = type;
        this.dataType = dataType;
        this.valuesource = valuesource;
        this.value = value;
    }

    public DBCallOutParameter() {

    }

    /**
     * Populates the DMI from the given parser instance. And also validates the DMI
     *
     * @param cursor STAX cursor
     * @throws FioranoException FioranoException
     */
    public void setFieldValues(FioranoStaxParser cursor)throws FioranoException{
        setFieldValues(cursor, false);
    }

    @Override
    public void setFieldValues(Reader reader, boolean validate) throws FioranoException {
        super.setFieldValues(reader, validate);
    }

     /*-------------------------------------------------[ To XML ]---------------------------------------------------*/
    /*
    *  <dbcallout-parameter name="Param1" type="IN|OUT|INOUT" datatype="varchar"
    *  valueSource="Body|BodyXPath|Header|Intime|OutTime|ProcessingTime|Constant|AppCtxt|AppCtxtXPath" value="">
    *       <namespaces>
    *           <namespace prefix="prefix" uri="uri"/>
    *       <namespaces>
    *  </dbcallout-parameter>
    * */

    @Override
    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        writer.writeStartElement(ELEM_DB_CALLOUT_PARAM);{
            writer.writeAttribute(ATTR_PARAM_NAME, name);
            writer.writeAttribute(ATTR_PARAM_TYPE, type);
            writer.writeAttribute(ATTR_PARAM_DATATYPE, dataType);
            writer.writeAttribute(ATTR_PARAM_VALUE_SOURCE, valuesource);
            writer.writeAttribute(ATTR_PARAM_VALUE, value);
            if (prefixMap.size() != 0) {
                writer.writeStartElement(ELEM_NAMESPACES);
                {
                    for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
                        writer.writeStartElement(ELEM_NAMESPACE);
                        writer.writeAttribute(ATTR_NAMESPACE_PREFIX, entry.getKey());
                        writer.writeAttribute(ATTR_NAMESPACE_URI, entry.getValue());
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    @Override
    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException {
        if (cursor.markCursor(ELEM_DB_CALLOUT_PARAM)){
            name = cursor.getAttributeValue(null, ATTR_PARAM_NAME);
            type = cursor.getAttributeValue(null, ATTR_PARAM_TYPE);
            dataType = cursor.getAttributeValue(null, ATTR_PARAM_DATATYPE);
            valuesource = cursor.getAttributeValue(null, ATTR_PARAM_VALUE_SOURCE);
            value = cursor.getAttributeValue(null, ATTR_PARAM_VALUE);
            while (cursor.nextElement()) {
                if (cursor.getLocalName().equals(ELEM_NAMESPACE)) {
                    prefixMap.put(cursor.getAttributeValue(null, ATTR_NAMESPACE_PREFIX), cursor.getAttributeValue(null, ATTR_NAMESPACE_URI));
                }
            }
        }
    }

    @Override
    public void fromStream(DataInput is, int versionNo) throws IOException {
        super.fromStream(is, versionNo);
        name = readUTF(is);
        type = readUTF(is);
        dataType = readUTF(is);
        valuesource = readUTF(is);
        value = readUTF(is);
        int count = is.readInt();
        for (int i = 0; i < count; i++) {
            String key = readUTF(is);
            String value = readUTF(is);
            prefixMap.put(key, value);
        }
    }

    @Override
    public void toStream(DataOutput out, int versionNo) throws IOException {
        super.toStream(out, versionNo);
        writeUTF(out, name);
        writeUTF(out, type);
        writeUTF(out, dataType);
        writeUTF(out, valuesource);
        writeUTF(out, value);
        out.writeInt(prefixMap.size());
        for(Map.Entry<String,String> entry : prefixMap.entrySet()) {
            writeUTF(out,entry.getKey());
            writeUTF(out,entry.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getDataType(){
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValuesource() {
        return valuesource;
    }

    public void setValuesource(String valuesource){
        this.valuesource = valuesource;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getPrefixMap() {
        return prefixMap;
    }


    @Override
    public int getObjectID() {
        return DmiObjectTypes.DB_CALL_OUT_PARAM;
    }

    @Override
    public void reset() {
        name = null;
        type = null;
        dataType = null;
        valuesource = null;
        value = null;
        if (prefixMap != null) {
            prefixMap.clear();
            prefixMap = null;
        }
    }

    @Override
    public void validate() throws FioranoException {
    }


}
