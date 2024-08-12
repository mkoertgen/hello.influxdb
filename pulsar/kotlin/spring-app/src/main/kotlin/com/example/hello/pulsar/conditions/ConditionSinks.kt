package com.example.hello.pulsar.conditions

import org.apache.pulsar.common.io.SinkConfig
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.pulsar.function.PulsarSink
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "spring.pulsar.sinks", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class ConditionSinks {
	@Bean
	fun conditionSink(): PulsarSink {
		// https://pulsar.apache.org/docs/3.2.x/io-elasticsearch-sink/#configuration
		val configs = mapOf(
			"elasticSearchUrl" to "https://localhost:9200",
			"indexName" to "conditions",
			"schemaEnable" to true,
			"createIndexIfNeeded" to true,
			"bulkEnabled" to true,
			"bulkActions" to 5,
			//
			"username" to "elastic",
			"password" to "*mKL69RuyynqHiXf0thA", // "changeme",
			"ssl" to mapOf(
				"enabled" to true, //false,
				"hostnameVerification" to false,
				"disableCertificateValidation" to true,
			)
		)
		// https://pulsar.apache.org/download/
		val sinkConfig = SinkConfig.builder()
			.tenant("public")
			.namespace(Condition.NAMESPACE)
			.name("conditionSink")
			.archive("builtin://elastic_search")
			.inputs(listOf(Condition.INPUT_TOPIC))
			.configs(configs)
			.build()
		return PulsarSink(sinkConfig, null)
	}
}
