/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.utils.Util;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

public class NamingManagerImpl {
    //  State indicators for various Admin Name entries
    private final static byte VALID_ENTRY = (byte) 0x00;
    private final static byte ADDING_ENTRY = (byte) 0x01;
    private final static byte DELETED_ENTRY = (byte) 0x02;
    public final static byte NAMED_SERIALIZABLE_OBJECT = 112;
    public final static byte SERIALIZED_XML = 113;

    //  File in which all admin Objects are stored/managed.
    private RandomAccessFile adminFile;

    //Configuration
    private NamingManagerConfig config = null;

    //  Last valid offset after which new user data has to be appended.
    private long lastValidOffset;

    //  AdminObject loaded up in memory for all persisted adminObjects. This is
    //  done for efficient lookUp.
    //  Hashtable  of the AdminObjects. It has a 1-1 correspondence
    //  with type.
    private Hashtable persistentAdminObjectsTable;

    //  Non-persistent admin object
    private Hashtable npAdminObjectsTable;

    private String adminFilePath;

    private String adminFileName;

    private static NamingManagerImpl namingManager = new NamingManagerImpl();
    /**
     * Construct a new instance of Naming Manager.
     */
    private NamingManagerImpl() {
        //  Initialize the Hashtables.
        //
        persistentAdminObjectsTable = new Hashtable();
        npAdminObjectsTable = new Hashtable();
        try {
            _configure();
            startService();
        } catch (FioranoException e) {
            e.printStackTrace();
        }
    }

    public static NamingManagerImpl GETINSTANCE(){
        return namingManager;
    }

    /**
     * Returns system queue name for object
     */
    public String getSystemQueueName(String name) {
        return name;
    }

    /**
     * Returns system topic name for object
     */
    public String getSystemTopicName(String name) {
        return name;
    }

    /**
     * Returns system queue CF name for object
     */
    public String getSystemQueueCFName(String name) {
        return name;
    }

    /**
     * Returns system topic CF name for object
     */
    public String getSystemTopicCFName(String name) {
        return name;
    }

    /**
     * Returns system admin CF name for object
     */
    public String getSystemAdminCFName(String name) {
        return name;
    }

    /**
     * Returns system unified CF name for object
     */
    public String getSystemUnifiedCFName(String name) {
        return name;
    }

    public Hashtable<String, Object[]> listNamingObjects()
            throws FioranoException {

        

        Hashtable<String, Object[]> namingObjects = new Hashtable<String, Object[]>();
        for (Object o : persistentAdminObjectsTable.keySet()) {
            String persObjectName = String.valueOf(o);
            Object[] adminInfo = new Object[2];
            adminInfo[0] = ((AdminInfo) persistentAdminObjectsTable.get(o)).adminObject;
            adminInfo[1] = Boolean.TRUE;
            namingObjects.put(persObjectName.toUpperCase(), adminInfo);
        }
        for (Object o : npAdminObjectsTable.keySet()) {
            String nonPObjectName = String.valueOf(o);
            Object[] adminInfo = new Object[2];
            adminInfo[0] = ((AdminInfo) npAdminObjectsTable.get(o)).adminObject;
            adminInfo[1] = Boolean.FALSE;
            namingObjects.put(nonPObjectName.toUpperCase(), adminInfo);
        }

        return namingObjects;
    }

    /**
     * @throws FioranoException
     */
    public void _configure()
            throws FioranoException {
        config = new NamingManagerConfig();
        adminFilePath = ServerConfig.getConfig().getRuntimeDataPath();
    }

    /**
     * @throws FioranoException
     */
    public synchronized void startService()
            throws FioranoException {
        loadAdminInfo();
    }

    /**
     * @throws FioranoException
     */
    public synchronized void stopService()
            throws FioranoException {
        closeAdminFile();
    }

