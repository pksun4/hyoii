package com.hyoii.mall.api.coupon

import com.hyoii.domain.coupon.CouponService
import com.hyoii.domain.coupon.dto.CouponRequest
import com.hyoii.domain.coupon.dto.CouponResponse
import com.hyoii.mall.common.res.ResponseData
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(
    private val couponService: CouponService
) {

    @PostMapping
    suspend fun saveCoupon(
        @RequestBody @Valid couponRequest: CouponRequest
    ) = couponService.saveCoupon(couponRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(CouponResponse(it)) }
    )
}
