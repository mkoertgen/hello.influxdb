package com.example.hello.pulsar.consumers

import com.example.hello.pulsar.Condition
import com.example.hello.pulsar.IPulsarFactory
import com.example.hello.pulsar.PulsarFactorExtensions.shared
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.apache.pulsar.client.api.Message
import org.apache.pulsar.client.impl.schema.AvroSchema
import org.slf4j.LoggerFactory

@Factory
@Singleton
class ConsumerFactory(
	@Inject private val factory: IPulsarFactory,
	@Value("\${pulsar.consumer.name}") val name: String,
	@Value("\${pulsar.consumer.topic}") val topic: String
) {
	private val schema = AvroSchema.of(Condition::class.java)

	@Singleton
	fun consumer(): ConsumerRunnable {
		val consumer = factory.consumerOf<Condition>(topic, schema)
			.shared(name).consumerName(name)
			.subscribe()
		return ConsumerRunner(consumer, this::sink)
	}

	@Singleton
	fun batchConsumer(): BatchConsumerRunnable {
		val consumer = factory.consumerOf<Condition>(topic, schema)
			.shared(name).consumerName(name)
			.subscribe()
		return BatchConsumerRunner(consumer, this::batchSink)
	}

	private fun sink(msg: Message<Condition>) {
		log.debug("Received condition={}", msg.value)
	}

	private fun batchSink(msg: List<Message<Condition>>) {
		log.debug("Received conditions batch (size={})", msg.size)
	}

	companion object {
		private val log = LoggerFactory.getLogger(ConsumerFactory::class.java)
	}
}
