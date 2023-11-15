package com.hyoii.core.domain.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRoleRepository: JpaRepository<MemberRole, Long>
