import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    extra.apply {
        // #junit5 #Mockk
        set("mockKVersion", "1.9.3")
        set("springMockKVersion", "1.1.2")
    }
}

val mockKVersion: String by extra
val springMockKVersion: String by extra

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.1")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2:2.0.202")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    testImplementation("com.rabbitmq:amqp-client:5.14.2")
    testImplementation("org.springframework.amqp:spring-rabbit-test:2.4.3")
    testImplementation("org.springframework.amqp:spring-rabbit-junit:2.4.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("com.ninja-squad:springmockk:${springMockKVersion}")
    testImplementation("io.mockk:mockk:${mockKVersion}")

//    test container
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("org.testcontainers:testcontainers:1.17.5")
    testImplementation("org.testcontainers:rabbitmq:1.17.5")
    testImplementation("org.testcontainers:mysql:1.17.5")
//    testImplementation ("org.testcontainers.containers.MySQLContainer:mysql:5.5")
//    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:junit-jupiter:1.17.5")

    implementation("org.postgresql:postgresql:42.4.1")
    implementation("org.flywaydb:flyway-core:8.4.4")
    implementation("io.github.microutils:kotlin-logging:1.7.6")
    implementation("org.springframework.boot:spring-boot-starter-web-services")

    implementation("com.github.tomakehurst:wiremock-jre8:2.33.2")
    implementation("org.springframework.cloud:spring-cloud-contract-wiremock:1.2.4.RELEASE")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
