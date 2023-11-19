import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    base
    java
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1" apply false

    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    kotlin("plugin.jpa") version "1.9.0"
    kotlin("plugin.allopen") version "1.9.0"
    kotlin("plugin.noarg") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    kotlin("kapt") version "1.9.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val springBootVersion: String by project
val springDataCommons: String by project
val queryDsl: String by project
val jacksonVersion: String by project
val jupiterVersion: String by project
val kotlinCoroutinesVersion: String by project

allprojects {
    group = "com.hyoii"
    version = "1.0.0"

    apply {
        plugin("project-report")
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
//    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("kotlin-spring")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
        implementation("org.springframework.boot:spring-boot-starter:$springBootVersion") {
            implementation("org.yaml:snakeyaml:2.0")
        }
        implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")

        // jackson
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
        implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:$jacksonVersion")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

        // Reactive
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutinesVersion")

        // XSS Filter
        implementation("org.jsoup:jsoup:1.16.1")
        implementation("javax.servlet:javax.servlet-api:4.0.1")

        // Apache Commons
        implementation("org.apache.commons:commons-lang3:3.13.0")
        implementation("commons-validator:commons-validator:1.7")

        // Arrow Function
        implementation("io.arrow-kt:arrow-core:1.2.1")

        // Test libraries
        testImplementation("org.junit.platform:junit-platform-launcher:1.9.3")
        testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
        testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion") {
            exclude(module = "mockito-core")
            exclude(module = "junit")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion") {
            exclude(module = "junit-platform-commons")
        }
        testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
        testImplementation("io.projectreactor:reactor-test:3.5.10")
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
        testImplementation("io.mockk:mockk:1.13.8")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion")

        // Javax Annotation (일부 외부 라이브러리에서 javax 사용중이라 gradle import)
        implementation("javax.annotation:javax.annotation-api:1.3.2")

        // Javax -> Jakarta
        implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")


        // Hibernate Validator
        implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
        implementation("org.glassfish:jakarta.el:5.0.0-M1")

        // Security
//        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("io.jsonwebtoken:jjwt-api:0.11.5")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    }

    kotlin.sourceSets.main {
        setBuildDir("$buildDir")
    }

    tasks.withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.set(listOf("-Xjsr305=strict"))
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    sourceSets {
        main {
            java.srcDir("src/main/kotlin")
        }
        test {
            java.srcDir("src/test/kotlin")
        }
    }
}

project("common") {
    apply {
        plugin("kotlin-allopen")
        plugin("kotlin-noarg")
        plugin("kotlin-kapt")
    }

    noArg {
        annotation("jakarta.persistence.Entity")
    }

    allOpen {
        annotation("jakarta.persistence.Entity")
        annotation("jakarta.persistence.MappedSuperclass")
        annotation("jakarta.persistence.Embeddable")
    }

    dependencies {
        implementation("au.com.console:kassava:2.1.0")
        testImplementation("org.mockito:mockito-inline:5.2.0")

        implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")

        compileOnly("org.springframework.boot:spring-boot-starter-web:$springBootVersion") {
            exclude(module = "commons-logging")
        }
        implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")

        // kotlin jpa specification (JPA & QueryDSL )
        compileOnly("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
        compileOnly("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
        implementation("org.springframework.data:spring-data-commons:$springDataCommons")
        implementation("com.querydsl:querydsl-jpa:$queryDsl:jakarta")
        implementation("com.querydsl:querydsl-apt:$queryDsl:jakarta")

        kapt("jakarta.persistence:jakarta.persistence-api")
        kapt("jakarta.annotation:jakarta.annotation-api")

        // Hikari
        implementation("com.zaxxer:HikariCP:5.0.1")
        // MySql
        runtimeOnly("com.mysql:mysql-connector-j")
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
        baseName = "core"
    }
}

project(":mall") {
    apply {
        plugin("kotlin-kapt")
    }

    dependencies {
        implementation(project(":common"))
        implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
        // kotlin jpa specification (JPA & QueryDSL )
        implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
        implementation("org.springframework.data:spring-data-commons:$springDataCommons")
        implementation("com.querydsl:querydsl-jpa:$queryDsl:jakarta")
        implementation("com.querydsl:querydsl-apt:$queryDsl:jakarta")
        kapt("jakarta.persistence:jakarta.persistence-api")
        kapt("jakarta.annotation:jakarta.annotation-api")
    }

    val moduleMainClass = "com.hyoii.mall.MallApplicationKt"
    tasks.getByName<BootJar>("bootJar") {
        enabled = true
        mainClass.set(moduleMainClass)
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}
