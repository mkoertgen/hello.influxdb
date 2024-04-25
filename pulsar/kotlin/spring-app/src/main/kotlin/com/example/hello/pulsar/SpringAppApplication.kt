package com.example.hello.pulsar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.pulsar.annotation.EnablePulsar
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnablePulsar
@SpringBootApplication
class SpringAppApplication

fun main(args: Array<String>) {
	runApplication<SpringAppApplication>(*args)
}
