/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.exception.FioranoException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Util{

    public static int hashCode(Object obj){
        return obj!=null ? obj.hashCode() : 0;
    }

    // Returns true if the specified arguments are equal, or both null.
    public static boolean equals(Object a, Object b){
        return a==null
                ? b==null
                : b!=null && a.equals(b);
    }

    public static Boolean toBoolean(boolean b){
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    private static boolean DEBUG = false;
    private static final String SERVICE_DESCRIPTOR_XML = "ServiceDescriptor.xml";
    private static final String NAME = "name";
    private static final String RESOURCE = "resource";
    private static final String EXECUTION = "execution";
    private static final String FALSE = "false";
    private static final String REQUIRED = "required";

    /**
     * Check whether OS is windows
     *
     * @return boolean
     */
    public static boolean isWindows(){
        String os = System.getProperty("os.name");

        if(os==null)
            return false;

        int index = os.toLowerCase().indexOf("win");

        return (index!=-1);
    }

    /**
     * Gets the fileName from the complete path
     *
     * @param completePath Description of the Parameter
     * @return The fileName value
     */
    public static String getFileName(String completePath){
        if(completePath==null)
            return null;

        return completePath.substring(
                completePath.lastIndexOf(File.separator)+1,
                completePath.length());
    }

    /**
     * Returns exception string for class
     *
     * @param t
     * @return
     */
    public static String getExceptionString(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        t.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Returns absolute path for class
     *
     * @param str
     * @return
     */
    public static boolean isAbsolutePath(String str){
        File f = new File(str);

        return f.isAbsolute();
    }

    /**
     * Get the list of files contained in parent parameter directory
     *
     * @param sourceName
     * @param filter
     * @return
     */
    public static ArrayList getFiles(String sourceName, FilenameFilter filter){
        File sourceFile = new File(sourceName);
        ArrayList result = new ArrayList();

        if(!sourceFile.exists() || !sourceFile.isDirectory())
            return result;

        String[] fileNames = sourceFile.list(filter);

        if(fileNames==null)
            return result;

        for(int i = 0; i<fileNames.length; i++){
            File file = new File(fileNames[i]);

            if(file.isDirectory())
                result.addAll(getFiles(fileNames[i], filter));
            else
                result.add(fileNames[i]);
        }

        return result;
    }

    /**
     * Specifies whether the directory is deleted or not.
     *
     * @param dirName
     * @return true only if directory does not exists.
     */
    public static boolean isDirectoryDeleted(String dirName){
        File dir = new File(dirName);
        return (!dir.exists());
    }

    /**
     * Returns all attributes for class
     *
     * @param obj
     * @return
     */
    public static HashMap getAllAttributes(Object obj){
        return getAttrs(obj.getClass().getMethods(), obj);
    }

    /**
     * Returns attributes for class
     *
     * @param obj
     * @return
     */
    public static HashMap getAttributes(Object obj){
        return getAttrs(obj.getClass().getDeclaredMethods(), obj);
    }

    /**
     * Gets the content of parameter file
     *
     * @param fileName
     * @return loaded character data from the file
     * @throws FioranoException
     */
    public static String getData(String fileName)
            throws FioranoException {
        File file = new File(fileName);

        if(!file.exists())
            throw new FioranoException("FILE_INVALID_ERROR");

        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] fileInput = new byte[(int)file.length()];

            fis.read(fileInput, 0, fileInput.length);
            fis.close();
            return new String(fileInput);
        }
        catch(Exception e){
            throw new FioranoException("FILE_READ_WRITE_ERROR", e);
        }
    }

    /**
     * @param input
     * @return
     * @throws FioranoException
     */
    public static String readString(InputStream input)
            throws FioranoException{
        try{
            byte[] nameBytes = new byte[readInt(input)];

            input.read(nameBytes);
            return new String(nameBytes);
        }
        catch(Exception e){
            throw new FioranoException("unable.to.read.string.from.inputstream");
        }
    }

    /**
     * @param output
     * @param data
     * @throws FioranoException
     */
    public static void writeString(OutputStream output, String data)
            throws FioranoException{
        try{
            if(data==null)
                writeInt(output, 0);
            else{
                byte[] dataBytes = data.getBytes();

                writeInt(output, dataBytes.length);
                output.write(dataBytes, 0, dataBytes.length);
            }
        }
        catch(Exception e){
            throw new FioranoException("unable.to.write.string.to.outputstream");
        }
    }

    /**
     * @param out
     * @param v
     * @throws FioranoException
     */
    public static void writeInt(OutputStream out, int v)
            throws FioranoException{
        try{
            out.write((v >>> 24) & 0xFF);
            out.write((v >>> 16) & 0xFF);
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        }
        catch(Exception e){
            throw new FioranoException("unable.to.write.int.to.outputstream");
        }
    }

    /**
     * @param in
     * @return
     * @throws FioranoException
     */
    public static int readInt(InputStream in)
            throws FioranoException{
        try{
            int ch1 = in.read();
            int ch2 = in.read();
            int ch3 = in.read();
            int ch4 = in.read();

            if((ch1 | ch2 | ch3 | ch4)<0)
                throw new FioranoException("unable.to.read.int.from.inputstream");
            return ((ch1 << 24)+(ch2 << 16)+(ch3 << 8)+(ch4 << 0));
        }
        catch(Exception e){
            throw new FioranoException("unable.to.read.int.from.inputstream");
        }
    }

    /**
     * @param out
     * @param v
     * @throws FioranoException
     */
    public static void writeLong(OutputStream out, long v)
            throws FioranoException{
        try{
            out.write((int)(v >>> 56) & 0xFF);
            out.write((int)(v >>> 48) & 0xFF);
            out.write((int)(v >>> 40) & 0xFF);
            out.write((int)(v >>> 32) & 0xFF);
            out.write((int)(v >>> 24) & 0xFF);
            out.write((int)(v >>> 16) & 0xFF);
            out.write((int)(v >>> 8) & 0xFF);
            out.write((int)(v >>> 0) & 0xFF);
        }
        catch(Exception e){
            throw new FioranoException("unable.to.write.long.to.outputstream");
        }
    }

    /**
     * @param in
     * @return
     * @throws FioranoException
     */
    public static long readLong(InputStream in)
            throws FioranoException{
        return ((long)(readInt(in)) << 32)+(readInt(in) & 0xFFFFFFFFL);
    }

//////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param input
     * @return
     * @throws FioranoException
     */
    public static String readString(DataInput input)
            throws FioranoException{
        try{
            byte[] nameBytes = new byte[readInt(input)];

            input.readFully(nameBytes);
            return new String(nameBytes);
        }
        catch(Exception e){
            throw new FioranoException("unable.to.read.string.from.datainput");
        }
    }

    /**
     * @param output
     * @param data
     * @throws FioranoException
     */
    public static void writeString(DataOutput output, String data)
            throws FioranoException{
        try{
            if(data==null)
                writeInt(output, 0);
            else{
                byte[] dataBytes = data.getBytes();

                writeInt(output, dataBytes.length);
                output.write(dataBytes, 0, dataBytes.length);
            }
        }
        catch(Exception e){
            throw new FioranoException("unable.to.write.string.to.dataoutput");
        }
    }

    /**
     * @param out
     * @param v
     * @throws FioranoException
     */
    public static void writeInt(DataOutput out, int v)
            throws FioranoException{
        try{
            out.write((v >>> 24) & 0xFF);
            out.write((v >>> 16) & 0xFF);
            out.write((v >>> 8) & 0xFF);
            out.write((v >>> 0) & 0xFF);
        }
        catch(Exception e){
            throw new FioranoException( "unable.to.write.int.to.dataoutput");
        }
    }

    /**
     * @param in
     * @return
     * @throws FioranoException
     */
    public static int readInt(DataInput in)
            throws FioranoException{
        try{
            int ch1 = in.readByte();
            int ch2 = in.readByte();
            int ch3 = in.readByte();
            int ch4 = in.readByte();

            if((ch1 | ch2 | ch3 | ch4)<0)
                throw new FioranoException("unable.to.read.int.from.datainput");
            return ((ch1 << 24)+(ch2 << 16)+(ch3 << 8)+(ch4 << 0));
        }
        catch(Exception e){
            throw new FioranoException("unable.to.read.int.from.datainput");
        }
    }

    /**
     * @param out
     * @param v
     * @throws FioranoException
     */
    public static void writeLong(DataOutput out, long v)
            throws FioranoException{
        try{
            out.write((int)(v >>> 56) & 0xFF);
            out.write((int)(v >>> 48) & 0xFF);
            out.write((int)(v >>> 40) & 0xFF);
            out.write((int)(v >>> 32) & 0xFF);
            out.write((int)(v >>> 24) & 0xFF);
            out.write((int)(v >>> 16) & 0xFF);
            out.write((int)(v >>> 8) & 0xFF);
            out.write((int)(v >>> 0) & 0xFF);
        }
        catch(Exception e){
            throw new FioranoException("unable.to.write.long.to.dataoutput");
        }
    }

    /**
     * @param in
     * @return
     * @throws FioranoException
     */
    public static long readLong(DataInput in)
            throws FioranoException{
        return ((long)(readInt(in)) << 32)+(readInt(in) & 0xFFFFFFFFL);
    }

    /**
     * @param buff
     * @param buffPointer
     * @param intData
     * @return
     */
    public static int writeInt(byte[] buff, int buffPointer, int intData){
        buff[buffPointer++] = (byte)((intData) & 0xFF);
        buff[buffPointer++] = (byte)((intData >>> 8) & 0xFF);
        buff[buffPointer++] = (byte)((intData >>> 16) & 0xFF);
        buff[buffPointer++] = (byte)((intData >>> 24) & 0xFF);
        return buffPointer;
    }

    /**
     * @param buff
     * @param buffPointer
     * @param intData
     * @param higherFirst
     * @return
     */
    public static int writeInt(byte[] buff, int buffPointer, int intData,
                               boolean higherFirst){
        if(!higherFirst)
            return writeInt(buff, buffPointer, intData);

        buff[buffPointer++] = (byte)((intData >>> 24) & 0xFF);
        buff[buffPointer++] = (byte)((intData >>> 16) & 0xFF);
        buff[buffPointer++] = (byte)((intData >>> 8) & 0xFF);
        buff[buffPointer++] = (byte)((intData) & 0xFF);
        return buffPointer;
    }

    /**
     * @param buff
     * @param buffPointer
     * @param shortIntData
     * @return
     */
    public static int writeShort(byte[] buff, int buffPointer, int shortIntData){
        buff[buffPointer++] = (byte)((shortIntData) & 0xFF);
        buff[buffPointer++] = (byte)((shortIntData >>> 8) & 0xFF);
        return buffPointer;
    }

    /**
     * @param buff
     * @param buffPointer
     * @param byteData
     * @return
     */
    public static int writeByte(byte[] buff, int buffPointer, int byteData){
        buff[buffPointer++] = (byte)(byteData);
        return buffPointer;
    }

    /**
     * @param buff
     * @param buffPointer
     * @param str
     * @return
     */
    public static int writeUTF(byte[] buff, int buffPointer, String str){
        int len = str.length();

        buff[buffPointer++] = (byte)((len) & 0xFF);
        buff[buffPointer++] = (byte)((len >>> 8) & 0xFF);

        for(int i = 0; i<len; ++i)
            buff[buffPointer++] = (byte)str.charAt(i);

        return buffPointer;
    }

    /**
     * @param dir
     * @return
     */
    public static boolean delTree(File dir){
        String[] fileList = dir.list();
        String path = dir.getAbsolutePath()+File.separator;
        File file;

        for(int i = 0; i<fileList.length; ++i){
            file = new File(path+fileList[i]);
            if(file.isDirectory())
                delTree(file);
            else
                file.delete();
        }
        return dir.delete();
    }

    /**
     * Delete the directory tree specified by parameter directory name. when user does the crc operation
     * @param directoryName
     * @param maxAttempts
     * @param timeInterval
     */

    @SuppressWarnings("unchecked")
    public static void delTreeForCRC(String directoryName, int maxAttempts, long timeInterval) {
        File temp = new File(directoryName);
        if(!new File(directoryName + File.separator + SERVICE_DESCRIPTOR_XML).exists())
            return;
        List<String> filesToDel = getResources(directoryName);
        for(String subDir : filesToDel){
            File tempFile = new File(directoryName+File.separator+subDir);
            boolean deleted=true;
            int j = 0;

            while(j<maxAttempts){
                if(tempFile.exists()){
                    deleted = tempFile.delete();
                }

                try{
                    if(!deleted)
                        Thread.sleep(timeInterval);
                    else
                        break;
                }
                catch(InterruptedException ie){
                    RuntimeException re = new RuntimeException("error.while.deleting.the.tree");
                    re.initCause(ie);
                    throw re;
                }
                j++;
            }
        }
    }

    /**
     * get the resources from service-discriptor.xml for deleting the resource files from directory
     * @param directoryName
     * @return
     * @throws FactoryConfigurationError
     */
    private static List<String> getResources(String directoryName)
            throws FactoryConfigurationError {
        List<String> filesToDel = new ArrayList<String>();
        File sdfile = new File(directoryName + File.separator + SERVICE_DESCRIPTOR_XML);
        if(sdfile.length()==0){
            filesToDel.add(SERVICE_DESCRIPTOR_XML);
            return filesToDel;
        }
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            int index =0;
            InputStream in = new FileInputStream(sdfile);
            XMLEventReader reader = inputFactory.createXMLEventReader(in);
            while(reader.hasNext()){
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()){
                    StartElement element = event.asStartElement();
                    if(element.getName().getLocalPart().equals(RESOURCE)|| element.getName().getLocalPart().equals(REQUIRED)){
                        Iterator<Attribute> attributes = element.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(NAME)) {
                                if(!attribute.getValue().startsWith("$")){
                                    filesToDel.add(index++, attribute.getValue());
                                }
                            }
                            if (attribute.getName().toString().equals(EXECUTION)) {
                                if(attribute.getValue().equalsIgnoreCase(FALSE)){
                                    filesToDel.remove(--index);
                                }
                            }
                        }

                    }
                }
            }
            in.close();
            reader.close();
            filesToDel.add(SERVICE_DESCRIPTOR_XML);
        } catch (FileNotFoundException e) {
            RuntimeException re = new RuntimeException("error.while.parsing.the.Service-Descriptor.xml");
            re.initCause(e);
            throw re;
        } catch (XMLStreamException e) {
            RuntimeException re = new RuntimeException("error.while.parsing.the.Service-Descriptor.xml");
            re.initCause(e);
            throw re;
        } catch (IOException e) {
            RuntimeException re = new RuntimeException("error.while.parsing.the.Service-Descriptor.xml");
            re.initCause(e);
            throw re;
        }
        return filesToDel;
    }

    /**
     * Delete the directory tree specified by parameter directory name.
     *
     * @param directoryName directory that need to be deleted
     * @param maxAttempts   maximum attempts that can be made for file deletion
     * @param timeInterval  specfies the sleep time after unsuccessful deletion attempt
     */
    public static void delTree(String directoryName, int maxAttempts, long timeInterval){
        File temp = new File(directoryName);

        if(!temp.isDirectory())
            return;

        String[] subDirs = temp.list();

        for(int i = 0; i<subDirs.length; i++){
            File tempFile = new File(directoryName+File.separator+subDirs[i]);

            if(tempFile.isDirectory()){
                delTree(directoryName+File.separator+subDirs[i], maxAttempts, timeInterval);
                continue;
            }

            boolean deleted;
            int j = 0;

            while(j<maxAttempts){
                deleted = tempFile.delete();
                try{
                    if(!deleted)
                        Thread.sleep(timeInterval);
                    else
                        break;
                }
                catch(InterruptedException ie){
                    RuntimeException re = new RuntimeException("error.while.deleting.the.tree");
                    re.initCause(ie);
                    throw re;
                }
                j++;
            }
        }
        temp.delete();
    }

    /**
     * @param dir
     * @param prefix
     * @return
     */
    public static boolean delAllFiles(File dir, String prefix){
        boolean bUnableToDeleteSomeFiles = false;

        String[] fileList = dir.list();
        String path = dir.getAbsolutePath()+File.separator;
        File file;

        for(int i = 0; i<fileList.length; ++i){
            if(fileList[i].startsWith(prefix)){
                try{
                    file = new File(path+fileList[i]);
                    file.delete();
                }
                catch(Exception e){
                    //  No problem continue with the cleanup of
                    //  rest of the files.
                    //
                    bUnableToDeleteSomeFiles = true;
                }
            }
        }

        return bUnableToDeleteSomeFiles;
    }

    /**
     * @param dir
     * @param prefix
     * @return
     */
    public static int countAllFiles(File dir, String prefix){
        int count = 0;
        String[] fileList = dir.list();

        for(int i = 0; i<fileList.length; ++i){
            if(fileList[i].startsWith(prefix)){
                try{
                    ++count;
                }
                catch(Exception e){
                    //  No problem continue with the cleanup of
                    //  rest of the files.
                    //
                }
            }
        }
        return count;
    }

    /**
     * This API copies the properties from src hashtable to the target hashtable
     *
     * @param src    specifies the source hashtable from which properties should be copied.
     * @param target specifies the target hashtable to which properties should be copied.
     */
    public static void copyProperties(Hashtable src, Hashtable target){
        // get all the keys of the source table.
        Enumeration keys = src.keys();

        // put all the keys from the source to destination table
        while(keys.hasMoreElements()){
            // get the next key
            Object key = keys.nextElement();

            // get the value
            Object value = src.get(key);

            // put the value in the target table
            target.put(key, value);
        }
    }

   /* *//**
     * This API checks whether the parameter error code matches the error code of
     * the parameter Fiorano exception.
     *
     * @param errorCode specifies the error code of the exception.
     * @param exception specifies the exception in which error message needs to be checked
     * @return boolean specifying whether the error code matches or not.
     *//*
    public static boolean errorCodeMatches(String errorCode, FioranoException exception){
        // null error code will match the error code of the last linked exception
        if(errorCode==null)
            return true;

        // initialize the exception
        FioranoException linkedException = exception;

        while(linkedException!=null){
            // get the error code
            String exceptionCode = linkedException.getErrorCode();

            if(exceptionCode==null)
                return false;

            if(errorCode.equalsIgnoreCase(exceptionCode))
                return true;

            try{
                // get the linked exception
                linkedException = (FioranoException)
                        linkedException.getLinkedException();
            }
            catch(ClassCastException ex){
                // In case linked exception is not FioranoException
                return false;
            }
        }

        return false;
    }*/

    /**
     * This API checks whether the parameter error code matches the error code of
     * the parameter FioranoJMSException.
     *
     * @param errorCode specifies the error code of the exception.
     * @param exception specifies the exception in which error message needs to be checked
     * @return boolean specifying whether the error code matches or not.
     */
    public static boolean errorCodeMatches(String errorCode, Exception exception){
        // null error code will match the error code of the last linked exception
        if(errorCode==null)
            return true;

        // initialize the exception
        Exception linkedException = exception;

        while(linkedException!=null) {
            // get the error code
            String exceptionCode = null;
            /*if (linkedException instanceof FioranoJMSException) {
                exceptionCode = ((FioranoJMSException)linkedException).getErrorCode();
            } else if (linkedException instanceof FioranoException){
                exceptionCode = ((FioranoException)linkedException).getErrorCode();
            }*/

            if(exceptionCode==null)
                return false;

            if(errorCode.equalsIgnoreCase(exceptionCode))
                return true;

            try{
                // get the linked exception
             /*   if (linkedException instanceof FioranoJMSException) {
                    linkedException = ((FioranoJMSException) linkedException).getLinkedException();
                } else if (linkedException instanceof FioranoException) {
                    linkedException = ((FioranoException)linkedException).getLinkedException();
                } else
                    linkedException = null;*/
            }
            catch(ClassCastException ex){
                // In case linked exception is not FioranoException and not FioranoJMSException
                return false;
            }
        }

        return false;
    }

    /*
    Returns the first linked exception that matches the errorcode.
     */
    public static Exception getLinkedException(String errorCode, Exception exception) {
        if(errorCode==null)
            return null;

        // initialize the exception
        Exception linkedException = exception;

        while(linkedException!=null) {
            // get the error code
            String exceptionCode = null;
                exceptionCode = linkedException.getMessage();

            if(exceptionCode==null)
                return null;

            if(errorCode.equalsIgnoreCase(exceptionCode))
                return linkedException;

            try{
                // get the linked exception
               /* if (linkedException instanceof FioranoJMSException) {
                    linkedException = ((FioranoJMSException) linkedException).getLinkedException();
                } else if (linkedException instanceof FioranoException) {
                    linkedException = ((FioranoException)linkedException).getLinkedException();
                } else
                    linkedException = null;*/
            }
            catch(ClassCastException ex){
                // In case linked exception is not FioranoException and not FioranoJMSException
                return null;
            }
        }

        return null;
    }

    /**
     * This method evaluates the parameter expression and returns
     * the result.
     * <p/>
     * The limitation of this expression evaluator is that, it can
     * only evaluates the expression of type "a * b".
     * <p/>
     * For example for the expression 2*3, it will return 6.
     *
     * @param expression specifies the expression that needs to be evaluated.
     * @return the long value.
     * @throws FioranoException
     */
    public static long evaluateExpression(String expression)
            throws FioranoException{
        if((expression==null) || (expression.trim().length()==0))
            throw new FioranoException("invalid.expression");

        long result = 1;

        // parse the expression
        StringTokenizer tokenizer = new StringTokenizer(expression, "*");

        while(tokenizer.hasMoreTokens()){
            // get the next token
            String token = tokenizer.nextToken();

            // trim the token
            token = token.trim();

            // parse the token
            long temp_Token = Long.parseLong(token);

            // result
            result = result*temp_Token;
        }

        return result;
    }

    /**
     * @param str
     * @return
     */
    public static String readValue(String str){
        return (str.trim().length()==0) ? null : str;
    }

    /**
     * @param str
     * @return
     */
    public static String writeValue(String str){
        return (str==null) ? "" : str;
    }

    /**
     * Move files from source to target directory.
     *
     * @param fromDirName
     * @param toDirName
     * @param filter
     * @return true if all files moved successfully, false otherwise.
     * @throws FioranoException source/target directory does not exists, or if unable to move some file(s)
     */
    public static boolean moveFiles(String fromDirName, String toDirName,
                                    FilenameFilter filter)
            throws FioranoException{
        boolean bUnableToDeleteSomeFiles = false;

        File fromDir = new File(fromDirName);
        File toDir = new File(toDirName);
        File fromFile;
        File toFile;

        if((!fromDir.exists()) || (!toDir.exists()))
            throw new FioranoException( "copy.failed.no.files.in.path");

        String[] fileList = fromDir.list(filter);

        for(int i = 0; i<fileList.length; ++i){
            try{
                fromFile = new File(fromDirName+File.separator+fileList[i]);
                toFile = new File(toDirName+File.separator+fileList[i]);

                if(!fromFile.renameTo(toFile))
                    bUnableToDeleteSomeFiles=true;
            }
            catch(Exception e){
                //  No problem continue with the cleanup of
                //  rest of the files.
                //
                bUnableToDeleteSomeFiles = true;
            }
        }
        if(bUnableToDeleteSomeFiles)
            throw new FioranoException("copy.failed");
        return bUnableToDeleteSomeFiles;
    }

    /**
     * Try to move files using the source.rename(target) api. If the target file
     * already exists, the rename may not succeed.
     *
     * @param sourcePath String
     * @param targetPath String
     */
    public static void movesFiles(String sourcePath, String targetPath){
        movesFiles(sourcePath, targetPath, false);
    }

    /**
     * Moves files by first calling target.delete() and then
     *
     * @param sourcePath String
     * @param targetPath String
     * @param force      boolean
     */
    public static void movesFiles(String sourcePath, String targetPath, boolean force){

        File sourceFolder = new File(sourcePath);

        sourcePath = sourceFolder.getAbsolutePath();

        File[] allFiles = sourceFolder.listFiles();

        for(int i = 0; allFiles!=null && i<allFiles.length; ++i){
            File file = allFiles[i];

            if(file.isDirectory()){
                // create the directory in the final folder list
                String completePath = file.getAbsolutePath();
                String relativePath = completePath.substring(
                        (sourcePath+File.separator).length(),
                        completePath.length());
                String folder = targetPath+File.separator+relativePath;
                File newFile = new File(folder);

                newFile.mkdirs();
                movesFiles(completePath, newFile.getAbsolutePath(), force);
            } else{
                File target = new File(
                        targetPath+File.separator+
                                getFileName(file.getAbsolutePath()));

                if(force)
                    target.delete();

                file.renameTo(target);
            }
        }
    }

    /**
     * Deletes the parameter file.
     * <p/>
     * In case it is a directory, delete the directory as well as
     * its content.
     *
     * @param basePath
     * @param filter
     * @return
     * @throws FioranoException
     */
    public static boolean deleteFile(String basePath, FilenameFilter filter)
            throws FioranoException{
        // Delete the parameter database.

        File tempDb = new File(basePath);

        return deleteFile(tempDb, filter);
    }

    /**
     * Deletes the contents of the parameter file.
     * <p/>
     * In case parameter file is a directory, then it
     * deletes the directory as well as all its files.
     *
     * @param file
     * @param filter
     * @return
     * @throws FioranoException if one or more file/directory is not deleted
     */
    public static boolean deleteFile(File file, FilenameFilter filter)
            throws FioranoException{
        boolean deletedAll = true;

        if(file==null)
            return true;

        boolean exists = file.exists();

        if(!exists)
            return true;

        // Delete the file in case its not a directory
        if(!file.isDirectory()){
            boolean deleted = file.delete();

            if(DEBUG)
                System.out.println("file.0.deleted.1");
            if(!deleted)
                throw new FioranoException( "unable.to.delete.some.files");
            return deleted;
        }

        // Its a directory
        //File directory = file;

        File[] files = file.listFiles(filter);

        if(files!=null){
            for(int i = 0; i<files.length; i++)
                deletedAll = (deletedAll && deleteFile(files[i], filter));
        }

        boolean deleted = file.delete();

        // Delete the directory.
        deletedAll = (deletedAll && deleted);
        if(DEBUG)
            System.out.println( "directory.0.deleted.1");

        if(!deletedAll)
            throw new FioranoException("unable.to.delete.some.files");

        return deletedAll;
    }


    /**
     * Rename the source file to target file
     *
     * @param source
     * @param target
     * @throws FioranoException if rename is unsuccessful
     */
    public static void renameFile(String source, String target)
            throws FioranoException{
        File sourceFile = new File(source);
        File targetFile = new File(target);

        boolean renamed = sourceFile.renameTo(targetFile);

        if(!renamed)
            throw new FioranoException("failed.to.rename.0.to.1");
    }

    /**
     * Zip a directory and writes it on parameter ZipOutStream.
     *
     * @param bufferSize specifies the size with which data is being read.
     * @param dir2zip
     * @param filter
     * @param zos
     * @throws IOException
     */
    public static void zipDir(String dir2zip, FilenameFilter filter,
                              java.util.zip.ZipOutputStream zos,
                              int bufferSize)
            throws IOException{
        // create a new File object based on the directory we have to zip
        File zipDir = new File(dir2zip);

        zipDir(zipDir, zipDir, filter, zos, bufferSize);
    }


    /**
     * Unzips a zipFile in unzip directory.
     *
     * @param zip
     * @param unzipDir
     * @param bufferSize
     * @throws IOException
     */
    public static void unzip(String zip, String unzipDir, int bufferSize)
            throws IOException{
        // Initialize the unzip Dir.
        createDirs(unzipDir);

        ZipFile zipFile = new ZipFile(zip);
        try {
            Enumeration entries = zipFile.entries();

            while(entries.hasMoreElements()){
                ZipEntry entry = (ZipEntry) entries.nextElement();

                // File
                File file = new File(unzipDir, entry.getName());

                if(entry.isDirectory()){
                    // Assume directories are stored parents first then children.
                    if(DEBUG)
                        System.out.println("extracting.directory.0");

                    if(!file.exists()){
                        boolean created = file.mkdir();

                        if(DEBUG)
                            System.out.println( "directory.0.created.1");
                    }

                    continue;
                }

                if(DEBUG)
                    System.out.println("extracting.file.0");

                BufferedOutputStream bos=null;
                try{
                    // write entry on file-system.
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    InputStream entryStream = zipFile.getInputStream(entry);

                    copyInputStream(entryStream, bos, bufferSize);
                } finally {
                    if(bos!=null)
                        bos.close();
                }
            }
        } finally {
            if(zipFile!=null)
                zipFile.close();
        }
    }

    /**
     * Create directory including all the parent directories.
     *
     * @param dirName
     * @return
     */
    public static boolean createDirs(String dirName){
        File dir = new File(dirName);

        if(dir.exists())
            return true;

        boolean created = createDirs(dir.getParent());

        if(!created)
            return false;

        return dir.mkdir();
    }

    /**
     * Wait on object for parameter timeout period.
     *
     * @param obj
     * @param timeout
     */
    public static void wait(Object obj, long timeout){
        synchronized(obj){
            try{
                obj.wait(timeout);
            }
            catch(InterruptedException ex){
                // Ignore the exception
            }
        }
    }

    /**
     * Compare current VM's version with parameter version.
     *
     * @param version String
     * @return boolean
     */
    public static boolean compareJavaVersion(String version){
        String javaVersion = System.getProperty("java.version");

        // compare the java version
        return (javaVersion.compareTo(version) >= 0);
    }


    private static HashMap getAttrs(Method[] methods, Object obj){
        HashMap hash = new HashMap();

        for(int i = 0; i<methods.length; i++){
            Method m = methods[i];

            String methodName = m.getName().toUpperCase();

            if(!(methodName.startsWith("IS") || methodName.startsWith("GET")))
                continue;

            boolean isGet = false;

            methodName = m.getName();

            if(methodName.startsWith("get"))
                isGet = true;
            if(isGet)
                methodName = methodName.substring(3);
            else
                methodName = methodName.substring(2);
            try{
                hash.put(methodName, m.invoke(obj, new Object[]
                        {}));
            }
            catch(Exception ex){
                RuntimeException re = new RuntimeException("error.while.getting.the.attributes");
                re.initCause(ex);
                throw re;
            }
        }
        return hash;
    }


    /**
     * Copies inputStream on OutputStream.
     *
     * @param in
     * @param out
     * @param bufferSize
     * @throws IOException
     */
    private final static void copyInputStream(InputStream in, OutputStream out, int bufferSize)
            throws IOException{
        byte[] buffer = new byte[bufferSize];
        int len;

        try {
            while((len = in.read(buffer))>=0)
                out.write(buffer, 0, len);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // ignore
            }
            try {
                out.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    /**
     * Zip a directory, where relative path is relativeDir
     *
     * @param bufferSize specifies the size with which data is being read.
     * @param zipDir
     * @param baseDir
     * @param filter
     * @param zos
     * @throws IOException
     */
    private static void zipDir(File zipDir, File baseDir, FilenameFilter filter,
                               java.util.zip.ZipOutputStream zos,
                               int bufferSize)
            throws IOException{
        if(zipDir.isFile()){
            //File file = zipDir;
            String entryName = entryName(zipDir, baseDir);

            // add zip entry
            _addZipEntry(zos, entryName);

            // add file to zip strea,
            _addFileToZipStream(zipDir, zos, bufferSize);
        } else{
            //get a listing of the directory content
            String[] dirList = zipDir.list(filter);

            //loop through dirList, and zip the files
            for(int i = 0; i<dirList.length; i++){
                File file = new File(zipDir, dirList[i]);

                if(file.isDirectory()){
                    String entryName = entryName(file, baseDir);

                    // add zip entry
                    _addZipEntry(zos, entryName);
                }

                zipDir(file, baseDir, filter, zos, bufferSize);
            }
        }
    }

    /**
     * Add file to parameter zipStream
     *
     * @param file
     * @param zos
     * @param bufferSize
     * @throws IOException
     */
    private static void _addFileToZipStream(File file, ZipOutputStream zos,
                                            int bufferSize)
            throws IOException{
        byte[] readBuffer = new byte[bufferSize];
        int bytesIn = 0;

        if(file.canRead()){
            java.io.FileInputStream fis = new java.io.FileInputStream(file);

            try{
                // write the content of the file to the ZipOutputStream
                while((bytesIn = fis.read(readBuffer))!=-1){
                    zos.write(readBuffer, 0, bytesIn);
                }
            } finally {
                // Close the Stream
                fis.close();
            }
        } else {
            System.out.println("unable.to.read.file.0");
        }
    }

    private static void _addZipEntry(ZipOutputStream zos, String entryName)
            throws IOException{
        // create a new zip entry
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry(entryName);

        if(DEBUG)
            System.out.println("entry.name.0");

        // place the zip entry in the ZipOutputStream object
        zos.putNextEntry(zipEntry);
    }


    /**
     * Converts file pathname to a form acceptable to ZIP files.
     * In particular, file separators are converted to forward slashes.
     *
     * @param file        the file to be converted.
     * @param relativeDir the dir which will be referenced when
     *                    caculates the returned file path
     * @return a relative path of the given file according to
     *         the specified relativeDir.
     *         <p/>
     *         For exsample: - If the absolute path of a file
     *         is e:\\a\\b\\c\\file.suf, and the relativeDir is
     *         e:\\a\\b, then "c/file.suf" will be returned.
     *         <p/>
     *         if the String which presents a path name contains
     *         "\\" as separator, "\\" will be replaced by "/"
     * @throws IOException
     */
    private static String entryName(File file, File relativeDir){
        if(relativeDir.isFile())
            return file.getName();

        String fileName = file.getAbsolutePath();
        String relativePath = relativeDir.getAbsolutePath();

        if(!relativePath.endsWith(File.separator)){
            relativePath += File.separator;
        }
        fileName = fileName.substring(relativePath.length());

        if((file.isDirectory()) && (!fileName.endsWith(File.separator)))
            fileName += File.separator;

        return fileName.replace(File.separatorChar, '/');
    }

}
