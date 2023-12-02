package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
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
    }
}
