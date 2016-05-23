/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.application.application;

import com.fiorano.openesb.application.DmiObjectTypes;
import com.fiorano.openesb.application.aps.PortInst;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.FioranoStaxParser;
import org.apache.commons.lang3.StringUtils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class InputPortInstance extends PortInstance{

    /**
     * element inputport-instance in event process xml
     */
    public static final String ELEM_INPUT_PORT_INSTANCE = "inputport-instance";

    public int getObjectID(){
        return DmiObjectTypes.NEW_INPUT_PORT_INSTANCE;
    }

    /*-------------------------------------------------[ transacted ]---------------------------------------------------*/
    /**
     * element inputport-instance in event process xml
     */
    public static final String ELEM_SUBSCRIBER = "subscriber";
    /**
     * element transaction in event process xml
     */
    public static final String ELEM_TRANSACTION = "transaction";
    /**
     * attribute enabled
     */
    public static final String ATTR_TRANSACTED = "enabled";

    private boolean transacted;

    /**
     * Specifies whether this input port is transacted or not
     * @return boolean
     */
    public boolean isTransacted(){
        return transacted;
    }

    /**
     * Sets a boolean specifying whether this input port is transacted or not
     * @param transacted boolean
     */
    public void setTransacted(boolean transacted){
        this.transacted = transacted;
    }

    /*-------------------------------------------------[ transactionSize ]---------------------------------------------------*/
    /**
     * attribute size
     */
    public static final String ATTR_TRANSACTION_SIZE = "size";

    private int transactionSize = 0;

    /**
     * Returns transaction Size of this input port
     * @return int
     */
    public int getTransactionSize(){
        return transactionSize;
    }

    /**
     * Sets specified <code>transactionSize</code> for this input port
     * @param transactionSize transaction size to be set
     */
    public void setTransactionSize(int transactionSize){
        this.transactionSize = transactionSize;
    }

    /*-------------------------------------------------[ DurableSubscription ]---------------------------------------------------*/
    /**
     * element subscription in event process xml
     */
    public static final String ELEM_SUBSCRIPTION = "subscription";
    /**
     * attribute durable
     */
    public static final String ATTR_DURABLE_SUBSCRIPTION = "durable";

    private boolean durableSubscription = false;

    /**
     * Returns whether durable Subscription is enabled on this input port
     * @return boolean
     */
    public boolean isDurableSubscription(){
        return durableSubscription;
    }

    /**
     * Sets a boolean specifying whether durable subscription is enabled on this input port
     * @param durableSubscription boolean
     */
    public void setDurableSubscription(boolean durableSubscription){
        this.durableSubscription = durableSubscription;
    }

    /*-------------------------------------------------[ SubscriptionName ]---------------------------------------------------*/
    /**
     * attribute name
     */
    public static final String ATTR_SUBSCRIPTION_NAME = "name";

    private String subscriptionName;

    /**
     * Returns subscription Name for this input port
     * @return String
     */
    public String getSubscriptionName(){
        return subscriptionName;
    }

    /**
     * Sets specified <code>subscriptionName</code> for this input port
     * @param subscriptionName subscription name to be set
     */
    public void setSubscriptionName(String subscriptionName){
        this.subscriptionName = subscriptionName;
    }

    /*-------------------------------------------------[ SessionCount ]---------------------------------------------------*/
    /**
     * attribute sessions
     */
    public static final String ATTR_SESSION_COUNT = "sessions";

    private int sessionCount = 1;

    /**
     * Returns Maximum number of sessions to be created for this input port. Used mainly for multi-threading support.
     * @return int
     */
    public int getSessionCount(){
        return sessionCount;
    }

    /**
     * Sets maximum number of sessions for this input port
     * @param sessionCount number of sessions to be set
     */
    public void setSessionCount(int sessionCount){
        this.sessionCount = sessionCount;
    }

    /*-------------------------------------------------[ acknowledgementMode ]---------------------------------------------------*/
    /**
     * attribute acknowledgement
     */
    public static final String ATTR_ACKNOWLEDGEMENT_MODE = "acknowledgement";
    /**
     * acknowledgement mode auto acknowledge
     */
    public static final int ACKNOWLEDGEMENT_MODE_AUTO = 1;
    /**
     * acknowledgement mode client acknowledge
     */
    public static final int ACKNOWLEDGEMENT_MODE_CLIENT = 2;
    /**
     * acknowledgement mode dups ok acknowledge
     */
    public static final int ACKNOWLEDGEMENT_MODE_DUPS_OK = 3;

    private int acknowledgementMode = ACKNOWLEDGEMENT_MODE_DUPS_OK;

    /**
     * Returns JMS acknowledgement mode for this input port
     * @return int
     */
    public int getAcknowledgementMode(){
        return acknowledgementMode;
    }

    /**
     * Sets acknowledgement mode for this input port
     * @param acknowledgementMode acknowledgement mode to be set
     */
    public void setAcknowledgementMode(int acknowledgementMode){
        this.acknowledgementMode = acknowledgementMode;
    }

    /*-------------------------------------------------[ MessageSelector ]---------------------------------------------------*/
    /**
     * attribute messageSelector
     */
    public static final String ATTR_MESSAGE_SELECTOR = "messageSelector";

    private String messageSelector;

    /**
     * Returns message selector for this input port
     * @return String
     */
    public String getMessageSelector(){
        return messageSelector;
    }

    /**
     * Sets message selector for this input port
     * @param messageSelector message selector to be set
     */
    public void setMessageSelector(String messageSelector){
        this.messageSelector = messageSelector;
    }

    /*-------------------------------------------------[ subscriber-config-name ]---------------------------------------------------*/
    /**
     * element subscriber-config-name in event process xml
     */
    public static final String ELEM_SUBSCRIBER_CONFIG_NAME = "subscriber-config-name";

    private String subscriberConfigName;

    /**
     * Returns configuration name of this subscriber instance
     * @return String
     */
    public String getSubscriberConfigName(){
        return subscriberConfigName;
    }

    /**
     * Sets configuration name of this subscriber instance
     * @param subscriberConfigName configuration name
     */
    public void setSubscriberConfigName(String subscriberConfigName){
        this.subscriberConfigName = subscriberConfigName;
    }

    /*-------------------------------------------------[ To XML ]---------------------------------------------------*/

    /*
     * <inputport-instance>
     *      ...super-class...
     *      <jms>
     *          ...super-class...
     *          <subscriber sessions="int"? acknowledgement="int"? selector="string"?>
     *              <transaction enabled="boolean" size="int"?/>
     *              <subscription durable="boolean" name="string"?/>
     *          </subscriber>
     *      </jms>
     *      ...super-class...
     * </inputport-instance>
     */

    protected void toJXMLString(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        toJXMLString(writer, ELEM_INPUT_PORT_INSTANCE, writeSchema);
    }


    protected void toJXMLString(XMLStreamWriter writer) throws XMLStreamException, FioranoException {
        toJXMLString(writer, true);
    }

    protected void toJXMLString_2(XMLStreamWriter writer, boolean writeSchema) throws XMLStreamException, FioranoException{
        if (writeSchema || subscriberConfigName == null) {         // We need to write port properties to stream when passing application launch packet to peer
            writer.writeStartElement(ELEM_SUBSCRIBER);
            {
                if(sessionCount!=1)
                    writer.writeAttribute(ATTR_SESSION_COUNT, String.valueOf(sessionCount));
                if(acknowledgementMode!=ACKNOWLEDGEMENT_MODE_DUPS_OK)
                    writer.writeAttribute(ATTR_ACKNOWLEDGEMENT_MODE, String.valueOf(acknowledgementMode));
                if(!StringUtils.isEmpty(messageSelector))
                    writer.writeAttribute(ATTR_MESSAGE_SELECTOR, String.valueOf(messageSelector));

                writer.writeStartElement(ELEM_TRANSACTION);
                {
                    writer.writeAttribute(ATTR_TRANSACTED, String.valueOf(transacted));
                    if(transactionSize!=0)
                        writer.writeAttribute(ATTR_TRANSACTION_SIZE, String.valueOf(transactionSize));
                }
                writer.writeEndElement();

                writer.writeStartElement(ELEM_SUBSCRIPTION);
                {
                    writer.writeAttribute(ATTR_DURABLE_SUBSCRIPTION, String.valueOf(durableSubscription));
                    if(!StringUtils.isEmpty(subscriptionName))
                        writer.writeAttribute(ATTR_SUBSCRIPTION_NAME, String.valueOf(subscriptionName));
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        } else {
            writer.writeStartElement(ELEM_SUBSCRIBER_CONFIG_NAME);
            {
                writer.writeAttribute(ATTR_NAME, subscriberConfigName);
            }
            writer.writeEndElement();
        }
    }

    /*-------------------------------------------------[ From XML ]---------------------------------------------------*/

    protected void populate(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        populate(cursor, ELEM_INPUT_PORT_INSTANCE);
    }

    protected void populate_2(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        String elemName = cursor.getLocalName();
        if(ELEM_SUBSCRIBER.equals(elemName)){
            populateSubscriberConfiguration(cursor);
        }else if(ELEM_SUBSCRIBER_CONFIG_NAME.equals(elemName)){
            subscriberConfigName = cursor.getAttributeValue(null, ATTR_NAME);
        }
    }

    /**
     * Populates the Subscriber Configuration for this Object.
     * @param cursor Parser containing the config information
     * @throws XMLStreamException
     * @throws FioranoException
     */
    public void populateSubscriberConfiguration(FioranoStaxParser cursor) throws XMLStreamException, FioranoException{
        if(cursor.markCursor(ELEM_SUBSCRIBER)){
            sessionCount = getIntegerAttribute(cursor, ATTR_SESSION_COUNT, 1);
            acknowledgementMode = getIntegerAttribute(cursor, ATTR_ACKNOWLEDGEMENT_MODE, ACKNOWLEDGEMENT_MODE_DUPS_OK);
            messageSelector = cursor.getAttributeValue(null, ATTR_MESSAGE_SELECTOR);

            while(cursor.nextElement()){
                if(ELEM_TRANSACTION.equals(cursor.getLocalName())){
                    transacted = Boolean.valueOf(cursor.getAttributeValue(null, ATTR_TRANSACTED)).booleanValue();
                    transactionSize = getIntegerAttribute(cursor, ATTR_TRANSACTION_SIZE, 0);
                } else if(ELEM_SUBSCRIPTION.equals(cursor.getLocalName())){
                    durableSubscription = Boolean.valueOf(cursor.getAttributeValue(null, ATTR_DURABLE_SUBSCRIPTION)).booleanValue();
                    subscriptionName = cursor.getAttributeValue(null, ATTR_SUBSCRIPTION_NAME);
                }
            }
            
            cursor.resetCursor();
        }
    }

    /*-------------------------------------------------[ Migration ]---------------------------------------------------*/

    public void convert_2(PortInst that){
        sessionCount = that.getSessionCount();
        acknowledgementMode = that.getAcknowledgementMode();
        messageSelector = that.getMessageSelector();

        transacted = that.isTransacted();
        transactionSize = that.getTransactionSize();

        durableSubscription = that.isDurableSubscription();
        subscriptionName = that.getSubscriptionName();
    }

    /*-------------------------------------------------[ Other Methods ]---------------------------------------------------*/

    /**
     * Resets this input port instance DMI
     */
    public void reset(){
        super.reset();

        transacted = false;
        transactionSize = 0;

        durableSubscription = false;
        subscriptionName = null;

        sessionCount = 1;
        acknowledgementMode = ACKNOWLEDGEMENT_MODE_DUPS_OK;
        messageSelector = null;
        subscriberConfigName = null;
    }

    /**
     * Validates the InputPortInstance DMI. Checks whether all the mandatory fields are set.
     *
     * The Possible Error Mesages:
     * @bundle EMPTY_SUBSCRIPTION_NAME=The Subsctiption Name cannot be null or empty for a durable subscription
     */
    public void validate() throws FioranoException{
        super.validate();

        if(durableSubscription && StringUtils.isEmpty(subscriptionName))
            throw new FioranoException("EMPTY_SUBSCRIPTION_NAME");
    }

    public void toMessage(BytesMessage bytesMessage) throws JMSException {
        super.toMessage(bytesMessage);
        bytesMessage.writeInt(acknowledgementMode);
        bytesMessage.writeUTF(messageSelector);
        bytesMessage.writeInt(sessionCount);
        bytesMessage.writeUTF(subscriberConfigName);
        bytesMessage.writeUTF(subscriptionName);
        bytesMessage.writeInt(transactionSize);
    }

    public void fromMessage(BytesMessage bytesMessage) throws JMSException {
        super.fromMessage(bytesMessage);
        acknowledgementMode=bytesMessage.readInt();
        messageSelector=bytesMessage.readUTF();
        sessionCount=bytesMessage.readInt();
        subscriberConfigName=bytesMessage.readUTF();
        subscriptionName= bytesMessage.readUTF();
        transactionSize= bytesMessage.readInt();
    }
}
