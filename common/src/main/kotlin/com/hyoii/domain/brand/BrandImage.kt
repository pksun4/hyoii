package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.*
import java.io.Serial

@Entity
@Table(name = "brand_image")
data class BrandImage(

    @Column(length = 200, nullable = false)
    var path: String,

    @Column(length = 50, nullable = false)
    var extension: String,

    @OneToOne
    @JoinColumn(foreignKey = ForeignKey(name = "brand_image_1"))
    var brand: Brand

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -1871912367941801453L

        fun from(
            path: String,
            extension: String,
            brand: Brand
        ) = BrandImage(
            path = path,
            extension = extension,
            brand = brand
        )
    }


}
