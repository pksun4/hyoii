package domain.member

import jakarta.persistence.*

@Entity
data class MemberTest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long
)
