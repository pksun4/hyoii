package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import com.hyoii.domain.brand.Brand
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
@Table(name = "product")
data class Product(

    @Column(name = "name", length = 200, nullable = false, columnDefinition = "")
    var name: String,

    @Column(name = "number", length = 100, nullable = false)
    var number: String,

    @Column(name = "content", length = 500, nullable = false)
    var content: String,

    @Column(name = "price")
    var price: Int,

    @Column(name = "sale_price")
    var salePrice: Int,

    @Column(name = "delivery_type")
    @Enumerated(EnumType.STRING)
    var deliveryType: DeliveryType,
) : BaseEntity() {

    @Column(name = "read_count")
    @ColumnDefault("0")
    var readCount: Int? = 0

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ProductOption::class)
    var optionList : List<ProductOption>? = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Brand::class)
    var brand: Brand? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1626749272279599298L

        fun from(productRequest: ProductRequest) = Product(
            name = productRequest.name,
            number = productRequest.number,
            content = productRequest.content,
            price =  productRequest.price,
            salePrice = productRequest.salePrice,
            deliveryType = productRequest.deliveryType
        )
    }

    enum class DeliveryType(
        val value: String
    ) {
        FREE("무료배송"),
        PAID("유료배송")
    }
}
