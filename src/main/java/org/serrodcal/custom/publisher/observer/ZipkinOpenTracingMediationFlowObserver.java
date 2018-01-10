package org.serrodcal.custom.publisher.observer;

import org.apache.synapse.aspects.flow.statistics.publishing.PublishingFlow;
import org.wso2.carbon.das.messageflow.data.publisher.observer.MessageFlowObserver;

public class ZipkinOpenTracingMediationFlowObserver implements MessageFlowObserver {
    @Override
    public void destroy() {

    }

    @Override
    public void updateStatistics(PublishingFlow publishingFlow) {

    }
}
