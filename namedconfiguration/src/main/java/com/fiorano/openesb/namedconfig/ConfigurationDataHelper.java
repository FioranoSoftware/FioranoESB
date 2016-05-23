/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.namedconfig;

import com.fiorano.openesb.application.DmiErrorCodes;
import com.fiorano.openesb.application.DmiObject;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.configuration.data.*;
import com.fiorano.openesb.utils.exception.FioranoException;

public class ConfigurationDataHelper {
    public static DmiObject getDataModelObject(int objectType) throws FioranoException {
        switch (objectType) {
            case DmiObjectTypes.NAMED_CONFIGURATION_OBJECT:
                return new NamedObject();
            case DmiObjectTypes.PORT_CONFIGURATION_NAMED_OBJECT:
                return new PortConfigurationNamedObject();
            case DmiObjectTypes.SERVICE_CONFIGURATION_NAMED_OBJECT:
                return new ServiceConfigurationNamedObject();
            case DmiObjectTypes.RESOURCE_CONFIGURATION_NAMED_OBJECT:
                return new ResourceConfigurationNamedObject();
            case DmiObjectTypes.TRANSFORMATION_CONFIGURATION_NAMED_OBJECT:
                return new TransformationConfigurationNamedObject();
            case DmiObjectTypes.DATA_CONFIGURATION_OBJECT:
                return new DataObject();
            case DmiObjectTypes.PORT_CONFIGURATION_DATA_OBJECT:
                return new PortConfigurationDataObject();
            case DmiObjectTypes.SERVICE_CONFIGURATION_DATA_OBJECT:
                return new ServiceConfigurationDataObject();
            case DmiObjectTypes.RESOURCE_CONFIGURATION_DATA_OBJECT:
                return new ResourceConfigurationDataObject();
            case DmiObjectTypes.TRANSFORMATION_CONFIGURATION_DATA_OBJECT:
                return new TransformationConfigurationDataObject();
            case DmiObjectTypes.RUNTIMEARG_CONFIGURATION_NAMED_OBJECT:
                return new RuntimeargConfigurationNamedObject();
            case DmiObjectTypes.RUNTIMEARG_CONFIGURATION_DATA_OBJECT:
                return new RuntimeargConfigurationDataObject();
            default:
                throw new FioranoException(DmiErrorCodes.ERR_INVALID_OBJECT_TYPE);
        }
    }
}

