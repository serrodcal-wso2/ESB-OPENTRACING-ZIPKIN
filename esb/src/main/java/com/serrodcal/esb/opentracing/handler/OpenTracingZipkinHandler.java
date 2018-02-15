package com.serrodcal.esb.opentracing.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import brave.Tracing;
import brave.opentracing.BraveTracer;
import io.opentracing.Span;
import io.opentracing.Tracer;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.TreeMap;

public class OpenTracingZipkinHandler extends AbstractSynapseHandler {

    private static final Log log = LogFactory.getLog("ZIPKIN_LOGGER");

    private final Tracing tracing;
    //private final OkHttpClient client;
    private final Tracer tracer;

    public OpenTracingZipkinHandler() {
        Sender sender = OkHttpSender.create("http://zipkin:9411/api/v2/spans");
        Reporter spanReporter = AsyncReporter.create(sender);
        Tracing braveTracing = Tracing.newBuilder().localServiceName("esb").spanReporter(spanReporter).build();
        this.tracing = braveTracing;
        //this.client = new OkHttpClient();
        this.tracer = BraveTracer.create(this.tracing);
    }

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {
        TreeMap headers_map = this.getHeaders(messageContext);

        if(headers_map != null) {
            //TODO: iniciar span
            this.log.info("entra!");

            Span span = this.tracer.buildSpan("esb-span").withTag("component","dropwizard").start();
            span.finish();
        }

        return true;
    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        return true;
    }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) {
        return true;
    }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        TreeMap headers_map = this.getHeaders(messageContext);

        if(headers_map != null) {
            //TODO: iniciar span
            this.log.info("sale!");
            /*Span span = tracer.activeSpan();
            span.finish();*/
        }

        return true;
    }

    private TreeMap getHeaders(MessageContext messageContext) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        Object headers = axis2MessageContext.getProperty("TRANSPORT_HEADERS");
        if( headers instanceof TreeMap ) {
            return ((TreeMap) headers);
        }else{
            return null;
        }
    }

}
