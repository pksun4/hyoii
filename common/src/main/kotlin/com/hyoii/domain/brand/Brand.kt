package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
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
    var memo: String?
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -1856400393347765651L

        @OneToOne(mappedBy = "brand", targetEntity = BrandImage::class, cascade = [CascadeType.ALL])
        var brandImage: BrandImage? = null

        fun from(
            brandKo: String,
            brandEn: String,
            memo: String?
        ) = Brand(
            brandKo = brandKo,
            brandEn = brandEn,
            memo = memo
        )
    }
}
