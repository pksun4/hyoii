package com.hyoii.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.io.Serializable

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity : Serializable {

    @CreatedDate
    @Column(name = "created_dt", nullable = false, updatable = false)
    protected var createdDt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(name = "modified_dt", nullable = false)
    protected var modifiedDt: LocalDateTime = LocalDateTime.now()
}
