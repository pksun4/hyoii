package com.hyoii.mall.config

import com.hyoii.common.security.JwtAuthenticationFilter
import com.hyoii.common.security.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // JWT 사용위해 세션 사용하지 않음
            .authorizeHttpRequests {
                it.requestMatchers("/",
                    "/swagger-ui/**",
                    "/v3/**").permitAll()
                    .requestMatchers("/api/v1/auth/signup",
                        "/api/v1/auth/login",
                        "/api/v1/test/**",
                        "/api/v1/brands/**"
                    ).anonymous()
                    .requestMatchers("/api/v1/member/**").hasRole("MEMBER")
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}
