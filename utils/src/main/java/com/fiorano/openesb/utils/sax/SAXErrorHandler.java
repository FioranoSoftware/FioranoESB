/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.sax;

import com.fiorano.openesb.utils.ErrorListener;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SAXErrorHandler implements ErrorHandler {
    private final ErrorListener delegate;

    public SAXErrorHandler(ErrorListener delegate){
        this.delegate = delegate;
    }

    public void error(SAXParseException exception) throws SAXException {
        try{
            delegate.error((Exception)exception);
        } catch(SAXException e){
            throw e;
        } catch(Exception e){
            throw new SAXException(e);
        }
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        try{
            delegate.fatalError((Exception)exception);
        } catch(SAXException e){
            throw e;
        } catch(Exception e){
            throw new SAXException(e);
        }
    }

    public void warning(SAXParseException exception) throws SAXException {
        try{
            delegate.warning((Exception)exception);
        } catch(SAXException e){
            throw e;
        } catch(Exception e){
            throw new SAXException(e);
        }
    }
}