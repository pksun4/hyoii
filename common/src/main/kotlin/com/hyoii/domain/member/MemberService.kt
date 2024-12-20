package com.hyoii.domain.member

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.member.dto.SignUpRequest
import com.hyoii.enums.MessageEnums
import com.hyoii.enums.RoleEnums
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
    suspend fun signUp(signUpRequest: SignUpRequest) =
        runCatching {
            memberRepository.findByEmail(signUpRequest.email)?.let {
                MemberError.MemberExist.left()
            } ?: memberRepository.save(
                signUpRequest.toEntity().apply {
                    this.memberRole = mutableListOf(MemberRole(
                        role = RoleEnums.MEMBER,
                        member = this
                    ))
                }
            ).right()
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
