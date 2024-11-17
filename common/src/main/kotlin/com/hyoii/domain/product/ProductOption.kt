package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import com.hyoii.domain.product.dto.ProductRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serial
import java.util.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "product_option")
class ProductOption(

    @Column(length = 100, nullable = false)
    var name: String,

    @Column(nullable = false)
    @ColumnDefault("0")
    var stock: Int = 0,
) : BaseEntity() {

    @ManyToOne
    @JoinColumn(name = "product_key", foreignKey = ForeignKey(name = "fk_product_option_1"))
    var product: Product? = null

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is ProductOption || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            ProductOption::name,
            ProductOption::stock
        )
    )

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6625420606770848114L
    }
}
