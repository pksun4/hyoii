package com.hyoii.domain.member

import com.hyoii.annotation.ValidEnum
import com.hyoii.enums.GenderEnums
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class AuthTokenRequest(
    @field:NotBlank
    val grantType: String,
    @field:NotBlank
    val accessToken: String,
    @field:NotBlank
    val refreshToken: String
)

class SignUpRequest (
    @field:NotBlank(message = "이메일은 필수값입니다.")
    @field:Email
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수값입니다.")
    val password: String,

    @field:NotBlank(message = "이름은 필수값입니다.")
    val name: String,

    @field:NotBlank(message = "성별은 필수값입니다.")
    @field:ValidEnum(enumClass = GenderEnums::class, message = "M/F")
    val gender: String
)

/**
 * 로그인 요청 DTO
 */
class LoginRequest(
    @field:NotBlank(message = "이메일은 필수값입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수값입니다.")
    val password: String
)
