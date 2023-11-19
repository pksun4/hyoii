package com.hyoii.mall.api.brand

import arrow.core.left
import arrow.core.right
import com.hyoii.domain.brand.BrandRepository
import com.hyoii.enums.MessageEnums
import com.hyoii.utils.logger
import org.springframework.stereotype.Service

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    fun getBrands() =
        runCatching {
            brandRepository.findAll().right()
        }.getOrElse {
            logger().error("getBrands")
            BrandError.UNKNOWN.left()
        }
}

sealed class BrandError(
    val messageEnums: MessageEnums
) {
    data object UNKNOWN : BrandError(MessageEnums.ERROR)
}
