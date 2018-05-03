package com.serrodcal.esb.opentracing.handler;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMapExtractAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class OpenTracingZipkinHandler extends AbstractSynapseHandler {

    private static final Log log = LogFactory.getLog("ZIPKIN_LOGGER");

    private final Config config;

    private final Tracing tracing;
    private final Tracer tracer;

    private static final String SPANKEY = "ACTIVE_SPAN";

    public OpenTracingZipkinHandler() {
        this.config = ConfigFactory.load();

        //"http://zipkin:9411/api/v2/spans"
        Optional<String> zipkinURL = Optional.of(this.config.getString("tracer.zipkin.url"));

        Sender sender = OkHttpSender.create(zipkinURL.orElse("http://localhost:9411/api/v2/spans"));
        Reporter spanReporter = AsyncReporter.create(sender);
        Tracing braveTracing = Tracing.newBuilder().localServiceName("Legacy Integration Gateway").spanReporter(spanReporter).build();
        this.tracing = braveTracing;
        this.tracer = BraveTracer.create(this.tracing);
    }

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {

        org.apache.axis2.context.MessageContext axis2MessageContext
                = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
        Object headers = axis2MessageContext.getProperty(
                org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        if (headers != null && headers instanceof Map) {
            Map headersMap = (Map) headers;

            SpanContext spanContext = this.tracer.extract(Format.Builtin.HTTP_HEADERS, new TextMapExtractAdapter(headersMap));

            String http_method = (String) axis2MessageContext.getProperty("HTTP_METHOD");
            String http_url = axis2MessageContext.getTo().getAddress();


            Span span = tracer.buildSpan("handleRequestInFlow")
                    .asChildOf(spanContext)
                    .withTag("component", "WSO2 ESB")
                    .withTag("http.method", http_method)
                    .withTag("http.url", http_url)
                    .withTag("span.kind", "middleware")
                    .start();

            span.log("handleRequestInFlow");

            this.tracer.inject(spanContext, Format.Builtin.HTTP_HEADERS, new HeadersBuilderCarrier(headersMap));

            messageContext.setProperty(SPANKEY, span);

        }

        return true;

    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) { return true; }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) { return true; }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {

        org.apache.axis2.context.MessageContext axis2MessageContext
                = ((Axis2MessageContext) messageContext).getAxis2MessageContext();
        Object headers = axis2MessageContext.getProperty(
                org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        if (headers != null && headers instanceof Map) {

            Span span = (Span) messageContext.getProperty(SPANKEY);

            span.log("handleResponseOutFlow");

            span.finish();

        }

        return true;

    }

    private class HeadersBuilderCarrier implements io.opentracing.propagation.TextMap{

        private Map headersMap;

        public HeadersBuilderCarrier(Map headersMap) {
            this.headersMap = headersMap;
        }

        @Override
        public Iterator<Map.Entry<String, String>> iterator() {
            throw new UnsupportedOperationException("Carrier is write-only");
        }

        @Override
        public void put(String key, String value) {
            if (this.headersMap.containsKey(key)) {
                this.headersMap.remove(key);
            }
            this.headersMap.put(key, value);
        }

    }

}
