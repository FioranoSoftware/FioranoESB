/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.events.*;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.osgi.framework.FrameworkUtil;

public class ApplicationEventRaiser {
    static EventsManager eventsManager = FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getService(FrameworkUtil.getBundle(EventsManager.class).getBundleContext().getServiceReference(EventsManager.class));
    public static void generateApplicationEvent(ApplicationEvent.ApplicationEventType eventType, Event.EventCategory category, String appGUID,
                                          String appName, String version, String description)
            throws FioranoException
    {
        ApplicationEvent event = new ApplicationEvent();
        event.setApplicationEventType(eventType);
        event.setSource("openESB");
        event.setEventGenerationDate(System.currentTimeMillis());
        event.setApplicationGUID(appGUID);
        event.setApplicationName(appName);
        event.setApplicationVersion(version);
        event.setEventDescription(description);
        event.setEventCategory(category);
        eventsManager.raiseEvent(event);
    }

    public static void generateRouteEvent(ApplicationEvent.ApplicationEventType eventType, Event.EventCategory category, String appGUID,
                                                String appName, String version, String routeGuid, String description)
            throws FioranoException
    {
        RouteEvent event = new RouteEvent();
        event.setRouteGUID(routeGuid);
        event.setApplicationEventType(eventType);
        event.setSource("openESB");
        event.setEventGenerationDate(System.currentTimeMillis());
        event.setApplicationGUID(appGUID);
        event.setApplicationName(appName);
        event.setApplicationVersion(version);
        event.setEventDescription(description);
        event.setEventCategory(category);
        eventsManager.raiseEvent(event);
    }
}
