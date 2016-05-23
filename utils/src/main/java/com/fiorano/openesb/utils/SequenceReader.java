/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;

public class SequenceReader extends Reader {
    private Enumeration e;
    private Reader in;

    public SequenceReader(Enumeration e/*<Reader>*/){
        this.e = e;
        try{
            nextReader();
        } catch(IOException ex){
            throw new Error("Panic");
        }
    }

    public SequenceReader(Reader s1, Reader s2){
        Vector v = new Vector(2);

        v.addElement(s1);
        v.addElement(s2);
        e = v.elements();
        try{
            nextReader();
        } catch(IOException ex){
            throw new Error("Panic");
        }
    }

    /**
     * Continues reading in the next stream if an EOF is reached.
     */
    final void nextReader() throws IOException{
        if(in!=null)
            in.close();

        if(e.hasMoreElements()){
            in = (Reader)e.nextElement();
            if(in==null)
                throw new NullPointerException();
        }else
            in = null;
    }

    public int read() throws IOException{
        if(in==null)
            return -1;
        int c = in.read();
        if(c==-1){
            nextReader();
            return read();
        }
        return c;
    }

    public int read(char ch[], int off, int len) throws IOException{
        if(in==null)
            return -1;
        else if(ch==null)
            throw new NullPointerException();
        else if((off<0) || (off>ch.length) || (len<0) ||
                ((off+len)>ch.length) || ((off+len)<0)){
            throw new IndexOutOfBoundsException();
        }else if(len==0)
            return 0;

        int n = in.read(ch, off, len);
        if(n<=0){
            nextReader();
            return read(ch, off, len);
        }
        return n;
    }

    public void close() throws IOException{
        do{
            nextReader();
        }while(in!=null);
    }
}

