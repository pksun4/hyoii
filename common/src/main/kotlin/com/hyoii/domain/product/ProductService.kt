package com.hyoii.domain.product

import arrow.core.left
import arrow.core.right
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    @Transactional
    fun saveProduct(productRequest: ProductRequest) =
        runCatching {
            val product = Product.from(productRequest).apply {
                this.optionList = ProductOption.from(productRequest, this)
            }
            productRepository.save(product).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    fun removeProduct(id: Long) =
        runCatching {
            productRepository.findById(id).getOrNull()?.let {
                productRepository.deleteById(id).right()
            } ?: ProductError.ProductEmpty.left()
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
    data object ProductEmpty: ProductError(MessageEnums.PRODUCT_EMPTY)
    data class Unknown(val className: String) : ProductError(MessageEnums.ERROR)
}
