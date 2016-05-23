/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Properties;

public class ServicePropertySheet extends DmiObject
{
    ServiceHeader   m_serviceHeader;

    Deployment      m_deployment;

    Execution       m_execution;

    PortDescriptor  m_portDescriptor;

    String          m_strOnKillServiceGUID;

    String          m_strOnKillServiceVersion;

    Security        m_security;


    /**
     *  Constructor for the ServicePropertySheet object
     *
     * @since Tifosi2.0
     */
    public ServicePropertySheet()
    {
    }


    /**
     *  This interface method is called to get the object of <code>ServiceHeader</code>
     *  containing header information of service for which, this is object of
     *  <code>ServicePropertySheet</code>.
     *
     * @return object of ServiceHeader
     * @see #setServiceHeader(ServiceHeader)
     * @since Tifosi2.0
     */
    public ServiceHeader getServiceHeader()
    {
        return m_serviceHeader;
    }


    /**
     *  This interface method is called to get the deployment information, contained
     *  in object of <code>Deployment</code>, about the service for which, this
     *  is object of <code>ServicePropertySheet</code>.
     *
     * @return object of DeploymentInfo
     * @see #setDeploymentInfo(Deployment)
     * @since Tifosi2.0
     */
    public Deployment getDeploymentInfo()
    {
        return m_deployment;
    }


    /**
     *  This interface method is called to get the execution information as object of
     *  <code>Execution</code>, about the service for which, this is object of
     *  <code>ServicePropertySheet</code>. Execution information include the
     *  type of execution, executable name, execution directory etc.
     *
     * @return object of Execution.
     * @see #setExecutionInfo(Execution)
     * @since Tifosi2.0
     */
    public Execution getExecutionInfo()
    {
        return m_execution;
    }


    /**
     *  This interface method is called to get the port information as object of
     *  <code>PortDescriptor</code>, about the service for which, this is object of
     *  <code>ServicePropertySheet</code>. PortDescriptor contains information
     *  about all the input and output ports for this service.
     *
     * @return object of PortDescriptor
     * @see #setPortDescriptor(PortDescriptor)
     * @since Tifosi2.0
     */
    public PortDescriptor getPortDescriptor()
    {
        return m_portDescriptor;
    }


    /**
     *  This interface method is called to get GUID of the service which is to be launched
     *  on termination of instance of the service for which, this is object of
     *  <code>ServicePropertySheet</code>.
     *
     * @return onKillServiceGUID value
     * @see #setOnKillServiceGUID(String)
     * @since Tifosi2.0
     */
    public String getOnKillServiceGUID()
    {
        return m_strOnKillServiceGUID;
    }


    /**
     *  This interface method is called to get Version of the service which is
     *  to be launched on termination of instance of the service for which, this
     *  is object of <code>ServicePropertySheet</code>.
     *
     * @return version of onKillService
     * @see #setOnKillServiceVersion(String)
     * @since Tifosi2.0
     */
    public String getOnKillServiceVersion()
    {
        return m_strOnKillServiceVersion;
    }


    /**
     *  This interface method is called to get the security information specified by
     *  object of <code>Security</code>, for the service for which, this is object of
     *  <code>ServicePropertySheet</code>.
     *
     * @return object of Security
     * @see #setSecurityInfo(Security)
     * @since Tifosi2.0
     */
    public Security getSecurityInfo()
    {
        return m_security;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_PROPERTIES;
    }


    /**
     *  This interface method is called to set the specified object
     *  of <code>ServiceHeader</code> containing header information of the service
     *  for which, this is object of <code>ServicePropertySheet</code>.
     *
     * @param serviceHeader object of ServiceHeader
     * @see #getServiceHeader()
     * @since Tifosi2.0
     */
    public void setServiceHeader(ServiceHeader serviceHeader)
    {
        m_serviceHeader = serviceHeader;
    }


