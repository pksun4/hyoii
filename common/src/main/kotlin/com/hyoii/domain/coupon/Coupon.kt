package com.hyoii.domain.coupon

import com.hyoii.common.BaseEntity
import com.hyoii.domain.coupon.dto.CouponRequest
import jakarta.persistence.Entity
import java.io.Serial
import java.time.LocalDateTime

@Entity
class Coupon(
    var name: String,
    var discountRate: Double,
    var discountLimit: Int,
    var startAt: LocalDateTime?,
    var endAt: LocalDateTime?
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 604743868997575962L

        private val properties = arrayOf(
            Coupon::name,
            Coupon::discountRate,
            Coupon::discountLimit,
            Coupon::startAt,
            Coupon::endAt
        )

        fun from(couponRequest: CouponRequest) = Coupon(
            name = couponRequest.name!!,
            discountRate = couponRequest.discountRate!!,
            discountLimit = couponRequest.discountLimit!!,
            startAt = couponRequest.startAt,
            endAt = couponRequest.endAt
        )
    }
}
