package com.example.hello.pulsar.producers

import com.example.hello.pulsar.Condition
import com.example.hello.pulsar.IPulsarFactory
import com.example.hello.pulsar.PulsarFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.apache.pulsar.client.impl.schema.AvroSchema
import java.time.Duration

@Factory
@Singleton
class ProducerFactory(
	@Inject private val factory: IPulsarFactory,
	@Value("\${pulsar.producer.name}") val name: String,
	@Value("\${pulsar.producer.topic}") val topic: String,
	// Must be a java.time.Duration so it can be injected
	@Value("\${pulsar.producer.interval}") val interval: Duration = Duration.ZERO,
	@Value("\${pulsar.producer.batch}") val batchSize: Int = 100,
) {

	private val schema = AvroSchema.of(Condition::class.java)

	@Singleton
	fun producer(): ProducerRunnable {
		val producer = factory.producerOf(topic, schema)
			.producerName(name)
			.create()
		return ProducerRunner(producer) {
			if (interval.toMillis() > 0) Thread.sleep(interval.toMillis())
			Condition.random()
		}
	}

	@Singleton
	fun batchProducer(): BatchProducerRunnable {
		val producer = factory.producerOf(topic, schema)
			.producerName(name)
			.batchingMaxMessages(batchSize)
			.create()
		return BatchProducerRunner(producer) {
			if (interval.toMillis() > 0) Thread.sleep(interval.toMillis())
			(0..batchSize).map { Condition.random() }
		}
	}
}
