/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.namedconfig;

import com.fiorano.openesb.application.configuration.data.NamedObject;

public interface ConfigurationRepositoryEventListener {

    /**
     * This API is invoked whenever a configuration is persisted into configuration repository
     * @param namedObject An object containing essential parameters which define the configuration persisted
     */
    public void configurationPersisted(NamedObject namedObject);

    /**
     * This API is invoked whenever a configuration is deleted from configuration repository
     * @param namedObject An object containing essential parameters which define the configuration deleted
     */
    public void configurationDeleted(NamedObject namedObject);
}
