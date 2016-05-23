/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper implements Runnable
{
    public final static byte CREATE_SOCKET = 0x001;
    public final static byte CLOSE_SOCKET = 0x002;
    public final static byte CREATE_CONNECTION = 0x003;
    public final static byte CLOSE_CONNECTION = 0x004;

    // host used to create a socket
    private String  m_strHost;

    // Port used to create a socket
    private int     m_nPort;

    // socket instance
    private Socket m_socket;

    // SQL Connection. Used for JDBC connections
    private Connection m_connection;

    // database URL used to connect to database
    private String  m_strDbUrl;

    // user name used to connect to database
    private String  m_strUserName;

    // password used to connect to database
    private String  m_strPassword;

    // request type
    private byte    m_reqType;

    // boolean indicating success or failure of the
    // request
    private boolean m_bSuccess = false;

    // exception
    private Exception m_exception = null;

    /**
     * Constructor
     *
     * @param host name of the host
     * @param port value of port used to create a
     * socket connection
     */
    public ConnectionHelper(String host, int port)
    {
        m_strHost = host;
        m_nPort = port;
    }

    /**
     * Constructor.
     *
     * This constructor is used to close a JDBC connection
     *
     * @param connection SQL connection
     */
    public ConnectionHelper(Connection connection)
    {
        m_connection = connection;
    }

    /**
     * Constructor.
     *
     * This constructor is used to close a socket
     *
     * @param socket
     */
    public ConnectionHelper(Socket socket)
    {
        m_socket = socket;
    }

    /**
     * Constructor
     *
     * This constructor is used to create a JDBC connection
     *
     * @param dbURL database URL
     * @param user user name used to connect to db
     * @param passwd passwd used to connect to db
     */
    public ConnectionHelper(String dbURL, String user, String passwd)
    {
        m_strDbUrl = dbURL;
        m_strUserName = user;
        m_strPassword = passwd;
    }

    /**
     * This method tries to create a socket in a thread.
     *
     * @param host host to which the connection is to be made
     * @param port port to connect to
     * @param timeout value after which the socket creation
     *        should timeout
     * @return Socket socket created. Null in case socket
     *                couldn't be created in the time specified
     * @exception Exception
     */
    public static Socket openSocket(String host, int port, int
            timeout)
            throws Exception
    {
        ConnectionHelper helper = new ConnectionHelper(host, port);

        helper.m_reqType = CREATE_SOCKET;

        Thread t = new Thread(helper);

        try {
            t.start();
        } catch (Throwable th) {
            throw new FioranoException(
                    "Socket_open_will_fail_as_unable_to_create_new_threads.Total_number_of_active_threads "+
                    new Integer(Thread.activeCount()),
                    th.getMessage());
        }

        try
        {
            t.join(timeout);
        }
        catch (InterruptedException exc)
        {
            // Can't do anything. So ignore the exception
            // and continue the good work
        }

        if (helper.m_exception != null)
            throw helper.m_exception;

        return helper.m_socket;
    }

    /**
     * This method tries to close a socket in a thread.
     *
     * @param socket socket which is to be closed
     * @param timeout value after which the socket close
     *                should timeout
     * @return boolean indicating whether socket was closed
     *         successfully or not
     * @exception Exception
     */
    public static boolean closeSocket(Socket socket, int timeout)
            throws Exception
    {
        ConnectionHelper helper = new ConnectionHelper(socket);

        helper.m_reqType = CLOSE_SOCKET;

        Thread t = new Thread(helper);

        try {
            t.start();
        } catch (Throwable th) {
            throw new FioranoException(
                    "Socket_open_will_fail_as_unable_to_create_new_threads.Total_number_of_active_threads "+
                    new Integer(Thread.activeCount()),
                    th.getMessage());
        }

        try
        {
            t.join(timeout);
        }
        catch (InterruptedException exc)
        {
            // Can't do anything. So ignore the exception
            // and continue the good work
        }

        if (helper.m_exception != null)
            throw helper.m_exception;

        return helper.m_bSuccess;
    }

    /**
     * This method tries to create a JDBC connection in
     * a thread.
     *
     * @param url database URL
     * @param user user name used used to connect to db
     * @param timeout value after which the connection
     *                creation should timeout
     * @param passwd
     * @return Connection connection created. Null in
     *                    case connection couldn't be
     *                    created in the time specified
     * @exception SQLException
     */
    public static Connection openConnection(String url,
                                            String user,
                                            String passwd,
                                            int timeout)
            throws SQLException, FioranoException
    {
        ConnectionHelper helper = new ConnectionHelper(url, user, passwd);

        helper.m_reqType = CREATE_CONNECTION;

        Thread t = new Thread(helper);

        try {
            t.start();
        } catch (Throwable th) {
            throw new FioranoException(
                    "DBConnection_open_will_fail_as_unable_to_create_new_threads.Total_number_of_active_threads "+
                    new Integer(Thread.activeCount()),
                    th.getMessage());
        }

        try
        {
            t.join(timeout);
        }
        catch (InterruptedException exc)
        {
            // Can't do anything. So ignore the exception
            // and continue the good work
        }

        if (helper.m_exception != null)
            throw (SQLException) helper.m_exception;

        return helper.m_connection;
    }

    /**
     * This method tries to close a JDBC connection
     *
     * @param timeout value after which the connection close
     *                should timeout
     * @param connection
     * @return boolean indicating whether connection close was
     *         successful or not
     * @exception SQLException
     */
    public static boolean closeConnection(Connection connection, int timeout)
            throws SQLException, FioranoException
    {
        ConnectionHelper helper = new ConnectionHelper(connection);

        helper.m_reqType = CLOSE_CONNECTION;

        Thread t = new Thread(helper);

        try {
            t.start();
        } catch (Throwable th) {
            throw new FioranoException(
                    "DBConnection_close_will_fail_as_unable_to_create_new_threads.Total_number_of_active_threads "+
                    new Integer(Thread.activeCount()),
                    th.getMessage());
        }


        try
        {
            t.join(timeout);
        }
        catch (InterruptedException exc)
        {
            // Can't do anything. So ignore the exception
            // and continue the good work
        }

        if (helper.m_exception != null)
            throw (SQLException) helper.m_exception;

        return helper.m_bSuccess;
    }

    /**
     * Executes the request specified by request type
     */
    public void run()
    {
        switch (m_reqType)
        {
            case CREATE_SOCKET:
                createSocket();
                break;
            case CLOSE_SOCKET:
                closeSocket();
                break;
            case CREATE_CONNECTION:
                createConnection();
                break;
            case CLOSE_CONNECTION:
                closeConnection();
                break;
        }
    }

    /**
     * Creates a Java socket
     */
    private void createSocket()
    {
        if (m_strHost == null || m_nPort == -1)
            return;

        try
        {
            m_socket = new Socket(m_strHost, m_nPort);
        }
        catch (IOException e)
        {
            m_exception = e;
        }
    }

    /**
     * Closes a socket
     */
    private void closeSocket()
    {
        if (m_socket == null)
            return;

        try
        {
            m_socket.close();
            m_bSuccess = true;
        }
        catch (IOException sqle)
        {
            m_exception = sqle;
        }
    }

    /**
     * Creates a JDBC connection
     */
    private void createConnection()
    {
        if (m_strDbUrl == null
                || m_strUserName == null
                || m_strPassword == null)
            return;

        boolean isRetry = true;

        while(isRetry)
        {
            try
            {
                m_connection = DriverManager.getConnection(m_strDbUrl, m_strUserName, m_strPassword);
                isRetry = false;
            }
            catch (SQLException e)
            {
                if (e.getMessage().indexOf("12519") != -1)
                {
                    try {
                        /*
                            The oracle error code 12519 comes when there are more number of connect and
                            disconnects in short span of time. This is an expected behavior as per Oracle forums.
                            The "processes" parameter in init.ora set to 40 on the server side, which on Oracle Database
                            XE install allows 19 new connections (taking the backgrounds into account).

                            And this is how it works:

                            PMON ( Oracle's process monitor ) provides this information to the listener as part
                            of it's periodic load updates to the listener, and whenever something changes on the server
                            side, it will keep the listener updated by sending load updates. So, if someone performs 19
                            connect/disconnect in a tight loop, listener will observe the 19 slots used up for the connects,
                            and until the next update from PMON comes, it won't know that the 19 servers have exited (only
                            the connects go through the listener, the disconnects don't). The update should come right
                            away, but obviously there is a window of time when listener thinks all slots are currently occupied,
                            and hence, it will refuse connections for that interval.
                            This is only a short race-condition, which is un-avoidable in Oracle DB.
                         */
                        Thread.sleep(500);
                    }
                    catch(InterruptedException e1){
                        e1.printStackTrace();
                        m_exception = e1;
                    }
                    isRetry = true;
                }
                else {
                    m_exception = e;
                    isRetry = false;
                }
            }
        }
    }

    /**
     * Closes a jdbc connection
     */
    private void closeConnection()
    {
        if (m_connection == null)
            return;

        try
        {
            m_connection.close();
            m_bSuccess = true;
        }
        catch (SQLException sqle)
        {
            m_exception = sqle;
        }
    }
}

