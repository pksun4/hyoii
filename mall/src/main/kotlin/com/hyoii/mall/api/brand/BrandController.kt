package com.hyoii.mall.api.brand

import com.hyoii.domain.brand.BrandRequest
import com.hyoii.domain.brand.BrandService
import com.hyoii.domain.brand.BrandUpdateRequest
import com.hyoii.mall.common.res.ResponseData
import jakarta.validation.Valid
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
        brandRequest: BrandRequest
    ) = brandService.saveBrand(brandRequest).fold(
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