    /**
     * Bind an object with a name. It provides an additional functionality
     * of binding the object in in-memory namimg store only.
     */
    public boolean bind(String name, Object data, boolean persist)
            throws FioranoException {
        if (!isBound(name) && !name.equalsIgnoreCase(" ")) {
            if (persist) {
                return bind(name, data);
            } else {
                return _bindNP(name, data);
            }
        } else {
            if (name.equalsIgnoreCase(" ")) {
                throw new FioranoException(NamingManagerImpl.class, NmErrorCodes.ERR_INVALID_ARGUMENTS_ERROR,
                        "NamingManagerImpl.bind.toBeFilled4");
            } else {
                throw new FioranoException(NamingManagerImpl.class,
                        NmErrorCodes.ERR_ADMINISTERED_OBJECT_ALREADY_EXIST,
                        "NamingManagerImpl._bind.toBeFilled2", name);
            }
        }
    }

    public boolean isBound(String name) {
        AdminInfo adminInfo = (AdminInfo) persistentAdminObjectsTable.get(
                name.toUpperCase());

        if (adminInfo == null) {
            adminInfo = (AdminInfo) npAdminObjectsTable.get(name.toUpperCase());
        }

        return adminInfo != null;
    }

    /**
     * Bind an object with a name. In case a binding already exists, then
     * its removed first before adding this binding. It provides an
     * additional functionalityof binding the object in in-memory namimg store only.
     */
    public synchronized boolean rebind(String adminObjectName, Object data, boolean persist)
            throws FioranoException {
        if (isBound(adminObjectName)) {
            destroy(adminObjectName);
        }
        return bind(adminObjectName, data, persist);
    }

    /**
     * Add the Admin Object specified by given AdminObjectName
     * to the list of authorised Administrated Objects that can be
     * used by  Client Applications that connect to this Server
     *
     * @throws FioranoException if this operation fails to complete.
     */
    public synchronized boolean bind(String adminObjectName, Object data)
            throws
            FioranoException {

        // for disallowing binding of null objects
        if (adminObjectName == null || data == null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_INVALID_ARGUMENTS_ERROR);
        }

