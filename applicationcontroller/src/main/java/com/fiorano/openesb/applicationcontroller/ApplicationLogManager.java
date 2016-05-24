/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.microservice.ccp.CCPEventManager;
import com.fiorano.openesb.microservice.ccp.event.peer.CommandEvent;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.LookUpUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ApplicationLogManager {

    private Logger logger = LoggerFactory.getLogger(Activator.class);

    private HashMap<String, File> applicationLogMap = new HashMap<>(8);

    private ApplicationController applicationController;

    private CCPEventManager ccpEventManager;

    ApplicationLogManager(ApplicationController applicationController, CCPEventManager ccpEventManager){
        this.applicationController = applicationController;
        this.ccpEventManager = ccpEventManager;
    }

    public byte[] exportApplicationLogs(String appGUID, float version, long index) throws FioranoException {
        byte[] contents = new byte[0];
        String eventProcessKey = appGUID.toUpperCase() + "__" + version;
        File tempZipFile;
        FileInputStream fis = null;
        boolean completed = false;
        if (applicationLogMap.get(eventProcessKey) == null) {
            File tempdir;
            List<ServiceInstance> serviceInstances = applicationController.getSavedApplication(appGUID, version).getServiceInstances();
            List<File> pathListFiles = new ArrayList<>();
            for(ServiceInstance serviceInstance : serviceInstances){
                String logDir = applicationController.getSavedApplication(appGUID, version).getServiceInstance(serviceInstance.getName()).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
                File path = new File(ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                        +File.separator+version+File.separator+serviceInstance.getName().toUpperCase());
                if(path.exists()) {
                    pathListFiles.add(path);
                }
            }

            if(pathListFiles.isEmpty()) {
                throw new FioranoException("Logs are empty");
            }
            tempdir = FileUtil.findFreeFile(FileUtil.TEMP_DIR, "applicationlogs", "tmp");
            boolean mkdir = tempdir.mkdir();
            if(!mkdir){
                logger.trace("Could not create dir " + tempdir.getAbsolutePath());
            }
            tempZipFile = FileUtil.findFreeFile(FileUtil.TEMP_DIR ,appGUID+"__"+version + "logs", "zip");
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempZipFile))) {
                ZipEntry e;
                for (File service : pathListFiles) {
                    File[] serviceListFiles = service.listFiles();
                    if (serviceListFiles != null) {
                        for (File f : serviceListFiles) {
                            if (f.getName().endsWith("lck")) {
                                continue;
                            }
                            e = new ZipEntry(f.getName());
                            out.putNextEntry(e);

                            byte[] buffer = new byte[4092];
                            int byteCount;
                            fis = new FileInputStream(f);
                            while ((byteCount = fis.read(buffer)) != -1) {
                                out.write(buffer, 0, byteCount);
                            }
                        }
                    }
                }
                applicationLogMap.put(eventProcessKey, tempZipFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
                FileUtil.deleteDir(tempdir);
            }
        } else {
            tempZipFile = applicationLogMap.get(eventProcessKey);
        }

        try ( BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempZipFile))){
            long skip = bis.skip(index);
            logger.trace("skipped " + skip + "bytes");
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount;
            readCount = bis.read(tempContents);
            if (readCount < 0) {
                completed = true;
                return null;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
        } catch (IOException e) {
            completed = true;
            throw new FioranoException("ERROR_SENDING_CONTENTS_OF_LOGS EventProcess", appGUID +":"+ version);
        } finally {
            if (completed) {
                applicationLogMap.remove(eventProcessKey);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete file " + tempZipFile.getAbsolutePath());
                }
            }

        }
        return contents;
    }

    public byte[] exportServiceLogs(String appGUID, float version, String serviceInst, long index) throws FioranoException{

        byte[] contents = new byte[0];
        String serviceKey = appGUID.toUpperCase() + "__" + version +"__"+ serviceInst.toUpperCase();
        File tempZipFile;
        FileInputStream fis= null;
        boolean completed = false;
        String logDir = applicationController.getSavedApplication(appGUID, version).getServiceInstance(serviceInst).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
        if (applicationLogMap.get(serviceKey) == null) {
            File tempdir = null;
            File path = new File(ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                    +File.separator+version+File.separator+serviceInst.toUpperCase());
            tempZipFile = FileUtil.findFreeFile(FileUtil.TEMP_DIR ,serviceKey, "zip");
            if(!path.exists()) {
                throw new FioranoException("Logs are empty");
            }
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempZipFile))){
                tempdir = FileUtil.findFreeFile(FileUtil.TEMP_DIR, "servicelogs","tmp");
                boolean mkdir = tempdir.mkdir();
                if(!mkdir){
                    logger.trace("Could not create dir " + tempZipFile.getAbsolutePath());
                }
                ZipEntry e;
                File[] pathFileList = path.listFiles();
                if (pathFileList != null) {
                    for(File f : pathFileList){
                        if(f.getName().endsWith("lck")){
                            continue;
                        }
                        e = new ZipEntry(f.getName());
                        out.putNextEntry(e);

                        byte[] buffer = new byte[4092];
                        int byteCount;
                        fis = new FileInputStream(f);
                        while ((byteCount = fis.read(buffer)) != -1)
                        {
                            out.write(buffer, 0, byteCount);
                        }

                    }
                }
                applicationLogMap.put(serviceKey, tempZipFile);
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fis!=null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        //ignore
                    }
                }
                if (tempdir != null)
                    FileUtil.deleteDir(tempdir);
            }
        } else {
            tempZipFile = applicationLogMap.get(serviceKey);
        }

        try (BufferedInputStream bis =  new BufferedInputStream(new FileInputStream(tempZipFile))){
            long skip = bis.skip(index);
            logger.trace("Skipped " +skip + "bytes");
            byte[] tempContents = new byte[Constants.CHUNK_SIZE];
            int readCount;
            readCount = bis.read(tempContents);
            if (readCount < 0) {
                completed = true;
                return null;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
        } catch (IOException e) {
            completed = true;
            throw new FioranoException("ERROR_SENDING_CONTENTS_OF_LOGS EventProcess", appGUID +":"+ version);
        } finally {
            if (completed) {
                applicationLogMap.remove(serviceKey);
                boolean delete = tempZipFile.delete();
                if(!delete){
                    logger.trace("Could not delete file " + tempZipFile.getAbsolutePath());
                }
            }
        }
        return contents;
    }

    public void deleteLogs(Application application, Vector<String> components) throws FioranoException {
        for(String deletedComponent: components){
            clearServiceErrLogs(deletedComponent, application.getGUID(), application.getVersion());
            clearServiceOutLogs(deletedComponent, application.getGUID(), application.getVersion());
        }
    }

    public String getLastOutTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws FioranoException {
        String logDir = applicationController.getSavedApplication(appGUID, appVersion).getServiceInstance(serviceName).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
        String path = ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                +File.separator+appVersion+File.separator+serviceName.toUpperCase();
        File f = new File(path);
        if(!f.exists()){
            return "";
        }
        File[] logfiles = f.listFiles();
        StringBuilder sb = new StringBuilder("");
        int lineCount=0;
        if (logfiles != null) {
            for(File file:logfiles){
                if(!file.getName().contains("out") || file.getName().contains("lck")){
                    continue;
                }
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                        lineCount++;
                        if(lineCount==numberOfLines){
                            return sb.toString();
                        }
                    }
                    if(lineCount==numberOfLines){
                        return sb.toString();
                    }
                } catch (IOException e) {
                    throw new FioranoException(e);
                }
            }
        }
        return sb.toString();
    }

    public String getLastErrTrace(int numberOfLines, String serviceName, String appGUID, float appVersion) throws FioranoException{
        String logDir = applicationController.getSavedApplication(appGUID, appVersion).getServiceInstance(serviceName).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
        String path = ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                +File.separator+appVersion+File.separator+serviceName.toUpperCase();
        File f = new File(path);
        if(!f.exists()){
            return "";
        }
        File[] logfiles = f.listFiles();
        StringBuilder sb = new StringBuilder("");
        int lineCount=0;
        if (logfiles != null) {
            for(File file:logfiles){
                if(!file.getName().contains("err") || file.getName().contains("lck")){
                    continue;
                }
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                        lineCount++;
                        if(lineCount==numberOfLines){
                            return sb.toString();
                        }
                    }
                    if(lineCount==numberOfLines){
                        return sb.toString();
                    }
                } catch (IOException e) {
                    throw new FioranoException(e);
                }
            }
        }
        return sb.toString();
    }

    public void clearServiceOutLogs(String serviceInst, String appGUID, float appVersion) throws FioranoException {
        if(applicationController.isServiceRunning(appGUID, appVersion, serviceInst)){
            CommandEvent commandEvent = new CommandEvent();
            commandEvent.setCommand(CommandEvent.Command.CLEAR_OUT_LOGS);
            try {
                ccpEventManager.getCcpEventGenerator().sendEvent(commandEvent, LookUpUtil.getServiceInstanceLookupName(appGUID, appVersion, serviceInst));
            } catch (Exception e) {
                logger.error("Error occurred while sending 'clear out logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion);
                throw new FioranoException("Error occurred while sending 'clear out logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion, e);
            }
            return;
        }
        String logDir = applicationController.getSavedApplication(appGUID, appVersion).getServiceInstance(serviceInst).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
        String path = ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                +File.separator+appVersion+File.separator+serviceInst.toUpperCase();
        File f = new File(path);
        if(!f.exists()){
            return;
        }
        File[] logfiles = f.listFiles();
        if (logfiles != null) {
            for(File file:logfiles){
                if(!file.getName().contains("out") || file.getName().contains("lck")){
                    continue;
                }
                boolean delete = file.delete();
                if(!delete)
                    logger.trace("Could not delete file " + file.getAbsolutePath());
            }
        }
    }

    public void clearServiceErrLogs(String serviceInst, String appGUID, float appVersion) throws FioranoException {
        if(applicationController.isServiceRunning(appGUID, appVersion, serviceInst)){
            CommandEvent commandEvent = new CommandEvent();
            commandEvent.setCommand(CommandEvent.Command.CLEAR_ERR_LOGS);
            try {
                ccpEventManager.getCcpEventGenerator().sendEvent(commandEvent, LookUpUtil.getServiceInstanceLookupName(appGUID, appVersion, serviceInst));
            } catch (Exception e) {
                logger.error("Error occurred while sending 'clear error logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion);
                throw new FioranoException("Error occurred while sending 'clear error logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion, e);
            }
            return;
        }
        String logDir = applicationController.getSavedApplication(appGUID, appVersion).getServiceInstance(serviceInst).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
        String path = ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                +File.separator+appVersion+File.separator+serviceInst.toUpperCase();
        File f = new File(path);
        if(!f.exists()){
            return;
        }
        File[] logfiles = f.listFiles();
        if (logfiles != null) {
            for(File file:logfiles){
                if(!file.getName().contains("err") || file.getName().contains("lck")){
                    continue;
                }
                boolean delete = file.delete();
                if(!delete){
                    logger.trace("Could not delte file " + file.getAbsolutePath());
                }
            }
        }
    }

    public void clearApplicationLogs(String appGUID, float appVersion) throws FioranoException {
        Application application = applicationController.getSavedApplication(appGUID, appVersion);
        for(ServiceInstance si : application.getServiceInstances()){
            String serviceInst = si.getName();
            if(applicationController.isServiceRunning(appGUID, appVersion, serviceInst)){
                CommandEvent commandEvent = new CommandEvent();
                commandEvent.setCommand(CommandEvent.Command.CLEAR_ERR_LOGS);
                String componentIdentifier =LookUpUtil.getServiceInstanceLookupName(appGUID, appVersion, serviceInst);
                try {
                    ccpEventManager.getCcpEventGenerator().sendEvent(commandEvent, componentIdentifier);
                } catch (Exception e) {
                    logger.error("Error occurred while sending 'clear error logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion);
                    throw new FioranoException("Error occurred while sending 'clear error logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion, e);
                }
                commandEvent.setCommand(CommandEvent.Command.CLEAR_OUT_LOGS);
                try {
                    ccpEventManager.getCcpEventGenerator().sendEvent(commandEvent, componentIdentifier);
                } catch (Exception e) {
                    logger.error("Error occurred while sending 'clear out logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion);
                    throw new FioranoException("Error occurred while sending 'clear out logs' command to service " + serviceInst + "of Application "+appGUID+":"+appVersion, e);
                }
                continue;
            }
            String logDir = applicationController.getSavedApplication(appGUID, appVersion).getServiceInstance(serviceInst).getLogManager().getProps().getProperty("java.util.logging.FileHandler.dir");
            String path = ServerConfig.getConfig().getRuntimeDataPath()+File.separator+logDir+File.separator+appGUID.toUpperCase()
                    +File.separator+appVersion+File.separator+serviceInst.toUpperCase();
            File f = new File(path);
            if(!f.exists()){
                return;
            }
            File[] logfiles = f.listFiles();
            if (logfiles != null) {
                for(File file:logfiles){
                    if(file.getName().contains("lck")){
                        continue;
                    }
                    boolean delete = file.delete();
                    if(!delete){
                        logger.trace("Could not delete file " + file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
