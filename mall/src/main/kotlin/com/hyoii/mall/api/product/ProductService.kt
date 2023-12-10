package com.hyoii.mall.api.product

import com.hyoii.domain.product.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

}
