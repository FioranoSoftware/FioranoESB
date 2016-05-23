/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.utils.MessagePropertyNames;
import com.fiorano.openesb.utils.StringUtil;
import com.owlike.genson.Genson;

import javax.jms.*;
import java.io.IOException;
import java.util.*;

public class JmsMessageUtil {
    // InTime denotes the time (in milliseconds) at which the message
    // enters any input port of the service. If a service is
    // originator of a message then the value will be -1. This is
    // useful for state based workflow.
    //
    public final static long IN_TIME_DEF = -1;

    //  The time (in milliseconds) at which the message exits any output port
    //  of the service. The value will be -1 whenever read in an onMessage().
    //  This is useful for state based workflow.
    //
    public final static long OUT_TIME_DEF = -1;

    //  The total time taken (in milliseconds) by a service during which message
    //  remained inside a service. It is equal to the difference between the
    //  outTime and inTime. If either of inTime/outTime is -1 then this value
    //  will also be -1. Of course, this is useful for state based workflow only.
    //
    public final static long TOTAL_TIME_DEF = -1;

    //  The name of the TPS from which the message has come to target service.
    //
    public final static String SOURCE_FPS_NAME_DEF = null;

    //  Comment associated with sbw state
    //
    public final static String COMMENT_DEF = null;

    //  Stores the user defined document ID.
    //
    public final static String USER_DEFINED_DOC_ID_DEF = null;

    //  Stores the workflowInstID.
    //
    public final static String WORK_FLOW_INST_ID_DEF = null;

    //  Stores the component inst Name
    //
    public final static String COMP_INST_NAME_DEF = null;

    //  Stores the application inst Name
    //
    public final static String EVENT_PROCESS_NAME_DEF = null;

    //  Stores the application inst Name
    //
    public final static String EVENT_PROCESS_EVN_DEF = null;

    //  Stores the application inst version
    //
    public final static String EVENT_PROCESS_VERSION_DEF = null;

    //  Stores the Attachments
    //
    public final static Hashtable ATTACHMENT_TABLE_DEF = null;
    public final static Hashtable ATTACHMENT_PROP_DEF = null;

    //  Bytes Data
    //
    public final static byte[] BYTES_DATA_DEF = null;

    //  Text Data
    //
    public final static String TEXT_DATA_DEF = null;

    //  Stores the data table
    //
    public final static Hashtable DATA_TABLE_DEF = null;
    public final static Hashtable DATA_PROP_DEF = null;

    // the port name
    //
    public final static String PORT_NAME_DEF = null;

    // The document ID
    //
    public final static String DOCUMENT_ID_DEF = null;

    // The event Description string
    //
    public final static String EVENT_DESCRIPTION_DEF = null;

    // Default event ID
    //
    public final static int EVENT_ID_DEF = 2000;

    // Default event module name
    //
    public final static String EVENT_MODULE_DEF = null;

    // Default event status
    //
    public final static String EVENT_STATUS_DEF = null;

    // Default event category name
    //
    public final static String EVENT_CATEGORY_DEF = null;

