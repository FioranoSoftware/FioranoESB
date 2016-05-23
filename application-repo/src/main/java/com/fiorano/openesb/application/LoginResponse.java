/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.util.Enumeration;
import java.util.Vector;

public class LoginResponse extends DmiObject
{
    Vector          m_vecPermissions;
    String          m_strHandleID;
    boolean         m_bSecurityEnabled;
    private boolean m_bIsActive;

    /**
     *  Constructor for the LoginResponse object
     */
    public LoginResponse()
    {
        m_vecPermissions = new Vector();
    }

    /**
     * Returns is active for object
     *
     * @return
     */
    public boolean getIsActive()
    {
        return m_bIsActive;
    }

    /**
     *  Gets the securityEnabled attribute of the LoginResponse object
     *
     * @return
     */
    public boolean isSecurityEnabled()
    {
        return m_bSecurityEnabled;
    }

    /**
     *  Gets the permissions of the LoginResponse
     *
     * @return The permissions value
     */
    public Enumeration getPermissions()
    {
        return m_vecPermissions.elements();
    }

    /**
     *  Gets the handleID of the LoginResponse
     *
     * @return The handleID value
     */
    public String getHandleID()
    {
        return m_strHandleID;
    }

    /**
     *  Gets the objectID attribute of the LoginResponse object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.LOGIN_RESPONSE;
    }

    /**
     * Sets is active for object
     *
     * @param isActive
     */
    public void setIsActive(boolean isActive)
    {
        m_bIsActive = isActive;
    }

    /**
     *  Sets the securityEnabled attribute of the LoginResponse object
     *
     * @param securityEnabled The new securityEnabled value
     */
    public void setSecurityEnabled(boolean securityEnabled)
    {
        m_bSecurityEnabled = securityEnabled;
    }

    /**
     *  Sets the handleID of the LoginResponse
     *
     * @param handleID The new handleID value
     */
    public void setHandleID(String handleID)
    {
        m_strHandleID = handleID;
    }

    /**
     *  Adds a feature to the Permission attribute of the LoginResponse object
     *
     * @param permission The feature to be added to the Permission attribute
     */
    public void addPermission(String permission)
    {
        m_vecPermissions.add(permission);
    }

    /**
     *  This method resets the values of the data members of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
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
     *  This method returns this <code>LoginResponse</code> object
     *  from the specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception java.io.IOException

     * @since Tifosi2.0
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);
        m_strHandleID = readUTF(is);

        int size = is.readInt();

        for (int i = 0; i < size; i++)
        {
            m_vecPermissions.add(readUTF(is));
        }
        m_bSecurityEnabled = is.readBoolean();
        m_bIsActive = is.readBoolean();
    }

    /**
     *  This method writes this <code>LoginResponse</code> object
     *  to the specified output stream object.
     *
     * @param os
     * @param versionNo
     * @exception java.io.IOException

     * @since Tifosi2.0
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);

        writeUTF(os, m_strHandleID);

        int size = m_vecPermissions.size();

        os.writeInt(size);
        for (int i = 0; i < size; i++)
        {
            String permission = (String) m_vecPermissions.get(i);

            writeUTF(os, permission);
        }
        os.writeBoolean(m_bSecurityEnabled);
        os.writeBoolean(m_bIsActive);
    }

    /**
     * This method returns the String representation of this
     * <code>LoginResponse</code> object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Login Response Details ");
        strBuf.append("[");
        strBuf.append("Security Enabled = ");
        strBuf.append(m_bSecurityEnabled);
        strBuf.append(", ");
        strBuf.append("handle ID = ");
        strBuf.append(m_strHandleID);
        strBuf.append(", ");
        if (m_vecPermissions != null)
        {
            strBuf.append("Permissions = ");
            for (int i = 0; i < m_vecPermissions.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append((String) m_vecPermissions.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }
}
