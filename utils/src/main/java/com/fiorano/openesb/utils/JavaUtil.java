/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

public class JavaUtil{
    public static final String JAVA_VERSION = System.getProperty("java.version"); //NOI18N
    public static final List KEY_WORDS = Arrays.asList(new String[]{
            "abstract", "assert",                                                     //NOI18N
            "boolean", "break", "byte",                                               //NOI18N
            "case", "catch", "class", "const", "continue",                            //NOI18N
            "default", "do", "double",                                                //NOI18N
            "else", "enum", "extends",                                                //NOI18N
            "false", "final", "finaly", "float", "for",                              //NOI18N
            "goto",                                                                   //NOI18N
            "if", "implements", "import", "instanceof", "int", "interface",           //NOI18N
            "long",                                                                   //NOI18N
            "native", "new", "null",                                                  //NOI18N
            "package", "private", "protected", "public",                              //NOI18N
            "return",                                                                 //NOI18N
            "short", "static", "strictfp", "super", "switch", "synchronized",         //NOI18N
            "this", "throw", "throws", "transient", "true", "try",                    //NOI18N
            "void", "volatile",                                                       //NOI18N
            "while",                                                                  //NOI18N
    });

    public static String getPID() throws IOException, InterruptedException {
        String pid = System.getProperty("pid"); //NOI18N
        if(pid==null){
            String p= ManagementFactory.getRuntimeMXBean().getName();
            pid= p.substring(0,p.indexOf('@')) ;
            if(pid!=null)
                System.setProperty("pid", pid); //NOI18N
            /* String cmd[] = null;
            File tempFile = null;
            if(OS.isUnix())     {
                if (OS.isUnix("Solaris")  ){
                    String p= ManagementFactory.getRuntimeMXBean().getName();
                    pid= p.substring(0,p.indexOf('@')) ;  }
                else
                    cmd = new String[]{ "/bin/sh", "-c", "echo $$ $PPID" }; //NOI18N
              }
            else if(OS.isWindows()){
                tempFile = FileUtil.findFreeFile(FileUtil.TEMP_DIR, "getpids", "exe"); //NOI18N
                // extract the embedded getpids.exe file from the jar and save it to above file
                new StreamPumper(JavaUtil.class.getResourceAsStream("getpids.exe"), new FileOutputStream(tempFile), true).execute(); //NOI18N
                cmd = new String[]{ tempFile.getAbsolutePath() };
            }
            if(cmd!=null){
                Process p = Runtime.getRuntime().exec(cmd);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                new StreamPumper(p.getInputStream(), bout, true).execute();
                if(tempFile!=null)
                    tempFile.delete();

                StringTokenizer stok = new StringTokenizer(bout.toString());
                stok.nextToken(); // this is pid of the process we spanned
                pid = stok.nextToken();
                if(pid!=null)
                    System.setProperty("pid", pid); //NOI18N
            }*/
        }
        return pid;
    }
}
