package com.example.hello.pulsar

import com.example.hello.pulsar.PulsarFactorExtensions.exclusive
import org.apache.pulsar.client.impl.schema.AvroSchema
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random

data class Condition(var temperature: Float = 0f, var humidity: Float = 0f) {

	companion object {
		fun random() = Condition(Random.nextFloat() * 40, Random.nextFloat() * 100)
	}
}

class ProducerConsumerTest : PulsarTest() {
	private val stage = "dev"
	private val topic = "conditions"

	@Test
	fun shouldProduceAndConsume() {
		container.factory(namespace = stage).use { factory ->
			log.info("Connected to pulsar: {}/{}", factory.config.tenant, factory.config.namespace)

			factory.ensureTopic(topic)

			val expected = Condition.random()

			val schema = AvroSchema.of(Condition::class.java)
			val subscription = this.subscription()
			val consumer = factory.consumerOf(topic, schema)
				.exclusive(subscription)
				.subscribe()

			val id = factory.producerOf(topic, schema).create().use { it.send(expected) }

			consumer.use {
				val msg = it.receive()
				assertThat(msg.messageId).isEqualTo(id)
				val actual = msg.value
				assertThat(actual).isEqualTo(expected)
			}
		}
	}
}
