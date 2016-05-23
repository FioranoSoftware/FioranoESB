/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

public class NamingManagerConfig {

    private static final long serialVersionUID = 1269276345532736745L;

    //  Minimum number of deleted entries for triggering auto-defragmentation of AdminObjectManager.
    private int m_nDefragmentationLowerThreshold = 10;

    //  Upper threshold for number of deleted entries after which auto-defragmentation is guarenteed during startup.
    private int m_nDefragmentationUpperThreshold = 15;
    //50

    //  Threshold percentage for auto-defragmentation during startup, if the number of deleted entries fall between the above two thresholds.
    private int m_nDefragmentationPercentage = 20;
    //30

    //  Maximum file size(in bytes) of Admin.dat beyond which file is auto defragmented.
    private int m_nAdminObjectFileSize = 512 * 1024 * 1024;
    // 512 MB

    //  Name of the file used for storing/managing AdminObject Names
//  a unix like fashion.
    private String ADMIN_OBJECT_FILE_NAME = "admin.dat";

    //  Name of the file used for backup of the admin file during defragmentation.
    private String ADMIN_OBJECT_BACKUP_FILE_NAME = "admin~1.dat";

    //  Name of the file used for defragmentation of the admin file.
    private String ADMIN_OBJECT_DEFRAG_FILE_NAME = "admin~2.dat";

    //  Root path at which this naming manager is started.
    private String m_strPath;

    /**
     * @jmx.managed-attribute access="read-only"
     * description="Name of the file used for storing/managing AdminObject Names"
     * @jmx.descriptor name="defaultValue" value="admin.dat"
     */
    public String getAdminObjectFilename() {
        return ADMIN_OBJECT_FILE_NAME;
    }

    /**
     * @jmx.managed-attribute access="read-only"
     * description="Name of the file used for defragmentation of the admin file"
     * @jmx.descriptor name="defaultValue" value="admin~2.dat"
     */
    public String getAdminObjectDefragFilename() {
        return ADMIN_OBJECT_DEFRAG_FILE_NAME;
    }


    /**
     * @jmx.managed-attribute access="read-only"
     * description="Name of the file used for backup of the admin file during defragmentation"
     * @jmx.descriptor name="defaultValue" value="admin~1.dat"
     */
    public String getAdminObjectBackupFilename() {
        return ADMIN_OBJECT_BACKUP_FILE_NAME;
    }

    /**
     * @jmx.managed-attribute access="read-write"
     * description="Root path for this naming manager"
     * @jmx.descriptor name="dynamic" value="false"
     * @jmx.descriptor name="defaultValue" value="."
     */
    public String getPath() {
        return m_strPath;
    }

    /**
     * @jmx.managed-attribute access="read-write" value="10"
     * description="Minimum number of deleted entries for triggering auto-defragmentation of AdminObjectManager"
     * @jmx.descriptor name="defaultValue" value="10"
     */
    public int getDefragmentationLowerThreshold() {
        return m_nDefragmentationLowerThreshold;
    }

    /**
     * @jmx.managed-attribute access="read-write" value="15"
     * description="Maximum number of deleted entries after which auto-defragmentation of AdminObjectManager is guarenteed"
     * @jmx.descriptor name="defaultValue" value="15"
     */
    public int getDefragmentationUpperThreshold() {
        return m_nDefragmentationUpperThreshold;
    }

    /**
     * @jmx.managed-attribute access="read-write" value="20"
     * description="Threshold percentage for auto-defragmentation during startup, if the number of deleted entries fall between the above two thresholds"
     * @jmx.descriptor name="defaultValue" value="20"
     */
    public int getDefragmentationPercentage() {
        return m_nDefragmentationPercentage;
    }

    /**
     * Sets admin object filename for object
     */
    public void setAdminObjectFilename(String str) {
        ADMIN_OBJECT_FILE_NAME = str;
    }

    /**
     * Sets admin object defrag filename for object
     */
    public void setAdminObjectDefragFilename(String str) {
        ADMIN_OBJECT_DEFRAG_FILE_NAME = str;
    }

    /**
     * Sets admin object backup filename for object
     */
    public void setAdminObjectBackupFilename(String str) {
        ADMIN_OBJECT_BACKUP_FILE_NAME = str;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setPath(String path) {
        m_strPath = path;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setDefragmentationLowerThreshold(int threshold) {
        m_nDefragmentationLowerThreshold = threshold;
    }

    /**
     * @jmx.managed-attribute
     */
    public void setDefragmentationUpperThreshold(int threshold) {
        m_nDefragmentationUpperThreshold = threshold;
    }

    /**
     * @jmx.managed-attribute description="Threshold percentage for auto-defragmentation during startup, if the number of deleted entries fall between the above two thresholds"
     */
    public void setDefragmentationPercentage(int pc) {
        m_nDefragmentationPercentage = pc;
    }

    /**
     * @jmx.managed-attribute description="Maximum file size(in bytes) of Admin.dat beyond which file is auto defragmented."
     */
    public void setAdminObjectFileSize(int size) {
        this.m_nAdminObjectFileSize = size;
    }

    /**
     * @jmx.managed-attribute access="read-write" value="20"
     * description="Maximum file size(in bytes) of Admin.dat beyond which file is auto defragmented."
     * @jmx.descriptor name="hidden" value="true"
     * @jmx.descriptor name="defaultValue" value="536870912"
     */
    public int getAdminObjectFileSize() {
        return m_nAdminObjectFileSize;
    }
}

