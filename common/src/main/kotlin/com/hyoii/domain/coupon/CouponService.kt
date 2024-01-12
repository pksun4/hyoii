package com.hyoii.domain.coupon

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.coupon.dto.CouponRequest
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {
    @Transactional
    suspend fun saveCoupon(couponRequest: CouponRequest) =
        runCatching {
            couponRepository.save(Coupon.from(couponRequest)).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun Throwable.throwUnknownError() = CouponError.Unknown(this.javaClass.name).left()
    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
}

sealed class CouponError(
    val messageEnums: MessageEnums
) {
    data class Unknown(val className: String) : CouponError(MessageEnums.ERROR)
}
