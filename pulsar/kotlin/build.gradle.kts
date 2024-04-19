plugins {
  kotlin("jvm") version "1.9.23"
  kotlin("kapt") version "1.9.23"
  kotlin("plugin.allopen") version "1.9.23"
  //--- Packaging pulsar functions https://pulsar.apache.org/docs/en/functions-package/#java
  id("com.github.johnrengelman.shadow") version "8.1.1"
  id("io.micronaut.application") version "4.3.6"
}

repositories {
  mavenCentral()
}

val pulsarVersion = "3.2.2"
val testContainersVersion = "1.19.7"

dependencies {
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

  implementation("org.apache.pulsar:pulsar-client:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-client-admin:${pulsarVersion}")
  implementation("org.apache.pulsar:pulsar-functions-api:${pulsarVersion}")

  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
  testImplementation("org.assertj:assertj-core:3.25.3")
  testImplementation("io.mockk:mockk:1.13.10")

  testImplementation("org.testcontainers:testcontainers:${testContainersVersion}")
  testImplementation("org.testcontainers:junit-jupiter:${testContainersVersion}")
  testImplementation("org.testcontainers:pulsar:${testContainersVersion}")
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
  useJUnitPlatform {
    val tags = listOf("integration")
    tags.forEach { tag ->
      if (!project.hasProperty(tag))
        excludeTags(tag)
    }
  }
  testLogging {
    events("passed", "skipped", "failed")
  }
}
