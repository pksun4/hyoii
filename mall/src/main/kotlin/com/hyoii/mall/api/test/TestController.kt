package com.hyoii.mall.api.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/test"))
class TestController(
    private val testService: TestService
) {

    @GetMapping("/api")
    suspend fun testApi() = "TEST"

}
