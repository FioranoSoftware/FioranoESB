/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileUtil{
    public static final File USER_DIR = new File(System.getProperty("user.dir")); //NOI18N
    public static final File USER_HOME = new File(System.getProperty("user.home")); //NOI18N
    public static final File DESKTOP_DIR = new File(USER_HOME, "Desktop").exists() ? new File(USER_HOME, "Desktop") : USER_HOME; //NOI18N
    public static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir")); //NOI18N
    public static final String FILE_ENCODING = System.getProperty("file.encoding"); //NOI18N

    /*-------------------------------------------------[ Deleting ]---------------------------------------------------*/

    /** deletes directory. works for non-empty directory also */
    public static boolean deleteDir(File file){
        boolean success = true;
        if(file==null)
            return false;
        File[] files = file.listFiles();
        if(files==null)
            return file.delete();
        for (File file1 : files) {
            if (file1.isDirectory())
                success &= deleteDir(file1);
            else
                success &= file1.delete();
            if (!success)
                return false;
        }
        return file.delete();
    }

    /**
     * Removes all empty directories from a directory.
     * <p/>
     * <p><em>Note that a directory that contains only empty
     * directories, directly or not, will be removed!</em></p>
     * <p/>
     * <p>Recurses depth-first to find the leaf directories
     * which are empty and removes them, then unwinds the
     * recursion stack, removing directories which have
     * become empty themselves, etc...</p>
     *
     * @param dir           the root directory to scan for empty directories.
     * @param removeIfEmpty whether to remove the root directory
     *                      itself if it becomes empty.
     * @return the number of empty directories actually removed.
     */
    public static int removeEmptyDirectories(File dir, boolean removeIfEmpty){
        int removedCount = 0;
        if(dir.isDirectory()){
            File[] children = dir.listFiles();
            for(int i = 0; i<children.length; ++i){
                File file = children[i];
                // Test here again to avoid method call for non-directories!
                if(file.isDirectory()){
                    removedCount += removeEmptyDirectories(file, true);
                }
            }
            if(children.length>0){
                // This directory may have become empty...
                // We need to re-query its children list!
                children = dir.listFiles();
            }
            if(children.length<1 && removeIfEmpty){
                dir.delete();
                ++removedCount;
            }
        }
        return removedCount;
    }

    /*-------------------------------------------------[ Relative Paths ]---------------------------------------------------*/

    public static File resolve(File home, String relativePath){
        if(File.separatorChar!='\\')
            relativePath = relativePath.replace('\\', File.separatorChar);
        if(File.separatorChar!='/')
            relativePath = relativePath.replace('/', File.separatorChar);
        File file = new File(relativePath);
        if(file.isAbsolute())
            return file;
        else
            return new File(home, relativePath);
    }

    public static String getRelativePath(File home, File file) throws IOException {
        File common = getSharedAncestor(home, file);
        if(common==null)
            return file.getCanonicalPath();

        home = home.getCanonicalFile();
        file = file.getCanonicalFile();

        StringBuffer up = new StringBuffer();
        while(!home.equals(common)){
            home = home.getParentFile();
            if(up.length()!=0)
                up.append("/");
            up.append(".."); //NOI18N
        }

        StringBuffer down = new StringBuffer();
        while(!file.equals(common)){
            if(down.length()==0)
                down.insert(0, file.getName());
            else
                down.insert(0, file.getName() + "/");
            file = file.getParentFile();
        }

        return up.length()>0
                ? up + "/"+ down
                : down.toString();
    }

    private static int getLevel(File file){
        int levels = 0;
        while((file = file.getParentFile())!=null)
            levels++;
        return levels;
    }

    public static File getSharedAncestor(File file1, File file2) throws IOException {
        file1 = file1.getCanonicalFile();
        file2 = file2.getCanonicalFile();

        if(file2.equals(file1))
            return file1;

        int level1, level2, diff;
        File node1, node2;

        level1 = getLevel(file1);
        level2 = getLevel(file2);

        if(level2>level1){
            diff = level2-level1;
            node1 = file2;
            node2 = file1;
        } else{
            diff = level1-level2;
            node1 = file1;
            node2 = file2;
        }

        // Go up the tree until the nodes are at the same level
        while(diff>0){
            node1 = node1.getParentFile();
            diff--;
        }

        // Move up the tree until we find a common ancestor.  Since we know
        // that both nodes are at the same level, we won't cross paths
        // unknowingly (if there is a common ancestor, both nodes hit it in
        // the same iteration).

        do{
            if(node1.equals(node2))
                return node1;
            node1 = node1.getParentFile();
            node2 = node2.getParentFile();
        } while(node1!=null);// only need to check one -- they're at the
        // same level so if one is null, the other is

        if(node1!=null || node2!=null)
            throw new Error("nodes should be null");

        return null;
    }

    /*-------------------------------------------------[ Smart RelativePaths ]---------------------------------------------------*/

    public static String getRelativePath(File home, File file, Map/*<String, File>*/ favorites) throws IOException {
        String filePath = file.getCanonicalPath();
        Iterator iter = favorites.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String value = ((File)entry.getValue()).getCanonicalPath();
            if(filePath.startsWith(value)){
                String key = (String)entry.getKey();
                return '$'+key+'$'+filePath.substring(value.length());
            }
        }
        return getRelativePath(home, file);
    }

    public static String getFavorite(String relativePath, Map/*<String, File>*/ favorites){
        Iterator iter = favorites.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String key = '$'+(String)entry.getKey()+'$';
            if(relativePath.startsWith(key))
                return (String)entry.getKey();
        }
        return null;
    }

    public static File resolve(File home, String relativePath, Map/*<String, File>*/ favorites){
        if(File.separatorChar!='\\')
            relativePath = relativePath.replace('\\', File.separatorChar);
        if(File.separatorChar!='/')
            relativePath = relativePath.replace('/', File.separatorChar);
        String favorite = getFavorite(relativePath, favorites);
        if(favorite!=null)
            return new File((File)favorites.get(favorite), relativePath.substring(favorite.length()+2));
        else
            return resolve(home, relativePath);
    }

    /*-------------------------------------------------[ Misc ]---------------------------------------------------*/

    private static Boolean caseSensitive = null;
    /** Checks if file names are case sensitive on the host operating system. */
    public static boolean areFileNamesCaseSensitive(){
        if(caseSensitive==null) // optimization: don't create objects every time
            caseSensitive = Util.toBoolean(new File("test").equals(new File("TEST"))); //NOI18N
        return caseSensitive.booleanValue();
    }

    public static String getExtension(File file){
        String name = file.getName();
        int index = name.lastIndexOf('.');
        return index!=-1 ? name.substring(index+1) : null;
    }

    public static File findFreeFile(File folder, String name, String ext){
        if(name==null || ext==null) throw new NullPointerException();
        name = getValidFileName(name, ext);
        File file = new File(folder, name+'.'+ext);
        if(!file.exists())
            return file;
        for(int i=1; ; i++){
            file = new File(folder, name + '_' + i + '.' +ext);
            if(!file.exists())
                return file;
        }
    }

    // note: "sister" is preferred in English, please don't ask me why ??
    /**
     * Finds brother file with same base name but different extension.
     *
     * @param file the file to find the brother for or <CODE>null</CODE>
     * @param ext  extension for the brother file
     * @return a brother file (with the requested extension and the same parent folder as the original) or
     *         <CODE>null</CODE> if the brother file can't not exist or the original file was <CODE>null</CODE>
     */
    public static File findBrother (File file, String ext) {
        if(file == null) return null;
        File parent = file.getParentFile();
        if(parent==null) return null;

        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));

        return new File(parent, name+'.'+ext);
    }

    /*-------------------------------------------------[ FileName Lengths ]---------------------------------------------------*/

    private static boolean maxFileNameLengthFound = false;
    private static int maxFileNameLength = 8;
    public static int getValidFileNameLength(int len){
        if(len<=maxFileNameLength)
            return len;
        if(maxFileNameLengthFound)
            return maxFileNameLength;

        char ch[] = new char[len];
        for(int i=0; i<len; i++)
            ch[i] = 't';

        for(int i=maxFileNameLength-1; i<len; i++){
            File file = new File(new String(ch, 0, i));
            if(file.exists()){
                maxFileNameLength = i+1;
                continue;
            }
            try{
                if(file.createNewFile()){
                    maxFileNameLength = i+1;
                    file.delete();
                }
            } catch(IOException ex){
                maxFileNameLengthFound = true;
                return maxFileNameLength;
            }
        }
        return maxFileNameLength;
    }

    public static String getValidFileName(String name, String ext){
        name = StringUtil.toJavaIdentifier(name);
        int validLen = getValidFileNameLength(name.length()+1+ext.length());
        return name.length() > validLen
                ? name.substring(0, validLen)
                : name;
    }

    public boolean isValidFileName(String name){
        if(name.indexOf('/')!=-1 || name.indexOf('\\')!=-1)
            return false;
        return name.length()==getValidFileNameLength(name.length());
    }

    /**
     * Copy File using the old 1.3 IO Library
     * @throws IOException
     */
    public static void copyFileUsingIO(File in, File out) throws IOException {
        InputStream inStream = new FileInputStream(in);
        OutputStream outStream = new FileOutputStream(out);
        new StreamPumper(inStream, outStream, true).execute();
    }

    public static void copyDirectory(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }

            String[] children = sourceLocation.list();
            for (int i = 0, childrenLength = children.length; i < childrenLength; i++) {
                String child = children[i];
                copyDirectory(new File(sourceLocation, child),
                        new File(targetLocation, child));
            }
        } else {
            copyFileUsingIO(sourceLocation,targetLocation);
        }
    }

    public static boolean isAbsolutePath(String str){
        File f = new File(str);
        return f.isAbsolute();
    }

    public static URL file2URL(File file){
        try{
            return file.toURI().toURL();
        } catch(MalformedURLException e){
            throw new RuntimeException("this should never happen: " + e); //NOI18N
        }
    }

}
