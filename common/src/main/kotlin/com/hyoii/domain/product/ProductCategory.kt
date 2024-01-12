package com.hyoii.domain.product

import au.com.console.kassava.kotlinToString
import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serial
import java.util.*

@Entity
@Table(name = "product_category")
data class ProductCategory(

    @Column(length = 50, nullable = false)
    var name: String

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 5824988711987350590L

        private val properties = arrayOf(
            ProductCategory::name
        )
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is ProductCategory || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = kotlinToString(properties)

}
