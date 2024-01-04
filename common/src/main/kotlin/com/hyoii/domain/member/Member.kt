package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import com.hyoii.common.security.SecurityUtil.passwordEncode
import com.hyoii.enums.GenderEnums
import jakarta.persistence.*
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
