package com.hyoii.domain.product.dto

import com.hyoii.domain.brand.dto.BrandResponse
import com.hyoii.domain.product.Product
import com.hyoii.domain.product.ProductCategory
import com.hyoii.domain.product.ProductOption
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

class ProductRequest(
    @field:NotNull(message = "브랜드 아이디는 필수값입니다.")
    val brandKey: Long?,
    @field:NotNull(message = "카테고리 아이디는 필수값입니다.")
    val categoryKey: Long?,
    @field:NotBlank(message = "상품명은 필수값 입니다.")
    val name: String?,
    @field:NotBlank(message = "상품번호는 필수값 입니다.")
    val number: String?,
    @field:NotBlank(message = "상품설명은 필수값 입니다.")
    val content: String?,
    @field:NotNull(message = "가격은 필수값 입니다.")
    val price: Int?,
    @field:NotNull(message = "할인 가격은 필수값 입니다.")
    val salePrice: Int?,
    @field:NotNull(message = "배송 유형은 필수값 입니다.")
    val deliveryType: Product.DeliveryType?,
    @field:NotNull(message = "노출 여부는 필수값 입니다.")
    val isExposed: Boolean?,
    @field:Valid
    @field:NotNull
    val optionList: List<ProductOptionRequest>?
) {
    fun toEntity() = Product(
        name = name!!,
        number = number!!,
        content = content!!,
        price =  price!!,
        deliveryType = deliveryType!!,
        isExposed = isExposed!!
    )
}

class ProductOptionRequest(
    @field:NotBlank(message = "상품 옵션명은 필수값 입니다.")
    val name: String?,
    @field:NotNull(message = "상품 옵션 재고는 필수값 입니다.")
    val stock: Int?
) {
    fun toEntity(product: Product) = ProductOption(
        name = name!!,
        stock = stock!!
    ).apply {
        this.product = product
    }
}

class ProductResponse(
    val id: Long,
    val name: String,
    val number: String,
    val content: String,
    val price: Int,
    val deliveryType: String,
    val createdDt: LocalDateTime,
    val brand: BrandResponse?
) {
    companion object{
        operator fun invoke(product: Product) = ProductResponse(
            id = product.id!!,
            name = product.name,
            number = product.number,
            content = product.content,
            price = product.price,
            deliveryType = product.deliveryType.value,
            brand = BrandResponse(product.brand),
            createdDt = product.createdAt
        )

        operator fun invoke(mutableList: MutableList<Product>) = mutableList.map { product ->
            ProductResponse(
                id = product.id!!,
                name = product.name,
                number = product.number,
                content = product.content,
                price = product.price,
                deliveryType = product.deliveryType.value,
                createdDt = product.createdAt,
                brand = BrandResponse(product.brand)
            )
        }
    }
}

class ProductCategoryRequest(
    @field:NotBlank(message = "상품 카테고리명은 필수값 입니다.")
    val categoryName: String
)

class ProductCategoryResponse(
    id: Long,
    name: String
) {
    companion object {
        operator fun invoke(productCategory: ProductCategory) = ProductCategoryResponse(
            id = productCategory.id!!,
            name = productCategory.name
        )
    }
}
