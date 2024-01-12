package com.hyoii.domain.order

class OrderRequest(
    val paymentAmount: Int,
    val usedPoint: Int,
    val couponKey: Long,
    val orderOptionList: List<OrderOptionRequest>
)

class OrderOptionRequest(
    val productOptionKey: Long,
    val quantity: Int
)
