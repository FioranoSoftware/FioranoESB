/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

public interface MetaDataConstants {
    //Name of the ConnectURL field in the ConnectionFactory Metadata
    final static String CONNECT_URL = "ConnectURL";

    //Name of the BackURLs filed in the  ConnectionFactory Metadata
    final static String BACKUP_URLS = "BackupConnectURLs";

    //Name of the ConnectionCLientID field in the ConnectionFactory Metadata
    final static String CONNECTION_CLIENTID = "ConnectionClientID";

    //Name of the Ping Disabled field in the ConnectionFactory Metadata
    final static String PING_DISABLED = "DisablePing";

    //Name of the Allow Durable Connections field in the ConnectionFactory Metadata
    final static String ALLOW_DURABLE_CONNECTIONS = "AllowDurableConnections";

    // Added new flag ENABLE_AUTO_REVALIDATION, whenever this flag is true in
    // server.cfg, It provides revalidation without CSP.
    //Name of the Enable Auto Revalidation field in the ConnectionFactory Metadata
    final static String ENABLE_AUTO_REVALIDATION = "EnableAutoRevalidation";

    // Added new flag ENABLE_MODIFY_PRINCIPAL_STORE_FROM_RTL, whenever this flag is disabled in
    // server.cfg, modification of principal store will not be allowed.
    // Name of the Enable Modify Principal Store field in the ConnectionFactory Metadata
    final static String ENABLE_MODIFY_PRINCIPAL_STORE_FROM_RTL = "ENABLE_MODIFY_PRINCIPAL_STORE_FROM_RTL";

    //Name of the Durable Connection Base Dir in the ConnectionFactory Metadata
    final static String DURABLE_CONNECTION_BASE_DIR =
            "DurableConnectionsBaseDir";

    //  Root Directory path for the client to store CSP cached messags.
    // Same as DURABLE_CONNECTION_BASE_DIR
    //  <maintaining for backward compatibility>
    public final static String CSP_BASE_DIR = "CSP_BASE_DIR";

    //Name of the Compression Manager field in the ConnectionFactory Metadata
    final static String COMPRESSION_MANAGER = "CompressionManager";

    //Name of the SyncFrequency field in the ConnectionFactory Metadata
    final static String UPDATE_FREQUENCY = "UpdateFrequency";

    //Name of the Transport Protocol field in the ConnectionFactory Metadata
    final static String TRANSPORT_PROTOCOL = "TransportProtocol";

    //Name of the Security Protocol field in the ConnectionFactory Metadata
    final static String SECURITY_PROTOCOL = "java.naming.security.protocol";

    //Name of the Security Manager field in the ConnectionFactory Metadata
    final static String SSL_SECURITY_MANAGER = "SecurityManager";

    // Name of the Http Proxy URL field in the ConnectionFactory Metadata
    final static String HTTP_PROXY_URL = "HTTPProxyURL";

    // Name of the SOCKS Proxy URL field in the ConnectionFactory Metadata
    final static String SOCKS_PROXY_URL = "SocksProxyURL";

    // Name of the PROXY REALM field in the ConnectionFactory Metadata
    final static String PROXY_AUTHENTICATION_REALM = "ProxyAuthenticationRealm";

    // Name of the PROXY PRINCIPAL field in the ConnectionFactory Metadata
    final static String PROXY_PRINCIPAL = "ProxyPrincipal";

    // Name of the PROXY PASSWORD field in the ConnectionFactory Metadata
    final static String PROXY_CREDENTIALS = "ProxyCredentials";

    // Name of the PROXY TYPE field in the ConnectionFactory Metadata
    final static String HTTP_PROXY_TYPE = "ProxyType";

    //Name of the ClientProxy URL field in the ConnectionFactory Metadata
    final static String CLIENT_PROXY_URL = "ClientProxyURL";

    //Name of the ServerProxy URL field in the ConnectionFactory Metadata
    final static String SERVER_PROXY_URL = "ServerProxyURL";
    final static String RESUME_TIMEOUT_INTERVAL = "ResumeTimeoutInterval";

    //Name of the CBR enabled field in the ConnectionFactory Metadata
    final static String USE_FIORANO_CBR = "UseFioranoCbr";

    //  Name of the Max number of reconnect attempts field that will be made by
    //  CSP's reconnect thread
    final static String MAX_RECONNECT_ATTEMPTS =
            "MaxDurableConnectionReconnectAttempts";

    //  Name of the CSP reconnect interval field in the ConnectionFactory Metadata
    final static String CSP_RECONNECT_INTERVAL =
            "DurableConnectionReconnectInterval";

    final static String ASYNC_SEND_BATCH_BUFFER_SIZE = "AsyncSendBatchBufferSize";

    final static String ASYNC_SEND_COMPLETION_WAIT_TIMEOUT = "AsyncSendCompletionWaitTimeout";

    final static String PUBLISHER_BEHAVIOUR_ON_ASYNC_SEND_BUFFER_OVERFLOW="PublisherBehaviourOnBufferOverflow";

