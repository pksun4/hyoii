package com.hyoii.domain.product

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
@Table(name = "product_option")
data class ProductOption(

    @Column(name = "name")
    var name: String

) : BaseEntity() {
    @Column(name = "stock")
    @ColumnDefault("0")
    var stock: Int = 0

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6625420606770848114L
    }
}
