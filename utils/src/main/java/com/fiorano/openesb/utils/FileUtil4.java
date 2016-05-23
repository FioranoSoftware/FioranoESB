/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

public final class FileUtil4 extends FileUtil{

    /*-------------------------------------------------[ File <--> COPY ]---------------------------------------------------*/
    private static Boolean isNioSupported = null;
    private static String errorMessage = "Invalid argument";

    public static void copyFile(File in, File out) throws IOException {
        if(Boolean.TRUE.equals(isNioSupported))
            copyFileUsingNIO(in, out);
        else if(Boolean.FALSE.equals(isNioSupported))
            copyFileUsingIO(in, out);
        else{
            try{
                copyFileUsingNIO(in, out);
                isNioSupported = Boolean.TRUE;
            } catch(IOException ex){
                if(ex.getMessage() != null && ex.getMessage().indexOf(errorMessage) != -1){
                    isNioSupported = Boolean.FALSE;
                    copyFileUsingIO(in, out);
                }
            }
        }
    }

    /**
     * Copy File using 1.4 NIO Library
     * @throws IOException
     */
    public static void copyFileUsingNIO(File in, File out) throws IOException{
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try{
            sourceChannel = new FileInputStream(in).getChannel();
            destinationChannel = new FileOutputStream(out).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        }
        finally{
            try{
                if(sourceChannel != null)
                    sourceChannel.close();
            } finally{
                if(destinationChannel != null)
                    destinationChannel.close();
            }
        }
    }
    /*-------------------------------------------------[ File <--> URL ]---------------------------------------------------*/

    /**
     * converts url to file
     * returns null if the given url is not file url
     */
    public static File url2File(URL url){
        try{
            return url.getProtocol().equals("file") //NOI18N
                    ? new File(new URI(url.toString()))
                    : null;
        } catch(URISyntaxException e){
            throw new RuntimeException("this should never happen: " + e); //NOI18N
        }
    }

    /**
     * converts file to url
     * it properly encodes any special characters
     * that might appear in file path
     * <p/>
     * don't use File.toURL() which lacks this feature
     */
    public static URL file2URL(File file){
        try{
            return file.toURI().toURL();
        } catch(MalformedURLException e){
            throw new RuntimeException("this should never happen: " + e); //NOI18N
        }
    }

    public static void main(String[] args){
        System.err.println(file2URL(new File("../ab c.txt"))); //NOI18N
    }
}
