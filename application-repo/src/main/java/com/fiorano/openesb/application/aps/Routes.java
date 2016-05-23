/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class Routes extends DmiObject
{
    Vector          m_routes = new Vector();


    /**
     *  This is the constructor of the <code>Routes</code> class.
     *
     * @since Tifosi2.0
     */
    public Routes()
    {
    }


    /**
     *  This method gets the enumeration of all the <code>Route</code>
     *  objects contained in this object of <code>Routes</code>.
     *
     * @return Enumeration of Route objects
     * @see #addRoute(Route)
     * @since Tifosi2.0
     */
    public Enumeration getRouteEnumeration()
    {
        return m_routes.elements();
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.ROUTES;
    }


    /**
     *  This method sets all the fieldValues of this object of
     *  <code>Routes</code>, using the specified XML string.
     *
     * @param routesElement
     * @exception FioranoException if an error occurs while parsing the
     *                             XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element routesElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element routesElement = doc.getDocumentElement();

        if (routesElement != null)
        {
            NodeList list = routesElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Route"))
                {
                    Route routeInstance = new Route();

                    routeInstance.setFieldValues((Element) child);
                    addRoute(routeInstance);
                }
            }
        }

        validate();
    }

    public void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException
    {
        //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if ( cursor.markCursor(APSConstants.ROUTES) )
        {
            while(cursor.nextElement())
            {
                String nodeName = cursor.getLocalName();
                if (nodeName.equalsIgnoreCase("Route"))
                {
                    Route routeInstance = new Route();

                    routeInstance.setFieldValues(cursor);
                    addRoute(routeInstance);
                }
            }

        }
        validate();
    }


    /**
     *  This method adds specified object of <code>Route</code>
     *  to this object of <code>Routes</code>.
     *
     * @param route object of Route to be added.
     * @see #getRouteEnumeration()
     * @since Tifosi2.0
     */
    public void addRoute(Route route)
    {
        Enumeration enums = m_routes.elements();
        boolean alreadyExists = false;

        while (enums.hasMoreElements())
        {
            Route routeInst = (Route) enums.nextElement();
            String guidOfRoute = routeInst.getRouteGUID();

            if (guidOfRoute.equalsIgnoreCase(route.getRouteGUID()))
            {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists)
            m_routes.add(route);
    }

    /**
     *  This method removes specified object of <code>Route</code>
     *  from this object of <code>Routes</code>.
     *
     * @param route object of Route to be removed.
     * @see #addRoute(Route route)
     * @since Tifosi2.0
     */
    public void removeRoute(Route route)
    {
        m_routes.remove(route);
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
     *  This method tests whether this object of <code>Routes</code>
     *  has the required(mandatory) fields set, before inserting values in to
     *  the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_routes != null)
        {
            Enumeration enums = m_routes.elements();

            while (enums.hasMoreElements())
            {
                Route route = (Route) enums.nextElement();

                route.validate();
            }
        }
    }


    /**
     *  This method writes this object of <code>Routes</code>
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

        if (m_routes != null)
        {
            int length = m_routes.size();

            out.writeInt(length);

            Enumeration routes = m_routes.elements();

            while (routes.hasMoreElements())
            {
                Route route = (Route) routes.nextElement();

                route.toStream(out, versionNo);
            }
        }
        else
        {
            out.writeInt(0);
        }
    }


    /**
     *  This is called to read this object <code>Routes</code> from the
     *  specified object of input stream.
     *
     * @param is SDataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *                         converting them into specified Java primitive type.

     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        int tempInt = 0;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                Route route = new Route();

                route.fromStream(is, versionNo);
                m_routes.add(route);
            }
        }
    }


    /**
     * This utility method is used to get the String representation of this
     * <code>Routes</code> object.
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
        strBuf.append("Routes ");
        strBuf.append("[");
        if (m_routes != null)
        {
            strBuf.append("route = ");
            for (int i = 0; i < m_routes.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_routes.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the XML string equivalent of this object.
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *                             node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = (Node) document.createElement("Routes");

        Element child = null;

        if (m_routes != null && m_routes.size() > 0)
        {
            Enumeration enums = m_routes.elements();

            while (enums.hasMoreElements())
            {
                Route route = (Route) enums.nextElement();
                Node pcData = route.toJXMLString(document);

                root0.appendChild(pcData);
            }
        }

        return root0;
    }

    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {
        //Start Routes
        writer.writeStartElement("Routes");

        if (m_routes != null && m_routes.size() > 0)
        {
            Enumeration enums = m_routes.elements();

            while (enums.hasMoreElements())
            {
                Route route = (Route) enums.nextElement();
                route.toJXMLString(writer);
            }
        }
        //End Routes
        writer.writeEndElement();
    }
}
