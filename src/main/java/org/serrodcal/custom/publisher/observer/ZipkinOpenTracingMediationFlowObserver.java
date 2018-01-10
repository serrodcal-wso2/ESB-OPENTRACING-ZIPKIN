package org.serrodcal.custom.publisher.observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.aspects.flow.statistics.publishing.PublishingFlow;
import org.serrodcal.custom.publisher.services.ZipkinOpenTracingPublisherThread;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.das.messageflow.data.publisher.observer.MessageFlowObserver;

import java.util.HashMap;
import java.util.Map;

public class ZipkinOpenTracingMediationFlowObserver implements MessageFlowObserver {

    private static final Log log = LogFactory.getLog(ZipkinOpenTracingMediationFlowObserver.class);

    private ZipkinOpenTracingPublisherThread publisherThread;

    // ServerConfiguration
    private ServerConfiguration serverConf = ServerConfiguration.getInstance();

    // Keep all needed configurations (final configurations)
    private Map<String, Object> configurations = new HashMap<>();

    public ZipkinOpenTracingMediationFlowObserver() {

    }

    @Override
    public void destroy() {
        publisherThread.shutdown();

        //TODO: close all

        if (log.isDebugEnabled()) {
            log.debug("Shutting down the mediation statistics observer of Zipkin OpenTracing");
        }
    }

    @Override
    public void updateStatistics(PublishingFlow publishingFlow) {
        //TODO
    }
}
