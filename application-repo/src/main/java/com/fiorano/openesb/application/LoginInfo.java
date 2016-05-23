/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class LoginInfo extends DmiObject
{
    private String  m_strUserName;
    private String  m_strPasswd;

    /**
     *  Constructor for the LoginInfo object
     */
    public LoginInfo()
    {
    }

    /**
     *  Gets the objectID attribute of the LoginInfo object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.LOGIN_INFO;
    }

    /**
     *  Gets the userName attribute of the LoginInfo object
     *
     * @return The userName value
     */
    public String getUserName()
    {
        return m_strUserName;
    }

    /**
     *  Gets the password attribute of the LoginInfo object
     *
     * @return The password value
     */
    public String getPassword()
    {
        return m_strPasswd;
    }

    /**
     *  Sets the userName attribute of the LoginInfo object
     *
     * @param userName The new userName value
     */
    public void setUserName(String userName)
    {
        m_strUserName = userName;
    }

    /**
     *  Sets the password attribute of the LoginInfo object
     *
     * @param passwd The new password value
     */
    public void setPassword(String passwd)
    {
        m_strPasswd = passwd;
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

    /**
     *  Description of the Method
     *
     * @param os Description of the Parameter
     * @param versionNo
     * @exception java.io.IOException Description of the Exception
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);
        writeUTF(os, m_strUserName);
        writeUTF(os, m_strPasswd);
    }

    /**
     *  Description of the Method
     *
     * @param is Description of the Parameter
     * @param versionNo
     * @exception java.io.IOException Description of the Exception
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);
        m_strUserName = readUTF(is);
        m_strPasswd = readUTF(is);
    }

}
