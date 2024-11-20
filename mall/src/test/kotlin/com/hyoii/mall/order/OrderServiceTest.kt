package com.hyoii.mall.order

import com.hyoii.common.security.CurrentUser
import com.hyoii.common.security.SecurityUtil
import com.hyoii.domain.member.MemberCouponRepositorySupport
import com.hyoii.domain.member.MemberCouponResponse
import com.hyoii.domain.member.MemberPointRepository
import com.hyoii.domain.order.Order
import com.hyoii.domain.order.OrderError
import com.hyoii.domain.order.OrderOption
import com.hyoii.domain.order.OrderRepository
import com.hyoii.domain.order.OrderService
import com.hyoii.domain.order.dto.OrderOptionRequest
import com.hyoii.domain.order.dto.OrderRequest
import com.hyoii.domain.product.ProductOption
import com.hyoii.domain.product.ProductOptionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority

internal class OrderServiceTest {

    private val securityUtil: SecurityUtil = mockk()
    private val orderRepository: OrderRepository = mockk()
    private val productOptionRepository: ProductOptionRepository = mockk()
    private val memberPointRepository: MemberPointRepository = mockk()
    private val memberCouponRepositorySupport: MemberCouponRepositorySupport = mockk()
    private val orderService: OrderService = OrderService(
        orderRepository,
        productOptionRepository,
        memberPointRepository,
        memberCouponRepositorySupport
    )

    companion object {
        private const val PAYMENT_AMOUNT = 30000
        private const val USED_POINT = 500
        private const val COUPON_KEY = 1L
        private const val USED_COUPON_AMOUNT = 3000
    }

    @Test
    fun saveOrder() {
        val params= OrderRequest(
            paymentAmount = PAYMENT_AMOUNT,
            usedPoint = USED_POINT,
            couponKey = COUPON_KEY,
            orderOptionList = listOf(
                OrderOptionRequest(
                    productOptionKey = 1L,
                    quantity = 1
                )
            )
        )
        val currentUser = createdCurrentUser()
        val memberCoupon = createdMemberCoupon()

        val order = createdOrder()
        var memberCouponDiscountAmount = order.totalProductAmount * memberCoupon.discountRate
        if (memberCouponDiscountAmount > memberCoupon.discountLimit) {
            memberCouponDiscountAmount = memberCoupon.discountLimit.toDouble()
        }

        coEvery { securityUtil.getCurrentUser() } answers { currentUser }
        coEvery { productOptionRepository.findAllById(any()) } answers { createdProductOptionList() }
        coEvery { memberCouponRepositorySupport.findMemberCoupon(currentUser.memberKey, params.couponKey!!) } answers { memberCoupon }
        coEvery { memberCouponRepositorySupport.updateMemberCouponForUse(any(), any()) } answers { 1L }
        coEvery { orderRepository.save(any()) } answers { order }

        runBlocking {
            orderService.saveOrder(params).fold(
                { fail -> Assertions.assertEquals(fail.messageEnums, OrderError.OrderOptionEmpty) },
                { success ->
                    Assertions.assertEquals(success.status, Order.OrderStatus.OPEN)
                    Assertions.assertEquals(success.paymentAmount, params.paymentAmount)
                    Assertions.assertEquals(success.usedCouponAmount, memberCouponDiscountAmount)
                    Assertions.assertEquals(success.usedPoint, params.usedPoint)
                }
            )

            coVerify(exactly = 1) { productOptionRepository.findAllById(any()) }
            coVerify(exactly = 1) { memberCouponRepositorySupport.findMemberCoupon(any(), any()) }
            coVerify(exactly = 1) { memberCouponRepositorySupport.updateMemberCouponForUse(any(), any()) }
            coVerify(exactly = 1) { orderRepository.save(any()) }
        }
    }

    private fun createdCurrentUser() = CurrentUser(
        memberKey = 1L,
        email = "hyo@gmail.com",
        role = listOf(SimpleGrantedAuthority("ROLE_USER"))
    )

    private fun createdProductOptionList() = mutableListOf(
        ProductOption(
            name = "FREE",
            stock = 100
        )
    )

    private fun createdMemberCoupon() = MemberCouponResponse(
        discountRate = 0.10,
        discountLimit = 10000
    )

    private fun createdOrder() = Order(
        status = Order.OrderStatus.OPEN,
        totalProductAmount = 30000,
        deliveryFee = 2500,
        paymentAmount = PAYMENT_AMOUNT,
        usedPoint = USED_POINT,
        usedCouponAmount = USED_COUPON_AMOUNT,
        couponKey = COUPON_KEY,
        memberKey = 1L
    ).apply {
        orderOptionList = listOf(
            OrderOption(
                quantity = 1,
                productOptionKey = 1L
            )
        )
    }
}
