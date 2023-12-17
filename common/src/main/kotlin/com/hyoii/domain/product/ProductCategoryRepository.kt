package com.hyoii.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryRepository: JpaRepository<ProductCategory, Long>
