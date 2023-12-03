package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.*
import java.io.Serial

@Entity
@Table(name = "member_token")
data class MemberToken(

    @Column(name = "access_token", length = 100, nullable = false)
    var accessToken: String,
    @Column(name = "refresh_token", length = 100, nullable = false)
    var refreshToken: String,
    @OneToOne
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_token_1"))
    var member: Member

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -7442137576893370716L
    }
}
