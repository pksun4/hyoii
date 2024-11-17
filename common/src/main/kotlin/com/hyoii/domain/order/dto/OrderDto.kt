package com.hyoii.domain.order.dto

import com.hyoii.domain.order.OrderOption

class OrderRequest(
    val paymentAmount: Int,
    val usedPoint: Int?,
    val couponKey: Long?,
    val orderOptionList: List<OrderOptionRequest>
)

class OrderOptionRequest(
    val productOptionKey: Long,
    val quantity: Int
) {
    fun toEntity() = OrderOption(
        quantity = quantity,
        productOptionKey = productOptionKey
    )
}
