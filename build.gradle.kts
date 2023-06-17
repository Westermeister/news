import io.freefair.gradle.plugins.sass.SassExtension
import sass.embedded_protocol.EmbeddedSass.OutputStyle

plugins {
    java
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.freefair.sass-base") version "8.0.1"
    id("io.freefair.sass-webjars") version "8.0.1"
    id("io.freefair.sass-java") version "8.0.1"
}

group = "com.westermeister"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.playwright:playwright:1.35.0")
    implementation("org.webjars:bootstrap:5.3.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.73")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.session:spring-session-jdbc")
    implementation("org.liquibase:liquibase-core")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

configure<SassExtension> {
    omitSourceMapUrl.set(false)
    outputStyle.set(OutputStyle.COMPRESSED)
    sourceMapContents.set(false)
    sourceMapEmbed.set(false)
    sourceMapEnabled.set(true)
}