    /**
     *  This interface method is called to set the deployment information, contained
     *  in specified object of <code>Deployment</code>, about the service for which,
     *  this is object of <code>ServicePropertySheet</code>.
     *
     * @param deployment object of DeploymentInfo
     * @see #getDeploymentInfo()
     * @since Tifosi2.0
     */
    public void setDeploymentInfo(Deployment deployment)
    {
        m_deployment = deployment;
    }


    /**
     *  This interface method is called to set the execution information specified
     *  by object of <code>Execution</code>, about the service for which, this
     *  is object of <code>ServicePropertySheet</code>.
     *  Execution information includes the type of execution, executable name,
     *  execution directory etc.
     *
     * @param execution Object of Execution
     * @see #getExecutionInfo()
     * @since Tifosi2.0
     */
    public void setExecutionInfo(Execution execution)
    {
        m_execution = execution;
    }


    /**
     *  This interface method is called to set the channel information, specified
     *  as object of <code>PortDescriptor</code>, for this service instance.
     *  PortDescriptor contains information about all the input and output ports
     *  for this service.
     *
     * @param portDesc object of PortDescriptor
     * @see #getPortDescriptor()
     * @since Tifosi2.0
     */
    public void setPortDescriptor(PortDescriptor portDesc)
    {
        m_portDescriptor = portDesc;
    }


    /**
     *  This interface method is called to set specified string as GUID of the service
     *  which has to be launched on the termination of the service instance to which
     *  this is object of <code>ServicePropertySheet</code> belongs.
     *
     * @param guid string to be set as onKillServiceGUID
     * @see #getOnKillServiceGUID()
     * @since Tifosi2.0
     */
    public void setOnKillServiceGUID(String guid)
    {
        m_strOnKillServiceGUID = guid;
    }


    /**
     *  This interface method is called to set the specified string as Version of
     *  the service which is to be launched on termination of instance of the service
     *  for which, this is object of <code>ServicePropertySheet</code>.
     *
     * @param version string to be set as version of onKillService
     * @see #getOnKillServiceVersion()
     * @since Tifosi2.0
     */
    public void setOnKillServiceVersion(String version)
    {
        m_strOnKillServiceVersion = version;
    }


    /**
     *  This interface method is called to set the security information specified by
     *  object of <code>Security</code>, for the service for which, this is object of
     *  <code>ServicePropertySheet</code>.
     *
     * @param sec object of Security to be set
     * @see #getSecurityInfo()
     * @since Tifosi2.0
     */
    public void setSecurityInfo(Security sec)
    {
        m_security = sec;
    }


