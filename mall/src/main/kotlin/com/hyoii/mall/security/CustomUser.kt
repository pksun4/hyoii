package com.hyoii.mall.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val userIdx: Long,
    userEmail: String,
    userPassword: String,
    authorities: Collection<GrantedAuthority>
) : User(userEmail, userPassword, authorities)
