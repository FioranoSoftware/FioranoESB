/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Enumeration;
import java.util.Vector;

public class FioranoStackSerializer {
  public FioranoStackSerializer() {
  }
  public static void writeElement(String object, String value, XMLStreamWriter writer) throws XMLStreamException
  {
      if(object != null)
      {
          if(value == null)
              value="";

          writer.writeStartElement(object);
          writer.writeCharacters(value);
          writer.writeEndElement();
      }
  }
  public static void writeVector(String object, Vector vals, XMLStreamWriter writer) throws XMLStreamException
  {
      if (vals != null && vals.size() > 0)
      {
          Enumeration enum1 = vals.elements();
          while (enum1.hasMoreElements())
          {
              String name = (String) enum1.nextElement();

              writer.writeStartElement(object);
              writer.writeCharacters(name);
              writer.writeEndElement();
          }
      }
    }
}
