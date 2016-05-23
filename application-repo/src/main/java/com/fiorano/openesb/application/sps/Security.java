/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
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
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Security extends DmiObject
{
    Certificate     m_certificate;

    Vector          m_certifiedActivities = new Vector();


    /**
     *  Constructor for the Security object
     *
     * @since Tifosi2.0
     */
    public Security()
    {
        reset();
    }


    /**
     * This interface method is called to get the object of
     * <code>Certificate</code> form this object of <code>Security</code>.
     *
     * @return object of Certificate
     * @see #setCertificate(Certificate)
     * @since Tifosi2.0
     */
    public Certificate getCertificate()
    {
        return m_certificate;
    }


    /**
     *  This interface method is called to get enumeration of list of CertifiedActivities
     *  for this object of <code>Security</code>.
     *
     * @return Enumeration of list of certifiedActivities
     * @see #addCertifiedActivity(String)
     * @see #removeCertifiedActivity(String)
     * @see #clearCertifiedActivity()
     * @since Tifosi2.0
     */
    public Enumeration getCertifiedActivities()
    {
        if (m_certifiedActivities == null)
        {
            m_certifiedActivities = new Vector();
        }
        return m_certifiedActivities.elements();
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.SECURITY;
    }


    /**
     *  This interface method is called to set the specified object
     *  of <code>Certificate</code> for this object of <code>Security</code>.
     *
     * @param cert object of Certificate to be set
     * @see #getCertificate()
     * @since Tifosi2.0
     */
    public void setCertificate(Certificate cert)
    {
        m_certificate = cert;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>Security</code>, using the specified XML string.
     *
     * @param security
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element security)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element security = doc.getDocumentElement();

        if (security != null)
        {
            NodeList children = security.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Certificate"))
                {
                    Certificate cert = new Certificate();

                    cert.setFieldValues((Element) child);
                    m_certificate = cert;
                }

                if (nodeName.equalsIgnoreCase("CertifiedActivity"))
                {
                    addCertifiedActivity(XMLUtils.getNodeValueAsString(child));
                }
            }
        }
        validate();
    }


    /**
     *  This is called to add the specified string to the list of CertifiedActivities,
     *  for this object of <code>Security</code>.
     *
     * @param activity the certified activity to be added to the list of
     *                      CertifiedActivities
     * @see #removeCertifiedActivity(String)
     * @see #clearCertifiedActivity()
     * @see #getCertifiedActivities()
     * @since Tifosi2.0
     */
    public void addCertifiedActivity(String activity)
    {
        if (m_certifiedActivities == null)
        {
            m_certifiedActivities = new Vector();
        }
        m_certifiedActivities.add(activity);
    }


    /**
     *  This is called to remove the specified string from the list of CertifiedActivities
     *  for this object of <code>Security</code>.
     *
     * @param activity the certified activity to be removed from the list
     * @see #addCertifiedActivity(String)
     * @see #clearCertifiedActivity()
     * @see #getCertifiedActivities()
     * @since Tifosi2.0
     */
    public void removeCertifiedActivity(String activity)
    {
        if (m_certifiedActivities != null)
        {
            m_certifiedActivities.remove(activity);
        }
    }


    /**
     *  This interface method is called to clear the list of CertifiedActivities
     *  for this object of <code>Security</code>.
     *
     * @see #addCertifiedActivity(String)
     * @see #removeCertifiedActivity(String)
     * @see #getCertifiedActivities()
     * @since Tifosi2.0
     */
    public void clearCertifiedActivity()
    {
        if (m_certifiedActivities != null)
        {
            m_certifiedActivities.clear();
        }
    }


    /**
     *  This method tests whether this object of <code>Security</code>
     *  has the required(mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_certificate != null)
            m_certificate.validate();
    }


    /**
     *  Rests the default values for this object
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
        m_certifiedActivities = new Vector();
    }


    /**
     *  This method is called to write this object of <code>Security</code>
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

        if (m_certificate != null)
        {
            out.writeInt(1);
            m_certificate.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

        if (m_certifiedActivities != null && m_certifiedActivities.size() > 0)
        {
            int num = m_certifiedActivities.size();

            out.writeInt(num);
            for (int i = 0; i < num; ++i)
            {
                writeUTF(out, (String) m_certifiedActivities.elementAt(i));
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>Security</code>
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

        int isnotNull = is.readInt();

        if (isnotNull == 1)
        {
            m_certificate = new Certificate();
            m_certificate.fromStream(is, versionNo);
        }

        int size = is.readInt();

        for (int i = 0; i < size; ++i)
        {
            String name = readUTF(is);

            m_certifiedActivities.addElement(name);
        }
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>Security</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Security))
        {
            return false;
        }

        Security rcvObj = (Security) obj;

        if (rcvObj.getCertificate().equals(m_certificate)
            && rcvObj.getCertifiedActivities().equals(getCertifiedActivities()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This utility method is used to get the String representation of this object
     * of <code>Security</code>.
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
        strBuf.append("Security Details ");
        strBuf.append("[");
        strBuf.append("Certificate = ");
        strBuf.append(m_certificate);
        strBuf.append(", ");
        if (m_certifiedActivities != null)
        {
            strBuf.append("Certified activities = ");
            for (int i = 0; i < m_certifiedActivities.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append((String) m_certifiedActivities.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
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
        Node root0 = document.createElement("SecurityAttributes");

        if (m_certificate != null)
        {
            root0.appendChild(m_certificate.toJXMLString(document));
        }

        // adding certified activities
        XMLDmiUtil.addVectorValues("CertifiedActivity", m_certifiedActivities,
            document, root0);
        return root0;
    }

}
