/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import javax.xml.namespace.QName;

public final class ClarkName{
    public static String toClarkName(String uri, String name){
        return uri!=null && uri.length()>0
                ? '{' + uri + '}' + name
                : name;
    }

    public static String[] getParts(String clarkName){
        String namespace, localName;

        if(clarkName != null && clarkName.trim().length() != 0 && clarkName.charAt(0) == '{'){
            int closeBrace = clarkName.indexOf('}');
            if(closeBrace < 0)
                throw new IllegalArgumentException("No closing '}' in Clark name");
            namespace = clarkName.substring(1, closeBrace);
            if(closeBrace == clarkName.length())
                throw new IllegalArgumentException("Missing local part in Clark name");
            localName = clarkName.substring(closeBrace + 1);
        } else{
            namespace = "";      //NOI18N
            localName = clarkName;
        }

        return new String[]{namespace, localName};
    }

    public static String getNamespace(String clarkName){
        if(clarkName != null && clarkName.trim().length() != 0) {
            if(clarkName.charAt(0) == '{'){
                int closeBrace = clarkName.indexOf('}');
                if(closeBrace < 0)
                    throw new IllegalArgumentException("No closing '}' in Clark name");
                return clarkName.substring(1, closeBrace);
            }
        }
        return "";
    }

    public static String getLocalName(String clarkName){
        if(clarkName != null && clarkName.trim().length() != 0) {
            if(clarkName.charAt(0) == '{'){
                int closeBrace = clarkName.indexOf('}');
                if(closeBrace < 0)
                    throw new IllegalArgumentException("No closing '}' in Clark name");
                if(closeBrace == clarkName.length())
                    throw new IllegalArgumentException("Missing local part in Clark name");
                return clarkName.substring(closeBrace + 1);
            }
        }
        return clarkName==null ? new String("") : clarkName;
    }

    public static QName toQName(String clarkName){
        String parts[] = getParts(clarkName);
        return new QName(parts[0].intern(), parts[1].intern());
    }
}
