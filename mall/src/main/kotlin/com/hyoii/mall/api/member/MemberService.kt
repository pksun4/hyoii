package com.hyoii.mall.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.*
import com.hyoii.enums.GenderEnums
import com.hyoii.enums.MessageEnums
import com.hyoii.enums.RoleEnums
import com.hyoii.mall.api.member.SignUpRequestDto
import com.hyoii.mall.security.SecurityUtil.passwordEncode
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository
) {

    /**
     * 회원가입
     */
    @Transactional
    fun signUp(signUpRequestDto: SignUpRequestDto) =
        runCatching {
            val member = memberRepository.findByEmail(signUpRequestDto.email)
            if (member != null) {
                MemberError.MemberExist.left()
            } else {
                memberRepository.save(
                    Member.from(
                        email = signUpRequestDto.email,
                        password = signUpRequestDto.password.passwordEncode(),
                        name = signUpRequestDto.name,
                        gender = GenderEnums.valueOf(signUpRequestDto.gender)
                    )
                ).apply {
                    memberRoleRepository.save(
                        MemberRole(
                            RoleEnums.MEMBER,
                            this
                        )
                    )
                }.right()
            }
        }.getOrElse {
            MemberError.MemberSignUp.left()
        }
}

/**
 * 회원 관련 오류
 */
sealed class MemberError(
    val messageEnums: MessageEnums
) {
    object MemberExist: MemberError(MessageEnums.MEMBER_EXIST)
    object MemberSignUp: MemberError(MessageEnums.MEMBER_SIGNUP_ERROR)
    object MemberEmpty: MemberError(MessageEnums.MEMBER_EMPTY)

    object MemberFail: MemberError(MessageEnums.MEMBER_MODIFY_FAIL)
}
