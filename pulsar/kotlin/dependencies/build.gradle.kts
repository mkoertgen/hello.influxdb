plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    // most dependencies pinned to pulsar/pom to avoid compatibility-issues
    // - https://github.com/apache/pulsar/blob/v3.2.2/pom.xml
    val pulsarVersion = "3.2.2"

    constraints {
        api("org.apache.pulsar:pulsar-client:$pulsarVersion")
        api("org.apache.pulsar:pulsar-common:$pulsarVersion")
        api("org.apache.pulsar:pulsar-functions-api:$pulsarVersion")
		//api("org.apache.pulsar:pulsar-functions-local-runner:$pulsarVersion")
        api("org.apache.pulsar:pulsar-io-core:$pulsarVersion")
        api("org.apache.pulsar:pulsar-client-admin:$pulsarVersion")

		// kotlin, testing, ...
		api("org.junit.jupiter:junit-jupiter:5.10.2")
		api("org.assertj:assertj-core:3.25.3")
		api("io.mockk:mockk:1.13.10")
		api("org.testcontainers:junit-jupiter:1.17.2")
		api("org.testcontainers:testcontainers:1.17.2")
		api("org.testcontainers:pulsar:1.17.2")
	}
}
