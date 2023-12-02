package com.hyoii.mall.config

import com.hyoii.config.JpaConfig
import com.hyoii.config.QueryDslConfig
import com.hyoii.config.WebClientConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    JpaConfig::class,
    QueryDslConfig::class,
    WebClientConfig::class
)
class MallConfig
