package com.hyoii.domain.member

import com.hyoii.domain.member.MemberRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRoleRepository: JpaRepository<MemberRole, Long>
