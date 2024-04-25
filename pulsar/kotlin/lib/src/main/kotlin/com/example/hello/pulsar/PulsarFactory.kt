package com.example.hello.pulsar

import org.apache.pulsar.client.admin.PulsarAdmin
import org.apache.pulsar.client.api.*
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit

data class PulsarConfig(
	val clientUrl: String = "pulsar://localhost:6650",
	val adminUrl: String = "http://localhost:8080",
	val tenant: String = "public",
	val namespace: String = "dev"
)

object PulsarFactorExtensions {
	fun <T> ConsumerBuilder<T>.batch(size: Int = 1000): ConsumerBuilder<T> =
		this.batchReceivePolicy(
			BatchReceivePolicy.builder()
				.maxNumMessages(size)
				.maxNumBytes(0) // disable
				.timeout(-1, TimeUnit.SECONDS) // disable, blocks!
				.build()
		)

	fun <T> ConsumerBuilder<T>.shared(subscription: String): ConsumerBuilder<T> =
		this.subscriptionName(subscription)
			.subscriptionMode(SubscriptionMode.Durable)
			.subscriptionType(SubscriptionType.Shared)
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)

	fun <T> ConsumerBuilder<T>.exclusive(subscription: String): ConsumerBuilder<T> =
		this.subscriptionName(subscription)
			.subscriptionMode(SubscriptionMode.NonDurable)
			.subscriptionType(SubscriptionType.Exclusive)
			.subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
}

interface IPulsarFactory : AutoCloseable {
	val config: PulsarConfig

	@Throws(PulsarClientException::class)
	fun client(): PulsarClient

	@Throws(PulsarClientException::class)
	fun admin(): PulsarAdmin

	fun <T> consumer(schema: Schema<T>): ConsumerBuilder<T>
	fun <T> producer(schema: Schema<T>): ProducerBuilder<T>
	fun ensureNamespace()
	fun ensureTopic(topic: String, partitions: Int = -1)

	fun<T> consumerOf(topic: String, schema: Schema<T>): ConsumerBuilder<T> = consumer(schema).topic(topic).also { ensureTopic(topic)}
	fun<T> producerOf(topic: String, schema: Schema<T>): ProducerBuilder<T> = producer(schema).topic(topic).also { ensureTopic(topic)}
	//fun <T> reader(schema: Schema<T>): ReaderBuilder<T> = client().newReader(schema)
}

class PulsarFactory(override val config: PulsarConfig = PulsarConfig()) : IPulsarFactory {
	private val log = LoggerFactory.getLogger(PulsarFactory::class.java)
	private var client: PulsarClient? = null
	private var admin: PulsarAdmin? = null

	@Throws(PulsarClientException::class)
	override fun client(): PulsarClient {
		if (client == null) client = PulsarClient.builder().serviceUrl(config.clientUrl).build()
		return client!!
	}

	@Throws(PulsarClientException::class)
	override fun admin(): PulsarAdmin {
		if (admin == null) admin = PulsarAdmin.builder().serviceHttpUrl(config.adminUrl).build()
		return admin!!
	}

	override fun <T> consumer(schema: Schema<T>): ConsumerBuilder<T> = client().newConsumer(schema)
	override fun <T> producer(schema: Schema<T>): ProducerBuilder<T> = client().newProducer(schema)

	override fun close() {
		client?.close()
		admin?.close()
	}

	override fun ensureNamespace() {
		val ns = this.admin().namespaces()
		if (ns.getNamespaces(config.tenant).find { it.endsWith(config.namespace) } != null) return
		val fqn = "${config.tenant}/${config.namespace}"
		ns.createNamespace(fqn)
		log.info("Created namespace $fqn")
	}

	override fun ensureTopic(topic: String, partitions: Int) {
		ensureNamespace()
		val t = admin().topics()
		val ns = "${config.tenant}/${config.namespace}"
		val fqn = "$ns/$topic"

		if (partitions > 0) {
			if (t.getPartitionedTopicList(ns).find { it.endsWith(topic) } != null) return
			t.createPartitionedTopic(fqn, partitions)
			log.info("Created topic $fqn with $partitions partitions")
		} else {
			if (t.getList(ns).find { it.endsWith(topic) } != null) return
			t.createNonPartitionedTopic(fqn)
			log.info("Created topic $fqn")
		}
	}
}
