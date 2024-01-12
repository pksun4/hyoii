package com.hyoii.mall.api.test

import arrow.core.right
import com.hyoii.domain.member.dto.SignUpRequest
import com.hyoii.mall.common.client.HostUri
import com.hyoii.mall.common.client.WebClientHeader
import com.hyoii.mall.common.client.WebClientService
import org.springframework.stereotype.Service

@Service
class TestService(
    private val webClientService: WebClientService
) {

    suspend fun testApi(signUpRequest: SignUpRequest) = webClientService.executePost(
        header = WebClientHeader(accessToken = null),
        hostUri = HostUri("localhost", "9090", "/api/v1/auth/signup"),
        requestBodyValue = signUpRequest,
        responseKClass = String::class
    ).fold(
        {fail -> fail.messageEnums},
        {response -> response.right()}
    )

}
