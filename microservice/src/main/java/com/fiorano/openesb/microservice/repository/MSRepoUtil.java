/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.repository;

import com.fiorano.openesb.application.service.Deployment;
import com.fiorano.openesb.application.service.Resource;
import com.fiorano.openesb.application.service.Service;
import com.fiorano.openesb.application.service.ServiceParser;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.util.*;

public class MSRepoUtil {

    public static void copyStream(InputStream in, OutputStream out) throws FioranoException {
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            while (true) {
                byte[] bytes = new byte[MicroServiceConstants.FILE_COPY_CHUNK_SIZE];

                int numRead = bis.read(bytes);
                if (numRead < 0)
                    break;

                if (numRead < MicroServiceConstants.FILE_COPY_CHUNK_SIZE) {
                    byte[] toWrite = new byte[numRead];
                    System.arraycopy(bytes, 0, toWrite, 0, numRead);
                    bos.write(toWrite);
                    continue;  // can stop since it wudve been end of file
                }

                bos.write(bytes);
            }

            bis.close();
            bos.close();
        } catch (IOException e) {
            throw new FioranoException(e);
        }
    }

    /**
     * Writes the specified data to the specified fie
     *
     * @param file     Description of the Parameter
     * @param fileData Description of the Parameter
     * @throws java.io.IOException           Description of the Exception
     * @throws java.io.FileNotFoundException Description of the Exception
     */
    public static void writeFileData(File file, byte[] fileData)
            throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(fileData);
        bos.close();
    }

    /**
     * Compares two string for equality. If both are null, returns true. Wild
     * card (*) can also be used
     *
     * @param str1 The source string which is to be compared
     * @param str2 The string from which comparison is to be done
     * @return Description of the Return Value
     */
    public static boolean compare(String str1, String str2) {

        if (str1 == null || str1.trim().equals("") || str1.equals("*"))
            return true;

        // if the string contains *, then check for the substring from index
        // 0 to the index of * and from * to the end of string
        if (str1.contains("*")) {
            boolean partBefore = true;
            boolean partAfter = true;

            // check for the part before *
            if (str1.indexOf("*") > 0) {
                String str1Substring1 = str1.substring(0, str1.indexOf("*"));

                if (!str2.startsWith(str1Substring1))
                    partBefore = false;
            }

            // check for the part after *
            String str1Substring2 = str1.substring(str1.indexOf("*") + 1, str1.length());

            if (!str2.endsWith(str1Substring2))
                partAfter = false;
            return partAfter && partBefore;
        }

        // The string doesn't have wild cards and it is not null. So,
        // simply compare the strings and return the result
        if (str2 == null)
            return false;
        else {
            int compResult = str1.compareTo(str2);

            return compResult == 0;
        }
    }

    /**
     * Gets the bytes from the file
     *
     * @param file FileObject
     * @return byte[]. If it returns NULL => the file had 0 bytes in it and This should be logged at the place where this method is called.
     * @throws FioranoException
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file)
            throws FioranoException, IOException {
        if (!file.exists())
            return null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            int numBytes = bis.available();

            if (numBytes <= 0) {
                bis.close();
                //LogHelper.logErr(ILogModule.SERVICE_REPOSITORY, 17,file.getName().getPath());
                //todo must LOG AT HIGHER LEVEL important
                return null;
            }

            byte[] b = new byte[numBytes];

            int n = bis.read(b);
            if (n != numBytes)
                throw new FioranoException("bytes number mismatch");
            //LogHelper.getErrMessage(ILogModule.SERVICE_REPOSITORY,15 ,file.getName().getPath()));

            bis.close();
            return b;
        } catch (IOException e) {

            throw new FioranoException(e);
        }

    }

    /**
     * Gets the bytes from the file
     *
     * @param in InputStream
     * @return byte[]. If it returns NULL => the file had 0 bytes in it and This should be logged at the place where this method is called.
     * @throws FioranoException
     * @throws IOException
     */
    public static byte[] getBytesFromInputStream(InputStream in, String nameSpace, String locationHint) throws FioranoException {
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            int numBytes = bis.available();

            if (numBytes <= 0) {
                bis.close();
                //todo must LOG AT HIGHER LEVEL important
                return null;
            }

            byte[] b = new byte[numBytes];

            int n = bis.read(b);
            if (n != numBytes)
                throw new FioranoException();

            bis.close();
            return b;
        } catch (IOException e) {
            //    e.printStackTrace(); -->todo :log at higher level.
            throw new FioranoException(e);
            //LogHelper.getErrMessage(ILogModule.SERVICE_REPOSITORY,15 ,file.getName().getPath()),e);
        }
    }


    public static String resolve(String home, String relativePath, Map/*<String, File>*/ favorites) {
        if (File.separatorChar != '\\')
            relativePath = relativePath.replace('\\', File.separatorChar);
        if (File.separatorChar != '/')
            relativePath = relativePath.replace('/', File.separatorChar);
        Iterator iter = favorites.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = '$' + (String) entry.getKey() + '$';
            if (relativePath.startsWith(key))
                return entry.getValue() + relativePath.substring(key.length());
        }

        return home + File.separator + relativePath;
    }

    public static void moveChildrenToTgtFolder(File src, File tgt) throws FioranoException {
        if (!src.isDirectory() || (tgt.exists() && !tgt.isDirectory())) {
            throw new FioranoException("source target mismatch");
        }

        File[] childlist = src.listFiles();
        for (int i = 0; i < childlist.length; i++) {
            File srcChild = childlist[i];
            File tgtChild = new File(tgt, srcChild.getName());
            srcChild.renameTo(tgtChild);
        }
    }

    public static void copyChildrenToTgtFolder(File src, File tgt)
            throws FioranoException {
        if ((!src.isDirectory() || (tgt.exists() && !tgt.isDirectory()))) {
            throw new FioranoException("Source target mismatch");
        }

        File[] childlist = src.listFiles();
        for (int i = 0; i < childlist.length; i++) {
            File srcChild = childlist[i];
            File tgtChild = new File(tgt, srcChild.getName());
            try {
                FileUtil.copyFileUsingIO(srcChild, tgtChild);
            } catch (IOException e) {
                throw new FioranoException(e);
            }

        }
    }

    static File getServiceDescriptor(String serviceGUID, String version) {
        String path = getServicePath(serviceGUID, version)
                + File.separator + MicroServiceConstants.SERVICE_DESCRIPTOR_FILE_NAME;
        return new File(path);
    }

    static Service getServicePropertySheet(String serviceGUID, String version)
            throws FioranoException, IOException {
        File servDescFile = getServiceDescriptor(serviceGUID, version);
        if (!servDescFile.exists()) {
            return null;
        }
        try {
            return ServiceParser.readService(servDescFile);
        } catch (FioranoException e) {
            throw new FioranoException("ERROR_PARSING_SERVICE_DESCRIPTOR", e);
        }
    }

    static String getServicePath(String serviceGUID, String version) {
        return MicroServiceRepoManager.COMPONENTS_REPOSITORY_FOLDER + File.separator + serviceGUID + File.separator + version;
    }

    static String getServiceRootPath(String serviceGUID) {
        return MicroServiceRepoManager.COMPONENTS_REPOSITORY_FOLDER + File.separator + serviceGUID;
    }

    static File getIcon(String serviceGUID, String version, String icon) {
        String path = getServicePath(serviceGUID, version) + File.separator + icon;
        return new File(path);
    }

    static void saveServiceDescriptor(Service sps) throws FioranoException {
        try {
            sps.setLastModifiedDate(new Date());
            File fo = getServiceDescriptor(sps.getGUID(), "" + sps.getVersion());
            ServiceParser.writeService(sps, fo.getParentFile());
        } catch (Exception e) {
            throw new FioranoException("FILE_SYSTEM_ERROR_WRITING_SERVICE_DESCRIPTOR", e);
        }
    }

    /*******************************************************************************************/



    static String getCompletedResourceFolder(String serviceGUID, String version) {
        return getServicePath(serviceGUID, version)
                + File.separator + MicroServiceRepoManager.TEMP_DOWNLOAD_DIR
                + File.separator + MicroServiceConstants.COMPLETE_RESOURCE_LOCATION;
    }

    static String getPartialResourceFolder(String serviceGUID, String version) {
        return getServicePath(serviceGUID, version)
                + File.separator + MicroServiceRepoManager.TEMP_DOWNLOAD_DIR
                + File.separator + MicroServiceConstants.PARTIAL_RESOURCE_LOCATION;
    }

    static Enumeration getAllServiceVersions(String serviceGUID)
            throws FioranoException {
        if (serviceGUID == null)
            throw new FioranoException("ERROR_GETTING_SERVICE_VERSION");

        File serviceRoot = new File(getServiceRootPath(serviceGUID));
        File[] allVersions = serviceRoot.listFiles();


        Vector<String> guids = new Vector<>();
        if (allVersions != null) {
            for (File allVersion : allVersions) {
                if (allVersion.exists()) {
                    guids.add(allVersion.getName());
                }
            }
        }
        return guids.elements();
    }

    static Enumeration getGUIDS(Hashtable table) {
        TreeSet<String> set = new TreeSet<>();
        Enumeration eenum = table.keys();
        while (eenum.hasMoreElements()) {
            String str = (String) eenum.nextElement();
            set.add(str.substring(0, str.indexOf(";")));
        }
        return Collections.enumeration(set);
    }

    static String getGUIDfromUniqueKey(String key) {
        return key.substring(0, key.indexOf(";"));
    }

    static boolean isPresentInNewDeployment(Resource oldresource, Deployment newDeployment) {
        for (Resource res : newDeployment.getResources()) {
            if (res.getName().equalsIgnoreCase(oldresource.getName()))
                return true;
        }
        return false;
    }
}
