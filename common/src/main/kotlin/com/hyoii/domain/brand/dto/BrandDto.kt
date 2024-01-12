package com.hyoii.domain.brand.dto

import com.hyoii.domain.brand.Brand
import jakarta.validation.constraints.NotBlank

class BrandRequest(
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandKo: String,
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandEn: String,
    val memo: String?,
    val imagePath: String?,
    val imageExtension: String?
)

class BrandUpdateRequest(
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandKo: String,
    @field:NotBlank(message = "브랜드명(영문)은 필수값입니다.")
    val brandEn: String,
    val memo: String?
)

class BrandResponse(
    val brandKo: String,
    val brandEn: String,
    val imagePath: String?,
    val imageExtension: String?
) {
    companion object {
        operator fun invoke(brand: Brand?) = brand?.let {
            BrandResponse(
                brandKo = it.brandKo,
                brandEn = brand.brandEn,
                imagePath = brand.imagePath,
                imageExtension = brand.imageExtension
            )
        }

        operator fun invoke(brandList: MutableList<Brand>) = brandList.map {
            BrandResponse(
                brandKo = it.brandKo,
                brandEn = it.brandEn,
                imagePath = it.imagePath,
                imageExtension = it.imageExtension
            )
        }.toMutableList()
    }
}
