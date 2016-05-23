/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.DmiEqualsUtil;
import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class ApplicationStateDetails extends DmiObject {
    private long m_launchTime;

    private long m_killTime;

    private Hashtable<String, ServiceInstanceStateDetails> m_serviceStates;

    private Hashtable m_serviceExceptionTraces;

    private String m_strAppGUID;

    private String m_strAppVersion;

    private String applicationLabel;

    private ArrayList m_debugRoutes;

    private ArrayList m_pendingDebugRoutesForClosure;

    private String killException;

    private String launchException;

    private List<String> m_previousSyncPeers;
    private String displayName;

    /**
     * This is the constructor of the <code>ApplicationStateDetails</code> class.
     *
     * @since Tifosi2.0
     */
    public ApplicationStateDetails() {
        m_serviceStates = new Hashtable();
        m_serviceExceptionTraces = new Hashtable();
        m_debugRoutes = new ArrayList();
        m_pendingDebugRoutesForClosure = new ArrayList();
        m_previousSyncPeers = new ArrayList<>();
        m_strAppGUID = "";
        displayName = "";
        m_strAppVersion = "";
        applicationLabel = "";
        m_launchTime = -1;
        m_killTime = -1;
        killException = "";
        launchException = "";
    }

    /**
     * This method gets the total number of status of service instances of
     * application, from the <code>ApplicationStateDetails</code> object.
     *
     * @return The totalNumberOfStatus value
     * @since Tifosi2.0
     */
    public int getTotalNumberOfStatus() {
        return m_serviceStates.size();
    }

    public void addDebugRoute(String routeGUID) {
        m_debugRoutes.add(routeGUID);
    }

    public Iterator getDebugRoutes() {
        return m_debugRoutes.iterator();
    }

    public void addPendingDebugRoutesForClosure(String routeGUID) {
        m_pendingDebugRoutesForClosure.add(routeGUID);
    }

    public Iterator getPendingDebugRoutesForClosure() {
        return m_pendingDebugRoutesForClosure.iterator();
    }

    public Iterator getPreviousSynchPeers() {
        return m_previousSyncPeers.iterator();
    }

    public void addPreviousSyncPeers(String peerName) {
        m_previousSyncPeers.add(peerName);
    }

    /**
     * Gets the launch Time for application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @return launch Time value
     * @see #getKillTime()
     * @see #setLaunchTime(long)
     * @since Tifosi2.0
     */
    public long getLaunchTime() {
        return m_launchTime;
    }


    /**
     * Gets the Abort Time for application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @return abort Time value
     * @see #getLaunchTime()
     * @see #setKillTime(long)
     * @since Tifosi2.0
     */
    public long getKillTime() {
        return m_killTime;
    }


    /**
     * Gets the GUID of application from this
     * <code>ApplicationStateDetails</code> object.
     *
     * @return The GUID of application
     * @see #setAppGUID(String)
     * @since Tifosi2.0
     */
    public String getAppGUID() {
        return m_strAppGUID;
    }

    /**
     * Gets the Version of application from this
     * <code>ApplicationStateDetails</code> object.
     *
     * @return The Version of application
     * @see #setAppVersion(String)
     * @since Tifosi2.0
     */
    public String getAppVersion() {
        return m_strAppVersion;
    }

    /**
     * This method gets the status for specified service instance
     * from this <code>ApplicationStateDetails</code> object.
     *
     * @param instName service instance name
     * @return object of ServiceInstanceStateDetails
     * @see #removeAllStatusStrings()
     * @see #removeServiceStatus(String)
     * @since Tifosi2.0
     */
    public ServiceInstanceStateDetails getServiceStatus(String instName) {
        return (ServiceInstanceStateDetails) m_serviceStates.get(instName);
    }

    public Hashtable<String, ServiceInstanceStateDetails> getAllServiceStateDetails(){
        return m_serviceStates;
    }


    /**
     * This method gets enumeration of names of all the service instances belonging
     * to an application with this <code>ApplicationStateDetails</code> object.
     *
     * @return Enumeration of service instance names.
     * @since Tifosi2.0
     */
    public Enumeration getAllServiceNames() {
        return m_serviceStates.keys();
    }


    /**
     * This method gets the exception trace for specified service instance, from
     * this <code>ApplicationStateDetails</code> object.
     *
     * @param instName service instance name
     * @return The serviceStatus value
     * @see #getAllServiceWithExceptions()
     * @see #addServiceExceptionTrace(String, String)
     * @since Tifosi2.0
     */
    public String getServiceExceptionTrace(String instName) {
        return (String) m_serviceExceptionTraces.get(instName);
    }


    /**
     * This method gets the exception trace for all the service instance, from this
     * <code>ApplicationStateDetails</code> object.
     *
     * @return Enumeration of exception trace for all the service instances.
     * @see #getServiceExceptionTrace(String)
     * @see #addServiceExceptionTrace(String, String)
     * @since Tifosi2.0
     */
    public Enumeration getAllServiceWithExceptions() {
        return m_serviceExceptionTraces.keys();
    }


    /**
     * This method returns the ID of this object.
     *
     * @return the id of the object.
     * @since Tifosi2.0
     */
    public int getObjectID() {
        return DmiObjectTypes.APP_STATE_DETAILS;
    }

    /**
     * This method sets the launch Time for application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @param launchTime launch Time value
     * @see #getLaunchTime()
     * @see #setKillTime(long)
     * @see #getKillTime()
     * @since Tifosi2.0
     */
    public void setLaunchTime(long launchTime) {
        m_launchTime = launchTime;
    }


    /**
     * Sets the Abort Time for application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @param killTime Abort Time value
     * @see #getKillTime()
     * @see #setLaunchTime(long)
     * @since Tifosi2.0
     */
    public void setKillTime(long killTime) {
        m_killTime = killTime;
    }


    /**
     * Sets the GUID of application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @param guid the  GUID of application to be set.
     * @see #getAppGUID()
     * @since Tifosi2.0
     */
    public void setAppGUID(String guid) {
        m_strAppGUID = guid;
    }

    /**
     * Sets the Version of application in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @param version the  Version of application to be set.
     * @see #getAppVersion()
     * @since Tifosi2.0
     */
    public void setAppVersion(String version) {
        m_strAppVersion = version;
    }


    /**
     * This method adds the specified <code>ServiceInstanceStateDetails</code>
     * object as status for the specified service instance, in this
     * <code>ApplicationStateDetails</code> object.
     *
     * @param instName The name of service instance.
     * @param status   object of ServiceInstanceStateDetails
     * @see #getServiceStatus(String)
     * @see #removeAllStatusStrings()
     * @see #removeServiceStatus(String)
     * @since Tifosi2.0
     */
    public void addServiceStatus(String instName, ServiceInstanceStateDetails status) {
        m_serviceStates.put(instName, status);
    }

    /**
     * This method removes service status for specified service
     * instance, from specified <code>ApplicationStateDetails</code> object.
     *
     * @param instName The feature to be added to the ServiceDetails attribute
     * @see #getServiceStatus(String)
     * @see #removeAllStatusStrings()
     * @see #addServiceStatus(String, ServiceInstanceStateDetails)
     * @since Tifosi2.0
     */
    public void removeServiceStatus(String instName) {
        m_serviceStates.remove(instName);
    }

    /**
     * This method removes the exception trace for specified service
     * instance from this <code>ApplicationStateDetails</code> object.
     *
     * @param instName The name of service instance.
     * @see #removeAllExceptionTraces()
     * @since Tifosi2.0
     */
    public void removeServiceExceptionTrace(String instName) {
        m_serviceExceptionTraces.remove(instName);
    }

    /**
     * This method removes exception trace for all the services from
     * this <code>ApplicationStateDetails</code> object.
     *
     * @see #removeServiceExceptionTrace(String)
     * @since Tifosi2.0
     */
    public void removeAllExceptionTraces() {
        m_serviceExceptionTraces.clear();
    }

    /**
     * This method removes status for all the services from this
     * of <code>ApplicationStateDetails</code> object.
     *
     * @see #getServiceStatus(String)
     * @see #removeServiceStatus(String)
     * @since Tifosi2.0
     */
    public void removeAllStatusStrings() {
        m_serviceStates.clear();
    }


    /**
     * This method adds the specified exception trace for specified service instance ,
     * to this <code>ApplicationStateDetails</code> object.
     *
     * @param instName Service instance name
     * @param trace    trace to be added.
     * @see #getAllServiceWithExceptions()
     * @see #getServiceExceptionTrace(String)
     * @since Tifosi2.0
     */
    public void addServiceExceptionTrace(String instName, String trace) {
        m_serviceExceptionTraces.put(instName, trace);
    }


    /**
     * This method resets the values of the data members of the object.
     *
     * @since Tifosi2.0
     */
    public void reset() {
    }


    /**
     * This method tests whether this object has the required(mandatory)
     * fields set, before inserting values in to the database. Not supported in
     * this version.
     *
     * @throws FioranoException if the validation process fails to succeed.
     * @since Tifosi2.0
     */
    public void validate()
            throws FioranoException {
    }


    /**
     * This method writes this object of <code>ApplicationStateDetails</code>
     * to the specified output stream object.
     *
     * @param out       DataOutput object
     * @param versionNo
     * @throws IOException if an error occurs while converting data and
     *                     writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
            throws IOException {
        super.toStream(out, versionNo);

        out.writeLong(m_launchTime);
        out.writeLong(m_killTime);
        writeUTF(out, m_strAppGUID);
        writeUTF(out, m_strAppVersion);
        out.writeInt(m_serviceStates.size());

        Enumeration enums = m_serviceStates.keys();

        while (enums.hasMoreElements()) {
            String instName = (String) enums.nextElement();
            ServiceInstanceStateDetails status = (ServiceInstanceStateDetails)
                    m_serviceStates.get(instName);

            UTFReaderWriter.writeUTF(out, instName);
            status.toStream(out, versionNo);
        }

        out.writeInt(m_serviceExceptionTraces.size());

        Enumeration traceEnums = m_serviceExceptionTraces.keys();

        while (traceEnums.hasMoreElements()) {
            String instName = (String) traceEnums.nextElement();
            String trace = (String) m_serviceExceptionTraces.get(instName);

            UTFReaderWriter.writeUTF(out, instName);
            UTFReaderWriter.writeUTF(out, trace);
        }

        out.writeInt(m_debugRoutes.size());
        Iterator debugRoutes = getDebugRoutes();

        while (debugRoutes.hasNext()) {
            String routeGUID = (String) debugRoutes.next();
            UTFReaderWriter.writeUTF(out, routeGUID);
        }

        out.writeInt(m_pendingDebugRoutesForClosure.size());
        Iterator pendingDebugRoutesForClosure = getPendingDebugRoutesForClosure();

        while (pendingDebugRoutesForClosure.hasNext()) {
            String routeGUID = (String) pendingDebugRoutesForClosure.next();
            UTFReaderWriter.writeUTF(out, routeGUID);
        }

        out.writeInt(m_previousSyncPeers.size());
        Iterator previousSynchPeers = getPreviousSynchPeers();
        while (previousSynchPeers.hasNext()) {
            String peerName = (String) previousSynchPeers.next();
            UTFReaderWriter.writeUTF(out, peerName);
        }
    }


    /**
     * This method reads this <code>ApplicationStateDetails</code> object from the
     * specified input stream object.
     *
     * @param is        DataInput object
     * @param versionNo
     * @throws IOException if any error occurs while reading bytes or while
     *                     converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
            throws IOException {
        super.fromStream(is, versionNo);

        m_launchTime = is.readLong();
        m_killTime = is.readLong();
        m_strAppGUID = readUTF(is);
        m_strAppVersion = readUTF(is);
        int hashTableSize = is.readInt();

        for (int i = 0; i < hashTableSize; i++) {
            ServiceInstanceStateDetails details = new ServiceInstanceStateDetails();
            String instName = UTFReaderWriter.readUTF(is);

            details.fromStream(is, versionNo);
            addServiceStatus(instName, details);
        }

        int tableSize = is.readInt();

        for (int i = 0; i < tableSize; i++) {
            addServiceExceptionTrace(UTFReaderWriter.readUTF(is), UTFReaderWriter.readUTF(is));
        }

        int debugRouteSize = is.readInt();
        for (int i = 0; i < debugRouteSize; i++) {
            addDebugRoute(UTFReaderWriter.readUTF(is));
        }

        int pendingRouteForClosureSize = is.readInt();
        for (int i = 0; i < pendingRouteForClosureSize; i++) {
            addDebugRoute(UTFReaderWriter.readUTF(is));
        }

        int previousSyncSize = is.readInt();
        for (int i = 0; i < previousSyncSize; i++) {
            m_previousSyncPeers.add(UTFReaderWriter.readUTF(is));
        }

    }


    /**
     * This utility method is used to compare this object of <code>ApplicationStateDetails</code>
     * with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj) {
        if (obj instanceof ApplicationStateDetails) {
            ApplicationStateDetails thisObj = (ApplicationStateDetails) obj;

            if ((thisObj.getKillTime() == getKillTime())
                    && (thisObj.getLaunchTime() == getLaunchTime())
                    && (DmiEqualsUtil.checkStringEquality(thisObj.getAppGUID(), getAppGUID()))
                    && (thisObj.m_serviceExceptionTraces.equals(m_serviceExceptionTraces))
                    && (thisObj.m_serviceStates.equals(m_serviceStates))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>ApplicationStateDetails</code>.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString() {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Application State Details = ");
        strBuf.append("[");
        strBuf.append("Launch Time = ");
        strBuf.append(m_launchTime);
        strBuf.append(", ");
        strBuf.append("Kill Time = ");
        strBuf.append(m_killTime);
        strBuf.append(", ");
        strBuf.append("Application GUID = ");
        strBuf.append(m_strAppGUID);
        strBuf.append(", ");
        strBuf.append("Application Version = ");
        strBuf.append(m_strAppVersion);
        strBuf.append(", ");

        Enumeration enums = m_serviceStates.keys();

        if (enums != null) {
            strBuf.append("Service States :: ");
            while (enums.hasMoreElements()) {
                String instName = (String) enums.nextElement();

                strBuf.append(instName);
                strBuf.append(", ");

                ServiceInstanceStateDetails status = (ServiceInstanceStateDetails) m_serviceStates.get(instName);

                strBuf.append(status.toString());
                strBuf.append(", ");
            }
        }

        Enumeration traceEnums = m_serviceExceptionTraces.keys();

        if (enums != null) {
            strBuf.append("Service Error Trace = ");
            while (traceEnums.hasMoreElements()) {
                String instName = (String) traceEnums.nextElement();

                strBuf.append(instName);
                strBuf.append(", ");

                String status = (String) m_serviceExceptionTraces.get(instName);

                strBuf.append(status);
                strBuf.append(", ");
            }
        }
        return strBuf.toString();
    }

    public String getApplicationLabel() {
        return applicationLabel;
    }

    public void setApplicationLabel(String applicationLabel) {
        this.applicationLabel = applicationLabel;
    }

    public String getKillException() {
        return killException;
    }

    public void setKillException(String killException) {
        this.killException = killException;
    }

    public String getLaunchException() {
        return launchException;
    }

    public void setLaunchException(String launchException) {
        this.launchException = launchException;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
