package com.example.hello.pulsar.consumers

import com.example.hello.pulsar.PulsarRunnable
import com.example.hello.pulsar.PulsarRunner
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Message
import org.slf4j.LoggerFactory

interface ConsumerRunnable : PulsarRunnable

class ConsumerRunner<T>(
  private val consumer: Consumer<T>,
  private val sink: (Message<T>) -> Unit
  ) : PulsarRunner(), ConsumerRunnable {

    override fun callOnce() {
        val message = consumer.receive()
        sink.invoke(message)
        consumer.acknowledge(message)
        log.debug("Received msg(time={} id={}), value={})", message.eventTime, message.messageId, message.value)
    }

    override fun close() {
        consumer.close()
    }

  companion object {
    private val log = LoggerFactory.getLogger(ConsumerRunner::class.java)
  }
}