    /**
     * Sets field values for object
     *
     * @param xmlElement
     * @exception FioranoException
     */
    public void setFieldValues(Element xmlElement)
        throws FioranoException
    {
        TreeWalker tree = new TreeWalker(xmlElement);

        // service header
        Element serviceHdrElement = tree.getNextElement("ServiceHeader");

        m_serviceHeader = new ServiceHeader();
        if (serviceHdrElement != null)
            m_serviceHeader.setFieldValues(serviceHdrElement);

        // deployment info
        tree.reset();

        Element depElement = tree.getNextElement("Deployment");

        if (depElement != null)
        {
            m_deployment = new Deployment();
            m_deployment.setFieldValues(depElement);
        }

        // execution
        tree.reset();

        Element execElement = tree.getNextElement("Execution");

        if (execElement != null)
        {
            m_execution = new Execution();
            m_execution.setFieldValues(execElement);

            if (m_execution.getExecType().equalsIgnoreCase("java"))
            {
                RuntimeArg arg = new RuntimeArg();

                arg.setArgName("JVM_PARAMS");
                arg.setType("Text");
                arg.setRequired(false);
                arg.setInMemorySupported(false);
                m_execution.addRuntimeArg(arg);
            }
        }

        // port descriptor
        tree.reset();

        Element portDescElement = tree.getNextElement("PortDescriptor");

        if (portDescElement != null)
        {
            m_portDescriptor = new PortDescriptor();
            m_portDescriptor.setFieldValues(portDescElement);
        }

        // OnServiceKill
        tree.reset();

        Node serviceKillNode = (Node) tree.getNextElement("OnServiceKill");

        if (serviceKillNode != null)
        {
            NodeList serviceGUIDNodeList = serviceKillNode.getChildNodes();
            Node child = null;

            for (int i = 0; i < serviceGUIDNodeList.getLength(); ++i)
            {
                child = serviceGUIDNodeList.item(i);

                String nodeName = child.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(child);

                if (nodeName.equalsIgnoreCase("ServiceGUID"))
                    m_strOnKillServiceGUID = nodeValue;

                if (nodeName.equalsIgnoreCase("Version"))
                    m_strOnKillServiceVersion = nodeValue;

            }
        }

        // security
        tree.reset();

        Element secElement = tree.getNextElement("SecurityAttributes");

        m_security = new Security();
        if (secElement != null)
            m_security.setFieldValues(secElement);

        validate();
    }

    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>ServicePropertySheet</code>, using the specified XML string.
     *
     * @param xmlString XML String.
     * @param versionNo
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(String xmlString, int versionNo)
        throws FioranoException
    {
        try
        {
            Document root = XMLUtils.getDOMDocumentFromXML(xmlString);

            setFieldValues(root.getDocumentElement());
        }
        catch (Exception e)
        {
            if (m_serviceHeader != null)
                throw new FioranoException(e);

            else
                throw new FioranoException(e);
        }
    }

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
     *  This method tests whether this object of <code>ServicePropertySheet</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_serviceHeader == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        m_serviceHeader.validate();
        if (m_deployment != null)
            m_deployment.validate();

        if (m_execution != null)
            m_execution.validate();

        if (m_portDescriptor != null)
            m_portDescriptor.validate();

        if (m_security != null)
            m_security.validate();

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
     *  This utility method is used to get the XML String representation for this object
     *  of <code>ServicePropertySheet</code>.
     *
     * @param versionNo
     * @return XML String for this object
     * @exception FioranoException if the calls fail to succeed.
     * @since Tifosi2.0
     */
    public String toXMLString(int versionNo)
        throws FioranoException
    {
        com.fiorano.openesb.utils.DocumentFactoryImpl m_documentFactory = new DocumentFactoryImpl();
        Document document = m_documentFactory.createDocument();

        Node root0 = (Node) document.createElement("ServiceDescriptor");

        document.appendChild(root0);

        Node serviceHeader = m_serviceHeader.toJXMLString(document);

        root0.appendChild(serviceHeader);

        if (m_deployment != null)
        {
            Node deployment = m_deployment.toJXMLString(document);

            root0.appendChild(deployment);
        }

        if (m_execution != null)
        {
            Node execution = m_execution.toJXMLString(document);

            root0.appendChild(execution);
        }

        if (m_portDescriptor != null)
        {
            Node portDesc = m_portDescriptor.toJXMLString(document);

            root0.appendChild(portDesc);
        }

        Node childNode1 = XMLDmiUtil.getNodeObject("ServiceGUID",
            m_strOnKillServiceGUID, document);
        Node childNode2 = XMLDmiUtil.getNodeObject("Version",
            m_strOnKillServiceVersion, document);

        if (childNode1 != null && childNode2 != null)
        {
            Node onServiceKillNode = (Node) document.createElement("OnServiceKill");

            onServiceKillNode.appendChild(childNode1);
            onServiceKillNode.appendChild(childNode2);
            root0.appendChild(onServiceKillNode);
        }

        if (m_security != null)
        {
            Node security = m_security.toJXMLString(document);

            root0.appendChild(security);
        }

        Properties options = new Properties();

        options.put(SerializerOptions.INDENT_STRING, "  ");
        options.put(SerializerOptions.OMIT_XML_DECLERATION, "yes");
        options.put(SerializerOptions.LINE_SEPARATOR,
            System.getProperty("line.separator"));

        return XMLUtils.serializeDocument(document, options);
    }


