package com.hyoii.mall.api.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.MemberLoginDto
import com.hyoii.enums.MessageEnums
import com.hyoii.mall.security.JwtTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(memberLoginDto: MemberLoginDto) =
        runCatching {
            val authenticationToken = UsernamePasswordAuthenticationToken(
                memberLoginDto.email,
                memberLoginDto.password
            )
            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
            ResponseToken(
                grantType = "Bearer",
                accessToken = jwtTokenProvider.createToken(authentication),
                refreshToken = jwtTokenProvider.createRefreshToken(authentication)
            ).right()
        }.getOrElse {
            println(it.message)
            AuthError.LoginFail.left()
        }

}

class ResponseToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)

sealed class AuthError(
    val messageEnums: MessageEnums
) {
    object LoginFail: AuthError(MessageEnums.LOGIN_FAIL)
}
