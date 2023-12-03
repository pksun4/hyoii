package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.enums.RoleEnums
import jakarta.persistence.*
import java.io.Serial

@Entity
@Table(name = "member_role")
data class MemberRole(

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var role: RoleEnums,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_1"))
    var member: Member

) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 6850736501224894548L
    }
}