    public static long getInTime(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.IN_TIME)) {
            return IN_TIME_DEF;
        }

        return message.getLongProperty(MessagePropertyNames.IN_TIME);
    }

    public static String getEventDescription(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_DESCRIPTION)) {
            return EVENT_DESCRIPTION_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_DESCRIPTION);
    }

    public static int getEventID(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_ID)) {
            return EVENT_ID_DEF;
        }

        return message.getIntProperty(MessagePropertyNames.EVENT_ID);
    }

    public static long getOutTime(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.OUT_TIME)) {
            return OUT_TIME_DEF;
        }

        return message.getLongProperty(MessagePropertyNames.OUT_TIME);
    }

    public static long getTotalTime(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.TOTAL_TIME)) {
            return TOTAL_TIME_DEF;
        }

        return message.getLongProperty(MessagePropertyNames.TOTAL_TIME);
    }


    public static String getSourceFPSName(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.SOURCE)) {
            return SOURCE_FPS_NAME_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.SOURCE);
    }

    public static String getPortName(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.PORT_NAME)) {
            return PORT_NAME_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.PORT_NAME);
    }

    public static String getDocumentID(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.DOCUMENT_ID)) {
            return DOCUMENT_ID_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.DOCUMENT_ID);
    }

    public static boolean getIsAlert(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.IS_ALERT)) {
            return false;
        }

        return message.getBooleanProperty(MessagePropertyNames.IS_ALERT);
    }

    public static String getComment(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.COMMENT)) {
            return COMMENT_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.COMMENT);
    }

    public static String getUserDefinedId(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.USER_DEFINED_DOC_ID)) {
            return USER_DEFINED_DOC_ID_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.USER_DEFINED_DOC_ID);
    }

    public static String getWorkFlowInstId(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.WORK_FLOW_INST_ID)) {
            return WORK_FLOW_INST_ID_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.WORK_FLOW_INST_ID);
    }

    public static String getSink(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.SINK)) {
            return null;
        }

        return message.getStringProperty(MessagePropertyNames.SINK);
    }

    public static String getCompInstName(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.COMP_INST_NAME)) {
            return COMP_INST_NAME_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.COMP_INST_NAME);
    }

    public static String getEventProcessEnv(Message message) throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_PROCESS_ENVIRONMENT))
            return EVENT_PROCESS_EVN_DEF;

        return message.getStringProperty(MessagePropertyNames.EVENT_PROCESS_ENVIRONMENT);
    }

    public static String getEventProcessName(Message message)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_PROCESS_NAME)) {
            return EVENT_PROCESS_NAME_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_PROCESS_NAME);
    }

    public static String getEventProcessVersion(Message message)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_PROCESS_VERSION)) {
            return EVENT_PROCESS_VERSION_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_PROCESS_VERSION);
    }

    public static String getExecutingIncr(Message message)
            throws
            JMSException {
        return message.getStringProperty(MessagePropertyNames.EXECUTING_INCR);
    }

    public static Object getAttachment(Message message, String name)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.ATTACHMENT_TABLE)) {
            return ATTACHMENT_PROP_DEF;
        }

        Hashtable attachmentTable;
        attachmentTable = (Hashtable) message.getObjectProperty(MessagePropertyNames.ATTACHMENT_TABLE);
        return attachmentTable.get(name);
    }

    public static Hashtable getAttachments(Message message)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.ATTACHMENT_TABLE)) {
            return ATTACHMENT_TABLE_DEF;
        }
        Hashtable attachments = new Hashtable();
        attachments.putAll((Map) message.getObjectProperty(MessagePropertyNames.ATTACHMENT_TABLE));
        return attachments;
    }


    public static CarryForwardContext getCarryForwardContext(Message message)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.CARRY_FORWARD_CONTEXT)) {
            return CarryForwardContext.getDefault();
        }
        String carryForwardContextJson = message.getStringProperty(MessagePropertyNames.CARRY_FORWARD_CONTEXT);
        if(!StringUtil.isEmpty(carryForwardContextJson)) {
            Genson genson = new Genson();
            CarryForwardContext carryForwardContext = genson.deserialize(carryForwardContextJson, CarryForwardContext.class);
            return carryForwardContext;
        }
        return CarryForwardContext.getDefault();
    }

    public static String getApplicationContext(Message message)
            throws
            JMSException {
        CarryForwardContext carryForwardContext = getCarryForwardContext(message);
        return carryForwardContext != null ? carryForwardContext.getAppContext() : null;
    }

    public static byte[] getBytesData(Message message)
            throws
            JMSException {
        if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;
            byte bytes[] = new byte[(int) bytesMessage.getBodyLength()];
            bytesMessage.readBytes(bytes);
            return bytes;
        }

        if (!message.propertyExists(MessagePropertyNames.BYTES_DATA)) {
            return BYTES_DATA_DEF;
        }

        return (byte[]) message.getObjectProperty(MessagePropertyNames.BYTES_DATA);
    }


    public static String getTextData(Message message)
            throws
            JMSException {
        if (message instanceof TextMessage) {
            return ((TextMessage) message).getText();
        }

        if (!message.propertyExists(MessagePropertyNames.TEXT_DATA)) {
            return TEXT_DATA_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.TEXT_DATA);
    }


    public static Object getDataEntry(Message message, Object name)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.DATA_TABLE)) {
            return DATA_PROP_DEF;
        }

        Hashtable datTable = (Hashtable) message.getObjectProperty(MessagePropertyNames.DATA_TABLE);

        return datTable.get(name);
    }


    public static Hashtable getDataTable(Message message)
            throws
            JMSException {
        if (!message.propertyExists(MessagePropertyNames.DATA_TABLE)) {
            return DATA_TABLE_DEF;
        }

        return (Hashtable) message.getObjectProperty(MessagePropertyNames.DATA_TABLE);
    }


    public static long getEventGenerationDate(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_GENERATION_DATE)) {
            return 0L;
        }

        return message.getLongProperty(MessagePropertyNames.EVENT_GENERATION_DATE);
    }

    public static String getEventModule(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_MODULE)) {
            return EVENT_MODULE_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_MODULE);
    }

    public static String getEventStatus(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_STATUS)) {
            return EVENT_STATUS_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_STATUS);
    }


    public static String getEventCategory(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_CATEGORY)) {
            return EVENT_MODULE_DEF;
        }

        return message.getStringProperty(MessagePropertyNames.EVENT_CATEGORY);
    }

    public static int getEventType(Message message)
            throws JMSException {
        if (!message.propertyExists(MessagePropertyNames.EVENT_TYPE)) {
            return 0;
        }

        return message.getIntProperty(MessagePropertyNames.EVENT_TYPE);
    }


    public static HashMap getAllProperties(Message jmsMessage)
            throws
            JMSException {
        HashMap props = new HashMap();

        if (jmsMessage == null) {
            return props;
        }

        Enumeration propNames = jmsMessage.getPropertyNames();

        while (propNames.hasMoreElements()) {
            String propName = (String) propNames.nextElement();
            Object value = getPropertyValue(propName, jmsMessage);

            props.put(propName, value);
        }

        return props;
    }


    public static Object getPropertyValue(String propertyName, Message jmsMessage)
            throws
            JMSException {
        if (jmsMessage == null) {
            return null;
        }

        if (propertyName == null) {
            return null;
        }

        return jmsMessage.getObjectProperty(propertyName);
    }


    public static String getWorkFlowStatus(Message message)
            throws JMSException {
        return message.getStringProperty(MessagePropertyNames.WORKFLOW_STATUS);
    }

    public static String getEventScope(Message message)
            throws JMSException {
        return message.getStringProperty(MessagePropertyNames.EVENT_SCOPE);
    }

    public static String getStateID(Message message)
            throws JMSException {
        return message.getStringProperty(MessagePropertyNames.STATE_ID);
    }

    public static void setPortName(Message message, String portName)
            throws JMSException {
        if (portName == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.PORT_NAME, portName);
    }

    public static void setTextData(Message message, String text)
            throws JMSException {
        if (text == null) {
            return;
        }

        if (message instanceof TextMessage) {
            ((TextMessage) message).setText(text);
        } else {
            message.setStringProperty(MessagePropertyNames.TEXT_DATA, text);
        }
    }

    public static void setEventModule(Message message, String eventModule)
            throws JMSException {
        message.setStringProperty(MessagePropertyNames.EVENT_MODULE, eventModule);
    }

    public static void setEventStatus(Message message, String eventStatus)
            throws JMSException {
        message.setStringProperty(MessagePropertyNames.EVENT_STATUS, eventStatus);
    }

    public static void setEventCategory(Message message, String eventCategory)
            throws JMSException {
        message.setStringProperty(MessagePropertyNames.EVENT_CATEGORY, eventCategory);
    }

    public static void setEventType(Message message, int eventType)
            throws JMSException {
        message.setIntProperty(MessagePropertyNames.EVENT_TYPE, eventType);
    }

    public static void setDocumentID(Message message, String docID)
            throws JMSException {
        if (docID == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.DOCUMENT_ID, docID);
    }


    public static void setEventDescription(Message message, String description)
            throws JMSException {
        if (description == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.EVENT_DESCRIPTION, description);
    }


    public static void setEventID(Message message, int eventID)
            throws JMSException {
        message.setIntProperty(MessagePropertyNames.EVENT_ID, eventID);
    }

    public static void setInTime(Message message, long intime)
            throws
            JMSException {
        message.setLongProperty(MessagePropertyNames.IN_TIME, intime);
    }

    public static void setIsAlert(Message message, boolean isAlert)
            throws JMSException {
        message.setBooleanProperty(MessagePropertyNames.IS_ALERT, isAlert);
    }


    public static void setOutTime(Message message, long time)
            throws
            JMSException {
        message.setLongProperty(MessagePropertyNames.OUT_TIME, time);
    }

    public static void setTotalTime(Message message, long time)
            throws
            JMSException {
        message.setLongProperty(MessagePropertyNames.TOTAL_TIME, time);
    }


    public static void setSourceFPSName(Message message, String fpsName)
            throws
            JMSException {
        if (fpsName == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.SOURCE, fpsName);
    }


    public static void setErrorSource(Message message, String errorSource)
            throws
            JMSException {
        if (errorSource == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.ERROR_SOURCE, errorSource);
    }


    public static void setComment(Message message, String comment)
            throws
            JMSException {
        if (comment == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.COMMENT, comment);
    }

    public static void setUserDefinedId(Message message, String id)
            throws
            JMSException {
        if (id == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.USER_DEFINED_DOC_ID, id);
    }

    public static void setWorkFlowInstId(Message message, String id)
            throws
            JMSException {
        if (id == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.WORK_FLOW_INST_ID, id);
    }


    public static void setWorkFlowStatus(Message message, String status)
            throws JMSException {
        if (status == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.WORKFLOW_STATUS, status);
    }


    public static void setEventScope(Message message, String scope)
            throws JMSException {
        if (scope == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.EVENT_SCOPE, scope);
    }

    public static void setEventGenerationDate(Message message, long date)
            throws JMSException {
        message.setLongProperty(MessagePropertyNames.EVENT_GENERATION_DATE, date);
    }


    public static void setCompInstName(Message message, String name)
            throws
            JMSException {
        if (name == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.COMP_INST_NAME, name);
    }


    public static void setEventProcessName(Message message, String name)
            throws
            JMSException {
        if (name == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.EVENT_PROCESS_NAME, name);
    }

    public static void setEventProcessEnv(Message msg, String label) throws JMSException {
        if (label == null)
            return;

        msg.setStringProperty(MessagePropertyNames.EVENT_PROCESS_ENVIRONMENT, label);

    }

    public static void setEventProcessVersion(Message message, String version)
            throws
            JMSException {
        if (version == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.EVENT_PROCESS_VERSION, version);
    }

    public static void setExecutingIncr(Message message, String value)
            throws
            JMSException {
        if (value == null) {
            return;
        }
        message.setStringProperty(MessagePropertyNames.EXECUTING_INCR, value);
    }


    public static void setAttachments(Message message, Hashtable attachmentTable)
            throws
            JMSException {
        if (attachmentTable == null) {
            return;
        }

        message.setObjectProperty(MessagePropertyNames.ATTACHMENT_TABLE, attachmentTable);
    }

    public static void setCarryForwardContext(Message message, Object carryForwardContext)
            throws
            JMSException {
        if (carryForwardContext == null) {
            return;
        }
        String carryForwardContextJson;
        //  carryForwardContextJson = fioranoJsonUtil.serialize((CarryForwardContext) carryForwardContext);
        Genson genson =  new Genson();
        carryForwardContextJson = genson.serialize(carryForwardContext);

        //message.setObjectProperty(MessagePropertyNames.CARRY_FORWARD_CONTEXT, carryForwardContext);
        message.setStringProperty(MessagePropertyNames.CARRY_FORWARD_CONTEXT,carryForwardContextJson);
    }


    public static void setBytesData(Message message, byte[] data)
            throws
            JMSException {
        if (data == null) {
            return;
        }

        if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;
            bytesMessage.clearBody();
            bytesMessage.writeBytes(data);
        } else {
            message.setObjectProperty(MessagePropertyNames.BYTES_DATA, data);
        }
    }


    public static void setDataTable(Message message, Hashtable dataTable)
            throws
            JMSException {
        if (dataTable == null) {
            return;
        }

        message.setObjectProperty(MessagePropertyNames.DATA_TABLE, dataTable);
    }

    public static void setAllProperties(Message jmsMessage, Map propertyTable)
            throws JMSException {

        if (jmsMessage == null || propertyTable == null) {
            return;
        }
        Iterator iter = propertyTable.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object name = entry.getKey();
            Object value = entry.getValue();
            if (name instanceof String) {
                setProperty((String) name, value, jmsMessage);
            }
        }
    }


    public static void setProperty(String name, Object value, Message jmsMessage)
            throws JMSException {

        if (jmsMessage == null || name == null) {
            return;
        }

        if (value instanceof Boolean) {
            jmsMessage.setBooleanProperty(name, ((Boolean) value).booleanValue());
        } else if (value instanceof Byte) {
            jmsMessage.setByteProperty(name, ((Byte) value).byteValue());
        } else if (value instanceof Double) {
            jmsMessage.setDoubleProperty(name, ((Double) value).doubleValue());
        } else if (value instanceof Float) {
            jmsMessage.setFloatProperty(name, ((Float) value).floatValue());
        } else if (value instanceof Integer) {
            jmsMessage.setIntProperty(name, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            jmsMessage.setLongProperty(name, ((Long) value).longValue());
        } else if (value instanceof Short) {
            jmsMessage.setShortProperty(name, ((Short) value).shortValue());
        } else if (value instanceof String) {
            jmsMessage.setStringProperty(name, (String) value);
        } else {
            jmsMessage.setObjectProperty(name, value);
        }

    }

    public static void setStateID(Message message, String id)
            throws JMSException {
        if (id == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.STATE_ID, id);
    }


    public static void setSink(Message message, String sink)
            throws JMSException {
        message.setStringProperty(MessagePropertyNames.SINK, sink);
    }


    public static void addAttachment(Message message, Object name,
                                     Object value)
            throws JMSException {
        Hashtable headerTable = null;

        if (!message.propertyExists(MessagePropertyNames.ATTACHMENT_TABLE)) {
            headerTable = new Hashtable();
        } else {
            headerTable = (Hashtable) message.getObjectProperty(MessagePropertyNames.ATTACHMENT_TABLE);
        }

        headerTable.put(name, value);

        if (!message.propertyExists(MessagePropertyNames.ATTACHMENT_TABLE)) {
            message.setObjectProperty(MessagePropertyNames.ATTACHMENT_TABLE, headerTable);
        }
    }


    public static void addDataEntry(Message message, Object name,
                                    Object value)
            throws JMSException {
        Hashtable dataTable = null;

        if (!message.propertyExists(MessagePropertyNames.DATA_TABLE)) {
            dataTable = new Hashtable();
        } else {
            dataTable = (Hashtable) message.getObjectProperty(MessagePropertyNames.DATA_TABLE);
        }

        dataTable.put(name, value);

        if (!message.propertyExists(MessagePropertyNames.DATA_TABLE)) {
            message.setObjectProperty(MessagePropertyNames.DATA_TABLE, dataTable);
        }
    }


    public static void cloneMessage(Message inputMessage, Message outputMessage)
            throws JMSException {

        cloneMessageExcludingProps(inputMessage, outputMessage, null);
    }


    public static void cloneMessageExcludingProps(Message inputMessage, Message outputMessage, List excludeProps)
            throws JMSException {

        if (inputMessage == null || outputMessage == null) {
            return;
        }

        String body = getTextData(inputMessage);

        if (body != null) {
            setTextData(outputMessage, body);
        }

        copyProperties(inputMessage, outputMessage, excludeProps);

        copyJMSHeaders(inputMessage, outputMessage);

        return;
    }


    public static void copyJMSHeaders(Message inputMessage, Message outputMessage)
            throws JMSException {
        Object propValue;

        if (inputMessage == null || outputMessage == null) {
            return;
        }

        propValue = inputMessage.getJMSMessageID();
        if (propValue != null) {
            outputMessage.setJMSMessageID((String) propValue);
        }

        propValue = inputMessage.getJMSDestination();
        if (propValue != null) {
            outputMessage.setJMSDestination((Destination) propValue);
        }

        propValue = inputMessage.getJMSReplyTo();
        if (propValue != null) {
            outputMessage.setJMSReplyTo((Destination) propValue);
        }

        outputMessage.setJMSTimestamp(inputMessage.getJMSTimestamp());

        propValue = inputMessage.getJMSCorrelationID();
        if (!StringUtil.isEmpty((String) propValue)) {
            outputMessage.setJMSCorrelationID((String) propValue);
        }

        outputMessage.setJMSPriority(inputMessage.getJMSPriority());

        outputMessage.setJMSExpiration(inputMessage.getJMSExpiration());

        return;
    }


    public static void makeMessageReadWrite(Message jmsMessage)
            throws JMSException {

        if (jmsMessage == null) {
            return;
        }

        String strMessage = getTextData(jmsMessage);

        if (strMessage != null) {
            jmsMessage.clearBody();
            setTextData(jmsMessage, strMessage);
        }

        HashMap propertyTable = getAllProperties(jmsMessage);

        jmsMessage.clearProperties();
        setAllProperties(jmsMessage, propertyTable);

    }

    public static void copyProperties(Message inputMessage, Message outputMessage)
            throws JMSException {
        copyProperties(inputMessage, outputMessage, null);
        return;
    }

    public static void copyProperties(Message inputMessage, Message outputMessage, List excludeProps)
            throws JMSException {

        if (inputMessage == null || outputMessage == null) {
            return;
        }

        Enumeration propNames = inputMessage.getPropertyNames();

        while (propNames.hasMoreElements()) {
            String propName = (String) propNames.nextElement();

            if (excludeProps == null || !excludeProps.contains(propName)) {
                Object value = getPropertyValue(propName, inputMessage);
                setProperty(propName, value, outputMessage);
            }
        }
    }

    public static Message createMessage(Session session, Class messageClazz) throws JMSException {
        if (session == null) {
            throw new JMSException("session cannot be null");
        }
        if (Message.class.isAssignableFrom(messageClazz)) {
            throw new JMSException("cannot create message of type: " + messageClazz.getName());
        }

        if (TextMessage.class.equals(messageClazz)) {
            return session.createTextMessage();
        } else if (StreamMessage.class.equals(messageClazz)) {
            return session.createStreamMessage();
        } else if (MapMessage.class.equals(messageClazz)) {
            return session.createMapMessage();
        } else if (ObjectMessage.class.equals(messageClazz)) {
            return session.createObjectMessage();
        } else if (BytesMessage.class.equals(messageClazz)) {
            return session.createBytesMessage();
        } else if (Message.class.equals(messageClazz)) {
            return session.createMessage();
        } else {
            throw new JMSException("cannot create message of type: " + messageClazz.getName());
        }


    }

    public static void setSourceDestinationName(Message message, String sourceDestinationName)
            throws
            JMSException {
        if (sourceDestinationName == null) {
            return;
        }

        message.setStringProperty(MessagePropertyNames.SOURCE_DESTINATION_NAME, sourceDestinationName);
    }

}

