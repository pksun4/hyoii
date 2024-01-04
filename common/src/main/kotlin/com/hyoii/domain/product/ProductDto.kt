package com.hyoii.domain.product

import com.hyoii.domain.brand.BrandResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

class ProductRequest(
    @field:NotNull(message = "브랜드 아이디는 필수값입니다.")
    val brandId: Long?,
    @field:NotNull(message = "카테고리 아이디는 필수값입니다.")
    val categoryId: Long?,
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
    val isExposed: Boolean?,
    @field:Valid
    @field:NotNull
    val optionList: List<ProductOptionRequest>?
)

class ProductOptionRequest(
    @field:NotBlank(message = "상품 옵션명은 필수값 입니다.")
    val name: String?,
    @field:NotNull(message = "상품 옵션 재고는 필수값 입니다.")
    val stock: Int?
)

class ProductResponse(
    val id: Long,
    val name: String,
    val number: String,
    val content: String,
    val price: Int,
    val salePrice: Int,
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
            salePrice = product.salePrice,
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
                salePrice = product.salePrice,
                deliveryType = product.deliveryType.value,
                createdDt = product.createdAt,
                brand = BrandResponse(product.brand)
            )
        }
    }
}
