/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.namedconfig;

import com.fiorano.openesb.application.ServerConfig;
import com.fiorano.openesb.application.application.ApplicationParser;
import com.fiorano.openesb.application.configuration.data.DataObject;
import com.fiorano.openesb.application.configuration.data.NamedObject;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.application.service.ServiceParser;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class NamedConfigRepository  {
    private String repositoryPath;
    private ArrayList<ConfigurationRepositoryEventListener> eventListeners = new ArrayList<ConfigurationRepositoryEventListener>();

    public void registerEventListener(ConfigurationRepositoryEventListener eventListener) {
        eventListeners.add(eventListener);
    }

    public void unRegisterEventListener(ConfigurationRepositoryEventListener eventListener) {
        eventListeners.remove(eventListener);
    }

    public NamedConfigRepository() {
        repositoryPath = getBaseRepositoryLocation();
        ApplicationParser.setConfigurationRepoPath(repositoryPath);
        ServiceParser.setConfigurationRepoPath(repositoryPath);
    }

    private String getBaseRepositoryLocation() {
        return ServerConfig.getConfig().getRepositoryPath();
    }

    /**
     * Logic which has to be implemented by all modules to enable itself to be switched to active
     * @throws FioranoException
     */
    protected void _switchToActiveMode() throws FioranoException {
        //Nothing to do
    }

    /**
     * Abstract method which needs to be implemented by all modules
     * to switch themselves to passive.
     * @throws FioranoException
     */
    protected void _switchToPassiveMode() throws FioranoException {
        //Nothing to do
    }

    /**
     * Returns the path of the repository where all named configurations are stored
     * @return path of the repository where all named configurations are stored
     */
    public String getConfigurationRepositoryPath() {
        return repositoryPath;
    }

    public void persistConfiguration(DataObject data, boolean update, String handleId) throws IOException, FioranoException {
        NamedConfigurationUtil.persistConfiguration(data, update, repositoryPath + File
                .separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, eventListeners);
    }

    public ArrayList<DataObject> getConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData, String handleId) throws IOException, FioranoException {
        return NamedConfigurationUtil.getConfigurations(namedObject, retrieveOnlyMetaData, repositoryPath + File
                .separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR);
    }

    public Map<String, Integer> getConfigurationsHash() throws FioranoException {
        return null; //TODO - Compute Hash and implement partial sync mechanism between HA servers
    }

    public boolean deleteConfiguration(NamedObject namedObject, String handleId) throws IOException, FioranoException {
        return NamedConfigurationUtil.deleteConfiguration(namedObject, repositoryPath + File
                .separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR, eventListeners) ;
    }

    public String getModuleName() {
        return "Configuration Repository";
    }

    public void realTimePersistConfiguration(DataObject data, boolean update, String handleId) throws IOException, FioranoException {
        persistConfiguration(data, update, handleId);
    }
}
