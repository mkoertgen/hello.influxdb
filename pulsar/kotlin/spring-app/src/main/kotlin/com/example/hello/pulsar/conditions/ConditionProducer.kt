package com.example.hello.pulsar.conditions

import org.apache.pulsar.client.api.PulsarClientException
import org.slf4j.LoggerFactory
import org.springframework.pulsar.core.PulsarTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ConditionProducer(private val pulsarTemplate: PulsarTemplate<Condition>) {
	companion object {
		private val LOGGER = LoggerFactory.getLogger(ConditionProducer::class.java)
		const val TOPIC = "conditions"
	}

	@Scheduled(fixedRate = 1000)
	@Throws(PulsarClientException::class)
	fun send() {
		val condition = Condition.random()
		pulsarTemplate.send(TOPIC, condition)
		LOGGER.info("Sent {} to {}", condition, TOPIC)
	}
}
