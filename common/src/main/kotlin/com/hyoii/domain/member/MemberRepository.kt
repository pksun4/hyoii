package com.hyoii.domain.member

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MemberRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Member::class.java) {

    fun findByName(name: String) = jpaQueryFactory.selectFrom(QMember.member)
        .where(QMember.member.name.eq(name))
        .fetch()

    fun findByEmail(email: String) = jpaQueryFactory.selectFrom(QMember.member)
        .where(QMember.member.email.eq(email))
        .fetchOne()
}

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String) : Member?
}

