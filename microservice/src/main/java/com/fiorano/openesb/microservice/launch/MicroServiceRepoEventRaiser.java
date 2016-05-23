/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch;

import com.fiorano.openesb.events.Event;
import com.fiorano.openesb.events.EventsManager;
import com.fiorano.openesb.events.MicroServiceRepoUpdateEvent;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.osgi.framework.FrameworkUtil;

public class MicroServiceRepoEventRaiser {
    static EventsManager eventsManager = FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getService(FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getServiceReference(EventsManager.class));
    public static void generateServiceRepositoryEvent(String serviceGUID,
                                                      String serviceVersion,
                                                      String resourceName,
                                                      String serviceStatus, Event.EventCategory category, String description) throws FioranoException{

        MicroServiceRepoUpdateEvent event = new MicroServiceRepoUpdateEvent();

        event.setServiceGUID(serviceGUID);
        event.setServiceVersion(serviceVersion);
        event.setResourceName(resourceName);
        event.setServiceStatus(serviceStatus);
        event.setEventCategory(category);
        event.setEventDescription(description);
        event.setEventGenerationDate(System.currentTimeMillis());
        event.setEventStatus(serviceStatus);
        event.setSource("Open ESB");

        eventsManager.raiseEvent(event);
    }
}
