package com.hyoii.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.common.security.JwtTokenProvider
import com.hyoii.common.security.SecurityUtil
import com.hyoii.domain.member.dto.AuthTokenRequest
import com.hyoii.domain.member.dto.AuthTokenResponse
import com.hyoii.domain.member.dto.LoginRequest
import com.hyoii.enums.MessageEnums
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
    private val memberRepository: MemberRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    companion object {
        private const val GRANT_TYPE = "Bearer"
    }

    @Transactional
    suspend fun login(loginRequest: LoginRequest) =
        runCatching {
            // 로그인 아이디/비밀번호 기반으로 AuthenticationToken 생성
            val authenticationToken = UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
            // 실제로 아이디/비밀번호 검증이 이루어지는 부분
            // authenticate 실행될 때 CustomUserDetailService 에서 만든 loadUserByUsername 실행
            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            memberRepository.findByEmail(loginRequest.email)?.let {
                buildToken(authentication).also {token ->
                    refreshTokenRepository.save(
                        RefreshToken(
                            refreshToken = token.refreshToken,
                            memberKey = it.id!!
                        )
                    )
                }.right()
            } ?: AuthError.LoginFail.left()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            AuthError.AuthenticationFail.left()
        }

    @Transactional
    suspend fun reissueToken(authTokenRequest: AuthTokenRequest) =
        runCatching {
            if (!jwtTokenProvider.validateToken(authTokenRequest.refreshToken)) {
                AuthError.RefreshTokenInvalid.left()
            }
            val authentication: Authentication =  jwtTokenProvider.parseToken(authTokenRequest.accessToken)
            val memberToken = refreshTokenRepository.findRefreshTokenByRefreshToken(authTokenRequest.refreshToken)
            memberToken?.let {
                if (memberToken.refreshToken != authTokenRequest.refreshToken) {
                    AuthError.RefreshTokenIssueFail.left()
                }
                buildToken(authentication).also {
                    refreshTokenRepository.save(
                        RefreshToken(
                            refreshToken = it.refreshToken,
                            memberKey = SecurityUtil.getCurrentUser()?.memberKey!!
                        )
                    )
                }.right()
            } ?: AuthError.RefreshTokenIssueFail.left()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    suspend fun logout(tokenRequest: AuthTokenRequest) =
        runCatching {
            refreshTokenRepository.findRefreshTokenByRefreshToken(tokenRequest.refreshToken)?.let {
                refreshTokenRepository.delete(
                    tokenRequest.toEntity(SecurityUtil.getCurrentUser()?.memberKey!!)
                )
                Unit.right()
            } ?: AuthError.LogoutFail.left()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun buildToken(authentication: Authentication) =
        AuthTokenResponse(
            grantType = GRANT_TYPE,
            accessToken = jwtTokenProvider.createToken(authentication),
            refreshToken = jwtTokenProvider.createRefreshToken(authentication)
        )

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = AuthError.Unknown(this.javaClass.name).left()
}

sealed class AuthError(
    val messageEnums: MessageEnums
) {
    data object AuthenticationFail : AuthError(MessageEnums.AUTHENTICATION_FAIL)
    data object LoginFail : AuthError(MessageEnums.LOGIN_FAIL)
    data object RefreshTokenIssueFail : AuthError(MessageEnums.REFRESH_TOKEN_ISSUE_FAIL)
    data object RefreshTokenInvalid : AuthError(MessageEnums.REFRESH_TOKEN_INVALID)
    data object LogoutFail : AuthError(MessageEnums.LOGOUT_FAIL)

    data class Unknown(val className: String) : AuthError(MessageEnums.ERROR)
}
