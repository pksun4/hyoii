package com.hyoii.domain.order

import arrow.core.left
import arrow.core.right
import com.hyoii.common.security.SecurityUtil
import com.hyoii.domain.member.MemberCouponRepositorySupport
import com.hyoii.domain.member.MemberCouponResponse
import com.hyoii.domain.member.MemberPointRepository
import com.hyoii.domain.order.dto.OrderRequest
import com.hyoii.domain.product.ProductOption
import com.hyoii.domain.product.ProductOptionRepository
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import kotlin.jvm.optionals.getOrNull
import kotlin.math.roundToInt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val memberPointRepository: MemberPointRepository,
    private val memberCouponRepositorySupport: MemberCouponRepositorySupport
) {
    @Transactional
    suspend fun saveOrder(orderRequest: OrderRequest) =
        runCatching {
            val currentUser = SecurityUtil.getCurrentUser()
            val optionIds = orderRequest.orderOptionList.map { option -> option.productOptionKey }.toMutableList()
            if (currentUser == null || optionIds.isEmpty()) {
                OrderError.OrderOptionEmpty.left()
            }
            val memberKey = currentUser!!.memberKey

            // 재고 체크
            val productOptionList = productOptionRepository.findAllById(optionIds)
            val checkProductOptionList = mutableListOf<ProductOption>()
            for (productOption in productOptionList) {
                if (productOption.stock > 0) {
                    productOption.stock -= 1
                    checkProductOptionList.add(productOption)
                }
            }
            val sumByProductPrice = checkProductOptionList.sumOf { it.product!!.price }
            val deliveryFee = getDeliveryFee(sumByProductPrice)

            // 쿠폰 적용 및 사용 처리
            val couponKey = orderRequest.couponKey
            var usedCouponAmount = 0
            if (couponKey != null && couponKey > 0L) {
                val memberCoupon = memberCouponRepositorySupport.findMemberCoupon(memberKey, couponKey)
                usedCouponAmount = getCouponAmount(memberCoupon, sumByProductPrice)
                memberCouponRepositorySupport.updateMemberCouponForUse(memberKey, couponKey)
            }

            // 포인트 적용 및 사용 처리
            var usedPoint = 0
            memberPointRepository.findById(memberKey).getOrNull()?.let {
                orderRequest.usedPoint?.let { used ->
                    if (it.point > used) {
                        usedPoint = used
                        it.point -= used
                    }
                }
            }

            // 주문
            val order = Order(
                status = Order.OrderStatus.OPEN,
                totalProductAmount = sumByProductPrice + 5000,
                deliveryFee = deliveryFee,
                paymentAmount = orderRequest.paymentAmount,
                usedPoint = usedPoint,
                usedCouponAmount = usedCouponAmount,
                couponKey = orderRequest.couponKey,
                memberKey = memberKey
            ).apply {
                orderOptionList = orderRequest.orderOptionList.map {
                    it.toEntity()
                }
            }
            orderRepository.save(order)

            order.right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun getDeliveryFee(sumByProductPrice: Int): Int {
        return if (sumByProductPrice > 50000) return 0 else 5000
    }

    private fun getCouponAmount(memberCouponResponse: MemberCouponResponse?, sumByProductPrice: Int) =
        memberCouponResponse?.let {
            val discountLimit = memberCouponResponse.discountLimit
            val discountAmt : Int = (memberCouponResponse.discountRate * sumByProductPrice).roundToInt()
            if (discountAmt > discountLimit) {
                discountLimit
            } else {
                discountAmt
            }
        } ?: 0

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = OrderError.Unknown(this.javaClass.name).left()
}

sealed class OrderError(
    val messageEnums: MessageEnums
) {
    data object OrderOptionEmpty : OrderError(MessageEnums.ORDER_OPTION_EMPTY)
    data class Unknown(val className: String) : OrderError(MessageEnums.ERROR)
}
