/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.SystemInfo;
import com.fiorano.openesb.application.TESPerformanceStats;
import com.fiorano.openesb.rmiconnector.api.FESPerformanceStats;
import com.fiorano.openesb.rmiconnector.api.IServiceProviderManager;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.rmiconnector.api.SystemInfoReference;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.transport.impl.jms.TransportConfig;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ServiceProviderManager extends AbstractRmiManager implements IServiceProviderManager {
    private InstanceHandler handler;
    private HashMap<String, File> getFESLogsMap = new HashMap<String, File>(4);
    protected ServerManager serverManager;
    protected ServerLogManager esbLogManager;

    public ServiceProviderManager(RmiManager rmiManager, InstanceHandler handler) {
        super(rmiManager);
        this.serverManager = rmiManager.getServerManager();
        this.esbLogManager = rmiManager.getLogManager();
        this.handler = handler;
        setHandleID(handler.getHandleID());
    }

    @Override
    public String getJMSProviderURL() {
       return TransportConfig.getInstance().getValue("providerURL");
    }

    public void restartServer() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Restart Enterprise server");
        try {
            serverManager.restart();
        } catch (FioranoException e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer() throws RemoteException, ServiceException {
        try {
            validateHandleID(handleId, "Shutdown Enterprise server");
            serverManager.shutdown();
        } catch (FioranoException e) {
            //logging done in internal class. so not doing here.
            throw new ServiceException( e.getMessage());
        }
    }

    public String getLastOutLogs(int numberOfLines) throws RemoteException, ServiceException {
        try {
            return esbLogManager.getTESLastOutLogs(numberOfLines);
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_GET_LOGS, "out", "Enterprise", "", "", e);
            throw new ServiceException( e.getMessage());
        }
    }

    public String getLastErrLogs(int numberOfLines) throws RemoteException, ServiceException {
        try {
            return esbLogManager.getTESLastErrLogs(numberOfLines);
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_GET_LOGS, "error", "Enterprise", "", "", e);
            throw new ServiceException( e.getMessage());
        }
    }

    public String getLastMQErrLogs(int numberOfLines) throws RemoteException, ServiceException {
        try {
            return esbLogManager.getMQLastErrLogs(numberOfLines);
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_GET_LOGS, "MQError", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public String getLastMQOutLogs(int numberOfLines) throws RemoteException, ServiceException {
        try {
            return esbLogManager.getMQLastOutLogs(numberOfLines);
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_GET_LOGS, "MQOut", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public HashMap getServerDetails() throws RemoteException, ServiceException {
        try {
            return serverManager.getServerDetails();
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_GET_ESB_DETAILS, "", e);
            throw new ServiceException("ERROR_GET_ESB_DETAILS");

        }
    }

    public int getRemoteRMIPort() {
        try {
            return serverManager.getRemoteRMIPort();
        } catch (FioranoException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getRemoteIPAddress() {
        try {
            return serverManager.getRemoteIPAddress();
        } catch (FioranoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getServerName() throws RemoteException, ServiceException {
       return null;
    }

    public long getFESActiveSwitchTime() throws RemoteException, ServiceException {
        return 0;//serverManager.getFESActiveSwitchTime();
    }

    public SystemInfoReference getServerSystemInfo() throws RemoteException, ServiceException {
        SystemInfo si = ServerInfo.getTESSystemInfo();
        return setSystemInformation(si);
    }

    public FESPerformanceStats getServerPerformanceStats() throws RemoteException, ServiceException {
        TESPerformanceStats tfs = ServerInfo.getTESPerformanceStats();
        return setPerformanceStats(tfs);
    }

    public void clearFESOutLogs() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Clear FES Out Logs");
        try {
            esbLogManager.clearTESOutLogs();
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CLEAR_LOGS, "out", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void clearFESMQOutLogs() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Clear FES MQ Out Logs");
        try {
            esbLogManager.clearTESMQOutLogs();
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CLEAR_MQ_LOGS, "out", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void clearFESErrLogs() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Clear FES Error Logs");
        try {
            esbLogManager.clearTESErrLogs();
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CLEAR_LOGS, "error", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public void clearFESMQErrLogs() throws RemoteException, ServiceException {
        validateHandleID(handleId, "Clear FES MQ Error Logs");
        try {
            esbLogManager.clearTESMQErrLogs();
        } catch (FioranoException e) {
            //rmiLogger.error(Bundle.class, Bundle.ERROR_CLEAR_MQ_LOGS, "error", "Enterprise", "", "", e);
            throw new ServiceException(e.getMessage());
        }
    }


    public byte[] exportLogs(long index) throws RemoteException, ServiceException
    {
        byte[] contents = new byte[0];
        File tempZipFile;
        BufferedInputStream bis = null;
        boolean completed = false;
        if (getFESLogsMap.get("FES") == null) {
            tempZipFile = FileUtil.findFreeFile(FileUtil.TEMP_DIR, "FES" + "logs", "zip");
            File tempDir=new File(FileUtil.TEMP_DIR,"TempDir");
            tempDir.mkdir();     //creating directory to be used as a temp location to hold
            try {
                esbLogManager.exportFESLogs(tempZipFile.getAbsolutePath(),tempDir.getAbsolutePath());
                getFESLogsMap.put("FES", tempZipFile);
            } catch (Exception e) {
                tempZipFile.delete();
                //rmiLogger.error(Bundle.class, Bundle.ERROR_EXPORT_LOGS, "EnterpriseServer", "FES", "", e);
                throw new ServiceException(e.getMessage());
            }

        } else
        {
            tempZipFile = getFESLogsMap.get("FES");
        }

        //Now we have Application Zip file, Read the contents of the Zip file by skipping till index.
        try {
            bis = new BufferedInputStream(new FileInputStream(tempZipFile));
            bis.skip(index);
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount;
            readCount = bis.read(tempContents);
            if (readCount < 0) {
                completed = true;
                return null;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
        } catch (
                IOException e) {
            completed = true;
            //rmiLogger.error(Bundle.class, Bundle.ERROR_SENDING_CONTENTS_OF_LOGS, "EnterpriseServer", "FES", e);
            throw new ServiceException("ERROR_SENDING_CONTENTS_OF_LOGS");
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (completed) {
                    getFESLogsMap.remove("FES");
                    if (tempZipFile != null) {
                        tempZipFile.delete();
                    }
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return contents;
    }

    protected String getHighestVersion(String id, String handleID) throws ServiceException {
        throw new UnsupportedOperationException("This operation is not supported for this instance");
    }

    /************************************************** PRIVATE METHODS BELOW ****************************************/


    /**
     * converts SystemInfo object to SystemInfoReference object.
     *
     * @param peerSystemInfo SystemInfo
     * @return SystemInfoReference
     */
    private SystemInfoReference setSystemInformation(SystemInfo peerSystemInfo) {

        if (peerSystemInfo == null)
            return null;

        SystemInfoReference peerSystemInfoReference = new SystemInfoReference();

        peerSystemInfoReference.setJreImplVendor(peerSystemInfo.getJREImplVendor());
        peerSystemInfoReference.setJreImplVersion(peerSystemInfo.getJREImplVersion());
        peerSystemInfoReference.setJreSpecVersion(peerSystemInfo.getJRESpecVersion());

        peerSystemInfoReference.setJvmImplName(peerSystemInfo.getJVMImplName());
        peerSystemInfoReference.setJvmImplVendor(peerSystemInfo.getJVMImplVendor());
        peerSystemInfoReference.setJvmImplVersion(peerSystemInfo.getJVMImplVersion());
        peerSystemInfoReference.setJvmSpecVersion(peerSystemInfo.getJVMSpecVersion());

        peerSystemInfoReference.setOpSysName(peerSystemInfo.getOSName());
        peerSystemInfoReference.setOpSysVersion(peerSystemInfo.getOSVersion());

        return peerSystemInfoReference;
    }

    /**
     * converts TESPerformanceStats object to FESPerformanceStats object.
     *
     * @param tfs performance stats
     * @return FESPerformanceStats
     */
    private FESPerformanceStats setPerformanceStats(TESPerformanceStats tfs) {
        if (tfs == null) return null;

        FESPerformanceStats fps = new FESPerformanceStats();
        fps.setFreeMemoryInKB(tfs.getFreeMemory());
        fps.setMemoryUsageInKB(tfs.getMemoryUsage());
        fps.setTotalMemoryInKB(tfs.getTotalMemory());
        fps.setTotalProcessCount(tfs.getTotalProcessCount());
        fps.setTotalThreadCount(tfs.getTotalThreadCount());
        fps.setCpuUtilization(tfs.getCpuUtilization());
        return fps;
    }

    public Map<String, String> getConnectionProperties(){
        return TransportConfig.getInstance().getConnectionProperties();
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());
    }

    public String toString() {
        return Constants.SERVICE_PROVIDER_MANAGER;
    }

    private IServiceProviderManager clientProxyInstance;

    void setClientProxyInstance(IServiceProviderManager clientProxyInstance){
        this.clientProxyInstance = clientProxyInstance;
    }

    IServiceProviderManager getClientProxyInstance() {
        return clientProxyInstance;
    }

    public String getHAServerStatus() throws RemoteException, ServiceException {
        return null;
    }
}
