/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.lp;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.application.*;
import com.fiorano.openesb.application.aps.PortInstConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.service.LogModule;
import com.fiorano.openesb.utils.DmiEqualsUtil;
import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class RouteLaunchPacket extends DmiObject
{
    // whether persitant Route
    boolean         m_bIsDebugPointSet;

    String          m_routeName;

    String          m_routeGUID;

    String          m_strTrgtAppGUID;

    String          m_strTrgtAppVersion;

    String          m_trgtServInst;

    String          m_trgtNodeName;

    String          m_trgtActualServInst;

    String          m_srcPortName;

    String          m_srcPortType;

    String          m_srcPortUserName;

    String          m_srcPortPasswd;

    String          m_trgtPortName;

    String          m_trgtPortType;

    String          m_trgtPortUserName;

    String          m_trgtPortPasswd;

    String          m_trgtTransportProtocol;
    String          m_trgtSecurityProtocol;
    String          m_trgtSecurityManager;

    // proxy parameters
    boolean         m_bEnableTrgtPortProxy;
    String          m_trgtProxyURL;
    String          m_trgtProxyPrincipal;
    String          m_trgtProxyCredentials;
    String          m_trgtProxyAuthenticationRealm;

    HashMap         m_selectors;

    String          m_shortDescription;

    String          m_longDescription;

    MessageTransformation transformation;

    boolean         m_isDurable = false;

    private boolean specifiedSourceDestinationUsed;
    private boolean specifiedTargetDestinationUsed;
    private String specifiedSrcPortName;
    private String specifiedTrgtPortName;

    private boolean isSrcEncrypted;
    private String srcAlgo;
    private String srcKey;
    private boolean srcAllowPaddingToKey;
    private String srcInitializationVector;

    private boolean isTargetEncrypted;
    private String tgtAlgo;
    private String tgtKey;
    private boolean tgtAllowPaddingToKey;
    private String tgtInitializationVector;

    /**
     *  Constructor for the RouteLaunchPacket object
     *
     * @since Tifosi2.0
     */
    public RouteLaunchPacket()
    {
    }

    public boolean isM_bIsDebugPointSet() {
        return m_bIsDebugPointSet;
    }

    public void setM_bIsDebugPointSet(boolean m_bIsDebugPointSet) {
        this.m_bIsDebugPointSet = m_bIsDebugPointSet;
    }

    public boolean isSrcEncrypted() {
        return isSrcEncrypted;
    }

    public void setSrcEncrypted(boolean srcEncrypted) {
        isSrcEncrypted = srcEncrypted;
    }

    public String getSrcAlgo() {
        return srcAlgo;
    }

    public void setSrcAlgo(String srcAlgo) {
        this.srcAlgo = srcAlgo;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public void setSrcKey(String srcKey) {
        this.srcKey = srcKey;
    }

    public boolean isSrcAllowPaddingToKey() {
        return srcAllowPaddingToKey;
    }

    public void setSrcAllowPaddingToKey(boolean srcAllowPaddingToKey) {
        this.srcAllowPaddingToKey = srcAllowPaddingToKey;
    }

    public String getSrcInitializationVector() {
        return srcInitializationVector;
    }

    public void setSrcInitializationVector(String srcInitializationVector) {
        this.srcInitializationVector = srcInitializationVector;
    }

    public boolean isTargetEncrypted() {
        return isTargetEncrypted;
    }

    public void setTargetEncrypted(boolean targetEncrypted) {
        isTargetEncrypted = targetEncrypted;
    }

    public String getTgtAlgo() {
        return tgtAlgo;
    }

    public void setTgtAlgo(String tgtAlgo) {
        this.tgtAlgo = tgtAlgo;
    }

    public String getTgtKey() {
        return tgtKey;
    }

    public void setTgtKey(String tgtKey) {
        this.tgtKey = tgtKey;
    }

    public boolean isTgtAllowPaddingToKey() {
        return tgtAllowPaddingToKey;
    }

    public void setTgtAllowPaddingToKey(boolean tgtAllowPaddingToKey) {
        this.tgtAllowPaddingToKey = tgtAllowPaddingToKey;
    }

    public String getTgtInitializationVector() {
        return tgtInitializationVector;
    }

    public void setTgtInitializationVector(String tgtInitializationVector) {
        this.tgtInitializationVector = tgtInitializationVector;
    }

    public boolean isDurable()
    {
        return m_isDurable;
    }

    public MessageTransformation getTransformation(){
        return transformation;
    }


    public void setTransformation(MessageTransformation transformation){
        this.transformation = transformation;
    }

    /**
     *  This interface method is called to check whether or not a debug point is
     *  set on the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch time information.
     *
     * @return true if debug point is set on this route, false otherwise
     * @see #setIsDebugPointSet(boolean)
     * @since Tifosi2.0
     */
    public boolean isDebugPointSet()
    {
        return m_bIsDebugPointSet;
    }


    /**
     *  This interface method is called to get name of the route, for which this
     *  object of <code>RouteLaunchPacket</code> contains launch time information.
     *
     * @return name for this route
     * @see #setRouteName(String)
     * @since Tifosi2.0
     */
    public String getRouteName()
    {
        return m_routeName;
    }

    /**
     *  This interface method is called to get GUID of the route, for which this
     *  object of <code>RouteLaunchPacket</code> contains launch time information.
     *
     * @return GUID of this route
     * @see #setRouteGUID(String)
     * @since Tifosi2.0
     */
    public String getRouteGUID()
    {
        return m_routeGUID;
    }

    /**
     *  This interface method is called to get GUID of the application, to which
     *  belongs the target service instance of the route, and for which this object
     *  of <code>RouteLaunchPacket</code> contains launch time information.
     *
     * @return GUID of application, to which the target service instance of
     *            this route belongs.
     * @see #setTargetApplicationGUID(String)
     * @since Tifosi2.0
     */
    public String getTargetApplicationGUID()
    {
        return m_strTrgtAppGUID;
    }

    /**
     *  This interface method is called to get version of the application, to which
     *  belongs the target service instance of the route, and for which this object
     *  of <code>RouteLaunchPacket</code> contains launch time information.
     *
     * @return version of application, to which the target service instance of
     *            this route belongs.
     * @see #setTargetAppVersion(String)
     * @since Tifosi2.0
     */
    public String getTargetAppVersion()
    {
        return m_strTrgtAppVersion;
    }



    /**
     *  This interface method is called to get the node name on which the source service
     *  instance of this route is running.
     *
     * @return Name of node on which the source service instance of this route
     *            is running.
     * @see #setSrcPortName(String)
     * @since Tifosi2.0
     */
    public String getSrcPortName()
    {
        return m_srcPortName;
    }


    /**
     *  This interface method is called to get the source port type.
     *
     * @return String
     */
    public String getSrcPortType()
    {
        return m_srcPortType;
    }

    /**
     *  This interface method is called to get the source port type.
     *
     * @return String
     */
    public String getSrcPortUserName()
    {
        return m_srcPortUserName;
    }

    /**
     *  This interface method is called to get the source port password
     *
     * @return String
     */
    public String getSrcPortPassword()
    {
        return m_srcPortPasswd;
    }

    /**
     *  This interface method is called to get the target port type.
     *
     * @return String
     */
    public String getTrgtPortType()
    {
        return m_trgtPortType;
    }

    /**
     *  This interface method is called to get the source port type.
     *
     * @return String
     */
    public String getTrgtPortUserName()
    {
        return m_trgtPortUserName;
    }

    /**
     *  This interface method is called to get the source port password
     *
     * @return String
     */
    public String getTrgtPortPassword()
    {
        return m_trgtPortPasswd;
    }

    /**
     *  This interface method is called to get the transport protocol
     *
     * @return String
     */
    public String getTrgtTransportProtocol()
    {
        return m_trgtTransportProtocol;
    }

    /**
     *  This interface method is called to get the security protocol
     *
     * @return String
     */
    public String getTrgtSecurityProtocol()
    {
        return m_trgtSecurityProtocol;
    }

    /**
     *  This interface method is called to get the security manager
     *
     * @return String
     */
    public String getTrgtSecurityManager()
    {
        return m_trgtSecurityManager;
    }

    /**
     *  This interface method is called to get the proxy url
     *
     * @return String
     */
    public String getTrgtProxyURL()
    {
        return m_trgtProxyURL;
    }

    /**
     *  This interface method is called to get the proxy url
     *
     * @return boolean
     */
    public boolean isTrgtPortProxyEnabled()
    {
        return m_bEnableTrgtPortProxy;
    }

    /**
     *  This interface method is called to get the proxy principal
     *
     * @return String
     */
    public String getTrgtProxyPrincipal()
    {
        return m_trgtProxyPrincipal;
    }

    /**
     *  This interface method is called to get the proxy principal
     *
     * @return String
     */
    public String getTrgtProxyCredentials()
    {
        return m_trgtProxyCredentials;
    }

    /**
     *  This interface method is called to get the proxy authrntication realm
     *
     * @return String
     */
    public String getTrgtProxyAuthReam()
    {
        return m_trgtProxyAuthenticationRealm;
    }

    /**
     *  This interface method is called to get the name of target service instance for
     *  the route, for this object of <code>RouteLaunchPacket</code>.
     *
     * @return The name of target service instance for this route
     * @see #setTrgtServInst(String)
     * @since Tifosi2.0
     */
    public String getTrgtServInst()
    {
        return m_trgtServInst;
    }

    /**
     *  This interface method is called to get the name of node, over which the target
     *  service instance for this route is running.
     *
     * @return The name of node over which the target service instance of this
     *            route is running
     * @see #setTrgtNodeName(String)
     * @since Tifosi2.0
     */
    public String getTrgtNodeName()
    {
        return m_trgtNodeName;
    }

    /**
     *   This interface method is called to get the actual instance name of the target
     *   service instance for this route. This is applicable to externally launched
     *   services, which are in fact part of some other application.
     *
     * @return The actual instance name of the target service instance for this
     *            route
     * @see #setActualTrgtServInst(String)
     * @since Tifosi2.0
     */
    public String getActualTrgtServInst()
    {
        return m_trgtActualServInst;
    }


    /**
     *  This interface method is called to get the name of target port for the route,
     *  for which this object of <code>RouteLaunchPacket</code> contains launch
     *  information.
     *
     * @return The name of target port for this route
     * @see #setTrgtPortName(String)
     * @since Tifosi2.0
     */
    public String getTrgtPortName()
    {
        return m_trgtPortName;
    }


    /**
     *  This interface method is called to get the short description for the route,
     *  for which this object of <code>RouteLaunchPacket</code> contains launch
     *  information.
     *
     * @return The short description for this route
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_shortDescription;
    }


    /**
     *  This interface method is called to get the long description for the route,
     *  for which this object of <code>RouteLaunchPacket</code> contains launch
     *  information.
     *
     * @return The long description for this route
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_longDescription;
    }

    /**
     *  This interface method is called to get selector for the route, for which this
     *  object of <code>RouteLaunchPacket</code> contains launch information.
     *
     * @return selector for this route
     * @since Tifosi2.0
     */
    public HashMap getSelectors()
    {
        return m_selectors;
    }

    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.ROUTE_LAUNCHPACKET;
    }


    /**
     *  This interface method is called to get the transport protocol
     *
     * @param transportProtocol
     */
    public void setTrgtTransportProtocol(String transportProtocol)
    {
        m_trgtTransportProtocol = transportProtocol;
    }

    /**
     *  This interface method is called to get the security protocol
     *
     * @param securityProtocol
     */
    public void setTrgtSecurityProtocol(String securityProtocol)
    {
        m_trgtSecurityProtocol = securityProtocol;
    }

    /**
     *  This interface method is called to get the security manager
     *
     * @param securityManager
     */
    public void setTrgtSecurityManager(String securityManager)
    {
        m_trgtSecurityManager = securityManager;
    }

    /**
     *  This interface method is called to get the proxy url
     *
     * @param proxyURL
     */
    public void setTrgtProxyURL(String proxyURL)
    {
        m_trgtProxyURL = proxyURL;
    }

    /**
     *  This interface method is called to get the proxy url
     *
     * @param portProxy
     */
    public void setTrgtPortProxyEnabled(boolean portProxy)
    {
        m_bEnableTrgtPortProxy = portProxy;
    }

    /**
     *  This interface method is called to get the proxy principal
     *
     * @param proxyPrincipal
     */
    public void setTrgtProxyPrincipal(String proxyPrincipal)
    {
        m_trgtProxyPrincipal = proxyPrincipal;
    }

    /**
     *  This interface method is called to get the proxy principal
     *
     * @param proxyCredentials
     */
    public void setTrgtProxyCredentials(String proxyCredentials)
    {
        m_trgtProxyCredentials = proxyCredentials;
    }

    /**
     *  This interface method is called to get the proxy authrntication realm
     *
     * @param proxyAuthRealm
     */
    public void setTrgtProxyAuthReam(String proxyAuthRealm)
    {
        m_trgtProxyAuthenticationRealm = proxyAuthRealm;
    }

    public void setIsDurable(boolean durable)
    {
        m_isDurable = durable;
    }


    /**
     *  This interface method is called to set a boolean specifying whether or not
     *  debug point is set on the route, for which this object
     *  of <code>RouteLaunchPacket</code> contains launch time information.
     *
     * @param isDebugPointSet boolean specifying whether or not debug point is set
     *                         for this route.
     * @see #isDebugPointSet()
     * @since Tifosi2.0
     */
    public void setIsDebugPointSet(boolean isDebugPointSet)
    {
        m_bIsDebugPointSet = isDebugPointSet;
    }

    /**
     *  This interface method is called to set the specified string as name of
     *  the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch time information.
     *
     * @param routeName The string to be set as name for this route
     * @see #getRouteName()
     * @since Tifosi2.0
     */
    public void setRouteName(String routeName)
    {
        m_routeName = routeName;
    }

    /**
     *  This interface method is called to set the specified string as GUID of
     *  the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch time information.
     *
     * @param routeGUID The string to be set as GUID for this route
     * @see #getRouteGUID()
     * @since Tifosi2.0
     */
    public void setRouteGUID(String routeGUID)
    {
        m_routeGUID = routeGUID;
    }

    /**
     *  This interface method is called to set the specified string as GUID of
     *  the application, to which belongs the target service instance of the route,
     *  and for which this object of <code>RouteLaunchPacket</code> contains
     *  launch time information.
     *
     * @param appGUID The string to be set as GUID of the application, to which the
     *                 target service instance of this route belongs.
     * @since Tifosi2.0
     */
    public void setTargetApplicationGUID(String appGUID)
    {
        m_strTrgtAppGUID = appGUID;
    }

    /**
     *  This interface method is called to set the specified string as version of
     *  the application, to which belongs the target service instance of the route,
     *  and for which this object of <code>RouteLaunchPacket</code> contains
     *  launch time information.
     *
     * @param appVersion The string to be set as version of the application, to which the
     *                 target service instance of this route belongs.
     * @since Tifosi2.0
     */
    public void setTargetAppVersion(String appVersion)
    {
        m_strTrgtAppVersion = appVersion;
    }


    /**
     *  This interface method is called to set the specified string as the node
     *  name on which the source service instance of this route is running.
     *
     * @param srcPortName The string to be set as the node on which the source service
     *                     instance of this route is running.
     * @see #getSrcPortName()
     * @since Tifosi2.0
     */
    public void setSrcPortName(String srcPortName)
    {
        m_srcPortName = srcPortName;
    }

    /**
     *  This interface method is called to set the source port type
     *
     * @param srcPortType
     * @since Tifosi2.0
     */
    public void setSrcPortType(String srcPortType)
    {
        m_srcPortType = srcPortType;
    }

    /**
     *  This interface method is called to set the source username
     *
     * @param srcUserName
     */
    public void setSrcPortUserName(String srcUserName)
    {
        m_srcPortUserName = srcUserName;
    }

    /**
     *  This interface method is called to set the source port password
     *
     * @param password
     */
    public void setSrcPortPassword(String password)
    {
        m_srcPortPasswd = password;
    }


    /**
     *  This interface method is called to set the source port type
     *
     * @param targetPortType
     * @since Tifosi2.0
     */
    public void setTrgtPortType(String targetPortType)
    {
        m_trgtPortType = targetPortType;
    }

    /**
     *  This interface method is called to set the source username
     *
     * @param targetUserName
     */
    public void setTrgtPortUserName(String targetUserName)
    {
        m_trgtPortUserName = targetUserName;
    }

    /**
     *  This interface method is called to set the source port password
     *
     * @param password
     */
    public void setTrgtPortPassword(String password)
    {
        m_trgtPortPasswd = password;
    }


    /**
     *  This interface method is called to set the specified string as name of the
     *  target service instance for the route, for this object of
     *  <code>RouteLaunchPacket</code>.
     *
     * @param trgtServInst The string to be set as target service instance name
     *                      for this route
     * @see #getTrgtServInst()
     * @since Tifosi2.0
     */
    public void setTrgtServInst(String trgtServInst)
    {
        m_trgtServInst = trgtServInst;
    }

    /**
     *  This interface method is called to set the specified string as name of
     *  the node, over which the target service instance for this route is running.
     *
     * @param tgtNode The string to be set as the node name over which target service
     *                 instance of this route is running.
     * @see #getTrgtNodeName()
     * @since Tifosi2.0
     */
    public void setTrgtNodeName(String tgtNode)
    {
        m_trgtNodeName = tgtNode;
    }

    /**
     * This interface method is called to set the specified string as actual instance name
     * of the target service instance for this route. This is applicable to externally
     * launched services, which are in fact part of some other application.
     *
     * @param trgtActualServInst The string to be set as actual instance name for
     *                            the target service instance of this route.
     * @see #getActualTrgtServInst()
     * @since Tifosi2.0
     */
    public void setActualTrgtServInst(String trgtActualServInst)
    {
        m_trgtActualServInst = trgtActualServInst;
    }

    /**
     *  This interface method is called to set the specified string as name of the
     *  target port for the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch information.
     *
     * @param trgtPortName The string to be set as target port for this route
     * @see #getTrgtPortName()
     * @since Tifosi2.0
     */
    public void setTrgtPortName(String trgtPortName)
    {
        m_trgtPortName = trgtPortName;
    }


    /**
     *  This interface method is called to set the specified string as short description
     *  for the route, for which this object of <code>RouteLaunchPacket</code>
     *   contains launch information.
     *
     * @param shortDescription The string to be set as short description for this route
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String shortDescription)
    {
        m_shortDescription = shortDescription;
    }


    /**
     *  This interface method is called to set the specified string as long description
     *  for the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch information.
     *
     * @param longDescription The string to be set as long description for this route
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String longDescription)
    {
        m_longDescription = longDescription;
    }

    /**
     *  This interface method is called to set the specified string as selector for the
     *  route, for which this object of <code>RouteLaunchPacket</code> contains
     *  launch information.
     *
     * @param selectors
     * @since Tifosi2.0
     */
    public void setSelectors(HashMap selectors)
    {
        m_selectors = selectors;
    }

    /**
     *  This interface method is called to set the specified string as type of selector
     *  for the route, for which this object of <code>RouteLaunchPacket</code>
     *  contains launch information.
     *
     * @param selectorType The string to be set as type of selector for this route
     * @param selector The feature to be added to the Selector attribute
     * @since Tifosi2.0
     */
    public void addSelector(String selectorType, String selector)
    {
        if (m_selectors == null)
            m_selectors = new HashMap();

        if(selectorType.equalsIgnoreCase(Route.SELECTOR_APPLICATION_CONTEXT) ||
                selectorType.equalsIgnoreCase(Route.SELECTOR_BODY))
        {
            try {
                addXPathSelector(selectorType, selector, null);
            } catch (FioranoException e) {
                // ignore as this cannot happen.
            }
            return;
        }
        m_selectors.put(selectorType, selector);
    }

    /**
     * Adds a feature to the XPathSelector attribute of the RouteLaunchPacket object
     *
     * @param type The feature to be added to the XPathSelector attribute
     * @param value The feature to be added to the XPathSelector attribute
     * @param namespaces The feature to be added to the XPathSelector attribute
     */
    public void addXPathSelector(String type, String value, Properties namespaces)
            throws FioranoException
    {
        if (m_selectors == null)
            m_selectors = new HashMap();
        // Even if namespaces are passed as null. we create the XPath DMI.
        XPathSelector xpath;

        if(type.equalsIgnoreCase(Route.SELECTOR_APPLICATION_CONTEXT) ||
                type.equalsIgnoreCase(Route.SELECTOR_BODY)){
            xpath = new XPathSelector(value, namespaces);
        }else
            throw new FioranoException("XPATH type : "+type+" not VALID. Please refer to API doc for valid values.");
        m_selectors.put(type, xpath);
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
     *  This method tests whether this object of <code>RouteLaunchPacket</code>
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
     *  This method is called to write this object of <code>RouteLaunchPacket</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.
     * @see #fromStream(DataInput, int)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeBoolean(m_isDurable);

        out.writeBoolean(compressed);

        out.writeBoolean(encrypted);

        DmiObject.toStream(transformation, out, versionNo);

        out.writeBoolean(m_bIsDebugPointSet);

        if (m_routeName != null)
        {
            UTFReaderWriter.writeUTF(out, m_routeName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_routeGUID != null)
        {
            UTFReaderWriter.writeUTF(out, m_routeGUID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strTrgtAppGUID != null)
        {
            UTFReaderWriter.writeUTF(out, m_strTrgtAppGUID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strTrgtAppVersion != null)
        {
            UTFReaderWriter.writeUTF(out, m_strTrgtAppVersion);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }


        if (m_trgtServInst != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtServInst);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtNodeName != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtNodeName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtActualServInst != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtActualServInst);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }
        if (m_srcPortName != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (errorPortName != null)
        {
            UTFReaderWriter.writeUTF(out, errorPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_srcPortType != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcPortType);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if(errorPortType!=null){
            UTFReaderWriter.writeUTF(out, errorPortType);
        } else{
            UTFReaderWriter.writeUTF(out, "");
        }

        out.writeBoolean(specifiedSourceDestinationUsed);
        out.writeBoolean(specifiedTargetDestinationUsed);

        if (specifiedSrcPortName != null)
        {
            UTFReaderWriter.writeUTF(out, specifiedSrcPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (specifiedTrgtPortName != null)
        {
            UTFReaderWriter.writeUTF(out, specifiedTrgtPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }
        
        if (m_srcPortUserName != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcPortUserName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_srcPortPasswd != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcPortPasswd);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtPortName != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtPortType != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtPortType);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtPortUserName != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtPortUserName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtPortPasswd != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtPortPasswd);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtTransportProtocol != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtTransportProtocol);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtSecurityProtocol != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtSecurityProtocol);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtSecurityManager != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtSecurityManager);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        out.writeBoolean(m_bEnableTrgtPortProxy);

        if (m_trgtProxyURL != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtProxyURL);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtProxyPrincipal != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtProxyPrincipal);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtProxyCredentials != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtProxyCredentials);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtProxyAuthenticationRealm != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtProxyAuthenticationRealm);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_selectors != null)
        {
            int size = m_selectors.size();

            out.writeInt(size);

            Set keys = m_selectors.keySet();
            Iterator itr = keys.iterator();

            while (itr.hasNext())
            {
                String type = (String) itr.next();
                Object obj = m_selectors.get(type);

                if (type == null)
                    type = "";

                UTFReaderWriter.writeUTF(out, type);
                if(obj instanceof XPathSelector)
                    DmiObject.toStream((XPathSelector)obj, out, versionNo);
                else
                    UTFReaderWriter.writeUTF(out, (String)obj);
            }
        }
        else
        {
            out.writeInt(0);
        }

        if (m_shortDescription != null)
        {
            UTFReaderWriter.writeUTF(out, m_shortDescription);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_longDescription != null)
        {
            UTFReaderWriter.writeUTF(out, m_longDescription);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (isSrcEncrypted) {
            out.writeBoolean(isSrcEncrypted);
            UTFReaderWriter.writeUTF(out, srcAlgo);
            UTFReaderWriter.writeUTF(out, srcKey);
            out.writeBoolean(srcAllowPaddingToKey);
            UTFReaderWriter.writeUTF(out, srcInitializationVector);
        } else {
            out.writeBoolean(false);
        }


        if (isTargetEncrypted) {
            out.writeBoolean(isTargetEncrypted);
            UTFReaderWriter.writeUTF(out, tgtAlgo);
            UTFReaderWriter.writeUTF(out, tgtKey);
            out.writeBoolean(tgtAllowPaddingToKey);
            UTFReaderWriter.writeUTF(out, tgtInitializationVector);
        } else {
            out.writeBoolean(false);
        }

        if(logManager ==null)
            logManager = new LogManager();
        logManager.toStream(out, versionNo);
        toStream(logModules, out, versionNo);
    }


    /**
     *  This is called to read this object <code>RouteLaunchPacket</code>
     *  from the specified object of input stream.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.
     * @see #toStream(DataOutput, int)
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_isDurable = is.readBoolean();

        compressed = is.readBoolean();

        encrypted = is.readBoolean();

        transformation = (MessageTransformation)DmiObject.fromStream(DmiObjectTypes.NEW_MESSAGE_TRANSFORMATION, is, versionNo);

        m_bIsDebugPointSet = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
        {
            m_routeName = null;
        }
        else
        {
            m_routeName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_routeGUID = null;
        }
        else
        {
            m_routeGUID = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strTrgtAppGUID = null;
        }
        else
        {
            m_strTrgtAppGUID = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strTrgtAppVersion = null;
        }
        else
        {
            m_strTrgtAppVersion = temp;
        }


        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtServInst = null;
        }
        else
        {
            m_trgtServInst = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtNodeName = null;
        }
        else
        {
            m_trgtNodeName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtActualServInst = null;
        }
        else
        {
            m_trgtActualServInst = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcPortName = null;
        }
        else
        {
            m_srcPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            errorPortName = null;
        }
        else
        {
            errorPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcPortType = null;
        }
        else
        {
            m_srcPortType = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            errorPortType = null;
        }
        else
        {
            errorPortType = temp;
        }

        specifiedSourceDestinationUsed = is.readBoolean();
        specifiedTargetDestinationUsed = is.readBoolean();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            specifiedSrcPortName = null;
        }
        else
        {
            specifiedSrcPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            specifiedTrgtPortName = null;
        }
        else
        {
            specifiedTrgtPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcPortUserName = null;
        }
        else
        {
            m_srcPortUserName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcPortPasswd = null;
        }
        else
        {
            m_srcPortPasswd = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtPortName = null;
        }
        else
        {
            m_trgtPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtPortType = null;
        }
        else
        {
            m_trgtPortType = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtPortUserName = null;
        }
        else
        {
            m_trgtPortUserName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtPortPasswd = null;
        }
        else
        {
            m_trgtPortPasswd = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtTransportProtocol = null;
        }
        else
        {
            m_trgtTransportProtocol = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtSecurityProtocol = null;
        }
        else
        {
            m_trgtSecurityProtocol = temp;
        }

        m_bEnableTrgtPortProxy = is.readBoolean();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtSecurityManager = null;
        }
        else
        {
            m_trgtSecurityManager = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtProxyURL = null;
        }
        else
        {
            m_trgtProxyURL = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtProxyPrincipal = null;
        }
        else
        {
            m_trgtProxyPrincipal = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtProxyCredentials = null;
        }
        else
        {
            m_trgtProxyCredentials = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtProxyAuthenticationRealm = null;
        }
        else
        {
            m_trgtProxyAuthenticationRealm = temp;
        }

        _readSelectors(is, versionNo);

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_shortDescription = null;
        }
        else
        {
            m_shortDescription = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_longDescription = null;
        }
        else
        {
            m_longDescription = temp;
        }

        isSrcEncrypted = is.readBoolean();
        if (isSrcEncrypted) {
            srcAlgo = UTFReaderWriter.readUTF(is);
            srcKey = UTFReaderWriter.readUTF(is);
            srcAllowPaddingToKey = is.readBoolean();
            srcInitializationVector = UTFReaderWriter.readUTF(is);
        }

        isTargetEncrypted = is.readBoolean();
        if (isTargetEncrypted) {
            tgtAlgo = UTFReaderWriter.readUTF(is);
            tgtKey = UTFReaderWriter.readUTF(is);
            tgtAllowPaddingToKey = is.readBoolean();
            tgtInitializationVector = UTFReaderWriter.readUTF(is);
        }

        logManager = new LogManager();
        logManager.fromStream(is, versionNo);
        fromStream(logModules,DmiObjectTypes.NEW_LOGMODULE, is, versionNo);
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>RouteLaunchPacket</code>.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Route Details ");
        strBuf.append("[");
        strBuf.append("Route Name = ");
        strBuf.append(m_routeName);
        strBuf.append(", ");
        strBuf.append("Route GUID = ");
        strBuf.append(m_routeGUID);
        strBuf.append(", ");
        strBuf.append("Target App GUID = ");
        strBuf.append(m_strTrgtAppGUID);
        strBuf.append(", ");
        strBuf.append("Target App Version = ");
        strBuf.append(m_strTrgtAppVersion);
        strBuf.append(", ");
        strBuf.append(", ");
        strBuf.append("Are Messages Compressed = ");
        strBuf.append(compressed);
        strBuf.append("Are Messages Encrypted = ");
        strBuf.append(encrypted);
        strBuf.append(", ");
        strBuf.append("transformation");
        strBuf.append(transformation);
        strBuf.append(", ");
        strBuf.append("Is Debug Point Set = ");
        strBuf.append(m_bIsDebugPointSet);
        strBuf.append(", ");
        strBuf.append("Time to Live = ");
        strBuf.append(", ");
        strBuf.append("Target Service Instance = ");
        strBuf.append(m_trgtServInst);
        strBuf.append(", ");
        strBuf.append("Actual Target Service Instance = ");
        strBuf.append(m_trgtActualServInst);
        strBuf.append(", ");
        strBuf.append("Target Node Name = ");
        strBuf.append(m_trgtNodeName);
        strBuf.append(", ");
        strBuf.append("Selectors = ");
        strBuf.append(m_selectors);

        strBuf.append(", ");
        strBuf.append("Short Description= ");
        strBuf.append(m_shortDescription);
        strBuf.append(", ");
        strBuf.append("Long Description= ");
        strBuf.append(m_longDescription);

        strBuf.append("]");
        return strBuf.toString();
    }

    /**
     *  This utility method is used to compare this object of
     *  <code>RouteLaunchPacket</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof RouteLaunchPacket))
            return false;
        RouteLaunchPacket thisObj = (RouteLaunchPacket)obj;

        return DmiEqualsUtil.checkStringEquality(thisObj.getActualTrgtServInst(), getActualTrgtServInst())
                && DmiEqualsUtil.checkStringEquality(thisObj.getRouteGUID(), getRouteGUID())
                && DmiEqualsUtil.checkStringEquality(thisObj.getRouteName(), getRouteName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSrcPortName(), getSrcPortName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSrcPortType(), getSrcPortType())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSrcPortUserName(), getSrcPortUserName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSrcPortPassword(), getSrcPortPassword())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTargetApplicationGUID(), getTargetApplicationGUID())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtNodeName(), getTrgtNodeName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtPortName(), getTrgtPortName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtPortType(), getTrgtPortType())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtPortUserName(), getTrgtPortUserName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtPortPassword(), getTrgtPortPassword())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtTransportProtocol(), getTrgtTransportProtocol())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtSecurityProtocol(), getTrgtSecurityProtocol())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtSecurityManager(), getTrgtSecurityManager())
                && DmiEqualsUtil.checkBooleanEquality(thisObj.isTrgtPortProxyEnabled(), isTrgtPortProxyEnabled())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtProxyURL(), getTrgtProxyURL())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtProxyPrincipal(), getTrgtProxyPrincipal())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtProxyCredentials(), getTrgtProxyCredentials())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtProxyAuthReam(), getTrgtProxyAuthReam())
                && DmiEqualsUtil.checkStringEquality(thisObj.getTrgtServInst(), getTrgtServInst())
                && DmiEqualsUtil.checkBooleanEquality(thisObj.isMessageCompressed(), isMessageCompressed())
                && DmiEqualsUtil.checkBooleanEquality(thisObj.isMessageEncrypted(), isMessageEncrypted())
                && DmiEqualsUtil.checkBooleanEquality(thisObj.isSpecifiedSourceDestinationUsed(), isSpecifiedSourceDestinationUsed())
                && DmiEqualsUtil.checkBooleanEquality(thisObj.isSpecifiedTargetDestinationUsed(), isSpecifiedTargetDestinationUsed())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSpecifiedSrcPortName(), getSpecifiedSrcPortName())
                && DmiEqualsUtil.checkStringEquality(thisObj.getSpecifiedTrgtPortName(), getSpecifiedTrgtPortName());
    }

    private void _readSelectors(DataInput is, int versionNo)
        throws IOException
    {
        int size = is.readInt();
        m_selectors = new HashMap();

        for (int i = 0; i < size; i++)
        {
            String type = UTFReaderWriter.readUTF(is);
            if ((type.equalsIgnoreCase(Route.SELECTOR_APPLICATION_CONTEXT) ||
                    type.equalsIgnoreCase(Route.SELECTOR_BODY))){
                m_selectors.put(type, DmiObject.fromStream(DmiObjectTypes.NEW_XPATH_SELECTOR, is, versionNo));
            }else
                m_selectors.put(type, UTFReaderWriter.readUTF(is));
        }
    }

   /*-------------------------------------------------[ LogManager ]---------------------------------------------------*/

    private LogManager logManager;

    public LogManager getLogManager(){
        return logManager;
    }

    public void setLogManager(LogManager logManager){
        this.logManager = logManager;
    }

    /*-------------------------------------------------[ LogModules ]---------------------------------------------------*/

    public static final String ELEM_LOGMODULES = "logmodules";

    private List logModules = new ArrayList();

    public void addLogModule(LogModule logModule){
        logModules.add(logModule);
    }

    public void removeLogModule(LogModule logModule){
        logModules.remove(logModule);
    }

    public List getLogModules(){
        return logModules;
    }

    public void setLogModules(List logModules){
        this.logModules = logModules;
    }

    public void clearLogModules(){
        this.logModules.clear();
    }

    /*-------------------------------------------------[ Compression ]---------------------------------------------------*/

    public static final String ELEM_MESSAGES = "messages";
    public static final String ATTR_COMPRESS = "compress";

    private boolean compressed = false;

    /**
     * returns true if the messages are compressed by route
     */
    public boolean isMessageCompressed(){
        return compressed;
    }

    public void setMessageCompression(boolean compressed){
        this.compressed = compressed;
    }

    /*-------------------------------------------------[ Encryption ]---------------------------------------------------*/

    public static final String ATTR_ENCRYPT = "encrypt";

    private boolean encrypted = false;

    /**
     * returns true if the messages are encrypted by route
     */
    public boolean isMessageEncrypted(){
        return encrypted;
    }

    public void setMessageEncryption(boolean encrypted){
        this.encrypted = encrypted;
    }

    /*-------------------------------------------------[ ErrorPortType ]---------------------------------------------------*/

    private String errorPortType = PortInstConstants.JMSDESTINATION_TOPIC;

    public String getErrorPortType(){
        return errorPortType;
    }

    public void setErrorPortType(String errorPortType){
        this.errorPortType = errorPortType;
    }

    /*-------------------------------------------------[ ErrorPortName ]---------------------------------------------------*/

    private String errorPortName = PortInstance.EXCEPTION_PORT_NAME;

    public String getErrorPortName(){
        return errorPortName;
    }

    public void setErrorPortName(String errorPortName){
        this.errorPortName = errorPortName;
    }

    public boolean isSpecifiedSourceDestinationUsed() {
        return specifiedSourceDestinationUsed;
    }

    public boolean isSpecifiedTargetDestinationUsed() {
        return specifiedTargetDestinationUsed;
    }

    public void setSpecifiedSourceDestinationUsed(boolean specifiedSourceDestinationUsed) {
        this.specifiedSourceDestinationUsed = specifiedSourceDestinationUsed;
    }

    public void setSpecifiedTargetDestinationUsed(boolean specifiedTargetDestinationUsed) {
        this.specifiedTargetDestinationUsed = specifiedTargetDestinationUsed;
    }

    public String getSpecifiedSrcPortName() {
        return specifiedSrcPortName;
    }

    public String getSpecifiedTrgtPortName() {
        return specifiedTrgtPortName;
    }

    public void setSpecifiedSrcPortName(String specifiedSrcPortName) {
        this.specifiedSrcPortName = specifiedSrcPortName;
    }

    public void setSpecifiedTrgtPortName(String specifiedTrgtPortName) {
        this.specifiedTrgtPortName = specifiedTrgtPortName;
    }
}

