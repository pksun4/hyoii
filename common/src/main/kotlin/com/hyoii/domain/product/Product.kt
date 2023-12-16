package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
@Table(name = "product")
data class Product(

    @Column(name = "name", length = 200, nullable = false, columnDefinition = "")
    var name: String,

    @Column(name = "number", length = 100, nullable = false)
    var number: String,

    @Column(name = "read_count")
    @ColumnDefault("0")
    var readCount: Int,

    @Column(name = "stock")
    @ColumnDefault("0")
    var stock: Int

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 1626749272279599298L
    }
}
