/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.queue;

public class FioranoQueueNode implements IFioranoQueueable
{
    // next pointer for this node.
    private IFioranoQueueable m_next = null;

    //  data stored in this node.
    private Object m_data;

    /**
    *   Creates a new FioranoQueueNode containing a reference to
    *   a given node.
    */
    public FioranoQueueNode (Object data)
    {
        m_data = data;
    }

    /**
       @return the next node, null if this is the last node
     */
    public IFioranoQueueable getNext()
    {
        return m_next;
    }

    /**
       @roseuid set the next pointer to given node.
     */
    public void setNext(IFioranoQueueable next)
    {
        m_next = next;
    }

    /**
       @return the data stored in this node
     */
    public Object getData ()
    {
        return m_data;
    }
}
