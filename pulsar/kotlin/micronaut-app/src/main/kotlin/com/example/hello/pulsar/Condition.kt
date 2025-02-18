package com.example.hello.pulsar

import kotlin.random.Random

data class Condition(var temperature: Float = 0f, var humidity: Float = 0f) {
	companion object {
		fun random() = Condition(Random.nextFloat() * 40, Random.nextFloat() * 100)
	}
}
