package com.hyoii.mall.api.member

import com.hyoii.domain.member.AuthService
import com.hyoii.domain.member.AuthTokenRequest
import com.hyoii.domain.member.LoginRequest
import com.hyoii.domain.member.SignUpRequest
import com.hyoii.mall.common.res.ResponseData
import com.hyoii.domain.member.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val memberService: MemberService
) {

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody @Valid signUpRequest: SignUpRequest) = memberService.signUp(signUpRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 토큰 발급
     */
    @PostMapping("/login")
    suspend fun login(@RequestBody @Valid loginRequest: LoginRequest) = authService.login(loginRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    suspend fun reissue(
        @RequestBody @Valid authTokenRequest: AuthTokenRequest
    ) = authService.reissueToken(authTokenRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

}

