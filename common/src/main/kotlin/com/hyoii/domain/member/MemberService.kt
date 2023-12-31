package com.hyoii.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    /**
     * 회원가입
     */
    @Transactional
    fun signUp(signUpRequest: SignUpRequest) =
        runCatching {
            memberRepository.findByEmail(signUpRequest.email)?.let {
                memberRepository.save(
                    Member.from(signUpRequest).apply {
                        this.memberRole = mutableListOf(MemberRole.fromForMember(it))
                    }
                ).right()
            } ?: MemberError.MemberExist.left()
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
