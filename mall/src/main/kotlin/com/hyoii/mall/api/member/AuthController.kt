package com.hyoii.mall.api.member

import com.hyoii.domain.member.MemberDto
import com.hyoii.domain.member.MemberLoginDto
import com.hyoii.mall.common.res.ResponseData
import com.hyoii.mall.domain.member.MemberService
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
     * 회원가입
     */
    @PostMapping("/signup")
    suspend fun signUp(@RequestBody @Valid memberDto: MemberDto) = memberService.signUp(memberDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 토큰 발급
     */
    @PostMapping("/login")
    suspend fun login(@RequestBody @Valid memberLoginDto: MemberLoginDto) = authService.login(memberLoginDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    suspend fun reissue(
        @RequestBody @Valid authDto: AuthTokenDto) = authService.reissueToken(authDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )


}
