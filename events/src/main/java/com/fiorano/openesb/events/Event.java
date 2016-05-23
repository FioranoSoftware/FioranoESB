/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.events;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.utils.UTFReaderWriter;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public abstract class Event extends DmiObject
{
    public enum EventCategory {
    INFORMATION, WARNING, ERROR
}

    private EventCategory m_dEventCategory = EventCategory.INFORMATION;

    private long m_generationDate;

    private int m_iBuildVersionNo;

    /**
     * This variable denotes whether this event object represents an "event" or
     * an "alert"
     * Default is false ( => represents an event).
     */
    private boolean m_isAlert = false;
    private int m_dEventID;
    private String m_dEventStatus;

    // scope of event is identified as --
    // for a system event - Tifosi
    // for a service event - applicationGUID
    //  Fix for bugID: 7782. Let's default it to TIFOSI.
    private String m_strEventScope = "TIFOSI";

    private long m_expiryTime;
    private boolean m_bIsPersistent;

    // source of event is identified as --
    // for a system event - tps name
    // for a service event - service instance name
    private String m_strEventSource;

    //Added by Bhuvan 18/10/02
    //destination (sink) of event is identified as --
    //for a service event - sp name
    //for a system event (published by tps) - sp name
    //The default value is ALL
    private String m_strEventSink = "ALL";

    // module of event is identified as --
    // for a system event - ThreadManager
    // for a service event - (for eg. chatUI for a ChatService)
    private String m_strEventModule;

    private String m_strEventDescription;

    /**
     *  Gets the type of event for this <code>TifosiEvent</code> object.
     *
     *@return    The eventType value
     *@since     Tifosi2.0
     */
    public abstract EventType getEventType();

    /**
     *  Gets the scope of the event.For a service event, the scope is the name
     *  of the application while for the System Event, the scope is "Tifosi".
     *
     *@return    The String representing the scope of event.
     *@see       #setEventScope(String)
     */
    public String getEventScope()
    {
        return m_strEventScope;
    }

    /**
     * Sets the build version of this Event Object.
     * Build version is used to control compatibility of this event object
     * between the different versions of Fiorano SOA product.
     *
     * @param  buildVersionNo
     */
    public void setBuildVersionNo(int buildVersionNo)
    {
        m_iBuildVersionNo = buildVersionNo;
    }

    /**
     * Gets the build version of this Event Object.
     * Build version is used to control compatibility of this event object
     * between the different versions of Fiorano SOA product.
     *
     * @return  int value represnting the build version number of this event object
     */
    public int getBuildVersionNo()
    {
        return m_iBuildVersionNo;
    }

    /**
     *  Sets the scope of the event.For a service event, the scope is the name
     *  of the application while for the System Event, the scope is "Tifosi".
     *
     *@param  eventScope  The String representing the scope of event.
     *@see                #setEventScope(java.lang.String)
     */
    public void setEventScope(String eventScope)
    {
        m_strEventScope = eventScope;
    }

    /**
     *  This method sets the module name for this event. Each event belongs to
     *  particular module. This method sets the module name for the event.
     *
     *@param  eventModule  The name of the module to which this event belongs.
     *@see                 #getEventModule()
     */
    public void setEventModule(String eventModule)
    {
        m_strEventModule = eventModule;
    }

    /**
     *  This method gets the module name for this event. Each event belongs to
     *  particular module. This method gets the module name for the event.
     *
     *@return    The name of the module to which this event belongs.
     *@see       #setEventModule(java.lang.String)
     */
    public String getEventModule()
    {
        return m_strEventModule;
    }

    /**
     *  This method sets the source of the event in this <code>TifosiEvent</code>
     *  object.The source of the service event is the serviceInstanceName while
     *  for system event it is the name of the FPS from (or for in case of TES
     *  events) which they are generated.
     *
     *@param  source  The source of the event.
     *@see            #getSource()
     *@since          Tifosi2.0
     */
    public void setSource(String source)
    {
        m_strEventSource = source;
    }

    /**
     *  This method gets the source of the event in this <code>TifosiEvent</code>
     *  object.The source of the service event is the serviceInstanceName while
     *  for system event it is the name of the FPS from (or for in case of TES
     *  events) which they are generated.
     *
     *@return    The source of the event.
     *@see       #setSource(java.lang.String)
     *@since     Tifosi2.0
     */
    public String getSource()
    {
        return m_strEventSource;
    }

    //Added by Bhuvan 21/10
    /**
     * This method sets the sink (destination) of the event
     * @author Bhuvan 21/10
     * @param source
     */

    public void setSink(String sink){
        m_strEventSink = sink;
    }

    /**
     * This method gets the sink (destination) of the event
     * @author Bhuvan 21/10
     * @param expiryTime
     */

    public String getSink(){
        return m_strEventSink;
    }


    /**
     *  This method sets the expiryTime for this <code>TifosiEvent</code>
     *  object. The expiryTime of the event specifies the time after which this
     *  event will be deleted from the TES database.
     *
     *@param  expiryTime  The expiryTime value in milliseconds.
     *@see                #getExpiryTime()
     */
    public void setExpiryTime(long expiryTime)
    {
        m_expiryTime = expiryTime;
    }

    /**
     *  This method gets the expiryTime for this <code>TifosiEvent</code>
     *  object. The expiryTime of the event specifies the time after which this
     *  event will be deleted from the TES database.
     *
     *@return    The expiryTime value in milliseconds.
     *@see       #setExpiryTime(long)
     */
    public long getExpiryTime()
    {
        return m_expiryTime;
    }

    /**
     *  This method sets the isPersistent value in this <code>TifosiEvent</code>
     *  object. The isPersistent parameter determines if this event needs to be
     *  stored in TES database.
     *
     *@param  isPersistent  The boolean parameter determines if the event needs
     *      to be stored in TES database.
     *@see                  #getIsPersistent()
     */
    public void setIsPersistent(boolean isPersistent)
    {
        m_bIsPersistent = isPersistent;
    }
    /**
     * This method returns whether the event supplied and
     * actual event generated is equal
     * This is a logical equal to than actual one.
     * Returns true if event type, status, module, category, scope, Sink and isAlert matches
     *
     * @param that event to be compared with
     * @return  whether supplied event is equal or not
     * @since ESB4.0
     */
    public abstract boolean isIdentical(Event that);

    /**
     *  This method gets the isPersistent value in this <code>TifosiEvent</code>
     *  object. The isPersistent parameter determines if this event needs to be
     *  stored in TES database.
     *
     *@return    The boolean parameter determines if the event needs to be
     *      stored in TES database.
     *@see       #setIsPersistent(boolean)
     */
    public boolean getIsPersistent()
    {
        return m_bIsPersistent;
    }

    /**
     *  This method sets the event category of this <code>TifosiEvent</code>
     *  object.The possible values of category of events are NORMAL_CATEGORY,
     *  WARNING_CATEGORY and EXCEPTION_CATEGORY.
     *
     *@param  category  The new value of event category.
     *@see              #getEventCategory()
     *@since            Tifosi2.0
     */
    public void setEventCategory(EventCategory category)
    {
        m_dEventCategory = category;
    }

    /**
     *  This method gets the event category of this <code>TifosiEvent</code>
     *  object. The possible values of category of events are NORMAL_CATEGORY,
     *  WARNING_CATEGORY and EXCEPTION_CATEGORY.
     *
     *@return    Event category for this object
     *@see       #setEventCategory(com.fiorano.openesb.events.Event.EventCategory)
     *@since     Tifosi2.0
     */
    public EventCategory getEventCategory()
    {
        return m_dEventCategory;
    }


    /**
     *  This method sets the generation date of this <code>TifosiEvent</code>
     *  object.
     *
     *@param  time  The new eventGenerationDate value
     *@see          #getEventGenerationDate()
     *@since        Tifosi2.0
     */
    public void setEventGenerationDate(long time)
    {
        m_generationDate = time;
    }


    /**
     *  This method gets the generation date from this <code>TifosiEvent</code>
     *  object.
     *
     *@return    The eventGenerationDate value
     *@see       #setEventGenerationDate(long)
     *@since     Tifosi2.0
     */
    public Date getEventGenerationDate()
    {
        return new Date(m_generationDate);
    }


    /**
     *  This method sets the event ID of this <code>TifosiEvent</code> object.
     *
     *@param  eventID  The string to be set as ID for this TifosiEvent.
     *@see             #getEventID()
     *@since           Tifosi2.0
     */
    public void setEventID(int eventID)
    {
        m_dEventID = eventID;
    }


    /**
     *  This method gets the event ID from this <code>TifosiEvent</code> object.
     *
     *@return    ID for this TifosiEvent
     *@see       #setEventID(int)
     *@since     Tifosi2.0
     */
    public int getEventID()
    {
        return m_dEventID;
    }


    /**
     *  Sets the status for this <code>TifosiEvent</code> object.
     *
     *@param  status  The string to be set as event status.
     *@see            #getEventStatus()
     *@since          Tifosi2.0
     */
    public void setEventStatus(String status)
    {
        m_dEventStatus = status;
    }


    /**
     *  Gets the status from this <code>TifosiEvent</code> object.
     *
     *@return    The status of this TifosiEvent.
     *@see       #setEventStatus(java.lang.String)
     *@since     Tifosi2.0
     */
    public String getEventStatus()
    {
        return m_dEventStatus;
    }

    /**
     *  Sets the event description of this <code>TifosiEvent</code> object.
     *
     *@param  strEventDescription  The string to be set as description of this
     *      event.
     *@see                         #getEventDescription()
     *@since                       Tifosi2.0
     */
    public void setEventDescription(String strEventDescription)
    {
        m_strEventDescription = strEventDescription;
    }

    /**
     *  Gets the description of this <code>TifosiEvent</code> object.
     *
     *@return    Description for this TifosiEvent.
     *@see       #setEventDescription(java.lang.String)
     *@since     Tifosi2.0
     */
    public String getEventDescription()
    {
        return m_strEventDescription;
    }

    /**
     *  Sets all the fields of this <code>TifosiEvent</code> object using the
     *  specified byte array argument.
     *
     *@param  bytes            Byte array containing all the field values of
     *      TifosiEvent.
     *@exception IOException  If an error occurs while setting the field values
     *      of TifosiEvent.
     *@see                     #getEventValues()
     *@since                   Tifosi2.0
     */
    public void setEventValues(byte[] bytes, int versionNo)
            throws IOException
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        m_dEventCategory = EventCategory.valueOf(readUTF(dis));
        m_strEventScope = readUTF(dis);
        m_strEventSource = readUTF(dis);

        //Added by Bhuvan 22/10/02
        //Set the sink also
        m_strEventSink = readUTF(dis);

        m_strEventModule = readUTF(dis);
        m_dEventID = dis.readInt();
        m_dEventStatus = readUTF(dis);
        m_generationDate = dis.readLong();
        m_strEventDescription = readUTF(dis);
        m_bIsPersistent = dis.readBoolean();
        m_expiryTime = dis.readLong();
        m_isAlert = dis.readBoolean();
    }


    /**
     *  Gets the byte array representation of this <code>TifosiEvent</code>
     *  object.
     *
     *@return                  Bytearray value of this object.
     *@exception  IOException  If an error occurs while getting the bytearray
     *      for specified TifosiEvent object.
     *@see                     #setEventValues(byte[])
     *@since                   Tifosi2.0
     */
    public byte[] getEventValues(int versionNo)
            throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        writeUTF(dos, m_dEventCategory.toString());
        writeUTF(dos, m_strEventScope);
        writeUTF(dos, m_strEventSource);

        //Added by Bhuvan on 22/10/02
        //write the event sink also
        writeUTF(dos,m_strEventSink);

        writeUTF(dos, m_strEventModule);
        dos.writeInt(m_dEventID);
        writeUTF(dos, m_dEventStatus);
        dos.writeLong(m_generationDate);
        writeUTF(dos, m_strEventDescription);
        dos.writeBoolean(m_bIsPersistent);
        dos.writeLong(m_expiryTime);
        dos.writeBoolean(m_isAlert);
        return bos.toByteArray();
    }


    /**
     *  This method is invoked to write the specified <code>String</code> to the
     *  specified <code>DataOutput</code> object.
     *
     *@param  out              Object of DataOutput stream
     *@param  str              The string variable to be written to the output
     *      DataOutput stream
     *@exception  IOException  If an error occurs while writing UTF to the
     *      specified DataOutput stream
     *@see                     #readUTF(DataInput)
     *@since                   Tifosi2.0
     */
    public void writeUTF(DataOutput out, String str)
            throws IOException
    {
        if (str == null)
        {
            out.writeInt(-1);
        }
        else
        {
            out.writeInt(1);
            UTFReaderWriter.writeUTF(out, str);
        }
    }


    /**
     *  This method returns a UTF representation of the specified <code>DataInput</code>
     *  stream.
     *
     *@param  is               Object of DataInput stream
     *@return                  The string read from stream
     *@exception  IOException  If an error occurs while reading UTF from the
     *      specified DataInput stream
     *@see                     #writeUTF(DataOutput, java.lang.String)
     *@since                   Tifosi2.0
     */
    public String readUTF(DataInput is)
            throws IOException
    {
        int isNotNull = is.readInt();
        if (isNotNull == 1)
        {
            return UTFReaderWriter.readUTF(is);
        }
        else
        {
            return null;
        }
    }


    /**
     *  This method writes a long to the specified <code>DataOutput</code>
     *  stream.
     *
     *@param  out              Object of DataOutput stream
     *@param  longValue        Variable of type long to be written.
     *@exception  IOException  If an error occurs while writing a long to the
     *      specified DataOutput stream
     *@see                     #readLong(DataInput)
     *@since                   Tifosi2.0
     */
    public void writeLong(DataOutput out, long longValue)
            throws IOException
    {
        out.writeLong(longValue);
    }


    /**
     *  This method reads a long from the specified <code>DataInput</code>
     *  stream.
     *
     *@param  is               Object of DataInput
     *@return                  Variable of type long
     *@exception  IOException  If an error occurs while reading long from the
     *      specified DataInput stream
     *@see                     #writeLong(DataOutput, long)
     *@since                   Tifosi2.0
     */
    public long readLong(DataInput is)
            throws IOException
    {
        return is.readLong();
    }


    /**
     *  This method reads this <code>TifosiEvent</code> object from the
     *  specified input stream .
     *
     *@param  is                       DataInput object
     *@exception  java.io.IOException  Description of the Exception
     *@see                             #toStream(DataOutput)
     *@since                           Tifosi2.0
     */
    public void fromStream(java.io.DataInput is, int versionNo)
            throws java.io.IOException
    {
        m_iBuildVersionNo = is.readInt();
        int length = is.readInt();
        byte[] array = new byte[length];
        is.readFully(array);
        setEventValues(array, versionNo);
    }


    /**
     *  This method writes this <code>TifosiEvent</code> object to the specified
     *  output stream object.
     *
     *@param  out                      DataOutput object
     *@exception  java.io.IOException  Description of the Exception
     *@see                             #fromStream(DataInput)
     *@since                           Tifosi2.0
     */
    public void toStream(java.io.DataOutput out, int versionNo)
            throws java.io.IOException
    {
        out.writeInt(m_iBuildVersionNo);

        byte[] array = getEventValues(versionNo);
        out.writeInt(array.length);
        out.write(array);
    }

    /**
     *  This method sets all the field values of this <code>TifosiEvent</code>
     *  object, using the specified XML string.
     *
     *@param  resultSet            The new fieldValues value
     *@exception  com.fiorano.openesb.utils.exception.FioranoException  If an error occurs while parsing the
     *      XMLString
     *@since                       Tifosi2.0
     */
    public void setFieldValues(ResultSet resultSet)
            throws FioranoException
    {
        try
        {
            m_dEventID = resultSet.getInt("EVENT_ID");
            m_dEventCategory = EventCategory.valueOf(resultSet.getString("EVENT_CATEGORY"));
            m_generationDate = resultSet.getTimestamp("GENERATION_DATE").getTime();
            m_strEventSource = resultSet.getString("EVENT_SOURCE");
            m_strEventScope = resultSet.getString("EVENT_SCOPE");
            m_strEventModule = resultSet.getString("EVENT_MODULE");
            m_strEventDescription = resultSet.getString("DESCRIPTION");
            m_dEventStatus = resultSet.getString("EVENT_STATUS");
            m_expiryTime = resultSet.getTimestamp("EXPIRY_TIME").getTime();

            // PATCH
            // TO HANDLE MS-SQL RETURN VALUES
            // TODO: handle properly
            if(m_strEventSource != null)
                m_strEventSource.trim();
            if(m_strEventScope != null)
                m_strEventScope.trim();
            if(m_strEventModule != null)
                m_strEventModule.trim();
            if(m_strEventDescription != null)
                m_strEventDescription.trim();
            if(m_dEventStatus != null)
                m_dEventStatus.trim();
        }
        catch (SQLException se)
        {
            throw new FioranoException( se);
        }
    }


    /**
     *  This method returns the ID of this object.
     *
     *@return    The id of this object
     *@since     Tifosi2.0
     */
    public int getObjectID()
    {
        return DmiObjectTypes.TIFOSI_EVENT;
    }


    /**
     *  This method has not been implemented in this version. (This method
     *  resets the values of the data members of this object.)
     *
     *@since    Tifosi2.0
     */
    public void reset() { }

    /**
     *  This method tests whether this object of <code>TifosiEvent</code> has
     *  the required(mandatory) fields set. This method should be invoked before
     *  inserting values in the database.
     *
     *@exception  FioranoException  If the object is not valid
     *@since                       Tifosi2.0
     */
    public void validate()
            throws FioranoException { }

    /**
     *  This method is used to get the String representation of this <code>TifosiEvent</code>
     *  object.
     *
     *@return    The String representation of this object.
     *@see       #fromStream(DataInput)
     *@since     Tifosi2.0
     */
    public String toString()
    {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("TifosiEvent Details ");
        strBuf.append("\n");
        strBuf.append("=================");
        strBuf.append("\n\t");
        strBuf.append("Event Category = ");
        strBuf.append(m_dEventCategory.toString());
        strBuf.append("\n\t");
        strBuf.append("GenerationDate = ");
        strBuf.append(new Date(m_generationDate));
        strBuf.append("\n\t");
        strBuf.append("EventID = ");
        strBuf.append(m_dEventID);
        strBuf.append("\n\t");
        strBuf.append("Event Status = ");
        strBuf.append(m_dEventStatus);
        strBuf.append("\n\t");
        strBuf.append("Event Scope = ");
        strBuf.append(m_strEventScope);
        strBuf.append("\n\t");
        strBuf.append("Event Source = ");
        strBuf.append(m_strEventSource);
        strBuf.append("\n\t");
        strBuf.append("Event Module = ");
        strBuf.append(m_strEventModule);
        strBuf.append("\n\t");
        strBuf.append("Event Description = ");
        strBuf.append(m_strEventDescription);
        strBuf.append("\n\t");
        strBuf.append("Expiry Time = ");
        strBuf.append(m_expiryTime);
        strBuf.append("\n\t");
        strBuf.append("Is Persistent = ");
        strBuf.append(m_bIsPersistent);

        return strBuf.toString();
    }

    /**
     * This method returns whether this object is an Event or an Alert
     * @return true if the object is an alert, false if the object is an event
     */
    public boolean getIsAlert()
    {
        return m_isAlert;
    }

    /**
     * This method is used to set whether this object is an Event or an Alert.
     * @param isAlert true if the object is an alert, false if the object is an event
     */
    public void setIsAlert(boolean isAlert)
    {
        m_isAlert = isAlert;
    }

    /**
     *  This method returns the XML string representation of <code>TifosiEvent</code> object.
     *
     *@return    The xml string representation of <code>TifosiEvent</code> object.
     */
    public String toXML()
    {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<TifosiEvent>");
        strBuf.append("<Category>");
        strBuf.append(m_dEventCategory.toString());
        strBuf.append("</Category>");
        strBuf.append("<GenerationDate>");
        strBuf.append(new Date(m_generationDate));
        strBuf.append("</GenerationDate>");
        strBuf.append("<EventID>");
        strBuf.append(m_dEventID);
        strBuf.append("</EventID>");
        strBuf.append("<Status>");
        strBuf.append(m_dEventStatus);
        strBuf.append("</Status>");
        strBuf.append("<Scope>");
        strBuf.append(m_strEventScope);
        strBuf.append("</Scope>");
        strBuf.append("<Source>");
        strBuf.append(m_strEventSource);
        strBuf.append("</Source>");
        strBuf.append("<Module>");
        strBuf.append(m_strEventModule);
        strBuf.append("</Module>");
        strBuf.append("<Description>");
        strBuf.append(m_strEventDescription);
        strBuf.append("</Description>");
        strBuf.append("</TifosiEvent>");
        return strBuf.toString();
    }
}