    /**
     *  This method is called to write this object of <code>ServicePropertySheet</code>
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

        if (m_deployment != null)
        {
            out.writeInt(1);
            m_deployment.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_execution != null)
        {
            out.writeInt(1);
            m_execution.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_portDescriptor != null)
        {
            out.writeInt(1);
            m_portDescriptor.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_security != null)
        {
            out.writeInt(1);
            m_security.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        if (m_serviceHeader != null)
        {
            out.writeInt(1);
            m_serviceHeader.toStream(out, versionNo);
        }
        else
            out.writeInt(0);

        writeUTF(out, m_strOnKillServiceGUID);
        writeUTF(out, m_strOnKillServiceVersion);
    }


    /**
     *  This is called to read this object <code>ServicePropertySheet</code>
     *  from the specified object of input stream.
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

        int isNotNull = is.readInt();

        if (isNotNull == 1)
        {
            m_deployment = new Deployment();
            m_deployment.fromStream(is, versionNo);
        }

        isNotNull = is.readInt();
        if (isNotNull == 1)
        {
            m_execution = new Execution();
            m_execution.fromStream(is, versionNo);

            if (m_execution.getExecType().equalsIgnoreCase("java"))
            {
                RuntimeArg arg = new RuntimeArg();

                arg.setArgName("JVM_PARAMS");
                arg.setType("Text");
                arg.setRequired(false);
                m_execution.addRuntimeArg(arg);
            }
        }

        isNotNull = is.readInt();
        if (isNotNull == 1)
        {
            m_portDescriptor = new PortDescriptor();
            m_portDescriptor.fromStream(is, versionNo);
        }

        isNotNull = is.readInt();
        if (isNotNull == 1)
        {
            m_security = new Security();
            m_security.fromStream(is, versionNo);
        }

        isNotNull = is.readInt();
        if (isNotNull == 1)
        {
            m_serviceHeader = new ServiceHeader();
            m_serviceHeader.fromStream(is, versionNo);
        }

        m_strOnKillServiceGUID = readUTF(is);
        m_strOnKillServiceVersion = readUTF(is);
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>ServicePropertySheet</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof ServicePropertySheet))
            return false;

        ServicePropertySheet rcvObj = (ServicePropertySheet) obj;

        if (rcvObj.getDeploymentInfo().equals(m_deployment)
            && rcvObj.getExecutionInfo().equals(m_execution)
            && rcvObj.getPortDescriptor().equals(m_portDescriptor)
            && rcvObj.getSecurityInfo().equals(m_security)
            && rcvObj.getServiceHeader().equals(m_serviceHeader)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getOnKillServiceGUID(), m_strOnKillServiceGUID)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getOnKillServiceVersion(), m_strOnKillServiceVersion))
            return true;
        else
            return false;
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>ServicePropertySheet</code>.
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
        strBuf.append("Service Property Sheet Details ");
        strBuf.append("[");
        if (m_serviceHeader != null)
        {
            strBuf.append("Service Header = ");
            strBuf.append(m_serviceHeader.toString());
            strBuf.append(", ");
        }
        if (m_execution != null)
        {
            strBuf.append("Execution = ");
            strBuf.append(m_execution.toString());
            strBuf.append(", ");
        }
        if (m_deployment != null)
        {
            strBuf.append("Deployment info = ");
            strBuf.append(m_deployment.toString());
            strBuf.append(", ");
        }
        if (m_portDescriptor != null)
        {
            strBuf.append("Port Descriptor = ");
            strBuf.append(m_portDescriptor.toString());
            strBuf.append(", ");
        }
        if (m_security != null)
        {
            strBuf.append("Security = ");
            strBuf.append(m_security.toString());
            strBuf.append(", ");
        }
        strBuf.append("On service kill GUID = ");
        strBuf.append(m_strOnKillServiceGUID);
        strBuf.append(", ");
        strBuf.append("on service kill Version = ");
        strBuf.append(m_strOnKillServiceVersion);
        strBuf.append("]");
        return strBuf.toString();
    }

}