    // Name of flag that specifies whether thread context class loader is required
    // to be used while ObjectMessage loading.
    final static String USE_THREAD_CONTEXT_CLASS_LOADER =
            "isThreadContextClassLoaderUsed";

    //  Name of the field to prevent sending of previously stored messages in CSP.
    final static String DONT_SEND_PREVIOUSLY_STORED_MESSAGES =
            "DisableSendingCSPStoredMessages";

    // Name of the Socket Timeout value field
    final static String SOCKET_TIMEOUT = "SocketTimeout";

    // Name of the Prepare call Timeout field
    final static String XA_SOCKET_TIMEOUT = "XASocketTimeout";

    // Name of the Max Socket creation tries field
    final static String MAX_SOCKET_CREATION_TRIES = "MaxSocketCreationTries";

    // Name of the sleep socket creation field
    final static String SLEEP_SOCKET_CREATION_TRIES =
            "SleepBetweenSocketCreationTries";

    //Name of the create local socket field
    final static String CREATE_LOCAL_SOCKET = "CreateLocalSocket";

    //Name of the lookup preferred server field
    final static String LOOKUP_PREFERRED_SERVER = "LookupPreferredServer";

    //Name of the Auto dispatch field
    final static String AUTO_DISPATCH = "AutoDispatchConnections";

    // Possible values for the Proxy Type field in the ConnectionFactory Metadata
    public final static String MS_ISA_PROXY = "MS_ISA_PROXY";
    public final static String WIN_PROXY = "WIN_PROXY";
    public final static String NETSCAPE_PROXY = "NETSCAPE_PROXY";
    public final static String WINGATE_PROXY = "WINGATE_PROXY";
    public final static String DEFAULT_PROXY = "DEFAULT_PROXY";

    //Possible values for the Security Protocol field in the ConnectionFactory Metadata
    public final static String PROTOCOL_PHAOS_SSL = "PHAOS_SSL";
    public final static String PROTOCOL_JSSE_SSL = "SUN_SSL";

    // Possible values for the Transport Protocol field in the ConnectionFactory Metadata
    public final static String PROTOCOL_TCP = "TCP";
    public final static String PROTOCOL_HTTP = "HTTP";

    // boolean indicating whether connect URL can be updated
    // or not
    public final static String AUTO_UPDATE = "AutoUpdate";

    // Following parameters are used in setting parameters
    // in ServerMetaData in case of dispatcher
    public final static String SERVER_URL = "SERVER_URL";
    public final static String ADMIN_URL = "ADMIN_URL";
    public final static String MAX_CLIENT_CONNECTIONS =
            "MAX_CLIENT_CONNECTIONS";

    //Size of a batch of NP messages to sent on a topic
    final static String TCP_BATCH_SIZE = "TCPBatchSize";

    // Specifies the timeout interval for which the messages can be kept in the batch.
    final static String BATCH_TIMEOUT_INTERVAL = "BatchTimeoutInterval";

    // delay in publishing new messages when CSP is syncing
    // up
    final static String PUBLISH_WAIT_DURING_CSP_SYNCUP =
            "PublishWaitDuringCSPSyncup";

    final static String PUBLISH_WAIT_DURING_SEND_PENDING_CSP =
            "PublishWaitDuringSendPendingCSP";

    // Name of the TCP Window size
    final static String TCP_WINDOW_SIZE = "TcpWindowSize";

    /**
     * This parameter specifies the maximum number of ping calls that should be sent with in
     * Ping Timeout Interval.
     */
    final static String MAX_PING_CALLS = "MaxPingCalls";

    /**
     * This parameter specifies the maximum Response time each ping call should wait
     * for the response from the server.
     */
    final static String MAX_PING_RESPONSE_TIME = "MaxPingResponseTime";

    /**
     * This parameter is used when durable connections is not enabled, but
     * auto-revalidation is.
     * <p/>
     * In this case when a client attempts to publish a message in its
     * disconnected state, then there are following options for the messaging layer.
     * <p/>
     * a) Throw an exception.
     * b) Block the send call till the message is sent.
     * c) Ignore the message.
     * <p/>
     * The value of this flag is used to choose among the above behaviour.
     * <p/>
     * Possible values are 0 (THROWEXCEPTION) , 1 (IGNORE),2 (BLOCK),
     * <p/>
     * BLOCK is not supported in the present release.
     */
    final static String PUBLISH_BEHAVIOUR_IN_AUTO_REVALIDATION =
            "PublishBehaviourInAutoRevalidation";

    final static String MAX_BLOCK_PUBLISHER_RETRY_ATTEMPTS =
            "MaxBlockPublisherRetryAttempts";

    final static String BLOCK_PUBLISHER_RETRY_INTERVAL =
            "BlockPublisherRetryInterval";
    //Adding for setting timeout value for receive no wait.
    final static String MAX_RECEIVE_TIMEOUT_ON_RECEIVE_NO_WAIT =
            "ReceiveNoWaitTimeout";
    //  Name of the Max number of reconnect attempts field that will be made by
    //  AdminConnection's reconnect thread
    final static String MAX_ADMIN_RECONNECT_ATTEMPTS =
            "MaxAdminConnectionReconnectAttempts";

