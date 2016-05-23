/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.ServiceReference;

import java.util.Enumeration;
import java.util.Vector;

public class ServiceInfo extends DmiObject
{

    ServiceReference   m_header;
    Vector          m_vecRunningInstances;

    /**
     *  Constructor for the ServiceInfo object
     */
    public ServiceInfo()
    {
        m_vecRunningInstances = new Vector();
    }

    /**
     *  Gets the serviceHeader attribute of the ServiceInfo object
     *
     * @return The serviceHeader value
     */
    public ServiceReference getServiceHeader()
    {
        return m_header;
    }

    /**
     *  Gets the runningInstances attribute of the ServiceInfo object
     *
     * @return The runningInstances value
     */
    public Vector getRunningInstances()
    {
        return m_vecRunningInstances;
    }

    /**
     *  Gets the objectID attribute of the ServiceInfo object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_INFO;
    }

    /**
     *  Sets the serviceHeader attribute of the ServiceInfo object
     *
     * @param header The new serviceHeader value
     */
    public void setServiceHeader(ServiceReference header)
    {
        m_header = header;
    }

    /**
     *  Adds a feature to the RunningInstance attribute of the ServiceInfo
     *  object
     *
     * @param instanceInfo The feature to be added to the RunningInstance
     *      attribute
     */
    public void addRunningInstance(ServiceInstanceInfo instanceInfo)
    {
        m_vecRunningInstances.add(instanceInfo);
    }


    /**
     *  The method resets the fields of the object to the default values
     */
    public void reset()
    {
    }


    /**
     *  The method is used to read the data from a DataInput object. The fields
     *  of the object are assigned values from the inout stream.
     *
     * @param is The input stream from which the data
     *  is to be read
     * @param versionNo
     * @exception java.io.IOException If there is some error reading data from
     * the stream
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        m_header = new ServiceReference();
        m_header.fromStream(is, versionNo);

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            ServiceInstanceInfo info = new ServiceInstanceInfo();

            info.fromStream(is, versionNo);
            m_vecRunningInstances.add(info);
        }
    }


    /**
     *  The method writes out the values of the fields of the object to
     *  the output stream.
     *
     * @param out The output stream to which the data is to be written
     * @param versionNo
     * @exception java.io.IOException If there is some error in writing the data
     * onto the output stream.
     */
    public void toStream(java.io.DataOutput out, int versionNo)
        throws java.io.IOException
    {
        m_header.toStream(out, versionNo);

        out.writeInt(m_vecRunningInstances.size());

        Enumeration elements = m_vecRunningInstances.elements();

        while (elements.hasMoreElements())
        {
            ServiceInstanceInfo info = (ServiceInstanceInfo) elements.nextElement();

            info.toStream(out, versionNo);
        }
    }


    /**
     *  Validates the fields of the object for correctness.
     *
     * @exception FioranoException Throws exception if the object is not valid
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  Returns the String representation of this event.
     *
     * @return String representation of the object
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Service info Details ");
        strBuf.append("[");
        if (m_vecRunningInstances != null)
        {
            strBuf.append("Running Instances = ");

            Enumeration elements = m_vecRunningInstances.elements();

            while (elements.hasMoreElements())
            {
                ServiceInstanceInfo info = (ServiceInstanceInfo) elements.nextElement();

                strBuf.append(info.toString());
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }

}
