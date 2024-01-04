package com.hyoii.mall.api.product

import com.hyoii.domain.product.ProductRequest
import com.hyoii.domain.product.ProductResponse
import com.hyoii.domain.product.ProductService
import com.hyoii.mall.common.res.ResponseData
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
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

    /**
     * 상품 조회
     */
    @GetMapping
    fun getProductList() = productService.getProductList().fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(ProductResponse(it)) }
    )

    /**
     * 상품 상세
     */
    @GetMapping("/{productKey}")
    fun getProduct(@PathVariable productKey: Long) = productService.getProduct(productKey).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(ProductResponse(it)) }
    )
}
