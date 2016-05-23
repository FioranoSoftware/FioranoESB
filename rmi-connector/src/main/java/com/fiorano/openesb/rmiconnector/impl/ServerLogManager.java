/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.utils.ConfigReader;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerLogManager {
    public String getTESLastOutLogs(int numberOfLines) throws FioranoException {
        Properties p = new Properties();
        try {
            ConfigReader.readPropertiesFromFile(new File(System.getProperty("user.dir") + File.separator + "etc" + File.separator + "org.ops4j.pax.logging.cfg"), p);
            String path = p.getProperty("log4j.appender.fiorano.file");
            if (path.contains("${karaf.data}")) {
                path = path.replace("${karaf.data}", System.getProperty("user.dir") + File.separator + "data");
            }
            if (path.contains("${karaf.base}")) {
                path = path.replace("${karaf.base}", System.getProperty("user.dir"));
            }

            byte[] encoded = Files.readAllBytes(Paths.get(new File(path).toURI()));
            return new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        /*try {
            String path = ((FileAppender)Logger.getRootLogger().getAppender("log4j.appender.fiorano")).getFile();
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return "";
    }

    public String getTESLastErrLogs(int numberOfLines) throws FioranoException {
        return "";
    }

    public String getMQLastErrLogs(int numberOfLines) throws FioranoException {
        return "";
    }

    public String getMQLastOutLogs(int numberOfLines) throws FioranoException {
        return "";
    }

    public void clearTESOutLogs() throws FioranoException {

        try {
            String path = getLogPath();
            new PrintWriter(path).close();
        } catch (IOException e) {
          throw new FioranoException(e);
        }
    }

    private String getLogPath() throws FioranoException {
        try {
            Properties properties = new Properties();
            ConfigReader.readPropertiesFromFile(new File(System.getProperty("user.dir") + File.separator + "etc" + File.separator + "org.ops4j.pax.logging.cfg"), properties);
            String path = properties.getProperty("log4j.appender.fiorano.file");
            if (path.contains("${karaf.data}")) {
                path = path.replace("${karaf.data}", System.getProperty("user.dir") + File.separator + "data");
            }
            if (path.contains("${karaf.base}")) {
                path = path.replace("${karaf.base}", System.getProperty("user.dir"));
            }
            return path;
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }

    public void clearTESMQOutLogs() throws FioranoException {

    }

    public void clearTESErrLogs() throws FioranoException {

    }

    public void clearTESMQErrLogs() throws FioranoException {

    }

    public void exportFESLogs(String absolutePath, String absolutePath1) throws Exception {
        String path = getLogPath();
        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(absolutePath));  FileInputStream fis = new FileInputStream(new File(path))) {
            ZipEntry e = new ZipEntry((new File(path).getName()));
            out.putNextEntry(e);

            byte[] buffer = new byte[4092];
            int byteCount = 0;
            while ((byteCount = fis.read(buffer)) != -1) {
                out.write(buffer, 0, byteCount);
            }
        } catch (Exception e) {
            throw new FioranoException(e);
        }
    }
}
