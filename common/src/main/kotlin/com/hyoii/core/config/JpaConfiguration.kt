package com.hyoii.core.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan("com.hyoii.core.domain")
@EnableJpaRepositories(
    basePackages = ["com.hyoii.core.domain"]
)
class JpaConfiguration
