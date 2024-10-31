package com.hyoii.domain.order

import com.hyoii.common.BaseEntity
import com.hyoii.domain.order.dto.OrderRequest
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serial
import java.util.*

@Entity
@Table(name = "order_option")
class OrderOption(
    var quantity: Int,
    var productOptionKey: Long
) : BaseEntity() {
    @ManyToOne
    @JoinColumn(name = "order_key", foreignKey = ForeignKey(name = "fk_order_option_1"))
    var order: Order? = null
    companion object {
        @Serial
        private const val serialVersionUID: Long = -4348676094587703335L

        private val properties = arrayOf(
            OrderOption::quantity,
            OrderOption::productOptionKey
        )

        fun from(orderRequest: OrderRequest, order: Order) = orderRequest.orderOptionList.map {
            OrderOption(
                quantity = it.quantity,
                productOptionKey = it.productOptionKey
            )
        }.toMutableList()
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is OrderOption || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(properties)
}
