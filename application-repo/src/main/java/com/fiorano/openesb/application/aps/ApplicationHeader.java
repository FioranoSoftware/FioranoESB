/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationHeader extends DmiObject
{
    public static final SimpleDateFormat CREATION_DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
    public static final SimpleDateFormat CREATION_DATE_FORMAT1 = new SimpleDateFormat("dd-MM-yyyy");

    /**
     *  Description of the Field
     */
    public final static String PARAM_SERVICE_NO_CACHE = "ServiceNoCache";

    //  boolean to indicate if the app can be used as a subgraph.
    boolean         m_canBeSubGraphed;

    //  Scope of the app...it can be either NETWORK or MULTIDOMAIN
    String          m_scope;

    //  the name of the app
    String          m_appName;

    //  GUID of the app
    String          m_appGUID;

    //  Date on which it was created. Format dd-Mon-YYY. 20-sep-2006.
    String          m_creationDate;

    //  Date on which it was created.
    Date            m_creationDateAsDate;


    //  Category of the app e.g. EAISubFlow.
    String          m_category;

    //  Author name.
    Vector          m_authorNames = new Vector();

    // icon name
    String          m_iconName;

    // short description
    String          m_shortDescription;

    // long description
    String          m_longDescription;

    // stores versions, this service is compatible with
    Vector          m_compatibleWith = new Vector();

    // max no of instances
    int             m_maxInstLimit;

    // hash of params
    //Hashtable m_params = new Hashtable();

    Vector          m_params = new Vector();

    String          m_version;

    boolean         m_isVersionLocked;

    //  Profile Label of the application.
    private String  m_strProfile;

    private ApplicationContext m_appContext;

    /**
     *  This is the constructor of the <code>ApplicationHeader</code> class.
     *
     * @since Tifosi2.0
     */
    public ApplicationHeader()
    {
        m_scope = "NETWORK";
        m_canBeSubGraphed = true;
    }


    /**
     *  This method obtains the scope of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return The scope value
     * @see #setScope(String)
     * @since Tifosi2.0
     * @deprecated
     */
    public String getScope()
    {
        return m_scope;
    }


    /**
     * Gets the context of the application from this <code>ApplicationHeader</code>
     * object.
     *
     * @return Application context for this application.
     * @since Tifosi2.0
     */
    public ApplicationContext getApplicationContext()
    {
        return m_appContext;
    }


    /**
     *  Gets name of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return name of the application
     * @see #setApplicationName(String)
     * @since Tifosi2.0
     */
    public String getApplicationName()
    {
        return m_appName;
    }


    /**
     *  Gets the application GUID from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return the application GUID
     * @see #setApplicationGUID(String)
     * @since Tifosi2.0
     */
    public String getApplicationGUID()
    {
        return m_appGUID;
    }


    /**
     *  Gets all the authors of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return enumeration of authors of the application.
     * @see #addAuthor(String)
     * @since Tifosi2.0
     */
    public Enumeration getAuthors()
    {
        return m_authorNames.elements();
    }


    /**
     *  Gets the date of creation of the application from this <code>ApplicationHeader</code>
     *  object.
     * The date is in dd-Mon-YYYY (20-Sep-2006)
     *
     * @return The creation Date of application.
     * @see #setCreationDate(String)
     * @since Tifosi2.0
     */
    public String getCreationDate()
    {
        return m_creationDate;
    }

    /**
     *  Gets the date of creation of the application from this <code>ApplicationHeader</code>
     *  object.
     * @return The creation Date of application.
     */
    public Date getCreationDateAsDate()
    {
        return m_creationDateAsDate;
    }


    /**
     *  Gets the category of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return The category of application
     * @see #setCategoryIn(String)
     * @since Tifosi2.0
     */
    public String getCategoryIn()
    {
        return m_category;
    }


    /**
     *  Gets all the compatible versions for this version of application, from
     *  this <code>ApplicationHeader</code> object.
     *
     * @return Enumeration of all compatible versions with this version of
     *      application.
     * @see #addCompatibleWith(String)
     * @since Tifosi2.0
     */
    public Enumeration getCompatibleVersions()
    {
        return m_compatibleWith.elements();
    }


    /**
     *  Gets the icon name of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return The icon name
     * @see #setIcon(String)
     * @since Tifosi2.0
     */
    public String getIcon()
    {
        return m_iconName;
    }


    /**
     *  Gets the long description of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return Long description about the application
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_longDescription;
    }


    /**
     *  Gets the short description of the application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return short description about the application
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_shortDescription;
    }

    /**
     *  This method returns the label name associated with the application
     *
     * @return the label name associated with the service
     * @see #setProfile(String)
     * @since Tifosi 2.2
     */

    public String getProfile()
    {
        return m_strProfile;
    }


    /**
     *  Gets all the extra parameters of application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return Vector of all extra parameters.
     * @see #addParam(Param)
     * @since Tifosi2.0
     */
    public Vector getParams()
    {
        return m_params;
    }


    /**
     *  Gets the version number of application from this <code>ApplicationHeader</code>
     *  object.
     *
     * @return Version number of application
     * @see #setVersionNumber(String)
     * @since Tifosi2.0
     */
    public String getVersionNumber()
    {
        return m_version;
    }


    /**
     *  Gets whether this version of application is locked or not, from this
     *  <code>ApplicationHeader</code> object.
     *
     * @return boolean specifying whether the version is locked or not
     * @see #setIsVersionLocked(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean getIsVersionLocked()
    {
        return m_isVersionLocked;
    }

    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APPLICATION_HEADER;
    }


    /**
     *  This method is not supported in the current version of Tifosi. (Sets the
     *  attribute specifying, if the application can be graphed or not, in the
     *  <code>ApplicationHeader</code> object.)
     *
     * @param canBeSubGraphed The new canBeSubGraphed value
     * @see #canBeSubGraphed()
     * @since Tifosi2.0
     */
    public void setCanBeSubGraphed(boolean canBeSubGraphed)
    {
        m_canBeSubGraphed = canBeSubGraphed;
    }


    /**
     *  Sets the scope of the application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param scope the application scope
     * @see #getScope()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setScope(String scope)
    {
        m_scope = scope;
    }


    /**
     *  Sets the context of the application
     *
     * @param appContext
     * @since Tifosi2.0
     */
    public void setApplicationContext(ApplicationContext appContext)
    {
        m_appContext = appContext;
    }


    /**
     *  Sets name of the application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param name the name of application
     * @see #getApplicationName()
     * @since Tifosi2.0
     */
    public void setApplicationName(String name)
    {
        m_appName = name;
    }


    /**
     *  Sets application GUID in this <code>ApplicationHeader</code> object.
     *
     * @param appGUID The GUID of application
     * @see #getApplicationGUID()
     * @since Tifosi2.0
     */
    public void setApplicationGUID(String appGUID)
    {
        if (appGUID != null)
            m_appGUID = appGUID.toUpperCase();
    }


    /**
     *  Sets the creation date of the application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param date the creation Date of application
     * @see #getCreationDate()
     * @since Tifosi2.0
     * @jmx.descriptor name="legalValues" value=ApplicationHeader.CREATION_DATE_FORMAT
     * @jmx.descriptor name="canEditAsText" value="true"
     */
    public void setCreationDate(String date)
    {
        m_creationDate = date;
    }

    /**
     *  This method sets the creation date of the application
     *
     * @param creationDate - The date to be set
     */
    public void setCreationDateAsDate(Date creationDate)
    {
        m_creationDateAsDate = creationDate;
    }


    /**
     *  Sets the category of the application in this <code>ApplicationHeader</code>
     *
     * @param category The Category to which the application belongs.
     * @see #getCategoryIn()
     * @since Tifosi2.0
     */
    public void setCategoryIn(String category)
    {
        m_category = category;
    }


    /**
     *  Sets an icon for the application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param iconName The icon name to be set
     * @see #getIcon()
     * @since Tifosi2.0
     */
    public void setIcon(String iconName)
    {
        m_iconName = iconName;
    }

    /**
     *  Sets the specified string as long description of the application in this
     *  <code>ApplicationHeader</code> object.
     *
     * @param longDescription Long description about the application
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String longDescription)
    {
        m_longDescription = longDescription;
    }


    /**
     *  Sets the specified string as short description of the application in
     *  this <code>ApplicationHeader</code> object.
     *
     * @param shortDescription Short description about the application
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String shortDescription)
    {
        m_shortDescription = shortDescription;
    }

    /**
     *  This method sets the profile of the application
     *
     * @param profile - The profile to be set
     * @see #getProfile()
     * @since Tifosi 2.2
     */

    public void setProfile(String profile)
    {
        m_strProfile = profile;
    }


    /**
     *  Sets the version number of application to this <code>ApplicationHeader </code>
     *  object.
     *
     * @param version Version number of application
     * @see #getVersionNumber()
     * @since Tifosi2.0
     */
    public void setVersionNumber(String version)
    {
        m_version = version;
    }


    /**
     *  Sets whether this version of application is locked or not, in this
     *  <code>ApplicationHeader</code> object.
     *
     * @param isVersionLocked boolean specifying whether the version is locked
     *      or not
     * @see #getIsVersionLocked()
     * @since Tifosi2.0
     */
    public void setIsVersionLocked(boolean isVersionLocked)
    {
        m_isVersionLocked = isVersionLocked;
    }

    /**
     *  Sets all the fieldValues of this <code>ApplicationHeader</code> object
     *  using the specified XML string.
     *
     * @param appHeaderElement The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element appHeaderElement)
        throws FioranoException
    {
        if (appHeaderElement != null)
        {
            boolean canSub = XMLDmiUtil.getAttributeAsBoolean(appHeaderElement, "isSubgraphable");

            setCanBeSubGraphed(canSub);

            String scope = appHeaderElement.getAttribute("Scope");

            setScope(scope);

            NodeList childList = appHeaderElement.getChildNodes();
            Node appchild;

            for (int i = 0; i < childList.getLength(); i++)
            {
                appchild = childList.item(i);
                if (appchild.getNodeType() == Node.TEXT_NODE)
                    continue;

                String nodeName = appchild.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(appchild);

                if (nodeName.equalsIgnoreCase("Name"))
                    setApplicationName(nodeValue);

                else if (nodeName.equalsIgnoreCase("ApplicationGUID"))
                    setApplicationGUID(nodeValue);

                else if (nodeName.equalsIgnoreCase("Author"))
                    addAuthor(nodeValue);

                else if (nodeName.equalsIgnoreCase("CreationDate"))
                    setCreationDate(nodeValue);

                else if (nodeName.equalsIgnoreCase("Icon"))
                    setIcon(nodeValue);

                else if (nodeName.equalsIgnoreCase("Version"))
                {
                    boolean isLocked = XMLDmiUtil.getAttributeAsBoolean
                        (((Element) appchild), "isLocked");

                    setIsVersionLocked(isLocked);
                    setVersionNumber(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("Label"))
                    m_strProfile = nodeValue;

                else if (nodeName.equalsIgnoreCase("CompatibleWith"))
                    addCompatibleWith(nodeValue);

                else if (nodeName.equalsIgnoreCase("Category"))
                    setCategoryIn(nodeValue);

                else if (nodeName.equalsIgnoreCase("LongDescription"))
                    setLongDescription(nodeValue);

                else if (nodeName.equalsIgnoreCase("ShortDescription"))
                    setShortDescription(nodeValue);

                else if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param param = new Param();

                    param.setFieldValues((Element) appchild);

                    m_params.add(param);
                }
                else if (nodeName.equalsIgnoreCase("ApplicationContext"))
                {
                    m_appContext = new ApplicationContext();
                    m_appContext.setFieldValues((Element) appchild);
                }
            }
        }
        validate();
    }


    /**
     *  Sets all the fieldValues of this <code>ApplicationHeader</code> object
     *  from the XML using STAX parser.
     */
    public void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{

        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if(cursor.markCursor(APSConstants.APPLICATION_HEADER)){

            // Get Attributes. This MUST be done before accessing any data of element.
            Hashtable attributes = cursor.getAttributes();
            if(attributes!=null && attributes.containsKey(APSConstants.ATTR_IS_SUBGRAPHABLE)){
                boolean canSub = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.ATTR_IS_SUBGRAPHABLE));
                setCanBeSubGraphed(canSub);
            }
            if(attributes!=null && attributes.containsKey(APSConstants.ATTR_SCOPE)){
                String scope = (String)attributes.get(APSConstants.ATTR_SCOPE);
                setScope(scope);
            }

            //Get associated Data
            //String nodeVal = cursor.getText();

            // Get Child Elements
            while(cursor.nextElement()){
                String nodeName = cursor.getLocalName();

                // Get Attributes
                attributes = cursor.getAttributes();

                //String nodeValue = cursor.getText();
                //if (nodeValue == null)
                //	continue;

                if(nodeName.equalsIgnoreCase("Name")){
                    String nodeValue = cursor.getText();
                    setApplicationName(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("ApplicationGUID")){
                    String nodeValue = cursor.getText();
                    setApplicationGUID(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("Author")){
                    String nodeValue = cursor.getText();
                    addAuthor(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("CreationDate")){
                    String nodeValue = cursor.getText();
                    setCreationDate(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("Icon")){
                    String nodeValue = cursor.getText();
                    setIcon(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("Version")){
                    String nodeValue = cursor.getText();
                    if(attributes!=null){
                        boolean isLocked = XMLUtils.getStringAsBoolean((String)attributes.get("isLocked"));
                        setIsVersionLocked(isLocked);
                        setVersionNumber(nodeValue);
                    }
                } else if(nodeName.equalsIgnoreCase("Label")){
                    m_strProfile = cursor.getText();
                }

                else if(nodeName.equalsIgnoreCase("CompatibleWith")){
                    String nodeValue = cursor.getText();
                    addCompatibleWith(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("Category")){
                    String nodeValue = cursor.getText();
                    setCategoryIn(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("LongDescription")){
                    String nodeValue = cursor.getText();
                    setLongDescription(nodeValue);
                }

                else if(nodeName.equalsIgnoreCase("ShortDescription")){
                    String nodeValue = cursor.getText();
                    setShortDescription(nodeValue);
                }

                // LOOK AT THIS. SHOULD PASS THIS TO BE HANDLED BY PARAM DMI
                else if(nodeName.equalsIgnoreCase("Param")){
                    String nodeValue = cursor.getText();
                    Param param = new Param();
                    param.setParamName((String)attributes.get(APSConstants.PARAM_NAME));
                    param.setParamValue(nodeValue);

                    //changed by Sandeep M
                    /*
                    param.setFieldValues(cursor);
                    */
                    /*
                         String name = (String)attributes.get("name");
                         param.setParamValue(nodeValue);

                         param.populate(cursor);
                         */
                    m_params.add(param);
                } else if(nodeName.equalsIgnoreCase("ApplicationContext")){
                    m_appContext = new ApplicationContext();
                    m_appContext.setFieldValues(cursor);
                }
            }
            validate();
        } else
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
    }


    /**
     *  This method is not supported in the current version of Tifosi. (Gets a
     *  boolean indicating whether the application can be graphed or not, from
     *  the <code>ApplicationHeader</code> object.)
     *
     * @return true if the application can be sub-graphed
     * @see #setCanBeSubGraphed(boolean)
     * @since Tifosi2.0
     */
    public boolean canBeSubGraphed()
    {
        return m_canBeSubGraphed;
    }


    /**
     *  This method adds the authors of the application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param author name of the author
     * @see #getAuthors()
     * @since Tifosi2.0
     */
    public void addAuthor(String author)
    {
        m_authorNames.add(author);
    }


    /**
     *  This method adds version, with which this version of application is
     *  compatible, in this <code>ApplicationHeader</code> object.
     *
     * @param compatibleVersionInfo compatible version
     * @see #getCompatibleVersions()
     * @since Tifosi2.0
     */
    public void addCompatibleWith(String compatibleVersionInfo)
    {
        m_compatibleWith.add(compatibleVersionInfo);
    }


    /**
     *  This method adds extra parameter of this application in this <code>ApplicationHeader</code>
     *  object.
     *
     * @param param Param object
     * @see #getParams()
     * @see com.fiorano.openesb.application.common.Param
     * @since Tifosi2.0
     */
    public void addParam(Param param)
    {
        m_params.add(param);
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
     *  This method tests whether this <code>ApplicationHeader</code> object has
     *  the required(mandatory) fields set. It should be invoked before
     *  inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_appName == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_appGUID == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        if (m_version == null)
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);

        Enumeration enums = m_params.elements();

        while (enums.hasMoreElements())
        {
            Param param = (Param) enums.nextElement();

            param.validate();
        }

        if (m_appContext != null)
            m_appContext.validate();

        // Validate Date
        m_creationDate = m_creationDate.trim();

        formatStringAsDate(m_creationDate);

		//Removing this check.
        /* try {
            CREATION_DATE_FORMAT.parse(m_creationDate);
        } catch (ParseException e) {
            throw new FioranoException("Creation Date Format Error");
        }  */
    }

    /**
     *  This method writes this <code>ApplicationHeader</code> object to the
     *  specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     * writing it to a binary stream.
     * @see #fromStream(DataInput, int)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeBoolean(m_canBeSubGraphed);

        if (m_scope != null)
            UTFReaderWriter.writeUTF(out, m_scope);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_appName != null)
            UTFReaderWriter.writeUTF(out, m_appName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_appGUID != null)
            UTFReaderWriter.writeUTF(out, m_appGUID);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_creationDate != null)
            UTFReaderWriter.writeUTF(out, m_creationDate);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_category != null)
            UTFReaderWriter.writeUTF(out, m_category);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_authorNames != null)
        {
            int length = m_authorNames.size();

            out.writeInt(length);

            Enumeration names = m_authorNames.elements();

            while (names.hasMoreElements())
            {
                String name = (String) names.nextElement();

                UTFReaderWriter.writeUTF(out, name);
            }
        }
        else
            out.writeInt(0);

        if (m_iconName != null)
            UTFReaderWriter.writeUTF(out, m_iconName);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_shortDescription != null)
            UTFReaderWriter.writeUTF(out, m_shortDescription);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_longDescription != null)
            UTFReaderWriter.writeUTF(out, m_longDescription);
        else
            UTFReaderWriter.writeUTF(out, "");

        if (m_compatibleWith != null)
        {
            int length = m_compatibleWith.size();

            out.writeInt(length);

            Enumeration names = m_compatibleWith.elements();

            while (names.hasMoreElements())
            {
                String name = (String) names.nextElement();

                UTFReaderWriter.writeUTF(out, name);
            }
        }
        else
            out.writeInt(0);

        out.writeInt(m_maxInstLimit);

        if (m_params != null)
        {
            int length = m_params.size();

            out.writeInt(length);

            Enumeration params = m_params.elements();

            while (params.hasMoreElements())
            {
                Param param = (Param) params.nextElement();

                param.toStream(out, versionNo);
            }
        }
        else
            out.writeInt(0);

        if (m_version != null)
            UTFReaderWriter.writeUTF(out, m_version);
        else
            UTFReaderWriter.writeUTF(out, "");

        out.writeBoolean(m_isVersionLocked);

        UTFReaderWriter.writeUTF(out, m_strProfile);

        if (m_appContext != null)
        {
            out.writeInt(1);
            m_appContext.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This method reads this <code>ApplicationHeaders</code> object from the
     *  specified input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_canBeSubGraphed = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
            m_scope = null;

        else
            m_scope = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_appName = null;

        else
            m_appName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_appGUID = null;

        else
            setApplicationGUID(temp);

        temp = UTFReaderWriter.readUTF(is);

        if (temp == null || temp.trim().equals(""))
            formatStringAsDate("");
        else
            formatStringAsDate(temp);


        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_category = null;

        else
            m_category = temp;

        int tempInt;

        if ((tempInt = is.readInt()) != 0)
            for (int i = 0; i < tempInt; i++)
            {
                String name = UTFReaderWriter.readUTF(is);

                m_authorNames.add(name);
            }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_iconName = null;

        else
            m_iconName = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_shortDescription = null;

        else
            m_shortDescription = temp;

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_longDescription = null;

        else
            m_longDescription = temp;

        if ((tempInt = is.readInt()) != 0)
            for (int i = 0; i < tempInt; i++)
            {
                String name = UTFReaderWriter.readUTF(is);

                m_compatibleWith.add(name);
            }

        m_maxInstLimit = is.readInt();

        if ((tempInt = is.readInt()) != 0)
            for (int i = 0; i < tempInt; i++)
            {
                Param param = new Param();

                param.fromStream(is, versionNo);
                m_params.add(param);
            }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
            m_version = null;

        else
            m_version = temp;

        m_isVersionLocked = is.readBoolean();

        m_strProfile = UTFReaderWriter.readUTF(is);

        int result = is.readInt();

        if (result == 1)
        {
            m_appContext = new ApplicationContext();
            m_appContext.fromStream(is, versionNo);
        }
    }

    /**
     *  This utility method is used to get the String representation of this
     *  <code>ApplicationHeader</code> object.
     *
     * @return The String representation of this object.
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Application Header Details ");
        strBuf.append("[");
        strBuf.append("Application subgraph = ");
        strBuf.append(m_canBeSubGraphed);
        strBuf.append(", ");
        strBuf.append("Application Scope= ");
        strBuf.append(m_scope);
        strBuf.append(", ");
        strBuf.append("Application Name= ");
        strBuf.append(m_appName);
        strBuf.append(", ");
        strBuf.append("Application GUID= ");
        strBuf.append(m_appGUID);
        strBuf.append(", ");
        if (m_strProfile != null)
        {
            strBuf.append("Application Label = ");
            strBuf.append(m_strProfile);
            strBuf.append(",");
        }
        strBuf.append("Creation Date = ");
        strBuf.append(m_creationDate);
        strBuf.append(", ");
        strBuf.append("Application Category = ");
        strBuf.append(m_category);
        strBuf.append(", ");

        if (m_authorNames != null)
        {
            strBuf.append("Author Names = ");
            for (int i = 0; i < m_authorNames.size(); i++)
            {
                strBuf.append(i + 1).append(". ");
                strBuf.append(m_authorNames.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("Icon Name = ");
        strBuf.append(m_iconName);
        strBuf.append(", ");
        strBuf.append("Short Description = ");
        strBuf.append(m_shortDescription);
        strBuf.append(", ");
        strBuf.append("Long Description = ");
        strBuf.append(m_longDescription);
        strBuf.append(", ");
        strBuf.append("Version = ");
        strBuf.append(m_version);
        strBuf.append(", ");
        if (m_compatibleWith != null)
        {
            strBuf.append("Compatible Versions = ");
            for (int i = 0; i < m_compatibleWith.size(); i++)
            {
                strBuf.append(i + 1).append(". ");
                strBuf.append(m_compatibleWith.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("Version Locked = ");
        strBuf.append(m_isVersionLocked);

        if (m_appContext != null)
        {
            strBuf.append("Application Context = ");
            strBuf.append(m_appContext.toString());
            strBuf.append(", ");
        }

        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     * @since Tifosi2.0
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("ApplicationHeader");

        ((Element) root0).setAttribute("isSubgraphable", "" + m_canBeSubGraphed);
        ((Element) root0).setAttribute("Scope", m_scope);

        Node node;

        node = XMLDmiUtil.getNodeObject("Name", m_appName, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("ApplicationGUID", m_appGUID, document);
        if (node != null)
            root0.appendChild(node);

        XMLDmiUtil.addVectorValues("Author", m_authorNames, document, root0);

        node = XMLDmiUtil.getNodeObject("CreationDate", m_creationDate, document);
        if (node != null)
            root0.appendChild(node);

        if(!StringUtils.isEmpty(m_iconName)){
            node = XMLDmiUtil.getNodeObject("Icon", m_iconName, document);
            if (node != null)
                root0.appendChild(node);
        }
        Element child;

        if (m_version != null)
        {
            child = document.createElement("Version");
            child.setAttribute("isLocked", "" + m_isVersionLocked);

            Node pcData = document.createTextNode(m_version);

            child.appendChild(pcData);
            root0.appendChild(child);
        }

        //adding labels

        //create label object
        node = XMLDmiUtil.getNodeObject("Label", m_strProfile, document);
        if (node != null)
            root0.appendChild(node);

        //        node = XMLDmiUtil.getNodeObject("CompatibleWith", "" + m_compatibleWith, document);
        //        root0.appendChild(node);
        XMLDmiUtil.addVectorValues("CompatibleWith", m_compatibleWith, document, root0);

        node = XMLDmiUtil.getNodeObject("Category", m_category, document);
        if (node != null)
            root0.appendChild(node);

        if (m_maxInstLimit == -1 || m_maxInstLimit > 0)
        {
            child = document.createElement("MaximumInstances");

            Node pcData = document.createTextNode("" + m_maxInstLimit);

            child.appendChild(pcData);
            root0.appendChild(child);
        }
        if(!StringUtils.isEmpty(m_longDescription)){
            node = XMLDmiUtil.getNodeObject("LongDescription", m_longDescription, document);
            if (node != null)
                root0.appendChild(node);
        }
        if(!StringUtils.isEmpty(m_shortDescription)){
            node = XMLDmiUtil.getNodeObject("ShortDescription", m_shortDescription, document);
            if (node != null)
                root0.appendChild(node);
        }
        if (m_params != null && m_params.size() > 0)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    root0.appendChild(param.toJXMLString(document));
            }
        }

        //  Add the ApplicationHeader Node to it.
        if (m_appContext != null)
        {
            Node contextNode = m_appContext.toJXMLString(document);

            root0.appendChild(contextNode);
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Application Header
        writer.writeStartElement("ApplicationHeader");
        writer.writeAttribute("isSubgraphable", "" + m_canBeSubGraphed);
        writer.writeAttribute("Scope", m_scope);

        //Write Name
        FioranoStackSerializer.writeElement("Name", m_appName, writer);

        //Write ApplicationGUID
        FioranoStackSerializer.writeElement("ApplicationGUID", m_appGUID, writer);

        //Write Auther Vector
        FioranoStackSerializer.writeVector("Author", m_authorNames, writer);

        //Write Creation Date
        FioranoStackSerializer.writeElement("CreationDate", m_creationDate, writer);

        //Write Icon
        if(!StringUtils.isEmpty(m_iconName)){
            FioranoStackSerializer.writeElement("Icon", m_iconName, writer);
        }

        //Write Version
        if (m_version != null)
        {
            writer.writeStartElement("Version");
            writer.writeAttribute("isLocked", "" + m_isVersionLocked);
            writer.writeCharacters(m_version);
            writer.writeEndElement();
        }

        //Write Label
        FioranoStackSerializer.writeElement("Label", m_strProfile, writer);

        //Write CompatibleWith
        FioranoStackSerializer.writeVector("CompatibleWith", m_compatibleWith, writer);

        //Write Category
        FioranoStackSerializer.writeElement("Category", m_category, writer);

        //Write Maximum Instances
        if (m_maxInstLimit == -1 || m_maxInstLimit > 0)
        {
            writer.writeStartElement("MaximumInstances");
            writer.writeCharacters("" + m_maxInstLimit);
            writer.writeEndElement();
        }

        //Write Long Description
        if(!StringUtils.isEmpty(m_longDescription)){
            FioranoStackSerializer.writeElement("LongDescription", m_longDescription, writer);
        }

        //Write Short Description
        if(!StringUtils.isEmpty(m_shortDescription)){
            FioranoStackSerializer.writeElement("ShortDescription", m_shortDescription, writer);
        }

        //Write mParams
        if (m_params != null && m_params.size() > 0){
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    param.toJXMLString(writer);
            }

        }

        // Write AppContext
        if (m_appContext != null)
        {
            m_appContext.toJXMLString(writer);
        }

        //End Application Header
        writer.writeEndElement();


    }

    /**
     * Sets is service no cache for object
     * @param isServiceNoCache
     */
    public void setIsServiceNoCache(boolean isServiceNoCache){
        Param.setParamValue(m_params, PARAM_SERVICE_NO_CACHE, isServiceNoCache);
    }

    /**
     *
     * @return boolean specifying if all service instances have cache enabled
     */
    public boolean isServiceNoCache(){
        return Param.getParamValueAsBoolean(m_params, PARAM_SERVICE_NO_CACHE);
    }

    private void formatStringAsDate(String dateAsString)
    {
        try{
            //for backward compatibility format with MMM-format.
            m_creationDateAsDate = CREATION_DATE_FORMAT.parse(dateAsString);
        }catch(Exception ex)
        {
            // Format with MM-format.
            try{
                m_creationDateAsDate = CREATION_DATE_FORMAT1.parse(dateAsString);
            } catch(Exception ex1)
            {
                //Set the currentDate as currentDate.
                m_creationDateAsDate = new Date(System.currentTimeMillis());
            }
        }

        //Set string format of date according to MM-format.
        m_creationDate = CREATION_DATE_FORMAT1.format(m_creationDateAsDate);
    }
}
