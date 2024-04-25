package com.example.hello.pulsar

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Factory
@Singleton
class MicronautPulsarFactory(
	@Value("\${pulsar.client.url:`pulsar://localhost:6650`}") val clientUrl: String = "pulsar://localhost:6650",
	@Value("\${pulsar.admin.url:`http://localhost:8080`}") val adminUrl: String = "http://localhost:8080",
	@Value("\${pulsar.tenant:public}") val tenant: String = "public",
	@Value("\${pulsar.namespace:dev}") val namespace: String = "dev"
) {
	val config = PulsarConfig(clientUrl, adminUrl, tenant, namespace)

	@Singleton
	fun factory(): IPulsarFactory = PulsarFactory(config)
}
