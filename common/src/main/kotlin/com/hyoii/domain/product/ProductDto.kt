package com.hyoii.domain.product

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

class ProductRequest(
    @field:NotBlank(message = "상품명은 필수값 입니다.")
    val name: String,
    @field:NotBlank(message = "상품번호는 필수값 입니다.")
    val number: String,
    @field:NotBlank(message = "상품설명은 필수값 입니다.")
    val content: String,
    @field:NotNull(message = "가격은 필수값 입니다.")
    val price: Int,
    @field:NotNull(message = "할인 가격은 필수값 입니다.")
    val salePrice: Int,
    @field:NotNull(message = "배송 유형은 필수값 입니다.")
    val deliveryType: Product.DeliveryType,
    val optionList: List<ProductOptionRequest>?
)

class ProductOptionRequest(
    val name: String,
    val stock: Int
)

class ProductResponse(
    val id: Long,
    val name: String,
    val number: String,
    val content: String,
    val price: Int,
    val salePrice: Int,
    val deliveryType: String,
    val createdDt: LocalDateTime
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
            createdDt = product.createdDt
        )
    }
}
