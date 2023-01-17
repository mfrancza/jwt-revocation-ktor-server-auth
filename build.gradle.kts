import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version = "2.2.2"

plugins {
    kotlin("jvm") version "1.8.0"
    id("maven-publish")
}

group = "com.mfrancza"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.mfrancza:jwt-revocation-ruleset-client:1.0-SNAPSHOT")
    implementation("com.mfrancza:jwt-revocation-rules:1.0-SNAPSHOT")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}