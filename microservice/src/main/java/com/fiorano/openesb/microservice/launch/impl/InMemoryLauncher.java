/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl;

import com.fiorano.openesb.application.service.Service;
import com.fiorano.openesb.microservice.bundle.Activator;
import com.fiorano.openesb.microservice.ccp.event.common.data.ComponentStats;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.launch.Launcher;
import com.fiorano.openesb.microservice.launch.MicroServiceRuntimeHandle;
import com.fiorano.openesb.microservice.launch.impl.cl.ClassLoaderManager;
import com.fiorano.openesb.microservice.launch.impl.cl.IClassLoaderManager;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.utils.LoggerUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.logging.FioranoLogHandler;
import org.apache.activemq.command.RemoveInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;

public class InMemoryLauncher implements Launcher {
    private IClassLoaderManager classLoaderManager;
    private Object runtimeService;
    private LaunchConfiguration launchConfiguration;
    private Class serviceClass;
    private ClassLoader serviceClassLoader;
    private Logger logger = LoggerFactory.getLogger(Activator.class);

    public InMemoryLauncher() throws FioranoException {
        classLoaderManager = new ClassLoaderManager();
    }

    public MicroServiceRuntimeHandle launch(LaunchConfiguration launchConfiguration, String configuration) throws Exception {
        this.launchConfiguration = launchConfiguration;
        ClassLoader classLoader = classLoaderManager.getClassLoader(getComponentPS(), launchConfiguration,null);
        InMemoryLaunchThread inMemoryLaunchThread = new InMemoryLaunchThread(classLoader);
        inMemoryLaunchThread.start();
        return inMemoryLaunchThread.runtimeHandle;
    }

    private Service getComponentPS() throws FioranoException {
        return MicroServiceRepoManager.getInstance().readMicroService(launchConfiguration.getMicroserviceId(),
                launchConfiguration.getMicroserviceVersion());
    }

    public class InMemoryLaunchThread extends Thread {

        private final Method startup;
        private InMemoryRuntimeHandle runtimeHandle;

        public InMemoryLaunchThread(ClassLoader classLoader) throws Exception {
            serviceClassLoader = classLoader;
            this.runtimeHandle = new InMemoryRuntimeHandle(launchConfiguration);
            setName(launchConfiguration.getServiceName() + "  InMemory Launch Thread");
            startup = initStartMethod();
        }

        public void run() {
            ClassLoader serverClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                System.setProperty("FIORANO_HOME", System.getProperty("user.dir"));
                Thread.currentThread().setContextClassLoader(serviceClassLoader);
                startup.invoke(runtimeService, getArguments());
                runtimeHandle.generateServiceBoundEvent();
            } catch (Throwable e) {
                logger.error("Error starting service " + launchConfiguration.getApplicationName() + ":"
                        + launchConfiguration.getApplicationVersion() + "-" + launchConfiguration.getMicroserviceId() + ":" + launchConfiguration.getMicroserviceVersion()
                        + e.getMessage(), e);
            } finally {
                Thread.currentThread().setContextClassLoader(serverClassLoader);
            }
        }

        private Method initStartMethod() throws FioranoException, ClassNotFoundException, InstantiationException, IllegalAccessException {
            String m_implClass = getComponentPS().getExecution().getInMemoryExecutable();
            ClassLoader serverClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(serviceClassLoader);
                if (m_implClass == null || m_implClass.trim().length() == 0)
                    throw new FioranoException(Bundle.class, LaunchErrorCodes.COMPONENT_INMEMORY_IMPL_NOT_SPECIFIED,
                            Bundle.COMPONENT_IMPL_INVALID);
                serviceClass = Class.forName(m_implClass, true, serviceClassLoader);
                try {
                    runtimeService = serviceClass.newInstance();
                } catch (ClassCastException e) {
                    throw new FioranoException(Bundle.class, LaunchErrorCodes.COMPONENT_CANNOT_LAUNCH_IN_MEMORY,
                            Bundle.COMPONENT_IMPL_INVALID);
                }
                Method startup;
                try {
                    //noinspection unchecked
                    startup = serviceClass.getMethod("startup", String[].class);
                    if (startup == null)
                        throw new FioranoException("Could not find the startup(Object) method.");
                } catch (NoSuchMethodException e) {
                    throw new FioranoException(Bundle.class, LaunchErrorCodes.COMPONENT_CANNOT_LAUNCH_IN_MEMORY, e,
                            Bundle.COMPONENT_IMPL_INVALID);
                }
                return startup;
            } finally {
                Thread.currentThread().setContextClassLoader(serverClassLoader);
            }
        }

        private Object[] getArguments() throws Exception {
            Object[] argListForInvokedMain = new Object[1];
            CommandProvider commandProvider = new JVMCommandProvider();
            @SuppressWarnings("unchecked")
            List<String> list = commandProvider.getCommandLineParams(launchConfiguration);
            argListForInvokedMain[0] = list.toArray(new String[list.size()]);
            return argListForInvokedMain;
        }

    }

    public class InMemoryRuntimeHandle extends MicroServiceRuntimeHandle {

        public InMemoryRuntimeHandle(LaunchConfiguration launchConfiguration) {
            super(launchConfiguration);
            this.launchConfiguration = launchConfiguration;
            isRunning = true;
            strStatus = EventStateConstants.SERVICE_HANDLE_BOUND;
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() throws Exception {
            ClassLoader serverClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                ClassLoader stopClassLoader = classLoaderManager.getClassLoader(getComponentPS(), launchConfiguration, RemoveInfo.class.getClassLoader());
                Thread.currentThread().setContextClassLoader(serviceClassLoader);
                @SuppressWarnings("unchecked")
                Method shutDownMethod = serviceClass.getMethod("shutdown", Object.class);
                shutDownMethod.invoke(runtimeService, "Shutdown Microservice");
            } finally {
                Thread.currentThread().setContextClassLoader(serverClassLoader);
            }
            isRunning = false;
            gracefulKill = true;
            classLoaderManager.unloadClassLoader(getComponentPS(), launchConfiguration);
            serviceClassLoader = null;
            strStatus = EventStateConstants.SERVICE_HANDLE_UNBOUND;
            generateServiceUnboundEvent("Shutdown", false);
        }

        public void kill() throws Exception {
            stop();
        }

        @Override
        public void setLogLevel(Map<String, String> modules) throws FioranoException {
            for (Map.Entry<String, String> modifiedLevel : modules.entrySet()) {
                java.util.logging.Logger logger = LoggerUtil.getServiceLogger(modifiedLevel.getKey(), launchConfiguration.getApplicationName(),
                        launchConfiguration.getApplicationVersion(), launchConfiguration.getMicroserviceId(), launchConfiguration.getServiceName());
                for (Handler handler : logger.getHandlers()) {
                    logger.removeHandler(handler);
                    if (handler instanceof FioranoLogHandler)
                        ((FioranoLogHandler) handler).setLogLevel(Level.parse(modifiedLevel.getValue()));
                    else
                        handler.setLevel(Level.parse(modifiedLevel.getValue()));
                    logger.addHandler(handler);
                }
            }
        }

        @Override
        public ComponentStats getComponentStats() throws FioranoException {
            return null;
        }

        @Override
        public LaunchConfiguration.LaunchMode getLaunchMode() {
            return LaunchConfiguration.LaunchMode.IN_MEMORY;
        }
    }
}
