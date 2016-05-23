/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application;

import com.fiorano.openesb.utils.exception.FioranoException;


public class DmiResourceData extends DmiObject
{
    /**
     *  The bytes representing the data for the resource.
     */
    private byte[]  m_bytes;

    /**
     *  Contains the name of the resource. The resorce name is is the name of
     *  the file and the relative path with respect to the folder that stores the service.
     */
    private String  m_strResName;

    /**
     *  Offset at which the data that is contained starts in the resource file
     */
    private long    m_lStartOffset;

    /**
     *  Offset at which the data that is contained ends in the resource file
     */
    private long    m_lEndOffset;

    /**
     *  The total size (in bytes) of the reource
     */
    private long    m_lResourceLength;


    /**
     *  Constructs an object of the type <code>DmiResourceData</code>.
     */
    public DmiResourceData()
    {
        reset();
    }


    /**
     *  Gets the ID of this <code>DmiResourceData</code> object.
     *
     * @return The objectID value
     */
    public int getObjectID()
    {
        return DmiObjectTypes.RESOURCE_DATA;
    }


    /**
     *  Gets the transferred value as a bytes array.
     *
     * @return the value transferred.
     */
    public byte[] getData()
    {
        return m_bytes;
    }


    /**
     *  Gets the name of the resource for which the data is being put.
     *
     * @return the name of the resource
     */
    public String getResourceName()
    {
        return m_strResName;
    }


    /**
     *  Gets the start offset of the data of the resource that is being put.
     *
     * @return startOffset - the start offset
     */
    public long getStartOffset()
    {
        return m_lStartOffset;
    }


    /**
     *  Gets the end offset of the data of the resource that is being put.
     *
     * @return endOffset the end offset
     */
    public long getEndOffset()
    {
        return m_lEndOffset;
    }


    /**
     *  Gets the total size of the resource that is being put.
     *
     * @return length - the size of the resource
     */
    public long getResourceLength()
    {
        return m_lResourceLength;
    }


    /**
     *  This method has not been implemented in this version.
     *  (Sets the values of the different values of the DmiObject.)
     *
     * @param xml The new fieldValues value
     * @exception FioranoException -if an error occurs while setting the values
     */
    public void setFieldValues(String xml)
        throws FioranoException
    {
    }


    /**
     *  Sets the value to be transferred using the specified byte array.
     *
     * @param bytes The new bytes value
     */
    public void setData(byte[] bytes)
    {
        m_bytes = bytes;
    }


    /**
     *  Sets the name of the resource for which the data is being put.
     *
     * @param resName The new resourceName value
     */
    public void setResourceName(String resName)
    {
        m_strResName = resName;
    }


    /**
     *  Sets the start offset of the data of the resource that is being put.
     *
     * @param startOffset the start offset
     */
    public void setStartOffset(long startOffset)
    {
        m_lStartOffset = startOffset;
    }


    /**
     *  Sets the end offset of the data of the resource that is being put.
     *
     * @param endOffset the end offset
     */
    public void setEndOffset(long endOffset)
    {
        m_lEndOffset = endOffset;
    }


    /**
     *  Sets the total size of the resource that is being put.
     *
     * @param length - the size of the resource
     */
    public void setResourceLength(long length)
    {
        m_lResourceLength = length;
    }


    /**
     *  Reads the data members of this <code>DmiResourceData</code> object from
     *  the specified input stream object.
     *
     * @param is Specify the DataInput object
     * @param versionNo
     * @exception java.io.IOException if an error occurs while reading bytes or while converting them into specified Java primitive type.
     */
    public void fromStream(java.io.DataInput is, int versionNo)
        throws java.io.IOException
    {
        super.fromStream(is, versionNo);

        int length = is.readInt();

        if (length > 0)
        {
            m_bytes = new byte[length];
            is.readFully(m_bytes);
        }
        m_strResName = readUTF(is);
        m_lEndOffset = is.readLong();
        m_lStartOffset = is.readLong();
        m_lResourceLength = is.readLong();
    }


    /**
     *  Writes the data members of this <code>DmiResourceData</code> object to
     *  the specified output stream object.
     *
     * @param os Specify the DataOutput object.
     * @param versionNo
     * @exception java.io.IOException if an error occurs while converting data and writing it to a binary stream.
     */
    public void toStream(java.io.DataOutput os, int versionNo)
        throws java.io.IOException
    {
        super.toStream(os, versionNo);
        if (m_bytes == null || m_bytes.length <= 0)
        {
            os.writeInt(-1);
        }
        else
        {
            os.writeInt(m_bytes.length);
            os.write(m_bytes);
        }
        writeUTF(os, m_strResName);
        os.writeLong(m_lEndOffset);
        os.writeLong(m_lStartOffset);
        os.writeLong(m_lResourceLength);
    }


    /**
     * Returns the String representation of this <code>DmiResourceData</code>
     * object.
     *
     * @return the String representation
     */
    public String toString()
    {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("[");
        strBuf.append("Bytes = ");
        strBuf.append(new String(m_bytes));
        strBuf.append(", ");
        strBuf.append("Resource Name = ");
        strBuf.append(m_strResName);
        strBuf.append(", ");
        strBuf.append("Resource Total Length = ");
        strBuf.append(m_lResourceLength);
        strBuf.append(", ");
        strBuf.append("Start Offset = ");
        strBuf.append(m_lStartOffset);
        strBuf.append(", ");
        strBuf.append("End Offset = ");
        strBuf.append(m_lEndOffset);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     * Return an XML representation of this DMI object as a String.
     *
     * Not supported in this version.
     *
     * @return the XML representation
     */
    public String toXMLString()
    {
        return null;
    }


    /**
     *  This method has not been implemented in this version.
     *  (This method tests whether the DmiObject has the required(mandatory)
     *  fields set, before inserting values in to the database.)
     *
     * @exception FioranoException
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  This method resets the values of the data members of this <code>DmiResourceData</code>
     *  object. This might be used to help the Dmifactory reuse Dmi objects.
     */
    public void reset()
    {
        m_lEndOffset = -1;
        m_lStartOffset = -1;
        m_lResourceLength = -1;
    }
}
