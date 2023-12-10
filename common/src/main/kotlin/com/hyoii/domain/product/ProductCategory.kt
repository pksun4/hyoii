package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serial

@Entity
@Table(name = "product_category")
data class ProductCategory(

    @Column
    var name: String

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 5824988711987350590L
    }

}
