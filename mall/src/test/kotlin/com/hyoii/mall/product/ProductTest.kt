package com.hyoii.mall.product

import com.hyoii.domain.product.ProductRepository
import com.hyoii.mall.api.product.ProductService
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ProductTest {

    private val productRepository: ProductRepository = mockk()
    private val productService: ProductService = ProductService(productRepository)

    @Test
    fun test() = println("TODO TEST!")

}
