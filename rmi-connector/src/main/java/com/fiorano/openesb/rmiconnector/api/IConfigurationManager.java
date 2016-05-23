/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.application.configuration.data.DataObject;
import com.fiorano.openesb.application.configuration.data.NamedObject;

import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IConfigurationManager {
    /**
     * API to persist a named configuration into the registry
     * @param data The data to be persisted under the named configuration repository
     * @param update boolean specifying whether to update the data if it already exists with the same name
     * @throws ServiceException Thrown if a configuration with the same name already exists in the configuration repository or an I/O exception while persisting data
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void persistConfiguration(DataObject data, boolean update) throws RemoteException, ServiceException;


    /**
     * API to real time persist a named configuration into the fes and peers registry.
     *
     * @param data The data to be persisted under the named configuration repository
     * @param update boolean specifying whether to update the data if it already exists with the same name
     * @throws ServiceException Thrown if a configuration with the same name already exists in the configuration repository or an I/O exception while persisting data
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public void realTimePersistConfiguration(DataObject data, boolean update) throws RemoteException, ServiceException;

    /**
     * Method to retrieve named configurations as filtered by the parameters passed.
     * <p/> To disable filtering on one or more parameters of NamedObject argument, set those parameters to null.
     *
     * @param namedObject The parameters which determine the object to be retrieved from configuration repository
     * @param retrieveOnlyMetaData retrieveOnlyMetaData <ul><li>If set to true, the DataObjects returned do not contain the actual data stored with the configuration. All other fields
     *          of DataObjects are populated which give meta-data information about the configuration stored. This can be useful when user just wants a list of
     *          the configurations stored without the overhead of reading each configuration and transferring it over the wire.</li>
     *          <br/><li>If set to false, the DataObjects returned have all the information about the configurations stored i.e. meta-data information as well
     *          as the actual data stored with the configuration.</li>
     * @return An arraylist of matching named configuration objects
     * @throws ServiceException Thrown if the configuration with the given name is not found in the configuration repository or an I/O exception while retrieving data
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public ArrayList<DataObject> getConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData) throws RemoteException, ServiceException;

    /**
     * Method to retrieve named configurations for a particular application. This method returns only those named configurations which exist in the
     * configuration repository. If some configuration is referred by the application, but it does not exist in the configuration repository,
     * it is skipped and no exception is thrown in such a case.
     *
     * @param appGUID The application GUID for which the configurations should be retrieved
     * @param appVersion The application version for which the configurations should be retrieved
     * @param retrieveOnlyMetaData retrieveOnlyMetaData <ul><li>If set to true, the DataObjects returned do not contain the actual data stored with the configuration. All other fields
     *          of DataObjects are populated which give meta-data information about the configuration stored. This can be useful when user just wants a list of
     *          the configurations stored without the overhead of reading each configuration and transferring it over the wire.</li>
     *          <br/><li>If set to false, the DataObjects returned have all the information about the configurations stored i.e. meta-data information as well
     *          as the actual data stored with the configuration.</li>
     * @return An arraylist of matching named configuration objects
     * @throws ServiceException Thrown if the application with the given name is not found in the configuration repository or an I/O exception while reading application
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public ArrayList<DataObject> getConfigurations(String appGUID, float appVersion, boolean retrieveOnlyMetaData) throws RemoteException, ServiceException;

    /**
     * Method to copy all application referring named configurations to target environment.This method copies only those named configurations which exist in
     * the configuration repository. If some configuration referred by the application but it does not exist in registry,
     * it is skipped and no exception is thrown in such a case.
     *
     * @param appGUID The application GUID for which the configurations should be retrieved
     * @param appVersion The application version for which the configurations should be retrieved
     * @param targetEnv  The environment to which application referring configurations should be copied
     * @param update boolean specifying whether to update the data if it already exists with the same name
     * @param copyNamedConfigs boolean value specifying whether to copy named configurations or not
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException Thrown if the application with the given name is not found in the configuration repository or an I/O exception while reading application
     */
    public void migrateEventProcessEnvironment(String appGUID, float appVersion, String targetEnv, boolean update, boolean copyNamedConfigs) throws RemoteException, ServiceException;

    /**
     * Deletes the configuration as determined by parameters passed. Returns true if the configuration was successfully deleted.
     * @param namedObject The parameters which determine the object to be deleted from configuration repository
     * @return true if the object was successfully deleted, false otherwise
     * @throws ServiceException Thrown if the configuration with the given name is not found in the configuration repository or an I/O exception while deleting data
     * @throws java.rmi.RemoteException A communication-related exception that may occur during the execution of a remote method call
     */
    public boolean deleteConfiguration(NamedObject namedObject) throws RemoteException, ServiceException;

    /**
     * Adds a listener to the configuration repository changes
     *
     * @param configurationRepositoryListener The listener to add
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void addConfigurationRepositoryListener(IConfigurationRepositoryListener configurationRepositoryListener) throws RemoteException, ServiceException;

    /**
     * Removes listener to the configuration repository changes
     *
     * @throws RemoteException A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void removeConfigurationRepositoryListener() throws RemoteException, ServiceException;
}
