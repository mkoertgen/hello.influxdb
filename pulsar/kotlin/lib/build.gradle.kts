plugins {
	kotlin("jvm")
	id("java-library")
}

repositories {
	mavenCentral()
}

java {
	sourceCompatibility = JavaVersion.toVersion("17")
}

dependencies {
	implementation(platform(project(":dependencies")))
	implementation("org.apache.pulsar:pulsar-client")
	implementation("org.apache.pulsar:pulsar-client-admin")
	implementation("org.apache.pulsar:pulsar-functions-api")

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core")
	testImplementation("io.mockk:mockk")

	testImplementation("org.testcontainers:testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:pulsar")
}

tasks.test {
	useJUnitPlatform()
}
