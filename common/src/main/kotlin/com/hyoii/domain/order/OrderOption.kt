package com.hyoii.domain.order

import au.com.console.kassava.kotlinToString
import com.hyoii.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serial
import java.util.*

@Entity
@Table(name = "order_option")
class OrderOption(
    var quantity: Int,
    var orderKey: Long,
    var productOptionKey: Long
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -4348676094587703335L

        private val properties = arrayOf(
            OrderOption::quantity,
            OrderOption::orderKey,
            OrderOption::productOptionKey
        )
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is OrderOption || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = kotlinToString(properties)
}
