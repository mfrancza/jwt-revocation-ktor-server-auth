import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version = "2.2.2"

plugins {
    kotlin("jvm") version "1.8.0"
    id("maven-publish")
}

group = "com.mfrancza"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.mfrancza:jwt-revocation-rules:1.0.0")
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

publishing {
    publications {
        create<MavenPublication>("jwt-revocation-ktor-server-auth") {
            from(components["java"])
        }
    }
}