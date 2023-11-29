package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.*
import java.io.Serial

@Entity
data class MemberToken(
    @Id
    @GeneratedValue
    var id: Long?,
    @Column(length = 100, nullable = false)
    var accessToken: String,
    @Column(length = 100, nullable = false)
    var refreshToken: String,
    @Column(nullable = false)
    var memberId: Long
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -7442137576893370716L
    }
}
