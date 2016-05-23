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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Deployment extends DmiObject
{

    /**
     * Constant specifying the Production Stage for the service
     */
    public final static String PRODUCT = "PRODUCTION";

    /**
     * Constant specifying the QA Stage for the service
     */
    public final static String QA = "QA";

    /**
     * Constant specifying the Development Stage for the service
     */
    public final static String DEVELOPMENT = "DEVELOPMENT";
    // whether the service is auto installable
    boolean         m_bIsAutoInstallable;
    // compatible to which platforms
    Vector          m_strApplicableOS = new Vector();
    // services on which this service is dependant
    Vector          m_servDependencies = new Vector();
    //contains ResourceInfo
    Vector          m_resources = new Vector();
    // deployment stage : can be one of these :
    // Development/QA/Staging or Product
    // by default, the stage is Product
    private String  m_strDeploymentStage;
    private static final String PRODUCT_OLD = "PRODUCT";


    /**
     *  Constructor for the DeploymentInfo object
     *
     * @since Tifosi2.0
     */
    public Deployment()
    {
        reset();
    }


    /**
     * This interface method is called to check whether the service about which this
     * object of <code>Deployment</code> represents deployment information, is
     * auto installable or not.
     *
     * @return true if service is auto installable, false otherwise.
     * @see #setAutoInstallable(boolean)
     * @since Tifosi2.0
     */
    public boolean isAutoInstallable()
    {
        return m_bIsAutoInstallable;
    }

    /**
     * This method is called to set the deployment stage, specifying the stage of
     * the service about which this object of <code>Deployment</code> represents
     * deployment information.
     *
     * @return String specifying the deployment stage of the service.
     * @see #setDeploymentStage(String)
     * @since Tifosi2.0
     */
    public String getDeploymentStage()
    {
        return m_strDeploymentStage;
    }


    /**
     *  This interface method is called to get enumeration of the list of applicable
     *  operating systems, for the service about which this object of
     *  <code>Deployment</code> represents deploy information.
     *
     * @return Enumeration of list of ApplicableOperatingSystems
     * @see #addApplicableOperatingSystem(String)
     * @see #removeApplicableOperatingSystem(String)
     * @see #clearApplicableOperatingSystem()
     * @since Tifosi2.0
     */
    public Enumeration getApplicableOperatingSystems()
    {
        if (m_strApplicableOS == null)
        {
            m_strApplicableOS = new Vector();
        }
        return m_strApplicableOS.elements();
    }


    /**
     *  This interface method is for enumeration containing all service dependencies, for
     *  the service about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @return enumeration containing the service dependencies
     * @see #addServiceDependency(ServiceDependency )
     * @see #removeServiceDependency(ServiceDependency)
     * @see #clearServiceDependency()
     * @since Tifosi2.0
     */
    public Enumeration getServiceDependencies()
    {
        return m_servDependencies.elements();
    }


    /**
     *  This interface method is called to get enumeration of resources, for the service
     *  about which this object of <code>Deployment</code> represents deploy information.
     *
     * @return Enumeration containing all the resources
     * @see #addResource(Resource)
     * @see #removeResource(Resource)
     * @see #removeAllResources()
     * @see #clearResources()
     * @since Tifosi2.0
     */
    public Enumeration getResources()
    {
        if (m_resources == null)
        {
            m_resources = new Vector();
        }
        return m_resources.elements();
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.DEPLOYMENT;
    }


    /**
     * This interface method is called to set the boolean, specifying whether the service
     * about which this object of <code>Deployment</code> represents deployment
     * information, is auto installable or not.
     *
     * @param autoInstallable boolean specifying whether service is
     *                             auto installable or not.
     * @see #isAutoInstallable()
     * @since Tifosi2.0
     */
    public void setAutoInstallable(boolean autoInstallable)
    {
        m_bIsAutoInstallable = autoInstallable;
    }

    /**
     * This method is called to set the deployment stage, specifying the stage of
     * the service about which this object of <code>Deployment</code> represents
     * deployment information.
     *
     * @param deploymentStage String specifying the deployment stage of the service.
     * @see #getDeploymentStage()
     * @since Tifosi2.0
     */
    public void setDeploymentStage(String deploymentStage)
    {
        m_strDeploymentStage = deploymentStage;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>Deployment</code>, using the specified XML string.
     *
     * @param depElement <code>Element</code> object which has the value
     *    of the deployment object
     * @exception FioranoException if an error occurs while parsing the
     *                             depElement
     * @since Tifosi2.0
     */
    public void setFieldValues(Element depElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element depElement = doc.getDocumentElement();
        if (depElement != null)
        {
            m_bIsAutoInstallable = XMLDmiUtil.getAttributeAsBoolean(depElement,
                "isAutoInstallable");

            m_strDeploymentStage = depElement.getAttribute("deploymentStage");

            if(m_strDeploymentStage.equalsIgnoreCase(PRODUCT_OLD))
                m_strDeploymentStage = PRODUCT;

            if (m_strDeploymentStage == null || m_strDeploymentStage.trim().equals(""))
                m_strDeploymentStage = PRODUCT;

            NodeList children = depElement.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("OS"))
                {
                    addApplicableOperatingSystem(XMLUtils.getNodeValueAsString(child));
                }

                if (nodeName.equalsIgnoreCase("ServiceDependency"))
                {
                    ServiceDependency serDep = new ServiceDependency();

                    serDep.setFieldValues((Element) child);
                    addServiceDependency(serDep);
                }

                if (nodeName.equalsIgnoreCase("Resources"))
                {
                    NodeList resList = child.getChildNodes();
                    Node resNode = null;

                    for (int j = 0; resList != null && j < resList.getLength(); ++j)
                    {
                        resNode = resList.item(j);
                        if (resNode.getNodeType() == resNode.TEXT_NODE)
                        {
                            continue;
                        }

                        Resource resource = new Resource();

                        resource.setFieldValues((Element) resNode);
                        addResource(resource);
                    }
                }
            }
        }
        validate();
    }


    /**
     *  This interface method is called to add the specified string to the list of
     *  applicable operating systems, for the service about which this
     *  object of <code>Deployment</code> represents deploy information.
     *
     * @param opSys The string to be added to the list of ApplicableOperatingSystem
     * @see #removeApplicableOperatingSystem(String)
     * @see #clearApplicableOperatingSystem()
     * @see #getApplicableOperatingSystems()
     * @since Tifosi2.0
     */
    public void addApplicableOperatingSystem(String opSys)
    {
        if (m_strApplicableOS == null)
        {
            m_strApplicableOS = new Vector();
        }
        if (!m_strApplicableOS.contains(opSys))
            m_strApplicableOS.add(opSys);
    }


    /**
     *  This interface method is called to remove the specified string from the list of
     *  applicable operating systems, for the service about which this
     *  object of <code>Deployment</code> represents deploy information.
     *
     * @param os the string to be removed from list of ApplicableOperatingSystem
     * @see #addApplicableOperatingSystem(String)
     * @see #clearApplicableOperatingSystem()
     * @see #getApplicableOperatingSystems()
     * @since Tifosi2.0
     */
    public void removeApplicableOperatingSystem(String os)
    {
        if (m_strApplicableOS != null)
        {
            m_strApplicableOS.remove(os);
        }
    }


    /**
     *  This interface method is called to clear the list of applicable operating systems,
     *  for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @see #addApplicableOperatingSystem(String)
     * @see #removeApplicableOperatingSystem(String)
     * @see #getApplicableOperatingSystems()
     * @since Tifosi2.0
     */
    public void clearApplicableOperatingSystem()
    {
        if (m_strApplicableOS != null)
        {
            m_strApplicableOS.clear();
        }
    }


    /**
     *  This interface method is called to add the specified object
     *  of <code>ServiceDependency</code> to the list of service dependencies.
     *  This is for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @param servDependencyInfo object of  ServiceDependency to be added.
     * @see #removeServiceDependency(ServiceDependency)
     * @see #clearServiceDependency()
     * @see #getServiceDependencies()
     * @since Tifosi2.0
     */
    public void addServiceDependency(ServiceDependency servDependencyInfo)
    {
        if (m_servDependencies == null)
        {
            m_servDependencies = new Vector();
        }
        if (!m_servDependencies.contains(servDependencyInfo))
            m_servDependencies.add(servDependencyInfo);
    }


    /**
     *  This interface method is called to remove the specified object
     *  of <code>ServiceDependency</code> from the list of service dependencies.
     *  This is for the service about which this object of <code>Deployment</code>
     *  represents deploy information.
     *
     * @param serv object of  ServiceDependency to be removed.
     * @see #addServiceDependency(ServiceDependency )
     * @see #clearServiceDependency()
     * @see #getServiceDependencies()
     * @since Tifosi2.0
     */
    public void removeServiceDependency(ServiceDependency serv)
    {
        if (m_servDependencies != null)
        {
            m_servDependencies.remove(serv);
        }
    }


    /**
     *  This interface method is called to clear the list of service dependencies, for
     *  the service about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @see #addServiceDependency(ServiceDependency )
     * @see #removeServiceDependency(ServiceDependency)
     * @see #getServiceDependencies()
     * @since Tifosi2.0
     */
    public void clearServiceDependency()
    {
        if (m_servDependencies != null)
        {
            m_servDependencies.clear();
        }
    }


    /**
     *  This interface method is called to add the specified object
     *  of <code>Resource</code> to the list of resources, for the service about
     *  which this object of <code>Deployment</code> represents deploy information.
     *
     * @param resource The object of Resource to be added.
     * @see #removeResource(Resource)
     * @see #removeAllResources()
     * @see #clearResources()
     * @see #getResources()
     * @since Tifosi2.0
     */
    public void addResource(Resource resource)
    {
        if (m_resources == null)
        {
            m_resources = new Vector();
        }
        if (!m_resources.contains(resource))
            m_resources.addElement(resource);
    }


    /**
     *  This interface method is called to remove the specified object
     *  of <code>Resource</code> from the list of resources. This is for the service
     *  about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @param res the object of Resource to be removed.
     * @see #addResource(Resource)
     * @see #removeAllResources()
     * @see #clearResources()
     * @see #getResources()
     * @since Tifosi2.0
     */
    public void removeResource(Resource res)
    {
        if (m_resources != null)
        {
            m_resources.remove(res);
        }
    }

    /**
     *  This interface method is called to remove all the objects
     *  of <code>Resource</code> from the list of resources. This is for the service
     *  about which this object of <code>Deployment</code> represents
     *  deploy information.
     *
     * @see #addResource(Resource)
     * @see #removeResource(Resource)
     * @see #clearResources()
     * @see #getResources()
     * @since Tifosi2.0
     */
    public void removeAllResources()
    {
        if (m_resources != null)
        {
            m_resources.clear();
        }
    }


    /**
     *  This interface method is called to clear the list of resources, for the service
     *  about which this object of <code>Deployment</code> represents deploy information
     *
     * @see #addResource(Resource)
     * @see #removeResource(Resource)
     * @see #removeAllResources()
     * @see #getResources()
     * @since Tifosi2.0
     */
    public void clearResources()
    {
        if (m_resources != null)
        {
            m_resources.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>Deployment</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        Enumeration _enum = m_resources.elements();

        while (_enum.hasMoreElements())
        {
            Resource res = (Resource) _enum.nextElement();

            res.validate();
        }

        if (m_servDependencies != null)
        {
            _enum = m_servDependencies.elements();
            while (_enum.hasMoreElements())
            {
                ServiceDependency serv = (ServiceDependency) _enum.nextElement();

                serv.validate();
            }
        }
        if (!m_strDeploymentStage.equalsIgnoreCase(PRODUCT)
            && !m_strDeploymentStage.equalsIgnoreCase(QA)
            && !m_strDeploymentStage.equalsIgnoreCase(DEVELOPMENT))
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
    }


    /**
     *  Resets the values of the data members of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_resources = new Vector();
        m_servDependencies = new Vector();
        m_strApplicableOS = new Vector();
        m_bIsAutoInstallable = true;
        m_strDeploymentStage = PRODUCT;
    }


    /**
     *  This utility method is used to get XML String representation for this object
     *  of <code>Deployment</code>.
     *
     * @return XML String for this object
     * @exception FioranoException if the calls fails to succeed.
     * @since Tifosi2.0
     */
    public String toXMLString()
        throws FioranoException
    {
        com.fiorano.openesb.utils.DocumentFactoryImpl documentFactory =
            new DocumentFactoryImpl();
        Document document = documentFactory.createDocument();

        return XMLUtils.serializeDocument(toJXMLString(document));
    }


    /**
     *  This method is called to write this object of <code>Deployment</code>
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

        out.writeBoolean(m_bIsAutoInstallable);
        writeUTF(out, m_strDeploymentStage);

        // Applicable OS
        if (m_strApplicableOS != null && m_strApplicableOS.size() > 0)
        {
            int num = m_strApplicableOS.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                writeUTF(out, (String) m_strApplicableOS.elementAt(i));
            }
        }
        else
        {
            out.writeInt(0);
        }

        // Service Dependency
        if (m_servDependencies != null && m_servDependencies.size() > 0)
        {
            int num = m_servDependencies.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                ServiceDependency serv = (ServiceDependency) m_servDependencies.elementAt(i);

                serv.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        // Resource Info
        if (m_resources != null && m_resources.size() > 0)
        {
            int num = m_resources.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                Resource res = (Resource) m_resources.elementAt(i);

                res.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>Deployment</code>
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

        m_bIsAutoInstallable = is.readBoolean();
        m_strDeploymentStage = readUTF(is);
        if (m_strDeploymentStage == null)
            m_strDeploymentStage = PRODUCT;

        // Applicable OS
        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            String name = readUTF(is);

            m_strApplicableOS.addElement(name);
        }

        // Service Dependency
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            ServiceDependency serv = new ServiceDependency();

            serv.fromStream(is, versionNo);
            m_servDependencies.addElement(serv);
        }

        // Resource Info
        size = is.readInt();
        for (int i = 0; i < size; ++i)
        {
            Resource res = new Resource();

            res.fromStream(is, versionNo);
            m_resources.addElement(res);
        }
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>Deployment</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Deployment))
        {
            return false;
        }

        Deployment rcvObj = (Deployment) obj;

        if (rcvObj.getApplicableOperatingSystems().equals(getApplicableOperatingSystems())
            && rcvObj.getResources().equals(getResources())
            && rcvObj.getServiceDependencies().equals(getServiceDependencies())
            && DmiEqualsUtil.checkStringEquality(rcvObj.getDeploymentStage(), m_strDeploymentStage))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>Deployment</code>.
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
        strBuf.append("Deployment Details ");
        strBuf.append("[");
        strBuf.append("is auto installable = ");
        strBuf.append(m_bIsAutoInstallable);
        strBuf.append(", ");
        strBuf.append("Deployment Stage = ");
        strBuf.append(m_strDeploymentStage);
        strBuf.append(", ");
        if (m_resources != null)
        {
            strBuf.append("Resources = ");
            for (int i = 0; i < m_resources.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((Resource) m_resources.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_servDependencies != null)
        {
            strBuf.append("Service dependencies = ");
            for (int i = 0; i < m_servDependencies.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((ServiceDependency) m_servDependencies.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        if (m_strApplicableOS != null)
        {
            strBuf.append("Applicable OS = ");
            for (int i = 0; i < m_strApplicableOS.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append((String) m_strApplicableOS.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Deployment");

        ((Element) root0).setAttribute("isAutoInstallable",
            "" + m_bIsAutoInstallable);
        if (m_strDeploymentStage != null && !m_strDeploymentStage.trim().equals(""))
            ((Element) root0).setAttribute("deploymentStage", m_strDeploymentStage);

        // adding authors
        XMLDmiUtil.addVectorValues("OS", m_strApplicableOS, document, root0);

        if (m_servDependencies != null && m_servDependencies.size() > 0)
        {
            Enumeration _enum = m_servDependencies.elements();

            while (_enum.hasMoreElements())
            {
                ServiceDependency serviceDependency = (ServiceDependency) _enum.nextElement();
                Node serviceDepNode = serviceDependency.toJXMLString(document);

                root0.appendChild(serviceDepNode);
            }
        }
        if (m_resources != null && m_resources.size() > 0)
        {
            Enumeration _enum = m_resources.elements();
            Node resources = document.createElement("Resources");

            while (_enum.hasMoreElements())
            {
                Resource resource = (Resource) _enum.nextElement();
                Node resNode = resource.toJXMLString(document);

                resources.appendChild(resNode);
            }
            root0.appendChild(resources);
        }

        return root0;
    }

}
