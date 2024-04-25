package com.example.hello.pulsar.consumers

import com.example.hello.pulsar.PulsarRunnable
import com.example.hello.pulsar.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.slf4j.LoggerFactory

interface BatchConsumerRunnable : PulsarRunnable

class BatchConsumerRunner<T>(
  private val consumer: Consumer<T>,
  private val sink: (List<Message<T>>) -> Unit
) : PulsarRunner(), BatchConsumerRunnable {

    override fun callOnce() {
        val messages = consumer.batchReceive()
        if (messages.size() == 0) return
        log.debug("Received batch (size={})", messages.size())
        sink.invoke(messages.toList())
        consumer.acknowledge(messages)
        log.debug("Processed batch (size={})", messages.size())
    }

    override fun close() {
        consumer.close()
    }

  companion object {
    private val log = LoggerFactory.getLogger(BatchConsumerRunner::class.java)
  }
}
