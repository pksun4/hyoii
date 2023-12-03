package com.hyoii.mall.config

import com.hyoii.config.DataSourceConfig
import com.hyoii.config.WebClientConfig
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    DataSourceConfig::class,
    WebClientConfig::class
)
class MallConfig
