package com.hyoii.mall.api.brand

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.brand.Brand
import com.hyoii.domain.brand.BrandImage
import com.hyoii.domain.brand.BrandImageRepository
import com.hyoii.domain.brand.BrandRepository
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
    fun saveBrand(brandRequestDto: BrandRequestDto) =
        runCatching {
            brandRepository.save(Brand.from(brandRequestDto.brand, brandRequestDto.memo)).apply {
                brandImageRepository.save(
                    BrandImage.from(
                        brandRequestDto.brandImage.path,
                        brandRequestDto.brandImage.extension,
                        this
                    )
                )
            }.right()
        }.getOrElse {
            BrandError.UNKNOWN.left()
        }
}

sealed class BrandError(
    val messageEnums: MessageEnums
) {
    data object UNKNOWN : BrandError(MessageEnums.ERROR)
}
