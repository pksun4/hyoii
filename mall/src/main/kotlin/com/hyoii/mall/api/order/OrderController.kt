package com.hyoii.mall.api.order

import com.hyoii.domain.order.OrderService
import com.hyoii.domain.order.dto.OrderRequest
import com.hyoii.mall.common.res.ResponseData
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService
) {

    /**
     * 주문
     */
    @PostMapping
    suspend fun saveOrder(orderRequest: OrderRequest) = orderService.saveOrder(orderRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    /**
     * 주문 목록
     */
    fun getOrderList() = ""

    /**
     * 주문 상세
     */
    fun getOrder() = ""
}
