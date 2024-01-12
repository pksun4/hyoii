package com.hyoii.domain.coupon.dto

import com.hyoii.domain.coupon.Coupon
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

class CouponRequest(
    @field:NotBlank(message = "쿠폰명은 필수값입니다.")
    val name: String?,
    @field:NotNull(message = "할인율은 필수값입니다.")
    val discountRate: Double?,
    @field:NotNull(message = "할인 제한금액은 필수값입니다.")
    val discountLimit: Int?,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
)

class CouponResponse(
    val name: String,
    val discountRate: Double,
    val discountLimit: Int,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
) {
    companion object {
        operator fun invoke(coupon: Coupon) = CouponResponse(
            name = coupon.name,
            discountRate = coupon.discountRate,
            discountLimit = coupon.discountLimit,
            startAt = coupon.startAt,
            endAt = coupon.endAt
        )
    }
}
