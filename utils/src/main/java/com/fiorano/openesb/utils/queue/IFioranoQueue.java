/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.queue;

import java.util.Enumeration;

/**
   Defines the semantics of a Queue implementation
 */
public interface IFioranoQueue
{
    /**
       @roseuid 35EC533800B2
     */
    public void push(Object data);

    /**
       @roseuid 35EC53480000
     */
    public Object pop();

    /**
       @roseuid 35EC534B039E
     */
    public void pushWithNotify(Object data);

    /**
       @roseuid 35EC535301F1
     */
    public Object popWithWait(long timeout);

    public Object popWithWait(long timeout, ICallState callee);

    /**
     *    Adds all elements of a FioranoQueueImpl to this Object.
     */
    public void addQueue(IFioranoQueue toAdd);

    /**
       @roseuid 35EC54470300
     */
    public Enumeration elements();

    /**
       @roseuid 35ED91B500C7
     */
    public int getSize();

    /**
       @roseuid 35ED91C2029D
     */
    public boolean remove(Object toRemove);

    /**
       @remove all nodes in this Queue.
     */
    public void clear();
}
