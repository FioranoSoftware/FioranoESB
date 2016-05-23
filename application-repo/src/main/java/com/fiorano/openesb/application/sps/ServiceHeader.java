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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public class ServiceHeader extends DmiObject
{
    public final static SimpleDateFormat CREATION_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    //service is launchable or not
    boolean         m_bLaunchable;

    //service is licensed or not
    boolean         m_bLicensed = false;

    //service CPS can be launched clrep or not
    boolean         m_bLaunchCPSinProcess;

    // display name of the service
    String          m_strDispName;
    // GUID of the service
    String          m_strServiceGUID;
    // date of creation of the service. Format dd/MM/yyyy
    String          m_strCreationDate;

    // date of creation of the service.
    Date          m_creationDate;
    // icon name
    String          m_strIconName;
    // icon name
    String          m_strLargeIconName;
    // version
    float           m_dVersion;
    // whether the error Handling is supported
    boolean         m_bIsErrorHandlingSupported;
    // stores authors of the service
    Vector          m_strAuthors = new Vector();
    // stores versions, this service is compatible with
    Vector          m_strCompatibleWith = new Vector();
    //stores categories to which this service belongs
    Vector          m_strCategoriesIn = new Vector();
    // Short description
    String          m_strShortDesc;
    // Long description
    String          m_strLongDesc;
    // last updated date
    Date            m_lastUpdatedDate;
    // The date format that will be used throughout the package
    private SimpleDateFormat m_dateFormat =
        new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a z");
    private SimpleDateFormat m_dateFormat1 =
        new SimpleDateFormat("dd/MM/yyyy");

    //Adding this format to differentiate dates in a day.
    //Due to Internationalization issues, old m_dateFormat cannot be used.
    private SimpleDateFormat m_dateFormat2 =
        new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private String  m_strLabel;

    // specifies whether it has any native component.
    private boolean m_bNative;

    /**
     *  Constructor for the ServiceHeader object
     *
     * @since Tifosi2.0
     */
    public ServiceHeader()
    {
        reset();
    }


    /**
     *  This interface is used to check whether or not the service about which, this is
     *  object of <code>ServiceHeader</code>, is launchable.
     *
     * @return true if service is launchable, false otherwise.
     * @see #setLaunchable(boolean)
     * @since Tifosi2.0
     */
    public boolean isLaunchable()
    {
        return m_bLaunchable;
    }

    /**
     * Returns licensed for object
     *
     * @return
     */
    public boolean isLicensed()
    {
        return m_bLicensed;
    }

    /**
     * Returns licensed for object
     *
     * @return
     */
    public boolean isCPSLaunchableInmemory()
    {
        return m_bLaunchCPSinProcess;
    }

    /**
     *  This interface is called to set the last updated date for this service.
     *
     * @return Date specify  date on which this service is updated.
     * @see #setUpdatedDate(Date)
     * @since Tifosi2.0
     */
    public Date getUpdatedDate()
    {
        return m_lastUpdatedDate;
    }


    /**
     *  This interface is called to get the displayName for the service about which, this
     *  is object of <code>ServiceHeader</code>.
     *
     * @return The displayName for service.
     * @see #setDisplayName(String)
     * @since Tifosi2.0
     */
    public String getDisplayName()
    {
        return m_strDispName;
    }


    /**
     *  This interface is called to get the GUID for the service about which, this is
     *  object of <code>ServiceHeader</code>.
     *
     * @return GUID of service
     * @see #setServiceGUID(String)
     * @since Tifosi2.0
     */
    public String getServiceGUID()
    {
        return m_strServiceGUID;
    }


    /**
     *  This interface method is called to get enumeration of the list of author names
     *  for the service, about which this is object of <code>ServiceHeader</code>.
     *
     * @return Enumeration of author names for service
     * @see #addAuthor(String)
     * @see #removeAuthor(String)
     * @see #clearAuthors()
     * @since Tifosi2.0
     */
    public Enumeration getServiceAuthors()
    {
        if (m_strAuthors == null)
        {
            m_strAuthors = new Vector();
        }
        return m_strAuthors.elements();
    }


    /**
     *  This interface method is called to get the creation date for the service about
     *  which, this is object of <code>ServiceHeader</code>.
     *  Format dd/MM/yyyy. Ex- 04/04/2006
     *
     * @return The creation date for service
     * @see #setCreationDate(String)
     * @since Tifosi2.0
     */
    public String getCreationDate()
    {
        return m_strCreationDate;
    }

    /**
     *  This interface method is called to get the creation date for the service about
     *  which, this is object of <code>ServiceHeader</code>.
     *
     * @return The creation date for service
     * @see #setCreationDateAsDate(Date)
     */
    public Date getCreationDateAsDate()
    {
        return m_creationDate;
    }


    /**
     *  This interface method is called to get the icon name for the service about which,
     *  this is object of <code>ServiceHeader</code>.
     *
     * @return The icon name for service
     * @see #setIcon(String)
     * @since Tifosi2.0
     */
    public String getIcon()
    {
        return m_strIconName;
    }

    /**
     *  This interface method is called to get the 32*32 icon name for the service about which,
     *  this is object of <code>ServiceHeader</code>.
     *
     * @return The icon name for service
     * @see #setLargeIcon(String)
     * @since Tifosi2.0
     */
    public String getLargeIcon()
    {
        return m_strLargeIconName;
    }


    /**
     *  This interface method is called to set the specified string as version for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @return version for service
     * @see #setVersion(float)
     * @since Tifosi2.0
     */
    public float getVersion()
    {
        return m_dVersion;
    }


    /**
     *  This interface method is called to get enumeration of list of compatible with
     *  versions for the service about which, this is object of <code>ServiceHeader</code>.
     *
     * @return Enumeration of compatible with Versions list
     * @see #addCompatibleWith(String)
     * @see #removeCompatibleWith(String)
     * @see #clearCompatibleWith()
     * @since Tifosi2.0
     */
    public Enumeration getCompatibleVersions()
    {
        if (m_strCompatibleWith == null)
        {
            m_strCompatibleWith = new Vector();
        }
        return m_strCompatibleWith.elements();
    }


    /**
     *  This interface method is called to get enumeration of the list of categories
     *  for the service about which, this is object of <code>ServiceHeader</code>.
     *
     * @return Enumeration of categories for the service
     * @see #addCategory(String)
     * @see #removeCategory(String)
     * @see #clearCategory()
     * @since Tifosi2.0
     */
    public Enumeration getCategories()
    {
        if (m_strCategoriesIn == null)
        {
            m_strCategoriesIn = new Vector();
        }
        return m_strCategoriesIn.elements();
    }


    /**
     *  This interface is called to check whether or not the service about which this is
     *  object of <code>ServiceHeader</code>, supports error handling.
     *
     * @return true if this service supports error handling, false otherwise.
     * @see #setErrorHandlingSupport(boolean)
     * @since Tifosi2.0
     */
    public boolean isErrorHandlingSupported()
    {
        return m_bIsErrorHandlingSupported;
    }


    /**
     *  This interface method is called to get the short description for the service
     *  about which, this is object of <code>ServiceHeader</code>.
     *
     * @return shortDescription for service
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_strShortDesc;
    }


    /**
     *  This interface method is called to get the long description for the service
     *  about which, this is object of <code>ServiceHeader</code>.
     *
     * @return long Description value for service
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_strLongDesc;
    }

    /**
     * This method returns the label name associated with the service
     *
     * @return the label name associated with the service
     * @see #setLabel(String)
     * @since Tifosi 2.2
     */

    public String getLabel()
    {
        return m_strLabel;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SERVICE_HEADER;
    }

    /**
     * Specifies that the service is native
     *
     * @param isNative
     */
    public void setNative(boolean isNative)
    {
        m_bNative = isNative;
    }

    /**
     *  This interface is called to set whether or not the service about which,
     *  this is an object of <code>ServiceHeader</code>, is launchable.
     *
     * @param launchable boolean specifying if service is launchable.
     * @see #isLaunchable()
     * @since Tifosi2.0
     */
    public void setLaunchable(boolean launchable)
    {
        m_bLaunchable = launchable;
    }

    /**
     * Sets licensed for object
     *
     * @param bLicensed
     */
    public void setLicensed(boolean bLicensed)
    {
        m_bLicensed = bLicensed;
    }

    /**
     * Sets isCPSLaunchableInMemory for object
     *
     * @param launchCPSinProcess
     */
    public void setLaunchCPSinProcess(boolean launchCPSinProcess)
    {
        m_bLaunchCPSinProcess = launchCPSinProcess;
    }

    /**
     *  This interface is called to set the last updated date for this service.
     *
     * @param date Date specify the date on which this service is updated.
     * @see #getUpdatedDate()
     * @since Tifosi2.0
     */
    public void setUpdatedDate(Date date)
    {
        m_lastUpdatedDate = date;
    }

    /**
     *  This interface is called to set the specified string as displayName for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param dispName The string to be set as displayName for service
     * @see #getDisplayName()
     * @since Tifosi2.0
     */
    public void setDisplayName(String dispName)
    {
        m_strDispName = dispName;
    }


    /**
     *  This interface is called to set the specified string as GUID for the service about
     *  which, this is object of <code>ServiceHeader</code>.
     *
     * @param servGUID The string to be set as GUID of service
     * @see #getServiceGUID()
     * @since Tifosi2.0
     */
    public void setServiceGUID(String servGUID)
    {
        m_strServiceGUID = servGUID;
    }


    /**
     *  This interface method is called to set the specified string as creation date
     *  for the service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param date The string to be set as creation date
     * @see #getCreationDate()
     * @since Tifosi2.0
     * @jmx.descriptor name="legalValues" value="dd/MM/yyyy"
     * @jmx.descriptor name="canEditAsText" value="true"
     */
    public void setCreationDate(String date)
    {
        m_strCreationDate = date;
        //Explicitly set Date object to have consistency in both formats.
        formatCreationDate(m_strCreationDate);
    }

        /**
     *  This interface method is called to set the specified date as creation date
     *  for the service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param date The string to be set as creation date
     * @see # getCreationDateAsDate()
     */
    public void setCreationDateAsDate(Date date)
    {
        m_creationDate = date;
        //Explicitly set String object to have consistency in both formats.
        m_strCreationDate = CREATION_DATE_FORMAT.format(m_creationDate);

    }

    /**
     *  This interface method is called to set the specified string as icon name for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param iconName The icon name for service
     * @see #getIcon()
     * @since Tifosi2.0
     */
    public void setIcon(String iconName)
    {
        m_strIconName = iconName;
    }


    /**
     *  This interface method is called to set the specified string as icon name for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param iconName The icon name for service
     * @see #getLargeIcon()
     * @since Tifosi2.0
     */
    public void setLargeIcon(String iconName)
    {
        m_strLargeIconName = iconName;
    }


    /**
     *  This interface method is called to set the specified string as version for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param version The float to be set as version for service
     * @see #getVersion()
     * @since Tifosi2.0
     */
    public void setVersion(float version)
    {
        m_dVersion = version;
    }


    /**
     *  This interface is called to set whether or not the service about which,
     *  this is object of <code>ServiceHeader</code>, supports error handling.
     *
     * @param isErrorHandlingSupported boolean specifying whether or not this service
     *                                  supports error handling.
     * @see #isErrorHandlingSupported()
     * @since Tifosi2.0
     */
    public void setErrorHandlingSupport(boolean isErrorHandlingSupported)
    {
        m_bIsErrorHandlingSupported = isErrorHandlingSupported;
    }


    /**
     *  This interface method is called to set the specified string as short
     *  description for service about which, this is object of <code>ServiceHeader</code>.
     *
     * @param desc string to be set as shortDescription for the service
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String desc)
    {
        m_strShortDesc = desc;
    }


    /**
     *  This interface method is called to set the specified string as long
     *  description for the service about which, this is object
     *  of <code>ServiceHeader</code>.
     *
     * @param desc string to be set as longDescription for service
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String desc)
    {
        m_strLongDesc = desc;
    }

    /**
     * This method sets the label of the service
     *
     * @param label - The label to be set
     * @see #getLabel()
     * @since Tifosi 2.2
     */

    public void setLabel(String label)
    {
        m_strLabel = label;
    }


    /**
     *  This interface method is called to set all the fieldValues of this
     *  object of <code>ServiceHeader</code>, using the specified XML string.
     *
     * @param hdrElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element hdrElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element hdrElement = doc.getDocumentElement();
        if (hdrElement != null)
        {
            m_bLaunchable = XMLDmiUtil.getAttributeAsBoolean(hdrElement, "launchable");
            m_bLicensed = XMLDmiUtil.getAttributeAsBoolean(hdrElement, "isLicensed");
            m_bIsErrorHandlingSupported = XMLDmiUtil.getAttributeAsBoolean(
                hdrElement, "isErrorHandlingSupported");
            m_bNative = XMLDmiUtil.getAttributeAsBoolean(
                hdrElement, "native");
            m_bLaunchCPSinProcess = XMLDmiUtil.getAttributeAsBoolean(
                hdrElement, "canLaunchCPSInProcess");

            String lastUpdatedAtDate = hdrElement.getAttribute("lastUpdatedAt");

            if (lastUpdatedAtDate != null && !lastUpdatedAtDate.trim().equals(""))
            {
                try
                {
                    //m_lastUpdatedDate = m_dateFormat.parse(lastUpdatedAtDate.trim());
                    formatLastUpdateDate(lastUpdatedAtDate.trim());
                }
                catch (Exception e)
                {
                }
            }

            NodeList children = hdrElement.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(child);

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    m_strDispName = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("ServiceGUID"))
                {
                    m_strServiceGUID = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("Author"))
                {
                    addAuthor(nodeValue);
                }
                if (nodeName.equalsIgnoreCase("CreationDate"))
                {
                    formatCreationDate(nodeValue);
                }
                if (nodeName.equalsIgnoreCase("Icon"))
                {
                    m_strIconName = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("LargeIcon"))
                {
                    m_strLargeIconName = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("Version"))
                {
                    m_dVersion = Float.parseFloat(nodeValue);
                }
                if (nodeName.equalsIgnoreCase("Label"))
                {
                    m_strLabel = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("CompatibleWith"))
                {
                    addCompatibleWith(nodeValue);
                }
                if (nodeName.equalsIgnoreCase("Category"))
                {
                    addCategory(nodeValue);
                }
                if (nodeName.equalsIgnoreCase("ShortDescription"))
                {
                    m_strShortDesc = nodeValue;
                }
                if (nodeName.equalsIgnoreCase("LongDescription"))
                {
                    m_strLongDesc = nodeValue;
                }
            }
        }
        //System.out.println("Component : " + m_strDispName + ", m_bLicensed = " + m_bLicensed);
        validate();
    }

    /**
     * Specifies whether the service is native
     *
     * @return
     */
    public boolean hasNative()
    {
        return m_bNative;
    }


    /**
     *  This interface method is called to add the specified string to the list of
     *  author names for the service, about which this is object of <code>ServiceHeader</code>.
     *
     * @param author The string to be added as author name of service
     * @see #removeAuthor(String)
     * @see #clearAuthors()
     * @see #getServiceAuthors()
     * @since Tifosi2.0
     */
    public void addAuthor(String author)
    {
        if (m_strAuthors == null)
        {
            m_strAuthors = new Vector();
        }
        m_strAuthors.add(author);
    }


    /**
     *  This interface method is called to remove the specified string from the list of
     *  author names for the service, about which this is object of <code>ServiceHeader</code>.
     *
     * @param author the name of the author to be removed from the list
     * @see #addAuthor(String)
     * @see #clearAuthors()
     * @see #getServiceAuthors()
     * @since Tifosi2.0
     */
    public void removeAuthor(String author)
    {
        if (m_strAuthors != null)
        {
            m_strAuthors.remove(author);
        }
    }


    /**
     *  This interface method is called to clear the list of author names for the
     *  service, about which this is object of <code>ServiceHeader</code>.
     *
     * @see #addAuthor(String)
     * @see #removeAuthor(String)
     * @see #getServiceAuthors()
     * @since Tifosi2.0
     */
    public void clearAuthors()
    {
        if (m_strAuthors != null)
        {
            m_strAuthors.clear();
        }
    }


    /**
     *  This interface method is called to add the specified string in the list of
     *  compatible with versions for the service about which, this is object of
     *  <code>ServiceHeader</code>.
     *
     * @param compatibleVersionInfo The string to be added to the list of
     *                               compatible versions for this service
     * @see #removeCompatibleWith(String)
     * @see #clearCompatibleWith()
     * @see #getCompatibleVersions()
     * @since Tifosi2.0
     */
    public void addCompatibleWith(String compatibleVersionInfo)
    {
        if (m_strCompatibleWith == null)
        {
            m_strCompatibleWith = new Vector();
        }
        m_strCompatibleWith.add(compatibleVersionInfo);
    }


    /**
     *  This interface method is called to remove the specified string from the list of
     *  compatible with versions for the service about which, this is object of
     *  <code>ServiceHeader</code>.
     *
     * @param version the version to be removed from the list
     * @see #addCompatibleWith(String)
     * @see #clearCompatibleWith()
     * @see #getCompatibleVersions()
     * @since Tifosi2.0
     */
    public void removeCompatibleWith(String version)
    {
        if (m_strCompatibleWith != null)
        {
            m_strCompatibleWith.remove(version);
        }
    }


    /**
     *  This interface is called to clear the list of compatible with versions for the
     *  service about which, this is object of <code>ServiceHeader</code>.
     *
     * @see #addCompatibleWith(String)
     * @see #removeCompatibleWith(String)
     * @see #getCompatibleVersions()
     * @since Tifosi2.0
     */
    public void clearCompatibleWith()
    {
        if (m_strCompatibleWith != null)
        {
            m_strCompatibleWith.clear();
        }
    }


    /**
     *  This interface method is called to add the specified string to the list of
     *  categories to which the service belongs, about which this is object of
     *  <code>ServiceHeader</code>.
     *
     * @param category the string to be added to the list of categories for this
     *                  service
     * @see #removeCategory(String)
     * @see #clearCategory()
     * @see #getCategories()
     * @since Tifosi2.0
     */
    public void addCategory(String category)
    {
        if (m_strCategoriesIn == null)
        {
            m_strCategoriesIn = new Vector();
        }
        if (!m_strCategoriesIn.contains(category))
            m_strCategoriesIn.add(category);
    }


    /**
     *  This interface method is called to remove the specified string from the list of
     *  categories to which the service belongs, about which this is object of
     *  <code>ServiceHeader</code>.
     *
     * @param category the category to be removed
     * @see #addCategory(String)
     * @see #clearCategory()
     * @see #getCategories()
     * @since Tifosi2.0
     */
    public void removeCategory(String category)
    {
        if (m_strCategoriesIn != null)
        {
            m_strCategoriesIn.remove(category);
        }
    }


    /**
     *  This interface method is called to clear the list of categories for the service
     *  about which, this is object of <code>ServiceHeader</code>.
     *
     * @see #addCategory(String)
     * @see #removeCategory(String)
     * @see #getCategories()
     * @since Tifosi2.0
     */
    public void clearCategory()
    {
        if (m_strCategoriesIn != null)
        {
            m_strCategoriesIn.clear();
        }
    }

    /**
     *  This method tests whether this object of <code>ServiceHeader</code> has
     *  the required(mandatory) fields set, before inserting values in the
     *  database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strDispName == null || m_strServiceGUID == null
            || m_strCreationDate == null
            || m_dVersion <= 0)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        if (m_strCategoriesIn == null || m_strCategoriesIn.size() <= 0)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        // Validate Date
        m_strCreationDate = m_strCreationDate.trim();

        // Dont throw exception. In case of error assign the current date.
        formatCreationDate(m_strCreationDate);

        /*try
        {
            CREATION_DATE_FORMAT.parse(m_strCreationDate);
        }
        catch (ParseException e)
        {
            throw new FioranoException("Creation Date Format Error");
        }*/
    }


    /**
     *  Resets the default values of this object.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_bIsErrorHandlingSupported = true;
        m_bLaunchable = true;
        m_bLicensed = false;
        m_bLaunchCPSinProcess = false;
        // By default service does not have native components
        //
        m_bNative = false;
        m_strAuthors = new Vector();
        m_strCategoriesIn = new Vector();
        m_strCompatibleWith = new Vector();
    }

    /**
     *  This utility method is used to get XML String representation for this
     *  object of <code>ServiceHeader</code>.
     *
     * @return XML String for this object
     * @exception FioranoException if the calls fails to succeed.
     * @since Tifosi2.0
     */
    public String toXMLString()
        throws FioranoException
    {
        com.fiorano.openesb.utils.DocumentFactoryImpl documentFactory =
            new DocumentFactoryImpl();
        Document document = documentFactory.createDocument();

        return XMLUtils.serializeDocument(toJXMLString(document));
    }

    /**
     *  This utility method is used to get the String representation of this
     *  object of <code>ServiceHeader</code>.
     *
     * @return The String representation of this object.
     * @since Tifosi2.0
     */
    public String toString()
    {
        try
        {
            return toXMLString();
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     *  This method is called to write this object of <code>ServiceHeader</code>
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

        out.writeBoolean(m_bLaunchable);
        out.writeBoolean(m_bLicensed);
        out.writeBoolean(m_bNative);

        writeUTF(out, m_strDispName);
        writeUTF(out, m_strServiceGUID);
        writeUTF(out, m_strCreationDate);
        writeUTF(out, m_strIconName);
        if (m_strLargeIconName == null)
            m_strLargeIconName = "";
        writeUTF(out, m_strLargeIconName);
        out.writeFloat(m_dVersion);
        out.writeBoolean(m_bIsErrorHandlingSupported);

        writeVector(m_strAuthors, out);
        writeVector(m_strCompatibleWith, out);
        writeVector(m_strCategoriesIn, out);

        writeUTF(out, m_strShortDesc);
        writeUTF(out, m_strLongDesc);
        if (m_lastUpdatedDate != null)
            out.writeLong(m_lastUpdatedDate.getTime());
        else
            out.writeLong(-1);

        //  write label
        UTFReaderWriter.writeUTF(out, m_strLabel);
        out.writeBoolean(m_bLaunchCPSinProcess);
    }


    /**
     *  This is called to read this object <code>ServiceHeader</code> from the
     *  specified object of input stream.
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

        m_bLaunchable = is.readBoolean();
        m_bLicensed = is.readBoolean();
        m_bNative = is.readBoolean();

        m_strDispName = readUTF(is);
        m_strServiceGUID = readUTF(is);
        formatCreationDate(readUTF(is));
        m_strIconName = readUTF(is);
        m_strLargeIconName = readUTF(is);
        m_dVersion = is.readFloat();
        m_bIsErrorHandlingSupported = is.readBoolean();

        readVector(m_strAuthors, is);
        readVector(m_strCompatibleWith, is);
        readVector(m_strCategoriesIn, is);

        m_strShortDesc = readUTF(is);
        m_strLongDesc = readUTF(is);

        long date = is.readLong();

        if (date > 0)
            m_lastUpdatedDate = new Date(date);

        //  Read Label
        m_strLabel = UTFReaderWriter.readUTF(is);
        //Karthik - moved the launchCPS writing part to the end
        try
        {
            m_bLaunchCPSinProcess = is.readBoolean();
        }
        catch (EOFException eofex)
        {
            m_bLaunchCPSinProcess = false;
        }
    }


    /**
     *  This utility method is used to compare this object of <code>ServiceHeader</code>
     *  with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof ServiceHeader))
        {
            return false;
        }

        ServiceHeader rcvObj = (ServiceHeader) obj;

        if (rcvObj.getCategories().equals(getCategories())
            && rcvObj.getCompatibleVersions().equals(getCompatibleVersions())
            && DmiEqualsUtil.checkStringEquality(rcvObj.getCreationDate(), m_strCreationDate)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getDisplayName(), m_strDispName)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getIcon(), m_strIconName)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getLargeIcon(), m_strLargeIconName)
            && rcvObj.getServiceAuthors().equals(getServiceAuthors())
            && DmiEqualsUtil.checkStringEquality(rcvObj.getServiceGUID(), m_strServiceGUID)
            && rcvObj.getVersion() == m_dVersion)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     *  Retruns the xml string equivalent of this object
     *
     * @param document instance of Xml Document.
     * @return org.w3c.dom.Node
     * @exception FioranoException thrown in case of error.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("ServiceHeader");

        ((Element) root0).setAttribute("launchable", "" + isLaunchable());
        ((Element) root0).setAttribute("isLicensed", "" + isLicensed());
        ((Element) root0).setAttribute("native", "" + hasNative());
        ((Element) root0).setAttribute("canLaunchCPSInProcess", "" + isCPSLaunchableInmemory());
        ((Element) root0).setAttribute("isErrorHandlingSupported",
            "" + isErrorHandlingSupported());

        if (m_lastUpdatedDate != null)
        {
            String date = null;

            try
            {
                //date = m_dateFormat.format(m_lastUpdatedDate);
                //date = m_dateFormat1.format(m_lastUpdatedDate);
                date = m_dateFormat2.format(m_lastUpdatedDate);
            }
            catch (Exception e)
            {
            }
            if (date != null)
                ((Element) root0).setAttribute("lastUpdatedAt", date);
        }

        Node child = null;

        child = XMLDmiUtil.getNodeObject("Name", m_strDispName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("ServiceGUID", m_strServiceGUID, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        // adding authors
        XMLDmiUtil.addVectorValues("Author", m_strAuthors, document, root0);

        child = XMLDmiUtil.getNodeObject("CreationDate", m_strCreationDate, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("Icon", m_strIconName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("LargeIcon", m_strLargeIconName, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("Version", m_dVersion + "", document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        //adding labels
        //create label object
        child = XMLDmiUtil.getNodeObject("Label", m_strLabel, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        // adding compatibility versions
        XMLDmiUtil.addVectorValues("CompatibleWith", m_strCompatibleWith, document, root0);

        // adding categories
        XMLDmiUtil.addVectorValues("Category", m_strCategoriesIn, document, root0);

        child = XMLDmiUtil.getNodeObject("ShortDescription", m_strShortDesc, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("LongDescription", m_strLongDesc, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        return root0;
    }


    /**
     *  Writes given vector to DataOutput stream
     *
     * @param strVector Vector containing Service Headers information.
     * @param out Data output stream.
     * @exception IOException thrown in case of error writing data to stream.
     */
    private void writeVector(Vector strVector, DataOutput out)
        throws IOException
    {
        if (strVector != null && strVector.size() > 0)
        {
            int num = strVector.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                writeUTF(out, (String) strVector.elementAt(i));
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  reads Vector from DataInputStream
     *
     * @param strVector Vector containing Service Header Information.
     * @param is Data Input Stream.
     * @exception IOException thrown in case of error.
     */
    private void readVector(Vector strVector, DataInput is)
        throws IOException
    {
        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            String name = readUTF(is);

            strVector.addElement(name);
        }
    }

    private void formatLastUpdateDate(String dateAsString)
    {
        try{
            if(dateAsString == null)
                    dateAsString = "";
            //for backward compatibility format with old-format.
            m_lastUpdatedDate = m_dateFormat.parse(dateAsString);
        }catch(Exception ex)
        {
            // Format with new-format.
            try{
                m_lastUpdatedDate = m_dateFormat2.parse(dateAsString);
            }
            catch(Exception ex1)
            {
                try{
                    m_lastUpdatedDate = m_dateFormat1.parse(dateAsString);
                }catch(Exception ex2)
                {
                    //Set the currentDate as currentDate.
                    m_lastUpdatedDate = new Date(System.currentTimeMillis());
                }
            }
        }
    }
    private void formatCreationDate(String dateAsString)
    {
        try{
            if(dateAsString == null)
                dateAsString = "";
            m_creationDate = CREATION_DATE_FORMAT.parse(dateAsString);
        }catch(Exception ex)
        {
            //In case of error, set the current date as creationdate
            m_creationDate = new Date(System.currentTimeMillis());
        }
        m_strCreationDate = CREATION_DATE_FORMAT.format(m_creationDate);
    }

}

