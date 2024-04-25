package com.example.hello.pulsar.conditions

import org.springframework.context.annotation.Bean
import org.springframework.pulsar.core.PulsarTopic
import org.springframework.stereotype.Component

@Component
class ConditionTopics() {
	@Bean
	fun conditionTopic(): PulsarTopic {
		// This will create a non-partitioned topic in the public/default namespace
		//return PulsarTopic.builder("simple-topic").build();
		return PulsarTopic.builder("persistent://public/${Condition.NAMESPACE}/${Condition.INPUT_TOPIC}")
			.numberOfPartitions(1)
			.build()
	}

	@Bean
	fun highTemperatureTopic(): PulsarTopic {
		return PulsarTopic.builder("persistent://public/${Condition.NAMESPACE}/${Condition.OUTPUT_TOPIC}")
			.numberOfPartitions(1)
			.build()
	}
}
