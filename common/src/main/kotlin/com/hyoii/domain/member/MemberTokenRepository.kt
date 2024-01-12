package com.hyoii.domain.member

import com.hyoii.domain.member.QMemberToken.memberToken
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MemberTokenRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(MemberToken::class.java) {
    fun findRecentTokenByMemberKey(memberKey: Long): MemberToken? = jpaQueryFactory
        .selectFrom(memberToken)
        .where(memberToken.member.id.eq(memberKey))
        .orderBy(memberToken.id.desc())
        .fetchFirst()
}

@Repository
interface MemberTokenRepository : JpaRepository<MemberToken, Long>
