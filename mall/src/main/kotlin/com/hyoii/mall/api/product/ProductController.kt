package com.hyoii.mall.api.product

import com.hyoii.domain.product.ProductRequest
import com.hyoii.domain.product.ProductResponse
import com.hyoii.domain.product.ProductService
import com.hyoii.mall.common.res.ResponseData
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    private val productService: ProductService
) {

    /**
     * 상품 등록
     */
    @PostMapping
    fun saveProduct(
        @RequestBody
        @Valid
        productRequest: ProductRequest
    ) = productService.saveProduct(productRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(ProductResponse(it)) }
    )
}
