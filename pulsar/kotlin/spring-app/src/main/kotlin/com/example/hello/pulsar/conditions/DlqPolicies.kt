package com.example.hello.pulsar.conditions

import org.apache.pulsar.client.api.DeadLetterPolicy
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DlqPolicies {
	@Bean(name = [Condition.DLQ_POLICY])
	fun conditionsDqlPolicy(): DeadLetterPolicy = DeadLetterPolicy.builder()
		.maxRedeliverCount(10)
		.deadLetterTopic(Condition.DLQ_TOPIC)
		.build()
}
