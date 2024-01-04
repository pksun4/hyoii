package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
@Table(name = "product_option")
data class ProductOption(

    @Column(length = 100, nullable = false)
    var name: String,

    @Column(nullable = false)
    @ColumnDefault("0")
    var stock: Int = 0,

    @ManyToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_product_option_1"))
    var product: Product?
) : BaseEntity() {

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6625420606770848114L

        fun from(productRequest: ProductRequest, product: Product): MutableList<ProductOption>? =
            productRequest.optionList?.map {
                ProductOption(
                    name = it.name!!,
                    stock = it.stock!!,
                    product = product
                )
            }?.toMutableList()
        }
}
