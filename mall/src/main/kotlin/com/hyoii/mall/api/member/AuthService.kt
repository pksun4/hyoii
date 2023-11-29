package com.hyoii.mall.api.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.MemberLoginDto
import com.hyoii.enums.MessageEnums
import com.hyoii.mall.security.JwtTokenProvider
import com.hyoii.utils.logger
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
            // 로그인 아이디/비밀번호 기반으로 AuthenticationToken 생성
            val authenticationToken = UsernamePasswordAuthenticationToken(
                memberLoginDto.email,
                memberLoginDto.password
            )
            // 실제로 아이디/비밀번호 검증이 이루어지는 부분
            // authenticate 실행될 때 CustomUserDetailService 에서 만든 loadUserByUsername 실행
            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            // 인증정보 바탕으로 토큰 실행
            ResponseToken(
                grantType = "Bearer",
                accessToken = jwtTokenProvider.createToken(authentication),
                refreshToken = jwtTokenProvider.createRefreshToken(authentication)
            ).right()
        }.getOrElse {
            println(it.message)
            AuthError.LoginFail.left()
        }

    fun issueRefreshToken() =
        runCatching {

        }.getOrElse {
            logger().error(it.message)
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
