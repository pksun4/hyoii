package com.hyoii.config

import com.hyoii.common.security.SecurityUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import java.util.*

@Configuration
class JpaAuditConfig {
    @Bean
    fun auditorAware(): AuditorAware<Long> = AuditorAware { Optional.of(SecurityUtil.getCurrentUser()!!.memberIdx) }

}
