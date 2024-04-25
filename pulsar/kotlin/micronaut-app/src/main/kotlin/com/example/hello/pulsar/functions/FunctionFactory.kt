package com.example.hello.pulsar.functions

import com.example.hello.pulsar.Condition
import com.example.hello.pulsar.IPulsarFactory
import com.example.hello.pulsar.PulsarFactorExtensions.shared
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.apache.pulsar.client.impl.schema.AvroSchema

@Factory
class FunctionFactory(
	@Inject private val factory: IPulsarFactory,
	@Value("\${pulsar.function.name}") val functionName: String,
	@Value("\${pulsar.function.input}") val inputTopic: String,
	@Value("\${pulsar.function.output}") val outputTopic: String
) {

	@Singleton
	fun function(): FunctionRunnable {
		val func = FilterTemperatureFunction()

		val source = factory.consumerOf(inputTopic, AvroSchema.of(Condition::class.java))
			.shared(functionName)
			.consumerName(functionName)
			.subscribe()

		val outputTopic = "${factory.config.tenant}/${factory.config.namespace}/$outputTopic"
		val sink = factory.producerOf(outputTopic, AvroSchema.of(Float::class.java))
			.create()

		val context = FunctionContext(
			factory.config.tenant, factory.config.namespace,
			functionName, inputTopic, outputTopic
		)
		return FunctionRunner(source, func, context, sink)
	}
}
