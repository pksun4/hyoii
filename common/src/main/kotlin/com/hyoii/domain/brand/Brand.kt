package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import java.io.Serial

@Entity
data class Brand(

    @Column(length = 100, nullable = false)
    var brand: String,

    @Column(length = 200)
    var memo: String?

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -1856400393347765651L

        @OneToOne(mappedBy = "brand", targetEntity = BrandImage::class, cascade = [CascadeType.ALL])
        var brandImage: BrandImage? = null

        fun from(
            brand: String,
            memo: String?
        ) = Brand(
            brand = brand,
            memo = memo
        )
    }
}
