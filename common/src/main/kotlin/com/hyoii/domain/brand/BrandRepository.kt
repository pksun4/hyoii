package com.hyoii.domain.brand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandRepository : JpaRepository<Brand, Long> {
    fun findBrandById(id: Long) : Brand?
}
