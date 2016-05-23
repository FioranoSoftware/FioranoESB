/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    /**
     * zip given directory, but retain the relative path from home
     * NOTE: even the empty folders also included in zip file
     *
     * @param home          directory from which relative path to be retained
     * @param dir2zip       the directory whose contents need to be zipped
     * @param zipFile       the zip file to be created
     */
    public static void zipDir(File home, File dir2zip, File zipFile) throws IOException {
        zipDir(home, dir2zip, zipFile, null);
    }

    /**
     * zip given directory into specified stream, but retain the relative path from home
     * NOTE: even the empty folders also included in zip file
     *
     * @param home          directory from which relative path to be retained
     * @param dir2zip       the directory whose contents need to be zipped
     * @param zos           the zip output stream, to which written
     */
    public static void zipDir(File home, File dir2zip, ZipOutputStream zos) throws IOException{
        zipDir(home, dir2zip, zos, null);
    }



    /**
     * zip given directory, but retain the relative path from home
     * NOTE: even the empty folders also included in zip file
     *
     * @param home          directory from which relative path to be retained
     * @param dir2zip       the directory whose contents need to be zipped
     * @param zipFile       the zip file to be created
     * @param filenameFilter filter for the files to be added to the zip.
     */
    public static void zipDir(File home, File dir2zip, File zipFile, FilenameFilter filenameFilter) throws IOException{
        ZipOutputStream zout = null;
        try{
            zout = new ZipOutputStream(new FileOutputStream(zipFile));
            zipDir(home, dir2zip, zout, filenameFilter);
        } finally{
            if(zout!=null)
                zout.close();
        }
    }

    /**
     * zip given directory into specified stream, but retain the relative path from home
     * NOTE: even the empty folders also included in zip file
     *
     * @param home          directory from which relative path to be retained
     * @param dir2zip       the directory whose contents need to be zipped
     * @param zos           the zip output stream, to which written
     * @param filenameFilter filter for the files to be added to the zip.
     */
    public static void zipDir(File home, File dir2zip, ZipOutputStream zos, FilenameFilter filenameFilter) throws IOException{
        String[] dirList = dir2zip.list(filenameFilter);
        if(dirList==null || dirList.length==0){
            if(!home.equals(dir2zip)){
                ZipEntry anEntry = new ZipEntry(FileUtil.getRelativePath(home, dir2zip)+'/');
                zos.putNextEntry(anEntry);
                zos.closeEntry();
            }
            return;
        }
        for(int i = 0; i<dirList.length; i++){
            File f = new File(dir2zip, dirList[i]);
            if(f.isDirectory()){
                zipDir(home, f, zos, filenameFilter);
                continue;
            }
            ZipEntry anEntry = new ZipEntry(FileUtil.getRelativePath(home, f));
            zos.putNextEntry(anEntry);
            new StreamPumper(new FileInputStream(f), zos, false).execute();
            zos.closeEntry();
        }
    }

    /**
     * extracts given zip file to the specified directory
     */
    public static void unzip(File zipFile, File todir) throws IOException{
        ZipFile zipfile = null;
        try{
            zipfile = new ZipFile(zipFile);
            Enumeration e = zipfile.entries();
            while(e.hasMoreElements()){
                ZipEntry entry = (ZipEntry)e.nextElement();
                File file = new File(todir, entry.getName());
                if(entry.getName().endsWith("/")) //NOI18N
                    file.mkdirs();
                else{
                    file.getParentFile().mkdirs();
                    new StreamPumper(zipfile.getInputStream(entry), new FileOutputStream(file), true).execute();
                }
            }
        }finally{
            if(zipfile!=null)
                zipfile.close();
        }
    }
}
