package com.hyoii.mall.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    companion object {
        const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30 // 30분
    }

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    /**
     * 토큰생성
     */
    fun createToken(authentication: Authentication): TokenInfo {
        // 권한 String 으로 뽑기
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)
        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

        val accessToken = Jwts
            .builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .claim("userIdx", (authentication.principal as CustomUser).userIdx)
            .setIssuedAt(now) // 발행시간
            .setExpiration(accessExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return TokenInfo("Bearer", accessToken)
    }

    /**
     * 토큰 추출
     */
    fun getAuthentication(token: String) : Authentication {
        val claims: Claims = getClaims(token)

        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val userIdx = claims["userId"] ?: throw RuntimeException("잘못된 토큰입니다.")

        // 권한정보 추출
        val authorities: Collection<GrantedAuthority> = (auth as String) // 여긴 현재 아직 타입 몰라서 캐스팅 필요
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        val principal: UserDetails = CustomUser(userIdx.toString().toLong(), claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    /**
     * 토큰 검증
     */
    fun validateToken(token: String): Boolean {
        runCatching {
            getClaims(token)
            return true
        }.getOrElse {
            when(it) {
                is SecurityException -> {}
                is MalformedJwtException -> {}
                is ExpiredJwtException -> {}
                is UnsupportedJwtException -> {}
                is IllegalArgumentException -> {}
                else -> {}
            }
            println(it.message)
        }
        return false
    }

    private fun getClaims(token: String) = Jwts
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .body
}

