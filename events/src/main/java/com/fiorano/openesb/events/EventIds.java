/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.events;

public class EventIds
{


    /**
     *  SERVICE EVENT : Failed to launch service.
     */
    public final static int SERVICE_FAILED_TO_LAUNCH = 100;

    /**
     *  SERVICE EVENT : Failed to kill service.
     */
    public final static int SERVICE_FAILED_TO_KILL = 101;

    /**
     * @deprecated  Not used any more
     */
    public final static int SERVICE_CURRENTLY_BEING_FETCHED = 102;

    /**
     * @deprecated  Not used any more
     */
    public final static int SERVICE_SYNCHRONIZED = 103;

    /**
     * @deprecated  Not used any more
     */
    public final static int SERVICE_FAILED_TO_SYNCHRONIZE = 104;

    /**
     *  SERVICE EVENT : Service Killed on FPS shutdown
     */
    public final static int SERVICE_KILLED_ON_TPS_SHUTDOWN = 105;

    /**
     *  SERVICE EVENT : Service Handle cretated for service
     */
    public final static int SERVICE_HANDLE_CREATED = 106;

    /**
     *  SERVICE EVENT : Service Handle bound to peer
     */
    public final static int SERVICE_HANDLE_BOUND = 107;

    /**
     * SERVICE EVENT : Service Handle bounding to peer
     */
    public final static int SERVICE_HANDLE_BOUNDING = 112;

    /**
     *  SERVICE EVENT : Service Handle unbound from peer
     */
    public final static int SERVICE_HANDLE_UNBOUND = 108;

    /**
     *  SERVICE EVENT : Service Handle destroyed for service
     */
    public final static int SERVICE_HANDLE_DESTROYED = 109;


    /**
     *  @deprecated  Not used any more
     */
    public final static int LAUNCH_CALL_IN_PROGRESS = 110;

    //  pubsub route data
    /**
     *  @deprecated  Not used any more
     */
    public final static int PUBSUB_ROUTE_DATA = 111;

    /**
     *  APPLICATION EVENT : Application Launhced
     */
    public final static int APPLICATION_LAUNCHED = 201;

    /**
     *  APPLICATION EVENT : Application Killed
     */
    public final static int APPLICATION_KILLED = 202;

    /**
     *  APPLICATION EVENT : Application Synchronized
     */

    public final static int APPLICATION_SYNCHRONIZED = 203;

    /**
     *  APPLICATION EVENT : Application Launch Started
     */
    public final static int APPLICATION_LAUNCH_STARTED = 204;

    /**
     * @deprecated  Not used any more
     */
    public final static int APPLICATION_FAILED_TO_PREPARE_FOR_LAUNCH = 205;

    /**
     * @deprecated  Not used any more
     */
    public final static int APPLICATION_FAILED_TO_SYNCHRONIZE = 206;

    /**
     * @deprecated  Not used any more
     */
    public final static int SP_SERVICE_HANDLE_CREATED = 207;

    /**
     *  APPLICATION EVENT : Application created or saved.
     */
    public final static int APPLICATION_CREATED = 208;

    /**
     *  APPLICATION EVENT : Application repository updated.
     */
    public static final int APPLICATION_REPOSITORY_UPDATION = 209;

    /**
     *  APPLICATION EVENT : Application deleted.
     */
    public static final int APPLICATION_DELETED = 210;

    /**
     *  APPLICATION EVENT : Application deleted.
     */
    public static final int APPLICATION_SYNCHRONIZE_STARTED = 211;

    /**
     *  ROUTE EVENT : Debugger set on route.
     */
    public static final int ROUTE_DEBUGGER_SET = 212;

    /**
     *  ROUTE EVENT : Debugger removed on route.
     */
    public static final int ROUTE_DEBUGGER_REMOVED = 213;

    /**
     *  APPLICATION EVENT : Application state restored on FES restart.
     */
    public static final int APPLICATION_STATE_RESTORED = 214;



    /**
     *  TPS Event : Peer server launched.
     */
    public final static int TIFOSI_SERVER_LAUNCHED = 301;

    /**
     *  TPS Event : Peer server shutdown.
     */
    public final static int TIFOSI_SERVER_SHUTDOWN = 302;

    /**
     *  TPS Event : Peer server application state info published.
     */
    public final static int TIFOSI_SERVER_APPLICATION_INFO = 303;

    /**
     * @deprecated  Not used any more
     */
    public final static int TIFOSI_SERVER_PING = 304;


    //  event for tps unavailable (cable problem ?)
    /**
     *  TPS EVENT : Peer server unavailable in network.
     */
    public final static int TIFOSI_SERVER_UNAVAILABLE_EVENT = 305;

    //  event for tsp available (cable problem solved)
    /**
     *  TPS EVENT : Peer server available in network.
     */
    public final static int TIFOSI_SERVER_AVAILABLE_EVENT = 306;

    /**
     * TPS EVENT : Event for new FPS being configured.
     */
    public final static int NEW_TIFOSI_SERVER_CONFIGURED_EVENT = 307;

    /**
     * Event for P2P connection getting broken between two peer servers.
     * @deprecated  Not used any more
     */
    public final static int P2P_CONNECTION_BROKEN_EVENT = 308;

