/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.sps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ServiceSearchContext extends DmiObject
{
    String          m_strAuthor;

    String          m_strCategory;

    String          m_strName;


    /**
     *  Constructor for the ServiceSearchContext object
     *
     * @since Tifosi2.0
     */
    public ServiceSearchContext()
    {
    }


    /**
     *  This is called to get the service <code>Name</code>, from this object of
     *  <code>ServiceSearchContext</code>, based on which a service can be searched.
     *
     * @return value Name in searchContext
     * @see #setName(String)
     * @since Tifosi2.0
     */
    public String getName()
    {
        return m_strName;
    }


    /**
     *  This is called to get the service <code>Author</code>, from this object of
     *  <code>ServiceSearchContext</code>, based on which a service can be searched.
     *
     * @return Name of Author
     * @see #setAuthor(String)
     * @since Tifosi2.0
     */
    public String getAuthor()
    {
        return m_strAuthor;
    }


    /**
     *   This is called to get the service <code>Category</code>, from this object of
     *  <code>ServiceSearchContext</code>, based on which a service can be searched.
     *
     * @return Service category
     * @see #setCategory(String)
     * @since Tifosi2.0
     */
    public String getCategory()
    {
        return m_strCategory;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_SEARCH_CONTEXT;
    }


    /**
     *  This is called to set the specified string as service <code>Name</code>,
     *  in this object of <code>ServiceSearchContext</code>,
     *  based on which a service can be searched.
     *
     * @param name The string to be set as Name in searchContext
     * @see #getName()
     * @since Tifosi2.0
     */
    public void setName(String name)
    {
        m_strName = name;
    }


    /**
     *  This is called to set the specified string as service <code>Author</code>,
     *  in this object of <code>ServiceSearchContext</code>, based on which a
     *  service can be searched.
     *
     * @param author The string to be set as Author name
     * @see #getAuthor()
     * @since Tifosi2.0
     */
    public void setAuthor(String author)
    {
        m_strAuthor = author;
    }


    /**
     *  This is called to set the specified string as service <code>Category</code>,
     *  in this object of <code>ServiceSearchContext</code>, based on which a
     *  service can be searched.
     *
     * @param category the string to be set as Service category
     * @see #getCategory()
     * @since Tifosi2.0
     */
    public void setCategory(String category)
    {
        m_strCategory = category;
    }


    /**
     *  This method tests whether this object of <code>ServiceSearchContext</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
    }


    /**
     *  Resets the values of the data members of this object. Not supported in
     *  this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>ServiceSearchContext</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof ServiceSearchContext))
        {
            return false;
        }

        ServiceSearchContext rcvObj = (ServiceSearchContext) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getAuthor(), m_strAuthor)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getCategory(), m_strCategory)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getName(), m_strName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     *  This method is called to write this object of <code>ServiceSearchContext</code>
     *  to the specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *                         writing it to a binary stream.

     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        writeUTF(out, m_strAuthor);
        writeUTF(out, m_strCategory);
        writeUTF(out, m_strName);
    }


    /**
     *  This is called to read this object <code>ServiceSearchContext</code>
     *  from the specified object of input stream.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_strAuthor = readUTF(is);
        m_strCategory = readUTF(is);
        m_strName = readUTF(is);
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>ServiceSearchContext</code>
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
        strBuf.append("Service Search Context Details ");
        strBuf.append("[");
        strBuf.append("Author = ");
        strBuf.append(m_strAuthor);
        strBuf.append(", ");
        strBuf.append("Category = ");
        strBuf.append(m_strCategory);
        strBuf.append(", ");
        strBuf.append("Name = ");
        strBuf.append(m_strName);
        strBuf.append("]");
        return strBuf.toString();
    }

}
