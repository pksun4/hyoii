package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.Entity
import java.io.Serial
import java.util.Objects

@Entity
class MemberCoupon(
    var memberKey: Long,
    var couponKey: Long,
    var isUsed: Boolean
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            (other == null || other !is Member || id != other.id) -> false
            else -> true
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun toString(): String = Objects.toString(
        arrayOf(
            MemberCoupon::memberKey,
            MemberCoupon::couponKey,
            MemberCoupon::isUsed
        )
    )

    companion object {
        @Serial
        private const val serialVersionUID: Long = -6799120670039750967L
    }
}
