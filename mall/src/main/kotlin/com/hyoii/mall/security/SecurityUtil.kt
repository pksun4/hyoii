package com.hyoii.mall.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtil {
    companion object {
        fun getCurrentUser(): CurrentUser {
            val authentication: Authentication = SecurityContextHolder.getContext().authentication
            authentication.let {
                val principal = authentication.principal as CustomUser
                return CurrentUser(
                    principal.memberId,
                    principal.username,
                    principal.authorities
                )
            }
        }
    }

    data class CurrentUser(
        val memberIdx: Long,
        val email: String,
        val role: Collection<GrantedAuthority>
    )
}
