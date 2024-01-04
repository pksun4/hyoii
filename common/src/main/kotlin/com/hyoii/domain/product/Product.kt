package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import com.hyoii.domain.brand.Brand
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
@Table(name = "product")
data class Product(

    @Column(length = 200, nullable = false)
    var name: String,

    @Column(length = 100, nullable = false)
    var number: String,

    @Column(length = 500, nullable = false)
    var content: String,

    @Column(nullable = false)
    @ColumnDefault("0")
    var price: Int,

    @Column(nullable = false)
    @ColumnDefault("0")
    var salePrice: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var deliveryType: DeliveryType,

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    @ColumnDefault("false")
    var isExposed: Boolean
) : BaseEntity() {
    @Column(nullable = false)
    @ColumnDefault("0")
    var readCount: Int? = 0

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ProductOption::class, mappedBy = "product", cascade = [CascadeType.PERSIST])
    var optionList : List<ProductOption>? = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Brand::class, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "brand_id", foreignKey = ForeignKey(name = "fk_product_1"))
    var brand: Brand? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "category_id", foreignKey = ForeignKey(name = "fk_product_2"))
    var productCategory: ProductCategory? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1626749272279599298L

        fun from(productRequest: ProductRequest) = Product(
            name = productRequest.name!!,
            number = productRequest.number!!,
            content = productRequest.content!!,
            price =  productRequest.price!!,
            salePrice = productRequest.salePrice!!,
            deliveryType = productRequest.deliveryType!!,
            isExposed = productRequest.isExposed!!
        )
    }

    enum class DeliveryType(
        val value: String
    ) {
        FREE("무료배송"),
        PAID("유료배송")
    }
}
