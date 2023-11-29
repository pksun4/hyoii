package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.enums.GenderEnums
import jakarta.persistence.*
import java.io.Serial

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

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8846109021518852076L
    }
}
