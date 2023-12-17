package com.hyoii.domain.brand

import arrow.core.left
import arrow.core.right
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository,
    private val brandImageRepository: BrandImageRepository
) {

    @Transactional(readOnly = true)
    fun getBrands() =
        runCatching {
            brandRepository.findAll().right()
        }.getOrElse {
            logger().error("getBrands")
            BrandError.UNKNOWN.left()
        }

    @Transactional
    fun saveBrand(brandRequest: BrandRequest) =
        runCatching {
            brandRepository.save(Brand.from(brandRequest.brandKo, brandRequest.brandEn, brandRequest.memo)).apply {
                brandImageRepository.save(
                    BrandImage.from(
                        brandRequest.brandImage.path,
                        brandRequest.brandImage.extension,
                        this
                    )
                )
            }.right()
        }.getOrElse {
            BrandError.UNKNOWN.left()
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
            BrandError.UNKNOWN.left()
        }
}

sealed class BrandError(
    val messageEnums: MessageEnums
) {
    data object UNKNOWN : BrandError(MessageEnums.ERROR)
}
