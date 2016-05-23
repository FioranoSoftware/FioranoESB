/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPumper implements Runnable{

    // TODO: make SIZE an instance variable.
    // TODO: add a status flag to note if an error occurred in run.

    private static final int SIZE = 128;
    private InputStream is;
    private OutputStream os;
    private boolean finished;
    private boolean closeWhenExhausted;
    private IOException exception;

    /**
     * Create a new stream pumper.
     *
     * @param is                 input stream to read data from
     * @param os                 output stream to write data to.
     * @param closeWhenExhausted if true, the output stream will be closed when
     *                           the input is exhausted.
     */
    public StreamPumper(InputStream is, OutputStream os,
                        boolean closeWhenExhausted){
        this.is = is;
        this.os = os;
        this.closeWhenExhausted = closeWhenExhausted;
    }

    /**
     * Create a new stream pumper.
     *
     * @param is input stream to read data from
     * @param os output stream to write data to.
     */
    public StreamPumper(InputStream is, OutputStream os){
        this(is, os, false);
    }


    /**
     * Copies data from the input stream to the output stream.
     * <p/>
     * Terminates as soon as the input stream is closed or an error occurs.
     */
    public void run(){
        try{
            execute();
        } catch(IOException ex){
            exception = ex;
        }
    }

    public void execute() throws IOException{
        synchronized(this){
            // Just in case this object is reused in the future
            finished = false;
        }

        final byte[] buf = new byte[SIZE];

        int length;
        try{
            while((length = is.read(buf))>0){
                os.write(buf, 0, length);
            }
        } catch(Exception e){
            // ignore errors
        } finally{
            try{
                is.close();
            } catch(IOException ex){
                exception = ex;
            }
            if(closeWhenExhausted){
                try{
                    os.close();
                } catch(IOException ex){
                    exception = ex;
                }
            }
            synchronized(this){
                finished = true;
                notifyAll();
            }
        }
        if(exception!=null)
            throw exception;
    }

    /**
     * Tells whether the end of the stream has been reached.
     *
     * @return true is the stream has been exhausted.
     */
    public synchronized boolean isFinished(){
        return finished;
    }

    /**
     * This method blocks until the stream pumper finishes.
     *
     * @see #isFinished()
     */
    public synchronized void waitFor()
            throws InterruptedException{
        while(!isFinished()){
            wait();
        }
    }

    public IOException getException(){
        return exception;
    }
}
