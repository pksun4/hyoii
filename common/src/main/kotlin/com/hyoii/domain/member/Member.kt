package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.enums.GenderEnums
import com.hyoii.enums.RoleEnums
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import jakarta.persistence.OneToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", targetEntity = MemberRole::class)
    var memberRole: List<MemberRole>? = mutableListOf()

    /**
     * Entity to Dto
     */
    fun toDto() = MemberResDto(
        idx!!, email, name, gender.gender

    )
}
