/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.application.aps;

import com.fiorano.openesb.application.*;
import com.fiorano.openesb.application.XPathDmi;
import com.fiorano.openesb.application.common.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public class Route extends DmiObject
{
    /**
     *  Description of the Field
     */
    public final static String TRANSFORMER_TYPE = "TransformerType";

    /**
     *  Description of the Field
     */
    public final static String XALAN = "Xalan";

    /**
     *  Description of the Field
     */
    public final static String SAXON = "Saxon";
    public final static String MESSAGE_TRANSFORMATION_XSL = "MessageTransformationXSL";
    public final static String TRANSFORMATION_PROJECT = "TransformationProject";

    // whether P2P Route
    boolean         m_isP2PRoute;

    // whether persitant Route
    boolean         m_isPersitant;

    // whether durable Route
    boolean         m_isDurable;

    // whether persitant Route
    boolean         m_applyTransformationAtSrc;

    String          m_routeName;

    String          m_routeGUID;

    long            m_iTimeToLive;

    String          m_srcServInst;

    String          m_trgtServInst;

    String          m_srcPortName;

    String          m_trgtPortName;

    String          m_transformationXSL;

    // List of selectors
    HashMap         m_selectors = new HashMap();

    // Vector of params
    Vector          m_params = new Vector();

    String          m_shortDescription;

    String          m_longDescription;

    AlternateDestination m_altDestination;

    // XPath types.
    public static final String XPATH_SELECTOR_OLD = "XPATH";
    public static final String SENDER_SELECTOR = "Sender";
    public static final String JMS_SELECTOR = "JMS";
    public final static String MESSAGE_BODY_XPATH = "MESSAGE_BODY_XPATH";
    public final static String APP_CONTEXT_XPATH = "APP_CONTEXT_XPATH";




    /**
     *  This is called to construct object of <code>Route</code>
     */
    public Route()
    {
    }


    /**
     *  This method checks whether or not this object of <code>Route</code> is
     *  for P2P route.
     *
     * @return true if it is for P2P route, false otherwise.
     * @see #setIsP2PRoute(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isP2PRoute()
    {
        return m_isP2PRoute;
    }


    /**
     *  Gets the persistant attribute of the Route object
     *
     * @return true if it is for persistent route, false otherwise
     * @see #setIsPersitant(boolean)
     * @since Tifosi2.0
     */
    public boolean isPersitant()
    {
        return m_isPersitant;
    }

    /**
     *  Gets the durable attribute of the Route object
     *
     * @return true if it is for durable route, false otherwise
     * @see #setIsDurable(boolean)
     * @since Tifosi2.0
     */
    public boolean isDurable()
    {
        return m_isDurable;
    }


    /**
     *  This is called to check if transformation is to be applied at source for
     *  the route, associated with this object of <code>Route</code>
     *
     * @return true if transformation is to be applied at source, false
     *      otherwise.
     * @see #setApplyTransformationAtSrc(boolean)
     * @since Tifosi2.0
     * @deprecated
     */
    public boolean isApplyTransformationAtSrc()
    {
        return m_applyTransformationAtSrc;
    }


    /**
     *  This method gets the name of route, associated with this object of
     *  <code>Route</code>
     *
     * @return Name of the route
     * @see #setRouteName(String)
     * @since Tifosi2.0
     */
    public String getRouteName()
    {
        return m_routeName;
    }

    /**
     *  This method gets the GUID of route, associated with this object of
     *  <code>Route</code>
     *
     * @return The GUID of route
     * @see #setRouteGUID(String)
     * @since Tifosi2.0
     */
    public String getRouteGUID()
    {
        return m_routeGUID;
    }


    /**
     *  This method gets the TimeToLive for the route associated with object of
     *  <code>Route</code> TimeToLive is the time limit after which the received
     *  message is lost. If this is 0 millisecond, which means infinite.
     *
     * @return TimeToLive is the time limit after which the received  message is lost.
     *         If this is 0 millisecond, which means infinite.
     * @see #setTimeToLive(long)
     * @since Tifosi2.0
     */
    public long getTimeToLive()
    {
        return m_iTimeToLive;
    }


    /**
     *  This method gets the name of source service instance, for the route
     *  associated with this object of <code>Route</code>
     *
     * @return The source service instance name
     * @see #setSrcServInst(String)
     * @since Tifosi2.0
     */
    public String getSrcServInst()
    {
        return m_srcServInst;
    }


    /**
     *  This method gets the node name, on which the source service instance for
     *  the route associated with this object of <code>Route</code>, is running.
     *
     * @return node name on which the source service instance is running
     * @see #setSrcPortName(String)
     * @since Tifosi2.0
     */
    public String getSrcPortName()
    {
        return m_srcPortName;
    }


    /**
     *  This method gets the name of target service instance for the route
     *  associated with this object of <code>Route</code>
     *
     * @return The target service instance name
     * @see #setTrgtServInst(String)
     * @since Tifosi2.0
     */
    public String getTrgtServInst()
    {
        return m_trgtServInst;
    }


    /**
     *  This method gets the node name, on which the target service instance for
     *  the route associated with this object of <code>Route</code>, is running.
     *
     * @return The target port name for this route.
     * @see #setTrgtPortName(String)
     * @since Tifosi2.0
     */
    public String getTrgtPortName()
    {
        return m_trgtPortName;
    }


    /**
     *  This is called to get the short description for the route associated
     *  with this object of <code>Route</code>
     *
     * @return Short description for this route
     * @see #setShortDescription(String)
     * @since Tifosi2.0
     */
    public String getShortDescription()
    {
        return m_shortDescription;
    }


    /**
     *  This is called to get the short description for the route associated
     *  with this object of <code>Route</code>
     *
     * @return Long description for this route
     * @see #setLongDescription(String)
     * @since Tifosi2.0
     */
    public String getLongDescription()
    {
        return m_longDescription;
    }


    /**
     *  This method gets the transformationXSL for the route associated with
     *  this object of <code>Route</code>
     *
     * @return The transformationXSL for this object
     * @see #setTransformationXSL(String)
     * @since Tifosi2.0
     */
    public String getTransformationXSL()
    {
        return m_transformationXSL;
    }


    /**
     *  This method gets the selector for route associated with this object of
     *  <code>Route</code>. Selector is specified to filter the received message
     *  before sending it to the destination node.
     *
     * @return The selector for this route
     * @since Tifosi2.0
     */
    public HashMap getSelectors()
    {
        return m_selectors;
    }

    /**
     *  This method gets the enumeration of all the extra parameters represented
     *  by objects of <code>Param</code>, set for the route associated with this
     *  object of <code>Route</code>.
     *
     * @return Enumeration of all the Param objects set for this route.
     * @see #addParam(Param)
     * @since Tifosi2.0
     */
    public Enumeration getParams()
    {
        return m_params.elements();
    }

    /**
     *  This is called to get object of <code>AlternateDestination</code>,
     *  specified as alternate destination , for the route associated with this
     *  object of <code>Route</code>. AlternateDestination is used when ever the
     *  specified target for the route is not available at the time of routing
     *  data.
     *
     * @return object of AlternateDestination
     * @see #setAlternateDestination(AlternateDestination)
     * @since Tifosi2.0
     */
    public AlternateDestination getAlternateDestination()
    {
        return m_altDestination;
    }


    /**
     *  This method returns the ID of this object.
     *
     * @return the id of this object.
     * @since Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.APP_ROUTE;
    }

    /**
     * Returns message transformation XSL for object
     *
     * @return String
     */
    public String getMessageTransformationXSL()
    {
        return Param.getParamValue(m_params, MESSAGE_TRANSFORMATION_XSL);
    }

    public String getTransformationType()
    {
        return Param.getParamValue(m_params, TRANSFORMER_TYPE);
    }

    public String getTransformationProject()
    {
        return Param.getParamValue(m_params, TRANSFORMATION_PROJECT);
    }


    /**
     *  This method sets whether or not this <code>Route</code> object is for
     *  P2P route.
     *
     * @param isP2PRoute boolean specifying if it is a P2P route.
     * @see #isP2PRoute()
     * @since Tifosi2.0
     * @deprecated
     */
    public void setIsP2PRoute(boolean isP2PRoute)
    {
        m_isP2PRoute = isP2PRoute;
    }


    /**
     *  This method sets whether or not this object of <code>Route</code> is for
     *  persistent route.
     *
     * @param isPersitant boolean specifying whether or not it is for
     *      persistent route.
     * @see #isPersitant()
     * @since Tifosi2.0
     */
    public void setIsPersitant(boolean isPersitant)
    {
        m_isPersitant = isPersitant;
    }

    /**
     *  This method sets whether or not this object of <code>Route</code> is a
     *  durable route.
     *
     * @param isDurable boolean specifying whether or not it is a
     *      durable route.
     * @see #isDurable()
     * @since Tifosi2.0
     */
    public void setIsDurable(boolean isDurable)
    {
        m_isDurable = isDurable;
    }


    /**
     *  This is called to set if transformation is to be applied at source of
     *  route or not, for route associated with this object of <code>Route</code>
     *
     * @param applyTransformationAtSrc boolean for transformation is to be
     *      applied at source or not.
     * @see #isApplyTransformationAtSrc()
     * @since Tifosi2.0
     */
    public void setApplyTransformationAtSrc(boolean applyTransformationAtSrc)
    {
        m_applyTransformationAtSrc = applyTransformationAtSrc;
    }


    /**
     *  This method sets the specified string as the name of route, for this
     *  object of <code>Route</code>
     *
     * @param routeName string to be set as name of route.
     * @see #getRouteName()
     * @since Tifosi2.0
     */
    public void setRouteName(String routeName)
    {
        m_routeName = routeName;
    }

    /**
     *  This method sets the specified string, as the GUID of route for this
     *  object of <code>Route</code>
     *
     * @param routeGUID The string to be set as GUID of route.
     * @see #getRouteGUID()
     * @since Tifosi2.0
     */
    public void setRouteGUID(String routeGUID)
    {
        m_routeGUID = routeGUID;
    }

    /**
     *  This method sets the specified integer as TimeToLive, for the route
     *  associated with object of<code>Route</code> TimeToLive is the time limit
     *  after which the received message is lost.
     *
     * @param iTimeToLive long to be set as TimeToLive
     * @see #getTimeToLive()
     * @since Tifosi2.0
     */
    public void setTimeToLive(long iTimeToLive)
    {
        m_iTimeToLive = iTimeToLive;
    }


    /**
     *  This method sets the specified string as name of source service
     *  instance, for the route associated with this object of <code>Route</code>
     *
     * @param srcServInst string to be set as source service instance
     * @see #getSrcServInst()
     * @since Tifosi2.0
     */
    public void setSrcServInst(String srcServInst)
    {
        m_srcServInst = srcServInst;
    }


    /**
     *  This method sets the specified string as node name, on which the source
     *  service instance for the route associated with this object of <code>Route</code>
     *  , is running.
     *
     * @param srcPortName string to be set as node name on which the source
     *      service instance is running
     * @see #getSrcPortName()
     * @since Tifosi2.0
     */
    public void setSrcPortName(String srcPortName)
    {
        m_srcPortName = srcPortName;
    }


    /**
     *  This method sets the specified string as name of target service
     *  instance, for the route associated with this object of <code>Route</code>
     *
     * @param trgtServInst The string to be set as target Service Instance name
     * @see #getTrgtServInst()
     * @since Tifosi2.0
     */
    public void setTrgtServInst(String trgtServInst)
    {
        m_trgtServInst = trgtServInst;
    }


    /**
     *  This method sets the specified string as node name, on which the target
     *  service instance for the route associated with this object of <code>Route</code>
     *  , is running.
     *
     * @param trgtPortName The string to be set as target port
     * @see #getTrgtPortName()
     * @since Tifosi2.0
     */
    public void setTrgtPortName(String trgtPortName)
    {
        m_trgtPortName = trgtPortName;
    }


    /**
     *  This is called to set the specified string as short description for the
     *  route associated with this object of <code>Route</code>.
     *
     * @param shortDescription string to be set as short description for this
     *      route
     * @see #getShortDescription()
     * @since Tifosi2.0
     */
    public void setShortDescription(String shortDescription)
    {
        m_shortDescription = shortDescription;
    }


    /**
     *  This is called to set the specified string as long description for the
     *  route associated with this object of<code>Route</code>
     *
     * @param longDescription string to be set as long description for this
     *      route
     * @see #getLongDescription()
     * @since Tifosi2.0
     */
    public void setLongDescription(String longDescription)
    {
        m_longDescription = longDescription;
    }


    /**
     *  This method sets the specified string as transformationXSL, for the
     *  route associated with this object of <code>Route</code>
     *
     * @param transformationXSL string to be set as transformationXSL
     * @see #getTransformationXSL()
     * @since Tifosi2.0
     */
    public void setTransformationXSL(String transformationXSL)
    {
        //if transformation is null, then m_applyTransformationAtSrc should be false;
        if(transformationXSL == null || transformationXSL.trim().equalsIgnoreCase(""))
            m_applyTransformationAtSrc = false;
        else
            m_applyTransformationAtSrc = true;

        m_transformationXSL = transformationXSL;
    }


    /**
     *  This method sets the specified string as the selector for route,
     *  associated with this object of <code>Route</code>. Selector is specified
     *  to filter the received message before sending it to the destination
     *  node.
     *
     * @param selectors
     * @since Tifosi2.0
     */
    public void setSelectors(HashMap selectors)
    {
        m_selectors = selectors;
    }


    /**
     *  This is called to set the specified object of <code>AlternateDestination</code>
     *  as alternate destination for the route associated with this object of
     *  <code>Route</code>. AlternateDestination is used when ever the specified
     *  target for the route is not available at the time of routing data.
     *
     * @param altDestination object of AlternateDestination
     * @see #getAlternateDestination()
     * @since Tifosi2.0
     */
    public void setAlternateDestination(AlternateDestination altDestination)
    {
//	  System.out.println("inside APS.Route from src:: "+m_srcServInst+ " to tgt::"+m_trgtServInst+", setting alternate dest::"+altDestination);
        m_altDestination = altDestination;
    }

    /**
     *  This method sets all the fieldValues of this object of <code>Route</code>
     *  , using the specified XML string.
     *
     * @param routeElement The new fieldValues value
     * @exception FioranoException if an error occurs while parsing the
     *      XMLString
     * @since Tifosi2.0
     */
    public void setFieldValues(Element routeElement)
        throws FioranoException
    {
//        Document doc = XMLUtils.getDOMDocumentFromXML(xmlString);
//        Element routeElement = doc.getDocumentElement();

        if (routeElement != null)
        {
            boolean isP2PRoute = XMLDmiUtil.getAttributeAsBoolean(routeElement, "isP2PRoute");

            setIsP2PRoute(isP2PRoute);

            boolean isPersistant = XMLDmiUtil.getAttributeAsBoolean(routeElement, "isPersistant");

            setIsPersitant(isPersistant);

            boolean isDurable = XMLDmiUtil.getAttributeAsBoolean(routeElement, "isDurable");

            setIsDurable(isDurable);

            boolean applyTransformationAtSrc = XMLDmiUtil.getAttributeAsBoolean
                (routeElement, "applyTransformationAtSrc");

            setApplyTransformationAtSrc(applyTransformationAtSrc);

            NodeList list = routeElement.getChildNodes();
            Node child = null;

            for (int i = 0; i < list.getLength(); i++)
            {
                child = list.item(i);

                String nodeName = child.getNodeName();

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    setRouteName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("RouteGUID"))
                {
                    setRouteGUID(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("TimeToLive"))
                {
                    setTimeToLive(XMLUtils.getNodeValueAsLong(child));
                }
                else if (nodeName.equalsIgnoreCase("SrcServiceInstance"))
                {
                    setSrcServInst(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("SrcPort"))
                {
                    setSrcPortName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("TransformationXSL"))
                {
                    setTransformationXSL(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("Selector"))
                {
                    String type = ((Element) child).getAttribute("type");

                    HashMap namespace = null;
                    if(type.equalsIgnoreCase(XPATH_SELECTOR_OLD))
                        type = MESSAGE_BODY_XPATH;

                    if (type.equalsIgnoreCase(MESSAGE_BODY_XPATH) || type.equalsIgnoreCase(APP_CONTEXT_XPATH))
                    {
                        NamedNodeMap nodemap = child.getAttributes();
                        int size = nodemap.getLength();

                        for (int iter = 0; iter < size; iter++)
                        {
                            Node node = nodemap.item(iter);
                            String attrName = node.getNodeName();

                            if (attrName.startsWith("esb_"))
                            {
                                if (namespace == null)
                                    namespace = new HashMap();

                                String val = node.getNodeValue();
                                namespace.put(attrName.substring(4), val);
                            }
                        }
                        String value = XMLUtils.getNodeValueAsString(child);

                        addXPathSelector(type, value, namespace);
                    }
                    else
                    {
                        String value = XMLUtils.getNodeValueAsString(child);

                        m_selectors.put(type, value);
                    }


                }
                else if (nodeName.equalsIgnoreCase("TgtServiceInstance"))
                {
                    setTrgtServInst(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("TgtPort"))
                {
                    setTrgtPortName(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("LongDescription"))
                {
                    setLongDescription(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("ShortDescription"))
                {
                    setShortDescription(XMLUtils.getNodeValueAsString(child));
                }
                else if (nodeName.equalsIgnoreCase("Param"))
                {
                    Param param = new Param();

                    param.setFieldValues((Element) child);
                    m_params.add(param);
                }
                else if (nodeName.equalsIgnoreCase("AlternateDestination"))
                {
                    AlternateDestination altDestination = new AlternateDestination();

                    altDestination.setFieldValues((Element) child);
                    setAlternateDestination(altDestination);
                }
            }
        }

        validate();
    }

    public void populate(FioranoStaxParser cursor) throws XMLStreamException,FioranoException
    {
    //Set cursor to the current DMI element. You can use either markCursor/getNextElement(<element>) API.
        if ( cursor.markCursor(APSConstants.APP_ROUTE) )
        {
            // Get Attributes. This MUST be done before accessing any data of element.
            Hashtable attributes = cursor.getAttributes();

            if ( attributes != null && attributes.containsKey(APSConstants.IS_P2PROUTE))
            {
                boolean isP2PRoute = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_P2PROUTE) );
                setIsP2PRoute(isP2PRoute);
            }
            if ( attributes != null && attributes.containsKey(APSConstants.IS_PERSISTANT))
            {
                boolean isPersistant = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_PERSISTANT));
                setIsPersitant(isPersistant);
            }
            if ( attributes != null && attributes.containsKey(APSConstants.IS_DURABLE))
            {
                boolean isDurable = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.IS_DURABLE));
                setIsDurable(isDurable);
            }
            if ( attributes != null && attributes.containsKey(APSConstants.APPLY_TRANSFORMATION_AT_SRC))
            {
                boolean applyTransformationAtSrc = XMLUtils.getStringAsBoolean((String)attributes.get(APSConstants.APPLY_TRANSFORMATION_AT_SRC));
                setApplyTransformationAtSrc(applyTransformationAtSrc);
            }

            //Get associated Data
            //String nodeVal = cursor.getText();

            // Get Child Elements
            while (cursor.nextElement())
            {

                String nodeName = cursor.getLocalName();

                // For debugging. Remove this befor chekin...

                // Get Attributes
                attributes = cursor.getAttributes();

                if (nodeName.equalsIgnoreCase("Name"))
                {
                    String nodeValue = cursor.getText();
                    setRouteName(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("RouteGUID"))
                {
                    String nodeValue = cursor.getText();
                    setRouteGUID(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("TimeToLive"))
                {
                    long nodeValue = XMLUtils.getStringAsLong(cursor.getText());
                    setTimeToLive(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("SrcServiceInstance"))
                {
                    String nodeValue = cursor.getText();
                    setSrcServInst(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("SrcPort"))
                {
                    String nodeValue = cursor.getText();
                    setSrcPortName(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("TransformationXSL"))
                {
                    String nodeValue = cursor.getCData();
                    setTransformationXSL(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("Selector"))
                {
                    String type = (String)attributes.get("type");
                    //Iterator itr = attributes.values().iterator();
                    Enumeration namesItr=attributes.keys();
                    HashMap namespace = null;

                    if(type.equalsIgnoreCase(XPATH_SELECTOR_OLD))
                        type = MESSAGE_BODY_XPATH;

                    if (type.equalsIgnoreCase(MESSAGE_BODY_XPATH) || type.equalsIgnoreCase(APP_CONTEXT_XPATH))
                    {
                        while(namesItr.hasMoreElements())
                        {
                            String attrName = (String)namesItr.nextElement();
                            if (attrName.startsWith("esb_"))
                            {
                                if (namespace == null)
                                    namespace = new HashMap();

                                String val = (String)attributes.get(attrName);
                                namespace.put(attrName.substring(4), val);
                            }
                        }
                        String value = cursor.getText();
                        addXPathSelector(type, value, namespace);
                    }
                    else
                    {
                        String value = cursor.getText();
                        m_selectors.put(type, value);
                    }


                }
                else if (nodeName.equalsIgnoreCase("TgtServiceInstance"))
                {
                    String nodeValue = cursor.getText();
                    setTrgtServInst(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("TgtPort"))
                {
                    String nodeValue = cursor.getText();
                    setTrgtPortName(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("LongDescription"))
                {
                    String nodeValue = cursor.getText();
                    setLongDescription(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("ShortDescription"))
                {
                    String nodeValue = cursor.getText();
                    setShortDescription(nodeValue);
                }
                else if (nodeName.equalsIgnoreCase("Param"))
                {
                    String nodeValue = cursor.getText();
                    Param param = new Param();
                    String name = (String)attributes.get("name");

                    param.setParamName(name);
                    param.setParamValue(nodeValue);

                    m_params.add(param);
                }
                else if (nodeName.equalsIgnoreCase("AlternateDestination"))
                {
                    AlternateDestination altDestination = new AlternateDestination();


                    altDestination.setFieldValues(cursor);
                    setAlternateDestination(altDestination);
                }
            }

           }
        validate();
    }
    /**
     * Sets message transformation XSL for object
     *
     * @param xsl
     */
    public void setMessageTransformationXSL(String xsl)
    {
        Param.setParamValue(m_params, MESSAGE_TRANSFORMATION_XSL, xsl);
    }

    public void setTransformationType(String type)
    {
        Param.setParamValue(m_params, TRANSFORMER_TYPE, type);
    }

    public void setTransformationProject(String project)
    {
        Param.setParamValue(m_params, TRANSFORMATION_PROJECT, project);
    }

    /**
     * Adds a Selector. If the selector is XPath, then its assumed that there are no namespaces.
     * to specify the namespaces, use addXPathSelector API.
     *
     * @param type The feature to be added to the Selector attribute
     * @param value The feature to be added to the Selector attribute
     */
    public void addSelector(String type, String value)
    {
        if (m_selectors == null)
            m_selectors = new HashMap();

        if(type.equalsIgnoreCase(APP_CONTEXT_XPATH) || type.equalsIgnoreCase(MESSAGE_BODY_XPATH))
        {
            try {
                addXPathSelector(type, value, null);
            } catch (FioranoException e) {
                // ignore as this cannot happen.
            }
            return;
        }

        m_selectors.put(type, value);
    }

    /**
     * Adds a XPath selector on the Message Body for the Messages flowing through this route.
     * Type mentions the type of the selector. it should one of
     * 1. APP_CONTEXT_XPATH  - to add the xpath selector on to the app context.
     * 2. MESSAGE_BODY_XPATH - to add the xpath selector to the message body.
     *
     * @param value The feature to be added to the XPathSelector attribute
     * @param namespaces The feature to be added to the XPathSelector attribute
     */
    public void addXPathSelector(String type, String value, HashMap namespaces)
            throws FioranoException
    {
        if (m_selectors == null)
            m_selectors = new HashMap();
        // Even if namespaces are passed as null. we create the XPath DMI.
        XPathDmi xpath;

        if(type.equalsIgnoreCase(APP_CONTEXT_XPATH) || type.equalsIgnoreCase(MESSAGE_BODY_XPATH))
            xpath = new XPathDmi(value, namespaces);
        else
            throw new FioranoException("XPATH type : "+type+" not VALID. Please refer to API doc for valid values.");
        m_selectors.put(type, xpath);
    }

    /**
     *  This method adds the specified object of <code>Param</code>, to the list
     *  of extra parameters, for route associated with this object of <code>Route</code>
     *
     * @param param The feature to be added to the Param attribute
     * @see #getParams()
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
     *  This method tests whether this object of <code>Route</code> has the
     *  required(mandatory) fields set, before inserting values in the database.
     *
     * @exception FioranoException if the object is not valid
     * @since Tifosi2.0
     */
    public void validate()
        throws FioranoException
    {
        if (m_routeGUID == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_srcServInst == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_trgtServInst == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_srcPortName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }
        if (m_trgtPortName == null)
        {
            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
        }

        if (m_params != null)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();

                param.validate();
            }
        }

        if (m_applyTransformationAtSrc)
        {
            if (m_transformationXSL == null || m_transformationXSL.equalsIgnoreCase(""))
            {
                throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR);
            }
        }

//        if ((m_selectorType != null && m_selector == null) || (m_selectorType == null && m_selector != null))
//            throw new FioranoException(DmiErrorCodes.ERR_INVALID_ARGUMENT_ERROR, LogHelper.getErrMessage(ILogModule.DMI, 27));

    }


    /**
     *  This method is called to write this object of <code>Route</code> to the
     *  specified output stream object.
     *
     * @param out DataOutput object
     * @param versionNo
     * @exception IOException if an error occurs while converting data and
     *      writing it to a binary stream.
     * @see #fromStream(DataInput, int)
     * @since Tifosi2.0
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        out.writeBoolean(m_isP2PRoute);

        out.writeBoolean(m_isPersitant);

        out.writeBoolean(m_isDurable);

        out.writeBoolean(m_applyTransformationAtSrc);

        if (m_routeName != null)
        {
            UTFReaderWriter.writeUTF(out, m_routeName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_routeGUID != null)
        {
            UTFReaderWriter.writeUTF(out, m_routeGUID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        out.writeLong(m_iTimeToLive);

        if (m_srcServInst != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcServInst);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtServInst != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtServInst);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_srcPortName != null)
        {
            UTFReaderWriter.writeUTF(out, m_srcPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_trgtPortName != null)
        {
            UTFReaderWriter.writeUTF(out, m_trgtPortName);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_transformationXSL != null)
        {
            UTFReaderWriter.writeUTF(out, m_transformationXSL);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_selectors != null)
        {
            int size = m_selectors.size();

            // For newer versions i am writing -1.
            out.writeInt(-1);
            out.writeInt(size);

            Set keys = m_selectors.keySet();
            Iterator itr = keys.iterator();

            while (itr.hasNext())
            {
                String type = (String) itr.next();
                Object obj = m_selectors.get(type);

                if (type == null)
                    type = "";

                // Write the type.
                UTFReaderWriter.writeUTF(out, type);

                if (type.equalsIgnoreCase(MESSAGE_BODY_XPATH)
                        || type.equalsIgnoreCase(APP_CONTEXT_XPATH))
                {
                    // Then ask the dmi to streamify.
                    ((XPathDmi) obj).toStream(out, versionNo);
                }
                else
                {
                    String value = (String) obj;

                    if (value == null)
                        value = "";

                    UTFReaderWriter.writeUTF(out, value);
                }
            }
        }
        else
        {
            out.writeInt(0);
        }

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
        {
            out.writeInt(0);
        }

        if (m_shortDescription != null)
        {
            UTFReaderWriter.writeUTF(out, m_shortDescription);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_longDescription != null)
        {
            UTFReaderWriter.writeUTF(out, m_longDescription);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_altDestination != null)
        {
            out.writeInt(1);
            m_altDestination.toStream(out, versionNo);
        }
        else
        {
            out.writeInt(0);
        }

    }


    /**
     *  This method reads this object <code>Routes</code> from the specified
     *  input stream object.
     *
     * @param is DataInput object
     * @param versionNo
     * @exception IOException if an error occurs while reading bytes or while
     *      converting them into specified Java primitive type.
     * @see #toStream(DataOutput, int)
     * @since Tifosi2.0
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        m_isP2PRoute = is.readBoolean();

        m_isPersitant = is.readBoolean();

        m_isDurable = is.readBoolean();

        m_applyTransformationAtSrc = is.readBoolean();

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
        {
            m_routeName = null;
        }
        else
        {
            m_routeName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_routeGUID = null;
        }
        else
        {
            m_routeGUID = temp;
        }

        m_iTimeToLive = is.readLong();

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcServInst = null;
        }
        else
        {
            m_srcServInst = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtServInst = null;
        }
        else
        {
            m_trgtServInst = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_srcPortName = null;
        }
        else
        {
            m_srcPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_trgtPortName = null;
        }
        else
        {
            m_trgtPortName = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_transformationXSL = null;
        }
        else
        {
            m_transformationXSL = temp;
        }

        int size = is.readInt();

        if (size > 0)
        {
            _readSelectorFromOldDmi(is, versionNo, size);
        }
        else if (size < 0)
        {
            _readSelector(is, versionNo);
        }

        int tempInt = 0;

        if ((tempInt = is.readInt()) != 0)
        {
            for (int i = 0; i < tempInt; i++)
            {
                Param param = new Param();

                param.fromStream(is, versionNo);
                m_params.add(param);
            }
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_shortDescription = null;
        }
        else
        {
            m_shortDescription = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_longDescription = null;
        }
        else
        {
            m_longDescription = temp;
        }

        if (is.readInt() == 1)
        {
            m_altDestination = new AlternateDestination();
            m_altDestination.fromStream(is, versionNo);
        }
    }


    /**
     *  This utility method is used to get the String representation of this
     *  <code>Route</code> object.
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
        strBuf.append("Route Details ");
        strBuf.append("[");
        strBuf.append("Route Name = ");
        strBuf.append(m_routeName);
        strBuf.append(", ");
        strBuf.append("Route GUID = ");
        strBuf.append(m_routeGUID);
        strBuf.append(", ");
        strBuf.append("Is P2P Route  = ");
        strBuf.append(m_isP2PRoute);
        strBuf.append(", ");
        strBuf.append("Is persistant Route = ");
        strBuf.append(m_isPersitant);
        strBuf.append(", ");
        strBuf.append("Is Durable Route = ");
        strBuf.append(m_isDurable);
        strBuf.append(", ");
        strBuf.append("Is Apply Transformation at src = ");
        strBuf.append(m_applyTransformationAtSrc);
        strBuf.append(", ");
        strBuf.append("Time to Live = ");
        strBuf.append(m_iTimeToLive);
        strBuf.append(", ");
        strBuf.append("Source Service Instance = ");
        strBuf.append(m_srcServInst);
        strBuf.append(", ");
        strBuf.append("Target Service Instance = ");
        strBuf.append(m_trgtServInst);
        strBuf.append(", ");
        strBuf.append("Target Service Instance = ");
        strBuf.append(m_trgtServInst);
        strBuf.append(", ");
        strBuf.append("Target Service Instance = ");
        strBuf.append(m_trgtServInst);
        strBuf.append(", ");
        strBuf.append("Transformation = ");
        strBuf.append(m_transformationXSL);
        strBuf.append(", ");
        strBuf.append("Selectors = ");
        strBuf.append(m_selectors);

        strBuf.append(", ");
        if (m_params != null)
        {
            strBuf.append("Parameters = ");
            for (int i = 0; i < m_params.size(); i++)
            {
                strBuf.append((i + 1) + ". ");
                strBuf.append(m_params.elementAt(i));
                strBuf.append(", ");
            }
        }
        strBuf.append(", ");
        strBuf.append("Short Description= ");
        strBuf.append(m_shortDescription);
        strBuf.append(", ");
        strBuf.append("Long Description= ");
        strBuf.append(m_longDescription);
        strBuf.append(", ");
        if (m_altDestination != null)
        {
            strBuf.append("Alternate Destinations= ");
            strBuf.append(m_altDestination.toString());
        }

        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     *  Returns the xml string equivalent of this object
     *
     * @param document the input Document object
     * @return element node
     * @exception FioranoException if an error occurs while creating the element
     *      node.
     */
    Node toJXMLString(Document document)
        throws FioranoException
    {
        Node root0 = document.createElement("Route");

        ((Element) root0).setAttribute("isP2PRoute", "" + m_isP2PRoute);
        ((Element) root0).setAttribute("isPersistant", "" + m_isPersitant);
        ((Element) root0).setAttribute("isDurable", "" + m_isDurable);
        ((Element) root0).setAttribute("applyTransformationAtSrc", "" + m_applyTransformationAtSrc);

        Node node = null;

        node = XMLDmiUtil.getNodeObject("Name", m_routeName, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("RouteGUID", m_routeGUID, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("TimeToLive", "" + m_iTimeToLive, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("SrcServiceInstance", m_srcServInst, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("SrcPort", m_srcPortName, document);
        if (node != null)
            root0.appendChild(node);

        Element child = null;

        if (m_transformationXSL != null)
        {
            child = document.createElement("TransformationXSL");

            Node pcData = document.createCDATASection(m_transformationXSL);

            child.appendChild(pcData);
            root0.appendChild(child);
        }
        if (m_selectors != null)
        {
            Iterator itr = m_selectors.keySet().iterator();

            while (itr.hasNext())
            {
                child = document.createElement("Selector");

                String type = (String) itr.next();

                child.setAttribute("type", type);

                Object val = m_selectors.get(type);
                String value = null;

                if (val instanceof String)
                {
                    value = (String) m_selectors.get(type);
                }
                else if (val instanceof XPathDmi)
                {
                    value = ((XPathDmi) val).getXPath();
                    HashMap map = ((XPathDmi) val).getNameSpace();

                    if (map != null)
                    {
                        Set keys = map.keySet();
                        Iterator iter = keys.iterator();

                        while (iter.hasNext())
                        {
                            String key = (String) iter.next();
                            String keyval = (String) map.get(key);

                            child.setAttribute("esb_" + key, keyval);
                        }
                    }
                }

                Node pcData = document.createTextNode(value);

                child.appendChild(pcData);
                root0.appendChild(child);
            }
        }

        node = XMLDmiUtil.getNodeObject("TgtServiceInstance", m_trgtServInst, document);
        if (node != null)
            root0.appendChild(node);

        node = XMLDmiUtil.getNodeObject("TgtPort", m_trgtPortName, document);
        if (node != null)
            root0.appendChild(node);

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
        if (m_altDestination != null)
        {
            root0.appendChild(m_altDestination.toJXMLString(document));
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

        return root0;
    }


    public void toJXMLString(XMLStreamWriter writer) throws XMLStreamException
    {

        //Start Route
        writer.writeStartElement("Route");

        //Add Route Attributes
        writer.writeAttribute("isP2PRoute", "" + m_isP2PRoute);
        writer.writeAttribute("isPersistant", "" + m_isPersitant);
        writer.writeAttribute("isDurable", "" + m_isDurable);
        writer.writeAttribute("applyTransformationAtSrc", "" + m_applyTransformationAtSrc);

        //Name
        FioranoStackSerializer.writeElement("Name", m_routeName, writer);

        //RouteGuid
        FioranoStackSerializer.writeElement("RouteGUID", m_routeGUID, writer);

        //TimeToLive
        FioranoStackSerializer.writeElement("TimeToLive", "" + m_iTimeToLive, writer);

        //SrcServiceInstance
        FioranoStackSerializer.writeElement("SrcServiceInstance", m_srcServInst, writer);

        //SrcPort
        FioranoStackSerializer.writeElement("SrcPort", m_srcPortName, writer);

        //TransformationXSL
        if (m_transformationXSL != null)
        {
            writer.writeStartElement("TransformationXSL");
            writer.writeCData(m_transformationXSL);
            writer.writeEndElement();

        }
        //Selectors
        if (m_selectors != null)
        {
            Iterator itr = m_selectors.keySet().iterator();

            while (itr.hasNext())
            {
                writer.writeStartElement("Selector");
                String type = (String) itr.next();
                writer.writeAttribute("type", type);
                Object val = m_selectors.get(type);
                String value = null;
                if (val instanceof String)
                {
                    value = (String) m_selectors.get(type);
                }
                else if (val instanceof XPathDmi)
                {
                    value = ((XPathDmi) val).getXPath();

                    HashMap map = ((XPathDmi) val).getNameSpace();

                    if (map != null)
                    {
                        Set keys = map.keySet();
                        Iterator iter = keys.iterator();

                        while (iter.hasNext())
                        {
                            String key = (String) iter.next();
                            String keyval = (String) map.get(key);
                            writer.writeAttribute("esb_" + key, keyval);
                        }
                    }
                }
                writer.writeCharacters(value);

                //End Selector
                writer.writeEndElement();
            }
        }

        //TgtServiceInstance
        FioranoStackSerializer.writeElement("TgtServiceInstance", m_trgtServInst, writer);

        //TgtPort
        FioranoStackSerializer.writeElement("TgtPort", m_trgtPortName, writer);

        //LongDescription
        if(!StringUtils.isEmpty(m_longDescription)){
            FioranoStackSerializer.writeElement("LongDescription", m_longDescription, writer);
        }

        //ShortDescription
        if(!StringUtils.isEmpty(m_shortDescription)){
            FioranoStackSerializer.writeElement("ShortDescription", m_shortDescription, writer);
        }

        if (m_altDestination != null)
        {
            m_altDestination.toJXMLString(writer);
        }

        if (m_params != null && m_params.size() > 0)
        {
            Enumeration enums = m_params.elements();

            while (enums.hasMoreElements())
            {
                Param param = (Param) enums.nextElement();
                if(!StringUtils.isEmpty(param.getParamName()) && !StringUtils.isEmpty(param.getParamValue()))
                    param.toJXMLString(writer);
            }
        }
        //End Route
        writer.writeEndElement();
   }

    private void _readSelectorFromOldDmi(DataInput is, int versionNo, int size)
        throws IOException
    {
        for (int i = 0; i < size; i++)
        {
            String type = UTFReaderWriter.readUTF(is);

            if (type.equals(""))
                type = null;

            String value = UTFReaderWriter.readUTF(is);

            if (value.equals(""))
                value = null;

            addSelector(type, value);
        }
    }

    private void _readSelector(DataInput is, int versionNo)
        throws IOException
    {
        int size = is.readInt();
        for (int i = 0; i < size; i++)
        {
            String type = UTFReaderWriter.readUTF(is);

            if (!(type.equalsIgnoreCase(MESSAGE_BODY_XPATH) || type.equalsIgnoreCase(APP_CONTEXT_XPATH)))
            {
                if (type.equals(""))
                    type = null;

                String value = UTFReaderWriter.readUTF(is);
                if (value.equals(""))
                    value = null;
                addSelector(type, value);
            }
            else
            {
                XPathDmi dmi = new XPathDmi();
                dmi.fromStream(is, versionNo);
                if(m_selectors == null)
                    m_selectors = new HashMap();

                m_selectors.put(type, dmi);
            }
        }
    }
}

