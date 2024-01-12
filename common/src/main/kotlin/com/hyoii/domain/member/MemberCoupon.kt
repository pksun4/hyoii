package com.hyoii.domain.member

import com.hyoii.common.BaseEntity
import jakarta.persistence.Entity
import java.io.Serial

@Entity
class MemberCoupon(
    var memberKey: Long,
    var couponKey: Long,
    var isUsed: Boolean
) : BaseEntity() {
    companion object {
        @Serial
        private const val serialVersionUID: Long = -6799120670039750967L
    }
}
