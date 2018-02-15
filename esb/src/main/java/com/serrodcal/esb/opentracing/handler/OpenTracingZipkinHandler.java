package com.serrodcal.esb.opentracing.handler;

import brave.Tracing;
import okhttp3.OkHttpClient;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.TreeMap;

public class OpenTracingZipkinHandler extends AbstractSynapseHandler {

    private final Tracing tracing;
    private final OkHttpClient client;

    public OpenTracingZipkinHandler() {
        Sender sender = OkHttpSender.create("http://zipkin:9411/api/v2/spans");
        Reporter spanReporter = AsyncReporter.create(sender);
        Tracing braveTracing = Tracing.newBuilder().localServiceName("esb").spanReporter(spanReporter).build();
        this.tracing = braveTracing;
        this.client = new OkHttpClient();
    }

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {
        TreeMap headers_map = this.getHeaders(messageContext);

        if(headers_map != null) {
            //TODO: iniciar span
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
