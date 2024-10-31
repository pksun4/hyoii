package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import java.io.Serial
import java.util.*
import org.hibernate.annotations.ColumnDefault

@Entity
class MemberPoint(
    @Column(nullable = false)
    @ColumnDefault("0")
    var point: Int,

    @OneToOne
    @JoinColumn(name = "member_key", foreignKey = ForeignKey(name = "fk_member_point_1"))
    var member: Member
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -5513005179252452172L

        private val properties = arrayOf(
            MemberPoint::point
        )
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is MemberPoint || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(properties)
}
