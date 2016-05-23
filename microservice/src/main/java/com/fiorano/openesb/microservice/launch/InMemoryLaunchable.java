/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch;

public interface InMemoryLaunchable
{
    /**
     * This method is called by the Peer server when it wants to launch the component.
     * Start method should start the component.
     *
     * @param args - Arguments needs for the component. Ex- applicationGUID, serviceinstance, FPS connect url, Backup urls etc ...
     */
    void startup(String[] args);

    /**
     * This methods will be called from the peer when the component is stopped.
     * Shutdown methods should cleanup all resources used by the component and leave no open handles/connections.
     *
     * @param hint - hintable param for shutdown
     */
    void shutdown(Object hint);

    /**
     * Causes the current thread to wait, if necessary, until the process represented by this <code>Process</code> object has 
     * terminated. This method returns immediately if the subprocess has already terminated. If the subprocess has not yet
     * terminated, the calling thread will be blocked until the subprocess exits.
     *
     * @return     the exit value of the process. By convention, <code>0</code> indicates normal termination.
     * @exception InterruptedException  if the current thread is {@link Thread#interrupt() interrupted} by another thread
     *             while it is waiting, then the wait is ended and an {@link InterruptedException} is thrown.
     */
    int waitFor() throws InterruptedException;

    /**
     * Returns the exit value for the subprocess.
     *
     * @return  the exit value of the subprocess represented by this <code>Process</code> object. by convention, the value
     *          <code>0</code> indicates normal termination.
     * @exception IllegalThreadStateException  if the subprocess represented by this <code>Process</code> object has not yet terminated.
     */
    int exitValue();
}
