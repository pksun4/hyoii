package com.hyoii.domain.brand

import arrow.core.left
import arrow.core.right
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    @Transactional(readOnly = true)
    fun getBrands() =
        runCatching {
            brandRepository.findAll().right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    fun saveBrand(brandRequest: BrandRequest) =
        runCatching {
            brandRepository.save(
                Brand.from(brandRequest).apply {
                    this.brandImage = BrandImage.from(brandRequest.brandImage)
                }
            ).right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    @Transactional
    fun updateBrand(brandId: Long, brandUpdateRequest: BrandUpdateRequest) =
        runCatching {
            brandRepository.findBrandById(brandId)?.let {
                it.brandKo = brandUpdateRequest.brandKo
                it.brandEn = brandUpdateRequest.brandEn
                it.memo = brandUpdateRequest.memo
            }
            Unit.right()
        }.getOrElse {
            it.errorLogging(this.javaClass)
            it.throwUnknownError()
        }

    private fun Throwable.throwUnknownError() = BrandError.Unknown(this.javaClass.name).left()
    private fun <C> Throwable.errorLogging(kClass: Class<C>) = logger().error("[Error][${kClass.javaClass.methods.first().name}]", this)
}

sealed class BrandError(
    val messageEnums: MessageEnums
) {
    data class Unknown(val className: String) : BrandError(MessageEnums.ERROR)
}
