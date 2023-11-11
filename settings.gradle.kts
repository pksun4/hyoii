pluginManagement {
    val springBootVersion: String by settings
    val dependencyManagementVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version dependencyManagementVersion apply false
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    }
}

rootProject.name = "hyoii"
include("common")
include("mall")
