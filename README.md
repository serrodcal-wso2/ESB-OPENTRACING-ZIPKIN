# WSO2 ESB Custom Publisher For Zipkin and OpenTracing

This project consist of a custom synapse handler for intrumenting on WSO2 ESB and integrate it with Zipkin following OpenTracing.

## Getting Started

### Prerequisites

Clone this project in local: `~$ git clone https://github.com/serrodcal/ESB-OPENTRACING-ZIPKIN.git`.

Please, compile and build all componentes (esb, formatter, publisher and hello subfolders) using `~$ mvn clean install`.

## Running and testing

In root directory, run `~$ docker-compose up`.

Then, send requests like this: `~$ curl -i -X GET 'http://localhost:8280/hello?helloTo=Sergio&greeting=Hola'`. Response will contains `hello published` as body result.

### Regarding this demo

An overview is the following image:

![](/img/flow.png)

Access to zipkin page to check spans, for this go to `http://localhost:9411`:

![](/img/image.png)

Access to WSO2 ESB web console, go to `https://localhost:9443`. 

For more info about componentes, please read `docker-compose.yml` file.

### Regarding OpenTracing

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
