package com.hyoii.domain.member

import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {
    fun findRefreshTokenByRefreshToken(refreshToken: String) : RefreshToken?
}
