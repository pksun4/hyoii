package com.hyoii.domain.member

import com.fasterxml.jackson.annotation.JsonProperty
import com.hyoii.annotation.ValidEnum
import com.hyoii.enums.GenderEnums
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MemberDto (
    @field:NotBlank
    @JsonProperty("email")
    @field:Email
    private val _email: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,

    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @field:ValidEnum(enumClass = GenderEnums::class, message = "M/F")
    @JsonProperty("gender")
    private val _gender: String?
) {
    val email: String
        get() = _email!!
    val password: String
        get() = _password!!
    val name: String
        get() = _name!!
    val gender: GenderEnums
        get() = GenderEnums.valueOf(_gender!!)

    /**
     * 확장함수 필요한 경우
     */
    private fun String.toLocalDate(): LocalDate =
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    /**
     * Entity 변환
     */
    fun toEntity(): Member = Member(
        email, password, name, gender
    )

}

/**
 * 로그인 요청 값
 */
data class MemberLoginDto(
    @field:NotBlank
    @JsonProperty("email")
    private val _email: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?
) {
    val email: String
        get() = _email!!

    val password: String
        get() = _password!!
}

/**
 * 회원정보 응답 값
 */
data class MemberResDto(
    val idx: Long,
    val email: String,
    val name: String,
    val gender: String
)
