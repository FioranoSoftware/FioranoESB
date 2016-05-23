/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;

public class SystemInfo extends DmiObject
{
    private String m_strOpSysName;
    private String m_strOpSysVersion;

    private String m_strJreSpecVersion;
    private String m_strJreImplVersion;
    private String m_strJreImplVendor;

    private String m_strJvmSpecVersion;
    private String m_strJvmImplVersion;
    private String m_strJvmImplVendor;
    private String m_strJvmImplName;

    /**
     *  Gets the name of operating system set for this <code>SystemInfo</code> object.
     *
     * @return The name of operating system.
     * @see #setOSName(java.lang.String)
     * @since Tifosi2.0
     */
    public String getOSName()
    {
        return m_strOpSysName;
    }

    /**
     *  Gets version of the operating system set for this
     *  <code>SystemInfo</code> object.
     *
     * @return The version of operating system.
     * @see #setOSVersion(java.lang.String)
     * @since Tifosi2.0
     */
    public String getOSVersion()
    {
        return m_strOpSysVersion;
    }

    /**
     *  Gets the version of JRE specification for this
     *  <code>SystemInfo</code> object.
     *
     * @return The version of JRE specification.
     * @see #setJRESpecVersion(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJRESpecVersion()
    {
        return m_strJreSpecVersion;
    }

    /**
     *  Gets the version of JRE implementation for this <code>SystemInfo</code>
     *  object.
     *
     * @return The version of JRE implementation.
     * @see #setJREImplVersion(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJREImplVersion()
    {
        return m_strJreImplVersion;
    }

    /**
     *  Gets the name of JRE implementation vendor for this
     *  <code>SystemInfo</code> object.
     *
     * @return The name of JRE implementation vendor.
     * @see #setJREImplVendor(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJREImplVendor()
    {
        return m_strJreImplVendor;
    }

    /**
     *  Gets the version of JVM specification for this <code>SystemInfo</code>
     *  object.
     *
     * @return The version of JVM Specification
     * @see #setJVMSpecVersion(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJVMSpecVersion()
    {
        return m_strJvmSpecVersion;
    }

    /**
     *  Gets the version of JVM implementation for this <code>SystemInfo</code>
     *  object.
     *
     * @return The version of JVM implementation
     * @see #setJVMImplVersion(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJVMImplVersion()
    {
        return m_strJvmImplVersion;
    }

    /**
     *  Gets the name of JVM implementation vendor for this of
     *  <code>SystemInfo</code> object.
     *
     * @return The name of JVM implementation vendor
     * @see #setJVMImplVendor(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJVMImplVendor()
    {
        return m_strJvmImplVendor;
    }

    /**
     *  Gets the name of JVM implementation for this <code>SystemInfo</code>
     *  object.
     *
     * @return The name of JVM implementation
     * @see #setJVMImplName(java.lang.String)
     * @since Tifosi2.0
     */
    public String getJVMImplName()
    {
        return m_strJvmImplName;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return The id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SYSTEM_INFO;
    }


    /**
     *  Sets the specified string as name of the operating system for this
     *  <code>SystemInfo</code> object.
     *
     * @param OSName The string to be set as name of the operating system.
     * @see #getOSName()
     * @since Tifosi2.0
     */
    public void setOSName(String OSName)
    {
        m_strOpSysName = OSName;
    }


    /**
     *  Sets the specified string as version of the operating system for this
     *  <code>SystemInfo</code> object.
     *
     * @param version The string to be set as version of operating system
     * @see #getOSVersion()
     * @since Tifosi2.0
     */
    public void setOSVersion(String version)
    {
        m_strOpSysVersion = version;
    }


    /**
     *  Sets the specified string as version of JRE specification for this
     *  <code>SystemInfo</code> object.
     *
     * @param version The string to be set as version of JRE specification
     * @see #getJRESpecVersion()
     * @since Tifosi2.0
     */
    public void setJRESpecVersion(String version)
    {
        m_strJreSpecVersion = version;
    }


    /**
     *  Sets the specified string as version of JRE implementation for this
     *  <code>SystemInfo</code> object.
     *
     * @param version The string to be set as version of JRE implementation
     * @see #getJREImplVersion()
     * @since Tifosi2.0
     */
    public void setJREImplVersion(String version)
    {
        m_strJreImplVersion = version;
    }


