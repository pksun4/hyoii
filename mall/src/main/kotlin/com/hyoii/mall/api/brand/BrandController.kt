package com.hyoii.mall.api.brand

import com.hyoii.mall.common.res.ResponseData
import org.springframework.web.bind.annotation.GetMapping
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
}
