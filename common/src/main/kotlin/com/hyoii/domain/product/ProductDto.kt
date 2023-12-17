package com.hyoii.domain.product

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

class ProductRequest(
    @field:NotBlank(message = "상품명은 필수값 입니다.")
    val name: String,
    @field:NotBlank(message = "상품번호는 필수값 입니다.")
    val number: String,
    val stock: Int?
)

class ProductResponse(
    val id: Long,
    val name: String,
    val number: String,
    val stock: Int,
    val createdDt: LocalDateTime
) {
    companion object{
        operator fun invoke(product: Product) = ProductResponse(
            id = product.id!!,
            name = product.name,
            number = product.number,
            stock = product.stock,
            createdDt = product.createdDt
        )
    }
}
