/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;

public class ApplicationInfo extends DmiObject
{
    private ApplicationStateDetails m_appStateDetails;

    private String  m_userName = null;
    private String  m_passwd = null;
    private String  m_appVersion = null;


    /**
     *  Gets the objectID attribute of the ApplicationInfo object
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APP_STATE_INFO;
    }


    /**
     *  Gets the appStateDetails attribute of the ApplicationInfo object
     *
     * @return The appStateDetails value
     */
    public ApplicationStateDetails getAppStateDetails()
    {
        return m_appStateDetails;
    }


    /**
     *  Gets the userName attribute of the ApplicationInfo object
     *
     * @return The userName value
     */
    public String getUserName()
    {
        return m_userName;
    }


    /**
     *  Gets the password attribute of the ApplicationInfo object
     *
     * @return The password value
     */
    public String getPassword()
    {
        return m_passwd;
    }


    /**
     *  Gets the appVersion attribute of the ApplicationInfo object
     *
     * @return The appVersion value
     */
    public String getAppVersion()
    {
        return m_appVersion;
    }


    /**
     *  Sets the appStateDetails attribute of the ApplicationInfo object
     *
     * @param appStateDetails The new appStateDetails value
     */
    public void setAppStateDetails(ApplicationStateDetails appStateDetails)
    {
        m_appStateDetails = appStateDetails;
    }


    /**
     *  Sets the userName attribute of the ApplicationInfo object
     *
     * @param userName The new userName value
     */
    public void setUserName(String userName)
    {
        m_userName = userName;
    }


    /**
     *  Sets the password attribute of the ApplicationInfo object
     *
     * @param passwd The new password value
     */
    public void setPassword(String passwd)
    {
        m_passwd = passwd;
    }


    /**
     *  Sets the appVersion attribute of the ApplicationInfo object
     *
     * @param appVersion The new appVersion value
     */
    public void setAppVersion(String appVersion)
    {
        m_appVersion = appVersion;
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
     * @exception FioranoException Description of the Exception
     */
    public void validate()
        throws FioranoException
    {
    }

}
