/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.application.application.SchemaReference;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ISchemaReferenceManager extends Remote {

    /**
     * This method adds Schema Reference to Schema Repository
     *
     * @param zippedContent Content of the schema reference
     * @param completed Notifies server that sending zip contents completed
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public void addSchemaReferences(byte[] zippedContent, boolean completed) throws RemoteException, ServiceException;

    /**
     * This method adds Schema reference to Schema Repository
     *
     *
     * @param filecontent Content of the schema reference
     * @param fileName File name of the Schema
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchemaReference(byte[] filecontent, String fileName) throws RemoteException, ServiceException;

    /**
     * This method adds schema reference to schema repository
     *
     * @param schemaAsString Schema as string
     * @param fileName File name of the Schema
     * @return String - File name after Schema addition
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchemaReference(String schemaAsString, String fileName) throws RemoteException, ServiceException;

    /**
     * This method returns all Schema References associated with services in a particular Application
     * @param appGUID  application GUID for which the Schema References should be retrieved
     * @param version  application Version for which the Schema References should be retrieved
     * @return List - List of Schema References
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the failed to get schema reference for application
     */
    public List<SchemaReference> getSchemaReferences(String appGUID, float version) throws RemoteException, ServiceException;

    /**
     * This method returns Schema Reference file Content as byte array from Schema Repository
     * @param nameSpace nameSpace associated with Schema
     * @param locationHint Schema Reference File Name.
     * @return Byte Array - Byte array of schema Reference file
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the failed to get Schema Reference from Schema Repository
     */
    public byte[] getSchemaReference(String nameSpace, String locationHint) throws RemoteException, ServiceException;

    /**
     * returns Schema content as string
     * @param namespace  nameSpace associated with Schema
     * @param location  Schema Reference File Name.
     * @return schema as string
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the failed to get Schema Reference from Schema Repository
     */
    public String getSchemaAsString(String namespace, String location) throws RemoteException, ServiceException;
    /**
     * Method to add Schema to repository with overwrite option
     * @param content Content of the schema reference
     * @param fileName File name of the Schema
     * @param overwrite true to over write schema if already exists
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchema(String content, String fileName, boolean overwrite) throws RemoteException, ServiceException;


    /**
     * Method to add Schema to repository
     * @param content Content of the schema reference
     * @param fileName File name of the Schema
     * @param overwrite true to over write schema if already exists
     * @param map to compare imported schemas in schema referrences
     * @return returns name of the file to which content has been written
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchema(String content, String fileName, boolean overwrite, Map map) throws RemoteException, ServiceException;

    /**
     * Method to add Schema to repository
     * @param content Content of the schema reference
     * @param fileName File name of the Schema
     * @param map to compare imported schemas in schema referrences
     * @return returns name of the file to which content has been written
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException  Thrown if the Schema Reference is failed to be added to Schema repository
     */
    public String addSchema(String content, String fileName, Map map) throws RemoteException, ServiceException;



    /**
     * This method removes the schema reference from schema repository
     *
     * @param nameSpace  name schema reference to be removed from fes
     * @param locationHint location
     * @return  boolean - returns true if schema reference is removed successfully, false otherwise
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the failed to get Schema Reference from Schema Repository
     */
    boolean removeSchemaReferences(String nameSpace, String locationHint) throws RemoteException, ServiceException;

    /**
     * This method returns list of all Schema References present in server schema repository
     *
     * @return List - List Of All Schema References in Server Repository
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the failed to get Schema Reference from Schema Repository
     */
    public List<SchemaReference> listOfAllSchemaReferences() throws RemoteException, ServiceException;

    /**
     * Returns zip byte  data holding xml-catalog and all schemas.
     *
     * @return zipped schemas
     * @param index
     */
    public byte[] getAllSchemasPresentInServer(long index) throws RemoteException, ServiceException;

}
