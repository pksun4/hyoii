package com.hyoii.mall.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val memberId: Long,
    userEmail: String,
    userPassword: String,
    authorities: Collection<GrantedAuthority>
) : User(userEmail, userPassword, authorities)
