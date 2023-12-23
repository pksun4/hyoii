package com.hyoii.mall.product

import com.hyoii.domain.product.Product
import com.hyoii.domain.product.ProductOption
import com.hyoii.domain.product.ProductRepository
import com.hyoii.domain.product.ProductRequest
import com.hyoii.domain.product.ProductService
import com.hyoii.enums.MessageEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ProductServiceTest {

    private val productRepository: ProductRepository = mockk()
    private val productService: ProductService = ProductService(productRepository)

    companion object {
        private const val PRODUCT_NAME = "상품명"
        private const val PRODUCT_NUMBER = "PRODUCT1"
        private const val CONTENT = "내용"
        private const val PRICE = 30000
        private const val SALE_PRICE = 27000
        private val DELIVERY_TYPE = Product.DeliveryType.FREE
    }

    @Test
    fun `success save product`() {
        val params = ProductRequest(
            name = PRODUCT_NAME,
            number = PRODUCT_NUMBER,
            content = CONTENT,
            price = PRICE,
            salePrice = SALE_PRICE,
            deliveryType = DELIVERY_TYPE
        )
        val product = createdProduct()

        coEvery { productRepository.save(any()) } answers { product }
        runBlocking {
            productService.saveProduct(params).fold(
                { fail -> Assertions.assertEquals(fail.messageEnums, MessageEnums.ERROR) },
                { success ->
                    Assertions.assertEquals(success.name, product.name)
                    Assertions.assertEquals(success.number, product.number)
                    Assertions.assertEquals(success.optionList?.size, 1)
                }
            )

            coVerify(exactly = 1) { productRepository.save(any()) }
        }
    }

    private fun createdProduct() = Product(
        name = PRODUCT_NAME,
        number = PRODUCT_NUMBER,
        content = CONTENT,
        price = PRICE,
        salePrice = SALE_PRICE,
        deliveryType = DELIVERY_TYPE
    ).apply {
        this.optionList = mutableListOf(createdProductOption())
    }

    private fun createdProductOption() = ProductOption(
        name = "BLACK M"
    )
}
