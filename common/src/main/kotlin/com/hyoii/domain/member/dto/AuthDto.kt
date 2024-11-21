package com.hyoii.domain.member.dto

import com.hyoii.annotation.ValidEnum
import com.hyoii.common.security.SecurityUtil.passwordEncode
import com.hyoii.domain.member.Member
import com.hyoii.domain.member.RefreshToken
import com.hyoii.enums.GenderEnums
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

/**
 * 회원가입 요청
 */
class SignUpRequest(
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
) {
    fun toEntity() = Member(
        email = email,
        password = password.passwordEncode(),
        name = name,
        gender = GenderEnums.valueOf(gender)
    )
}

/**
 * 회원가입 응답
 */
class SignUpResponse(
    val id: Long,
    val email: String,
    val name: String,
    val gender: GenderEnums,
    val createdAt: LocalDateTime
) {
    companion object {
        operator fun invoke(member: Member) = SignUpResponse(
            id = member.id!!,
            email = member.email,
            name = member.name,
            gender = member.gender,
            createdAt = member.createdAt
        )
    }
}

/**
 * 로그인 요청
 */
class LoginRequest(
    @field:NotBlank(message = "이메일은 필수값입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수값입니다.")
    val password: String
)

/**
 * 토큰 재발급 요청
 */
class AuthTokenRequest(
    @field:NotBlank
    val accessToken: String,
    @field:NotBlank
    val refreshToken: String
) {
    fun toEntity(memberKey: Long) = RefreshToken(
        refreshToken = refreshToken,
        memberKey = memberKey
    )
}

/**
 * 토큰 응답
 */
class AuthTokenResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)

