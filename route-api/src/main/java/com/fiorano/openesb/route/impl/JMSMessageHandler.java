/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import java.io.CharArrayWriter;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JMSMessageHandler extends DefaultHandler{
    private String  propertyName, propertyType, propertyValue,removePropertyName;
    private Message record;
    private CharArrayWriter contents = new CharArrayWriter();

    public JMSMessageHandler(Message msg)
    {
        this.record = msg;
    }

    public final void startElement(String uri, String localName, String qName, Attributes attributes)
    {
        contents.reset();
        if (qName.equals("Property"))
        {
            propertyName = attributes.getValue("name");
            propertyType = attributes.getValue("type");
        } else  if (qName.equals("RemoveProperty")) {
            removePropertyName = attributes.getValue("name");
        }
    }

    public final void characters(char[] ch, int start, int length)
    {
        // accumulate the contents into a buffer.
        contents.write(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        try
        {
            if (qName.equals("Property"))
            {
                propertyValue = contents.toString();
                setProperty();
            }
            else if (qName.equals("CorrelationID"))
            {
                record.setJMSCorrelationID(contents.toString());
            }
            else if (qName.equals("Text"))
            {
                if (record instanceof TextMessage)
                    ((TextMessage) record).setText(contents.toString());
            } else if ("RemoveProperty".equals(qName)) {
                if (record instanceof Message) {
                   throw new UnsupportedOperationException("Remove operation is not supported");
                }
            }
        }
        catch (JMSException e)
        {
            throw new SAXException(e);
        }
        contents.reset();
    }

    private void setProperty()
        throws JMSException
    {
        if ("Byte".equals(propertyType))
            record.setByteProperty(propertyName, Byte.parseByte(propertyValue));
        else if ("Short".equals(propertyType))
            record.setShortProperty(propertyName, Short.parseShort(propertyValue));
        else if ("Integer".equals(propertyType))
            record.setIntProperty(propertyName, Integer.parseInt(propertyValue));
        else if ("Long".equals(propertyType))
            record.setLongProperty(propertyName, Long.parseLong(propertyValue));
        else if ("Float".equals(propertyType))
            record.setFloatProperty(propertyName, Float.parseFloat(propertyValue));
        else if ("Double".equals(propertyType))
            record.setDoubleProperty(propertyName, Double.parseDouble(propertyValue));
        else if ("Boolean".equals(propertyType))
            record.setBooleanProperty(propertyName, Boolean.valueOf(propertyValue).booleanValue());
        else
            record.setStringProperty(propertyName, propertyValue);
        propertyName = propertyValue = propertyType = null;
    }
}

