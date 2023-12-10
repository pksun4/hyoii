package com.hyoii.domain.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductCategoryRepository: JpaRepository<ProductCategory, Long>
