package org.serrodcal.custom.publisher.observer;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.aspects.flow.statistics.publishing.PublishingFlow;
import org.wso2.carbon.das.messageflow.data.publisher.observer.MessageFlowObserver;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.Map;

public class ZipkinOpenTracingMediationFlowObserver implements MessageFlowObserver {

    private static final Log log = LogFactory.getLog(ZipkinOpenTracingMediationFlowObserver.class);

    private final Sender sender;
    private final Tracer tracer;
    private final Tracing braveTracing;

    public ZipkinOpenTracingMediationFlowObserver() {
        this.sender = OkHttpSender.create("http://zipkin:9411/api/v2/spans");
        Reporter spanReporter = AsyncReporter.create(this.sender);
        this.braveTracing = Tracing.newBuilder().localServiceName("wso2esb-5.0.0").spanReporter(spanReporter).build();
        this.tracer = BraveTracer.create(this.braveTracing);
    }

    @Override
    public void destroy() {
        try {
            this.sender.close();
        }catch (Exception e){
            // Do nothing
        }
        this.braveTracing.close();
        System.exit(0);
    }

    @Override
    public void updateStatistics(PublishingFlow publishingFlow) {
        //TODO
        Span span = tracer.buildSpan("example")
                .withTag("component","wso2esb")
                .withTag("span.kind","esb")
                .start();
        String id = publishingFlow.getMessageFlowId();
        Map<String, Object> map = publishingFlow.getObjectAsMap();
        log.error(id);
        log.error(map.toString());
        span.finish();
        /*for (PublishingEvent event : publishingFlow.getEvents()) {
            event.
        }*/
    }
}
