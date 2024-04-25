package com.example.hello.pulsar.conditions

import kotlin.random.Random

data class Condition(var temperature: Float = 0f, var humidity: Float = 0f) {
	companion object {
		fun random() = Condition(Random.nextFloat() * 40, Random.nextFloat() * 100)

		const val NAMESPACE = "default" //"dev" // TODO: auto-create namespace?
		const val INPUT_TOPIC = "conditions"
        const val OUTPUT_TOPIC = "highTemperature"
        const val DLQ_TOPIC = "$INPUT_TOPIC-dlq"
        const val DLQ_POLICY = "conditionsDqlPolicy"
        const val SUBSCRIPTION_NAME = "my-spring-sub"
        const val DQL_SUBSCRIPTION = "$SUBSCRIPTION_NAME-dlq"
    }
}
