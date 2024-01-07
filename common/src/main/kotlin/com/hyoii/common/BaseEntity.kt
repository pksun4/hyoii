package com.hyoii.common

import com.hyoii.common.security.SecurityUtil
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.boot.context.properties.bind.DefaultValue
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.time.LocalDateTime
import java.io.Serializable

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -6895063439119023377L
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, columnDefinition = "tinyint(1)")
    @ColumnDefault("false")
    var isDeleted: Boolean = false

    @CreatedBy
    @Column(nullable = false, updatable = false)
    var createdBy: Long? = 0

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedBy
    var updatedBy: Long? = 0

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
