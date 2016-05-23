/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ServiceInstanceStateDetails extends DmiObject
{
    private long    m_launchTime;

    private long    m_killTime;

    private String  m_statusString;

    private String  m_serviceGUID;

    private String  m_serviceInstName;

    private String  m_serviceNodeName;

    private String  m_runningVersion;

    private String  m_strUniqueRunningInstID;
    private boolean m_gracefulKill=true;   /* bug#14718 , gracefullKill should be true by default*/
    private int launchType;

    /**
     *  This is the constructor of the <code>ServiceInstanceStateDetails</code>
     *  class.
     *
     * @since Tifosi2.0
     */
    public ServiceInstanceStateDetails()
    {
    }


    /**
     *  This method gets the launch time for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @return The launch Time value for the service instance
     * @see #setLaunchTime(long)
     * @see #setKillTime(long)
     * @see #getKillTime()
     * @since Tifosi2.0
     */
    public long getLaunchTime()
    {
        return m_launchTime;
    }


    /**
     *  This method gets the abort time for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @return Abort Time for the service instance
     * @see #setLaunchTime(long)
     * @see #setKillTime(long)
     * @see #getLaunchTime()
     * @since Tifosi2.0
     */
    public long getKillTime()
    {
        return m_killTime;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of the object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_STATE_DETAILS;
    }


    /**
     *  This method gets service status for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @return The isRunning value
     * @see #setStatusString(String)
     * @since Tifosi2.0
     */
    public String getStatusString()
    {
        return m_statusString;
    }

     /**
     *  This method gets service status message for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @return The status Message for service
     */
     public String getStatusMessage()
     {
         String message = "";
         if(getServiceNodeName()==null)
             message=" ("+")";
         return m_statusString + message;
     }

    /**
     *  This method gets the serviceGUID for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @return The serviceGUID value
     * @see #setServiceGUID(String)
     * @since Tifosi2.0
     */
    public String getServiceGUID()
    {
        return m_serviceGUID;
    }


    /**
     *  This method gets the Instance name for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code> contains state
     *  details.
     *
     * @return The Instance name of service instance.
     * @see #setServiceInstanceName(String)
     * @since Tifosi2.0
     */
    public String getServiceInstanceName()
    {
        return m_serviceInstName;
    }


    /**
     *  This method sets the specified string as node name on which the service
     *  instance is running, about which this object of <code>ServiceInstanceStateDetails</code
     *  > contains state details
     *
     * @return The node name over which the service instance is running
     * @see #setServiceNodeName(String)
     * @since Tifosi2.0
     */
    public String getServiceNodeName()
    {
        return m_serviceNodeName;
    }

    /**
     *  This method gets the runningVersion of the <code>ServiceInstance</code>
     *  object.
     *
     * @return The runningVersion.
     */
    public String getRunningVersion()
    {
        return m_runningVersion;
    }

    /**
     * Returns unique running inst ID for object
     *
     * @return
     */
    public String getUniqueRunningInstID()
    {
        return m_strUniqueRunningInstID;
    }

    /**
     * The Graceful way of killing a component is killing it from RTL.
     *  While restoring the state of the component, the compoents that are not killed from RTL will be relaunched
     * @return boolaen specifying whether or not component is killed from RTL call.
     */
    public boolean isGracefulKill(){
        return m_gracefulKill;
    }

    public void setGracefulKill(boolean gracefulKill){
        m_gracefulKill = gracefulKill;
    }

    /**
     *  This method sets the launch time for the service instance, about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @param launchTime The time to be set as launch time
     * @see #setKillTime(long)
     * @see #getLaunchTime()
     * @see #getKillTime()
     * @since Tifosi2.0
     */
    public void setLaunchTime(long launchTime)
    {
        m_launchTime = launchTime;
    }


    /**
     *  This method sets the launch time for the service instance about which
     *  this object of <code>ServiceInstanceStateDetails</code > contains state
     *  details.
     *
     * @param killTime The time to be set as abort time
     * @see #setLaunchTime(long)
     * @see #getLaunchTime()
     * @see #getKillTime()
     * @since Tifosi2.0
     */
    public void setKillTime(long killTime)
    {
        m_killTime = killTime;
    }


    /**
     *  This method sets the specified string as service status. It is set for
     *  the service instance, about which this object of <code>ServiceInstanceStateDetails</code
     *  > contains state details.
     *
     * @param statusString The new statusString value
     * @see #getStatusString()
     * @since Tifosi2.0
     */
    public void setStatusString(String statusString)
    {
        m_statusString = statusString;
    }


    /**
     *  This method sets the specified string as serviceGUID for the service
     *  instance, about which this object of <code>ServiceInstanceStateDetails</code
     *  > contains state details.
     *
     * @param serviceGUID The string to be set as serviceGUID
     * @see #getServiceGUID()
     * @since Tifosi2.0
     */
    public void setServiceGUID(String serviceGUID)
    {
        m_serviceGUID = serviceGUID;
    }


    /**
     *  This method sets the specified string as serviceInstancename for the
     *  service instance, about which this object of <code>ServiceInstanceStateDetails</code
     *  > contains state details.
     *
     * @param serviceInstName String that is to be set as instance name
     * @see #getServiceInstanceName()
     * @since Tifosi2.0
     */
    public void setServiceInstanceName(String serviceInstName)
    {
        m_serviceInstName = serviceInstName;
    }


    /**
     *  This method sets the specified string as node name on which the service
     *  instance is running, about which this object of <code>ServiceInstanceStateDetails</code
     *  > contains state details
     *
     * @param serviceNodeName String to be set as node name over which service
     *      instance is running
     * @see #getServiceNodeName()
     * @since Tifosi2.0
     */
    public void setServiceNodeName(String serviceNodeName)
    {
        m_serviceNodeName = serviceNodeName;
    }

    /**
     *  This method sets the runningVersion of the <code>ServiceInstance</code>
     *  object.
     *
     * @param runningVersion The runningVersion.
     */
    public void setRunningVersion(String runningVersion)
    {
        m_runningVersion = runningVersion;
    }

    /**
     * Sets unique running inst ID for object
     *
     * @param uniqueRunningInstID
     */
    public void setUniqueRunningInstID(String uniqueRunningInstID)
    {
        m_strUniqueRunningInstID = uniqueRunningInstID;
    }

    /**
     *  This method resets values of the data members of this object. Not
     *  supported in this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method tests whether this object of <code>ServiceInstanceStateDetails</code>
     *  has the required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method writes this object of <code>ServiceInstanceStateDetails</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeLong(m_launchTime);
        out.writeLong(m_killTime);

        if (m_statusString != null)
            UTFReaderWriter.writeUTF(out, m_statusString);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_serviceGUID != null)
            UTFReaderWriter.writeUTF(out, m_serviceGUID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_serviceInstName != null)
            UTFReaderWriter.writeUTF(out, m_serviceInstName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_serviceNodeName != null)
            UTFReaderWriter.writeUTF(out, m_serviceNodeName);
        else
            UTFReaderWriter.writeUTF(out, "");
        if (m_runningVersion != null)
            UTFReaderWriter.writeUTF(out, m_runningVersion);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_strUniqueRunningInstID != null)
            UTFReaderWriter.writeUTF(out, m_strUniqueRunningInstID);
        else
            UTFReaderWriter.writeUTF(out, "");
        out.writeBoolean(m_gracefulKill);
    }


    /**
     *  This method reads this object <code>ServiceInstanceStateDetails</code>
     *  from the specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_launchTime = is.readLong();
        m_killTime = is.readLong();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_statusString = null;
        else
            m_statusString = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_serviceGUID = null;
        else
            m_serviceGUID = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_serviceInstName = null;
        else
            m_serviceInstName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_serviceNodeName = null;
        else
            m_serviceNodeName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_runningVersion = null;
        else
            m_runningVersion = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_strUniqueRunningInstID = null;
        else
            m_strUniqueRunningInstID = temp;
        m_gracefulKill = is.readBoolean();
    }


    /**
     *  This utility method compares the specified object with this object of
     *  <code>ServiceInstanceStateDetails</code>.
     *
     * @param obj object with which the comparison is to be made.
     * @return true if the two objects are equal
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof ServiceInstanceStateDetails)
        {
            ServiceInstanceStateDetails thisObj = (ServiceInstanceStateDetails) obj;

            if ((thisObj.getKillTime() == getKillTime())
                && (thisObj.getLaunchTime() == getLaunchTime())
                && (DmiEqualsUtil.checkStringEquality(thisObj.getServiceGUID(), getServiceGUID()))
                && (DmiEqualsUtil.checkStringEquality(thisObj.getServiceInstanceName(), getServiceInstanceName()))
                && (DmiEqualsUtil.checkStringEquality(thisObj.getServiceNodeName(), getServiceNodeName()))
                && (DmiEqualsUtil.checkStringEquality(thisObj.getStatusString(), getStatusString())))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     *  This utility method gets the String representation of this object of
     *  <code>ServiceInstanceStateDetails</code>.
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
        strBuf.append("Service Instance State Details ");
        strBuf.append("[");
        strBuf.append("Launch Time = ");
        strBuf.append(String.valueOf(m_launchTime));
        strBuf.append(", ");
        strBuf.append("Kill time = ");
        strBuf.append(String.valueOf(m_killTime));
        strBuf.append(", ");
        strBuf.append("Status String = ");
        strBuf.append(m_statusString);
        strBuf.append(", ");
        strBuf.append("Service GUID = ");
        strBuf.append(m_serviceGUID);
        strBuf.append(", ");
        strBuf.append("Service Instance Name = ");
        strBuf.append(m_serviceInstName);
        strBuf.append(", ");
        strBuf.append("Service node Name = ");
        strBuf.append(m_serviceNodeName);
        strBuf.append(", ");
        strBuf.append("Service Unique RunningInstanceID = ");
        strBuf.append(m_strUniqueRunningInstID);
        strBuf.append(", ");
        strBuf.append("Service Running Version = ");
        strBuf.append(m_runningVersion);
        strBuf.append("]");
        return strBuf.toString();
    }


    public void setLaunchType(int launchType) {
        this.launchType = launchType;
    }

    public int getLaunchType() {
        return launchType;
    }
}
