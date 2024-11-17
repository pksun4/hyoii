package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.enums.RoleEnums
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serial
import java.util.*

@Entity
@Table(name = "member_role")
class MemberRole(
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var role: RoleEnums,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_key", foreignKey = ForeignKey(name = "fk_member_role_1"))
    var member: Member
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is MemberRole || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            MemberRole::role
        )
    )

    companion object {
        @Serial
        private const val serialVersionUID: Long = 6850736501224894548L
    }
}
