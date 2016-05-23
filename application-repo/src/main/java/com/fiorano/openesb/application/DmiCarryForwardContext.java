/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DmiCarryForwardContext extends DmiObject implements Serializable
{
    // vector of SourceContext
    Vector          m_vecOfContextsCarriedFwd;

    Hashtable       m_hashCarryFwdProps;

    // ApplicationContext
    private String  m_appContext;

    /**
     *  Constructor for the DmiCarryForwardContext object
     *
     * @since Tifosi2.0
     */
    public DmiCarryForwardContext()
    {
        m_vecOfContextsCarriedFwd = new Vector();
        m_hashCarryFwdProps = new Hashtable();
    }


    /**
     * @return The Application Context for this DataPacket
     * @since Tifosi2.0
     */
    public String getAppContext()
    {
        return m_appContext;
    }

    /**
     *  This method returns an enumeration containing all the objects of <code>DmiSourceContext</code>
     *  contained in this <code>DmiCarryForwardContext</code> object.
     *
     * @return Enumeration containing DmiSourceContext objects
     * @see #addContext(DmiSourceContext)
     * @since Tifosi2.0
     */
    public Enumeration getContexts()
    {
        return m_vecOfContextsCarriedFwd.elements();
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.CARRY_FORWARD_CONTEXT;
    }

    /**
     *  Gets the carryFwdProps attribute of the DmiCarryForwardContext object
     *
     * @return The carryFwdProps value
     */
    public Hashtable getCarryFwdProps()
    {
        return m_hashCarryFwdProps;
    }

    /**
     *  Gets the carryFwdProperty attribute of the DmiCarryForwardContext object
     *
     * @param name Description of the Parameter
     * @return The carryFwdProperty value
     */
    public String getCarryFwdProperty(String name)
    {
        return (String) m_hashCarryFwdProps.get(name);
    }


    /**
     *  This method sets the specified string as ApplicationContext
     *
     * @param appContext ApplicationContext
     * @since Tifosi2.0
     */
    public void setAppContext(String appContext)
    {
        m_appContext = appContext;
    }


    /**
     *  Sets the carryFwdProps attribute of the DmiCarryForwardContext object
     *
     * @param properties The new carryFwdProps value
     */
    public void setCarryFwdProps(Hashtable properties)
    {
        m_hashCarryFwdProps = properties;
    }

    /**
     *  Sets the carryFwdProperty attribute of the DmiCarryForwardContext object
     *
     * @param name The new carryFwdProperty value
     * @param value The new carryFwdProperty value
     */
    public void setCarryFwdProperty(String name, String value)
    {
        m_hashCarryFwdProps.put(name, value);
    }


    /**
     *  This method adds the specified <code>DmiSourceContext</code> object to
     *  this <code>DmiCarryForwardContext</code> object.
     *
     * @param context The feature to be added to the Context attribute
     * @see #getContexts()
     * @since Tifosi2.0
     */
    public void addContext(DmiSourceContext context)
    {
        if (!m_vecOfContextsCarriedFwd.contains(context))
            m_vecOfContextsCarriedFwd.add(context);
    }


    /**
     *  This method resets the source context information.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_vecOfContextsCarriedFwd.clear();
        m_appContext = null;
    }


    /**
     *  This method tests whether this <code>DmiCarryForwardContext</code>
     *  object has the required(mandatory) fields set. This method should be
     *  invoked before inserting values in the database.
     *
     * @exception FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void fromStream(java.io.DataInput is, int clientVersionNo)
        throws java.io.IOException
    {
        super.fromStream(is, clientVersionNo);

        m_appContext = readUTF(is);

        int size = is.readInt();

        for (int i = 0; i < size; i++)
        {
            DmiSourceContext context = new DmiSourceContext();

            context.fromStream(is, clientVersionNo);
            m_vecOfContextsCarriedFwd.add(context);
        }

        if (clientVersionNo <= 3000)
            return;

        int count = is.readInt();

        for (int i = 0; i < count; i++)
        {
            String key = readUTF(is);
            String value = readUTF(is);

            m_hashCarryFwdProps.put(key, value);
        }
    }

    /**
     *  Description of the Method
     *
     * @param os Description of the Parameter
     * @param clientVersionNo Description of the Parameter
     * @exception java.io.IOException Description of the Exception
     */
    public void toStream(java.io.DataOutput os, int clientVersionNo)
        throws java.io.IOException
    {
        super.toStream(os, clientVersionNo);

        writeUTF(os, m_appContext);

        int size = m_vecOfContextsCarriedFwd.size();

        os.writeInt(size);

        Enumeration enu = m_vecOfContextsCarriedFwd.elements();

        while (enu.hasMoreElements())
        {
            DmiSourceContext context = (DmiSourceContext) enu.nextElement();

            context.toStream(os, clientVersionNo);
        }

        if (clientVersionNo <= 3000)
            return;

        os.writeInt(m_hashCarryFwdProps.size());

        Enumeration keys = m_hashCarryFwdProps.keys();

        while (keys.hasMoreElements())
        {
            String key = (String) keys.nextElement();
            String value = (String) m_hashCarryFwdProps.get(key);

            writeUTF(os, key);
            writeUTF(os, value);
        }
    }


    /**
     *  This method returns the String representation of this <code>DmiCarryForwardContext</code>
     *  object.
     *
     * @return The String representation of this object
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("dmi carry forward context Details ");
        strBuf.append("[");
        strBuf.append("Application Context : ");
        strBuf.append(m_appContext);
        strBuf.append(", ");
        if (m_vecOfContextsCarriedFwd != null)
        {
            strBuf.append("Context carried forward = ");
            for (int i = 0; i < m_vecOfContextsCarriedFwd.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(((DmiSourceContext) m_vecOfContextsCarriedFwd.elementAt(i)).toString());
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }

}
