package com.example.hello.pulsar.functions

import com.example.hello.pulsar.PulsarRunnable
import com.example.hello.pulsar.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.functions.api.Context
import org.slf4j.LoggerFactory

interface FunctionRunnable : PulsarRunnable

class FunctionRunner<I, O>(
	private val source: Consumer<I>,
	private val func: org.apache.pulsar.functions.api.Function<I, O>,
	private val context: Context,
	private val sink: Producer<O>
) : PulsarRunner(), FunctionRunnable {
	override fun callOnce() {
		val messages = source.batchReceive()
		if (messages.size() == 0) return
		log.debug("Received batch (size={})", messages.size())
		var processed = 0
		messages.forEach { message ->
			try {
				val input = message.value
				val output = func.process(input, context)
				sink.send(output)
				source.acknowledge(message)
				processed++
			} catch (e: Exception) {
				log.warn("Could not process message", e)
				source.negativeAcknowledge(message)
			}
		}
		source.acknowledge(messages)
		log.debug("Processed batch (size={}, ack={})", messages.size(), processed)
	}

	override fun close() {
		source.close()
		sink.close()
	}

	companion object {
		private val log = LoggerFactory.getLogger(FunctionRunner::class.java)
	}
}
