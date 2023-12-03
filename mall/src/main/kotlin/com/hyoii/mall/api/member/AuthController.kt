package com.hyoii.mall.api.member

import com.hyoii.annotation.ValidEnum
import com.hyoii.enums.GenderEnums
import com.hyoii.mall.common.res.ResponseData
import com.hyoii.mall.domain.member.MemberService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
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
    suspend fun signUp(@RequestBody @Valid signUpRequestDto: SignUpRequestDto) = memberService.signUp(signUpRequestDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 토큰 발급
     */
    @PostMapping("/login")
    suspend fun login(@RequestBody @Valid memberLoginRequestDto: LoginRequestDto) = authService.login(memberLoginRequestDto).fold(
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

/**
 * 회원가입 요청 DTO
 */
class SignUpRequestDto (
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
class LoginRequestDto(
    @field:NotBlank(message = "이메일은 필수값입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수값입니다.")
    val password: String
)
