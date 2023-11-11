import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    base
    java
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.1"

    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.9.0"
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
    group = "company.app"
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
    }

    dependencies {
        implementation("au.com.console:kassava:2.1.0")
        testImplementation("org.mockito:mockito-inline:5.2.0")

        compileOnly("org.springframework.boot:spring-boot-starter-web:$springBootVersion") {
            exclude(module = "commons-logging")
        }
        compileOnly("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

project(":mall") {
    apply {
        plugin("kotlin-kapt")
    }

    dependencies {
        implementation(project(":common"))
    }

    val moduleMainClass = "com.hyoii.kotlin.MallApplication"
    tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
        enabled = true
        mainClass.set(moduleMainClass)
    }

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}
