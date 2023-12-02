package com.hyoii.mall.api.test

import com.hyoii.domain.member.MemberDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/test"))
class TestController(
    private val testService: TestService
) {

    @GetMapping("/api")
    suspend fun testApi() = testService.testApi(
        MemberDto(
            idx = null,
            _password = "1111",
            _gender = "F",
            _email = "test@daum.net",
            _name = "test1"
        )
    )

}
