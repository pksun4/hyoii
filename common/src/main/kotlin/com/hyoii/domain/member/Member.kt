package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.common.security.SecurityUtil.passwordEncode
import com.hyoii.enums.GenderEnums
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.io.Serial

@Entity
@Table(name = "member")
data class Member(
    @Column(nullable = false, length = 200)
    var email: String,

    @Column(nullable = false, length = 200)
    var password: String,

    @Column(nullable = false, length = 200)
    var name: String,

    @Enumerated(EnumType.STRING)
    var gender: GenderEnums
) : BaseEntity() {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", targetEntity = MemberRole::class, cascade = [CascadeType.PERSIST])
    var memberRole: List<MemberRole>? = mutableListOf()

    @OneToOne(mappedBy = "member")
    var memberPoint: MemberPoint? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = -8846109021518852076L

        fun from(singUpRequest: SignUpRequest) = Member(
            email = singUpRequest.email,
            password = singUpRequest.password.passwordEncode(),
            name = singUpRequest.name,
            gender = GenderEnums.valueOf(singUpRequest.gender)
        )
    }
}
