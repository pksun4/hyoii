package com.hyoii.mall.config

import com.hyoii.core.config.JpaConfig
import com.hyoii.core.config.QueryDslConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    JpaConfig::class,
    QueryDslConfig::class
)
class MallConfig
