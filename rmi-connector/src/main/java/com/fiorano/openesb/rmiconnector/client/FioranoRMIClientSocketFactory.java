/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FioranoRMIClientSocketFactory implements Serializable, RMIClientSocketFactory {

    public final String socketFactoryID ="FioranoRMIClientDef";
    private static final long serialVersionUID = 4224669557041617665L;

    public boolean equals(Object obj) {

        boolean ret=false;

        if (this == obj) ret= true;

        else if (obj == null) ret= false;

        else if (getClass() == obj.getClass()) ret = true;

        return ret;
    }

    public int hashCode() {      //keeping hashcode inline with .equals as per javadoc.
        return socketFactoryID.hashCode();
    }

    /**
     * creates a socket
     * @param hostString comma separated values of hostnames of server machine. [Example: '192.168.1.213,10.0.0.213']
     * @param port port to connect to
     * @return Socket
     * @throws IOException on failure to connect to server on any one hostname
     */
    public Socket createSocket(String hostString, int port) throws IOException {

            String val = System.getProperty("FIORANO_RMI_CALL_TIMEOUT");
            int timeout=120000;//default timeout.
            if(val != null )
                timeout= Integer.parseInt(val);
            String hostNameToConnect =  getHostToConnect(hostString,port);
            Socket s = RMISocketFactory.getDefaultSocketFactory().createSocket(hostNameToConnect,port);
            s.setSoTimeout(timeout);
            return s;
    }

    private String getHostToConnect(String hostString,int port) throws IOException{
        //TODO : make this part as common code for every fiorano rmi client socket factory.
          final String[] hosts = hostString.split(",");
            final int nhosts = hosts.length;
            if (nhosts < 2)
                return hostString;

        List<IOException> exceptions = new ArrayList<IOException>();

        String host = null;
        Selector selector = null;
        SocketChannel connectedChannel = null;
        Socket connectedSocket = null;
        try {
            selector = Selector.open();
            for (String hostName : hosts) {
                SocketChannel channel = SocketChannel.open();
                try {
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_CONNECT);
                } catch (IOException e) {
                    try {
                        channel.close();
                    } catch (IOException ignore) {
                        //did our best to close.
                    }
                    throw e;
                }
                SocketAddress addr = new InetSocketAddress(hostName, port);
                channel.connect(addr);
            }

            boolean found = false;
            while (!found) {
                if (selector.keys().isEmpty()) {
                    throw new IOException("Connection failed for " + hostString +
                            ": " + exceptions);
                }
                selector.select(5000);//adding time out for selection
                Set<SelectionKey> keys = selector.selectedKeys();
                if (keys.isEmpty()) {
                    throw new IOException("Selection keys unexpectedly empty for " +
                            hostString + "[exceptions: " + exceptions + "]");
                }
                for (SelectionKey key : keys) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    key.cancel();
                    try {
                        channel.configureBlocking(true);
                        channel.finishConnect();
                        connectedChannel = channel;
                        found = true;
                        break;
                    } catch (IOException e) {
                        exceptions.add(e);
                    }
                }
            }

            if (connectedChannel != null) {
                connectedSocket = connectedChannel.socket();
                host = connectedSocket.getInetAddress().getHostAddress();
            }
        } finally {
            if (selector != null) {
                // Close the channels that didn't connect
                for (SelectionKey key : selector.keys()) {
                    Channel channel = key.channel();
                    if (channel != connectedChannel) {
                        Socket socket = ((SocketChannel) channel).socket();
                        try {
                            if (socket != null)
                                socket.close();
                        } catch (IOException e) {
                            //Ignore
                        }

                        try {
                            if (channel != null)
                                channel.close();
                        } catch (IOException e) {
                            //Ignore
                        }

                        key.cancel();
                    }
                }

                try {
                    if (connectedSocket != null)
                        connectedSocket.close();
                } catch (IOException e) {
                    //Ignore
                }

                try {
                    if (connectedChannel != null)
                        connectedChannel.close();
                } catch (IOException e) {
                    //Ignore
                }

                try {
                    selector.close();
                } catch (IOException e) {
                    //Ignore
                }
            }
        }
        // We've determined that we can connect to a machine with this host name. now it we return the hostname
        // and create the neccessary socket(default socket,ssl socket,compressed socket etc...) with it.
        return host;
    }
}
