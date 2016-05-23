/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ApplicationParser;
import com.fiorano.openesb.application.configuration.data.DataObject;
import com.fiorano.openesb.application.configuration.data.Label;
import com.fiorano.openesb.application.configuration.data.NamedObject;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.namedconfig.NamedConfigRepository;
import com.fiorano.openesb.namedconfig.NamedConfigurationUtil;
import com.fiorano.openesb.rmiconnector.api.IConfigurationManager;
import com.fiorano.openesb.rmiconnector.api.IConfigurationRepositoryListener;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.security.SecurityManager;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ConfigurationManager extends AbstractRmiManager implements IConfigurationManager {
    private NamedConfigRepository configurationManager;
    private InstanceHandler handler;
    private ApplicationRepository applicationRepository;
    private final ApplicationController applicationController;
    private SecurityManager securityManager;

    protected ConfigurationManager(RmiManager rmiManager, InstanceHandler handler) {
        super(rmiManager);
        this.configurationManager = rmiManager.getNamedConfigRepository();
        this.applicationRepository = rmiManager.getApplicationRepository();
        applicationController = rmiManager.getApplicationController();
        this.securityManager = rmiManager.getSecurityManager();
        this.handler = handler;
        setHandleID(handler.getHandleID());
    }

    /**
     * API to persist a named configuration into the registry
     *
     * @param data   The data to be persisted under the named configuration repository
     * @param update boolean specifying whether to update the data if it already exists with the same name
     * @throws ServiceException Thrown if a configuration with the same name already exists in the configuration repository or an I/O exception while persisting data
     */
    public void persistConfiguration(DataObject data, boolean update) throws ServiceException {
        try {
            configurationManager.persistConfiguration(data, update, handleId);
        } catch (Exception e) {
            throw new ServiceException("ERR_PERSISTING_CONFIGURATION",e);
        }
    }

    @Override
    public void realTimePersistConfiguration(DataObject data, boolean update) throws RemoteException, ServiceException {
        try {
            configurationManager.realTimePersistConfiguration(data, update,  handleId);
        } catch (Exception e) {
            throw new ServiceException("ERR_REAL_TIME_PERSISTING_CONFIGURATION", e);
        }
    }

    /**
     * Method to retrieve named configurations as filtered by the parameters passed.
     * <p/> To disable filtering on one or more parameters of NamedObject argument, set those parameters to null.
     *
     * @param namedObject          The parameters which determine the object to be retrieved from configuration repository
     * @param retrieveOnlyMetaData retrieveOnlyMetaData <ul><li>If set to true, the DataObjects returned do not contain the actual data stored with the configuration. All other fields
     *                             of DataObjects are populated which give meta-data information about the configuration stored. This can be useful when user just wants a list of
     *                             the configurations stored without the overhead of reading each configuration and transferring it over the wire.</li>
     *                             <br/><li>If set to false, the DataObjects returned have all the information about the configurations stored i.e. meta-data information as well
     *                             as the actual data stored with the configuration.</li>
     * @return An arraylist of matching named configuration objects
     * @throws ServiceException Thrown if the configuration with the given name is not found in the configuration repository or an I/O exception while retrieving data
     */
    public ArrayList<DataObject> getConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData) throws ServiceException {
        try {
            return configurationManager.getConfigurations(namedObject, retrieveOnlyMetaData, handleId);
        }  catch (Exception e) {
            throw new ServiceException("ERR_SEARCHING_CONFIGURATION", e);
        }
    }

    private ArrayList<DataObject> getApplicationConfigurations(NamedObject namedObject, boolean retrieveOnlyMetaData) throws ServiceException {
        try {
            return configurationManager.getConfigurations(namedObject, retrieveOnlyMetaData, handleId);
        }  catch (Exception ignore) {
            return new ArrayList<DataObject>();
        }
    }

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
     */
    public ArrayList<DataObject> getConfigurations(String appGUID, float appVersion, boolean retrieveOnlyMetaData) throws ServiceException {
        try {
            File applicationDir = applicationRepository.getAppDir(appGUID, appVersion);
            Application application;
            try {
                application = ApplicationParser.readApplication(applicationDir, true, true);
            } catch (Exception e) {
                throw new ServiceException("ERR_READING_EVENT_PROCESS", e);
            }

            ArrayList<DataObject> namedConfigurations = new ArrayList<DataObject>();
            NamedConfigurationUtil.getApplicationConfigurations(application, retrieveOnlyMetaData,
                    configurationManager.getConfigurationRepositoryPath() + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR,
                    namedConfigurations);

            return namedConfigurations;
        } catch (Exception e) {
            throw new ServiceException("ERR_SEARCHING_APP_CONFIGURATIONS", e);
        }
    }

    @Override
    public void migrateEventProcessEnvironment(String appGUID, float appVersion, String targetEnv, boolean update, boolean copyNamedConfigs) throws RemoteException, ServiceException {

        try {
            Application.Label.valueOf(targetEnv.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("ERROR_WRONG_ARGUMENT");
        }
        final ArrayList<DataObject> namedConfigurations;
        Application application;
        File applicationDir;
        try {
            if (applicationController.isApplicationRunning(appGUID, appVersion, handleId))
                throw new ServiceException("EVENT_PROCESS_IN_RUNNING_STATE");
        } catch (FioranoException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        try {
            applicationDir = applicationRepository.getAppDir(appGUID, appVersion);
            if(!applicationDir.exists())
            {
                throw new ServiceException("ERROR_APP_DOESNT_EXIST");
            }
            try {
                application = ApplicationParser.readApplication(applicationDir, false, false);
            } catch (FioranoException e) {
                throw new ServiceException("ERR_READING_EVENT_PROCESS");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (application.getLabel().equalsIgnoreCase(targetEnv)) {
            throw new ServiceException("ERROR_SAME_TARGET_AND_APP_ENVS");
        }

        if (copyNamedConfigs) {
            namedConfigurations = getConfigurations(appGUID, appVersion, false);
            for (DataObject dataObject : namedConfigurations) {
                dataObject.setLabel(Label.valueOf(targetEnv.toUpperCase()));
                persistConfiguration(dataObject, update);
            }
        }

        application.setLabel(targetEnv.toLowerCase());
        try {
            applicationController.saveApplication(application, true, handleId);
        } catch (Exception e) {
            throw new ServiceException("ERROR_WRITING_EVENT_PROCESS", e);
        }
    }

    /**
     * Deletes the configuration as determined by parameters passed. Returns true if the configuration was successfully deleted.
     *
     * @param namedObject The parameters which determine the object to be deleted from configuration repository
     * @return true if the object was successfully deleted, false otherwise
     * @throws ServiceException Thrown if the configuration with the given name is not found in the configuration repository or an I/O exception while deleting data
     */
    public boolean deleteConfiguration(NamedObject namedObject) throws ServiceException {
        try {
            return configurationManager.deleteConfiguration(namedObject, handleId);
        }  catch (Exception e) {
            throw new ServiceException("ERR_DELETING_CONFIGURATION",e);
        }
    }

    public void addConfigurationRepositoryListener(IConfigurationRepositoryListener configurationRepositoryListener) throws RemoteException, ServiceException {
        //Check the validity of the connection
        validateHandleID(handleId, "Add Configuration Repository Listener");
        dapiEventManager.registerConfigurationRepositoryEventListener(configurationRepositoryListener, handleId);
    }

    public void removeConfigurationRepositoryListener() throws RemoteException, ServiceException {
        //Check the validity of the connection
        validateHandleID(handleId, "Remove Configuration Repository Listener");
        dapiEventManager.unRegisterConfigurationRepositoryEventListener(handleId);
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());
    }

    public String toString() {
        return Constants.CONFIGURATION_MANAGER;
    }

    protected String getHighestVersion(String id, String handleID) throws ServiceException {
        throw new UnsupportedOperationException("This operation is not supported for this instance");
    }

    private IConfigurationManager clientProxyInstance;

    void setClientProxyInstance(IConfigurationManager clientProxyInstance){
        this.clientProxyInstance = clientProxyInstance;
    }

    IConfigurationManager getClientProxyInstance() {
        return clientProxyInstance;
    }
}

