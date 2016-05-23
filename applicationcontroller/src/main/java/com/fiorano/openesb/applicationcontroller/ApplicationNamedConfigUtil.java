/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.ApplicationParser;
import com.fiorano.openesb.application.application.NamedConfigurationProperty;
import com.fiorano.openesb.application.application.ServiceInstance;
import com.fiorano.openesb.application.configuration.data.ObjectCategory;
import com.fiorano.openesb.application.configuration.data.ResourceConfigurationNamedObject;
import com.fiorano.openesb.application.constants.ConfigurationRepoConstants;
import com.fiorano.openesb.namedconfig.NamedConfigRepository;
import com.fiorano.openesb.namedconfig.NamedConfigurationUtil;
import com.fiorano.openesb.utils.ComponentErrorCodes;
import com.fiorano.openesb.utils.crypto.CommonConstants;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationNamedConfigUtil {
    private static Logger logger = LoggerFactory.getLogger(Activator.class);
    public static HashMap<String, String> resolveNamedConfigurations(ApplicationHandle applicationHandle, String servInstance, NamedConfigRepository namedConfigRepository) throws FioranoException {
        Application application = applicationHandle.getApplication();
        ServiceInstance serviceInstance = application.getServiceInstance(servInstance);
        ArrayList<NamedConfigurationProperty> namedConfigurations = serviceInstance.getNamedConfigurations();
        HashMap<String, String> resolvedConfigurations = new HashMap<>();

        if (namedConfigurations != null && namedConfigurations.size() > 0) {
            for(NamedConfigurationProperty configurationProperty : namedConfigurations){
                String configurationName = configurationProperty.getConfigurationName();
                String configurationType = configurationProperty.getConfigurationType();
                String configurationData = getConfigurationData(namedConfigRepository, configurationName, configurationType, serviceInstance.getGUID(), serviceInstance.getVersion(), application.getLabel());

                if(configurationData != null)
                    resolvedConfigurations.put(configurationType + "__" + configurationName, configurationData);
            }
        }
        try {
            addDefaultConfigs(namedConfigRepository, resolvedConfigurations, CommonConstants.KEY_STORE_CONFIG, CommonConstants.KEY_STORE_RESOURCE_TYPE, servInstance, serviceInstance.getVersion(), application.getLabel());
            addDefaultConfigs(namedConfigRepository, resolvedConfigurations, CommonConstants.MSG_ENCRYPTION_CONFIG, CommonConstants.MSG_ENCRYPTION_CONFIG_TYPE, servInstance, serviceInstance.getVersion(), application.getLabel());
        } catch (FioranoException e) {
            logger.error("Error occured while resolving Named configurations for Service: " +servInstance +"of Application: "+applicationHandle.getAppGUID() +":"+applicationHandle.getVersion());
            throw new FioranoException(e);
        } catch (IOException e) {
            logger.error("");
            throw new FioranoException(e);
        }
        return resolvedConfigurations;
    }

    private static void addDefaultConfigs(NamedConfigRepository namedConfigRepository, HashMap<String, String> resolvedConfigurations, String configName, String resourceType,String serviceInstance, float servInstVersion, String label) throws IOException, FioranoException {
        ResourceConfigurationNamedObject namedObject = new ResourceConfigurationNamedObject();
        namedObject.setName(configName);
        namedObject.setResourceType(resourceType);
        ArrayList configList = namedConfigRepository.getConfigurations(namedObject, true, null);
        if (configList!=null && !configList.isEmpty()) {
            resolvedConfigurations.put("resource" + "__" + configName, getConfigurationData(namedConfigRepository, configName, "resource", serviceInstance, servInstVersion, label));
        }
    }

    private static String getConfigurationData(NamedConfigRepository namedConfigRepository, String configurationName, String configurationType, String servInstanceGuid, float servVersion, String label) throws FioranoException {
        if (configurationType != null) {
            String configurationRepoPath = namedConfigRepository.getConfigurationRepositoryPath() + File.separator + ConfigurationRepoConstants.CONFIGURATIONS_DIR;

            ObjectCategory objectCategory = ObjectCategory.getObjectCategory(configurationType);
            switch (objectCategory) {
                case SERVICE_CONFIGURATION:
                    File serviceConfigurationFile = NamedConfigurationUtil.getServiceConfigurationFile(configurationRepoPath, servInstanceGuid, String.valueOf(servVersion), label, configurationName);
                    return getConfigurationContents(serviceConfigurationFile);
                case MISCELLANEOUS:
                    File miscConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.MISC,
                            label, configurationName);
                    return getConfigurationContents(miscConfigurationFile);
                case RESOURCE_CONFIGURATION:
                    File resourceConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.RESOURCES,
                            label, configurationName);
                    return getConfigurationContents(resourceConfigurationFile);
                case ROUTE:
                    File routeConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.ROUTES, label,
                            configurationName);
                    return getConfigurationContents(routeConfigurationFile);
                case SELECTOR:
                    File selectorConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.SELECTORS, label,
                            configurationName);
                    return getConfigurationContents(selectorConfigurationFile);
                case TRANSFORMATION:
                    File transformationConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.TRANSFORMATIONS, label,
                            configurationName);
                    return getConfigurationContents(transformationConfigurationFile);
                case WORKFLOW:
                    File workflowConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.WORKFLOWS, label,
                            configurationName);
                    return getConfigurationContents(workflowConfigurationFile);
                case MESSAGEFILTERS:
                    File messageFilterConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.MESSAGEFILTERS, label,
                            configurationName);
                    return getConfigurationContents(messageFilterConfigurationFile);
                case RUNTIME_ARG_CONFIGURATION:
                    File runtimeConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.RUNTIME_ARGS, label,
                            configurationName);
                    return getConfigurationContents(runtimeConfigurationFile);
                case CONNECTION_FACTORY_CONFIGURATION:
                    File connectionFactoryConfigurationFile = NamedConfigurationUtil.getConfigurationFile(configurationRepoPath, ConfigurationRepoConstants.CONNECTION_FACTORY, label,
                            configurationName);
                    return getConfigurationContents(connectionFactoryConfigurationFile);
                case PORT_CONFIGURATION:
                    throw new FioranoException(ComponentErrorCodes.PORT_CONFIGURATION_TYPE_NOT_SUPPORTED);
                case DESTINATION:
                    throw new FioranoException(ComponentErrorCodes.DESTINATION_CONFIGURATION_TYPE_NOT_SUPPORTED);
                default:
                    throw new FioranoException(ComponentErrorCodes.CONFIGURATION_TYPE_INVALID);
            }
        } else
            throw new FioranoException(ComponentErrorCodes.CONFIGURATION_TYPE_INVALID);
    }

    private static String getConfigurationContents(File file) throws FioranoException {
        if (!file.exists())
            throw new FioranoException(ComponentErrorCodes.CONFIGURATION_NOT_PRESENT);

        if (file.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);

                return DmiObject.getContents(fis);

            } catch (IOException e) {
                throw new FioranoException(ComponentErrorCodes.CONFIGURATION_READ_ERROR, e);
            } finally {
                try {
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    //Ignore
                }
            }
        } else {
            try {
                return ApplicationParser.toXML(file);
            } catch (Exception e) {
                throw new FioranoException(ComponentErrorCodes.CONFIGURATION_READ_ERROR, e);
            }
        }
    }

}