    /**
     *  Sets the specified string as name of JRE implementation vendor for this
     *  <code>SystemInfo</code> object.
     *
     * @param vendor The string to be set as name of JRE implementation version
     * @see #getJREImplVendor()
     * @since Tifosi2.0
     */
    public void setJREImplVendor(String vendor)
    {
        m_strJreImplVendor = vendor;
    }


    /**
     *  Sets the specified string as version of JVM specification for this
     *  <code>SystemInfo</code> object.
     *
     * @param version The string to be set as version of JVM specification
     * @see #getJVMSpecVersion()
     * @since Tifosi2.0
     */
    public void setJVMSpecVersion(String version)
    {
        m_strJvmSpecVersion = version;
    }


    /**
     * Sets the specified string as version of JVM implementation for this
     *  <code>SystemInfo</code> object.
     *
     * @param version The string to be set as version of JVM implementation
     * @see #getJVMImplVersion()
     * @since Tifosi2.0
     */
    public void setJVMImplVersion(String version)
    {
        m_strJvmImplVersion = version;
    }


    /**
     *  Sets the specified string as name of JVM implementation vendor for this
     *  <code>SystemInfo</code> object.
     *
     * @param vendor The string to be set as name of JVM implementation vendor
     * @see #getJVMImplVendor()
     * @since Tifosi2.0
     */
    public void setJVMImplVendor(String vendor)
    {
        m_strJvmImplVendor = vendor;
    }


    /**
     * Sets the specified string as name of JVM implementation for this
     *  <code>SystemInfo</code> object.
     *
     * @param name The string to be set as name of JVM implementation
     * @see #getJVMImplName()
     * @since Tifosi2.0
     */
    public void setJVMImplName(String name)
    {
        m_strJvmImplName = name;
    }


    /**
     *  This method has not been implemented in this version.
     *  (Resets the values of the data members of this object.)
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }

    /**
     *  This method tests whether this <code>SystemInfo</code> object
     *  has the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException If the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }

    /**
     *  This method is called to read this <code>SystemInfo</code> object
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
        m_strOpSysName = readUTF(is);
        m_strOpSysVersion = readUTF(is);

        m_strJreSpecVersion = readUTF(is);
        m_strJreImplVersion = readUTF(is);
        m_strJreImplVendor = readUTF(is);

        m_strJvmSpecVersion = readUTF(is);
        m_strJvmImplVersion = readUTF(is);
        m_strJvmImplVendor = readUTF(is);
        m_strJvmImplName = readUTF(is);

    }


    /**
     *  This method is called to write this <code>SystemInfo</code> object
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception java.io.IOException
     * @since Tifosi2.0
     */
    public void toStream(java.io.DataOutput out, int versionNo)
        throws java.io.IOException
    {
        writeUTF(out, m_strOpSysName);
        writeUTF(out, m_strOpSysVersion);

        writeUTF(out, m_strJreSpecVersion);
        writeUTF(out, m_strJreImplVersion);
        writeUTF(out, m_strJreImplVendor);

        writeUTF(out, m_strJvmSpecVersion);
        writeUTF(out, m_strJvmImplVersion);
        writeUTF(out, m_strJvmImplVendor);
        writeUTF(out, m_strJvmImplName);
    }

    /**
     * This method returns the String representation of this <code>SystemInfo</code>
     * object.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("System Details ");
        strBuf.append("[");
        strBuf.append("Jre Impl. Vendor = ");
        strBuf.append(m_strJreImplVendor);
        strBuf.append(", ");
        strBuf.append("Jre Impl. Version = ");
        strBuf.append(m_strJreImplVersion);
        strBuf.append(", ");
        strBuf.append("Jre specification Version = ");
        strBuf.append(m_strJreSpecVersion);
        strBuf.append(", ");
        strBuf.append("Jvm impl. name = ");
        strBuf.append(m_strJvmImplName);
        strBuf.append(", ");
        strBuf.append("Jvm impl. Vendor = ");
        strBuf.append(m_strJvmImplVendor);
        strBuf.append(", ");
        strBuf.append("Jvm impl. version  = ");
        strBuf.append(m_strJvmImplVersion);
        strBuf.append(", ");
        strBuf.append("Jvm specification version  = ");
        strBuf.append(m_strJvmSpecVersion);
        strBuf.append(", ");
        strBuf.append("Operating System name  = ");
        strBuf.append(m_strOpSysName);
        strBuf.append(", ");
        strBuf.append("Operating System version  = ");
        strBuf.append(m_strOpSysVersion);
        strBuf.append("]");
        return strBuf.toString();
    }
}
