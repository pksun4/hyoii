//package com.hyoii.mall.domain.member
//
//import arrow.core.left
//import arrow.core.right
//import com.hyoii.core.domain.member.*
//import com.hyoii.core.enums.MessageEnums
//import com.hyoii.core.enums.RoleEnums
//import jakarta.transaction.Transactional
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Service
//
//@Service
//class MemberService(
//    private val memberRepository: MemberRepository,
//    private val memberRepositorySupport: MemberRepositorySupport,
//    private val memberRoleRepository: MemberRoleRepository,
//    private val passwordEncoder: PasswordEncoder
//) {
//
//    fun getMemberById(id: Long) = memberRepository.findByIdOrNull(id)
//
//    fun getMemberList() =
//        runCatching {
//            memberRepository.findAll().right()
//        }.getOrElse {
//            "fail".left()
//        }
//
//    /**
//     * 회원가입
//     */
//    @Transactional
//    fun signUp(memberDto: MemberDto) =
//        runCatching {
//            val member = memberRepositorySupport.findByEmail(memberDto.email)
//            if (member != null) {
//                MemberError.MemberExist.left()
//            } else {
//                val member = memberDto.toEntity().apply { this.password = passwordEncoder.encode(this.password) }
//                val savedMember = memberRepository.save(member)
//                memberRoleRepository.save(MemberRole(
//                    null,
//                    RoleEnums.MEMBER,
//                    savedMember
//                ))
//                savedMember.right()
//            }
//        }.getOrElse {
//            MemberError.MemberSignUp.left()
//        }
//
//    /**
//     * 회원조회
//     */
//    fun getMember(idx: Long) =
//
//        runCatching {
//            // todo 검증추가
//            val userIdx = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userIdx
//            memberRepository.findByIdOrNull(userIdx).let {
//                when (it) {
//                    null -> MemberError.MemberEmpty.left()
//                    else -> it.toDto().right()
//                }
//            }
//        }.getOrElse {
//            MemberError.MemberEmpty.left()
//        }
//
//    /**
//     * 회원정보 수정
//     */
//    fun modifyMember(memberDto: MemberDto) =
//        runCatching {
//            memberDto.idx = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userIdx
//            memberRepository.save(memberDto.toEntity()).right()
//        }.getOrElse {
//            MemberError.MemberFail.left()
//        }
//}
//
//class CustomUser(
//    val userIdx: Long,
//    userEmail: String,
//    userPassword: String,
//    authorities: Collection<GrantedAuthority>
//) : User(userEmail, userPassword, authorities)
//
///**
// * 회원 관련 오류
// */
//sealed class MemberError(
//    val messageEnums: MessageEnums
//) {
//    object MemberExist: MemberError(MessageEnums.MEMBER_EXIST)
//    object MemberSignUp: MemberError(MessageEnums.MEMBER_SIGNUP_ERROR)
//    object MemberEmpty: MemberError(MessageEnums.MEMBER_EMPTY)
//
//    object MemberFail: MemberError(MessageEnums.MEMBER_MODIFY_FAIL)
//}
