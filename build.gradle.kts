plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.gorylenko.gradle-git-properties")
//    id("com.github.spotbugs")
}

val appGroup: String by project
val appVersion: String by project

group = appGroup
version = appVersion

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    configureEach {
        exclude(group = "commons-logging", module = "commons-logging")
    }
}

repositories {
    mavenCentral()
}

springBoot {
    buildInfo()
}

val springCloud: String by project

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloud")
    }
}

val jjwt: String by project
val oci: String by project
val queryDSL: String by project
val pdfbox: String by project
val redisson: String by project
val scrimage: String by project
val springdoc: String by project
val tsid: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdoc") {
        exclude(group = "org.webjars", module = "swagger-ui")
    }
    implementation("org.redisson:redisson-spring-boot-starter:$redisson")
    implementation("com.github.f4b6a3:tsid-creator:$tsid")
    implementation("io.jsonwebtoken:jjwt-api:$jjwt")
    implementation("io.github.openfeign.querydsl:querydsl-core:$queryDSL")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:$queryDSL")
    implementation("org.apache.pdfbox:pdfbox:$pdfbox")
    implementation("com.sksamuel.scrimage:scrimage-core:$scrimage")
    implementation("com.sksamuel.scrimage:scrimage-webp:$scrimage")
    implementation("com.oracle.oci.sdk:oci-java-sdk-common:$oci")
    implementation("com.oracle.oci.sdk:oci-java-sdk-objectstorage:$oci")
    implementation("com.oracle.oci.sdk:oci-java-sdk-common-httpclient-jersey3:$oci")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwt")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwt")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.github.openfeign.querydsl:querydsl-apt:$queryDSL:jpa")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
