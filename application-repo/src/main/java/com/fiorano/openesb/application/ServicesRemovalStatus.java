/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class ServicesRemovalStatus extends DmiObject
{
    //status of call for service removal
    /**
     *  Service status specifying that the running instance of service is aborted
     *  and service is removed successfully.
     *
     * @since Tifosi2.0
     */
    public final static String INSTANCES_KILLED_AND_SERVICE_REMOVED_SUCCESSFULLY = "Instances killed and Service removed successfully.";
    /**
     *  Service status specifying that the service is currently running and hence
     *  cannot be removed.
     *
     * @since Tifosi2.0
     */
    public final static String SERVICE_CURRENTLY_RUNNING_AND_NOT_REMOVED = "Service is currently running.Hence not removed.";
    /**
     *  Service status specifying that the service is being used as a dependency
     *  by a running service and hence cannot be removed.
     *
     * @since Tifosi2.0
     */
    public final static String SERVICE_BEING_USED_AS_DEPENDENCY_BY_A_RUNNING_SERVICE_AND_NOT_REMOVED = "Service being used as dependency by running services.Hence not removed.";
    /**
     *  Service status specifying successful removal of service
     *
     * @since Tifosi2.0
     */
    public final static String SERVICE_SUCCESSFULLY_REMOVED = "Service successfully removed.";

    /**
     *  Service status specifying successful removal of service
     *
     * @since Tifosi2.0
     */
    public final static String SERVICE_NOT_PRESENT = "Service Not Present.";

    private Hashtable m_hashServiceRemovalStatus;

    /**
     *  Constructor for the <code>ServiceRemovalStatus</code> object.
     *
     * @since Tifosi2.0
     */
    public ServicesRemovalStatus()
    {
        m_hashServiceRemovalStatus = new Hashtable();
    }


    /**
     *  This method returns the removal status information about the service
     *  specified by the <code>serviceGUID</code> and <code>version</code>
     *  arguments, from this <code>ServiceRemovalStatus</code> object.
     *
     * @param serviceGUID GUID of service for which to get the removal status
     * @param version version of service for which to get the removal status
     * @return object of ServiceRemovalInfo
     * @see #addStatus(String, String, ServiceRemovalInfo)
     * @see #getCompleteStatus()
     * @since Tifosi2.0
     */
    public ServiceRemovalInfo getRemovalInfo(String serviceGUID, String version)
    {
        return (ServiceRemovalInfo) m_hashServiceRemovalStatus.get(serviceGUID + "_" + version);
    }

    /**
     *  This method returns a hashtable containing the <code>serviceGUID</code>
     *  and <code>version</code> information versus the service removal status from
     *  this <code>ServiceRemovalStatus</code> object.
     *
     * @return Hashtable containing service information versus service
     *                     removal status
     * @see #addStatus(String, String, ServiceRemovalInfo)
     * @see #getRemovalInfo(String, String)
     * @since Tifosi2.0
     */
    public Hashtable getCompleteStatus()
    {
        return m_hashServiceRemovalStatus;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return The id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICES_REMOVAL_STATUS;
    }

    /**
     *  Adds the specified object of <code>ServiceRemovalInfo</code> to be set
     *  for a service identified by the <code>serviceGUID</code> and
     *  <code>version</code> arguments. This is added to the list of status
     *  for this <code>ServiceRemovalStatus</code> object.
     *
     * @param serviceGUID GUID of service for which to set the status.
     * @param version version of service for which to set the status.
     * @param removalInfo object of ServiceRemovalInfo to be set as status.
     * @see #getRemovalInfo(String, String)
     * @see #getCompleteStatus()
     * @since Tifosi2.0
     */
    public void addStatus(String serviceGUID, String version, ServiceRemovalInfo removalInfo)
    {
        m_hashServiceRemovalStatus.put(serviceGUID + "_" + version, removalInfo);
    }


    /**
     *  This method has not been implemented in this version.
     *  (Resets the values of the data members of this object.)
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method reads this <code>ServicesRemovalStatus</code> object
     *  from the specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException thrown in case of error while reading data
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        int size = is.readInt();

        for (int i = 0; i < size; i++)
        {
            String key = readUTF(is);
            ServiceRemovalInfo val = new ServiceRemovalInfo();

            val.fromStream(is, versionNo);
            m_hashServiceRemovalStatus.put(key, val);
        }
    }


    /**
     *  This method writes this <code>ServicesRemovalStatus</code> object
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException thrown in case of error while writing data.
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        out.writeInt(m_hashServiceRemovalStatus.size());

        Enumeration keys = m_hashServiceRemovalStatus.keys();

        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            ServiceRemovalInfo val = (ServiceRemovalInfo) m_hashServiceRemovalStatus.get(key);

            writeUTF(out, key);
            val.toStream(out, versionNo);
        }
    }


    /**
     *  This method tests whether this <code>ServicesRemovalStatus</code> object
     *  has the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

}
