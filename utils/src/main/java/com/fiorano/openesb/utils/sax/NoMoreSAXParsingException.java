/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import org.xml.sax.SAXException;

public class NoMoreSAXParsingException extends SAXException{
    private Object data;

    public NoMoreSAXParsingException(Object data){
        super((String)null);
        this.data = data;
    }

    public NoMoreSAXParsingException(String message, Object data){
        super(message);
        this.data = data;
    }

    public NoMoreSAXParsingException(Object data, Exception e){
        super(e);
        this.data = data;
    }

    public NoMoreSAXParsingException(String message, Exception e, Object data){
        super(message, e);
        this.data = data;
    }

    public Object getData(){
        return data;
    }
}