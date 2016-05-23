/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;

public interface IFPSManager extends Remote {

    /**
     * This method returns the MQ URL of the specified peer server
     *
     * @param fpsName peer server name
     * @return String - MQ URL of specified peer server
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException         ServiceException
     */
    public String getURLForFPS(String fpsName) throws RemoteException, ServiceException;

    /**
     * This method returns the Jetty Server Port of the specified peer server
     *
     *
     *
     * @param fpsName peer server name
     * @param ssl if true port corresponding to SSL connections is returned otherwise the default connector port.
     * @return int - Jetty Port of the specified peer server
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException         ServiceException
     */
    public int getJettyPortForFPS(String fpsName, boolean ssl) throws RemoteException, ServiceException;

    /**
     * This method returns all the connection properties to be used to connect to the specified Peer Server
     *
     * @param fpsName peer server name
     * @return Hashtable - Containing all the connection properties to be used to connect to the specified Peer Server
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException         ServiceException
     */
    public Hashtable<String, String> getConnectionProperties(String fpsName) throws RemoteException, ServiceException;

    /**
     * This method returns a vector of peers present in the peer repository of the Enterprise Server.
     *
     * @return Vector - Vector of Strings. Each string is a name of a peer present in the peer repository of the Enterprise Server
     * @throws ServiceException         ServiceException
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public Vector<String> getAllPeersInRepository() throws RemoteException, ServiceException;

    /**
     * This method adds a listener for events relating to Fiorano Servers.<br>
     * The listener added, notifies the client when:</br>
     * <br> 1. the Fiorano Enterprise server is down/out of network.</br>
     * <br> 2. a Fiorano Peer Server is available in the network.</br>
     * <br> 3. a Fiorano Peer Server is no longer available in the network.</br>
     *
     * @param listener The listener which receives the server events
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void addListener(IServerStateListener listener) throws RemoteException, ServiceException;

    /**
     * This method removes the Server state listener registered by the client.
     *
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void removeListener() throws RemoteException, ServiceException;

    /**
     * This method returns system information such as name and version of the operating
     * system, version of JRE specification and implementation, vendor of JRE
     * implementation, version of JVM specification and implementation, vendor and
     * name of JVM implementation on which the peer is running.
     *
     * @param fpsName peer server name
     * @return SystemInfoReference - SystemInfoReference object containing the peer system info
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    public SystemInfoReference getPeerSystemInfo(String fpsName) throws RemoteException, ServiceException;

    /**
     * This method returns a vector of backup URLs for the peer server specified by <code>fpsName</code>.
     *
     * @param fpsName peer server Name whose backup URLs have to be retrieved.
     * @return Vector - Vector of backup URLs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public Vector getBackupURLsForFPS(String fpsName) throws RemoteException, ServiceException;

    /**
     * This method returns a vector of aliases of Peer Server name.
     * @param fpsName Peer Server name
     * @return Vector - Vector of aliases of Peer server name
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public Vector<String> getFPSAliases(String fpsName) throws RemoteException, ServiceException;

    /**
     * This method returns a boolean indicating whether the peer ,specified by the <code>peerName</code> parameter is running or not.
     *
     * @param peerName Name of the peer
     * @return boolean - true if peer is running , false otherwise
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public boolean isPeerRunning(String peerName) throws RemoteException, ServiceException;

    /**
     * This method restarts a peer server.
     *
     * @param peerName Name of the peer
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void restartPeer(String peerName) throws RemoteException, ServiceException;

    /**
     * This method shuts down a peer server.
     *
     * @param peerName Name of the peer
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void shutdownPeer(String peerName) throws RemoteException, ServiceException;

    /**
     * This method returns the url for the Fiorano Peer Server(FPS) specified by the <code>peerName</code> parameter.
     *
     * @param peerName Name of the Peer Server
     * @return String - Connect URL for Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public String getConnectURLForPeer(String peerName) throws RemoteException, ServiceException;

    /**
     * This method returns the Connect Protocol for the Fiorano Peer Server(FPS) specified by the <code>peerName</code> parameter.
     *
     * @param peerName Name of the Peer Server
     * @return String - Connect Protocol for Peer Server
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public String getConnectProtocolForPeer(String peerName) throws RemoteException;

    /**
     * This method returns the latest output logs of a Peer Server.
     *
     * @param peerName      Name of the Peer Server
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Latest Output Logs for the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public String getLastPeerOutLogs(String peerName, int numberOfLines) throws RemoteException, ServiceException;

    /**
     * This method returns the last error logs of a Peer Server.
     *
     * @param peerName      Name of the Peer Server
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Latest Error Logs for the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public String getLastPeerErrLogs(String peerName, int numberOfLines) throws RemoteException, ServiceException;

    /**
     * This method clears the output logs of the Peer Server
     *
     * @param peerName Name of the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void clearPeerOutLogs(String peerName) throws RemoteException, ServiceException;

    /**
     * This method clears the error logs of the Peer Server
     *
     * @param peerName Name of the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void clearPeerErrLogs(String peerName) throws RemoteException, ServiceException;

    /**
     * This method clears the MQ error logs of the Peer Server
     *
     * @param peerName Name of the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void clearPeerMQErrLogs(String peerName) throws RemoteException, ServiceException;



    /**
     * This method clears the MQ output logs of the Peer Server
     *
     * @param peerName Name of the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void clearPeerMQOutLogs(String peerName) throws RemoteException, ServiceException;


    /**
     * This method exports Peer Server logs to a zip file.
     *
     * @param peerName Name of the peer
     * @param index    index of byte[] from where to read the zip contents
     * @return Byte Array - Byte Array containing the Zip file of logs
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public byte[] exportFPSLogs(String peerName, long index) throws RemoteException, ServiceException;

    /**
     * This method changes Peer Deployment Label, by default the deployment label is set to "Development"
     *
     * @param fpsName  Name of the Peer Server
     * @param deploymentLabel  deployment label of the Peer Server
     */
    public void changeTPSDeploymentLabel(String fpsName, String deploymentLabel);

    /**
     * This method returns the Peer Deployment label
     *
     * @param fpsName Name of the Peer Server
     * @return  deployment label of the Peer Server
     */
    public String getTPSDeploymentLabel(String fpsName);

    /**
     * This method returns the last MQ Output logs of a Peer Server.
     *
     * @param peerName      Name of the Peer Server
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Latest MQ Output logs of Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public String getLastPeerMQOutLogs(String peerName, int numberOfLines) throws RemoteException, ServiceException;

    /**
     * This method returns the last MQ error logs of a Peer Server.
     *
     * @param peerName      Name of the Peer Server
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Latest MQ Error Logs of the Peer Server
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public String getLastPeerMQErrLogs(String peerName, int numberOfLines) throws RemoteException, ServiceException;

    /**
     * This method returns the Memory Usage of specified Peer Server.
     * @param peerName      Name of the Peer Server
     * @return MemoryUsageMetaData - Object of MemoryUsageMetaData containing the Memory Usage details of Peer Server
     * @throws ServiceException ServiceException
     */
    public MemoryUsageMetaData getMemoryUsage(String peerName) throws ServiceException ;


    /**
     * This method clears the FPS repository for specified Peer Server.
     * @param fpsName name of the Peer Server
     * @param handleId client id
     */
    public void removeFPS(String fpsName, String handleId) throws ServiceException;

    /**
     * Gets FES Url As per Peer Server
     * @param fpsName Peer Server Name
     * @return FES Url As per Peer Server
     * @throws ServiceException
     */
    public String getFesRtlURLFromPeer(String fpsName) throws ServiceException;

}
