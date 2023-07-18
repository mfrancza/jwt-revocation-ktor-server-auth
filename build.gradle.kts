import java.net.URI

val ktor_version = "2.3.2"

plugins {
    kotlin("jvm") version "1.9.0"
    id("maven-publish")
}

group = "com.mfrancza"
version = "1.1.0-SNAPSHOT"

repositories {
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.github.com/mfrancza/jwt-revocation-rules")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
    mavenCentral()
}

dependencies {
    implementation("com.mfrancza:jwt-revocation-rules:1.1.0-SNAPSHOT")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("jwt-revocation-ktor-server-auth") {
            from(components["java"])
        }
    }
    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = URI("https://maven.pkg.github.com/mfrancza/jwt-revocation-ktor-server-auth")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}