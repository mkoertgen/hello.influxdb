package com.example.hello.pulsar

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Status
import io.micronaut.http.exceptions.HttpStatusException
import org.slf4j.LoggerFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

abstract class ServiceController<T : PulsarRunnable>(
  private val executor: ExecutorService,
  private val service: T
) {
	private var job : Future<*>? = null
	private val isRunning: Boolean get() = job != null

    @Get("/status", produces=["text/plain"])
    fun status(): String {
        return if (isRunning) "Running" else "Not running"
    }

    @Status(HttpStatus.ACCEPTED)
    @Get("/start", produces=["text/plain"])
    fun start(): String {
		if (isRunning) throw HttpStatusException(HttpStatus.PRECONDITION_FAILED, "Already running")
        job = executor.submit(service)
        log.info("Started service: {}", service)
        return "Started"
    }

    @Status(HttpStatus.ACCEPTED)
    @Get("/stop", produces=["text/plain"])
    fun stop(): String {
        if (!isRunning) throw HttpStatusException(HttpStatus.PRECONDITION_REQUIRED, "Not running")
		job!!.cancel(true)
        log.info("Stopped service: {}", service)
		job = null
        return "Stopped"
    }

  companion object {
    private val log = LoggerFactory.getLogger(ServiceController::class.java)
  }
}
