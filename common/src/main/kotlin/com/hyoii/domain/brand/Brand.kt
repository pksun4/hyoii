package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serial

@Entity
@Table(name = "brand")
data class Brand(
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

        fun from(brandRequest: BrandRequest) = Brand(
            brandKo = brandRequest.brandKo,
            brandEn = brandRequest.brandEn,
            memo = brandRequest.memo,
            imagePath = brandRequest.imagePath,
            imageExtension = brandRequest.imageExtension
        )
    }
}
