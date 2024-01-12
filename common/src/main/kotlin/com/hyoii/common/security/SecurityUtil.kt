package com.hyoii.common.security

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object SecurityUtil {

    fun cryptPasswordEncoder() = BCryptPasswordEncoder()

    fun String.passwordEncode(): String = cryptPasswordEncoder().encode(this)

    fun getCurrentUser(): CurrentUser? {
        SecurityContextHolder.getContext().authentication?.let {
            if (it is AnonymousAuthenticationToken) {
                return null
            }

            val principal = it.principal as CustomUser
            return CurrentUser(
                principal.memberKey,
                principal.username,
                principal.authorities
            )
        }
        return null
    }

    fun isAuthenticated(): Boolean {
        SecurityContextHolder.getContext().authentication?.let {
            if (it is AnonymousAuthenticationToken) {
                return false
            }
            return it.isAuthenticated
        }
        return false
    }

}

data class CurrentUser(
    val memberKey: Long,
    val email: String,
    val role: Collection<GrantedAuthority>
)
