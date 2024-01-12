package com.hyoii.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderOptionRepository : JpaRepository<OrderOption, Long>

