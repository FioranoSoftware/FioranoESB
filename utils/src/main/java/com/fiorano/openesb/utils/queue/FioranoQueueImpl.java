/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.queue;

import java.util.Enumeration;

public class FioranoQueueImpl implements IFioranoQueue {
    //  number of elements in the queue at any point of execution
    protected volatile int m_nSize = 0;

    //  Head of queue, elements are popped off from the m_head of the queue
    protected IFioranoQueueable m_head = null;

    //  End of the queue, new elements are added to the end of the queue
    protected IFioranoQueueable m_tail = null;

    /**
     *  Pushes an Object into the queue if it does not exceed the
     *  maximum allowed range.
     *
     *  @param data the Object to be pushed
     *  @roseuid 35EC85E9002F
     */
    public void push(Object data)
    {
        //  Creates a new node and assign the Object to it.
        //
        IFioranoQueueable newNode = new FioranoQueueNode (data);

        //  push this node into the Queue.
        //
        push (newNode);
    }

    /**
     *  Pushes a Queueable Object into the queue. The new element
     *  is added to the BOTTOM of the Queue.
     *
     *  @param newNode the Object to be pushed
     */
    public synchronized void push (IFioranoQueueable newNode)
    {
        if (newNode == null)
            return;

        //  Check if this is the first element being added to this
        //  queue or some nodes already exists.
        //
        if (m_tail == null)
        {
            //  If it's the first element than set the m_head and
            //  the m_tail  of the Object to the same Object
            //  created hereby.
            //
            m_tail = newNode;
            m_head = newNode;
        }
        else
        {
            //  Otherwise readjust the m_tail of the queue
            //
            m_tail.setNext (newNode);
            m_tail = newNode;
        }

        //  Increement the size of the queue to represent the
        //  addition of a new node.
        //
        ++m_nSize;
    }

    /**
     *  Pops the element if any from the Queue.
     *  @return Object the poped element. return null, if
     *          this Queue is empty.
     *  @roseuid 35EC85E9015C
     */
    public synchronized Object pop()
    {
        //  If the queue doesn't contain any elements, throw QueueUnderflowException.
        //
        if (m_nSize == 0)
            return null;

        //  Get an object from the m_head of the queue and move the m_head pointer
        //
        Object poppedObj = m_head.getData ();
        IFioranoQueueable poppedNode = m_head;

        //  If this operation empties this queue, than reset m_head and m_tail to null values.
        //  otherwise move the m_head pointer to the next element in the queue.
        //
        if (m_head == m_tail)
            m_head = m_tail = null;
        else
            m_head = m_head.getNext ();

        poppedNode.setNext (null);

        //  decreement size and return the popped off value.
        //
        --m_nSize;
        return poppedObj;
    }

    /**
     *  @roseuid 35EC85E901C0
     */
    public synchronized void pushWithNotify (Object data)
    {
        push (data);
        this.notify ();
    }

    /**
     *  Pushes a Queueable Object into the queue if it does not
     *  exceed the maximum allowed range.
     *  After pushing this node, notify a single threads waiting
     *  on this Queue for data.
     *
     *  @param newNode the Object to be pushed
     */
    public synchronized void pushWithNotify (IFioranoQueueable newNode)
    {
        push (newNode);
        this.notify ();
    }

    /**
     *  Pops an element if any. If their is no element in the queue then waits for
     *  the specified <code>timeout</code> milliseconds. An exception
     *  occurs if no data could be poped after the <code>timeout</code> milliseconds.
     *  This method has to be used with <code>pushWithNotify</code>.
     *  @param timeout the time in milliseconds to wait. A zero value
     *         indicates infinite wait.
     *         If a node is not available till the timeout occurs, null
     *         is returned.
     *  @return Object the poped element
     *  @roseuid 35EC85E902EC
     */
    public synchronized Object popWithWait(long timeout)
    {
        //  If the queue doesn't contain any elements, wait for a maximum of timeout
        //  period for an element to be pushed (notifying this Object in the process,
        //  using pushWithNotify () method.
        //
        if (m_nSize <= 0)
        {
            long timeRemaining = 0;
            long startTime = 0;
            boolean bInfiniteWait = true;

            if (timeout > 0)
            {
                bInfiniteWait = false;
                timeRemaining = timeout;
                startTime = System.currentTimeMillis ();
            }

            while (true)
            {
                try
                {
                    //  Wait for a maximum of timeout period.
                    //
                    this.wait (timeRemaining);
                }
                catch (Exception e)
                {
                    //  If unable to wait, then ignore this failure and recheck
                    //  the size of the queue.
                    //
                }

                if (m_nSize > 0)
                    break;

                if (!bInfiniteWait)
                {
                    timeRemaining = timeout - (System.currentTimeMillis () - startTime);
                    if (timeRemaining <= 0)
                        break;
                }
            }
        }

        return pop ();
    }

