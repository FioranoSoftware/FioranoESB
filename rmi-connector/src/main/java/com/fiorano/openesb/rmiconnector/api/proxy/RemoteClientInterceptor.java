/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.HashMap;
import java.io.Serializable;

public class RemoteClientInterceptor implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 420549831750656506L;
    private IRemoteServerProxy serverProxy;


    public RemoteClientInterceptor(IRemoteServerProxy serverProxy) {
        this.serverProxy = serverProxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HashMap additionalInfo=captureData(method);
        return serverProxy.invoke(method.getName(), args, additionalInfo);
    }

    private HashMap captureData(Method method)
    {
        HashMap additionalInfo = new HashMap();
        //get clients langauage --> for the feature, a server in england can serve clients in japan... i.e. errors / messages can be in japanese.
        additionalInfo.put(Locale.class.toString(), Locale.getDefault());

        //get his ipaddresses if u want to :) --> not including this as of now.
        additionalInfo.put("method_parameter_types", getArgumentClassesKey(method));
        return additionalInfo;
    }

    private String getArgumentClassesKey(Method m) {
        Class argClasses[] = m.getParameterTypes();
        if (argClasses.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Class c : argClasses) {
            builder.append(c.getName());
            builder.append(':');
        }
        String args = builder.toString();
        args = args.substring(0, args.lastIndexOf(":"));
        return args;
    }
}
