```
http://18.208.188.128:8082/api/health
http://18.208.188.128:8082/api/customers/CUST-1001
http://18.208.188.128:8082/api/orders/ORD-5001
http://18.208.188.128:8082/api/orders?status=COMPLETED&limit=20
```
```bash
Heavy cpu intensive call
http://18.208.188.128:8082/api/reports/daily-sales?businessDate=2025-12-01
```
```yaml
version:0.0.1

java -javaagent:/home/ubuntu/dd-java-agent.jar \
  -Ddd.profiling.enabled=true \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -Ddd.logs.injection=true \
  -Ddd.trace.sample.rate=1  \
  -Ddd.trace.health.metrics.enabled=true \
  -Ddd.tags=app-name:order-service,team:sre \
  -Ddd.service=order-service \
  -Ddd.env=dev \
  -Ddd.version=0.0.1 \
  -Ddd.profiling.enabled=true \
  -Ddd.profiling.heap.enabled=true \
  -Ddd.profiling.ddprof.wall.enabled=true \
  -Ddd.profiling.ddprof.enabled=true \
  -Ddd.profiling.ddprof.alloc.enabled=true \
  -Ddd.profiling.ddprof.cpu.enabled=true \
  -Ddd.profiling.ddprof.liveheap.enabled=true \
  -Ddd.dynamic.instrumentation.enabled=true \
  -Ddd.code.origin.for.spans.enabled=true \
  -Ddd.data-streams.enabled=true \
  -Ddd.trace.remove.integration-service-names.enabled=true \
  -Ddd.jmxfetch.enabled=true \
  -jar orderservice-0.0.1-SNAPSHOT.jar


version:0.0.2

java -javaagent:/home/ubuntu/dd-java-agent.jar \
  -Ddd.profiling.enabled=true \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -Ddd.logs.injection=true \
  -Ddd.trace.sample.rate=1  \
  -Ddd.trace.health.metrics.enabled=true \
  -Ddd.tags=app-name:order-service,team:sre \
  -Ddd.service=order-service \
  -Ddd.env=dev \
  -Ddd.version=0.0.2 \
  -Ddd.profiling.enabled=true \
  -Ddd.profiling.heap.enabled=true \
  -Ddd.profiling.ddprof.wall.enabled=true \
  -Ddd.profiling.ddprof.enabled=true \
  -Ddd.profiling.ddprof.alloc.enabled=true \
  -Ddd.profiling.ddprof.cpu.enabled=true \
  -Ddd.profiling.ddprof.liveheap.enabled=true \
  -Ddd.dynamic.instrumentation.enabled=true \
  -Ddd.code.origin.for.spans.enabled=true \
  -Ddd.data-streams.enabled=true \
  -Ddd.trace.remove.integration-service-names.enabled=true \
  -Ddd.jmxfetch.enabled=true \
  -jar orderservice-0.0.1-SNAPSHOT.jar

```
