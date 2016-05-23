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
import java.io.IOException;

public class Certificate extends DmiObject
{
    String          m_strSigner;
    String          m_strIssuedTo;
    String          m_strValidFromDate;
    String          m_strValidToDate;


    public Certificate()
    {
        reset();
    }


    /**
     *  This interface method is called to get signer of the <code>Certificate</code>
     *  for service.
     *
     * @return The signer for certificate
     * @see #setSigner(String)
     * @since Tifosi2.0
     */
    public String getSigner()
    {
        return m_strSigner;
    }


    /**
     *  This interface method is called to get the name of an individual or organization
     *  to whom this <code>Certificate</code> is issued, for a service.
     *
     * @return the name an of individual to whom this certificate is issued
     * @see #setIssuedTo(String)
     * @since Tifosi2.0
     */
    public String getIssuedTo()
    {
        return m_strIssuedTo;
    }


    /**
     *  This interface method is called to get the date, from which the validity of this
     *  </code>Certificate</code> starts for a service.
     *
     * @return The date from which validity of certificate starts.
     * @see #setValidFromDate(String)
     * @since Tifosi2.0
     */
    public String getValidFrom()
    {
        return m_strValidFromDate;
    }


    /**
     *  This interface method is called to get the date up to which this
     *  </code>Certificate</code> is valid for a service.
     *
     * @return The date up to which this certificate is valid.
     * @see #setValidTo(String)
     * @since Tifosi2.0
     */
    public String getValidTo()
    {
        return m_strValidToDate;
    }


    /**
     *  This API returns the id of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.CERTIFICATE;
    }


    /**
     * This interface method is called to set the specified string as signer of the
     * <code>Certificate</code>, for service.
     *
     * @param signer The string to be set as signer for certificate.
     * @see #getSigner()
     * @since Tifosi2.0
     */
    public void setSigner(String signer)
    {
        m_strSigner = signer;
    }


    /**
     *  This interface method is called to set the specified string as name of an
     *  individual or organization to whom this <code>Certificate</code> is
     *  issued, for a service.
     *
     * @param issuedTo The string to be set as name of issuedTo
     * @see #getIssuedTo()
     * @since Tifosi2.0
     */
    public void setIssuedTo(String issuedTo)
    {
        m_strIssuedTo = issuedTo;
    }


    /**
     *  This interface method is called to set the specified string as date, from which
     *  the validity of this </code>Certificate</code> starts for a service.
     *
     * @param fromDate The string to be set as date from which validity of
     *                      certificate starts.
     * @see #getValidFrom()
     * @since Tifosi2.0
     */
    public void setValidFromDate(String fromDate)
    {
        m_strValidFromDate = fromDate;
    }


    /**
     *  This interface method is called to set the specified string as date up to which
     *  this </code>Certificate</code> is valid for a service.
     *
     * @param toDate The string to be set as date up to which this certificate
     *                  is valid.
     * @see #getValidTo()
     * @since Tifosi2.0
     */
    public void setValidTo(String toDate)
    {
        m_strValidToDate = toDate;
    }


    /**
     *  This interface method is called to set all the fieldValues of this object of
     *  <code>Certificate</code>, using the specified XML string.
     *
     * @param certificate
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element certificate)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element certificate = doc.getDocumentElement();

        if (certificate != null)
        {
            NodeList children = certificate.getChildNodes();
            Node child = null;

            for (int i = 0; children != null && i < children.getLength(); ++i)
            {
                child = children.item(i);

                String nodeName = child.getNodeName();
                String nodeValue = XMLUtils.getNodeValueAsString(child);

                if (nodeName.equalsIgnoreCase("Signer"))
                {
                    m_strSigner = nodeValue;
                }

                if (nodeName.equalsIgnoreCase("IssuedTo"))
                {
                    m_strIssuedTo = nodeValue;
                }

                if (nodeName.equalsIgnoreCase("ValidFrom"))
                {
                    m_strValidFromDate = nodeValue;
                }

                if (nodeName.equalsIgnoreCase("ValidTo"))
                {
                    m_strValidToDate = nodeValue;
                }
            }
        }
        validate();
    }


    /**
     *  This method tests whether this object of <code>Certificate</code>
     *  has the required (mandatory) fields set, before inserting values in
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_strIssuedTo == null || m_strSigner == null
            || m_strValidFromDate == null || m_strValidToDate == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
    }


    /**
     *  Resets the default values for the object. Not supported in this version.
     *
     * @since Tifosi2.0
     */
    public void reset()
    {
    }


    /**
     *  This method is called to write this object of <code>Certificate</code>
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

        writeUTF(out, m_strSigner);
        writeUTF(out, m_strIssuedTo);
        writeUTF(out, m_strValidFromDate);
        writeUTF(out, m_strValidToDate);
    }


    /**
     *  This is called to read this object <code>Certificate</code>
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

        m_strSigner = readUTF(is);
        m_strIssuedTo = readUTF(is);
        m_strValidFromDate = readUTF(is);
        m_strValidToDate = readUTF(is);
    }


    /**
     *  This utility method is used to compare this object of
     *  <code>Certificate</code> with the specified object.
     *
     * @param obj the object with which comparison is to be made
     * @return true if the objects are equal.
     * @since Tifosi2.0
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Certificate))
        {
            return false;
        }

        Certificate rcvObj = (Certificate) obj;

        if (DmiEqualsUtil.checkStringEquality(rcvObj.getIssuedTo(), m_strIssuedTo)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getSigner(), m_strSigner)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getValidFrom(), m_strValidFromDate)
            && DmiEqualsUtil.checkStringEquality(rcvObj.getValidTo(), m_strValidToDate))
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
     * of <code>Certificate</code>
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
        strBuf.append("Certificate Details ");
        strBuf.append("[");
        strBuf.append("Issued to = ");
        strBuf.append(m_strIssuedTo);
        strBuf.append(", ");
        strBuf.append("Signer = ");
        strBuf.append(m_strSigner);
        strBuf.append(", ");
        strBuf.append("Valid from Date = ");
        strBuf.append(m_strValidFromDate);
        strBuf.append(", ");
        strBuf.append("Valid to Date = ");
        strBuf.append(m_strValidToDate);
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element node.
     */
    protected Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Certificate");

        Node child = null;

        child = XMLDmiUtil.getNodeObject("Signer", m_strSigner, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("IssuedTo", m_strIssuedTo, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("ValidFrom", m_strValidFromDate, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        child = XMLDmiUtil.getNodeObject("ValidTo", m_strValidToDate, document);
        if (child != null)
        {
            root0.appendChild(child);
        }

        return root0;
    }

}
