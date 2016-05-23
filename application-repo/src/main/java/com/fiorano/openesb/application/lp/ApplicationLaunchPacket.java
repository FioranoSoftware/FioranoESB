/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.lp;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class ApplicationLaunchPacket extends DmiObject
{
    private Vector  m_allServiceLP = new Vector();
    private String  m_appGUID;

    //Added by Bhuvan, server name 21/10/02
    private String  m_serverID;
    private String  m_appVersion;
    private String  m_strUserName;
    private String  m_strPasswd;
    private String  m_appName;
    private String label;
    private boolean deleteDestinationAfterStop;

    //flag determining routes of the application is durable or not
    //this flag is set after considering FES durable route(ApplicationControllerConfig) and fesDurabilityOverride flag(ApplicationReference)
    private boolean routeDurable = false;

    private String  m_defaultAppContext;

    //  Flag indicating whether service needs to be re-fetched during
    //  prepareLaunch
    private boolean m_bNoCache = false;

    //profile to be used
    private String  m_profilePS;

    // List of users remotely allowed to access this application
    private ArrayList<String> remoteUsers;

    // flag indicating component launch order enabled or not
    private boolean componentLaunchOrder = false;


    /**
     *  Constructor for the ApplicationLaunchPacket object
     *
     * @since Tifosi2.0
     */
    public ApplicationLaunchPacket()
    {
    }


    /**
     *  This interface method is called to get the vector containing all the
     *  objects of <code>ServiceLaunchPacket</code>, set for this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @return Vector containing all the objects of ServiceLaunchPacket
     * @see #addServiceLP(ServiceLaunchPacket)
     * @see #removeServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPEnumeration()
     * @see #clearServiceLPVector()
     * @see #getServiceLP(String)
     * @since Tifosi2.0
     */
    public Vector getServiceLPVector()
    {
        return m_allServiceLP;
    }


    /**
     *  This interface method is called to get enumeration containing all the
     *  objects of <code>ServiceLaunchPacket</code>, set for this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @return Enumeration containing all objects of ServiceLaunchPacket
     * @see #addServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPVector()
     * @see #removeServiceLP(ServiceLaunchPacket)
     * @see #clearServiceLPVector()
     * @see #getServiceLP(String)
     * @since Tifosi2.0
     */
    public Enumeration getServiceLPEnumeration()
    {
        return m_allServiceLP.elements();
    }


    /**
     *  This interface method is called to get the object of <code>ServiceLaunchPacket</code>
     *  for the service instance with specified name from this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @param instName name of service instance for which the
     *      servicelaunchpacket is to be obtained
     * @return object of ServiceLaunchPacket for the specified service
     *      instance
     * @see #addServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPVector()
     * @see #removeServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPEnumeration()
     * @see #clearServiceLPVector()
     * @since Tifosi2.0
     */
    public ServiceLaunchPacket getServiceLP(String instName)
    {
        Enumeration enums = m_allServiceLP.elements();

        while (enums.hasMoreElements())
        {
            ServiceLaunchPacket serviceLP = (ServiceLaunchPacket) enums.nextElement();
            String serviceInstName = serviceLP.getServiceInstName();

            if (serviceInstName.equalsIgnoreCase(instName))
            {
                return serviceLP;
            }
        }

        return null;
    }


    /**
     *  This interface method is called to get name of the user who is trying to
     *  launch the application, using this object of <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @return The name of user
     * @see #setUserName(String)
     * @since Tifosi2.0
     */
    public String getUserName()
    {
        return m_strUserName;
    }


    /**
     *  This interface method is called to get name of the user who is trying to
     *  launch the application, using this object of <code>ApplicationLaunchPacket</code>
     *
     * @return The name of user
     * @see #setUserName(String)
     * @since Tifosi2.0
     */
    public boolean getNoCache()
    {
        return m_bNoCache;
    }

    /**
     * This api is called to know whether the component launch order is enabled or not
     *
     * @return component launch order boolean
     */
    public boolean isComponentLaunchOrder() {
        return componentLaunchOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * This method sets whether component launch order status.
     *
     * @param componentLaunchOrder true for enable
     */
    public void setComponentLaunchOrder(boolean componentLaunchOrder) {
        this.componentLaunchOrder = componentLaunchOrder;
    }

    /**
     *  This interface method is called to get GUID of the application to be
     *  launched, using this object of <code>ApplicationLaunchPacket</code>.
     *
     * @return The applicationGUID
     * @see #setApplicationGUID(String)
     * @since Tifosi2.0
     */
    public String getApplicationGUID()
    {
        return m_appGUID;
    }


    /**
     *  This interface method gets the SP name associated with the launch packet
     *
     * @return The serverID value
     * @author Bhuvan
     */
    public String getServerID()
    {
        return m_serverID;
    }


    /**
     *  This interface method is called to get version of the application to be
     *  launched, using this object of <code>ApplicationLaunchPacket</code>.
     *
     * @return The version of application
     * @see #setApplicationVersion(String)
     * @since Tifosi2.0
     */
    public String getApplicationVersion()
    {
        return m_appVersion;
    }


    /**
     *  This interface method is called to get password of the user who is
     *  trying to launch the application, using this object of <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @return password of the user.
     * @see #setPasswd(String)
     * @since Tifosi2.0
     */
    public String getPasswd()
    {
        return m_strPasswd;
    }

    /**
     *  Gets the applicationName attribute of the ApplicationLaunchPacket object
     *
     * @return The applicationName value
     */
    public String getApplicationName()
    {
        return m_appName;
    }

    /**
     *  Gets the deleteDestinationAfterStop attribute of the ApplicationLaunchPacket object
     *
     * @return The deleteDestinationAfterStop value
     */
    public Boolean getDeleteDestinationAfterStop()
    {
        return deleteDestinationAfterStop;
    }
    public void setDeleteDestinationAfterStop(boolean deleteDestinationAfterStop)
    {
        this.deleteDestinationAfterStop=deleteDestinationAfterStop  ;
    }

    /**
     * @return the Default AppContext of the ApplicationLaunchPacket object
     */
    public String getDefaultAppContext()
    {
        return m_defaultAppContext;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APPLICATION_LAUNCHPACKET;
    }


    /**
     *  This interface method is called to get name of the node over which the
     *  service instance with specified name, belonging to the application with
     *  this object <code>ApplicationLaunchPacket</code>, is to be launched.
     *
     * @param serviceInst the name of service instance
     * @return The name of node over which this specified service
     *      instance is launched.
     * @see #getNodeNames()
     * @since Tifosi2.0
     */
    public String getNodeNameForService(String serviceInst)
    {
        for (int i = 0; i < m_allServiceLP.size(); ++i)
        {
            ServiceLaunchPacket slp = (ServiceLaunchPacket) m_allServiceLP.elementAt(i);

            if (slp.getServiceInstName().equalsIgnoreCase(serviceInst))
                return slp.getNodeName();
        }
        return null;
    }


    /**
     *  This interface method is called to get the list of names of all the
     *  nodes, over which the service instances belonging to the application
     *  with this <code>ApplicationLaunchPacket</code>, are to be launched.
     *
     * @return The list of names of all the nodes
     * @exception FioranoException If the call of getting all the node names
     *      fails to complete successfully.
     * @see #getNodeNameForService(String)
     * @since Tifosi2.0
     */
    public String[] getNodeNames()
        throws FioranoException
    {
        Vector nodeNames = new Vector();
        Enumeration enums = m_allServiceLP.elements();

        while (enums.hasMoreElements())
        {
            ServiceLaunchPacket serviceLP = (ServiceLaunchPacket) enums.nextElement();
            String nodeName = serviceLP.getNodeName();

            nodeNames.add(nodeName);
        }

        String[] nodes = new String[nodeNames.size()];

        nodeNames.copyInto(nodes);

        return nodes;
    }


    /**
     *  This API checks whether or not the service specified by instName exists
     *  in this application.
     *
     * @param instName Name of the serviceInstance.
     * @return true if the service exists, false otherwise.
     * @since Tifosi2.0
     */
    public boolean isServiceExist(String instName)
    {
        Enumeration enums = m_allServiceLP.elements();

        while (enums.hasMoreElements())
        {
            ServiceLaunchPacket serviceLP = (ServiceLaunchPacket) enums.nextElement();
            String serviceInstName = serviceLP.getServiceInstName();

            if (serviceInstName.equalsIgnoreCase(instName))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Boolean determining routes is durable or not in Enterprise level
     * @return route durable flag
     */
    public boolean isRouteDurable(){
        return routeDurable;
    }

    /**
     *  This interface method is called to get enumeration of all the objects of
     *  <code>RouteLaunchPacket</code> for the service with specified service
     *  instance name, from this object of <code>ApplicationLaunchPacket</code>.
     *
     * @param serviceInst name of the service instance for which to get
     *      enumeration of RouteLaunchPacket
     * @return Enumeration containing objects of RouteLaunchPacket
     * @since Tifosi2.0
     */
    public Enumeration getRouteLPEnumerationForService(String serviceInst)
    {
        Enumeration enums = m_allServiceLP.elements();

        while (enums.hasMoreElements())
        {
            ServiceLaunchPacket serviceLP = (ServiceLaunchPacket) enums.nextElement();

            if (serviceLP.getServiceInstName().equalsIgnoreCase(serviceInst))
            {
                return serviceLP.getRouteLPEnumeration();
            }
        }
        // return an empty enumeration
        return (new Vector()).elements();
    }


    /**
     * Returns profile PS for object
     *
     * @return
     */
    public String getProfilePS()
    {
        return m_profilePS;
    }


    /**
     *  This interface method is called to set the specified string as name of
     *  the user who is trying to launch the application, using this object of
     *  <code>ApplicationLaunchPacket</code>.
     *
     * @param userName The string to be set as name of user
     * @see #getUserName()
     * @since Tifosi2.0
     */
    public void setUserName(String userName)
    {
        m_strUserName = userName;
    }
    /**
     * Boolean determining routes is durable or not in Enterprise level
     * @param durable
     */
    public void setRouteDurable(boolean durable){
        routeDurable = durable;
    }

    /**
     *  This interface method is called to set the specified string as name of
     *  the user who is trying to launch the application, using this object of
     *  <code>ApplicationLaunchPacket</code>.
     *
     * @param bNoCache
     * @see #getUserName()
     * @since Tifosi2.0
     */
    public void setNoCache(boolean bNoCache)
    {
        m_bNoCache = bNoCache;
    }

    public void setRemoteUsers(ArrayList<String> remoteUsers) {
        this.remoteUsers = remoteUsers;
    }

    public ArrayList<String> getRemoteUsers() {
        return remoteUsers;
    }

    /**
     *  This interface method is called to set the specified string as GUID of
     *  the application to be launched, using this object of <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @param appGUID The string to be set as GUID of application
     * @see #getApplicationGUID()
     * @since Tifosi2.0
     */
    public void setApplicationGUID(String appGUID)
    {
        m_appGUID = appGUID;
    }


    /**
     *  This interface method is used to set the SP name in the launch packet
     *
     * @param serverID The new serverID value
     * @author Bhuvan 21/10/02
     */

    public void setServerID(String serverID)
    {
        m_serverID = serverID;
    }


    /**
     *  This interface method is called to set the specified string as version
     *  of the application to be launched, using this object of <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @param version The string to be set as version of application
     * @see #getApplicationVersion()
     * @since Tifosi2.0
     */
    public void setApplicationVersion(String version)
    {
        m_appVersion = version;
    }


    /**
     *  This interface method is called to set the specified string as password
     *  of the user who is trying to launch the application, using this object
     *  of <code>ApplicationLaunchPacket</code>.
     *
     * @param passwd The string to be set as password
     * @see #getPasswd()
     * @since Tifosi2.0
     */
    public void setPasswd(String passwd)
    {
        m_strPasswd = passwd;
    }

    /**
     *  Sets the applicationName attribute of the ApplicationLaunchPacket object
     *
     * @param appName The new applicationName value
     */
    public void setApplicationName(String appName)
    {
        m_appName = appName;
    }


    /**
     *  Set the Default application context of the ApplicationLaunchPacket
     *
     * @param defaultAppContext The default application context value
     */
    public void setDefaultAppContext(String defaultAppContext)
    {
        m_defaultAppContext = defaultAppContext;
    }

    /**
     * Sets profile PS for object
     *
     * @param profilePS
     */
    public void setProfilePS(String profilePS)
    {
        m_profilePS = profilePS;
    }


    /**
     *  This interface method is called to add the specified object of <code>ServiceLaunchPacket</code>
     *  to the list of service launch packets, for this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @param serviceLP The object of ServiceLaunchPacket to be added
     * @see #getServiceLPVector()
     * @see #removeServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPEnumeration()
     * @see #clearServiceLPVector()
     * @see #getServiceLP(String)
     * @since Tifosi2.0
     */
    public void addServiceLP(ServiceLaunchPacket serviceLP)
    {
        m_allServiceLP.add(serviceLP);
    }


    /**
     *  This interface method is called to remove the specified object of <code>ServiceLaunchPacket</code>
     *  from the list of service launch packets, for this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @param serviceLP Object of ServiceLaunchPacket to be removed.
     * @see #addServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPVector()
     * @see #getServiceLPEnumeration()
     * @see #clearServiceLPVector()
     * @see #getServiceLP(String)
     * @since Tifosi2.0
     */
    public void removeServiceLP(ServiceLaunchPacket serviceLP)
    {
        m_allServiceLP.remove(serviceLP);
    }


    /**
     *  This interface method is called to clear the vector containing all the
     *  objects of <code>ServiceLaunchPacket</code>, set for this <code>ApplicationLaunchPacket</code>
     *  .
     *
     * @see #addServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPVector()
     * @see #removeServiceLP(ServiceLaunchPacket)
     * @see #getServiceLPEnumeration()
     * @see #getServiceLP(String)
     * @since Tifosi2.0
     */
    public void clearServiceLPVector()
    {
        m_allServiceLP.clear();
    }

    /**
     *  This method is called to write this object of <code>ApplicationLaunchPacket</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.
     * @see #fromStream
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        writeUTF(out, m_appGUID);

        writeUTF(out, m_appName);
        writeUTF(out, m_defaultAppContext);

        //Added by Bhuvan 22/10/02
        //write server name to the stream
        writeUTF(out, m_serverID);
        writeUTF(out, m_appVersion);
        writeUTF(out, m_strUserName);
        writeUTF(out, m_strPasswd);
        writeUTF(out, label);

        if(remoteUsers != null && remoteUsers.size() > 0){
            out.writeInt(remoteUsers.size());
            for(String remoteUser : remoteUsers)
                writeUTF(out, remoteUser);
        }else
            out.writeInt(-1);

        out.writeBoolean(deleteDestinationAfterStop);
        out.writeBoolean(m_bNoCache);
        out.writeBoolean(routeDurable);
        if (m_allServiceLP != null)
        {
            int size = m_allServiceLP.size();

            out.writeInt(size);
            for (int i = 0; i < size; i++)
            {
                ((ServiceLaunchPacket) m_allServiceLP.get(i)).toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
        if (m_profilePS == null)
        {
            out.writeInt(0);
        }
        else
        {
            out.writeInt(1);
            out.writeUTF(m_profilePS);
        }

    }


    /**
     *  This is called to read this object <code>ApplicationLaunchPacket</code>
     *  from the specified object of input stream.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.
     * @see #toStream
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_appGUID = readUTF(is);

        m_appName = readUTF(is);
        m_defaultAppContext = readUTF(is);

        //Added by Bhuvan 22/10/02
        m_serverID = readUTF(is);

        m_appVersion = readUTF(is);
        m_strUserName = readUTF(is);
        m_strPasswd = readUTF(is);
        label = readUTF(is);

        int numRemoteUsers = is.readInt();
        if(numRemoteUsers > 0){
            remoteUsers = new ArrayList<String>();
            for(int i = 0; i < numRemoteUsers; i++){
                String remoteUser = readUTF(is);
                remoteUsers.add(remoteUser);
            }
        }

        deleteDestinationAfterStop = is.readBoolean();
        m_bNoCache = is.readBoolean();
        routeDurable = is.readBoolean();
        int tempInt = is.readInt();

        if (tempInt != 0)
        {
            m_allServiceLP = new Vector();
            for (int i = 0; i < tempInt; i++)
            {
                ServiceLaunchPacket serviceLP = new ServiceLaunchPacket();

                serviceLP.fromStream(is, versionNo);
                m_allServiceLP.add(serviceLP);
            }
        }

        tempInt = is.readInt();
        if (tempInt != 0)
        {
            m_profilePS = is.readUTF();
        }
    }


    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>ApplicationLaunchPacket</code>.
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
        strBuf.append("Service Launch Packet Details ");
        strBuf.append("[");
        strBuf.append("ApplicationGUID = ");
        strBuf.append(m_appGUID);
        strBuf.append(", ");
        strBuf.append("Application Name = ");
        strBuf.append(m_appName);
        strBuf.append(", ");
        strBuf.append("Default AppContext = ");
        strBuf.append(m_defaultAppContext);
        strBuf.append(", ");
        strBuf.append("Application Version = ");
        strBuf.append(m_appVersion);
        strBuf.append(", ");
        strBuf.append("User Name = ");
        strBuf.append(m_strUserName);
        strBuf.append(", ");
        strBuf.append("Password = ");
        strBuf.append(m_strPasswd);
        strBuf.append(", ");
        strBuf.append("NoCache = ");
        strBuf.append(m_bNoCache);
        strBuf.append(", ");
        if (m_allServiceLP != null)
        {
            strBuf.append("Service Launch Packet = ");
            for (int i = 0; i < m_allServiceLP.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((ServiceLaunchPacket) m_allServiceLP.get(i)));
                strBuf.append(", ");
            }
        }
        if (m_profilePS != null)
        {
            strBuf.append("Profile = ");
            strBuf.append(m_profilePS);

        }

        strBuf.append("]");

        return strBuf.toString();
    }


    /**
     *  This method tests whether this object of <code>ApplicationLaunchPacket</code>
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
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }

}
