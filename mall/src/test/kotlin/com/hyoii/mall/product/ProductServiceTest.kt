package com.hyoii.mall.product

import com.hyoii.domain.brand.BrandRepository
import com.hyoii.domain.product.Product
import com.hyoii.domain.product.ProductCategoryRepository
import com.hyoii.domain.product.ProductError
import com.hyoii.domain.product.ProductOption
import com.hyoii.domain.product.ProductRepository
import com.hyoii.domain.product.ProductService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.jvm.optionals.getOrNull

internal class ProductServiceTest {

    private val productRepository: ProductRepository = mockk()
    private val productCategoryRepository: ProductCategoryRepository = mockk()
    private val brandRepository: BrandRepository = mockk()
    private val productService: ProductService = ProductService(productRepository, productCategoryRepository, brandRepository)

    companion object {
        private const val PRODUCT_ID = 1L
        private const val BRAND_ID = 1L
        private const val CATEGORY_ID = 1L
        private const val PRODUCT_NAME = "상품명"
        private const val PRODUCT_NUMBER = "PRODUCT1"
        private const val CONTENT = "내용"
        private const val PRICE = 30000
        private const val SALE_PRICE = 27000
        private val DELIVERY_TYPE = Product.DeliveryType.FREE
    }

//    @Test
//    fun `success save product`() {
//        val params = ProductRequest(
//            brandId = BRAND_ID,
//            categoryId = CATEGORY_ID,
//            name = PRODUCT_NAME,
//            number = PRODUCT_NUMBER,
//            content = CONTENT,
//            price = PRICE,
//            salePrice = SALE_PRICE,
//            deliveryType = DELIVERY_TYPE,
//            isExposed = true,
//            optionList = null
//        )
//        val product = createdProduct()
//
//        coEvery { productRepository.save(any()) } answers { product }
//        runBlocking {
//            productService.saveProduct(params).fold(
//                { fail -> Assertions.assertEquals(fail.messageEnums, MessageEnums.ERROR) },
//                { success ->
//                    Assertions.assertEquals(success.name, product.name)
//                    Assertions.assertEquals(success.number, product.number)
//                    Assertions.assertEquals(success.optionList?.size, 1)
//                }
//            )
//            coVerify(exactly = 1) { productRepository.save(any()) }
//        }
//    }

    @Test
    fun `success remove product`() {
        coEvery { productRepository.findById(any()).getOrNull() } answers { createdProduct() }
        coEvery { productRepository.deleteById(any()) } just runs
        runBlocking {
            productService.removeProduct(PRODUCT_ID).fold(
                { fail ->  Assertions.assertEquals(fail, ProductError.ProductEmpty) },
                { success -> Assertions.assertEquals(success, Unit) }
            )
            coVerify(exactly = 1) { productRepository.findById(PRODUCT_ID) }
            coVerify(exactly = 1) { productRepository.deleteById(PRODUCT_ID) }
        }
    }

    @Test
    fun `fail remove product`() {
        coEvery { productRepository.findById(any()).getOrNull() } answers { null }
        coEvery { productRepository.deleteById(any()) } just runs
        runBlocking {
            productService.removeProduct(PRODUCT_ID).fold(
                { fail -> Assertions.assertEquals(fail, ProductError.ProductEmpty) },
                { success -> Assertions.assertEquals(success, Unit) }
            )
            coVerify(exactly = 1) { productRepository.findById(PRODUCT_ID) }
            coVerify(exactly = 0) { productRepository.deleteById(PRODUCT_ID) }
        }
    }

    private fun createdProduct() = Product(
        name = PRODUCT_NAME,
        number = PRODUCT_NUMBER,
        content = CONTENT,
        price = PRICE,
        deliveryType = DELIVERY_TYPE,
        isExposed = true
    ).apply {
        this.optionList = mutableListOf(createdProductOption(this))
    }

    private fun createdProductOption(product: Product) = ProductOption(
        name = "BLACK M",
        stock = 100
    ).apply {
        this.product = product
    }
}
