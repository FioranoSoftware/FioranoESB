/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.management;

import com.fiorano.openesb.application.ApplicationRepository;
import com.fiorano.openesb.application.aps.ApplicationStateDetails;
import com.fiorano.openesb.application.aps.ServiceInstanceStateDetails;
import com.fiorano.openesb.applicationcontroller.ApplicationController;
import com.fiorano.openesb.microservice.ccp.event.common.data.MemoryUsage;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true,
        maxAge = 1
)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestServiceImpl implements ApplicationsService {

    public RestServiceImpl() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/applications")
    public Response getApplications() {
        BundleContext bundleContext = FrameworkUtil.getBundle(ApplicationsService.class).getBundleContext();
        ApplicationRepository controller = bundleContext.getService(bundleContext.getServiceReference(ApplicationRepository.class));
        Response response = new Response();
        Map<String,ApplicationHeader> applicationDetails = new HashMap<>();
        for(String appName : controller.getApplicationIdWithVersions()){
            try {
                ApplicationHeader applicationHeader = new ApplicationHeader();
                applicationHeader.setRunning(getController().isApplicationRunning(appName.substring(0, appName.indexOf(':')),Float.valueOf(appName.substring(appName.indexOf(':') + 1)),null));
                applicationDetails.put(appName,applicationHeader);
            } catch (FioranoException e) {
                response.setMessage(e.getMessage());
                response.setStatus(false);
                return response;
            }

        }
        response.setApplications(applicationDetails);
        response.setStatus(true);
        return response;
    }


    @Path("/applications/{applicationName}/{applicationVersion}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getApplicationDetails(@PathParam("applicationName") String applicationName,
                                        @PathParam("applicationVersion") String applicationVersion) {
        try {
            ApplicationStateDetails stateOfApplication = getController().getCurrentStateOfApplication(applicationName, Float.parseFloat(applicationVersion), null);
            com.fiorano.openesb.management.Application application = new com.fiorano.openesb.management.Application();

            List<Microservice> services = new ArrayList<>();
            @SuppressWarnings("unchecked") Enumeration<String> serviceNames = stateOfApplication.getAllServiceNames();
            while (serviceNames.hasMoreElements()) {
                String service = serviceNames.nextElement();
                ServiceInstanceStateDetails serviceInstance = stateOfApplication.getServiceStatus(service);
                Microservice microservice = new Microservice();
                microservice.setGuid(serviceInstance.getServiceGUID());
                microservice.setName(serviceInstance.getServiceInstanceName());
                microservice.setVersion(String.valueOf(serviceInstance.getRunningVersion()));
                boolean microserviceRunning = getController().isMicroserviceRunning(applicationName, applicationVersion, serviceInstance.getServiceInstanceName());
                microservice.setRunning(microserviceRunning);
                microservice.setLaunchMode(getLaunchMode(serviceInstance.getLaunchType()));
                services.add(microservice);
            }
            application.setServices(services);
            application.setId(stateOfApplication.getAppGUID());
            application.setName(stateOfApplication.getDisplayName());
            application.setVersion(applicationVersion);
            application.setIsRunning(getController().isApplicationRunning(applicationName, Float.parseFloat(applicationVersion), null));
            return application;
        } catch (Exception e) {
            Response response = new Response();
            response.setStatus(false);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    private String getLaunchMode(int launchType) {
        switch (launchType){
            case 1:
                return "Separate Process";
            case 2 :
                return "In Memory";
            case 4:
                return "Manual Launch";
            default:
                return "None";
        }
    }

    @POST
    @Path("/applications/{applicationName}/{applicationVersion}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response performApplicationAction(@PathParam("applicationName") String applicationName, @PathParam("applicationVersion") String applicationVersion, Action action) {
        ApplicationController controller = getController();
        Response response = new Response();
        try {
            String actionStr = action.getAction();
            if (actionStr.equalsIgnoreCase("start") || actionStr.equalsIgnoreCase("launch")) {
                controller.launchApplication(applicationName, applicationVersion, null);
                response.setMessage("Application launched successfully");
            } else if (actionStr.equalsIgnoreCase("stop")) {
                controller.stopApplication(applicationName, applicationVersion, null);
                response.setMessage("Application stopped successfully");
            } else if (actionStr.equalsIgnoreCase("synchronize")) {
                controller.synchronizeApplication(applicationName,applicationVersion,null);
            }
            response.setStatus(true);
            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(false);
            return response;
        }

    }

    private ApplicationController getController() {
        BundleContext bundleContext = FrameworkUtil.getBundle(ApplicationController.class).getBundleContext();
        return bundleContext.getService(bundleContext.getServiceReference(ApplicationController.class));
    }

    private ApplicationRepository getApplicationRepository() {
        BundleContext bundleContext = FrameworkUtil.getBundle(ApplicationRepository.class).getBundleContext();
        return bundleContext.getService(bundleContext.getServiceReference(ApplicationRepository.class));
    }

    @POST
    @Path("/applications/{applicationName}/{applicationVersion}/{microServiceName}")
    public Response performMicroServiceAction(@PathParam("applicationName") String applicationName, @PathParam("applicationVersion") String applicationVersion, @PathParam("microServiceName") String microServiceName, Action action) {
        ApplicationController controller = getController();
        Response response = new Response();
        try {
            if("stop".equalsIgnoreCase(action.getAction())){
                response.setStatus(controller.stopMicroService(applicationName, applicationVersion,microServiceName ,null));
                return response;
            } else {
                response.setStatus(controller.startMicroService(applicationName, applicationVersion, microServiceName, null));
                return response;
            }
        } catch (FioranoException e) {
            response.setMessage(e.getMessage());
            response.setStatus(false);
            return response;
        }

    }

}