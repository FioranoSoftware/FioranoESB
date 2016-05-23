/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.service;

import com.fiorano.openesb.application.CommonSchemas;
import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.MapThreadLocale;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.application.sps.ServicePropertySheet;
import com.fiorano.openesb.utils.FioranoStaxParser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class Service extends ServiceMetadata{

    public int getObjectID(){
        return DmiObjectTypes.NEW_SERVICE;
    }

    /*-------------------------------------------------[ Deployment ]---------------------------------------------------*/

    private Deployment deployment = new Deployment();

    /**
     * Returns deployment attribute of this service
     * @return Deployment - Deployment attribute
     */
    public Deployment getDeployment(){
        return deployment;
    }

    /**
     * Sets deployment attribute of this service
     * @param deployment Deployment attribute
     */
    public void setDeployment(Deployment deployment){
        this.deployment = deployment;
    }

    /*-------------------------------------------------[ Execution ]---------------------------------------------------*/

    private Execution execution = null;

    /**
     * Returns execution attribute of this service
     * @return Execution - Execution attribute
     */
    public Execution getExecution(){
        return execution;
    }

    /**
     * Sets execution attribute of this service
     * @param execution Execution attribute
     */
    public void setExecution(Execution execution){
        this.execution = execution;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /**
     * <service>
     *      ...superclass...
     *      ...deployment...
     *      ...execution?...
     * </service>
     */

    protected void toJXMLString_1(XMLStreamWriter writer,boolean writeCDataSections) throws XMLStreamException, FioranoException{
        MapThreadLocale.getInstance().getMap().put("ERROR_XSD", CommonSchemas.ERROR_XSD);

        try{
            deployment.toJXMLString(writer);
            if(execution!=null)
                execution.toJXMLString(writer,writeCDataSections);
        } finally{
            MapThreadLocale.getInstance().getMap().clear();
        }
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        MapThreadLocale.getInstance().getMap().put("ERROR_XSD", CommonSchemas.ERROR_XSD);
        try{
            super.populate(cursor);
        } finally{
            MapThreadLocale.getInstance().getMap().clear();
        }
    }

    protected void populate_1(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        String elemName = cursor.getLocalName();
        if(Deployment.ELEM_DEPLOYMENT.equals(elemName))
            deployment.setFieldValues(cursor);
        else if(Execution.ELEM_EXECUTION.equals(elemName)){
            execution = new Execution();
            execution.setFieldValues(cursor);
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    protected void convert_1(ServicePropertySheet that){
        deployment.convert(that.getDeploymentInfo());
        if(that.getServiceHeader().isLaunchable()){
            execution = new Execution();
            execution.convert(that);
        }
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    public void reset(){
        super.reset();
        deployment.reset();
        if(execution!=null)
            execution.reset();
    }

    public void validate() throws FioranoException{
        super.validate();
        deployment.validate();
        if(execution!=null)
            execution.validate();
    }
}
