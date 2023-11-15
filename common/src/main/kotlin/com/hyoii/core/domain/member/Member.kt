package com.hyoii.core.domain.member

import com.hyoii.core.common.BaseEntity
import com.hyoii.core.enums.GenderEnums
import com.hyoii.core.enums.RoleEnums
import jakarta.persistence.*

@Entity
data class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long?,

    @Column(nullable = false, length = 200)
    var email: String,

    @Column(nullable = false, length = 200)
    var password: String,

    @Column(nullable = false, length = 200)
    var name: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderEnums,

    ) : BaseEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    var memberRole: List<MemberRole>? = null

    /**
     * Entity to Dto
     */
    fun toDto() = MemberResDto(
        idx!!, email, name, gender.gender

    )
}

@Entity
class MemberRole(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var role: RoleEnums,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_1"))
    var member: Member

)
