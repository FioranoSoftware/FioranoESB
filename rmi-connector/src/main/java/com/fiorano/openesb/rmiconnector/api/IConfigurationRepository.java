/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import com.fiorano.openesb.application.configuration.data.DataObject;
import com.fiorano.openesb.application.configuration.data.NamedObject;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public interface IConfigurationRepository {
    /**
     * API to persist a named configuration into the registry
     * @param data The data to be persisted under the named configuration repository
     * @param update boolean specifying whether to update the data if it already exists with the same name
     * @throws FioranoException Any other exception which occurs while persisting data
     * @throws java.io.IOException I/O exception while persisting data
     */
    public void persistConfiguration(DataObject data, boolean update) throws IOException, FioranoException;

    /**
     * Method to retrieve named configurations as filtered by the parameters passed.
     * <p/> To disable filtering on one or more parameters of NamedObject argument, set those parameters to null.
     *
     * @param namedObject The parameters which determine the object to be retrieved from configuration repository
     * @param retrieveOnlyMetaData <ul><li>If set to true, the DataObjects returned do not contain the actual data stored with the configuration. All other fields
     *          of DataObjects are populated which give meta-data information about the configuration stored. This can be useful when user just wants a list of
     *          the configurations stored without the overhead of reading each configuration and transferring it over the wire.</li>
     *          <br/><li>If set to false, the DataObjects returned have all the information about the configurations stored i.e. meta-data information as well
     *          as the actual data stored with the configuration.</li>
     * @return An arraylist of matching named configuration objects
     * @throws FioranoException Any other exception which occurs while retrieving the configuration
     * @throws java.io.IOException I/O exception while retrieving data
     */
    public ArrayList<DataObject> getConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData) throws IOException, FioranoException;

    /**
     * Deletes the configuration as determined by parameters passed. Returns true if the configuration was successfully deleted.
     * @param namedObject The parameters which determine the object to be deleted from configuration repository
     * @return true if the object was successfully deleted, false otherwise
     * @throws FioranoException Any other exception which occurs while deleting the configuration
     * @throws java.io.IOException I/O exception while deleting data
     */
    public boolean deleteConfiguration(NamedObject namedObject) throws IOException, FioranoException;

    /**
     * Returns the hash of the configurations mapped with the configuration name.
     * This will be used to find whether the configuration repositories are in sync with each other or not.
     *
     * @return Map of configuration name with the hash of the configuration object
     * @throws FioranoException If an exception occurs while calculating
     */
    public Map<String, Integer> getConfigurationsHash() throws FioranoException;

    /**
     * Returns the path of the repository where all named configurations are stored
     * @return path of the repository where all named configurations are stored
     */
    public String getConfigurationRepositoryPath();
}


