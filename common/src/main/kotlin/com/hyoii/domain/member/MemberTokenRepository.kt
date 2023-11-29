package com.hyoii.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repositorys
interface MemberTokenRepository : JpaRepository<MemberToken, Long>
