/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.security;

import java.io.Serializable;

public class ConnectionHandle implements Serializable
{
    private String  m_strConnectionID;
    private String  m_strUserName;
    private String password;
    private String m_strAgent;
    private String m_strIP;
    public ConnectionHandle(String connectionID, String username, String password, String agent, String clientIP)
    {
        m_strConnectionID = connectionID;
        m_strUserName = username;
        this.password = password;
        m_strAgent = agent;
        m_strIP = clientIP;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName()
    {
        return m_strUserName;
    }

    public String getAgent()
    {
        return m_strAgent;
    }

    public String getClientIP() {
        return m_strIP;
    }

    public void setClientIP(String m_strIP) {
        this.m_strIP = m_strIP;
    }

    public void setUserName(String userName)
    {
        m_strUserName = userName;
    }

    public void setAgent(String agent)
    {
        m_strAgent = agent;
    }

    public String getConnectionId(){
        return m_strConnectionID;
    }
}
