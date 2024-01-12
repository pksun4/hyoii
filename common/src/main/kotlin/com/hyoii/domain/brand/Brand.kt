package com.hyoii.domain.brand

import au.com.console.kassava.kotlinToString
import com.hyoii.common.BaseEntity
import com.hyoii.domain.brand.dto.BrandRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.io.Serial
import java.util.*

@Entity
class Brand(
    @Column(name = "brand_ko", length = 100, nullable = false)
    var brandKo: String,

    @Column(name = "brand_en", length = 100, nullable = false)
    var brandEn: String,

    @Column(length = 200)
    var memo: String?,

    @Column(length = 200, nullable = false)
    var imagePath: String?,

    @Column(length = 50, nullable = false)
    var imageExtension: String?
) : BaseEntity() {

    companion object {
        @Serial
        private const val serialVersionUID: Long = -1856400393347765651L

        private val properties = arrayOf(
            Brand::brandKo,
            Brand::brandEn,
            Brand::memo,
            Brand::imagePath,
            Brand::imageExtension
        )

        fun from(brandRequest: BrandRequest) = Brand(
            brandKo = brandRequest.brandKo,
            brandEn = brandRequest.brandEn,
            memo = brandRequest.memo,
            imagePath = brandRequest.imagePath,
            imageExtension = brandRequest.imageExtension
        )

    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is Brand || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = kotlinToString(properties)
}
