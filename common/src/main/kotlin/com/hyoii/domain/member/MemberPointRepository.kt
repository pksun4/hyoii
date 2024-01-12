package com.hyoii.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberPointRepository: JpaRepository<MemberPoint, Long>
