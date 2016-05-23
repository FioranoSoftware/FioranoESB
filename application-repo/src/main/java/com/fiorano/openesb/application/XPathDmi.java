/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class XPathDmi extends DmiObject
{
   // name space associated with this xpath
    private HashMap m_nameSpace;
    // Value of the xpath.
    private String  m_strXPath;

    /**
     *  Constructor for the XPathDmi object
     */
    public XPathDmi()
    {
    }


    /**
     *  Constructor for the XPathDmi object
     *
     * @param nameSpace Description of the Parameter
     * @param xPath Description of the Parameter
     */
    public XPathDmi(String xPath, HashMap nameSpace)
    {
        m_nameSpace = nameSpace;
        m_strXPath = xPath;
    }

    /**
     *  Gets the objectID attribute of the XPathDmi object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.XPATH_DMI;
    }


    /**
     *  Gets the nameSpace attribute of the XPathDmi object
     *
     * @return The nameSpace value
     */
    public HashMap getNameSpace()
    {
        return m_nameSpace;
    }


    /**
     *  Gets the xPath attribute of the XPathDmi object
     *
     * @return The xPath value
     */
    public String getXPath()
    {
        return m_strXPath;
    }


    /**
     *  Description of the Method
     *
     * @param out Description of the Parameter
     * @param versionNo Description of the Parameter
     * @exception IOException Description of the Exception
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        if (m_strXPath == null)
            m_strXPath = "";

        out.writeUTF(m_strXPath);

        if (m_nameSpace != null)
        {
            out.writeInt(m_nameSpace.size());

            Set keys = m_nameSpace.keySet();
            Iterator iter = keys.iterator();

            while (iter.hasNext())
            {
                String key = (String) iter.next();
                String value = (String) m_nameSpace.get(key);

                out.writeUTF(key);
                out.writeUTF(value);
            }
        }
        else
            out.writeInt(0);

    }


    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param versionNo Description of the Parameter
     * @exception IOException Description of the Exception
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        m_strXPath = is.readUTF();

        int size = is.readInt();

        m_nameSpace = new HashMap();

        while (size > 0)
        {
            String key = is.readUTF();
            String value = is.readUTF();

            m_nameSpace.put(key, value);
            size--;
        }
    }


    /**
     *  Description of the Method
     *
     * @exception FioranoException Description of the Exception
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  Description of the Method
     */
    public void reset()
    {
    }
}
