package com.hyoii.mall.api.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.MemberRepository
import com.hyoii.domain.member.MemberToken
import com.hyoii.domain.member.MemberTokenRepository
import com.hyoii.enums.MessageEnums
import com.hyoii.mall.security.CustomUser
import com.hyoii.mall.security.JwtTokenProvider
import com.hyoii.utils.logger
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberTokenRepository: MemberTokenRepository,
    private val memberRepository: MemberRepository
) {
    companion object {
        private const val GRANT_TYPE = "Bearer"
    }

    @Transactional
    suspend fun login(loginRequestDto: LoginRequestDto) =
        runCatching {
            // 로그인 아이디/비밀번호 기반으로 AuthenticationToken 생성
            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequestDto.email,
                loginRequestDto.password
            )
            // 실제로 아이디/비밀번호 검증이 이루어지는 부분
            // authenticate 실행될 때 CustomUserDetailService 에서 만든 loadUserByUsername 실행
            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            memberRepository.findByEmail(loginRequestDto.email)?.let {
                // 인증정보 바탕으로 토큰 생성
                buildToken(authentication).apply {
                    memberTokenRepository.save(
                        MemberToken(
                            this.accessToken,
                            this.refreshToken,
                            member = it
                        )
                    )
                }.right()
            } ?: AuthError.LoginFail.left()
        }.getOrElse {
            println(it.message)
            AuthError.LoginFail.left()
        }

    @Transactional
    suspend fun reissueToken(authTokenDto: AuthTokenDto) =
        runCatching {
            if (!jwtTokenProvider.validateToken(authTokenDto.refreshToken)) {
                AuthError.RefreshTokenInvalid.left()
            }
            val authentication: Authentication =  jwtTokenProvider.parseToken(authTokenDto.accessToken)
            val memberToken = memberTokenRepository.findByMemberId((authentication.principal as CustomUser).memberId)
            memberToken?.let {
                if (memberToken.refreshToken != authTokenDto.refreshToken) {
                    AuthError.RefreshTokenIssueFail.left()
                }
                val freshToken = buildToken(authentication)
                memberToken.accessToken = freshToken.accessToken
                memberToken.refreshToken = freshToken.refreshToken
                memberTokenRepository.save(memberToken)

                freshToken.right()
            } ?: AuthError.RefreshTokenIssueFail.left()
        }.getOrElse {
            logger().error(it.message)
            AuthError.RefreshTokenIssueFail.left()
        }

    private fun buildToken(authentication: Authentication) =
        AuthTokenDto(
            grantType = GRANT_TYPE,
            accessToken = jwtTokenProvider.createToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication)
        )
}

sealed class AuthError(
    val messageEnums: MessageEnums
) {
    object LoginFail: AuthError(MessageEnums.LOGIN_FAIL)
    object RefreshTokenIssueFail: AuthError(MessageEnums.REFRESH_TOKEN_ISSUE_FAIL)
    object RefreshTokenInvalid: AuthError(MessageEnums.REFRESH_TOKEN_INVALID)
}
