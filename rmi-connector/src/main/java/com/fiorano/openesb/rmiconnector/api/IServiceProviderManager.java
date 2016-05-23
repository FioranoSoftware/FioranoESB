/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.HashMap;
import java.util.Map;

public interface IServiceProviderManager extends Remote {

    public Map<String, String> getConnectionProperties();

    public String getJMSProviderURL();
     /**
     * This method restarts the Enterprise Server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void restartServer() throws RemoteException, ServiceException;

    /**
     * This method shuts down the server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void shutdownServer() throws RemoteException, ServiceException;

    /**
     * This method clears the error logs of the Enterprise Server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void clearFESErrLogs() throws RemoteException, ServiceException;


    /**
     * This method clears the MQ error logs of the Enterprise Server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void clearFESMQErrLogs() throws RemoteException, ServiceException;

     /**
     * This method clears the output logs of the Enterprise Server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void clearFESOutLogs() throws RemoteException, ServiceException;

    /**
     * This method clears the MQ output logs of the Enterprise Server.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void clearFESMQOutLogs() throws RemoteException, ServiceException;

    /**
     * This method returns a hashmap containing the following details of the Enterprise Server.
     * </br><br>(a) Name of the server (key - 'Name')</br>
     * <br>(b) Server URL (key - 'Server URL' )</br>
     * <br>(c) Server Backup URLs (key - 'Backup URLs' )</br>
     * <br>(d) Server ProcessCount (key - 'ProcessCount' )</br>
     * <br>(e) Server ThreadCount (key - 'ThreadCount' )</br>
     * <br>(f) Server OS (key - 'OS' ) </br>
     * <br>(g) Server JRE (key - 'JRE' )</br>
     *
     * @return HashMap - containing Enterprise Server's details
     * @throws RemoteException
     * @throws ServiceException
     */
    public HashMap getServerDetails() throws RemoteException, ServiceException;

    /**
     * This method returns the RMI connection port
     * @return  int - Remote RMI Port
     */
    public int getRemoteRMIPort() ;

    /**
     * This method returns the IP Address of the RMI server
     * @return String - Remote IP Address
     */
    public String getRemoteIPAddress() ;
    /**
    * This method returns the Profile name of the server.
    * @return String - Name of the server
    * @throws RemoteException,
     *@throws ServiceException
    */
    public String getServerName() throws RemoteException, ServiceException;

    /**
    * This method returns the time at which the server became active.
    * @return long - Time when server became active
    * @throws RemoteException
    *@throws ServiceException
    */
    public long getFESActiveSwitchTime() throws RemoteException,ServiceException;

     /**
     * This method returns system information such as name and version of the operating
     * system, version of JRE specification and implementation, vendor of JRE
     * implementation, version of JVM specification and implementation, vendor and
     * name of JVM implementation on which the Enterprise server is running.
     *
     * @return SystemInfoReference - An object of SystemInfoReference containing system details
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    public SystemInfoReference getServerSystemInfo()throws RemoteException,ServiceException;

    /**
     * This method returns Performance statistics of the Enterprise Server
     * @return FESPerformanceStats - An object of FESPerformanceStats containing performance details of Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public FESPerformanceStats getServerPerformanceStats()throws RemoteException,ServiceException;

     /**
     * This method returns the last output logs of the Enterprise Server.
      * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - last output logs of Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getLastOutLogs(int numberOfLines)throws RemoteException, ServiceException;

     /**
     * This method returns the last error logs of the Enterprise Server.
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - last error logs of Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getLastErrLogs(int numberOfLines)throws RemoteException, ServiceException;

    /**
     * This method returns the last output logs of Enterprise Server's MQ.
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Last MQ output logs of Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getLastMQOutLogs(int numberOfLines)throws RemoteException, ServiceException;

    /**
     * This method returns the last error logs of Enterprise Server's MQ.
     * @param numberOfLines number of lines to be retrieved from the log file from the end.
     * @return String - Last MQ error logs of Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getLastMQErrLogs(int numberOfLines)throws RemoteException, ServiceException;

    /**
     * This method exports Enterprise Server logs.
     * @param index index of logs to be exported
     * @return Byte Array - Byte Array containing the logs of the Enterprise Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public byte[] exportLogs(long index)throws RemoteException, ServiceException;

    /**
     * This method returns the status of HA Server.
     *
     * @return String - Status of the HA Server
     * @throws RemoteException
     * @throws ServiceException
     */
    public String getHAServerStatus() throws RemoteException, ServiceException;
}
