package com.hyoii.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories(basePackages = ["com.hyoii.domain"])
class RedisConfig(
    @Value("\${spring.data.redis.host}") val host: String,
    @Value("\${spring.data.redis.port}") val port: Int
) {
    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(host, port)

}
