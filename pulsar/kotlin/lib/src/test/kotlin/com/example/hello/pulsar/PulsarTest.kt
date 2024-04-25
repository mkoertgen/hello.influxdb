package com.example.hello.pulsar

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.PulsarContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.*

@Testcontainers
abstract class PulsarTest {
	fun subscription() = "${PulsarTest::class.java.name}_${UUID.randomUUID()}"

	companion object {
		val log: Logger = LoggerFactory.getLogger(PulsarTest::class.java)

		private const val PULSAR_VERSION = "3.2.2"

		@Container
		val container: PulsarContainer = PulsarContainer(DockerImageName.parse("apachepulsar/pulsar:$PULSAR_VERSION"))

		fun PulsarContainer.factory(tenant: String = "public", namespace: String = "dev") = PulsarFactory(
			PulsarConfig(this.pulsarBrokerUrl, this.httpServiceUrl, tenant, namespace)
		)
	}
}
