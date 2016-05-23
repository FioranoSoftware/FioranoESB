/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.CommonSchemas;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.MapThreadLocale;
import com.fiorano.openesb.application.aps.ApplicationPropertySheet;
import com.fiorano.openesb.application.aps.ExternalServiceInstance;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import com.fiorano.openesb.utils.Namespaces;
import com.fiorano.openesb.utils.XMLUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Application extends ApplicationReference{
    private static final String ELEM_TARGET = "target";
    private static final String XMLNS_XSI = "xmlns:xsi";
    private static final String XMLNS_TARGET = "xmlns:target";
    private static final String XSI_LOCATION = "xsi:schemaLocation";

    /**
     * Returns the id of this object. This is used internally to identify different types of DMI objects.
     * @return the id of this object.
     */
    public int getObjectID(){
        return DmiObjectTypes.NEW_APPLICATION;
    }

    /*-------------------------------------------------[ Folder Structure ]---------------------------------------------------*/
    /**
     * schemas folder name
     */
    public static final String FOLDER_SCHEMAS = "schemas";
    /**
     * configuration folder name
     */
    public static final String FOLDER_CONFUGURATIONS = "config";
    /**
     * managable properties folder name
     */
    public static final String FOLDER_ENVIRONMENT = "env";
    /**
     * Event Process xml file name
     */
    public static final String FILE_EVENTPROCESS = "EventProcess.xml";
    /**
     * development environment xml
     */
    public static final String FILE_DEVELOPMENT = "development.xml";
    /**
     * Types of Environment: 1) development 2) testing 3) staging 4) production
     *
     *  <br>Label 'none' indicates that parsing of environment properties file is not required.
     *  To the external world only 4 labels exist, i.e. 'development,testing, staging, production'.</br>
     * @author FSTPL
     * @version 10
     */

    //Note:Please maintain labels in lower case
    public enum Label {
        /**
         * Denotes Development environment
         */
        development,
        /**
         * Denotes Testing environment
         */
        testing,
        /**
         * Denotes Staging environment
         */
         staging,
        /**
         * Denotes Production environment
         */
        production,
        /**
         * Denotes No environment
         */
        none
    }

    /**
     * xml extension
     */
    public static final String EXTENSION_CONFIG =".xml";
    /**
     * xsd extension
     */
    public static final String EXTENSION_SCHEMA =".xsd";
    /**
     * xml extension
     */
    public static final String EXTENSION_ENVIRONMENT =".xml";


    /*-------------------------------------------------[ Overridden methods ]---------------------------------------------------*/
    /**
     * over ride the setField values to make the default behavior of validation to true
     * @param is InputStream of Event process file contents
     */
    public void setFieldValues(InputStream is) throws FioranoException {
        setFieldValues(is, true);
    }

    /**
     * over ride the setField values to make the default behavior of validation to true
     * @param reader Reader of Event process file contents
     */
    public void setFieldValues(Reader reader) throws FioranoException {
        setFieldValues(reader, true);
    }

    /**
     * over ride the setField values to make the default behavior of validation to true
     * @param cursor FioranoStaxParser of Event process file contents
     */
    public void setFieldValues(FioranoStaxParser cursor) throws FioranoException {
        setFieldValues(cursor, true);
    }

    /*-------------------------------------------------[ layout ]---------------------------------------------------*/
    /**
     * element layout in event process xml
     */
    public static final String ELEM_LAYOUT = "layout";

    private String layout;

    /**
     * Returns layout of this event process
     * @return String
     */
    public String getLayout(){
        return layout;
    }

    /**
     * Sets specified <code>layout</code> of this event process
     * @param layout layout to be set
     */
    public void setLayout(String layout){
        this.layout = layout;
    }

    /*-------------------------------------------------[ ApplicationContext ]---------------------------------------------------*/

    private ApplicationContext applicationContext;

    /**
     * Returns application context of this event process
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * Sets specified <code>applicationContext</code> of this event process
     * @param applicationContext application context to be set
     */
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    /*-------------------------------------------------[ ServiceInstances ]---------------------------------------------------*/
    /**
     * element service-instances in event process xml
     */
    public static final String ELEM_SERVICE_INSTANCES="service-instances";

    private List<ServiceInstance> serviceInstances = new ArrayList<ServiceInstance>();

    /**
     * Adds specified service instance <code>inst</code> to this event process
     * @param inst service instance to be set
     */
    public void addServiceInstance(ServiceInstance inst){
        serviceInstances.add(inst);
    }

    /**
     * Removes specified service instance <code>inst</code> from this event process
     * @param inst service instance to be removed
     */
    public void removeServiceInstance(ServiceInstance inst){
        serviceInstances.remove(inst);
    }

    /**
     * Returns a list of all service instances of this event process
     * @return List
     */
    public List<ServiceInstance> getServiceInstances(){
        return serviceInstances;
    }

    /**
     * Adds specified list of service instances to this event process
     * @param serviceInstances list of service instances to be added
     */
    public void setServiceInstances(List<ServiceInstance> serviceInstances){
        this.serviceInstances = serviceInstances;
    }

    /**
     * Returns ServiceInstance DMI for specified service instance having name <code>serviceName</code>
     * @param serviceName name of service instance
     * @return ServiceInstance
     */
    public ServiceInstance getServiceInstance(String serviceName){
        return (ServiceInstance)DmiObject.findNamedObject(serviceInstances, serviceName);
    }

    /**
     * Returns RemoteServiceInstance DMI for specified remote service instance having name <code>serviceName</code>
     * @param serviceName name of remote service instance
     * @return RemoteServiceInstance
     */
    public RemoteServiceInstance getRemoteServiceInstance(String serviceName){
        return (RemoteServiceInstance)DmiObject.findNamedObject(remoteServiceInstances, serviceName);
    }

    /*-------------------------------------------------[ RemoteServiceInstances ]---------------------------------------------------*/

    private List<RemoteServiceInstance> remoteServiceInstances = new ArrayList<RemoteServiceInstance>();

    /**
     * Adds specified remote service instance <code>rinst</code> to this event process
     * @param rinst remote service instance to be added
     */
    public void addRemoteServiceInstance(RemoteServiceInstance rinst){
        remoteServiceInstances.add(rinst);
    }

    /**
     * Removes specified remote service instance <code>rinst</code> from this event process
     * @param rinst remote service instance to be removed
     */
    public void removeServiceInstance(RemoteServiceInstance rinst){
        remoteServiceInstances.remove(rinst);
    }

    /**
     * Returns remote service instances of this event process
     * @return List
     */
    public List getRemoteServiceInstances(){
        return remoteServiceInstances;
    }

    /**
     * Adds specified list of remote service instances to this event process
     * @param remoteServiceInstances list of remote service instances to be added
     */
    public void setRemoteServiceInstances(List<RemoteServiceInstance> remoteServiceInstances){
        this.remoteServiceInstances = remoteServiceInstances;
    }

    /*-------------------------------------------------[ Routes ]---------------------------------------------------*/
    /**
     * element routes in event process xml
     */
    public static final String ELEM_ROUTES="routes";

    private List<Route> routes = new ArrayList<Route>();

    /**
     * Adds specified route <code>route</code> to this event process
     * @param route route to be added
     */
    public void addRoute(Route route){
        routes.add(route);
    }

    /**
     * Removes specified route from this event process
     * @param route route to be removed
     */
    public void removeRoute(Route route){
        routes.remove(route);
    }

    /**
     * Returns list of routes of this event process
     * @return List
     */
    public List<Route> getRoutes(){
        return routes;
    }

    /**
     * Adds specified list of routes to this event process
     * @param routes list of routes to be added
     */
    public void setRoutes(List<Route> routes){
        this.routes = routes;
    }

    /*-------------------------------------------------[ WEB Interface Support ]---------------------------------------------------*/

    /**
     * Returns true if an event process is web service enabled, i.e. if it has a stub component
     * @param webInterfaceType type of web interface
     * @return boolean specifying whether the event process is web service enabled
     */
    public boolean isWebInterfaceEnabled(int webInterfaceType){
        if(webInterfaceType==WEB_SERVICE){
            if(! isWebInterfaceSupported(WEB_SERVICE))
                return false;
            for(int i = 0; i<serviceInstances.size(); i++){
                if(((ServiceInstance)serviceInstances.get(i)).getGUID().equalsIgnoreCase("wsstub"))
                    return true;
            }
        }else if(webInterfaceType==HTTP_SERVICE){
            if(! isWebInterfaceSupported(HTTP_SERVICE))
                return false;
            for(int i = 0; i<serviceInstances.size(); i++){
                if(((ServiceInstance)serviceInstances.get(i)).getGUID().equalsIgnoreCase("httpstub"))
                    return true;
            }
        }else if(webInterfaceType==WORK_LIST){
            if(! isWebInterfaceSupported(WORK_LIST))
                return false;
            for(int i = 0; i<serviceInstances.size(); i++){
                if(((ServiceInstance)serviceInstances.get(i)).getGUID().indexOf("WorkList") != -1)
                    return true;
            }
        }
        return false;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <application>
     *      ...super-class...
     *      ...application-context?...
     *      <service-instances>
     *          ...inst*...
     *          ...remote-inst*...
     *      </service-instances>?
     *      <routes>
     *          ...route+...
     *      </routes>?
     *      <layout>string</layout>?
     * </application>
     */

    protected void toJXMLString_1(XMLStreamWriter writer, boolean writeManageableProperties) throws XMLStreamException, FioranoException{

        MapThreadLocale.getInstance().getMap().put("ERROR_XSD", CommonSchemas.ERROR_XSD);

        try{
            if(applicationContext!=null)
                applicationContext.toJXMLString(writer, writeManageableProperties);

            if(serviceInstances.size()>0 || remoteServiceInstances.size()>0){
                writer.writeStartElement(ELEM_SERVICE_INSTANCES);
                {
                    writeCollection(writer, serviceInstances, null, writeManageableProperties);
                    writeCollection(writer, remoteServiceInstances, null);
                }
                writer.writeEndElement();
            }

            writeCollection(writer, routes, ELEM_ROUTES, writeManageableProperties);
            writeCDATAElement(writer, ELEM_LAYOUT, layout);
        } finally{
            MapThreadLocale.getInstance().getMap().clear();
        }
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    /**
     * Populates the application instance from <code>cursor</code>
     * @param cursor FioranoStaxParser of application file contents
     * @throws XMLStreamException
     * @throws FioranoException
     */

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        MapThreadLocale.getInstance().getMap().put("ERROR_XSD", CommonSchemas.ERROR_XSD);
        try{
            super.populate(cursor);
        } finally{
            MapThreadLocale.getInstance().getMap().clear();
        }
    }

    /**
     * Populates the application instance from <code>cursor</code>
     * @param cursor FioranoStaxParser of application file contents
     * @throws XMLStreamException
     * @throws FioranoException
     */
    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        String elemName = cursor.getLocalName();
        if(ApplicationContext.ELEM_APPLICATION_CONTEXT.equals(elemName)){
            applicationContext = new ApplicationContext();
            applicationContext.setFieldValues(cursor);
        }else if(ELEM_SERVICE_INSTANCES.equals(elemName)){
            cursor.markCursor(cursor.getLocalName());
            while(cursor.nextElement()){
                if(ServiceInstance.ELEM_SERVICE_INSTANCE.equals(cursor.getLocalName())){
                    ServiceInstance inst = new ServiceInstance();
                    inst.setFieldValues(cursor);
                    serviceInstances.add(inst);
                } else if(RemoteServiceInstance.ELEM_REMOTE_SERVICE_INSTANCE.equals(cursor.getLocalName())){
                    RemoteServiceInstance remoteInst = new RemoteServiceInstance();
                    remoteInst.setFieldValues(cursor);
                    remoteServiceInstances.add(remoteInst);
                }
            }
            cursor.resetCursor();
        }else if(ELEM_ROUTES.equals(elemName)){
            cursor.markCursor(cursor.getLocalName());
            while(cursor.nextElement()){
                if(Route.ELEM_ROUTE.equals(cursor.getLocalName())){
                    Route route = new Route();
                    route.setFieldValues(cursor);
                    routes.add(route);
                }
            }
            cursor.resetCursor();
        }else if(ELEM_LAYOUT.equals(elemName))
            layout = cursor.getCData();


    }

    /**
     * Reads Manageable Properties from <code>applicationFolderName</code> with Label <code>label</code>
     * @param applicationFolderName event process folder name
     * @param label Label environment label
     * @throws FioranoException FioranoException
     * @throws XMLStreamException XMLStreamException
     * @throws FileNotFoundException FileNotFoundException
     */
    public void readManageableProperties(File applicationFolderName, Label label) throws XMLStreamException, FileNotFoundException,FioranoException {
        //1. get the manageable properties file for the corresponding label
        if(label.equals(Label.none))
            return;
        File manageablePropertiesFile = getManageablePropertiesFile(applicationFolderName, label);
        if(!manageablePropertiesFile.exists())
                throw new FioranoException();
        //2. Get the stax parser for the file
        FileInputStream fis = new FileInputStream(manageablePropertiesFile);
        FioranoStaxParser cursor = new FioranoStaxParser(fis);
        //3. For each element that is a service instance
        //            Populate the manageable properties for the service
        while (cursor.nextElement()) {
            String elemName = cursor.getLocalName();
            if (ServiceInstance.ELEM_INSTANCE.equalsIgnoreCase(elemName)) {
                String instanceName = DmiObject.getStringAttribute(cursor, ServiceInstance.ATTR_NAME, null);
                getServiceInstance(instanceName).readManageableProperties(cursor);

            }
        }

        cursor.close();
        try {
            fis.close();
        } catch (IOException e) {
            throw new FioranoException(e);
        }
    }

    /**
     * Writes manageable properties file with the specified label
     * @param applicationFolderName event process folder
     * @param label environment label
     * @throws FioranoException FioranoException
     * @throws XMLStreamException XMLStreamException
     */
    public void writeManageableProperties(File applicationFolderName, Label label) throws FioranoException, XMLStreamException {
        File manageablePropertiesFile = getManageablePropertiesFile(applicationFolderName, label);
        if(!manageablePropertiesFile.exists())
            manageablePropertiesFile.getParentFile().mkdirs();

        XMLOutputFactory outputFactory = XMLUtils.getStaxOutputFactory();
//        System.out.println(".....:"+outputFactory);
        //outputFactory.setProperty(XMLOutputFactory.INDENTATION, "/t");
        XMLStreamWriter writer = null;
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(manageablePropertiesFile);
            writer = outputFactory.createXMLStreamWriter(fos);
            writer.writeStartDocument();
            {

                writer.writeStartElement(ELEM_TARGET, ELEM_TARGET, Namespaces.URI_TARGET);
                writer.writeAttribute(XMLNS_TARGET, Namespaces.URI_TARGET);
                writer.writeAttribute(XMLNS_XSI, Namespaces.URI_XSI);
                writer.writeAttribute(XSI_LOCATION, Namespaces.URI_ENV_XSD);
                {
                    for (ServiceInstance instance : getServiceInstances()) {
                        instance.writeManageableProperties(writer);
                    }

                }
                writer.writeEndElement();
            }

            writer.writeEndDocument();

            writer.flush();
        }catch(XMLStreamException e){
            throw new FioranoException(e);
        }
        catch(IOException e){
            throw new FioranoException(e);
        }finally{
            try {
                if (writer != null)
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

    }

    /**
     * Gets manageable properties file with the specified label
     * @param applicationFolderName event process folder
     * @param label environment label
     * @throws FioranoException FioranoException
     * @throws XMLStreamException XMLStreamException
     */
    private File getManageablePropertiesFile(File applicationFolderName, Label label) {
        String fileName = applicationFolderName + File.separator + FOLDER_ENVIRONMENT + File.separator + label.name() + EXTENSION_ENVIRONMENT;
        return new File(fileName);

    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    protected void convert_1(ApplicationPropertySheet that){
        if(that.getApplicationHeader().getApplicationContext()!=null
                && !StringUtils.isEmpty(that.getApplicationHeader().getApplicationContext().getStructure())){
            applicationContext = new ApplicationContext();
            applicationContext.convert(that.getApplicationHeader().getApplicationContext());
        }

        Enumeration enumer = that.getServiceInstances().getServiceInstances();
        while(enumer.hasMoreElements()){
            ServiceInstance inst = new ServiceInstance();
            inst.convert((com.fiorano.openesb.application.aps.ServiceInstance)enumer.nextElement());
            serviceInstances.add(inst);
        }

        enumer = that.getServiceInstances().getExtServiceInstances();
        while(enumer.hasMoreElements()){
            RemoteServiceInstance remoteInst = new RemoteServiceInstance();
            remoteInst.convert((ExternalServiceInstance)enumer.nextElement());
            remoteServiceInstances.add(remoteInst);
        }

        enumer = that.getRoutes().getRouteEnumeration();
        while(enumer.hasMoreElements()){
            Route route = new Route();
            route.convert((com.fiorano.openesb.application.aps.Route)enumer.nextElement());
            routes.add(route);
        }

        layout = that.getLayoutInfo();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    /**
     * Resets this application DMI
     */
    public void reset(){
        super.reset();

        layout = null;
        applicationContext = null;
        serviceInstances.clear();
        remoteServiceInstances.clear();
        routes.clear();
    }

    /**
     * Validates this Application DMI. Checks whether all the mandatory fields are set.
     */
    public void validate() throws FioranoException{
        try{
        super.validate();

        if(applicationContext!=null)
            applicationContext.validate();
        validateList(serviceInstances);
        validateList(remoteServiceInstances);
        validateList(routes);
        }catch (FioranoException t) {
                throw new FioranoException(t);
        }
    }
}

/*-------------------------------------------------[ Complete Application XMLStructure ]---------------------------------------------------*/
/*
<application guid="string" version="string">
	 <display name="string" categories="cvs"/>
	 <creation date="date" authors="cvs"/>
	 <short-description>string</short-description>
	 <long-description>string</long-description>
	 <deployment label="string" cache="boolean"/>
	 <component-launch-order enabled="boolean"/>
	 <component-stop-order  enabled="boolean" useReverseComponentLaunchOrder="boolean'/>
	 <web exposedInterfaces='int'/>

     <application-context>
		<schema type="int" root="string">
			 <content>string</content>
			 <schemarefs>
				 <schemaref uri="string">string</schemaref>+
			 </schemarefs>?
		</schema>
		<value>string</value>?
	 </application-context>?
     <service-instances>
		<inst name="string">
			 <service guid="string" version="float" version-locked="boolean"?/>
			 <short-description>string</short-description>?
			 <long-description>string</long-description>?
			 <configuration>string</configuration>?
			 <deployment nodes="cvs" cache="boolean"/>
			 <execution launch-type="int"? buffer-limit="long"?>
				 <debug enable="boolean" port="int"/>
				 <launch first-available-node="boolean"? kill-primary-on-secondary-launch="boolean"?/>?
			 </execution>
			 <inputport-instances>
				<inputport-instance name="string" request-reply="boolean"?>
				  <description>string</description>?
				  <schema type="int" root="string">
						 <content>string</content>
						 <schemarefs>?
							 <schemaref uri="string">string</schemaref>+
						 </schemarefs>
				   </schema> ?

				  <proxy used="boolean" url="string">
					  <authentication user="string" password="string"/>
				  </proxy>
				  <jms clientid="string" persistant="boolean" enabled="boolean">
					  <destination type="int" custom="boolean" name="string"/>
					  <authentication security-manager="string" user="string" password="string"/>?
					  <subscriber sessions="int"? acknowledgement="int"? selector="string"?>
						 <transaction enabled="boolean" size="int"/>?
						 <subscription durable="boolean" name="string"/>
					  </subscriber>

				  </jms>
				  <workflow type="int"/>?

				</inputport-instance>+

			 </inputport-instances>?
			 <outputport-instances>
				 <outputport-instance name="string" request-reply="boolean">?
					 <description>string</description>?
					 <proxy used="boolean" url="string">
						 <authentication user="string" password="string"/>
					 </proxy>
					 <jms clientid="string" persistant="boolean" enabled="boolean">
						 <destination type="int" custom="boolean" name="string"/>
						 <authentication security-manager="string" user="string" password="string"/>?
						 <publisher>
							  <messages time-to-leave="int" compress="boolean" priority="int"/>?
							  <transaction enabled="boolean" size="int"/>?
							  <subscription durable="boolean" name="string"/>
						  </publisher>
					 </jms>
					 <workflow type="int"/>?
					 					 <message-transformation factory="string">
						 <script>string</script>
						 <project>string</project>?
						 <jms-script>string</jms-script>?
					</message-transformation>?
				 </outputport-instance>+
			 </outputport-instances>?
			 <logmanager logger="string">
				 <property name="string">string</property>*
			 </logmanager>?

			 <runtime-arguments>
				<runtime-argument name="string" type="string" mandatory="boolean"? inmemory="boolean"?>
					 <value>string</value>?
				</runtime-argument>+
			 </runtime-arguments>?
			 <servicerefs>
				 <serviceref guid="string" version="float"/>+
			 </servicerefs>?
		</inst>*

         <remoteinst name="string" application-guid="string" inst-name="string"/>*
     </service-instances>?
     <routes>
		<route name="string" displayName="string"?>
			 <source inst="string" port="string"/>
			 <target inst="string" port="string"/>
			 <short-description>string</short-description>?
			 <long-description>string</long-description>?
			 <messages persistant="true" durable="boolean" time-to-live="long"/>
			 			 <message-transformation factory="string">
				 <script>string</script>
				 <project>string</project>?
				 <jms-script>string</jms-script>?
			</message-transformation>?
			  <selectors>
				 <selector type="sender|jms|body|application-context">string|...xpath...</selector>+
			 </selectors>?
		</route>+
     </routes>?
     <layout>string</layout>?
</application>
*/

