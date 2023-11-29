package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.enums.RoleEnums
import jakarta.persistence.*

@Entity
data class MemberRole(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var role: RoleEnums,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_1"))
    var member: Member

) : BaseEntity()
