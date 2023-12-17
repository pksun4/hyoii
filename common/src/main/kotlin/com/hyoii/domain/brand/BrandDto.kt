package com.hyoii.domain.brand

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class BrandRequest(
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandKo: String,
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandEn: String,
    val memo: String?,
    @field:Valid @field:NotNull
    val brandImage: BrandImageRequest
)

class BrandImageRequest(
    @field:NotBlank(message = "브랜드 이미지 형식은 필수값입니다.")
    val extension: String,
    @field:NotBlank(message = "브랜드 이미지 경로는 필수값입니다.")
    val path: String,
)

class BrandUpdateRequest(
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandKo: String,
    @field:NotBlank(message = "브랜드명은 필수값입니다.")
    val brandEn: String,
    val memo: String?
)
