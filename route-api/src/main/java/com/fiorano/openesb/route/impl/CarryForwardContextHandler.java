/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.route.impl;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.InputPortInstance;
import com.fiorano.openesb.application.application.PortInstance;
import com.fiorano.openesb.application.application.Transformation;
import com.fiorano.openesb.route.FilterMessageException;
import com.fiorano.openesb.route.RouteOperationHandler;
import com.fiorano.openesb.route.bundle.Activator;
import com.fiorano.openesb.transport.impl.jms.JMSMessage;
import com.fiorano.openesb.utils.SourceContext;
import com.fiorano.openesb.utils.StringUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;

public class CarryForwardContextHandler implements RouteOperationHandler<JMSMessage> {
    // Transformation helper
    private Transformation m_transformHelper;

    // Specifies whether xalan or saxon should be used in transformations
    private String m_transformerType;
    private PortInstance port;
    private String serviceInstName;
    private Application application;
    private Logger logger;


    private final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";


    public CarryForwardContextHandler(CarryForwardContextConfiguration carryForwardContextConfiguration) {

        this.application = carryForwardContextConfiguration.getApplication();
        this.serviceInstName = carryForwardContextConfiguration.getServiceInstanceName();
        this.port = carryForwardContextConfiguration.getPortInstance();
        this.logger = LoggerFactory.getLogger(Activator.class);
    }

    @Override
    public void handleOperation(JMSMessage message) throws FilterMessageException, FioranoException {
        Message jmsMessage = message.getMessage();

        CarryForwardContext carryForwardContext = null;
        try {
            carryForwardContext = JmsMessageUtil.getCarryForwardContext(jmsMessage);
        } catch (Exception e) {
            throw new FioranoException(e);
        }
        if (port.getAppContextAction().equals(PortInstance.STORE_APP_CONTEXT)) {
            if (carryForwardContext != null && !StringUtil.isEmpty(carryForwardContext.getAppContext())) {
                try {
                    jmsMessage.setStringProperty(application.getGUID() + "__" + application.getVersion() + "__" + APPLICATION_CONTEXT, carryForwardContext.getAppContext());
                } catch (JMSException e) {
                    logger.error(e.getMessage(), e);
                }
                carryForwardContext.setAppContext(null);
                try {
                    JmsMessageUtil.setCarryForwardContext(jmsMessage, carryForwardContext);
                } catch (Exception e) {
                    throw new FioranoException(e);
                }
            }
        } else if (port.getAppContextAction().equals(PortInstance.RESTORE_APP_CONTEXT)) {
            String appContext = null;
            try {
                appContext = jmsMessage.getStringProperty(application.getGUID() + "__" + application.getVersion() + "__" + APPLICATION_CONTEXT);
            } catch (JMSException e) {
                logger.error(e.getMessage(), e);
            }
            if (appContext != null) {
                try {
                    jmsMessage.setStringProperty(application.getGUID() + "__" + application.getVersion() + "__" + APPLICATION_CONTEXT, null);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                if (carryForwardContext != null) {
                    carryForwardContext.setAppContext(appContext);
                    try {
                        JmsMessageUtil.setCarryForwardContext(jmsMessage, carryForwardContext);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (port.getAppContextAction().equals(PortInstance.SET_DEFAULT_APP_CONTEXT)) {
            // TODO: 3/2/16 check toString
            String defaultAppContext = application.getApplicationContext().getValue();
            if (carryForwardContext != null) {
                carryForwardContext.setAppContext(defaultAppContext);
            } else {
                carryForwardContext = new CarryForwardContext();
                carryForwardContext.setAppContext(defaultAppContext);
            }
            try {
                JmsMessageUtil.setCarryForwardContext(jmsMessage, carryForwardContext);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        //set application environment
        try {
            JmsMessageUtil.setEventProcessEnv(jmsMessage, application.getLabel());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {

            if (isInputPort()) {
                jmsMessage.setStringProperty("ESBX__SYSTEM__INPUT_PORT", port.getName());
            } else {
                jmsMessage.setStringProperty("ESBX__SYSTEM__OUTPUT_PORT", port.getName());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {
            carryForwardContext = JmsMessageUtil.getCarryForwardContext(jmsMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // Setting carry forward context to default values in the absence of carry forward context, to specify message coming from an external source
        // This presence of carry forward context also generates carry forward properties that is used at the time of document re- injection
        if (carryForwardContext == null) {

            carryForwardContext = new CarryForwardContext();
            SourceContext sourceContext = new SourceContext();
            sourceContext.setAppInstName(application.getDisplayName());
            sourceContext.setAppInstVersion(String.valueOf(application.getVersion()));
            sourceContext.setSrvInstName(serviceInstName);
            sourceContext.setNodeName("fps1");
            carryForwardContext.addContext(sourceContext);

            try {
                JmsMessageUtil.setCarryForwardContext(jmsMessage, carryForwardContext);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            // Source should be set on messages originating from the first service.
            // So moved addSourceContext from input port to output port
            // -Aseem
            //
            // update application context
            try {
                addSourceContext(jmsMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            // Set outtime as message on output can be published
            // by component only. (component includes all jms applications)
            //
            // We should not set outtime on messages published by routes
            // which cannot be possible as this is an output port
            //
            // MessageUtil.setOutTime(msg, System.currentTimeMillis());
            /*if(msg instanceof TextMessage)
                applyContextTranformation((TextMessage)msg);*/

        }

        if (port.getName().endsWith(PortInstance.EXCEPTION_PORT_NAME)) {
            try {
                JmsMessageUtil.setPortName(jmsMessage, port.getName());
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                JmsMessageUtil.setCompInstName(jmsMessage, application.getServiceInstance(serviceInstName).getGUID());
                JmsMessageUtil.setEventProcessName(jmsMessage, application.getGUID());
                JmsMessageUtil.setEventProcessVersion(jmsMessage, application.getSchemaVersion());
                //JmsMessageUtil.setSourceFPSName(msg, handle.slp.getNodeName());
            } catch (Exception e) {

            }
        }
    }

    private void addSourceContext(Message msg) throws JMSException {
        CarryForwardContext carryForwardContext = (CarryForwardContext)
                JmsMessageUtil.getCarryForwardContext(msg);

        if (carryForwardContext == null)
            carryForwardContext = new CarryForwardContext();

        SourceContext sourceContext = new SourceContext();
        sourceContext.setAppInstName(application.getGUID());
        sourceContext.setAppInstVersion(String.valueOf(application.getVersion()));
        sourceContext.setSrvInstName(serviceInstName);
        carryForwardContext.addContext(sourceContext);

        JmsMessageUtil.setCarryForwardContext(msg, carryForwardContext);
    }

    public boolean isInputPort() {
        return port instanceof InputPortInstance;
    }
}
