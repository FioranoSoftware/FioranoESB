/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.events;

import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.*;

public class MicroServiceRepoUpdateEvent extends SystemEvent {

        public final static String SERVICE_CREATED = "Service Created";
        public final static String SERVICE_REGISTERED = "Service Registered";
        public final static String SERVICE_REMOVED = "Service Removed";
        public final static String RESOURCE_CREATED = "Resource Created";
        public final static String RESOURCE_REMOVED = "Resource Removed";
        public final static String RESOURCE_UPLOADED = "Resource Uploaded";
        public final static String UNREGISTERED_SERVICE_EDITED = "Unregistered Service Edited";
        public static final String SERVICE_OVERWRITTEN = "Service Overwritten";
        private String  m_serviceGUID;
        private String  m_serviceVersion;
        private String  m_serviceStatus;

        private String  m_resourceName;
        public static final String REGISTERED_SERVICE_EDITED = "Registered Service Edited.";

        /**
         *  Gets the eventType attribute of the ServiceRepositoryUpdationEvent
         *  object
         *
         * @return The eventType value
         */
    public EventType getEventType()
    {
        return EventType.MICRO_SERVICE_REPO_UPDATE_EVENT;
    }

    /**
     *  Gets the serviceGUID attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @return The serviceGUID value
     */
    public String getServiceGUID()
    {
        return m_serviceGUID;
    }

    /**
     * Returns resource name for object
     *
     * @return resource name
     */
    public String getResourceName()
    {
        return m_resourceName;
    }

    /**
     *  Gets the serviceVersion attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @return The serviceVersion value
     */
    public String getServiceVersion()
    {
        return m_serviceVersion;
    }

    /**
     *  Gets the serviceStatus attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @return The serviceStatus value
     */
    public String getServiceStatus()
    {
        return m_serviceStatus;
    }


    /**
     *  Gets the eventValues attribute of the SPEvent object
     *
     * @param versionNo
     * @return The eventValues value
     * @exception IOException Description of the Exception
     */
    public byte[] getEventValues(int versionNo)
            throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        byte[] superBytes = super.getEventValues(versionNo);

        if (superBytes != null && superBytes.length > 0)
        {
            dos.writeInt(superBytes.length);
            dos.write(superBytes);
        }
        else
            dos.writeInt(-1);

        writeUTF(dos, m_serviceGUID);
        writeUTF(dos, m_serviceVersion);
        writeUTF(dos, m_serviceStatus);
        writeUTF(dos, m_resourceName);

        return bos.toByteArray();
    }

    /** Checks if this event is identical to event passed as parameter.
     * @param event to be compared
     * @return 'true' if the events are identical else 'false'
     */
    public boolean isIdentical(Event event) {
        MicroServiceRepoUpdateEvent that;
        try
        {
            that= (MicroServiceRepoUpdateEvent)event;
            if(!this.getServiceStatus().equals(that.getServiceStatus()))
                return false;
            if(this.getServiceGUID().equals(that.getServiceGUID()) & this.getServiceVersion().equals(that.getServiceVersion()))
            {
                if(this.getResourceName() == null & that.getResourceName() == null)
                    return true;
                else if(this.getResourceName() == null || that.getResourceName() == null)
                    return false;
                else
                    return this.getResourceName().equals(that.getResourceName());
            }
        }
        catch(ClassCastException e)
        {
            //ignore exception. return false e.printStackTrace();
        }

        return false;
    }

    /**
     *  Sets the serviceGUID attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @param serviceGUID The new serviceGUID value
     */
    public void setServiceGUID(String serviceGUID)
    {
        m_serviceGUID = serviceGUID;
    }

    /**
     * Sets resource name for object
     *
     * @param resourceName
     */
    public void setResourceName(String resourceName)
    {
        m_resourceName = resourceName;
    }

    /**
     *  Sets the serviceVersion attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @param serviceVersion The new serviceVersion value
     */
    public void setServiceVersion(String serviceVersion)
    {
        m_serviceVersion = serviceVersion;
    }

    /**
     *  Sets the serviceStatus attribute of the ServiceRepositoryUpdationEvent
     *  object
     *
     * @param serviceStatus The new serviceStatus value
     */
    public void setServiceStatus(String serviceStatus)
    {
        m_serviceStatus = serviceStatus;
    }

    /**
     *  Sets the eventValues attribute of the SPEvent object
     *
     * @param bytes The new eventValues value
     * @param versionNo
     * @exception IOException Description of the Exception
     */
    public void setEventValues(byte[] bytes, int versionNo)
            throws IOException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        int numBytes = dis.readInt();

        if (numBytes > 0)
        {
            byte[] superBytes = new byte[numBytes];

            dis.read(superBytes);
            super.setEventValues(superBytes, versionNo);
        }

        m_serviceGUID = readUTF(dis);
        m_serviceVersion = readUTF(dis);
        m_serviceStatus = readUTF(dis);
        m_resourceName = readUTF(dis);
    }

    /**
     * This method is called to write this object of <code>ServiceRepositoryUpdationEvent</code> to
     * the specified output stream object.
     * @param dataOutput
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.
     */
    public void toSream(DataOutput dataOutput)
            throws IOException
    {
        UTFReaderWriter.writeUTF(dataOutput, m_serviceGUID);
        UTFReaderWriter.writeUTF(dataOutput, m_serviceVersion);
        UTFReaderWriter.writeUTF(dataOutput, m_serviceStatus);
        UTFReaderWriter.writeUTF(dataOutput, m_resourceName);
    }

    /**
     * This is called to read this object <code>ServiceRepositoryUpdationEvent</code> from the
     * specified object of input stream.
     * @param dataInput
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.
     */
    public void fromStream(DataInput dataInput)
            throws IOException
    {
        m_serviceGUID = UTFReaderWriter.readUTF(dataInput);
        m_serviceVersion = UTFReaderWriter.readUTF(dataInput);
        m_serviceVersion = UTFReaderWriter.readUTF(dataInput);
        m_resourceName = UTFReaderWriter.readUTF(dataInput);
    }
}