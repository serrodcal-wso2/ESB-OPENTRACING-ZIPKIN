# WSO2 ESB Custom Publisher For Zipkin and OpenTracing

This repository contains the project for a proof of concept about ESB Customizing Statics Publishing implementation following OpenTracing.

## Building

* Clone this repository.
* Run `mvn clean install` in cloned directory.
* Copy `org.serrodcal.custom.publisher_1.0.jar` from target directory into dropins directory of ESB installation.

## Testing

There is another project in this repository to test this componentes, for that, go to [here](https://github.com/serrodcal/ESBAnalyticsOpenTracing).

## Configuring ESB to run custom observer

_Under construction_.

## Regarding OpenTracing

Enter OpenTracing: by offering consistent, expressive, vendor-neutral APIs for popular platforms, OpenTracing makes it easy for developers to add (or switch) tracing implementations with an O(1) configuration change. OpenTracing also offers a lingua franca for OSS instrumentation and platform-specific tracing helper libraries.

At the highest level, a trace tells the story of a transaction or workflow as it propagates through a (potentially distributed) system. In OpenTracing, a trace is a directed acyclic graph (DAG) of "spans": named, timed operations representing a contiguous segment of work in that trace.

![DAG example](http://opentracing.io/documentation/images/OTHT_1.png)

Each component in a distributed trace will contribute its own span or spans.

![](http://opentracing.io/documentation/images/OTOV_2.png)

Tracing a workflow or transaction through a distributed system often looks something like the above. While this type of visualization can be useful to see how various components fit together, it does not convey any time durations, does not scale well, and is cumbersome when parallelism is involved. Another limitation is that there is no way to easily show latency or other aspects of timing. A more useful way to visualize even a basic trace often looks like this:

![](http://opentracing.io/documentation/images/OTOV_3.png)

Using HTTP, each component propagates four headers as given below:

```
X-B3-TraceId
X-B3-ParentSpanId
X-B3-SpanId
X-B3-Sampled
```
