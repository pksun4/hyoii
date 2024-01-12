package com.hyoii.domain.product

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
interface ProductOptionRepository: JpaRepository<ProductOption, Long> {
    override fun findAllById(ids: MutableIterable<Long>): MutableList<ProductOption>
}

@Repository
class ProductOptionRepositorySupport(
    val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(ProductOption::class.java)
