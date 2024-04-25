package com.example.hello.pulsar

import org.slf4j.LoggerFactory

interface PulsarRunnable: Runnable {}

abstract class PulsarRunner : AutoCloseable, PulsarRunnable {
    override fun run() {
		while (true) {
			try {
				callOnce()
			}
			catch (e: InterruptedException) {
				log.info("Stop signal received, exiting")
				break
			} catch (e: Exception) {
				log.warn("Could not process", e)
			}
		}
    }

    protected abstract fun callOnce()

    companion object {
        private val log = LoggerFactory.getLogger(PulsarRunner::class.java)
    }
}