    /**
     *  Pops an element if any. If their is no element in the queue then waits for
     *  the specified <code>timeout</code> milliseconds. An exception
     *  occurs if no data could be poped after the <code>timeout</code> milliseconds.
     *  This method has to be used with <code>pushWithNotify</code>.
     *
     *  It addition to the normal popWithWait () call as defined above,
     *  this call verifies the state of the calle, each time it enters
     *  wait state. If the 'callee' has died than this method returns
     *  prematurely even before the timeout occurs.
     *
     *  @param timeout the time in milliseconds to wait. A zero value
     *         indicates infinite wait.
     *         If a node is not available till the timeout occurs, null
     *         is returned.
     *  @return Object the poped element
     *  @roseuid 35EC85E902EC
     */
    public synchronized Object popWithWait (long timeout, ICallState callee)
    {
        //  If the queue doesn't contain any elements, wait for a maximum of timeout
        //  period for an element to be pushed (notifying this Object in the process,
        //  using pushWithNotify () method.
        //
        if (m_nSize <= 0)
        {
            long timeRemaining = 0;
            long startTime = 0;
            boolean bInfiniteWait = true;

            if (timeout > 0)
            {
                bInfiniteWait = false;
                timeRemaining = timeout;
                startTime = System.currentTimeMillis ();
            }

            while (true)
            {
                try
                {
                    if (!callee.isAlive())
                        return null;

                    //  Wait for a maximum of timeout period.
                    //
                    this.wait (timeRemaining);
                }
                catch (Exception e)
                {
                    //  If unable to wait, then ignore this failure and recheck
                    //  the size of the queue.
                    //
                }

                if (m_nSize > 0)
                    break;

                if (!bInfiniteWait)
                {
                    timeRemaining -= (System.currentTimeMillis () - startTime);
                    if (timeRemaining <= 0)
                        break;
                }
            }
        }

        return pop ();
    }

    /**
     *  @roseuid 35EC85EA0031
     */
    public synchronized Enumeration elements()
    {
        return new FioranoQueueEnumerator (m_head,m_nSize);
    }

    /**
     *  @roseuid 35ED920000ED
     */
    public synchronized int getSize()
    {
        return m_nSize;
    }

    /**
     *    Adds all elements of a FioranoQueueImpl to this Object.
     */
    public synchronized void addQueue (IFioranoQueue toAdd)
    {
        if (toAdd == null)
            return;

        Object current = null;
        while ((current = toAdd.pop ()) != null)
        {
            push (current);
        }

        // NOTE TO DEEPAK::
        // This is totally buggy. If the toAdd Queue has atleast 1 non-null element
        // in it. It will go into an infinite loop also there is No need for
        // the synhronization here.

        /*synchronized (toAdd)
        {
        	for (Object current = toAdd.pop(); current != null; )
        		push (current);
        }*/
    }

    /**
     *  @roseuid 35ED92000151
     *  Fixed for bugID: 7261.
     */
    public synchronized boolean remove(Object toRemove)
    {
        //  If the queue doesn't contain any elements, return false;
        //
        try
        {
            if (m_nSize == 0)
                return false;

            if (m_head.getData () == toRemove)
            {
                IFioranoQueueable poppedNode = m_head;

                //  If this operation empties this queue, than reset m_head and m_tail to null values.
                //  otherwise move the m_head pointer to the next element in the queue.
                //
                if (m_head == m_tail)
                    m_head = m_tail = null;
                else
                    m_head = m_head.getNext ();

                poppedNode.setNext (null);

                //  decreement size and return the popped off value.
                //
                --m_nSize;
                return true;
            }

            IFioranoQueueable parent = m_head;
            IFioranoQueueable current = m_head.getNext ();

            while (current != null)
            {
                if (current.getData () == toRemove)
                {
                    IFioranoQueueable poppedNode = current;

                    if (current == m_tail)
                        m_tail = parent;

                    parent.setNext (current.getNext ());

                    poppedNode.setNext (null);

                    //  decreement size and return the popped off value.
                    //
                    --m_nSize;
                    return true;
                }

                parent = current;
                current = current.getNext ();
            }
        }
        catch (Throwable err)
        {
            // Exception during remove.. return false
        }
        return false;
    }

    /**
     *  @remove all nodes in this Queue.
     */
    public synchronized void clear ()
    {
        if (m_head == null)
            return;

        m_nSize = 0;
        IFioranoQueueable next = null;
        while (m_head != null)
        {
            next = m_head.getNext ();
            m_head.setNext (null);
            m_head = next;
        }
        m_tail = null;
    }
}
