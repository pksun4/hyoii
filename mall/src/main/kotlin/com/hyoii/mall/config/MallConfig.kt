package com.hyoii.mall.config

import com.hyoii.config.DataSourceConfig
import com.hyoii.config.WebClientConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
//@Import(
//    DataSourceConfig::class,
//    WebClientConfig::class
//)
@ComponentScan(basePackages = ["com.hyoii"])
class MallConfig
