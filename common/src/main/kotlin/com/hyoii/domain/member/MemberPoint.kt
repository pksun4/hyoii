package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.hibernate.annotations.ColumnDefault
import java.io.Serial

@Entity
data class MemberPoint(
    @Column(nullable = false)
    @ColumnDefault("0")
    var point: Int,

    @OneToOne
    @JoinColumn(name = "member_id", foreignKey = ForeignKey(name = "fk_member_point_1"))
    var member: Member
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -5513005179252452172L
    }
}
