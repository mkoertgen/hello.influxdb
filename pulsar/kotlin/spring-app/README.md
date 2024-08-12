# Spring Pulsar App

Placeholder for documentation

## Local Development

```shell
# Start Kafka, (optional) Kafka-UI (Redpanda Console), elasticsearch and (optional) kibana
$ docker compose -f ../docker-compose.yml up -d pulsar pulsar-manager elasticsearch kibana
# Check running containers & ports
$ docker compose ps

# Start the service locally with profile "dev"
./gradlew bootRun --args='--spring.profiles.active=dev'
```

Next, check the following endpoints:

```shell
# Get the Spring Boot Build Info
$ curl "http://localhost:8081/info"

# Check Health
$ curl "http://localhost:8081/health"

# Check Metrics
$ curl "http://localhost:8081/prometheus"
```

### Kafka UI (Redpanda Console)

Booting the application locally should automatically create the necessary Kafka Topics.
You can view these topics in the [Kafka UI](http://localhost:8090/).

![Redpanda Console](../_docs/development/img/kafka-ui.png)

### elasticsearch / kibana

Placeholder
