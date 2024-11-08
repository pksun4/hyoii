package com.hyoii.mall.api.product

import com.hyoii.domain.product.dto.ProductCategoryRequest
import com.hyoii.domain.product.dto.ProductCategoryResponse
import com.hyoii.domain.product.dto.ProductRequest
import com.hyoii.domain.product.dto.ProductResponse
import com.hyoii.domain.product.ProductService
import com.hyoii.mall.common.res.ResponseData
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "상품 관리")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping("/category")
    fun saveProductCategory(
        @RequestBody @Valid productCategoryRequest: ProductCategoryRequest
    ) = productService.saveProductCategory(productCategoryRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(ProductCategoryResponse(it)) }
    )

    /**
     * 상품 등록
     */
    @PostMapping
    fun saveProduct(
        @RequestBody @Valid productRequest: ProductRequest
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
