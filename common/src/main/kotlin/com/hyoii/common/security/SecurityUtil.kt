package com.hyoii.common.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object SecurityUtil {

    fun cryptPasswordEncoder() = BCryptPasswordEncoder()

    fun String.passwordEncode(): String = cryptPasswordEncoder().encode(this)

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
