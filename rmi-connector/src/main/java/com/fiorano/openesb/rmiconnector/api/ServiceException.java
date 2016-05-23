/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

public class ServiceException extends Exception {


   	public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
	public static final String ZIP_TOO_BIG_ERROR = "ZIP_TOO_BIG_ERROR";
    private static final long serialVersionUID = 869149237802891848L;

    private final String errorCode;



    /**
     * Default Constructor
     */
	public ServiceException() {
		this("Internal Server Error", INTERNAL_ERROR);
	}

    /**
     * Constructs a service exception with specified error code and exception
     * @param errorCode Error code of the exception
     * @param e Exception thrown
     */
    public ServiceException(String errorCode, Exception e){
        super(e.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs a service exception with specified message
     * @param message error message
     */
	public ServiceException(String message) {
		this(INTERNAL_ERROR,message);
	}

    /**
     * Constructs a service exception with specified message and error code
     * @param message error message
     * @param errorCode error code
     */
	public ServiceException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

    /**
     * Returns error code
     * @return int - Error code
     */
	public String getErrorCode() {
		return errorCode;
	}

}
