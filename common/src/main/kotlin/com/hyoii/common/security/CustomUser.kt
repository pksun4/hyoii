package com.hyoii.common.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import java.io.Serial

class CustomUser(
    val memberKey: Long,
    userEmail: String,
    userPassword: String,
    authorities: Collection<GrantedAuthority>
) : User(userEmail, userPassword, authorities) {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -3673064265555887219L
    }
}
