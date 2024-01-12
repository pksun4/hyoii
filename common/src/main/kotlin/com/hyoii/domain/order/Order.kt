package com.hyoii.domain.order

import au.com.console.kassava.kotlinToString
import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial
import java.util.*

@Entity
@Table(name = "orders")
class Order(
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatus,

    @Column(nullable = false)
    @ColumnDefault("0")
    var totalProductAmount: Int,

    @Column(nullable = false)
    @ColumnDefault("0")
    var deliveryFee: Int,

    @Column(nullable = false)
    @ColumnDefault("0")
    var paymentAmount: Int,

    @Column(nullable = false)
    @ColumnDefault("0")
    var usedPoint: Int,

    @Column(nullable = false)
    @ColumnDefault("0")
    var usedCouponAmount: Int,

    @Column(nullable = true)
    var couponKey: Long,

    @Column(nullable = true)
    var memberKey: Long
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -3377967933007872763L

        private val properties = arrayOf(
            Order::status,
            Order::totalProductAmount,
            Order::deliveryFee,
            Order::paymentAmount,
            Order::usedPoint,
            Order::usedCouponAmount,
            Order::couponKey,
            Order::memberKey
        )
    }

    enum class OrderStatus {
        OPEN,
        CANCEL,
        ING,
        COMPLETED,
        CLOSED
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is Order || id != other.id || memberKey != other.memberKey) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id, memberKey)

    @Override
    override fun toString(): String = kotlinToString(properties)
}
