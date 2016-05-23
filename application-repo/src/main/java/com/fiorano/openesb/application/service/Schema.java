/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.InflatableDMIObject;
import com.fiorano.openesb.application.MapThreadLocale;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.application.aps.ApplicationContext;
import com.fiorano.openesb.application.aps.PortInst;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.InPort;
import com.fiorano.openesb.application.sps.OutPort;
import com.fiorano.openesb.utils.ClarkName;
import com.fiorano.openesb.utils.CollectionUtil;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.StringUtil;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Schema extends InflatableDMIObject{
    /**
     * Element schema in service descriptor xml
     */
    public static final String ELEM_SCHEMA = "schema";

    public int getObjectID(){
        return DmiObjectTypes.NEW_SCHEMA;
    }

    /*-------------------------------------------------[ Type ]---------------------------------------------------*/
    /**
     * Attribute type
     */
    public static final String ATTR_TYPE = "type";
    /**
     * DTD type
     */
    public static final int TYPE_DTD = 0;
    /**
     * XSD type
     */
    public static final int TYPE_XSD = 1;

    private int type = TYPE_XSD;

    /**
     * Returns schema type
     * @return int - Schema type
     */
    public int getType(){
        return type;
    }

    /**
     * Sets schema type
     * @param type Schema type i.e 0 for dtd and 1 for xsd
     */
    public void setType(int type){
        this.type = type;
    }

    /*-------------------------------------------------[ RootElement ]---------------------------------------------------*/
    /**
     * Attribute root
     */
    public static final String ATTR_ROOT_ELEMENT = "root";

    private String rootElement; // in clarkname format

    /**
     * Returns root Element of this schema
     * @return String - Root element
     */
    public String getRootElement(){
        return rootElement;
    }

    /**
     * Sets root element of this schema
     * @param rootElement Root element to be set
     */
    public void setRootElement(String rootElement){
        this.rootElement = rootElement;
    }

    /*-------------------------------------------------[ Content ]---------------------------------------------------*/
    /**
     * Element content in service descriptor xml
     */
    public static final String ELEM_CONTENT = "content";

    private String content;

    /**
     * Returns schema content
     * @return String - Schema content
     */
    public String getContent(){
        return content;
    }

    /**
     * Sets schema content
     * @param content Content to be set
     */
    public void setContent(String content){
        this.content = content;
    }

    /*-------------------------------------------------[ File ]---------------------------------------------------*/
    /**
     * Element file in service descriptor xml
     */
    public static final String ELEM_FILE = "file";

    private String file;

    /**
     * Returns schema filename
     * @return String - Name of the file
     */
    public String getFile(){
        return file;
    }

    /**
     * Sets schema filename
     * @param file Filename to be set
     */
    public void setFile(String file){
        this.file = file;
    }

    /*-------------------------------------------------[ config-name ]---------------------------------------------------*/
    /**
     * Element messaging-config-name in event process xml
     */
    public static final String ELEM_CONFIG_NAME = "config-name";

    private String configName;

    /**
     * Returns configuration name of this schema element
     * @return String - Name of the configuration
     */
    public String getConfigName(){
        return configName;
    }

    /**
     * Sets configuration name of this schema element
     * @param configName Configuration name to be set
     */
    public void setConfigName(String configName){
        this.configName = configName;
    }

    /*-------------------------------------------------[ Schema Refs ]---------------------------------------------------*/
    /**
     * Element schemarefs in service descriptor xml
     */
    public static final String ELEM_SCHEMA_REFS = "schemarefs";
    /**
     * Element schemaref in service descriptor xml
     */
    public static final String ELEM_SCHEMA_REF = "schemaref";
    /**
     * Attribute uri
     */
    public static final String ATTR_URI = "uri";

    private Map schemaRefs = new HashMap();
    private Map schemaRefFiles = new HashMap();
    private Map<String, String> schemaRefConfigs = new HashMap<String, String>();

    /**
     * Returns schema references of this schema
     * @return Map - Map of schema references
     */
    public Map getSchemaRefs(){
        return schemaRefs;
    }

    /**
     * Returns the files of schema references
     * @return Map - Map of schema reference files
     */
    public Map getSchemaRefFiles() {
        return schemaRefFiles;
    }

    /**
     * Returns the schema reference configurations
     * @return Map - Map of schema reference configurations
     */
    public Map getSchemaRefConfigs() {
        return schemaRefConfigs;
    }

    /**
     * Sets schemarefs for this schema
     * @param schemaRefs Schema references
     */
    public void setSchemaRefs(Map schemaRefs){
        this.schemaRefs = schemaRefs;
    }

    /**
     * Adds specified schemaref to schemarefs of this schema
     * @param uri Schemaref uri
     * @param content Content of the schema
     */
    public void addSchemaRef(String uri, String content){
        if(!StringUtil.isEmpty(uri)  && !StringUtil.isEmpty(content))
            schemaRefs.put(uri, content);
    }

    /**
     * Removes schemaref with specified uri from schemarefs of this schema
     * @param uri Schemaref uri
     */
    public void removeSchemaRef(String uri){
        schemaRefs.remove(uri);
    }

    /**
     * Adds specified uri and filename in schemaRefFiles(map of uri Vs. filename)
     * @param uri uri of the schema
     * @param fileName Name of the file
     */
    public void addSchemaRefFile(String uri, String fileName){
        if(!StringUtil.isEmpty(uri)  && !StringUtil.isEmpty(fileName))
            schemaRefFiles.put(uri, fileName);
    }

    /**
     * Removes specified uri and its filename from schemaRefFiles(map of uri Vs. filename)
     * @param uri uri of the schema
     */
    public void removeSchemaRefFile(String uri){
        schemaRefFiles.remove(uri);
    }

    /**
     * Adds specified uri and config name in schemaRefConfigs(map of uri Vs. filename)
     * @param uri uri of the schema
     * @param fileName Name of the file
     */
    public void addSchemaRefConfig(String uri, String fileName){
        if(!StringUtil.isEmpty(uri)  && !StringUtil.isEmpty(fileName))
            schemaRefConfigs.put(uri, fileName);
    }

    /**
     * Removes specified uri and its config name from schemaRefConfigs(map of uri Vs. filename)
     * @param uri uri of the schema
     */
    public void removeSchemaRefConfig(String uri){
        schemaRefConfigs.remove(uri);
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <schema type="int"? root="string">
     *      <content>string</content>?
     *      <file>string</file>?
     *      <schemarefs>?
     *          <schemaref uri="string">string</schemaref>+
     *      </schemarefs>
     * </schema>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_SCHEMA, writeSchema);
    }

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException{
        toJXMLString(writer, true);
    }

    private String optimizeSchemaContent(String content, String subkey){
        if(!StringUtil.isEmpty(content)){
            String key = (String)CollectionUtil.getKeyWithValue(MapThreadLocale.getInstance().getMap(), content.trim());
            if(key!=null)
                return "${"+key+"}";
            else{
                String instName = (String)MapThreadLocale.getInstance().getMap().get(ServiceInstance.ELEM_SERVICE_INSTANCE);
                String portName = (String)MapThreadLocale.getInstance().getMap().get(Port.ELEM_PORT);
                key = portName!=null ? instName+'.'+portName : instName;
                if(subkey!=null)
                    key = key + '.' + subkey;
                MapThreadLocale.getInstance().getMap().put(key, content.trim());
                return content;
            }
        }
        return content;
    }

    protected void toJXMLString(XMLStreamWriter writer, String rootElement, boolean writeSchema) throws XMLStreamException, FioranoException{
        writer.writeStartElement(rootElement);
        {
            if(type!=TYPE_XSD)
                writer.writeAttribute(ATTR_TYPE, Integer.toString(type));
            if(this.rootElement!=null)
                writer.writeAttribute(ATTR_ROOT_ELEMENT, this.rootElement);

            if(writeSchema)
                writeCDATAElement(writer, ELEM_CONTENT, optimizeSchemaContent(content, null));
            else{
                if(configName != null)
                    DmiObject.writeElement(writer, ELEM_CONFIG_NAME, configName);
                else
                    DmiObject.writeElement(writer, ELEM_FILE, file);
            }

            if(schemaRefs.size()>0){
                writer.writeStartElement(ELEM_SCHEMA_REFS);
                {
                    for(Iterator iter = schemaRefs.entrySet().iterator(); iter.hasNext();){
                        Map.Entry entry = (Map.Entry)iter.next();
                        writer.writeStartElement(ELEM_SCHEMA_REF);
                        writer.writeAttribute(ATTR_URI, (String)entry.getKey());
                        if(writeSchema)
                            writer.writeCData(optimizeSchemaContent((String)entry.getValue(), (String)entry.getKey()));
                        else{
                            if(schemaRefConfigs.get(entry.getKey().toString()) != null)
                                DmiObject.writeElement(writer, ELEM_CONFIG_NAME, schemaRefConfigs.get(entry.getKey().toString()));
                            else if(schemaRefFiles.get(entry.getKey()) != null)
                                DmiObject.writeElement(writer, ELEM_FILE, (String) schemaRefFiles.get(entry.getKey()));
                        }
                        writer.writeEndElement();
                    }
                }
                writer.writeEndElement();
            }
            toJXMLString_1(writer);
        }
        writer.writeEndElement();
    }

    // subclasses can add additional content using this
    protected void toJXMLString_1(XMLStreamWriter writer) throws XMLStreamException, FioranoException{}

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException{
        populate(cursor, ELEM_SCHEMA);
    }

    /**
     * Returns actual schema content from optimized schema
     * @param content Schema content
     * @param subkey Key
     * @return String - De-optimized content
     */
    public String deOptimizeSchemaContent(String content, String subkey){
        if(!StringUtil.isEmpty(content)){
            if(content.startsWith("${") && content.endsWith("}")){
                String key = content.substring(2, content.length()-1);

                return (String)MapThreadLocale.getInstance().getMap().get(key);
            }else{
                String instName = (String)MapThreadLocale.getInstance().getMap().get(ServiceInstance.ELEM_SERVICE_INSTANCE);
                String portName = (String)MapThreadLocale.getInstance().getMap().get(Port.ELEM_PORT);
                String key = portName!=null ? instName+'.'+portName : instName;
                if(subkey!=null)
                    key = key + '.' + subkey;
                MapThreadLocale.getInstance().getMap().put(key, content.trim());
                return content;
            }
        }
        return content;
    }


    protected void populate(FioranoStaxParser cursor, String rootElement) throws XMLStreamException,FioranoException{
        if(cursor.markCursor(rootElement)){
            type = getIntegerAttribute(cursor, ATTR_TYPE, TYPE_XSD);
            this.rootElement = cursor.getAttributeValue(null, ATTR_ROOT_ELEMENT);

            while(cursor.nextElement()){
                String elem = cursor.getLocalName();
                if(ELEM_CONTENT.equals(elem))
                    content = deOptimizeSchemaContent(cursor.getCData(), null);
                else if(ELEM_FILE.equals(elem)){
                    file = cursor.getText();
                }else if(ELEM_CONFIG_NAME.equals(elem)){
                    configName = cursor.getText();
                }else if(ELEM_SCHEMA_REFS.equals(elem)){
                    cursor.markCursor(cursor.getLocalName());
                    while(cursor.nextElement()){
                        String elemName = cursor.getLocalName();
                        if(ELEM_SCHEMA_REF.equals(elemName)){
                            cursor.markCursor(cursor.getLocalName());
                            String uri = cursor.getAttributeValue(null, ATTR_URI);

                            while (cursor.hasNext()) {
                                switch (cursor.next()) {
                                    case XMLStreamConstants.CDATA:
                                        String cData = cursor.getCData();
                                        if (cData != null)
                                            addSchemaRef(uri, deOptimizeSchemaContent(cData, uri));
                                        
                                        break;
                                    case XMLStreamConstants.START_ELEMENT:
                                        String subElemName = cursor.getLocalName();
                                        if(ELEM_FILE.equals(subElemName)){
                                            String schemaRefFileName = cursor.getText();
                                            if(schemaRefFileName != null)
                                                addSchemaRefFile(uri, schemaRefFileName);
                                        }else if(ELEM_CONFIG_NAME.equals(subElemName)){
                                            String schemaRefConfigName = cursor.getText();
                                            if(schemaRefConfigName != null)
                                                addSchemaRefConfig(uri, schemaRefConfigName);
                                        }
                                        
                                        break;
                                }
                            }
                            cursor.resetCursor();
                        }
                    }
                    cursor.resetCursor();
                }else
                    populate_1(cursor);
            }
        }
        if(content == null){
            content = file;
        }
    }

    // subclasses can read additional content using this
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException,FioranoException{}

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    /**
     * Converts to new DMI
     * @param that old DMI
     * @bundle IGNORING_SCHEMA_REF=Ignoring Schema Reference of {0} as the namespace is null
     */
    public void convert(InPort that){
        type = InPort.TYPE_XSD==that.getStructureType() ? TYPE_XSD :TYPE_DTD;
        if(!StringUtil.isEmpty(that.getRootElemName()))
            rootElement = ClarkName.toClarkName(that.getRootElemNS(), that.getRootElemName());
        content = that.getXSD();

        Enumeration namespaces = that.getExtNamespaces();
        while(namespaces.hasMoreElements()){
            String ns = (String)namespaces.nextElement();
            if(ns!=null)
                addSchemaRef(ns, that.getExternalXSD(ns));
            else
                System.err.println("IGNORING_SCHEMA_REF");

        }
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(OutPort that){
        type = InPort.TYPE_XSD==that.getStructureType() ? TYPE_XSD :TYPE_DTD;
        if(!StringUtil.isEmpty(that.getRootElemName()))
            rootElement = ClarkName.toClarkName(that.getRootElemNS(), that.getRootElemName());
        content = that.getXSD();

        Enumeration namespaces = that.getExtNamespaces();
        while(namespaces.hasMoreElements()){
            String ns = (String)namespaces.nextElement();
            if(ns!=null)
                addSchemaRef(ns, that.getExternalXSD(ns));
            else
                System.err.println( "IGNORING_SCHEMA_REF");

        }
    }

    /**
     * Converts to new DMI
     * @param that old DMI
     */
    public void convert(PortInst that){
        type = InPort.TYPE_XSD==that.getStructureType() ? TYPE_XSD :TYPE_DTD;
        if(!StringUtil.isEmpty(that.getRootElemName()))
            rootElement = ClarkName.toClarkName(that.getRootElemNS(), that.getRootElemName());
        content = that.getXSD();

        Enumeration namespaces = that.getExtNamespaces();
        while(namespaces.hasMoreElements()){
            String ns = (String)namespaces.nextElement();
            if(ns!=null)
                addSchemaRef(ns, that.getExternalXSD(ns));
            else
                System.err.println("IGNORING_SCHEMA_REF");

        }
    }

    /**
     * Converts to new DMI
     * @param that old ApplicationContext DMI
     * @bundle IGNORING_SCHEMA_REF_OF_APP_CXT=Ignoring Schema Reference of the Application Context as the namespace is null
     */
    public void convert(ApplicationContext that){
        type = InPort.TYPE_XSD==that.getStructureType() ? TYPE_XSD :TYPE_DTD;
        if(!StringUtil.isEmpty(that.getRootElement()))
            rootElement = ClarkName.toClarkName(that.getRootElementNamespace(), that.getRootElement());
        content = that.getStructure();

        Enumeration namespaces = that.getExternalStructureNames();
        while(namespaces.hasMoreElements()){
            String ns = (String)namespaces.nextElement();
            if(ns!=null)
                addSchemaRef(ns, that.getExternalStructure(ns));
            else
                System.err.println("IGNORING_SCHEMA_REF_OF_APP_CXT");

        }
        convert_1(that);
    }

    /**
     * Converts to new DMI
     * @param that old ApplicationContext DMI
     */
    public void convert_1(ApplicationContext that){}

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        file = null;
        configName = null;
    }

    /**
     * @bundle INVALID_SCHEMA_TYPE=Invalid Schema Type {0}
     * @bundle SCHEMA_CONTENT_UNSPECIFIED=Schema Content is not specified
     * @bundle SCHEMA_REF_URI_UNSPECIFIED=SchemaRef URI is not specified
     * @bundle SCHEMA_REF_CONTENT_UNSPECIFIED=Content of SchemaRef URI "{0}" is not specified
     */
    public void validate() throws FioranoException{
        if(type!=TYPE_DTD && type!=TYPE_XSD)
            throw new FioranoException("INVALID_SCHEMA_TYPE");
        if(StringUtil.isEmpty(content))
            throw new FioranoException("SCHEMA_CONTENT_UNSPECIFIED");
        Iterator iter = schemaRefs.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            if(StringUtil.isEmpty((String)entry.getKey()))
                throw new FioranoException("SCHEMA_REF_URI_UNSPECIFIED");
            if(StringUtil.isEmpty((String)entry.getValue()))
                throw new FioranoException("SCHEMA_REF_CONTENT_UNSPECIFIED");
        }
    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        bytesMessage.writeUTF(file);
        bytesMessage.writeUTF(configName);
        bytesMessage.writeUTF(content);
        //schema ref configs
        if(schemaRefConfigs == null){
            bytesMessage.writeInt(-1);
        }else{
            bytesMessage.writeInt(schemaRefConfigs.size());
            for(String configKey : schemaRefConfigs.keySet()){
                bytesMessage.writeUTF(configKey);
                bytesMessage.writeUTF(schemaRefConfigs.get(configKey));
            }
        }

        //schema ref files
        if(schemaRefFiles == null){
            bytesMessage.writeInt(-1);
        }else{
            bytesMessage.writeInt(schemaRefFiles.size());
            for(Object configKey : schemaRefFiles.keySet()){
                bytesMessage.writeUTF((String) configKey);
                bytesMessage.writeUTF((String) schemaRefFiles.get(configKey));
            }
        }

        //schema refs
        if(schemaRefs == null){
            bytesMessage.writeInt(-1);
        }else {
            bytesMessage.writeInt(schemaRefs.size());
            for(Object configKey : schemaRefs.keySet()){
                bytesMessage.writeUTF((String) configKey);
                bytesMessage.writeUTF((String) schemaRefs.get(configKey));
            }
        }


    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        file=bytesMessage.readUTF();
        configName=bytesMessage.readUTF();
        content=bytesMessage.readUTF();

        int numConfigs = bytesMessage.readInt();
        if(numConfigs >0 ){
            schemaRefConfigs = new HashMap<>();
            for(int i = 0; i < numConfigs; i++){
                String key = bytesMessage.readUTF();
                String config = bytesMessage.readUTF();
                schemaRefConfigs.put(key, config);
            }
        }
        numConfigs = bytesMessage.readInt();
        if(numConfigs >0 ){
            schemaRefFiles = new HashMap<>();
            for(int i = 0; i < numConfigs; i++){
                String key = bytesMessage.readUTF();
                String config = bytesMessage.readUTF();
                schemaRefFiles.put(key, config);
            }
        }
        numConfigs = bytesMessage.readInt();
        if(numConfigs >0 ){
            schemaRefs = new HashMap<>();
            for(int i = 0; i < numConfigs; i++){
                String key = bytesMessage.readUTF();
                String config = bytesMessage.readUTF();
                schemaRefs.put(key, config);
            }
        }
    }
}
