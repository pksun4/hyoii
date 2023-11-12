package com.hyoii.core.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_dt", nullable = false, updatable = false)
    protected var createdDt: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(name = "modified_dt", nullable = false)
    protected var modifiedDt: LocalDateTime = LocalDateTime.MIN

}
