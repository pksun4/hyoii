package com.hyoii.mall.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.common.security.SecurityUtil.passwordEncode
import com.hyoii.domain.member.Member
import com.hyoii.domain.member.MemberRepository
import com.hyoii.domain.member.MemberRole
import com.hyoii.domain.member.MemberRoleRepository
import com.hyoii.domain.member.SignUpRequest
import com.hyoii.enums.GenderEnums
import com.hyoii.enums.MessageEnums
import com.hyoii.enums.RoleEnums
import jakarta.transaction.Transactional
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
    fun signUp(signUpRequest: SignUpRequest) =
        runCatching {
            val member = memberRepository.findByEmail(signUpRequest.email)
            if (member != null) {
                MemberError.MemberExist.left()
            } else {
                memberRepository.save(
                    Member.from(
                        email = signUpRequest.email,
                        password = signUpRequest.password.passwordEncode(),
                        name = signUpRequest.name,
                        gender = GenderEnums.valueOf(signUpRequest.gender)
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
