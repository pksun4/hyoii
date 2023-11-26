package com.hyoii.mall.security

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
    // todo refresh token
)
