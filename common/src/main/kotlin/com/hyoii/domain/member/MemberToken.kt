package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serial
import java.util.*

@Entity
@Table(name = "member_token")
class MemberToken(
    @Column(name = "access_token", length = 300, nullable = false)
    var accessToken: String,

    @Column(name = "refresh_token", length = 100, nullable = false)
    var refreshToken: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_key", foreignKey = ForeignKey(name = "fk_member_token_1"))
    var member: Member
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is MemberToken || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            MemberToken::accessToken,
            MemberToken::refreshToken
        )
    )

    companion object {
        @Serial
        private const val serialVersionUID: Long = -7442137576893370716L
    }
}
