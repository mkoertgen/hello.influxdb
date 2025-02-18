plugins {
	kotlin("jvm")
	kotlin("kapt")
	kotlin("plugin.allopen")
	//--- Packaging pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("io.micronaut.application") version "4.3.6"
}

repositories {
	mavenCentral()
}

val testContainersVersion = "1.19.7"

dependencies {
	implementation(platform(project(":dependencies")))
	implementation(project(":lib"))

	implementation("org.apache.pulsar:pulsar-client")
	implementation("org.apache.pulsar:pulsar-client-admin")
	implementation("org.apache.pulsar:pulsar-functions-api")

	kapt("io.micronaut:micronaut-http-validation")
	kapt("io.micronaut.openapi:micronaut-openapi")

	annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
	implementation("io.micronaut.validation:micronaut-validation")

	implementation("io.micronaut:micronaut-http-client")
	implementation("io.micronaut:micronaut-jackson-databind")
	annotationProcessor("io.micronaut:micronaut-management")
	implementation("io.micronaut:micronaut-management")

	implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
	implementation("io.micronaut.micrometer:micronaut-micrometer-core")
	implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")
	implementation("io.micronaut.openapi:micronaut-openapi")
	implementation("io.swagger.core.v3:swagger-annotations")
	implementation("jakarta.annotation:jakarta.annotation-api")

	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	runtimeOnly("ch.qos.logback:logback-classic")

	runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core")
	testImplementation("io.mockk:mockk")

	testImplementation("org.testcontainers:testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:pulsar")
}

application {
	// Define the main class for the application.
	//mainClass.set("com.example.hello.pulsar.AppKt")
	mainClass.set("com.example.hello.pulsar.Application")
}

java {
	sourceCompatibility = JavaVersion.toVersion("17")
}


tasks {
	compileKotlin {
		kotlinOptions {
			jvmTarget = "17"
		}
	}
	compileTestKotlin {
		kotlinOptions {
			jvmTarget = "17"
		}
	}
}

graalvmNative.toolchainDetection.set(false)

micronaut {
	runtime("netty")
	testRuntime("junit5")
	processing {
		incremental(true)
		annotations("com.example.*")
	}
}

tasks.test {
	useJUnitPlatform()
}
