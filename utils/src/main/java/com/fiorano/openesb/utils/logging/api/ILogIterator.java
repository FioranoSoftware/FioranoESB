/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging.api;

public interface ILogIterator
{
    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext();

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext(int logFileNo);

    /**

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     */
    public String next();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     */
    public String next(int logFileNo);

    /**
     * Returns <tt>true</tt> if this list iterator has more elements when
     * traversing the list in the reverse direction.  (In other words, returns
     * <tt>true</tt> if <tt>previous</tt> would return an element rather than
     * throwing an exception.)
     *
     * @return <tt>true</tt> if the list iterator has more elements when
     *	       traversing the list in the reverse direction.
     */
    public boolean hasPrevious();

    /**
     * Returns the previous element in the list.  This method may be called
     * repeatedly to iterate through the list backwards, or intermixed with
     * calls to <tt>next</tt> to go back and forth.  (Note that alternating
     * calls to <tt>next</tt> and <tt>previous</tt> will return the same
     * element repeatedly.)
     *
     * @return the previous element in the list.
     */
    public String previous();

    /**
     * Close the resources opened for this iterator
     */
    public void close();
}
