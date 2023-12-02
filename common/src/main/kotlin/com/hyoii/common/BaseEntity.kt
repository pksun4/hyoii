package com.hyoii.common

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
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
    protected var id: Long? = null

    @CreatedDate
    @Column(name = "created_dt", nullable = false, updatable = false)
    protected var createdDt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(name = "modified_dt", nullable = false)
    protected var modifiedDt: LocalDateTime = LocalDateTime.now()
}
