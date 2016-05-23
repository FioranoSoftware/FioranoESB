/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.rmiconnector.api.IDapiEventManager;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;

public abstract class AbstractRmiManager implements IDistributedRemoteObject {

    protected String handleId="FioranohandleID";
    protected DapiEventManager dapiEventManager;

    /** Contains info like client locale,client ip addresses.*/
    protected HashMap clientInfo;
    private RmiManager rmiManager;

    protected AbstractRmiManager(RmiManager rmiManager){
        super();
        this.rmiManager = rmiManager;
        this.dapiEventManager = rmiManager.getDapiEventsManager();
        loadMethods();
    }

    protected final void validateHandleID(String handleId, String operation) throws ServiceException {
        if (rmiManager.getConnectionHandle(handleId) == null)
            throw new ServiceException("Failed to " + operation + ", Reason : Invalid Handle ID");
    }


    protected float getAnyVersion() {
        return -1; // -1 is the ANY_VERSION by default
    }


    protected File getTempFile(String name, String ext) {
        return FileUtil.findFreeFile(FileUtil.TEMP_DIR, name, ext);
    }

    protected static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    protected static byte[] getBytesFromFile(File file) throws FioranoException {

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            int numBytes = bis.available();

            if (numBytes <= 0) {
                bis.close();
                return null;
            }

            byte[] b = new byte[numBytes];

            int n = bis.read(b);
            if (n != numBytes)
                throw new FioranoException("IO Exception occured while trying to read the resource File" + file.getPath());

            bis.close();
            return b;
        }
        catch (IOException e) {
            throw new FioranoException(e.getMessage());
        }
        finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }

    protected static File findFileinDir(File path, final String fileName) throws Exception {
        File toreturn = null;
        File[] files = path.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(fileName) || pathname.isDirectory();
            }
        });
        if ((files == null) || (files.length == 0)) return toreturn;

        for (File file : files) {
            if (file.isDirectory()) {
                toreturn = findFileinDir(file, fileName);
            } else {
                toreturn = file;
            }
        }
        return toreturn;
    }

    public void unreferenced() {
    }

    protected void setHandleID(String handleID) {
        this.handleId = handleID;
    }

    public Object invoke(String methodName, Object[] args, HashMap additionalInfo) throws RemoteException,ServiceException{

        Object ret=null;
        //get additional info about the client.This info is sent only once by the client.why should client send the same information over and over again?
        if(additionalInfo != null)
            clientInfo=additionalInfo;

        //validate handle id if one must
        validateHandleID(this.handleId,methodName);

        try {
            Method toInvoke = methods.get(methodName);
            if (toInvoke == null) {
                String key = (String)additionalInfo.get("method_parameter_types"); //key will not be null here.
                toInvoke = methods.get(methodName + "__" + key);
            }
            ret = toInvoke.invoke(this, args);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new ServiceException();
        } catch (InvocationTargetException ite) {
            Throwable tgt = ite.getTargetException();
            if(tgt instanceof ServiceException)
                throw (ServiceException)tgt; //propogate to client
            else
            {
                ite.printStackTrace();
                throw new ServiceException();
            }
        }

        return ret;
    }

    private Hashtable<String,Method> methods= new Hashtable<String,Method>();

    private void loadMethods() {
        Method[] meths = this.getClass().getMethods();
        for (Method m : meths) {
            String argClasses = getArgumentClassesKey(m);
            if (argClasses != null)
                methods.put(m.getName() + "__" + argClasses, m);   //for overloaded methods
            else
                methods.put(m.getName(), m);
        }
    }

    private String getArgumentClassesKey(Method m) {
        Class argClasses[] = m.getParameterTypes();
        if (argClasses.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Class c : argClasses) {
            builder.append(c.getName());
            builder.append(':');
        }
        String args = builder.toString();
        args = args.substring(0, args.lastIndexOf(":"));
        return args;
    }
}

