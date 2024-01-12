package com.hyoii.mall.brand

import com.hyoii.domain.brand.Brand
import com.hyoii.domain.brand.BrandRepository
import com.hyoii.domain.brand.dto.BrandRequest
import com.hyoii.domain.brand.BrandService
import com.hyoii.enums.MessageEnums
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BrandServiceTest {

    private val brandRepository: BrandRepository = mockk()
    private val brandService: BrandService = BrandService(brandRepository)

    companion object {
        private const val BRAND_KO = "나이키"
        private const val BRAND_EN = "NIKE"
    }

    @Test
    fun `success save brand`() {
        val params = BrandRequest(
            brandKo = BRAND_KO,
            brandEn = BRAND_EN,
            memo = "MEMO",
            imagePath = null,
            imageExtension = null
        )
        val brand = createdBrand()

        coEvery { brandRepository.save(any()) } answers { brand }
        runBlocking {
            brandService.saveBrand(params).fold(
                { fail -> Assertions.assertEquals(fail.messageEnums, MessageEnums.ERROR) },
                { success ->
                    Assertions.assertEquals(success.brandKo, params.brandKo)
                    Assertions.assertEquals(success.brandEn, params.brandEn)
                }
            )
            coVerify(exactly = 1) { brandRepository.save(any()) }
        }
    }

    private fun createdBrand() = Brand(
        brandKo = BRAND_KO,
        brandEn = BRAND_EN,
        memo = null,
        imagePath = null,
        imageExtension = null
    )
}
