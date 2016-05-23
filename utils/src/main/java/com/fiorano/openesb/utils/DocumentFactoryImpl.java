/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import com.fiorano.openesb.utils.exception.FioranoException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;

public class DocumentFactoryImpl
{
	/**
	*  Constructs a new DocumentFactoryImpl.
	*/
	public DocumentFactoryImpl ()
	{
	}

	/**
	*  Create an empty DOM document.
	*
	*  @return The newly created document.
	*
	*  @exception FioranoException Thrown if an error occurs while
	*             creating the document.
	*/
	public Document createDocument ()
		throws FioranoException
	{
		try
		{
			DocumentBuilder db = XMLUtils.createDocumentBuilder ();
			return db.newDocument ();
		}
		catch (Exception e)
		{
			throw new FioranoException (e);
		}
	}
}

