/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.exception.FioranoException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Enumeration;
import java.util.Vector;

public class XMLDmiUtil
{
    //  PLEASE NOTE::::

    //  Whatever change you make to this class, please make the IDENTICAL changes to
    //  ApplicationComposer's com.fiorano.appdesigner.utils.XMLDmiUtil class.

    /**
     * Returns node object for class
     *
     * @param nodeName
     * @param nodeValue
     * @param document
     * @return
     * @exception FioranoException
     */
    public static Node getNodeObject(String nodeName, String nodeValue, Document document)
        throws FioranoException
    {
        if (nodeName != null)
        {
            Element node = document.createElement(nodeName);

            if (nodeValue == null)
                nodeValue = "";

            Node pcdata = document.createTextNode(nodeValue);

            node.appendChild(pcdata);
            return node;
        }
        else
            return null;
    }

    /**
     * Returns attribute as boolean for class
     *
     * @param elem
     * @param attribName
     * @return
     */
    public static boolean getAttributeAsBoolean(Element elem, String attribName)
    {
        String atribVal = elem.getAttribute(attribName);
        boolean retVal = false;

        try
        {
            retVal = Boolean.valueOf(atribVal).booleanValue();
        }
        catch (Exception e)
        {
        }
        return retVal;
    }

    /**
     * Returns attribute as boolean for class
     *
     * @param elem
     * @param attribName
     * @param defaultVal
     * @return
     */
    public static boolean getAttributeAsBoolean(Element elem, String attribName, boolean defaultVal)
    {
        String atribVal = elem.getAttribute(attribName);
        boolean retVal = defaultVal;

        try
        {
            retVal = Boolean.valueOf(atribVal).booleanValue();
        }
        catch (Exception e)
        {
        }
        return retVal;
    }

    /**
     * Adds a feature to the VectorValues attribute of the XMLDmiUtil class
     *
     * @param nodeName The feature to be added to the VectorValues attribute
     * @param vals The feature to be added to the VectorValues attribute
     * @param document The feature to be added to the VectorValues attribute
     * @param root0 The feature to be added to the VectorValues attribute
     * @exception FioranoException
     */
    public static void addVectorValues(String nodeName, Vector vals,
        Document document, Node root0)
        throws FioranoException
    {
        if (vals != null && vals.size() > 0)
        {
            Enumeration _enum = vals.elements();
            Element child = null;


            while (_enum.hasMoreElements())
            {
                String name = (String) _enum.nextElement();

                child = document.createElement(nodeName);

                Node pcdata = document.createTextNode(name);

                child.appendChild(pcdata);
                root0.appendChild(child);
            }
        }
    }
}