    /**
     * Event for P2P connection getting established between two peer servers.
     * @deprecated  Not used any more
     */
    public final static int P2P_CONNECTION_ESTABLISHED_EVENT = 309;

    /**
     * TPS Event: HA Event for FPS State changes
     */
    public final static int TIFOSI_HA_STATE_CHANGE_EVENT = 310;

    /**
     *  SP EVENT : FES server started.
     */
    public final static int SERVICE_PROVIDER_STARTED = 401;

    /**
     *  SP EVENT : FES server shutdown.
     */
    public final static int SERVICE_PROVIDER_SHUTDOWN = 402;

    /**
     *  @deprecated  Not used any more
     */
    public final static int MESSAGE_BUS_CONNECTION_FAILURE = 403;

    /**
     *  @deprecated  Not used any more
     */
    public final static int RECONNECTED_TO_MESSAGE_BUS = 404;

    /**
     *  @deprecated  Not used any more
     */
    public final static int JMS_STATE_CHANGED = 405;




    //SBW SPECIFIC EVENT IDs
    /**
     *  SBW EVENT : Out port state traversed.
     */
    public final static int OUT_PORT_STATE_TRAVERSED = 1000;
    /**
     *  SBW EVENT : In port state traversed.
     */
    public final static int IN_PORT_STATE_TRAVERSED = 1001;
    /**
     * @deprecated  Not used any more
     */
    public final static int SERVICE_STATE_TRAVERSED = 1002;

    /**
     * @deprecated  Not used any more
     */
    public final static int SERVICE_TRACE_MESSAGE = 1003;




    //SECURITY SPECIFIC EVENT IDs

    /**
     *  SECURITY EVENT : FES Login Request.
     */
    public final static int SP_LOGIN_REQUEST = 1500;

    /**
     *  SECURITY EVENT : Generated on security voilaion for application launch request.
     */
    public final static int APPLICATION_LAUNCH_REQUEST = 1501;

    /**
     *  SECURITY EVENT : Generated on security voilation for application kill request.
     */
    public final static int APPLICATION_KILL_REQUEST = 1502;

    /**
     *  SECURITY EVENT : Generated on security voilation for service launch request.
     */
    public final static int SERVICE_LAUNCH_REQUEST = 1503;

    /**
     *  SECURITY EVENT : Generated on security voilation for service kill request.
     */
    public final static int SERVICE_KILL_REQUEST = 1504;

    /**
     *  SECURITY EVENT : Login Event for FPS.
     */
    public final static int TPS_LOGIN_REQUEST = 1505;

    /**
     *  @deprecated  Not used any more
     */
    public final static int APPLICATION_SYNCHRONIZE_REQUEST = 1506;

    /**
     *  SECURITY EVENT : Generated on security voilation for application prepare launch request.
     */
    public final static int APPLICATION_PREPARE_LAUNCH_REQUEST = 1507;

    /**
     *  SECURITY EVENT : Generated on security voilation for service deployment request.
     */
    public final static int SERVICE_DEPLOYMENT_REQUEST = 1508;

    /**
     * @deprecated  Not used any more
     */
    public final static int CREATE_ACL_REQUEST = 1509;

    /**
     *  SECURITY EVENT : Generated on secuirty voilation for configure peer server request.
     */
    public final static int CONFIGURE_TPS_REQUEST = 1510;

    /**
     *  SECURITY EVENT : Generated on security voilation for edit application request.
     */
    public final static int APPLICATION_EDIT_REQUEST = 1511;

    /**
     *  SECURITY EVENT : Generated on secuirty voilation for view application request.
     */
    public final static int APPLICATION_VIEW_REQUEST = 1516;

    /**
     * @deprecated  Not used any more
     */
    public final static int CREATE_NEW_APPLICATION_REQUEST = 1512;

    /**
     * @deprecated  Not used any more
     */
    public final static int APPLICATION_SAVED = 1513;

    /**
     * @deprecated  Not used any more
     */
    public final static int APPLICATION_RENAMED = 1514;

    /**
     *  SECURITY EVENT : Generated on security voilation for creating ACL request.
     */
    public final static int SET_ACL_REQUEST = 1515;

    /**
     *  SECURITY EVENT : Generated on password change success.
     */
    public final static int CHANGE_PASSWORD_SUCCESS = 1516;

    // Event for FPS (P2P Store-n-forward ) Purpose only
    /**
     * @deprecated  Not used any more
     */
    public final static int PERSISTENT_SERVICES_LAUNCHED_ON_TPS = 1600;

    /**
     * @deprecated  Not used any more
     */
    public final static int DISK_USAGE = 1601;

    /**
     * @deprecated  Not used any more
     */
    public final static int DB_CONNECTION = 1602;


    // HA Events.

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_ACTIVE = 2001;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_WAITING = 2002;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_STANDALONE = 2003;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_PASSIVE = 2004;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_ACTIVE_SYNC = 2005;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_SYNC_STATUS = 2006;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_PASSIVE_SYNC = 2007;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_HA_PEER_CONNECTED = 2008;

    /**
     * HA EVENT
     */
    public static final int TIFOSI_SERVER_RECONNECT_EVENT = 2009;
}