    //  Name of the AdminConnection reconnect interval
    final static String ADMIN_RECONNECT_INTERVAL =
            "AdminConnectionReconnectInterval";

    // Socket creation timeout value field
    final static String SOCKET_CREATION_TIMEOUT = "SocketCreationTimeout";

    // Drop non-persistent messages
    final static String NEVER_DROP_NP_MESSAGES = "NeverDropNPMessages";

    // Interval after which publisher will retry publishing NP messages
    final static String RETRY_PUBLISH_INTERVAL = "RetryPublishInterval";

    // Flag to enable/disable Shutdown hook
    final static String ADD_SHUTDOWN_HOOK = "AddShutdownHook";

    // Flag to enable/disable LMS
    final static String ENABLE_LMS = "EnableLMS";

    /////////////////////////////////////////////////////////////////
    // Constants for Topic/Queue Meta Data properties
    //
    // Storage URL property.
    final static String STORAGE_URL = "StorageURL";

    // If Topic/Queue is Temporary.
    final static String IS_TEMPORARY = "IsTemporary";

    // If Topic/Queue is Encrypted.
    final static String IS_ENCRYPTED = "IsEncrypted";

    // Encryption algorithm
    final static String ENCRYPTION_ALGO = "EncryptionAlgo";

    // Encryption Key
    final static String ENCRYPTION_KEY = "EncryptionKey";

    // Storage Type
    final static String STORAGE_TYPE = "StorageType";

    // Per-destination replication-enabled flag
    final static String REPLICATION_ENABLED = "ReplicationEnabled";

    // If Topic/Queue is Compressed.
    final static String IS_COMPRESSED = "IsCompressed";

    // Compression Level.
    final static String COMPRESSION_LEVEL = "CompressionLevel";

    // Compression Strategy
    final static String COMPRESSION_STRATEGY = "CompressionStrategy";
    //////////////////////////////////////////////////////////////////
    // Connection Factory MetaData Constant to define if the client will use LPC
    // - Sachin
    final static String IS_FOR_LPC = "IsForLPC";

    // Disable Reader Cache
    final static String DISABLE_READER_CACHE = "DisableReaderCache";

    // Enable Batching
    final static String BATCHING_ENABLED = "BatchingEnabled";

    // Enable/Disable Keep-Alive
    final static String SOCKET_KEEP_ALIVE_ENABLED = "SocketKeepAliveEnabled";

    // Enable/Disable USE_SINGLE_SOCKET_FOR_SEND_RECEIVE
    final static String USE_SINGLE_SOCKET_FOR_SEND_RECEIVE = "UseSingleSocketForSendReceive";

    // Whether State transition should be listened on receive socket or not.
    final static String STATE_TRANSITION_ON_RECEIVE_SOCKET = "StateTransitionOnReceiveSocket";

    //Wait Interval on GC for CSP DB
    final static String CSP_TBL_WAIT_INTERVAL_ON_GC =
            "CSPTableWaitIntervalOnGC";

    //Enable LRU for CSP Cache Table flag
    final static String CSP_TBL_ENABLE_LRU = "CSPLRUEnabledForCacheTable";

    //CSP DB Max Open Handle Count
    final static String CSP_MAX_OPEN_HANDLE_COUNT = "CSPMaxOpenHandleCount";

    //CSP DB Suggest Open Handle Count
    final static String CSP_SUGGEST_OPEN_HANDLE_COUNT =
            "CSPSuggestOpenHandleCount";

    //CSP SDB Sync Frequency
    final static String CSP_SYNC_FREQUENCY = "CSPSyncFrquency";

    //Lazy Thread Creation
    final static String LAZY_THREAD_CREATION = "LazyThreadCreation";

    //Lazy Thread Creation
    final static String LAZY_RS_CREATION = "LazyRSCreation";

    //Default ACL creation
    final static String CREATE_DEFAULT_ACLS = "CREATE_DEFAULT_ACLS";

    //added for WMT Dispatcher properties
    final static String ADMIN_CONNECTION_FACTORY = "AdminConnectionFactory";
    final static String LOGIN_NAME = "LoginName";
    final static String SERVER_NAME = "ServeName";

    // Disable Connection Close when CSP data is corrupt
    final static String DISABLE_CONNECTION_CLOSE_ON_CSP_CORRUPT = "DisableConnectionCloseOnCSPCorrupt";

    // allow padding to encryption key used
    final static String ENCRYPTION_ALLOW_PADDING_TO_KEY = "AllowPaddingToEncKey";

    final static String ENCRYPTION_INITIALIZATION_VECTOR = "EncKeyInitializationVector";

    final static String LOAD_ADMINOBJECT_CONFIGS = "LoadAdminObjectConfigsEnabled";

    final static String REDELIVERY_TRIES_ON_LISTENER_EXCEPTION = "RedeliveryTriesOnListenerException";

    final static String CORRUPTED_COUNT = "CorruptedCount";

    final static String DELAY_IN_MSG_DELIVERY_ON_LISTENER_EXCEPTION = "DelayInMsgDeliveryOnListenerException";

    final String CREATION_TIME = "CREATION_TIME";
}

