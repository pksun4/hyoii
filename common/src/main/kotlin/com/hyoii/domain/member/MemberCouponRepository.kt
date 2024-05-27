package com.hyoii.domain.member

import com.hyoii.domain.coupon.QCoupon.coupon
import com.hyoii.domain.member.QMemberCoupon.memberCoupon
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository


@Repository
class MemberCouponRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(MemberCoupon::class.java) {
    fun findMemberCoupon(memberKey: Long, couponKey: Long): MemberCouponResponse? = jpaQueryFactory
        .select(
            Projections.constructor(
                MemberCouponResponse::class.java,
                coupon.discountRate,
                coupon.discountRate,
            )
        )
        .from(memberCoupon)
        .join(coupon)
        .on(memberCoupon.couponKey.eq(coupon.id))
        .where(
            memberCoupon.memberKey.eq(memberKey),
            memberCoupon.couponKey.eq(couponKey),
            memberCoupon.isUsed.isFalse,
            memberCoupon.isDeleted.isFalse,
            coupon.endAt.before(LocalDateTime.now())
        ).fetchOne()

    fun updateMemberCouponForUse(memberKey: Long, couponKey: Long) = jpaQueryFactory
        .update(memberCoupon)
        .set(memberCoupon.isUsed, true)
        .where(
            memberCoupon.memberKey.eq(memberKey),
            memberCoupon.couponKey.eq(couponKey),
            memberCoupon.isUsed.eq(false)
        ).execute()
}

@Repository
interface MemberCouponRepository : JpaRepository<MemberCoupon, Long>

class MemberCouponResponse (
    val discountRate: Double,
    val discountLimit: Int
)
