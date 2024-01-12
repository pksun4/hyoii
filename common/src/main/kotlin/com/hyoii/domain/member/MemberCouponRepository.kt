package com.hyoii.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberCouponRepository : JpaRepository<MemberCoupon, Long>
