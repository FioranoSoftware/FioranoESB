/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.Remote;

public interface IDebugManager extends Remote {

	public void deleteServiceInstance(String name);

	public void startServiceInstance(String name);

	public void stopServiceInstance(String name);

	public void createRoute(String name, String source, String target);

	public void changeRouteSource(String name, String oldSource, String newSource);

	public void changeRouteTarget(String name, String oldTarget, String newTarget);

	public void deleteRoute(String name);

	public void setConfig(String serviceInstanceName, byte[] config);

}

//(*) Delete a service instance
//(*) Stop a service instance
//(*) Start a service instance
//(*) Change the configuration of a service instance
//(*) Create a route
//(*) Change the route
//(*) Delete the route

//(*) Create a service instance

//(*) Change the configuration of a service instance
//(*) Change log parameters 
//(*) Add Breaktpoint
//(*) Remove Breakpoint
