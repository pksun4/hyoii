package com.hyoii.domain.member

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)
class RefreshToken(
    @Id
    @Indexed
    val refreshToken: String,
    val memberKey: Long
)
