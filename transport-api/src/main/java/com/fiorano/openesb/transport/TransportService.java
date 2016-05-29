/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.transport;


public interface TransportService<E extends Port, M extends Message> {

    ConnectionProvider getConnectionProvider();

    E enablePort(PortConfiguration portConfiguration) throws Exception;

    void disablePort(PortConfiguration portConfiguration) throws Exception;

    Consumer<M> createConsumer(E port, ConsumerConfiguration consumerConfiguration,String sessionId) throws Exception;

    Producer<M> createProducer(E port, ProducerConfiguration producerConfiguration,String sessionId) throws Exception;

    M createMessage(MessageConfiguration config) throws Exception;

    void closeSession(String sessionId);

}
