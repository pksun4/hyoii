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
        const val TOKEN_EXPIRATION_MS: Long = 1000 * 60 * 30        // 30분
        const val REFRESH_EXPIRATION_MS: Long = 1000 * 60 * 60 * 24 // 하루
    }

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    /**
     * 토큰생성
     */
    fun createToken(authentication: Authentication): String {
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)
        // 토큰 만료 설정
        val now = Date()
        val accessExpiration = Date(now.time + TOKEN_EXPIRATION_MS)
        return Jwts
            .builder()
            .setSubject(authentication.name)
            .claim("auth", authorities)
            .claim("memberId", (authentication.principal as CustomUser).memberId)
            .setIssuedAt(now) // 발행시간
            .setExpiration(accessExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * 리프레시 토큰 생성
     */
    fun createRefreshToken(authentication: Authentication): String {
        return Jwts
            .builder()
            .setExpiration(Date(Date().time + REFRESH_EXPIRATION_MS))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    /**
     * 토큰 추출
     */
    fun getAuthentication(token: String) : Authentication {
        val claims: Claims = getClaims(token)

        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val memberId = claims["memberId"] ?: throw RuntimeException("잘못된 토큰입니다.")

        // 권한정보 추출
        val authorities: Collection<GrantedAuthority> = (auth as String) // 여긴 현재 아직 타입 몰라서 캐스팅 필요
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        val principal: UserDetails = CustomUser(memberId.toString().toLong(), claims.subject, "", authorities)
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

