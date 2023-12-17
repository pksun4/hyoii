package com.hyoii.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.common.security.SecurityUtil.passwordEncode
import com.hyoii.enums.GenderEnums
import com.hyoii.enums.MessageEnums
import com.hyoii.enums.RoleEnums
import com.hyoii.utils.logger
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
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = MemberError.Unknown(this.javaClass.name).left()
}

/**
 * 회원 관련 오류
 */
sealed class MemberError(
    val messageEnums: MessageEnums
) {
    data object MemberExist: MemberError(MessageEnums.MEMBER_EXIST)
    data object MemberSignUp: MemberError(MessageEnums.MEMBER_SIGNUP_ERROR)
    data object MemberEmpty: MemberError(MessageEnums.MEMBER_EMPTY)
    data object MemberFail: MemberError(MessageEnums.MEMBER_MODIFY_FAIL)
    data class Unknown(val className: String): MemberError(MessageEnums.ERROR)
}
