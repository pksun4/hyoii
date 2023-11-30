package com.hyoii.mall.api.member

import jakarta.validation.constraints.NotBlank

class AuthTokenDto(
    @field:NotBlank
    val grantType: String,
    @field:NotBlank
    val accessToken: String,
    @field:NotBlank
    val refreshToken: String
)
