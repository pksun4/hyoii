package com.hyoii.domain.brand

import com.hyoii.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.io.Serial

@Entity
data class Brand(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -1856400393347765651L
    }
}
