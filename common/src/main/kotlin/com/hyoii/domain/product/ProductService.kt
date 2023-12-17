package com.hyoii.domain.product

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.product.ProductRepository
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    @Transactional
    fun saveProduct(productRequest: ProductRequest) =
        runCatching {
            productRepository.save(Product.from(productRequest)).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
    private fun Throwable.throwUnknownError() = ProductError.Unknown(this.javaClass.name).left()
}

sealed class ProductError(
    val messageEnums: MessageEnums
) {
    data class Unknown(val className: String) : ProductError(MessageEnums.ERROR)
}
