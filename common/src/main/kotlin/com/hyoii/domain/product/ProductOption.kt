package com.hyoii.domain.product

import au.com.console.kassava.kotlinToString
import com.hyoii.common.BaseEntity
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
data class ProductOption(

    @Column(length = 100, nullable = false)
    var name: String,

    @Column(nullable = false)
    @ColumnDefault("0")
    var stock: Int = 0,

    @ManyToOne
    @JoinColumn(name = "product_key", foreignKey = ForeignKey(name = "fk_product_option_1"))
    var product: Product?
) : BaseEntity() {

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6625420606770848114L

        private val properties = arrayOf(
            ProductOption::name,
            ProductOption::stock
        )

        fun from(productRequest: ProductRequest, product: Product): MutableList<ProductOption>? =
            productRequest.optionList?.map {
                ProductOption(
                    name = it.name!!,
                    stock = it.stock!!,
                    product = product
                )
            }?.toMutableList()
        }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is ProductOption || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = kotlinToString(properties)
}
