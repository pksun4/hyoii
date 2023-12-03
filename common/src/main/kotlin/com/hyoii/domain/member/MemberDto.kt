package com.hyoii.domain.member

import com.fasterxml.jackson.annotation.JsonProperty
import com.hyoii.annotation.ValidEnum
import com.hyoii.enums.GenderEnums
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.format.DateTimeFormatter



/**
 * 회원정보 응답 값
 */
data class MemberResDto(
    val idx: Long,
    val email: String,
    val name: String,
    val gender: String
)
