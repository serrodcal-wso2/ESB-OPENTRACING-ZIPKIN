package org.serrodcal.custom.publisher.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ZipkinOpenTracingPublisherThread extends Thread {

    private static final Log log = LogFactory.getLog(ZipkinOpenTracingPublisherThread.class);

    // To stop running
    private volatile boolean shutdownRequested = false;

    public void init() {
        //TODO
    }

    @Override
    public void run() {
        if (log.isDebugEnabled()) {
            log.debug("Zipkin OpenTracing publisher thread started.");
        }
        //TODO
    }

    /**
     * Shutdown thread, stop running
     */
    public void shutdown() {
        if (log.isDebugEnabled()) {
            log.debug("Statistics reporter thread is being stopped");
        }
        shutdownRequested = true;
    }

    /**
     * @return boolean shutdownRequested
     */
    public boolean getShutdown() {
        return shutdownRequested;
    }

}
