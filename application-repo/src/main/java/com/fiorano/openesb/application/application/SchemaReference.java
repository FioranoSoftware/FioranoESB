/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
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

 public class SchemaReference extends DmiObject{

    private String namespace;
    private String location;
    private boolean requiredAtRuntime = false;

    /**
     * Element schema-reference
     */
    public static final String ELEM_SCHEMA_REFERENCE = "schema-reference";
    /**
     * Attribute namespace
     */
    public static final String ATTR_SERVICE_REFERENCE_NAMESPACE = "namespace";
    /**
     * Attribute location
     */
    public static final String ATTR_SERVICE_REFERNCE_LOCATION = "location";
    /**
     * Attribute required-at-runtime
     */
    public static final String ATTR_SERVICE_REFERNCE_REQUIREDATRUNTIME = "required-at-runtime";


    /**
     * Default constructor
     */
    public SchemaReference() { }

    /**
     * Constructs object of this class with passed parameters
     * @param namespace Namespace used
     * @param location  Location of schema
     * @param requiredAtRuntime  Requirement at runtime
     */
    public SchemaReference(String namespace, String location, boolean requiredAtRuntime) {
        this.namespace = namespace;
        this.location = location;
        this.requiredAtRuntime = requiredAtRuntime;
    }

    /**
     * Populates the DMI from the given parser instance. And also validates the DMI
     *
     * @param cursor STAX cursor
     * @throws FioranoException FioranoException
     */

    public void setFieldValues(FioranoStaxParser cursor) throws FioranoException{
        setFieldValues(cursor, false);
    }

    @Override
    public int getObjectID() {
        return DmiObjectTypes.SCHEMA_REFERENCE_PROPERTY;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Sets the namespace to be used
     * @param namespace Namespace to be set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Sets the location of the Schema
     * @param location Schema location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Specifies whether the schema is required at runtime or not
     * @param requiredAtRuntime true if schema is required at runtime, false otherwise
     */
    public void setRequiredAtRuntime(boolean requiredAtRuntime) {
        this.requiredAtRuntime = requiredAtRuntime;
    }

    @Override
    public void reset() {
        namespace = null;
        location = null;
        requiredAtRuntime = false;
    }

    @Override
    public void validate() throws FioranoException {
    }


    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/
    /*
    * <SchemaReference namespace="http://www.salesforce.com/orders" location="locationHintInRepo" requiredAtRuntime="false" />
    * */
    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        writer.writeStartElement(ELEM_SCHEMA_REFERENCE);{
            writer.writeAttribute(ATTR_SERVICE_REFERENCE_NAMESPACE, namespace);
            writer.writeAttribute(ATTR_SERVICE_REFERNCE_LOCATION, location);
            writer.writeAttribute(ATTR_SERVICE_REFERNCE_REQUIREDATRUNTIME, String.valueOf(requiredAtRuntime));
        }
        writer.writeEndElement();
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_SCHEMA_REFERENCE)){
            namespace = cursor.getAttributeValue(null, ATTR_SERVICE_REFERENCE_NAMESPACE);
            location = cursor.getAttributeValue(null, ATTR_SERVICE_REFERNCE_LOCATION);
            requiredAtRuntime = getBooleanAttribute(cursor, ATTR_SERVICE_REFERNCE_REQUIREDATRUNTIME, false);

        }
    }

    /**
     * Reads from input stream
     * @param is input stream
     * @param versionNo version
     * @throws IOException Exception in reading the content
     */
    public void fromStream(DataInput is, int versionNo) throws IOException {
        super.fromStream(is, versionNo);
        namespace = readUTF(is);
        location = readUTF(is);
        requiredAtRuntime = Boolean.parseBoolean(readUTF(is));

    }

    /**
     * Writes to output stream
     * @param out output stream
     * @param versionNo version
     * @throws IOException Exception in writing the content
     */
    public void toStream(DataOutput out, int versionNo) throws IOException{
        super.toStream(out, versionNo);
        writeUTF(out, namespace);
        writeUTF(out, location);
        writeUTF(out, new Boolean(requiredAtRuntime).toString());

    }

    /**
     * Returns the namespace being used
     * @return String - Namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Returns the location of the schema
     * @return String - Schema location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns whether the schema is required at runtime or not
     * @return boolean - true if schema is required at runtime, false otherwise
     */
    public boolean isRequiredAtRuntime() {
        return requiredAtRuntime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchemaReference that = (SchemaReference) o;

        if (requiredAtRuntime != that.requiredAtRuntime) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (namespace != null ? !namespace.equals(that.namespace) : that.namespace != null) return false;

        return true;
    }

    /**
     * Returns hash code of the schema reference object
     * @return int - Hash code
     */
    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (requiredAtRuntime ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Namespace='" + namespace + '\'' + ", Location='" + location + '\'';
    }
}
