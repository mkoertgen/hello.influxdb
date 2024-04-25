package com.example.hello.pulsar.conditions

import org.apache.pulsar.client.api.PulsarClientException
import org.apache.pulsar.client.api.SubscriptionType
import org.apache.pulsar.common.schema.SchemaType
import org.slf4j.LoggerFactory
import org.springframework.pulsar.annotation.PulsarListener
import org.springframework.pulsar.listener.AckMode
import org.springframework.pulsar.reactive.core.ReactivePulsarTemplate
import org.springframework.stereotype.Component


@Component
class FilterTemperatureFunction(
	private val pulsarTemplate: ReactivePulsarTemplate<Float>
) {
	private val highTemperature: Float = 40f

	// Docs:
	// - https://docs.spring.io/spring-pulsar/docs/current/reference/reference/pulsar.html
	// - https://www.baeldung.com/spring-boot-apache-pulsar
	// - https://docs.spring.io/spring-boot/docs/current/reference/html/messaging.html#messaging.pulsar
	// - https://docs.spring.io/spring-pulsar/reference/reference/pulsar-function.html
	// - https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.integration
	// - https://docs.spring.io/spring-cloud-stream/reference/pulsar/pulsar_binder.html
	companion object {
		private val LOGGER = LoggerFactory.getLogger(FilterTemperatureFunction::class.java)
	}

	@PulsarListener(
		topics = [Condition.INPUT_TOPIC],
		subscriptionName = Condition.SUBSCRIPTION_NAME,
		subscriptionType = [SubscriptionType.Shared],
		deadLetterPolicy = Condition.DLQ_POLICY,
//		schemaType = SchemaType.JSON, // .AVRO,
//		ackMode = AckMode.BATCH,
//		batch = true,
		//properties = ["ackTimeout=60s"]
	)
	@Throws(PulsarClientException::class)
	fun conditionListener(condition: Condition) {
		LOGGER.info("Received {} in {} with : {}", Condition::class.java.name, Condition.INPUT_TOPIC, condition.temperature)
		val currTemp = condition.temperature
		if (currTemp > highTemperature) {
			pulsarTemplate.send(Condition.OUTPUT_TOPIC, currTemp)
				.subscribe()
			LOGGER.info("Sent {} to {}", currTemp, Condition.OUTPUT_TOPIC)
		}
	}

	@PulsarListener(
		subscriptionName = Condition.DQL_SUBSCRIPTION,
		topics = [Condition.DLQ_TOPIC],
		subscriptionType = [SubscriptionType.Shared]
	)
	@Throws(PulsarClientException::class)
	fun dlqListener(condition: Condition) {
		LOGGER.info("Received {} in {} with : {}", Condition::class.java.name, Condition.DLQ_TOPIC, condition.temperature)
	}
}