        // JMSMetaData is also instance of Serializable
        if (!(data instanceof Serializable)) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_UNSERIALIZABLE_OBJECT);
        }

        //  Convert it to upper-case first.
        adminObjectName = adminObjectName.trim().toUpperCase();

        return _bind(adminObjectName, data);
    }

    /**
     * Remove the adminObject identified by a given adminObjectName from the list
     * of administered Objects
     *
     * @throws FioranoException if this operation fails to complete.
     */
    public synchronized boolean destroy(String adminObjectName)
            throws
            FioranoException {
        
        if (adminObjectName == null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_NULL);
        }

        //  Convert it to Lower-case first.
        adminObjectName = adminObjectName.trim().toUpperCase();

        return _destroy(adminObjectName);
    }

    /**
     * returns an enumeration of all Adminobjects(either Destination or
     * ConnectionFactory) depending on the specified type
     *
     * @throws FioranoException if this operation fails to complete.
     */
    public Enumeration elements()
            throws FioranoException {
        
        return new NamingInfoEnumeration();
    }

    /**
     * Get the  description of the AdminObject. Retrieve from the local storage.
     * @throws FioranoException if this operation fails to complete.
     */
    public synchronized Object lookup(String adminObjectName)
            throws
            FioranoException {



      /*  namingManagerLogger.trace(NamingManagerImpl.class,
                "NamingManagerImpl.lookup.toBeFilled01",
                adminObjectName);*/


        if (adminObjectName == null) {
            throw new FioranoException(NamingManagerImpl.class, NmErrorCodes.ERR_ADMINISTERED_OBJECT_NULL);
        }

        //  Convert it to Lower-case first.
        adminObjectName = adminObjectName.trim().toUpperCase();

        AdminInfo adminInfo = (AdminInfo) persistentAdminObjectsTable.get(adminObjectName);

        // If not found in persistent adminObject table, check it up in
        // np table
        //
        if (adminInfo == null) {
            adminInfo = (AdminInfo) npAdminObjectsTable.get(adminObjectName);

            if (adminInfo == null) {

                /*namingManagerLogger.trace(NamingManagerImpl.class,
                        "NamingManagerImpl.lookup.toBeFilled02",
                        adminObjectName);*/

                return null;
            }
        }

        return adminInfo.adminObject;
    }

    /**
     * Zips the database in targetDirectory\targetZipName
     */
    public synchronized boolean zipDatabase(String ZipFileName, boolean append)
            throws FioranoException {
        File zipFile = new File(ZipFileName);

        if (!append) {
            Util.deleteFile(zipFile, null);
        }

        try {
            if (adminFile != null) {
                adminFile.getFD().sync();
            }
        } catch (IOException ex1) {
            // Ignore the exception

            /*namingManagerLogger.debug(NamingManagerImpl.class,
                    "debug.NaminManagerImpl.zipDatabase.toBeFilled1", ex1);*/

        }

        try {
            ZipOutputStream zipOstream = new ZipOutputStream(new FileOutputStream(zipFile));
            try {
                Util.zipDir(adminFileName, null, zipOstream, 1024);
            } finally {
                zipOstream.close();
            }
            return true;
        } catch (IOException ex) {
          //  namingManagerLogger.debug(NamingManagerImpl.class, "NaminManagerImpl.zipDatabase.toBeFilled5", ex);
            throw new FioranoException(NamingManagerImpl.class, "NaminManagerImpl.zipDatabase.toBeFilled5", ex);
        }
    }

    /**
     * Extract the database from parameter directory
     */
    public synchronized boolean extractDatabase(String extractedDb, boolean overwrite)
            throws FioranoException {
        try {
            if (adminFile != null) {
                adminFile.close();
            }
        } catch (IOException ex) {
            // Ignore the exception

            /*namingManagerLogger.debug(NamingManagerImpl.class,
                    "debug.NaminManagerImpl.extractDatabase.toBeFilled1", ex);*/

        }

        // Delete the older database
        Util.deleteFile(adminFileName, null);
        Util.moveFiles(extractedDb, adminFilePath, null);

        return true;
    }

    private boolean _bind(String adminObjectName, Object data)
            throws FioranoException {
        AdminInfo info;

        //  One Context can NOT be bound to more than ONE Object.
        //  Ensure that the name is NOT already bound.
        //
        if (persistentAdminObjectsTable.get(adminObjectName) != null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_ALREADY_EXIST,
                    "NamingManagerImpl._bind.toBeFilled2", adminObjectName);
        }

        if (npAdminObjectsTable.get(adminObjectName) != null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_ALREADY_EXIST,
                    "NamingManagerImpl._bind.toBeFilled2", adminObjectName);
        }

        try {
            //Fix for Bug# 15220
            if (adminFile.length() > config.getAdminObjectFileSize()) {
                defragment();
               /* namingManagerLogger.info(NamingManagerImpl.class,
                        FMQI18Nutil.getL10NMsg(this.getClass(), "defragmented.the.file.0.because.it.exceeds.the.maximum.size.1.bytes",
                                config.getAdminObjectFilename(), config.getAdminObjectFileSize()));*/
            }
            info = new AdminInfo(adminObjectName, data, lastValidOffset);

            //  Set the state as ADDING_ENTRY
            //
            adminFile.seek(lastValidOffset);
            adminFile.writeByte(ADDING_ENTRY);

            ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
            DataOutputStream dos = new DataOutputStream(bos);

            byte objType = NAMED_SERIALIZABLE_OBJECT;

            try {
                /*if (data instanceof JMSMetaData) {
                    JMSMetaData metadata = (JMSMetaData) data;

                    objType = metadata.getType();
                    metadata.toStream(dos, IFioranoBuildConstants.MQ_MINOR_VERSION_AS_INT);
                } else */if (data instanceof byte[]) {
                    objType = SERIALIZED_XML;
                    dos.writeInt(((byte[]) data).length);
                    dos.write((byte[]) data);
                } else {
                    ObjectOutput oos = new ObjectOutputStream(dos);

                    oos.writeObject(data);
                }
            } catch (ClassCastException cce) {
                throw new FioranoException(cce.getMessage());
            }

            byte[] buff = bos.toByteArray();

            adminFile.writeUTF(adminObjectName);
            adminFile.writeByte(objType);
            adminFile.writeInt(buff.length);
            adminFile.write(buff);

            //  Reset State to VALID
            //
            long newPosition = adminFile.getFilePointer();

            adminFile.seek(lastValidOffset);
            adminFile.writeByte(VALID_ENTRY);
            adminFile.getFD().sync();

            lastValidOffset = newPosition;

            //Putting it in hashtable.
            persistentAdminObjectsTable.put(info.name, info);
        } catch (IOException e) {
            /*fmqNamingManagerServiceLogger.error(NamingManagerImpl.class,
                    "error.NamingManagerImpl._bind.toBeFilled1", e.getMessage());
            fmqNamingManagerServiceLogger.debug(e);*/
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_CREATION_ERROR, e);
        }
        return true;
    }

    private boolean _destroy(String adminObjectName)
            throws FioranoException {
        AdminInfo info = (AdminInfo) persistentAdminObjectsTable.remove(adminObjectName);

        // Find it in np named objects
        if (info == null) {
            info = (AdminInfo) npAdminObjectsTable.remove(adminObjectName);
            //doing delete only if the admin Object exists. otherwise no issues.
            //if (info == null)
            //throw new FioranoException(NamingManagerImpl.class, NmErrorCodes.ERR_INVALID_ARGUMENTS_ERROR, "Destroy.call.Non-exist");
        }

        if (info != null) {
            try {
                info.delete();
                return true;
            } catch (FioranoException e) {
                /*fmqNamingManagerServiceLogger.error(NamingManagerImpl.class,
                        "error.NamingManagerImpl._destroy.toBeFilled1",
                        e.getMessage());
                fmqNamingManagerServiceLogger.debug(e);*/
                throw new FioranoException(NamingManagerImpl.class,
                        "error.NamingManagerImpl._destroy.toBeFilled1", e);
            }
        }
        return false;
    }

    /**
     * Bind Object in non-persistent table
     */
    private synchronized boolean _bindNP(String name, Object data)
            throws FioranoException {

        /*namingManagerLogger.trace(NamingManagerImpl.class,
                "NamingManagerImpl.bind.toBeFilled02",
                name);*/

        // for disallowing binding of null objects
        if (name == null || data == null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_INVALID_ARGUMENTS_ERROR);
        }

        //  Convert it to upper-case first.
        name = name.toUpperCase();

        AdminInfo info;

        //  One Context can NOT be bound to more than ONE Object.
        //  Ensure that the name is NOT already bound.
        //
        if (persistentAdminObjectsTable.get(name) != null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_ALREADY_EXIST,
                    "NamingManagerImpl._bind.toBeFilled2", name);
        }

        if (npAdminObjectsTable.get(name) != null) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_ADMINISTERED_OBJECT_ALREADY_EXIST,
                    "NamingManagerImpl._bind.toBeFilled2", name);
        }

        // Since data is never persisted in cache, using -1 as offset
        info = new AdminInfo(name, data, -1);

        //Putting it in hashtable.
        npAdminObjectsTable.put(info.name, info);

        return true;
    }

    /**
     * Initialize the Admin Info file. If the file is not found, then
     * check for defragment or backup files created during
     * defragmentation of the same.
     */
    private String initAdminFile() {
        // Fix for Bug# 156 - Jissy
        // Config path will now be used to locate the Admin Objects related files.
        // Fixed N_389
        // COMMENT -Ankit
        // After JMX changes the file names should be read from config provided
        // by configuration manager. So above line replaced by following line
        /**
         *m_strPath = env.get (IMQConstants.PATH);
         */
        String fileName = adminFilePath + File.separator +
                config.getAdminObjectFilename();

        //  Check if an old admin file is located.
        //
        File m_adminFile = new File(fileName);

        if (m_adminFile.exists()) {
            return fileName;
        }

        //  If m_adminFile doesn't exists, than check for backup files
        //
        //  Check for backup file if the main file is missing.
        //
        String strBackupFileName = adminFilePath + File.separator +
                config.getAdminObjectBackupFilename();

        File backupFile = new File(strBackupFileName);

        if (backupFile.exists()) {
            backupFile.renameTo(m_adminFile);
        } else {
            //  If the backup file is also missing than look for any
            //  leftovers of the defragFiles.
            //
            String strDefragFileName = adminFilePath + File.separator +
                    config.getAdminObjectDefragFilename();
            File defragFile = new File(strDefragFileName);

            if (defragFile.exists()) {
                defragFile.renameTo(m_adminFile);
            }
        }
        return fileName;
    }

    /**
     * Initialize the AdminObject File, open's it and verify it for
     * integrity. Mark the last valid offset as official end of
     * file. If unable to open the file an appropritate exception
     * indicating the same is reported.
     *
     * @throws FioranoException if unable to initialize the file.
     */
    private void loadAdminInfo()
            throws
            FioranoException {
        adminFileName = initAdminFile();

        long fileLength;
        int deletedEntriesCount = 0;
        ByteArrayInputStream bis;

        try {
            adminFile = new RandomAccessFile(adminFileName, "rw");
            fileLength = adminFile.length();
            lastValidOffset = 0;
            adminFile.seek(lastValidOffset);
        } catch (IOException e) {
            throw new FioranoException(NamingManagerImpl.class,
                    NmErrorCodes.ERR_FILE_ACCESS_ERROR, e);
        }

        long lastEntryOffset;

        while (lastValidOffset < fileLength) {
            try {
                // store the location of the entry to be read
                lastEntryOffset = adminFile.getFilePointer();

                byte status = adminFile.readByte();
                String adminInfoName = adminFile.readUTF();
                byte type = adminFile.readByte();
                int m_bufferLength = adminFile.readInt();
                byte[] buff;

                try {
                    buff = new byte[m_bufferLength];
                } catch (OutOfMemoryError o) {
                    throw new FioranoException(o.getMessage());
                }

                adminFile.read(buff);

                bis = new ByteArrayInputStream(buff);

                DataInputStream dis = new DataInputStream(bis);
                Object adminObj;

                /*// Added to get the version number mentioned in the Admin.dat file
                // To use to differentiate between the build older than MQ2006
                String version = new String(buff, 2, 4);

                // Made a check as the installers of previous version numbers had
                // Constants value starting from '0' instead of starting from
                // '100'. This check has been placed to provide backword compatibility
                // to the previous installers, when they are shifting to MQ2006 or greater.
                if (Integer.parseInt(version) > 4000) {*/         // Commented as throwing NumberFormatException

                switch (type) {
                   /* case IMQConstants.NAMED_QUEUE:
                        adminObj = new QueueMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_QUEUE_CONNECTION_FACTORY:
                        adminObj = new QueueConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_TOPIC:
                        adminObj = new TopicMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_TOPIC_CONNECTION_FACTORY:
                        adminObj = new TopicConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_ADMIN_CONNECTION_FACTORY:
                        adminObj = new AdminConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_COMMON_CONNECTION_FACTORY:
                        adminObj = new UnifiedConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_XA_COMMON_CONNECTION_FACTORY:
                        adminObj = new UnifiedXAConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_XA_QUEUE_CONNECTION_FACTORY:
                        adminObj = new XAQueueConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case IMQConstants.NAMED_XA_TOPIC_CONNECTION_FACTORY:
                        adminObj = new XATopicConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;*/
                    case SERIALIZED_XML:
                        int size = dis.readInt();
                        adminObj = new byte[size];
                        dis.read((byte[]) adminObj);
                        break;
                    case NAMED_SERIALIZABLE_OBJECT:
                        ObjectInput ois = new ObjectInputStream(dis);
                        adminObj = ois.readObject();
                        break;
                    /*case 1:
                        adminObj = new QueueMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 3:
                        adminObj = new QueueConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 0:
                        adminObj = new TopicMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 2:
                        adminObj = new TopicConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 4:
                        adminObj = new AdminConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 8:
                        adminObj = new UnifiedConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 11:
                        adminObj = new UnifiedXAConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 9:
                        adminObj = new XAQueueConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;
                    case 10:
                        adminObj = new XATopicConnectionFactoryMetaData();
                        ((JMSMetaData) adminObj).fromStream(dis);
                        break;*/
                    default:
                        throw new FioranoException(NamingManagerImpl.class,
                                "NamingManagerImpl.loadAdminInfo.toBeFilled1");
                }

                long infoOffset = lastEntryOffset;

                lastValidOffset = adminFile.getFilePointer();

                if (status != VALID_ENTRY) {
                    ++deletedEntriesCount;
                    continue;
                }

                AdminInfo m_adminInfo = new AdminInfo(adminInfoName, adminObj,
                        infoOffset);

                // Check whether it is valid to load the metaData.
                // Here are some of the invalidity pointing cases.

                // o XA is not enabled on the server and xaconnectionfactory is being loaded
                // o RDBMS impl is not enabled and rdbms based destination is being loaded

                // So it is not desirable to load such conn factories and destinations.

             /*   if (npAdminObjectsTable.remove(m_adminInfo.name) != null)
                    namingManagerLogger.trace(NamingManagerImpl.class, "Removed_admin_info_from_non_pesistent_hashtable"
                            , m_adminInfo.name);*/

                persistentAdminObjectsTable.put(m_adminInfo.name, m_adminInfo);


                /*namingManagerLogger.trace(NamingManagerImpl.class,
                        "trace.NamingManagerImpl.loadAdminInfo.loaded",
                        m_adminInfo.name);*/

            } catch (IOException e) {
                //  reached EOF, no problem just break out of the while
                //  loop.
                //

                /*fmqNamingManagerServiceLogger.debug(NamingManagerImpl.class,
                        "debug.NamingManagerImpl.loadAdminInfo.cantRead", e);*/


                break;
            } catch (ClassNotFoundException e) {
                // error while reading object from adminFile.
               /* fmqNamingManagerServiceLogger.error(NamingManagerImpl.class,
                        "Unable_to_load_admin_object", e.getMessage());
                fmqNamingManagerServiceLogger.debug(e);*/
                break;
            }
        }

        try {
            //  Check if ExAdminObjectManager needs defragmentation due
            //  to too many deleted entries.
            //
            checkDefragmentation(deletedEntriesCount);
        } catch (IOException e) {
            throw new FioranoException(NamingManagerImpl.class, NmErrorCodes.ERR_FILE_DEFRAGMENTATION_ERROR, e);
        }
    }

    /**
     * Closes the Admin file and releases all associated resources.
     */
    private void closeAdminFile() {
        try {
            adminFile.close();
        } catch (IOException e) {
            //  Unable to close password file properly. No problem
            //  continue the good work and log this error.
            /*namingManagerLogger.error(NamingManagerImpl.class,
                    "Unable_to_close_password_file", e.getMessage());
            namingManagerLogger.debug(e);*/
        }
        adminFile = null;
    }

    /**
     * Check if the associated file needs to be defragmented due
     * to a huge number of deleted entries.
     */
    private void checkDefragmentation(int delCount)
            throws IOException,
            FioranoException {
        boolean bShouldDefrag = false;

        if (delCount > config.getDefragmentationLowerThreshold()) {
            if (delCount > config.getDefragmentationUpperThreshold()) {
                bShouldDefrag = true;
            } else {
                int totalCount = persistentAdminObjectsTable.size() + delCount;

                if (delCount * 100 >
                        totalCount * config.getDefragmentationPercentage()) {
                    bShouldDefrag = true;
                }
            }
        }

        //  Check if defragmentation is required, then copy the
        //  new file in backup area and defragment.
        //
        if (bShouldDefrag) {
            defragment();
        }
    }

    /**
     * Defragment the Admin's persistent storage medium without
     * verifying the number of deleted entries etc.
     */
    private void defragment()
            throws IOException, FioranoException {
        Hashtable validEntries = new Hashtable();
        String defragFileName = adminFilePath + File.separator +
                config.getAdminObjectDefragFilename();

        Enumeration adminObjects = persistentAdminObjectsTable.elements();

        File dfgFile = new File(defragFileName);

        if (dfgFile.exists() && !dfgFile.delete()) {
            throw new IOException(
                    "NamingManagerImpl.defragment.toBeFilled1");
        }

        //  create a new defrag file and dump the contents in it.
        //
        RandomAccessFile defragFile =
                new RandomAccessFile(defragFileName, "rw");

        long newValidoffset;

        //  remove deleted entries and copy all valid entries into
        //  this new file.
        //
        while (adminObjects.hasMoreElements()) {
            AdminInfo adminInfo = (AdminInfo) adminObjects.nextElement();

            //  Add it directly to the new temporary file.
            //
            if (adminInfo.isValid()) {
                newValidoffset = defragFile.getFilePointer();
                defragFile.writeByte(VALID_ENTRY);

                ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
                DataOutputStream dos = new DataOutputStream(bos);
                Object data = adminInfo.getData();
                byte objType = NAMED_SERIALIZABLE_OBJECT;

                try {
                    /*if (data instanceof JMSMetaData) {
                        JMSMetaData metadata = (JMSMetaData) data;

                        objType = metadata.getType();
                        metadata.toStream(dos, IFioranoBuildConstants.MQ_MINOR_VERSION_AS_INT);
                    } else*/ {
                        ObjectOutput oos = new ObjectOutputStream(dos);

                        oos.writeObject(data);
                    }
                } catch (ClassCastException cce) {
                    throw new FioranoException(cce.getMessage());
                }

                byte[] buff = bos.toByteArray();

                defragFile.writeUTF(adminInfo.name);
                defragFile.writeByte(objType);
                defragFile.writeInt(buff.length);
                defragFile.write(buff);

                AdminInfo newAdminInfo = new AdminInfo(adminInfo,
                        newValidoffset);

                validEntries.put(newAdminInfo.name, newAdminInfo);
            }
        }

        newValidoffset = defragFile.length();

        defragFile.close();
        adminFile.close();

        File oldFile = new File(adminFilePath + File.separator +
                config.getAdminObjectFilename());
        File newFile = new File(adminFilePath + File.separator +
                config.getAdminObjectDefragFilename());
        String tmpFileName = adminFilePath + File.separator +
                config.getAdminObjectBackupFilename();

        File tmpFile = new File(tmpFileName);

        if (tmpFile.exists()) {
            tmpFile.delete();
        }

        if (!oldFile.renameTo(tmpFile)) {
            /*throw new IOException(FMQI18Nutil.getL10NMsg(NamingManagerImpl.class,
                    "NamingManagerImpl.defragment.toBeFilled2",
                    oldFile.getName(), tmpFileName));*/
        }

        if (!newFile.renameTo(oldFile)) {
            /*throw new IOException(FMQI18Nutil.getL10NMsg(NamingManagerImpl.class,
                    "NamingManagerImpl.defragment.toBeFilled2",
                    newFile.getName(), oldFile.getName()));*/
        }

        tmpFile.delete();

        String fileName = adminFilePath + File.separator +
                config.getAdminObjectFilename();

        adminFile = new RandomAccessFile(fileName, "rw");
        persistentAdminObjectsTable = validEntries;
        lastValidOffset = newValidoffset;
    }

    /**
     * Class <code> NamingInfoEnumeration <code/> is made especially for returning the
     * enumeration of elements of MetaData Objects.Because we can't directly
     * return elements of our hashtable as it contains AdminInfo objects
     * and not MetaData objects.
     *
     * @author FSIPL
     * @version 1.0
     * @created December 30, 2004
     */
    private class NamingInfoEnumeration
            implements Enumeration {
        private Enumeration pAdminInfoEnum = null;
        private Enumeration npAdminInfoEnum = null;

        // constructor returns enumeration of AdminInfo objects only
        NamingInfoEnumeration() {
            pAdminInfoEnum = persistentAdminObjectsTable.elements();
            npAdminInfoEnum = npAdminObjectsTable.elements();
        }

        //Returns the hasMoreElements of the local enumeration

        /**
         * @return
         */
        public boolean hasMoreElements() {
            return pAdminInfoEnum.hasMoreElements() || npAdminInfoEnum.hasMoreElements();

        }

        /**
         * Returns the MetaData object as the next element.
         *
         * @return the MetaData object as the next element.
         * @throws NoSuchElementException
         */
        public Object nextElement()
                throws NoSuchElementException {
            if (pAdminInfoEnum.hasMoreElements()) {
                return ((AdminInfo) pAdminInfoEnum.nextElement()).getData();
            }

            return ((AdminInfo) npAdminInfoEnum.nextElement()).getData();
        }
    }

    /**
     * class <code> AdminInfo </code> encapsulates information about
     * various AdminObjects. It also maintains offset in the
     * file at which this entry exists.
     *
     * @author FSIPL
     * @version 1.0
     * @created December 30, 2004
     */
    private class AdminInfo {
        private boolean deleted;
        private String name;
        private long offset;
        private Object adminObject;

        /**
         * Constructs a new UserInfo Object, representing a valid
         * user allowed to connect to this JMS Server.
         */
        AdminInfo(String manager,
                  Object adminObject, long offset) {
            name = manager;
            this.offset = offset;
            this.adminObject = adminObject;
            deleted = false;
        }

        /**
         * Copy Constructs a new UserInfo Object, representing a valid
         * user allowed to connect to this JMS Server. New offset is used instead
         * of the original offset.
         */
        AdminInfo(AdminInfo info, long offset) {
            adminObject = info.adminObject;
            deleted = info.deleted;
            name = info.name;

            //  Use the supplied Offset.
            //
            this.offset = offset;
        }

        long getOffset() {
            return offset;
        }

        /**
         * @return true if this entry is not yet deleted.
         */
        boolean isValid() {
            return !deleted;
        }

        /**
         * Get the description of the AdminInfo Object.
         */
        Object getData() {
            return adminObject;
        }

        /**
         * Delete this admin info and mark this object as deleted in the
         * UserInfo file.
         */
        void delete()
                throws FioranoException {
            synchronized (NamingManagerImpl.this) {
                if (deleted) {
                    return;
                }

                try {
                    if (offset != -1) {
                        adminFile.seek(offset);
                        adminFile.writeByte(DELETED_ENTRY);
                        deleted = true;
                        adminFile.getFD().sync();
                    }
                } catch (IOException e) {
                    throw new FioranoException(NamingManagerImpl.class, NmErrorCodes.ERR_FILE_ACCESS_ERROR, e);
                }
            }
        }
    }
}

