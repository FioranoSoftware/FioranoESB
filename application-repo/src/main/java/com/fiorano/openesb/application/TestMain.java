/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ApplicationParser;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.File;

public class TestMain {
    public static void main(String [] args){
        try {
           Application application = ApplicationParser.readApplication(new File("D:\\sources_dec22\\installer\\esb\\server\\repository\\applications\\SIMPLECHAT\\1.0"), false);
          System.out.println(application.toString());
        } catch (FioranoException e) {
            e.printStackTrace();
        }
    }
}
