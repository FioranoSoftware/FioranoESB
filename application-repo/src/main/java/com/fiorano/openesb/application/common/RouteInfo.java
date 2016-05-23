/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.common;

import com.fiorano.openesb.application.lp.RouteLaunchPacket;
import com.fiorano.openesb.utils.DmiEqualsUtil;
import com.fiorano.openesb.utils.UTFReaderWriter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RouteInfo extends RouteLaunchPacket
{
    String          m_strSrcNode;
    String          m_strSrcAppInstID;
    String          m_strSrcAppVersion;
    String          m_strSrcServInstID;

    String          m_strSrcServGUID;
    String          m_strTgtServGUID;

    String          m_strSrcServVersion;
    String          m_strTgtServVersion;

    /**
     *  Constructor for the RouteInfo object
     */
    public RouteInfo()
    {
    }

    /**
     *  Gets the srcNode attribute of the RouteInfo object
     *
     * @return The srcNode value
     */
    public String getSrcNode()
    {
        return m_strSrcNode;
    }


    /**
     *  Gets the srcAppInstID attribute of the RouteInfo object
     *
     * @return The srcAppInstID value
     */
    public String getSrcAppInstID()
    {
        return m_strSrcAppInstID;
    }

    /**
     *  Gets the srcAppVersion attribute of the RouteInfo object
     *
     * @return The srcAppVersion value
     */
    public String getSrcAppVersion()
    {
        return m_strSrcAppVersion;
    }

    /**
     *  Gets the srcServInstID attribute of the RouteInfo object
     *
     * @return The srcServInstID value
     */
    public String getSrcServInstID()
    {
        return m_strSrcServInstID;
    }


    /**
     *  Gets the srcServGUID attribute of the RouteInfo object
     *
     * @return The srcServGUID value
     */
    public String getSrcServGUID()
    {
        return m_strSrcServGUID;
    }


    /**
     *  Gets the tgtServGUID attribute of the RouteInfo object
     *
     * @return The tgtServGUID value
     */
    public String getTgtServGUID()
    {
        return m_strTgtServGUID;
    }

    /**
     *  Gets the srcServVersion attribute of the RouteInfo object
     *
     * @return The srcServVersion value
     */
    public String getSrcServVersion()
    {
        return m_strSrcServVersion;
    }


    /**
     *  Gets the tgtServVersion attribute of the RouteInfo object
     *
     * @return The tgtServVersion value
     */
    public String getTgtServVersion()
    {
        return m_strTgtServVersion;
    }


    /**
     *  Sets the srcNode attribute of the RouteInfo object
     *
     * @param srcNode The new srcNode value
     */
    public void setSrcNode(String srcNode)
    {
        m_strSrcNode = srcNode;
    }

//
//    /**
//     *  Sets the srcNode attribute of the RouteInfo object
//     *
//     *@param  srcNodes  The new srcNode value
//     */
//    public void setSrcNode(Enumeration srcNodes)
//    {
//        m_strSrcNode = (String) srcNodes.nextElement();
//    }

//    /**
//     *  Sets the tgtNode attribute of the RouteInfo object
//     *
//     *@param  tgtNodes  The new tgtNode value
//     */
//    public void setTgtNode(Enumeration tgtNodes)
//    {
//        m_strTgtNode = (String) tgtNodes.nextElement();
//    }



    /**
     *  Sets the tgtAppInstID attribute of the RouteInfo object
     *
     * @param srcAppInstID
     */
//    public void setTgtAppInstID(String tgtAppInstID)
//    {
//        m_strTgtAppInstID = tgtAppInstID;
//    }


    /**
     *  Gets the tgtAppInstID attribute of the RouteInfo object
     *
     * @param srcAppInstID
     */
//    public String getTgtAppInstID()
//    {
//        return m_strTgtAppInstID;
//    }


    /**
     *  Sets the srcAppInstID attribute of the RouteInfo object
     *
     * @param srcAppInstID The new srcAppInstID value
     */
    public void setSrcAppInstID(String srcAppInstID)
    {
        m_strSrcAppInstID = srcAppInstID;
    }

    /**
     *  Sets the srcAppVersion attribute of the RouteInfo object
     *
     * @param srcAppVersion The new srcAppVersion value
     */
    public void setSrcAppVersion(String srcAppVersion)
    {
        m_strSrcAppVersion = srcAppVersion;
    }

    /**
     *  Sets the srcServInstID attribute of the RouteInfo object
     *
     * @param servInstId
     */
    public void setSrcServInstID(String servInstId)
    {
        m_strSrcServInstID = servInstId;
    }


    /**
     *  Sets the srcServGUID attribute of the RouteInfo object
     *
     * @param srcServGUID The new srcServGUID value
     */
    public void setSrcServGUID(String srcServGUID)
    {
        m_strSrcServGUID = srcServGUID;
    }

    /**
     *  Sets the tgtServGUID attribute of the RouteInfo object
     *
     * @param tgtServGUID The new tgtServGUID value
     */
    public void setTgtServGUID(String tgtServGUID)
    {
        m_strTgtServGUID = tgtServGUID;
    }

    /**
     *  Sets the srcServVersion attribute of the RouteInfo object
     *
     * @param version The new srcServVersion value
     */
    public void setSrcServVersion(String version)
    {
        m_strSrcServVersion = version;
    }


    /**
     *  Sets the tgtServVersion attribute of the RouteInfo object
     *
     * @param version The new tgtServVersion value
     */
    public void setTgtServVersion(String version)
    {
        m_strTgtServVersion = version;
    }


    /**
     *  Sets the route attribute of the RouteInfo object
     *
     * @param route The new route value
     */
    public void setRoute(RouteLaunchPacket route)
    {
        setIsDurable(route.isDurable());
        setTransformation(route.getTransformation());
        setRouteName(route.getRouteName());
        setRouteGUID(route.getRouteGUID());
        setMessageCompression(route.isMessageCompressed());
        setMessageEncryption(route.isMessageEncrypted());

//        setSrcServInst(routegetSrcServInst());
        setSrcPortName(route.getSrcPortName());
        setErrorPortName(route.getErrorPortName());
        setSrcPortType(route.getSrcPortType());
        setErrorPortType(route.getErrorPortType());
        setSrcPortUserName(route.getSrcPortUserName());
        setSrcPortPassword(route.getSrcPortPassword());
        setSpecifiedSourceDestinationUsed(route.isSpecifiedSourceDestinationUsed());
        setSpecifiedSrcPortName(route.getSpecifiedSrcPortName());

        setTrgtPortName(route.getTrgtPortName());
        setTrgtPortType(route.getTrgtPortType());
        setTrgtPortUserName(route.getTrgtPortUserName());
        setTrgtPortPassword(route.getTrgtPortPassword());
        setSpecifiedTargetDestinationUsed(route.isSpecifiedTargetDestinationUsed());
        setSpecifiedTrgtPortName(route.getSpecifiedTrgtPortName());

        setTrgtTransportProtocol(route.getTrgtTransportProtocol());
        setTrgtSecurityProtocol(route.getTrgtSecurityProtocol());
        setTrgtSecurityManager(route.getTrgtSecurityManager());

        setTrgtPortProxyEnabled(route.isTrgtPortProxyEnabled());
        setTrgtProxyURL(route.getTrgtProxyURL());
        setTrgtProxyPrincipal(route.getTrgtProxyPrincipal());
        setTrgtProxyCredentials(route.getTrgtProxyCredentials());
        setTrgtProxyAuthReam(route.getTrgtProxyAuthReam());

        setTargetApplicationGUID(route.getTargetApplicationGUID());
        setTargetAppVersion(route.getTargetAppVersion());
        setTrgtServInst(route.getActualTrgtServInst());
        setActualTrgtServInst(route.getActualTrgtServInst());
        setTrgtNodeName(route.getTrgtNodeName());

        setSelectors(route.getSelectors());

        setShortDescription(route.getShortDescription());
        setLongDescription(route.getLongDescription());
    }

    /**
     * @param is
     * @param versionNo
     * @exception IOException
     */
    public void fromStream(DataInput is, int versionNo)
        throws IOException
    {
        super.fromStream(is, versionNo);

        String temp = UTFReaderWriter.readUTF(is);

        if (temp.equals(""))
        {
            m_strSrcNode = null;
        }
        else
        {
            m_strSrcNode = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strSrcAppInstID = null;
        }
        else
        {
            m_strSrcAppInstID = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strSrcAppVersion = null;
        }
        else
        {
            m_strSrcAppVersion = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strSrcServGUID = null;
        }
        else
        {
            m_strSrcServGUID = temp;
        }

//        temp = UTFReaderWriter.readUTF(is);
//        if (temp.equals(""))
//        {
//            m_strTgtAppInstID = null;
//        }
//        else
//        {
//            m_strTgtAppInstID = temp;
//        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strTgtServGUID = null;
        }
        else
        {
            m_strTgtServGUID = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strSrcServVersion = null;
        }
        else
        {
            m_strSrcServVersion = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        if (temp.equals(""))
        {
            m_strTgtServVersion = null;
        }
        else
        {
            m_strTgtServVersion = temp;
        }

        temp = UTFReaderWriter.readUTF(is);
        loggerId = temp.equals("")?null:temp;
        maxLogLevel = is.readInt();
    }

    /**
     * @param out
     * @param versionNo
     * @exception IOException
     */
    public void toStream(DataOutput out, int versionNo)
        throws IOException
    {
        super.toStream(out, versionNo);

        if (m_strSrcNode != null)
        {
            UTFReaderWriter.writeUTF(out, m_strSrcNode);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strSrcAppInstID != null)
        {
            UTFReaderWriter.writeUTF(out, m_strSrcAppInstID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strSrcAppVersion != null)
        {
            UTFReaderWriter.writeUTF(out, m_strSrcAppVersion);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strSrcServGUID != null)
        {
            UTFReaderWriter.writeUTF(out, m_strSrcServGUID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

//        if (m_strTgtAppInstID != null)
//        {
//            UTFReaderWriter.writeUTF(out, m_strTgtAppInstID);
//        }
//        else
//        {
//            UTFReaderWriter.writeUTF(out, "");
//        }

        if (m_strTgtServGUID != null)
        {
            UTFReaderWriter.writeUTF(out, m_strTgtServGUID);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strSrcServVersion != null)
        {
            UTFReaderWriter.writeUTF(out, m_strSrcServVersion);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }

        if (m_strTgtServVersion != null)
        {
            UTFReaderWriter.writeUTF(out, m_strTgtServVersion);
        }
        else
        {
            UTFReaderWriter.writeUTF(out, "");
        }
        if(loggerId!=null)
            UTFReaderWriter.writeUTF(out, loggerId);
        else
            UTFReaderWriter.writeUTF(out, "");
        out.writeInt(maxLogLevel);


    }

    /**
     *  Returns the String representation of this event.
     *
     * @return Description of the Return Value
     */
    public String toString()
    {
        String baseString = super.toString();
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(baseString);
        strBuf.append("");
        strBuf.append("Route Info Details ");
        strBuf.append("[");
        strBuf.append("Source application instance ID = ");
        strBuf.append(m_strSrcAppInstID);
        strBuf.append(", ");
        strBuf.append("Source application instance Version = ");
        strBuf.append(m_strSrcAppVersion);
        strBuf.append(", ");
        strBuf.append("Source Node = ");
        strBuf.append(m_strSrcNode);
        strBuf.append(", ");
        strBuf.append("Source Service GUID = ");
        strBuf.append(m_strSrcServGUID);
        strBuf.append(", ");
        strBuf.append("Source Service Version = ");
        strBuf.append(m_strSrcServVersion);

        strBuf.append(", ");
//        strBuf.append("Target application instance ID = ");
//        strBuf.append(m_strTgtAppInstID);
//        strBuf.append(", ");
        strBuf.append("Target Service GUID = ");
        strBuf.append(m_strTgtServGUID);
        strBuf.append(", ");
        strBuf.append("Target Service Version = ");
        strBuf.append(m_strTgtServVersion);

        strBuf.append("]");
        return strBuf.toString();
    }


    /**
     * @param obj
     * @return boolean specifying whethe the DMI objects are equal
     */
    public boolean equals(Object obj)
    {
        RouteInfo thisObj = null;

        try
        {
            thisObj = (RouteInfo) obj;
        }
        catch (ClassCastException e)
        {
        }

        if (thisObj == null)
            return false;

        thisObj = (RouteInfo) obj;

        if (DmiEqualsUtil.checkStringEquality(thisObj.getSrcAppInstID(), getSrcAppInstID())
            && DmiEqualsUtil.checkStringEquality(thisObj.getSrcNode(), getSrcNode())
            && super.equals(obj))
        {
            return true;
        }
        return false;
    }

    /*-------------------------------------------------[ Logger Id ]---------------------------------------------------*/

    private String loggerId;
    public void setLoggerId(String loggerId){
        this.loggerId = loggerId;
    }

    public String getLoggerId(){
        return loggerId;
    }

    /*-------------------------------------------------[ LogModules ]---------------------------------------------------*/

    private int maxLogLevel;

    public void setMaxLoglevel(int maxLogLevel){
        this.maxLogLevel = maxLogLevel;
    }

    public int getMaxLogLevel(){
        return maxLogLevel;
    }
}
