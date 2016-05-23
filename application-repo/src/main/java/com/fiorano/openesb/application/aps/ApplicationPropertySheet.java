/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.Param;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.schema.SchemaUtil;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class ApplicationPropertySheet extends DmiObject
{

    private static final String C_SCHEMA = "schema";

    ApplicationHeader m_appHeader;
    ServiceInstances m_servInstances;
    WorkflowStartPorts m_workflowStartPorts;
    WorkflowExitPorts m_workflowExitPorts;
    Routes          m_routes;
    OnException     m_onException;
    String          m_layoutInfo;

    Hashtable m_schemas = new Hashtable(1, 1);

    /**
     * This is called to construct an object of the
     * <code>ApplicationPropertySheet</code> class.
     *
     * @since Tifosi2.0
     */
    public ApplicationPropertySheet()
    {
    }


    /**
     *  Gets the header information of the application specified by the
     *  <code>ApplicationHeader</code> argument.
     *
     * @return object of ApplicationHeader
     * @see #setApplicationHeader(ApplicationHeader)
     * @since Tifosi2.0
     */
    public ApplicationHeader getApplicationHeader()
    {
        return m_appHeader;
    }


    /**
     * This method is called to obtain an object of <code>ServiceInstances</code>,
     * containing information about all the service instances belonging to the
     * application.
     *
     * @return object of ServiceInstances
     * @see #setServiceInstances(ServiceInstances)
     * @since Tifosi2.0
     */
    public ServiceInstances getServiceInstances()
    {
        return m_servInstances;
    }


    /**
     *  Gets an object of <code>WorkflowStartPorts</code>,
     *  containing workFlow Start Ports of the application.
     *
     * @return object of WorkflowStartPorts
     * @see #setWorkflowStartPorts(WorkflowStartPorts)
     * @since Tifosi2.0
     */
    public WorkflowStartPorts getWorkflowStartPorts()
    {
        return m_workflowStartPorts;
    }


    /**
     *  Gets a object of <code>WorkflowExitPorts</code>,
     *  containing workFlow Exit Ports of application.
     *
     * @return object of WorkflowExitPorts
     * @see #setWorkFlowExitPorts(WorkflowExitPorts)
     * @since Tifosi2.0
     */
    public WorkflowExitPorts getWorkFlowExitPorts()
    {
        return m_workflowExitPorts;
    }


    /**
     * Gets an object of <code>Routes</code>, which contains
     * information about all the routes.
     *
     * @return object of Routes
     * @see #setRoutes(Routes)
     * @since Tifosi2.0
     */
    public Routes getRoutes()
    {
        return m_routes;
    }


    /**
     *  Gets the <code>onException</code> object of the application.
     *
     * @return onException object
     * @see #setOnException(OnException)
     * @since Tifosi2.0
     */
    public OnException getOnException()
    {
        return m_onException;
    }


    /**
     *  Gets the <code>layoutInfo</code> of the application.
     *
     * @return The layoutInfo value
     * @see #setLayoutInfo(String)
     * @since Tifosi2.0
     */
    public String getLayoutInfo()
    {
        return m_layoutInfo;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APPLICATION_PROPERTIES;
    }


    /**
     *  Sets the header information of the application specified by the
     *  <code>ApplicationHeader</code> argument, to this
     *  <code>ApplicationPropertySheet</code> object.
     *
     * @param appHeader object of ApplicationHeader
     * @see #getApplicationHeader()
     * @since Tifosi2.0
     */
    public void setApplicationHeader(ApplicationHeader appHeader)
    {
        m_appHeader = appHeader;
    }


    /**
     * This method is called to set an object of <code>ServiceInstances</code>,
     * containing information about all the service instances belonging to
     * the application.
     *
     * @param serInstances object of ServiceInstances
     * @see #getServiceInstances()
     * @since Tifosi2.0
     */
    public void setServiceInstances(ServiceInstances serInstances)
    {
        m_servInstances = serInstances;
    }


    /**
     *  Sets the <code>WorkflowStartPorts</code> object, containing
     *  information about start ports of workflow.
     *
     * @param workflowStartPorts object of WorkflowStartPorts
     * @see #getWorkflowStartPorts()
     * @since Tifosi2.0
     */
    public void setWorkflowStartPorts(WorkflowStartPorts workflowStartPorts)
    {
        m_workflowStartPorts = workflowStartPorts;
    }


    /**
     *  Sets a <code>WorkFlowExitPorts</code> object, containing information
     *  about exit ports of workflow of the application.
     *
     * @param workflowExitPorts object of workFlowExitPorts
     * @see #getWorkFlowExitPorts()
     * @since Tifosi2.0
     */
    public void setWorkFlowExitPorts(WorkflowExitPorts workflowExitPorts)
    {
        m_workflowExitPorts = workflowExitPorts;
    }


    /**
     *  Sets the <code>Routes</code>, containing information about all the
     *  routes.
     *
     * @param routes object of Routes.
     * @see #getRoutes()
     * @since Tifosi2.0
     */
    public void setRoutes(Routes routes)
    {
        m_routes = routes;
    }


    /**
     *  Sets the <code>onException</code> attribute of the application.
     *
     * @param onException The new onException value
     * @see #getOnException()
     * @since Tifosi2.0
     */
    public void setOnException(OnException onException)
    {
        m_onException = onException;
    }


    /**
     *  Sets the <code>layoutInfo</code> for the application.
     *
     * @param layoutInfo The layoutInfo value
     * @see #getLayoutInfo()
     * @since Tifosi2.0
     */
    public void setLayoutInfo(String layoutInfo)
    {
        m_layoutInfo = layoutInfo;
    }

    /**
     * Resets the Port Inst Schemas
     */
    public void resetPortInstSchemas()
    {
        if (m_schemas != null && m_schemas.size() > 0)
        {
            Enumeration serviceInstanceEnum = getServiceInstances().getServiceInstances();
            while(serviceInstanceEnum != null && serviceInstanceEnum.hasMoreElements())
            {
                ServiceInstance svcInstance = (ServiceInstance)serviceInstanceEnum.nextElement();
                Enumeration inPortsEnum = svcInstance.getPortInstDescriptor().getInPorts();
                while(inPortsEnum != null && inPortsEnum.hasMoreElements())
                {
                    PortInst portInst = (PortInst)inPortsEnum.nextElement();
                    String xsdRef = portInst.getXSDRef();
                    if (xsdRef != null)
                    {
                         portInst.setXSD((String)m_schemas.get(xsdRef));
                    }
                    Vector params = portInst.getParameters();
                    if (params != null && params.size() > 0)
                    {
                        Enumeration _enum = params.elements();
                        while (_enum.hasMoreElements())
                        {
                            Param param = (Param) _enum.nextElement();
                            String paramXsdRef = param.getParamValue();
                            if(paramXsdRef != null && paramXsdRef.startsWith(C_SCHEMA))
                            {
                                String paramXsd = (String)m_schemas.get(paramXsdRef);
                                if(paramXsd != null)
                                    param.setParamValue(paramXsd);
                            }
                        }
                    }
                }
                Enumeration outPortsEnum = svcInstance.getPortInstDescriptor().getOutPorts();
                while(outPortsEnum != null && outPortsEnum.hasMoreElements())
                {
                    PortInst portInst = (PortInst)outPortsEnum.nextElement();
                    String xsdRef = portInst.getXSDRef();
                    if (xsdRef != null)
                    {
                           portInst.setXSD((String)m_schemas.get(xsdRef));
                    }
                    Vector params = portInst.getParameters();
                    if (params != null && params.size() > 0)
                    {
                        Enumeration _enum = params.elements();
                        while (_enum.hasMoreElements())
                        {
                            Param param = (Param) _enum.nextElement();
                            String paramXsdRef = param.getParamValue();
                            if(paramXsdRef != null && paramXsdRef.startsWith(C_SCHEMA))
                            {
                                String paramXsd = (String)m_schemas.get(paramXsdRef);
                                if(paramXsd != null)
                                    param.setParamValue(paramXsd);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *  Sets all the fieldValues, of this <code>ApplicationPropertySheet</code>
     *  object, using the specified XML Element
     *
     * @param xmlElement XML Element.
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element xmlElement)
        throws FioranoException
    {
        TreeWalker walker = new TreeWalker(xmlElement);

        Element schemasElement = walker.getNextElement("Schemas");
        if (schemasElement != null)
        {
            m_schemas = new Hashtable(1, 1);
            NodeList children = schemasElement.getChildNodes();
            Node child;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equals("Schema"))
                {
                    String key = ((Element)child).getAttribute("id");
                    String nodeValue = XMLUtils.getNodeValueAsString(child);
                    if (key != null && nodeValue != null)
                        m_schemas.put(key, nodeValue);
                }
            }
        }

        //  ApplicationHeader information.
        walker.reset();

        Element appHeaderElement = walker.getNextElement("ApplicationHeader");

        if (appHeaderElement == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        m_appHeader = new ApplicationHeader();
        m_appHeader.setFieldValues(appHeaderElement);

        //  ServiceInstances information.
        walker.reset();

        Element serviceInstancesElement = walker.getNextElement("ServiceInstances");

        if (serviceInstancesElement == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        m_servInstances = new ServiceInstances();
        m_servInstances.setFieldValues(serviceInstancesElement);

        //  WorkflowStartPorts information.
        walker.reset();

        Element wfStartElement = walker.getNextElement("WorkflowStartPorts");

        if (wfStartElement != null)
        {
            m_workflowStartPorts = new WorkflowStartPorts();
            m_workflowStartPorts.setFieldValues(wfStartElement);
        }

        //  WorkflowExitPorts information.
        walker.reset();

        Element wfExitElement = walker.getNextElement("WorkflowExitPorts");

        if (wfExitElement != null)
        {
            m_workflowExitPorts = new WorkflowExitPorts();
            m_workflowExitPorts.setFieldValues(wfExitElement);
        }

        //  Routes information.
        walker.reset();

        Element routesElement = walker.getNextElement("Routes");

        if (routesElement != null)
        {
            m_routes = new Routes();
            m_routes.setFieldValues(routesElement);
        }

        //  OnException information.
        walker.reset();

        Element onExcElement = walker.getNextElement("OnException");

        if (onExcElement != null)
        {
            m_onException = new OnException();
            m_onException.setFieldValues(onExcElement);
        }

        //  Layout information.
        walker.reset();

        Element layoutElement = walker.getNextElement("Layout");

        if (layoutElement != null)
        {
            m_layoutInfo = XMLUtils.getNodeValueAsString(layoutElement);
        }
        resetPortInstSchemas();
        validate();
    }

    /**
     *  Sets all the fieldValues, of this <code>ApplicationPropertySheet</code>
     *  object, using the specified XML string.
     *
     * @param xmlString XML String.
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(String xmlString)
        throws FioranoException
    {
        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString); 
        setFieldValues(doc.getDocumentElement());
    }

    /**
     *  Sets all the fieldValues, of this <code>ApplicationPropertySheet</code>
     *  object, using the specified InputSource
     * @param source InputSource
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(InputSource source) throws FioranoException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        try{
            Document doc = factory.newDocumentBuilder().parse(source);
            setFieldValues(doc.getDocumentElement());
        } catch(Exception ex){
            throw new FioranoException(ex);
        }
    }


    /**
     * This will populate the AppliationPropertySheet from the underlying XML.
     *
     * @param cursor
     * @throws XMLStreamException
     */
    protected void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException
    {
        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if(cursor.markCursor(APSConstants.APPLICATION)){
            // Get Child Elements
            while(cursor.nextElement()){
                String nodeName = cursor.getLocalName();

                if(nodeName.equalsIgnoreCase(APSConstants.SCHEMAS)){
                    populateSchema(cursor);
                }

                else if(nodeName.equalsIgnoreCase(APSConstants.APPLICATION_HEADER)){
                    m_appHeader = new ApplicationHeader();
                    m_appHeader.setFieldValues(cursor);
                }

                // ServiceInstances information.
                else if(nodeName.equalsIgnoreCase(APSConstants.SERVICE_INSTANCES)){
                    m_servInstances = new ServiceInstances();
                    m_servInstances.setFieldValues(cursor);
                }

                // WorkflowStartPorts information.
                else if(nodeName.equalsIgnoreCase(APSConstants.WORKFLOW_START_PORTS)){
                    m_workflowStartPorts = new WorkflowStartPorts();
                    m_workflowStartPorts.setFieldValues(cursor);
                }
                // WorkflowExitPorts information.
                else if(nodeName.equalsIgnoreCase(APSConstants.WORKFLOW_EXIT_PORTS)){
                    m_workflowExitPorts = new WorkflowExitPorts();
                    m_workflowExitPorts.setFieldValues(cursor);
                }

                //OnException information.
                else if(nodeName.equalsIgnoreCase(APSConstants.ON_EXCEPTION)){
                    m_onException = new OnException();
                     m_onException.setFieldValues(cursor);
                }

                //  Routes information.
                else if(nodeName.equalsIgnoreCase(APSConstants.ROUTES)){
                    m_routes = new Routes();
                     m_routes.setFieldValues(cursor);
                }

                else if(nodeName.equalsIgnoreCase(APSConstants.LAYOUT)){
                    m_layoutInfo = getLayoutInfo(cursor);
                    } else{
                    // Throw some error.
                }

                nodeName = null;
            }

             resetPortInstSchemas();
             validate();

//            if(attributes!=null){
//                attributes.clear();
//                attributes = null;
//            }
//
//            nodeValue = null;

        } else
            throw new XMLStreamException("Invalid Application File");
    }


    private void populateSchema(FioranoStaxParser cursor) throws XMLStreamException{
        if(cursor.markCursor(APSConstants.SCHEMAS)){
            // Get Attributes. This MUST be done before accessing any data of element.
            Hashtable attributes = cursor.getAttributes();
            m_schemas = new Hashtable(1, 1);

            // Get Child Elements
            while(cursor.nextElement()){
                String nodeName = cursor.getLocalName();


                // Get Attributes
                attributes = cursor.getAttributes();

                if(nodeName.equalsIgnoreCase(APSConstants.SCHEMA)){
                    if(attributes!=null){
                        String key = (String)attributes.get(APSConstants.SCHEMA_ID);
                        // Get the Schema CData
                        String nodeValue = cursor.getCData();
                        if(key!=null && nodeValue!=null)
                            m_schemas.put(key, nodeValue);
                    }
                } else{
                    // WE MUST THROW SOME EXCEPTION.
                    System.out.println("some error in ApplicationPropertySheet");
                }
            }

            //This is required to allow cursor to proceede furthur.
            cursor.resetCursor();
        }

    }

    private String getLayoutInfo(FioranoStaxParser cursor) throws XMLStreamException{
        if(cursor.markCursor(APSConstants.LAYOUT)){
            return cursor.getCData();
        }
        return null;
    }
    /**
     *  This utility method is used to get XML String representation of this
     *  <code>ApplicationPropertySheet</code> object.
     *
     * @return XML String for this object
     * @exception FioranoException if the calls fails to succeed.
     * @since Tifosi2.0
     */
    public String toXMLString()
        throws FioranoException
    {
        return toJXMLString();
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method tests whether this object of <code>ApplicationPropertySheet</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_appHeader == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        m_appHeader.validate();

        if (m_onException != null)
        {
            m_onException.validate();
        }

        if (m_routes != null)
        {
            m_routes.validate();
        }

        if (m_servInstances == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        m_servInstances.validate();

        if (m_workflowExitPorts != null)
        {
            m_workflowExitPorts.validate();
        }

        if (m_workflowStartPorts != null)
        {
            m_workflowStartPorts.validate();
        }
    }


    /**
     *  This method writes this <code>ApplicationPropertySheet</code> object
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        if (m_layoutInfo != null)
        {
            UTFReaderWriter.writeUTF(out, m_layoutInfo);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_appHeader != null)
        {
            out.writeInt(1);
            m_appHeader.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_servInstances != null)
        {
            out.writeInt(1);
            m_servInstances.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_workflowStartPorts != null)
        {
            out.writeInt(1);
            m_workflowStartPorts.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_workflowExitPorts != null)
        {
            out.writeInt(1);
            m_workflowExitPorts.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_routes != null)
        {
            out.writeInt(1);
            m_routes.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_onException != null)
        {
            out.writeInt(1);
            m_onException.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This method reads this object <code>ApplicationPropertySheet</code> from
     *  the specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        String layoutInfo = UTFReaderWriter.readUTF(is);

        if (layoutInfo.equals(""))
        {
            m_layoutInfo = null;
        }
        else
        {
            m_layoutInfo = layoutInfo;
        }

        int result = is.readInt();

        if (result == 1)
        {
            m_appHeader = new ApplicationHeader();
            m_appHeader.fromStream(is, versionNo);
        }

        result = is.readInt();
        if (result == 1)
        {
            m_servInstances = new ServiceInstances();
            m_servInstances.fromStream(is, versionNo);
        }

        result = is.readInt();
        if (result == 1)
        {
            m_workflowStartPorts = new WorkflowStartPorts();
            m_workflowStartPorts.fromStream(is, versionNo);
        }

        result = is.readInt();
        if (result == 1)
        {
            m_workflowExitPorts = new WorkflowExitPorts();
            m_workflowExitPorts.fromStream(is, versionNo);
        }

        result = is.readInt();
        if (result == 1)
        {
            m_routes = new Routes();
            m_routes.fromStream(is, versionNo);
        }

        result = is.readInt();
        if (result == 1)
        {
            m_onException = new OnException();
            m_onException.fromStream(is, versionNo);
        }
    }

    /**
     * This utility method is used to get the String representation of this
     * <code>ApplicationPropertySheet</code> object.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Application Property Sheet = ");
        strBuf.append("[");
        if (m_appHeader != null)
        {
            strBuf.append("Application Header = ");
            strBuf.append(m_appHeader.toString());
            strBuf.append(", ");
        }
        if (m_servInstances != null)
        {
            strBuf.append("Service Instance = ");
            strBuf.append(m_servInstances.toString());
            strBuf.append(", ");
        }
        if (m_workflowStartPorts != null)
        {
            strBuf.append("Work Flow Start ports = ");
            strBuf.append(m_workflowStartPorts.toString());
            strBuf.append(", ");
        }
        if (m_workflowExitPorts != null)
        {
            strBuf.append("Work Flow Exit ports = ");
            strBuf.append(m_workflowExitPorts.toString());
            strBuf.append(", ");
        }
        if (m_routes != null)
        {
            strBuf.append("Routes = ");
            strBuf.append(m_routes.toString());
            strBuf.append(", ");
        }
        return strBuf.toString();
    }

    /**
     *
     */
    public void populateSchemas()
    {
        if(getServiceInstances()==null)
            return;
        Enumeration serviceInstanceEnum = getServiceInstances().getServiceInstances();
        while(serviceInstanceEnum != null && serviceInstanceEnum.hasMoreElements())
        {
            ServiceInstance svcInstance = (ServiceInstance)serviceInstanceEnum.nextElement();
            Enumeration inPortsEnum = svcInstance.getPortInstDescriptor().getInPorts();
            while(inPortsEnum != null && inPortsEnum.hasMoreElements())
            {
                PortInst portInst = (PortInst)inPortsEnum.nextElement();
                if (portInst.getXSD() != null && portInst.getXSD().trim().length() > 0)
                {
                    // check if the schema is the default error schema. if yes then dont write it
                    if(SchemaUtil.isDefaultErrorPortXSD(portInst.getXSD()))
                        continue;

                    String xsdRef = populateSchema(portInst.getXSD());
                    portInst.setXSDRef(xsdRef);
                }
                Vector params = portInst.getParameters();
                if (params != null && params.size() > 0)
                {
                    Enumeration _enum = params.elements();
                    while (_enum.hasMoreElements())
                    {
                        Param param = (Param) _enum.nextElement();
                        String paramXsd = param.getParamValue();
                        if(paramXsd != null && param.getParamName() != null && param.getParamName().startsWith("External_"))
                        {
                            String xsdRef = populateSchema(paramXsd);
                            if(xsdRef != null)
                                param.setParamValue(xsdRef);
                        }
                    }
                }

            }
            Enumeration outPortsEnum = svcInstance.getPortInstDescriptor().getOutPorts();
            while(outPortsEnum != null && outPortsEnum.hasMoreElements())
            {
                PortInst portInst = (PortInst)outPortsEnum.nextElement();
                if (portInst.getXSD() != null && portInst.getXSD().trim().length() > 0)
                {
                    // check if the schema is the default error schema. if yes then dont write it
                    if(SchemaUtil.isDefaultErrorPortXSD(portInst.getXSD()))
                        continue;

                    String xsdRef = populateSchema(portInst.getXSD());
                    portInst.setXSDRef(xsdRef);
                }
                Vector params = portInst.getParameters();
                if (params != null && params.size() > 0)
                {
                    Enumeration _enum = params.elements();
                    while (_enum.hasMoreElements())
                    {
                        Param param = (Param) _enum.nextElement();
                        String paramXsd = param.getParamValue();
                        if(paramXsd != null && param.getParamName() != null && param.getParamName().startsWith("External_"))
                        {
                            String xsdRef = populateSchema(paramXsd);
                            if(xsdRef != null)
                                param.setParamValue(xsdRef);
                        }
                    }
                }

            }
        }
    }

    public String populateSchema(String schema)
    {
        for (int ctr=0; ctr < m_schemas.size(); ctr++)
        {
            String inSchema = (String)m_schemas.get(C_SCHEMA + ctr);
            if (inSchema.equals(schema))
            {
                return C_SCHEMA + ctr;
            }
        }
        String key = C_SCHEMA + m_schemas.size();
        m_schemas.put(key, schema);
        return key;
    }

    /**
     *  Returns fields using jaxp parser.
     *
     * @return String representation of XML.
     * @exception FioranoException thrown in case of error.
     */
    private String toJXMLString()
        throws FioranoException
    {
        com.fiorano.openesb.utils.DocumentFactoryImpl m_documentFactory = new DocumentFactoryImpl();
        Document document = m_documentFactory.createDocument();

        //  Start off the Application Node.
        Node root0 = document.createElement("Application");

        document.appendChild(root0);

        populateSchemas();

        //Add Schemas Node to it
        Node schemasElem = document.createElement("Schemas");
        root0.appendChild(schemasElem);

        Enumeration keysEnum = m_schemas.keys();
        while (keysEnum != null && keysEnum.hasMoreElements())
        {
            String key = (String)keysEnum.nextElement();
            Node schemaElem = document.createElement("Schema");
            ((Element)schemaElem).setAttribute("id", key);
            CDATASection cdata = document.createCDATASection((String)m_schemas.get(key));
            schemaElem.appendChild(cdata);
            schemasElem.appendChild(schemaElem);
        }

        //  Add the ApplicationHeader Node to it.
        if (m_appHeader != null)
        {
            Node headerNode = m_appHeader.toJXMLString(document);

            root0.appendChild(headerNode);
        }

        //  Add the ServicesInstances Node to it.
        if (m_servInstances != null)
        {
            Node serviceNode = m_servInstances.toJXMLString(document);

            root0.appendChild(serviceNode);
        }

        //  Add the WorkFlowStartPorts to it.
        if (m_workflowStartPorts != null)
        {
            Node startPorts = m_workflowStartPorts.toJXMLString(document);

            root0.appendChild(startPorts);
        }

        //  Add the WorkFlowExitPorts to it.
        if (m_workflowExitPorts != null)
        {
            Node exitPorts = m_workflowExitPorts.toJXMLString(document);

            root0.appendChild(exitPorts);
        }

        //  Add the Routes to it.
        if (m_routes != null)
        {
            Node routes = m_routes.toJXMLString(document);

            root0.appendChild(routes);
        }

        //  Add the OnException to it.
        if (m_onException != null)
        {
            Node onException = m_onException.toJXMLString(document);

            root0.appendChild(onException);
        }

        //  Add the layout info too to it.
        Node layoutInfo = document.createElement("Layout");
        Node pcData = document.createCDATASection(m_layoutInfo);

        layoutInfo.appendChild(pcData);
        root0.appendChild(layoutInfo);

        Properties options = new Properties();

        options.put(SerializerOptions.INDENT_STRING, "  ");
        options.put(SerializerOptions.OMIT_XML_DECLERATION, "yes");
        options.put(SerializerOptions.LINE_SEPARATOR,
            System.getProperty("line.separator"));

        return XMLUtils.serializeDocument(document, options);
    }

    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException,
      FioranoException {

        //  Start off the Application Node.
        writer.writeStartElement("Application");

        populateSchemas();
        //Add Schemas Node to it

        //Start Schemas
        writer.writeStartElement("Schemas");
        Enumeration keysEnum = m_schemas.keys();

        while (keysEnum != null && keysEnum.hasMoreElements())
        {
            String key = (String)keysEnum.nextElement();
            //Start Schema
            writer.writeStartElement("Schema");
            writer.writeAttribute("id", key);
            writer.writeCData((String)m_schemas.get(key));
            //End Schema
            writer.writeEndElement();
        }
        //End Schemas
        writer.writeEndElement();

        //  Add the ApplicationHeader Node to it.
        if (m_appHeader != null)
        {
            m_appHeader.toJXMLString(writer);
        }

        // Add the ServicesInstances Node to it
        if (m_servInstances != null)
        {
             m_servInstances.toJXMLString(writer);
        }

        // Add the WorkFlowStartPorts to it.
        if (m_workflowStartPorts != null)
        {
            m_workflowStartPorts.toJXMLString(writer);
        }

        // Add the WorkFlowExitPorts to it.
        if (m_workflowExitPorts != null)
        {
            m_workflowExitPorts.toJXMLString(writer);
        }

        // Add the Routes to it.
        if (m_routes != null)
        {
            m_routes.toJXMLString(writer);
        }


        // Add the OnException to it.
        if (m_onException != null)
        {
            m_onException.toJXMLString(writer);
        }

        // Add the layout info.
        // Write empty Layout element if m_layoutInfo is null.
        writer.writeStartElement("Layout");
        if(m_layoutInfo != null)
        	writer.writeCData(m_layoutInfo);
        writer.writeEndElement();

        // End the Application Node
        writer.writeEndElement();


        writer.flush();
        writer.close();

    }

}
