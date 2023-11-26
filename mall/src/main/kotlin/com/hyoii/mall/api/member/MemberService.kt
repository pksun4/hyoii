package com.hyoii.mall.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.MemberDto
import com.hyoii.domain.member.MemberRepository
import com.hyoii.domain.member.MemberRole
import com.hyoii.domain.member.MemberRoleRepository
import com.hyoii.enums.MessageEnums
import com.hyoii.enums.RoleEnums
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 회원가입
     */
    @Transactional
    fun signUp(memberDto: MemberDto) =
        runCatching {
            val member = memberRepository.findByEmail(memberDto.email)
            if (member != null) {
                MemberError.MemberExist.left()
            } else {
                val member = memberDto.toEntity().apply { this.password = passwordEncoder.encode(this.password) }
                val savedMember = memberRepository.save(member)
                memberRoleRepository.save(
                    MemberRole(
                    null,
                    RoleEnums.MEMBER,
                    savedMember
                )
                )
                savedMember.right()
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
