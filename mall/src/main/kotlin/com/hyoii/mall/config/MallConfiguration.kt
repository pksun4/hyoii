package com.hyoii.mall.config

import com.hyoii.core.config.JpaConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(JpaConfiguration::class)
class MallConfiguration
