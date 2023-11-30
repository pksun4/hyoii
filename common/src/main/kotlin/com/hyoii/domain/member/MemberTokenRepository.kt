package com.hyoii.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberTokenRepository : JpaRepository<MemberToken, Long> {
    fun findByMemberId(memberId: Long): MemberToken?
}
