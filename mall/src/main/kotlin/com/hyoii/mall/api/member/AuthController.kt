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
    fun signUp(@RequestBody @Valid memberDto: MemberDto) = memberService.signUp(memberDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid memberLoginDto: MemberLoginDto) = authService.login(memberLoginDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )
}
