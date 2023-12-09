package com.hyoii.mall.api.brand

import com.hyoii.mall.common.res.ResponseData
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/brands")
class BrandController(
    private val brandService: BrandService
) {

    @GetMapping("")
    fun getBrands() = brandService.getBrands().fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    @PostMapping
    fun saveBrand(
        @RequestBody @Valid
        brandRequestDto: BrandRequest
    ) = brandService.saveBrand(brandRequestDto).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )

    @PutMapping("/{brandId}")
    fun updateBrand(
        @PathVariable brandId: Long,
        @RequestBody @Valid brandUpdateRequest: BrandUpdateRequest
    ) = brandService.updateBrand(brandId, brandUpdateRequest).fold(
        { ResponseData.fail(it.messageEnums) },
        { ResponseData.success(it) }
    )
}

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
