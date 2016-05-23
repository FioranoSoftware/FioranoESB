/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ServiceInstances extends DmiObject
{
    Vector          m_serviceInstances = new Vector();

    Vector          m_extServiceInstances = new Vector();


    /**
     *  This is the constructor of the <code>ServiceInstances</code> class.
     *
     * @since Tifosi2.0
     */
    public ServiceInstances()
    {
    }


    /**
     *  This method gets enumeration of all the service instances
     *  contained in this object of <code>ServiceInstances</code>.
     *
     * @return Enumeration of objects of ServiceInstance
     * @see #addServiceInstance(ServiceInstance)
     * @see #getExtServiceInstances()
     * @see #addExtServiceInstance(ExternalServiceInstance)
     * @since Tifosi2.0
     */
    public Enumeration getServiceInstances()
    {
        return m_serviceInstances.elements();
    }

    public ServiceInstance getServiceInstance(String instName){
        Enumeration insts = getServiceInstances();
        while(insts.hasMoreElements()){
            ServiceInstance inst = (ServiceInstance)insts.nextElement();
            if(inst.getServiceInstName().equals(instName))
                return inst;
        }
        return null;
    }

    /**
     *  This method gets enumeration of all the externally launched
     *  service instances, contained in this object of <code>ServiceInstances</code>.
     *
     * @return Enumeration of all ExternalServiceInstance objects
     * @see #addExtServiceInstance(ExternalServiceInstance)
     * @see #addServiceInstance(ServiceInstance)
     * @see #getServiceInstances()
     * @since Tifosi2.0
     */
    public Enumeration getExtServiceInstances()
    {
        return m_extServiceInstances.elements();
    }


    /**
     *  This method gets the total number of service instances contained in this
     *  object of <code>ServiceInstances</code>.
     *
     * @return Total number of ServiceInstances.
     * @see #getExtServiceInstCount()
     * @since Tifosi2.0
     */
    public int getServiceInstCount()
    {
        return m_serviceInstances.size();
    }


    /**
     *  This method gets the total number of externally launched service instances
     *  contained in this object of <code>ServiceInstances</code>.
     *
     * @return Total number of  ExternalServiceInstance
     * @see #getServiceInstCount()
     * @since Tifosi2.0
     */
    public int getExtServiceInstCount()
    {
        return m_extServiceInstances.size();
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of the object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_INSTANCES;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>ServiceInstances</code>, using the specified XML string.
     *
     * @param serInstancesElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element serInstancesElement)
        throws FioranoException
    {
        //Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
        //Element serInstancesElement = doc.getDocumentElement();

        if (serInstancesElement != null)
        {
            NodeList list = serInstancesElement.getChildNodes();

            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("ServiceInstance"))
                {
                    ServiceInstance serInstance = new ServiceInstance();

                    serInstance.setFieldValues((Element) child);
                    m_serviceInstances.add(serInstance);
                }
                else if (nodeName.equalsIgnoreCase("ExternalServiceInstance"))
                {
                    ExternalServiceInstance extInstance = new ExternalServiceInstance();

                    extInstance.setFieldValues((Element) child);
                    m_extServiceInstances.add(extInstance);
                }
            }
        }

        validate();
    }

    protected void populate(FioranoStaxParser parser)throws XMLStreamException,FioranoException
    {
        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if ( parser.markCursor(APSConstants.SERVICE_INSTANCES) )
        {
        // Get Attributes if needs to accessed later. This MUST be done before accessing any data of element.
            Hashtable attributes = parser.getAttributes();

            // Get Child Elements
            while (parser.nextElement())
            {
                String nodeName = parser.getLocalName();

                if (nodeName.equalsIgnoreCase(APSConstants.SERVICE_INSTANCE))
                {
                    ServiceInstance serInstance = new ServiceInstance();
                    serInstance.setFieldValues(parser);
                    m_serviceInstances.add(serInstance);
                }
                else if (nodeName.equalsIgnoreCase(APSConstants.EXTERNAL_SERVICE_INSTANCE))
                {
                    ExternalServiceInstance extInstance = new ExternalServiceInstance();

                    extInstance.setFieldValues(parser);
                    m_extServiceInstances.add(extInstance);
                }


            }

            if ( attributes != null)
            {
                attributes.clear();
                attributes = null;
            }


        }
        validate();
    }

    /**
     *  This method adds the specified object of
     *  <code>ServiceInstance</code> to this object of
     *  <code>ServiceInstances</code>.
     *
     * @param servInst The feature to be added to the ServiceInstance attribute
     * @see #getServiceInstances()
     * @see #getExtServiceInstances()
     * @see #addExtServiceInstance(ExternalServiceInstance)
     * @since Tifosi2.0
     */
    public void addServiceInstance(ServiceInstance servInst)
    {
        Enumeration enums = m_serviceInstances.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            ServiceInstance inst = (ServiceInstance) enums.nextElement();
            String nameOfServInst = inst.getServiceInstName();

            if (nameOfServInst.equalsIgnoreCase(servInst.getServiceInstName()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_serviceInstances.add(servInst);
    }

    /**
     *  This method removes the specified object of
     *  <code>ServiceInstance</code> to this object of
     *  <code>ServiceInstances</code>.
     *
     * @param servInst The object to be removed to the ServiceInstance attribute
     * @since Tifosi2.0
     */
    public void removeServiceInstance(ServiceInstance servInst)
    {
        m_serviceInstances.remove(servInst);
    }


    /**
     *  This method adds the specified object of
     *  <code>ExternalServiceInstance</code> to this object of
     *  <code>ServiceInstances</code>. <code>ExternalServiceInstance</code>
     *  contains complete information about the externally launched service instances.
     *
     * @param extServInst object of ExtServiceInstance to be added
     * @see #getExtServiceInstances()
     * @see #addServiceInstance(ServiceInstance)
     * @see #getServiceInstances()
     * @since Tifosi2.0
     */
    public void addExtServiceInstance(ExternalServiceInstance extServInst)
    {
        m_extServiceInstances.add(extServInst);
    }


    /**
     *  This method resets the values of the data members of this object.
     *  Not supported in this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method tests whether this object of <code>ServiceInstances</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_serviceInstances != null)
        {
            Enumeration enums = m_serviceInstances.elements();

            while (enums.hasMoreElements())
            {
                ServiceInstance inst = (ServiceInstance) enums.nextElement();

                inst.validate();
            }
        }

        if (m_extServiceInstances != null)
        {
            Enumeration _enum = m_extServiceInstances.elements();

            while (_enum.hasMoreElements())
            {
                ExternalServiceInstance instance = (ExternalServiceInstance) _enum.nextElement();

                instance.validate();
            }
        }
    }


    /**
     *  This method writes this object of <code>ServiceInstances</code>
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

        if (m_serviceInstances != null)
        {
            int length = m_serviceInstances.size();

            out.writeInt(length);

            Enumeration instances = m_serviceInstances.elements();

            while (instances.hasMoreElements())
            {
                ServiceInstance instance = (ServiceInstance) instances.nextElement();

                instance.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if (m_extServiceInstances != null)
        {
            int length = m_extServiceInstances.size();

            out.writeInt(length);

            Enumeration instances = m_extServiceInstances.elements();

            while (instances.hasMoreElements())
            {
                ExternalServiceInstance instance = (ExternalServiceInstance) instances.nextElement();

                instance.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This method reads this object <code>ServiceInstances</code> from the
     *  specified input stream object.
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

        int tempInt = 0;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                ServiceInstance inst = new ServiceInstance();

                inst.fromStream(is, versionNo);
                m_serviceInstances.add(inst);
            }
        }

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                ExternalServiceInstance inst = new ExternalServiceInstance();

                inst.fromStream(is, versionNo);
                m_extServiceInstances.add(inst);
            }
        }
    }

    /**
     * This utility method gets the String representation of this object
     * of <code>ServiceInstances</code>.
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
        strBuf.append("Service Instances ");
        strBuf.append("[");
        if (m_serviceInstances != null)
        {
            strBuf.append("Service Instance = ");
            for (int i = 0; i < m_serviceInstances.size(); ++i)
            {
                strBuf.append((i + 1) + ". ");
                m_serviceInstances.elementAt(i);
                strBuf.append(", ");
            }
        }
        if (m_extServiceInstances != null)
        {
            strBuf.append("External Service Instance = ");
            for (int i = 0; i < m_extServiceInstances.size(); ++i)
            {
                strBuf.append((i + 1) + ". ");
                m_extServiceInstances.elementAt(i);
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the XML string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("ServiceInstances");
        Enumeration serviceEnums = m_serviceInstances.elements();

        while (serviceEnums.hasMoreElements())
        {
            ServiceInstance instance = (ServiceInstance) serviceEnums.nextElement();
            Node instanceNode = instance.toJXMLString(document);

            root0.appendChild(instanceNode);
        }

        Enumeration extServiceEnums = m_extServiceInstances.elements();

        while (extServiceEnums.hasMoreElements())
        {
            ExternalServiceInstance instance = (ExternalServiceInstance) extServiceEnums.nextElement();
            Node instanceNode = instance.toJXMLString(document);

            root0.appendChild(instanceNode);
        }
        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException,
      FioranoException {

        //Start ServiceInstances
        writer.writeStartElement("ServiceInstances");

        Enumeration serviceEnums = m_serviceInstances.elements();

        while (serviceEnums.hasMoreElements())
        {
            ServiceInstance instance = (ServiceInstance) serviceEnums.nextElement();
            instance.toJXMLString(writer);

        }

        Enumeration extServiceEnums = m_extServiceInstances.elements();

        while (extServiceEnums.hasMoreElements())
        {
            ExternalServiceInstance instance = (ExternalServiceInstance) extServiceEnums.nextElement();
            instance.toJXMLString(writer);

        }

        //End ServiceInstances
        writer.writeEndElement();
    }
}
