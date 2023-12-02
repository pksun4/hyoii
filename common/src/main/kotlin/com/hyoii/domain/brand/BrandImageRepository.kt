package com.hyoii.domain.brand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandImageRepository : JpaRepository<BrandImage, Long>
