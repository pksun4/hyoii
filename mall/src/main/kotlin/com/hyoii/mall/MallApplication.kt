package com.hyoii.mall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan("com.hyoii.core.domain")
@EnableJpaRepositories(
    basePackages = ["com.hyoii.core.domain"]
)
class MallApplication

fun main(args: Array<String>) {
    runApplication<MallApplication>(*args)
}
